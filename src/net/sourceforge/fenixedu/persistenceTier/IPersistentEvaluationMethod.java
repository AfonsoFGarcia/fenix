/*
 * Created on 23/Abr/2003
 *
 * 
 */
package net.sourceforge.fenixedu.persistenceTier;

import net.sourceforge.fenixedu.domain.IEvaluationMethod;

/**
 * @author Jo�o Mota
 * 
 *  
 */
public interface IPersistentEvaluationMethod extends IPersistentObject {

    public IEvaluationMethod readByIdExecutionCourse(Integer executionCourseOID)
            throws ExcepcaoPersistencia;

}