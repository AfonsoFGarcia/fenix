/*
 * LerAula.java
 *
 * Created on December 16th, 2002, 1:58
 */

package net.sourceforge.fenixedu.applicationTier.Servico.student;

/**
 * Servi�o LerAluno.
 * 
 * @author tfc130
 */
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadStudent extends Service {

    // FIXME: We have to read the student by type also !!

    public Object run(Integer number) {

        InfoStudent infoStudent = null;

        // ////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Isto n�o � para ficar assim. Est� assim temporariamente at� se
        // saber como � feita de facto a distin��o
        // dos aluno, referente ao tipo, a partir da p�gina de login.
        // ////////////////////////////////////////////////////////////////////////////////////////////////////////
        Registration registration = Registration.readStudentByNumberAndDegreeType(number, DegreeType.DEGREE);

        if (registration != null) {
            infoStudent = new InfoStudent(registration);
            infoStudent.setIdInternal(registration.getIdInternal());
        }

        return infoStudent;
    }

}