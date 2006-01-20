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
import net.sourceforge.fenixedu.dataTransferObject.InfoCountry;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

public class ReadStudent extends Service {

	// FIXME: We have to read the student by type also !!

	public Object run(Integer number) throws ExcepcaoPersistencia {

		InfoStudent infoStudent = null;

		ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
		// ////////////////////////////////////////////////////////////////////////////////////////////////////////
		// Isto n�o � para ficar assim. Est� assim temporariamente at� se
		// saber como � feita de facto a distin��o
		// dos aluno, referente ao tipo, a partir da p�gina de login.
		// ////////////////////////////////////////////////////////////////////////////////////////////////////////
		Student student = persistentSupport.getIPersistentStudent().readStudentByNumberAndDegreeType(number,
				DegreeType.DEGREE);

		if (student != null) {
			InfoPerson infoPerson = new InfoPerson();
			infoPerson.setNome(student.getPerson().getNome());
			infoPerson.setUsername(student.getPerson().getUsername());
			infoPerson.setPassword(student.getPerson().getPassword());
			infoPerson.setDistritoNaturalidade(student.getPerson().getDistritoNaturalidade());
			infoPerson.setInfoPais(InfoCountry.newInfoFromDomain(student.getPerson().getPais()));
			infoPerson.setNomePai(student.getPerson().getNomePai());
			infoPerson.setNomeMae(student.getPerson().getNomeMae());
			infoPerson.setIdInternal(student.getPerson().getIdInternal());

			infoStudent = new InfoStudent(student.getNumber(), student.getState(), infoPerson, student
					.getDegreeType());
			infoStudent.setIdInternal(student.getIdInternal());
		}

		return infoStudent;
	}

}