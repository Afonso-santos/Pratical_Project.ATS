package Projeto;
import java.time.*;

/**
 * Classe Flexoes
 *
 * @author Grupo10
 * @version 09/05/24
 * Notas versão : --
 */
public class Flexoes extends AtivRepeticoes
{
    // variáveis de classe
    private static final double MET = 3.5;
    /**
     * Construtores de Flexoes
     */
    
    /**
     * Construtor vazio
     */
    public Flexoes()
    {
        super();
    }
    
    /**
     * Construtor parametrizado
     */
    public Flexoes(LocalDateTime realizacao, LocalTime tempo, int freqCardiaca, int repeticoes)
    {
        super(realizacao, tempo, freqCardiaca, repeticoes);
    }
    
    /**
     * Construtor de cópia
     */
    public Flexoes(Flexoes flexoes)
    {
        super(flexoes);
    }

    /**
     * Método que calcula o consumo de calorias de uma série de flexões
     *
     * @param  utilizador  utilizador que realiza o treino
     * @return    consumo de calorias do treino
     */
    public double consumoCalorias(Utilizador utilizador){
        double consumoCalorias = Flexoes.MET * (utilizador.getFatorMultiplicativo() + this.getFatorRepeticoes(0.5, 0.2) + this.getFatorFreqCardiaca(utilizador)) 
                                             * utilizador.getBMR() / (24 * 60 * 60)
                                             * this.getTempo().toSecondOfDay();
        return consumoCalorias;
    }

    public Atividade geraAtividade(Utilizador utilizador, double consumoCalorias){
        Atividade a = new Flexoes();
        double tempoDouble = consumoCalorias / (Flexoes.MET * (utilizador.getBMR() / (24 * 60 * 60)) * utilizador.getFatorMultiplicativo());
        int tempo = (int) tempoDouble;
        int reps = tempo * 1;
        LocalTime t = LocalTime.MIN.plusSeconds(tempo);
        a.setTempo(t);
        a.setFreqCardiaca(0);
        ((AtivRepeticoes)a).setRepeticoes(reps);
        return a;
    }

    /**
     * Método toString
     */
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append("\nTipo de atividade: Flexões\n");
        return (sb.toString());
    }
    
    /**
     * Método equals
     */
    public boolean equals(Object o) {
        if (this==o) return true;
        if ((o==null) || (this.getClass() != o.getClass())) return false;
        Flexoes f = (Flexoes) o;
        return (super.equals(f));
    }
    
    /**
     * Método clone
     */
    public Object clone(){
        Flexoes f = new Flexoes(this);
        return f;
    }
}
