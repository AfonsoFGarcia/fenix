package middleware.dataClean.personFilter.ServidorErros;


public class Erros {
  public static final String SEM_DADOS = "Distrito, Concelho e Freguesia sem Dados";
  public static final String ERRO_FREG = "Freguesia inv�lida para este concelho";
  public static final String ERRO_CONC = "Concelho inv�lido para este distrito";
  public static final String ERRO_DIST = "Distrito inv�lido";
  public static final String ERRO_CF_DIST = "S� tem freguesia: N�o � poss�vel inferir distrito";
  public static final String ERRO_CF_CONC = "S� tem freguesia: N�o � poss�vel inferir concelho";
  public static final String ERRO_CF_FREG = "S� tem freguesia: N�o � poss�vel inferir concelho";

  public Erros() {
  }

}