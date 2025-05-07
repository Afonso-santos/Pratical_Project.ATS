package Projeto;
import java.util.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * Classe Menu, que implementa uma interface em modo texto
 *
 * @author Grupo10
 * @version 10/05/24
 * Notas versão :
 */
public class Menu
{
    private List<String> opcoes; //opção 0 nesta lista é o nome do menu e a última é o nome da opção de saída
    private int op;
    
    /**
     * Construtor de Menu, recebe a lista com as opções e o nome do menu
     */
    public Menu(String[] opcoes){
        this.opcoes = Arrays.asList(opcoes);
        this.op=-1;
    }
    
    public int getOpcao(){
        return this.op;
    }
    
    /**
     * Método mostraMenu, que imprime as opções disponíveis
     */
    private void mostraMenu(){
        //limpa a tela
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        //imprime o nome do menu
        System.out.print("-------------");
        System.out.print(this.opcoes.get(0));
        System.out.print("-------------\n");
        int i;
        for (i=1; i<this.opcoes.size()-1; i++){
            System.out.print("Opção ");
            System.out.print(i);
            System.out.print(": ");
            System.out.println(this.opcoes.get(i));
        }
        System.out.print("Opção 0: ");
        System.out.println(this.opcoes.get(i));
    }
    
    /**
     * Método lerOpcao, que lê a opção selecionada
     */
    private int lerOpcao(){
        int op;
        Scanner scan = new Scanner(System.in);
        System.out.print("Opção: ");
        try{
            op = scan.nextInt();
        }
        catch (InputMismatchException e){
            op = -1;
        }
        if(op<0 || op>this.opcoes.size()-2){
            pedeString("Opção inválida.\nEnter para continuar");
            op=-1;
        }
        return op;
    }
    
    /**
     * Método runMenu, que mostra o menu e lê uma opção
     */
    public void runMenu(){
        this.op = -1;
        while (this.op == -1){
            this.mostraMenu();
            this.op = this.lerOpcao();
        }
    }
    
    /**
    * Método pedeString
    * @param mensagem: mensagem para mostrar ao utilizador
    * @returns próxima linha inserida
    */
    public String pedeString(String mensagem){
        System.out.println(mensagem);
        String input;
        Scanner scan = new Scanner(System.in);
        input = scan.nextLine();
        return input;
    }
    
    /**
    * Método pedeInt
    * @param mensagem: mensagem para mostrar ao utilizador
    * @returns próximo inteiro inserido
    */
    public int pedeInt(String mensagem){
        System.out.println(mensagem);
        int input = -1;
        Scanner scan;
        while(input == -1){
        try{
            scan = new Scanner(System.in);
            input = scan.nextInt();
        }
        catch (InputMismatchException e){
            System.out.println("Inserir um número inteiro");
        }
        }
        return input;
    }
    
    /**
    * Método pedeDouble
    * @param mensagem: mensagem para mostrar ao utilizador
    * @returns próximo número inserido
    */
    public double pedeDouble(String mensagem){
        System.out.println(mensagem);
        double input = -1;
        Scanner scan;
        while(input == -1){
        try{
            scan = new Scanner(System.in);
            input = scan.nextDouble();
        }
        catch (InputMismatchException e){
            System.out.println("Inserir um número");
        }
        }
        return input;
    }
    
    /**
    * Método pedeData
    * @param mensagem: mensagem para mostrar ao utilizador
    * @returns próxima data inserida
    */
    public LocalDate pedeData(String mensagem){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        System.out.println(mensagem);
        LocalDate input = null;
        Scanner scan;
        while(input == null){
        try{
            scan = new Scanner(System.in);
            input = LocalDate.parse(scan.nextLine(), formatter);
        }
        catch (Exception e){
            System.out.println("Inserir uma data no formato dia/mês/ano");
        }
        }
        return input;
    }
    
    /**
    * Método pedeTempo
    * @param mensagem: mensagem para mostrar ao utilizador
    * @returns próximo tempo inserido
    */
    public LocalTime pedeTempo(String mensagem){
        System.out.println(mensagem);
        LocalTime input = null;
        Scanner scan;
        while(input == null){
        try{
            scan = new Scanner(System.in);
            input = LocalTime.parse(scan.nextLine());
        }
        catch (Exception e){
            System.out.println("Inserir uma duração no formato horas:minutos ou horas:minutos:segundos");
        }
        }
        return input;
    }
    
    /**
    * Método pedeDataHora
    * @param mensagem: mensagem para mostrar ao utilizador
    * @returns próxima data com hora inserida
    */
    public LocalDateTime pedeDataHora(String mensagem){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        System.out.println(mensagem);
        LocalDateTime input = null;
        Scanner scan;
        while(input == null){
        try{
            scan = new Scanner(System.in);
            input = LocalDateTime.parse(scan.nextLine(), formatter);
        }
        catch (Exception e){
            System.out.println("Inserir data e hora no formato dia/mês/ano horas:minutos:segundos");
        }
        }
        return input;
    }
}

