/*
 * Created on Feb 23, 2005
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;


import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class ReadExecutionCourseByCodeAndExecutionPeriodId {

    @Service
    public static InfoExecutionCourse run(Integer executionPeriodId, String code) throws ExcepcaoInexistente,
            FenixServiceException {
        InfoExecutionCourse infoExecCourse = null;
        final ExecutionSemester executionSemester = RootDomainObject.getInstance().readExecutionSemesterByOID(executionPeriodId);
        ExecutionCourse iExecCourse = executionSemester.getExecutionCourseByInitials(code);

        if (iExecCourse != null) {
            infoExecCourse = InfoExecutionCourse.newInfoFromDomain(iExecCourse);
        }

        return infoExecCourse;
    }

}