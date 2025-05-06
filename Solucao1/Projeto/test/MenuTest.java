package Projeto.test;

import Projeto.Menu;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class MenuTest {

    @Test
    void getOpcao() {
        String[] opcoes = {"Menu Principal", "Opção 1", "Sair"};
        Menu menu = new Menu(opcoes);
        assertEquals(-1, menu.getOpcao());  // default value
    }

    @Test
    void pedeString() {
        String input = "hello\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Menu menu = new Menu(new String[]{"Menu", "Sair"});
        String result = menu.pedeString("Escreva algo:");
        assertEquals("hello", result);
    }

    @Test
    void pedeInt() {
        String input = "42\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Menu menu = new Menu(new String[]{"Menu", "Sair"});
        int result = menu.pedeInt("Insira um inteiro:");
        assertEquals(42, result);
    }

    @Test
    void pedeDouble() {
        String input = "3.14\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Menu menu = new Menu(new String[]{"Menu", "Sair"});
        double result = menu.pedeDouble("Insira um número:");
        assertEquals(3.14, result, 0.0001);
    }

    @Test
    void pedeData() {
        String input = "10/05/2024\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Menu menu = new Menu(new String[]{"Menu", "Sair"});
        LocalDate result = menu.pedeData("Insira uma data:");
        assertEquals(LocalDate.of(2024, 5, 10), result);
    }

    @Test
    void pedeTempo() {
        String input = "12:30\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Menu menu = new Menu(new String[]{"Menu", "Sair"});
        LocalTime result = menu.pedeTempo("Insira uma hora:");
        assertEquals(LocalTime.of(12, 30), result);
    }

    @Test
    void pedeDataHora() {
        String input = "10/05/2024 15:45:30\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Menu menu = new Menu(new String[]{"Menu", "Sair"});
        LocalDateTime result = menu.pedeDataHora("Insira data e hora:");
        assertEquals(LocalDateTime.of(2024, 5, 10, 15, 45, 30), result);
    }

    // Note: runMenu and mostraMenu rely on user interaction and are best tested via integration testing or mocks
}
