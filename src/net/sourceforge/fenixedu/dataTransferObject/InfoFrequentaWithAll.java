/*
 * Created on 18/Jun/2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.IAttends;

/**
 * @author T�nia Pous�o 18/Jun/2004
 */
public class InfoFrequentaWithAll extends InfoFrequenta {

    public void copyFromDomain(IAttends frequenta) {
        super.copyFromDomain(frequenta);
        if (frequenta != null) {
            setAluno(InfoStudentWithInfoPerson.newInfoFromDomain(frequenta.getAluno()));
            setDisciplinaExecucao(InfoExecutionCourse.newInfoFromDomain(frequenta
                    .getDisciplinaExecucao()));
            setInfoEnrolment(InfoEnrolment.newInfoFromDomain(frequenta.getEnrolment()));
        }
    }

    public static InfoFrequenta newInfoFromDomain(IAttends frequenta) {
        InfoFrequentaWithAll infoFrequenta = null;
        if (frequenta != null) {
            infoFrequenta = new InfoFrequentaWithAll();
            infoFrequenta.copyFromDomain(frequenta);
        }

        return infoFrequenta;
    }
}