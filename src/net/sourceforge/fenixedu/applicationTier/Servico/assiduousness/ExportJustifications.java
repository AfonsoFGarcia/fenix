package net.sourceforge.fenixedu.applicationTier.Servico.assiduousness;

import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.AssiduousnessExportChoices;
import net.sourceforge.fenixedu.domain.assiduousness.Assiduousness;
import net.sourceforge.fenixedu.domain.assiduousness.Justification;
import net.sourceforge.fenixedu.domain.assiduousness.Leave;
import net.sourceforge.fenixedu.domain.assiduousness.util.JustificationType;
import net.sourceforge.fenixedu.util.LanguageUtils;
import net.sourceforge.fenixedu.util.report.StyledExcelSpreadsheet;

import org.joda.time.YearMonthDay;

public class ExportJustifications extends Service {

    public StyledExcelSpreadsheet run(AssiduousnessExportChoices assiduousnessExportChoices) {
	final ResourceBundle bundle = ResourceBundle.getBundle("resources.AssiduousnessResources",
		LanguageUtils.getLocale());
	final ResourceBundle enumBundle = ResourceBundle.getBundle("resources.EnumerationResources",
		LanguageUtils.getLocale());
	StyledExcelSpreadsheet spreadsheet = new StyledExcelSpreadsheet(bundle
		.getString("link.justifications"), false);
	HashMap<Assiduousness, List<Justification>> justificationsMap = assiduousnessExportChoices
		.getAllJustificationMap();
	spreadsheet.newHeaderRow();
	spreadsheet.addHeader(bundle.getString("label.employeeNumber"));
	spreadsheet.addHeader(bundle.getString("label.beginDate"));
	spreadsheet.addHeader(bundle.getString("label.endDateOrDuration"));
	spreadsheet.addHeader(bundle.getString("label.acronym"));
	spreadsheet.addHeader(bundle.getString("label.type"));
	spreadsheet.addHeader(bundle.getString("label.beginDate"));
	spreadsheet.addHeader(bundle.getString("label.endDate"));
	for (Assiduousness assiduousness : rootDomainObject.getAssiduousnesss()) {
	    if (assiduousnessExportChoices.satisfiedAll(assiduousness)) {
		List<Justification> justificationList = justificationsMap.get(assiduousness);
		if (justificationList != null) {
		    for (Justification justification : justificationList) {
			if (justification.isLeave()) {
			    spreadsheet.newRow();
			    spreadsheet.addCell(justification.getAssiduousness().getEmployee()
				    .getEmployeeNumber().toString());
			    if (justification.getJustificationMotive().getJustificationType() == JustificationType.TIME) {
				spreadsheet.addDateTimeCell(justification.getDate());
				spreadsheet.addDateTimeCell(((Leave) justification).getEndDate());
				if (justification.getJustificationMotive().getJustificationType() == JustificationType.BALANCE) {
				    spreadsheet.addDateCell(justification.getDate().toYearMonthDay());
				    spreadsheet.addTimeCell(((Leave) justification).getEndDate()
					    .toTimeOfDay());
				}
			    } else {
				spreadsheet.addDateCell(justification.getDate().toYearMonthDay());
				spreadsheet.addDateCell(((Leave) justification).getEndDate()
					.toYearMonthDay());
			    }
			    spreadsheet.addCell(justification.getJustificationMotive().getAcronym());
			    spreadsheet.addCell(enumBundle.getString(justification
				    .getJustificationMotive().getJustificationType().toString()));
			    YearMonthDay begin = assiduousnessExportChoices.getBeginDate().isAfter(
				    justification.getDate().toYearMonthDay()) ? assiduousnessExportChoices
				    .getBeginDate()
				    : justification.getDate().toYearMonthDay();
			    YearMonthDay justificationDate = ((Leave) justification)
				    .getEndYearMonthDay() == null ? ((Leave) justification).getDate()
				    .toYearMonthDay() : ((Leave) justification).getEndYearMonthDay();
			    YearMonthDay end = assiduousnessExportChoices.getEndDate().isBefore(
				    justificationDate) ? assiduousnessExportChoices.getEndDate()
				    : justificationDate;
			    spreadsheet.addDateCell(begin);
			    spreadsheet.addDateCell(end);
			} else if (justification.isMissingClocking()) {
			    spreadsheet.newRow();
			    spreadsheet.addCell(justification.getAssiduousness().getEmployee()
				    .getEmployeeNumber().toString());
			    spreadsheet.addDateTimeCell(justification.getDate());
			    spreadsheet.addCell("");
			    spreadsheet.addCell(justification.getJustificationMotive().getAcronym());
			}
		    }
		}
	    }
	}
	return spreadsheet;
    }
}