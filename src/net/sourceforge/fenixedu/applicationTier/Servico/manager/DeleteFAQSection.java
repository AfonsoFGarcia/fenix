package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.support.FAQSection;

public class DeleteFAQSection extends FenixService {

    public void run(Integer faqSectionId) throws FenixServiceException {
	FAQSection faqSection = rootDomainObject.readFAQSectionByOID(faqSectionId);
	if (faqSection != null) {
	    faqSection.delete();
	}
    }

}
