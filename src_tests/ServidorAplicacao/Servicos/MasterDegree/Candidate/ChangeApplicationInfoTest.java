/*
 * ChangeApplicationInfoTest.java
 *
 * Created on 07 de Dezembro de 2002, 17:49
 *
 * Tests :
 * 
 * - 1 : Change Master Degree Candidate Personal Information
 * - 2 : Change Master Degree Candidate Personal Information (Unexisting
 *       Candidate)
 */

/**
 *
 * Autores : 
 *   - Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *   - Joana Mota (jccm@rnl.ist.utl.pt)
 *
 */

package ServidorAplicacao.Servicos.MasterDegree.Candidate;

import java.util.ArrayList;
import java.util.Collection;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoMasterDegreeCandidate;
import DataBeans.InfoRole;
import DataBeans.util.Cloner;
import Dominio.IMasterDegreeCandidate;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.Servico.UserView;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentMasterDegreeCandidate;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.RoleType;

public class ChangeApplicationInfoTest extends TestCaseServicosCandidato {
    
    public ChangeApplicationInfoTest(java.lang.String testName) {
        super(testName);
    }
    
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(ChangeApplicationInfoTest.class);
        
        return suite;
    }
    
    protected void setUp() {
        super.setUp();
    }
    
    protected void tearDown() {
        super.tearDown();
    }
    public void testAlterarCandidatoComPrivilegios() {
        System.out.println("- Test 1 : Change Master Degree Candidate Personal Information");
		
		UserView userView = this.getUserViewToBeTested("nmsn", true);
		
		InfoMasterDegreeCandidate infoMasterDegreeCandidate = new InfoMasterDegreeCandidate();
		
		infoMasterDegreeCandidate.setAverage(new Double(100));
		infoMasterDegreeCandidate.setMajorDegree("Curso");
		infoMasterDegreeCandidate.setMajorDegreeSchool("Escola");
		infoMasterDegreeCandidate.setMajorDegreeYear(new Integer(1));
		
		Object[] args = { infoMasterDegreeCandidate, userView}; 
		
        try {
            gestor.executar(userView, "ChangeApplicationInfo", args);
        } catch (Exception ex) {
            System.out.println("Service not Executed: " + ex);
        }   

		// Check the alteration

		InfoMasterDegreeCandidate newInfoMasterDegreeCandidate = this.readMasterDegreeCandidate("nmsn");
		
		assertEquals(infoMasterDegreeCandidate.getAverage(), newInfoMasterDegreeCandidate.getAverage());
		assertEquals(infoMasterDegreeCandidate.getMajorDegree(), newInfoMasterDegreeCandidate.getMajorDegree());
		assertEquals(infoMasterDegreeCandidate.getMajorDegreeSchool(), newInfoMasterDegreeCandidate.getMajorDegreeSchool());
		assertEquals(infoMasterDegreeCandidate.getMajorDegreeYear(), newInfoMasterDegreeCandidate.getMajorDegreeYear());

   }
   public void testReadMasterDegreeCandidateExistingWithoutRole() {
	   System.out.println("- Test 2 : Change Application Info without Role");
        
	   UserView userView = this.getUserViewToBeTested("nmsn", false);
	   Object args[] = new Object[1];
		
	   args[0] = userView;

	   InfoMasterDegreeCandidate infoMasterDegreeCandidate = null;

		try {
			infoMasterDegreeCandidate = (InfoMasterDegreeCandidate) gestor.executar(userView, "ChangeApplicationInfo", args);
		} catch (FenixServiceException ex) {
			// All is OK
		} catch (Exception ex) {
		   fail("Error Reading without Role");
		}
		assertNull(infoMasterDegreeCandidate);   
   }

   private UserView getUserViewToBeTested(String username, boolean withRole) {
	   Collection roles = new ArrayList();
	   InfoRole infoRole = new InfoRole();
	   if (withRole)
		infoRole.setRoleType(RoleType.MASTER_DEGREE_CANDIDATE);
	   roles.add(infoRole);
	   UserView userView = new UserView(username, roles);
	   return userView;
   }


   private InfoMasterDegreeCandidate readMasterDegreeCandidate(String username) {
	   IMasterDegreeCandidate masterDegreeCandidate = null;
	   ISuportePersistente sp = null;
	   try {
		   sp = SuportePersistenteOJB.getInstance();
		   IPersistentMasterDegreeCandidate persistentMasterDegreeCandidate = sp.getIPersistentMasterDegreeCandidate();
		   sp.iniciarTransaccao();
		   masterDegreeCandidate = persistentMasterDegreeCandidate.readMasterDegreeCandidateByUsername(username);
		   sp.confirmarTransaccao();
	   } catch (ExcepcaoPersistencia e) {
		   try {
			   sp.cancelarTransaccao();
		   } catch (ExcepcaoPersistencia ex) {
			   //ignored
		   }
		   e.printStackTrace();
		   fail("Error reading person to test equal password!");
	   }
		
	   return Cloner.copyIMasterDegreeCandidate2InfoMasterDegreCandidate(masterDegreeCandidate);
   }
}

