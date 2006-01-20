package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.publication.InfoAttribute;
import net.sourceforge.fenixedu.domain.publication.Attribute;
import net.sourceforge.fenixedu.domain.publication.PublicationType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublicationType;
import net.sourceforge.fenixedu.applicationTier.Service;

public class ReadNonRequiredAttributes extends Service {

    public List<InfoAttribute> run(int publicationTypeId) throws ExcepcaoPersistencia {

        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IPersistentPublicationType persistentPublicationType = persistentSupport
                .getIPersistentPublicationType();
        PublicationType publicationType = (PublicationType) persistentPublicationType.readByOID(
                PublicationType.class, new Integer(publicationTypeId));

        List<Attribute> nonRequiredAttributeList = publicationType.getNonRequiredAttributes();

        List<InfoAttribute> result = new ArrayList<InfoAttribute>();
        
        for(Attribute attribute : nonRequiredAttributeList) {
            result.add(InfoAttribute.newInfoFromDomain(attribute));
        }
        return result;
    }

}
