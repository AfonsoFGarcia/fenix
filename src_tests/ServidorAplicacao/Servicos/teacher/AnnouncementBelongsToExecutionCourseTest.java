package ServidorAplicacao.Servicos.teacher;

import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase;

/**
 * @author Barbosa
 * @author Pica
 * Created on 8/Out/2003
 */

public abstract class AnnouncementBelongsToExecutionCourseTest
	extends ServiceNeedsAuthenticationTestCase {

	protected AnnouncementBelongsToExecutionCourseTest(String name) {
		super(name);
	}

	public void testAnnouncementNotBelongsToExecutionCourse() {
		Object[] serviceArguments = getAnnouncementUnsuccessfullArguments();
		IUserView userView = authenticateUser(getAuthorizedUser());
		
		try {
			gestor.executar(
				userView,
				getNameOfServiceToBeTested(),
				serviceArguments);
			fail(
				"Fail AnnouncementBelongsToExecutionCourseTestUnsuccessful"
					+ getNameOfServiceToBeTested());
		} catch (NotAuthorizedException ex) {
			/*
			 * O an�ncio existe mas n�o pertence � disciplina.
			 * Os pr�-filtros lan�am uma excepcao NotAuthorizedException,
			 * o servi�o n�o chega a ser invocado
			 */
			//Comparacao do dataset
			//compareDataSet(getDataSetFilePath());
			System.out.println(
				"AnnouncementBelongsToExecutionCourseTestUnsuccessful was SUCCESSFULY runned by service: "
					+ getNameOfServiceToBeTested());

		} catch (Exception ex) {
			fail(
				"Fail AnnouncementBelongsToExecutionCourseTestUnsuccessful"
					+ getNameOfServiceToBeTested());
		}
	}

	/*
	 *  (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase
	 */
	protected abstract String[] getAuthorizedUser();
	protected abstract String[] getUnauthorizedUser();
	protected abstract String[] getNonTeacherUser();
	protected abstract String getNameOfServiceToBeTested();
	protected abstract Object[] getAuthorizeArguments();
	protected abstract String getDataSetFilePath();
	protected abstract String getApplication();
	/*
	 * An�ncio que n�o pertence � disciplina escolhida
	 */
	protected abstract Object[] getAnnouncementUnsuccessfullArguments();
}