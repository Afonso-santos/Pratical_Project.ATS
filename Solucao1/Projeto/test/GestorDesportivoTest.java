package Projeto.test;

import Projeto.*;
import Projeto.GestorDesportivo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class GestorDesportivoTest {

    private GestorDesportivo gestor;

    @BeforeEach
    void setUp() {
        gestor = new GestorDesportivo();
    }

    @Test
    void testAddUtilizadorAndShow() {
        int cod = gestor.addUtilizador("João", "Lisboa", "joao@example.com", 70, 70, 175, LocalDate.of(1990, 1, 1), 'M', 1);
        assertTrue(gestor.existeUtilizador(cod));
        String info = gestor.showUtilizador(cod);
        assertTrue(info.contains("João"));
    }

    @Test
    void testAddAtividadeAndShow() {
        LocalDateTime realizacao = LocalDateTime.of(2024, 5, 1, 10, 0);
        LocalTime tempo = LocalTime.of(0, 30);
        int cod = gestor.addAtivDist(realizacao, tempo, 120, 5.0, 1);
        assertTrue(gestor.existeAtividade(cod));
        String info = gestor.showAtividade(cod);
        assertTrue(info.contains("Corrida"));
    }

    @Test
    void testPlanoTreinoAddAndRegister() {
        LocalDate data = LocalDate.of(2024, 5, 1);
        int planoCod = gestor.addPlanoTreino(data);
        assertTrue(gestor.existePlano(planoCod));
        String info = gestor.showPlanoTreino(planoCod);
        assertTrue(info.contains("Plano de Treino"));
    }

    @Test
    void testGuardaEstadoCarregaEstado() throws IOException, ClassNotFoundException {
        int cod = gestor.addUtilizador("Maria", "Porto", "maria@example.com", 75, 60, 165, LocalDate.of(1995, 3, 15), 'F', 1);
        gestor.guardaEstado("test_state.ser");

        GestorDesportivo carregado = gestor.carregaEstado("test_state.ser");
        assertNotNull(carregado);
        assertTrue(carregado.existeUtilizador(cod));
    }

    @Test
    void testAtividadesUtilizador() {
        int codU = gestor.addUtilizador("Rui", "Coimbra", "rui@example.com", 72, 80, 180, LocalDate.of(1988, 6, 20), 'M', 1);
        int codA = gestor.addAtivDist(LocalDateTime.now(), LocalTime.of(0, 45), 130, 10.0, 1);
        gestor.registaAtividade(codU, codA);

        String atividades = gestor.atividadesUtilizador(codU);
        assertTrue(atividades.contains("Corrida"));
    }
}
