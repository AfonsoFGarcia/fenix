package net.sourceforge.fenixedu.applicationTier.Servico.teacher.finalDegreeWork;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.finalDegreeWork.FinalDegreeWorkGroup;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupProposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupStudent;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Scheduleing;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

/**
 * Service that transposes the given proposal to another execution year.
 * 
 * The service is responsible for replicating all the relations and groups
 * associated with the proposal.
 * 
 * 
 * @author Joao Carvalho (joao.pedro.carvalho@ist.utl.pt)
 * 
 */

public class TransposeFinalDegreeWorkProposalToExecutionYear {

    /**
     * Executes the service.
     * 
     * 
     * @param originalProposalOID
     * @param targetExecutionYear
     * @return
     * @throws FenixServiceException
     */

    @Checked("RolePredicates.TEACHER_PREDICATE")
    @Service
    public static Proposal run(String originalProposalOID, ExecutionYear targetExecutionYear) throws FenixServiceException {

	Long originalProposalOIDLong;

	try {
	    originalProposalOIDLong = Long.parseLong(originalProposalOID);
	} catch (NumberFormatException e) {
	    throw new FenixServiceException("Invalid OID");
	}

	Proposal originalProposal = Proposal.fromOID(originalProposalOIDLong);

	if (originalProposal == null || targetExecutionYear == null)
	    throw new FenixServiceException("The arguments provided were invalid!");

	/*
	 * Scheduling lookup
	 */

	Scheduleing originalScheduleing = originalProposal.getScheduleing();

	ExecutionDegree oneExecutionDegree = originalScheduleing.getExecutionDegrees().get(0);

	ExecutionDegree executionDegree = ExecutionDegree.getByDegreeCurricularPlanAndExecutionYear(
		oneExecutionDegree.getDegreeCurricularPlan(), targetExecutionYear);

	if (executionDegree == null) {
	    throw new FenixServiceException("There's no such degree for the given execution year!");
	}

	Scheduleing newScheduleing = executionDegree.getScheduling();

	if (newScheduleing == null) {
	    throw new ProposalPeriodNotDefined();
	}

	/*
	 * Check whether the current Scheduling is compatible with the original
	 * one (there were some cases where the original scheduling contained
	 * several degrees grouped together, and the new one only contained some
	 * of them).
	 */

	// TODO Get information by other means, instead of just returning an
	// error...

	if (originalScheduleing.getExecutionDegreesCount() != newScheduleing.getExecutionDegreesCount()) {
	    throw new FenixServiceException("The target Scheduling is not compatible with the source Scheduling");
	}

	List<ExecutionDegree> newDegrees = newScheduleing.getExecutionDegrees();

	for (Iterator<ExecutionDegree> originalIterator = originalScheduleing.getExecutionDegreesIterator(); originalIterator
		.hasNext();) {

	    ExecutionDegree originalDegree = originalIterator.next();

	    boolean found = false;

	    for (int i = 0; i < newDegrees.size(); i++) {
		if (newDegrees.get(i).getDegreeCurricularPlan().equals(originalDegree.getDegreeCurricularPlan())) {
		    found = true;
		    break;
		}
	    }

	    if (!found)
		throw new FenixServiceException("The target Scheduling is not compatible with the source Scheduling");

	}

	/*
	 * New object, copy trivial properties
	 */

	Proposal newProposal = new Proposal();

	newProposal.setCompanionMail(originalProposal.getCompanionMail());
	newProposal.setCompanionName(originalProposal.getCompanionName());
	newProposal.setCompanionPhone(originalProposal.getCompanionPhone());
	newProposal.setCompanyAdress(originalProposal.getCompanyAdress());
	newProposal.setCompanyName(originalProposal.getCompanyName());

	newProposal.setOrientator(originalProposal.getOrientator());
	newProposal.setCoorientator(originalProposal.getCoorientator());

	newProposal.setCoorientatorsCreditsPercentage(originalProposal.getCoorientatorsCreditsPercentage());
	newProposal.setDegreeType(originalProposal.getDegreeType());
	newProposal.setDeliverable(originalProposal.getDeliverable());
	newProposal.setDescription(originalProposal.getDescription());

	newProposal.setFraming(originalProposal.getFraming());
	newProposal.setLocation(originalProposal.getLocation());

	newProposal.setMaximumNumberOfGroupElements(originalProposal.getMaximumNumberOfGroupElements());
	newProposal.setMinimumNumberOfGroupElements(originalProposal.getMinimumNumberOfGroupElements());
	newProposal.setObjectives(originalProposal.getObjectives());
	newProposal.setObservations(originalProposal.getObservations());

	newProposal.setOrientatorsCreditsPercentage(originalProposal.getOrientatorsCreditsPercentage());
	newProposal.setRequirements(originalProposal.getRequirements());
	newProposal.setTitle(originalProposal.getTitle());
	newProposal.setUrl(originalProposal.getUrl());

	newProposal.setStatus(originalProposal.getStatus());

	newProposal.setProposalNumber(newScheduleing.getCurrentProposalNumber());
	newScheduleing.setCurrentProposalNumber(newScheduleing.getCurrentProposalNumber() + 1);

	newProposal.setScheduleing(newScheduleing);

	Iterator<Branch> branchIterator = originalProposal.getBranches().iterator();
	while (branchIterator.hasNext()) {
	    Branch branch = branchIterator.next();
	    newProposal.addBranches(branch);
	}

	if (originalProposal.getGroupAttributed() != null) {

	    FinalDegreeWorkGroup newGroup = replicateWorkGroup(originalProposal.getGroupAttributed(), originalProposal,
		    newProposal, targetExecutionYear);

	    newProposal.setGroupAttributed(newGroup);
	}

	if (originalProposal.getGroupAttributedByTeacher() != null) {

	    FinalDegreeWorkGroup newGroup = replicateWorkGroup(originalProposal.getGroupAttributedByTeacher(), originalProposal,
		    newProposal, targetExecutionYear);

	    newProposal.setGroupAttributedByTeacher(newGroup);
	}

	return newProposal;

    }

    /**
     * 
     * Replicates the given FinalWorkGroup, creating a copy of it.
     * 
     * Note: this method does NOT connect the Group with the proposal. Such
     * operation must be made by the caller, who then determines whether the
     * group was attributed by the coordinator or the teacher.
     * 
     * @param originalGroup
     * @param newProposal
     * @param targetExecutionYear
     * @return
     */
    private static FinalDegreeWorkGroup replicateWorkGroup(FinalDegreeWorkGroup originalGroup, Proposal originalProposal,
	    Proposal newProposal, ExecutionYear targetExecutionYear) {

	FinalDegreeWorkGroup newWorkGroup = new FinalDegreeWorkGroup();

	newWorkGroup.setExecutionDegree(ExecutionDegree.getByDegreeCurricularPlanAndExecutionYear(originalGroup
		.getExecutionDegree().getDegreeCurricularPlan(), targetExecutionYear));

	for (Iterator<GroupProposal> proposalIterator = originalGroup.getGroupProposalsIterator(); proposalIterator.hasNext();) {
	    GroupProposal proposal = proposalIterator.next();

	    if (proposal.getFinalDegreeWorkProposal() == originalProposal) {

		GroupProposal newGroupProposal = new GroupProposal();
		newGroupProposal.setFinalDegreeDegreeWorkGroup(newWorkGroup);
		newGroupProposal.setFinalDegreeWorkProposal(newProposal);
		newGroupProposal.setOrderOfPreference(proposal.getOrderOfPreference());

	    }
	}

	for (Iterator<GroupStudent> studentsIterator = originalGroup.getGroupStudentsIterator(); studentsIterator.hasNext();) {
	    GroupStudent groupStudent = studentsIterator.next();

	    if (groupStudent.getFinalDegreeWorkProposalConfirmation() == originalProposal) {

		GroupStudent newStudent = new GroupStudent();
		newStudent.setFinalDegreeDegreeWorkGroup(newWorkGroup);
		newStudent.setFinalDegreeWorkProposalConfirmation(newProposal);
		newStudent.setRegistration(groupStudent.getRegistration());
	    }
	}

	return newWorkGroup;
    }

    /**
     * 
     * Transposes the proposal to the current execution year.
     * 
     * @param originalProposalOID
     * @return
     * @throws FenixServiceException
     */

    public static Proposal run(String originalProposalOID) throws FenixServiceException {

	ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();

	return run(originalProposalOID, currentExecutionYear);
    }

    @SuppressWarnings("serial")
    public static class ProposalAlreadyTransposed extends FenixServiceException {

	public ProposalAlreadyTransposed() {
	    super();
	}

	public ProposalAlreadyTransposed(int errorType) {
	    super(errorType);
	}

	public ProposalAlreadyTransposed(String s) {
	    super(s);
	}

	public ProposalAlreadyTransposed(Throwable cause) {
	    super(cause);
	}

	public ProposalAlreadyTransposed(String message, Throwable cause) {
	    super(message, cause);
	}

    }

    @SuppressWarnings("serial")
    public static class ProposalPeriodNotDefined extends FenixServiceException {

	public ProposalPeriodNotDefined() {
	    super();
	}
    }
}
