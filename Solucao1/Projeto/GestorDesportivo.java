package Projeto;
import java.util.*;
import java.io.*;
import java.time.LocalDate;
import java.time.*;
import java.util.stream.Collectors;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Classe GestorDesportivo, que corresponde à lógica de negócio da aplicação / model
 *
 * @author Grupo10
 * @version 09/05/24
 * Notas versão :
 */
public class GestorDesportivo implements Serializable
{
    private LocalDate dataAtual;
    private Map<Integer, Utilizador> utilizadores;
    private Map<Integer, Utilizador> utilizadoresNumPeriodo;
    private Map<Integer, Atividade> atividades;
    private Map<Integer, PlanoTreino> planos;
    
    public GestorDesportivo(){
        this.dataAtual = LocalDate.now();
        this.utilizadores = new HashMap<Integer, Utilizador>();
        this.utilizadoresNumPeriodo = new HashMap<Integer, Utilizador>();
        this.atividades = new HashMap<Integer,Atividade>();
        this.planos = new HashMap<Integer,PlanoTreino>();
    }
    
    public void guardaEstado(String nome) throws IOException {
        FileOutputStream file = new FileOutputStream(nome);
        ObjectOutputStream oos = new ObjectOutputStream(file);
        oos.writeObject(this);
        oos.flush();
        oos.close();
    } 
    
    public GestorDesportivo carregaEstado(String nome) throws ClassNotFoundException, IOException {
        FileInputStream file = new FileInputStream(nome);
        ObjectInputStream ois = new ObjectInputStream(file);
        GestorDesportivo model = (GestorDesportivo) ois.readObject();
        ois.close();
        return (model);
    }
    
    public void estadoAtualizado () {
        if (!this.utilizadores.isEmpty()) {
            int maiorChaveU = Collections.max(this.utilizadores.keySet());
            utilizadores.get(1).setProximoCodigo(maiorChaveU + 1);
        }
        if (!this.atividades.isEmpty()) {
            int maiorChaveA = Collections.max(this.atividades.keySet());
            atividades.get(1).setProximoCodigo(maiorChaveA + 1);
        }
        if (!this.planos.isEmpty()) {
            int maiorChaveP = Collections.max(this.planos.keySet());
            planos.get(1).setProximoCodigo(maiorChaveP + 1);
        }
    }

    public void atualizaInfo (LocalDate datafim) {
        this.utilizadoresNumPeriodo = new HashMap<Integer, Utilizador>();
        List<Utilizador> utilizadores = new ArrayList<Utilizador>();
        for (Utilizador u : this.utilizadores.values()) {
            Utilizador value = (Utilizador) u.utilizadorNumPeriodo(LocalDate.MIN,datafim);
            int chave = value.getCodUtilizador();
            this.utilizadoresNumPeriodo.put(chave,value);
        }
    }
    
        /**
     * Requisitos 3.2
     * ponto 6
     */
    public PlanoTreino planoTreinoMaisCalorias (LocalDate f) {
        double maxCal = 0;
        PlanoTreino b = null;
        for (Utilizador u : this.utilizadoresNumPeriodo.values()) {
            PlanoTreino a = u.planoTreinoMaisCalorias(LocalDate.MIN, f);
            if (a.caloriasDispendidas(u) >= maxCal) {
                b = a;
                maxCal = a.caloriasDispendidas(u);
            }
        }
        return b;
    }
    
    /**
     * Requisitos 3.2
     * ponto 3
     */
    public String atividadeMaisRealizada () {
        List<Atividade> atividades = new ArrayList<Atividade>();
        Predicate<Atividade> p = atividade -> true;
        this.utilizadoresNumPeriodo.values().forEach(u -> atividades.addAll(u.atividadesNumPeriodoQueRespeitamP(LocalDate.MIN, LocalDate.MAX, p)));
        
        Map<String,Integer> tipoAtividadeQuantidade = new HashMap<>();
        
        for (Atividade atividade : atividades) {
            String tipo = atividade.getClass().getSimpleName();
            tipoAtividadeQuantidade.put(tipo, tipoAtividadeQuantidade.getOrDefault(tipo, 0) + 1);
        }
        
        String atividadeMaisRealizada = null;
        int contagemMaisFrequente = 0;
        
        for (Map.Entry<String, Integer> entry : tipoAtividadeQuantidade.entrySet()) {
            if (entry.getValue() >= contagemMaisFrequente) {
                atividadeMaisRealizada = entry.getKey();
                contagemMaisFrequente = entry.getValue();
            }
        }

        return atividadeMaisRealizada;
    }
    
    /** 
     * Utilizador com mais atividades
     * Requisito 3.2
     * ponto 2
    */
    public Utilizador maisAtividades (LocalDate dataInicial, LocalDate dataFinal) {
         return (Utilizador) this.utilizadoresNumPeriodo.values().stream().reduce((u1, u2) -> u1.numeroAtividades(dataInicial,dataFinal) > u2.numeroAtividades(dataInicial,dataFinal) ? u1 : u2).orElse(null).clone();
    }
    
    /**
     * Método maisCaloriasGastas, que calcula qual utilizador dispendeu mais calorias num período ou desde sempre
     * Requisito 3.2
     * ponto 1
    */
    public Utilizador maisCaloriasGastas(LocalDate dataInicial, LocalDate dataFinal) {
        return (Utilizador) this.utilizadoresNumPeriodo.values().stream().reduce((u1, u2) -> u1.totalCaloriasDispendidas(dataInicial,dataFinal) > u2.totalCaloriasDispendidas(dataInicial,dataFinal) ? u1 : u2).orElse(null).clone();
    }
    
    
    public int addUtilizador(String nome, String morada, String email, int freqCardiaca, int peso, int altura, LocalDate dataNascimento, char genero, int tipo){
         Utilizador novoUtilizador;
         if(tipo==1){
             novoUtilizador = new UtilizadorAmador(nome, morada, email, freqCardiaca, peso, altura, dataNascimento, genero);
         }
         else if(tipo==3){
             novoUtilizador = new UtilizadorProfissional(nome, morada, email, freqCardiaca, peso, altura, dataNascimento, genero);
         }
         else{
             novoUtilizador = new UtilizadorPraticanteOcasional(nome, morada, email, freqCardiaca, peso, altura, dataNascimento, genero);
         }
         this.utilizadores.put(novoUtilizador.getCodUtilizador(), novoUtilizador);
         return novoUtilizador.getCodUtilizador();
    }
    
    public String showUtilizador(int codigo) {
        if (this.utilizadores.containsKey(codigo)) {
            return this.utilizadores.get(codigo).toString();
        } else {
            return "Nao existe utilizador com este codigo\n";
        }
    }
    
    public String showAtividade(int codigo) {
        if (this.atividades.containsKey(codigo)) {
            return this.atividades.get(codigo).toString();
        } else {
            return "Nao existe atividade com este codigo\n";
        }
    }
    
    public String showPlanoTreino(int codigo) {
        if (this.planos.containsKey(codigo)) {
            return this.planos.get(codigo).toString();
        } else {
            return "Nao existe plano de treino com este codigo\n";
        }
    }
    
    public boolean existeUtilizador(int codUtilizador){
        return this.utilizadores.containsKey(codUtilizador);
    }
    
    public boolean existeAtividade(int codAtividade){
        return this.atividades.containsKey(codAtividade);
    }
    
    public boolean existePlano(int codPlano){
        return this.planos.containsKey(codPlano);
    }
    
    public int addAtivDist(LocalDateTime realizacao, LocalTime tempo, int freqCardiaca, double distancia, int tipo){
         Atividade novaAtividade;
         if(tipo==1){
             novaAtividade = new Corrida(realizacao, tempo, freqCardiaca, distancia);
         }
         else{
             novaAtividade = new Ciclismo(realizacao, tempo, freqCardiaca, distancia);
         }
         this.atividades.put(novaAtividade.getCodAtividade(), novaAtividade);
         return novaAtividade.getCodAtividade();
    }
    
    public int addAtivDistAlt(LocalDateTime realizacao, LocalTime tempo, int freqCardiaca, double distancia, double altimetria, int tipo){
         Atividade novaAtividade;
         if(tipo==3){
             novaAtividade = new Trail(realizacao, tempo, freqCardiaca, distancia, altimetria);
         }
         else{
             novaAtividade = new Btt(realizacao, tempo, freqCardiaca, distancia, altimetria);
         }
         this.atividades.put(novaAtividade.getCodAtividade(), novaAtividade);
         return novaAtividade.getCodAtividade();
    }
    
    public int addAtivRep(LocalDateTime realizacao, LocalTime tempo, int freqCardiaca, int repeticoes, int tipo){
         Atividade novaAtividade;
         if(tipo==5){
             novaAtividade = new Flexoes(realizacao, tempo, freqCardiaca, repeticoes);
         }
         else{
             novaAtividade = new Abdominais(realizacao, tempo, freqCardiaca, repeticoes);
         }
         this.atividades.put(novaAtividade.getCodAtividade(), novaAtividade);
         return novaAtividade.getCodAtividade();
    }
    
    public int addAtivRepsPeso(LocalDateTime realizacao, LocalTime tempo, int freqCardiaca, int repeticoes, double peso, int tipo){
         Atividade novaAtividade;
         if(tipo==7){
             novaAtividade = new LegPress(realizacao, tempo, freqCardiaca, repeticoes, peso);
         }
         if(tipo==8){
             novaAtividade = new BenchPress(realizacao, tempo, freqCardiaca, repeticoes, peso);
         }
         else{
             novaAtividade = new BicepCurls(realizacao, tempo, freqCardiaca, repeticoes, peso);
         }
         this.atividades.put(novaAtividade.getCodAtividade(), novaAtividade);
         return novaAtividade.getCodAtividade();
    }
    
    public void registaAtividade(int codUtilizador, int codAtividade){
        Atividade ativ =  this.atividades.get(codAtividade);
        this.utilizadores.get(codUtilizador).addAtividade((Atividade) ativ.clone());
    }
    
    public void addAtivDistPlano(int codPlano, int iteracoes, LocalDateTime realizacao, LocalTime tempo, int freqCardiaca, double distancia, int tipo){
         Atividade novaAtividade;
         if(tipo==1){
             novaAtividade = new Corrida(realizacao, tempo, freqCardiaca, distancia);
         }
         else{
             novaAtividade = new Ciclismo(realizacao, tempo, freqCardiaca, distancia);
         }
         this.planos.get(codPlano).addAtividade(novaAtividade, iteracoes);
    }
    
    public void addAtivDistAltPlano(int codPlano, int iteracoes, LocalDateTime realizacao, LocalTime tempo, int freqCardiaca, double distancia, double altimetria, int tipo){
         Atividade novaAtividade;
         if(tipo==3){
             novaAtividade = new Trail(realizacao, tempo, freqCardiaca, distancia, altimetria);
         }
         else{
             novaAtividade = new Btt(realizacao, tempo, freqCardiaca, distancia, altimetria);
         }
         this.planos.get(codPlano).addAtividade(novaAtividade, iteracoes);
    }
    
    public void addAtivRepPlano(int codPlano, int iteracoes, LocalDateTime realizacao, LocalTime tempo, int freqCardiaca, int repeticoes, int tipo){
         Atividade novaAtividade;
         if(tipo==5){
             novaAtividade = new Flexoes(realizacao, tempo, freqCardiaca, repeticoes);
         }
         else{
             novaAtividade = new Abdominais(realizacao, tempo, freqCardiaca, repeticoes);
         }
         this.planos.get(codPlano).addAtividade(novaAtividade, iteracoes);
    }
    
    public void addAtivRepsPesoPlano(int codPlano, int iteracoes, LocalDateTime realizacao, LocalTime tempo, int freqCardiaca, int repeticoes, double peso, int tipo){
         Atividade novaAtividade;
         if(tipo==7){
             novaAtividade = new LegPress(realizacao, tempo, freqCardiaca, repeticoes, peso);
         }
         if(tipo==8){
             novaAtividade = new BenchPress(realizacao, tempo, freqCardiaca, repeticoes, peso);
         }
         else{
             novaAtividade = new BicepCurls(realizacao, tempo, freqCardiaca, repeticoes, peso);
         }
         this.planos.get(codPlano).addAtividade(novaAtividade, iteracoes);
    }
    
    public void registaPlanoTreino(int codUtilizador, int codPlano){
        PlanoTreino plano =  this.planos.get(codPlano);
        this.utilizadores.get(codUtilizador).addPlanoTreino((PlanoTreino) plano.clone());
    }
    
    public int addPlanoTreino(LocalDate data){
        PlanoTreino plano = new PlanoTreino(data);
        this.planos.put(plano.getCodPlano(), plano);
        return plano.getCodPlano();
    }
    
    public double kmsPercorridos(int codUtilizador, LocalDate dataInicio, LocalDate dataFim){
        return this.utilizadoresNumPeriodo.get(codUtilizador).allKmsDistancia(dataInicio,dataFim);
    }
    
    public double metrosAltimetria(int codUtilizador, LocalDate dataInicio, LocalDate dataFim){
        return this.utilizadoresNumPeriodo.get(codUtilizador).allMetrosAltimetria(dataInicio,dataFim);
    }
    
    public String atividadesExecutadasUtilizador(int codUtilizador){
        StringBuilder sb = new StringBuilder();
        for (Atividade a : this.utilizadoresNumPeriodo.get(codUtilizador).allAtividades(LocalDate.MIN,LocalDate.MAX)){
            sb.append(a.toString());
        }
        return sb.toString();
    }
    
    public String atividadesUtilizador(int codUtilizador){
        StringBuilder sb = new StringBuilder();
        for (Atividade a : this.utilizadores.get(codUtilizador).allAtividades(LocalDate.MIN,LocalDate.MAX)){
            sb.append(a.toString());
        }
        return sb.toString();
    }
    
    public String mostraInfo(){
        StringBuilder sb = new StringBuilder();
        for(Utilizador u : this.utilizadoresNumPeriodo.values()){
            sb.append(u.toString());
        }
        return sb.toString();
    }

    public double recordMaisPeso(Class<?> tipo){
        double peso;
        peso = recordDouble(tipo, a -> ((AtivRepsPeso)a).getPeso());
        return peso;
    }

    public int recordMaisRepeticoes(Class<?> tipo){
        int reps;
        reps = recordInt(tipo, a -> ((AtivRepeticoes)a).getRepeticoes());
        return reps;
    }

    public double recordMaiorAltimetria(Class<?> tipo){
        double altimetria;
        altimetria = recordDouble(tipo, a -> ((AtivDistAltimetria)a).getAltimetria());
        return altimetria;
    }

    public double recordMaisVelocidade(Class<?> tipo){
        double velocidade;
        velocidade = recordDouble(tipo, a -> ((AtivDistancia)a).getVelocidade());
        return velocidade;
    }

    public int recordMaisTempo(Class<?> tipo){
        int tempo;
        tempo = recordInt(tipo, a -> ((Atividade)a).getTempo().toSecondOfDay());
        return tempo;
    }

    public double recordMaisDistancia(Class<?> tipo){
        double distancia;
        distancia = recordDouble(tipo, a -> ((AtivDistancia)a).getDistancia());
        return distancia;
    }

    public double recordMaisCalorias(Class<?> tipo){
        LocalDate inicio = LocalDate.MIN;
        LocalDate fim = LocalDate.MAX;
        Predicate<Atividade> p = a -> tipo.isInstance(a);
        BiFunction<Atividade,Utilizador,Double> f = (a,u) -> ((Atividade)a).consumoCalorias((Utilizador)u);
        return this.utilizadores.values().stream()
                                .flatMap(u -> u.infoDasAtividadesUtilizadorNumPeriodoQueRespeitamP(inicio, fim, p, f, u).stream())
                                .reduce((n1, n2) -> n1 > n2 ? n1 : n2)
                                .orElse(0.0);
    }

    public double recordDouble(Class<?> tipo, Function<Atividade,Double> f){
        LocalDate inicio = LocalDate.MIN;
        LocalDate fim = LocalDate.MAX;
        Predicate<Atividade> p = a -> tipo.isInstance(a);
        return this.utilizadores.values().stream()
                                .flatMap(u -> u.infoDasAtividadesNumPeriodoQueRespeitamP(inicio, fim, p, f).stream())
                                .reduce((n1, n2) -> n1 > n2 ? n1 : n2)
                                .orElse(0.0);
    }

    public int recordInt(Class<?> tipo, Function<Atividade,Integer> f){
        LocalDate inicio = LocalDate.MIN;
        LocalDate fim = LocalDate.MAX;
        Predicate<Atividade> p = a -> tipo.isInstance(a);
        return this.utilizadores.values().stream()
                                .flatMap(u -> u.infoDasAtividadesNumPeriodoQueRespeitamP(inicio, fim, p, f).stream())
                                .reduce((n1, n2) -> n1 > n2 ? n1 : n2)
                                .orElse(0);
    }

    public List<Atividade> listaAtividades(int[] atividades){
        int size = atividades.length;
        List<Atividade> a = new ArrayList<>();
        int i = 0;
        Atividade at;
        for (i=0; i<size; i++) {
            if (atividades[i] == 1) { //Abdominais
                at = new Abdominais();
            }
            else if (atividades[i] == 2) { //BenchPress
                at = new BenchPress();
            }
            else if (atividades[i] == 3) { //BicepCurls
                at = new BicepCurls();
            }
            else if (atividades[i] == 4) { //Btt
                at = new Btt();
            }
            else if (atividades[i] == 5) { //Ciclismo
                at = new Ciclismo();
            }
            else if (atividades[i] == 6) { //Corrida
                at = new Corrida();
            }
            else if (atividades[i] == 7) { //Flexoes
                at = new Flexoes();
            }
            else if (atividades[i] == 8) { //LegPress
                at = new LegPress();
            }
            else if (atividades[i] == 9) { //Trail
                at = new Trail();
            }
            else
                at = new Corrida();
            a.add(at);
        }
        return a;
    }

    public void geraPlanoTreinoUtilizador(int codUtilizador, int[] atividades, int maxAtivDia, int ativPorSemana, double consumoCaloricoMinimo, LocalDate inicioSemana){
            List<Atividade> a = listaAtividades(atividades);
            List<PlanoTreino> p = new ArrayList<>();
            PlanoTreino plano = new PlanoTreino();
            System.out.println("Ok\n");
            p = plano.geraPlanoTreino(this.utilizadores.get(codUtilizador), a, maxAtivDia, ativPorSemana, consumoCaloricoMinimo, inicioSemana);
            int size = p.size();
            int i = 0;
            int res[] = new int[size];
            for (i=0; i<size; i++) {
                this.utilizadores.get(codUtilizador).addPlanoTreino(p.get(i));
                res[i] = p.get(i).getCodPlano();
            }
        }
}
