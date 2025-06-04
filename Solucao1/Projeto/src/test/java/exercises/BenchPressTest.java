package Projeto.test.exercises;

import Projeto.*;
import Projeto.Atividade;
import Projeto.BenchPress;
import Projeto.Utilizador;
import Projeto.UtilizadorAmador;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class BenchPressTest {

    @Test
    void consumoCalorias() {
        // Arrange
        LocalDateTime data = LocalDateTime.of(2023, 5, 5, 12, 0);
        LocalTime tempo = LocalTime.of(0, 30); // 30 minutes
        int freqCardiaca = 120;
        int repeticoes = 40;
        double peso = 50.0;

        // Create BenchPress instance
        BenchPress atividade = new BenchPress(data, tempo, freqCardiaca, repeticoes, peso);

        // Create a mock Utilizador instance
        Utilizador utilizador = new UtilizadorAmador();
        utilizador.addAtividade(atividade);

        // Compute expected result manually
        double fatorReps = atividade.getFatorRepeticoes(0.25, 0.2); // e.g., 0.25 + 0.2 * 40 = 8.25
        double fatorPeso = atividade.getFatorPeso(utilizador, 0.5, 0.2); // assume returns 1.0 (mock or override logic)
        double fatorFreq = atividade.getFatorFreqCardiaca(utilizador); // assume returns 1.0 for simplicity
        double segundos = tempo.toSecondOfDay(); // 1800 seconds

        double expected = 4 * (utilizador.getFatorMultiplicativo() + fatorReps + fatorPeso + fatorFreq)
                * utilizador.getBMR() / (24 * 60 * 60)
                * segundos;

        // Act
        double actual = atividade.consumoCalorias(utilizador);

        // Assert
        assertEquals(expected, actual, 0.01);
    }

    @Test
    void geraAtividade() {
        // Given
        Utilizador utilizador = new UtilizadorAmador(); // Ensure it returns predictable values
        double bmr = utilizador.getBMR();               // Assume 1500 kcal/day
        double fatorMult = utilizador.getFatorMultiplicativo(); // Assume 1.0
        double pesoUtilizador = utilizador.getPeso();   // e.g., 70.0 kg
        double consumoCalorias = 10.0;

        // Assume no past BenchPress activities, so maxPeso will be 0 and fallback will be used
        // Expected values
        double peso = pesoUtilizador; // fallback when no past activities
        double maxPeso = peso * 0.8;
        double fatorPeso = ((maxPeso / pesoUtilizador) - 0.5) * 0.2;
        fatorPeso = Math.max(fatorPeso, 0);

        double tempoDouble = consumoCalorias / (4 * (bmr / (24 * 60 * 60)) * (fatorMult + fatorPeso));
        int expectedTempoSec = (int) tempoDouble;
        int expectedReps = (int) (expectedTempoSec * 0.25);

        BenchPress benchPress = new BenchPress();

        // When
        Atividade atividade = benchPress.geraAtividade(utilizador, consumoCalorias);

        // Then
        assertEquals(LocalTime.MIN.plusSeconds(expectedTempoSec), atividade.getTempo(), "Expected tempo incorrect");
        assertEquals(0, atividade.getFreqCardiaca(), "Expected freqCardiaca to be 0");
        assertTrue(atividade instanceof BenchPress, "Expected atividade to be instance of BenchPress");
        assertEquals(expectedReps, ((BenchPress) atividade).getRepeticoes(), "Expected number of repetitions");
        assertEquals(peso, ((BenchPress) atividade).getPeso(), 0.01, "Expected bench press weight");
    }



    @Test
    void testToString() {
        // Given
        BenchPress atividade = new BenchPress(
                LocalDateTime.of(2023, 5, 5, 12, 0),
                LocalTime.of(0, 30),
                120,
                40,
                50.0
        );

        // When
        String resultado = atividade.toString();

        // Then
        assertTrue(resultado.contains("Tipo de atividade: Bench press"),
                "The toString output should contain 'Tipo de atividade: Abdominais'");
    }


    @Test
    void testEquals() {
        // Common test values
        LocalDateTime data = LocalDateTime.of(2023, 5, 5, 12, 0);
        LocalTime tempo = LocalTime.of(0, 30);
        int freqCardiaca = 120;
        int repeticoes = 50;
        double peso = 80.0;

        // Same instance
        BenchPress atividade1 = new BenchPress(data, tempo, freqCardiaca, repeticoes, peso);
        assertEquals(atividade1, atividade1, "An object should be equal to itself");

        // Equivalent objects
        BenchPress atividade2 = new BenchPress(data, tempo, freqCardiaca, repeticoes, peso);
        assertEquals(atividade1, atividade2, "Objects with same attributes should be equal");

        // Different repetitions
        BenchPress atividade3 = new BenchPress(data, tempo, freqCardiaca, repeticoes + 10, peso);
        assertNotEquals(atividade1, atividade3, "Objects with different repetitions should not be equal");

        // Different weight
        BenchPress atividade4 = new BenchPress(data, tempo, freqCardiaca, repeticoes, peso + 5.0);
        assertNotEquals(atividade1, atividade4, "Objects with different weight should not be equal");

        // Null comparison
        assertNotEquals(atividade1, null, "Object should not be equal to null");

        // Different type
        assertNotEquals(atividade1, "not a BenchPress object", "Object should not be equal to a different type");
    }



    @Test
    void testClone() {
        // Arrange: Create an instance of BenchPress
        LocalDateTime data = LocalDateTime.of(2023, 5, 5, 12, 0);
        LocalTime tempo = LocalTime.of(0, 30);
        int freqCardiaca = 120;
        int repeticoes = 50;
        double peso = 80.0;

        BenchPress original = new BenchPress(data, tempo, freqCardiaca, repeticoes, peso);

        // Act: Clone the original
        BenchPress clone = (BenchPress) original.clone();

        // Assert:
        assertNotSame(original, clone, "Clone should be a different object instance");
        assertEquals(original, clone, "Clone should be equal to the original object");
    }

}