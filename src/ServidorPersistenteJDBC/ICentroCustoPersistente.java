package ServidorPersistenteJDBC;

import java.util.List;

import Dominio.CentroCusto;

/**
 * 
 * @author Fernanda Quit�rio e Tania Pous�o
 */
public interface ICentroCustoPersistente {
    public boolean alterarCentroCusto(CentroCusto centroCusto);

    public boolean escreverCentroCusto(CentroCusto centroCusto);

    public CentroCusto lerCentroCusto(String sigla);

    public CentroCusto lerCentroCusto(int codigoInterno);

    public List lerTodosCentrosCusto();
}