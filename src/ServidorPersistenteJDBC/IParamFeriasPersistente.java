package ServidorPersistenteJDBC;

import java.util.ArrayList;

import Dominio.ParamFerias;

/**
 *
 * @author  Fernanda Quit�rio e Tania Pous�o
 */
public interface IParamFeriasPersistente {
  public boolean alterarParamFerias(ParamFerias tipoFerias);
  public boolean apagarParamFerias(int codigoInterno);
  public boolean apagarParamFeriasPorSigla(String sigla);
  public boolean escreverParamFerias(ParamFerias tipoFerias);
  public ParamFerias lerParamFerias(int codigoInterno);
  public ParamFerias lerParamFeriasPorSigla(String sigla);
  public ArrayList lerTodosParamFerias();  
}
