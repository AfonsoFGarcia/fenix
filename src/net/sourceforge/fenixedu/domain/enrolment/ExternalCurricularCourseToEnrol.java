package net.sourceforge.fenixedu.domain.enrolment;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;

public class ExternalCurricularCourseToEnrol extends DegreeModuleToEnrol {

    private DomainReference<CurricularCourse> curricularCourse;

    public ExternalCurricularCourseToEnrol(final CurriculumGroup curriculumGroup, final CurricularCourse curricularCourse,
	    final ExecutionSemester executionSemester) {
	super(curriculumGroup, null, executionSemester);
	this.curricularCourse = new DomainReference<CurricularCourse>(curricularCourse);
    }

    @Override
    public boolean canCollectRules() {
	return false;
    }

    @Override
    public double getAccumulatedEctsCredits(ExecutionSemester executionSemester) {
	return getStudentCurricularPlan().getAccumulatedEctsCredits(executionSemester, (CurricularCourse) getDegreeModule());
    }

    @Override
    public Context getContext() {
	throw new DomainException("error.ExternalCurricularCourseToEnrol.doesnot.have.context");
    }

    @Override
    public Set<ICurricularRule> getCurricularRulesFromCurriculumGroup(ExecutionSemester executionSemester) {
	return Collections.emptySet();
    }

    @Override
    public List<CurricularRule> getCurricularRulesFromDegreeModule(ExecutionSemester executionSemester) {
	return Collections.emptyList();
    }

    public CurricularCourse getDegreeModule() {
	return (this.curricularCourse == null) ? null : this.curricularCourse.getObject();
    }

    @Override
    public Double getEctsCredits(ExecutionSemester executionSemester) {
	return getDegreeModule().getEctsCredits(executionSemester);
    }

    @Override
    public String getKey() {
	final StringBuilder stringBuilder = new StringBuilder();
	stringBuilder.append(getCurriculumGroup().getClass().getName()).append(":").append(getCurriculumGroup().getIdInternal())
		.append(",").append(this.getDegreeModule().getClass().getName()).append(":").append(getName()).append(",")
		.append(getExecutionPeriod().getClass().getName()).append(":").append(getExecutionPeriod().getIdInternal());
	return stringBuilder.toString();
    }

    @Override
    public String getName() {
	return getDegreeModule().getName(getExecutionPeriod());
    }

    @Override
    public String getYearFullLabel() {
	throw new DomainException("error.ExternalCurricularCourseToEnrol.doesnot.have.context");
    }

    @Override
    public boolean isAnnualCurricularCourse(final ExecutionYear executionYear) {
	return getDegreeModule().isAnual(executionYear);
    }

    @Override
    public boolean isOptionalCurricularCourse() {
	return getDegreeModule().isOptionalCurricularCourse();
    }

    @Override
    public boolean equals(final Object obj) {
	if (obj instanceof ExternalCurricularCourseToEnrol) {
	    final ExternalCurricularCourseToEnrol degreeModuleToEnrol = (ExternalCurricularCourseToEnrol) obj;
	    return getDegreeModule().equals(degreeModuleToEnrol.getDegreeModule())
		    && getCurriculumGroup().equals(degreeModuleToEnrol.getCurriculumGroup());
	}
	return false;
    }

    @Override
    public int hashCode() {
	return getDegreeModule().hashCode() + getCurriculumGroup().hashCode();
    }

}
