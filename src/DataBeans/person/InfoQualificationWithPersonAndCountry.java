/*
 * Created on Jun 26, 2004
 */
package DataBeans.person;

import DataBeans.InfoCountry;
import DataBeans.InfoPersonWithInfoCountry;
import Dominio.IQualification;
import Dominio.Qualification;

/**
 * @author Pica
 * @author Barbosa
 */
public class InfoQualificationWithPersonAndCountry extends InfoQualification {

    public void copyFromDomain(IQualification qualification) {
        super.copyFromDomain(qualification);
        if (qualification != null) {
            setInfoCountry(InfoCountry.newInfoFromDomain(qualification.getCountry()));
            setInfoPerson(InfoPersonWithInfoCountry.newInfoFromDomain(qualification.getPerson()));
        }
    }

    public static InfoQualification newInfoFromDomain(IQualification qualification) {
        InfoQualificationWithPersonAndCountry infoQualification = null;
        if (qualification != null) {
            infoQualification = new InfoQualificationWithPersonAndCountry();
            infoQualification.copyFromDomain(qualification);
        }
        return infoQualification;
    }

    public void copyToDomain(InfoQualification infoQualification, IQualification qualification) {
        super.copyToDomain(infoQualification, qualification);

        qualification.setCountry(InfoCountry.newDomainFromInfo(infoQualification.getInfoCountry()));
        qualification.setPerson(InfoPersonWithInfoCountry.newDomainFromInfo(infoQualification
                .getInfoPerson()));
    }

    public static IQualification newDomainFromInfo(InfoQualification infoQualification) {
        IQualification qualification = null;
        InfoQualificationWithPersonAndCountry infoQualificationWithPersonAndCountry = null;
        if (infoQualification != null) {
            qualification = new Qualification();
            infoQualificationWithPersonAndCountry = new InfoQualificationWithPersonAndCountry();
            infoQualificationWithPersonAndCountry.copyToDomain(infoQualification, qualification);
        }
        return qualification;
    }
}