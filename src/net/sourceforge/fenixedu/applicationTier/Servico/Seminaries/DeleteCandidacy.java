/*
 * Created on 25/Ago/2003, 15:09:58
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Servico.Seminaries;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.Seminaries.Candidacy;
import net.sourceforge.fenixedu.domain.Seminaries.CaseStudyChoice;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.Seminaries.IPersistentSeminaryCandidacy;
import net.sourceforge.fenixedu.persistenceTier.Seminaries.IPersistentSeminaryCaseStudyChoice;
import net.sourceforge.fenixedu.presentationTier.Action.Seminaries.Exceptions.BDException;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 25/Ago/2003, 15:09:58
 * 
 */
public class DeleteCandidacy implements IService {

	public void run(Integer id) throws BDException, ExcepcaoPersistencia {

		ISuportePersistente persistenceSupport = PersistenceSupportFactory
				.getDefaultPersistenceSupport();
		IPersistentSeminaryCandidacy persistentCandidacy = persistenceSupport
				.getIPersistentSeminaryCandidacy();
		IPersistentSeminaryCaseStudyChoice persistentChoice = persistenceSupport
				.getIPersistentSeminaryCaseStudyChoice();
		Candidacy candidacy = (Candidacy) persistentCandidacy.readByOID(Candidacy.class, id);
		List choices = candidacy.getCaseStudyChoices();
		for (Iterator iterator = choices.iterator(); iterator.hasNext();) {
			CaseStudyChoice choice = (CaseStudyChoice) iterator.next();
			choice.setCaseStudy(null);
			persistentChoice.deleteByOID(CaseStudyChoice.class, choice.getIdInternal());
		}
		candidacy.setCurricularCourse(null);
		candidacy.setModality(null);
		candidacy.setSeminary(null);
		candidacy.setStudent(null);
		candidacy.setTheme(null);
		candidacy.getCaseStudyChoices().clear();
		persistentCandidacy.deleteByOID(Candidacy.class, id);
	}

}