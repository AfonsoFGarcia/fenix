package net.sourceforge.fenixedu.domain.enrolment;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors.CurricularRuleLevel;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;

public class EnrolmentContext {

    private StudentCurricularPlan studentCurricularPlan;

    private ExecutionPeriod executionPeriod;

    private Set<IDegreeModuleToEvaluate> degreeModulesToEvaluate;

    private List<CurriculumModule> curriculumModulesToRemove;

    private CurricularRuleLevel curricularRuleLevel;

    private Person responsiblePerson;

    public EnrolmentContext(final Person responsiblePerson, final StudentCurricularPlan studentCurricularPlan,
	    final ExecutionPeriod executionPeriod, final Set<IDegreeModuleToEvaluate> degreeModulesToEnrol,
	    final List<CurriculumModule> curriculumModulesToRemove, final CurricularRuleLevel curricularRuleLevel) {

	this.responsiblePerson = responsiblePerson;

	this.studentCurricularPlan = studentCurricularPlan;

	this.degreeModulesToEvaluate = new HashSet<IDegreeModuleToEvaluate>();
	for (final IDegreeModuleToEvaluate moduleToEnrol : degreeModulesToEnrol) {
	    if (curriculumModulesToRemove.contains(moduleToEnrol.getCurriculumGroup())) {
		throw new DomainException(
			"error.StudentCurricularPlan.cannot.remove.enrollment.on.curriculum.group.because.other.enrollments.depend.on.it",
			moduleToEnrol.getCurriculumGroup().getName().getContent());
	    }

	    this.addDegreeModuleToEvaluate(moduleToEnrol);
	}

	this.executionPeriod = executionPeriod;
	this.curriculumModulesToRemove = curriculumModulesToRemove;
	this.curricularRuleLevel = curricularRuleLevel;
    }

    public Set<IDegreeModuleToEvaluate> getDegreeModulesToEvaluate() {
	return degreeModulesToEvaluate;
    }

    public Set<IDegreeModuleToEvaluate> getAllChildDegreeModulesToEvaluateFor(final DegreeModule degreeModule) {
	final Set<IDegreeModuleToEvaluate> result = new HashSet<IDegreeModuleToEvaluate>();
	for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : this.degreeModulesToEvaluate) {
	    if (degreeModule.hasDegreeModule(degreeModuleToEvaluate.getDegreeModule())) {
		result.add(degreeModuleToEvaluate);
	    }
	}

	return result;
    }

    public void addDegreeModuleToEvaluate(final IDegreeModuleToEvaluate degreeModuleToEvaluate) {
	getDegreeModulesToEvaluate().add(degreeModuleToEvaluate);
    }

    public ExecutionPeriod getExecutionPeriod() {
	return executionPeriod;
    }

    public void setExecutionPeriod(ExecutionPeriod executionPeriod) {
	this.executionPeriod = executionPeriod;
    }

    public StudentCurricularPlan getStudentCurricularPlan() {
	return studentCurricularPlan;
    }

    public Registration getRegistration() {
	return studentCurricularPlan.getRegistration();
    }

    public void setStudentCurricularPlan(StudentCurricularPlan studentCurricularPlan) {
	this.studentCurricularPlan = studentCurricularPlan;
    }

    public List<CurriculumModule> getToRemove() {
	return curriculumModulesToRemove;
    }

    public CurricularRuleLevel getCurricularRuleLevel() {
	return curricularRuleLevel;
    }

    public void setCurricularRuleLevel(CurricularRuleLevel curricularRuleLevel) {
	this.curricularRuleLevel = curricularRuleLevel;
    }

    public Person getResponsiblePerson() {
	return responsiblePerson;
    }

    public void setResponsiblePerson(Person responsiblePerson) {
	this.responsiblePerson = responsiblePerson;
    }

}
