package ServidorPersistenteJDBC;

import java.util.List;

import Dominio.Regime;

/**
 * 
 * @author Fernanda Quit�rio e Tania Pous�o
 */
public interface IRegimePersistente {
    public boolean alterarRegime(Regime regime);

    public boolean apagarRegime(String designacao);

    public boolean escreverRegime(Regime regime);

    public List lerDesignacaoRegimes(List listaIdRegimes);

    public Regime lerRegime(String designacao);

    public Regime lerRegime(int codigoInterno);

    public List lerRegimes();
}