package Projeto;
import java.time.*;


/**
 * Classe AtivRepeticoes - classe abstrata que engloba as atividades em que são feitas repetições de exercícios
 *
 * @author Grupo10
 * @version 09/05/24
 * Notas versão : --
 */
public abstract class AtivRepeticoes extends Atividade
{
    // variáveis de instância
    private int repeticoes;

    /**
     * Construtores de AtivRepeticoes
     * 
     * AtivRepeticoes é uma classe abstrata, logo, os construtores só vão ser invocados pelas suas sub-classes nos seus próprios construtores, e não para criar instâncias de AtivRepeticoes
     */

    /**
     * Construtor vazio
     */
    public AtivRepeticoes()
    {
        super();
        this.repeticoes = 0;
    }

    /**
     * Construtor parametrizado
     */
    public AtivRepeticoes(LocalDateTime realizacao, LocalTime tempo, int freqCardiaca, int repeticoes)
    {
        super(realizacao, tempo, freqCardiaca);
        this.repeticoes = repeticoes;
    }

    /**
     * Construtor de cópia
     */
    public AtivRepeticoes(AtivRepeticoes umaAtivRepeticoes)
    {
        super(umaAtivRepeticoes);
        this.repeticoes = umaAtivRepeticoes.getRepeticoes();
    }

    //Getters e setters
    
    public int getRepeticoes() {
        return this.repeticoes;
    }

    public void setRepeticoes(int repeticoes) {
        this.repeticoes = repeticoes;
    }

    /**
     * Método que calcula o fator de intensidade pela quantidade de repetições durante a atividade
     * 
     * 
     * @param valorNulo repetições/segundo para a qual o fator deve ser nulo
     * @param valorIncremento valor no qual uma unidade de repetições/segundo aumenta o fator
     * @return fator de intensidade da velocidade a que é realizada a atividade
     */
    public double getFatorRepeticoes(double valorNulo, double valorIncremento){
        double repeticoesPorSegundo = this.repeticoes / this.getTempo().toSecondOfDay();
        return (repeticoesPorSegundo - valorNulo) * valorIncremento;
    }

    /**
     * Método equals
     */
    public boolean equals(Object o) {
        if (this==o) return true;
        if ((o==null) || (this.getClass() != o.getClass())) return false;
        AtivRepeticoes a = (AtivRepeticoes) o;
        return ((super.equals(a)) && (this.repeticoes == a.getRepeticoes()));
    }
    
    /**
     * Método toString, deve ser implementado pelas sub-classes
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append("\nRepetiçoes: ");
        sb.append(this.repeticoes);
        return (sb.toString());
    }
}
