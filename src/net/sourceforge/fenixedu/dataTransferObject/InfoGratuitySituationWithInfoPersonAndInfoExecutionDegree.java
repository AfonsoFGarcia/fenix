/*
 * Created on 8/Jul/2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.IGratuitySituation;

/**
 * @author T�nia Pous�o
 *  
 */
public class InfoGratuitySituationWithInfoPersonAndInfoExecutionDegree extends InfoGratuitySituation {

    /*
     * (non-Javadoc)
     * 
     * @see net.sourceforge.fenixedu.dataTransferObject.InfoGratuitySituation#copyFromDomain(Dominio.IGratuitySituation)
     */
    public void copyFromDomain(IGratuitySituation gratuitySituation) {
        super.copyFromDomain(gratuitySituation);
        if (gratuitySituation != null) {
            setInfoStudentCurricularPlan(InfoStudentCurricularPlanWithInfoStudentAndDegree
                    .newInfoFromDomain(gratuitySituation.getStudentCurricularPlan()));
            setInfoGratuityValues(InfoGratuityValuesWithInfoExecutionDegree
                    .newInfoFromDomain(gratuitySituation.getGratuityValues()));
        }
    }

    public static InfoGratuitySituation newInfoFromDomain(IGratuitySituation gratuitySituation) {
        InfoGratuitySituationWithInfoPersonAndInfoExecutionDegree infoGratuitySituation = null;
        if (gratuitySituation != null) {
            infoGratuitySituation = new InfoGratuitySituationWithInfoPersonAndInfoExecutionDegree();
            infoGratuitySituation.copyFromDomain(gratuitySituation);
        }
        return infoGratuitySituation;
    }
}