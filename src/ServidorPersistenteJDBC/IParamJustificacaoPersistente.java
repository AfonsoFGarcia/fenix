package ServidorPersistenteJDBC;

import java.util.List;

import Dominio.ParamJustificacao;

/**
 * 
 * @author Fernanda Quit�rio e Tania Pous�o
 */
public interface IParamJustificacaoPersistente {
    public boolean alterarParamJustificacao(ParamJustificacao paramJustificacao);

    public boolean apagarParamJustificacao(String sigla);

    public boolean escreverParamJustificacao(ParamJustificacao paramJustificacao);

    public List lerGruposParamJustificacoes();

    public ParamJustificacao lerParamJustificacao(int codigoInterno);

    public ParamJustificacao lerParamJustificacao(String sigla);

    public List lerParamJustificacoes(List listaJustificacoes);

    public List lerSiglasJustificacao();

    public List lerTipoDiasParamJustificacoes();

    public List lerTiposParamJustificacoes();

    public List lerTodasParamJustificacoes(String ordem);
}