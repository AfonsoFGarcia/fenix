/*
 * Created on Jan 12, 2005
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.projectsManagement;

import net.sourceforge.fenixedu.domain.projectsManagement.IMovementReportLine;
import net.sourceforge.fenixedu.util.projectsManagement.ReportType;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

import pt.utl.ist.fenix.tools.util.excel.ExcelStyle;

/**
 * @author Susana Fernandes
 * 
 */
public class InfoMovementReportLine extends InfoReportLine {

    private String movementId;

    private Integer rubricId;

    private String type;

    private String date;

    private String description;

    private Double value;

    private Double tax;

    private Double total;

    public String getDate() {
	return date;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public String getMovementId() {
	return movementId;
    }

    public void setMovementId(String movementId) {
	this.movementId = movementId;
    }

    public Integer getRubricId() {
	return rubricId;
    }

    public void setRubricId(Integer rubricId) {
	this.rubricId = rubricId;
    }

    public Double getTax() {
	return tax;
    }

    public void setTax(Double tax) {
	this.tax = tax;
    }

    public Double getTotal() {
	return total;
    }

    public void setTotal(Double total) {
	this.total = total;
    }

    public String getType() {
	return type;
    }

    public void setType(String type) {
	this.type = type;
    }

    public Double getValue() {
	return value;
    }

    public void setValue(Double value) {
	this.value = value;
    }

    public void setDate(String date) {
	this.date = date;
    }

    public void copyFromDomain(IMovementReportLine movementReportLine) {
	if (movementReportLine != null) {
	    setMovementId(movementReportLine.getMovementId());
	    setRubricId(movementReportLine.getRubricId());
	    setType(movementReportLine.getType());
	    setDate(movementReportLine.getDate());
	    setDescription(movementReportLine.getDescription());
	    setValue(movementReportLine.getValue());
	    setTax(movementReportLine.getTax());
	    setTotal(new Double(getValue().doubleValue() + getTax().doubleValue()));
	}
    }

    public static InfoMovementReportLine newInfoFromDomain(IMovementReportLine movementReportLine) {
	InfoMovementReportLine infoMovementReportLine = null;
	if (movementReportLine != null) {
	    infoMovementReportLine = new InfoMovementReportLine();
	    infoMovementReportLine.copyFromDomain(movementReportLine);
	}
	return infoMovementReportLine;
    }

    @Override
    public Double getValue(int column) {
	switch (column) {
	case 5:
	    return getValue();
	case 6:
	    return getTax();
	case 7:
	    return getTotal();
	default:
	    return null;
	}
    }

    @Override
    public void getHeaderToExcel(HSSFSheet sheet, ExcelStyle excelStyle, ReportType reportType) {
	HSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);
	HSSFCell cell = row.createCell((short) 0);
	cell.setCellValue(getString("label.idMov"));
	cell.setCellStyle(excelStyle.getHeaderStyle());
	cell = row.createCell((short) 1);
	cell.setCellValue(getString("label.rubric"));
	cell.setCellStyle(excelStyle.getHeaderStyle());
	cell = row.createCell((short) 2);
	cell.setCellValue(getString("label.type"));
	cell.setCellStyle(excelStyle.getHeaderStyle());
	cell = row.createCell((short) 3);
	cell.setCellValue(getString("label.date"));
	cell.setCellStyle(excelStyle.getHeaderStyle());
	cell = row.createCell((short) 4);
	cell.setCellValue(getString("label.description"));
	cell.setCellStyle(excelStyle.getHeaderStyle());
	cell = row.createCell((short) 5);
	cell.setCellValue(getString("label.value"));
	cell.setCellStyle(excelStyle.getHeaderStyle());
	cell = row.createCell((short) 6);
	cell.setCellValue(getString("label.tax"));
	cell.setCellStyle(excelStyle.getHeaderStyle());
	cell = row.createCell((short) 7);
	cell.setCellValue(getString("label.total"));
	cell.setCellStyle(excelStyle.getHeaderStyle());
    }

    @Override
    public void getLineToExcel(HSSFSheet sheet, ExcelStyle excelStyle, ReportType reportType) {
	HSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);
	HSSFCell cell = row.createCell((short) 0);
	cell.setCellValue(getMovementId());
	cell.setCellStyle(excelStyle.getStringStyle());
	cell = row.createCell((short) 1);
	cell.setCellValue(Double.parseDouble(getRubricId().toString()));
	cell.setCellStyle(excelStyle.getIntegerStyle());
	cell = row.createCell((short) 2);
	cell.setCellValue(getType());
	cell.setCellStyle(excelStyle.getStringStyle());
	cell = row.createCell((short) 3);
	cell.setCellValue(getDate());
	cell.setCellStyle(excelStyle.getStringStyle());
	cell = row.createCell((short) 4);
	cell.setCellValue(getDescription());
	cell.setCellStyle(excelStyle.getStringStyle());
	cell = row.createCell((short) 5);
	cell.setCellValue(getValue().doubleValue());
	if (getValue().doubleValue() < 0)
	    cell.setCellStyle(excelStyle.getDoubleNegativeStyle());
	else
	    cell.setCellStyle(excelStyle.getDoubleStyle());
	cell = row.createCell((short) 6);
	cell.setCellValue(getTax().doubleValue());
	if (getTax().doubleValue() < 0)
	    cell.setCellStyle(excelStyle.getDoubleNegativeStyle());
	else
	    cell.setCellStyle(excelStyle.getDoubleStyle());
	cell = row.createCell((short) 7);
	double total = getValue().doubleValue() + getTax().doubleValue();
	cell.setCellValue(total);
	if (total < 0)
	    cell.setCellStyle(excelStyle.getDoubleNegativeStyle());
	else
	    cell.setCellStyle(excelStyle.getDoubleStyle());
    }

    @Override
    public int getNumberOfColumns() {
	return 7;
    }

}
