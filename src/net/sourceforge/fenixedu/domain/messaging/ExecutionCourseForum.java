package net.sourceforge.fenixedu.domain.messaging;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accessControl.Group;

public class ExecutionCourseForum extends ExecutionCourseForum_Base {

    public ExecutionCourseForum() {
        super();
        setOjbConcreteClass(this.getClass().getName());
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public ExecutionCourseForum(Person owner, String name, String description, Group readersGroup,
            Group writersGroup) {
        super();
        setOjbConcreteClass(this.getClass().getName());
        setRootDomainObject(RootDomainObject.getInstance());
        init(owner, name, description, readersGroup, writersGroup);
    }

    @Override
    public void setExecutionCourse(ExecutionCourse executionCourse) {
        if (this.getName() != null) {
            executionCourse.checkIfCanAddForum(this.getName());
        }

        super.setExecutionCourse(executionCourse);
    }

    @Override
    public void setName(String name) {
        if (this.getExecutionCourse() != null) {
            getExecutionCourse().checkIfCanAddForum(name);
        }

        super.setName(name);
    }

}
