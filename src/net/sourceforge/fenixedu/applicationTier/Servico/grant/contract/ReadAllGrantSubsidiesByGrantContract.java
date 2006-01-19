/*
 * Created on 04 Mar 2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantSubsidy;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantSubsidyWithContract;
import net.sourceforge.fenixedu.domain.grant.contract.GrantSubsidy;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantSubsidy;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import net.sourceforge.fenixedu.applicationTier.IService;

/**
 * @author Barbosa
 * @author Pica
 * 
 */
public class ReadAllGrantSubsidiesByGrantContract implements IService {

	public List run(Integer idContract) throws FenixServiceException, ExcepcaoPersistencia {
		List subsidies = null;
		IPersistentGrantSubsidy pgs = null;

		ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		pgs = sp.getIPersistentGrantSubsidy();
		subsidies = pgs.readAllSubsidiesByGrantContract(idContract);

		if (subsidies == null)
			return new ArrayList();

		List infoSubsidyList = (List) CollectionUtils.collect(subsidies, new Transformer() {
			public Object transform(Object input) {
				GrantSubsidy grantSubsidy = (GrantSubsidy) input;
				InfoGrantSubsidy infoGrantSubsidy = InfoGrantSubsidyWithContract
						.newInfoFromDomain(grantSubsidy);
				return infoGrantSubsidy;
			}
		});
		return infoSubsidyList;
	}
}