package net.sourceforge.fenixedu.persistenceTierJDBC;

import java.util.List;

import net.sourceforge.fenixedu.domain.CostCenter;

/**
 * 
 * @author Fernanda Quit�rio e Tania Pous�o
 */
public interface ICentroCustoPersistente {
    public boolean alterarCentroCusto(CostCenter centroCusto);

    public boolean escreverCentroCusto(CostCenter centroCusto);

    public CostCenter lerCentroCusto(String sigla);

    public CostCenter lerCentroCusto(int codigoInterno);

    public List lerTodosCentrosCusto();
}