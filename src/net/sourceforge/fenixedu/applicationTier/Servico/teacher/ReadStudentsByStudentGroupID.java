/*
 * Created on 18/Set/2003, 18:17:51
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 18/Set/2003, 18:17:51
 * 
 */
public class ReadStudentsByStudentGroupID extends Service {

    public List run(Integer executionCourseId, Integer groupId) throws FenixServiceException,
            ExcepcaoPersistencia {

        List infoStudents = new LinkedList();
        
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentStudent persistentStudent = sp.getIPersistentStudent();

        StudentGroup studentGroup = (StudentGroup) sp.getIPersistentObject().readByOID(
                StudentGroup.class, groupId);
       
        Iterator iter = studentGroup.getAttends().iterator();

        while(iter.hasNext()) {
            Attends attend = (Attends) iter.next();
            Integer studentID = attend.getAluno().getIdInternal();
            Student student = (Student) persistentStudent.readByOID(Student.class, studentID);
            infoStudents.add(InfoStudent.newInfoFromDomain(student));
        }
        return infoStudents;
    }
}