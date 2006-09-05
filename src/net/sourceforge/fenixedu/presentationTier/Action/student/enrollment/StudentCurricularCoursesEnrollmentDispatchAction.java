package net.sourceforge.fenixedu.presentationTier.Action.student.enrollment;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.presentationTier.Action.degreeAdministrativeOffice.CurricularCoursesEnrollmentDispatchAction;

import org.apache.struts.action.DynaActionForm;

public class StudentCurricularCoursesEnrollmentDispatchAction extends
	CurricularCoursesEnrollmentDispatchAction {

    @Override
    protected Registration getStudent(IUserView userView, DynaActionForm form) {
	Registration registration = userView.getPerson().getStudentByUsername();
	if (registration == null) {
	    return userView.getPerson().getStudentByType(DegreeType.DEGREE);
	}
	return registration;
    }
}