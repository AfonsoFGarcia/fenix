/*
 * CriarAula.java
 *
 * Created on 2003/03/26
 */

package ServidorAplicacao.Servico.sop;

/**
 * Servi�o CriarAula.
 *
 * @author Luis Cruz & Sara Ribeiro
 **/

import java.util.Calendar;

import DataBeans.InfoExecutionCourse;
import DataBeans.util.Cloner;
import Dominio.Exam;
import Dominio.IDisciplinaExecucao;
import Dominio.IExam;
import Dominio.IExecutionPeriod;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.Season;

public class CreateExam implements IServico {

	private static CreateExam _servico = new CreateExam();
	/**
	 * The singleton access method of this class.
	 **/
	public static CreateExam getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private CreateExam() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "CreateExam";
	}

	public Boolean run(
		Calendar examDateAndTime,
		Season season,
		InfoExecutionCourse infoExecutionCourse)
		throws FenixServiceException {

		Boolean result = new Boolean(false);

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IDisciplinaExecucaoPersistente executionCourseDAO =
				sp.getIDisciplinaExecucaoPersistente();

			IExecutionPeriod executionPeriod =
				Cloner.copyInfoExecutionPeriod2IExecutionPeriod(
					infoExecutionCourse.getInfoExecutionPeriod());

			IDisciplinaExecucao executionCourse =
				executionCourseDAO
					.readByExecutionCourseInitialsAndExecutionPeriod(
					infoExecutionCourse.getSigla(),
					executionPeriod);

			IExam exam =
				new Exam(
					examDateAndTime.getTime(),
					examDateAndTime,
					null,
					season,
					executionCourse);

			// TODO : Falta realizar verifica��es adicionais
			//        antes de escrever.
			//   Verifica��es a fazer
			try {
				sp.getIPersistentExam().lockWrite(exam);
			} catch (ExistingPersistentException ex) {
				throw new ExistingServiceException(ex);
			}


			result = new Boolean(true);
		} catch (ExcepcaoPersistencia ex) {

			throw new FenixServiceException(ex.getMessage());
		}

		return result;
	}

}