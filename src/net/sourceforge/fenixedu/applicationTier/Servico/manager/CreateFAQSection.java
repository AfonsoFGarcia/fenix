/*
 * Created on 2004/08/30
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.support.InfoFAQSection;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.support.FAQSection;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Luis Cruz
 */
public class CreateFAQSection extends Service {

    public void run(InfoFAQSection infoFAQSection) throws ExcepcaoPersistencia {
        FAQSection parentFAQSection = null;
        if (infoFAQSection.getParentSection() != null
                && infoFAQSection.getParentSection().getIdInternal() != null) {
            parentFAQSection = (FAQSection) persistentObject.readByOID(FAQSection.class, infoFAQSection
                    .getParentSection().getIdInternal());
        }

        FAQSection faqSection = DomainFactory.makeFAQSection();
        faqSection.setSectionName(infoFAQSection.getSectionName());
        faqSection.setParentSection(parentFAQSection);
    }

}