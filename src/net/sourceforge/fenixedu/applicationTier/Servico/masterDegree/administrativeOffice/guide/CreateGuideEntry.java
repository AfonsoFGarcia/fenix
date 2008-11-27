package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide;

import pt.ist.fenixWebFramework.services.Service;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.DocumentType;
import net.sourceforge.fenixedu.domain.GraduationType;
import net.sourceforge.fenixedu.domain.Guide;
import net.sourceforge.fenixedu.domain.GuideEntry;

/**
 * 
 * @author <a href="mailto:shezad@ist.utl.pt">Shezad Anavarali </a>
 * 
 */
public class CreateGuideEntry extends FenixService {

    @Checked("RolePredicates.MANAGER_PREDICATE")
    @Service
    public static void run(Integer guideID, GraduationType graduationType, DocumentType documentType, String description, Double price,
	    Integer quantity) {

	Guide guide = rootDomainObject.readGuideByOID(guideID);

	GuideEntry guideEntry = new GuideEntry();

	guideEntry.setDescription(description);
	guideEntry.setDocumentType(documentType);
	guideEntry.setGraduationType(graduationType);
	guideEntry.setGuide(guide);
	guideEntry.setPrice(price);
	guideEntry.setQuantity(quantity);

	// update guide's total value
	guide.updateTotalValue();
    }

}