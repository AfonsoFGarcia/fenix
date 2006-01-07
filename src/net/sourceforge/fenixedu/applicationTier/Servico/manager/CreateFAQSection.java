/*
 * Created on 2004/08/30
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.dataTransferObject.support.InfoFAQSection;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.support.FAQSection;
import net.sourceforge.fenixedu.domain.support.FAQSection;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Luis Cruz
 */
public class CreateFAQSection implements IService {

    public void run(InfoFAQSection infoFAQSection) throws ExcepcaoPersistencia {
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentObject dao = sp.getIPersistentObject();

        FAQSection parentFAQSection = null;
        if (infoFAQSection.getParentSection() != null
                && infoFAQSection.getParentSection().getIdInternal() != null) {
            parentFAQSection = (FAQSection) dao.readByOID(FAQSection.class, infoFAQSection
                    .getParentSection().getIdInternal());
        }

        FAQSection faqSection = DomainFactory.makeFAQSection();
        faqSection.setSectionName(infoFAQSection.getSectionName());
        faqSection.setParentSection(parentFAQSection);
    }

}