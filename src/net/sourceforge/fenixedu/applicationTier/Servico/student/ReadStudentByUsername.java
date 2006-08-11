package net.sourceforge.fenixedu.applicationTier.Servico.student;

/**
 * 
 * @author tfc130
 */
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadStudentByUsername extends Service {

    public Object run(String username) throws ExcepcaoPersistencia {
        final Registration student = Registration.readByUsername(username);
        return student == null ? null : InfoStudent.newInfoFromDomain(student);
    }

}