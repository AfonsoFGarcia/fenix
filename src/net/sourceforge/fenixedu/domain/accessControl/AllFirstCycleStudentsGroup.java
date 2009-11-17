package net.sourceforge.fenixedu.domain.accessControl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;

public class AllFirstCycleStudentsGroup extends Group {

    private static final long serialVersionUID = 1L;

    public AllFirstCycleStudentsGroup() {
    }

    @Override
    public Set<Person> getElements() {
	Set<Person> elements = new HashSet<Person>();

	for (final Degree degree : RootDomainObject.getInstance().getDegreesSet()) {
	    if (degree.isBolonhaDegree() && degree.getDegreeType().hasCycleTypes(CycleType.FIRST_CYCLE)) {
		for (final DegreeCurricularPlan degreeCurricularPlan : degree.getDegreeCurricularPlansSet()) {
		    if (degreeCurricularPlan.isActive()) {
			for (final StudentCurricularPlan studentCurricularPlan : degreeCurricularPlan.getStudentCurricularPlansSet()) {
			    if (studentCurricularPlan.isActive()) {
				final CycleCurriculumGroup cycleCurriculumGroup = studentCurricularPlan.getCycle(CycleType.FIRST_CYCLE);
				if (cycleCurriculumGroup != null && !cycleCurriculumGroup.isConcluded()) {
				    elements.add(studentCurricularPlan.getPerson());
				}
			    }
			}
		    }
		}
	    }
	}

	return elements;
    }

    @Override
    protected Argument[] getExpressionArguments() {
	return null;
    }

}
