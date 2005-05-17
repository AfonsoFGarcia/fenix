package net.sourceforge.fenixedu.domain;

import java.util.List;

import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState;
import net.sourceforge.fenixedu.util.AgreementType;
import net.sourceforge.fenixedu.util.EntryPhase;
import net.sourceforge.fenixedu.util.StudentState;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * @author dcs-rjao
 * 
 * 24/Mar/2003
 */

public class Student extends Student_Base{

    protected StudentState state;

    protected DegreeType degreeType;

    private AgreementType agreementType;

    private EntryPhase entryPhase;

    public Student(Integer idInternal) {
        setIdInternal(idInternal);
    }

    public Student() {
        setNumber(null);
        setState(null);
        setPerson(null);
        setDegreeType(null);

        setPersonKey(null);
        setStudentKind(null);
        setStudentKindKey(null);
        setSpecialSeason(new Boolean(false));
    }

    public Student(Integer number, StudentState state, IPerson person, DegreeType degreeType) {
        this();
        setNumber(number);
        setState(state);
        setPerson(person);
        setDegreeType(degreeType);

        setPersonKey(null);
        setSpecialSeason(new Boolean(false));
    }

    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof IStudent) {
            IStudent student = (IStudent) obj;

            resultado = (student != null)
                    && ((this.getNumber().equals(student.getNumber()) && this.getDegreeType().equals(
                            student.getDegreeType())) || (this.getDegreeType().equals(
                            student.getDegreeType()) && this.getPerson().equals(student.getPerson())));
        }
        return resultado;
        //		return true;
    }

    public String toString() {
        String result = "[" + this.getClass().getName() + "; ";
        result += "internalCode = " + getIdInternal() + "; ";
        result += "number = " + this.getNumber() + "; ";
        result += "state = " + this.state + "; ";
        result += "degreeType = " + this.degreeType + "; ";
        result += "studentKind = " + getStudentKind() + "; ";
        return result;
    }

    /**
     * Returns the degreeType.
     * 
     * @return DegreeType
     */
    public DegreeType getDegreeType() {
        return degreeType;
    }

    /**
     * Returns the state.
     * 
     * @return StudentState
     */
    public StudentState getState() {
        return state;
    }

    /**
     * Sets the degreeType.
     * 
     * @param degreeType
     *            The degreeType to set
     */
    public void setDegreeType(DegreeType degreeType) {
        this.degreeType = degreeType;
    }

    /**
     * Sets the state.
     * 
     * @param state
     *            The state to set
     */
    public void setState(StudentState state) {
        this.state = state;
    }




    /**
     * @return Returns the agreementType.
     */
    public AgreementType getAgreementType() {
        return agreementType;
    }

    /**
     * @param agreementType
     *            The agreementType to set.
     */
    public void setAgreementType(AgreementType agreementType) {
        this.agreementType = agreementType;
    }

    /**
     * @return Returns the entryPhase.
     */
    public EntryPhase getEntryPhase() {
        return entryPhase;
    }

    /**
     * @param entryPhase
     *            The entryPhase to set.
     */
    public void setEntryPhase(EntryPhase entryPhase) {
        this.entryPhase = entryPhase;
    }

    /*
     * (non-Javadoc)
     * 
     * @see Dominio.IStudent#getActiveStudentCurricularPlan()
     */
    public IStudentCurricularPlan getActiveStudentCurricularPlan() {
        List curricularPlans = getStudentCurricularPlans();
        IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) CollectionUtils.find(
                curricularPlans, new Predicate() {

                    public boolean evaluate(Object arg0) {
                        IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) arg0;
                        return studentCurricularPlan.getCurrentState().equals(StudentCurricularPlanState.ACTIVE);
                    }
                });

        if (studentCurricularPlan == null) {
            studentCurricularPlan = (IStudentCurricularPlan) curricularPlans.get(0);
        }
        return studentCurricularPlan;
    }



}