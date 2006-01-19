/**
 * Aug 30, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.dataTransferObject.student.InfoDislocatedStudent;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ReadDislocatedStudentByStudentID extends Service {

    public InfoDislocatedStudent run(Integer studentID) throws ExcepcaoPersistencia {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentStudent persistentStudent = sp.getIPersistentStudent();

        Student student = (Student) persistentStudent.readByOID(Student.class, studentID);
        return InfoDislocatedStudent.newInfoFromDomain(student.getDislocatedStudent());
    }
}
