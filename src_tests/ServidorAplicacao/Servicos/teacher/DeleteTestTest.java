/*
 * Created on 11/Ago/2003
 *
 */
package ServidorAplicacao.Servicos.teacher;

import ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices;

/**
 * @author Susana Fernandes
 */
public class DeleteTestTest extends TestCaseDeleteAndEditServices {

	public DeleteTestTest(String testName) {
		super(testName);
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}
	
	protected String getNameOfServiceToBeTested() {
		return "DeleteTest";
	}
	
	protected boolean needsAuthorization() {
		return true;
	}
	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
		Object[] args = {new Integer(5)};
		return args;
	}
	
	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
		return null;
	}
}
