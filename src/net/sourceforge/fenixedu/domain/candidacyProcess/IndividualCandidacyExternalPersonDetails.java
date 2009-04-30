package net.sourceforge.fenixedu.domain.candidacyProcess;

import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddress;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.person.MaritalStatus;
import net.sourceforge.fenixedu.domain.student.Student;

import org.joda.time.YearMonthDay;

public class IndividualCandidacyExternalPersonDetails extends IndividualCandidacyExternalPersonDetails_Base {
    public IndividualCandidacyExternalPersonDetails(IndividualCandidacy candidacy, IndividualCandidacyProcessBean bean) {
	super();
	setCandidacy(candidacy);
	setInternalized(Boolean.FALSE);
	
	PersonBean personBean = bean.getPersonBean();
	edit(personBean);
    }

    @Override
    public boolean isInternal() {
	return false;
    }

    @Override
    public void edit(PersonBean personBean) {

	setNationality(personBean.getNationality());
	
	setDateOfBirthYearMonthDay(personBean.getDateOfBirth());
	setDocumentIdNumber(personBean.getDocumentIdNumber());
	setEmissionDateOfDocumentIdYearMonthDay(personBean.getDocumentIdEmissionDate());
	setEmissionLocationOfDocumentId(personBean.getDocumentIdEmissionLocation());
	setExpirationDateOfDocumentIdYearMonthDay(personBean.getDocumentIdExpirationDate());
	setGender(personBean.getGender());
	setIdDocumentType(personBean.getIdDocumentType());
	setMaritalStatus(personBean.getMaritalStatus());
	setName(personBean.getName());
	
	/*
	 * 10/04/2009 - We want to retrieve the fiscal code
	 */
	setFiscalCode(personBean.getFiscalCode());
	
	setAddress(personBean.getAddress());
	setArea(personBean.getArea());
	setAreaCode(personBean.getAreaCode());
	setAreaOfAreaCode(personBean.getAreaOfAreaCode());
	
	setTelephoneContact(personBean.getPhone());
	setEmail(personBean.getEmail());
    }

    @Override
    public void ensurePersonInternalization() {
	// TODO Auto-generated method stub
	// creates an internal person, this is called just before the
	// Registration is created.
	setInternalized(Boolean.TRUE);
    }

    @Override
    public Student getStudent() {
	return hasPerson() ? getPerson().getStudent() : null;
    }

    @Override
    public Country getCountry() {
	return this.getNationality();
    }

    @Override
    public void setCountry(Country country) {
	this.setNationality(country);
    }

    @Override
    public String getSocialSecurityNumber() {
	return this.getFiscalCode();
    }

    @Override
    public void setSocialSecurityNumber(String number) {
	this.setFiscalCode(number);
    }

    /**
     *  External candidacy submissions dont use PhysicalAddress
     */
    @Override
    public PhysicalAddress getDefaultPhysicalAddress() {
	return null;
    }
}
