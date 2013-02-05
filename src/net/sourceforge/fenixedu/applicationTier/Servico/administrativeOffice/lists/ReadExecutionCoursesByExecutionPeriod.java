package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.lists;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;

public class ReadExecutionCoursesByExecutionPeriod extends FenixService {

    public List run(Integer executionPeriodId) throws FenixServiceException {
        List allExecutionCoursesFromExecutionPeriod = null;
        List<InfoExecutionCourse> allExecutionCourses = null;

        ExecutionSemester executionSemester = rootDomainObject.readExecutionSemesterByOID(executionPeriodId);

        if (executionSemester == null) {
            throw new NonExistingServiceException("message.nonExistingExecutionPeriod", null);
        }
        allExecutionCoursesFromExecutionPeriod = executionSemester.getAssociatedExecutionCourses();

        if (allExecutionCoursesFromExecutionPeriod == null || allExecutionCoursesFromExecutionPeriod.isEmpty()) {
            return allExecutionCoursesFromExecutionPeriod;
        }
        allExecutionCourses = new ArrayList<InfoExecutionCourse>(allExecutionCoursesFromExecutionPeriod.size());
        Iterator iter = allExecutionCoursesFromExecutionPeriod.iterator();
        while (iter.hasNext()) {
            ExecutionCourse executionCourse = (ExecutionCourse) iter.next();
            allExecutionCourses.add(InfoExecutionCourse.newInfoFromDomain(executionCourse));
        }

        return allExecutionCourses;
    }
}