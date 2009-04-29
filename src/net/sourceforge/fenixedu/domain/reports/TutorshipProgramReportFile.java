package net.sourceforge.fenixedu.domain.reports;

import java.math.BigDecimal;
import java.math.RoundingMode;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Grade;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.candidacy.CandidacyInformationBean;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.util.StringUtils;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

public class TutorshipProgramReportFile extends TutorshipProgramReportFile_Base {

    public TutorshipProgramReportFile() {
	super();
    }

    @Override
    public String getJobName() {
	return "Listagem para programa tutorado";
    }

    @Override
    protected String getPrefix() {
	return "tutorshipProgram";
    }

    @Override
    public void renderReport(final Spreadsheet spreadsheet) throws Exception {
	spreadsheet.setHeader("N�mero");
	spreadsheet.setHeader("Sexo");
	spreadsheet.setHeader("M�dia");
	spreadsheet.setHeader("M�dia Anual");
	spreadsheet.setHeader("N�mero Inscri��es");
	spreadsheet.setHeader("N�mero Aprova��es");
	spreadsheet.setHeader("Nota de Seria��o");
	spreadsheet.setHeader("Local de Origem");

	final ExecutionYear executionYear = getExecutionYear();
	for (final Degree degree : Degree.readNotEmptyDegrees()) {
	    if (checkDegreeType(getDegreeType(), degree)) {
		if (isActive(degree)) {
		    for (final Registration registration : degree.getRegistrationsSet()) {
			if (registration.isRegistered(getExecutionYear())) {

			    int enrolmentCounter = 0;
			    int aprovalCounter = 0;
			    BigDecimal bigDecimal = null;
			    double totalCredits = 0;

			    for (final Registration otherRegistration : registration.getStudent().getRegistrationsSet()) {
				if (otherRegistration.getDegree() == registration.getDegree()) {
				    for (final StudentCurricularPlan studentCurricularPlan : otherRegistration.getStudentCurricularPlansSet()) {
					for (final Enrolment enrolment : studentCurricularPlan.getEnrolmentsSet()) {
					    final ExecutionSemester executionSemester = enrolment.getExecutionPeriod();
					    if (executionSemester.getExecutionYear() == executionYear) {
						enrolmentCounter++;
						if (enrolment.isApproved()) {
						    aprovalCounter++;
						    final Grade grade = enrolment.getGrade();
						    if (grade.isNumeric()) {
							final double credits = enrolment.getEctsCredits().doubleValue();
							totalCredits += credits;
							bigDecimal = bigDecimal.add(grade.getNumericValue().multiply(new BigDecimal(credits)));
						    }
						}
					    }
					}
				    }
				}
			    }

			    final Row row = spreadsheet.addRow();
			    row.setCell(registration.getNumber().toString());
			    row.setCell(registration.getPerson().getGender().toLocalizedString());
			    row.setCell(registration.getAverage(executionYear));
			    row.setCell(bigDecimal.divide(new BigDecimal(totalCredits), 5, RoundingMode.HALF_UP));
			    row.setCell(Integer.toString(enrolmentCounter));
			    row.setCell(Integer.toString(aprovalCounter));
			    row.setCell(registration.getEntryGrade() != null ? registration.getEntryGrade().toString() : StringUtils.EMPTY);
			    final CandidacyInformationBean candidacyInformationBean = registration.getCandidacyInformationBean();
			    final Boolean dislocated = candidacyInformationBean.getDislocatedFromPermanentResidence();
			    final String dislocatedString = dislocated == null ? "" : (dislocated.booleanValue() ? "Deslocado" : "N�o Deslocado");
			    row.setCell(dislocatedString);
			}
		    }
		}
	    }
	}
    }

}
