
/*
 * CriarSalaServicosTest.java
 * JUnit based test
 *
 * Created on 24 de Outubro de 2002, 12:00
 */

package ServidorAplicacao.Servicos.sop;

/**
 *
 * @author tfc130
 */
import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoRoom;
import ServidorAplicacao.Servicos.TestCaseNeedAuthorizationServices;
import Util.TipoSala;

public class CriarSalaServicosTest extends TestCaseNeedAuthorizationServices {

	public CriarSalaServicosTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(CriarSalaServicosTest.class);

		return suite;
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}

	/**
	 * @see ServidorAplicacao.Servicos.TestCaseNeedAuthorizationServices#getNameOfServiceToBeTested()
	 */
	protected String getNameOfServiceToBeTested() {
		return "CriarSala";
	}

	// write existing sala
	public void testCreateExistingSala() {
		Object argsCriarSala[] = new Object[1];
		argsCriarSala[0] = new InfoRoom(new String("Ga1"), new String("Pavilhilh�o Central"), new Integer(0), new TipoSala(TipoSala.ANFITEATRO), new Integer(100), new Integer(50));

		Object result = null;
		try {
			result = _gestor.executar(_userView, getNameOfServiceToBeTested(), argsCriarSala);
			fail("testCreateExistingSala");
		} catch (Exception ex) {
			assertNull("testCreateExistingSala", result);
		}
	}

	// write new non-existing sala
	public void testCreateNonExistingSala() {
		Object argsCriarSala[] = new Object[1];
		argsCriarSala[0] = new InfoRoom(new String("Ga4"), new String("Pavilhilh�o Central"), new Integer(1), new TipoSala(TipoSala.ANFITEATRO), new Integer(100), new Integer(50));

		Object result = null;
		try {
			result = _gestor.executar(_userView, getNameOfServiceToBeTested(), argsCriarSala);
			assertEquals("testCreateNonExistingSala", Boolean.TRUE.booleanValue(), ((Boolean) result).booleanValue());
		} catch (Exception ex) {
			fail("testCreateNonExistingSala");
		}
	}

	// TODO This class must extend class TestCaseNeedAuthorizationServices so this method will be gonne
	protected boolean needsAuthorization() {
		return true;
	}

}