package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.BibliographicReference;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Fernanda Quit�rio
 * 
 */
public class DeleteBibliographicReference extends Service {

    public boolean run(Integer bibliographicReferenceOID) throws FenixServiceException,
            ExcepcaoPersistencia {

        BibliographicReference bibliographicReference = rootDomainObject.readBibliographicReferenceByOID(bibliographicReferenceOID);
        if (bibliographicReference == null) {
            throw new InvalidArgumentsServiceException();
        }
        
        bibliographicReference.delete();
        return true;
    }
    
}
