/*
 * Created on 24/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourseEditor;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author lmac1 modified by Fernanda Quitério
 */
public class InsertExecutionCourseAtExecutionPeriod {

    @Service
    public static void run(InfoExecutionCourseEditor infoExecutionCourse) throws FenixServiceException {

        final ExecutionSemester executionSemester =
                RootDomainObject.getInstance().readExecutionSemesterByOID(
                        infoExecutionCourse.getInfoExecutionPeriod().getIdInternal());
        if (executionSemester == null) {
            throw new DomainException("message.nonExistingExecutionPeriod");
        }

        final ExecutionCourse existentExecutionCourse =
                executionSemester.getExecutionCourseByInitials(infoExecutionCourse.getSigla());
        if (existentExecutionCourse != null) {
            throw new DomainException("error.manager.executionCourseManagement.acronym.exists",
                    existentExecutionCourse.getSigla(), executionSemester.getName(), executionSemester.getExecutionYear()
                            .getYear(), existentExecutionCourse.getName());
        }

        final ExecutionCourse executionCourse =
                new ExecutionCourse(infoExecutionCourse.getNome(), infoExecutionCourse.getSigla(), executionSemester,
                        infoExecutionCourse.getEntryPhase());
        executionCourse.createSite();
    }
}