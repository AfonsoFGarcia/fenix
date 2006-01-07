/*
 * Created on 25/Set/2003, 16:56:39
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Servico.Seminaries;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.Seminaries.Candidacy;
import net.sourceforge.fenixedu.domain.Seminaries.Candidacy;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.Seminaries.IPersistentSeminaryCandidacy;
import net.sourceforge.fenixedu.presentationTier.Action.Seminaries.Exceptions.BDException;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 25/Set/2003, 16:56:39
 * 
 */
public class ChangeCandidacyApprovanceStatus implements IService {

	public void run(List candidaciesIDs) throws BDException, ExcepcaoPersistencia {
		ISuportePersistente persistenceSupport = PersistenceSupportFactory
				.getDefaultPersistenceSupport();
		IPersistentSeminaryCandidacy persistentCandidacy = persistenceSupport
				.getIPersistentSeminaryCandidacy();
		for (Iterator iterator = candidaciesIDs.iterator(); iterator.hasNext();) {
			Candidacy candidacy = (Candidacy) persistentCandidacy.readByOID(Candidacy.class,
					(Integer) iterator.next());
			persistentCandidacy.simpleLockWrite(candidacy);
			if (candidacy.getApproved() == null)
				candidacy.setApproved(new Boolean(false));
			candidacy.setApproved(new Boolean(!candidacy.getApproved().booleanValue()));
		}
	}
}