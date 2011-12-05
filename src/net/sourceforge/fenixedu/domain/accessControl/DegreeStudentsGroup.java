package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Set;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.util.BundleUtil;

public class DegreeStudentsGroup extends DegreeGroup {

    /**
         * 
         */
    private static final long serialVersionUID = -2591751862204681731L;

    public DegreeStudentsGroup(Degree object) {
	super(object);
    }

    @Override
    public Set<Person> getElements() {
	Set<Person> elements = super.buildSet();

	for (DegreeCurricularPlan degreeCurricularPlan : getDegree().getActiveDegreeCurricularPlans()) {
	    for (StudentCurricularPlan studentCurricularPlan : degreeCurricularPlan.getStudentCurricularPlans()) {
		if (studentCurricularPlan.isActive()) {
		    elements.add(studentCurricularPlan.getRegistration().getPerson());
		}
	    }
	}

	return super.freezeSet(elements);
    }

    @Override
    public boolean isMember(Person person) {
	if (person != null && person.hasStudent()) {
	    for (final Registration registration : person.getStudent().getRegistrationsSet()) {
		if (registration.isActive() && registration.getActiveStudentCurricularPlan().getDegree() == getDegree()) {
		    return true;
		}
	    }
	}

	return false;
    }


    @Override
    public String getName() {
	return BundleUtil.getStringFromResourceBundle("resources/GroupNameResources", "label.name." + getClass().getSimpleName(), getObject().getPresentationName());
    }

    public static class Builder extends DegreeGroup.DegreeGroupBuilder {

	public Group build(Object[] arguments) {
	    return new DegreeStudentsGroup(getDegree(arguments));
	}

    }
}
