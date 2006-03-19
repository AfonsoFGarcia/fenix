package net.sourceforge.fenixedu.domain.degree.finalProject;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.credits.event.ICreditsEventOriginator;

public class TeacherDegreeFinalProjectStudent extends TeacherDegreeFinalProjectStudent_Base implements
        ICreditsEventOriginator {
    
    public TeacherDegreeFinalProjectStudent() {
    	setRootDomainObject(RootDomainObject.getInstance());
    }

    public TeacherDegreeFinalProjectStudent(ExecutionPeriod executionPeriod, Teacher teacher,
            Student student) {
    	this();
        setExecutionPeriod(executionPeriod);
        setTeacher(teacher);
        setStudent(student);
    }

    public boolean belongsToExecutionPeriod(ExecutionPeriod executionPeriod) {
        return this.getExecutionPeriod().equals(executionPeriod);
    }
    
    public void delete() {
        setExecutionPeriod(null);
        setTeacher(null);
        setStudent(null);
        
        super.deleteDomainObject();
    }
}