package ServidorAplicacao.Servicos.teacher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import DataBeans.InfoRole;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.TestCaseServices;
import Util.RoleType;

/**
 * @author T�nia
 *
 */
public class ReadCurricularCourseListByExecutionCourseCodeTest extends TestCaseServices {
	/**
	 * @param testName
	 */
	public ReadCurricularCourseListByExecutionCourseCodeTest(String testName) {
		super(testName);
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseServices#getNameOfServiceToBeTested()
	 */
	protected String getNameOfServiceToBeTested() {
		return "ReadCurricularCourseListByExecutionCourseCode";
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseServices#getDataSetFilePath()
	 */
	protected String getDataSetFilePath() {
		return "etc/testDataSetForGesDis.xml";
	}

	public void readTest() {
		//Exectuion Course 24

		Object args[] = { new Integer(24)};
		GestorServicos gestorServicos = GestorServicos.manager();

		List curricularCourseList = new ArrayList();
		
		try {
			curricularCourseList = (List) gestorServicos.executar(authorizedUserView(), getNameOfServiceToBeTested(), args);
		} catch (FenixServiceException e) {
			fail("Executing  Service!");
			e.printStackTrace();
		}

		assertEquals("curricularCourseNumber", 1, curricularCourseList.size());
	}

	public IUserView authorizedUserView() {
		InfoRole infoRole = new InfoRole();
		infoRole.setRoleType(RoleType.TEACHER);

		Collection roles = new ArrayList();
		roles.add(infoRole);

		UserView userView = new UserView("user", roles);

		return userView;
	}
}
