package ServidorAplicacao.Servico.publico;

import java.util.ArrayList;
import java.util.List;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.util.Cloner;
import Dominio.ICursoExecucao;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Jo�o Mota
 */
public class ReadExecutionCoursesByDegreeAndExecutionPeriod
	implements IServico {

	private static ReadExecutionCoursesByDegreeAndExecutionPeriod _servico =
		new ReadExecutionCoursesByDegreeAndExecutionPeriod();

	/**
	  * The actor of this class.
	  **/

	private ReadExecutionCoursesByDegreeAndExecutionPeriod() {

	}

	/**
	 * Returns Service Name
	 */
	public String getNome() {
		return "ReadExecutionCoursesByDegreeAndExecutionPeriod";
	}

	/**
	 * Returns the _servico.
	 * @return SelectExecutionCourse
	 */
	public static ReadExecutionCoursesByDegreeAndExecutionPeriod getService() {
		return _servico;
	}

	public Object run(
		InfoExecutionDegree infoExecutionDegree,
		InfoExecutionPeriod infoExecutionPeriod)
		throws FenixServiceException {

		List infoExecutionCourseList = new ArrayList();

		try {

			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			IDisciplinaExecucaoPersistente executionCourseDAO =
				sp.getIDisciplinaExecucaoPersistente();

			ICursoExecucao executionDegree =
				Cloner.copyInfoExecutionDegree2ExecutionDegree(
					infoExecutionDegree);
			IExecutionPeriod executionPeriod =
				Cloner.copyInfoExecutionPeriod2IExecutionPeriod(
					infoExecutionPeriod);
			List executionCourseList = new ArrayList();
			List temp = null;
			for (int i = 1; i < 6; i++) {
				temp =
					executionCourseDAO
						.readByCurricularYearAndExecutionPeriodAndExecutionDegree(
						new Integer(i),
						executionPeriod,
						executionDegree);
				executionCourseList.addAll(temp);
			}

			for (int i = 0; i < executionCourseList.size(); i++) {
				IDisciplinaExecucao aux =
					(IDisciplinaExecucao) executionCourseList.get(i);
				InfoExecutionCourse infoExecutionCourse =
					Cloner.copyIExecutionCourse2InfoExecutionCourse(aux);
				infoExecutionCourseList.add(infoExecutionCourse);
			}

		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}

		return infoExecutionCourseList;

	}

}
