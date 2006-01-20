package net.sourceforge.fenixedu.applicationTier.Servico.commons.externalPerson;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.institution.InsertInstitution;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExternalPerson;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.ExternalPerson;
import net.sourceforge.fenixedu.domain.Institution;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class InsertExternalPersons extends Service {

    public List<ExternalPerson> run(List<InfoExternalPerson> infoExternalPersons)
            throws ExcepcaoPersistencia, ExistingServiceException {

        List<ExternalPerson> externalPersons = new ArrayList<ExternalPerson>();

        List<Institution> institutions = (List<Institution>)persistentSupport.getIPersistentInstitution().readAll();

        // generate new identification number
        String lastDocumentIdNumber = persistentSupport.getIPersistentExternalPerson().readLastDocumentIdNumber();
        int nextID = Integer.parseInt(lastDocumentIdNumber);

        for (InfoExternalPerson infoExternalPerson : infoExternalPersons) {

            Institution currentInstitution = null;

            // retrieving existing work location
            for (Institution institution : institutions) {
                if (institution.getName().equals(infoExternalPerson.getInfoInstitution().getName())) {
                    currentInstitution = institution;
                    break;
                }
            }

            // creating a new one if it doesn't already exist
            if (currentInstitution == null) {
                InsertInstitution iwl = new InsertInstitution();
                currentInstitution = iwl.run(infoExternalPerson.getInfoInstitution().getName());
                institutions.add(currentInstitution);
            }

            String name = infoExternalPerson.getInfoPerson().getNome();
            String documentIDNumber = String.valueOf(++nextID);

            // creating a new ExternalPerson
            ExternalPerson externalPerson = DomainFactory.makeExternalPerson(name, Gender.MALE, "", "",
                    "", "", "", documentIDNumber, currentInstitution);

            externalPersons.add(externalPerson);
        }

        return externalPersons;
    }

}
