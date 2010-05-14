package net.sourceforge.fenixedu.domain.candidacyProcess;

import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddress;
import net.sourceforge.fenixedu.domain.student.Student;

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
	setExpirationDateOfDocumentIdYearMonthDay(personBean.getDocumentIdExpirationDate());
	setGender(personBean.getGender());
	setIdDocumentType(personBean.getIdDocumentType());
	setName(personBean.getName());

	/*
	 * 08/05/2009 - After social security number is the correct property for
	 * VAT Number
	 */
	setSocialSecurityNumber(personBean.getSocialSecurityNumber());

	setAddress(personBean.getAddress());
	setArea(personBean.getArea());
	setAreaCode(personBean.getAreaCode());

	setTelephoneContact(personBean.getPhone());
	setEmail(personBean.getEmail());
	setCountryOfResidence(personBean.getCountryOfResidence());
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
     * External candidacy submissions dont use PhysicalAddress
     */
    @Override
    public PhysicalAddress getDefaultPhysicalAddress() {
	return null;
    }

    @Override
    public Boolean hasAnyRole() {
	return false;
    }

    @Override
    public Boolean isEmployee() {
	return false;
    }

    @Override
    public void editPublic(PersonBean personBean) {
	this.edit(personBean);
    }

    @Override
    public String getEidentifier() {
	return null;
    }
}
