

package ServidorAplicacao.Servicos.MasterDegree.coordinator;

/**
 *
 * @author Nuno Nunes & Joana Mota 
 */
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoRole;
import DataBeans.util.Cloner;
import Dominio.ICurso;
import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import Dominio.IExecutionYear;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servicos.TestCaseServicos;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.RoleType;

public class ReadDegreeCandidatesTest extends TestCaseServicos {
	
	private InfoExecutionDegree infoExecutionDegree = null;
	
	public ReadDegreeCandidatesTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(ReadDegreeCandidatesTest.class);

		return suite;
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}


	public void testReadCoordinatedDegreesList() {
		System.out.println("- Test 1 : Read Degree Candidates List");
		
		UserView userView = this.getUserViewToBeTested("nmsn", true);
		this.ligarSuportePersistente();
		Object[] args = { this.infoExecutionDegree };
		
		List degreeCandidatesList = null;
		try {
			degreeCandidatesList = (List) _gestor.executar(userView, "ReadDegreeCandidates", args);
		} catch (FenixServiceException ex) {
			fail("Fenix Service Exception");
		} catch (Exception ex) {
			fail("Exception");
		}
		 
		assertNotNull(degreeCandidatesList);
		assertEquals(degreeCandidatesList.size(), 2);

	}
	
	private void ligarSuportePersistente() {

			ISuportePersistente sp = null;
	
			try {
				sp = SuportePersistenteOJB.getInstance();
				sp.iniciarTransaccao();
	
	
				ICursoPersistente persistentDegree = sp.getICursoPersistente();
				ICurso degree = persistentDegree.readBySigla("MEEC");
				assertNotNull(degree);
	
				IPersistentExecutionYear persistentExecutionYear = sp.getIPersistentExecutionYear();
				IExecutionYear executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
				assertNotNull(executionYear);
	
				IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = sp.getIPersistentDegreeCurricularPlan();
				IDegreeCurricularPlan degreeCurricularPlan = persistentDegreeCurricularPlan.readByNameAndDegree("plano2", degree);
				assertNotNull(degreeCurricularPlan);
	
				ICursoExecucaoPersistente persistentExecutionDegree = sp.getICursoExecucaoPersistente();
				ICursoExecucao executionDegree = null;
				executionDegree = persistentExecutionDegree.readByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan, executionYear);
				assertNotNull(executionDegree);
		
				this.infoExecutionDegree = Cloner.copyIExecutionDegree2InfoExecutionDegree(executionDegree);
				sp.confirmarTransaccao();
	
			} catch (ExcepcaoPersistencia excepcao) {
				try {
					sp.cancelarTransaccao();
				} catch (ExcepcaoPersistencia ex) {
					fail("ligarSuportePersistente: cancelarTransaccao");
				}
				fail("ligarSuportePersistente: confirmarTransaccao");
			}
		}

	private UserView getUserViewToBeTested(String username, boolean withRole) {
		Collection roles = new ArrayList();
		InfoRole infoRole = new InfoRole();
		if (withRole) infoRole.setRoleType(RoleType.COORDINATOR);
		else infoRole.setRoleType(RoleType.PERSON);
		roles.add(infoRole);
		UserView userView = new UserView(username, roles);
		return userView;
	}


}