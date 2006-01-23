package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoAttribute;
import net.sourceforge.fenixedu.domain.publication.Attribute;
import net.sourceforge.fenixedu.domain.publication.PublicationType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadNonRequiredAttributes extends Service {

    public List<InfoAttribute> run(int publicationTypeId) throws ExcepcaoPersistencia {
        PublicationType publicationType = (PublicationType) persistentObject.readByOID(
                PublicationType.class, new Integer(publicationTypeId));

        List<Attribute> nonRequiredAttributeList = publicationType.getNonRequiredAttributes();

        List<InfoAttribute> result = new ArrayList<InfoAttribute>();
        
        for(Attribute attribute : nonRequiredAttributeList) {
            result.add(InfoAttribute.newInfoFromDomain(attribute));
        }
        return result;
    }

}
