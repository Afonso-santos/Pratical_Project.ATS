package Projeto.test.exercises;

import Projeto.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class FlexoesTest {

    @Test
    void consumoCalorias() {
        LocalDateTime data = LocalDateTime.of(2023, 5, 5, 10, 0);
        LocalTime tempo = LocalTime.of(0, 20); // 1200 seconds
        int freq = 130;
        int repeticoes = 50;

        Flexoes flexoes = new Flexoes(data, tempo, freq, repeticoes);
        Utilizador utilizador = new UtilizadorAmador(); // Expects default BMR = 1500 and fatorMultiplicativo = 1.0

        double fatorRep = flexoes.getFatorRepeticoes(0.5, 0.2);
        double fatorFreq = flexoes.getFatorFreqCardiaca(utilizador);

        double expected = 3.5 * (utilizador.getFatorMultiplicativo() + fatorRep + fatorFreq)
                * utilizador.getBMR() / (24 * 60 * 60)
                * tempo.toSecondOfDay();

        double actual = flexoes.consumoCalorias(utilizador);

        assertEquals(expected, actual, 0.01);
    }

    @Test
    void geraAtividade() {
        Utilizador utilizador = new UtilizadorAmador(); // BMR = 1500, mult = 1.0
        double consumo = 200;

        double tempoDouble = consumo / (3.5 * (utilizador.getBMR() / (24 * 60 * 60)) * utilizador.getFatorMultiplicativo());
        int tempoSec = (int) tempoDouble;
        int expectedReps = tempoSec; // 1 rep per second
        LocalTime expectedTime = LocalTime.MIN.plusSeconds(tempoSec);

        Atividade atividade = new Flexoes().geraAtividade(utilizador, consumo);

        assertTrue(atividade instanceof Flexoes);
        assertEquals(expectedTime, atividade.getTempo());
        assertEquals(0, atividade.getFreqCardiaca());
        assertEquals(expectedReps, ((Flexoes) atividade).getRepeticoes());
    }

    @Test
    void testToString() {
        Flexoes flexoes = new Flexoes(LocalDateTime.of(2023, 5, 5, 11, 0), LocalTime.of(0, 15), 125, 40);
        String result = flexoes.toString();
        assertTrue(result.contains("Tipo de atividade: Flexões"));
    }

    @Test
    void testEquals() {
        LocalDateTime dt = LocalDateTime.of(2023, 5, 5, 11, 0);
        LocalTime tm = LocalTime.of(0, 15);

        Flexoes f1 = new Flexoes(dt, tm, 125, 40);
        Flexoes f2 = new Flexoes(dt, tm, 125, 40);
        Flexoes f3 = new Flexoes(dt, tm, 130, 45); // different values

        assertEquals(f1, f2);
        assertNotEquals(f1, f3);
        assertNotEquals(f1, null);
        assertNotEquals(f1, "Different class");
    }

    @Test
    void testClone() {
        LocalDateTime dt = LocalDateTime.of(2023, 5, 5, 11, 0);
        LocalTime tm = LocalTime.of(0, 15);

        Flexoes original = new Flexoes(dt, tm, 125, 40);
        Flexoes clone = (Flexoes) original.clone();

        assertNotSame(original, clone);
        assertEquals(original, clone);
    }
}
