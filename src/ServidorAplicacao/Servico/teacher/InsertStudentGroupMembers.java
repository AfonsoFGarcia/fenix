/*
 * Created on 23/Ago/2003
 *
 */
package ServidorAplicacao.Servico.teacher;

import java.util.Iterator;
import java.util.List;

import Dominio.DisciplinaExecucao;
import Dominio.IDisciplinaExecucao;
import Dominio.IFrequenta;
import Dominio.IGroupProperties;
import Dominio.IStudent;
import Dominio.IStudentGroup;
import Dominio.IStudentGroupAttend;
import Dominio.Student;
import Dominio.StudentGroup;
import Dominio.StudentGroupAttend;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidSituationServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IPersistentStudentGroup;
import ServidorPersistente.IPersistentStudentGroupAttend;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author asnr and scpo
 *
 */

public class InsertStudentGroupMembers implements IServico {

	private static InsertStudentGroupMembers service = new InsertStudentGroupMembers();

	/**
		* The singleton access method of this class.
		*/
	public static InsertStudentGroupMembers getService() {
		return service;
	}
	/**
	 * The constructor of this class.
	 */
	private InsertStudentGroupMembers() {
	}
	/**
	 * The name of the service
	 */
	public final String getNome() {
		return "InsertStudentGroupMembers";
	}

	/**
	 * Executes the service.
	 */

	public Boolean run(Integer executionCourseCode, Integer studentGroupCode, List studentCodes) throws FenixServiceException {

		IPersistentStudentGroupAttend persistentStudentGroupAttend = null;
		IPersistentStudentGroup persistentStudentGroup = null;
		IFrequentaPersistente persistentAttend = null;
		IPersistentStudent persistentStudent = null;
		IDisciplinaExecucaoPersistente persistentExecutionCourse = null;
		try {

			ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();

			persistentStudentGroup = persistentSupport.getIPersistentStudentGroup();
			persistentStudent = persistentSupport.getIPersistentStudent();
			persistentAttend = persistentSupport.getIFrequentaPersistente();
			persistentExecutionCourse = persistentSupport.getIDisciplinaExecucaoPersistente();
			persistentStudentGroupAttend = persistentSupport.getIPersistentStudentGroupAttend();

			IStudentGroup studentGroup = (IStudentGroup) persistentStudentGroup.readByOId(new StudentGroup(studentGroupCode), false);

			if (studentGroup == null)
				throw new ExistingServiceException();

			IDisciplinaExecucao executionCourse =
				(IDisciplinaExecucao) persistentExecutionCourse.readByOId(new DisciplinaExecucao(executionCourseCode), false);

			IGroupProperties groupProperties = studentGroup.getGroupProperties();
			List allStudentGroup = persistentStudentGroup.readAllStudentGroupByGroupProperties(groupProperties);

			Iterator iterGroups = allStudentGroup.iterator();

			while (iterGroups.hasNext()) {
				IStudentGroup existingStudentGroup = (IStudentGroup) iterGroups.next();
				IStudentGroupAttend newStudentGroupAttend = null;
				Iterator iterator = studentCodes.iterator();

				while (iterator.hasNext()) {
					IStudent student = (IStudent) persistentStudent.readByOId(new Student((Integer) iterator.next()), false);

					IFrequenta attend =
						persistentAttend.readByAlunoAndDisciplinaExecucao(student, groupProperties.getExecutionCourse());

					newStudentGroupAttend = persistentStudentGroupAttend.readBy(existingStudentGroup, attend);

					if (newStudentGroupAttend != null)
						throw new InvalidSituationServiceException();

				}
			}

			Iterator iter = studentCodes.iterator();

			while (iter.hasNext()) {

				IStudent student = (IStudent) persistentStudent.readByOId(new Student((Integer) iter.next()), false);

				IFrequenta attend = persistentAttend.readByAlunoAndDisciplinaExecucao(student, groupProperties.getExecutionCourse());

				IStudentGroupAttend notExistingSGAttend = new StudentGroupAttend(studentGroup, attend);

				persistentStudentGroupAttend.lockWrite(notExistingSGAttend);
			}

			
		

		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia.getMessage());
		}

		return new Boolean(true);
	}
}
