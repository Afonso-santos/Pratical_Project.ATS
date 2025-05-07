package Projeto.test.Atividades;

import Projeto.src.main.java.AtivRepsPeso;
import Projeto.src.main.java.Atividade;
import Projeto.src.main.java.Utilizador;
import Projeto.src.main.java.UtilizadorAmador;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;



class AtivRepsPesoConcreta extends AtivRepsPeso {
    public AtivRepsPesoConcreta() {
        super();
    }

    public AtivRepsPesoConcreta(LocalDateTime realizacao, LocalTime tempo, int freqCardiaca, int repeticoes, double peso) {
        super(realizacao, tempo, freqCardiaca, repeticoes, peso);
    }

    public AtivRepsPesoConcreta(AtivRepsPeso atividade) {
        super(atividade);
    }

    @Override
    public double consumoCalorias(Utilizador utilizador) {
        return 0;
    }

    @Override
    public Atividade geraAtividade(Utilizador utilizador, double consumoCalorias) {
        return null;
    }

    @Override
    public Object clone() {
        return new AtivRepsPesoConcreta(this);
    }
}



class AtivRepsPesoTest {

    @Test
    void getPeso() {
        AtivRepsPesoConcreta atividade = new AtivRepsPesoConcreta();
        atividade.setPeso(10.0);
        assertEquals(10.0, atividade.getPeso());
    }

    @Test
    void setPeso() {
        AtivRepsPesoConcreta atividade = new AtivRepsPesoConcreta();
        atividade.setPeso(15.0);
        assertEquals(15.0, atividade.getPeso());
    }

    @Test
    void getFatorPeso() {
        AtivRepsPesoConcreta atividade = new AtivRepsPesoConcreta();
        Utilizador utilizador = new UtilizadorAmador();
        utilizador.setPeso(70.0);

        // Peso maior que o do utilizador
        atividade.setPeso(80.0);
        double fator = atividade.getFatorPeso(utilizador, 1.0, 2.0);
        assertEquals(0.2857142857142857, fator, 0.0001);

        // Peso igual ao do utilizador
        atividade.setPeso(70.0);
        fator = atividade.getFatorPeso(utilizador, 1.0, 2.0);
        assertEquals(0.0, fator, 0.0001);

        // Peso menor que o do utilizador
        atividade.setPeso(60.0);
        fator = atividade.getFatorPeso(utilizador, 1.0, 2.0);
        assertEquals(-0.2857142857142857, fator, 0.0001);

        // Peso nulo
        atividade.setPeso(0.0);
        fator = atividade.getFatorPeso(utilizador, 1.0, 2.0);
        assertEquals(-2.0, fator, 0.0001); // (0 - 1.0) * 2.0 = -2.0

        // Peso negativo
        atividade.setPeso(-10.0);
        fator = atividade.getFatorPeso(utilizador, 1.0, 2.0);
        double razaoPeso = -10.0 / 70.0; // proper double division
        double fatorEsperado = (razaoPeso - 1.0) * 2.0;
        assertEquals(fatorEsperado, fator, 0.0001);
    }


    @Test
    void testEquals() {
        // Criação de objetos de teste
        AtivRepsPesoConcreta atividade1 = new AtivRepsPesoConcreta(
                LocalDateTime.of(2023, 5, 5, 12, 0),
                LocalTime.of(1, 0),
                120,
                10,
                50.0
        );

        AtivRepsPesoConcreta atividade2 = new AtivRepsPesoConcreta(
                LocalDateTime.of(2023, 5, 5, 12, 0),
                LocalTime.of(1, 0),
                120,
                10,
                50.0
        );

        AtivRepsPesoConcreta atividade3 = new AtivRepsPesoConcreta(
                LocalDateTime.of(2023, 5, 5, 12, 0),
                LocalTime.of(1, 0),
                120,
                15,
                60.0
        );

        // Teste 1: Comparar o mesmo objeto (deve retornar true)
        assertTrue(atividade1.equals(atividade1));

        // Teste 2: Comparar com null (deve retornar false)
        assertFalse(atividade1.equals(null));

        // Teste 3: Comparar com objeto de classe diferente (deve retornar false)
        assertFalse(atividade1.equals(new Object()));

        // Teste 4: Comparar com objeto de classe igual e mesmos valores (deve retornar true)
        assertTrue(atividade1.equals(atividade2));

        // Teste 5: Comparar com objeto de classe igual e valores diferentes (deve retornar false)
        assertFalse(atividade1.equals(atividade3));
    }


    @Test
    void testToString() {
        // Criação de objetos de teste
        AtivRepsPesoConcreta atividade1 = new AtivRepsPesoConcreta(
                LocalDateTime.of(2023, 5, 5, 12, 0),
                LocalTime.of(1, 0),
                120,
                10,
                50.0
        );

        // Teste 1: Verificar se o toString inclui o peso correto
        String resultadoEsperado = "Data de realização: 2023-05-05T12:00\n" +
                "Tempo: 01:00\n" +
                "Frequência cardíaca: 120 bpm\n" +
                "Repetições: 10\n" +  // Esta parte depende do comportamento da classe pai
                "Peso: 50.0 kilos";
        assertEquals(resultadoEsperado, atividade1.toString());

        // Teste 2: Teste com peso 0.0
        AtivRepsPesoConcreta atividade2 = new AtivRepsPesoConcreta(
                LocalDateTime.of(2023, 5, 5, 12, 0),
                LocalTime.of(1, 0),
                100,
                5,
                0.0
        );

        resultadoEsperado = "Data de realização: 2023-05-05T12:00\n" +
                "Tempo: 01:00\n" +
                "Frequência cardíaca: 100 bpm\n" +
                "Repetições: 5\n" +  // Esta parte depende do comportamento da classe pai
                "Peso: 0.0 kilos";
        assertEquals(resultadoEsperado, atividade2.toString());

        // Teste 3: Teste com valor de peso negativo
        AtivRepsPesoConcreta atividade3 = new AtivRepsPesoConcreta(
                LocalDateTime.of(2023, 5, 5, 12, 0),
                LocalTime.of(1, 0),
                110,
                8,
                -10.0
        );

        resultadoEsperado = "Data de realização: 2023-05-05T12:00\n" +
                "Tempo: 01:00\n" +
                "Frequência cardíaca: 110 bpm\n" +
                "Repetições: 8\n" +  // Esta parte depende do comportamento da classe pai
                "Peso: -10.0 kilos";
        assertEquals(resultadoEsperado, atividade3.toString());
    }

}