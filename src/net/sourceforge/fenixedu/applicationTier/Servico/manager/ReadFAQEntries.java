/*
 * Created on 2004/08/30
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.support.InfoFAQEntry;
import net.sourceforge.fenixedu.domain.support.FAQEntry;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentFAQEntries;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import net.sourceforge.fenixedu.applicationTier.IService;

/**
 * @author Luis Cruz
 */
public class ReadFAQEntries implements IService {

    public Collection run() throws ExcepcaoPersistencia {
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentFAQEntries dao = sp.getIPersistentFAQEntries();

        List faqEntries = dao.readAll();
        return CollectionUtils.collect(faqEntries, new Transformer() {
            public Object transform(Object arg0) {
                FAQEntry faqEntry = (FAQEntry) arg0;
                return InfoFAQEntry.newInfoFromDomain(faqEntry);
            }
        });
    }

}