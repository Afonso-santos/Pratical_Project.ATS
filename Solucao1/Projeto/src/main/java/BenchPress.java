package Projeto;
import java.time.*;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Classe BenchPress
 *
 * @author Grupo10
 * @version 09/05/24
 * Notas versão : --
 */
public class BenchPress extends AtivRepsPeso
{
    // variáveis de classe
    private static final double MET = 4;
    /**
     * Construtores de BenchPress
     */
    
    /**
     * Construtor vazio
     */
    public BenchPress()
    {
        super();
    }
    
    /**
     * Construtor parametrizado
     */
    public BenchPress(LocalDateTime realizacao, LocalTime tempo, int freqCardiaca, int repeticoes, double peso)
    {
        super(realizacao, tempo, freqCardiaca, repeticoes, peso);
    }
    
    /**
     * Construtor de cópia
     */
    public BenchPress(BenchPress benchPress)
    {
        super(benchPress);
    }
    
    /**
     * Método que calcula o consumo de calorias de uma série de bench press
     *
     * @param  utilizador  utilizador que realiza o treino
     * @return    consumo de calorias do treino
     */
    public double consumoCalorias(Utilizador utilizador){
        double consumoCalorias = BenchPress.MET * (utilizador.getFatorMultiplicativo() + this.getFatorRepeticoes(0.25, 0.2) + this.getFatorPeso(utilizador, 0.5, 0.2) + this.getFatorFreqCardiaca(utilizador)) 
                                                * utilizador.getBMR() / (24 * 60 * 60)
                                                * this.getTempo().toSecondOfDay();
        return consumoCalorias;
    }

    public Atividade geraAtividade(Utilizador utilizador, double consumoCalorias){
        Atividade a = new BenchPress();
        Predicate<Atividade> p = at -> at instanceof BenchPress;
        Function<Atividade,Double> f = at -> ((BenchPress)at).getPeso();
        double maxPeso = utilizador.infoDasAtividadesNumPeriodoQueRespeitamP(LocalDate.MIN, LocalDate.MAX, p, f)
                                   .stream().reduce((p1,p2) -> p1 > p2 ? p1 : p2).orElse(0.0);
        double peso = maxPeso == 0 ? utilizador.getPeso() : maxPeso * 0.8;
        maxPeso *= 0.8;
        maxPeso /= utilizador.getPeso();
        double fatorPeso = (maxPeso - 0.5) * 0.2;
        if (fatorPeso < 0) fatorPeso = 0;
        double tempoDouble = consumoCalorias / (BenchPress.MET * (utilizador.getBMR() / (24 * 60 * 60)) * (utilizador.getFatorMultiplicativo() + fatorPeso));
        int tempo = (int) tempoDouble;
        double reps = tempo * 0.25;
        LocalTime t = LocalTime.MIN.plusSeconds(tempo);
        a.setTempo(t);
        a.setFreqCardiaca(0);
        ((AtivRepeticoes)a).setRepeticoes((int)reps);
        ((AtivRepsPeso)a).setPeso(peso);
        return a;
    }

    /**
     * Método toString
     */
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append("\nTipo de atividade: Bench press\n");
        return (sb.toString());
    }
    
    /**
     * Método equals
     */
    public boolean equals(Object o) {
        if (this==o) return true;
        if ((o==null) || (this.getClass() != o.getClass())) return false;
        BenchPress b = (BenchPress) o;
        return (super.equals(b));
    }
    
    /**
     * Método clone
     */
    public Object clone(){
        BenchPress b = new BenchPress(this);
        return b;
    }
}
