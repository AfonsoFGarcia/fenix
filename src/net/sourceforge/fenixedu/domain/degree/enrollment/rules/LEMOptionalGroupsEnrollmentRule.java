/*
 * Created on 10/Fev/2005
 *
 */
package net.sourceforge.fenixedu.domain.degree.enrollment.rules;

import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseGroup;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.enrollment.CurricularCourse2Enroll;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * @author Ricardo Rodrigues
 *  
 */
public class LEMOptionalGroupsEnrollmentRule implements IEnrollmentRule {

    private StudentCurricularPlan studentCurricularPlan;

    private ExecutionPeriod executionPeriod;

    public LEMOptionalGroupsEnrollmentRule(StudentCurricularPlan studentCurricularPlan,
            ExecutionPeriod executionPeriod) {
        this.studentCurricularPlan = studentCurricularPlan;
        this.executionPeriod = executionPeriod;
    }

    /*
     * (non-Javadoc)
     * 
     * @see Dominio.degree.enrollment.rules.IEnrollmentRule#apply(java.util.List)
     */
    public List apply(List curricularCoursesToBeEnrolledIn) {
    	List<CurricularCourseGroup> optionalCurricularCourseGroups = studentCurricularPlan.getBranch().getOptionalCurricularCourseGroups();
    	
    	CurricularCourseGroup optionalCurricularCourseGroup = (CurricularCourseGroup) CollectionUtils
    	.find(optionalCurricularCourseGroups, new Predicate() {
    		public boolean evaluate(Object arg0) {
    			CurricularCourseGroup ccg = (CurricularCourseGroup) arg0;
    			return ccg.getName().equalsIgnoreCase("Op��es 4�Ano 2�Sem");
    		}
    	});
    	
    	CurricularCourse firstOptionalCurricularCourse = optionalCurricularCourseGroup
    	.getCurricularCourses().get(0);
    	CurricularCourse secondOptionalCurricularCourse = optionalCurricularCourseGroup
    	.getCurricularCourses().get(1);
    	
    	if (studentCurricularPlan.isCurricularCourseEnrolled(firstOptionalCurricularCourse)
    			|| studentCurricularPlan.isCurricularCourseApproved(firstOptionalCurricularCourse)) {
    		curricularCoursesToBeEnrolledIn.add(transformToCurricularCourse2Enroll(
    				secondOptionalCurricularCourse, executionPeriod));
    	}
    	
    	else if (studentCurricularPlan.isCurricularCourseEnrolled(secondOptionalCurricularCourse)
    			|| studentCurricularPlan.isCurricularCourseApproved(secondOptionalCurricularCourse)) {
    		curricularCoursesToBeEnrolledIn.add(transformToCurricularCourse2Enroll(
    				firstOptionalCurricularCourse, executionPeriod));
    	}

        return curricularCoursesToBeEnrolledIn;
    }

    protected CurricularCourse2Enroll transformToCurricularCourse2Enroll(
            CurricularCourse curricularCourse, ExecutionPeriod currentExecutionPeriod) {
        return new CurricularCourse2Enroll(curricularCourse, studentCurricularPlan
                .getCurricularCourseEnrollmentType(curricularCourse, currentExecutionPeriod),
                new Boolean(true));
    }

}
