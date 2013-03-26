package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.person.RoleType;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

public class EditWrittenEvaluationAuthorization extends Filtro {

    @Override
    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
        final IUserView userView = getRemoteUser(request);

        if (!userView.hasRoleType(RoleType.RESOURCE_ALLOCATION_MANAGER)) {

            final Object[] arguments = getServiceCallArguments(request);
            final WrittenEvaluation writtenEvaluation = readWrittenEvaluation(arguments);

            if (writtenEvaluation.getWrittenEvaluationSpaceOccupations().size() > 0) {
                throw new NotAuthorizedFilterException("written.evaluation.has.alocated.rooms");
            }
        }
    }

    private WrittenEvaluation readWrittenEvaluation(final Object[] arguments) {
        final Integer writtenEvaluationID = getWrittenEvaluationID(arguments);
        return (WrittenEvaluation) rootDomainObject.readEvaluationByOID(writtenEvaluationID);
    }

    private Integer getWrittenEvaluationID(final Object[] arguments) {
        return (Integer) ((arguments.length == 2) ? arguments[1] : arguments[7]);
    }
}
