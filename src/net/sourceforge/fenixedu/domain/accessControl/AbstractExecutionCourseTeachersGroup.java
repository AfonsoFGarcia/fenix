package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Collection;
import java.util.Set;

import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;

import org.apache.commons.collections.Transformer;

public abstract class AbstractExecutionCourseTeachersGroup extends ExecutionCourseGroup {

    protected static final long serialVersionUID = -4575035849468586468L;

    protected class ProfessorshipPersonTransformer implements Transformer {

	public Object transform(Object arg0) {
	    Professorship professorship = (Professorship) arg0;

	    return professorship.getTeacher().getPerson();
	}
    }

    public AbstractExecutionCourseTeachersGroup(ExecutionCourse executionCourse) {
	super(executionCourse);
    }

    @Override
    public int getElementsCount() {
	return hasExecutionCourse() ? this.getExecutionCourse().getProfessorshipsCount() : 0;
    }

    @Override
    public Set<Person> getElements() {
	final Set<Person> elements = super.buildSet();
	if (hasExecutionCourse()) {
	    final Collection<Professorship> professorships = getProfessorships();
	    final Collection<Person> persons = CollectionUtils.collect(professorships, new ProfessorshipPersonTransformer());
	    elements.addAll(persons);
	}

	return super.freezeSet(elements);
    }

    public abstract Collection<Professorship> getProfessorships();

    @Override
    public boolean isMember(Person person) {
	if (person != null && person.hasTeacher() && hasExecutionCourse()) {
	    for (final Professorship professorship : getProfessorships()) {
		if (professorship.getTeacher() == person.getTeacher()) {
		    return true;
		}
	    }
	}
	return false;
    }
}
