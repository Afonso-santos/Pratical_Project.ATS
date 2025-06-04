package Projeto;
import java.time.*;


/**
 * Classe AtivRepsPeso - classe abstrata que engloba as atividades em que são feitas repetições de exercícios com pesos
 *
 * @author Grupo10
 * @version 09/05/24
 * Notas versão : --
 */
public abstract class AtivRepsPeso extends AtivRepeticoes
{
    // variáveis de instância
    private double peso;

    /**
     * Construtores de AtivRepsPeso
     * 
     * AtivRepsPeso é uma classe abstrata, logo, os construtores só vão ser invocados pelas suas sub-classes nos seus próprios construtores, e não para criar instâncias de AtivRepsPeso
     */

    /**
     * Construtor vazio
     */
    public AtivRepsPeso()
    {
        super();
        this.peso = 0.0;
    }

    /**
     * Construtor parametrizado
     */
    public AtivRepsPeso(LocalDateTime realizacao, LocalTime tempo, int freqCardiaca, int repeticoes, double peso)
    {
        super(realizacao, tempo, freqCardiaca, repeticoes);
        this.peso = peso;
    }

    /**
     * Construtor de cópia
     */
    public AtivRepsPeso(AtivRepsPeso umaAtivRepsPeso)
    {
        super(umaAtivRepsPeso);
        this.peso = umaAtivRepsPeso.getPeso();
    }
    
    //Getters e setters

    public double getPeso() {
        return this.peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    /**
     * Método que calcula o fator de intensidade pelo peso utilizado na atividade
     * 
     * 
     * @param utilizador utilizador que realiza a atividade
     * @param valorNulo razão de peso para a qual o fator deve ser nulo
     * @param valorIncremento valor no qual uma unidade da razão aumenta o fator
     * @return fator de intensidade da velocidade a que é realizada a atividade
     */
    public double getFatorPeso(Utilizador utilizador, double valorNulo, double valorIncremento){
        double razaoPeso = this.peso / utilizador.getPeso();
        return (razaoPeso - valorNulo) * valorIncremento;
    }

    /**
     * Método equals
     */
    public boolean equals(Object o) {
        if (this==o) return true;
        if ((o==null) || (this.getClass() != o.getClass())) return false;
        AtivRepsPeso a = (AtivRepsPeso) o;
        return ((super.equals(a)) && (this.peso == a.getPeso()));
    }
    
    /**
     * Método toString, deve ser implementado pelas sub-classes
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append("\nPeso: ");
        sb.append(this.peso);
        sb.append(" kilos");
        return (sb.toString());
    }
}
