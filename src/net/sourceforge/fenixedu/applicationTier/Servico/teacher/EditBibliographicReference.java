package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.BibliographicReference;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentBibliographicReference;

/**
 * @author Fernanda Quit�rio
 * 
 */
public class EditBibliographicReference extends Service {

    public boolean run(Integer bibliographicReferenceID, String newTitle, String newAuthors,
            String newReference, String newYear, Boolean optional) throws FenixServiceException,
            ExcepcaoPersistencia {
        final IPersistentBibliographicReference persistentBibliographicReference = persistentSupport
                .getIPersistentBibliographicReference();

        final BibliographicReference bibliographicReference = (BibliographicReference) persistentBibliographicReference
                .readByOID(BibliographicReference.class, bibliographicReferenceID);
        if (bibliographicReference == null) {
            throw new InvalidArgumentsServiceException();
        }
        bibliographicReference.edit(newTitle, newAuthors, newReference, newYear, optional);

        return true;
    }
}