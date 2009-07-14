package net.sourceforge.fenixedu.domain.reports;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationConclusionBean;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.candidacy.CandidacyInformationBean;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.StudentStatute;
import net.sourceforge.fenixedu.domain.student.StudentStatuteType;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;

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
//		 if (count == 20) {
//		    break;
//		}
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
	spreadsheet.setHeader("n�. anos lectivos inscri��o curso actual");
	spreadsheet.setHeader("estabelecimento curso anterior");
	spreadsheet.setHeader("curso anterior");
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
	spreadsheet.setHeader("bolseiro (info. RAIDES)");
	spreadsheet.setHeader("bolseiro (info. oficial)");
	spreadsheet.setHeader("habilita��o anterior");
	spreadsheet.setHeader("pa�s habilita��o anterior");
	spreadsheet.setHeader("tipo estabelecimento ensino secund�rio");
	spreadsheet.setHeader("total ECTS conclu�dos fim ano lectivo anterior (1� Semestre do ano lectivo actual)");
	spreadsheet.setHeader("total ECTS necess�rios para a conclus�o");
	spreadsheet.setHeader("Tem situa��o de propinas no lectivo dos dados?");
    }

    private void reportRaidesGraduate(final Spreadsheet sheet, final Registration registration, ExecutionYear executionYear,
	    final CycleType cycleType, final boolean concluded, final YearMonthDay conclusionDate) {

	final Row row = sheet.addRow();
	final Person graduate = registration.getPerson();
	final CandidacyInformationBean candidacyInformationBean = registration.getCandidacyInformationBean();

	// Ciclo
	row.setCell(cycleType.getDescription());

	// Conclu�do
	row.setCell(String.valueOf(concluded));

	// M�dia do Ciclo
	row.setCell(concluded ? registration.getLastStudentCurricularPlan().getCycle(cycleType).getCurriculum(
		conclusionDate.toDateTimeAtMidnight()).getAverage().toPlainString() : "n/a");

	// Data de conclus�o
	row.setCell(conclusionDate != null ? conclusionDate.toString("yyyy/MM/dd") : "");

	// Data de In�cio
	row.setCell(registration.getStartDate() != null ? registration.getStartDate().toString("yyyy/MM/dd") : "");

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
	row.setCell(graduate.getDateOfBirthYearMonthDay() != null ? graduate.getDateOfBirthYearMonthDay().toString("yyyy/MM/dd")
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

	// Estabelecimento do Curso Anterior (se o aluno ingressou por uma via
	// diferente CNA, e deve
	// ser IST caso o aluno tenha estado matriculado noutro curso do IST)
	row.setCell(candidacyInformationBean.getInstitution() != null ? candidacyInformationBean.getInstitution().getName() : "");

	// Curso Anterior (se o aluno ingressou por uma via diferente CNA, e
	// deve ser IST caso o aluno
	// tenha estado matriculado noutro curso do IST)
	row.setCell(candidacyInformationBean.getDegreeDesignation());

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

	// Condi��o perante a situa��o na profiss�o/Ocupa��o do Pai
	if (candidacyInformationBean.getFatherProfessionalCondition() != null) {
	    row.setCell(candidacyInformationBean.getFatherProfessionalCondition().getName());
	} else {
	    row.setCell("");
	}

	// Condi��o perante a situa��o na profiss�o/Ocupa��o da M�e
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

	// Bolseiro (info. RAIDES)
	if (candidacyInformationBean.getGrantOwnerType() != null) {
	    row.setCell(candidacyInformationBean.getGrantOwnerType().getName());
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

	// Tipo de Estabelecimento Frequentado no Ensino Secund�rio
	if (candidacyInformationBean.getHighSchoolType() != null) {
	    row.setCell(candidacyInformationBean.getHighSchoolType().getName());
	} else {
	    row.setCell("");
	}

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
