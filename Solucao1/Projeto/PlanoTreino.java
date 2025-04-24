package Projeto;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.io.*;

/**
 * Classe PlanoTreino, que engloba as várias atividades realizadas por um utilizador numa dada altura
 * 
 * @author Grupo10
 * @version 09/05/24
 * Notas versão : Falta adicionar atividades
 */
public class PlanoTreino implements Comparable<PlanoTreino>, Serializable
{   
    // variáveis de classe
    private static int proximoCodigo = 1;
    // variáveis de instância
    private int codPlano;
    private LocalDate dataRealizacao;
    private List<AtividadeIteracoes> atividades;

    /**
     * Classe AtividadeIteracoes, classe auxiliar para armazenar iterações de atividades
     */
    class AtividadeIteracoes {
        //variáveis de instância
        private int iteracoes;
        private Atividade atividade;
    
        /**
         * Construtor parameterizado
         */
        public AtividadeIteracoes(int iteracoes, Atividade atividade){
            this.iteracoes = iteracoes;
            this.atividade = atividade;
        }
        
        public AtividadeIteracoes(AtividadeIteracoes a) {
            this.iteracoes = a.getIteracoes();
            this.atividade = a.getAtividade();
        }
    
        public int getIteracoes(){
            return this.iteracoes;
        }
    
        public Atividade getAtividade(){
            return (Atividade) this.atividade.clone();
        }
    
        public void setIteracoes(int iteracoes){
            this.iteracoes = iteracoes;
        }
    
        public void setAtividade(Atividade atividade){
            this.atividade = (Atividade)atividade.clone();
        }
        
        /**
         * clone
         */
        public Object clone() {
            AtividadeIteracoes c = new AtividadeIteracoes(this);
            return c;
        }
        
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(this.atividade);
            sb.append("Iteracoes: ");
            sb.append(this.iteracoes);
            sb.append("\n");
            return (sb.toString());
        }
        
        /**
         * Método equals
         */
        public boolean equals(Object o){
            if (this==o) return true;
            if ((o==null)||(this.getClass()!=o.getClass())) return false;
            AtividadeIteracoes a = (AtividadeIteracoes) o;
            return (this.getIteracoes()==a.getIteracoes()&&this.getAtividade().equals(a.getAtividade()));
        }
    }

    /**
     * Construtor vazio
     */
    public PlanoTreino()
    {
        this.codPlano = 0;
        this.dataRealizacao = LocalDate.now();
        this.atividades = new ArrayList<>();
    }

    /**
     * Construtor parameterizado
     */
    public PlanoTreino(LocalDate dataRealizacao)
    {
        this.codPlano = PlanoTreino.proximoCodigo++;
        this.dataRealizacao = dataRealizacao;
        this.atividades = new ArrayList<>();
    }

    /**
     * Construtor de cópia
     */
    public PlanoTreino(PlanoTreino planoTreino)
    {
        this.codPlano = planoTreino.getCodPlano();
        this.dataRealizacao = planoTreino.getDataRealizacao();
        this.atividades = planoTreino.getAtividades();
    }
    
    public PlanoTreino(PlanoTreino planoTreino, LocalDate inicio, LocalDate fim) {
        this.codPlano = planoTreino.getCodPlano();
        this.dataRealizacao = planoTreino.getDataRealizacao();
        this.atividades = planoTreino.getAtividadesNumPeriodo(inicio,fim);
    }
    
    public void setProximoCodigo(int proximoCodigo) {
        PlanoTreino.proximoCodigo = proximoCodigo;
    }
    // Getters e setters
    public List<AtividadeIteracoes> getAtividadesNumPeriodo(LocalDate inicio, LocalDate fim) {
        LocalDateTime i2 = LocalDateTime.of(inicio, LocalTime.MIDNIGHT);
        LocalDateTime f2 = LocalDateTime.of(fim, LocalTime.MIDNIGHT);
        List<AtividadeIteracoes> atividades = new ArrayList<AtividadeIteracoes>();
        for(AtividadeIteracoes a : this.atividades) {
            if (a.getAtividade().getDataRealizacao().compareTo(i2) >= 0 && a.getAtividade().getDataRealizacao().compareTo(f2) <= 0) atividades.add((AtividadeIteracoes) a.clone());
        }
        return atividades;
    }
    public int getCodPlano(){
        return this.codPlano;
    }
    
    public List<AtividadeIteracoes> getAtividades() {
        List<AtividadeIteracoes> atividades = new ArrayList<AtividadeIteracoes>();
        for(AtividadeIteracoes a : this.atividades) {
            atividades.add((AtividadeIteracoes) a.clone());
        }
        return atividades;
    }
    
    public LocalDate getDataRealizacao(){
        return this.dataRealizacao;
    }

    public void setDataRealizacao(LocalDate dataRealizacao){
        this.dataRealizacao = dataRealizacao;
    }

    public List<Atividade> atividadesQueRespeitamP(LocalDate ii, LocalDate f, Predicate<Atividade> p) {
        LocalDateTime i2 = LocalDateTime.of(ii, LocalTime.MIDNIGHT);
        LocalDateTime f2 = LocalDateTime.of(f, LocalTime.MIDNIGHT);
        List<Atividade> atividades = new ArrayList<Atividade>();
        for (AtividadeIteracoes atividadeIter : this.atividades) {
            int iteracoes = atividadeIter.getIteracoes();
            for (int i = 0; i < iteracoes; i++) {
                Atividade atividade = atividadeIter.getAtividade();
                if (p.test(atividade) && atividade.getDataRealizacao().compareTo(i2) >= 0 && atividade.getDataRealizacao().compareTo(f2) <= 0) atividades.add((Atividade)atividade.clone());
            }
        }
        return atividades;
    }
    
    /**
     * Método caloriasDispendidas que calcula o total de calorias dispendidas num plano de treino
     * 
     * @param utilizador utilizador que realiza o plano de treino
     * @return calorias dispendidas durante o plano de treino
     */
    public double caloriasDispendidas(Utilizador utilizador){
        double calorias = 0;
        for (AtividadeIteracoes a : this.atividades) {
            calorias += a.getIteracoes() * a.getAtividade().consumoCalorias(utilizador);
        }
        return calorias;
    }
    
    public void addAtividade(Atividade atividade, int iteracoes){
        AtividadeIteracoes adicionar = new AtividadeIteracoes(iteracoes,(Atividade) atividade.clone());
        this.atividades.add(adicionar);
    }

    public List<PlanoTreino> geraPlanoTreino(Utilizador utilizador, List<Atividade> atividades, int maxAtivDia, int ativPorSemana, double consumoCaloricoMinimo, LocalDate inicio){
        List<PlanoTreino> planos = new ArrayList<PlanoTreino>();
        int i = 0, j = 0, size = atividades.size();
        int salto = 2;
        List<Atividade> ativsHard = atividades.stream().filter(a -> a instanceof Hard).collect(Collectors.toList());
        List<Atividade> ativsNormal = atividades.stream().filter(a -> !(a instanceof Hard)).collect(Collectors.toList());
        int hard = ativsHard.size();
        int sizeHard = hard, sizeNormal = ativsNormal.size();
        int[] semana = new int[]{0, 0, 0, 0, 0, 0, 0}; // array auxiliar para gerir dias em que há atividades
        if (ativPorSemana % 2 == 0) i = 0;
        else i = 1;
        if (hard > 3) hard = 3; // não pode haver mais que 3 atividades hard por semana
        if (hard < 3) salto = 3;
        if (hard == 1) i = ativPorSemana%2 == 0 ? 4 : 6;
        //atividades hard
        while (hard > 0 && ativPorSemana > 0) {
            semana[i] = -1;
            i+=salto;
            hard--;
            ativPorSemana--;
        }
        i = 0;
        //atividades normais
        while (ativPorSemana > 0) {
            if (semana[i] >= 0 && semana[i] < maxAtivDia) {
                semana[i]++;
                ativPorSemana--;
            }
            i=(i+1)%7;
        }
        LocalDate dia = inicio;
        double caloriasCadaAtividade = consumoCaloricoMinimo/ativPorSemana;
        hard = 0;
        Atividade a;
        int iteracoes = 1;
        for (i=0; i<7; i++) {
            //adiciona atividades normais
            if (semana[i] > 0) {
                PlanoTreino p = new PlanoTreino(dia.plusDays(i));
                while (semana[i] > 0) {
                    a = ativsNormal.get(j);
                    if (a instanceof AtivRepeticoes)
                        iteracoes = ((i % 5) * sizeNormal * (j + 1)) % 10;
                    a.geraAtividade(utilizador, caloriasCadaAtividade/iteracoes);
                    p.addAtividade(a, iteracoes);
                    iteracoes = 1;
                    j=(j+1)%sizeNormal;
                    semana[i]--;
                }
                planos.add(p);
            }
            //adiciona atividades hard
            if (semana[i] < 0) {
                PlanoTreino p = new PlanoTreino(dia.plusDays(i));
                a = ativsHard.get(hard);
                a.geraAtividade(utilizador, caloriasCadaAtividade);
                p.addAtividade(a, 1);
                hard++;
                semana[i] = 0;
                planos.add(p);
            }
            dia = inicio;
        }
        return planos;
    }
    
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        StringBuilder sb = new StringBuilder();
        sb.append("Plano de Treino\nCodigo de Plano de Treino: ");
        sb.append(this.codPlano);
        sb.append("\nData de realizaçao: ");
        sb.append(this.dataRealizacao);
        sb.append("\nAtividades e suas iteraçoes: \n");
        for (AtividadeIteracoes a : this.atividades){
            sb.append(a.toString());
        }
        return (sb.toString());
    }
    
    /**
     * Método compareTo
     */
    public int compareTo(PlanoTreino p){
        int res = this.dataRealizacao.compareTo(p.getDataRealizacao());
        if (res == 0) res = this.codPlano - p.getCodPlano();
        return res;
    }
    
    /**
     * clone
     */
    public Object clone() {
        PlanoTreino c = new PlanoTreino(this);
        return c;
    }
    
    public Object planoTreinoNumPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        PlanoTreino t = new PlanoTreino (this, dataInicio, dataFim);
        return t;
    }
}
