package net.sourceforge.fenixedu.domain.studentCurriculum;

import java.util.Collection;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.dismissal.DismissalBean.SelectedCurricularCourse;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;

public class TemporarySubstitution extends TemporarySubstitution_Base {
    
    public TemporarySubstitution() {
        super();
    }
    
    public TemporarySubstitution(StudentCurricularPlan studentCurricularPlan,
	    Collection<SelectedCurricularCourse> dismissals, Collection<IEnrolment> enrolments) {
	init(studentCurricularPlan, dismissals, enrolments);
    }    
}
