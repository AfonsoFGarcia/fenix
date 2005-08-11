/*
 * Created on Jan 12, 2005
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.projectsManagement;

import net.sourceforge.fenixedu.domain.projectsManagement.IRevenueReportLine;
import net.sourceforge.fenixedu.util.projectsManagement.ExcelStyle;
import net.sourceforge.fenixedu.util.projectsManagement.ReportType;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.util.CellReference;

/**
 * @author Susana Fernandes
 * 
 */
public class InfoRevenueReportLine extends InfoReportLine {
    private Integer projectCode;

    private String movementId;

    private String financialEntity;

    private Integer rubric;

    private String date;

    private String description;

    private Double value;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFinancialEntity() {
        return financialEntity;
    }

    public void setFinancialEntity(String financialEntity) {
        this.financialEntity = financialEntity;
    }

    public String getMovementId() {
        return movementId;
    }

    public void setMovementId(String movementId) {
        this.movementId = movementId;
    }

    public Integer getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(Integer projectCode) {
        this.projectCode = projectCode;
    }

    public Integer getRubric() {
        return rubric;
    }

    public void setRubric(Integer rubric) {
        this.rubric = rubric;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public void copyFromDomain(IRevenueReportLine revenueReportLine) {
        if (revenueReportLine != null) {
            setProjectCode(revenueReportLine.getProjectCode());
            setMovementId(revenueReportLine.getMovementId());
            setFinancialEntity(revenueReportLine.getFinancialEntity());
            setRubric(revenueReportLine.getRubric());
            setDate(revenueReportLine.getDate());
            setDescription(revenueReportLine.getDescription());
            setValue(revenueReportLine.getValue());
        }
    }

    public static InfoRevenueReportLine newInfoFromDomain(IRevenueReportLine revenueReportLine) {
        InfoRevenueReportLine infoRevenueReportLine = null;
        if (revenueReportLine != null) {
            infoRevenueReportLine = new InfoRevenueReportLine();
            infoRevenueReportLine.copyFromDomain(revenueReportLine);
        }
        return infoRevenueReportLine;
    }

    public int getNumberOfColumns() {
        return 5;
    }

    public void getHeaderToExcel(HSSFSheet sheet, ExcelStyle excelStyle, ReportType reportType) {
        HSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);
        HSSFCell cell = row.createCell((short) 0);
        cell.setCellValue(getString("label.idMov"));
        cell.setCellStyle(excelStyle.getHeaderStyle());
        cell = row.createCell((short) 1);
        cell.setCellValue(getString("label.financialEntity"));
        cell.setCellStyle(excelStyle.getHeaderStyle());
        cell = row.createCell((short) 2);
        cell.setCellValue(getString("label.rubric"));
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
    }

    public void getLineToExcel(HSSFSheet sheet, ExcelStyle excelStyle, ReportType reportType) {
        HSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);
        HSSFCell cell = row.createCell((short) 0);
        cell.setCellValue(getMovementId());
        cell.setCellStyle(excelStyle.getStringStyle());
        cell = row.createCell((short) 1);
        cell.setCellValue(getFinancialEntity());
        cell.setCellStyle(excelStyle.getStringStyle());
        cell = row.createCell((short) 2);
        cell.setCellValue(Double.parseDouble(getRubric().toString()));
        cell.setCellStyle(excelStyle.getIntegerStyle());
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
    }

    public void getTotalLineToExcel(HSSFSheet sheet, ExcelStyle excelStyle, ReportType reportType) {
        HSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);
        HSSFCell cell = row.createCell((short) 0);
        cell.setCellStyle(excelStyle.getStringStyle());
        cell.setCellValue(getString("label.total"));
        CellReference cellRef1 = new CellReference(1, 5);
        CellReference cellRef2 = new CellReference(((short) row.getRowNum() - 1), 5);
        cell = row.createCell((short) 5);
        cell.setCellStyle(excelStyle.getDoubleStyle());
        cell.setCellFormula("sum(" + cellRef1.toString() + ":" + cellRef2.toString() + ")");
    }

    public Double getValue(int column) {
        switch (column) {
        case 5:
            return getValue();
        default:
            return null;
        }
    }
}
