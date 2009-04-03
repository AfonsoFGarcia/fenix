package net.sourceforge.fenixedu.domain.reports;

import java.util.Collections;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.candidacy.Ingression;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

public class RegistrationReportFile extends RegistrationReportFile_Base {

    public RegistrationReportFile() {
	super();
    }

    @Override
    public String getJobName() {
	return "Listagem de matr�culas";
    }

    @Override
    protected String getPrefix() {
	return "matriculas";
    }

    @Override
    public void renderReport(Spreadsheet spreadsheet) throws Exception {
	spreadsheet.setHeader("n�mero aluno");
	setDegreeHeaders(spreadsheet);
	spreadsheet.setHeader("c�digo regime de ingresso na matr�cula");
	spreadsheet.setHeader("regime de ingresso na matr�cula");
	spreadsheet.setHeader("ano l�ctivo de in�cio da matr�cula");
	spreadsheet.setHeader("c�digo regime de ingresso na escola");
	spreadsheet.setHeader("regime de ingresso na escola");
	spreadsheet.setHeader("ano l�ctivo de ingresso na escola");
	spreadsheet.setHeader("tipo de aluno");
	spreadsheet.setHeader("ano curricular");

	for (final Degree degree : Degree.readNotEmptyDegrees()) {
	    if (checkDegreeType(getDegreeType(), degree)) {
		if (isActive(degree)) {
		    for (final Registration registration : degree.getRegistrationsSet()) {
			if (registration.isRegistered(getExecutionYear())) {
			    final Row row = spreadsheet.addRow();
			    row.setCell(registration.getNumber());
			    setDegreeCells(row, degree);

			    reportIngression(row, registration);

			    final Registration firstRegistration = findFirstRegistration(registration.getStudent());
			    reportIngression(row, firstRegistration);

			    if (registration.getRegistrationAgreement() != null) {
				row.setCell(registration.getRegistrationAgreement().getName());
			    } else {
				row.setCell("");
			    }
			    row.setCell(Integer.toString(registration.getCurricularYear(getExecutionYear())));
			}
		    }
		}
	    }
	}
    }

    private Registration findFirstRegistration(final Student student) {
	return Collections.min(student.getRegistrationsSet(), Registration.COMPARATOR_BY_START_DATE);
    }

    private void reportIngression(final Row row, final Registration registration) {
	final Ingression ingression = registration.getIngression();
	if (ingression != null) {
	    row.setCell(ingression.getName());
	    row.setCell(ingression.getDescription());
	} else {
	    row.setCell("");
	    row.setCell("");
	}
	row.setCell(registration.getStartExecutionYear().getYear());
    }

}
