package ServidorApresentacao;

import junit.framework.Test;
import junit.framework.TestSuite;
import servletunit.struts.MockStrutsTestCase;
import Dominio.IPessoa;
import Dominio.Pessoa;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.security.PasswordEncryptor;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoDocumentoIdentificacao;


/**
 * @author jorge
 *
 */
public class AuthenticationActionTest extends MockStrutsTestCase {
  protected ISuportePersistente _suportePersistente = null;
  protected IPessoaPersistente _pessoaPersistente = null;
  protected IPessoa _pessoa1 = null;
  protected IPessoa _pessoa2 = null;

  public static void main(java.lang.String[] args) {
    junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
    TestSuite suite = new TestSuite(AuthenticationActionTest.class);
        
    return suite;
  }

  public void setUp() throws Exception {
    super.setUp();
    // define ficheiro de configura��o a utilizar
    setServletConfigFile("/WEB-INF/web.xml");
    
    ligarSuportePersistente();
    cleanData();
    _suportePersistente.iniciarTransaccao();

    _pessoa1 = new Pessoa();
    _pessoa1.setNumeroDocumentoIdentificacao("0123456789");
    _pessoa1.setCodigoFiscal("9876543210");
    _pessoa1.setTipoDocumentoIdentificacao(new TipoDocumentoIdentificacao(
    			   TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE));
    _pessoa1.setUsername("user");
    _pessoa1.setPassword(PasswordEncryptor.encryptPassword("pass"));
    _pessoaPersistente.escreverPessoa(_pessoa1);
    
    _pessoa2 = new Pessoa();
    _pessoa2.setNumeroDocumentoIdentificacao("0321654987");
    _pessoa2.setCodigoFiscal("7894561230");
    _pessoa2.setTipoDocumentoIdentificacao(new TipoDocumentoIdentificacao(
    			 TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE));
    _pessoa2.setUsername("nome2");
    _pessoa2.setPassword(PasswordEncryptor.encryptPassword("pass2"));
    _pessoaPersistente.escreverPessoa(_pessoa2);    
    
    _suportePersistente.confirmarTransaccao();
  }
  
  public void tearDown() throws Exception {
    super.tearDown();
  }
  
  public AuthenticationActionTest(String testName) {
    super(testName);
  }

  public void testSuccessfulAutenticacao() {      
    // define mapping de origem
    setRequestPathInfo("/login");
    
    // Preenche campos do formul�rio
    addRequestParameter("username","user");
    addRequestParameter("password","pass");

    // coloca credenciais na sess�o
//    HashSet privilegios = new HashSet();
//    IUserView userView = new UserView("athirduser", privilegios);
//    getSession().setAttribute("UserView", userView);
    
    // invoca ac��o
    actionPerform();
    
	//verifica ausencia de erros
	verifyNoActionErrors();

    // verifica reencaminhamento
    verifyForward("sucess");
    
    
    //verifica UserView guardado na sess�o
    UserView newUserView = (UserView) getSession().getAttribute(SessionConstants.U_VIEW);
    assertEquals("Verify UserView", newUserView.getUtilizador(), "user");

  }
  

  public void testUnsuccessfulAutorizacao() {
    // define mapping de origem
    setRequestPathInfo("/login");
    
    // Preenche campos do formul�rio
    addRequestParameter("username","user");
    addRequestParameter("password","xpto");

    // coloca credenciais na sess�o
//    HashSet privilegios = new HashSet();
//    IUserView userView = new UserView("athirduser", privilegios);
//    getSession().setAttribute("UserView", userView);
    
    // invoca ac��o
    actionPerform();
    
    // verifica endere�o do reencaminhamento (ExcepcaoAutorizacao)
    verifyForwardPath("/loginPage.jsp");
    
    //verifica existencia de erros
    verifyActionErrors(new String[] {"errors.invalidAuthentication"});

    
    //verifica UserView guardado na sess�o
    UserView newUserView = (UserView) getSession().getAttribute(SessionConstants.U_VIEW);
    assertNull("UserView in session!", newUserView);
  }

  protected void ligarSuportePersistente() {
    try {
      _suportePersistente = SuportePersistenteOJB.getInstance();
    } catch (ExcepcaoPersistencia excepcao) {
      fail("Exception when opening database");
    }
    _pessoaPersistente = _suportePersistente.getIPessoaPersistente();
  }
    
  protected void cleanData() {
    try {
      _suportePersistente.iniciarTransaccao();
      //_pessoaPersistente.deleteAll();
      _pessoaPersistente.apagarTodasAsPessoas();
      _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia excepcao) {
      fail("Exception when cleaning data");
    }
  } 
}