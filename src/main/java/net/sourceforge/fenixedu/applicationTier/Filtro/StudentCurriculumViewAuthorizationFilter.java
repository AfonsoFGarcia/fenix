package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.predicates.AcademicPredicates;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import pt.utl.ist.berserk.logic.filterManager.exceptions.FilterException;

public class StudentCurriculumViewAuthorizationFilter extends Filtro {

    public final static StudentCurriculumViewAuthorizationFilter instance = new StudentCurriculumViewAuthorizationFilter();

    @Override
    final public void execute(Object[] parameters) throws FilterException, Exception {
        if (!AcademicPredicates.VIEW_FULL_STUDENT_CURRICULUM.evaluate(AccessControl.getUserView().getPerson())) {
            throw new NotAuthorizedFilterException();
        }
    }

}
