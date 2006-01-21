package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoPublication;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.publication.PublicationType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublicationType;

public class InsertPublication extends Service {

    public void run(InfoPublication infoPublication) throws ExcepcaoPersistencia, ExistingServiceException {
        final IPersistentPublicationType persistentPublicationType = persistentSupport.getIPersistentPublicationType();

        final List infoAuthorsList = infoPublication.getInfoPublicationAuthors();

        final List<Person> authors = new InsertInexistentAuthors().run(infoAuthorsList);

        final PublicationType publicationType = (PublicationType) persistentPublicationType.readByOID(
                PublicationType.class, infoPublication.getInfoPublicationType().getIdInternal());
        
        DomainFactory.makePublication(infoPublication,publicationType,authors);
    }
    
}