package net.sourceforge.fenixedu.domain.reports;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CourseLoad;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeModuleScope;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.CurricularCourseScope.DegreeModuleScopeCurricularCourseScope;
import net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseInformation;
import net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseLoad;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.Context.DegreeModuleScopeContext;
import net.sourceforge.fenixedu.domain.organizationalStructure.CompetenceCourseGroupUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.DepartmentUnit;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

public class CourseLoadAndResponsiblesReportFile extends CourseLoadAndResponsiblesReportFile_Base {
    public CourseLoadAndResponsiblesReportFile() {
	super();
    }

    @Override
    public String getJobName() {
	return "Listagem de respons�veis e cargas hor�rias de unidades curriculares";
    }

    @Override
    public String getDescription() {
	return getJobName() + " no formato " + getType().toUpperCase();
    }

    @Override
    protected String getPrefix() {
	return "carga_horaria_e_respons�veis";
    }

    @Override
    public void renderReport(Spreadsheet spreadsheet) throws Exception {
	spreadsheet.setHeader("Tipo Curso");
	spreadsheet.setHeader("Nome Curso");
	spreadsheet.setHeader("Nome Disciplina");
	spreadsheet.setHeader("Semestre");
	spreadsheet.setHeader("Ano Curricular");
	spreadsheet.setHeader("Grupo");
	spreadsheet.setHeader("Responsaveis");
	spreadsheet.setHeader("Departamento");
	spreadsheet.setHeader("Area Cientifica");
	spreadsheet.setHeader("Carga Horaria Total");
	spreadsheet.setHeader("Carga Horaria de Contacto");
	spreadsheet.setHeader("Carga Trabalho Autonomo");
	spreadsheet.setHeader("Carga Horaria Teoricas");
	spreadsheet.setHeader("Carga Horaria Praticas");
	spreadsheet.setHeader("Carga Horaria Teorico-Pr�tica");
	spreadsheet.setHeader("Carga Horaria Laboratorial");
	spreadsheet.setHeader("Carga Horaria Semin�rios");
	spreadsheet.setHeader("Carga Horaria Problemas");
	spreadsheet.setHeader("Carga Horaria Trabalho de Campo");
	spreadsheet.setHeader("Carga Horaria Estagio");
	spreadsheet.setHeader("Carga Horaria Orientacao Tutorial");

	for (Degree degree : Degree.readNotEmptyDegrees()) {
	    if (degree.getDegreeType().equals(getDegreeType())) {
		for (DegreeCurricularPlan dcp : degree.getDegreeCurricularPlansSet()) {
		    for (CurricularCourse curricularCourse : dcp.getAllCurricularCourses()) {
			// this is rather stupid. the test seems redundant
			// but it needs to be here because in the old days dcps
			// could have courses from other degrees.
			if (curricularCourse.getDegreeType().equals(getDegreeType())) {
			    for (ExecutionSemester semester : getExecutionYear().getExecutionPeriods()) {
				if (curricularCourse.isActive(semester)) {
				    for (ExecutionCourse executionCourse : curricularCourse
					    .getExecutionCoursesByExecutionPeriod(semester)) {
					final Row row = spreadsheet.addRow();
					investigate(curricularCourse, semester, row, executionCourse);
				    }
				}
			    }
			}
		    }
		}
	    }
	}
    }

    private void investigate(final CurricularCourse curricularCourse, final ExecutionSemester executionPeriod, final Row row,
	    final ExecutionCourse executionCourse) {
	final Degree degree = curricularCourse.getDegree();

	if (executionCourse == null) {
	    throw new Error("No execution course found for: " + curricularCourse.getName() + " and " + executionPeriod.getName()
		    + " " + executionPeriod.getExecutionYear().getYear());
	}
	final DegreeModuleScope degreeModuleScope = findDegreeModuleScope(curricularCourse, executionPeriod);
	final String responsibleTeachers = findResponsibleTeachers(executionCourse);
	final String group = findGroup(curricularCourse, degreeModuleScope);
	final String department = findDepartment(curricularCourse);
	final String scientificArea = findScientificAres(curricularCourse);
	final CompetenceCourseLoad competenceCourseLoad = findCompetenceCourseLoad(curricularCourse, executionPeriod);

	row.setCell(0, degree.getDegreeType().getLocalizedName());
	row.setCell(1, degree.getNome());
	row.setCell(2, curricularCourse.getName());
	if (curricularCourse.isAnual()) {
	    row.setCell(3, " ");
	} else {
	    row.setCell(3, degreeModuleScope.getCurricularSemester().toString());
	}
	row.setCell(4, degreeModuleScope.getCurricularYear().toString());
	row.setCell(5, group);
	row.setCell(6, responsibleTeachers);
	row.setCell(7, department);
	row.setCell(8, scientificArea);

	row.setCell(9, findTotalCourseLoad(competenceCourseLoad, executionCourse, null).toString());
	row.setCell(10, findCourseLoadContact(competenceCourseLoad, executionCourse, null).toString());
	row.setCell(11, findAutonomousCourseLoad(competenceCourseLoad).toString());
	row.setCell(12, findCourseLoad(competenceCourseLoad, executionCourse, ShiftType.TEORICA).toString());
	row.setCell(13, findCourseLoad(competenceCourseLoad, executionCourse, ShiftType.PRATICA).toString());
	row.setCell(14, findCourseLoad(competenceCourseLoad, executionCourse, ShiftType.TEORICO_PRATICA).toString());
	row.setCell(15, findCourseLoad(competenceCourseLoad, executionCourse, ShiftType.LABORATORIAL).toString());
	row.setCell(16, findCourseLoad(competenceCourseLoad, executionCourse, ShiftType.SEMINARY).toString());
	row.setCell(17, findCourseLoad(competenceCourseLoad, executionCourse, ShiftType.PROBLEMS).toString());
	row.setCell(18, findCourseLoad(competenceCourseLoad, executionCourse, ShiftType.FIELD_WORK).toString());
	row.setCell(19, findCourseLoad(competenceCourseLoad, executionCourse, ShiftType.TRAINING_PERIOD).toString());
	row.setCell(20, findCourseLoad(competenceCourseLoad, executionCourse, ShiftType.TUTORIAL_ORIENTATION).toString());
    }

    private CompetenceCourseLoad findCompetenceCourseLoad(final CurricularCourse curricularCourse,
	    final ExecutionSemester executionPeriod) {
	final CompetenceCourse competenceCourse = curricularCourse.getCompetenceCourse();
	if (competenceCourse != null) {
	    final CompetenceCourseInformation competenceCourseInformation = competenceCourse
		    .findCompetenceCourseInformationForExecutionPeriod(executionPeriod);
	    if (competenceCourseInformation != null) {
		for (final CompetenceCourseLoad competenceCourseLoad : competenceCourseInformation.getCompetenceCourseLoadsSet()) {
		    return competenceCourseLoad;
		}
	    }
	}
	return null;
    }

    private String findScientificAres(CurricularCourse curricularCourse) {
	final CompetenceCourse competenceCourse = curricularCourse.getCompetenceCourse();
	if (competenceCourse != null) {
	    final CompetenceCourseGroupUnit competenceCourseGroupUnit = competenceCourse.getCompetenceCourseGroupUnit();
	    if (competenceCourseGroupUnit != null) {
		return competenceCourseGroupUnit.getName();
	    }
	}
	return " ";
    }

    private String findDepartment(final CurricularCourse curricularCourse) {
	final StringBuilder stringBuilder = new StringBuilder();
	final CompetenceCourse competenceCourse = curricularCourse.getCompetenceCourse();
	if (competenceCourse != null) {
	    final DepartmentUnit departmentUnit = competenceCourse.getDepartmentUnit();
	    if (departmentUnit == null) {
		for (final Department department : competenceCourse.getDepartmentsSet()) {
		    if (stringBuilder.length() > 0) {
			stringBuilder.append(", ");
		    }
		    stringBuilder.append(department.getName());
		}
	    } else {
		stringBuilder.append(departmentUnit.getName());
	    }
	}
	return stringBuilder.length() == 0 ? " " : stringBuilder.toString();
    }

    private String findGroup(final CurricularCourse curricularCourse, final DegreeModuleScope degreeModuleScope) {
	if (degreeModuleScope instanceof DegreeModuleScopeCurricularCourseScope) {
	    final DegreeModuleScopeCurricularCourseScope degreeModuleScopeCurricularCourseScope = (DegreeModuleScopeCurricularCourseScope) degreeModuleScope;
	    final Branch branch = degreeModuleScopeCurricularCourseScope.getCurricularCourseScope().getBranch();
	    return branch == null ? " " : branch.getName();
	} else {
	    final DegreeModuleScopeContext degreeModuleScopeContext = (DegreeModuleScopeContext) degreeModuleScope;
	    final Context context = degreeModuleScopeContext.getContext();
	    return context.getParentCourseGroup().getName();
	}
    }

    private String findResponsibleTeachers(final ExecutionCourse executionCourse) {
	final StringBuilder stringBuilder = new StringBuilder();
	for (final Professorship professorship : executionCourse.getProfessorshipsSet()) {
	    if (professorship.isResponsibleFor()) {
		if (stringBuilder.length() > 0) {
		    stringBuilder.append(", ");
		}
		stringBuilder.append(professorship.getTeacher().getTeacherNumber());
	    }
	}
	return stringBuilder.length() == 0 ? " " : stringBuilder.toString();
    }

    private DegreeModuleScope findDegreeModuleScope(final CurricularCourse curricularCourse,
	    final ExecutionSemester executionPeriod) {
	for (final DegreeModuleScope degreeModuleScope : curricularCourse.getDegreeModuleScopes()) {
	    if (degreeModuleScope.isActiveForExecutionPeriod(executionPeriod)) {
		return degreeModuleScope;
	    }
	}
	return null;
    }

    private BigDecimal findTotalCourseLoad(final CompetenceCourseLoad competenceCourseLoad,
	    final ExecutionCourse executionCourse, final ShiftType shiftType) {
	if (competenceCourseLoad != null) {
	    BigDecimal bigDecimal = BigDecimal.valueOf(0);
	    // bigDecimal =
	    // findCourseLoadFromExecutionCourse(competenceCourseLoad,
	    // executionCourse, ShiftType.PRATICA);
	    // bigDecimal =
	    // bigDecimal.add(findCourseLoadFromExecutionCourse(competenceCourseLoad,
	    // executionCourse, ShiftType.TEORICO_PRATICA));
	    // bigDecimal =
	    // bigDecimal.multiply(BigDecimal.valueOf(CompetenceCourseLoad.NUMBER_OF_WEEKS));
	    bigDecimal = bigDecimal.add(BigDecimal.valueOf(competenceCourseLoad.getTotalLoad().doubleValue()));
	    return bigDecimal;
	}
	return findCourseLoadFromExecutionCourse(competenceCourseLoad, executionCourse, shiftType).multiply(
		BigDecimal.valueOf(CompetenceCourseLoad.NUMBER_OF_WEEKS));
    }

    private BigDecimal findCourseLoadContact(final CompetenceCourseLoad competenceCourseLoad,
	    final ExecutionCourse executionCourse, final ShiftType shiftType) {
	final BigDecimal total = findTotalCourseLoad(competenceCourseLoad, executionCourse, shiftType);
	final BigDecimal autonomous = findAutonomousCourseLoad(competenceCourseLoad);
	return total.subtract(autonomous);
    }

    private BigDecimal findAutonomousCourseLoad(final CompetenceCourseLoad competenceCourseLoad) {
	return competenceCourseLoad == null ? BigDecimal.valueOf(0) : BigDecimal.valueOf(competenceCourseLoad
		.getAutonomousWorkHours());
    }

    private BigDecimal findCourseLoad(final CompetenceCourseLoad competenceCourseLoad, final ExecutionCourse executionCourse,
	    final ShiftType shiftType) {
	if (competenceCourseLoad != null) {
	    if (shiftType == null) {
		return findTotalCourseLoad(competenceCourseLoad, executionCourse, shiftType);
	    }
	    if (shiftType == ShiftType.TEORICA) {
		return BigDecimal.valueOf(competenceCourseLoad.getTheoreticalHours());
	    }
	    if (shiftType == ShiftType.PRATICA) {
		// Information not present in competence course.
	    }
	    if (shiftType == ShiftType.TEORICO_PRATICA) {
		// Information not present in competence course.
	    }
	    if (shiftType == ShiftType.LABORATORIAL) {
		return BigDecimal.valueOf(competenceCourseLoad.getLaboratorialHours());
	    }
	    if (shiftType == ShiftType.SEMINARY) {
		return BigDecimal.valueOf(competenceCourseLoad.getSeminaryHours());
	    }
	    if (shiftType == ShiftType.PROBLEMS) {
		return BigDecimal.valueOf(competenceCourseLoad.getProblemsHours());
	    }
	    if (shiftType == ShiftType.FIELD_WORK) {
		return BigDecimal.valueOf(competenceCourseLoad.getFieldWorkHours());
	    }
	    if (shiftType == ShiftType.TRAINING_PERIOD) {
		return BigDecimal.valueOf(competenceCourseLoad.getTrainingPeriodHours());
	    }
	    if (shiftType == ShiftType.TUTORIAL_ORIENTATION) {
		return BigDecimal.valueOf(competenceCourseLoad.getTutorialOrientationHours());
	    }
	}

	return findCourseLoadFromExecutionCourse(competenceCourseLoad, executionCourse, shiftType);
    }

    private BigDecimal findCourseLoadFromExecutionCourse(final CompetenceCourseLoad competenceCourseLoad,
	    final ExecutionCourse executionCourse, final ShiftType shiftType) {
	BigDecimal total = BigDecimal.valueOf(0);
	for (final CourseLoad courseLoad : executionCourse.getCourseLoadsSet()) {
	    if (shiftType == null || courseLoad.getType() == shiftType) {
		total = total.add(courseLoad.getWeeklyHours());
	    }
	}
	return total;
    }
}
