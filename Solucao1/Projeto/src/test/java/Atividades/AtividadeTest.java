package Projeto.test.Atividades;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import Projeto.Atividade;
import Projeto.Utilizador;
import Projeto.UtilizadorAmador;
import Projeto.UtilizadorPraticanteOcasional;
import Projeto.UtilizadorProfissional;

class AtividadeConcreta extends Atividade {
    public AtividadeConcreta() {
        super();
    }

    public AtividadeConcreta(LocalDateTime realizacao, LocalTime tempo, int freqCardiaca) {
        super(realizacao, tempo, freqCardiaca);
    }

    public AtividadeConcreta(Atividade atividade) {
        super(atividade);
    }

    public double consumoCalorias(Utilizador utilizador) {
        // Implementar o cálculo de calorias consumidas
        return 0.0;
    }

    public Atividade geraAtividade(Utilizador utilizador, double consumoCalorias) {
        // Implementar a geração de uma nova atividade
        return new AtividadeConcreta(this);
    }

    public Object clone()  {
        return new AtividadeConcreta(this);
    }
}

class AtividadeTest {

    
    @Test
    void testConstrutorVazio() {
        Atividade atividade = new AtividadeConcreta();

        assertNotNull(atividade.getDataRealizacao());
        assertEquals(LocalTime.of(0, 0), atividade.getTempo());
        assertEquals(0, atividade.getFreqCardiaca());
    }

    @Test
    void testConstrutorParametrizado() {
        LocalDateTime data = LocalDateTime.of(2023, 5, 5, 12, 0);
        LocalTime tempo = LocalTime.of(1, 30);
        int freq = 75;

        Atividade atividade = new AtividadeConcreta(data, tempo, freq);


        assertEquals(data, atividade.getDataRealizacao());
        assertEquals(tempo, atividade.getTempo());
        assertEquals(freq, atividade.getFreqCardiaca());
    }

    @Test
    void testConstrutorCopia() {
        LocalDateTime data = LocalDateTime.of(2023, 1, 1, 8, 0);
        LocalTime tempo = LocalTime.of(0, 45);
        int freq = 60;

        Atividade original = new AtividadeConcreta(data, tempo, freq);
        Atividade copia = new AtividadeConcreta(original);

        assertEquals(original.getCodAtividade(), copia.getCodAtividade());
        assertEquals(original.getDataRealizacao(), copia.getDataRealizacao());
        assertEquals(original.getTempo(), copia.getTempo());
        assertEquals(original.getFreqCardiaca(), copia.getFreqCardiaca());
    }

    @Test
    void setProximoCodigo() {
        // Set initial value to something unique for test clarity
        Atividade atividade = new AtividadeConcreta();
        atividade.setProximoCodigo(100);

        // Create an instance and check if it gets the set value
        Atividade atividade1 = new AtividadeConcreta();
        assertEquals(100, atividade1.getCodAtividade());

        // Next one should auto-increment
        Atividade atividade2 = new AtividadeConcreta();
        assertEquals(101, atividade2.getCodAtividade());
    }



    @Test
    void getDataRealizacao() {
        // Create an instance of Atividade
        LocalDateTime now = LocalDateTime.now();
        Atividade atividade = new AtividadeConcreta(now, LocalTime.of(1, 0), 70);

        // Check if the date is set correctly
        assertEquals(now, atividade.getDataRealizacao());
    }

    @Test
    void getTempo() {
        // Create an instance of Atividade
        LocalTime tempo = LocalTime.of(1, 0);
        Atividade atividade = new AtividadeConcreta(LocalDateTime.now(), tempo, 70);

        // Check if the time is set correctly
        assertEquals(tempo, atividade.getTempo());
    }

    @Test
    void getFreqCardiaca() {
        // Create an instance of Atividade
        int freqCardiaca = 70;
        Atividade atividade = new AtividadeConcreta(LocalDateTime.now(), LocalTime.of(1, 0), freqCardiaca);

        // Check if the heart rate is set correctly
        assertEquals(freqCardiaca, atividade.getFreqCardiaca());
    }

    @Test
    void setDataRealizacao() {
        // Create an instance of Atividade
        LocalDateTime now = LocalDateTime.now();
        Atividade atividade = new AtividadeConcreta(now, LocalTime.of(1, 0), 70);

        // Set a new date
        LocalDateTime newDate = now.plusDays(1);
        atividade.setDataRealizacao(newDate);

        // Check if the date is set correctly
        assertEquals(newDate, atividade.getDataRealizacao());
    }

    @Test
    void setTempo() {
        // Create an instance of Atividade
        LocalTime tempo = LocalTime.of(1, 0);
        Atividade atividade = new AtividadeConcreta(LocalDateTime.now(), tempo, 70);

        // Set a new time
        LocalTime newTempo = LocalTime.of(2, 0);
        atividade.setTempo(newTempo);

        // Check if the time is set correctly
        assertEquals(newTempo, atividade.getTempo());
    }

    @Test
    void setFreqCardiaca() {
        // Create an instance of Atividade
        int freqCardiaca = 70;
        Atividade atividade = new AtividadeConcreta(LocalDateTime.now(), LocalTime.of(1, 0), freqCardiaca);

        // Set a new heart rate
        int newFreqCardiaca = 80;
        atividade.setFreqCardiaca(newFreqCardiaca);

        // Check if the heart rate is set correctly
        assertEquals(newFreqCardiaca, atividade.getFreqCardiaca());
    }

    @Test
    void getFatorFreqCardiacaAmador() {
        // Create an instance of Utilizador
        Utilizador utilizador = new UtilizadorAmador();
        double freqCardiaca = utilizador.getFreqCardiaca();

        // Create an instance of Atividade
        Atividade atividade = new AtividadeConcreta(LocalDateTime.now(), LocalTime.of(1, 0), 70);

        // Check if the heart rate factor is calculated correctly
        double expectedFactor = ((freqCardiaca/ 70)-2) * 0.4; // Assuming 60 is the resting heart rate
        assertEquals(expectedFactor, atividade.getFatorFreqCardiaca(utilizador));
    }

    @Test
    void getFatorFreqCardiacaPraticanteOcasional() {
        // Create an instance of Utilizador
        Utilizador utilizador = new UtilizadorPraticanteOcasional();
        double freqCardiaca = utilizador.getFreqCardiaca();

        // Create an instance of Atividade
        Atividade atividade = new AtividadeConcreta(LocalDateTime.now(), LocalTime.of(1, 0), 70);

        // Check if the heart rate factor is calculated correctly
        double expectedFactor = ((freqCardiaca/ 70)-2) * 0.4; // Assuming 60 is the resting heart rate
        assertEquals(expectedFactor, atividade.getFatorFreqCardiaca(utilizador));
    }

    @Test
    void getFatorFreqCardiacaProfissional() {
        // Create an instance of Utilizador
        Utilizador utilizador = new UtilizadorProfissional();
        double freqCardiaca = utilizador.getFreqCardiaca();

        // Create an instance of Atividade
        Atividade atividade = new AtividadeConcreta(LocalDateTime.now(), LocalTime.of(1, 0), 70);

        // Check if the heart rate factor is calculated correctly
        double expectedFactor = ((freqCardiaca/ 70)-2) * 0.4; // Assuming 60 is the resting heart rate
        assertEquals(expectedFactor, atividade.getFatorFreqCardiaca(utilizador));
    }

    @Test
    void testToString() {
        LocalDateTime now = LocalDateTime.of(2023, 5, 5, 10, 30, 0);
        LocalTime tempo = LocalTime.of(1, 0);
        int freq = 70;

        Atividade atividade = new AtividadeConcreta(now, tempo, freq);
        int cod = atividade.getCodAtividade();

        String expected = "Atividade\nId: " + cod +
                "\nData e hora: " + now.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) +
                "\nDuraçao: " + tempo +
                "\nFrequencia Cardiaca: " + freq + " bpm";

        assertEquals(expected, atividade.toString());
    }

    @Test
    void testEquals() {
        LocalDateTime now = LocalDateTime.now();
        LocalTime tempo = LocalTime.of(1, 0);
        int freq = 70;

        Atividade atividade1 = new AtividadeConcreta(now, tempo, freq);
        Atividade atividade2 = new AtividadeConcreta(now, tempo, freq);

        assertTrue(atividade1.equals(atividade2));

        // Test with different values
        atividade2.setFreqCardiaca(80);
        assertFalse(atividade1.equals(atividade2));
    }

    @Test
    void compareTo() {
        LocalDateTime now = LocalDateTime.now();
        LocalTime tempo = LocalTime.of(1, 0);
        int freq = 70;

        Atividade atividade1 = new AtividadeConcreta(now, tempo, freq);
        Atividade atividade2 = new AtividadeConcreta(now.plusDays(1), tempo, freq);

        assertTrue(atividade1.compareTo(atividade2) < 0);
        assertTrue(atividade2.compareTo(atividade1) > 0);
    }
}