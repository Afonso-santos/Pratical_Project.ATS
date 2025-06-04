package Projeto.test.exercises;

import Projeto.*;
import Projeto.Atividade;
import Projeto.Trail;
import Projeto.Utilizador;
import Projeto.UtilizadorAmador;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class TrailTest {

    @Test
    void getFatorHard() {
        Trail t1 = new Trail(LocalDateTime.now(), LocalTime.of(0, 30), 120, 5.0, 800);
        Trail t2 = new Trail(LocalDateTime.now(), LocalTime.of(0, 30), 120, 5.0, 1500);
        Trail t3 = new Trail(LocalDateTime.now(), LocalTime.of(0, 30), 120, 5.0, 2500);

        assertEquals(1.15, t1.getFatorHard(), 0.001);
        assertEquals(1.25, t2.getFatorHard(), 0.001);
        assertEquals(1.35, t3.getFatorHard(), 0.001);
    }

    @Test
    void consumoCalorias() {
        LocalDateTime dt = LocalDateTime.of(2023, 5, 5, 10, 0);
        LocalTime tempo = LocalTime.of(0, 20); // 1200 seconds
        double distancia = 4.0;
        double altimetria = 800;
        int freq = 130;

        Trail trail = new Trail(dt, tempo, freq, distancia, altimetria);
        Utilizador utilizador = new UtilizadorAmador(); // BMR = 1500, mult = 1.0

        double fatorVel = trail.getFatorVelocidade(2.2, 0.22);
        double fatorAlt = trail.getFatorAltimetria();
        double fatorFreq = trail.getFatorFreqCardiaca(utilizador);
        double fatorHard = trail.getFatorHard();

        double expected = 10 * (utilizador.getFatorMultiplicativo() + fatorVel + fatorFreq + fatorAlt)
                * utilizador.getBMR() / (24 * 60 * 60)
                * fatorHard
                * tempo.toSecondOfDay();

        double actual = trail.consumoCalorias(utilizador);

        assertEquals(expected, actual, 0.01);
    }

    @Test
    void geraAtividade() {
        Utilizador utilizador = new UtilizadorAmador();
        double consumo = 400;

        double tempoDouble = consumo / (10 * (utilizador.getBMR() / (24 * 60 * 60)) * (utilizador.getFatorMultiplicativo()));
        int tempoSec = (int) tempoDouble;
        double expectedDist = tempoSec * 2.2;
        LocalTime expectedTime = LocalTime.MIN.plusSeconds(tempoSec);

        Atividade atividade = new Trail().geraAtividade(utilizador, consumo);

        assertTrue(atividade instanceof Trail);
        assertEquals(expectedTime, atividade.getTempo());
        assertEquals(0, atividade.getFreqCardiaca());
        assertEquals(expectedDist, ((Trail) atividade).getDistancia(), 0.1);
        assertTrue(((Trail) atividade).getAltimetria() >= 0);
    }

    @Test
    void testToString() {
        Trail trail = new Trail(LocalDateTime.of(2023, 5, 5, 10, 0), LocalTime.of(0, 20), 120, 6.0, 1000);
        String result = trail.toString();
        assertTrue(result.contains("Tipo de atividade: Trail"));
    }

    @Test
    void testEquals() {
        LocalDateTime dt = LocalDateTime.of(2023, 5, 5, 10, 0);
        LocalTime tm = LocalTime.of(0, 20);

        Trail t1 = new Trail(dt, tm, 120, 6.0, 1000);
        Trail t2 = new Trail(dt, tm, 120, 6.0, 1000);
        Trail t3 = new Trail(dt, tm, 125, 7.0, 1100); // different

        assertEquals(t1, t2);
        assertNotEquals(t1, t3);
        assertNotEquals(t1, null);
        assertNotEquals(t1, "Other type");
    }

    @Test
    void testClone() {
        Trail original = new Trail(LocalDateTime.of(2023, 5, 5, 10, 0), LocalTime.of(0, 20), 120, 6.0, 1000);
        Trail clone = (Trail) original.clone();

        assertNotSame(original, clone);
        assertEquals(original, clone);
    }
}
