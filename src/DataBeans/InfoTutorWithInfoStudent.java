package DataBeans;

import Dominio.ITutor;

/**
 * @author Fernanda Quit�rio
 * Created on 9/Jul/2004
 * 
 */
public class InfoTutorWithInfoStudent extends InfoTutor {
    public void copyFromDomain(ITutor tutor) {
        super.copyFromDomain(tutor);
        if(tutor != null) {
            setInfoStudent(InfoStudentWithInfoPerson.newInfoFromDomain(tutor.getStudent()));
        }
    }
    
    public static InfoTutor newInfoFromDomain(ITutor tutor) {
        InfoTutorWithInfoStudent infoTutorWithInfoStudent = null;
        if(tutor != null) {
            infoTutorWithInfoStudent = new InfoTutorWithInfoStudent();
            infoTutorWithInfoStudent.copyFromDomain(tutor);
        }
        return infoTutorWithInfoStudent;
    }
    
}
