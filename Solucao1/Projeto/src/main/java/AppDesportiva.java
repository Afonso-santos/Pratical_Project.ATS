package Projeto;
import java.io.*;
import java.time.*;

/**
 * Classe AppDesportiva, que corresponde ao controller
 *
 * @author Grupo10
 * @version 10/05/24
 * Notas versão :
 */
public class AppDesportiva
{
    private GestorDesportivo model;
    private Menu menuInicial, menuSetup, menuSimulacao, menuEstatisticas;
    private LocalDate dataAtual;
    private static String[] opcoesInicial = {"Menu Inicial","Carregar estado", "Novo estado", "Sair"};
    private static String[] opcoesSetup = {"Menu Setup","Adicionar utilizador","Visualizar utilizador", "Adicionar atividade", "Registar execução de atividade", "Adicionar atividade e registar execução","Visualizar atividade", "Adicionar plano de treino", "Registar execução de plano de treino", "Adicionar plano de treino e registar execução","Visualizar plano de treino", "Iniciar simulação", "Guardar estado", "Guardar e sair","Sair sem guardar"};
    private static String[] opcoesSimulacao = {"Menu Simulação", "Avançar tempo","Consultar recordes", "Consultar estatísticas", "Mostrar todas as informações (todos os utilizadores e as suas atividades e planos de treino executados)", "Voltar ao setup"};
    private static String[] opcoesEstatisticas = {"Estatísticas","Utilizador com mais calorias gastas", "Utilizador com mais atividades realizadas", "Atividade mais realizada", "Total de kilómetros percorridos", "Metros de altimetria acumulados", "Plano de treino mais exigente", "Atividades de um utilizador", "Voltar"};
    private String path;
    
    /**
     * Construtor de AppDesportiva
     */
    public AppDesportiva()
    {
        this.model = new GestorDesportivo();
        this.menuInicial = new Menu(AppDesportiva.opcoesInicial);
        this.menuSetup = new Menu(AppDesportiva.opcoesSetup);
        this.menuSimulacao = new Menu(AppDesportiva.opcoesSimulacao);
        this.menuEstatisticas = new Menu(AppDesportiva.opcoesEstatisticas);
        this.dataAtual = LocalDate.now();
        this.runInicial();
    }
    
    /**
     * Construtor de AppDesportiva quando é passado o nome do ficheiro a carregar
     * @param nome do ficheiro
     */
    public AppDesportiva(String ficheiro)
    {
        this.model = new GestorDesportivo();
        this.menuInicial = new Menu(AppDesportiva.opcoesInicial);
        this.menuSetup = new Menu(AppDesportiva.opcoesSetup);
        this.menuSimulacao = new Menu(AppDesportiva.opcoesSimulacao);
        this.menuEstatisticas = new Menu(AppDesportiva.opcoesEstatisticas);
        this.dataAtual = LocalDate.now();
        try{
            this.model = this.model.carregaEstado(ficheiro);
            System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
            menuInicial.pedeString("Dados carregados com sucesso.\nEnter para continuar");
            this.model.estadoAtualizado();
            this.path = ficheiro;
            this.runSetup();
        }
        catch (ClassNotFoundException clExc){
            System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
            menuInicial.pedeString("Não foi possível carregar os dados do ficheiro.\nEnter para continuar");
            this.runInicial();
        }
        catch (IOException ioExc){
            System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
            menuInicial.pedeString("Não foi possível aceder ao ficheiro.\nEnter para continuar");
            this.runInicial();
        }
    }
    
    /**
     * Método auxiliar carregaDados
     * @param menu atual
     */
    private int carregaDados(Menu menuAtual){
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        String ficheiro = menuAtual.pedeString("Insira o nome do ficheiro a carregar");
        try{
            this.model = this.model.carregaEstado(ficheiro);
            this.model.estadoAtualizado();
            menuAtual.pedeString("Dados carregados com sucesso.\nEnter para continuar");
            this.path=ficheiro;
            return 0;
        }
        catch (ClassNotFoundException clExc){
            menuAtual.pedeString("Não foi possível carregar os dados do ficheiro.\nEnter para continuar");
        }
        catch (IOException ioExc){
            menuAtual.pedeString("Não foi possível aceder ao ficheiro.\nEnter para continuar");
        }
        return 1;
    }
    
    /**
     * Método auxiliar guardaDados
     * @param menu atual
     */
    private int guardaDados(Menu menuAtual){
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        try {
            this.model.guardaEstado(this.path);
            menuAtual.pedeString("Estado guardado com sucesso.\nEnter para continuar");
            return 0;
        }
        catch (IOException e){
            menuAtual.pedeString("Não foi possível aceder ao ficheiro.\nEnter para continuar");
        }
        return 1;
    }
    
    /**
     * Método auxiliar adicionaUser
     * @param menu atual
     */
    private void adicionaUser(Menu menuAtual){
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        String nome = menuAtual.pedeString("Insira o nome do utilizador");
        String morada = menuAtual.pedeString("Insira a morada do utilizador");
        String email = menuAtual.pedeString("Insira o email do utilizador");
        int freqCardiaca = menuAtual.pedeInt("Insira a frequência cardíaca média do utilizador");
        int peso = menuAtual.pedeInt("Insira o peso do utilizador (em kilos)");
        int altura = menuAtual.pedeInt("Insira a altura do utilizador (em centímetros)");
        LocalDate dataNascimento = menuAtual.pedeData("Insira a data de nascimento do utilizador (dia/mês/ano)");
        char genero = menuAtual.pedeString("Insira o género do utilizador: M ou F").charAt(0);
        while(genero!='M'&&genero!='F'){
            genero = menuAtual.pedeString("Insira o género do utilizador: M ou F").charAt(0);
        }
        int tipo = menuAtual.pedeInt("Insira o tipo de utilizador:\n1: Utilizador amador\n2: Utilizador praticante ocasional\n3: Utilizador profissional");
        while(tipo<1||tipo>3){
            tipo = menuAtual.pedeInt("Insira o tipo de utilizador:\n1: Utilizador amador\n2: Utilizador praticante ocasional\n3: Utilizador profissional");
        }
        int cod = model.addUtilizador(nome, morada, email, freqCardiaca, peso, altura, dataNascimento, genero, tipo);
        System.out.print("Utilizador adicionado com sucesso.\nCódigo do utilizador adicionado: ");
        System.out.println(cod);
        menuAtual.pedeString("Enter para continuar");
    }
    
    private void mostraUser(Menu menuAtual){
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        int codigo = menuAtual.pedeInt("Insira o codigo do utilizador");
        String s = model.showUtilizador(codigo);
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        System.out.print(s);
        menuAtual.pedeString("Enter para continuar");
    }
    
    private void mostraAtividade(Menu menuAtual){
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        int codigo = menuAtual.pedeInt("Insira o codigo da atividade");
        String s = model.showAtividade(codigo);
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        System.out.print(s);
        menuAtual.pedeString("Enter para continuar");
    }
    
    private void mostraPlanoTreino(Menu menuAtual){
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        int codigo = menuAtual.pedeInt("Insira o codigo do plano de treino");
        String s = model.showPlanoTreino(codigo);
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        System.out.print(s);
        menuAtual.pedeString("Enter para continuar");
    }
    
    /**
     * Método auxiliar addAtividade
     * @param menu atual
     */
    private int addAtividade(Menu menuAtual){
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        int tipoAtiv = menuAtual.pedeInt("Insira o tipo da atividade:\n1: Corrida\n2: Ciclismo\n3: Trail\n4: Btt\n5: Flexões\n6: Abdominais\n7: Leg Press\n8: Bench Press\n9: Bicep Curls");
        if(tipoAtiv<1||tipoAtiv>9){
            tipoAtiv = menuAtual.pedeInt("Insira o tipo da atividade:\n1: Corrida\n2: Ciclismo\n3: Trail\n4: Btt\n5: Flexões\n6: Abdominais\n7: Leg Press\n8: Bench Press\n9: Bicep Curls");
        }
        LocalDateTime realizacao = menuAtual.pedeDataHora("Insira a data e hora de realização (dia/mês/ano horas:minutos:segundos)");
        LocalTime tempo = menuAtual.pedeTempo("Insira o tempo de realização horas:minutos:segundos");
        int freq = menuAtual.pedeInt("Insira a frequência cardíaca média durante a realização");
        int id;
        if(tipoAtiv<=4){
                double dist = menuAtual.pedeDouble("Insira a distância percorrida (em metros)");
                if(tipoAtiv<=2) id = this.model.addAtivDist(realizacao, tempo, freq, dist, tipoAtiv);
                else{
                    double alt = menuAtual.pedeDouble("Insira os metros de altimetria");
                    id = this.model.addAtivDistAlt(realizacao, tempo, freq, dist, alt, tipoAtiv);
                }
        }
        else{
                int reps = menuAtual.pedeInt("Insira o número de repetições");
                if(tipoAtiv<=6) id = this.model.addAtivRep(realizacao, tempo, freq, reps, tipoAtiv);
                else{
                    double peso = menuAtual.pedeDouble("Insira o peso utilizado (em kilos)");
                    id = this.model.addAtivRepsPeso(realizacao, tempo, freq, reps, peso, tipoAtiv);
                }
        }
        System.out.print("Atividade adicionada com sucesso.\nCódigo da atividade adicionada: ");
        System.out.println(id);
        menuSetup.pedeString("Enter para continuar");
        return id;
    }
    
    /**
     * Método auxiliar registaAtividade
     * @param menu atual
     */
    private void registaAtividade(Menu menuAtual){
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        int codUtilizador = menuAtual.pedeInt("Insira o código do utilizador");
        if (!model.existeUtilizador(codUtilizador)){
                menuAtual.pedeString("Utilizador não existe.\nEnter para continuar");
                return;
        }
        int codAtividade = menuAtual.pedeInt("Insira o código da atividade");
        if (!model.existeAtividade(codAtividade)){
                menuAtual.pedeString("Atividade não existe.\nEnter para continuar");
                return;
        }
        model.registaAtividade(codUtilizador, codAtividade);
        menuAtual.pedeString("Atividade registada com sucesso.\nEnter para continuar");
    }
    
    /**
     * Método auxiliar addRegistaAtividade
     * @param menu atual
     */
    private int addRegistaAtividade(Menu menuAtual){
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        int id = addAtividade(menuAtual);
        int codUtilizador = menuAtual.pedeInt("Insira o código do utilizador");
        while (!model.existeUtilizador(codUtilizador)){
                menuAtual.pedeString("Utilizador não existe.\nEnter para continuar");
                return 0;
        }
        menuAtual.pedeString("Atividade registada com sucesso.\nEnter para continuar");
        model.registaAtividade(codUtilizador, id);
        return id;
    }
    
    /**
     * Método auxiliar adicionaAtividadePlano
     */
    private void adicionaAtividadePlano(Menu menuAtual, int id_plano, LocalDate data){
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        int tipoAtiv = menuAtual.pedeInt("Insira o tipo da atividade:\n1: Corrida\n2: Ciclismo\n3: Trail\n4: Btt\n5: Flexões\n6: Abdominais\n7: Leg Press\n8: Bench Press\n9: Bicep Curls");
        if(tipoAtiv<1||tipoAtiv>9){
            tipoAtiv = menuAtual.pedeInt("Insira o tipo da atividade:\n1: Corrida\n2: Ciclismo\n3: Trail\n4: Btt\n5: Flexões\n6: Abdominais\n7: Leg Press\n8: Bench Press\n9: Bicep Curls");
        }
        LocalDateTime realizacao = menuAtual.pedeDataHora("Insira a data e hora de realização (dia/mês/ano horas:minutos:segundos)");
        while(realizacao.isBefore(data.atStartOfDay())||realizacao.isAfter(data.plusWeeks(1).atTime(23,59))){
            realizacao = menuAtual.pedeDataHora("Data de realização tem de estar na semana do plano de treino");
        }
        LocalTime tempo = menuAtual.pedeTempo("Insira o tempo de realização horas:minutos:segundos");
        int freq = menuAtual.pedeInt("Insira a frequência cardíaca média durante a realização");
        int iter = menuAtual.pedeInt("Insira o número de iterações da atividade");
        if(tipoAtiv<=4){
                double dist = menuAtual.pedeDouble("Insira a distância percorrida (em metros)");
                if(tipoAtiv<=2) this.model.addAtivDistPlano(id_plano, iter, realizacao, tempo, freq, dist, tipoAtiv);
                else{
                    double alt = menuAtual.pedeDouble("Insira os metros de altimetria");
                    this.model.addAtivDistAltPlano(id_plano, iter, realizacao, tempo, freq, dist, alt, tipoAtiv);
                }
        }
        else{
                int reps = menuAtual.pedeInt("Insira o número de repetições");
                if(tipoAtiv<=6) this.model.addAtivRepPlano(id_plano, iter,realizacao, tempo, freq, reps, tipoAtiv);
                else{
                    double peso = menuAtual.pedeDouble("Insira o peso utilizado (em kilos)");
                    this.model.addAtivRepsPesoPlano(id_plano, iter,realizacao, tempo, freq, reps, peso, tipoAtiv);
                }
        }
        menuAtual.pedeString("Atividade adicionada com sucesso.\nEnter para continuar");
    }
    
    private int addPlano(Menu menuAtual){
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        LocalDate dataRealizacao = menuAtual.pedeData("Insira a data de início do plano de treino (segunda feira)");
        while(dataRealizacao.getDayOfWeek()!=DayOfWeek.MONDAY){
            dataRealizacao = menuAtual.pedeData("Insira a data de início do plano de treino (segunda feira)");
        }
        int id = this.model.addPlanoTreino(dataRealizacao);
        int nAtividades = menuAtual.pedeInt("Insira o número de atividades a colocar no plano de treino");
        while(nAtividades>0){
            this.adicionaAtividadePlano(menuAtual, id, dataRealizacao);
            nAtividades--;
        }
        System.out.print("Plano de treino adicionado com sucesso.\nCódigo do plano adicionado: ");
        System.out.println(id);
        menuSetup.pedeString("Enter para continuar");
        return id;
    }
    
    private void registaPlano(Menu menuAtual){
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        int codUtilizador = menuAtual.pedeInt("Insira o código do utilizador");
        if (!model.existeUtilizador(codUtilizador)){
                menuAtual.pedeString("Utilizador não existe.\nEnter para continuar");
                return;
        }
        int codPlano = menuAtual.pedeInt("Insira o código do plano de treino");
        if (!model.existePlano(codPlano)){
                menuAtual.pedeString("Plano de treino não existe.\nEnter para continuar");
                return;
        }
        model.registaPlanoTreino(codUtilizador, codPlano);
        menuAtual.pedeString("Plano de treino registado com sucesso.\nEnter para continuar");
    }
    
    /**
     * Método auxiliar addRegistaPlano
     * @param menu atual
     */
    private int addRegistaPlano(Menu menuAtual){
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        int codUtilizador = menuAtual.pedeInt("Insira o código do utilizador");
        if (!model.existeUtilizador(codUtilizador)){
                menuAtual.pedeString("Utilizador não existe.\nEnter para continuar");
                return 0;
        }
        int id = addPlano(menuAtual);
        model.registaPlanoTreino(codUtilizador, id);
        return id;
    }

    private boolean existeAtiv(int ativ[],int tipo){
        boolean res = false;
        int i;
        for (i=0; i<ativ.length; i++){
            if(ativ[i] == tipo){
                res= true;
                break;
            }
        }
        return res;
    }
    
    /**
     * Método geraPlanoTreino()
     * Não acabado
     */
    private void geraPlanoTreino(){
            System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
            int codUtilizador = menuSetup.pedeInt("Insira o código do utilizador");
            while (!model.existeUtilizador(codUtilizador)){
                menuSetup.pedeString("Utilizador não existe.\nEnter para continuar");
            }
            int ativ[] = new int[9];
            int i = 0;
            int tipoAtiv = menuSetup.pedeInt("Insira o tipo de atividade a incluir:\n1: Corrida\n2: Ciclismo\n3: Trail\n4: Btt\n5: Flexões\n6: Abdominais\n7: Leg Press\n8: Bench Press\n9: Bicep Curls");
            while(tipoAtiv<1||tipoAtiv>9){
                tipoAtiv = menuSetup.pedeInt("Insira o tipo de atividade a incluir:\n1: Corrida\n2: Ciclismo\n3: Trail\n4: Btt\n5: Flexões\n6: Abdominais\n7: Leg Press\n8: Bench Press\n9: Bicep Curls");
            }
            while(tipoAtiv!=0){
                ativ[i] = tipoAtiv; i++;
                tipoAtiv = menuSetup.pedeInt("Insira o tipo de atividade (não repetida) a incluir:\n1: Corrida\n2: Ciclismo\n3: Trail\n4: Btt\n5: Flexões\n6: Abdominais\n7: Leg Press\n8: Bench Press\n9: Bicep Curls\n0: Nenhum");
                while(tipoAtiv<0||tipoAtiv>9 && !existeAtiv(ativ, tipoAtiv) ){
                        tipoAtiv = menuSetup.pedeInt("Insira o tipo de atividade (não repetida) a incluir:\n1: Corrida\n2: Ciclismo\n3: Trail\n4: Btt\n5: Flexões\n6: Abdominais\n7: Leg Press\n8: Bench Press\n9: Bicep Curls\n0: Nenhum");
                }
            }
            int maxAtivDia = menuSetup.pedeInt("Insira o número máximo de atividades por dia");            
            int ativPorSemana = menuSetup.pedeInt("Insira o número de atividades a realizar por semana");        
            double consumoCaloricoMinimo = menuSetup.pedeDouble("Insira o consumo calórico mínimo pretendido"); 
            LocalDate dataRealizacao = menuSetup.pedeData("Insira a data de início do plano de treino (segunda feira)");
            while(dataRealizacao.getDayOfWeek()!=DayOfWeek.MONDAY){
                dataRealizacao = menuSetup.pedeData("Insira a data de início do plano de treino (segunda feira)");
            }
            this.model.geraPlanoTreinoUtilizador(codUtilizador, ativ, maxAtivDia, ativPorSemana, consumoCaloricoMinimo, dataRealizacao);
            menuSetup.pedeString("Plano de treino gerado com sucesso.\nEnter para continuar");
    }
    
    public void estatisticaEntreDatas(int op){
        LocalDate dataInicial;
        LocalDate dataFinal;
        int d = 0;
        while(d!=1&&d!=2){
            d = this.menuEstatisticas.pedeInt("1: Desde sempre\n2: Entre datas");
        }
        if(d==1){
            dataInicial = LocalDate.MIN;
            dataFinal = this.dataAtual;
        }
        else{
            dataInicial = this.menuEstatisticas.pedeData("Insira data inicial (dia/mês/ano)");
            dataFinal = this.menuEstatisticas.pedeData("Insira data final (dia/mês/ano)");
        }
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        switch (op){
                case 1:
                Utilizador res = this.model.maisCaloriasGastas(dataInicial,dataFinal);
                System.out.println(res.toString());
                break;
            case 2:
                Utilizador user = this.model.maisAtividades(dataInicial,dataFinal);
                System.out.println(user.toString());
                break;
            case 6:
                PlanoTreino plano = this.model.planoTreinoMaisCalorias(dataFinal);
                if (plano != null)
                System.out.println(plano.toString());
                break;
        }
        menuEstatisticas.pedeString("Enter para continuar");
    }
    
    public void estatisticaUtilizadorEntreDatas(int op){
        int codUser = menuEstatisticas.pedeInt("Insira o código do utilizador");
        if (!model.existeUtilizador(codUser)){
                menuEstatisticas.pedeString("Utilizador não existe.\nEnter para continuar");
                return;
        }
        LocalDate dataInicial;
        LocalDate dataFinal;
        int d = 0;
        while(d!=1&&d!=2){
            d = this.menuEstatisticas.pedeInt("1: Desde sempre\n2: Entre datas");
        }
        if(d==1){
            dataInicial = LocalDate.MIN;
            dataFinal = this.dataAtual;
        }
        else{
            dataInicial = this.menuEstatisticas.pedeData("Insira data inicial (dia/mês/ano)");
            dataFinal = this.menuEstatisticas.pedeData("Insira data final (dia/mês/ano)");
        }
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        switch (op){
            case 4:
                double kms = model.kmsPercorridos(codUser, dataInicial, dataFinal);
                System.out.print(kms);
                System.out.println(" kilómetros\n");
                break;
            case 5:
                double metros = model.metrosAltimetria(codUser, dataInicial, dataFinal);
                System.out.print(metros);
                System.out.println(" metros\n");
                break;
        }
        menuEstatisticas.pedeString("Enter para continuar");
    }
    
    /**
     * Método runInicial, que executa o menu inicial, lê e trata as opções escolhidas
     */
    public void runInicial(){
        menuInicial.runMenu();
        int op = menuInicial.getOpcao();
        while (op!=0){
            switch (op){ 
                case 1 :   //opção "Carregar estado"
                    int carregar = this.carregaDados(menuInicial);
                    if (carregar == 1) break;
                    int ext = this.runSetup();
                    if (ext==1) op=0;
                    op=0;
                    break;
                case 2 :    //opção "Novo estado"
                    String ficheiro = menuInicial.pedeString("Insira o nome do ficheiro onde guardar o estado");
                    this.path=ficheiro;
                    try {
                        this.model.guardaEstado(this.path);
                    }
                    catch (IOException e){
                        menuInicial.pedeString("Não foi possível aceder ao ficheiro.\nEnter para continuar");
                        break;
                    }
                    int exit = this.runSetup();
                    if (exit==1) op=0;
                    break;
            }
            if(op!=0){
                menuInicial.runMenu();
                op = menuInicial.getOpcao();
            }
        }
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        System.out.println("Programa encerrado");
    }
    
    /**
     * Método runSetup, que executa o menu onde são adicionados os utilizadores, atividades e planos de treino
     */
    public int runSetup(){
        menuSetup.runMenu();
        int op = menuSetup.getOpcao();
        while (op!=0){ 
            switch (op){
                case 1 :    //opção "Adicionar utilizador"
                    this.adicionaUser(menuSetup);
                    break;
                case 2 :
                    this.mostraUser(menuSetup);
                    break;
                case 3 :    //opção "Adicionar atividade"
                    this.addAtividade(menuSetup);
                    break;
                case 4 :    //opção "Registar execução de atividade"
                    this.registaAtividade(menuSetup);
                    break;
                case 5 :    //opção "Adicionar atividade e registar execução"
                    this.addRegistaAtividade(menuSetup);
                    break;
                case 6 :
                    this.mostraAtividade(menuSetup);
                    break;
                case 7 :    //opção "Adicionar plano de treino"
                    this.addPlano(menuSetup);
                    break;
                case 8 :    //opção "Registar execução de plano de treino"
                    this.registaPlano(menuSetup);
                    break;
                case 9 :    //opção "Adicionar plano de treino e registar execução"
                    int codPlano = this.addRegistaPlano(menuSetup);
                    break;
                case 10 :
                    this.mostraPlanoTreino(menuSetup);
                    break;
                case 11 :    //opção "Iniciar simulação"
                    int d = 0;
                    while(d!=1&&d!=2){
                        d = this.menuSetup.pedeInt("1: Escolher data\n2: Utilizar data do sistema");
                    }
                    if(d==1) this.dataAtual = this.menuSetup.pedeData("Insira a data (dia/mês/ano)");
                    this.model.atualizaInfo(this.dataAtual);
                    this.runSimulacao();
                    break;
                case 12 :    //opção "Guardar estado"
                    this.guardaDados(menuSetup);
                    break;
                case 13 :    //opção "Guardar e sair"
                    int guardar = this.guardaDados(menuSetup);
                    if (guardar == 1) break;
                    op=0;
                    break;
            }
            if(op!=0){
                menuSetup.runMenu();
                op = menuSetup.getOpcao();
            }
        }
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        System.out.println("Programa encerrado");
        return 1;
    }
    
    /**
     * Método runSimulacao que executa o menu depois de iniciada a simulação
     */
    public void runSimulacao(){
        menuSimulacao.runMenu();
        System.out.print("Data atual da simulação: ");
        System.out.println(this.dataAtual.toString());
        int op = menuSimulacao.getOpcao();
        while (op!=0){
            switch (op){
                case 1 :    //opção "Avançar tempo"
                    int d = 0;
                    while(d!=1&&d!=2){
                        d = this.menuSimulacao.pedeInt("1: Escolher data para a qual avançar\n2: Inserir número de dias a avançar");
                    }
                    if(d==1) this.dataAtual = this.menuSimulacao.pedeData("Insira a data (dia/mês/ano)");
                    else this.dataAtual = this.dataAtual.plusDays(this.menuSimulacao.pedeInt("Insira o número de dias a avançar"));
                    this.model.atualizaInfo(this.dataAtual);
                    break;
                case 2 :    //opção "Consultar recordes"
                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
                    int tipoAtiv = menuSimulacao.pedeInt("Insira o tipo da atividade:\n1: Corrida\n2: Ciclismo\n3: Trail\n4: Btt\n5: Flexões\n6: Abdominais\n7: Leg Press\n8: Bench Press\n9: Bicep Curls");
                    if(tipoAtiv<1||tipoAtiv>9){
                        tipoAtiv = menuSimulacao.pedeInt("Insira o tipo da atividade:\n1: Corrida\n2: Ciclismo\n3: Trail\n4: Btt\n5: Flexões\n6: Abdominais\n7: Leg Press\n8: Bench Press\n9: Bicep Curls");
                    }
                    Class<?> classe;
                    double calorias;
                    double kms;
                    int tempo;
                    double velocidade;
                    double altimetria;
                    int rep;
                    double peso;
                    switch(tipoAtiv) {
                        case 1:
                            System.out.println("\nRecords das atividades do tipo Corrida:");
                            System.out.print("-> o maior numero de calorias numa atividade deste tipo: ");
                            classe = Corrida.class;
                            calorias = model.recordMaisCalorias(classe);
                            System.out.println(calorias);
                            System.out.print("-> o maior numero de kms percorridos numa atividade deste tipo: ");
                            kms = model.recordMaisDistancia(classe);
                            System.out.println(kms);
                            System.out.print("-> o maior numero de segundos passados a executar uma atividade deste tipo: ");
                            tempo = model.recordMaisTempo(classe);
                            System.out.println(tempo);
                            System.out.print("-> a maior velocidade media numa atividade deste tipo: ");
                            velocidade = model.recordMaisVelocidade(classe);
                            System.out.println(velocidade);
                            menuSimulacao.pedeString("\nEnter para continuar");
                            break;
                        case 2:
                            System.out.println("\nRecords das atividades do tipo Ciclismo:");
                            System.out.print("-> o maior numero de calorias gastas numa atividade deste tipo: ");
                            classe = Ciclismo.class;
                            calorias = model.recordMaisCalorias(classe);
                            System.out.println(calorias);
                            System.out.print("-> o maior numero de kms percorridos numa atividade deste tipo: ");
                            kms = model.recordMaisDistancia(classe);
                            System.out.println(kms);
                            System.out.print("-> o maior numero de segundos passados a executar uma atividade deste tipo: ");
                            tempo = model.recordMaisTempo(classe);
                            System.out.println(tempo);
                            System.out.print("-> a maior velocidade media numa atividade deste tipo: ");
                            velocidade = model.recordMaisVelocidade(classe);
                            System.out.println(velocidade);
                            menuSimulacao.pedeString("\nEnter para continuar");
                            break;
                        case 3:
                            System.out.println("\nRecords das atividades do tipo Trail:"); 
                            System.out.print("-> o maior numero de calorias gastas numa atividade deste tipo: ");
                            classe = Trail.class;
                            calorias = model.recordMaisCalorias(classe);
                            System.out.println(calorias);
                            System.out.print("-> o maior numero de kms percorridos numa atividade deste tipo: ");
                            kms = model.recordMaisDistancia(classe);
                            System.out.println(kms);
                            System.out.print("-> o maior numero de segundos passados a executar uma atividade deste tipo: ");
                            tempo = model.recordMaisTempo(classe);
                            System.out.println(tempo);
                            System.out.print("-> a maior altimetria numa atividade deste tipo: ");
                            altimetria = model.recordMaiorAltimetria(classe);
                            System.out.println(altimetria);
                            menuSimulacao.pedeString("\nEnter para continuar");
                            break;
                        case 4:
                            System.out.println("\nRecords das atividades do tipo Btt:");
                            System.out.print("-> o maior numero de calorias gastas numa atividade deste tipo: ");
                            classe = Btt.class;
                            calorias = model.recordMaisCalorias(classe);
                            System.out.println(calorias);
                            System.out.print("-> o maior numero de kms percorridos numa atividade deste tipo: ");
                            kms = model.recordMaisDistancia(classe);
                            System.out.println(kms);
                            System.out.print("-> o maior numero de segundos passados a executar uma atividade deste tipo: ");
                            tempo = model.recordMaisTempo(classe);
                            System.out.println(tempo);
                            System.out.print("-> a maior altimetria numa atividade deste tipo: ");
                            altimetria = model.recordMaiorAltimetria(classe);
                            System.out.println(altimetria);
                            menuSimulacao.pedeString("\nEnter para continuar");
                            break;
                        case 5:
                            System.out.println("\nRecords das atividades do tipo Flexoes:");
                            System.out.print("-> o maior numero de calorias gastas numa atividade deste tipo: ");
                            classe = Flexoes.class;
                            calorias = model.recordMaisCalorias(classe);
                            System.out.println(calorias);
                            System.out.print("-> o maior numero de repeticoes numa atividade deste tipo: ");
                            rep = model.recordMaisRepeticoes(classe);
                            System.out.println(rep);
                            menuSimulacao.pedeString("\nEnter para continuar");
                            break;
                        case 6:
                            System.out.println("\nRecords das atividades do tipo Abdominais:");
                            System.out.print("-> o maior numero de calorias gastas numa atividade deste tipo: ");
                            classe = Abdominais.class;
                            calorias = model.recordMaisCalorias(classe);
                            System.out.println(calorias);
                            System.out.print("-> o maior numero de repeticoes numa atividade deste tipo: ");
                            rep = model.recordMaisRepeticoes(classe);
                            System.out.println(rep);
                            menuSimulacao.pedeString("\nEnter para continuar");
                            break;
                        case 7:
                            System.out.println("\nRecords das atividades do tipo Leg Press:");
                            System.out.print("-> o maior numero de calorias gastas numa atividade deste tipo: ");
                            classe = LegPress.class;
                            calorias = model.recordMaisCalorias(classe);
                            System.out.println(calorias);
                            menuSimulacao.pedeString("\nEnter para continuar");
                            break;
                        case 8:System.out.println("\nRecords das atividades do tipo Bench Press:");
                            System.out.print("-> o maior numero de calorias gastas numa atividade deste tipo: ");
                            classe = BenchPress.class;
                            calorias = model.recordMaisCalorias(classe);
                            System.out.println(calorias);
                            System.out.print("-> o maior numero de repeticoes numa atividade deste tipo: ");
                            rep = model.recordMaisRepeticoes(classe);
                            System.out.println(rep);
                            System.out.print("-> o maior peso usado numa atividade deste tipo: ");
                            peso = model.recordMaisPeso(classe);
                            System.out.println(peso);
                            menuSimulacao.pedeString("\nEnter para continuar");
                            break;
                        case 9:
                            System.out.println("\nRecords das atividades do tipo Bicep Curls:");
                            System.out.print("-> o maior numero de calorias gastas numa atividade deste tipo: ");
                            classe = BicepCurls.class;
                            calorias = model.recordMaisCalorias(classe);
                            System.out.println(calorias);
                            System.out.print("-> o maior numero de repeticoes numa atividade deste tipo: ");
                            rep = model.recordMaisRepeticoes(classe);
                            System.out.println(rep);
                            System.out.print("-> o maior peso usado numa atividade deste tipo: ");
                            peso = model.recordMaisPeso(classe);
                            System.out.println(peso);
                            menuSimulacao.pedeString("\nEnter para continuar");
                            break;
                    }
                    break;
                case 3 :    //opção "Consultar estatísticas"
                    this.runQueries();
                    break;
                case 4 :    //opção "Mostrar todas as informações"
                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
                    String str = this.model.mostraInfo();
                    System.out.println(str);
                    menuSimulacao.pedeString("Enter para continuar");
                    break;
            }
            menuSimulacao.runMenu();
            op = menuSimulacao.getOpcao();
        }
    }
    
    /**
     * Método runQueries que executa o menu de estatísticas
     */
    public void runQueries(){
        this.menuEstatisticas.runMenu();
        int op = this.menuEstatisticas.getOpcao();
        while (op!=0){
            switch (op){
                case 1 :    //opção "Utilizador com mais calorias gastas"
                    this.estatisticaEntreDatas(op);
                    break;
                case 2 :    //opção "Utilizador com mais atividades realizadas"
                    this.estatisticaEntreDatas(op);
                    break;
                case 3 :    //opção "Atividade mais realizada"
                    String resposta = this.model.atividadeMaisRealizada();
                    System.out.println(resposta);
                    menuEstatisticas.pedeString("Enter para continuar");
                    break;
                case 4 :    //opção "Total de kilómetros percorridos"
                    this.estatisticaUtilizadorEntreDatas(op);
                    break;
                case 5 :    //opção "Metros de altimetria acumulados"
                    this.estatisticaUtilizadorEntreDatas(op);
                    break;
                case 6 :    //opção "Plano de treino mais exigente"
                    this.estatisticaEntreDatas(op);
                    break;
                case 7 :    //opção "Atividades de um utilizador"
                    int codUser = menuEstatisticas.pedeInt("Insira o código do utilizador");
                    if (!model.existeUtilizador(codUser)){
                        menuEstatisticas.pedeString("Utilizador não existe.\nEnter para continuar");
                        break;
                    }
                    int d = 0;
                    while(d!=1&&d!=2){
                        d = this.menuSetup.pedeInt("1: Mostrar atividades já realizadas\n2: Mostrar todas as atividades");
                    }
                    String res;
                    if (d==1) res = this.model.atividadesExecutadasUtilizador(codUser);
                    else res = this.model.atividadesUtilizador(codUser);
                    System.out.print(res);
                    menuEstatisticas.pedeString("Enter para continuar");
                    break;
            }
            menuEstatisticas.runMenu();
            op = menuEstatisticas.getOpcao();
        }
    }
}
