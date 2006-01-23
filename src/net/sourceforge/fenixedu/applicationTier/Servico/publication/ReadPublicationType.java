package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoPublicationType;
import net.sourceforge.fenixedu.domain.publication.PublicationType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadPublicationType extends Service {

    public InfoPublicationType run(Integer publicationTypeId) throws ExcepcaoPersistencia  {
            PublicationType publicationType = (PublicationType) persistentObject.readByOID(
                    PublicationType.class, publicationTypeId);
            
            return InfoPublicationType.newInfoFromDomain(publicationType);

    }
    
}