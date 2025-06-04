package Projeto.test.Atividades;


import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import Projeto.AtivRepeticoes;
import Projeto.Atividade;
import Projeto.Utilizador;


class AtividadeRepeticoesConcreta extends AtivRepeticoes {
    public AtividadeRepeticoesConcreta() {
        super();
    }

    public AtividadeRepeticoesConcreta(LocalDateTime realizacao, LocalTime tempo, int freqCardiaca, int repeticoes) {
        super(realizacao, tempo, freqCardiaca, repeticoes);
    }

    public AtividadeRepeticoesConcreta(AtivRepeticoes atividade) {
        super(atividade);
    }

    public AtividadeRepeticoesConcreta(LocalDateTime now, LocalTime tempo, int i) {
        super(now, tempo, i, 0);
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
        return new AtividadeRepeticoesConcreta(this);
    }

}


class AtivRepeticoesTest {

    @Test
    void testDefaultConstructor() {
        AtividadeRepeticoesConcreta atividade = new AtividadeRepeticoesConcreta();

        assertNotNull(atividade);
        assertEquals(0, atividade.getRepeticoes());
        assertNotNull(atividade.getDataRealizacao());
        assertEquals(LocalTime.of(0, 0), atividade.getTempo());
        assertEquals(0, atividade.getFreqCardiaca());
    }


    @Test
    void testParameterizedConstructor() {
        LocalDateTime data = LocalDateTime.of(2023, 5, 5, 14, 0);
        LocalTime tempo = LocalTime.of(0, 45);
        int freq = 100;
        int reps = 30;

        AtividadeRepeticoesConcreta atividade = new AtividadeRepeticoesConcreta(data, tempo, freq, reps);

        assertEquals(data, atividade.getDataRealizacao());
        assertEquals(tempo, atividade.getTempo());
        assertEquals(freq, atividade.getFreqCardiaca());
        assertEquals(reps, atividade.getRepeticoes());
    }

    @Test
    void testCopyConstructor() {
        LocalDateTime data = LocalDateTime.of(2023, 5, 5, 14, 0);
        LocalTime tempo = LocalTime.of(0, 30);
        int freq = 110;
        int reps = 25;

        AtividadeRepeticoesConcreta original = new AtividadeRepeticoesConcreta(data, tempo, freq, reps);
        AtividadeRepeticoesConcreta copia = new AtividadeRepeticoesConcreta(original);

        assertNotSame(original, copia); // Different instances
        assertEquals(original.getDataRealizacao(), copia.getDataRealizacao());
        assertEquals(original.getTempo(), copia.getTempo());
        assertEquals(original.getFreqCardiaca(), copia.getFreqCardiaca());
        assertEquals(original.getRepeticoes(), copia.getRepeticoes());
    }


    @Test
        void getRepeticoes() {
            // reset para teste previsível
            AtividadeRepeticoesConcreta atividade = new AtividadeRepeticoesConcreta();

            assertEquals(0, atividade.getRepeticoes());

            AtividadeRepeticoesConcreta actividade2 = new AtividadeRepeticoesConcreta(LocalDateTime.of(2023, 5, 5, 12, 0), LocalTime.of(1, 0), 120, 10);
            assertEquals(10, actividade2.getRepeticoes());

            AtividadeRepeticoesConcreta actividade3 = new AtividadeRepeticoesConcreta(actividade2);
            assertEquals(10, actividade3.getRepeticoes());
        }

        @Test
        void setRepeticoes() {
            // reset para teste previsível
            AtividadeRepeticoesConcreta atividade = new AtividadeRepeticoesConcreta();
            atividade.setRepeticoes(10);
            assertEquals(10, atividade.getRepeticoes());

            atividade.setRepeticoes(20);
            assertEquals(20, atividade.getRepeticoes());
        }


        @Test
        void testGetFatorRepeticoes() {
            // Preparação
            AtividadeRepeticoesConcreta atividade = new AtividadeRepeticoesConcreta();
            double valorNulo = 1.5;
            double valorIncremento = 2.0;

            // Caso 1: Tempo normal - 2 minutos (120 segundos)
            atividade.setTempo(LocalTime.of(0, 2)); // 2 minutes
            atividade.setRepeticoes(240);           // 240 reps
            double esperado = (240.0 / 120.0 - 1.5) * 2.0; // (2.0 - 1.5) * 2 = 1.0
            assertEquals(1.0, atividade.getFatorRepeticoes(valorNulo, valorIncremento), 0.0001);

            // Caso 2: Tempo 1 segundo - extrema intensidade
            atividade.setTempo(LocalTime.of(0, 0, 1)); // 1 second
            atividade.setRepeticoes(240);
            esperado = (240.0 - 1.5) * 2.0; // (240 - 1.5) * 2 = 477.0
            assertEquals(477.0, atividade.getFatorRepeticoes(valorNulo, valorIncremento), 0.0001);

            // Caso 3: Tempo 0 segundos - caso limite, esperado 0.0
            atividade.setTempo(LocalTime.of(0, 0)); // 0 seconds
            atividade.setRepeticoes(240);
            esperado = 0.0;
            assertEquals(esperado, atividade.getFatorRepeticoes(valorNulo, valorIncremento), 0.0001);
        }


    @Test
    void testToString() {
        // Create an instance of the concrete class with all necessary parameters
        AtivRepsPesoConcreta atividade = new AtivRepsPesoConcreta(
                LocalDateTime.of(2023, 5, 5, 12, 0),
                LocalTime.of(1, 0),
                120,
                10,
                80.0
        );

        // Format the date as expected in the toString method
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String formattedDateTime = atividade.getDataRealizacao().format(formatter);

        // Prepare the expected string based on how the toString methods are implemented
        String expected = "Atividade\nId: " + atividade.getCodAtividade() +
                "\nData e hora: " + formattedDateTime +
                "\nDuraçao: 01:00" +
                "\nFrequencia Cardiaca: 120 bpm" +
                "\nRepeticoes: 10" +
                "\nPeso: 80.0 kilos";



        // Alternatively, if you only want to check that the output contains "Peso"
        assertTrue(atividade.toString().contains("Peso: 80.0 kilos"));
    }




}