package ServidorApresentacao.publico;

import junit.framework.Test;
import junit.framework.TestSuite;
import servletunit.struts.MockStrutsTestCase;

/**
 * @author tfc130
 *
 */
public class PrepareConsultRoomsFormActionTest extends MockStrutsTestCase {

  public static void main(java.lang.String[] args) {
    junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
    TestSuite suite = new TestSuite(PrepareConsultRoomsFormActionTest.class);
        
    return suite;
  }

  public void setUp() throws Exception {
    super.setUp();
    // define ficheiro de configura��o Struts a utilizar
    setServletConfigFile("/WEB-INF/tests/web-publico.xml");
  }
  
  public void tearDown() throws Exception {
    super.tearDown();
  }
  
  public PrepareConsultRoomsFormActionTest(String testName) {
    super(testName);
  }

  public void testSuccessfulPrepareConsultRooms() {      
    // define mapping de origem
    setRequestPathInfo("publico", "/prepareConsultRooms");

	// colocar qq coisa na sess�o.
	// O simulator tem que l� ter qq coisa para funcionar.
	String xpto = "This is strange, I cannot explain it!";
	getSession().setAttribute("UserView", xpto);

    // invoca ac��o
    actionPerform();

    // verifica reencaminhamento
    verifyForward("Sucess");

    //verifica ausencia de erros
    verifyNoActionErrors();
  }

}