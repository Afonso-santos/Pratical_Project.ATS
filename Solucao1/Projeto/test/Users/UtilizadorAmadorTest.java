package Projeto.test.Users;


import Projeto.UtilizadorAmador;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UtilizadorAmadorTest {

    @Test
    void getFatorMultiplicativo() {
        UtilizadorAmador utilizador = new UtilizadorAmador("John", "123 Street", "john@example.com", 70, 75, 180,
                LocalDate.of(1990, 5, 20), 'M');
        assertEquals(1.0, utilizador.getFatorMultiplicativo(), 0.0001);
    }

    @Test
    void testToString() {
        UtilizadorAmador utilizador = new UtilizadorAmador("John", "123 Street", "john@example.com", 70, 75, 180,
                LocalDate.of(1990, 5, 20), 'M');
        String result = utilizador.toString();
        assertTrue(result.contains("Tipo de Utilizador: Amador"));
        assertTrue(result.contains("John"));
    }

    @Test
    void testEquals() {
        UtilizadorAmador utilizador1 = new UtilizadorAmador("John", "123 Street", "john@example.com", 70, 75, 180,
                LocalDate.of(1990, 5, 20), 'M');
        UtilizadorAmador utilizador2 = new UtilizadorAmador("John", "123 Street", "john@example.com", 70, 75, 180,
                LocalDate.of(1990, 5, 20), 'M');
        UtilizadorAmador utilizador3 = new UtilizadorAmador("Jane", "456 Street", "jane@example.com", 65, 60, 165,
                LocalDate.of(1992, 3, 15), 'F');

        assertEquals(utilizador1, utilizador2);
        assertNotEquals(utilizador1, utilizador3);
    }

    @Test
    void testClone() {
        UtilizadorAmador original = new UtilizadorAmador("John", "123 Street", "john@example.com", 70, 75, 180,
                LocalDate.of(1990, 5, 20), 'M');
        UtilizadorAmador clone = (UtilizadorAmador) original.clone();

        assertEquals(original, clone);
        assertNotSame(original, clone);
    }

    @Test
    void utilizadorNumPeriodo() {
        UtilizadorAmador original = new UtilizadorAmador("John", "123 Street", "john@example.com", 70, 75, 180,
                LocalDate.of(1990, 5, 20), 'M');

        LocalDate start = LocalDate.of(2024, 1, 1);
        LocalDate end = LocalDate.of(2024, 12, 31);

        UtilizadorAmador inPeriod = (UtilizadorAmador) original.utilizadorNumPeriodo(start, end);

        assertNotNull(inPeriod);
        assertEquals(original, inPeriod);
        assertNotSame(original, inPeriod);
    }
}
