/*
 * Created on 8/Jul/2004
 *
 */
package DataBeans;

import Dominio.IGratuitySituation;

/**
 * @author T�nia Pous�o
 *  
 */
public class InfoGratuitySituationWithAll extends InfoGratuitySituation {

    /*
     * (non-Javadoc)
     * 
     * @see DataBeans.InfoGratuitySituation#copyFromDomain(Dominio.IGratuitySituation)
     */
    public void copyFromDomain(IGratuitySituation gratuitySituation) {
        super.copyFromDomain(gratuitySituation);
        if (gratuitySituation != null) {
            setInfoStudentCurricularPlan(InfoStudentCurricularPlan.newInfoFromDomain(gratuitySituation
                    .getStudentCurricularPlan()));
            setInfoGratuityValues(InfoGratuityValues.newInfoFromDomain(gratuitySituation
                    .getGratuityValues()));
        }
    }

    public static InfoGratuitySituation newInfoFromDomain(IGratuitySituation gratuitySituation) {
        InfoGratuitySituationWithAll infoGratuitySituation = new InfoGratuitySituationWithAll();
        if (gratuitySituation != null) {
            infoGratuitySituation = new InfoGratuitySituationWithAll();
            infoGratuitySituation.copyFromDomain(gratuitySituation);
        }
        return infoGratuitySituation;
    }
}