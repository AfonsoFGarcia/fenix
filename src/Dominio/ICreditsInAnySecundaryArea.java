package Dominio;

/**
 * @author David Santos Jan 14, 2004
 */

public interface ICreditsInAnySecundaryArea extends IDomainObject {
    public IEnrollment getEnrolment();

    public void setEnrolment(IEnrollment enrolment);

    public Integer getEnrolmentKey();

    public void setEnrolmentKey(Integer enrolmentKey);

    public Integer getGivenCredits();

    public void setGivenCredits(Integer givenCredits);

    public IStudentCurricularPlan getStudentCurricularPlan();

    public void setStudentCurricularPlan(IStudentCurricularPlan studentCurricularPlan);

    public Integer getStudentCurricularPlanKey();

    public void setStudentCurricularPlanKey(Integer studentCurricularPlanKey);
}