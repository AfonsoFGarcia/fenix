package net.sourceforge.fenixedu.domain.phd.candidacy;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.caseHandling.StartActivity;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.caseHandling.Activity;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.phd.PhdProgramCandidacyProcessState;
import net.sourceforge.fenixedu.domain.student.Student;

import org.joda.time.LocalDate;

public class PhdProgramCandidacyProcess extends PhdProgramCandidacyProcess_Base {

    @StartActivity
    public static class CreateCandidacy extends Activity<PhdProgramCandidacyProcess> {

	@Override
	public void checkPreConditions(PhdProgramCandidacyProcess process, IUserView userView) {
	    // no precondition to check
	    if (!isMasterDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected PhdProgramCandidacyProcess executeActivity(PhdProgramCandidacyProcess process, IUserView userView, Object object) {
	    final PhdProgramCandidacyProcess result = new PhdProgramCandidacyProcess((PhdProgramCandidacyProcessBean) object);

	    result.setState(PhdProgramCandidacyProcessState.STAND_BY_WITH_MISSING_INFORMATION);

	    return result;

	}

    }

    public static class UploadDocuments extends Activity<PhdProgramCandidacyProcess> {

	@Override
	public void checkPreConditions(PhdProgramCandidacyProcess process, IUserView userView) {
	    // no precondition to check
	    if (!isMasterDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@SuppressWarnings("unchecked")
	@Override
	protected PhdProgramCandidacyProcess executeActivity(PhdProgramCandidacyProcess process, IUserView userView, Object object) {
	    final List<PhdCandidacyDocumentUploadBean> documents = (List<PhdCandidacyDocumentUploadBean>) object;

	    for (final PhdCandidacyDocumentUploadBean each : documents) {
		if (each.hasAnyInformation()) {
		    new PhdProgramCandidacyProcessDocument(process, each.getType(), each.getRemarks(), each.getFileContent(),
			    each.getFilename());
		}

	    }

	    return process;

	}

    }

    public static class DeleteDocument extends Activity<PhdProgramCandidacyProcess> {

	@Override
	public void checkPreConditions(PhdProgramCandidacyProcess process, IUserView userView) {
	    // no precondition to check
	    if (!isMasterDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@SuppressWarnings("unchecked")
	@Override
	protected PhdProgramCandidacyProcess executeActivity(PhdProgramCandidacyProcess process, IUserView userView, Object object) {
	    ((PhdProgramCandidacyProcessDocument) object).delete();

	    return process;
	}
    }

    public static class EditCandidacyDate extends Activity<PhdProgramCandidacyProcess> {

	@Override
	public void checkPreConditions(PhdProgramCandidacyProcess process, IUserView userView) {
	    // no precondition to check
	    if (!isMasterDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@SuppressWarnings("unchecked")
	@Override
	protected PhdProgramCandidacyProcess executeActivity(PhdProgramCandidacyProcess process, IUserView userView, Object object) {
	    return process.edit((LocalDate) object);
	}
    }

    static private boolean isMasterDegreeAdministrativeOfficeEmployee(IUserView userView) {
	return userView.hasRoleType(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE)
		&& userView.getPerson().getEmployeeAdministrativeOffice().isMasterDegree();
    }

    static private List<Activity> activities = new ArrayList<Activity>();
    static {
	activities.add(new UploadDocuments());
	activities.add(new DeleteDocument());
	activities.add(new EditCandidacyDate());
    }

    private PhdProgramCandidacyProcess(final PhdProgramCandidacyProcessBean candidacyProcessBean) {
	super();

	checkCandidacyDate(candidacyProcessBean.getExecutionYear(), candidacyProcessBean.getCandidacyDate());
	setCandidacyDate(candidacyProcessBean.getCandidacyDate());

	final Person person = candidacyProcessBean.getOrCreatePersonFromBean();
	new Student(person);
	person.setIstUsername();

	setCandidacy(new PHDProgramCandidacy(person));

	if (candidacyProcessBean.hasDegree()) {
	    getCandidacy().setExecutionDegree(candidacyProcessBean.getExecutionDegree());
	}

	new PhdProgramCandidacyEvent(AdministrativeOffice.readByAdministrativeOfficeType(AdministrativeOfficeType.MASTER_DEGREE),
		candidacyProcessBean.getOrCreatePersonFromBean(), this);
    }

    private void checkCandidacyDate(ExecutionYear executionYear, LocalDate candidacyDate) {
	check(candidacyDate, "error.phd.candidacy.PhdProgramCandidacyProcess.invalid.candidacy.date");
	if (!executionYear.containsDate(candidacyDate)) {
	    throw new DomainException(
		    "error.phd.candidacy.PhdProgramCandidacyProcess.executionYear.doesnot.contains.candidacy.date", candidacyDate
			    .toString("dd/MM/yyyy"), executionYear.getQualifiedName(), executionYear.getBeginDateYearMonthDay()
			    .toString("dd/MM/yyyy"), executionYear.getEndDateYearMonthDay().toString("dd/MM/yyyy"));
	}
    }

    @Override
    public boolean canExecuteActivity(IUserView userView) {
	return false;
    }

    @Override
    public List<Activity> getActivities() {
	return activities;
    }

    @Override
    public String getDisplayName() {
	return ResourceBundle.getBundle("resources/PhdResources").getString(getClass().getSimpleName());
    }

    private PhdProgramCandidacyProcess edit(final LocalDate candidacyDate) {
	checkCandidacyDate(getExecutionYear(), candidacyDate);
	setCandidacyDate(candidacyDate);
	return this;
    }

    private ExecutionYear getExecutionYear() {
	return getIndividualProgramProcess().getExecutionYear();
    }
}
