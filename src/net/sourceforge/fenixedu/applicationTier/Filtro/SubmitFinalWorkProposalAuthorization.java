package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoProposalEditor;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.ScientificCommission;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Scheduleing;
import net.sourceforge.fenixedu.domain.person.RoleType;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

public class SubmitFinalWorkProposalAuthorization extends Filtro {

    @Override
    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
	final IUserView userView = (IUserView) request.getRequester();
	final InfoProposalEditor infoProposalEditor = (InfoProposalEditor) request.getServiceParameters().getParameter(0);
	if (infoProposalEditor.getIdInternal() != null) {
	    final Proposal proposal = rootDomainObject.readProposalByOID(infoProposalEditor.getIdInternal());
	    if (!authorized(userView.getPerson(), proposal)) {
		throw new NotAuthorizedFilterException();
	    }
	} else {
	    final Integer executionDegreeId = infoProposalEditor.getExecutionDegree().getIdInternal();
	    final ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(executionDegreeId);
	    final Scheduleing scheduleing = executionDegree.getScheduling();
	    if (!authorized(userView.getPerson(), scheduleing)) {
		throw new NotAuthorizedFilterException();
	    }
	}
    }

    private boolean authorized(final Person person, final Scheduleing scheduleing) {
	if ((person.hasRole(RoleType.TEACHER) || person.hasAnyProfessorships() || person.hasRole(RoleType.RESEARCHER))
		&& scheduleing.isInsideProposalSubmissionPeriod()) {
	    return true;
	}
	return isCoordinatorOrDepartmentAdminOffice(person, scheduleing);
    }

    private boolean authorized(final Person person, final Proposal proposal) {
	final Scheduleing scheduleing = proposal.getScheduleing();
	if (proposal.getOrientator() == person || proposal.getCoorientator() == person) {
	    if (scheduleing.isInsideProposalSubmissionPeriod()) {
		return true;
	    }
	}
	return isCoordinatorOrDepartmentAdminOffice(person, scheduleing);
    }

    private boolean isCoordinatorOrDepartmentAdminOffice(final Person person, final Scheduleing scheduleing) {
	for (final ExecutionDegree executionDegree : scheduleing.getExecutionDegreesSet()) {
	    if (isCoordinatorFor(executionDegree, person)) {
		return true;
	    }
	    if (isDepartmentAdminOfficeMemberFor(executionDegree, person)) {
		return true;
	    }
	}
	return false;
    }

    private boolean isCoordinatorFor(final ExecutionDegree executionDegree, final Person person) {
	for (final Coordinator coordinator : executionDegree.getCoordinatorsListSet()) {
	    if (coordinator.getPerson() == person) {
		return true;
	    }
	}
	for (final ScientificCommission scientificCommission : person.getScientificCommissionsSet()) {
	    if (executionDegree == scientificCommission.getExecutionDegree()
		    || (executionDegree.getDegreeCurricularPlan() == scientificCommission.getExecutionDegree()
			    .getDegreeCurricularPlan() && executionDegree.getExecutionYear() == scientificCommission
			    .getExecutionDegree().getExecutionYear().getPreviousExecutionYear())) {
		return true;
	    }
	}
	return false;
    }

    private boolean isDepartmentAdminOfficeMemberFor(final ExecutionDegree executionDegree, final Person person) {
	if (person.hasRole(RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE)) {
	    final Degree degree = executionDegree.getDegree();
	    for (final Department department : degree.getDepartmentsSet()) {
		if (personWorksInDepartment(department, person)) {
		    return true;
		}
	    }
	}
	return false;
    }

    private boolean personWorksInDepartment(final Department department, final Person person) {
	final Employee employee = person.getEmployee();
	return employee.getCurrentDepartmentWorkingPlace() == department;
    }

}
