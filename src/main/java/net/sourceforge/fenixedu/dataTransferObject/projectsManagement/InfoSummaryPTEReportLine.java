/*
 * Created on Jan 12, 2005
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.projectsManagement;

import net.sourceforge.fenixedu.domain.projectsManagement.ISummaryPTEReportLine;
import net.sourceforge.fenixedu.util.projectsManagement.ReportType;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

import pt.utl.ist.fenix.tools.util.StringAppender;
import pt.utl.ist.fenix.tools.util.excel.ExcelStyle;

/**
 * @author Susana Fernandes
 * 
 */
public class InfoSummaryPTEReportLine extends InfoReportLine {

    private String projectCode;

    private Double revenue;

    private Double expense;

    private Double tax;

    private Double total;

    public Double getExpense() {
        return expense;
    }

    public void setExpense(Double expense) {
        this.expense = expense;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public Double getRevenue() {
        return revenue;
    }

    public void setRevenue(Double revenue) {
        this.revenue = revenue;
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

    public void copyFromDomain(ISummaryPTEReportLine summaryPTEReportLine) {
        if (summaryPTEReportLine != null) {
            setProjectCode(summaryPTEReportLine.getProjectCode());
            setExpense(summaryPTEReportLine.getExpense());
            setRevenue(summaryPTEReportLine.getRevenue());
            setTax(summaryPTEReportLine.getTax());
            setTotal(summaryPTEReportLine.getTotal());
        }
    }

    public static InfoSummaryPTEReportLine newInfoFromDomain(ISummaryPTEReportLine summaryPTEReportLine) {
        InfoSummaryPTEReportLine infoSummaryPTEReportLine = null;
        if (summaryPTEReportLine != null) {
            infoSummaryPTEReportLine = new InfoSummaryPTEReportLine();
            infoSummaryPTEReportLine.copyFromDomain(summaryPTEReportLine);
        }
        return infoSummaryPTEReportLine;
    }

    @Override
    public void getLineToExcel(HSSFSheet sheet, ExcelStyle excelStyle, ReportType reportType) {
        int nextRow = sheet.getLastRowNum() + 2;
        HSSFRow row = sheet.createRow(nextRow);
        HSSFCell cell = row.createCell((short) 0);
        cell.setCellValue(StringAppender.append(getString("link.revenue"), " ", getString("label.pte"), ":"));
        cell.setCellStyle(excelStyle.getLabelStyle());
        // sheet.addMergedRegion(new Region((short) row.getRowNum(), (short) 0,
        // (short) row.getRowNum(), (short) 2));
        cell = row.createCell((short) 3);
        cell.setCellValue(getRevenue().doubleValue());
        if (getRevenue().doubleValue() < 0) {
            cell.setCellStyle(excelStyle.getDoubleNegativeStyle());
        } else {
            cell.setCellStyle(excelStyle.getDoubleStyle());
        }
        nextRow++;
        row = sheet.createRow(nextRow);
        cell = row.createCell((short) 0);
        cell.setCellValue(StringAppender.append(getString("link.expenses"), " ", getString("label.pte"), ":"));
        // sheet.addMergedRegion(new Region((short) row.getRowNum(), (short) 0,
        // (short) row.getRowNum(), (short) 2));
        cell.setCellStyle(excelStyle.getLabelStyle());
        cell = row.createCell((short) 3);
        cell.setCellValue(getExpense().doubleValue());
        if (getExpense().doubleValue() < 0) {
            cell.setCellStyle(excelStyle.getDoubleNegativeStyle());
        } else {
            cell.setCellStyle(excelStyle.getDoubleStyle());
        }
        nextRow++;
        row = sheet.createRow(nextRow);
        cell = row.createCell((short) 0);
        cell.setCellValue(StringAppender.append(getString("label.tax"), " ", getString("label.pte"), ":"));
        // sheet.addMergedRegion(new Region((short) row.getRowNum(), (short) 0,
        // (short) row.getRowNum(), (short) 2));
        cell.setCellStyle(excelStyle.getLabelStyle());
        cell = row.createCell((short) 3);
        cell.setCellValue(getTax().doubleValue());
        if (getTax().doubleValue() < 0) {
            cell.setCellStyle(excelStyle.getDoubleNegativeStyle());
        } else {
            cell.setCellStyle(excelStyle.getDoubleStyle());
        }
        row = sheet.createRow(nextRow++);
        cell = row.createCell((short) 3);
        cell.setCellValue(getTotal().doubleValue());
        if (getTotal().doubleValue() < 0) {
            cell.setCellStyle(excelStyle.getDoubleNegativeStyle());
        } else {
            cell.setCellStyle(excelStyle.getDoubleStyle());
        }
    }

}
