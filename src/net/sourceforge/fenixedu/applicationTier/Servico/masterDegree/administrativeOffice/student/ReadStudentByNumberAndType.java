/*
 * LerAula.java
 *
 * Created on December 16th, 2002, 1:58
 */

package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.student;

/**
 * Servi�o LerAluno.
 * 
 * @author tfc130
 */

import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.TipoCurso;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadStudentByNumberAndType implements IService {

    // FIXME: We have to read the student by type also !!

    public Object run(Integer number, TipoCurso degreeType) {

        InfoStudent infoStudent = null;

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            //////////////////////////////////////////////////////////////////////////////////////////////////////////
            // Isto n�o � para ficar assim. Est� assim temporariamente at� se
            // saber como � feita de facto a distin��o
            // dos aluno, referente ao tipo, a partir da p�gina de login.
            //////////////////////////////////////////////////////////////////////////////////////////////////////////
            IStudent student = sp.getIPersistentStudent().readStudentByNumberAndDegreeType(number,
                    degreeType);

            if (student != null) {
                infoStudent = Cloner.copyIStudent2InfoStudent(student);
            }

        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
        }

        return infoStudent;
    }

}