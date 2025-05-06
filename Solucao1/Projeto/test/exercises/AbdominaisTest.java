package Projeto.test.exercises;

import Projeto.Abdominais;
import Projeto.Atividade;
import Projeto.Utilizador;
import Projeto.UtilizadorAmador;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class AbdominaisTest {

    @Test
    void consumoCalorias() {
        LocalDateTime data = LocalDateTime.of(2023, 5, 5, 12, 0);
        LocalTime tempo = LocalTime.of(0, 30); // 30 minutes
        int freqCardiaca = 120;
        int repeticoes = 50;

        // Create Abdominais instance
        Abdominais atividade = new Abdominais(data, tempo, freqCardiaca, repeticoes);

        // Create a mock Utilizador instance
        Utilizador utilizador = new UtilizadorAmador();
        utilizador.addAtividade(atividade);

        // Calculate expected calories burned
        double fatorReps = 1 + 0.2 * repeticoes; // = 11.0
        double fatorFreq = atividade.getFatorFreqCardiaca(utilizador); // assume this returns 1.0 for simplicity
        double segundos = tempo.toSecondOfDay(); // = 1800
        double expected = 3 * (1.0 + fatorReps + fatorFreq) * utilizador.getBMR() / (24 * 60 * 60) * segundos;

        // When
        double actual = atividade.consumoCalorias(utilizador);

        assertEquals(expected, actual, 0.01);

    }

    @Test
    void geraAtividade() {
        // Given
        Utilizador utilizador = new UtilizadorAmador(); //  Ensure it has a known BMR, e.g., 1500
        double bmr = utilizador.getBMR();               // Assume 1500 by default for Amador
        double fatorMult = utilizador.getFatorMultiplicativo(); // Assume 1.0 for Amador
        double consumoCalorias = 10.0;

        Abdominais abdominais = new Abdominais();

        // When
        Atividade atividade = abdominais.geraAtividade(utilizador, consumoCalorias);

        // Then
        // Calculate expected tempo
        double tempoDouble = consumoCalorias / (3 * (bmr / (24 * 60 * 60)) * fatorMult);
        int expectedTempoSec = (int) tempoDouble;
        int expectedReps = expectedTempoSec * 1;

        assertEquals(LocalTime.MIN.plusSeconds(expectedTempoSec), atividade.getTempo(), "Expected tempo incorrect");
        assertEquals(0, atividade.getFreqCardiaca(), "Expected freqCardiaca to be 0");
        assertTrue(atividade instanceof Abdominais, "Expected atividade to be instance of Abdominais");
        assertEquals(expectedReps, ((Abdominais) atividade).getRepeticoes(), "Expected number of repetitions");
    }

    @Test
    void testToString() {
        // Given
        Abdominais atividade = new Abdominais(
                LocalDateTime.of(2023, 5, 5, 12, 0),
                LocalTime.of(0, 30),
                120,
                50
        );

        // When
        String resultado = atividade.toString();

        // Then
        assertTrue(resultado.contains("Tipo de atividade: Abdominais"),
                "The toString output should contain 'Tipo de atividade: Abdominais'");
    }

    @Test
    void testEquals() {
        // Common test values
        LocalDateTime data = LocalDateTime.of(2023, 5, 5, 12, 0);
        LocalTime tempo = LocalTime.of(0, 30);
        int freqCardiaca = 120;
        int repeticoes = 50;

        // Same instance
        Abdominais atividade1 = new Abdominais(data, tempo, freqCardiaca, repeticoes);
        assertEquals(atividade1, atividade1, "An object should be equal to itself");

        // Equivalent objects
        Abdominais atividade2 = new Abdominais(data, tempo, freqCardiaca, repeticoes);
        assertEquals(atividade1, atividade2, "Objects with same attributes should be equal");

        // Different repetitions
        Abdominais atividade3 = new Abdominais(data, tempo, freqCardiaca, repeticoes + 10);
        assertNotEquals(atividade1, atividade3, "Objects with different repetitions should not be equal");

        // Null comparison
        assertNotEquals(atividade1, null, "Object should not be equal to null");

        // Different type
        assertNotEquals(atividade1, "not an Abdominais object", "Object should not be equal to a different type");
    }


    @Test
    void testClone() {
        // Arrange: Create an instance of Abdominais
        LocalDateTime data = LocalDateTime.of(2023, 5, 5, 12, 0);
        LocalTime tempo = LocalTime.of(0, 30);
        int freqCardiaca = 120;
        int repeticoes = 50;

        Abdominais original = new Abdominais(data, tempo, freqCardiaca, repeticoes);

        // Act: Clone the original
        Abdominais clone = (Abdominais) original.clone();

        // Assert:
        assertNotSame(original, clone, "Clone should be a different object instance");
        assertEquals(original, clone, "Clone should be equal to the original object");
    }
}