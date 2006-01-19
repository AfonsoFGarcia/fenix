/*
 * Created on 18/12/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContract;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContractWithGrantOwnerAndGrantType;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantCostCenter;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantOrientationTeacher;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantOrientationTeacherWithTeacherAndGrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContractRegime;
import net.sourceforge.fenixedu.domain.grant.contract.GrantOrientationTeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantContract;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantContractRegime;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantOrientationTeacher;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author Barbosa
 * @author Pica
 * 
 */
public class ReadAllContractsByGrantOwner extends Service {

	public List run(Integer grantOwnerId) throws FenixServiceException, ExcepcaoPersistencia {
		List contracts = null;
		IPersistentGrantOrientationTeacher pgot = null;
		IPersistentGrantContractRegime persistentGrantContractRegime = null;

		ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		IPersistentGrantContract pgc = sp.getIPersistentGrantContract();
		pgot = sp.getIPersistentGrantOrientationTeacher();
		persistentGrantContractRegime = sp.getIPersistentGrantContractRegime();
		contracts = pgc.readAllContractsByGrantOwner(grantOwnerId);

		if (contracts == null) {
			return new ArrayList();
		}

		Iterator contractIter = contracts.iterator();
		List contractList = new ArrayList();

		// gather information related to each contract
		while (contractIter.hasNext()) {
			GrantContract grantContract = (GrantContract) contractIter.next();
			InfoGrantContract infoGrantContract = InfoGrantContractWithGrantOwnerAndGrantType
					.newInfoFromDomain(grantContract);

			// get the GrantOrientationTeacher for each contract
			GrantOrientationTeacher orientationTeacher = pgot
					.readActualGrantOrientationTeacherByContract(grantContract.getIdInternal(),
							new Integer(0));
			InfoGrantOrientationTeacher infoOrientationTeacher = InfoGrantOrientationTeacherWithTeacherAndGrantContract
					.newInfoFromDomain(orientationTeacher);
			infoGrantContract.setGrantOrientationTeacherInfo(infoOrientationTeacher);

			// get the GrantCostCenter for each contract

			if (grantContract.getGrantCostCenter() != null) {

				InfoGrantCostCenter infoGrantCostCenter = InfoGrantCostCenter
						.newInfoFromDomain(grantContract.getGrantCostCenter());
				infoGrantContract.setGrantCostCenterInfo(infoGrantCostCenter);
			}

			/*
			 * Verify if the contract is active or not. The contract is active
			 * if: 1- The end contract motive is not filled 2 - The actual grant
			 * contract regime is active
			 */
			if (infoGrantContract.getEndContractMotiveSet()) {
				infoGrantContract.setActive(new Boolean(false));
			} else {
				List grantContractRegimeActual = persistentGrantContractRegime
						.readGrantContractRegimeByGrantContractAndState(infoGrantContract
								.getIdInternal(), new Integer(1));
				if (grantContractRegimeActual.isEmpty()) {
					throw new FenixServiceException(
							"Grant Contract has no Grant Contract Regime (actual) Associated");
				}
				GrantContractRegime grantContractRegime = (GrantContractRegime) grantContractRegimeActual
						.get(0);
				infoGrantContract.setActive(grantContractRegime.getContractRegimeActive());
			}
			contractList.add(infoGrantContract);
		}
		Collections.reverse(contractList);
		return contractList;
	}
}