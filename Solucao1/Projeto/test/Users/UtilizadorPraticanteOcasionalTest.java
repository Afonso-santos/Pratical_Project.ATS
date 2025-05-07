package Projeto.test.Users;

import Projeto.src.main.java.UtilizadorPraticanteOcasional;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UtilizadorPraticanteOcasionalTest {

    @Test
    void getFatorMultiplicativo() {
        UtilizadorPraticanteOcasional u = new UtilizadorPraticanteOcasional("Alice", "Rua A", "alice@example.com", 65, 60, 165,
                LocalDate.of(1995, 3, 10), 'F');
        assertEquals(1.25, u.getFatorMultiplicativo(), 0.0001);
    }

    @Test
    void testToString() {
        UtilizadorPraticanteOcasional u = new UtilizadorPraticanteOcasional("Alice", "Rua A", "alice@example.com", 65, 60, 165,
                LocalDate.of(1995, 3, 10), 'F');
        String result = u.toString();
        assertTrue(result.contains("Tipo de Utilizador: Praticante Ocasional"));
        assertTrue(result.contains("Alice"));
    }

    @Test
    void testEquals() {
        UtilizadorPraticanteOcasional u1 = new UtilizadorPraticanteOcasional("Alice", "Rua A", "alice@example.com", 65, 60, 165,
                LocalDate.of(1995, 3, 10), 'F');
        UtilizadorPraticanteOcasional u2 = new UtilizadorPraticanteOcasional("Alice", "Rua A", "alice@example.com", 65, 60, 165,
                LocalDate.of(1995, 3, 10), 'F');
        UtilizadorPraticanteOcasional u3 = new UtilizadorPraticanteOcasional("Bob", "Rua B", "bob@example.com", 70, 75, 180,
                LocalDate.of(1990, 5, 20), 'M');

        assertEquals(u1, u2);
        assertNotEquals(u1, u3);
    }

    @Test
    void testClone() {
        UtilizadorPraticanteOcasional original = new UtilizadorPraticanteOcasional("Alice", "Rua A", "alice@example.com", 65, 60, 165,
                LocalDate.of(1995, 3, 10), 'F');
        UtilizadorPraticanteOcasional clone = (UtilizadorPraticanteOcasional) original.clone();

        assertEquals(original, clone);
        assertNotSame(original, clone);
    }

    @Test
    void utilizadorNumPeriodo() {
        UtilizadorPraticanteOcasional original = new UtilizadorPraticanteOcasional("Alice", "Rua A", "alice@example.com", 65, 60, 165,
                LocalDate.of(1995, 3, 10), 'F');

        LocalDate start = LocalDate.of(2024, 1, 1);
        LocalDate end = LocalDate.of(2024, 12, 31);

        UtilizadorPraticanteOcasional inPeriod = (UtilizadorPraticanteOcasional) original.utilizadorNumPeriodo(start, end);

        assertNotNull(inPeriod);
        assertEquals(original, inPeriod);
        assertNotSame(original, inPeriod);
    }
}
