package ServidorPersistenteJDBC;

import java.util.List;

import Dominio.RegularizacaoMarcacaoPonto;

/**
 * 
 * @author Fernanda Quit�rio e Tania Pous�o
 */
public interface IRegularizacaoMarcacaoPontoPersistente {
    public boolean alterarRegularizacaoMarcacaoPonto(RegularizacaoMarcacaoPonto regularizacao);

    public boolean apagarRegularizacaoMarcacaoPonto(int chaveMarcacaoPonto);

    public boolean apagarRegularizacoesMarcacoesPonto();

    public boolean escreverRegularizacaoMarcacaoPonto(RegularizacaoMarcacaoPonto regularizacao);

    public boolean escreverRegularizacoesMarcacoesPonto(List listaRegularizacoes);

    public boolean existeRegularizacaoMarcacaoPonto(RegularizacaoMarcacaoPonto regularizacao);

    public RegularizacaoMarcacaoPonto lerRegularizacaoMarcacaoPonto(int chaveMarcacaoPonto);

    public List lerTodasRegularizacoes();

}