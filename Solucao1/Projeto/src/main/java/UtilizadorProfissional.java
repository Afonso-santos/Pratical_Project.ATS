package Projeto;
import java.time.LocalDate;

/**
 * Classe UtilizadorProfissional - classe que engloba todos os utilizadores que sao atletas profissionais.
 *
 * @author Grupo10
 * @version 09/05/24
 * Notas Versão : --
 */
public class UtilizadorProfissional extends Utilizador
{
    private static final double fatorMultiplicativo = 1.5;
    
    /**
     * Construtor vazio
     */
    public UtilizadorProfissional()
    {
        super();
    }
    
    /**
     * Construtor parametrizado
     */
    public UtilizadorProfissional(String nome, String morada, String email, int freqCardiaca, int peso, int altura, LocalDate dataNascimento, char genero)
    {
        super(nome,morada,email,freqCardiaca,peso,altura,dataNascimento,genero);
    }
    
    /**
     * Construtor de cópia
     */
    public UtilizadorProfissional(UtilizadorProfissional u) {
        super(u);
    }
    
    public UtilizadorProfissional(UtilizadorProfissional u, LocalDate i, LocalDate f) {
        super(u,i,f);
    }
    
    /**
     * Método que calcula o fator multiplicativo de um utilizador profissional
     *
     * @param  
     * @return    fatorMultiplicativo
     */
    public double getFatorMultiplicativo()
    {
        return UtilizadorProfissional.fatorMultiplicativo;
    }
    
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append("\nTipo de Utilizador: Profissional\n");
        return (sb.toString());
    }
    
    /**
     * Método equals
     */
    public boolean equals(Object o) {
        if (this==o) return true;
        if ((o==null) || (this.getClass() != o.getClass())) return false;
        UtilizadorProfissional b = (UtilizadorProfissional) o;
        return (super.equals(b));
    }
    
    /**
     * Método clone
     */
    public Object clone(){
        UtilizadorProfissional t = new UtilizadorProfissional(this);
        return t;
    }
    
    public Object utilizadorNumPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        UtilizadorProfissional t = new UtilizadorProfissional (this, dataInicio, dataFim);
        return t;
    }
}
