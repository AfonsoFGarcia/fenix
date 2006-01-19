/*
 * Created on 26/Ago/2003, 14:50:16
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Servico.Seminaries;

import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoCandidacy;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoCandidacyWithCaseStudyChoices;
import net.sourceforge.fenixedu.domain.Seminaries.Candidacy;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.Seminaries.IPersistentSeminaryCandidacy;
import net.sourceforge.fenixedu.presentationTier.Action.Seminaries.Exceptions.BDException;
import net.sourceforge.fenixedu.applicationTier.IService;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 26/Ago/2003, 14:50:16
 * 
 */
public class GetCandidacyById implements IService {

	public InfoCandidacy run(Integer id) throws BDException, ExcepcaoPersistencia {
		InfoCandidacy infoCandidacy = null;

		ISuportePersistente persistenceSupport = PersistenceSupportFactory
				.getDefaultPersistenceSupport();
		IPersistentSeminaryCandidacy persistentSeminaryCandidacy = persistenceSupport
				.getIPersistentSeminaryCandidacy();
		Candidacy candidacy = (Candidacy) persistentSeminaryCandidacy.readByOID(Candidacy.class, id);
		infoCandidacy = InfoCandidacyWithCaseStudyChoices.newInfoFromDomain(candidacy);

		return infoCandidacy;
	}
}