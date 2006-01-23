/*
 * Created on 17/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentBranch;

/**
 * @author lmac1
 */

public class DeleteBranches extends Service {

	// delete a set of branches
	public List run(List internalIds, Boolean forceDelete) throws FenixServiceException, ExcepcaoPersistencia {
		Iterator iter = internalIds.iterator();

		List undeletedCodes = new ArrayList();
		Integer internalId;
		Branch branch;

		while (iter.hasNext()) {
			internalId = (Integer) iter.next();
			branch = (Branch) persistentObject.readByOID(Branch.class, internalId);
			if (branch != null) {
				try {
					if (branch.getStudentCurricularPlans().isEmpty()) {
						branch.delete();
					} else {
						if (forceDelete.booleanValue() == true) {
							branch.delete();
						} else {
							undeletedCodes.add(branch.getCode());
						}
					}
				} catch (DomainException e) {
					undeletedCodes.add(branch.getCode());
				}
			}
		}

		return undeletedCodes;
	}
}