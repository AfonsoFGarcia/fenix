package net.sourceforge.fenixedu.domain;

import java.util.Calendar;
import java.util.Date;

import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.domain.curriculum.EnrolmentEvaluationType;
import net.sourceforge.fenixedu.domain.curriculum.GradeFactory;
import net.sourceforge.fenixedu.domain.curriculum.IGrade;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.EnrolmentEvaluationState;
import net.sourceforge.fenixedu.util.MarkType;

/**
 * @author dcs-rjao 24/Mar/2003
 */

public class EnrolmentEvaluation extends EnrolmentEvaluation_Base implements Comparable {

    private static final String RECTIFICATION = "RECTIFICA��O";

	public EnrolmentEvaluation() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}
	
	public EnrolmentEvaluation(Enrolment enrolment, EnrolmentEvaluationType type) {
		this();
		setEnrolment(enrolment);
		setEnrolmentEvaluationType(type);
	}

    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object o) {
        EnrolmentEvaluation enrolmentEvaluation = (EnrolmentEvaluation) o;
        EnrollmentState myEnrolmentState = this.getEnrollmentStateByGrade(this.getGrade());
        EnrollmentState otherEnrolmentState = this.getEnrollmentStateByGrade(enrolmentEvaluation.getGrade());
        String otherGrade = enrolmentEvaluation.getGrade();
        Date otherWhenAltered = enrolmentEvaluation.getWhen();

        if (this.getEnrolment().getStudentCurricularPlan().getStudent().getDegreeType().equals(
                DegreeType.MASTER_DEGREE)) {
            return compareMyWhenAlteredDateToAnotherWhenAlteredDate(otherWhenAltered);
        } else if (this.getObservation() != null && this.getObservation().equals(RECTIFICATION)
                && enrolmentEvaluation.getObservation() != null
                && enrolmentEvaluation.getObservation().equals(RECTIFICATION)) {
            return compareMyWhenAlteredDateToAnotherWhenAlteredDate(otherWhenAltered);
        } else if (this.getObservation() != null && this.getObservation().equals(RECTIFICATION)) {
            return 1;
        } else if (enrolmentEvaluation.getObservation() != null
                && enrolmentEvaluation.getObservation().equals(RECTIFICATION)) {
            if (this.getEnrolmentEvaluationType().equals(EnrolmentEvaluationType.IMPROVEMENT)) {
                return compareForEqualStates(myEnrolmentState, otherEnrolmentState, otherGrade,
                        otherWhenAltered);
            }
            return -1;
        } else if (myEnrolmentState.equals(otherEnrolmentState)) {
            return compareForEqualStates(myEnrolmentState, otherEnrolmentState, otherGrade,
                    otherWhenAltered);
        } else {
            return compareForNotEqualStates(myEnrolmentState, otherEnrolmentState);
        }
    }

    private int compareMyWhenAlteredDateToAnotherWhenAlteredDate(Date whenAltered) {
        if (this.getWhen() == null) {
            return -1;
        }
        if (whenAltered == null) {
            return 1;
        }
        if (this.getWhen().compareTo(whenAltered) >= 0) {
            return 1;
        }

        return -1;

    }

    private int compareMyGradeToAnotherGrade(String grade) {
        Integer myGrade = null;
        Integer otherGrade = null;
        if (this.getGrade() == null) {
            myGrade = new Integer(0);
        } else {

            myGrade = Integer.valueOf(this.getGrade());
        }
        if (grade == null) {
            otherGrade = new Integer(0);
        } else {

            otherGrade = Integer.valueOf(grade);
        }

        if (myGrade.intValue() >= otherGrade.intValue()) {
            return 1;
        }

        return -1;

    }

    private int compareForEqualStates(EnrollmentState myEnrolmentState,
            EnrollmentState otherEnrolmentState, String otherGrade, Date otherWhenAltered) {
        if (myEnrolmentState.equals(EnrollmentState.APROVED)) {
            return compareMyGradeToAnotherGrade(otherGrade);
        }

        return compareMyWhenAlteredDateToAnotherWhenAlteredDate(otherWhenAltered);

    }

    private int compareForNotEqualStates(EnrollmentState myEnrolmentState,
            EnrollmentState otherEnrolmentState) {
        if (myEnrolmentState.equals(EnrollmentState.APROVED)) {
            return 1;
        } else if (myEnrolmentState.equals(EnrollmentState.NOT_APROVED)
                && otherEnrolmentState.equals(EnrollmentState.APROVED)) {
            return -1;
        } else if (myEnrolmentState.equals(EnrollmentState.NOT_APROVED)) {
            return 1;
        } else if (myEnrolmentState.equals(EnrollmentState.NOT_EVALUATED)) {
            return -1;
        } else {
            return 0;
        }
    }

    public EnrollmentState getEnrollmentStateByGrade() {
        return getEnrollmentStateByGrade(getGrade());
    }

    private EnrollmentState getEnrollmentStateByGrade(String grade) {
        if (grade == null) {
            return EnrollmentState.NOT_EVALUATED;
        }

        if (grade.equals("")) {
            return EnrollmentState.NOT_EVALUATED;
        }

        if (grade.equals("0")) {
            return EnrollmentState.NOT_EVALUATED;
        }

        if (grade.equals("NA")) {
            return EnrollmentState.NOT_EVALUATED;
        }

        if (grade.equals("RE")) {
            return EnrollmentState.NOT_APROVED;
        }

        if (grade.equals("AP")) {
            return EnrollmentState.APROVED;
        }

        return EnrollmentState.APROVED;
    }

    public boolean isNormal() {
        return getEnrolmentEvaluationType() == EnrolmentEvaluationType.NORMAL;
    }

    public boolean isImprovment() {
        return getEnrolmentEvaluationType() == EnrolmentEvaluationType.IMPROVEMENT;
    }

    public boolean isSpecialSeason() {
        return getEnrolmentEvaluationType() == EnrolmentEvaluationType.SPECIAL_SEASON;
    }

    public boolean isNotEvaluated() {
        return getEnrollmentStateByGrade() == EnrollmentState.NOT_EVALUATED;
    }

    public boolean isFlunked() {
        return getEnrollmentStateByGrade() == EnrollmentState.NOT_APROVED;
    }

    public boolean isApproved() {
        return getEnrollmentStateByGrade() == EnrollmentState.APROVED;
    }

	public void edit(Person responsibleFor, String grade, Date availableDate, Date examDate, String checksum) {
		
        setCheckSum(checksum);
        setGrade(grade);
        setGradeAvailableDate(availableDate);
        setPersonResponsibleForGrade(responsibleFor);
		
		if (examDate == null && grade == null)
			setExamDate(null);
		
		else if (examDate == null && grade != null)
			setExamDate(availableDate);
		
		else
			setExamDate(examDate);
	}
	
	
	public void confirmSubmission(Employee employee, String observation) {
		
        setEnrolmentEvaluationState(EnrolmentEvaluationState.FINAL_OBJ);
        Calendar calendar = Calendar.getInstance();
        setWhen(calendar.getTime());
        setEmployee(employee);
        setObservation(observation);
        setCheckSum("");
		
		Enrolment enrolment = getEnrolment();
        EnrollmentState newEnrolmentState = EnrollmentState.APROVED;

        if (MarkType.getRepMarks().contains(getGrade())) {
            newEnrolmentState = EnrollmentState.NOT_APROVED;
        } else if (MarkType.getNaMarks().contains(getGrade())) {
            newEnrolmentState = EnrollmentState.NOT_EVALUATED;
        }
        enrolment.setEnrollmentState(newEnrolmentState);
	}
	
	
	
	
	public void delete() {
		removePersonResponsibleForGrade();
		removeEmployee();
		removeEnrolment();
		
		super.deleteDomainObject();
	}
	
	public void insertStudentFinalEvaluationForMasterDegree(String grade, Person responsibleFor, Date examDate) 
		throws DomainException {
		
		DegreeCurricularPlan degreeCurricularPlan = getEnrolment().getStudentCurricularPlan().getDegreeCurricularPlan();

		if (grade == null || grade.length() == 0) {	
		
			if (getGrade() != null && getGrade().length() > 0)
				edit(null, null, null, null, null);
		} 
		
		else if (grade != null && grade.length() > 0) {
		
			if (degreeCurricularPlan.isGradeValid(grade)) {
				Calendar calendar = Calendar.getInstance();
				edit(responsibleFor, grade, calendar.getTime(), examDate, "");
			}
		
			else
				throw new DomainException("error.invalid.grade");
		}
	}
	
	
	
	public void alterStudentEnrolmentEvaluationForMasterDegree(String grade, Employee employee, Person responsibleFor,
			EnrolmentEvaluationType evaluationType, Date evaluationAvailableDate, Date examDate, String observation) 
				throws DomainException {

		Enrolment enrolment = getEnrolment();
		DegreeCurricularPlan degreeCurricularPlan = getEnrolment().getStudentCurricularPlan().getDegreeCurricularPlan();
        		
		if (grade.equals("0") || grade.equals("")) {

            EnrolmentEvaluation enrolmentEvaluation = new EnrolmentEvaluation(enrolment, getEnrolmentEvaluationType());
			enrolmentEvaluation.confirmSubmission(employee, observation);
			enrolment.setEnrollmentState(EnrollmentState.ENROLLED);

        } else {

			if (degreeCurricularPlan.isGradeValid(grade)) {
				
				EnrolmentEvaluation enrolmentEvaluation = new EnrolmentEvaluation(enrolment, evaluationType);
				enrolmentEvaluation.edit(responsibleFor, grade, evaluationAvailableDate, examDate, "");
				enrolmentEvaluation.confirmSubmission(employee, observation);
			}
			
			else
				throw new DomainException("error.invalid.grade");
        }
	}

    public IGrade getGradeWrapper() {
        return GradeFactory.getInstance().getGrade(getGrade());
    }
}
