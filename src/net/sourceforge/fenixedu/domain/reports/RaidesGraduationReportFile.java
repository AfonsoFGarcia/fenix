package net.sourceforge.fenixedu.domain.reports;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationConclusionBean;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.GrantOwnerType;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.candidacy.CandidacyInformationBean;
import net.sourceforge.fenixedu.domain.candidacy.Ingression;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.student.PrecedentDegreeInformation;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.StudentStatute;
import net.sourceforge.fenixedu.domain.student.StudentStatuteType;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumLine;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule.ConclusionValue;

import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

public class RaidesGraduationReportFile extends RaidesGraduationReportFile_Base {

    public RaidesGraduationReportFile() {
	super();
    }

    @Override
    public String getJobName() {
	return "Listagem RAIDES - Gradua��o";
    }

    @Override
    protected String getPrefix() {
	return "graduationRAIDES";
    }

    @Override
    public void renderReport(Spreadsheet spreadsheet) throws Exception {
	ExecutionYear executionYear = getExecutionYear();
	createSpreadsheet(spreadsheet);

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

			if (cycleCGroup.isConcluded()) {
			    final ExecutionYear conclusionYear = registrationConclusionBean.getConclusionYear();

			    if (conclusionYear != executionYear && conclusionYear != executionYear.getPreviousExecutionYear()) {
				continue;
			    }

			}

			LinkedList<RegistrationState> states = new LinkedList<RegistrationState>();
			for (RegistrationState state : registration.getRegistrationStates()) {
			    if (!state.getStateDate().isAfter(executionYear.getEndDateYearMonthDay().toDateMidnight())) {
				states.add(state);
			    }
			}
			Collections.sort(states, RegistrationState.DATE_COMPARATOR);
			RegistrationStateType lastState = states.isEmpty() ? RegistrationStateType.REGISTERED : states.getLast()
				.getStateType();

			if ((lastState.isActive() || lastState == RegistrationStateType.CONCLUDED)
				&& (cycleCGroup.isConcluded(executionYear.getPreviousExecutionYear()) == ConclusionValue.CONCLUDED)) {
			    reportRaidesGraduate(spreadsheet, registration, executionYear, cycleType, true,
				    registrationConclusionBean.getConclusionDate(), registrationConclusionBean.getAverage());
			} else if ((lastState.isActive() || lastState == RegistrationStateType.CONCLUDED)
				&& registration.getLastDegreeCurricularPlan().hasExecutionDegreeFor(executionYear)) {
			    reportRaidesGraduate(spreadsheet, registration, executionYear, cycleType, false, null, registrationConclusionBean.getAverage());
			}
		    }
		}

		count++;

		// if (count % 20 == 0) {
		// System.out.println("Processed " + count);
		// return;
		// }
	    }

	}

	System.out.println("END report for " + getDegreeType().name());

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
	    for (StudentCurricularPlan studentCurricularPlan : executionDegree.getDegreeCurricularPlan()
		    .getStudentCurricularPlans()) {
		if (!studentCurricularPlan.getStartDateYearMonthDay().isAfter(executionYear.getEndDateYearMonthDay())) {
		    result.add(studentCurricularPlan);
		}
	    }
	}
    }

    private void createSpreadsheet(final Spreadsheet spreadsheet) {
	spreadsheet.setHeader("ciclo");
	spreadsheet.setHeader("conclu�do (ano anterior)?");
	spreadsheet.setHeader("m�dia do ciclo");
	spreadsheet.setHeader("Data de conclus�o");
	spreadsheet.setHeader("Data de in�cio");
	spreadsheet.setHeader("n�mero aluno");
	spreadsheet.setHeader("n�mero identifica��o");
	spreadsheet.setHeader("tipo identifica��o");
	spreadsheet.setHeader("nome");
	spreadsheet.setHeader("g�nero");
	spreadsheet.setHeader("data nascimento");
	spreadsheet.setHeader("pa�s nascimento");
	spreadsheet.setHeader("pa�s nacionalidade");
	spreadsheet.setHeader("tipo curso");
	spreadsheet.setHeader("nome curso");
	spreadsheet.setHeader("sigla curso");
	spreadsheet.setHeader("ramo");
	spreadsheet.setHeader("ano curricular");
	spreadsheet.setHeader("ano ingresso curso actual");
	spreadsheet.setHeader("n�. anos lectivos inscri��o curso actual");
	spreadsheet.setHeader("regime frequ�ncia curso");
	spreadsheet.setHeader("tipo aluno");
	spreadsheet.setHeader("regime ingresso (c�digo)");
	spreadsheet.setHeader("regime ingresso (designa��o)");
	spreadsheet.setHeader("estabelecimento de proveni�ncia (qd aplic�vel)");
	spreadsheet.setHeader("curso de proveni�ncia (qd aplic�vel)");
	spreadsheet.setHeader("estabelecimento curso anterior");
	spreadsheet.setHeader("curso anterior");
	spreadsheet.setHeader("n� de inscri��es no curso anterior");
	spreadsheet.setHeader("nota ingresso");
	spreadsheet.setHeader("op��o ingresso");
	spreadsheet.setHeader("n� outras candidaturas ensino superior");
	spreadsheet.setHeader("estado civil");
	spreadsheet.setHeader("pa�s resid�ncia permanente");
	spreadsheet.setHeader("distrito resid�ncia permanente");
	spreadsheet.setHeader("concelho resid�ncia permanente");
	spreadsheet.setHeader("deslocado resid�ncia permanente");
	spreadsheet.setHeader("n�ve escolaridade pai");
	spreadsheet.setHeader("n�vel escolaridade m�e");
	spreadsheet.setHeader("condi��o perante profiss�o pai");
	spreadsheet.setHeader("condi��o perante profiss�o m�e");
	spreadsheet.setHeader("profiss�o pai");
	spreadsheet.setHeader("profiss�o m�e");
	spreadsheet.setHeader("profiss�o aluno");
	spreadsheet.setHeader("estatuto trabalhador estudante introduzido (info. RAIDES)");
	spreadsheet.setHeader("estatuto trabalhador 1� semestre ano (info. oficial)");
	spreadsheet.setHeader("estatuto trabalhador 2� semestre ano (info. oficial)");
	spreadsheet.setHeader("bolseiro (info. RAIDES)");
	spreadsheet.setHeader("institui��o que atribuiu a bolsa (qd aplic�vel)");
	spreadsheet.setHeader("bolseiro (info. oficial)");
	spreadsheet.setHeader("habilita��o anterior");
	spreadsheet.setHeader("pa�s habilita��o anterior");
	spreadsheet.setHeader("ano de conclus�o da habilita��o anterior");
	spreadsheet.setHeader("nota da habilita��o anterior");
	spreadsheet.setHeader("n� reten��es ensino secund�rio");
	spreadsheet.setHeader("tipo estabelecimento ensino secund�rio");
	spreadsheet.setHeader("total ECTS conclu�dos fim ano lectivo anterior");
	spreadsheet.setHeader("n�. disciplinas inscritas ano lectivo anterior dados");
	spreadsheet.setHeader("n�. disciplinas aprovadas ano lectivo anterior dados");
	spreadsheet.setHeader("n�. disciplinas inscritas 1� semestre ano dados");
	spreadsheet.setHeader("n�. inscri��es externas ano dados");
	spreadsheet.setHeader("estado matr�cula ano anterior dados");
	spreadsheet.setHeader("estado matr�cula ano dados");
	spreadsheet.setHeader("data do estado de matr�cula");
	spreadsheet.setHeader("n�. ECTS 1� ciclo conclu�dos fim ano lectivo anterior");
	spreadsheet.setHeader("n�. ECTS 2� ciclo conclu�dos fim ano lectivo anterior");
	spreadsheet.setHeader("n�. ECTS extra 1� ciclo conclu�dos fim ano lectivo anterior");
	spreadsheet.setHeader("n�. ECTS extracurriculares conclu�dos fim ano lectivo anterior");
	spreadsheet.setHeader("n�. ECTS Propedeuticas conclu�dos fim ano lectivo anterior");
	spreadsheet.setHeader("Tem situa��o de propinas no lectivo dos dados?");
    }

    private String printDouble(Double value) {
	return value == null ? "" : value.toString().replace('.', ',');
    }

    private String printBigDecimal(BigDecimal value) {
	return value == null ? "" : value.toPlainString().replace('.', ',');
    }

    private void reportRaidesGraduate(final Spreadsheet sheet, final Registration registration, ExecutionYear executionYear,
	    final CycleType cycleType, final boolean concluded, final YearMonthDay conclusionDate, BigDecimal average) {

	final Row row = sheet.addRow();
	final Person graduate = registration.getPerson();
	List<Registration> registrationPath = getFullRegistrationPath(registration);
	Registration sourceRegistration = registrationPath.get(0);
	final CandidacyInformationBean candidacyInformationBean = sourceRegistration.getCandidacyInformationBean();
	StudentCurricularPlan lastStudentCurricularPlan = registration.getLastStudentCurricularPlan();

	// Ciclo
	row.setCell(cycleType.getDescription());

	// Conclu�do
	row.setCell(String.valueOf(concluded));

	// M�dia do Ciclo
	row.setCell(concluded ? printBigDecimal(average.setScale(0, BigDecimal.ROUND_HALF_EVEN)) : printBigDecimal(average));

	// Data de Conclus�o
	row.setCell(conclusionDate != null ? conclusionDate.toString("dd-MM-yyyy") : "");

	// Data de In�cio
	row.setCell(registration.getStartDate() != null ? registration.getStartDate().toString("dd-MM-yyyy") : "");

	// N� de aluno
	row.setCell(registration.getNumber());

	// N� de Identifica��o
	row.setCell(graduate.getDocumentIdNumber());

	// Tipo Identifica��o
	row.setCell(graduate.getIdDocumentType().getLocalizedName());

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

	// Ano Curricular
	row.setCell(registration.getCurricularYear());

	// Ano de Ingresso no Curso Actual
	row.setCell(sourceRegistration.getStartExecutionYear().getName());

	// N� de anos lectivos de inscri��o no Curso actual
	int numberOfEnrolmentYears = 0;
	for (Registration current : registrationPath) {
	    numberOfEnrolmentYears += current.getEnrolmentsExecutionYears().size();
	}
	row.setCell(numberOfEnrolmentYears);

	// Regime de frequ�ncia curso: Tempo integral/Tempo Parcial
	row.setCell(registration.getRegimeType(executionYear) != null ? registration.getRegimeType(executionYear).getName() : "");

	// Tipo de Aluno (AFA, AM, ERASMUS, etc)
	row.setCell(registration.getRegistrationAgreement() != null ? registration.getRegistrationAgreement().getName() : "");

	// Regime de Ingresso no Curso Actual (c�digo)
	Ingression ingression = sourceRegistration.getIngression();
	if (ingression == null && sourceRegistration.getStudentCandidacy() != null) {
	    ingression = sourceRegistration.getStudentCandidacy().getIngression();
	}
	row.setCell(ingression != null ? ingression.getName() : "");

	// Regime de Ingresso no Curso Actual (designa��o)
	row.setCell(ingression != null ? ingression.getFullDescription() : "");

	if (sourceRegistration.getStudentCandidacy() != null) {
	    // Estabelecimento de proveni�ncia: Institui��o onde esteve
	    // inscrito mas
	    // n�o obteve grau, (e.g: transferencias, mudan�as de curso...)
	    PrecedentDegreeInformation precedence = sourceRegistration.getStudentCandidacy().getPrecedentDegreeInformation();
	    row.setCell(precedence != null && precedence.getInstitutionName() != null ? precedence.getInstitutionName() : "");
	    // Curso de proveni�ncia
	    row.setCell(precedence != null && precedence.getDegreeDesignation() != null ? precedence.getDegreeDesignation() : "");
	} else {
	    row.setCell("");
	    row.setCell("");
	}

	// Estabelecimento do Curso Anterior (se o aluno ingressou por uma via
	// diferente CNA, e deve
	// ser IST caso o aluno tenha estado matriculado noutro curso do IST)
	row.setCell(candidacyInformationBean.getInstitution() != null ? candidacyInformationBean.getInstitution().getName() : "");

	// Curso Anterior (se o aluno ingressou por uma via diferente CNA, e
	// deve ser IST caso o aluno
	// tenha estado matriculado noutro curso do IST)
	row.setCell(candidacyInformationBean.getDegreeDesignation());

	// N� de inscri��es no curso anterior"
	if (sourceRegistration.getIndividualCandidacy() != null
		&& sourceRegistration.getIndividualCandidacy().getPrecedentDegreeInformation() != null) {
	    row.setCell(sourceRegistration.getIndividualCandidacy().getPrecedentDegreeInformation()
		    .getNumberOfEnroledCurricularCourses());
	} else {
	    row.setCell("");
	}

	// Nota de Ingresso
	row.setCell(printDouble(candidacyInformationBean.getEntryGrade()));

	// Op��o de Ingresso
	row.setCell(candidacyInformationBean.getPlacingOption());

	// N� de Candidaturas ao Ensino Superior para Al�m Desta
	row.setCell(candidacyInformationBean.getNumberOfCandidaciesToHigherSchool());

	// Estado Civil
	row.setCell(candidacyInformationBean.getMaritalStatus() != null ? candidacyInformationBean.getMaritalStatus().toString()
		: registration.getPerson().getMaritalStatus().toString());

	// Pa�s de Resid�ncia Permanente
	if (candidacyInformationBean.getCountryOfResidence() != null) {
	    row.setCell(candidacyInformationBean.getCountryOfResidence().getName());
	} else {
	    row.setCell(registration.getStudent().getPerson().getCountryOfResidence() != null ? registration.getStudent()
		    .getPerson().getCountryOfResidence().getName() : "");
	}

	// Distrito de Resid�ncia Permanente
	if (candidacyInformationBean.getDistrictSubdivisionOfResidence() != null) {
	    row.setCell(candidacyInformationBean.getDistrictSubdivisionOfResidence().getDistrict().getName());
	} else {
	    row.setCell(registration.getStudent().getPerson().getDistrictOfResidence());
	}

	// Concelho de Resid�ncia Permanente
	if (candidacyInformationBean.getDistrictSubdivisionOfResidence() != null) {
	    row.setCell(candidacyInformationBean.getDistrictSubdivisionOfResidence().getName());
	} else {
	    row.setCell(registration.getStudent().getPerson().getDistrictSubdivisionOfResidence());
	}

	// Deslocado da Resid�ncia Permanente
	if (candidacyInformationBean.getDislocatedFromPermanentResidence() != null) {
	    row.setCell(candidacyInformationBean.getDislocatedFromPermanentResidence().toString());
	} else {
	    row.setCell("");
	}

	// N�vel de Escolaridade do Pai
	if (candidacyInformationBean.getFatherSchoolLevel() != null) {
	    row.setCell(candidacyInformationBean.getFatherSchoolLevel().getName());
	} else {
	    row.setCell("");
	}

	// N�vel de Escolaridade da M�e
	if (candidacyInformationBean.getMotherSchoolLevel() != null) {
	    row.setCell(candidacyInformationBean.getMotherSchoolLevel().getName());
	} else {
	    row.setCell("");
	}

	// Condi��o perante a situa��o na profiss�o/Ocupa��o do
	// Pai
	if (candidacyInformationBean.getFatherProfessionalCondition() != null) {
	    row.setCell(candidacyInformationBean.getFatherProfessionalCondition().getName());
	} else {
	    row.setCell("");
	}

	// Condi��o perante a situa��o na profiss�o/Ocupa��o da
	// M�e
	if (candidacyInformationBean.getMotherProfessionalCondition() != null) {
	    row.setCell(candidacyInformationBean.getMotherProfessionalCondition().getName());
	} else {
	    row.setCell("");
	}

	// Profiss�o do Pai
	if (candidacyInformationBean.getFatherProfessionType() != null) {
	    row.setCell(candidacyInformationBean.getFatherProfessionType().getName());
	} else {
	    row.setCell("");
	}

	// Profiss�o da M�e
	if (candidacyInformationBean.getMotherProfessionType() != null) {
	    row.setCell(candidacyInformationBean.getMotherProfessionType().getName());
	} else {
	    row.setCell("");
	}

	// Profiss�o do Aluno
	if (candidacyInformationBean.getProfessionType() != null) {
	    row.setCell(candidacyInformationBean.getProfessionType().getName());
	} else {
	    row.setCell("");
	}

	// Estatuto de Trabalhador Estudante introduzido pelo aluno
	if (candidacyInformationBean.getProfessionalCondition() != null) {
	    row.setCell(candidacyInformationBean.getProfessionalCondition().getName());
	} else {
	    row.setCell("");
	}

	// Estatuto de Trabalhador Estudante 1� semestre do ano a que se
	// referem
	// os dados
	boolean working1Found = false;
	for (StudentStatute statute : registration.getStudent().getStudentStatutes()) {
	    if (statute.getStatuteType() == StudentStatuteType.WORKING_STUDENT
		    && statute.isValidInExecutionPeriod(executionYear.getFirstExecutionPeriod())) {
		working1Found = true;
		break;
	    }
	}
	row.setCell(String.valueOf(working1Found));

	// Estatuto de Trabalhador Estudante 1� semestre do ano a que se
	// referem
	// os dados
	boolean working2Found = false;
	for (StudentStatute statute : registration.getStudent().getStudentStatutes()) {
	    if (statute.getStatuteType() == StudentStatuteType.WORKING_STUDENT
		    && statute.isValidInExecutionPeriod(executionYear.getLastExecutionPeriod())) {
		working2Found = true;
		break;
	    }
	}
	row.setCell(String.valueOf(working2Found));

	// Bolseiro (info. RAIDES)
	if (candidacyInformationBean.getGrantOwnerType() != null) {
	    row.setCell(candidacyInformationBean.getGrantOwnerType().getName());
	} else {
	    row.setCell("");
	}

	// Institui��o que atribuiu a bolsa
	if (candidacyInformationBean.getGrantOwnerType() != null
		&& candidacyInformationBean.getGrantOwnerType().equals(GrantOwnerType.OTHER_INSTITUTION_GRANT_OWNER)) {
	    row.setCell(candidacyInformationBean.getGrantOwnerProviderName());
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

	// Habilita��o Anterior ao Curso Actual
	row.setCell(candidacyInformationBean.getSchoolLevel() != null ? candidacyInformationBean.getSchoolLevel().getName() : "");

	// Pa�s de Habilita��o Anterior ao Curso Actual
	row.setCell(candidacyInformationBean.getCountryWhereFinishedPrecedentDegree() != null ? candidacyInformationBean
		.getCountryWhereFinishedPrecedentDegree().getName() : "");

	// Ano de conclus�o da habilita��o anterior
	row.setCell(candidacyInformationBean.getConclusionYear());

	// Nota de conclus�o da habilita��o anterior
	row.setCell(candidacyInformationBean.getConclusionGrade() != null ? candidacyInformationBean.getConclusionGrade() : "");

	// N� de vezes que ficou retido no Ensino Secund�rio
	row.setCell(candidacyInformationBean.getNumberOfFlunksOnHighSchool());

	// Tipo de Estabelecimento Frequentado no Ensino Secund�rio
	if (candidacyInformationBean.getHighSchoolType() != null) {
	    row.setCell(candidacyInformationBean.getHighSchoolType().getName());
	} else {
	    row.setCell("");
	}

	int totalEnrolmentsInPreviousYear = 0;
	int totalEnrolmentsApprovedInPreviousYear = 0;
	int totalEnrolmentsInFirstSemester = 0;
	double totalEctsConcludedUntilPreviousYear = 0d;
	for (final CycleCurriculumGroup cycleCurriculumGroup : lastStudentCurricularPlan.getInternalCycleCurriculumGrops()) {

	    totalEctsConcludedUntilPreviousYear += cycleCurriculumGroup.getCreditsConcluded(executionYear
		    .getPreviousExecutionYear());

	    totalEnrolmentsInPreviousYear += cycleCurriculumGroup.getEnrolmentsBy(executionYear.getPreviousExecutionYear())
		    .size();

	    for (final Enrolment enrolment : cycleCurriculumGroup.getEnrolmentsBy(executionYear.getPreviousExecutionYear())) {
		if (enrolment.isApproved()) {
		    totalEnrolmentsApprovedInPreviousYear++;
		}
	    }

	    totalEnrolmentsInFirstSemester += cycleCurriculumGroup.getEnrolmentsBy(executionYear.getFirstExecutionPeriod())
		    .size();

	}

	// Total de ECTS conclu�dos at� ao fim do ano lectivo anterior ao
	// que se
	// referem os dados (neste caso at� ao fim de 2007/08) no curso actual
	row.setCell(printDouble(totalEctsConcludedUntilPreviousYear));

	// N� de Disciplinas Inscritos no ano lectivo anterior ao que se
	// referem
	// os dados
	row.setCell(totalEnrolmentsInPreviousYear);

	// N� de Disciplinas Aprovadas no ano lectivo anterior ao que se
	// referem
	// os dados
	row.setCell(totalEnrolmentsApprovedInPreviousYear);

	// N� de Disciplinas Inscritos no 1� Semestre do ano a que se
	// referem os
	// dados
	row.setCell(totalEnrolmentsInFirstSemester);

	// N� de Inscri��es Externas no ano a que se referem os dados
	int extraCurricularEnrolmentsCount = lastStudentCurricularPlan.getExtraCurriculumGroup().getEnrolmentsBy(executionYear)
		.size();

	for (final CycleCurriculumGroup cycleCurriculumGroup : lastStudentCurricularPlan.getExternalCurriculumGroups()) {
	    extraCurricularEnrolmentsCount += cycleCurriculumGroup.getEnrolmentsBy(executionYear).size();
	}

	if (lastStudentCurricularPlan.hasPropaedeuticsCurriculumGroup()) {
	    extraCurricularEnrolmentsCount += lastStudentCurricularPlan.getPropaedeuticCurriculumGroup().getEnrolmentsBy(
		    executionYear).size();
	}

	row.setCell(extraCurricularEnrolmentsCount);

	// Estados de matr�cula
	SortedSet<RegistrationState> states = new TreeSet<RegistrationState>(RegistrationState.DATE_COMPARATOR);
	for (Registration current : registrationPath) {
	    states.addAll(current.getRegistrationStates());
	}
	RegistrationState previousYearState = null;
	RegistrationState currentYearState = null;
	for (RegistrationState state : states) {
	    if (!state.getStateDate().isAfter(
		    executionYear.getPreviousExecutionYear().getEndDateYearMonthDay().toDateTimeAtMidnight())) {
		previousYearState = state;
	    }
	    if (!state.getStateDate().isAfter(executionYear.getEndDateYearMonthDay().toDateTimeAtMidnight())) {
		currentYearState = state;
	    }
	}

	// Estado da matr�cula no ano lectivo anterior ao que se referem os
	// dados
	row.setCell(previousYearState != null ? previousYearState.getStateType().getDescription() : "n/a");

	// Estado (da matr�cula) no ano a que se referem os dados
	row.setCell(currentYearState != null ? currentYearState.getStateType().getDescription() : "n/a");

	// Data do estado de matr�cula
	row.setCell(currentYearState != null ? currentYearState.getStateDate().toString("dd-MM-yyyy") : "n/a");

	// N� ECTS do 1� Ciclo conclu�dos at� ao fim do ano lectivo
	// anterior ao que se referem os dados
	final CycleCurriculumGroup firstCycleCurriculumGroup = lastStudentCurricularPlan.getRoot().getCycleCurriculumGroup(
		CycleType.FIRST_CYCLE);
	row.setCell(firstCycleCurriculumGroup != null ? printBigDecimal(firstCycleCurriculumGroup.getCurriculum(executionYear)
		.getSumEctsCredits()) : "");

	// N� ECTS do 2� Ciclo conclu�dos at� ao fim do ano lectivo
	// anterior ao que se referem os dados
	final CycleCurriculumGroup secondCycleCurriculumGroup = lastStudentCurricularPlan.getRoot().getCycleCurriculumGroup(
		CycleType.SECOND_CYCLE);
	row
		.setCell(secondCycleCurriculumGroup != null && !secondCycleCurriculumGroup.isExternal() ? printBigDecimal(secondCycleCurriculumGroup
			.getCurriculum(executionYear).getSumEctsCredits())
			: "");

	// N� ECTS do 2� Ciclo Extra primeiro ciclo conclu�dos at� ao fim do ano
	// lectivo anterior ao que se referem os dados
	Double extraFirstCycleEcts = 0d;
	for (final CycleCurriculumGroup cycleCurriculumGroup : lastStudentCurricularPlan.getExternalCurriculumGroups()) {
	    for (final CurriculumLine curriculumLine : cycleCurriculumGroup.getAllCurriculumLines()) {
		if (!curriculumLine.getExecutionYear().isAfter(executionYear.getPreviousExecutionYear())) {
		    extraFirstCycleEcts += curriculumLine.getCreditsConcluded(executionYear.getPreviousExecutionYear());
		}
	    }
	}
	row.setCell(printDouble(extraFirstCycleEcts));

	// N� ECTS Extracurriculares conclu�dos at� ao fim do ano lectivo
	// anterior que ao se referem os dados
	Double extraCurricularEcts = 0d;
	for (final CurriculumLine curriculumLine : lastStudentCurricularPlan.getExtraCurriculumGroup().getAllCurriculumLines()) {
	    if (curriculumLine.isApproved() && curriculumLine.hasExecutionPeriod()
		    && !curriculumLine.getExecutionYear().isAfter(executionYear.getPreviousExecutionYear())) {
		extraCurricularEcts += curriculumLine.getEctsCreditsForCurriculum().doubleValue();
	    }
	}
	row.setCell(printDouble(extraCurricularEcts));

	// N� ECTS Propaedeutic conclu�dos at� ao fim do ano lectivo
	// anterior que ao se referem os dados
	Double propaedeuticEcts = 0d;
	if (lastStudentCurricularPlan.getPropaedeuticCurriculumGroup() != null) {
	    for (final CurriculumLine curriculumLine : lastStudentCurricularPlan.getPropaedeuticCurriculumGroup()
		    .getAllCurriculumLines()) {
		if (curriculumLine.isApproved() && curriculumLine.hasExecutionPeriod()
			&& !curriculumLine.getExecutionYear().isAfter(executionYear.getPreviousExecutionYear())) {
		    propaedeuticEcts += curriculumLine.getEctsCreditsForCurriculum().doubleValue();
		}
	    }
	}
	row.setCell(printDouble(propaedeuticEcts));

	// Tem situa��o de propinas no lectivo dos dados
	row.setCell(String.valueOf(lastStudentCurricularPlan.hasAnyGratuityEventFor(executionYear)));
    }
}