package net.sourceforge.fenixedu.domain;

import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.BothAreasAreTheSameServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.exceptions.FenixDomainException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.Specialization;
import net.sourceforge.fenixedu.util.StudentCurricularPlanState;
import net.sourceforge.fenixedu.util.enrollment.CurricularCourseEnrollmentType;

/**
 * @author David Santos in Jun 24, 2004
 */

public interface IStudentCurricularPlan extends IDomainObject {
    public IBranch getBranch();

    public Double getClassification();

    public Integer getCompletedCourses();

    public StudentCurricularPlanState getCurrentState();

    public IDegreeCurricularPlan getDegreeCurricularPlan();

    public IEmployee getEmployee();

    public Integer getEnrolledCourses();

    public List getEnrolments();

    public Double getGivenCredits();

    public String getObservations();

    public IBranch getSecundaryBranch();

    public Specialization getSpecialization();

    public Date getStartDate();

    public IStudent getStudent();

    public Date getWhen();

    public List getEnrollmentReports();

    public void setBranch(IBranch branch);

    public void setClassification(Double classification);

    public void setCompletedCourses(Integer completedCourses);

    public void setCurrentState(StudentCurricularPlanState currentState);

    public void setDegreeCurricularPlan(IDegreeCurricularPlan degreeCurricularPlan);

    public void setEmployee(IEmployee employee);

    public void setEnrolledCourses(Integer enrolledCourses);

    public void setEnrolments(List enrolments);

    public void setGivenCredits(Double givenCredits);

    public void setObservations(String observations);

    public void setSecundaryBranch(IBranch secundaryBranch);

    public void setSpecialization(Specialization specialization);

    public void setStartDate(Date startDate);

    public void setStudent(IStudent student);

    public void setWhen(Date when);

    public void setEnrollmentReports(List enrollmentReports);

    // -------------------------------------------------------------
    // BEGIN: Only for enrollment purposes
    // -------------------------------------------------------------
    public List getCurricularCoursesToEnroll(IExecutionPeriod executionPeriod) throws FenixDomainException;

    //	public List getAllStudentEnrolledEnrollments();
    public List getAllStudentEnrolledEnrollmentsInExecutionPeriod(IExecutionPeriod executionPeriod);

    //	public List getStudentTemporarilyEnrolledEnrollments();
    public Integer getMinimumNumberOfCoursesToEnroll();

    public Integer getMaximumNumberOfCoursesToEnroll();

    public Integer getMaximumNumberOfAcumulatedEnrollments();

    public int getNumberOfApprovedCurricularCourses();

    public int getNumberOfEnrolledCurricularCourses();

    public boolean isCurricularCourseApproved(ICurricularCourse curricularCourse);

    public boolean isCurricularCourseEnrolled(ICurricularCourse curricularCourse);

    public boolean isCurricularCourseEnrolledInExecutionPeriod(ICurricularCourse curricularCourse,
            IExecutionPeriod executionPeriod);

    public Integer getCurricularCourseAcumulatedEnrollments(ICurricularCourse curricularCourse);

    public List getNotNeedToEnrollCurricularCourses();

    public void setNotNeedToEnrollCurricularCourses(List notNeedToEnrollCurricularCourses);

    public boolean areNewAreasCompatible(IBranch specializationArea, IBranch secundaryArea)
            throws ExcepcaoPersistencia, BothAreasAreTheSameServiceException,
            InvalidArgumentsServiceException;

    public boolean getCanChangeSpecializationArea();

    public CurricularCourseEnrollmentType getCurricularCourseEnrollmentType(
            ICurricularCourse curricularCourse, IExecutionPeriod currentExecutionPeriod);

    public Integer getCreditsInSecundaryArea();

    public void setCreditsInSecundaryArea(Integer creditsInSecundaryArea);

    public Integer getCreditsInSpecializationArea();

    public void setCreditsInSpecializationArea(Integer creditsInSpecializationArea);
    
    public List getAprovedEnrolmentsInExecutionPeriod(final IExecutionPeriod executionPeriod);

    // -------------------------------------------------------------
    // END: Only for enrollment purposes
    // -------------------------------------------------------------

}