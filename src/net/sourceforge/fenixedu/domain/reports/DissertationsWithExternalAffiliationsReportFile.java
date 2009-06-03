package net.sourceforge.fenixedu.domain.reports;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.domain.thesis.ThesisEvaluationParticipant;
import net.sourceforge.fenixedu.domain.thesis.ThesisParticipationType;
import net.sourceforge.fenixedu.presentationTier.Action.coordinator.thesis.ThesisPresentationState;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

public class DissertationsWithExternalAffiliationsReportFile extends DissertationsWithExternalAffiliationsReportFile_Base {

    public DissertationsWithExternalAffiliationsReportFile() {
	super();
    }

    @Override
    public String getJobName() {
	return "Listagem de disserta��es com afilia��es externas";
    }

    @Override
    protected String getPrefix() {
	return "disserta��es com afilia��es externas";
    }

    @Override
    public void renderReport(Spreadsheet spreadsheet) throws IOException {
	list(spreadsheet, getExecutionYear());
    }

    private void list(Spreadsheet spreadsheet, final ExecutionYear executionYear) throws IOException {
	spreadsheet.setName("Dissertacoes " + executionYear.getYear().replace("/", ""));
	spreadsheet.setHeader("Numero aluno");
	spreadsheet.setHeader("Nome aluno");
	spreadsheet.setHeader("Tipo Curso");
	spreadsheet.setHeader("Curso");
	spreadsheet.setHeader("Sigla Curso");
	spreadsheet.setHeader("Tese");
	spreadsheet.setHeader("Estado da tese");
	spreadsheet.setHeader("Affiliacao Orientador");
	spreadsheet.setHeader("Distribuicao Creditos Orientador");
	spreadsheet.setHeader("Affiliacao Corientador");
	spreadsheet.setHeader("Distribuicao Creditos Corientador");

	for (final Thesis thesis : getRootDomainObject().getThesesSet()) {
	    final Enrolment enrolment = thesis.getEnrolment();
	    final ExecutionSemester executionPeriod = enrolment.getExecutionPeriod();
	    if (executionPeriod.getExecutionYear() == executionYear) {
		final ThesisPresentationState thesisPresentationState = ThesisPresentationState
			.getThesisPresentationState(thesis);

		final Degree degree = enrolment.getStudentCurricularPlan().getDegree();
		final DegreeType degreeType = degree.getDegreeType();

		final Row row = spreadsheet.addRow();
		row.setCell(thesis.getStudent().getNumber().toString());
		row.setCell(thesis.getStudent().getPerson().getName());
		row.setCell(degreeType.getLocalizedName());
		row.setCell(degree.getPresentationName());
		row.setCell(degree.getSigla());
		row.setCell(thesis.getTitle().getContent());
		row.setCell(thesisPresentationState.getName());

		addTeacherRows(thesis, row, ThesisParticipationType.ORIENTATOR);
		addTeacherRows(thesis, row, ThesisParticipationType.COORIENTATOR);
	    }
	}
	spreadsheet.exportToXLSSheet(new File("dissertacoes" + executionYear.getYear().replace("/", "") + ".xls"));
    }

    protected void addTeacherRows(final Thesis thesis, final Row row, final ThesisParticipationType thesisParticipationType) {
	final StringBuilder oasb = new StringBuilder();
	final StringBuilder odsb = new StringBuilder();
	for (final ThesisEvaluationParticipant thesisEvaluationParticipant : thesis.getAllParticipants(thesisParticipationType)) {
	    if (oasb.length() > 0) {
		oasb.append(" ");
	    }
	    final String affiliation = thesisEvaluationParticipant.getAffiliation();
	    if (affiliation != null) {
		oasb.append(affiliation);
	    } else {
		oasb.append("--");
	    }
	    if (odsb.length() > 0) {
		odsb.append(" ");
	    }
	    final double credistDistribution = thesisEvaluationParticipant.getCreditsDistribution();
	    odsb.append(Double.toString(credistDistribution));
	}
	row.setCell(oasb.toString());
	row.setCell(odsb.toString());
    }

    private Set<String> getAffiliations(final Thesis thesis) {
	final Set<String> affiliations = new TreeSet<String>();
	for (final ThesisEvaluationParticipant thesisEvaluationParticipant : thesis.getParticipationsSet()) {
	    final ThesisParticipationType thesisParticipationType = thesisEvaluationParticipant.getType();
	    if (thesisParticipationType == ThesisParticipationType.ORIENTATOR
		    || thesisParticipationType == ThesisParticipationType.COORIENTATOR) {
		final String affiliation = thesisEvaluationParticipant.getAffiliation();
		if (affiliation != null) {
		    affiliations.add(affiliation);
		}
	    }
	}
	return affiliations;
    }

}
