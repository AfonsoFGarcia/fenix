package net.sourceforge.fenixedu.domain.studentCurriculum;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import net.sourceforge.fenixedu.domain.Enrolment;
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

	if (!isResponsiblePersonManager() && !hasRegistrationInValidState()) {
	    throw new DomainException("error.StudentCurricularPlan.cannot.enrol.with.registration.inactive");
	}

	super.assertEnrolmentPreConditions();
    }

    private boolean hasRegistrationInValidState() {
	return getRegistration().isInRegisteredState(getExecutionYear())
		|| getRegistration().isInRegisteredState(getExecutionYear().getPreviousExecutionYear());
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

		    final Set<ICurricularRule> curricularRules = new HashSet<ICurricularRule>();
		    curricularRules.add(new ImprovementOfApprovedEnrolment(enrolment));

		    result.put(degreeModuleToEvaluate, curricularRules);
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

}
