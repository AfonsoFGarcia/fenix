package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.predicates.AcademicPredicates;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import pt.utl.ist.berserk.logic.filterManager.exceptions.FilterException;

public class AcademicCurriculumsViewAuthorization extends Filtro {

    public final static AcademicCurriculumsViewAuthorization instance = new AcademicCurriculumsViewAuthorization();

    @Override
    final public void execute(ServiceRequest request) throws FilterException, Exception {
        if (!AcademicPredicates.MANAGE_MARKSHEETS.evaluate(AccessControl.getUserView().getPerson())) {
            throw new NotAuthorizedFilterException();
        }
    }

}
