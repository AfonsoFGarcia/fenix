/*
 * Created on 9/Set/2003
 *
 */
package ServidorAplicacao.Servicos.student;

import java.util.ArrayList;
import java.util.List;

import DataBeans.InfoExecutionCourse;
import DataBeans.util.Cloner;
import Dominio.DisciplinaExecucao;
import Dominio.IExecutionCourse;
import ServidorAplicacao.Servicos.TestCaseReadServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Susana Fernandes
 */
public class ReadExecutionCoursesByStudentTest extends TestCaseReadServices {

	/**
	* @param testName
	*/
	public ReadExecutionCoursesByStudentTest(String testName) {
		super(testName);

	}

	protected String getNameOfServiceToBeTested() {
		return "ReadExecutionCoursesByStudent";
	}

	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
		return null;
	}

	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
		Object[] args = { new String("15")};
		return args;
	}

	protected int getNumberOfItemsToRetrieve() {
		return 2;
	}

	protected Object getObjectToCompare() {
		List infoExecutionCousesList = new ArrayList();
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();

			IExecutionCourse disciplinaExecucao25 =
				new DisciplinaExecucao(new Integer(25));
			IExecutionCourse disciplinaExecucao26 =
				new DisciplinaExecucao(new Integer(26));

			disciplinaExecucao25 =
				(IExecutionCourse) sp
					.getIDisciplinaExecucaoPersistente()
					.readByOId(
					disciplinaExecucao25,
					false);
			disciplinaExecucao26 =
				(IExecutionCourse) sp
					.getIDisciplinaExecucaoPersistente()
					.readByOId(
					disciplinaExecucao26,
					false);

			sp.confirmarTransaccao();

			InfoExecutionCourse infoExecutionCourse25 =
				Cloner.copyIExecutionCourse2InfoExecutionCourse(
					disciplinaExecucao25);
			InfoExecutionCourse infoExecutionCourse26 =
				Cloner.copyIExecutionCourse2InfoExecutionCourse(
					disciplinaExecucao26);

			infoExecutionCousesList.add(infoExecutionCourse25);
			infoExecutionCousesList.add(infoExecutionCourse26);

		} catch (ExcepcaoPersistencia e) {
			fail("exception: ExcepcaoPersistencia ");
		}
		return infoExecutionCousesList;
	}

	protected boolean needsAuthorization() {
		return true;
	}
}