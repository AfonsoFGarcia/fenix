package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import net.sourceforge.fenixedu.dataTransferObject.publication.InfoPublicationType;
import net.sourceforge.fenixedu.domain.publication.PublicationType;
import net.sourceforge.fenixedu.domain.publication.PublicationType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublicationType;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadPublicationType implements IService {

    public InfoPublicationType run(Integer publicationTypeId) throws ExcepcaoPersistencia  {
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            IPersistentPublicationType persistentPublicationType = sp.getIPersistentPublicationType();

            PublicationType publicationType = (PublicationType) persistentPublicationType.readByOID(
                    PublicationType.class, publicationTypeId);
            
            return InfoPublicationType.newInfoFromDomain(publicationType);

    }
    
}