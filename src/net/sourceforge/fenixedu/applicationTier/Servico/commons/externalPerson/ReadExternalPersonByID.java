package net.sourceforge.fenixedu.applicationTier.Servico.commons.externalPerson;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExternalPerson;
import net.sourceforge.fenixedu.domain.ExternalPerson;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * 
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 * 
 */
public class ReadExternalPersonByID extends Service {

    public Object run(Integer externalPersonID) throws FenixServiceException, ExcepcaoPersistencia {
        InfoExternalPerson infoExternalPerson = null;
        ExternalPerson externalPerson = null;

        externalPerson = (ExternalPerson) persistentObject.readByOID(ExternalPerson.class, externalPersonID);

        if (externalPerson == null)
            throw new NonExistingServiceException("error.exception.commons.ExternalPersonNotFound");

        infoExternalPerson = InfoExternalPerson.newInfoFromDomain(externalPerson);

        return infoExternalPerson;
    }
}