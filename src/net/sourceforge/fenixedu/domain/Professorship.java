package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship.ResponsibleForValidator;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship.ResponsibleForValidator.InvalidCategory;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship.ResponsibleForValidator.MaxResponsibleForExceed;
import net.sourceforge.fenixedu.domain.credits.event.ICreditsEventOriginator;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

/**
 * @author Jo�o Mota
 */
public class Professorship extends Professorship_Base implements ICreditsEventOriginator {

    public String toString() {
        String result = "Professorship :\n";
        result += "\n  - ExecutionCourse : " + getExecutionCourse();
        result += "\n  - Teacher : " + getTeacher();

        return result;
    }

    public boolean belongsToExecutionPeriod(IExecutionPeriod executionPeriod) {
        return this.getExecutionCourse().getExecutionPeriod().equals(executionPeriod);
    }

    public static IProfessorship create(Boolean responsibleFor, IExecutionCourse executionCourse,
            ITeacher teacher, Double hours) throws MaxResponsibleForExceed, InvalidCategory {

        if (responsibleFor == null || executionCourse == null || teacher == null)
            throw new NullPointerException();

        IProfessorship professorShip = new Professorship();
        professorShip.setHours((hours == null) ? new Double(0.0) : hours);
        
        if (responsibleFor.booleanValue()) {
            ResponsibleForValidator.getInstance().validateResponsibleForList(teacher, executionCourse,
                    professorShip);
        }
        professorShip.setResponsibleFor(responsibleFor);
        professorShip.setExecutionCourse(executionCourse);
        professorShip.setTeacher(teacher);

        return professorShip;
    }

    public void delete() {
        if (hasAnyAssociatedSummaries() || hasAnyAssociatedShiftProfessorship() || hasAnySupportLessons()) {
            throw new DomainException(this.getClass().getName(), "");
        }
        this.removeExecutionCourse();
        this.removeTeacher();

        super.deleteDomainObject();
    }

}
