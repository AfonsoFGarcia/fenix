package ServidorPersistenteJDBC;

import java.util.ArrayList;

import Dominio.ParamJustificacao;

/**
 *
 * @author  Fernanda Quit�rio e Tania Pous�o
 */
public interface IParamJustificacaoPersistente {
  public boolean alterarParamJustificacao(ParamJustificacao paramJustificacao);
  public boolean apagarParamJustificacao(String sigla);
  public boolean escreverParamJustificacao(ParamJustificacao paramJustificacao);
	public ParamJustificacao lerParamJustificacao(int codigoInterno);
  public ParamJustificacao lerParamJustificacao(String sigla);
  public ArrayList lerParamJustificacoes(ArrayList listaJustificacoes);
  public ArrayList lerSiglasJustificacao();
  public ArrayList lerTodasParamJustificacoes(String ordem);
}