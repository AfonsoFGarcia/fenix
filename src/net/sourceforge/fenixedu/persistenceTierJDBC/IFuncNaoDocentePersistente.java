package net.sourceforge.fenixedu.persistenceTierJDBC;

import net.sourceforge.fenixedu.domain.Administrative;

/**
 * 
 * @author Fernanda Quit�rio e Tania Pous�o
 */
public interface IFuncNaoDocentePersistente {

    public Administrative lerFuncNaoDocentePorNumMecanografico(int numMecanografico);

}