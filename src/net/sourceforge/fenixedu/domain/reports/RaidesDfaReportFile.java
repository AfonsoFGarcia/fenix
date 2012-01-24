package net.sourceforge.fenixedu.domain.reports;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationConclusionBean;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.candidacy.PersonalInformationBean;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.StudentStatute;
import net.sourceforge.fenixedu.domain.student.StudentStatuteType;
import net.sourceforge.fenixedu.domain.studentCurriculum.Credits;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;
import net.sourceforge.fenixedu.util.BundleUtil;

import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

public class RaidesDfaReportFile extends RaidesDfaReportFile_Base {

    public RaidesDfaReportFile() {
	super();
    }

    @Override
    public String getJobName() {
	return "Listagem RAIDES - DFA";
    }

    @Override
    protected String getPrefix() {
	return "dfaRAIDES";
    }

    @Override
    public DegreeType getDegreeType() {
	return DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA;
    }

    @Override
    public void renderReport(Spreadsheet spreadsheet) throws Exception {

	ExecutionYear executionYear = getExecutionYear();
	fillSpreadsheet(spreadsheet);

	System.out.println("BEGIN report for " + getDegreeType().name());
	int count = 0;

	for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansToProcess(executionYear)) {
	    final Registration registration = studentCurricularPlan.getRegistration();

	    if (registration != null && !registration.isTransition()) {

		for (final CycleType cycleType : registration.getDegreeType().getCycleTypes()) {
		    final CycleCurriculumGroup cycleCGroup = studentCurricularPlan.getRoot().getCycleCurriculumGroup(cycleType);
		    if (cycleCGroup != null && !cycleCGroup.isExternal()) {
			final RegistrationConclusionBean registrationConclusionBean = new RegistrationConclusionBean(
				registration, cycleCGroup);

			ExecutionYear conclusionYear = null;
			if (cycleCGroup.isConcluded()) {
			    conclusionYear = registrationConclusionBean.getConclusionYear();

			    if (conclusionYear != executionYear && conclusionYear != executionYear.getPreviousExecutionYear()) {
				continue;
			    }

			}

			if ((registration.isActive() || registration.isConcluded()) && conclusionYear != null) {
			    reportRaidesGraduate(spreadsheet, registration, executionYear, cycleType, true,
				    registrationConclusionBean.getConclusionDate());
			} else if (registration.isActive()) {
			    reportRaidesGraduate(spreadsheet, registration, executionYear, cycleType, false, null);
			}
		    }
		}

		count++;
		// if (count == 20) {
		// break;
		// }
	    }

	}

    }

    private Set<StudentCurricularPlan> getStudentCurricularPlansToProcess(ExecutionYear executionYear) {
	final Set<StudentCurricularPlan> result = new HashSet<StudentCurricularPlan>();

	collectStudentCurricularPlansFor(executionYear, result);

	if (executionYear.getPreviousExecutionYear() != null) {
	    collectStudentCurricularPlansFor(executionYear.getPreviousExecutionYear(), result);
	}

	return result;

    }

    private void collectStudentCurricularPlansFor(final ExecutionYear executionYear, final Set<StudentCurricularPlan> result) {
	for (final ExecutionDegree executionDegree : executionYear.getExecutionDegreesByType(this.getDegreeType())) {
	    result.addAll(executionDegree.getDegreeCurricularPlan().getStudentCurricularPlans());
	}
    }

    private void fillSpreadsheet(Spreadsheet spreadsheet) {
	spreadsheet.setHeader("ciclo");
	spreadsheet.setHeader("conclu�do (ano anterior)?");
	spreadsheet.setHeader("m�dia do ciclo");
	spreadsheet.setHeader("Data de conclus�o");
	spreadsheet.setHeader("Data de In�cio");
	spreadsheet.setHeader("n�mero aluno");
	spreadsheet.setHeader("tipo identifica��o");
	spreadsheet.setHeader("n�mero identifica��o");
	spreadsheet.setHeader("digitos controlo");
	spreadsheet.setHeader("vers�o doc identifica��o");
	spreadsheet.setHeader("nome");
	spreadsheet.setHeader("g�nero");
	spreadsheet.setHeader("data nascimento");
	spreadsheet.setHeader("pa�s nascimento");
	spreadsheet.setHeader("pa�s nacionalidade");
	spreadsheet.setHeader("tipo curso");
	spreadsheet.setHeader("nome curso");
	spreadsheet.setHeader("sigla curso");
	spreadsheet.setHeader("ramo");
	spreadsheet.setHeader("n�. anos lectivos inscri��o curso actual");
	spreadsheet.setHeader("�ltimo ano inscrito neste curso");
	spreadsheet.setHeader("estabelecimento curso anterior");
	spreadsheet.setHeader("curso anterior");
	spreadsheet.setHeader("estado civil");
	spreadsheet.setHeader("pa�s resid�ncia permanente");
	spreadsheet.setHeader("distrito resid�ncia permanente");
	spreadsheet.setHeader("concelho resid�ncia permanente");
	spreadsheet.setHeader("deslocado resid�ncia permanente");
	spreadsheet.setHeader("n�vel escolaridade pai");
	spreadsheet.setHeader("n�vel escolaridade m�e");
	spreadsheet.setHeader("condi��o perante profiss�o pai");
	spreadsheet.setHeader("condi��o perante profiss�o m�e");
	spreadsheet.setHeader("profiss�o pai");
	spreadsheet.setHeader("profiss�o m�e");
	spreadsheet.setHeader("profiss�o aluno");
	spreadsheet.setHeader("estatuto trabalhador estudante introduzido (info. RAIDES)");
	spreadsheet.setHeader("bolseiro (info. RAIDES)");
	spreadsheet.setHeader("bolseiro (info. oficial)");
	spreadsheet.setHeader("Grau Precedente");
	spreadsheet.setHeader("habilita��o anterior");
	spreadsheet.setHeader("pa�s habilita��o anterior");
	spreadsheet.setHeader("ano de conclus�o da habilita��o anterior");
	spreadsheet.setHeader("nota da habilita��o anterior");
	spreadsheet.setHeader("N� inscri��es anteriores em cursos superiores");
	spreadsheet.setHeader("Dura��o programa mobilidade");
	spreadsheet.setHeader("tipo estabelecimento ensino secund�rio");
	spreadsheet.setHeader("total ECTS inscritos no ano");
	spreadsheet.setHeader("total ECTS conclu�dos fim ano lectivo anterior (1� Semestre do ano lectivo actual)");
	spreadsheet.setHeader("total ECTS equival�ncia/substitui��o/dispensa");
	spreadsheet.setHeader("total ECTS necess�rios para a conclus�o");
	spreadsheet.setHeader("Tem situa��o de propinas no lectivo dos dados?");
    }

    private void reportRaidesGraduate(final Spreadsheet sheet, final Registration registration, ExecutionYear executionYear,
	    final CycleType cycleType, final boolean concluded, final YearMonthDay conclusionDate) {

	final Row row = sheet.addRow();
	final Person graduate = registration.getPerson();
	final PersonalInformationBean personalInformationBean = registration.getPersonalInformationBean(executionYear);

	// Ciclo
	row.setCell(cycleType.getDescription());

	// Conclu�do
	row.setCell(String.valueOf(concluded));

	// M�dia do Ciclo
	row.setCell(concluded ? registration.getLastStudentCurricularPlan().getCycle(cycleType)
		.getCurriculum(conclusionDate.toDateTimeAtMidnight()).getAverage().toPlainString() : "n/a");

	// Data de conclus�o
	row.setCell(conclusionDate != null ? conclusionDate.toString("dd-MM-yyyy") : "");

	// Data de In�cio
	row.setCell(registration.getStartDate() != null ? registration.getStartDate().toString("dd-MM-yyyy") : "");

	// N� de aluno
	row.setCell(registration.getNumber());

	// Tipo Identifica��o
	row.setCell(graduate.getIdDocumentType().getLocalizedName());

	// N� de Identifica��o
	row.setCell(graduate.getDocumentIdNumber());

	// D�gitos de Controlo
	row.setCell(graduate.getIdentificationDocumentExtraDigitValue());

	// Vers�o Doc. Identifica��o
	row.setCell(graduate.getIdentificationDocumentSeriesNumberValue());

	// Nome
	row.setCell(registration.getName());

	// Sexo
	row.setCell(graduate.getGender().toString());

	// Data de Nascimento
	row.setCell(graduate.getDateOfBirthYearMonthDay() != null ? graduate.getDateOfBirthYearMonthDay().toString("dd-MM-yyyy")
		: "n/a");

	// Pa�s de Nascimento
	row.setCell(graduate.getCountryOfBirth() != null ? graduate.getCountryOfBirth().getName() : "n/a");

	// Pa�s de Nacionalidade
	row.setCell(graduate.getCountry() != null ? graduate.getCountry().getName() : "n/a");

	// Tipo Curso
	row.setCell(registration.getDegreeType().getLocalizedName());

	// Nome Curso
	row.setCell(registration.getDegree().getNameI18N().getContent());

	// Sigla Curso
	row.setCell(registration.getDegree().getSigla());

	// Ramo (caso se aplique)
	row.setCell("n�o determin�vel");

	// N� de anos lectivos de inscri��o no Curso actual
	row.setCell(calculateNumberOfEnrolmentYears(registration));

	// �ltimo ano em que esteve inscrito
	row.setCell(registration.getLastEnrolmentExecutionYear() != null ? registration.getLastEnrolmentExecutionYear().getName()
		: "");

	// Estabelecimento do Curso Anterior (se o aluno ingressou por uma via
	// diferente CNA, e deve
	// ser IST caso o aluno tenha estado matriculado noutro curso do IST)
	row.setCell(personalInformationBean.getInstitution() != null ? personalInformationBean.getInstitution().getName() : "");

	// Curso Anterior (se o aluno ingressou por uma via diferente CNA, e
	// deve ser IST caso o aluno
	// tenha estado matriculado noutro curso do IST)
	row.setCell(personalInformationBean.getDegreeDesignation());

	// Estado Civil
	row.setCell(personalInformationBean.getMaritalStatus() != null ? personalInformationBean.getMaritalStatus().toString()
		: registration.getPerson().getMaritalStatus().toString());

	// Pa�s de Resid�ncia Permanente
	if (personalInformationBean.getCountryOfResidence() != null) {
	    row.setCell(personalInformationBean.getCountryOfResidence().getName());
	} else {
	    row.setCell(registration.getStudent().getPerson().getCountryOfResidence() != null ? registration.getStudent()
		    .getPerson().getCountryOfResidence().getName() : "");
	}

	// Distrito de Resid�ncia Permanente
	if (personalInformationBean.getDistrictSubdivisionOfResidence() != null) {
	    row.setCell(personalInformationBean.getDistrictSubdivisionOfResidence().getDistrict().getName());
	} else {
	    row.setCell(registration.getStudent().getPerson().getDistrictOfResidence());
	}

	// Concelho de Resid�ncia Permanente
	if (personalInformationBean.getDistrictSubdivisionOfResidence() != null) {
	    row.setCell(personalInformationBean.getDistrictSubdivisionOfResidence().getName());
	} else {
	    row.setCell(registration.getStudent().getPerson().getDistrictSubdivisionOfResidence());
	}

	// Deslocado da Resid�ncia Permanente
	if (personalInformationBean.getDislocatedFromPermanentResidence() != null) {
	    row.setCell(personalInformationBean.getDislocatedFromPermanentResidence().toString());
	} else {
	    row.setCell("");
	}

	// N�vel de Escolaridade do Pai
	if (personalInformationBean.getFatherSchoolLevel() != null) {
	    row.setCell(personalInformationBean.getFatherSchoolLevel().getName());
	} else {
	    row.setCell("");
	}

	// N�vel de Escolaridade da M�e
	if (personalInformationBean.getMotherSchoolLevel() != null) {
	    row.setCell(personalInformationBean.getMotherSchoolLevel().getName());
	} else {
	    row.setCell("");
	}

	// Condi��o perante a situa��o na profiss�o/Ocupa��o do Pai
	if (personalInformationBean.getFatherProfessionalCondition() != null) {
	    row.setCell(personalInformationBean.getFatherProfessionalCondition().getName());
	} else {
	    row.setCell("");
	}

	// Condi��o perante a situa��o na profiss�o/Ocupa��o da M�e
	if (personalInformationBean.getMotherProfessionalCondition() != null) {
	    row.setCell(personalInformationBean.getMotherProfessionalCondition().getName());
	} else {
	    row.setCell("");
	}

	// Profiss�o do Pai
	if (personalInformationBean.getFatherProfessionType() != null) {
	    row.setCell(personalInformationBean.getFatherProfessionType().getName());
	} else {
	    row.setCell("");
	}

	// Profiss�o da M�e
	if (personalInformationBean.getMotherProfessionType() != null) {
	    row.setCell(personalInformationBean.getMotherProfessionType().getName());
	} else {
	    row.setCell("");
	}

	// Profiss�o do Aluno
	if (personalInformationBean.getProfessionType() != null) {
	    row.setCell(personalInformationBean.getProfessionType().getName());
	} else {
	    row.setCell("");
	}

	// Estatuto de Trabalhador Estudante introduzido pelo aluno
	if (personalInformationBean.getProfessionalCondition() != null) {
	    row.setCell(personalInformationBean.getProfessionalCondition().getName());
	} else {
	    row.setCell("");
	}

	// Bolseiro (info. RAIDES)
	if (personalInformationBean.getGrantOwnerType() != null) {
	    row.setCell(personalInformationBean.getGrantOwnerType().getName());
	} else {
	    row.setCell("");
	}

	// Bolseiro (info. oficial)
	boolean sasFound = false;
	for (StudentStatute statute : registration.getStudent().getStudentStatutes()) {
	    if (statute.getStatuteType() == StudentStatuteType.SAS_GRANT_OWNER
		    && statute.isValidInExecutionPeriod(executionYear.getFirstExecutionPeriod())) {
		sasFound = true;
		break;
	    }
	}
	row.setCell(String.valueOf(sasFound));

	// Grau Precedente
	row.setCell(personalInformationBean.getPrecedentDegreeDesignation() != null ? personalInformationBean
		.getPrecedentDegreeDesignation() : "");

	// Habilita��o Anterior ao Curso Actual
	row.setCell(personalInformationBean.getSchoolLevel() != null ? personalInformationBean.getSchoolLevel().getName() : "");

	// Pa�s de Habilita��o Anterior ao Curso Actual
	row.setCell(personalInformationBean.getCountryWhereFinishedPrecedentDegree() != null ? personalInformationBean
		.getCountryWhereFinishedPrecedentDegree().getName() : "");

	// Ano de conclus�o da habilita��o anterior
	row.setCell(personalInformationBean.getConclusionYear());

	// Nota de conclus�o da habilita��o anterior
	row.setCell(personalInformationBean.getConclusionGrade() != null ? personalInformationBean.getConclusionGrade() : "");

	// N�mero de inscri��es anteriores em cursos superiores
	row.setCell(personalInformationBean.getNumberOfPreviousEnrolmentsInDegrees() != null ? personalInformationBean
		.getNumberOfPreviousEnrolmentsInDegrees().toString() : "");

	// Dura��o do programa de mobilidade
	row.setCell(personalInformationBean.getMobilityProgramDuration() != null ? BundleUtil.getEnumName(personalInformationBean
		.getMobilityProgramDuration()) : "");

	// Tipo de Estabelecimento Frequentado no Ensino Secund�rio
	if (personalInformationBean.getHighSchoolType() != null) {
	    row.setCell(personalInformationBean.getHighSchoolType().getName());
	} else {
	    row.setCell("");
	}

	// Total de ECTS inscritos no total do ano
	double totalCreditsEnrolled = 0d;
	for (Enrolment enrollment : registration.getLastStudentCurricularPlan().getEnrolmentsByExecutionYear(executionYear)) {
	    totalCreditsEnrolled += enrollment.getEctsCredits();
	}
	row.setCell(totalCreditsEnrolled);

	// Total de ECTS conclu�dos at� ao fim do ano lectivo anterior (1�
	// Semestre do ano lectivo actual) ao que se
	// referem os dados (neste caso at� ao fim de 2008) no curso actual
	double totalEctsConcludedUntilPreviousYear = 0d;
	for (final CycleCurriculumGroup cycleCurriculumGroup : registration.getLastStudentCurricularPlan()
		.getInternalCycleCurriculumGrops()) {

	    // We can use current year because only the first semester has
	    // occured
	    totalEctsConcludedUntilPreviousYear += cycleCurriculumGroup.getCreditsConcluded(executionYear);
	}
	row.setCell(totalEctsConcludedUntilPreviousYear);

	// N� ECTS equival�ncia/substitui��o/dispensa
	double totalCreditsDismissed = 0d;
	for (Credits credits : registration.getLastStudentCurricularPlan().getCredits()) {
	    if (credits.isEquivalence()) {
		totalCreditsDismissed += credits.getEnrolmentsEcts();
	    }
	}
	row.setCell(totalCreditsDismissed);

	// Total de ECTS necess�rios para a conclus�o
	if (concluded) {
	    row.setCell(0);
	} else {
	    row.setCell(registration.getLastStudentCurricularPlan().getRoot().getDefaultEcts(executionYear)
		    - totalEctsConcludedUntilPreviousYear);
	}

	// Tem situa��o de propinas no lectivo dos dados?
	row.setCell(String.valueOf(registration.getLastStudentCurricularPlan().hasAnyGratuityEventFor(executionYear)));
    }

    private int calculateNumberOfEnrolmentYears(Registration registration) {
	return registration.getEnrolmentsExecutionYears().size();
    }

}
