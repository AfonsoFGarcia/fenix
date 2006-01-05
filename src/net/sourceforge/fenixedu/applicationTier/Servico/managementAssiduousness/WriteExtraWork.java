package net.sourceforge.fenixedu.applicationTier.Servico.managementAssiduousness;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.managementAssiduousness.InfoExtraWork;
import net.sourceforge.fenixedu.dataTransferObject.managementAssiduousness.InfoExtraWorkWithAll;
import net.sourceforge.fenixedu.domain.IEmployee;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.managementAssiduousness.ExtraWork;
import net.sourceforge.fenixedu.domain.managementAssiduousness.IExtraWork;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEmployee;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.managementAssiduousness.IPersistentExtraWork;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

public class WriteExtraWork implements IService {

    public List run(String usernameWho, List<InfoExtraWork> infoExtraWorkList, Integer employeeNumber,
            String compensation) throws Exception {

        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        final IPessoaPersistente personDAO = sp.getIPessoaPersistente();
        IPerson personWho = personDAO.lerPessoaPorUsername(usernameWho);

        // Read employee logged
        final IPersistentEmployee employeeDAO = sp.getIPersistentEmployee();
        IEmployee employeeWho = null;
        if (personWho != null) {
            employeeWho = employeeDAO.readByPerson(personWho.getIdInternal().intValue());
        }
        IEmployee employee = employeeDAO.readByNumber(employeeNumber);
        if (employee == null) {
            // TODO
        }

        final IPersistentExtraWork extraWorkDAO = sp.getIPersistentExtraWork();
        List<IExtraWork> extraWorkList = new ArrayList<IExtraWork>();
        for (InfoExtraWork infoExtraWork : infoExtraWorkList) {
            IExtraWork extraWork = null;
            if (infoExtraWork.getIdInternal() != null && infoExtraWork.getIdInternal().intValue() > 0) {
                extraWork = (IExtraWork) extraWorkDAO.readByOID(ExtraWork.class, infoExtraWork
                        .getIdInternal());
            }
            if (extraWork == null) {
                extraWork = extraWorkDAO.readExtraWorkByDay(infoExtraWork.getDay());
            }

            if (extraWork == null) {
                extraWork = new ExtraWork();
            }

            extraWork.setDay(infoExtraWork.getDay());
            extraWork.setBeginHour(infoExtraWork.getBeginHour());
            extraWork.setEndHour(infoExtraWork.getEndHour());
            extraWork.setMealSubsidy(infoExtraWork.getMealSubsidy());
            extraWork.setMealSubsidyAuthorized(infoExtraWork.getMealSubsidyAuthorized());
            extraWork.setDiurnalFirstHour(infoExtraWork.getDiurnalFirstHour());
            extraWork.setDiurnalFirstHourAuthorized(infoExtraWork.getDiurnalFirstHourAuthorized());
            extraWork.setDiurnalAfterSecondHour(infoExtraWork.getDiurnalAfterSecondHour());
            extraWork.setDiurnalAfterSecondHourAuthorized(infoExtraWork
                    .getDiurnalAfterSecondHourAuthorized());
            extraWork.setNocturnalFirstHour(infoExtraWork.getNocturnalFirstHour());
            extraWork.setNocturnalFirstHourAuthorized(infoExtraWork.getNocturnalFirstHourAuthorized());
            extraWork.setNocturnalAfterSecondHour(infoExtraWork.getNocturnalAfterSecondHour());
            extraWork.setNocturnalAfterSecondHourAuthorized(infoExtraWork
                    .getNocturnalAfterSecondHourAuthorized());
            extraWork.setRestDay(infoExtraWork.getRestDay());
            extraWork.setRestDayAuthorized(infoExtraWork.getRestDayAuthorized());

            extraWork.setEmployee(employee);

            extraWork.setWho(employeeWho.getIdInternal().intValue());
            extraWork.setWhoEmployee(employeeWho);
            extraWork.setWhen(Calendar.getInstance().getTime());
            extraWorkList.add(extraWork);
        }

        return (List) CollectionUtils.collect(extraWorkList, new Transformer() {

            public Object transform(Object arg0) {
                ExtraWork extraWork = (ExtraWork) arg0;
                return InfoExtraWorkWithAll.newInfoFromDomain(extraWork);
            }

        });
    }

}
