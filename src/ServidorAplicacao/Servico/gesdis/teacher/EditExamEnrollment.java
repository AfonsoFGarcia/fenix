/*
 * Created on 28/Mai/2003
 *
 */
package ServidorAplicacao.Servico.gesdis.teacher;

import java.util.Calendar;

import Dominio.ExamEnrollment;
import Dominio.IExamEnrollment;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExamEnrollment;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author T�nia Nunes
 *
 */
public class EditExamEnrollment implements IServico {

	/**
	 * Returns the service name.
	 **/

	public final String getNome() {

		return "EditExamEnrollment";

	}
	private static EditExamEnrollment service = new EditExamEnrollment();

	/**
	 * The singleton access method of this class.
	 **/

	public static EditExamEnrollment getService() {

		return service;

	}

	/**
		 * Executes the service. Returns the current exam enrollment
		 * 
		 **/

	public Boolean run(
		Integer disciplinaExecucaoIdInternal,
		Integer examEnrollmentIdInternal,
		Calendar newBeginDate,
		Calendar newEndDate)
		throws FenixServiceException {
		try {

			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentExamEnrollment persistentExamEnrollment =
				sp.getIPersistentExamEnrollment();

			IExamEnrollment examEnrollment = new ExamEnrollment();
			examEnrollment.setIdInternal(examEnrollmentIdInternal);

			IExamEnrollment oldExamEnrollment =
				(IExamEnrollment) persistentExamEnrollment.readByOId(
					examEnrollment);

			oldExamEnrollment.setBeginDate(newBeginDate);
			oldExamEnrollment.setEndDate(newEndDate);

			persistentExamEnrollment.lockWrite(oldExamEnrollment);

		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
		return new Boolean(true);
	}
}
