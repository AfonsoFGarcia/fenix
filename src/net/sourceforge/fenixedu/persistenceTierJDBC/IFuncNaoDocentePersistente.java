package net.sourceforge.fenixedu.persistenceTierJDBC;

import net.sourceforge.fenixedu.domain.NonTeacherEmployee;

/**
 * 
 * @author Fernanda Quit�rio e Tania Pous�o
 */
public interface IFuncNaoDocentePersistente {

    public NonTeacherEmployee lerFuncNaoDocentePorNumMecanografico(int numMecanografico);

}