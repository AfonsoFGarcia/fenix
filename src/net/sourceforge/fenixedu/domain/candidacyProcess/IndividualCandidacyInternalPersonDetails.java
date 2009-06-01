package net.sourceforge.fenixedu.domain.candidacyProcess;

import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddress;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.person.MaritalStatus;
import net.sourceforge.fenixedu.domain.student.Student;

import org.joda.time.YearMonthDay;

public class IndividualCandidacyInternalPersonDetails extends IndividualCandidacyInternalPersonDetails_Base {
    public IndividualCandidacyInternalPersonDetails(IndividualCandidacy candidacy, Person person) {
	super();
	setCandidacy(candidacy);
	setPerson(person);
    }

    @Override
    public boolean isInternal() {
	return true;
    }

    @Override
    public void edit(PersonBean personBean) {
	getPerson().edit(personBean);
    }

    @Override
    public void ensurePersonInternalization() {
	// Nothing to do since the candidacy was started by an internal person.
    }

    @Override
    public Student getStudent() {
	return getPerson().getStudent();
    }

    @Override
    public String getName() {
	return getPerson().getName();
    }

    @Override
    public void setName(String name) {
	getPerson().setName(name);
    }

    @Override
    public String getDocumentIdNumber() {
	return getPerson().getDocumentIdNumber();
    }

    @Override
    public void setDocumentIdNumber(String documentIdNumber) {
	getPerson().setDocumentIdNumber(documentIdNumber);
    }

    @Override
    public Country getCountry() {
	return getPerson().getCountry();
    }

    @Override
    public void setCountry(Country country) {
	getPerson().setCountry(country);
    }

    @Override
    public YearMonthDay getDateOfBirthYearMonthDay() {
	return getPerson().getDateOfBirthYearMonthDay();
    }

    @Override
    public void setDateOfBirthYearMonthDay(YearMonthDay birthday) {
	getPerson().setDateOfBirthYearMonthDay(birthday);
    }

    @Override
    public PhysicalAddress getDefaultPhysicalAddress() {
	return getPerson().getDefaultPhysicalAddress();
    }

    @Override
    public YearMonthDay getEmissionDateOfDocumentIdYearMonthDay() {
	return getPerson().getEmissionDateOfDocumentIdYearMonthDay();
    }

    @Override
    public void setEmissionDateOfDocumentIdYearMonthDay(YearMonthDay date) {
	getPerson().setEmissionDateOfDocumentIdYearMonthDay(date);
    }

    @Override
    public String getEmissionLocationOfDocumentId() {
	return getPerson().getEmissionLocationOfDocumentId();
    }

    @Override
    public void setEmissionLocationOfDocumentId(String location) {
	getPerson().setEmissionLocationOfDocumentId(location);
    }

    @Override
    public YearMonthDay getExpirationDateOfDocumentIdYearMonthDay() {
	return getPerson().getExpirationDateOfDocumentIdYearMonthDay();
    }

    @Override
    public void setExpirationDateOfDocumentIdYearMonthDay(YearMonthDay date) {
	getPerson().setExpirationDateOfDocumentIdYearMonthDay(date);
    }

    @Override
    public Gender getGender() {
	return getPerson().getGender();
    }

    @Override
    public void setGender(Gender gender) {
	getPerson().setGender(gender);
    }

    @Override
    public IDDocumentType getIdDocumentType() {
	return getPerson().getIdDocumentType();
    }

    @Override
    public void setIdDocumentType(IDDocumentType type) {
	getPerson().setIdDocumentType(type);
    }

    @Override
    public MaritalStatus getMaritalStatus() {
	return getPerson().getMaritalStatus();
    }

    @Override
    public void setMaritalStatus(MaritalStatus status) {
	getPerson().setMaritalStatus(status);
    }

    @Override
    public String getSocialSecurityNumber() {
	return getPerson().getSocialSecurityNumber();
    }

    @Override
    public void setSocialSecurityNumber(String number) {
	getPerson().setSocialSecurityNumber(number);
    }

    @Override
    public String getAddress() {
	return getDefaultPhysicalAddress() != null ? getDefaultPhysicalAddress().getAddress() : null;
    }

    @Override
    public String getArea() {
	return getDefaultPhysicalAddress() != null ? getDefaultPhysicalAddress().getArea() : null;
    }

    @Override
    public String getAreaCode() {
	return getDefaultPhysicalAddress() != null ? getDefaultPhysicalAddress().getAreaCode() : null;
    }

    @Override
    public String getAreaOfAreaCode() {
	return getDefaultPhysicalAddress() != null ? getDefaultPhysicalAddress().getAreaOfAreaCode() : null;
    }

    @Override
    public Country getCountryOfResidence() {
	return getPerson().getCountryOfResidence();
    }

    @Override
    public void setCountryOfResidence(Country country) {
	this.getPerson().setCountryOfResidence(country);
    }

    @Override
    public void setAddress(String address) {
	this.getPerson().getDefaultPhysicalAddress().setAddress(address);
	
    }

    @Override
    public void setArea(String area) {
	this.getPerson().getDefaultPhysicalAddress().setArea(area);
	
    }
    
    @Override
    public void setAreaCode(String areaCode) {
	this.getPerson().getDefaultPhysicalAddress().setAreaCode(areaCode);
    }

    @Override
    public void setAreaOfAreaCode(String areaOfAreaCode) {
	this.getPerson().getDefaultPhysicalAddress().setAreaOfAreaCode(areaOfAreaCode);
    }

    @Override
    @Deprecated
    public String getFiscalCode() {
	return this.getPerson().getSocialSecurityNumber();
    }

    @Override
    @Deprecated
    public void setFiscalCode(String value) {
	this.getPerson().setSocialSecurityNumber(value);
    }

    @Override
    public String getEmail() {
	return getPerson().getDefaultEmailAddress() != null ? this.getPerson().getDefaultEmailAddress().getValue() : null;
    }

    @Override
    public String getTelephoneContact() {
	return getPerson().getDefaultPhone() != null ? this.getPerson().getDefaultPhone().getNumber() : null;
    }

    @Override
    public void setEmail(String email) {
	this.getPerson().setDefaultEmailAddressValue(email);
    }

    @Override
    public void setTelephoneContact(String telephoneContact) {
	this.getPerson().setDefaultPhoneNumber(telephoneContact);	
    }
    
    @Override
    public String getProfession() {
	return this.getPerson().getProfession();
    }
    
    @Override
    public void setProfession(String profession) {
	this.getPerson().setProfession(profession);
    }
    
}
