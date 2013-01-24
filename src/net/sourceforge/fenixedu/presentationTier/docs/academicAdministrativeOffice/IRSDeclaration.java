package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UniversityUnit;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.IDocumentRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.IRSDeclarationRequest;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.util.Money;
import net.sourceforge.fenixedu.util.StringUtils;

import org.joda.time.YearMonthDay;

public class IRSDeclaration extends AdministrativeOfficeDocument {

    protected IRSDeclaration(final IDocumentRequest documentRequest) {
	super(documentRequest);
    }

    @Override
    protected DocumentRequest getDocumentRequest() {
	return (DocumentRequest) super.getDocumentRequest();
    }

    @Override
    protected void fillReport() {
	addParameter("documentRequest", getDocumentRequest());

	addParameter("institutionName", RootDomainObject.getInstance().getInstitutionUnit().getName());
	addParameter("universityName", UniversityUnit.getInstitutionsUniversityUnit().getName());

	final Registration registration = getDocumentRequest().getRegistration();
	addParameter("registration", registration);

	final Person person = registration.getPerson();
	setPersonFields(registration, person);

	final Integer civilYear = ((IRSDeclarationRequest) getDocumentRequest()).getYear();
	addParameter("civilYear", civilYear.toString());

	setAmounts(person, civilYear);
	setEmployeeFields();

	addParameter("day", new YearMonthDay().toString(DD_MMMM_YYYY, getLocale()));
    }

    final private void setPersonFields(final Registration registration, final Person person) {
	final String name = person.getName().toUpperCase();
	addParameter("name", StringUtils.multipleLineRightPad(name, LINE_LENGTH, END_CHAR));

	final String registrationNumber = registration.getNumber().toString();
	addParameter("registrationNumber", StringUtils.multipleLineRightPad(registrationNumber, LINE_LENGTH
		- "aluno deste Instituto com o Número ".length(), END_CHAR));

	final StringBuilder documentIdType = new StringBuilder();
	documentIdType.append("portador" + (person.isMale() ? EMPTY_STR : "a"));
	documentIdType.append(" do ");
	documentIdType.append(person.getIdDocumentType().getLocalizedName());
	documentIdType.append(" Nº ");
	addParameter("documentIdType", documentIdType.toString());

	final String documentIdNumber = person.getDocumentIdNumber();
	addParameter("documentIdNumber",
		StringUtils.multipleLineRightPad(documentIdNumber, LINE_LENGTH - documentIdType.toString().length(), END_CHAR));
    }

    final private void setAmounts(final Person person, final Integer civilYear) {
	Money gratuityPayedAmount = person.getMaxDeductableAmountForLegalTaxes(EventType.GRATUITY, civilYear);
	Money othersPayedAmount = calculateOthersPayedAmount(person, civilYear);

	final StringBuilder eventTypes = new StringBuilder();
	final StringBuilder payedAmounts = new StringBuilder();
	if (!gratuityPayedAmount.isZero()) {
	    eventTypes.append("- ").append(getEnumerationBundle().getString(EventType.GRATUITY.getQualifiedName()))
		    .append(LINE_BREAK);
	    payedAmounts.append("*").append(gratuityPayedAmount.toPlainString()).append("Eur").append(LINE_BREAK);
	}
	if (!othersPayedAmount.isZero()) {
	    eventTypes.append("- Outras despesas de educação").append(LINE_BREAK);
	    payedAmounts.append("*").append(othersPayedAmount.toPlainString()).append("Eur").append(LINE_BREAK);
	}
	addParameter("eventTypes", eventTypes.toString());
	addParameter("payedAmounts", payedAmounts.toString());

	Money totalPayedAmount = othersPayedAmount.add(gratuityPayedAmount);
	addParameter("totalPayedAmount", "*" + totalPayedAmount.toString() + "Eur");
    }

    private Money calculateOthersPayedAmount(final Person person, final Integer civilYear) {
	Money result = Money.ZERO;

	for (final EventType eventType : EventType.values()) {
	    if (eventType != EventType.GRATUITY) {
		result = result.add(person.getMaxDeductableAmountForLegalTaxes(eventType, civilYear));
	    }
	}

	return result;
    }

    final private void setEmployeeFields() {
	Unit adminOfficeUnit = getAdministrativeOffice().getUnit();
	addParameter("administrativeOfficeCoordinator", adminOfficeUnit.getActiveUnitCoordinator());
	addParameter("administrativeOfficeName", getMLSTextContent(adminOfficeUnit.getPartyName()));

	addParameter("employeeLocation", adminOfficeUnit.getCampus().getLocation());

    }

}
