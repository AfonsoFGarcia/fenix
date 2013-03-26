/*
 * Created on Jan 12, 2005
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.projectsManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.projectsManagement.IMovementReport;
import net.sourceforge.fenixedu.domain.projectsManagement.IMovementReportLine;
import net.sourceforge.fenixedu.util.projectsManagement.FormatDouble;
import net.sourceforge.fenixedu.util.projectsManagement.ReportType;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.hssf.util.Region;

import pt.utl.ist.fenix.tools.util.excel.ExcelStyle;

/**
 * @author Susana Fernandes
 * 
 */
public class InfoMovementReport extends InfoReportLine {

    private String parentMovementId;

    private String parentProjectCode;

    private Integer parentRubricId;

    private String parentType;

    private String parentDate;

    private String parentDescription;

    private Double parentValue;

    private List movements;

    public List getMovements() {
        return movements;
    }

    public void setMovements(List movements) {
        this.movements = movements;
    }

    public String getParentDate() {
        return parentDate;
    }

    public void setParentDate(String parentDate) {
        this.parentDate = parentDate;
    }

    public String getParentDescription() {
        return parentDescription;
    }

    public void setParentDescription(String parentDescription) {
        this.parentDescription = parentDescription;
    }

    public String getParentMovementId() {
        return parentMovementId;
    }

    public void setParentMovementId(String parentMovementId) {
        this.parentMovementId = parentMovementId;
    }

    public String getParentProjectCode() {
        return parentProjectCode;
    }

    public void setParentProjectCode(String parentProjectCode) {
        this.parentProjectCode = parentProjectCode;
    }

    public Integer getParentRubricId() {
        return parentRubricId;
    }

    public void setParentRubricId(Integer parentRubricId) {
        this.parentRubricId = parentRubricId;
    }

    public String getParentType() {
        return parentType;
    }

    public void setParentType(String parentType) {
        this.parentType = parentType;
    }

    public Double getParentValue() {
        return parentValue;
    }

    public void setParentValue(Double parentValue) {
        this.parentValue = parentValue;
    }

    public void copyFromDomain(IMovementReport movementReport) {
        if (movementReport != null) {
            setParentMovementId(movementReport.getParentMovementId());
            setParentProjectCode(movementReport.getParentProjectCode());
            setParentRubricId(movementReport.getParentRubricId());
            setParentType(movementReport.getParentType());
            setParentDate(movementReport.getParentDate());
            setParentDescription(movementReport.getParentDescription());
            setParentValue(movementReport.getParentValue());
            if (movementReport.getMovements() != null) {
                List infoMovementResportLineList = new ArrayList();
                for (int i = 0; i < movementReport.getMovements().size(); i++) {
                    infoMovementResportLineList.add(InfoMovementReportLine.newInfoFromDomain((IMovementReportLine) movementReport
                            .getMovements().get(i)));
                }
                setMovements(infoMovementResportLineList);
            }
        }
    }

    public static InfoMovementReport newInfoFromDomain(IMovementReport movementReport) {
        InfoMovementReport infoMovementReport = null;
        if (movementReport != null) {
            infoMovementReport = new InfoMovementReport();
            infoMovementReport.copyFromDomain(movementReport);
        }
        return infoMovementReport;
    }

    @Override
    public Double getValue(int column) {
        return null;
    }

    @Override
    public void getHeaderToExcel(HSSFSheet sheet, ExcelStyle excelStyle, ReportType reportType) {
    }

    @Override
    public void getLineToExcel(HSSFSheet sheet, ExcelStyle excelStyle, ReportType reportType) {
        HSSFRow row = sheet.createRow(sheet.getLastRowNum() + 2);
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
        cell.setCellStyle(excelStyle.getHeaderStyle());
        cell = row.createCell((short) 6);
        cell.setCellStyle(excelStyle.getHeaderStyle());
        cell = row.createCell((short) 7);
        cell.setCellStyle(excelStyle.getHeaderStyle());
        sheet.addMergedRegion(new Region(row.getRowNum(), (short) 4, row.getRowNum(), (short) 7));
        cell = row.createCell((short) 8);
        cell.setCellValue(getString("label.total"));
        cell.setCellStyle(excelStyle.getHeaderStyle());

        row = sheet.createRow(sheet.getLastRowNum() + 1);
        cell = row.createCell((short) 0);
        cell.setCellValue(getParentMovementId());
        cell.setCellStyle(excelStyle.getStringStyle());
        cell = row.createCell((short) 1);
        cell.setCellValue(Double.parseDouble(getParentRubricId().toString()));
        cell.setCellStyle(excelStyle.getIntegerStyle());
        cell = row.createCell((short) 2);
        cell.setCellValue(getParentType());
        cell.setCellStyle(excelStyle.getStringStyle());
        cell = row.createCell((short) 3);
        cell.setCellValue(getParentDate());
        cell.setCellStyle(excelStyle.getStringStyle());
        cell = row.createCell((short) 4);
        cell.setCellValue(getParentDescription());
        cell.setCellStyle(excelStyle.getStringStyle());
        cell = row.createCell((short) 5);
        cell.setCellStyle(excelStyle.getStringStyle());
        cell = row.createCell((short) 6);
        cell.setCellStyle(excelStyle.getStringStyle());
        cell = row.createCell((short) 7);
        cell.setCellStyle(excelStyle.getStringStyle());
        sheet.addMergedRegion(new Region(row.getRowNum(), (short) 4, row.getRowNum(), (short) 7));
        cell = row.createCell((short) 8);
        cell.setCellValue(getParentValue().doubleValue());
        if (getParentValue().doubleValue() < 0) {
            cell.setCellStyle(excelStyle.getDoubleNegativeStyle());
        } else {
            cell.setCellStyle(excelStyle.getDoubleStyle());
        }
        double totalJustified = 0;
        if (movements.size() != 0) {
            row = sheet.createRow(sheet.getLastRowNum() + 1);
            ((IReportLine) movements.get(0)).getHeaderToExcel(sheet, excelStyle, reportType);
            for (int i = 0; i < movements.size(); i++) {
                ((IReportLine) movements.get(i)).getLineToExcel(sheet, excelStyle, reportType);
                totalJustified =
                        FormatDouble.round(totalJustified + ((InfoMovementReportLine) movements.get(i)).getTotal().doubleValue());
            }
        }
        row = sheet.createRow(sheet.getLastRowNum() + 2);
        cell = row.createCell((short) 0);
        cell.setCellValue(getString("label.total") + ":");
        cell.setCellStyle(excelStyle.getLabelStyle());
        sheet.addMergedRegion(new Region(row.getRowNum(), (short) 0, row.getRowNum(), (short) 2));
        cell = row.createCell((short) 3);
        cell.setCellValue(getParentValue().doubleValue());
        if (getParentValue().doubleValue() <= 0) {
            cell.setCellStyle(excelStyle.getDoubleNegativeStyle());
        } else {
            cell.setCellStyle(excelStyle.getDoubleStyle());
        }
        row = sheet.createRow(sheet.getLastRowNum() + 1);
        cell = row.createCell((short) 0);
        cell.setCellValue(getString("label.totalExecuted"));
        cell.setCellStyle(excelStyle.getLabelStyle());
        sheet.addMergedRegion(new Region(row.getRowNum(), (short) 0, row.getRowNum(), (short) 2));
        cell = row.createCell((short) 3);
        cell.setCellValue(totalJustified);
        if (totalJustified < 0) {
            cell.setCellStyle(excelStyle.getDoubleNegativeStyle());
        } else {
            cell.setCellStyle(excelStyle.getDoubleStyle());
        }
        row = sheet.createRow(sheet.getLastRowNum() + 1);
        cell = row.createCell((short) 0);
        cell.setCellValue(getString("label.forExecuted"));
        cell.setCellStyle(excelStyle.getLabelStyle());
        sheet.addMergedRegion(new Region(row.getRowNum(), (short) 0, row.getRowNum(), (short) 2));
        cell = row.createCell((short) 3);
        double forJustify = FormatDouble.round(getParentValue().doubleValue() - totalJustified);
        cell.setCellValue(forJustify);
        if (forJustify < 0) {
            cell.setCellStyle(excelStyle.getDoubleNegativeStyle());
        } else {
            cell.setCellStyle(excelStyle.getDoubleStyle());
        }
        row = sheet.createRow(sheet.getLastRowNum() + 1);
    }

    @Override
    public void getTotalLineToExcel(HSSFSheet sheet, ExcelStyle excelStyle, ReportType reportType) {
        HSSFRow row = sheet.createRow(sheet.getLastRowNum() + 2);
        HSSFCell cell = row.createCell((short) 0);
        cell.setCellValue(getString("link.summary"));
        cell.setCellStyle(excelStyle.getTitleStyle());
        sheet.addMergedRegion(new Region(row.getRowNum(), (short) 0, row.getRowNum(), (short) getNumberOfColumns()));

        row = sheet.createRow(sheet.getLastRowNum() + 1);
        cell = row.createCell((short) 0);
        cell.setCellValue(getString("label.total") + ":");
        cell.setCellStyle(excelStyle.getLabelStyle());
        sheet.addMergedRegion(new Region(row.getRowNum(), (short) 0, row.getRowNum(), (short) 2));
        cell = row.createCell((short) 3);
        CellReference cellRef1 = new CellReference(1, 8);
        CellReference cellRef2 = new CellReference(((short) row.getRowNum() - 1), 8);
        cell.setCellStyle(excelStyle.getDoubleStyle());
        cell.setCellFormula("sum(" + cellRef1.formatAsString() + ":" + cellRef2.formatAsString() + ")");

        row = sheet.createRow(sheet.getLastRowNum() + 1);
        cell = row.createCell((short) 0);
        cell.setCellValue(getString("label.executedOrJustified"));
        cell.setCellStyle(excelStyle.getLabelStyle());
        sheet.addMergedRegion(new Region(row.getRowNum(), (short) 0, row.getRowNum(), (short) 2));
        cell = row.createCell((short) 3);
        cellRef1 = new CellReference(1, 7);
        cellRef2 = new CellReference(((short) row.getRowNum() - 1), 7);
        cell.setCellStyle(excelStyle.getDoubleStyle());
        cell.setCellFormula("sum(" + cellRef1.formatAsString() + ":" + cellRef2.formatAsString() + ")");

        row = sheet.createRow(sheet.getLastRowNum() + 1);
        cell = row.createCell((short) 0);
        cell.setCellValue(getString("label.forExecuted"));
        cell.setCellStyle(excelStyle.getLabelStyle());
        sheet.addMergedRegion(new Region(row.getRowNum(), (short) 0, row.getRowNum(), (short) 2));
        cell = row.createCell((short) 3);
        cellRef1 = new CellReference(sheet.getLastRowNum() - 2, 3);
        cellRef2 = new CellReference(sheet.getLastRowNum() - 1, 3);
        cell.setCellStyle(excelStyle.getDoubleStyle());
        cell.setCellFormula(cellRef1.formatAsString() + "-" + cellRef2.formatAsString());

    }

    @Override
    public int getNumberOfColumns() {
        return 8;
    }

}
