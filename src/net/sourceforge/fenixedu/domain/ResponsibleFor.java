package net.sourceforge.fenixedu.domain;

/**
 * @author Jo�o Mota
 * 
 *  
 */
public class ResponsibleFor extends ResponsibleFor_Base {

    public ResponsibleFor() {
    }

    public ResponsibleFor(ITeacher teacher, IExecutionCourse executionCourse) {
        setTeacher(teacher);
        setExecutionCourse(executionCourse);
    }

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof IResponsibleFor) {
            IResponsibleFor responsibleFor = (IResponsibleFor) obj;
            result = getTeacher().equals(responsibleFor.getTeacher());
            result &= getExecutionCourse().equals(responsibleFor.getExecutionCourse());
        }
        return result;
    }
}