/*
 * Created on 20/Dez/2004
 */
package net.sourceforge.fenixedu.applicationTier.Servico.managementAssiduousness;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ListIterator;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCostCenter;
import net.sourceforge.fenixedu.dataTransferObject.managementAssiduousness.InfoMoneyCostCenter;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.managementAssiduousness.MoneyCostCenter;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEmployee;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.managementAssiduousness.IPersistentMoneyCostCenter;
import net.sourceforge.fenixedu.applicationTier.IService;

/**
 * @author T�nia Pous�o
 * 
 */
public class UpdateAllMoneyCostCenterByYear implements IService {

    public UpdateAllMoneyCostCenterByYear() {
    }

    public List run(String usernameWho, List infoMoneyCostCenterList)
            throws FenixServiceException {
        ISuportePersistente sp;
        List infoMoneyCostCenterUpdatedList = new ArrayList();
        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            // Read employee logged
            Employee employeeWho = null;

            IPessoaPersistente personDAO = sp.getIPessoaPersistente();
            Person personWho = personDAO.lerPessoaPorUsername(usernameWho);
            if (personWho != null) {
                IPersistentEmployee employeeDAO = sp.getIPersistentEmployee();
                employeeWho = employeeDAO.readByPerson(personWho
                        .getIdInternal().intValue());
            }

            if (infoMoneyCostCenterList != null
                    && infoMoneyCostCenterList.size() > 0) {

                IPersistentMoneyCostCenter moneyCostCenterDAO = sp
                .getIPersistentMoneyCostCenter();
                ListIterator iterator = infoMoneyCostCenterList.listIterator();
                while (iterator.hasNext()) {
                    InfoMoneyCostCenter infoMoneyCostCenter = (InfoMoneyCostCenter) iterator.next();

                    MoneyCostCenter moneyCostCenter = (MoneyCostCenter) moneyCostCenterDAO.readByOID(MoneyCostCenter.class, infoMoneyCostCenter.getIdInternal());
                    Calendar now = Calendar.getInstance();
                    moneyCostCenter.setWhen(now.getTime());
                    moneyCostCenter.setWhoEmployee(employeeWho);

                    moneyCostCenter.setInitialValue(infoMoneyCostCenter.getInitialValue());
                    moneyCostCenter.setTotalValue(infoMoneyCostCenter.getTotalValue());  
                    
                    InfoMoneyCostCenter infoMoneyCostCenterUpdated = InfoMoneyCostCenter
                            .newInfoFromDomain(moneyCostCenter);
                    if(infoMoneyCostCenterUpdated != null) {
	                    InfoCostCenter infoCostCenter = InfoCostCenter
	                            .newInfoFromDomain(moneyCostCenter.getCostCenter());
	                    infoMoneyCostCenterUpdated.setInfoCostCenter(infoCostCenter);
                    }
                    
                    infoMoneyCostCenterUpdatedList.add(infoMoneyCostCenterUpdated);
                }
            }
        } catch (Exception e) {
            throw new FenixServiceException(e);
        }
        if (infoMoneyCostCenterUpdatedList == null
                || infoMoneyCostCenterUpdatedList.size() <= 0) {
            throw new FenixServiceException();
        }

        return infoMoneyCostCenterUpdatedList;
    }
}
