/*
 * Created on 10/Out/2003
 *
 */
package ServidorAplicacao.Servicos.teacher;

import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 *
 */
public abstract class EvaluationMethodBelongsExecutionCourse
	extends ServiceNeedsAuthenticationTestCase {

	/**
	 * @param name
	 */
	public EvaluationMethodBelongsExecutionCourse(String name) {
		super(name);
	}

	public void testEvaluationMethodBelongsExecutionCourse() {

		Object serviceArguments[] = getTestEvaluationMethodSuccessfullArguments();

		Object result = null;

		try {
			result =
				gestor.executar(
					userView,
					getNameOfServiceToBeTested(),
					serviceArguments);
			System.out.println(
				"testEvaluationMethodBelongsExecutionCourse was SUCCESSFULY runned by service: "
					+ getNameOfServiceToBeTested());

		}
		catch (NotAuthorizedException ex) {
			fail(
				getNameOfServiceToBeTested()
					+ "fail testEvaluationMethodBelongsExecutionCourse");
		}
		catch (Exception ex) {
			fail(
				getNameOfServiceToBeTested()
					+ "fail testEvaluationMethodBelongsExecutionCourse");
		}
	}

	public void testEvaluationMethodNotBelongsExecutionCourse() {

		Object serviceArguments[] = getTestEvaluationMethodUnsuccessfullArguments();

		Object result = null;

		try {
			result =
				gestor.executar(
					userView,		//isto aqui deve ser com a userView para ele dar erro que o metodo avaliacao
									//n pertence ao execution course e n dar erro de autorizado ou n
					getNameOfServiceToBeTested(),
					serviceArguments);
			fail(
				getNameOfServiceToBeTested()
					+ "fail testEvaluationMethodNotBelongsExecutionCourse");
		}
		catch (NotAuthorizedException ex) {

			System.out.println(
				"testEvaluationMethodNotBelongsExecutionCourse was SUCCESSFULY runned by service: "
					+ getNameOfServiceToBeTested());
		}
		catch (Exception ex) {
			fail(
				getNameOfServiceToBeTested()
					+ "fail testEvaluationMethodNotBelongsExecutionCourse");
		}
	}
	
	
	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getAuthorizedUser()
	 */
	protected abstract String[] getAuthorizedUser();

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getUnauthorizedUser()
	 */
	protected abstract String[] getUnauthorizedUser();

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getNonTeacherUser()
	 */
	protected abstract String[] getNonTeacherUser();

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getNameOfServiceToBeTested()
	 */
	protected abstract String getNameOfServiceToBeTested();

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getAuthorizeArguments()
	 */
	protected abstract Object[] getAuthorizeArguments();

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.ServiceTestCase#getDataSetFilePath()
	 */
	protected abstract String getDataSetFilePath();

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getApplication()
	 */
	protected abstract String getApplication();
	
	protected abstract Object[] getTestEvaluationMethodSuccessfullArguments();
	protected abstract Object[] getTestEvaluationMethodUnsuccessfullArguments();
}
