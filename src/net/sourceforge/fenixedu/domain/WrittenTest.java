/*
 * Created on 10/Out/2003
 *
 */
package net.sourceforge.fenixedu.domain;

import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.Checked;
import net.sourceforge.fenixedu.util.EvaluationType;

import org.joda.time.YearMonthDay;

/**
 * @author Ana e Ricardo
 * 
 */
public class WrittenTest extends WrittenTest_Base {

    public WrittenTest(Date testDate, Date testStartTime, Date testEndTime,
            List<ExecutionCourse> executionCoursesToAssociate,
            List<DegreeModuleScope> curricularCourseScopesToAssociate, List<AllocatableSpace> rooms,
            String description) {
        
    	super();
        checkEvaluationDate(testDate, executionCoursesToAssociate);
        setAttributesAndAssociateRooms(testDate, testStartTime, testEndTime,
                executionCoursesToAssociate, curricularCourseScopesToAssociate, rooms);

        this.setOjbConcreteClass(WrittenTest.class.getName());
        this.setDescription(description);
        checkIntervalBetweenEvaluations();
    }

    private void checkEvaluationDate(final Date writtenEvaluationDate,
            final List<ExecutionCourse> executionCoursesToAssociate) {

        for (final ExecutionCourse executionCourse : executionCoursesToAssociate) {
            if (executionCourse.getExecutionPeriod().getBeginDate().after(writtenEvaluationDate)
                    || executionCourse.getExecutionPeriod().getEndDate().before(writtenEvaluationDate)) {
                throw new DomainException("error.invalidWrittenTestDate");
            }
        }
    }

    public void edit(Date testDate, Date testStartTime, Date testEndTime,
            List<ExecutionCourse> executionCoursesToAssociate,
            List<DegreeModuleScope> curricularCourseScopesToAssociate, List<AllocatableSpace> rooms,
            String description) {
        
        checkEvaluationDate(testDate, executionCoursesToAssociate);

        this.getAssociatedExecutionCourses().clear();
        this.getAssociatedCurricularCourseScope().clear();

        setAttributesAndAssociateRooms(testDate, testStartTime, testEndTime, executionCoursesToAssociate, curricularCourseScopesToAssociate, rooms);
        this.setDescription(description);
        checkIntervalBetweenEvaluations();
    }

    @Override
    @Checked("WrittenTestPredicates.changeDatePredicate")
    public void setDayDate(Date date) {
        final IUserView requestor = AccessControl.getUserView();
        if (hasTimeTableManagerPrivledges(requestor) || hasCoordinatorPrivledges(requestor) || (isTeacher(requestor) && allowedPeriod(date))) {
            super.setDayDate(date);
        } else {
            throw new DomainException("not.authorized.to.set.this.date");
        }
    }

    private boolean isTeacher(IUserView requestor) {
	Person person = requestor.getPerson();	
	Teacher teacher = person.getTeacher();
	if(teacher != null) {
	    for (ExecutionCourse executionCourse : getAssociatedExecutionCourses()) {
		if(teacher.hasProfessorshipForExecutionCourse(executionCourse)) {
		   return true; 
		}
	    }	    	
	}
	return false;
    }

    private boolean allowedPeriod(final Date date) {
        final YearMonthDay yearMonthDay = new YearMonthDay(date.getTime());
        for (final ExecutionCourse executionCourse : getAssociatedExecutionCourses()) {
            final ExecutionPeriod executionPeriod = executionCourse.getExecutionPeriod();
            final ExecutionYear executionYear = executionCourse.getExecutionPeriod().getExecutionYear();
            for (final CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCourses()) {
                final DegreeCurricularPlan degreeCurricularPlan = curricularCourse.getDegreeCurricularPlan();
                final ExecutionDegree executionDegree = degreeCurricularPlan.getExecutionDegreeByYear(executionYear);
                final Date startExamsPeriod;
                if (executionPeriod.getSemester().intValue() == 1) {
                    startExamsPeriod = executionDegree.getPeriodExamsFirstSemester().getStart();
                } else if (executionPeriod.getSemester().intValue() == 2) {
                    startExamsPeriod = executionDegree.getPeriodExamsSecondSemester().getStart();
                } else {
                    throw new DomainException("unsupported.execution.period.semester");
                }
                if (!new YearMonthDay(startExamsPeriod.getTime()).isAfter(yearMonthDay)) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean hasCoordinatorPrivledges(final IUserView requestor) {
        if (requestor.hasRoleType(RoleType.COORDINATOR)) {
            final Person person = requestor.getPerson();
            if (person != null) {
                for (final Coordinator coordinator : person.getCoordinators()) {
                    final ExecutionDegree executionDegree = coordinator.getExecutionDegree();
                    for (final ExecutionCourse executionCourse : getAssociatedExecutionCourses()) {
                        if (executionCourse.getExecutionPeriod().getExecutionYear() == executionDegree.getExecutionYear()) {
                            final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
                            for (final CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCourses()) {
                                if (degreeCurricularPlan == curricularCourse.getDegreeCurricularPlan()) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean hasTimeTableManagerPrivledges(final IUserView requestor) {
        return requestor.hasRoleType(RoleType.RESOURCE_ALLOCATION_MANAGER);
    }
    
    public EvaluationType getEvaluationType() {
        return EvaluationType.TEST_TYPE;
    }

}
