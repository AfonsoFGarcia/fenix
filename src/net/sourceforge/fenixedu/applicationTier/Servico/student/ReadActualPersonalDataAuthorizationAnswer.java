/**
 * Aug 29, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.student.StudentPersonalDataAuthorization;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.PeriodState;
import net.sourceforge.fenixedu.util.StudentPersonalDataAuthorizationChoice;

/**
 * @author Ricardo Rodrigues
 * 
 */
public class ReadActualPersonalDataAuthorizationAnswer extends Service {

    public StudentPersonalDataAuthorizationChoice run(Integer studentID) throws ExcepcaoPersistencia {
        final Student student = (Student) persistentObject.readByOID(Student.class, studentID);

        for (final StudentPersonalDataAuthorization studentPersonalDataAuthorization :
                student.getStudentPersonalDataAuthorizations()) {
            if (studentPersonalDataAuthorization.getExecutionYear().getState().equals(PeriodState.CURRENT)) {
                return studentPersonalDataAuthorization.getAnswer();
            }
        }

        return null;
    }

}
