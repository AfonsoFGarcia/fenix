/*
 * Created on 26/Ago/2003
 *
 */
package ServidorAplicacao.Servicos.teacher;

import Dominio.DistributedTest;
import Dominio.IDistributedTest;
import ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentDistributedTest;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Susana Fernandes
 */
public class EditDistributedTestTest extends TestCaseDeleteAndEditServices {

	public EditDistributedTestTest(String testName) {
		super(testName);
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}

	protected String getNameOfServiceToBeTested() {
		return "EditDistributedTest";
	}

	protected boolean needsAuthorization() {
		return true;
	}

	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
		IDistributedTest distributedTest = new DistributedTest(new Integer(25));
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();
			IPersistentDistributedTest persistentDistributedTest =
				sp.getIPersistentDistributedTest();
			distributedTest =
				(IDistributedTest) persistentDistributedTest.readByOId(
					distributedTest,
					false);
			sp.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			fail("excepcao");
		}
		Object[] args =
			{
				new Integer(24),
				distributedTest.getBeginDate(),
				distributedTest.getBeginHour(),
				distributedTest.getEndDate(),
				distributedTest.getEndHour(),
				distributedTest.getTestType(),
				distributedTest.getCorrectionAvailability(),
				distributedTest.getStudentFeedback()};
		return args;
	}

	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
		return null;
	}
}
