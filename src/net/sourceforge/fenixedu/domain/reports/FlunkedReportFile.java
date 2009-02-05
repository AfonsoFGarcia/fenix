package net.sourceforge.fenixedu.domain.reports;

import pt.ist.fenixWebFramework.services.Service;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;
import net.sourceforge.fenixedu.util.report.Spreadsheet;
import net.sourceforge.fenixedu.util.report.Spreadsheet.Row;

public class FlunkedReportFile extends FlunkedReportFile_Base {
    
    public  FlunkedReportFile() {
        super();
    }
    
    public String getJobName() {
	return "Listagem de prescri��es";
    }
    
    protected String getPrefix() {
	return "prescricoes";
    }
    
    @Service
    public static GepReportFile newInstance(String type, DegreeType degreeType, ExecutionYear executionYear) {
	FlunkedReportFile flunkedReportFile = new FlunkedReportFile();
	flunkedReportFile.setType(type);
	flunkedReportFile.setDegreeType(degreeType);
	flunkedReportFile.setExecutionYear(executionYear);
	return flunkedReportFile;
    }
    
    
    public void renderReport(Spreadsheet spreadsheet) {
	spreadsheet.setHeader("n�mero aluno");
	setDegreeHeaders(spreadsheet);

	for (final Degree degree : Degree.readNotEmptyDegrees()) {
	    if (checkDegreeType(getDegreeType(), degree)) {
		for (final Registration registration : degree.getRegistrationsSet()) {
		    for (final RegistrationState registrationState : registration.getRegistrationStates()) {
			final RegistrationStateType registrationStateType = registrationState.getStateType();
			if (registrationStateType == RegistrationStateType.FLUNKED
				&& registrationState.getExecutionYear() == getExecutionYear()) {
			    final Row row = spreadsheet.addRow();
			    row.setCell(registration.getNumber());
			    setDegreeCells(row, degree);
			}
		    }
		}
	    }
	}
    }
    
}
