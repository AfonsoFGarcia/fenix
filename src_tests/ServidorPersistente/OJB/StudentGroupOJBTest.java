/*
 * Created on 12/Mai/2003
 *
 */
package ServidorPersistente.OJB;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.IGroupProperties;
import Dominio.IStudentGroup;
import Dominio.ITurno;
import Dominio.StudentGroup;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.IPersistentGroupProperties;
import ServidorPersistente.IPersistentStudentGroup;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoPersistente;

/**
 * @author asnr and scpo
 *
 */
public class StudentGroupOJBTest extends TestCaseOJB {
	ISuportePersistente persistentSupport = null;
	IDisciplinaExecucaoPersistente persistentExecutionCourse = null;
	IPersistentGroupProperties persistentGroupProperties = null;
	IPersistentStudentGroup persistentStudentGroup = null;
	ITurnoPersistente persistentShift = null;
	IPersistentExecutionYear persistentExecutionYear = null;
	IPersistentExecutionPeriod persistentExecutionPeriod = null;

	IGroupProperties groupProperties1 = null;
	IGroupProperties groupProperties2 = null;
	ITurno shift1 = null;
	ITurno shift2 = null;

	public StudentGroupOJBTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(StudentGroupOJBTest.class);

		return suite;
	}

	protected void setUp() {

		try {

			persistentSupport = SuportePersistenteOJB.getInstance();
			persistentExecutionCourse = persistentSupport.getIDisciplinaExecucaoPersistente();
			persistentExecutionYear = persistentSupport.getIPersistentExecutionYear();
			persistentExecutionPeriod = persistentSupport.getIPersistentExecutionPeriod();
			persistentGroupProperties = persistentSupport.getIPersistentGroupProperties();
			persistentStudentGroup = persistentSupport.getIPersistentStudentGroup();
			persistentShift = persistentSupport.getITurnoPersistente();
			persistentSupport.iniciarTransaccao();

			IDisciplinaExecucao executionCourse1 =
				persistentExecutionCourse.readBySiglaAndAnoLectivoAndSiglaLicenciatura("PO", "2002/2003", "MEEC");
			IExecutionYear executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");

			IExecutionPeriod executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear("2� Semestre", executionYear);

			IDisciplinaExecucao executionCourse2 =
				persistentExecutionCourse.readByExecutionCourseInitialsAndExecutionPeriod("TFCII", executionPeriod);

			groupProperties1 = persistentGroupProperties.readGroupPropertiesByExecutionCourseAndName(executionCourse1, "nameB");
			groupProperties2 = persistentGroupProperties.readGroupPropertiesByExecutionCourseAndName(executionCourse2, "projecto A");

			shift1 = persistentShift.readByNameAndExecutionCourse("turno_po_teorico", executionCourse1);
			shift2 = persistentShift.readByNameAndExecutionCourse("turno3", executionCourse2);

			persistentSupport.confirmarTransaccao();

		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			fail("Error");
		}

		super.setUp();

	}

	protected void tearDown() {
		super.tearDown();
	}

	/** Test of readBy method, of class ServidorPersistente.OJB.StudentGroupOJB. */
	public void testReadStudentGroupByGroupPropertiesAndGroupNumber() {

		//read existing StudentGroup  
		try {
			persistentSupport.iniciarTransaccao();
			IStudentGroup existingStudentGroup =
				persistentStudentGroup.readStudentGroupByGroupPropertiesAndGroupNumber(groupProperties2, new Integer(1));
			assertNotNull(existingStudentGroup);
			persistentSupport.confirmarTransaccao();

		} catch (ExcepcaoPersistencia ex) {
			fail("testReadBy:fail read existing StudentGroup");
		}

		// read unexisting StudentGroup
		try {
			persistentSupport.iniciarTransaccao();
			IStudentGroup unexistingStudentGroup =
				persistentStudentGroup.readStudentGroupByGroupPropertiesAndGroupNumber(groupProperties1, new Integer(99));
			assertNull(unexistingStudentGroup);
			persistentSupport.confirmarTransaccao();

		} catch (ExcepcaoPersistencia ex) {
			fail("testReadBy:fail read non - existing StudentGroup");
		}
	}

		/** Test of readAllStudentGroupByGroupProperties method, of class ServidorPersistente.OJB.StudentGroupOJB. */
		  public void testReadAllStudentGroupByGroupProperties() {
			
		  	
				  //read existing list of StudentGroups  
				  try {
						  persistentSupport.iniciarTransaccao();	
						  List existingStudentGroupList = persistentStudentGroup.readAllStudentGroupByGroupProperties(groupProperties2);
						  assertEquals(existingStudentGroupList.size(),4);			
						  persistentSupport.confirmarTransaccao();
	
					} catch (ExcepcaoPersistencia ex) {
						fail("testReadBy:fail read existing list of StudentGroups");
					}
	
					
				}
	  
		/** Test of readAllStudentGroupByGroupProperties method, of class ServidorPersistente.OJB.StudentGroupOJB. */
		  public void testReadAllStudentGroupByGroupPropertiesAndShift() {
			
		  	
				  //read existing list of StudentGroups  
				  try {
						  persistentSupport.iniciarTransaccao();	
						  List existingStudentGroupList = persistentStudentGroup.readAllStudentGroupByGroupPropertiesAndShift(groupProperties2,shift2);
						  assertEquals(existingStudentGroupList.size(),2);			
						  persistentSupport.confirmarTransaccao();
	
					} catch (ExcepcaoPersistencia ex) {
						fail("testReadBy:fail read existing list of StudentGroups");
					}
	
					
				}
	  
	  
		/** Test of delete method, of class ServidorPersistente.OJB.StudentGroupOJB. */
				public void testDeleteStudentGroup() {
				
					try {
					//	read existing StudentGroup
						persistentSupport.iniciarTransaccao();	
				  		IStudentGroup existingStudentGroup = persistentStudentGroup.readStudentGroupByGroupPropertiesAndGroupNumber(groupProperties2,new Integer(1));
				  		assertNotNull(existingStudentGroup);			
				  		persistentStudentGroup.delete(existingStudentGroup);
						persistentSupport.confirmarTransaccao();
				
					//	trying to read deleted StudentGroup
						persistentSupport.iniciarTransaccao();	
						IStudentGroup studentGroupDeleted = persistentStudentGroup.readStudentGroupByGroupPropertiesAndGroupNumber(groupProperties2,new Integer(1));
				  		assertNull(studentGroupDeleted);	
						persistentSupport.confirmarTransaccao();
					} catch (ExcepcaoPersistencia ex) {
						fail("testDeleteStudentGroup");
					}
				}
		/** Test of Write method, of class ServidorPersistente.OJB.StudentGroupOJB. */
				public void testLockWrite() {
					try{
						//write existing StudentGroup
						persistentSupport.iniciarTransaccao();
						IStudentGroup existingStudentGroup = persistentStudentGroup.readStudentGroupByGroupPropertiesAndGroupNumber(groupProperties2,new Integer(1));
						persistentStudentGroup.lockWrite(existingStudentGroup);
						persistentSupport.confirmarTransaccao();
					}catch(ExcepcaoPersistencia excepcaoPersistencia) 
					{
						fail("testLockWrite: write the same StudentGroup");
					
					}
				
					//	write existing StudentGroup changed
					try{
						persistentSupport.iniciarTransaccao();
						IStudentGroup existingStudentGroupToChange = persistentStudentGroup.readStudentGroupByGroupPropertiesAndGroupNumber(groupProperties2,new Integer(1));
						Integer idAntigo = existingStudentGroupToChange.getIdInternal();
						existingStudentGroupToChange.setIdInternal(new Integer(idAntigo.intValue()+1));
						persistentStudentGroup.lockWrite(existingStudentGroupToChange);
						persistentSupport.confirmarTransaccao();
				
					}catch(ExcepcaoPersistencia excepcaoPersistencia) {
							System.out.println("testLockWrite: write Existing changed");
					
					}
				
					StudentGroup newStudentGroup =  new StudentGroup(new Integer(10),groupProperties2,shift2);
				
					try {
						persistentSupport.iniciarTransaccao();
						persistentStudentGroup.lockWrite(newStudentGroup);
						persistentSupport.confirmarTransaccao();
					} catch (ExcepcaoPersistencia excepcaoPersistencia) {
						fail("testLockWrite: write an invalid StudentGroup");
					}
	//
					// read StudentGroup written
					try {
						persistentSupport.iniciarTransaccao();
						IStudentGroup newStudentGroupRead = persistentStudentGroup.readStudentGroupByGroupPropertiesAndGroupNumber(groupProperties2,new Integer(10));
						assertNotNull(newStudentGroupRead);
						persistentSupport.confirmarTransaccao();
					} catch (ExcepcaoPersistencia excepcaoPersistencia) {
						fail("testEscreverNota: unexpected exception reading");
					}
				
		
				}
		/** Test of deleteAll method, of class ServidorPersistente.OJB.StudentGroupOJB. */
				public void testDeleteAll() {
					List allStudentGroup=null;
					
					try {
						
					//	read all StudentGroup	
						persistentSupport.iniciarTransaccao();	
						allStudentGroup = persistentStudentGroup.readAll();
						assertEquals(allStudentGroup.size(),10);
						persistentSupport.confirmarTransaccao();
						
					//	deleteAll Method
						persistentSupport.iniciarTransaccao();	
						persistentStudentGroup.deleteAll();
						persistentSupport.confirmarTransaccao();
						
					
					//	read all deleted StudentGroup	
						persistentSupport.iniciarTransaccao();	
						allStudentGroup = persistentStudentGroup.readAll();
						assertEquals(allStudentGroup.size(),0);
						persistentSupport.confirmarTransaccao();
						
				
					} catch (ExcepcaoPersistencia ex) {
						fail("testDeleteAllStudentGroup");
					}
				}
	
		/** Test of readAll method, of class ServidorPersistente.OJB.StudentGroupOJB. */
				public void testReadAll() {
					List allStudentGroup=null;
					
					try {
						
						//	read all StudentGroup	
							persistentSupport.iniciarTransaccao();	
							allStudentGroup = persistentStudentGroup.readAll();
							assertEquals(allStudentGroup.size(),10);
							persistentSupport.confirmarTransaccao();
						
						
							} catch (ExcepcaoPersistencia ex) {
								fail("testReadAllStudentGroup");
							}
						}

}
