package ServidorAplicacao.Servicos;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;

import junit.framework.TestCase;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import Tools.dbaccess;


public class TestCaseServicos extends TestCase {
	protected GestorServicos _gestor = null;
	protected IUserView _userView = null;
	protected IUserView _userView2 = null;
	
	protected String argsAutenticacao1[] = new String[3];
	protected String argsAutenticacao2[] = new String[3];

	private dbaccess dbAcessPoint = null;


	public TestCaseServicos(String testName) {
		super(testName);
		String argsAutenticacao3[] = {"user", "pass" , getApplication()};
		String argsAutenticacao4[] = {"julia", "pass" , getApplication()};
		this.argsAutenticacao1 = argsAutenticacao3;
		this.argsAutenticacao2 = argsAutenticacao4;
	}
	
	protected void setUp() {
		
		try {
			dbAcessPoint = new dbaccess();
			dbAcessPoint.openConnection();
			dbAcessPoint.backUpDataBaseContents("etc/testBackup.xml");
			
			dbAcessPoint.loadDataBase(getDataSetFilePath());
			
			dbAcessPoint.closeConnection();
			PersistenceBroker persistenceBroker = PersistenceBrokerFactory.defaultPersistenceBroker();
			persistenceBroker.clearCache();
		} catch (Exception ex) {
			System.out.println("Setup failed: " + ex);
		}

		_gestor = GestorServicos.manager();
//		String argsAutenticacao1[] = { "user", "pass" , getApplication()};
//		String argsAutenticacao2[] = { "julia", "pass" , getApplication()};
		try {
			_userView = (IUserView) _gestor.executar(null, "Autenticacao", this.argsAutenticacao1);
		} catch (Exception ex) {
			
			System.out.println("Servico n�o executado: " + ex);
			fail("Authenticating userview");
		}
		try {
			_userView2 = (IUserView) _gestor.executar(null, "Autenticacao", this.argsAutenticacao2);
		} catch (Exception ex) {
			System.out.println("Servico n�o executado: " + ex);
			fail("Authenticating userview2");
		}
	}

	/**
	 * @return
	 */
	protected String getDataSetFilePath() {
		return "etc/testDataSet.xml";
	}
	
	public String getApplication(){
		return Autenticacao.EXTRANET;
	}

	protected void tearDown() {
//		try {
//			dbAcessPoint.openConnection();
//			dbAcessPoint.loadDataBase("etc/testBackup.xml");
//			dbAcessPoint.closeConnection();
//		} catch (Exception ex) {
//			System.out.println("Tear down failed: " +ex);
//		}
	}
}