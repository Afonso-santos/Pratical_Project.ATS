package Projeto.test.Atividades;


import Projeto.AtivDistAltimetria;
import Projeto.Atividade;
import Projeto.Utilizador;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class AtivDistAlimetriaConcreta extends AtivDistAltimetria {
    public AtivDistAlimetriaConcreta() {
        super();
    }

    public AtivDistAlimetriaConcreta(LocalDateTime realizacao, LocalTime tempo, int freqCardiaca, double distancia, double altimetria) {
        super(realizacao, tempo, freqCardiaca, distancia, altimetria);
    }

    public AtivDistAlimetriaConcreta(AtivDistAltimetria atividade) {
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
        return new AtivDistAlimetriaConcreta(this);
    }

}

class AtivDistAltimetriaTest {
    @Test
    void testDefaultConstructor() {
        // Create an instance using the default constructor
        AtivDistAlimetriaConcreta atividade = new AtivDistAlimetriaConcreta();

        // Verify that the altimetria is set to the default value (0.0)
        assertEquals(0.0, atividade.getAltimetria(), 0.0);

        // Verify that the other attributes (inherited from AtivDistAltimetria) are correctly initialized
        assertNotNull(atividade.getDataRealizacao()); // Ensures the superclass constructor sets the dataRealizacao
        assertNotNull(atividade.getTempo());          // Ensures the tempo is initialized
        assertEquals(0, atividade.getFreqCardiaca()); // Ensures the freqCardiaca is initialized to 0
        assertEquals(0.0, atividade.getDistancia(), 0.0); // Ensures the distancia is initialized to 0.0
    }

    @Test
    void testParameterizedConstructor() {
        // Create test values
        LocalDateTime realizacao = LocalDateTime.of(2023, 5, 5, 12, 0);
        LocalTime tempo = LocalTime.of(1, 0);
        int freqCardiaca = 120;
        double distancia = 10.5;
        double altimetria = 300.0;

        // Create an instance using the parameterized constructor
        AtivDistAlimetriaConcreta atividade = new AtivDistAlimetriaConcreta(realizacao, tempo, freqCardiaca, distancia, altimetria);

        // Verify that the attributes are correctly set
        assertEquals(realizacao, atividade.getDataRealizacao());
        assertEquals(tempo, atividade.getTempo());
        assertEquals(freqCardiaca, atividade.getFreqCardiaca());
        assertEquals(distancia, atividade.getDistancia(), 0.0);
        assertEquals(altimetria, atividade.getAltimetria(), 0.0);
    }

    @Test
    void testCopyConstructor() {
        // Create test values
        LocalDateTime realizacao = LocalDateTime.of(2023, 5, 5, 12, 0);
        LocalTime tempo = LocalTime.of(1, 0);
        int freqCardiaca = 120;
        double distancia = 10.5;
        double altimetria = 300.0;

        // Create an instance using the parameterized constructor
        AtivDistAltimetria original = new AtivDistAlimetriaConcreta(realizacao, tempo, freqCardiaca, distancia, altimetria);

        // Create a copy of the original object using the copy constructor
        AtivDistAltimetria copia = new AtivDistAlimetriaConcreta(original);

        // Verify that the copy has the same values as the original
        assertEquals(original.getAltimetria(), copia.getAltimetria(), 0.0);
        assertEquals(original.getDataRealizacao(), copia.getDataRealizacao());
        assertEquals(original.getTempo(), copia.getTempo());
        assertEquals(original.getFreqCardiaca(), copia.getFreqCardiaca());
        assertEquals(original.getDistancia(), copia.getDistancia(), 0.0);

        // Verify that they are different objects (not the same reference)
        assertNotSame(original, copia);
    }

    @Test
    void getAltimetria() {
        AtivDistAlimetriaConcreta atividade = new AtivDistAlimetriaConcreta();
        atividade.setAltimetria(100.0);
        assertEquals(100.0, atividade.getAltimetria(), 0.0);

        // Test with a negative value
        atividade.setAltimetria(-50.0);
        assertEquals(-50.0, atividade.getAltimetria(), 0.0);
        // Test with zero
        atividade.setAltimetria(0.0);
    }

    @Test
    void setAltimetria() {
        AtivDistAlimetriaConcreta atividade = new AtivDistAlimetriaConcreta();
        atividade.setAltimetria(200.0);
        assertEquals(200.0, atividade.getAltimetria(), 0.0);

        // Test with a negative value
        atividade.setAltimetria(-100.0);
        assertEquals(-100.0, atividade.getAltimetria(), 0.0);
        // Test with zero
        atividade.setAltimetria(0.0);
    }

    @Test
    void getFatorAltimetria() {
        AtivDistAlimetriaConcreta atividade = new AtivDistAlimetriaConcreta();
        atividade.setAltimetria(100.0);
        assertEquals(0.05, atividade.getFatorAltimetria(), 0.0);

        // Test with a negative value
        atividade.setAltimetria(-50.0);
        assertEquals(-0.025, atividade.getFatorAltimetria(), 0.0);
        // Test with zero
        atividade.setAltimetria(0.0);
        assertEquals(0.0, atividade.getFatorAltimetria(), 0.0);
    }

    @Test
    void testEquals() {
        // Create two instances of AtivDistAltimetriaConcreta
        LocalDateTime realizacao = LocalDateTime.of(2023, 5, 5, 12, 0);
        LocalTime tempo = LocalTime.of(1, 0);
        int freqCardiaca = 120;
        double distancia = 1000.0;
        double altimetria = 200.0;

        AtivDistAlimetriaConcreta atividade1 = new AtivDistAlimetriaConcreta(realizacao, tempo, freqCardiaca, distancia, altimetria);
        AtivDistAlimetriaConcreta atividade2 = new AtivDistAlimetriaConcreta(realizacao, tempo, freqCardiaca, distancia, altimetria);

        // Test equality with the same data
        assertTrue(atividade1.equals(atividade2), "The objects should be equal when they have the same data");

        // Modify one of the objects (altimetria) to ensure they are no longer equal
        AtivDistAlimetriaConcreta atividade3 = new AtivDistAlimetriaConcreta(realizacao, tempo, freqCardiaca, distancia, altimetria + 50.0);
        assertFalse(atividade1.equals(atividade3), "The objects should not be equal when their data is different");
    }

    @Test

    void testToString() {
        // Create an instance of AtivDistAltimetriaConcreta
        LocalDateTime realizacao = LocalDateTime.of(2023, 5, 5, 12, 0);
        LocalTime tempo = LocalTime.of(1, 0);
        int freqCardiaca = 120;
        double distancia = 1000.0;
        double altimetria = 200.0;

        AtivDistAlimetriaConcreta atividade = new AtivDistAlimetriaConcreta(realizacao, tempo, freqCardiaca, distancia, altimetria);

        // Format the LocalDateTime properly to match the expected format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        String formattedDataRealizacao = realizacao.format(formatter);

        // Expected string output based on the toString method
        String expectedString = "Data de realização: " + formattedDataRealizacao + "\n" +
                "Tempo: 01:00\n" +
                "Frequência cardíaca: 120 bpm\n" +
                "Distancia: 1000.0 metros\n" +
                "Altimetria: 200.0 metros";

        // Check if the toString includes the expected parts
        System.out.println(atividade.toString());
        assertTrue(atividade.toString().contains("05/05/2023 12:00:00"),
                "The toString output should contain the correct dataRealizacao");
        assertTrue(atividade.toString().contains("Duraçao: 01:00"),
                "The toString output should contain the correct tempo");
        assertTrue(atividade.toString().contains("Frequencia Cardiaca: 120 bpm"),
                "The toString output should contain the correct freqCardiaca");
        assertTrue(atividade.toString().contains("Distancia: 1000.0 metros"),
                "The toString output should contain the correct distancia");
        assertTrue(atividade.toString().contains("Altimetria: 200.0 metros"),
                "The toString output should contain the correct altimetria");
    }

}