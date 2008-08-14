package net.sourceforge.fenixedu.applicationTier.Filtro;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.finalDegreeWork.FinalDegreeWorkGroup;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupProposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupStudent;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.util.StudentCurricularPlanIDDomainType;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

public class FinalDegreeWorkOrientatorForCandidacy extends AccessControlFilter {

    @Override
    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
	final IUserView userView = (IUserView) request.getRequester();
	if (userView == null) {
	    throw new NotAuthorizedFilterException("authentication.not.provided");
	}

	final StudentCurricularPlanIDDomainType studentCurricularPlanIDDomainType = (StudentCurricularPlanIDDomainType) request
		.getServiceParameters().parametersArray()[1];
	final Integer studentCurricularPlanId = studentCurricularPlanIDDomainType.getId();
	if (studentCurricularPlanId == null) {
	    throw new NotAuthorizedFilterException("no.student.specified");
	} else if (studentCurricularPlanId.intValue() < 0) {
	    return;
	}

	final Person person = userView.getPerson();
	final List<Proposal> orientatingProposals = person.getAssociatedProposalsByOrientator();
	final List<Proposal> coorientatingProposals = person.getAssociatedProposalsByCoorientator();

	final List<Proposal> proposals = new ArrayList(orientatingProposals.size() + coorientatingProposals.size());
	proposals.addAll(orientatingProposals);
	proposals.addAll(coorientatingProposals);

	for (final Proposal proposal : proposals) {
	    for (final GroupProposal groupProposal : proposal.getGroupProposals()) {
		final FinalDegreeWorkGroup group = groupProposal.getFinalDegreeDegreeWorkGroup();
		for (final GroupStudent groupStudent : group.getGroupStudents()) {
		    final Registration registration = groupStudent.getRegistration();
		    for (final StudentCurricularPlan studentCurricularPlan : registration.getStudentCurricularPlans()) {
			if (studentCurricularPlan.getIdInternal().equals(studentCurricularPlanId)) {
			    return;
			}
		    }
		}
	    }
	}

	throw new NotAuthorizedFilterException();
    }

}
