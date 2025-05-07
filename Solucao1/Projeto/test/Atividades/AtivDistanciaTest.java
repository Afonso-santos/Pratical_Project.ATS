package Projeto.test.Atividades;

import Projeto.src.main.java.AtivDistancia;
import Projeto.src.main.java.Atividade;
import Projeto.src.main.java.Utilizador;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class AtivDistanciaConcreta extends AtivDistancia {
    public AtivDistanciaConcreta() {
        super();
    }

    public AtivDistanciaConcreta(LocalDateTime realizacao, LocalTime tempo, int freqCardiaca, double distancia) {
        super(realizacao, tempo, freqCardiaca, distancia);
    }

    public AtivDistanciaConcreta(AtivDistancia umaAtivDistancia) {
        super(umaAtivDistancia);
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
        return new AtivDistanciaConcreta(this);
    }
}

class AtivDistanciaTest {
    @Test
    void testDefaultConstructor() {
        // Create an instance using the default constructor
        AtivDistanciaConcreta atividade = new AtivDistanciaConcreta();

        // Verify that the distance is set to the default value (0.0)
        assertEquals(0.0, atividade.getDistancia(), 0.0);

        // Also check that the other attributes (inherited from Atividade) are correctly initialized
        assertNotNull(atividade.getDataRealizacao()); // Ensures the superclass constructor sets the dataRealizacao
        assertNotNull(atividade.getTempo());          // Ensures the tempo is initialized
        assertEquals(0, atividade.getFreqCardiaca()); // Ensures the freqCardiaca is initialized to 0
    }

    @Test
    void testParameterizedConstructor() {
        // Create a LocalDateTime and LocalTime for testing
        LocalDateTime realizacao = LocalDateTime.of(2023, 5, 5, 12, 0);
        LocalTime tempo = LocalTime.of(1, 0);
        int freqCardiaca = 120;
        double distancia = 10.5; // Example distance value

        // Create an instance using the parameterized constructor
        AtivDistanciaConcreta atividade = new AtivDistanciaConcreta(realizacao, tempo, freqCardiaca, distancia);

        // Verify that the distance is correctly set
        assertEquals(distancia, atividade.getDistancia(), 0.0);

        // Verify the other fields are also correctly initialized
        assertEquals(realizacao, atividade.getDataRealizacao());
        assertEquals(tempo, atividade.getTempo());
        assertEquals(freqCardiaca, atividade.getFreqCardiaca());
    }

    @Test
    void testCopyConstructor() {
        // Create a LocalDateTime and LocalTime for testing
        LocalDateTime realizacao = LocalDateTime.of(2023, 5, 5, 12, 0);
        LocalTime tempo = LocalTime.of(1, 0);
        int freqCardiaca = 120;
        double distancia = 10.5; // Example distance value

        // Create an instance using the parameterized constructor
        AtivDistancia original = new AtivDistanciaConcreta(realizacao, tempo, freqCardiaca, distancia);

        // Create a copy of the original object using the copy constructor
        AtivDistancia copia = new AtivDistanciaConcreta(original);

        // Verify that the copy has the same values as the original
        assertEquals(original.getDistancia(), copia.getDistancia(), 0.0);
        assertEquals(original.getDataRealizacao(), copia.getDataRealizacao());
        assertEquals(original.getTempo(), copia.getTempo());
        assertEquals(original.getFreqCardiaca(), copia.getFreqCardiaca());

        // Verify that they are different objects (not the same reference)
        assertNotSame(original, copia);
    }

    @Test
    void getDistancia() {
        // Create a LocalDateTime and LocalTime for testing
        LocalDateTime realizacao = LocalDateTime.of(2023, 5, 5, 12, 0);
        LocalTime tempo = LocalTime.of(1, 0);
        int freqCardiaca = 120;
        double distancia = 10.5; // Example distance value

        // Create an instance using the parameterized constructor
        AtivDistanciaConcreta atividade = new AtivDistanciaConcreta(realizacao, tempo, freqCardiaca, distancia);

        // Verify that the distance is correctly retrieved
        assertEquals(distancia, atividade.getDistancia(), 0.0);
    }

    @Test
    void setDistancia() {
        // Create a LocalDateTime and LocalTime for testing
        LocalDateTime realizacao = LocalDateTime.of(2023, 5, 5, 12, 0);
        LocalTime tempo = LocalTime.of(1, 0);
        int freqCardiaca = 120;
        double distancia = 10.5; // Example distance value

        // Create an instance using the parameterized constructor
        AtivDistanciaConcreta atividade = new AtivDistanciaConcreta(realizacao, tempo, freqCardiaca, distancia);

        // Set a new distance
        double novaDistancia = 15.0;
        atividade.setDistancia(novaDistancia);

        // Verify that the new distance is correctly set
        assertEquals(novaDistancia, atividade.getDistancia(), 0.0);

        atividade.setDistancia(-5.0);
        // Verify that the distance is still the same (not negative)
        assertEquals(0.0, atividade.getDistancia(), 0.0);
    }

    @Test
    void getFatorVelocidade() {
        // Create a LocalDateTime and LocalTime for testing
        LocalDateTime realizacao = LocalDateTime.of(2023, 5, 5, 12, 0);
        LocalTime tempo = LocalTime.of(1, 0);
        int freqCardiaca = 120;
        double distancia = 10.5; // Example distance value

        // Create an instance using the parameterized constructor
        AtivDistanciaConcreta atividade = new AtivDistanciaConcreta(realizacao, tempo, freqCardiaca, distancia);

        // Calculate the expected factor of velocity
        double valorNulo = 0.0;
        double valorIncremento = 1.0;
        double expectedFatorVelocidade = (distancia / tempo.toSecondOfDay() - valorNulo) * valorIncremento;

        // Verify that the factor of velocity is correctly calculated
        assertEquals(expectedFatorVelocidade, atividade.getFatorVelocidade(valorNulo, valorIncremento), 0.0);

        // Test with a different value for valorNulo and valorIncremento
        distancia = -10.5; // Example distance value
        atividade.setDistancia(distancia);
        valorNulo = 0.0;
        valorIncremento = 1.0;

        // distance need to be 00 becasue is negative
        expectedFatorVelocidade = ( 0/ tempo.toSecondOfDay() - valorNulo) * valorIncremento;

        // Verify that the factor of velocity is correctly calculated
        assertEquals(expectedFatorVelocidade, atividade.getFatorVelocidade(valorNulo, valorIncremento), 0.0);
    }



    @Test
    void getVelocidade() {
        // Create a LocalDateTime and LocalTime for testing
        LocalDateTime realizacao = LocalDateTime.of(2023, 5, 5, 12, 0);
        LocalTime tempo = LocalTime.of(1, 0);
        int freqCardiaca = 120;
        double distancia = 10.5; // Example distance value

        // Create an instance using the parameterized constructor
        AtivDistanciaConcreta atividade = new AtivDistanciaConcreta(realizacao, tempo, freqCardiaca, distancia);

        // Verify that the velocity is correctly calculated
        assertEquals(distancia / tempo.toSecondOfDay(), atividade.getVelocidade(), 0.0);
    }

    @Test
    void testEquals() {
        // Create two instances of AtivDistanciaConcreta with same values
        AtivDistanciaConcreta atividade1 = new AtivDistanciaConcreta(
                LocalDateTime.of(2023, 5, 5, 12, 0),
                LocalTime.of(1, 0),
                120,
                10.5
        );

        AtivDistanciaConcreta atividade2 = new AtivDistanciaConcreta(
                LocalDateTime.of(2023, 5, 5, 12, 0),
                LocalTime.of(1, 0),
                120,
                10.5
        );

        // Same object reference, should return true
        assertTrue(atividade1.equals(atividade1), "Should be equal to itself");

        // Null object, should return false
        assertFalse(atividade1.equals(null), "Should not be equal to null");

        // Different class type, should return false
        String otherObject = "Some String"; // Different class
        assertFalse(atividade1.equals(otherObject), "Should not be equal to a different class");

        // Different values (e.g., distance), should return false
        AtivDistanciaConcreta atividade3 = new AtivDistanciaConcreta(
                LocalDateTime.of(2023, 5, 5, 12, 0),
                LocalTime.of(1, 0),
                120,
                15.0 // Different distance
        );
        assertFalse(atividade1.equals(atividade3), "Should not be equal to an object with different values");

        // Identical objects, should return true
        assertTrue(atividade1.equals(atividade2), "Should be equal to an identical object");
    }

    void testToString() {
        // Criação de objetos de teste
        AtivDistanciaConcreta atividade1 = new AtivDistanciaConcreta(
                LocalDateTime.of(2023, 5, 5, 12, 0),
                LocalTime.of(1, 0),
                120,
                1000.0 // Distância de exemplo
        );

        // Teste 1: Verificar se o toString inclui a distância correta
        String resultadoEsperado = "Data de realização: 2023-05-05T12:00\n" +
                "Tempo: 01:00\n" +
                "Frequência cardíaca: 120 bpm\n" +
                "Distancia: 1000.0 metros";  // A parte da distância depende do comportamento da classe pai
        assertTrue(atividade1.toString().contains("Data de realização: 2023-05-05T12:00"));
        assertTrue(atividade1.toString().contains("Tempo: 01:00"));
        assertTrue(atividade1.toString().contains("Frequência cardíaca: 120 bpm"));
        assertTrue(atividade1.toString().contains("Distancia: 1000.0 metros"));

        // Teste 2: Teste com distância 0.0
        AtivDistanciaConcreta atividade2 = new AtivDistanciaConcreta(
                LocalDateTime.of(2023, 5, 5, 12, 0),
                LocalTime.of(1, 0),
                100,
                0.0
        );

        resultadoEsperado = "Data de realização: 2023-05-05T12:00\n" +
                "Tempo: 01:00\n" +
                "Frequência cardíaca: 100 bpm\n" +
                "Distancia: 0.0 metros";  // Testando a distância 0
        assertTrue(atividade2.toString().contains("Data de realização: 2023-05-05T12:00"));
        assertTrue(atividade2.toString().contains("Tempo: 01:00"));
        assertTrue(atividade2.toString().contains("Frequência cardíaca: 100 bpm"));
        assertTrue(atividade2.toString().contains("Distancia: 0.0 metros"));

        // Teste 3: Teste com distância negativa
        AtivDistanciaConcreta atividade3 = new AtivDistanciaConcreta(
                LocalDateTime.of(2023, 5, 5, 12, 0),
                LocalTime.of(1, 0),
                110,
                -500.0  // Distância negativa
        );

        resultadoEsperado = "Data de realização: 2023-05-05T12:00\n" +
                "Tempo: 01:00\n" +
                "Frequência cardíaca: 110 bpm\n" +
                "Distancia: -500.0 metros";  // Testando a distância negativa
        assertTrue(atividade3.toString().contains("Data de realização: 2023-05-05T12:00"));
        assertTrue(atividade3.toString().contains("Tempo: 01:00"));
        assertTrue(atividade3.toString().contains("Frequência cardíaca: 110 bpm"));
        assertTrue(atividade3.toString().contains("Distancia: -500.0 metros"));
    }


}