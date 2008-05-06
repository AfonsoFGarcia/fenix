package net.sourceforge.fenixedu.dataTransferObject.person;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.contacts.EmailAddress;
import net.sourceforge.fenixedu.domain.contacts.MobilePhone;
import net.sourceforge.fenixedu.domain.contacts.Phone;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddress;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddressData;
import net.sourceforge.fenixedu.domain.contacts.WebAddress;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.person.MaritalStatus;

import org.joda.time.YearMonthDay;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * @author - �ngela Almeida (argelina@ist.utl.pt)
 * 
 */
public class PersonBean implements Serializable {

    private DomainReference<Person> person;

    // personal information
    private String name; // read only

    private String username; // read only

    private Gender gender; // read only

    private String documentIdNumber; // read only

    private IDDocumentType idDocumentType;

    private String documentIdEmissionLocation;

    private YearMonthDay documentIdEmissionDate;

    private YearMonthDay documentIdExpirationDate;

    private String socialSecurityNumber;

    private String profession;

    private MaritalStatus maritalStatus;

    // contacts
    private String phone;

    private String mobile;

    private String email;

    private String webAddress;

    private boolean isEmailAvailable;

    private boolean isHomepageAvailable;

    private boolean isPhotoAvailable;

    // birth information
    private YearMonthDay dateOfBirth;

    private DomainReference<Country> nationality;

    private String parishOfBirth;

    private String districtSubdivisionOfBirth;

    private String districtOfBirth;

    private String fatherName;

    private String motherName;

    private DomainReference<Country> countryOfBirth;

    // residence
    private String address;

    private String areaCode; // zip code

    private String areaOfAreaCode; // location of zip code

    private String area; // location

    private String parishOfResidence;

    private String districtSubdivisionOfResidence;

    private String districtOfResidence;

    private DomainReference<Country> countryOfResidence;

    public PersonBean() {
	super();
    }

    public PersonBean(String name, String identificationNumber, IDDocumentType idDocumentType, YearMonthDay dateOfBirth) {
	setName(name);
	setDocumentIdNumber(identificationNumber);
	setIdDocumentType(idDocumentType);
	setDateOfBirth(dateOfBirth);
    }

    public PersonBean(Person person) {

	setName(person.getName());
	setUsername(person.getUsername());
	setGender(person.getGender());
	setMaritalStatus(person.getMaritalStatus());
	setFatherName(person.getNameOfFather());
	setMotherName(person.getNameOfMother());
	setProfession(person.getProfession());
	setNationality(person.getCountry());

	setCountryOfBirth(person.getCountryOfBirth());
	setDateOfBirth(person.getDateOfBirthYearMonthDay());
	setParishOfBirth(person.getParishOfBirth());
	setDistrictOfBirth(person.getDistrictOfBirth());
	setDistrictSubdivisionOfBirth(person.getDistrictSubdivisionOfBirth());

	setDocumentIdEmissionDate(person.getEmissionDateOfDocumentIdYearMonthDay());
	setDocumentIdEmissionLocation(person.getEmissionLocationOfDocumentId());
	setDocumentIdExpirationDate(person.getExpirationDateOfDocumentIdYearMonthDay());
	setDocumentIdNumber(person.getDocumentIdNumber());
	setIdDocumentType(person.getIdDocumentType());
	setSocialSecurityNumber(person.getSocialSecurityNumber());

	if (person.hasDefaultPhysicalAddress()) {
	    final PhysicalAddress physicalAddress = person.getDefaultPhysicalAddress();
	    setAddress(physicalAddress.getAddress());
	    setArea(physicalAddress.getArea());
	    setAreaCode(physicalAddress.getAreaCode());
	    setAreaOfAreaCode(physicalAddress.getAreaOfAreaCode());
	    setParishOfResidence(physicalAddress.getParishOfResidence());
	    setDistrictSubdivisionOfResidence(physicalAddress.getDistrictSubdivisionOfResidence());
	    setDistrictOfResidence(physicalAddress.getDistrictOfResidence());
	    setCountryOfResidence(physicalAddress.getCountryOfResidence());
	}

	setPhone(person.hasDefaultPhone() ? person.getDefaultPhone().getNumber() : null);
	setMobile(person.hasDefaultMobilePhone() ? person.getDefaultMobilePhone().getNumber() : null);
	setWebAddress(person.hasDefaultWebAddress() ? person.getDefaultWebAddress().getUrl() : null);

	setEmail(person.getEmail());

	setEmailAvailable(person.getAvailableEmail());
	setHomepageAvailable(person.getAvailableWebSite());
	setPhotoAvailable(person.getAvailablePhoto());

	setPerson(person);
    }

    public PersonBean(String name, String username, String documentIdNumber) {
	setDocumentIdNumber(documentIdNumber);
	setName(name);
	setUsername(username);
    }

    public String getAddress() {
	return address;
    }

    public void setAddress(String address) {
	this.address = address;
    }

    public String getArea() {
	return area;
    }

    public void setArea(String area) {
	this.area = area;
    }

    public String getAreaCode() {
	return areaCode;
    }

    public void setAreaCode(String areaCode) {
	this.areaCode = areaCode;
    }

    public String getAreaOfAreaCode() {
	return areaOfAreaCode;
    }

    public void setAreaOfAreaCode(String areaOfAreaCode) {
	this.areaOfAreaCode = areaOfAreaCode;
    }

    public Country getCountryOfBirth() {
	return countryOfBirth == null ? null : countryOfBirth.getObject();
    }

    public void setCountryOfBirth(Country countryOfBirth) {
	this.countryOfBirth = countryOfBirth == null ? null : new DomainReference<Country>(countryOfBirth);
    }

    public Country getCountryOfResidence() {
	return countryOfResidence == null ? null : countryOfResidence.getObject();
    }

    public void setCountryOfResidence(Country countryOfResidence) {
	this.countryOfResidence = countryOfResidence == null ? null : new DomainReference<Country>(countryOfResidence);
    }

    public YearMonthDay getDateOfBirth() {
	return dateOfBirth;
    }

    public void setDateOfBirth(YearMonthDay dateOfBirth) {
	this.dateOfBirth = dateOfBirth;
    }

    public String getDistrictOfBirth() {
	return districtOfBirth;
    }

    public void setDistrictOfBirth(String districtOfBirth) {
	this.districtOfBirth = districtOfBirth;
    }

    public String getDistrictOfResidence() {
	return districtOfResidence;
    }

    public void setDistrictOfResidence(String districtOfResidence) {
	this.districtOfResidence = districtOfResidence;
    }

    public String getDistrictSubdivisionOfBirth() {
	return districtSubdivisionOfBirth;
    }

    public void setDistrictSubdivisionOfBirth(String districtSubdivisionOfBirth) {
	this.districtSubdivisionOfBirth = districtSubdivisionOfBirth;
    }

    public String getDistrictSubdivisionOfResidence() {
	return districtSubdivisionOfResidence;
    }

    public void setDistrictSubdivisionOfResidence(String districtSubdivisionOfResidence) {
	this.districtSubdivisionOfResidence = districtSubdivisionOfResidence;
    }

    public YearMonthDay getDocumentIdEmissionDate() {
	return documentIdEmissionDate;
    }

    public void setDocumentIdEmissionDate(YearMonthDay documentIdEmissionDate) {
	this.documentIdEmissionDate = documentIdEmissionDate;
    }

    public String getDocumentIdEmissionLocation() {
	return documentIdEmissionLocation;
    }

    public void setDocumentIdEmissionLocation(String documentIdEmissionLocation) {
	this.documentIdEmissionLocation = documentIdEmissionLocation;
    }

    public YearMonthDay getDocumentIdExpirationDate() {
	return documentIdExpirationDate;
    }

    public void setDocumentIdExpirationDate(YearMonthDay documentIdExpirationDate) {
	this.documentIdExpirationDate = documentIdExpirationDate;
    }

    public String getDocumentIdNumber() {
	return documentIdNumber;
    }

    public void setDocumentIdNumber(String documentIdNumber) {
	this.documentIdNumber = documentIdNumber;
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public String getFatherName() {
	return fatherName;
    }

    public void setFatherName(String fatherName) {
	this.fatherName = fatherName;
    }

    public Gender getGender() {
	return gender;
    }

    public void setGender(Gender gender) {
	this.gender = gender;
    }

    public IDDocumentType getIdDocumentType() {
	return idDocumentType;
    }

    public void setIdDocumentType(IDDocumentType idDocumentType) {
	this.idDocumentType = idDocumentType;
    }

    public boolean isEmailAvailable() {
	return isEmailAvailable;
    }

    public void setEmailAvailable(boolean isEmailAvailable) {
	this.isEmailAvailable = isEmailAvailable;
    }

    public boolean isHomepageAvailable() {
	return isHomepageAvailable;
    }

    public void setHomepageAvailable(boolean isHomepageAvailable) {
	this.isHomepageAvailable = isHomepageAvailable;
    }

    public boolean isPhotoAvailable() {
	return isPhotoAvailable;
    }

    public void setPhotoAvailable(boolean isPhotoAvailable) {
	this.isPhotoAvailable = isPhotoAvailable;
    }

    public MaritalStatus getMaritalStatus() {
	return maritalStatus == null ? MaritalStatus.UNKNOWN : maritalStatus;
    }

    public void setMaritalStatus(MaritalStatus maritalStatus) {
	this.maritalStatus = maritalStatus;
    }

    public String getMobile() {
	return mobile;
    }

    public void setMobile(String mobileNumber) {
	this.mobile = mobileNumber;
    }

    public String getMotherName() {
	return motherName;
    }

    public void setMotherName(String motherName) {
	this.motherName = motherName;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public Country getNationality() {
	return nationality == null ? null : nationality.getObject();
    }

    public void setNationality(Country nationality) {
	this.nationality = nationality == null ? null : new DomainReference<Country>(nationality);
    }

    public String getParishOfBirth() {
	return parishOfBirth;
    }

    public void setParishOfBirth(String parishOfBirth) {
	this.parishOfBirth = parishOfBirth;
    }

    public String getParishOfResidence() {
	return parishOfResidence;
    }

    public void setParishOfResidence(String parishOfResidence) {
	this.parishOfResidence = parishOfResidence;
    }

    public String getPhone() {
	return phone;
    }

    public void setPhone(String phoneNumber) {
	this.phone = phoneNumber;
    }

    public String getProfession() {
	return profession;
    }

    public void setProfession(String profession) {
	this.profession = profession;
    }

    public String getSocialSecurityNumber() {
	return socialSecurityNumber;
    }

    public void setSocialSecurityNumber(String socialSecurityNumber) {
	this.socialSecurityNumber = socialSecurityNumber;
    }

    public String getUsername() {
	return username;
    }

    public void setUsername(String username) {
	this.username = username;
    }

    public String getWebAddress() {
	return webAddress;
    }

    public void setWebAddress(String webAddress) {
	this.webAddress = webAddress;
    }

    public Person getPerson() {
	return person == null ? null : person.getObject();
    }

    public void setPerson(Person person) {
	this.person = person == null ? null : new DomainReference<Person>(person);
    }

    public List<PhysicalAddress> getSortedPhysicalAdresses() {
	final List<PhysicalAddress> result = getPerson().getPhysicalAddresses();
	Collections.sort(result, PhysicalAddress.COMPARATOR_BY_ADDRESS);
	return result;
    }

    public PhysicalAddressData getPhysicalAddressData() {
	return new PhysicalAddressData(getAddress(), getAreaCode(), getAreaOfAreaCode(), getArea(), getParishOfResidence(),
		getDistrictSubdivisionOfResidence(), getDistrictOfResidence(), getCountryOfResidence());
    }

    public List<Phone> getSortedPhones() {
	final List<Phone> result = getPerson().getPhones();
	Collections.sort(result, Phone.COMPARATOR_BY_NUMBER);
	return result;
    }

    public List<MobilePhone> getSortedMobilePhones() {
	final List<MobilePhone> result = getPerson().getMobilePhones();
	Collections.sort(result, MobilePhone.COMPARATOR_BY_NUMBER);
	return result;
    }

    public List<EmailAddress> getSortedEmailAddresses() {
	final List<EmailAddress> result = getPerson().getEmailAddresses();
	Collections.sort(result, EmailAddress.COMPARATOR_BY_EMAIL);
	return result;
    }

    public List<WebAddress> getSortedWebAddresses() {
	final List<WebAddress> result = getPerson().getWebAddresses();
	Collections.sort(result, WebAddress.COMPARATOR_BY_URL);
	return result;
    }

    public boolean hasPerson() {
	return getPerson() != null;
    }
}
