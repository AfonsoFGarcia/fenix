package net.sourceforge.fenixedu.domain.careerWorkshop;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.joda.time.DateTime;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;

import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.spreadsheet.SheetData;
import pt.utl.ist.fenix.tools.spreadsheet.SpreadsheetBuilder;
import pt.utl.ist.fenix.tools.spreadsheet.WorkbookExportFormat;

public class CareerWorkshopApplicationEvent extends CareerWorkshopApplicationEvent_Base {

    public CareerWorkshopApplicationEvent(DateTime beginDate, DateTime endDate, String relatedInformation) {
	super();
	evaluateDatesConsistency(beginDate, endDate);
	setBeginDate(beginDate);
	setEndDate(endDate);
	setRelatedInformation(relatedInformation);
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public void evaluateDatesConsistency(DateTime beginDate, DateTime endDate) {
	if (beginDate == null || endDate == null)
	    throw new DomainException("error.careerWorkshop.creatingNewEvent: Invalid values for begin/end dates");
	if (!beginDate.isBefore(endDate))
	    throw new DomainException("error.careerWorkshop.creatingNewEvent: Inconsistent values for begin/end dates");
	if (isOverlapping(beginDate, endDate))
	    throw new DomainException("error.careerWorkshop.creatingNewEvent: New period overlaps existing period");
    }

    public void delete() {
	if (!getCareerWorkshopApplications().isEmpty())
	    throw new DomainException(
		    "error.careerWorkshop.deletingEvent: This event already have applications associated, therefore it cannot be destroyed.");
	removeSpreadsheet();
	setRootDomainObject(null);
	deleteDomainObject();
    }

    public CareerWorkshopSpreadsheet getApplications() {
	if (getLastUpdate() == null || getSpreadsheet() == null)
	    generateSpreadsheet();
	if (getLastUpdate().isAfter(getSpreadsheet().getUploadTime())) {
	    generateSpreadsheet();
	}
	return getSpreadsheet();
    }

    @Service
    public void generateSpreadsheet() {
	StringBuilder stringBuilder = new StringBuilder();
	stringBuilder.append("ISTCareerWorkshopsApplications-");
	stringBuilder.append(getLastUpdate().toString("ddMMyyyyhhmm"));
	stringBuilder.append(".csv");

	final SheetData<CareerWorkshopApplication> dataSheet = new SheetData<CareerWorkshopApplication>(
		getProcessedList()) {

	    @Override
	    protected void makeLine(CareerWorkshopApplication item) {
		DateTime timestamp = item.getSealStamp();

		Registration reg = null;
		for (Registration regIter : item.getStudent().getActiveRegistrationsIn(
			ExecutionSemester.readActualExecutionSemester())) {
		    if (regIter.getStudentCurricularPlan(ExecutionSemester.readActualExecutionSemester()).isSecondCycle()) {
			reg = regIter;
			break;
		    }
		}

		Integer registrationLength = 0;
		ExecutionYear bottom = reg.getIngressionYear().getPreviousExecutionYear();
		ExecutionYear yearIter = ExecutionYear.readCurrentExecutionYear();
		while (yearIter != bottom) {
		    if (reg.hasAnyActiveState(yearIter)) {
			registrationLength++;
		    }
		    yearIter = yearIter.getPreviousExecutionYear();
		}
		CareerWorkshopSessions[] sessionPreferences = item.getSessionPreferences();
		CareerWorkshopThemes[] themePreferences = item.getThemePreferences();

		addCell("Data de inscri��o", timestamp.toString("dd-MM-yyyy"));
		addCell("Hora de inscri��o", timestamp.toString("hh:mm"));
		addCell("N�mero aluno", item.getStudent().getNumber());
		addCell("Nome", item.getStudent().getName());
		addCell("Email", item.getStudent().getPerson().getDefaultEmailAddressValue());
		addCell("Curso", reg.getDegree().getSigla());
		addCell("Ano Curricular", reg.getCurricularYear());
		addCell("N�mero de inscri��es", registrationLength);
		for(int i = 0; i < sessionPreferences.length; i++) {
		    addCell(("Sessao"+(i+1)),sessionPreferences[i].getDescription());
		}		
		for(int i = 0; i < themePreferences.length; i++) {
		    addCell(("Tema"+(i+1)),themePreferences[i].getDescription());
		}
		
	    }

	};

	try {
	    ByteArrayOutputStream io = new ByteArrayOutputStream();
	    new SpreadsheetBuilder().addSheet(stringBuilder.toString(), dataSheet).build(WorkbookExportFormat.CSV, io);

	    setSpreadsheet(new CareerWorkshopSpreadsheet(stringBuilder.toString(), io.toByteArray()));
	} catch (IOException ioe) {
	    throw new DomainException("error.careerWorkshop.criticalFailureGeneratingTheSpreadsheetFile", ioe);
	}
    }

    public String getFormattedBeginDate() {
	return getBeginDate().toString("dd-MM-yyyy");
    }

    public String getFormattedEndDate() {
	return getEndDate().toString("dd-MM-yyyy");
    }

    private boolean isOverlapping(DateTime beginDate, DateTime endDate) {
	for (CareerWorkshopApplicationEvent each : RootDomainObject.getInstance().getCareerWorkshopApplicationEvents()) {
	    if ((!beginDate.isBefore(each.getBeginDate()) && !beginDate.isAfter(each.getEndDate()))
		    || (!endDate.isBefore(each.getBeginDate()) && !endDate.isAfter(each.getEndDate())))
		return true;
	}
	return false;
    }
    
    private List<CareerWorkshopApplication> getProcessedList() {
	List<CareerWorkshopApplication> processedApplications = new ArrayList<CareerWorkshopApplication>();
	for(CareerWorkshopApplication application : getCareerWorkshopApplications()) {
	    if(application.getSealStamp() != null) {
		processedApplications.add(application);
	    }
	}
	Collections.sort(processedApplications, new Comparator<CareerWorkshopApplication>() {

	    @Override
	    public int compare(CareerWorkshopApplication o1, CareerWorkshopApplication o2) {
		if(o1.getSealStamp().isBefore(o2.getSealStamp())) {
		    return -1;
		}
		if(o1.getSealStamp().isAfter(o2.getSealStamp())) {
		    return 1;
		}
		return 0;
	    }
	});
	return processedApplications;
    }

    public boolean isApplicationEventOpened() {
	DateTime today = new DateTime();
	return (today.isBefore(getBeginDate()) || today.isAfter(getEndDate())) ? false : true;
    }

    static public CareerWorkshopApplicationEvent getActualEvent() {
	for (CareerWorkshopApplicationEvent each : RootDomainObject.getInstance().getCareerWorkshopApplicationEvents()) {
	    if (each.isApplicationEventOpened())
		return each;
	}
	return null;
    }

}
