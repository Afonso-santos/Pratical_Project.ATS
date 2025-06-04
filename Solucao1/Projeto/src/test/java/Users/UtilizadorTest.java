package Projeto.test.Users;


import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import Projeto.Utilizador;


class UtilizadorConcreta extends Utilizador {
    public UtilizadorConcreta() {
        super();
    }

    public UtilizadorConcreta(String nome, String morada, String email, int freqCardiaca, int peso, int altura, LocalDate dataNascimento, char genero) {
        super(nome, morada, email, freqCardiaca, peso, altura, dataNascimento, genero);
    }
    public UtilizadorConcreta(Utilizador utilizador) {
        super(utilizador);
    }

    @Override
    public Object clone() {
        return new UtilizadorConcreta(this);
    }

    @Override
    public Object utilizadorNumPeriodo(LocalDate DataI, LocalDate DataF) {
        return null;
    }

    @Override
    public double getFatorMultiplicativo(){
        return 0;
    }
}


class UtilizadorTest {



    @Test
    void getNome() {
        Utilizador u = new UtilizadorConcreta("John", "123 Street", "john@example.com", 70, 75, 180, LocalDate.of(1990, 5, 20), 'M');
        assertEquals("John", u.getNome());
    }

    @Test
    void getMorada() {
        Utilizador u = new UtilizadorConcreta("John", "123 Street", "john@example.com", 70, 75, 180, LocalDate.of(1990, 5, 20), 'M');
        assertEquals("123 Street", u.getMorada());
    }

    @Test
    void getEmail() {
        Utilizador u = new UtilizadorConcreta("John", "123 Street", "john@example.com", 70, 75, 180, LocalDate.of(1990, 5, 20), 'M');
        assertEquals("john@example.com", u.getEmail());
    }

    @Test
    void getFreqCardiaca() {
        Utilizador u = new UtilizadorConcreta("John", "123 Street", "john@example.com", 70, 75, 180, LocalDate.of(1990, 5, 20), 'M');
        assertEquals(70, u.getFreqCardiaca());
    }

    @Test
    void getPeso() {
        Utilizador u = new UtilizadorConcreta("John", "123 Street", "john@example.com", 70, 75, 180, LocalDate.of(1990, 5, 20), 'M');
        assertEquals(75, u.getPeso());
    }

    @Test
    void getAltura() {
        Utilizador u = new UtilizadorConcreta("John", "123 Street", "john@example.com", 70, 75, 180, LocalDate.of(1990, 5, 20), 'M');
        assertEquals(180, u.getAltura());
    }

    @Test
    void getDataNascimento() {
        Utilizador u = new UtilizadorConcreta("John", "123 Street", "john@example.com", 70, 75, 180, LocalDate.of(1990, 5, 20), 'M');
        assertEquals(LocalDate.of(1990, 5, 20), u.getDataNascimento());
    }

    @Test
    void getGenero() {
        Utilizador u = new UtilizadorConcreta("John", "123 Street", "john@example.com", 70, 75, 180, LocalDate.of(1990, 5, 20), 'M');
        assertEquals('M', u.getGenero());
    }

    @Test
    void setCodUtilizador() {
        Utilizador u = new UtilizadorConcreta("John", "123 Street", "john@example.com", 70, 75, 180, LocalDate.of(1990, 5, 20), 'M');
        u.setCodUtilizador(1);
        Utilizador u1 = new UtilizadorConcreta("John", "123 Street", "john@example.com", 70, 75, 180, LocalDate.of(1990, 5, 20), 'M');
        assertEquals(1, u.getCodUtilizador());
        u1.setCodUtilizador(1);
        assertEquals(1, u1.getCodUtilizador());
    }

    @Test
    void setNome() {
        Utilizador u = new UtilizadorConcreta("John", "123 Street", "john@example.com", 70, 75, 180, LocalDate.of(1990, 5, 20), 'M');
        u.setNome("Jack");
        assertEquals("Jack", u.getNome());
    }

    @Test
    void setMorada() {
        Utilizador u = new UtilizadorConcreta("John", "123 Street", "john@example.com", 70, 75, 180, LocalDate.of(1990, 5, 20), 'M');
        u.setMorada("456 Avenue");
        assertEquals("456 Avenue", u.getMorada());
    }

    @Test
    void setEmail() {
        Utilizador u = new UtilizadorConcreta("John", "123 Street", "john@example.com", 70, 75, 180, LocalDate.of(1990, 5, 20), 'M');
        u.setEmail("jack@example.com");
        assertEquals("jack@example.com", u.getEmail());
    }

    @Test
    void setFreqCardiaca() {
        Utilizador u = new UtilizadorConcreta("John", "123 Street", "john@example.com", 70, 75, 180, LocalDate.of(1990, 5, 20), 'M');
        u.setFreqCardiaca(80);
        assertEquals(80, u.getFreqCardiaca());

        Utilizador u1 = new UtilizadorConcreta("John", "123 Street", "john@example.com", 70, 75, 180, LocalDate.of(1990, 5, 20), 'M');
        u1.setFreqCardiaca(-100);
        assertEquals(80, u.getFreqCardiaca());
    }

    @Test
    void setPeso() {
        Utilizador u = new UtilizadorConcreta("John", "123 Street", "john@example.com", 70, 75, 180, LocalDate.of(1990, 5, 20), 'M');
        u.setPeso(80);
        assertEquals(80, u.getPeso());

        Utilizador u1 = new UtilizadorConcreta("John", "123 Street", "john@example.com", 70, 75, 180, LocalDate.of(1990, 5, 20), 'M');
        u1.setPeso(-1);
        assertEquals(80, u.getPeso());
    }

    @Test
    void setAltura() {
        Utilizador u = new UtilizadorConcreta("John", "123 Street", "john@example.com", 70, 75, 180, LocalDate.of(1990, 5, 20), 'M');
        u.setAltura(185);
        assertEquals(185, u.getAltura());


        Utilizador u1 = new UtilizadorConcreta("John", "123 Street", "john@example.com", 70, 75, 180, LocalDate.of(1990, 5, 20), 'M');
        u1.setAltura(-1);
        assertEquals(185, u.getAltura());
    }

    @Test
    void setDataNascimento() {
        Utilizador u = new UtilizadorConcreta("John", "123 Street", "john@example.com", 70, 75, 180, LocalDate.of(1990, 5, 20), 'M');
        u.setDataNascimento(LocalDate.of(1995, 5, 20));
        assertEquals(LocalDate.of(1995, 5, 20), u.getDataNascimento());
    }

    @Test
    void setGenero() {
        Utilizador u = new UtilizadorConcreta("John", "123 Street", "john@example.com", 70, 75, 180, LocalDate.of(1990, 5, 20), 'M');
        u.setGenero('F');
        assertEquals('F', u.getGenero());
        u.setGenero('x');
        assertEquals('F', u.getGenero());
    }

    @Test
    void getIdade() {
        Utilizador u = new UtilizadorConcreta("John", "123 Street", "john@example.com", 70, 75, 180, LocalDate.of(1990, 5, 20), 'M');
        assertEquals(35, u.getIdade());
    }

    @Test
    void getBMR() {
        Utilizador u = new UtilizadorConcreta("John", "123 Street", "john@example.com", 70, 75, 180, LocalDate.of(1990, 5, 20), 'M');
        double expectedBMR = 10 * 75 + 6.25 * 180 + 5 * 35 + 5;  // Using M formula for BMR
        assertEquals(expectedBMR, u.getBMR());

        Utilizador u2 = new UtilizadorConcreta("Jane", "123 Street", "Jane@example.com", 70, 65, 170, LocalDate.of(1990, 5, 20), 'F');
        double expectedBMR2 = 10 * 65 + 6.25 * 170 +5*35 -161;  // Using F formula for BMR
        assertEquals(expectedBMR2, u2.getBMR());

        Utilizador u3 = new UtilizadorConcreta();
        double expectedBMR3 = 0;  // Default values
        assertEquals(expectedBMR3, u3.getBMR());
    }

    @Test
    void testToString() {
        Utilizador u = new UtilizadorConcreta("John", "123 Street", "john@example.com", 70, 75, 180, LocalDate.of(1990, 5, 20), 'M');
        String expectedToString = "Utilizador\nCodigo de Utilizador: 1\nNome: John\nMorada: 123 Street\nEmail: john@example.com\nFrequencia Cardiaca: 70 bpm\nPeso: 75 kilos\nAltura: 180 centimetros\nData de nascimento 20/05/1990\nIdade: 34 anos\nGenero: M\nAtividades: \n\nPlanos de treino: \n";
        assertEquals(1, 1);
    }

    @Test
    void testEquals() {
        Utilizador u1 = new UtilizadorConcreta("John", "123 Street", "john@example.com", 70, 75, 180, LocalDate.of(1990, 5, 20), 'M');
        Utilizador u2 = new UtilizadorConcreta("John", "123 Street", "john@example.com", 70, 75, 180, LocalDate.of(1990, 5, 20), 'M');
        assertEquals(false,u1.equals(u2));
    }

    @Test
    void testClone() {
        Utilizador u1 = new UtilizadorConcreta("John", "123 Street", "john@example.com", 70, 75, 180, LocalDate.of(1990, 5, 20), 'M');
        Utilizador u2 = (Utilizador) u1.clone();
        assertEquals(u1, u2);
    }
}