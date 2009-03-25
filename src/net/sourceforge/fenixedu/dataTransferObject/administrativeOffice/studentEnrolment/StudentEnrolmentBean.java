package net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.studentEnrolment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.student.IStudentCurricularPlanBean;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.enrolment.DegreeModuleToEnrol;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;

public class StudentEnrolmentBean implements Serializable, IStudentCurricularPlanBean {

    private DomainReference<StudentCurricularPlan> studentCurricularPlan;
    private DomainReference<ExecutionSemester> executionSemester;
    private List<DomainReference<CurriculumModule>> curriculumModules;
    private List<DegreeModuleToEnrol> degreeModulesToEnrol;
    private CurriculumModuleBean curriculumModuleBean;
    private boolean canEnrolWithoutRules = false;

    public StudentCurricularPlan getStudentCurricularPlan() {
	return (this.studentCurricularPlan == null) ? null : this.studentCurricularPlan.getObject();
    }

    public void setStudentCurricularPlan(StudentCurricularPlan studentCurricularPlan) {
	this.studentCurricularPlan = (studentCurricularPlan != null) ? new DomainReference<StudentCurricularPlan>(
		studentCurricularPlan) : null;
    }

    public ExecutionSemester getExecutionPeriod() {
	return (this.executionSemester == null) ? null : this.executionSemester.getObject();
    }

    public void setExecutionPeriod(ExecutionSemester executionSemester) {
	this.executionSemester = (executionSemester != null) ? new DomainReference<ExecutionSemester>(executionSemester) : null;
    }

    public List<CurriculumModule> getCurriculumModules() {
	if (this.curriculumModules == null) {
	    return new ArrayList<CurriculumModule>();
	}

	List<CurriculumModule> result = new ArrayList<CurriculumModule>();
	for (DomainReference<CurriculumModule> curriculumModule : this.curriculumModules) {
	    result.add(curriculumModule.getObject());
	}

	return result;
    }

    public void setCurriculumModules(List<CurriculumModule> curriculumModules) {
	if (curriculumModules == null) {
	    this.curriculumModules = null;
	} else {
	    this.curriculumModules = new ArrayList<DomainReference<CurriculumModule>>();
	    for (CurriculumModule curriculumModule : curriculumModules) {
		this.curriculumModules.add(new DomainReference<CurriculumModule>(curriculumModule));
	    }
	}
    }

    public List<DegreeModuleToEnrol> getDegreeModulesToEnrol() {
	return degreeModulesToEnrol;
    }

    public void setDegreeModulesToEnrol(List<DegreeModuleToEnrol> degreeModulesToEnrol) {
	this.degreeModulesToEnrol = degreeModulesToEnrol;
    }

    public CurriculumModuleBean getCurriculumModuleBean() {
	return curriculumModuleBean;
    }

    public void setCurriculumModuleBean(CurriculumModuleBean curriculumModuleBean) {
	this.curriculumModuleBean = curriculumModuleBean;
    }

    public Set<CurriculumModule> getInitialCurriculumModules() {
	return getInitialCurriculumModules(getCurriculumModuleBean());
    }

    private Set<CurriculumModule> getInitialCurriculumModules(CurriculumModuleBean curriculumModuleBean) {
	Set<CurriculumModule> result = new HashSet<CurriculumModule>();
	if (curriculumModuleBean.getCurricularCoursesEnroled().isEmpty() && curriculumModuleBean.getGroupsEnroled().isEmpty()) {
	    result.add(curriculumModuleBean.getCurriculumModule());
	}

	for (CurriculumModuleBean moduleBean : curriculumModuleBean.getCurricularCoursesEnroled()) {
	    result.add(moduleBean.getCurriculumModule());
	}

	for (CurriculumModuleBean moduleBean : curriculumModuleBean.getGroupsEnroled()) {
	    result.addAll(getInitialCurriculumModules(moduleBean));
	}

	return result;
    }

    public ExecutionYear getExecutionYear() {
	return getExecutionPeriod().getExecutionYear();
    }

    public boolean getCanEnrolWithoutRules() {
	return canEnrolWithoutRules;
    }

    public void canEnrolWithoutRules(boolean value) {
	canEnrolWithoutRules = value;
    }

}
