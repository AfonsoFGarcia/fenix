/*
 *
 * Created on 03/09/2003
 */

package ServidorAplicacao.Servicos.student;

/**
 *
 * @author asnr and scpo
 */
import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoSiteStudentGroup;
import DataBeans.InfoSiteStudentInformation;
import DataBeans.util.Cloner;
import Dominio.IStudentGroup;
import Dominio.StudentGroup;
import ServidorAplicacao.Servicos.TestCaseReadServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ReadStudentGroupInformationTest extends TestCaseReadServices {

	public ReadStudentGroupInformationTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(ReadStudentGroupInformationTest.class);

		return suite;
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}

	protected String getNameOfServiceToBeTested() {
		return "ReadStudentGroupInformation";
	}

	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
	
		return null;
	}

	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
		Object[] result = { new Integer(8)};
		return result;

	}

	protected int getNumberOfItemsToRetrieve() {
		return 1;
	}

	protected Object getObjectToCompare() {
		InfoSiteStudentGroup infoSiteStudentGroup = new InfoSiteStudentGroup();
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();
			IStudentGroup studentGroup =(IStudentGroup) sp.getIPersistentStudentGroup().readByOId(
								new StudentGroup(new Integer(8)),
								false);
			
			InfoSiteStudentInformation infoSiteStudentInformation = new InfoSiteStudentInformation();
			infoSiteStudentInformation.setName("Nome da Pessoa");
			infoSiteStudentInformation.setEmail("s@h.c");
			infoSiteStudentInformation.setNumber(new Integer(42222));
			infoSiteStudentInformation.setUsername("13");
			
			List infoSiteStudentInformationList = new ArrayList();
			infoSiteStudentInformationList.add(infoSiteStudentInformation);
			infoSiteStudentGroup.setInfoSiteStudentInformationList(infoSiteStudentInformationList);
			infoSiteStudentGroup.setInfoStudentGroup(Cloner.copyIStudentGroup2InfoStudentGroup(studentGroup));
			infoSiteStudentGroup.setNrOfElements(new Integer(3));						
			sp.confirmarTransaccao();
		}catch (ExcepcaoPersistencia ex) {
		ex.printStackTrace();
		}
		return infoSiteStudentGroup;
		
	}

}
