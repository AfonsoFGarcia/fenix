/*
 * Created on Jan 12, 2005
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.projectsManagement;

import net.sourceforge.fenixedu.dataTransferObject.DataTranferObject;
import net.sourceforge.fenixedu.domain.projectsManagement.ISummaryEURReportLine;
import net.sourceforge.fenixedu.util.projectsManagement.ExcelStyle;
import net.sourceforge.fenixedu.util.projectsManagement.ReportType;

import org.apache.poi.hssf.usermodel.HSSFSheet;

/**
 * @author Susana Fernandes
 * 
 */
public class InfoSummaryEURReportLine extends DataTranferObject implements IReportLine {

    private Integer projectCode;

    private Double revenue;

    private Double expense;

    private Double tax;

    private Double adiantamentosPorJustificar;

    private Double total;

    public Double getAdiantamentosPorJustificar() {
        return adiantamentosPorJustificar;
    }

    public void setAdiantamentosPorJustificar(Double adiantamentosPorJustificar) {
        this.adiantamentosPorJustificar = adiantamentosPorJustificar;
    }

    public Double getExpense() {
        return expense;
    }

    public void setExpense(Double expense) {
        this.expense = expense;
    }

    public Integer getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(Integer projectCode) {
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

    public void copyFromDomain(ISummaryEURReportLine summaryEURReportLine) {
        if (summaryEURReportLine != null) {
            setProjectCode(summaryEURReportLine.getProjectCode());
            setExpense(summaryEURReportLine.getExpense());
            setRevenue(summaryEURReportLine.getRevenue());
            setTax(summaryEURReportLine.getTax());
            setAdiantamentosPorJustificar(summaryEURReportLine.getAdiantamentosPorJustificar());
            setTotal(summaryEURReportLine.getTotal());
        }
    }

    public static InfoSummaryEURReportLine newInfoFromDomain(ISummaryEURReportLine summaryEURReportLine) {
        InfoSummaryEURReportLine infoSummaryEURReportLine = null;
        if (summaryEURReportLine != null) {
            infoSummaryEURReportLine = new InfoSummaryEURReportLine();
            infoSummaryEURReportLine.copyFromDomain(summaryEURReportLine);
        }
        return infoSummaryEURReportLine;
    }

    public Double getValue(int column) {
        return null;
    }

    public void getHeaderToExcel(HSSFSheet sheet, ExcelStyle excelStyle, ReportType reportType) {
    }

    public void getLineToExcel(HSSFSheet sheet, ExcelStyle excelStyle, ReportType reportType) {
    }

    public void getTotalLineToExcel(HSSFSheet sheet, ExcelStyle excelStyle, ReportType reportType) {
    }

    public int getNumberOfColumns() {
        return 0;
    }

}
