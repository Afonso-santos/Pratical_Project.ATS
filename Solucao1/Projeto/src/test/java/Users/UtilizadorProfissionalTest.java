package Projeto.test.Users;


import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import Projeto.UtilizadorProfissional;

class UtilizadorProfissionalTest {

    @Test
    void getFatorMultiplicativo() {
        UtilizadorProfissional u = new UtilizadorProfissional("Carlos", "Rua C", "carlos@example.com", 60, 70, 175,
                LocalDate.of(1988, 7, 15), 'M');
        assertEquals(1.5, u.getFatorMultiplicativo(), 0.0001);
    }

    @Test
    void testToString() {
        UtilizadorProfissional u = new UtilizadorProfissional("Carlos", "Rua C", "carlos@example.com", 60, 70, 175,
                LocalDate.of(1988, 7, 15), 'M');
        String result = u.toString();
        assertTrue(result.contains("Tipo de Utilizador: Profissional"));
        assertTrue(result.contains("Carlos"));
    }

    @Test
    void testEquals() {
        UtilizadorProfissional u1 = new UtilizadorProfissional("Carlos", "Rua C", "carlos@example.com", 60, 70, 175,
                LocalDate.of(1988, 7, 15), 'M');
        UtilizadorProfissional u2 = new UtilizadorProfissional("Carlos", "Rua C", "carlos@example.com", 60, 70, 175,
                LocalDate.of(1988, 7, 15), 'M');
        UtilizadorProfissional u3 = new UtilizadorProfissional("Diana", "Rua D", "diana@example.com", 72, 68, 168,
                LocalDate.of(1992, 4, 5), 'F');


        assertNotEquals(u1, u3);
    }

    @Test
    void testClone() {
        UtilizadorProfissional original = new UtilizadorProfissional("Carlos", "Rua C", "carlos@example.com", 60, 70, 175,
                LocalDate.of(1988, 7, 15), 'M');
        UtilizadorProfissional clone = (UtilizadorProfissional) original.clone();

        assertEquals(original, clone);
        assertNotSame(original, clone);
    }

    @Test
    void utilizadorNumPeriodo() {
        UtilizadorProfissional original = new UtilizadorProfissional("Carlos", "Rua C", "carlos@example.com", 60, 70, 175,
                LocalDate.of(1988, 7, 15), 'M');

        LocalDate start = LocalDate.of(2024, 1, 1);
        LocalDate end = LocalDate.of(2024, 12, 31);

        UtilizadorProfissional inPeriod = (UtilizadorProfissional) original.utilizadorNumPeriodo(start, end);

        assertNotNull(inPeriod);
        assertEquals(original, inPeriod);
        assertNotSame(original, inPeriod);
    }
}
