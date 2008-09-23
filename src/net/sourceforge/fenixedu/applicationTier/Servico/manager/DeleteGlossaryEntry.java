/*
 * Created on 2004/08/30
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Luis Cruz
 */
public class DeleteGlossaryEntry extends FenixService {

    public void run(Integer entryId) {
	rootDomainObject.readGlossaryEntryByOID(entryId).delete();
    }

}
