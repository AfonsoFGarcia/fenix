package net.sourceforge.fenixedu.applicationTier.Servico.manager.precedences;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.precedences.Restriction;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentRestriction;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

public class DeleteRestrictionFromDegreeCurricularPlan extends Service {

	public void run(Integer restrictionID) throws FenixServiceException, ExcepcaoPersistencia {
		ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
		IPersistentRestriction restrictionDAO = persistentSuport.getIPersistentRestriction();
		Restriction restriction = (Restriction) restrictionDAO.readByOID(Restriction.class,
				restrictionID);

		restriction.delete();
	}
}