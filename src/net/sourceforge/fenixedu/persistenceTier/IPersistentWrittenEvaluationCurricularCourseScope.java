package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.ICurricularCourseScope;

/**
 * @author Fernanda Quit�rio Created on 16/Jun/2004
 *  
 */
public interface IPersistentWrittenEvaluationCurricularCourseScope extends IPersistentObject {

    public List readByCurricularCourseScope(ICurricularCourseScope curricularCourseScope)
            throws ExcepcaoPersistencia;

    public List readByCurricularCourseScopeList(List scopes) throws ExcepcaoPersistencia;
}