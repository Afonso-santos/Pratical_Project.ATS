package Projeto.test.exercises;

import Projeto.*;
import Projeto.Atividade;
import Projeto.Btt;
import Projeto.Utilizador;
import Projeto.UtilizadorAmador;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class BttTest {

    @Test
    void getFatorHard() {
        // Altimetria < 1000
        Btt btt1 = new Btt(LocalDateTime.now(), LocalTime.of(1, 0), 120, 20.0, 800);
        assertEquals(1.05, btt1.getFatorHard(), 0.001);

        // Altimetria > 1000
        Btt btt2 = new Btt(LocalDateTime.now(), LocalTime.of(1, 0), 120, 20.0, 1500);
        assertEquals(1.15, btt2.getFatorHard(), 0.001);

        // Altimetria > 2000
        Btt btt3 = new Btt(LocalDateTime.now(), LocalTime.of(1, 0), 120, 20.0, 2500);
        assertEquals(1.25, btt3.getFatorHard(), 0.001);
    }

    @Test
    void consumoCalorias() {
        LocalDateTime data = LocalDateTime.of(2023, 5, 5, 10, 0);
        LocalTime tempo = LocalTime.of(0, 45); // 2700 seconds
        int freq = 130;
        double distancia = 15.0;
        double altimetria = 1200;

        Btt atividade = new Btt(data, tempo, freq, distancia, altimetria);
        Utilizador utilizador = new UtilizadorAmador(); // Assumes BMR = 1500, fatorMult = 1.0

        double fatorVel = atividade.getFatorVelocidade(10.5, 0.11);
        double fatorFreq = atividade.getFatorFreqCardiaca(utilizador);
        double fatorAltimetria = atividade.getFatorAltimetria();
        double fatorHard = atividade.getFatorHard();
        double segundos = tempo.toSecondOfDay();

        double expected = 10 * (utilizador.getFatorMultiplicativo() + fatorVel + fatorFreq + fatorAltimetria)
                * utilizador.getBMR() / (24 * 60 * 60)
                * fatorHard
                * segundos;

        double actual = atividade.consumoCalorias(utilizador);

        assertEquals(expected, actual, 0.01);
    }

    @Test
    void geraAtividade() {
        Utilizador utilizador = new UtilizadorAmador(); // mock user
        double consumo = 300;

        double pesoUtilizador = utilizador.getPeso();
        double bmr = utilizador.getBMR();
        double fatorMult = utilizador.getFatorMultiplicativo();

        double altimetria = 0.0; // default fallback
        double fatorAltimetria = altimetria * 0.0005;

        double tempoDouble = consumo / (10 * (bmr / (24 * 60 * 60)) * (fatorMult + fatorAltimetria));
        int tempoSec = (int) tempoDouble;
        double expectedDistancia = tempoSec * 10.5;

        Atividade atividade = new Btt().geraAtividade(utilizador, consumo);

        assertTrue(atividade instanceof Btt);
        assertEquals(LocalTime.MIN.plusSeconds(tempoSec), atividade.getTempo(), "Expected time mismatch");
        assertEquals(0, atividade.getFreqCardiaca(), "Expected frequency 0");
        assertEquals(expectedDistancia, ((Btt) atividade).getDistancia(), 0.01, "Expected distance mismatch");
        assertEquals(altimetria, ((Btt) atividade).getAltimetria(), 0.01, "Expected altimetria mismatch");
    }

    @Test
    void testToString() {
        Btt btt = new Btt(LocalDateTime.of(2023, 5, 5, 10, 0), LocalTime.of(0, 45), 120, 20.0, 1500);
        String result = btt.toString();
        assertTrue(result.contains("Tipo de atividade: BTT"));
    }

    @Test
    void testEquals() {
        LocalDateTime dt = LocalDateTime.of(2023, 5, 5, 10, 0);
        LocalTime tm = LocalTime.of(0, 45);

        Btt b1 = new Btt(dt, tm, 130, 20.0, 1500);
        Btt b2 = new Btt(dt, tm, 130, 20.0, 1500);
        Btt b3 = new Btt(dt, tm, 130, 25.0, 1500); // different distance
        Btt b4 = new Btt(dt, tm, 130, 20.0, 2000); // different altimetry

        assertEquals(b1, b2);
        assertNotEquals(b1, b3);
        assertNotEquals(b1, b4);
        assertNotEquals(b1, null);
        assertNotEquals(b1, "Not a Btt");
    }

    @Test
    void testClone() {
        LocalDateTime dt = LocalDateTime.of(2023, 5, 5, 10, 0);
        LocalTime tm = LocalTime.of(0, 45);

        Btt original = new Btt(dt, tm, 130, 20.0, 1500);
        Btt clone = (Btt) original.clone();

        assertNotSame(original, clone);
        assertEquals(original, clone);
    }
}
