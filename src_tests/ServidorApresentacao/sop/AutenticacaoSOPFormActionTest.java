package ServidorApresentacao.sop;

import java.util.HashSet;

import junit.framework.Test;
import junit.framework.TestSuite;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorApresentacao.TestCasePresentation;
import ServidorApresentacao.Action.sop.utils.SessionConstants;


/**
 * @author tfc130
 *
 */
public class AutenticacaoSOPFormActionTest extends TestCasePresentation {
  

  public static void main(java.lang.String[] args) {
    junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
    TestSuite suite = new TestSuite(AutenticacaoSOPFormActionTest.class);
        
    return suite;
  }

  public void setUp() throws Exception {
    super.setUp();
    // define ficheiro de configura��o a utilizar
    setServletConfigFile("/WEB-INF/tests/web-sop.xml");
    
    
  }
  
  
  
  public AutenticacaoSOPFormActionTest(String testName) {
    super(testName);
  }

  public void testSuccessfulAutenticacao() {      
    // define mapping de origem
    setRequestPathInfo("", "/autenticacaoSOPForm");

    // Preenche campos do formul�rio
    addRequestParameter("utilizador","user");
    addRequestParameter("password","pass");

    // coloca credenciais na sess�o
    HashSet privilegios = new HashSet();
    IUserView userView = new UserView("athirduser", privilegios);
    getSession().setAttribute("UserView", userView);
    
    // invoca ac��o
    actionPerform();
    
    // verifica reencaminhamento
    verifyForward("SOP");
    
    //verifica ausencia de erros
    verifyNoActionErrors();
    
    //verifica UserView guardado na sess�o
    UserView newUserView = (UserView) getSession().getAttribute(SessionConstants.U_VIEW);
    assertEquals("Verify UserView", newUserView.getUtilizador(), "user");
    assertTrue("Verify authorization", newUserView.getPrivilegios().contains("CriarSala"));
  }
  

  public void testUnsuccessfulAutorizacao() {
    // define mapping de origem
    setRequestPathInfo("", "/autenticacaoSOPForm");
    
    // Preenche campos do formul�rio
    addRequestParameter("utilizador","nome");
    addRequestParameter("password","xpto");

    // coloca credenciais na sess�o
    HashSet privilegios = new HashSet();
    IUserView userView = new UserView("athirduser", privilegios);
    getSession().setAttribute("UserView", userView);
    
    // invoca ac��o
    actionPerform();
    
    // verifica endere�o do reencaminhamento (ExcepcaoAutorizacao)
    verifyForwardPath("/autenticacaoSOP.jsp");
    
    //verifica existencia de erros
    verifyActionErrors(new String[] {"errors.invalidAuthentication"});

    
    //verifica UserView guardado na sess�o
    UserView newUserView = (UserView) getSession().getAttribute("UserView");
    assertEquals("Verify UserView", newUserView.getUtilizador(), "athirduser");
  }

  
    
  
}