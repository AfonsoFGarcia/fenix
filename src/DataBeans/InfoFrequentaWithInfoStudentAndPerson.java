/*
 * Created on 23/Jun/2004
 *
 */
package DataBeans;

import Dominio.IAttends;

/**
 * @author T�nia Pous�o 23/Jun/2004
 */
public class InfoFrequentaWithInfoStudentAndPerson extends InfoFrequenta {
    public void copyFromDomain(IAttends frequenta) {
        super.copyFromDomain(frequenta);
        if (frequenta != null) {
            setAluno(InfoStudentWithInfoPerson.newInfoFromDomain(frequenta.getAluno()));
        }
    }

    public static InfoFrequenta newInfoFromDomain(IAttends attend) {
        InfoFrequentaWithInfoStudentAndPerson infoAttend = null;
        if (attend != null) {
            infoAttend = new InfoFrequentaWithInfoStudentAndPerson();
            infoAttend.copyFromDomain(attend);
        }

        return infoAttend;
    }
}