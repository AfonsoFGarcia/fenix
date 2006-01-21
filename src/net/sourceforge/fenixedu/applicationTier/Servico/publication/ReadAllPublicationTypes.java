package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoPublicationType;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoPublicationTypeWithAttributesAndSubtypes;
import net.sourceforge.fenixedu.domain.publication.PublicationType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublicationType;

public class ReadAllPublicationTypes extends Service {

    public List<InfoPublicationType> run(String user) throws ExcepcaoPersistencia {
        IPersistentPublicationType persistentPublicationType = persistentSupport
                .getIPersistentPublicationType();
        List<PublicationType> publicationTypeList = (List<PublicationType>) persistentPublicationType.readAll(PublicationType.class);

        List<InfoPublicationType> result = new ArrayList<InfoPublicationType>();
        for (PublicationType publicationType : publicationTypeList) {
        	result.add(InfoPublicationTypeWithAttributesAndSubtypes.newInfoFromDomain(publicationType));
		}

        return result;
    }
}