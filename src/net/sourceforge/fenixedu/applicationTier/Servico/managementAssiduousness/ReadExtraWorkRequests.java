/*
 * Created on 5/Fev/2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.managementAssiduousness;

import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.managementAssiduousness.InfoExtraWorkRequestsWithAll;
import net.sourceforge.fenixedu.domain.CostCenter;
import net.sourceforge.fenixedu.domain.managementAssiduousness.ExtraWorkRequests;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.persistenceTier.managementAssiduousness.IPersistentCostCenter;
import net.sourceforge.fenixedu.persistenceTier.managementAssiduousness.IPersistentExtraWorkRequests;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import net.sourceforge.fenixedu.applicationTier.IService;

/**
 * @author T�nia Pous�o
 * 
 */
public class ReadExtraWorkRequests implements IService {

	public List run(String usernameWho, Date beginDate, Date endDate, String costCenterCode,
			String costCenterCodeMoney) throws Exception {
		List infoExtraWorkRequestsListAfter = null;
		List extraWorkRequestsList = null;
		ISuportePersistente sp;

		sp = SuportePersistenteOJB.getInstance();

		// Read employee logged
		IPersistentCostCenter costCenterDAO = sp.getIPersistentCostCenter();

		CostCenter costCenter = costCenterDAO.readCostCenterByCode(costCenterCode);
		if (costCenter == null) {
			// TODO
		}

		CostCenter costCenterMoney = costCenterDAO.readCostCenterByCode(costCenterCodeMoney);
		if (costCenterMoney == null) {
			// TODO
		}
		IPersistentExtraWorkRequests extraWorkRequestsDAO = sp.getIPersistentExtraWorkRequests();
		extraWorkRequestsList = extraWorkRequestsDAO.readExtraWorkRequestBetweenDaysAndByCC(beginDate,
				endDate, costCenter.getIdInternal(), costCenterMoney.getIdInternal());
		if (extraWorkRequestsList != null && extraWorkRequestsList.size() > 0) {
			infoExtraWorkRequestsListAfter = (List) CollectionUtils.collect(extraWorkRequestsList,
					new Transformer() {
						public Object transform(Object arg0) {
							ExtraWorkRequests extraWorkRequests = (ExtraWorkRequests) arg0;
							return InfoExtraWorkRequestsWithAll.newInfoFromDomain(extraWorkRequests);
						}

					});
		}

		return infoExtraWorkRequestsListAfter;
	}
}
