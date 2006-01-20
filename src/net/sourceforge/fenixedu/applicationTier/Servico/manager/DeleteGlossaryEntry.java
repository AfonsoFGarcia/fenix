/*
 * Created on 2004/08/30
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.support.GlossaryEntry;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Luis Cruz
 */
public class DeleteGlossaryEntry extends Service {

    public void run(Integer entryId) throws ExcepcaoPersistencia {
        persistentObject.deleteByOID(GlossaryEntry.class, entryId);
    }

}