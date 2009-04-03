/*
 * Created on Jan 12, 2005
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.projectsManagement;

import net.sourceforge.fenixedu.domain.projectsManagement.IProjectBudgetaryBalanceReportLine;
import net.sourceforge.fenixedu.util.projectsManagement.ReportType;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

import pt.utl.ist.fenix.tools.util.excel.ExcelStyle;

/**
 * @author Susana Fernandes
 * 
 */
public class InfoProjectBudgetaryBalanceReportLine extends InfoReportLine {
    private Integer rubric;

    private String rubricDescription;

    private Double budget;

    private Double executed;

    private Double balance;

    public Double getBalance() {
	return balance;
    }

    public void setBalance(Double balance) {
	this.balance = balance;
    }

    public Double getBudget() {
	return budget;
    }

    public void setBudget(Double budget) {
	this.budget = budget;
    }

    public Double getExecuted() {
	return executed;
    }

    public void setExecuted(Double executed) {
	this.executed = executed;
    }

    public Integer getRubric() {
	return rubric;
    }

    public void setRubric(Integer rubric) {
	this.rubric = rubric;
    }

    public String getRubricDescription() {
	return rubricDescription;
    }

    public void setRubricDescription(String rubricDescription) {
	this.rubricDescription = rubricDescription;
    }

    public void copyFromDomain(IProjectBudgetaryBalanceReportLine budgetaryBalanceReportLine) {
	if (budgetaryBalanceReportLine != null) {
	    setRubric(budgetaryBalanceReportLine.getRubric());
	    setRubricDescription(budgetaryBalanceReportLine.getRubricDescription());
	    setBudget(budgetaryBalanceReportLine.getBudget());
	    setExecuted(budgetaryBalanceReportLine.getExecuted());
	    setBalance(budgetaryBalanceReportLine.getBalance());

	}
    }

    public static InfoProjectBudgetaryBalanceReportLine newInfoFromDomain(
	    IProjectBudgetaryBalanceReportLine budgetaryBalanceReportLine) {
	InfoProjectBudgetaryBalanceReportLine infoBudgetaryBalanceReportLine = null;
	if (budgetaryBalanceReportLine != null) {
	    infoBudgetaryBalanceReportLine = new InfoProjectBudgetaryBalanceReportLine();
	    infoBudgetaryBalanceReportLine.copyFromDomain(budgetaryBalanceReportLine);
	}
	return infoBudgetaryBalanceReportLine;
    }

    @Override
    public int getNumberOfColumns() {
	return 5;
    }

    @Override
    public void getHeaderToExcel(HSSFSheet sheet, ExcelStyle excelStyle, ReportType reportType) {
	HSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);
	HSSFCell cell = row.createCell((short) 0);
	cell.setCellValue(getString("label.rubric"));
	cell.setCellStyle(excelStyle.getHeaderStyle());
	cell = row.createCell((short) 1);
	cell.setCellValue(getString("label.name"));
	cell.setCellStyle(excelStyle.getHeaderStyle());
	cell = row.createCell((short) 2);
	cell.setCellValue(getString("label.budget"));
	cell.setCellStyle(excelStyle.getHeaderStyle());
	cell = row.createCell((short) 3);
	cell.setCellValue(getString("label.executed"));
	cell.setCellStyle(excelStyle.getHeaderStyle());
	cell = row.createCell((short) 4);
	cell.setCellValue(getString("label.balance"));
	cell.setCellStyle(excelStyle.getHeaderStyle());
    }

    @Override
    public void getLineToExcel(HSSFSheet sheet, ExcelStyle excelStyle, ReportType reportType) {
	HSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);
	HSSFCell cell = row.createCell((short) 0);
	cell.setCellValue(getRubric());
	cell.setCellStyle(excelStyle.getStringStyle());
	cell = row.createCell((short) 1);
	cell.setCellValue(getRubricDescription());
	cell.setCellStyle(excelStyle.getStringStyle());
	cell = row.createCell((short) 2);
	cell.setCellValue(getBudget().doubleValue());
	if (getBudget().doubleValue() < 0)
	    cell.setCellStyle(excelStyle.getDoubleNegativeStyle());
	else
	    cell.setCellStyle(excelStyle.getDoubleStyle());
	cell = row.createCell((short) 3);
	cell.setCellValue(getExecuted().doubleValue());
	if (getExecuted().doubleValue() < 0)
	    cell.setCellStyle(excelStyle.getDoubleNegativeStyle());
	else
	    cell.setCellStyle(excelStyle.getDoubleStyle());
	cell = row.createCell((short) 4);
	cell.setCellValue(getBalance().doubleValue());
	if (getBalance().doubleValue() < 0)
	    cell.setCellStyle(excelStyle.getDoubleNegativeStyle());
	else
	    cell.setCellStyle(excelStyle.getDoubleStyle());
    }

}
