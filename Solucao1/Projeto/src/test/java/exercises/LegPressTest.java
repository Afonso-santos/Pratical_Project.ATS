package Projeto.test.exercises;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import Projeto.Atividade;
import Projeto.LegPress;
import Projeto.Utilizador;
import Projeto.UtilizadorAmador;

class LegPressTest {

    @Test
    void consumoCalorias() {
        LocalDateTime data = LocalDateTime.of(2023, 5, 5, 10, 0);
        LocalTime tempo = LocalTime.of(0, 20); // 1200 seconds
        int freq = 130;
        int repeticoes = 50;
        double peso = 60;

        LegPress legPress = new LegPress(data, tempo, freq, repeticoes, peso);
        Utilizador utilizador = new UtilizadorAmador(); // Expects default BMR = 1500 and fatorMultiplicativo = 1.0

        double fatorRep = legPress.getFatorRepeticoes(0.5, 0.2);
        double fatorPeso = legPress.getFatorPeso(utilizador, 1, 0.2);
        double fatorFreq = legPress.getFatorFreqCardiaca(utilizador);

        double expected = 4.0 * (utilizador.getFatorMultiplicativo() + fatorRep + fatorPeso + fatorFreq)
                * utilizador.getBMR() / (24 * 60 * 60)
                * tempo.toSecondOfDay();

        double actual = legPress.consumoCalorias(utilizador);

        assertEquals(expected, actual, 0.01);
    }

    @Test
    void geraAtividade() {
        Utilizador utilizador = new UtilizadorAmador(); // BMR = 1500, mult = 1.0
        double consumo = 200;

        // Expected values calculation based on assumptions
        double tempoDouble = consumo / (4.0 * (utilizador.getBMR() / (24 * 60 * 60)) * utilizador.getFatorMultiplicativo());
        int tempoSec = (int) tempoDouble;
        int expectedReps = (int) (tempoSec * 0.25);
        LocalTime expectedTime = LocalTime.MIN.plusSeconds(tempoSec);
        LocalTime zero = LocalTime.of(0, 0);

        Atividade atividade = new LegPress().geraAtividade(utilizador, consumo);

        assertTrue(atividade instanceof LegPress);
        assertEquals(zero, atividade.getTempo());
        assertEquals(0, atividade.getFreqCardiaca());
        assertEquals(0, ((LegPress) atividade).getRepeticoes());
        assertTrue(((LegPress) atividade).getPeso() == 0); // peso estimado
    }

    @Test
    void testToString() {
        LegPress legPress = new LegPress(LocalDateTime.of(2023, 5, 5, 11, 0), LocalTime.of(0, 15), 125, 40, 70);
        String result = legPress.toString();
        assertTrue(result.contains("Tipo de atividade: Leg press"));
    }

    @Test
    void testEquals() {
        LocalDateTime dt = LocalDateTime.of(2023, 5, 5, 11, 0);
        LocalTime tm = LocalTime.of(0, 15);

        LegPress l1 = new LegPress(dt, tm, 125, 40, 70);
        LegPress l2 = new LegPress(dt, tm, 125, 40, 70);
        LegPress l3 = new LegPress(dt, tm, 130, 45, 80); // different values

        assertEquals(l1, l2);
        assertNotEquals(l1, l3);
        assertNotEquals(l1, null);
        assertNotEquals(l1, "Different class");
    }

    @Test
    void testClone() {
        LocalDateTime dt = LocalDateTime.of(2023, 5, 5, 11, 0);
        LocalTime tm = LocalTime.of(0, 15);

        LegPress original = new LegPress(dt, tm, 125, 40, 70);
        LegPress clone = (LegPress) original.clone();

        assertNotSame(original, clone);
        assertEquals(original, clone);
    }
}
