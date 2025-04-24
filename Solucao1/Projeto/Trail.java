package Projeto;
import java.time.*;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Classe Trail
 *
 * @author Grupo10
 * @version 09/05/24
 * Notas versão : --
 */
public class Trail extends AtivDistAltimetria implements Hard
{
    // variáveis de classe
    private static final double MET = 10;
    /**
     * Construtores de Trail
     */
    
    /**
     * Construtor vazio
     */
    public Trail()
    {
        super();
    }
    
    /**
     * Construtor parametrizado
     */
    public Trail(LocalDateTime realizacao, LocalTime tempo, int freqCardiaca, double distancia, double altimetria)
    {
        super(realizacao, tempo, freqCardiaca, distancia, altimetria);
    }
    
    /**
     * Construtor de cópia
     */
    public Trail(Trail umTrail)
    {
        super(umTrail);
    }
    
    public double getFatorHard() {
        double hard = 1.15;
        if (this.getAltimetria() > 1000) {
            hard+=0.10;
            if (this.getAltimetria() > 2000) hard+=0.10;
        }
        return hard;
    }
    
    /**
     * Método que calcula o consumo de calorias de um trail
     *
     * @param  utilizador  utilizador que realiza o trail
     * @return    consumo de calorias do trail
     */
    public double consumoCalorias(Utilizador utilizador){
        double consumoCalorias = Trail.MET * (utilizador.getFatorMultiplicativo() + this.getFatorVelocidade(2.2, 0.22) + this.getFatorFreqCardiaca(utilizador) + this.getFatorAltimetria()) 
                                           * utilizador.getBMR() / (24 * 60 * 60) * this.getFatorHard()
                                           * this.getTempo().toSecondOfDay();
        return consumoCalorias;
    }

    public Atividade geraAtividade(Utilizador utilizador, double consumoCalorias){
        Atividade a = new Trail();
        Predicate<Atividade> p = at -> at instanceof BicepCurls;
        Function<Atividade,Double> f = at -> ((Trail)at).getAltimetria();
        double maxAltimetria = utilizador.infoDasAtividadesNumPeriodoQueRespeitamP(LocalDate.MIN, LocalDate.MAX, p, f)
                                         .stream().reduce((p1,p2) -> p1 > p2 ? p1 : p2).orElse(0.0);
        double altimetria = maxAltimetria * 0.8;
        double fatorAltimetria = altimetria * 0.0005;
        double tempoDouble = consumoCalorias / (Trail.MET * (utilizador.getBMR() / (24 * 60 * 60)) * (utilizador.getFatorMultiplicativo() + fatorAltimetria));
        int tempo = (int) tempoDouble;
        double distancia = tempo * 2.2;
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
        sb.append("\nTipo de atividade: Trail\n");
        return (sb.toString());
    }

    /**
     * Método equals
     */
    public boolean equals(Object o) {
        if (this==o) return true;
        if ((o==null) || (this.getClass() != o.getClass())) return false;
        Trail t = (Trail) o;
        return (super.equals(t));
    }
    
    /**
     * Método clone
     */
    public Object clone(){
        Trail t = new Trail(this);
        return t;
    }
}
