/*
 * Created on 22/Set/2003
 */
package ServidorAplicacao.Servico.manager;

import DataBeans.InfoProfessorShip;
import Dominio.DisciplinaExecucao;
import Dominio.IDisciplinaExecucao;
import Dominio.IProfessorship;
import Dominio.ITeacher;
import Dominio.Professorship;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author lmac1
 */

public class InsertProfessorShip implements IServico {

	private static InsertProfessorShip service = new InsertProfessorShip();

	public static InsertProfessorShip getService() {
		return service;
	}

	private InsertProfessorShip() {
	}

	public final String getNome() {
		return "InsertProfessorShip";
	}
	

	public void run(InfoProfessorShip infoProfessorShip) throws FenixServiceException {
	
		try {
				ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
				
				Integer executionCourseId = infoProfessorShip.getInfoExecutionCourse().getIdInternal();
				IDisciplinaExecucaoPersistente persistentExecutionCourse = persistentSuport.getIDisciplinaExecucaoPersistente();
				IDisciplinaExecucao executionCourse = (IDisciplinaExecucao) persistentExecutionCourse.readByOId(new DisciplinaExecucao(executionCourseId), false);
				
				if(executionCourse == null)
					throw new NonExistingServiceException("message.nonExisting.executionCourse", null);
				
				Integer teacherNumber = infoProfessorShip.getInfoTeacher().getTeacherNumber();
				IPersistentTeacher persistentTeacher = persistentSuport.getIPersistentTeacher();
				ITeacher teacher = persistentTeacher.readTeacherByNumber(teacherNumber);
				
				if(teacher == null)
					throw new NonExistingServiceException("message.non.existing.teacher", null);
				
				IPersistentProfessorship persistentProfessorShip = persistentSuport.getIPersistentProfessorship();
				
				IProfessorship professorShip = new Professorship();
				professorShip.setExecutionCourse(executionCourse);
				professorShip.setTeacher(teacher);						

				persistentProfessorShip.lockWrite(professorShip);
					
		} catch (ExistingPersistentException e) {
			return;
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia);
		}
	}
}