/*
 * CreateExam.java
 *
 * Created on 2003/03/26
 */

package ServidorAplicacao.Servico.sop;

/**
 * Servi�o CriarAula.
 *
 * @author Luis Cruz & Sara Ribeiro
 **/

import DataBeans.InfoExecutionCourse;
import DataBeans.util.Cloner;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class DefineExamComment implements IServico {

	private static DefineExamComment _servico = new DefineExamComment();
	/**
	 * The singleton access method of this class.
	 **/
	public static DefineExamComment getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private DefineExamComment() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "DefineExamComment";
	}

	public Boolean run(
		InfoExecutionCourse infoExecutionCourse,
		String comment)
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

			executionCourse.setComment(comment);

			result = new Boolean(true);
			
		} catch (ExcepcaoPersistencia ex) {

			throw new FenixServiceException(ex.getMessage());
		}

		return result;
	}

}