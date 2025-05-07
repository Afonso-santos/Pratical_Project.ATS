package Projeto;
import java.time.LocalDate;

/**
 * Classe UtilizadorPraticanteOcasional - classe que engloba todos os utilizadores que sao praticantes ocasionais.
 *
 * @author Grupo10
 * @version 09/05/24
 * Notas Versão : --
 */
public class UtilizadorPraticanteOcasional extends Utilizador
{
    private static final double fatorMultiplicativo = 1.25;
    
    /**
     * Construtor vazio
     */
    public UtilizadorPraticanteOcasional()
    {
        super();
    }
    
    /**
     * Construtor parametrizado
     */
    public UtilizadorPraticanteOcasional(String nome, String morada, String email, int freqCardiaca, int peso, int altura, LocalDate dataNascimento, char genero)
    {
        super(nome,morada,email,freqCardiaca,peso,altura,dataNascimento,genero);
    }
    
    /**
     * Construtor de cópia
     */
    public UtilizadorPraticanteOcasional(UtilizadorPraticanteOcasional u) {
        super(u);
    }
    
    public UtilizadorPraticanteOcasional(UtilizadorPraticanteOcasional u, LocalDate i, LocalDate f) {
        super(u,i,f);
    }
    
    /**
     * Método que calcula o fator multiplicativo de um utilizador praticante ocasional
     *
     * @param
     * @return    fatorMultiplicativo
     */
    public double getFatorMultiplicativo()
    {
        return UtilizadorPraticanteOcasional.fatorMultiplicativo;
    }
    
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append("\nTipo de Utilizador: Praticante Ocasional\n");
        return (sb.toString());
    }
    
    /**
     * Método equals
     */
    public boolean equals(Object o) {
        if (this==o) return true;
        if ((o==null) || (this.getClass() != o.getClass())) return false;
        UtilizadorPraticanteOcasional b = (UtilizadorPraticanteOcasional) o;
        return (super.equals(b));
    }
    
    /**
     * Método clone
     */
    public Object clone(){
        UtilizadorPraticanteOcasional t = new UtilizadorPraticanteOcasional(this);
        return t;
    }
    
    public Object utilizadorNumPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        UtilizadorPraticanteOcasional t = new UtilizadorPraticanteOcasional(this,dataInicio,dataFim);
        return t;
    }
}
