package ServidorPersistenteJDBC;

import java.sql.Timestamp;
import java.util.List;

import Dominio.MarcacaoPonto;
import Dominio.RegularizacaoMarcacaoPonto;

/**
 * 
 * @author Fernanda Quit�rio e Tania Pous�o
 */
public interface IMarcacaoPontoPersistente {
    public boolean alterarMarcacaoPonto(MarcacaoPonto marcacaoPonto);

    public boolean apagarMarcacaoPonto(int chaveMarcacao);

    public boolean apagarMarcacaoPonto(Timestamp dataMarcacao);

    public boolean apagarMarcacoesPonto();

    public List consultarMarcacoesPonto(List listaFuncionarios, List listaCartoes, List listaEstados,
            Timestamp dataInicio, Timestamp dataFim);

    public List consultarMarcacoesPontoErros(String estado); //n�o ser�

    // preciso!!

    public boolean escreverMarcacaoPonto(MarcacaoPonto marcacaoPonto);

    public boolean escreverMarcacoesPonto(List listaMarcacoesPonto);

    public MarcacaoPonto lerMarcacaoPonto(int chaveMarcacao);

    public MarcacaoPonto lerMarcacaoPonto(Timestamp dataMarcacao, int numFuncionario);

    public List lerMarcacoesPonto();

    public List lerMarcacoesPonto(int numCartao);

    public List obterMarcacoesPonto(int numFuncionario, int numCartao, Timestamp dataFim);

    public List obterMarcacoesPonto(int numCartao, Timestamp dataInicio, Timestamp dataFim);

    public int ultimoCodigoInterno();

    public boolean alterarMarcacaoPontoRegularizacao(RegularizacaoMarcacaoPonto regularizacao);

    public boolean alterarMarcacaoPontoEscreverRegularizacao(RegularizacaoMarcacaoPonto regularizacao);

    public int escreverMarcacaoPontoRegularizacao(MarcacaoPonto marcacaoPonto,
            RegularizacaoMarcacaoPonto regularizacao);
}