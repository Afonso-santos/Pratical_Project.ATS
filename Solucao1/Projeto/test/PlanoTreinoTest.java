package Projeto.test;

import Projeto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

class PlanoTreinoTest {


    @Test
    void setProximoCodigo() {
        PlanoTreino plano = new PlanoTreino(LocalDate.of(2025, 1, 1));
        plano.setProximoCodigo(100);
        PlanoTreino plano2 = new PlanoTreino(LocalDate.of(2025, 1, 2));
        assertTrue(plano2.getCodPlano() >= 100);
    }

    @Test
    void getAtividadesNumPeriodo() {
        PlanoTreino plano = new PlanoTreino(LocalDate.of(2024, 5, 1));
        Atividade atividade = new Corrida(LocalDateTime.now(), LocalTime.of(0, 30), 120, 5.0);
        plano.addAtividade(atividade, 2);
        List<?> atividades = plano.getAtividadesNumPeriodo(LocalDate.now().minusDays(1), LocalDate.now().plusDays(1));
        assertEquals(1, atividades.size());
    }

    @Test
    void getCodPlano() {
        PlanoTreino plano = new PlanoTreino(LocalDate.of(2024, 5, 1));
        assertTrue(plano.getCodPlano() > 0);
    }

    @Test
    void getAtividades() {
        PlanoTreino plano = new PlanoTreino(LocalDate.of(2024, 5, 1));
        Atividade atividade = new Corrida(LocalDateTime.now(), LocalTime.of(0, 30), 120, 5.0);
        plano.addAtividade(atividade, 1);
        assertEquals(1, plano.getAtividades().size());
    }

    @Test
    void getDataRealizacao() {
        PlanoTreino plano = new PlanoTreino(LocalDate.of(2024, 5, 1));
        assertEquals(LocalDate.of(2024, 5, 1), plano.getDataRealizacao());
    }

    @Test
    void setDataRealizacao() {
        PlanoTreino plano = new PlanoTreino(LocalDate.of(2024, 5, 1));
        plano.setDataRealizacao(LocalDate.of(2024, 6, 1));
        assertEquals(LocalDate.of(2024, 6, 1), plano.getDataRealizacao());
    }

    @Test
    void atividadesQueRespeitamP() {
        PlanoTreino plano = new PlanoTreino(LocalDate.of(2024, 5, 1));
        Atividade atividade = new Corrida(LocalDateTime.now(), LocalTime.of(0, 30), 120, 5.0);
        plano.addAtividade(atividade, 2);
        Predicate<Atividade> p = a -> a instanceof Corrida;
        List<Atividade> result = plano.atividadesQueRespeitamP(LocalDate.now().minusDays(1), LocalDate.now().plusDays(1), p);
        assertEquals(2, result.size());
    }

    @Test
    void caloriasDispendidas() {
        PlanoTreino plano = new PlanoTreino(LocalDate.of(2024, 5, 1));
        Utilizador utilizador = new UtilizadorAmador("Teste", "Cidade", "teste@mail.com", 70, 75, 175, LocalDate.of(2000, 1, 1), 'M');
        Atividade atividade = new Corrida(LocalDateTime.now(), LocalTime.of(0, 30), 120, 5.0);
        plano.addAtividade(atividade, 2);
        double calorias = plano.caloriasDispendidas(utilizador);
        System.out.println("Calorias dispendidas: " + calorias);
        assertTrue(calorias < 0);
    }

    @Test
    void addAtividade() {
        PlanoTreino plano = new PlanoTreino(LocalDate.of(2024, 5, 1));
        Atividade atividade = new Corrida(LocalDateTime.now(), LocalTime.of(0, 30), 120, 5.0);
        plano.addAtividade(atividade, 3);
        assertEquals(1, plano.getAtividades().size());
    }

    @Test
    void geraPlanoTreino() {
        PlanoTreino plano = new PlanoTreino(LocalDate.of(2024, 5, 1));
        Utilizador utilizador = new UtilizadorAmador("Teste", "Cidade", "teste@mail.com", 70, 75, 175, LocalDate.of(2000, 1, 1), 'M');
        List<Atividade> atividades = List.of(new Corrida(LocalDateTime.now(), LocalTime.of(0, 20), 110, 3.0),
                new Abdominais(LocalDateTime.now(), LocalTime.of(0, 15), 100, 20));
        List<PlanoTreino> planos = plano.geraPlanoTreino(utilizador, atividades, 2, 3, 600, LocalDate.now());
        assertFalse(planos.isEmpty());
    }

    @Test
    void testToString() {
        PlanoTreino plano = new PlanoTreino(LocalDate.of(2024, 5, 1));
        Atividade atividade = new Corrida(LocalDateTime.now(), LocalTime.of(0, 30), 120, 5.0);
        plano.addAtividade(atividade, 1);
        String output = plano.toString();
        assertTrue(output.contains("Plano de Treino"));
        assertTrue(output.contains("Atividades"));
    }

    @Test
    void compareTo() {
        PlanoTreino plano1 = new PlanoTreino(LocalDate.of(2024, 5, 1));
        PlanoTreino plano2 = new PlanoTreino(LocalDate.of(2024, 5, 2));
        assertTrue(plano1.compareTo(plano2) < 0);
    }

    @Test
    void testClone() {
        PlanoTreino plano = new PlanoTreino(LocalDate.of(2024, 5, 1));
        Atividade atividade = new Corrida(LocalDateTime.now(), LocalTime.of(0, 30), 120, 5.0);
        plano.addAtividade(atividade, 1);
        PlanoTreino clone = (PlanoTreino) plano.clone();
        assertNotSame(plano, clone);
        assertEquals(plano.getAtividades().size(), clone.getAtividades().size());
    }

    @Test
    void planoTreinoNumPeriodo() {
        PlanoTreino plano = new PlanoTreino(LocalDate.of(2024, 5, 1));
        Atividade atividade = new Corrida(LocalDateTime.now(), LocalTime.of(0, 30), 120, 5.0);
        plano.addAtividade(atividade, 1);
        PlanoTreino subPlano = (PlanoTreino) plano.planoTreinoNumPeriodo(LocalDate.now().minusDays(1), LocalDate.now().plusDays(1));
        assertFalse(subPlano.getAtividades().isEmpty());
    }
}
