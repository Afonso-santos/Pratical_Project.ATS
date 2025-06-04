package Projeto;
import java.time.*;

/**
 * Classe Corrida
 *
 * @author Grupo10
 * @version 09/05/24
 * Notas versão : --
 */
public class Corrida extends AtivDistancia
{
    // variáveis de classe
    private static final double MET = 8;
    /**
     * Construtores de Corrida
     */
    
    /**
     * Construtor vazio
     */
    public Corrida()
    {
        super();
    }
    
    /**
     * Construtor parametrizado
     */
    public Corrida(LocalDateTime realizacao, LocalTime tempo, int freqCardiaca, double distancia)
    {
        super(realizacao, tempo, freqCardiaca, distancia);
    }
    
    /**
     * Construtor de cópia
     */
    public Corrida(Corrida umaCorrida)
    {
        super(umaCorrida);
    }
    
    /**
     * Método que calcula o consumo de calorias de uma corrida
     *
     * @param  utilizador  utilizador que realiza a corrida
     * @return    consumo de calorias da corrida
     */
    public double consumoCalorias(Utilizador utilizador){
        double consumoCalorias = Corrida.MET * (utilizador.getFatorMultiplicativo() + this.getFatorVelocidade(2.2, 0.22) + this.getFatorFreqCardiaca(utilizador)) 
                                             * utilizador.getBMR() / (24 * 60 * 60)
                                             * this.getTempo().toSecondOfDay();
        return consumoCalorias;
    }

    public Atividade geraAtividade(Utilizador utilizador, double consumoCalorias){
        Atividade a = new Corrida();
        double tempoDouble = consumoCalorias / (Corrida.MET * (utilizador.getBMR() / (24 * 60 * 60)) * utilizador.getFatorMultiplicativo());
        int tempo = (int) tempoDouble;
        double distancia = tempo * 2.2;
        LocalTime t = LocalTime.MIN.plusSeconds(tempo);
        a.setTempo(t);
        a.setFreqCardiaca(0);
        ((AtivDistancia)a).setDistancia(distancia);
        return a;
    }

    /**
     * Método toString
     */
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append("\nTipo de atividade: Corrida\n");
        return (sb.toString());
    }

    /**
     * Método equals
     */
    public boolean equals(Object o){
        if (this==o) return true;
        if ((o==null)||(this.getClass()!=o.getClass())) return false;
        Corrida c = (Corrida) o;
        return (super.equals(c));
    }
    
    /**
     * Método clone
     */
    public Object clone(){
        Corrida c = new Corrida(this);
        return c;
    }
}
