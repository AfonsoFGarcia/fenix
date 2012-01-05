package net.sourceforge.fenixedu.domain.reports;

import java.io.File;
import java.io.IOException;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.ExternalTeacherAuthorization;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Qualification;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.GiafProfessionalData;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonContractSituation;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonProfessionalData;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalCategory;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalRegime;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalRelation;
import net.sourceforge.fenixedu.domain.teacher.CategoryType;

import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

public class TeachersListFromGiafReportFile extends TeachersListFromGiafReportFile_Base {

    public TeachersListFromGiafReportFile() {
	super();
    }

    @Override
    public String getJobName() {
	return "Informa��o sobre docentes do IST";
    }

    @Override
    protected String getPrefix() {
	return "Informa��o sobre docentes do IST";
    }

    @Override
    public void renderReport(Spreadsheet spreadsheet) throws IOException {
	listTeachers(spreadsheet, getExecutionYear());
    }

    private void generateNameAndHeaders(Spreadsheet spreadsheet, ExecutionYear executionYear) {
	spreadsheet.setName("Docentes do IST " + executionYear.getQualifiedName().replace("/", ""));
	spreadsheet.setHeader("Identifica��o");
	spreadsheet.setHeader("OID_PERSON");
	spreadsheet.setHeader("Tipo Docente");
	spreadsheet.setHeader("Nome");
	spreadsheet.setHeader("Data de nascimento");
	spreadsheet.setHeader("Sexo");
	spreadsheet.setHeader("Nacionalidade");
	spreadsheet.setHeader("Departamento ou Sec��o Aut�noma");
	spreadsheet.setHeader("�rea cient�fica ou Sec��o");
	spreadsheet.setHeader("Grau acad�mico");
	spreadsheet.setHeader("Local de obten��o do grau");
	spreadsheet.setHeader("Nome ou �rea do grau");
	spreadsheet.setHeader("E-mail");
	spreadsheet.setHeader("Categoria");
	spreadsheet.setHeader("Regime de contrata��o");
	spreadsheet.setHeader("V�nculo");
	spreadsheet.setHeader("Data in�cio contrato/Autoriza��o");
	spreadsheet.setHeader("Data conclus�o contrato/Autoriza��o");
	spreadsheet.setHeader("N� de anos na institui��o");
    }

    private void listTeachers(Spreadsheet spreadsheet, final ExecutionYear executionYear) throws IOException {
	generateNameAndHeaders(spreadsheet, executionYear);

	for (final Teacher teacher : getRootDomainObject().getTeachers()) {
	    PersonProfessionalData personProfessionalData = teacher.getPerson().getPersonProfessionalData();
	    if (personProfessionalData != null) {
		GiafProfessionalData giafProfessionalData = personProfessionalData
			.getGiafProfessionalDataByCategoryType(CategoryType.TEACHER);
		if (personProfessionalData != null && giafProfessionalData != null) {
		    PersonContractSituation personContractSituation = personProfessionalData
			    .getCurrentOrLastPersonContractSituationByCategoryType(CategoryType.TEACHER, executionYear
				    .getBeginDateYearMonthDay().toLocalDate(), executionYear.getEndDateYearMonthDay()
				    .toLocalDate());
		    if (personContractSituation != null) {

			Department department = teacher.getLastWorkingDepartment(executionYear.getBeginDateYearMonthDay(),
				executionYear.getEndDateYearMonthDay());

			ProfessionalCategory professionalCategory = personProfessionalData.getLastProfessionalCategory(
				giafProfessionalData, executionYear.getBeginDateYearMonthDay().toLocalDate(), executionYear
					.getEndDateYearMonthDay().toLocalDate());

			ProfessionalRegime professionalRegime = personProfessionalData.getLastProfessionalRegime(
				giafProfessionalData, executionYear.getBeginDateYearMonthDay().toLocalDate(), executionYear
					.getEndDateYearMonthDay().toLocalDate());

			ProfessionalRelation professionalRelation = personProfessionalData.getLastProfessionalRelation(
				giafProfessionalData, executionYear.getBeginDateYearMonthDay().toLocalDate(), executionYear
					.getEndDateYearMonthDay().toLocalDate());

			Period yearsInHouse = new Period(giafProfessionalData.getInstitutionEntryDate(),
				(personContractSituation.getEndDate() == null ? new LocalDate()
					: personContractSituation.getEndDate()));

			writePersonInformationRow(spreadsheet, executionYear, teacher, "CONTRATADO", department,
				professionalCategory, professionalRegime, professionalRelation,
				personContractSituation.getBeginDate(), personContractSituation.getEndDate(),
				yearsInHouse.getYears());

		    }
		}
	    }
	}

	for (ExecutionSemester executionSemester : executionYear.getExecutionPeriodsSet()) {
	    for (ExternalTeacherAuthorization externalTeacherAuthorization : ExternalTeacherAuthorization
		    .getExternalTeacherAuthorizationSet(executionSemester)) {
		writePersonInformationRow(spreadsheet, executionYear, externalTeacherAuthorization.getTeacher(), "AUTORIZADO",
			externalTeacherAuthorization.getDepartment(), externalTeacherAuthorization.getProfessionalCategory(),
			null, null, externalTeacherAuthorization.getExecutionSemester().getBeginDateYearMonthDay().toLocalDate(),
			externalTeacherAuthorization.getExecutionSemester().getEndDateYearMonthDay().toLocalDate(), null);

	    }
	}

	spreadsheet.exportToXLSSheet(new File("Docentes do IST " + executionYear.getQualifiedName().replace("/", "") + ".xls"));
    }

    private void writePersonInformationRow(Spreadsheet spreadsheet, ExecutionYear executionYear, Teacher teacher,
	    String teacherType, Department department, ProfessionalCategory professionalCategory,
	    ProfessionalRegime professionalRegime, ProfessionalRelation professionalRelation, LocalDate beginDate,
	    LocalDate endDate, Integer yearsInInstitution) {
	final Row row = spreadsheet.addRow();
	// Coluna "Nr mecanogr�fico"
	row.setCell(teacher.getPerson().getIstUsername());
	// Coluna "OID"
	row.setCell(teacher.getPerson().getExternalId());
	// Coluna "Tipo"
	row.setCell(teacherType);
	// Coluna "Nome"
	row.setCell(teacher.getPerson().getName());
	// Coluna "Data de nascimento"
	row.setCell(writeDate(YearMonthDay.fromDateFields(teacher.getPerson().getDateOfBirth())));
	// Coluna "Sexo"
	row.setCell(teacher.getPerson().getGender().toLocalizedString());
	// Coluna "Nacionalidade"
	row.setCell(teacher.getPerson().getCountry() != null ? teacher.getPerson().getCountry().getCountryNationality()
		.getContent() : null);

	// Coluna "Departamento ou Sec��o Aut�noma" e
	// "�rea cient�fica ou Sec��o"
	row.setCell(department != null ? department.getName() : null);
	row.setCell(department != null && department.getDepartmentUnit() != null ? department.getDepartmentUnit().getName()
		: null);

	// Coluna "Grau acad�mico"
	// Coluna "Local de obten��o do grau"
	// Coluna "Nome ou �rea do grau"
	Qualification qualification = getBetterQualificationOfPersonByExecutionYear(teacher.getPerson(), executionYear);

	row.setCell(qualification != null && qualification.getType() != null ? qualification.getType().getLocalizedName() : null);
	row.setCell(qualification != null ? qualification.getSchool() : null);
	row.setCell(qualification != null ? qualification.getDegree() : null);

	// Coluna "E-mail"
	row.setCell(teacher.getPerson().getEmailForSendingEmails());

	// Coluna "Categoria"
	row.setCell(professionalCategory != null ? professionalCategory.getName().getContent() : null);

	// Coluna "Regime de contrata��o"
	row.setCell(professionalRegime != null ? professionalRegime.getName().toString() : null);

	// Coluna "V�nculo"
	row.setCell(professionalRelation != null ? professionalRelation.getName().toString() : null);

	// Coluna "Data in�cio contrato/Autoriza��o"
	row.setCell(writeDate(beginDate));

	// Coluna "Data conclus�o contrato/Autoriza��o"
	row.setCell(writeDate(endDate));

	// Coluna "N� de anos na institui��o"
	row.setCell(yearsInInstitution);

	return;
    }

    private Qualification getBetterQualificationOfPersonByExecutionYear(Person person, ExecutionYear executionYear) {
	Qualification qualification = null;
	for (Qualification q : person.getAssociatedQualifications()) {
	    if (q.getDate() != null && q.getDate().before(executionYear.getEndDate())
		    && ((qualification == null) || (Qualification.COMPARATOR_BY_YEAR.compare(qualification, q) < 0))) {
		qualification = q;
	    }
	}
	return qualification;
    }

    private String writeDate(LocalDate localDate) {
	return localDate == null ? null : localDate.toString();
    }

    private String writeDate(YearMonthDay yearMonthDay) {
	return yearMonthDay.toString();
    }
}
