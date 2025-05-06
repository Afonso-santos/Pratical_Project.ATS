package Projeto.test.exercises;

import Projeto.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class CorridaTest {

    @Test
    void consumoCalorias() {
        LocalDateTime data = LocalDateTime.of(2023, 5, 5, 8, 0);
        LocalTime tempo = LocalTime.of(0, 30); // 1800 seconds
        int freq = 135;
        double distancia = 5.0;

        Corrida corrida = new Corrida(data, tempo, freq, distancia);
        Utilizador utilizador = new UtilizadorAmador(); // Assuming BMR = 1500, fatorMult = 1.0

        double fatorVel = corrida.getFatorVelocidade(2.2, 0.22);
        double fatorFreq = corrida.getFatorFreqCardiaca(utilizador);

        double expected = 8 * (utilizador.getFatorMultiplicativo() + fatorVel + fatorFreq)
                * utilizador.getBMR() / (24 * 60 * 60)
                * tempo.toSecondOfDay();

        double actual = corrida.consumoCalorias(utilizador);

        assertEquals(expected, actual, 0.01);
    }

    @Test
    void geraAtividade() {
        Utilizador utilizador = new UtilizadorAmador(); // BMR = 1500, mult = 1.0
        double consumo = 300;

        double tempoDouble = consumo / (8 * (utilizador.getBMR() / (24 * 60 * 60)) * utilizador.getFatorMultiplicativo());
        int tempoSec = (int) tempoDouble;
        double expectedDist = tempoSec * 2.2;

        Atividade atividade = new Corrida().geraAtividade(utilizador, consumo);

        assertTrue(atividade instanceof Corrida);
        assertEquals(LocalTime.MIN.plusSeconds(tempoSec), atividade.getTempo());
        assertEquals(0, atividade.getFreqCardiaca());
        assertEquals(expectedDist, ((Corrida) atividade).getDistancia(), 0.01);
    }

    @Test
    void testToString() {
        Corrida corrida = new Corrida(LocalDateTime.of(2023, 5, 5, 9, 0), LocalTime.of(0, 25), 120, 6.0);
        String result = corrida.toString();
        assertTrue(result.contains("Tipo de atividade: Corrida"));
    }

    @Test
    void testEquals() {
        LocalDateTime dt = LocalDateTime.of(2023, 5, 5, 9, 0);
        LocalTime tm = LocalTime.of(0, 30);

        Corrida c1 = new Corrida(dt, tm, 135, 6.0);
        Corrida c2 = new Corrida(dt, tm, 135, 6.0);
        Corrida c3 = new Corrida(dt, tm, 140, 7.0); // different values

        assertEquals(c1, c2);
        assertNotEquals(c1, c3);
        assertNotEquals(c1, null);
        assertNotEquals(c1, "Different class");
    }

    @Test
    void testClone() {
        LocalDateTime dt = LocalDateTime.of(2023, 5, 5, 9, 0);
        LocalTime tm = LocalTime.of(0, 30);

        Corrida original = new Corrida(dt, tm, 135, 6.0);
        Corrida clone = (Corrida) original.clone();

        assertNotSame(original, clone);
        assertEquals(original, clone);
    }
}
