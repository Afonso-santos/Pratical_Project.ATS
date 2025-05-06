package Projeto.test.exercises;

import Projeto.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class BicepCurlsTest {

    @Test
    void consumoCalorias() {
        // Arrange
        LocalDateTime data = LocalDateTime.of(2023, 5, 5, 12, 0);
        LocalTime tempo = LocalTime.of(0, 30); // 30 minutes
        int freqCardiaca = 120;
        int repeticoes = 40;
        double peso = 50.0;

        BicepCurls atividade = new BicepCurls(data, tempo, freqCardiaca, repeticoes, peso);
        Utilizador utilizador = new UtilizadorAmador();
        utilizador.addAtividade(atividade);

        double fatorReps = atividade.getFatorRepeticoes(0.5, 0.4);
        double fatorPeso = atividade.getFatorPeso(utilizador, 0.2, 0.4);
        double fatorFreq = atividade.getFatorFreqCardiaca(utilizador);
        double segundos = tempo.toSecondOfDay();

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
        Utilizador utilizador = new UtilizadorAmador();
        double bmr = utilizador.getBMR();
        double fatorMult = utilizador.getFatorMultiplicativo();
        double pesoUtilizador = utilizador.getPeso();
        double consumoCalorias = 10.0;

        // Expected values
        double peso = pesoUtilizador;
        double maxPeso = peso * 0.8;
        double fatorPeso = ((maxPeso / pesoUtilizador) - 0.2) * 0.4;
        fatorPeso = Math.max(fatorPeso, 0);

        double tempoDouble = consumoCalorias / (4 * (bmr / (24 * 60 * 60)) * (fatorMult + fatorPeso));
        int expectedTempoSec = (int) tempoDouble;
        int expectedReps = (int) (expectedTempoSec * 0.25);

        BicepCurls bicepCurls = new BicepCurls();

        // When
        Atividade atividade = bicepCurls.geraAtividade(utilizador, consumoCalorias);

        // Then
        assertEquals(LocalTime.MIN.plusSeconds(expectedTempoSec), atividade.getTempo(), "Expected tempo incorrect");
        assertEquals(0, atividade.getFreqCardiaca(), "Expected freqCardiaca to be 0");
        assertTrue(atividade instanceof BicepCurls, "Expected atividade to be instance of BicepCurls");
        assertEquals(expectedReps, ((BicepCurls) atividade).getRepeticoes(), "Expected number of repetitions");
        assertEquals(peso, ((BicepCurls) atividade).getPeso(), 0.01, "Expected bicep curls weight");
    }

    @Test
    void testToString() {
        // Given
        BicepCurls atividade = new BicepCurls(
                LocalDateTime.of(2023, 5, 5, 12, 0),
                LocalTime.of(0, 30),
                120,
                40,
                50.0
        );

        // When
        String resultado = atividade.toString();

        // Then
        assertTrue(resultado.contains("Tipo de atividade: Bicep curls"),
                "The toString output should contain 'Tipo de atividade: Bicep curls'");
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
        BicepCurls atividade1 = new BicepCurls(data, tempo, freqCardiaca, repeticoes, peso);
        assertEquals(atividade1, atividade1, "An object should be equal to itself");

        // Equivalent objects
        BicepCurls atividade2 = new BicepCurls(data, tempo, freqCardiaca, repeticoes, peso);
        assertEquals(atividade1, atividade2, "Objects with same attributes should be equal");

        // Different repetitions
        BicepCurls atividade3 = new BicepCurls(data, tempo, freqCardiaca, repeticoes + 10, peso);
        assertNotEquals(atividade1, atividade3, "Objects with different repetitions should not be equal");

        // Different weight
        BicepCurls atividade4 = new BicepCurls(data, tempo, freqCardiaca, repeticoes, peso + 5.0);
        assertNotEquals(atividade1, atividade4, "Objects with different weight should not be equal");

        // Null comparison
        assertNotEquals(atividade1, null, "Object should not be equal to null");

        // Different type
        assertNotEquals(atividade1, "not a BicepCurls object", "Object should not be equal to a different type");
    }

    @Test
    void testClone() {
        // Arrange: Create an instance of BicepCurls
        LocalDateTime data = LocalDateTime.of(2023, 5, 5, 12, 0);
        LocalTime tempo = LocalTime.of(0, 30);
        int freqCardiaca = 120;
        int repeticoes = 50;
        double peso = 80.0;

        BicepCurls original = new BicepCurls(data, tempo, freqCardiaca, repeticoes, peso);

        // Act: Clone the original
        BicepCurls clone = (BicepCurls) original.clone();

        // Assert:
        assertNotSame(original, clone, "Clone should be a different object instance");
        assertEquals(original, clone, "Clone should be equal to the original object");
    }
}
