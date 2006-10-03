package net.sourceforge.fenixedu.applicationTier.Servico.commons.externalPerson;

import org.joda.time.YearMonthDay;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.ExternalContract;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class InsertExternalPerson extends Service {

    public ExternalContract run(String name, String sex, String address, Integer institutionID,
	    String phone, String mobile, String homepage, String email) throws FenixServiceException,
	    ExcepcaoPersistencia {

	ExternalContract storedExternalContract = ExternalContract.readByPersonNameAddressAndInstitutionID(
		name, address, institutionID);
	if (storedExternalContract != null)
	    throw new ExistingServiceException(
		    "error.exception.commons.ExternalContract.existingExternalContract");

	Unit institutionLocation = (Unit) rootDomainObject.readPartyByOID(institutionID);
	Person externalPerson = Person.createExternalPerson(name, Gender.valueOf(sex), address, null,
		null, null, null, null, null, phone, mobile, homepage, email, String.valueOf(System
			.currentTimeMillis()), IDDocumentType.EXTERNAL);

	return new ExternalContract(externalPerson, institutionLocation, new YearMonthDay(), null);
    }

    public ExternalContract run(String personName, String organizationName) {
	final Unit organization = Unit.createNewExternalInstitution(organizationName);
	Person externalPerson = Person.createExternalPerson(personName, Gender.MALE, null, null, null,
		null, null, null, null, null, null, null, null, String.valueOf(System
			.currentTimeMillis()), IDDocumentType.EXTERNAL);

	return new ExternalContract(externalPerson, organization, new YearMonthDay(), null);
    }

    public ExternalContract run(String personName, Unit organization) throws FenixServiceException {
	ExternalContract storedExternalContract = null;
	storedExternalContract = ExternalContract.readByPersonNameAddressAndInstitutionID(personName, null,
		organization.getIdInternal());
	if (storedExternalContract != null)
	    throw new ExistingServiceException(
		    "error.exception.commons.ExternalContract.existingExternalContract");

	Person externalPerson = Person.createExternalPerson(personName, Gender.MALE, null, null, null,
		null, null, null, null, null, null, null, null, String.valueOf(System
			.currentTimeMillis()), IDDocumentType.EXTERNAL);
	return new ExternalContract(externalPerson, organization, new YearMonthDay(), null);
    }
}
