/*
 * Created on 15/Out/2003
 */
package ServidorAplicacao.Servicos.teacher;

import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 * 
 */
public abstract class BibliographicReferenceBelongsExecutionCourse
	extends ServiceNeedsAuthenticationTestCase {

	public BibliographicReferenceBelongsExecutionCourse(String name) {
		super(name);
	}

	public void testBibliographicReferenceExecutionCourse() {

		Object serviceArguments[] = getTestBibliographicReferenceSuccessfullArguments();

		Object result = null;

		try {
			result = gestor.executar(userView, getNameOfServiceToBeTested(), serviceArguments);
			System.out.println(
				"testBibliographicReferenceBelongsExecutionCourse was SUCCESSFULY runned by service: "
					+ getNameOfServiceToBeTested());

		} catch (NotAuthorizedException ex) {
			fail(getNameOfServiceToBeTested() + "fail testBibliographicReferenceBelongsExecutionCourse");
		} catch (Exception ex) {
			fail(getNameOfServiceToBeTested() + "fail testBibliographicReferenceBelongsExecutionCourse");
		}
	}

	public void testBibliographicReferenceNotBelongsExecutionCourse() {

		Object serviceArguments[] = getTestBibliographicReferenceUnsuccessfullArguments();

		Object result = null;

		try {
			result = gestor.executar(userView, getNameOfServiceToBeTested(), serviceArguments);
			fail(getNameOfServiceToBeTested() + "fail testBibliographicReferenceNotBelongsExecutionCourse");
		} catch (NotAuthorizedException ex) {

			System.out.println(
				"testBibliographicReferenceNotBelongsExecutionCourse was SUCCESSFULY runned by service: "
					+ getNameOfServiceToBeTested());
		} catch (Exception ex) {
			fail(getNameOfServiceToBeTested() + "fail testBibliographicReferenceNotBelongsExecutionCourse");
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

	protected abstract Object[] getTestBibliographicReferenceSuccessfullArguments();
	protected abstract Object[] getTestBibliographicReferenceUnsuccessfullArguments();

}
