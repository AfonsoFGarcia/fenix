package net.sourceforge.fenixedu.presentationTier.Action.webSiteManager;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResearchUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResearchContract.ResearchContractType;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.person.PersonName;

import org.joda.time.YearMonthDay;

public class ResearchContractBean implements Serializable {

    private Boolean externalPerson;

    private PersonName personName;

    private String personNameString;

    private YearMonthDay begin;

    private YearMonthDay end;

    private ResearchUnit unit;

    private String email;

    private IDDocumentType documentType;

    private String documentIDNumber;

    private ResearchContractType contractType;

    public String getDocumentIDNumber() {
	return documentIDNumber;
    }

    public void setDocumentIDNumber(String documentIDNumber) {
	this.documentIDNumber = documentIDNumber;
    }

    public IDDocumentType getDocumentType() {
	return documentType;
    }

    public void setDocumentType(IDDocumentType documentType) {
	this.documentType = documentType;
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public ResearchContractBean() {
	setPersonName(null);
	setUnit(null);
	setExternalPerson(Boolean.FALSE);
	setPersonNameString(null);
    }

    public YearMonthDay getBegin() {
	return begin;
    }

    public void setBegin(YearMonthDay begin) {
	this.begin = begin;
    }

    public YearMonthDay getEnd() {
	return end;
    }

    public void setEnd(YearMonthDay end) {
	this.end = end;
    }

    public Person getPerson() {
	PersonName personName = getPersonName();
	return personName != null ? personName.getPerson() : null;
    }

    public PersonName getPersonName() {
	return personName;
    }

    public void setPersonName(PersonName personName) {
	this.personName = personName;
    }

    public ResearchUnit getUnit() {
	return unit;
    }

    public void setUnit(ResearchUnit unit) {
	this.unit = unit;
    }

    public String getPersonNameString() {
	return personNameString;
    }

    public void setPersonNameString(String personName) {
	this.personNameString = personName;
    }

    public Boolean getExternalPerson() {
	return externalPerson;
    }

    public void setExternalPerson(Boolean externalPerson) {
	this.externalPerson = externalPerson;
    }

    public boolean getShowMessage() {
	return getExternalPerson() && getPerson() == null && getPersonNameString() != null && getPersonNameString().length() > 0;
    }

    public ResearchContractType getContractType() {
	return contractType;
    }

    public void setContractType(ResearchContractType contractType) {
	this.contractType = contractType;
    }
}
