/**
 * 
 * Project sop 
 * Package ServidorApresentacao.sop 
 * Created on 15/Jan/2003
 */
package ServidorApresentacao.sop;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;

import junit.framework.Test;
import junit.framework.TestSuite;
import servletunit.struts.MockStrutsTestCase;
import DataBeans.InfoDegree;
import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionYear;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorApresentacao.Action.sop.ClassesManagerDispatchAction;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author Jo�o Mota
 *
 */
public class ClassesManagerDispatchActionTest extends MockStrutsTestCase {

	/**
	 * Constructor for ClassesManagerDispatchAction.
	 * @param arg0
	 */
	public ClassesManagerDispatchActionTest(String arg0) {
		super(arg0);
	}
	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(ClassesManagerDispatchActionTest.class);
		return suite;
	}

	public void setUp() throws Exception {
		super.setUp();
		// define ficheiro de configuracao Struts a utilizar
		setServletConfigFile("/WEB-INF/tests/web-sop.xml");

	}

	public void testUnAuthorizedListClasses() {
		//set request path
		setRequestPathInfo("sop", "/ClassesManagerDA");
		//sets needed objects to session/request
		addRequestParameter("method", "listClasses");
		//falta  contexto
		// coloca credenciais na sessao
		HashSet privilegios = new HashSet();
		IUserView userView = new UserView("user", privilegios);
		getSession().setAttribute("UserView", userView);

		//		Coloca contexto em sess�o
		InfoDegree iL =
			new InfoDegree(
				"LEIC",
				"Licenciatura de Engenharia Informatica e de Computadores");
		InfoExecutionDegree iLE =
			new InfoExecutionDegree(
				new InfoDegreeCurricularPlan("plano1", iL),
				new InfoExecutionYear("2002/2003"));
		getSession().setAttribute(
			SessionConstants.INFO_EXECUTION_PERIOD_KEY,
			new InfoExecutionPeriod(
				"2� Semestre",
				new InfoExecutionYear("2002/2003")));
		getSession().setAttribute(
			SessionConstants.INFO_EXECUTION_DEGREE_KEY,
			iLE);
		//action perform
		actionPerform();
		//verify that there are errors
		String[] errors = { "ServidorAplicacao.NotAuthorizedException" };
		verifyActionErrors(errors);

	}

	public void testAuthorizedListClasses() {
		//set request path
		setRequestPathInfo("sop", "/ClassesManagerDA");
		//sets needed objects to session/request
		addRequestParameter("method", "listClasses");
		//falta  contexto
		// coloca credenciais na sessao
		HashSet privilegios = new HashSet();

		privilegios.add("LerTurmas");
		IUserView userView = new UserView("user", privilegios);
		getSession().setAttribute("UserView", userView);

		//		Coloca contexto em sess�o
		InfoDegree iL =
			new InfoDegree(
				"LEIC",
				"Licenciatura de Engenharia Informatica e de Computadores");
		InfoExecutionDegree iLE =
			new InfoExecutionDegree(
				new InfoDegreeCurricularPlan("plano1", iL),
				new InfoExecutionYear("2002/2003"));
		getSession().setAttribute(
			SessionConstants.INFO_EXECUTION_PERIOD_KEY,
			new InfoExecutionPeriod(
				"2� Semestre",
				new InfoExecutionYear("2002/2003")));
		getSession().setAttribute(
			SessionConstants.INFO_EXECUTION_DEGREE_KEY,
			iLE);
		getSession().setAttribute(
					SessionConstants.CURRICULAR_YEAR_KEY,
					new Integer(1));

		//action perform
		actionPerform();
		//verify that there are no errors
		verifyNoActionErrors();
		//verify forward
		verifyForward("listClasses");
		//verify that all that should be in session is
		GestorServicos gestor = GestorServicos.manager();
		Object argsLerTurmas[] =
			{
				iLE,
				new InfoExecutionPeriod(
					"2� Semestre",
					new InfoExecutionYear("2002/2003")),
				new Integer(1)};

		try {
			List classesList =
				(List) gestor.executar(
					SessionUtils.getUserView(getRequest()),
					"LerTurmas",
					argsLerTurmas);
			if (classesList != null && !classesList.isEmpty()) {
				assertEquals(
					classesList,
					getRequest().getAttribute(ClassesManagerDispatchAction.CLASS_LIST_KEY));
			}
		} catch (Exception e) {
		}

	}

	/**
	 * Method verifySessionAttributes.
	 * @param httpSession
	 * @param attributesList
	 */
	private void verifySessionAttributes(
		HttpSession session,
		List existingAttributesList,
		List nonExistingAttributesList) {
		Enumeration attNames = session.getAttributeNames();

		verifySessionAttributes(session, existingAttributesList, true);
		verifySessionAttributes(session, nonExistingAttributesList, false);
	}
	/**
	 * Method verifySessionAttributes.
	 * @param session
	 * @param existingAttributesList
	 * @param exists
	 */
	private void verifySessionAttributes(
		HttpSession session,
		List list,
		boolean exists) {
		if (list != null) {
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				String attName = (String) iterator.next();
				if (!((session.getAttribute(attName) != null) == exists)) {
					String message = " Session contains attribute ";
					if (exists)
						message = "Session doesn't contains attribute ";
					fail(message + attName + ".");
				}
			}
		}

	}

}
