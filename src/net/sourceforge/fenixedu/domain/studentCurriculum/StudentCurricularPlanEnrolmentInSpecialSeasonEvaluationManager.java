package net.sourceforge.fenixedu.domain.studentCurriculum;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.curricularRules.EnrolmentInSpecialSeasonEvaluation;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.MaximumNumberOfECTSInSpecialSeasonEvaluation;
import net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors.CurricularRuleLevel;
import net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors.EnrolmentResultType;
import net.sourceforge.fenixedu.domain.enrolment.EnroledCurriculumModuleWrapper;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.student.StudentStatute;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;
import net.sourceforge.fenixedu.domain.studentCurriculum.StudentCurricularPlanEnrolmentPreConditions.EnrolmentPreConditionResult;

public class StudentCurricularPlanEnrolmentInSpecialSeasonEvaluationManager extends StudentCurricularPlanEnrolment {

    public StudentCurricularPlanEnrolmentInSpecialSeasonEvaluationManager(final EnrolmentContext enrolmentContext) {
	super(enrolmentContext);
    }

    @Override
    protected void assertEnrolmentPreConditions() {
	if (!isResponsiblePersonManager()
		&& !getRegistration().hasStateType(getExecutionYear(), RegistrationStateType.REGISTERED)) {
	    throw new DomainException("error.StudentCurricularPlan.cannot.enrol.with.registration.inactive");
	}

	super.assertEnrolmentPreConditions();
    }

    @Override
    protected void checkDebts() {
	if (getStudent().isAnyGratuityOrAdministrativeOfficeFeeAndInsuranceInDebt(getExecutionYear())) {
	    throw new DomainException("error.StudentCurricularPlan.cannot.enrol.with.debts.for.previous.execution.years");
	}
    }

    @Override
    protected void assertAcademicAdminOfficePreConditions() {

	checkEnrolmentWithoutRules();

	if (updateRegistrationAfterConclusionProcessPermissionEvaluated()) {
	    return;
	}
    }

    @Override
    protected void assertStudentEnrolmentPreConditions() {

	if (!getRegistrationsToEnrolByStudent(getResponsiblePerson().getStudent()).contains(getRegistration())) {
	    throw new DomainException("error.StudentCurricularPlan.student.is.not.allowed.to.perform.enrol");
	}

	if (!hasSpecialSeasonStatute()) {
	    throw new DomainException("error.StudentCurricularPlan.student.has.no.special.season.statute");
	}

	if (getCurricularRuleLevel() != CurricularRuleLevel.SPECIAL_SEASON_ENROLMENT) {
	    throw new DomainException("error.StudentCurricularPlan.invalid.curricular.rule.level");
	}

	final EnrolmentPreConditionResult result = StudentCurricularPlanEnrolmentPreConditions
		.checkEnrolmentPeriodsForSpecialSeason(getStudentCurricularPlan(), getExecutionSemester());

	if (!result.isValid()) {
	    throw new DomainException(result.message(), result.args());
	}
    }

    private Collection<Registration> getRegistrationsToEnrolByStudent(final Student student) {
	final Collection<Registration> registrations = new HashSet<Registration>();

	for (final Registration registration : student.getRegistrations()) {
	    if (isRegistrationEnrolmentByStudentAllowed(registration)) {

		if (registration.isActive() || isRegistrationAvailableToEnrol(registration)) {
		    registrations.add(registration);
		}
	    }
	}

	return registrations;
    }

    public boolean isRegistrationEnrolmentByStudentAllowed(final Registration registration) {
	return registration.getRegistrationAgreement().isEnrolmentByStudentAllowed()
		&& registration.getDegreeTypesToEnrolByStudent().contains(registration.getDegreeType());
    }

    private boolean isRegistrationAvailableToEnrol(final Registration registration) {
	return registration.hasAnyEnrolmentsIn(getExecutionYear())
		&& registration.getLastStudentCurricularPlan().hasExternalCycleCurriculumGroups();
    }

    @Override
    protected void unEnrol() {
	for (final CurriculumModule curriculumModule : enrolmentContext.getToRemove()) {
	    if (curriculumModule instanceof Enrolment) {
		final Enrolment enrolment = (Enrolment) curriculumModule;
		enrolment.deleteSpecialSeasonEvaluation();
	    } else {
		throw new DomainException(
			"StudentCurricularPlanEnrolmentInSpecialSeasonEvaluationManager.can.only.manage.enrolment.evaluations.of.enrolments");
	    }
	}
    }

    @Override
    protected void addEnroled() {
	for (final Enrolment enrolment : getStudentCurricularPlan().getSpecialSeasonEnrolments(getExecutionYear())) {
	    enrolmentContext.addDegreeModuleToEvaluate(new EnroledCurriculumModuleWrapper(enrolment, getExecutionSemester()));
	}
    }

    @Override
    protected Map<IDegreeModuleToEvaluate, Set<ICurricularRule>> getRulesToEvaluate() {
	final Map<IDegreeModuleToEvaluate, Set<ICurricularRule>> result = new HashMap<IDegreeModuleToEvaluate, Set<ICurricularRule>>();

	for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : enrolmentContext.getDegreeModulesToEvaluate()) {

	    if (degreeModuleToEvaluate.isEnroled() && degreeModuleToEvaluate.canCollectRules()) {
		final EnroledCurriculumModuleWrapper moduleEnroledWrapper = (EnroledCurriculumModuleWrapper) degreeModuleToEvaluate;

		if (moduleEnroledWrapper.getCurriculumModule() instanceof Enrolment) {
		    final Enrolment enrolment = (Enrolment) moduleEnroledWrapper.getCurriculumModule();

		    final Set<ICurricularRule> curricularRules = new HashSet<ICurricularRule>();
		    if (!enrolment.hasSpecialSeason()) {
			curricularRules.add(new EnrolmentInSpecialSeasonEvaluation(enrolment));
		    }
		    curricularRules.add(new MaximumNumberOfECTSInSpecialSeasonEvaluation());

		    result.put(degreeModuleToEvaluate, curricularRules);
		} else {
		    throw new DomainException(
			    "StudentCurricularPlanEnrolmentInSpecialSeasonEvaluationManager.can.only.manage.enrolment.evaluations.of.enrolments");
		}
	    }
	}

	return result;
    }

    @Override
    protected void performEnrolments(final Map<EnrolmentResultType, List<IDegreeModuleToEvaluate>> degreeModulesToEvaluate) {
	for (final Entry<EnrolmentResultType, List<IDegreeModuleToEvaluate>> entry : degreeModulesToEvaluate.entrySet()) {

	    for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : entry.getValue()) {
		if (degreeModuleToEvaluate.isEnroled()) {
		    final EnroledCurriculumModuleWrapper moduleEnroledWrapper = (EnroledCurriculumModuleWrapper) degreeModuleToEvaluate;

		    if (moduleEnroledWrapper.getCurriculumModule() instanceof Enrolment) {
			final Enrolment enrolment = (Enrolment) moduleEnroledWrapper.getCurriculumModule();

			if (!enrolment.hasSpecialSeason()) {
			    enrolment.createSpecialSeasonEvaluation(getResponsiblePerson().getEmployee());
			}
		    } else {
			throw new DomainException(
				"StudentCurricularPlanEnrolmentInSpecialSeasonEvaluationManager.can.only.manage.enrolment.evaluations.of.enrolments");
		    }
		}
	    }
	}
    }

    private boolean hasSpecialSeasonStatute() {
	List<StudentStatute> statutes = getResponsiblePerson().getStudent().getStudentStatutes();
	for (StudentStatute statute : statutes) {
	    if (!statute.getStatuteType().isSpecialSeasonGranted() && !statute.hasSeniorStatuteForRegistration(getRegistration()))
		continue;
	    if (!statute.isValidInExecutionPeriod(getExecutionSemester()))
		continue;

	    return true;

	}
	return false;
    }

}
