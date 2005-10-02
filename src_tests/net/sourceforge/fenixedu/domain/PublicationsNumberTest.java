/*
 * Created on 18/Set/2005 - 19:35:33
 * 
 */

package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoPublicationsNumber;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.teacher.IPublicationsNumber;
import net.sourceforge.fenixedu.util.PublicationType;

/**
 * @author Jo�o Fialho & Rita Ferreira
 *
 */
public class PublicationsNumberTest extends DomainTestBase {
	
	private ITeacher teacher;
	
	private InfoPublicationsNumber infoPublicationsNumberToCreate;
	private InfoPublicationsNumber infoPublicationsNumberEdit;
	
	protected void setUp() throws Exception {
		super.setUp();
		
		teacher = DomainFactory.makeTeacher();
		
		infoPublicationsNumberToCreate = new InfoPublicationsNumber();
		infoPublicationsNumberToCreate.setNational(1);
		infoPublicationsNumberToCreate.setInternational(3);
		infoPublicationsNumberToCreate.setPublicationType(PublicationType.AUTHOR_BOOK);
		
	}
	
	private void setUpEdit() {
		infoPublicationsNumberEdit = new InfoPublicationsNumber();
		infoPublicationsNumberEdit.setNational(5);
		infoPublicationsNumberEdit.setInternational(3);
	}
	
	public void testCreatePublicationsNumber() {
		try {
			DomainFactory.makePublicationsNumber(null, infoPublicationsNumberToCreate);
			fail("Should have thrown a DomainException");
		
		} catch (DomainException de) {
			
		}
		
		IPublicationsNumber newPublicationsNumber = DomainFactory.makePublicationsNumber(teacher, infoPublicationsNumberToCreate);
		
		assertTrue("Failed to reference teacher!", newPublicationsNumber.hasTeacher());
		assertEquals("Different teacher!", newPublicationsNumber.getTeacher(), teacher);
		verifyPublicationsNumberAttributes(newPublicationsNumber, infoPublicationsNumberToCreate);
	}
	
	public void testEditPublicationsNumber() {
		setUpEdit();
		IPublicationsNumber publicationsNumberToEdit = DomainFactory.makePublicationsNumber(teacher, infoPublicationsNumberToCreate);
		
		publicationsNumberToEdit.edit(infoPublicationsNumberEdit);
		verifyPublicationsNumberAttributes(publicationsNumberToEdit, infoPublicationsNumberEdit);
		
	}


	private void verifyPublicationsNumberAttributes(IPublicationsNumber publicationsNumber, InfoPublicationsNumber infoPublicationsNumber) {
		assertEquals("Different national!", publicationsNumber.getNational(), infoPublicationsNumber.getNational());
		assertEquals("Different international!", publicationsNumber.getInternational(), infoPublicationsNumber.getInternational());
		assertEquals("Different publicationType!", publicationsNumber.getPublicationType(), infoPublicationsNumber.getPublicationType());
	}
	

	
}
