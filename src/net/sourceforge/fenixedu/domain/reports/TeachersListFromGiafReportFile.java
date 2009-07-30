package net.sourceforge.fenixedu.domain.reports;

import java.io.File;
import java.io.IOException;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Qualification;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.EmployeeContractSituation;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.EmployeeProfessionalCategory;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.EmployeeProfessionalRegime;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.EmployeeProfessionalRelation;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalCategory;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalRegime;

import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

public class TeachersListFromGiafReportFile extends TeachersListFromGiafReportFile_Base {

    public TeachersListFromGiafReportFile() {
	super();
    }

    @Override
    public String getJobName() {
	return "Informa��o sobre docentes do IST (Do GIAF)";
    }

    @Override
    protected String getPrefix() {
	return "Informa��o sobre docentes do IST (Do GIAF)";
    }

    @Override
    public void renderReport(Spreadsheet spreadsheet) throws IOException {
	listTeachers(spreadsheet, getExecutionYear());
    }

    private void generateNameAndHeaders(Spreadsheet spreadsheet, ExecutionYear executionYear) {
	spreadsheet.setName("Docentes do IST " + executionYear.getQualifiedName().replace("/", ""));
	spreadsheet.setHeader("Nr mecanogr�fico");
	spreadsheet.setHeader("Nome");
	spreadsheet.setHeader("Data de nascimento");
	spreadsheet.setHeader("Sexo");
	spreadsheet.setHeader("Departamento ou Sec��o Aut�noma");
	spreadsheet.setHeader("�rea cient�fica ou Sec��o");
	spreadsheet.setHeader("Grau acad�mico");
	spreadsheet.setHeader("Local de obten��o do grau");
	spreadsheet.setHeader("Nome ou �rea do grau");
	spreadsheet.setHeader("E-mail");
	spreadsheet.setHeader("Categoria");
	spreadsheet.setHeader("Regime de contrata��o");
	spreadsheet.setHeader("V�nculo");
	spreadsheet.setHeader("Data in�cio contrato");
	spreadsheet.setHeader("Data conclus�o contrato");
	spreadsheet.setHeader("Data in�cio do primeiro contrato");
    }

    private void listTeachers(Spreadsheet spreadsheet, final ExecutionYear executionYear) throws IOException {
	generateNameAndHeaders(spreadsheet, executionYear);

	for (final Teacher teacher : getRootDomainObject().getTeachers()) {
	    EmployeeContractSituation situation = getLastSituationFromOneTeacherAndExecutionYear(teacher, executionYear);
	    if (situation != null) {
		final Row row = spreadsheet.addRow();
		Person person = teacher.getPerson();
		Employee employee = teacher.getEmployee();

		// Coluna "Nr mecanogr�fico"
		row.setCell(teacher.getTeacherNumber());

		// Coluna "Nome"
		row.setCell(person.getName());

		// Coluna "Data de nascimento"
		row.setCell(writeDate(YearMonthDay.fromDateFields(person.getDateOfBirth())));

		// Coluna "Sexo"
		row.setCell(person.getGender().toLocalizedString());

		// Coluna "Departamento ou Sec��o Aut�noma"
		Department department = employee.getLastDepartmentWorkingPlace(executionYear.getBeginDateYearMonthDay(),
			executionYear.getEndDateYearMonthDay());
		if (department != null) {
		    row.setCell(department.getName());
		} else {
		    row.setCell("");
		}

		// Coluna "�rea cient�fica ou Sec��o"
		Unit unit = employee.getLastWorkingPlace(executionYear.getBeginDateYearMonthDay(), executionYear
			.getEndDateYearMonthDay());
		if (unit != null) {
		    row.setCell(unit.getName());
		} else {
		    row.setCell("");
		}

		// Coluna "Grau acad�mico"
		// Coluna "Local de obten��o do grau"
		// Coluna "Nome ou �rea do grau"
		Qualification qualification = getBetterQualificationOfPersonByExecutionYear(person, executionYear);
		if (qualification != null) {
		    row.setCell(qualification.getDegree());
		    row.setCell(qualification.getSchool());
		    row.setCell(qualification.getSpecializationArea());
		} else {
		    row.setCell("");
		    row.setCell("");
		    row.setCell("");
		}

		// Coluna "E-mail"
		row.setCell(person.getEmail());

		// Coluna "Categoria"
		EmployeeProfessionalCategory employeeProfessionalCategory = getLastCategoryFromOneTeacherAndExecutionYear(
			teacher, executionYear);
		ProfessionalCategory professionalCategory = null;
		if (employeeProfessionalCategory != null) {
		    professionalCategory = employeeProfessionalCategory.getProfessionalCategory();
		} else {
		    professionalCategory = situation.getProfessionalCategory();
		}
		if (professionalCategory != null) {
		    row.setCell(professionalCategory.getName().toString());
		} else {
		    row.setCell("");
		}

		// Coluna "Regime de contrata��o"
		EmployeeProfessionalRegime employeeProfessionalRegime = getLastRegimeFromOneTeacherAndExecutionYear(teacher,
			executionYear);
		ProfessionalRegime professionalRegime = null;
		if (employeeProfessionalRegime != null) {
		    professionalRegime = employeeProfessionalRegime.getProfessionalRegime();
		} else if (employeeProfessionalCategory != null) {
		    professionalRegime = employeeProfessionalCategory.getProfessionalRegime();
		}
		if (professionalRegime != null) {
		    row.setCell(professionalRegime.getName().toString());
		} else {
		    row.setCell("");
		}

		// Coluna "V�nculo"
		EmployeeProfessionalRelation employeeProfessionalRelation = getLastRelationFromOneTeacherAndExecutionYear(
			teacher, executionYear);
		String professionalRelation = null;
		if (employeeProfessionalRelation != null) {
		    professionalRelation = employeeProfessionalRelation.getProfessionalRelation().getName().toString();
		} else {
		    professionalRelation = situation.getContractSituation().getName().toString();
		}
		if (professionalRelation != null) {
		    row.setCell(professionalRelation);
		} else {
		    row.setCell("");
		}

		// Coluna "Data in�cio contrato"
		if (situation.getBeginDate() != null) {
		    row.setCell(writeDate(situation.getBeginDate()));
		} else {
		    row.setCell("");
		}

		// Coluna "Data conclus�o contrato"
		if (situation.getEndDate() != null) {
		    row.setCell(writeDate(situation.getEndDate()));
		} else {
		    row.setCell("");
		}

		// Coluna "Data in�cio do primeiro contrato"
		situation = getFirstSituationFromOneTeacher(teacher);
		if (situation != null && situation.getBeginDate() != null) {
		    row.setCell(writeDate(situation.getBeginDate()));
		} else {
		    row.setCell("");
		}
	    }
	}

	spreadsheet.exportToXLSSheet(new File("Docentes do IST " + executionYear.getQualifiedName().replace("/", "") + ".xls"));
    }

    private EmployeeContractSituation getFirstSituationFromOneTeacher(Teacher teacher) {
	EmployeeContractSituation situation = null;
	for (EmployeeContractSituation ecs : teacher.getEmployee().getEmployeeContractSituations()) {
	    if (situation == null || situation.getBeginDate().isAfter(ecs.getBeginDate())) {
		situation = ecs;
	    }
	}
	return situation;
    }

    private EmployeeProfessionalRelation getLastRelationFromOneTeacherAndExecutionYear(Teacher teacher,
	    ExecutionYear executionYear) {
	EmployeeProfessionalRelation relation = null;
	for (EmployeeProfessionalRelation rel : teacher.getEmployee().getEmployeeProfessionalRelations()) {
	    if (isPeriodInExecutionYear(rel.getBeginDate(), rel.getEndDate(), executionYear)
		    && ((relation == null) || (relation.getBeginDate().isBefore(rel.getBeginDate())))) {
		relation = rel;
	    }
	}
	return relation;
    }

    private EmployeeProfessionalRegime getLastRegimeFromOneTeacherAndExecutionYear(Teacher teacher, ExecutionYear executionYear) {
	EmployeeProfessionalRegime regime = null;
	for (EmployeeProfessionalRegime reg : teacher.getEmployee().getEmployeeProfessionalRegimes()) {
	    if (isPeriodInExecutionYear(reg.getBeginDate(), reg.getEndDate(), executionYear)
		    && ((regime == null) || (regime.getBeginDate().isBefore(reg.getBeginDate())))) {
		regime = reg;
	    }
	}
	return regime;
    }

    private EmployeeProfessionalCategory getLastCategoryFromOneTeacherAndExecutionYear(Teacher teacher,
	    ExecutionYear executionYear) {
	EmployeeProfessionalCategory category = null;
	for (EmployeeProfessionalCategory cat : teacher.getEmployee().getEmployeeProfessionalCategories()) {
	    if (isPeriodInExecutionYear(cat.getBeginDate(), cat.getEndDate(), executionYear)
		    && ((category == null) || (category.getBeginDate().isBefore(cat.getBeginDate())))) {
		category = cat;
	    }
	}
	return category;
    }

    private EmployeeContractSituation getLastSituationFromOneTeacherAndExecutionYear(Teacher teacher, ExecutionYear executionYear) {
	EmployeeContractSituation situation = null;
	for (EmployeeContractSituation ecs : teacher.getEmployee().getEmployeeContractSituations()) {
	    if (ecs.getContractSituation().getEndSituation() == false
		    && isPeriodInExecutionYear(ecs.getBeginDate(), ecs.getEndDate(), executionYear)
		    && ((situation == null) || (situation.getBeginDate().isBefore(ecs.getBeginDate())))) {
		situation = ecs;
	    }
	}
	return situation;
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

    private boolean isPeriodInExecutionYear(LocalDate beginLocalDate, LocalDate endLocalDate, ExecutionYear executionYear) {
	if (endLocalDate == null) {
	    return !beginLocalDate.isAfter(executionYear.getEndDateYearMonthDay());
	} else {
	    return (!beginLocalDate.isBefore(executionYear.getBeginDateYearMonthDay()) && !beginLocalDate.isAfter(executionYear
		    .getEndDateYearMonthDay()))
		    || (!endLocalDate.isBefore(executionYear.getBeginDateYearMonthDay()) && !endLocalDate.isAfter(executionYear
			    .getEndDateYearMonthDay()));
	}
    }

    private String writeDate(LocalDate localDate) {
	return localDate.toString();
    }

    private String writeDate(YearMonthDay yearMonthDay) {
	return yearMonthDay.toString();
    }
}
