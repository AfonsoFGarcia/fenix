package ServidorAplicacao.Servicos.student;

import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.notAuthorizedServiceDeleteException;
import ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase;

/**
 * @author  Luis Egidio, lmre@mega.ist.utl.pt
 * 			Nuno Ochoa,  nmgo@mega.ist.utl.pt
 *
 */
public class UnEnrollStudentInExamTest
	extends ServiceNeedsAuthenticationTestCase {

	/**
	 * @param testName
	 */
	public UnEnrollStudentInExamTest(String name) {
		super(name);
	}

	protected String getNameOfServiceToBeTested() {
		return "UnEnrollStudentInExam";
	}

	protected String getDataSetFilePath() {
		return "etc/datasets/servicos/student/testUnEnrollStudentInExamDataSet.xml";
	}

	protected String[] getAuthenticatedAndAuthorizedUser() {
		String[] args = { "13", "pass", getApplication()};
		return args;
	}

	protected String[] getAuthenticatedAndUnauthorizedUser() {
		String[] args = { "nmsn", "pass", getApplication()};
		return args;
	}

	protected String[] getNotAuthenticatedUser() {
		String[] args = { "fiado", "pass", getApplication()};
		return args;
	}

	protected Object[] getAuthorizeArguments() {
		Object[] args = { userView.getUtilizador(), new Integer(2)};
		return args;
	}

	protected String getApplication() {
		return Autenticacao.EXTRANET;
	}

	public void testUnEnrollNonExistingStudentInExam() {
		Object[] args = { "UsernameOfNonExistingStudent", new Integer(2)};
		Boolean result;

		try {
			result =
				(Boolean) gestor.executar(
					userView,
					getNameOfServiceToBeTested(),
					args);
			assertTrue(result.booleanValue());
			compareDataSetUsingExceptedDataSetTableColumns(
				"etc/datasets/servicos/student/"
					+ "testExpectedUnEnrollNonExistingStudentInExamDataSet.xml");
			System.out.println(
				"testUnEnrollNonExistingStudentInExam was SUCCESSFULY runned by class: "
					+ this.getClass().getName());

		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(
				"testUnEnrollNonExistingStudentInExam was UNSUCCESSFULY runned by class: "
					+ this.getClass().getName());
			fail("testUnEnrollNonExistingStudentInExam");
		}
	}

	public void testUnEnrollStudentInNonExistingExam() {
		Object[] args = { userView.getUtilizador(), new Integer(7)};
		Boolean result;

		try {
			result =
				(Boolean) gestor.executar(
					userView,
					getNameOfServiceToBeTested(),
					args);
			assertTrue(result.booleanValue());
			compareDataSetUsingExceptedDataSetTableColumns(
				"etc/datasets/servicos/student/"
					+ "testExpectedUnEnrollStudentInNonExistingExamDataSet.xml");
			System.out.println(
				"testUnEnrollStudentInNonExistingExam was SUCCESSFULY runned by class: "
					+ this.getClass().getName());

		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(
				"testUnEnrollStudentInNonExistingExam was UNSUCCESSFULY runned by class: "
					+ this.getClass().getName());
			fail("testUnEnrollStudentInNonExistingExam");
		}
	}

	public void testInvalidUnEnrollStudentInExam() {
		Object[] args = { userView.getUtilizador(), new Integer(6)};
		// This exam has no Enrollment_End_Day or Time associated. In this case,
		// it is considered to be an invalid unenrollment of such exam.

		try {
			gestor.executar(userView, getNameOfServiceToBeTested(), args);
			System.out.println(
				"testInvalidUnEnrollStudentInExam was UNSUCCESSFULY runned by class: "
					+ this.getClass().getName());
			fail("testInvalidUnEnrollStudentInExam");

		} catch (notAuthorizedServiceDeleteException ex) {
			compareDataSetUsingExceptedDataSetTableColumns(
				"etc/datasets/servicos/student/"
					+ "testExpectedInvalidUnEnrollStudentInExamDataSet.xml");
			ex.printStackTrace();
			System.out.println(
				"testInvalidUnEnrollStudentInExam was SUCCESSFULY runned by class: "
					+ this.getClass().getName());

		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(
				"testInvalidUnEnrollStudentInExam was UNSUCCESSFULY runned by class: "
					+ this.getClass().getName());
			fail("testInvalidUnEnrollStudentInExam");
		}
	}

	public void testNotAuthorizedUnEnrollStudentInExam() {
		Object[] args = { userView.getUtilizador(), new Integer(1)};
		// Reservation for this student has already been made in a exam room.

		try {
			gestor.executar(userView, getNameOfServiceToBeTested(), args);
			System.out.println(
				"testNotAuthorizedUnEnrollStudentInExam was UNSUCCESSFULY runned by class: "
					+ this.getClass().getName());
			fail("testNotAuthorizedUnEnrollStudentInExam");

		} catch (notAuthorizedServiceDeleteException ex) {
			compareDataSetUsingExceptedDataSetTableColumns(
				"etc/datasets/servicos/student/"
					+ "testExpectedNotAuthorizedUnEnrollStudentInExamDataSet.xml");
			ex.printStackTrace();
			System.out.println(
				"testNotAuthorizedUnEnrollStudentInExam was SUCCESSFULY runned by class: "
					+ this.getClass().getName());

		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(
				"testNotAuthorizedUnEnrollStudentInExam was UNSUCCESSFULY runned by class: "
					+ this.getClass().getName());
			fail("testNotAuthorizedUnEnrollStudentInExam");
		}
	}

	public void testUnEnrollNonExistingEnrollment() {
		Object[] args = { userView.getUtilizador(), new Integer(3)};
		Boolean result;

		try {
			result =
				(Boolean) gestor.executar(
					userView,
					getNameOfServiceToBeTested(),
					args);
			assertTrue(result.booleanValue());
			compareDataSetUsingExceptedDataSetTableColumns(
				"etc/datasets/servicos/student/"
					+ "testExpectedUnEnrollNonExistingEnrollmentDataSet.xml");
			System.out.println(
				"testUnEnrollNonExistingEnrollment was SUCCESSFULY runned by class: "
					+ this.getClass().getName());

		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(
				"testUnEnrollNonExistingEnrollment was UNSUCCESSFULY runned by class: "
					+ this.getClass().getName());
			fail("testUnEnrollNonExistingEnrollment");
		}
	}

	public void testUnEnrollStudentInExam() {
		Boolean result;

		try {
			result =
				(Boolean) gestor.executar(
					userView,
					getNameOfServiceToBeTested(),
					getAuthorizeArguments());
			assertTrue(result.booleanValue());
			compareDataSetUsingExceptedDataSetTableColumns(
				"etc/datasets/servicos/student/"
					+ "testExpectedUnEnrollStudentInExamDataSet.xml");
			System.out.println(
				"testUnEnrollStudentInExam was SUCCESSFULY runned by class: "
					+ this.getClass().getName());

		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(
				"testUnEnrollStudentInExam was UNSUCCESSFULY runned by class: "
					+ this.getClass().getName());
			fail("testUnEnrollStudentInExam");
		}
	}

}
