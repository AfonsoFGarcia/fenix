package ServidorApresentacao.sop;

import java.util.HashSet;

import junit.framework.Test;
import junit.framework.TestSuite;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorApresentacao.TestCasePresentation;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.TipoSala;

/**
 * @author tfc130
 *
 */
public class CriarSalaFormActionTest extends TestCasePresentation {
  

  public static void main(java.lang.String[] args) {
    junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
    TestSuite suite = new TestSuite(CriarSalaFormActionTest.class);
        
    return suite;
  }

  public void setUp() {
    super.setUp();
    // define ficheiro de configura��o Struts a utilizar
    setServletConfigFile("/WEB-INF/tests/web-sop.xml");
    
  }
  
  public CriarSalaFormActionTest(String testName) {
    super(testName);
  }

  public void testSuccessfulCriarSala() {      
	getSession().setAttribute(SessionConstants.SESSION_IS_VALID, SessionConstants.SESSION_IS_VALID);

    // define mapping de origem
    setRequestPathInfo("", "/criarSalaForm");
    
    // Preenche campos do formul�rio
    addRequestParameter("name","Fa2");
    addRequestParameter("building","Pavilh�o Novas Licenciaturas");
    addRequestParameter("floor","0");
    addRequestParameter("type",(new Integer(TipoSala.ANFITEATRO)).toString());
    addRequestParameter("capacityNormal","100");
    addRequestParameter("capacityExame","50");

    // coloca credenciais na sess�o
    HashSet privilegios = new HashSet();
    privilegios.add("CriarSala");
    IUserView userView = new UserView("user", privilegios);
    getSession().setAttribute("UserView", userView);
    
    // invoca ac��o
    actionPerform();
    
    // verifica reencaminhamento
    verifyForward("Sucesso");
    
    //verifica ausencia de erros
    verifyNoActionErrors();
  }

  public void testUnsuccessfulCriarSala() {
	getSession().setAttribute(SessionConstants.SESSION_IS_VALID, SessionConstants.SESSION_IS_VALID);

    setRequestPathInfo("", "/criarSalaForm");
    addRequestParameter("name","Fa1");
    addRequestParameter("building","Pavilh�o Novas Licenciaturas");
    addRequestParameter("floor","0");
    addRequestParameter("type",(new Integer(TipoSala.ANFITEATRO)).toString());
    addRequestParameter("capacityNormal","100");
    addRequestParameter("capacityExame","50");
    actionPerform();
    verifyForwardPath("/naoExecutado.do");
    
//	verifyActionErrors(new String[] {"error.invalid.session"});
  }
  

}