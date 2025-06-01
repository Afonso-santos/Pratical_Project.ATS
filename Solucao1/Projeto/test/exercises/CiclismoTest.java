package Projeto.test.exercises;

import Projeto.*;
import Projeto.Atividade;
import Projeto.Ciclismo;
import Projeto.Utilizador;
import Projeto.UtilizadorAmador;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class CiclismoTest {

    @Test
    void consumoCalorias() {
        LocalDateTime data = LocalDateTime.of(2023, 5, 5, 10, 0);
        LocalTime tempo = LocalTime.of(0, 30); // 1800 seconds
        int freq = 130;
        double distancia = 10.0;

        Ciclismo ciclismo = new Ciclismo(data, tempo, freq, distancia);
        Utilizador utilizador = new UtilizadorAmador(); // assume BMR = 1500, fatorMult = 1.0

        double fatorVel = ciclismo.getFatorVelocidade(10.5, 0.11);
        double fatorFreq = ciclismo.getFatorFreqCardiaca(utilizador);

        double expected = 6 * (utilizador.getFatorMultiplicativo() + fatorVel + fatorFreq)
                * utilizador.getBMR() / (24 * 60 * 60)
                * tempo.toSecondOfDay();

        double actual = ciclismo.consumoCalorias(utilizador);

        assertEquals(expected, actual, 0.01);
    }

    @Test
    void geraAtividade() {
        Utilizador utilizador = new UtilizadorAmador(); // mock user
        double consumo = 300;

        double bmr = utilizador.getBMR();
        double fatorMult = utilizador.getFatorMultiplicativo();
        double tempoDouble = consumo / (6 * (bmr / (24 * 60 * 60)) * fatorMult);
        int tempoSec = (int) tempoDouble;
        double expectedDistancia = tempoSec * 10.5;

        Atividade atividade = new Ciclismo().geraAtividade(utilizador, consumo);

        assertTrue(atividade instanceof Ciclismo);
        assertEquals(LocalTime.MIN.plusSeconds(tempoSec), atividade.getTempo());
        assertEquals(0, atividade.getFreqCardiaca());
        assertEquals(expectedDistancia, ((Ciclismo) atividade).getDistancia(), 0.01);
    }

    @Test
    void testToString() {
        Ciclismo ciclismo = new Ciclismo(LocalDateTime.of(2023, 5, 5, 10, 0), LocalTime.of(0, 45), 120, 20.0);
        String result = ciclismo.toString();
        assertTrue(result.contains("Tipo de atividade: Ciclismo"));
    }

    @Test
    void testEquals() {
        LocalDateTime dt = LocalDateTime.of(2023, 5, 5, 10, 0);
        LocalTime tm = LocalTime.of(0, 45);

        Ciclismo c1 = new Ciclismo(dt, tm, 130, 20.0);
        Ciclismo c2 = new Ciclismo(dt, tm, 130, 20.0);
        Ciclismo c3 = new Ciclismo(dt, tm, 130, 25.0); // different distance

        assertEquals(c1, c2);
        assertNotEquals(c1, c3);
        assertNotEquals(c1, null);
        assertNotEquals(c1, "Not a Ciclismo");
    }

    @Test
    void testClone() {
        LocalDateTime dt = LocalDateTime.of(2023, 5, 5, 10, 0);
        LocalTime tm = LocalTime.of(0, 45);

        Ciclismo original = new Ciclismo(dt, tm, 130, 20.0);
        Ciclismo clone = (Ciclismo) original.clone();

        assertNotSame(original, clone);
        assertEquals(original, clone);
    }
}
