package Projeto;
import java.time.*;


/**
 * Classe AtivDistAltimetria - classe abstrata que engloba as atividades em que é preciso saber a distância percorrida e a altimetria
 *
 * @author Grupo10
 * @version 09/05/24
 * Notas versão : --
 */
public abstract class AtivDistAltimetria extends AtivDistancia
{
    // variáveis de instância
    private double altimetria;

    /**
     * Construtores de AtivDistAltimetria
     * 
     * AtivDistAltimetria é uma classe abstrata, logo, os construtores só vão ser invocados pelas suas sub-classes nos seus próprios construtores, e não para criar instâncias de AtivDistAltimetria
     */

    /**
     * Construtor vazio
     */
    public AtivDistAltimetria()
    {
        super();
        this.altimetria = 0.0;
    }

    /**
     * Construtor parametrizado
     */
    public AtivDistAltimetria(LocalDateTime realizacao, LocalTime tempo, int freqCardiaca, double distancia, double altimetria)
    {
        super(realizacao, tempo, freqCardiaca, distancia);
        this.altimetria = altimetria;
    }

    /**
     * Construtor de cópia
     */
    public AtivDistAltimetria(AtivDistAltimetria umaAtivDistAltimetria)
    {
        super(umaAtivDistAltimetria);
        this.altimetria = umaAtivDistAltimetria.getAltimetria();
    }
    
    //Getters e setters

    public double getAltimetria() {
        return this.altimetria;
    }

    public void setAltimetria(double altimetria) {
        this.altimetria = altimetria;
    }

    /**
     * Método que calcula o fator de intensidade pela altimetria da atividade
     * 
     * 
     * @return fator de intensidade da altimetria a que é realizada a atividade
     */
    public double getFatorAltimetria(){
        return this.altimetria * 0.0005;
    }

    /**
     * Método equals
     */
    public boolean equals(Object o) {
        if (this==o) return true;
        if ((o==null) || (this.getClass() != o.getClass())) return false;
        AtivDistAltimetria a = (AtivDistAltimetria) o;
        return ((super.equals(a)) && (this.altimetria == a.getAltimetria()));
    }
    
    /**
     * Método toString, deve ser implementado pelas sub-classes
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append("\nAltimetria: ");
        sb.append(this.altimetria);
        sb.append(" metros");
        return (sb.toString());
    }
}
