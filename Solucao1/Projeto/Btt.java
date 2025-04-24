package Projeto;
import java.time.*;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Classe Btt
 *
 * @author Grupo10
 * @version 09/05/24
 * Notas versão : --
 */
public class Btt extends AtivDistAltimetria implements Hard
{
    // variáveis de classe
    private static final double MET = 10;
    /**
     * Construtores de Btt
     */
    
    /**
     * Construtor vazio
     */
    public Btt()
    {
        super();
    }
    
    /**
     * Construtor parametrizado
     */
    public Btt(LocalDateTime realizacao, LocalTime tempo, int freqCardiaca, double distancia, double altimetria)
    {
        super(realizacao, tempo, freqCardiaca, distancia, altimetria);
    }
    
    /**
     * Construtor de cópia
     */
    public Btt(Btt btt)
    {
        super(btt);
    }
    
    public double getFatorHard() {
        double hard = 1.05;
        if (this.getAltimetria() > 1000) {
            hard+=0.10;
            if (this.getAltimetria() > 2000) hard+=0.10;
        }
        return hard;
    }
    
    /**
     * Método que calcula o consumo de calorias de um treino de BTT
     *
     * @param  utilizador  utilizador que realiza o treino
     * @return    consumo de calorias do treino
     */
    public double consumoCalorias(Utilizador utilizador){
        double consumoCalorias = Btt.MET * (utilizador.getFatorMultiplicativo() + this.getFatorVelocidade(10.5, 0.11) + this.getFatorFreqCardiaca(utilizador) + this.getFatorAltimetria()) 
                                         * utilizador.getBMR() / (24 * 60 * 60) * this.getFatorHard()
                                         * this.getTempo().toSecondOfDay();
        return consumoCalorias;
    }

    public Atividade geraAtividade(Utilizador utilizador, double consumoCalorias){
        Atividade a = new Btt();
        Predicate<Atividade> p = at -> at instanceof BicepCurls;
        Function<Atividade,Double> f = at -> ((Btt)at).getAltimetria();
        double maxAltimetria = utilizador.infoDasAtividadesNumPeriodoQueRespeitamP(LocalDate.MIN, LocalDate.MAX, p, f)
                                         .stream().reduce((p1,p2) -> p1 > p2 ? p1 : p2).orElse(0.0);
        double altimetria = maxAltimetria * 0.8;
        double fatorAltimetria = altimetria * 0.0005;
        double tempoDouble = consumoCalorias / (Btt.MET * (utilizador.getBMR() / (24 * 60 * 60)) * (utilizador.getFatorMultiplicativo() + fatorAltimetria));
        int tempo = (int) tempoDouble;
        double distancia = tempo * 10.5;
        LocalTime t = LocalTime.MIN.plusSeconds(tempo);
        a.setTempo(t);
        a.setFreqCardiaca(0);
        ((AtivDistancia)a).setDistancia(distancia);
        ((AtivDistAltimetria)a).setAltimetria(altimetria);
        return a;
    }

    /**
     * Método toString
     */
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append("\nTipo de atividade: BTT\n");
        return (sb.toString());
    }
    
    /**
     * Método equals
     */
    public boolean equals(Object o) {
        if (this==o) return true;
        if ((o==null) || (this.getClass() != o.getClass())) return false;
        Btt b = (Btt) o;
        return (super.equals(b));
    }
    
    /**
     * Método clone
     */
    public Object clone(){
        Btt b = new Btt(this);
        return b;
    }
}
