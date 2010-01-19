package net.sourceforge.fenixedu.applicationTier.Servico.student.administrativeOfficeServices;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.studentCurriculum.NoCourseGroupCurriculumGroupType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class CreateExtraEnrolment extends FenixService {

    @Checked("RolePredicates.ACADEMIC_ADMINISTRATIVE_OFFICE_PREDICATE")
    @Service
    public static void run(final StudentCurricularPlan studentCurricularPlan, final ExecutionSemester executionSemester,
	    final CurricularCourse curricularCourse, final NoCourseGroupCurriculumGroupType groupType) {
	studentCurricularPlan.createNoCourseGroupCurriculumGroupEnrolment(curricularCourse, executionSemester, groupType,
		AccessControl.getPerson());
    }

}