package Dominio;

import java.util.Date;
import java.util.List;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerAware;
import org.apache.ojb.broker.PersistenceBrokerException;

import Dominio.log.EnrolmentLog;
import Dominio.log.IEnrolmentLog;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.OJB.log.IPersistentEnrolmentLog;
import Util.EnrollmentState;
import Util.EnrolmentAction;
import Util.EnrolmentEvaluationType;
import Util.enrollment.EnrollmentCondition;

/**
 * @author dcs-rjao
 * 
 * 24/Mar/2003
 */

public class Enrolment extends DomainObject implements IEnrollment,
        PersistenceBrokerAware {
    private IStudentCurricularPlan studentCurricularPlan;

    private ICurricularCourse curricularCourse;

    private IExecutionPeriod executionPeriod;

    private EnrollmentState enrollmentState;

    private EnrolmentEvaluationType enrolmentEvaluationType;

    private Integer studentCurricularPlanKey;

    private Integer curricularCourseKey;

    private Integer keyExecutionPeriod;

    private List evaluations;

    private String ojbConcreteClass;

    private Date creationDate;

    private EnrollmentCondition condition;

    private Integer accumulatedWeight;

    private String createdBy;

    public Enrolment() {
        this.ojbConcreteClass = this.getClass().getName();
    }

    /**
     * @return Returns the curricularCourse.
     */
    public ICurricularCourse getCurricularCourse() {
        return curricularCourse;
    }

    /**
     * @param curricularCourse
     *            The curricularCourse to set.
     */
    public void setCurricularCourse(ICurricularCourse curricularCourse) {
        this.curricularCourse = curricularCourse;
    }

    /**
     * @return Returns the curricularCourseKey.
     */
    public Integer getCurricularCourseKey() {
        return curricularCourseKey;
    }

    /**
     * @param curricularCourseKey
     *            The curricularCourseKey to set.
     */
    public void setCurricularCourseKey(Integer curricularCourseKey) {
        this.curricularCourseKey = curricularCourseKey;
    }

    /**
     * @return Returns the enrolmentEvaluationType.
     */
    public EnrolmentEvaluationType getEnrolmentEvaluationType() {
        return enrolmentEvaluationType;
    }

    /**
     * @param enrolmentEvaluationType
     *            The enrolmentEvaluationType to set.
     */
    public void setEnrolmentEvaluationType(
            EnrolmentEvaluationType enrolmentEvaluationType) {
        this.enrolmentEvaluationType = enrolmentEvaluationType;
    }

    /**
     * @return Returns the enrollmentState.
     */
    public EnrollmentState getEnrollmentState() {
        return enrollmentState;
    }

    /**
     * @param enrollmentState
     *            The enrollmentState to set.
     */
    public void setEnrollmentState(EnrollmentState enrollmentState) {
        this.enrollmentState = enrollmentState;
    }

    /**
     * @return Returns the evaluations.
     */
    public List getEvaluations() {
        return evaluations;
    }

    /**
     * @param evaluations
     *            The evaluations to set.
     */
    public void setEvaluations(List evaluations) {
        this.evaluations = evaluations;
    }

    /**
     * @return Returns the executionPeriod.
     */
    public IExecutionPeriod getExecutionPeriod() {
        return executionPeriod;
    }

    /**
     * @param executionPeriod
     *            The executionPeriod to set.
     */
    public void setExecutionPeriod(IExecutionPeriod executionPeriod) {
        this.executionPeriod = executionPeriod;
    }

    /**
     * @return Returns the keyExecutionPeriod.
     */
    public Integer getKeyExecutionPeriod() {
        return keyExecutionPeriod;
    }

    /**
     * @param keyExecutionPeriod
     *            The keyExecutionPeriod to set.
     */
    public void setKeyExecutionPeriod(Integer keyExecutionPeriod) {
        this.keyExecutionPeriod = keyExecutionPeriod;
    }

    /**
     * @return Returns the ojbConcreteClass.
     */
    public String getOjbConcreteClass() {
        return ojbConcreteClass;
    }

    /**
     * @param ojbConcreteClass
     *            The ojbConcreteClass to set.
     */
    public void setOjbConcreteClass(String ojbConcreteClass) {
        this.ojbConcreteClass = ojbConcreteClass;
    }

    /**
     * @return Returns the studentCurricularPlan.
     */
    public IStudentCurricularPlan getStudentCurricularPlan() {
        return studentCurricularPlan;
    }

    /**
     * @param studentCurricularPlan
     *            The studentCurricularPlan to set.
     */
    public void setStudentCurricularPlan(
            IStudentCurricularPlan studentCurricularPlan) {
        this.studentCurricularPlan = studentCurricularPlan;
    }

    /**
     * @return Returns the studentCurricularPlanKey.
     */
    public Integer getStudentCurricularPlanKey() {
        return studentCurricularPlanKey;
    }

    /**
     * @param studentCurricularPlanKey
     *            The studentCurricularPlanKey to set.
     */
    public void setStudentCurricularPlanKey(Integer studentCurricularPlanKey) {
        this.studentCurricularPlanKey = studentCurricularPlanKey;
    }

    public boolean equals(Object obj) {
        boolean result = false;

        if (obj instanceof IEnrollment) {
            IEnrollment enrolment = (IEnrollment) obj;

            result = this.getStudentCurricularPlan().equals(
                    enrolment.getStudentCurricularPlan())
                    && this.getCurricularCourse().equals(
                            enrolment.getCurricularCourse())
                    && this.getExecutionPeriod().equals(
                            enrolment.getExecutionPeriod());
        }
        return result;
    }

    public String toString() {
        String result = "[" + this.getClass().getName() + "; ";
        result += "idInternal = " + super.getIdInternal() + "; ";
        result += "studentCurricularPlan = " + this.getStudentCurricularPlan()
                + "; ";
        result += "enrollmentState = " + this.getEnrollmentState() + "; ";
        result += "execution Period = " + this.getExecutionPeriod() + "; ";
        result += "curricularCourse = " + this.getCurricularCourse() + "]\n";
        return result;
    }

    /**
     * @return Returns the creationDate.
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * @param creationDate
     *            The creationDate to set.
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * @return Returns the condition.
     */
    public EnrollmentCondition getCondition() {
        return condition;
    }

    /**
     * @param condition
     *            The condition to set.
     */
    public void setCondition(EnrollmentCondition condition) {
        this.condition = condition;
    }

    /**
     * @return Returns the accumulatedWeight.
     */
    public Integer getAccumulatedWeight() {
        return accumulatedWeight;
    }

    /**
     * @param accumulatedWeight
     *            The accumulatedWeight to set.
     */
    public void setAccumulatedWeight(Integer accumulatedWeight) {
        this.accumulatedWeight = accumulatedWeight;
    }

    /**
     * @return Returns the createdBy.
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy
     *            The createdBy to set.
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ojb.broker.PersistenceBrokerAware#afterDelete(org.apache.ojb.broker.PersistenceBroker)
     */
    public void afterDelete(PersistenceBroker arg0)
            throws PersistenceBrokerException {

        createNewEnrolmentLog(EnrolmentAction.UNENROL, arg0);

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ojb.broker.PersistenceBrokerAware#afterInsert(org.apache.ojb.broker.PersistenceBroker)
     */
    public void afterInsert(PersistenceBroker arg0)
            throws PersistenceBrokerException {

        createNewEnrolmentLog(EnrolmentAction.ENROL, arg0);

    }

    private void createNewEnrolmentLog(EnrolmentAction action, PersistenceBroker arg0)
            throws PersistenceBrokerException {
        try {
            IPersistentEnrolmentLog persistentEnrolmentLog = SuportePersistenteOJB
                    .getInstance().getIPersistentEnrolmentLog();
        } catch (ExcepcaoPersistencia e) {
            throw new PersistenceBrokerException(e);
        }
        IEnrolmentLog enrolmentLog = new EnrolmentLog();
        //persistentEnrolmentLog.simpleLockWrite(enrolmentLog);
        enrolmentLog.setDate(new Date());
        enrolmentLog.setAction(action);
        enrolmentLog.setCurricularCourse(this.getCurricularCourse());
        enrolmentLog.setStudent(this.getStudentCurricularPlan().getStudent());
        arg0.store(enrolmentLog);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ojb.broker.PersistenceBrokerAware#afterLookup(org.apache.ojb.broker.PersistenceBroker)
     */
    public void afterLookup(PersistenceBroker arg0)
            throws PersistenceBrokerException {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ojb.broker.PersistenceBrokerAware#afterUpdate(org.apache.ojb.broker.PersistenceBroker)
     */
    public void afterUpdate(PersistenceBroker arg0)
            throws PersistenceBrokerException {
       
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ojb.broker.PersistenceBrokerAware#beforeDelete(org.apache.ojb.broker.PersistenceBroker)
     */
    public void beforeDelete(PersistenceBroker arg0)
            throws PersistenceBrokerException {
        
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ojb.broker.PersistenceBrokerAware#beforeInsert(org.apache.ojb.broker.PersistenceBroker)
     */
    public void beforeInsert(PersistenceBroker arg0)
            throws PersistenceBrokerException {
        
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ojb.broker.PersistenceBrokerAware#beforeUpdate(org.apache.ojb.broker.PersistenceBroker)
     */
    public void beforeUpdate(PersistenceBroker arg0)
            throws PersistenceBrokerException {
        
    }
}