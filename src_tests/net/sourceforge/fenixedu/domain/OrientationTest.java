/*
 * Created on 18/Set/2005 - 19:35:33
 * 
 */

package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoOrientation;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.teacher.IOrientation;
import net.sourceforge.fenixedu.util.OrientationType;

/**
 * @author Jo�o Fialho & Rita Ferreira
 *
 */
public class OrientationTest extends DomainTestBase {
	
	private ITeacher teacher;
	
	private InfoOrientation infoOrientationToCreate;
	private InfoOrientation infoOrientationEdit;
	
	protected void setUp() throws Exception {
		super.setUp();
		
		teacher = DomainFactory.makeTeacher();
		
		infoOrientationToCreate = new InfoOrientation();
		infoOrientationToCreate.setOrientationType(OrientationType.DEGREE);
		infoOrientationToCreate.setNumberOfStudents(10);
		infoOrientationToCreate.setDescription("description");
		
	}
	
	private void setUpEdit() {
		infoOrientationEdit = new InfoOrientation();
		infoOrientationEdit.setOrientationType(OrientationType.DEGREE);
		infoOrientationEdit.setNumberOfStudents(5);
		infoOrientationEdit.setDescription("edit");
	}
	
	public void testCreateOrientation() {
		try {
			DomainFactory.makeOrientation(null, infoOrientationToCreate);
			fail("Should have thrown a DomainException");
		
		} catch (DomainException de) {
			
		}
		
		IOrientation newOrientation = DomainFactory.makeOrientation(teacher, infoOrientationToCreate);
		
		assertTrue("Failed to reference teacher!", newOrientation.hasTeacher());
		assertEquals("Different teacher!", newOrientation.getTeacher(), teacher);
		verifyOrientationAttributes(newOrientation, infoOrientationToCreate);
	}
	
	public void testEditOrientation() {
		setUpEdit();
		IOrientation orientationToEdit = DomainFactory.makeOrientation(teacher, infoOrientationToCreate);
		
		orientationToEdit.edit(infoOrientationEdit);
		verifyOrientationAttributes(orientationToEdit, infoOrientationEdit);
		
	}


	private void verifyOrientationAttributes(IOrientation orientation, InfoOrientation infoOrientation) {
		assertEquals("Different orientationType!", orientation.getOrientationType(), infoOrientation.getOrientationType());
		assertEquals("Different numberOfStudents!", orientation.getNumberOfStudents(), infoOrientation.getNumberOfStudents());
		assertEquals("Different description!", orientation.getDescription(), infoOrientation.getDescription());
	}
	

	
}
