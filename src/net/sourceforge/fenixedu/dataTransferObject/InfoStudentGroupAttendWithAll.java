/*
 * Created on 7/Jul/2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.IStudentGroupAttend;

/**
 * @author T�nia Pous�o
 *  
 */
public class InfoStudentGroupAttendWithAll extends InfoStudentGroupAttend {

    /*
     * (non-Javadoc)
     * 
     * @see DataBeans.InfoStudentGroupAttend#copyFromDomain(Dominio.IStudentGroupAttend)
     */
    public void copyFromDomain(IStudentGroupAttend studentGroupAttend) {
        super.copyFromDomain(studentGroupAttend);
        if (studentGroupAttend != null) {
            setInfoAttend(InfoFrequentaWithInfoStudent.newInfoFromDomain(studentGroupAttend.getAttend()));
            setInfoStudentGroup(InfoStudentGroup.newInfoFromDomain(studentGroupAttend.getStudentGroup()));
        }
    }

    public static InfoStudentGroupAttend newInfoFromDomain(IStudentGroupAttend studentGroupAttend) {
        InfoStudentGroupAttendWithAll infoStudentGroupAttend = null;
        if (studentGroupAttend != null) {
            infoStudentGroupAttend = new InfoStudentGroupAttendWithAll();
            infoStudentGroupAttend.copyFromDomain(studentGroupAttend);
        }
        return infoStudentGroupAttend;
    }
}