package Util;

/**
 * Implementa um NullObject para representar uma data nao 
 * disponivel
 *
 * @author  Ivo Brand�o
 */
public class DataIndisponivel extends java.util.Date {
    
    /** variaveis de construcao de data invalida */
    private static int ano = 0;
    private static int mes = 0;
    private static int dia = 1;
    
    /** variavel que contem valor de data invalida */
    private static java.util.Date valor = new java.util.Date(ano, mes, dia);
    
    /** mensagem a imprimir */
    private static String mensagem = new String("Data Indispon�vel");
    
    /** Creates a new instance of DataIndisponivel */
    public DataIndisponivel() {
        /* invoca construtor da classe mae */
        super(ano, mes, dia);
    }

    /** Identifica esta data como data invalida
     * @return string data indisponivel
     */
    public String toString() {
        return mensagem;
    }
    
    /** Retorna o valor utilizado para definir uma data invalida
     * @return Date(0, 0, 1) (1/1/1900)
     */
    public static java.util.Date getValor(){
        return valor;
    }
    
    public static boolean isDataIndisponivel(java.util.Date dataGenerica){
        boolean resultado = false;
        
        if (dataGenerica.equals(valor)) resultado = true;
        
        return resultado;
    }
    
}
