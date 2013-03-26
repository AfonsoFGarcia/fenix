package net.sourceforge.fenixedu.dataTransferObject;

import java.util.List;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class InfoCandidateRegistration extends InfoObject {

    private InfoStudentCurricularPlan infoStudentCurricularPlan;

    private InfoMasterDegreeCandidate infoMasterDegreeCandidate;

    private List<InfoEnrolment> enrolments;

    /**
     * @return
     */
    public List<InfoEnrolment> getEnrolments() {
        return enrolments;
    }

    /**
     * @param enrolments
     */
    public void setEnrolments(List<InfoEnrolment> enrolments) {
        this.enrolments = enrolments;
    }

    /**
     * @return
     */
    public InfoMasterDegreeCandidate getInfoMasterDegreeCandidate() {
        return infoMasterDegreeCandidate;
    }

    /**
     * @param infoMasterDegreeCandidate
     */
    public void setInfoMasterDegreeCandidate(InfoMasterDegreeCandidate infoMasterDegreeCandidate) {
        this.infoMasterDegreeCandidate = infoMasterDegreeCandidate;
    }

    /**
     * @return
     */
    public InfoStudentCurricularPlan getInfoStudentCurricularPlan() {
        return infoStudentCurricularPlan;
    }

    /**
     * @param infoStudentCurricularPlan
     */
    public void setInfoStudentCurricularPlan(InfoStudentCurricularPlan infoStudentCurricularPlan) {
        this.infoStudentCurricularPlan = infoStudentCurricularPlan;
    }

    public InfoCandidateRegistration() {
    }

}