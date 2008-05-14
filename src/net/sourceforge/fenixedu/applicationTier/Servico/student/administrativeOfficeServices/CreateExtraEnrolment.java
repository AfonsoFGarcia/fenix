package net.sourceforge.fenixedu.applicationTier.Servico.student.administrativeOfficeServices;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.studentCurriculum.NoCourseGroupCurriculumGroupType;

public class CreateExtraEnrolment extends Service {

    public void run(StudentCurricularPlan studentCurricularPlan, ExecutionSemester executionSemester,
	    CurricularCourse curricularCourse, NoCourseGroupCurriculumGroupType groupType) throws FenixServiceException {
	studentCurricularPlan.createNoCourseGroupCurriculumGroupEnrolment(curricularCourse, executionSemester, groupType);	
    }

}
