/*
 * Created on 13/Mai/2003
 *
 * 
 */
package ServidorAplicacao.Servico.gesdis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoExecutionCourse;
import DataBeans.util.Cloner;
import Dominio.IDisciplinaExecucao;
import Dominio.IExam;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Jo�o Mota
 * @author Fernanda Quit�rio
 *
 */
public class ReadExams implements IServico {

	private static ReadExams service = new ReadExams();

	/**
	 * The singleton access method of this class.
	 **/

	public static ReadExams getService() {

		return service;

	}

	/**
	 * The ctor of this class.
	 **/

	private ReadExams() {
	}

	/**
	 * Devolve o nome do servico
	 **/

	public final String getNome() {

		return "ReadExams";

	}

	/**
	 * Executes the service. Returns the current collection of
	 * exams
	 *
	 * 
	 **/

	public List run(InfoExecutionCourse infoExecutionCourse)
		throws FenixServiceException {
			try {
					ISuportePersistente sp;
				IDisciplinaExecucao executionCourse = Cloner.copyInfoExecutionCourse2ExecutionCourse(infoExecutionCourse);
				sp = SuportePersistenteOJB.getInstance();
				IDisciplinaExecucaoPersistente persistentExecutionCourse=sp.getIDisciplinaExecucaoPersistente();
				executionCourse = (IDisciplinaExecucao) persistentExecutionCourse.readByOId(executionCourse);
				List infoExams=new ArrayList();
				List exams = null;
				if (executionCourse!=null){ exams = executionCourse.getAssociatedExams();
				
				Iterator iter = exams.iterator();
				while (iter.hasNext()) {
					infoExams.add(Cloner.copyIExam2InfoExam((IExam) iter.next()));
				}
				
				
				}
				return infoExams;
			} catch (ExcepcaoPersistencia e) {
				throw new FenixServiceException(e);
			}

	}

}
