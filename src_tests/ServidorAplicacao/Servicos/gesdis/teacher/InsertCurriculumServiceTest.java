/*
 * Created on 24/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorAplicacao.Servicos.gesdis.teacher;

import java.util.HashMap;

import DataBeans.InfoCurriculum;
import DataBeans.InfoExecutionCourse;
import DataBeans.util.Cloner;
import Dominio.ICurriculum;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import ServidorAplicacao.Servicos.TestCaseCreateServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentCurriculum;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author jmota
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class InsertCurriculumServiceTest extends TestCaseCreateServices {

	/**
	 * @param testName
	 */
	public InsertCurriculumServiceTest(String testName) {
		super(testName);

	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseNeedAuthorizationServices#getNameOfServiceToBeTested()
	 */
	protected String getNameOfServiceToBeTested() {
		return "InsertCurriculum";
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseCreateServices#getArgumentsOfServiceToBeTestedUnsuccessfuly()
	 */
	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
		InfoCurriculum oldCurriculum = new InfoCurriculum();

		ISuportePersistente sp = null;
		IExecutionYear executionYear = null;
		IExecutionPeriod executionPeriod = null;
		IDisciplinaExecucao executionCourse = null;
		try {
			sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();

			IPersistentExecutionYear ieyp = sp.getIPersistentExecutionYear();
			executionYear = ieyp.readExecutionYearByName("2002/2003");

			IPersistentExecutionPeriod iepp =
				sp.getIPersistentExecutionPeriod();

			executionPeriod =
				iepp.readByNameAndExecutionYear("2� Semestre", executionYear);

			IDisciplinaExecucaoPersistente idep =
				sp.getIDisciplinaExecucaoPersistente();
			executionCourse =
				idep.readByExecutionCourseInitialsAndExecutionPeriod(
					"TFCI",
					executionPeriod);
			IPersistentCurriculum persistentCurriculum =
				sp.getIPersistentCurriculum();

			ICurriculum curriculum =
				persistentCurriculum.readCurriculumByExecutionCourse(
					executionCourse);
			sp.confirmarTransaccao();

			oldCurriculum = Cloner.copyICurriculum2InfoCurriculum(curriculum);

		} catch (ExcepcaoPersistencia e) {
			System.out.println("failed setting up the test data");
			e.printStackTrace();
		}
		Object[] args = { oldCurriculum };
		return args;
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseCreateServices#getArgumentsOfServiceToBeTestedSuccessfuly()
	 */
	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
		InfoCurriculum oldCurriculum = new InfoCurriculum();

		ISuportePersistente sp = null;
		IExecutionYear executionYear = null;
		IExecutionPeriod executionPeriod = null;
		IDisciplinaExecucao executionCourse = null;
		try {
			sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();

			IPersistentExecutionYear ieyp = sp.getIPersistentExecutionYear();
			executionYear = ieyp.readExecutionYearByName("2002/2003");

			IPersistentExecutionPeriod iepp =
				sp.getIPersistentExecutionPeriod();

			executionPeriod =
				iepp.readByNameAndExecutionYear("2� Semestre", executionYear);

			IDisciplinaExecucaoPersistente idep =
				sp.getIDisciplinaExecucaoPersistente();
			executionCourse =
				idep.readByExecutionCourseInitialsAndExecutionPeriod(
					"IP",
					executionPeriod);
			
			sp.confirmarTransaccao();
			InfoExecutionCourse infoExecutionCourse =Cloner.copyIExecutionCourse2InfoExecutionCourse(executionCourse); 
		
			oldCurriculum = new InfoCurriculum();
			oldCurriculum.setGeneralObjectives("bla");
			oldCurriculum.setOperacionalObjectives("bla");
			oldCurriculum.setProgram("b�a");
			oldCurriculum.setInfoExecutionCourse(infoExecutionCourse);
			
		} catch (ExcepcaoPersistencia e) {
			System.out.println("failed setting up the test data");
			e.printStackTrace();
		}
		Object[] args = { oldCurriculum };
		return args;
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseCreateServices#getArgumentListOfServiceToBeTestedUnsuccessfuly()
	 */
	protected HashMap getArgumentListOfServiceToBeTestedUnsuccessfuly() {
		
		return null;
	}

}
