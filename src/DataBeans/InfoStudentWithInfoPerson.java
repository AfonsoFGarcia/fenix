/*
 * Created on 17/Jun/2004
 */
package DataBeans;

import Dominio.IStudent;


/**
 * @author T�nia Pous�o
 *
 */
public class InfoStudentWithInfoPerson extends InfoStudent {
    
    public void copyFromDomain(IStudent student) {
        super.copyFromDomain(student);
        if(student != null) {
            setInfoPerson(InfoPerson.newInfoFromDomain(student.getPerson()));
        }
    }
    
    public static InfoStudent newInfoFromDomain(IStudent student) {
        InfoStudentWithInfoPerson infoStudent = null;
        if(student != null) {
            infoStudent = new InfoStudentWithInfoPerson();
            infoStudent.copyFromDomain(student);
        }
        return infoStudent;        
    }
}
