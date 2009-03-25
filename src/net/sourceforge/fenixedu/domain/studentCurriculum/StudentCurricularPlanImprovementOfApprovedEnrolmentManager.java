package net.sourceforge.fenixedu.domain.studentCurriculum;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.accessControl.academicAdminOffice.AdministrativeOfficePermission;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.ImprovementOfApprovedEnrolment;
import net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors.EnrolmentResultType;
import net.sourceforge.fenixedu.domain.enrolment.EnroledCurriculumModuleWrapper;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class StudentCurricularPlanImprovementOfApprovedEnrolmentManager extends StudentCurricularPlanEnrolment {

    public StudentCurricularPlanImprovementOfApprovedEnrolmentManager(final EnrolmentContext enrolmentContext) {
	super(enrolmentContext);
    }

    @Override
    protected void assertEnrolmentPreConditions() {
	if (isResponsiblePersonManager()) {
	    return;
	}

	if (!hasRegistrationInValidState()) {
	    throw new DomainException("error.StudentCurricularPlan.cannot.enrol.with.registration.inactive");
	}

	if (getStudent().isAnyGratuityOrAdministrativeOfficeFeeAndInsuranceInDebt()) {
	    throw new DomainException("error.StudentCurricularPlan.cannot.enrol.with.debts.for.previous.execution.years");
	}

	final AdministrativeOfficePermission permission = getUpdateRegistrationAfterConclusionProcessPermission();
	if (permission != null) {
	    checkPermission(permission);
	}
    }

    private boolean hasRegistrationInValidState() {
	return getRegistration().isRegistered(getExecutionYear())
		|| getRegistration().isRegistered(getExecutionYear().getPreviousExecutionYear());
    }

    @Override
    protected void unEnrol() {
	for (final CurriculumModule curriculumModule : enrolmentContext.getToRemove()) {
	    if (curriculumModule instanceof Enrolment) {
		final Enrolment enrolment = (Enrolment) curriculumModule;
		enrolment.unEnrollImprovement(getExecutionSemester());
	    } else {
		throw new DomainException(
			"StudentCurricularPlanImprovementOfApprovedEnrolmentManager.can.only.manage.enrolment.evaluations.of.enrolments");
	    }
	}
    }

    @Override
    protected void addEnroled() {
    }

    @Override
    protected Map<IDegreeModuleToEvaluate, Set<ICurricularRule>> getRulesToEvaluate() {
	final Map<IDegreeModuleToEvaluate, Set<ICurricularRule>> result = new HashMap<IDegreeModuleToEvaluate, Set<ICurricularRule>>();

	for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : enrolmentContext.getDegreeModulesToEvaluate()) {

	    if (degreeModuleToEvaluate.isEnroled() && degreeModuleToEvaluate.canCollectRules()) {
		final EnroledCurriculumModuleWrapper moduleEnroledWrapper = (EnroledCurriculumModuleWrapper) degreeModuleToEvaluate;

		if (moduleEnroledWrapper.getCurriculumModule() instanceof Enrolment) {
		    final Enrolment enrolment = (Enrolment) moduleEnroledWrapper.getCurriculumModule();
		    result.put(degreeModuleToEvaluate, Collections
			    .<ICurricularRule> singleton(new ImprovementOfApprovedEnrolment(enrolment)));

		} else {
		    throw new DomainException(
			    "StudentCurricularPlanImprovementOfApprovedEnrolmentManager.can.only.manage.enrolment.evaluations.of.enrolments");
		}
	    }
	}

	return result;
    }

    @Override
    protected void performEnrolments(final Map<EnrolmentResultType, List<IDegreeModuleToEvaluate>> degreeModulesToEvaluate) {
	Collection<Enrolment> toCreate = new HashSet<Enrolment>();

	for (final Entry<EnrolmentResultType, List<IDegreeModuleToEvaluate>> entry : degreeModulesToEvaluate.entrySet()) {

	    for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : entry.getValue()) {
		if (degreeModuleToEvaluate.isEnroled()) {
		    final EnroledCurriculumModuleWrapper moduleEnroledWrapper = (EnroledCurriculumModuleWrapper) degreeModuleToEvaluate;

		    if (moduleEnroledWrapper.getCurriculumModule() instanceof Enrolment) {
			final Enrolment enrolment = (Enrolment) moduleEnroledWrapper.getCurriculumModule();
			toCreate.add(enrolment);
		    } else {
			throw new DomainException(
				"StudentCurricularPlanImprovementOfApprovedEnrolmentManager.can.only.manage.enrolment.evaluations.of.enrolments");
		    }
		}
	    }
	}

	if (!toCreate.isEmpty()) {
	    getStudentCurricularPlan().createEnrolmentEvaluationForImprovement(toCreate, getResponsiblePerson().getEmployee(),
		    getExecutionSemester());
	}
    }

    @Override
    protected boolean isEnrolingInCycle(CycleCurriculumGroup cycle) {
	for (final IDegreeModuleToEvaluate dmte : enrolmentContext.getDegreeModulesToEvaluate()) {
	    if (dmte.isEnroled() && cycle.hasCurriculumModule(dmte.getCurriculumGroup())) {
		return true;
	    }
	}
	return false;
    }

}
