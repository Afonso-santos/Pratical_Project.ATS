package Projeto;

/**
 * Classe ProgPrincipal, que corresponde ao programa principal, tem apenas o método main
 *
 * @author Grupo10
 * @version 10/05/24
 * Notas versão :
 */
public class ProgPrincipal
{
    private AppDesportiva controller;
    
    /**
     * Construtor vazio de ProgPrincipal
     */
    public ProgPrincipal()
    {
        this.controller = new AppDesportiva();
    }
    
    /**
     * Construtor de ProgPrincipal quando é passado o nome do ficheiro a carregar
     */
    public ProgPrincipal(String ficheiro)
    {
        this.controller = new AppDesportiva(ficheiro);
    }
    
    /**
     * Método main
     */
    public static void main(String[] args){
        if(args.length==0) new ProgPrincipal();
        else new ProgPrincipal(args[0]);
    }
}
