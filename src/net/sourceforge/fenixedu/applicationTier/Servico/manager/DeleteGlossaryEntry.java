/*
 * Created on 2004/08/30
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.domain.support.GlossaryEntry;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Luis Cruz
 */
public class DeleteGlossaryEntry implements IService {

    public void run(Integer entryId) throws ExcepcaoPersistencia {
        ISuportePersistente sp = SuportePersistenteOJB.getInstance();
        IPersistentObject dao = sp.getIPersistentObject();

        dao.deleteByOID(GlossaryEntry.class, entryId);
    }

}