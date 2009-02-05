package net.sourceforge.fenixedu.domain.reports;

import java.util.Iterator;

import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeInfo;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.util.report.Spreadsheet;
import net.sourceforge.fenixedu.util.report.Spreadsheet.Row;
import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class EctsLabelDegreeReportFile extends EctsLabelDegreeReportFile_Base {
    
    public  EctsLabelDegreeReportFile() {
        super();
    }

    public String getJobName() {
	return "Listagem para ECTS LABEL Cursos";
    }
    
    @Service
    public static GepReportFile newInstance(String type, DegreeType degreeType, ExecutionYear executionYear) {
	EctsLabelDegreeReportFile ectsLabelDegreeReportFile = new EctsLabelDegreeReportFile();
	ectsLabelDegreeReportFile.setType(type);
	ectsLabelDegreeReportFile.setDegreeType(degreeType);
	ectsLabelDegreeReportFile.setExecutionYear(executionYear);
	return ectsLabelDegreeReportFile;
    }
    
    protected String getPrefix() {
	return "ectsLabel_Cursos";
    }
    
    public void renderReport(Spreadsheet spreadsheet) throws Exception {

	createEctsLabelDegreesHeader(spreadsheet);

	for (final Degree degree : Degree.readNotEmptyDegrees()) {
	    if (checkDegreeType(getDegreeType(), degree)) {
		for (final DegreeCurricularPlan degreeCurricularPlan : degree.getDegreeCurricularPlansSet()) {
		    if (checkExecutionYear(getExecutionYear(), degreeCurricularPlan)) {
			addEctsLabelDegreeRow(spreadsheet, degreeCurricularPlan, getExecutionYear());
		    }
		}
	    }
	}
    }
    private void createEctsLabelDegreesHeader(final Spreadsheet spreadsheet) {
	spreadsheet.setHeaders(new String[] {

	"Nome",

	"Nome Ingl�s",

	"Tipo Curso",

	"Dura��o em anos",

	"Dura��o em Semanas de Estudo",

	"Cr�ditos ECTS",

	"Requisitos de Ingresso",

	"Requisitos de Ingresso (ingl�s)",

	"Objectivos Educacionais",

	"Objectivos Educacionais (ingl�s)",

	"Acesso a um n�vel superior de estudos",

	"Acesso a um n�vel superior de estudos (ingl�s)",

	"Normas e Regulamentos",

	"Normas e Regulamentos (ingl�s)",

	"Coordenador",

	"Contactos",

	"Contactos (ingl�s)"

	});
    }
    
    private String getResponsibleCoordinatorNames(final DegreeCurricularPlan degreeCurricularPlan,
	    final ExecutionYear executionYear) {
	final StringBuilder builder = new StringBuilder();
	final ExecutionDegree executionDegree = degreeCurricularPlan.getExecutionDegreeByYear(executionYear);
	final Iterator<Coordinator> coordinators = executionDegree.getResponsibleCoordinators().iterator();
	while (coordinators.hasNext()) {
	    builder.append(coordinators.next().getPerson().getName()).append(coordinators.hasNext() ? ", " : "");
	}
	return builder.toString();
    }

    private void addEctsLabelDegreeRow(final Spreadsheet spreadsheet, final DegreeCurricularPlan degreeCurricularPlan,
	    final ExecutionYear executionYear) {

	final Row row = spreadsheet.addRow();
	final Degree degree = degreeCurricularPlan.getDegree();

	row.setCell(normalize(degree.getNameFor(executionYear).getContent(Language.pt)));
	row.setCell(normalize(degree.getNameFor(executionYear).getContent(Language.en)));

	row.setCell(degree.getDegreeType().getLocalizedName());
	row.setCell(degree.getDegreeType().getYears());
	row.setCell(degree.getDegreeType().getYears() * 40);
	row.setCell(degree.getEctsCredits());

	final DegreeInfo degreeInfo = degree.getMostRecentDegreeInfo(executionYear);
	if (degreeInfo != null) {
	    row.setCell(normalize(degreeInfo.getDesignedFor(Language.pt)));
	    row.setCell(normalize(degreeInfo.getDesignedFor(Language.en)));
	    row.setCell(normalize(degreeInfo.getObjectives(Language.pt)));
	    row.setCell(normalize(degreeInfo.getObjectives(Language.en)));
	    row.setCell(normalize(degreeInfo.getProfessionalExits(Language.pt)));
	    row.setCell(normalize(degreeInfo.getProfessionalExits(Language.en)));
	    row.setCell(normalize(degreeInfo.getOperationalRegime(Language.pt)));
	    row.setCell(normalize(degreeInfo.getOperationalRegime(Language.en)));
	    row.setCell(normalize(getResponsibleCoordinatorNames(degreeCurricularPlan, executionYear)));
	    row.setCell(normalize(degreeInfo.getAdditionalInfo(Language.pt)));
	    row.setCell(normalize(degreeInfo.getAdditionalInfo(Language.en)));
	}
    }
    
}
