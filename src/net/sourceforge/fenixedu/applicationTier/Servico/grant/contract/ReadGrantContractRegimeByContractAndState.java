/*
 * Created on 18/12/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContractRegime;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContractRegimeWithTeacherAndContract;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContractRegime;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantContractRegime;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author Barbosa
 * @author Pica
 * 
 */
public class ReadGrantContractRegimeByContractAndState extends Service {

	public List run(Integer grantContractId, Integer state) throws FenixServiceException, ExcepcaoPersistencia {
		List contractRegimes = null;

		IPersistentGrantContractRegime persistentGrantContractRegime = persistentSupport
				.getIPersistentGrantContractRegime();
		contractRegimes = persistentGrantContractRegime.readGrantContractRegimeByGrantContractAndState(
				grantContractId, state);

		if (contractRegimes == null)
			return new ArrayList();

		List infoContractRegimeList = (ArrayList) CollectionUtils.collect(contractRegimes,
				new Transformer() {
					public Object transform(Object input) {
						GrantContractRegime grantContractRegime = (GrantContractRegime) input;
						InfoGrantContractRegime infoGrantContractRegime = InfoGrantContractRegimeWithTeacherAndContract
								.newInfoFromDomain(grantContractRegime);
						return infoGrantContractRegime;
					}
				});

		return infoContractRegimeList;

	}
}