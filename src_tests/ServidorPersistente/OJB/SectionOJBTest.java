/*
 * SectionOJBTest.java
 * JUnit based test
 *
 * Created on 11 de March de 2003, 11:09
 */

package ServidorPersistente.OJB;


/**
 *
 * @author lmac1
 */

import junit.framework.Test;
import junit.framework.TestSuite;
import Dominio.IDisciplinaExecucao;
import Dominio.ISection;
import Dominio.ISite;
import Dominio.Section;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentSection;
import ServidorPersistente.IPersistentSite;

public class SectionOJBTest extends TestCaseOJB {
  
  SuportePersistenteOJB persistentSupport = null; 
  IPersistentSite persistentSite = null;
  IPersistentSection persistentSection = null;
  IDisciplinaExecucaoPersistente persistentExecutionCourse = null;
  
  public SectionOJBTest(String testName) {
	super(testName);
  }
    
  public static void main(String[] args) {
	junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
	TestSuite suite = new TestSuite(SectionOJBTest.class);

	return suite;
  }
    
  protected void setUp() {
	super.setUp();    

	try {
		persistentSupport = SuportePersistenteOJB.getInstance();
	} catch (ExcepcaoPersistencia e) {
		e.printStackTrace();
		fail("Error");
	}
	persistentSite = persistentSupport.getIPersistentSite();
	persistentSection = persistentSupport.getIPersistentSection();
	persistentExecutionCourse = persistentSupport.getIDisciplinaExecucaoPersistente();
  }
    
  protected void tearDown() {
	super.tearDown();
  }
  
  /** Test of readBySiteAndSectionAndName() method, of class ServidorPersistente.OJB.SectionOJB.*/

  
  
	public void testReadBySiteAndSectionAndName() {
	
	ISection section = null;
	ISite site = null;
	ISection superiorSection = null;
	ISection supSection2 = null;
	IDisciplinaExecucao executionCourse = null;
	
	//	read existing section without superiorSection
	

	try {
		 persistentSupport.iniciarTransaccao();

		 executionCourse = persistentExecutionCourse.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI", "2002/2003", "LEIC");
		 assertNotNull(executionCourse);
		 site = persistentSite.readByExecutionCourse(executionCourse);
		 assertNotNull(site);
		 section = persistentSection.readBySiteAndSectionAndName(site, superiorSection,"Seccao1deTFCI");

		 persistentSupport.confirmarTransaccao();
		 }
	
	 catch (ExcepcaoPersistencia ex) 
		 {
		   fail("testReadBySiteAndSectionAndName:fail read existing section ");
		 }
		
		 assertNotNull(section);
		
		 assertEquals(section.getName(), "Seccao1deTFCI");
		 assertEquals(section.getSite(), site);
		 assertEquals(section.getSuperiorSection(), superiorSection);
		 
		 System.out.println("Acabei a 1� parte do teste");
	
	
	
	
	//	read existing section with superiorSection
	
   try {
		persistentSupport.iniciarTransaccao();
		executionCourse = persistentExecutionCourse.readBySiglaAndAnoLectivoAndSiglaLicenciatura("PO", "2002/2003", "LEEC");
		assertNotNull(executionCourse);
	    
		site = persistentSite.readByExecutionCourse(executionCourse);
		assertNotNull(site);
	
		superiorSection = persistentSection.readBySiteAndSectionAndName(site, null,"Seccao1dePO");	
		assertNotNull(superiorSection);
		
		assertEquals(superiorSection.getName(), "Seccao1dePO");
		assertEquals(superiorSection.getSite(), site);
		assertEquals(superiorSection.getSuperiorSection(), null);
		
		System.out.println("superiorSection: " +superiorSection);
		System.out.println("superiorSection internal Code: " + ((Section) superiorSection).getInternalCode());
		
		
		section= persistentSection.readBySiteAndSectionAndName(site, superiorSection,"SubSeccao2dePO");

		persistentSupport.confirmarTransaccao();
		}
	
	catch (ExcepcaoPersistencia ex) 
		{
		  fail("testReadBySiteAndSectionAndName:fail read existing section ");
		}
		
		assertNotNull(section);
		
		assertEquals(section.getName(), "SubSeccao2dePO");
		assertEquals(section.getSite(), site);
		assertEquals(section.getSuperiorSection(), superiorSection);
	
		System.out.println("Acabei a 2� parte do teste");
     
    
     
	// read unexisting section (unexisting name)
	try {
			persistentSupport.iniciarTransaccao();
	  
			executionCourse = persistentExecutionCourse.readBySiglaAndAnoLectivoAndSiglaLicenciatura("PO", "2002/2003", "LEEC");
			assertNotNull(executionCourse);
	    
			site = persistentSite.readByExecutionCourse(executionCourse);
			assertNotNull(site);
	
			section = persistentSection.readBySiteAndSectionAndName(site , null , "section5");
			persistentSupport.confirmarTransaccao();
			assertNull("testReadBySiteAndSectionAndName:fail read unexisting section", section);
		} catch (ExcepcaoPersistencia ex) {
		fail("testreadBySiteAndSectionAndName:fail read unexisting section");
		}
	
	
   //	read unexisting section (section doesnt belong to the site)
   try {
		 persistentSupport.iniciarTransaccao();
		 section = persistentSection.readBySiteAndSectionAndName(site , null , "seccao1deTFCI");
		 persistentSupport.confirmarTransaccao();
		 assertNull("testReadBySiteAndSectionAndName:fail read unexisting section", section);
	   } catch (ExcepcaoPersistencia ex) {
		 fail("testreadBySiteAndSectionAndName:fail read unexisting section");
	   }


	   //	read unexisting section (section doesnt belong to the superiorSection)
	   try {
			 persistentSupport.iniciarTransaccao();
			 supSection2= persistentSection.readBySiteAndSectionAndName(site, null,"SubSeccao2dePO");
			 section = persistentSection.readBySiteAndSectionAndName(site , supSection2 , "seccao1dePO");
			 persistentSupport.confirmarTransaccao();
			 assertNull("testReadBySiteAndSectionAndName:fail read unexisting section", section);
		   } catch (ExcepcaoPersistencia ex) {
			 fail("testreadBySiteAndSectionAndName:fail read unexisting section");
		   }

}
}

