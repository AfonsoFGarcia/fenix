/*
 * Created on 12/12/2003
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.grant.owner;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.grant.owner.InfoGrantOwner;
import net.sourceforge.fenixedu.dataTransferObject.grant.owner.InfoGrantOwnerWithPerson;
import net.sourceforge.fenixedu.domain.grant.owner.GrantOwner;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantOwner;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Barbosa
 * @author Pica
 * 
 */
public class ReadGrantOwnerByPerson implements IService {

	public InfoGrantOwner run(Integer personId) throws FenixServiceException, ExcepcaoPersistencia {
		InfoGrantOwner infoGrantOwner = null;
		ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		IPersistentGrantOwner persistentGrantOwner = sp.getIPersistentGrantOwner();
		GrantOwner grantOwner = persistentGrantOwner.readGrantOwnerByPerson(personId);

		infoGrantOwner = InfoGrantOwnerWithPerson.newInfoFromDomain(grantOwner);
		return infoGrantOwner;
	}

}