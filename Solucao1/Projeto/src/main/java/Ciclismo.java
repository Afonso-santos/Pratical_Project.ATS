package Projeto;
import java.time.*;

/**
 * Classe Ciclismo
 *
 * @author Grupo10
 * @version 09/05/24
 * Notas versão : --
 */
public class Ciclismo extends AtivDistancia
{
    // variáveis de classe
    private static final double MET = 6;
    /**
     * Construtores de Ciclismo
     */
    
    /**
     * Construtor vazio
     */
    public Ciclismo()
    {
        super();
    }
    
    /**
     * Construtor parametrizado
     */
    public Ciclismo(LocalDateTime realizacao, LocalTime tempo, int freqCardiaca, double distancia)
    {
        super(realizacao, tempo, freqCardiaca, distancia);
    }
    
    /**
     * Construtor de cópia
     */
    public Ciclismo(Ciclismo ciclismo)
    {
        super(ciclismo);
    }
    
    /**
     * Método que calcula o consumo de calorias de um treino de ciclismo
     *
     * @param  utilizador  utilizador que realiza o treino
     * @return    consumo de calorias do treino
     */
    public double consumoCalorias(Utilizador utilizador){
        double consumoCalorias = Ciclismo.MET * (utilizador.getFatorMultiplicativo() + this.getFatorVelocidade(10.5, 0.11) + this.getFatorFreqCardiaca(utilizador)) 
                                              * utilizador.getBMR() / (24 * 60 * 60)
                                              * this.getTempo().toSecondOfDay();
            return consumoCalorias;
    }

    public Atividade geraAtividade(Utilizador utilizador, double consumoCalorias){
        Atividade a = new Ciclismo();
        double tempoDouble = consumoCalorias / (Ciclismo.MET * (utilizador.getBMR() / (24 * 60 * 60)) * utilizador.getFatorMultiplicativo());
        int tempo = (int) tempoDouble;
        double distancia = tempo * 10.5;
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
        sb.append("\nTipo de atividade: Ciclismo\n");
        return (sb.toString());
    }

    /**
     * Método equals
     */
    public boolean equals(Object o){
        if (this==o) return true;
        if ((o==null)||(this.getClass()!=o.getClass())) return false;
        Ciclismo c = (Ciclismo) o;
        return (super.equals(c));
    }
    
    /**
     * Método clone
     */
    public Object clone(){
        Ciclismo c = new Ciclismo(this);
        return c;
    }
}
