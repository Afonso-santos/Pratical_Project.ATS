package Projeto;
import java.time.LocalDate;

/**
 * Classe UtilizadorAmador - classe que engloba todos os utilizadores que sao atletas amadores.
 *
 * @author Grupo10
 * @version 09/05/24
 * Notas Versão : --
 */
public class UtilizadorAmador extends Utilizador
{
    private static final double fatorMultiplicativo = 1;
    
    /**
     * Construtor vazio
     */
    public UtilizadorAmador()
    {
        super();
    }
    
    /**
     * Construtor parametrizado
     */
    public UtilizadorAmador(String nome, String morada, String email, int freqCardiaca, int peso, int altura, LocalDate dataNascimento, char genero)
    {
        super(nome,morada,email,freqCardiaca,peso,altura,dataNascimento,genero);
    }
    
    /**
     * Construtor de cópia
     */
    public UtilizadorAmador(UtilizadorAmador u) {
        super(u);
    }
    
    public UtilizadorAmador(UtilizadorAmador u, LocalDate i, LocalDate f) {
        super(u,i,f);
    }
    
    /**
     * Método que calcula o fator multiplicativo de um utilizador amador
     *
     * @param
     * @return    fatorMultiplicativo
     */
    public double getFatorMultiplicativo()
    {
        return UtilizadorAmador.fatorMultiplicativo;
    }
    
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append("\nTipo de Utilizador: Amador\n");
        return (sb.toString());
    }
    
    /**
     * Método equals
     */
    public boolean equals(Object o) {
        if (this==o) return true;
        if ((o==null) || (this.getClass() != o.getClass())) return false;
        UtilizadorAmador b = (UtilizadorAmador) o;
        return (super.equals(b));
    }
    
    /**
     * Método clone
     */
    public Object clone(){
        UtilizadorAmador t = new UtilizadorAmador(this);
        return t;
    }
    
    public Object utilizadorNumPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        UtilizadorAmador t = new UtilizadorAmador (this, dataInicio, dataFim);
        return t;
    }
}
