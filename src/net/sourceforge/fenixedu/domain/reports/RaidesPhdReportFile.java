package net.sourceforge.fenixedu.domain.reports;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.candidacy.PersonalInformationBean;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdProgram;
import net.sourceforge.fenixedu.domain.phd.PhdProgramProcessState;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.StudentStatute;
import net.sourceforge.fenixedu.domain.student.StudentStatuteType;
import net.sourceforge.fenixedu.domain.studentCurriculum.Credits;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;
import net.sourceforge.fenixedu.util.BundleUtil;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

public class RaidesPhdReportFile extends RaidesPhdReportFile_Base {

    public RaidesPhdReportFile() {
	super();
    }

    @Override
    public String getJobName() {
	return "Listagem RAIDES - PHD";
    }

    @Override
    protected String getPrefix() {
	return "phdRAIDES";
    }

    @Override
    public DegreeType getDegreeType() {
	return DegreeType.BOLONHA_ADVANCED_SPECIALIZATION_DIPLOMA;
    }

    @Override
    public void renderReport(Spreadsheet spreadsheet) throws Exception {

	ExecutionYear executionYear = getExecutionYear();
	int civilYear = executionYear.getBeginCivilYear();
	fillSpreadsheet(spreadsheet);

	System.out.println("BEGIN report for " + getDegreeType().name());

	List<PhdIndividualProgramProcess> retrieveProcesses = retrieveProcesses(executionYear);

	for (PhdIndividualProgramProcess phdIndividualProgramProcess : retrieveProcesses) {
	    if (phdIndividualProgramProcess.isConcluded()) {
		LocalDate conclusionDate = phdIndividualProgramProcess.getThesisProcess().getConclusionDate();

		if (conclusionDate.getYear() != civilYear && conclusionDate.getYear() != civilYear - 1) {
		    continue;
		}
	    }

	    if (phdIndividualProgramProcess.isConcluded() || phdIndividualProgramProcess.isInWorkDevelopment()) {
		reportRaidesGraduate(spreadsheet, phdIndividualProgramProcess, executionYear);
	    }
	}
    }

    private List<PhdIndividualProgramProcess> retrieveProcesses(ExecutionYear executionYear) {
	List<PhdIndividualProgramProcess> phdIndividualProgramProcessList = new ArrayList<PhdIndividualProgramProcess>();

	for (PhdProgram program : RootDomainObject.getInstance().getPhdPrograms()) {
	    phdIndividualProgramProcessList.addAll(program.getIndividualProgramProcesses());
	}

	return phdIndividualProgramProcessList;
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

    private String getReportName(final String prefix, final ExecutionYear executionYear) {

	final StringBuilder result = new StringBuilder();
	result.append(new LocalDateTime().toString("yyyyMMddHHmm"));
	result.append("_").append(prefix).append("_").append(executionYear.getName().replace('/', '_'));
	return result.toString();
    }

    private void fillSpreadsheet(Spreadsheet spreadsheet) {
	spreadsheet.setHeader("Matriculado de acordo com o plano de estudos?");
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
	spreadsheet.setHeader("sigla programa doutoral");
	spreadsheet.setHeader("programa doutoral");
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
	spreadsheet.setHeader("Data preenchimento dados RAIDES");
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
	spreadsheet.setHeader("doutoramento: inscrito parte curricular");
	spreadsheet.setHeader("n� doutoramento");
	spreadsheet.setHeader("estado processo doutoramento");
	spreadsheet.setHeader("Ambito");
	spreadsheet.setHeader("data de candidatura");
	spreadsheet.setHeader("data de homologa��o");
	spreadsheet.setHeader("data de inicio dos estudos");
	spreadsheet.setHeader("data e hora da prova");
    }

    private void reportRaidesGraduate(Spreadsheet spreadsheet, PhdIndividualProgramProcess process, ExecutionYear executionYear) {
	final Row row = spreadsheet.addRow();
	final Person graduate = process.getPerson();
	final PersonalInformationBean personalInformationBean = process.getPersonalInformationBean(executionYear);
	final Registration registration = process.getRegistration();
	final boolean concluded = process.isConcluded();
	final LocalDate conclusionDate = process.getConclusionDate();

	if (registration != null && !registration.isBolonha()) {
	    return;
	}

	YearMonthDay registrationConclusionDate = registration != null ? registration.getLastStudentCurricularPlan().getCycle(
		CycleType.THIRD_CYCLE).getConclusionDate() : null;

	if (registration != null && registrationConclusionDate == null) {
	    registrationConclusionDate = registration.getLastStudentCurricularPlan().calculateConclusionDate(
		    CycleType.THIRD_CYCLE);
	}

	row.setCell(String.valueOf(registration != null && !registration.isCanceled()));

	// Ciclo
	row.setCell(CycleType.THIRD_CYCLE.getDescription());

	// Conclu�do
	row.setCell(String.valueOf(process.isConcluded()));

	// M�dia do Ciclo
	String grade = concluded ? process.getFinalGrade().getLocalizedName() : "n/a";
	if (concluded && registration != null && registration.isConcluded()) {
	    grade += " "
		    + registration.getLastStudentCurricularPlan().getCycle(CycleType.THIRD_CYCLE).getCurriculum(
			    registrationConclusionDate.toDateTimeAtMidnight()).getAverage().toPlainString();
	}
	row.setCell(grade);

	// Data de conclus�o
	row.setCell(conclusionDate != null ? conclusionDate.toString("dd-MM-yyyy") : "");

	// Data de In�cio
	row.setCell(process.getCandidacyDate().toString("dd-MM-yyyy"));

	// N� de aluno
	row.setCell(process.getStudent().getNumber());

	// Tipo Identifica��o
	row.setCell(graduate.getIdDocumentType().getLocalizedName());

	// N� de Identifica��o
	row.setCell(graduate.getDocumentIdNumber());

	// D�gitos de Controlo
	row.setCell(graduate.getIdentificationDocumentExtraDigitValue());

	// Vers�o Doc. Identifica��o
	row.setCell(graduate.getIdentificationDocumentSeriesNumberValue());

	// Nome
	row.setCell(registration != null ? registration.getName() : "n/a");

	// Sexo
	row.setCell(graduate.getGender().toString());

	// Data de Nascimento
	row.setCell(graduate.getDateOfBirthYearMonthDay() != null ? graduate.getDateOfBirthYearMonthDay().toString("dd-MM-yyyy")
		: "n/a");

	// Pa�s de Nascimento
	row.setCell(graduate.getCountryOfBirth() != null ? graduate.getCountryOfBirth().getName() : "n/a");

	// Pa�s de Nacionalidade
	row.setCell(graduate.getCountry() != null ? graduate.getCountry().getName() : "n/a");

	// Sigla programa doutoral
	row.setCell(process.getPhdProgram().getAcronym());

	// Programa doutoral
	row.setCell(process.getPhdProgram().getName().getContent());

	// Tipo Curso
	row.setCell(registration != null ? registration.getDegreeType().getLocalizedName() : "n/a");

	// Nome Curso
	row.setCell(registration != null ? registration.getDegree().getNameI18N().getContent() : "n/a");

	// Sigla Curso
	row.setCell(registration != null ? registration.getDegree().getSigla() : "n/a");

	// Ramo (caso se aplique)
	row.setCell("n�o determin�vel");

	if (registration != null) {
	    // N� de anos lectivos de inscri��o no Curso actual
	    row.setCell(calculateNumberOfEnrolmentYears(registration));

	    // �ltimo ano em que esteve inscrito
	    row.setCell(registration.getLastEnrolmentExecutionYear() != null ? registration.getLastEnrolmentExecutionYear()
		    .getName() : "");
	} else {
	    row.setCell("n/a");
	    row.setCell("n/a");
	}

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
		: process.getPerson().getMaritalStatus().toString());

	// Pa�s de Resid�ncia Permanente
	if (personalInformationBean.getCountryOfResidence() != null) {
	    row.setCell(personalInformationBean.getCountryOfResidence().getName());
	} else {
	    row.setCell(process.getStudent().getPerson().getCountryOfResidence() != null ? process.getStudent().getPerson()
		    .getCountryOfResidence().getName() : "");
	}

	// Distrito de Resid�ncia Permanente
	if (personalInformationBean.getDistrictSubdivisionOfResidence() != null) {
	    row.setCell(personalInformationBean.getDistrictSubdivisionOfResidence().getDistrict().getName());
	} else {
	    row.setCell(process.getStudent().getPerson().getDistrictOfResidence());
	}

	// Concelho de Resid�ncia Permanente
	if (personalInformationBean.getDistrictSubdivisionOfResidence() != null) {
	    row.setCell(personalInformationBean.getDistrictSubdivisionOfResidence().getName());
	} else {
	    row.setCell(process.getStudent().getPerson().getDistrictSubdivisionOfResidence());
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

	// Data preenchimento dados RAIDES
	if (personalInformationBean.getLastModifiedDate() != null) {
	    DateTime dateTime = personalInformationBean.getLastModifiedDate();
	    row.setCell(dateTime.getYear() + "-" + dateTime.getMonthOfYear() + "-" + dateTime.getDayOfMonth());
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
	for (StudentStatute statute : process.getStudent().getStudentStatutes()) {
	    if (statute.getStatuteType() == StudentStatuteType.SAS_GRANT_OWNER
		    && statute.isValidInExecutionPeriod(executionYear.getFirstExecutionPeriod())) {
		sasFound = true;
		break;
	    }
	}
	row.setCell(String.valueOf(sasFound));

	// Grau Precedente
	row.setCell(personalInformationBean.getPrecedentSchoolLevel() != null ? personalInformationBean.getPrecedentSchoolLevel()
		.getName() : "");

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

	double totalEctsConcludedUntilPreviousYear = 0d;
	if (registration != null) {

	    // Total de ECTS inscritos no total do ano
	    double totalCreditsEnrolled = 0d;
	    for (Enrolment enrollment : registration.getLastStudentCurricularPlan().getEnrolmentsByExecutionYear(executionYear)) {
		totalCreditsEnrolled += enrollment.getEctsCredits();
	    }
	    row.setCell(totalCreditsEnrolled);

	    // Total de ECTS conclu�dos at� ao fim do ano lectivo anterior (1�
	    // Semestre do ano lectivo actual) ao que se
	    // referem os dados (neste caso at� ao fim de 2008) no curso actual
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

	} else {
	    row.setCell("n/a");
	    row.setCell("n/a");
	    row.setCell("n/a");
	}

	if (registration != null) {
	    // Total de ECTS necess�rios para a conclus�o
	    if (concluded) {
		row.setCell(0);
	    } else {
		row.setCell(registration.getLastStudentCurricularPlan().getRoot().getDefaultEcts(executionYear)
			- totalEctsConcludedUntilPreviousYear);
	    }
	} else {
	    row.setCell("n/a");
	}

	// Se alunos de Doutoramento, inscrito na Parte Curricular?
	if (registration != null && registration.isDEA()) {
	    row.setCell(String.valueOf(registration.getLastStudentCurricularPlan().hasEnrolments(executionYear)));
	} else {
	    row.setCell("not PhD");
	}

	row.setCell(process.getPhdStudentNumber());

	PhdProgramProcessState lastActiveState = process.getMostRecentState();
	row.setCell(lastActiveState != null ? lastActiveState.getType().getLocalizedName() : "n/a");

	if (process.getCollaborationType() != null) {
	    row.setCell(process.getCollaborationType().getLocalizedName());
	} else {
	    row.setCell("n/a");
	}

	if (process.getCandidacyDate() != null) {
	    row.setCell(process.getCandidacyDate().toString("dd/MM/yyyy"));
	} else {
	    row.setCell("n/a");
	}

	if (process.getCandidacyProcess().getWhenRatified() != null) {
	    row.setCell(process.getCandidacyDate().toString("dd/MM/yyyy"));
	} else {
	    row.setCell("n/a");
	}

	if (process.getWhenStartedStudies() != null) {
	    row.setCell(process.getWhenStartedStudies().toString("dd/MM/yyyy"));
	} else {
	    row.setCell("n/a");
	}

	if (process.getThesisProcess() != null && process.getThesisProcess().getDiscussionDate() != null) {
	    row.setCell(process.getThesisProcess().getDiscussionDate().toString("dd/MM/yyyy"));
	} else {
	    row.setCell("n/a");
	}
    }

    private int calculateNumberOfEnrolmentYears(Registration registration) {
	return registration.getEnrolmentsExecutionYears().size();
    }

}
