package net.sourceforge.fenixedu.persistenceTierJDBC;

import net.sourceforge.fenixedu.domain.CostCenter;

/**
 * 
 * @author Fernanda Quit�rio e Tania Pous�o
 */
public interface ICentroCustoPersistente {
    public CostCenter lerCentroCusto(int codigoInterno);
}
