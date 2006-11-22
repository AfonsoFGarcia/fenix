package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import java.util.ArrayList;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.util.LanguageUtils;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

public class RegistrationDeclaration extends AdministrativeOfficeDocument {

    private DomainReference<Registration> registrationDomainReference;

    private DomainReference<Person> employeeDomainReference;

    public RegistrationDeclaration(final DocumentRequest documentRequest) {
	this(documentRequest.getRegistration(), AccessControl.getUserView().getPerson());
    }

    public RegistrationDeclaration(final Registration registration, final Person loggedPerson) {
	this.dataSource = new ArrayList();
	this.registrationDomainReference = new DomainReference<Registration>(registration);
	this.employeeDomainReference = new DomainReference<Person>(loggedPerson);
	
	fillReport();
    }

    @Override
    protected void fillReport() {
	parameters.put("RegistrationDeclaration", this);
	resourceBundle = ResourceBundle.getBundle("resources.AcademicAdminOffice", LanguageUtils
		.getLocale());
	
	dataSource.add(this);
    }

    public Person getEmployee() {
	return employeeDomainReference == null ? null : employeeDomainReference.getObject();
    }

    public Registration getRegistration() {
	return registrationDomainReference == null ? null : registrationDomainReference.getObject();
    }

    public static final ResourceBundle enumResourceBundle = ResourceBundle.getBundle(
	    "resources.EnumerationResources", LanguageUtils.getLocale());

    private static final DateTimeFormatter fmt = new DateTimeFormatterBuilder().appendMonthOfYearText()
	    .toFormatter();

    public String getDeclarationBody() {
	final Person employee = getEmployee();
	final Registration registration = getRegistration();
	final Person student = registration.getStudent().getPerson();
	final StudentCurricularPlan studentCurricularPlan = registration
		.getActiveStudentCurricularPlan();

	final StringBuilder stringBuilder = new StringBuilder();
	try {
	    // if (employee.getGender() == Gender.MALE) {
	    // stringBuilder.append(resourceBundle.getString("message.declaration.registration.person.title.male"));
	    // } else if (employee.getGender() == Gender.FEMALE) {
	    stringBuilder.append(resourceBundle
		    .getString("message.declaration.registration.person.title.female"));
	    // } else {
	    // throw new Error("unknown.gender.type.of.employee");
	    // }
	    stringBuilder.append(" ");
	    stringBuilder.append(resourceBundle
		    .getString("message.declaration.registration.person.tail"));
	    stringBuilder.append("\n");
	    if (student.getGender() == Gender.MALE) {
		stringBuilder.append(resourceBundle
			.getString("message.declaration.registration.student.number.header.male"));
	    } else if (student.getGender() == Gender.FEMALE) {
		stringBuilder.append(resourceBundle
			.getString("message.declaration.registration.student.number.header.female"));
	    } else {
		throw new Error("unknown.gender.type.of.student");
	    }
	    stringBuilder.append(" ");
	    stringBuilder.append(registration.getNumber());
	    stringBuilder.append(resourceBundle.getString("message.declaration.registration.comma"));
	    stringBuilder.append(" ");
	    stringBuilder.append(StringUtils.upperCase(student.getName()));
	    stringBuilder.append(resourceBundle.getString("message.declaration.registration.comma"));
	    stringBuilder.append(" ");
	    stringBuilder.append(resourceBundle
		    .getString("message.declaration.registration.document.id.prefix"));
	    stringBuilder.append(" ");
	    stringBuilder.append(enumResourceBundle.getString(student.getIdDocumentType().getName()));
	    stringBuilder.append(" ");
	    stringBuilder.append(student.getDocumentIdNumber());
	    stringBuilder.append(resourceBundle.getString("message.declaration.registration.comma"));
	    stringBuilder.append(" ");
	    stringBuilder.append(resourceBundle
		    .getString("message.declaration.registration.nationality.prefix"));
	    stringBuilder.append(" ");
	    stringBuilder.append(student.getNationality().getName());
	    stringBuilder.append(resourceBundle.getString("message.declaration.registration.comma"));
	    stringBuilder.append(" ");
	    stringBuilder.append(resourceBundle
		    .getString("message.declaration.registration.father.prefix"));
	    stringBuilder.append(" ");
	    stringBuilder.append(student.getNameOfFather());
	    stringBuilder.append(" ");
	    stringBuilder.append(resourceBundle
		    .getString("message.declaration.registration.mother.prefix"));
	    stringBuilder.append(" ");
	    stringBuilder.append(student.getNameOfMother());
	    stringBuilder.append(resourceBundle.getString("message.declaration.registration.comma"));
	    stringBuilder.append(" ");
	    stringBuilder.append(resourceBundle
		    .getString("message.declaration.registration.execution.year.prefix"));
	    stringBuilder.append(" ");
	    stringBuilder.append(ExecutionYear.readCurrentExecutionYear().getYear());
	    stringBuilder.append(" ");
	    stringBuilder
		    .append(resourceBundle.getString("message.declaration.registration.is.enroled"));
	    stringBuilder.append(" ");
	    // stringBuilder.append(resourceBundle.getString("message.declaration.registration.was.enroled"));
	    stringBuilder.append(resourceBundle.getString("message.declaration.registration.in"));
	    stringBuilder.append(" ");
	    stringBuilder.append(registration.getCurricularYear());
	    stringBuilder.append(resourceBundle
		    .getString("message.declaration.registration.degree.prefix"));
	    stringBuilder.append(" ");
	    stringBuilder.append(enumResourceBundle.getString(studentCurricularPlan
		    .getDegreeCurricularPlan().getDegree().getTipoCurso().getName()));
	    stringBuilder.append(" ");
	    stringBuilder.append(resourceBundle.getString("label.in"));
	    stringBuilder.append(" ");
	    stringBuilder.append(studentCurricularPlan.getDegreeCurricularPlan().getDegree().getName());
	    stringBuilder.append(" ");
	    stringBuilder.append(resourceBundle
		    .getString("message.declaration.registration.enroled.courses.prefix"));
	    stringBuilder.append(" ");
	    stringBuilder.append(studentCurricularPlan.countCurrentEnrolments());
	    stringBuilder.append(" ");
	    stringBuilder.append(resourceBundle
		    .getString("message.declaration.registration.enroled.courses.posfix"));
	    stringBuilder.append("\n \n");
	    stringBuilder.append(resourceBundle
		    .getString("message.declaration.registration.location.prefix"));
	    stringBuilder.append(" ");
	    // TODO : fix this when the new spaces structure is introduced
                // and the location of each campus is known.
	    if (studentCurricularPlan.getCurrentCampus().getName().equals("Alameda")) {
		stringBuilder.append("Lisboa");
	    } else {
		stringBuilder.append("Oeiras");
	    }
	    stringBuilder.append(resourceBundle.getString("message.declaration.registration.comma"));
	    stringBuilder.append(" ");
	    final YearMonthDay today = new YearMonthDay();
	    stringBuilder.append(today.getDayOfMonth());
	    stringBuilder.append(" ");
	    stringBuilder.append(resourceBundle.getString("message.declaration.registration.of"));
	    stringBuilder.append(" ");
	    stringBuilder.append(today.toString(fmt));
	    stringBuilder.append(" ");
	    stringBuilder.append(resourceBundle.getString("message.declaration.registration.of"));
	    stringBuilder.append(" ");
	    stringBuilder.append(today.getYear());
	    stringBuilder.append(" ");
	    stringBuilder.append("\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t");
	    // if (employee.getGender() == Gender.MALE) {
	    // stringBuilder.append(resourceBundle.getString("message.declaration.registration.person.title.male"));
	    // } else if (employee.getGender() == Gender.FEMALE) {
	    stringBuilder.append(resourceBundle
		    .getString("message.declaration.registration.person.title.female"));
	    // } else {
	    // throw new Error("unknown.gender.type.of.employee");
	    // }
	} catch (Throwable t) {
	    t.printStackTrace();
	}
	return stringBuilder.toString();
    }

}
