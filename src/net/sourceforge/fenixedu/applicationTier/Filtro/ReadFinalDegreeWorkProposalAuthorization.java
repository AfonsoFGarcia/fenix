/*
 * Created on 2004/04/06
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.coordinator.ExecutionDegreeCoordinatorAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.util.FinalDegreeWorkProposalStatus;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

/**
 * @author Luis Cruz
 *  
 */
public class ReadFinalDegreeWorkProposalAuthorization extends Filtro {

    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
        IUserView id = getRemoteUser(request);
        Integer finalDegreeWorkProposalOID = (Integer) request.getServiceParameters().parametersArray()[0];
        if (finalDegreeWorkProposalOID != null) {
            Proposal proposal = (Proposal) persistentObject.readByOID(Proposal.class,
                    finalDegreeWorkProposalOID);
            if (proposal != null) {
                if (proposal.getStatus() != null
                        && proposal.getStatus().equals(FinalDegreeWorkProposalStatus.PUBLISHED_STATUS)) {
                    return;
                }
                if (proposal.getOrientator() != null && proposal.getOrientator().getPerson() != null
                        && proposal.getOrientator().getPerson().getUsername() != null && id != null
                        && proposal.getOrientator().getPerson().getUsername().equals(id.getUtilizador())) {
                    return;
                }
                if (proposal.getCoorientator() != null
                        && proposal.getCoorientator().getPerson() != null
                        && proposal.getCoorientator().getPerson().getUsername() != null
                        && id != null
                        && proposal.getCoorientator().getPerson().getUsername().equals(
                                id.getUtilizador())) {
                    return;
                }
                for (final ExecutionDegree executionDegree : proposal.getScheduleing().getExecutionDegrees()) {
                    if (ExecutionDegreeCoordinatorAuthorizationFilter.verifyCondition(id, executionDegree.getIdInternal())) {
                        return;
                    }
                }
            }
        }
        throw new NotAuthorizedFilterException();
    }

}