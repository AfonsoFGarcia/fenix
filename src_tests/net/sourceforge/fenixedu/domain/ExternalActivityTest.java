/*
 * Created on 8/Ago/2005 - 14:35:22
 * 
 */

package net.sourceforge.fenixedu.domain;

import java.util.Calendar;
import java.util.Date;

import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoExternalActivity;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.teacher.IExternalActivity;

/**
 * @author Jo�o Fialho & Rita Ferreira
 *
 */
public class ExternalActivityTest extends DomainTestBase {
	
    Date lastModificationDate = Calendar.getInstance().getTime();
    String activity = "activity";
    String activity2 = "activity2";
    
	ITeacher teacher;
	
	InfoExternalActivity infoExternalActivityToCreate;
	InfoExternalActivity infoExternalActivityToEdit;

	IExternalActivity externalActivityToDelete;
	IExternalActivity externalActivityToEdit;
	
	protected void setUp() throws Exception {
		super.setUp();
		teacher = new Teacher();
		infoExternalActivityToCreate = new InfoExternalActivity();
		infoExternalActivityToCreate.setActivity(activity);
	}

	private void initializeExternalActivity(IExternalActivity externalActivity) {
		externalActivity.setActivity(activity);
	}
	
	private void setUpDelete() {
		externalActivityToDelete = DomainFactory.makeExternalActivity(teacher, infoExternalActivityToCreate);
		initializeExternalActivity(externalActivityToDelete);
	}

	private void setUpEdit() {
		externalActivityToEdit = DomainFactory.makeExternalActivity(teacher, infoExternalActivityToCreate);
		initializeExternalActivity(externalActivityToEdit);

		infoExternalActivityToEdit = new InfoExternalActivity();
		infoExternalActivityToEdit.setActivity(activity2);
	}
	
	public void testCreateExternalActivity() {
		try {
			DomainFactory.makeExternalActivity(null, infoExternalActivityToCreate);
			fail("Should have thrown a DomainException");

		} catch(DomainException de) {
			
		}

		IExternalActivity newExternalActivity =  DomainFactory.makeExternalActivity(teacher, infoExternalActivityToCreate);
		assertTrue("Failed to reference teacher!", newExternalActivity.hasTeacher());
		verifyExternalActivityAttributes(newExternalActivity, infoExternalActivityToCreate);
	}
	
	public void testDeleteExternalActivity() {
		setUpDelete();
		externalActivityToDelete.delete();
		assertFalse("Failed to dereference Teacher", externalActivityToDelete.hasTeacher());
	}
	
	public void testEditExternalActivity() {
		setUpEdit();
		
		try {
			externalActivityToEdit.edit(null);
			fail("Should have thrown a NullPointerException!");
			
		} catch (NullPointerException npe) {
			verifyExternalActivityAttributes(externalActivityToEdit, infoExternalActivityToCreate);
		}
				
		externalActivityToEdit.edit(infoExternalActivityToEdit);

		verifyExternalActivityAttributes(externalActivityToEdit, infoExternalActivityToEdit);

	}

	private void verifyExternalActivityAttributes(IExternalActivity externalActivity, InfoExternalActivity infoExternalActivity) {
		assertEquals("Different activity!", externalActivity.getActivity(), infoExternalActivity.getActivity());
	}

}
