package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.IExternalPerson;

/**
 * @author Fernanda Quit�rio Created on 6/Set/2004
 *  
 */
public class InfoExternalPersonWithPersonAndWLocation extends InfoExternalPersonWithPerson {

    public void copyFromDomain(IExternalPerson externalPerson) {
        super.copyFromDomain(externalPerson);
        if (externalPerson != null) {
            setInfoInstitution(InfoInstitution.newInfoFromDomain(externalPerson.getInstitution()));
        }
    }

    public static InfoExternalPerson newInfoFromDomain(IExternalPerson externalPerson) {
        InfoExternalPersonWithPersonAndWLocation infoExternalPersonWithPersonAndWLocation = null;
        if (externalPerson != null) {
            infoExternalPersonWithPersonAndWLocation = new InfoExternalPersonWithPersonAndWLocation();
            infoExternalPersonWithPersonAndWLocation.copyFromDomain(externalPerson);
        }
        return infoExternalPersonWithPersonAndWLocation;
    }
}