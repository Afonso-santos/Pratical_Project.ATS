package Projeto;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.io.*;

/**
 * Classe Atividade - classe abstrata que engloba os vários tipos de atividades desportivas
 *
 * @author Grupo10
 * @version 09/05/24
 * Notas versão : --
 */
public abstract class Atividade implements Comparable<Atividade>, Serializable
{
    //variáveis de calsse
    private static int proximoCodAtividade = 1;
    // variáveis de instância
    private int codAtividade;
    private LocalDateTime dataRealizacao; 
    private LocalTime tempo;
    private int freqCardiaca;

    /**
     * Construtores de Atividade
     *
     * Atividade é uma classe abstrata, logo, os construtores só vão ser invocados pelas suas sub-classes nos seus próprios construtores, e não para criar instâncias de Atividade
     */
    
    /**
     * Construtor vazio
     */
    public Atividade()
    {
        this.codAtividade = Atividade.proximoCodAtividade++;
        this.dataRealizacao = LocalDateTime.now();
        this.tempo = LocalTime.of(0,0);
        this.freqCardiaca = 0;
    }
    
    /**
     * Construtor parametrizado
     */
    public Atividade(LocalDateTime realizacao, LocalTime tempo, int freqCardiaca)
    {
        this.codAtividade = Atividade.proximoCodAtividade++;
        this.dataRealizacao = realizacao;
        this.tempo = tempo;
        this.freqCardiaca = freqCardiaca;
    }
    
    /**
     * Construtor de cópia
     */
    public Atividade(Atividade umaAtividade)
    {
        this.codAtividade = umaAtividade.getCodAtividade();
        this.dataRealizacao = umaAtividade.getDataRealizacao();
        this.tempo = umaAtividade.getTempo();
        this.freqCardiaca = umaAtividade.getFreqCardiaca();
    }
    
    public void setProximoCodigo(int proximoCodigo) {
        Atividade.proximoCodAtividade = proximoCodigo;
    }
    // Getters e setters

    public int getCodAtividade(){
        return this.codAtividade;
    }

    public LocalDateTime getDataRealizacao(){
        return this.dataRealizacao;
    }

    public LocalTime getTempo(){
        return this.tempo;
    }

    public int getFreqCardiaca(){
        return this.freqCardiaca;
    }
    
    public void setDataRealizacao(LocalDateTime dataRealizacao){
        this.dataRealizacao = dataRealizacao;
    }

    public void setTempo(LocalTime tempo){
        this.tempo = tempo;
    }
    
    public void setFreqCardiaca(int freqCardiaca){
        this.freqCardiaca = freqCardiaca;
    }

    /**
     * Método que calcula o consumo de calorias de uma atividade, da maneira definida para essa atividade - deve ser implementado pelas sub-classes
     *
     * @param  utilizador  utilizador que realiza a atividade
     * @return    consumo de calorias da atividade
     */
    public abstract double consumoCalorias(Utilizador utilizador);

    /**
     * Método que calcula um fator de intensidade pela razão entre a frequência cardíaca a que é feita a atividade e a frequência cardíaca normal
     * 
     * @param utilizador utilizador que realiza a atividade
     * @return fator de intensidade da frequência cardíaca realizada durante a atividade
     */
    public double getFatorFreqCardiaca(Utilizador utilizador){
        double razaoFreqCardiaca = utilizador.getFreqCardiaca() / this.freqCardiaca;
        return (razaoFreqCardiaca - 2) * 0.4; //cada unidade da razão aumenta o fator em 0.4
    }

    public abstract Atividade geraAtividade(Utilizador utilizador, double consumoCalorias);

    /**
     * Método toString, deve ser implementado pelas sub-classes
     */
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        StringBuilder sb = new StringBuilder();
        sb.append("Atividade\nId: ");
        sb.append(this.codAtividade);
        sb.append("\nData e hora: ");
        sb.append(this.dataRealizacao.format(formatter));
        sb.append("\nDuraçao: ");
        sb.append(this.tempo);
        sb.append("\nFrequencia Cardiaca: ");
        sb.append(this.freqCardiaca);
        sb.append(" bpm");
        return (sb.toString());
    }

    /**
     * Método equals
     */
    public boolean equals(Object o){
        if (this==o) return true;
        if ((o==null)||(this.getClass()!=o.getClass())) return false;
        Atividade a = (Atividade) o;
        return (((this.tempo).equals(a.getTempo()))&&(this.freqCardiaca ==a.getFreqCardiaca()) && ((this.dataRealizacao).equals(a.getDataRealizacao())));
    }

    /**
     * Método clone, deve ser implementado pelas sub-classes
     */
    public abstract Object clone();

    /**
     * Método compareTo
     */
    public int compareTo(Atividade a){
        int res = this.dataRealizacao.compareTo(a.getDataRealizacao());
        if (res == 0) res = this.codAtividade - a.getCodAtividade();
        return res;
    }
}
