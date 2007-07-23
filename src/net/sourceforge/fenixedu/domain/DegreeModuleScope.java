package net.sourceforge.fenixedu.domain;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.domain.degreeStructure.Context;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

public abstract class DegreeModuleScope {

    static final protected Comparator<DegreeModuleScope> COMPARATOR_BY_ID = new Comparator<DegreeModuleScope>() {
        public int compare(DegreeModuleScope o1, DegreeModuleScope o2) {
	    return o1.getIdInternal().compareTo(o2.getIdInternal());
        }
    };

    public static final Comparator<DegreeModuleScope> COMPARATOR_BY_CURRICULAR_YEAR_AND_SEMESTER_AND_CURRICULAR_COURSE_NAME = new ComparatorChain();
    public static final Comparator<DegreeModuleScope> COMPARATOR_BY_NAME = new ComparatorChain();
    static {
        ((ComparatorChain) COMPARATOR_BY_CURRICULAR_YEAR_AND_SEMESTER_AND_CURRICULAR_COURSE_NAME).addComparator(new BeanComparator("curricularYear"));
        ((ComparatorChain) COMPARATOR_BY_CURRICULAR_YEAR_AND_SEMESTER_AND_CURRICULAR_COURSE_NAME).addComparator(new BeanComparator("curricularSemester"));
        ((ComparatorChain) COMPARATOR_BY_CURRICULAR_YEAR_AND_SEMESTER_AND_CURRICULAR_COURSE_NAME).addComparator(new BeanComparator("curricularCourse.name", Collator.getInstance()));
        ((ComparatorChain) COMPARATOR_BY_CURRICULAR_YEAR_AND_SEMESTER_AND_CURRICULAR_COURSE_NAME).addComparator(DegreeModuleScope.COMPARATOR_BY_ID);
        ((ComparatorChain) COMPARATOR_BY_NAME).addComparator(new BeanComparator("curricularCourse.name", Collator.getInstance()));
        ((ComparatorChain) COMPARATOR_BY_NAME).addComparator(DegreeModuleScope.COMPARATOR_BY_ID);
    }

    public abstract Integer getIdInternal();
    public abstract Integer getCurricularSemester();
    public abstract Integer getCurricularYear();    
    public abstract String getBranch();
    public abstract String getAnotation();
    public abstract CurricularCourse getCurricularCourse();
    public abstract boolean isActiveForExecutionPeriod(ExecutionPeriod executionPeriod);

    public static List<DegreeModuleScope> getDegreeModuleScopes(WrittenEvaluation writtenEvaluation) {
        return getDegreeModuleScopes(writtenEvaluation.getAssociatedCurricularCourseScope(), writtenEvaluation.getAssociatedContexts());
    }

    public static List<DegreeModuleScope> getDegreeModuleScopes(CurricularCourse curricularCourse) {
        return getDegreeModuleScopes(curricularCourse.getScopes(), curricularCourse.getParentContexts());
    }

    private static List<DegreeModuleScope> getDegreeModuleScopes(List<CurricularCourseScope> curricularCourseScopes, List<Context> contexts){
        List<DegreeModuleScope> degreeModuleScopes = new ArrayList<DegreeModuleScope>();
        for (CurricularCourseScope curricularCourseScope : curricularCourseScopes) {
            degreeModuleScopes.add(curricularCourseScope.getDegreeModuleScopeCurricularCourseScope());
        }
        for (Context context : contexts) {
            degreeModuleScopes.add(context.getDegreeModuleScopeContext());
        }
        return degreeModuleScopes;
    }

    public boolean isActiveForExecutionYear(ExecutionYear executionYear) {
        for (final ExecutionPeriod executionPeriod : executionYear.getExecutionPeriodsSet()) {
            if (isActiveForExecutionPeriod(executionPeriod)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isActive() {
	return isActiveForExecutionPeriod(ExecutionPeriod.readActualExecutionPeriod());
    }
    
    public boolean isActive(int year, int semester) {
	return getCurricularYear().intValue() == year && getCurricularSemester().intValue() == semester;
    }
    
    public boolean isFirstSemester() {
    	return (this.getCurricularSemester().intValue() == 1);
    }
    
    public boolean isSecondSemester() {
    	return (this.getCurricularSemester().intValue() == 2);
    }

}
