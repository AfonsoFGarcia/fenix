/*
 * Created on Jan 12, 2005
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.projectsManagement;

import java.text.SimpleDateFormat;
import java.util.Date;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.util.projectsManagement.ReportType;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import pt.utl.ist.fenix.tools.util.excel.ExcelStyle;

/**
 * @author Susana Fernandes
 * 
 */
public class InfoCoordinatorReport extends InfoReport {

    private InfoRubric infoCoordinator;

    public InfoRubric getInfoCoordinator() {
	return infoCoordinator;
    }

    public void setInfoCoordinator(InfoRubric infoCoordinator) {
	this.infoCoordinator = infoCoordinator;
    }

    public void getReportToExcel(IUserView userView, HSSFWorkbook wb, ReportType reportType) {
	HSSFSheet sheet = wb.createSheet(infoCoordinator.getCode());
	sheet.setGridsPrinted(false);
	ExcelStyle excelStyle = new ExcelStyle(wb);
	HSSFRow row = sheet.createRow((short) 0);
	HSSFCell cell = row.createCell((short) 0);
	cell.setCellValue(reportType.getReportLabel());
	cell.setCellStyle(excelStyle.getTitleStyle());

	row = sheet.createRow((short) 2);
	cell = row.createCell((short) 0);
	cell.setCellValue(getString("label.coordinator") + ":");
	cell.setCellStyle(excelStyle.getLabelStyle());
	cell = row.createCell((short) 1);
	cell.setCellValue(infoCoordinator.getDescription());
	cell.setCellStyle(excelStyle.getValueStyle());
	row = sheet.createRow((short) 3);
	cell = row.createCell((short) 0);
	cell.setCellValue(getString("label.date") + ":");
	cell.setCellStyle(excelStyle.getLabelStyle());
	cell = row.createCell((short) 1);
	SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy '�s' HH:mm");
	cell.setCellValue(formatter.format(new Date()));
	cell.setCellStyle(excelStyle.getValueStyle());

	getReportToExcel(sheet, excelStyle, reportType);
    }
}
