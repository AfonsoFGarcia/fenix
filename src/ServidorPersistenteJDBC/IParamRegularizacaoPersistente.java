package ServidorPersistenteJDBC;

import java.util.ArrayList;

import Dominio.ParamRegularizacao;

/**
 *
 * @author  Fernanda Quit�rio e Tania Pous�o
 */
public interface IParamRegularizacaoPersistente {
  public boolean alterarParamRegularizacao(ParamRegularizacao paramRegularizacao);
  public boolean apagarParamRegularizacao(String sigla);
  public boolean escreverParamRegularizacao(ParamRegularizacao paramRegularizacao);
  public ParamRegularizacao lerParamRegularizacao(int chaveParamRegularizacao);
  public ParamRegularizacao lerParamRegularizacao(String sigla); 
  public ArrayList lerTodasParamRegularizacoes();
}