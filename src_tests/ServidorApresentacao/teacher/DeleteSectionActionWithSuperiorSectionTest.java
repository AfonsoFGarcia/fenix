/*
 * Created on 8/Abr/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorApresentacao.teacher;

import java.util.Hashtable;
import java.util.Map;

import DataBeans.InfoSection;
import DataBeans.util.Cloner;
import Dominio.IDisciplinaExecucao;
import Dominio.ISection;
import Dominio.ISite;
import ServidorApresentacao.TestCasePresentationTeacherPortal;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac1
 *
 */

public class DeleteSectionActionWithSuperiorSectionTest extends TestCasePresentationTeacherPortal {

	/**
	 * @param testName
	 */
	public DeleteSectionActionWithSuperiorSectionTest(String testName) {
		super(testName);
	}

	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getServletConfigFile()
	 */
	protected String getServletConfigFile() {
		return "/WEB-INF/web.xml";
	}

	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getRequestPathInfoPathAction()
	 */
	protected String getRequestPathInfoPathAction() {
		return "/teacher";
	}

	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getRequestPathInfoNameAction()
	 */
	protected String getRequestPathInfoNameAction() {
		return "/deleteSection";
	}

	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getSuccessfulForward()
	 */
	protected String getSuccessfulForward() {
		return "AccessSectionManagement";
	}


	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInSessionForActionToBeTestedSuccessfuly()
	 */
	protected Map getItemsToPutInSessionForActionToBeTestedSuccessfuly() {
	
		Map result = new Hashtable();
	try{
		
		ISuportePersistente sp = SuportePersistenteOJB.getInstance();
		sp.iniciarTransaccao();
		
		IDisciplinaExecucao executionCourse = sp.getIDisciplinaExecucaoPersistente().readBySiglaAndAnoLectivoAndSiglaLicenciatura("PO", "2002/2003", "MEEC");
		ISite site = sp.getIPersistentSite().readByExecutionCourse(executionCourse);
		ISection superiorSection =sp.getIPersistentSection().readBySiteAndSectionAndName(site, null, "seccao1dePO");
		 
		ISection section = sp.getIPersistentSection().readBySiteAndSectionAndName(site, superiorSection, "subSeccao1dePO");
		InfoSection infoSection = Cloner.copyISection2InfoSection(section);
		sp.confirmarTransaccao(); 
	    
		result.put(SessionConstants.INFO_SECTION, infoSection);
	
	  }catch (ExcepcaoPersistencia exception) {
	  exception.printStackTrace(System.out);
	  fail("Using services at getItemsToPutInSessionForActionToBeTestedSuccessfuly()!");
	}

		return result;
	}

	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInSessionForActionToBeTestedUnsuccessfuly()
	 */
	protected Map getItemsToPutInSessionForActionToBeTestedUnsuccessfuly() {
		return null;
	}

	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInRequestForActionToBeTestedSuccessfuly()
	 */
	protected Map getItemsToPutInRequestForActionToBeTestedSuccessfuly() {
		return null;
	}

	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInRequestForActionToBeTestedUnsuccessfuly()
	 */
	protected Map getItemsToPutInRequestForActionToBeTestedUnsuccessfuly() {
		return null;
	}

	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getExistingAttributesListToVerifyInSuccessfulExecution()
	 */
	protected Map getExistingAttributesListToVerifyInSuccessfulExecution() {
		return null;
	}

	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getNonExistingAttributesListToVerifyInSuccessfulExecution()
	 */
	protected Map getNonExistingAttributesListToVerifyInSuccessfulExecution() {
		return null;
	}

	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getExistingAttributesListToVerifyInUnsuccessfulExecution()
	 */
	protected Map getExistingAttributesListToVerifyInUnsuccessfulExecution() {
		return null;
	}

	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getNonExistingAttributesListToVerifyInUnsuccessfulExecution()
	 */
	protected Map getNonExistingAttributesListToVerifyInUnsuccessfulExecution() {
		return null;
	}
	
}