/*
 * Created on 11/Ago/2005 - 19:12:31
 * 
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.domain.teacher.ExternalActivity;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentExternalActivity;
import net.sourceforge.fenixedu.applicationTier.IService;

/**
 * @author Jo�o Fialho & Rita Ferreira
 *
 */
public class DeleteExternalActivity implements IService {

	public void run(Integer externalActivityId) throws ExcepcaoPersistencia {
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		IPersistentExternalActivity pea = sp.getIPersistentExternalActivity();
		
		ExternalActivity externalActivity = (ExternalActivity) pea.readByOID(ExternalActivity.class, externalActivityId);
		externalActivity.delete();
		
	}
}
