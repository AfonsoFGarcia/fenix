/*
 * Created on Oct 14, 2003
 *
 */
package DataBeans;

import Dominio.IMasterDegreeThesis;

/**
 * @author : - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 *  
 */
public class InfoMasterDegreeThesis extends InfoObject {
    private InfoStudentCurricularPlan infoStudentCurricularPlan;

    public void setInfoStudentCurricularPlan(InfoStudentCurricularPlan infoStudentCurricularPlan) {
        this.infoStudentCurricularPlan = infoStudentCurricularPlan;
    }

    public InfoStudentCurricularPlan getInfoStudentCurricularPlan() {
        return infoStudentCurricularPlan;
    }

    public String toString() {
        String result = "[" + this.getClass().getName() + ": \n";
        result += "idInternal = " + getIdInternal() + "; \n";
        result += "infoStudentCurricularPlan = " + this.infoStudentCurricularPlan.getIdInternal()
                + "; \n";
        result += "] \n";

        return result;
    }

    public boolean equals(Object obj) {
        boolean result = false;

        if (obj instanceof InfoMasterDegreeThesis) {
            InfoMasterDegreeThesis infoMasterDegreeThesis = (InfoMasterDegreeThesis) obj;
            result = this.infoStudentCurricularPlan.equals(infoMasterDegreeThesis
                    .getInfoStudentCurricularPlan());
        }
        return result;
    }

    public static InfoMasterDegreeThesis newInfoFromDomain(IMasterDegreeThesis masterDegreeThesis) {
        InfoMasterDegreeThesis infoMasterDegreeThesis = null;
        if (masterDegreeThesis != null) {
            infoMasterDegreeThesis = new InfoMasterDegreeThesis();
            infoMasterDegreeThesis.copyFromDomain(masterDegreeThesis);
        }
        return infoMasterDegreeThesis;
    }

    public void copyFromDomain(IMasterDegreeThesis masterDegreeThesis) {
        super.copyFromDomain(masterDegreeThesis);
        if (masterDegreeThesis != null) {
            setInfoStudentCurricularPlan(InfoStudentCurricularPlanWithInfoStudentAndDegree
                    .newInfoFromDomain(masterDegreeThesis.getStudentCurricularPlan()));
        }
    }

}