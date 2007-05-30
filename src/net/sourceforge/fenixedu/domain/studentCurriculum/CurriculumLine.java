package net.sourceforge.fenixedu.domain.studentCurriculum;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public abstract class CurriculumLine extends CurriculumLine_Base {
    
    public CurriculumLine() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }
    
    public boolean isLeaf() {
    	return true;
    }
    
    @Override
    public boolean isRoot() {
        return false;
    }
    
    protected void validateDegreeModuleLink(CurriculumGroup curriculumGroup, CurricularCourse curricularCourse) {
    	if(!curriculumGroup.getDegreeModule().validate(curricularCourse)) {
    	    throw new DomainException("error.studentCurriculum.curriculumLine.invalid.curriculum.group");
    	}
    }
    
    @Override
    public List<Enrolment> getEnrolments() {
	return Collections.emptyList();
    }
    
    @Override
    public void collectDismissals(final List<Dismissal> result) {
	// nothing to do
    }
    
    @Override
    public StudentCurricularPlan getStudentCurricularPlan() {
        return hasCurriculumGroup() ? getCurriculumGroup().getStudentCurricularPlan() : null; 
    }

    @Override
    public boolean isAproved(CurricularCourse curricularCourse, ExecutionPeriod executionPeriod) {
        return false;
    }
    
    @Override
    public boolean isEnroledInExecutionPeriod(CurricularCourse curricularCourse, ExecutionPeriod executionPeriod) {
        return false;
    }
    
    @Override
    public boolean hasEnrolmentWithEnroledState(final CurricularCourse curricularCourse, final ExecutionPeriod executionPeriod) {
        return false;
    }
    
    public CurricularCourse getCurricularCourse() {
    	return (CurricularCourse) getDegreeModule();
    }
    
    public void setCurricularCourse(CurricularCourse curricularCourse) {
    	setDegreeModule(curricularCourse);
    }
    
    public boolean hasCurricularCourse() {
	return hasDegreeModule();
    }

    @Override
    public Enrolment findEnrolmentFor(final CurricularCourse curricularCourse, final ExecutionPeriod executionPeriod) {
        return null;
    }
    
    @Override
    public Set<IDegreeModuleToEvaluate> getDegreeModulesToEvaluate(ExecutionPeriod executionPeriod) {
        return Collections.emptySet();
    }
    
    @Override
    public Collection<Enrolment> getSpecialSeasonEnrolments(ExecutionYear executionYear) {
        return Collections.emptySet();
    }

}
