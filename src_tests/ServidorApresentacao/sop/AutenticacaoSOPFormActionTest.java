package ServidorApresentacao.sop;

import java.util.HashSet;

import junit.framework.Test;
import junit.framework.TestSuite;

import servletunit.struts.MockStrutsTestCase;

import Dominio.IPessoa;
import Dominio.Pessoa;
import Dominio.Privilegio;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoDocumentoIdentificacao;


/**
 * @author tfc130
 *
 */
public class AutenticacaoSOPFormActionTest extends MockStrutsTestCase {
  protected ISuportePersistente _suportePersistente = null;
  protected IPessoaPersistente _pessoaPersistente = null;
  protected IPessoa _pessoa1 = null;
  protected IPessoa _pessoa2 = null;

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
    
    ligarSuportePersistente();
    cleanData();
    _suportePersistente.iniciarTransaccao();

    HashSet privilegios = new HashSet();
    _pessoa1 = new Pessoa();
    _pessoa1.setNumeroDocumentoIdentificacao("0123456789");
    _pessoa1.setCodigoFiscal("9876543210");
    _pessoa1.setTipoDocumentoIdentificacao(new TipoDocumentoIdentificacao(
    			   TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE));
    _pessoa1.setUsername("nome");
    _pessoa1.setPassword("pass");
    privilegios.add(new Privilegio(_pessoa1, new String("CriarSitio")));
    _pessoa1.setPrivilegios(privilegios);
    _pessoaPersistente.escreverPessoa(_pessoa1);
    
    privilegios = new HashSet();
    _pessoa2 = new Pessoa();
    _pessoa2.setNumeroDocumentoIdentificacao("0321654987");
    _pessoa2.setCodigoFiscal("7894561230");
    _pessoa2.setTipoDocumentoIdentificacao(new TipoDocumentoIdentificacao(
    			 TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE));
    _pessoa2.setUsername("nome2");
    _pessoa2.setPassword("pass2");
    privilegios.add(new Privilegio(_pessoa2, new String("AlterarSitio")));
    _pessoa2.setPrivilegios(privilegios);
    _pessoaPersistente.escreverPessoa(_pessoa2);
    
    _suportePersistente.confirmarTransaccao();
  }
  
  public void tearDown() throws Exception {
    super.tearDown();
  }
  
  public AutenticacaoSOPFormActionTest(String testName) {
    super(testName);
  }

  public void testSuccessfulAutenticacao() {      
    // define mapping de origem
    setRequestPathInfo("", "/autenticacaoSOPForm");

    // Preenche campos do formul�rio
    addRequestParameter("utilizador","nome");
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
    UserView newUserView = (UserView) getSession().getAttribute("UserView");
    assertEquals("Verify UserView", newUserView.getUtilizador(), "nome");
    assertTrue("", newUserView.getPrivilegios().contains("CriarSitio"));
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
    verifyForwardPath("/autenticacaoSOP.do");
    
    //verifica existencia de erros
    verifyActionErrors(new String[] {"ServidorAplicacao.Servico.ExcepcaoAutenticacao"});

    
    //verifica UserView guardado na sess�o
    UserView newUserView = (UserView) getSession().getAttribute("UserView");
    assertEquals("Verify UserView", newUserView.getUtilizador(), "athirduser");
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