package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.candidacy;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.candidacy.DFACandidacy;
import net.sourceforge.fenixedu.domain.candidacy.StandByCandidacySituation;
import net.sourceforge.fenixedu.domain.candidacy.DFACandidacyEvent;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.person.RoleType;

public class CreateDFACandidacy extends Service {
    public DFACandidacy run(ExecutionDegree executionDegree, String name,
            String identificationDocumentNumber, IDDocumentType identificationDocumentType) {
        Person person = Person.readByDocumentIdNumberAndIdDocumentType(identificationDocumentNumber,
                identificationDocumentType);
		if(person == null) {
            person = new Person(name, identificationDocumentNumber, identificationDocumentType,
                    Gender.MALE, "T" + System.currentTimeMillis());
		} 
		
        person.addPersonRoleByRoleType(RoleType.CANDIDATE);
        person.addPersonRoleByRoleType(RoleType.PERSON);

		DFACandidacy candidacy = new DFACandidacy(person, executionDegree);

        new DFACandidacyEvent(person, candidacy);

		return candidacy;
	}
}
