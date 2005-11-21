package net.sourceforge.fenixedu.applicationTier.Servico.person.qualification;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoPersonWithInfoCountry;
import net.sourceforge.fenixedu.dataTransferObject.person.InfoQualification;
import net.sourceforge.fenixedu.dataTransferObject.person.InfoQualificationWithPersonAndCountry;
import net.sourceforge.fenixedu.dataTransferObject.person.InfoSiteQualifications;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IQualification;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadQualifications implements IService {

    public InfoSiteQualifications run(String user) throws ExcepcaoPersistencia {
        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPessoaPersistente persistentPerson = sp.getIPessoaPersistente();
        final IPerson person = persistentPerson.lerPessoaPorUsername(user);

        List<IQualification> qualifications = person.getAssociatedQualifications();

        List infoQualifications = (List) CollectionUtils.collect(qualifications, new Transformer() {
            public Object transform(Object o) {
                IQualification qualification = (IQualification) o;
                return InfoQualificationWithPersonAndCountry.newInfoFromDomain(qualification);
            }
        });
        Collections.sort(infoQualifications, new Comparator() {

            public int compare(Object o1, Object o2) {
                InfoQualification infoQualification1 = (InfoQualification) o1;
                InfoQualification infoQualification2 = (InfoQualification) o2;
                if (infoQualification1.getDate() == null && infoQualification2.getDate() == null) {
                    return 0;
                } else if (infoQualification1.getDate() == null) {
                    return -1;
                } else if (infoQualification2.getDate() == null) {
                    return 1;
                } else {
                    if (infoQualification1.getDate().before(infoQualification2.getDate())) {
                        return -1;
                    } else if (infoQualification1.getDate().after(infoQualification2.getDate())) {
                        return 1;
                    } else if (infoQualification1.getDate().equals(infoQualification2.getDate())) {
                        return 0;
                    }
                }
                return 0;
            }
        });

        InfoSiteQualifications infoSiteQualifications = new InfoSiteQualifications();
        infoSiteQualifications.setInfoQualifications(infoQualifications);

        final InfoPerson infoPerson = InfoPersonWithInfoCountry.newInfoFromDomain(person);
        infoSiteQualifications.setInfoPerson(infoPerson);

        return infoSiteQualifications;
    }

}
