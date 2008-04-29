/*
 * Created on Jan 24, 2006
 *	by mrsp & lepc
 */
package net.sourceforge.fenixedu.util.report;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.util.projectsManagement.ExcelStyle;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class Spreadsheet {

    public class Row {
	private List<Object> cells = new ArrayList<Object>();

	protected Row() {
	}

	public void setCell(final int columnIndex, final String cellValue) {
	    for (int i = cells.size(); i < (columnIndex + 1); i++) {
		cells.add("");
	    }
	    cells.set(columnIndex, cellValue);
	}

	public void setCell(final String cellValue) {
	    cells.add(cellValue);
	}

	public void setValues(final String[] values) {
	    for (int i = 0; i < values.length; i++) {
		setCell(i, values[i]);
	    }
	}

	protected List<Object> getCells() {
	    return Collections.unmodifiableList(cells);
	}
    }

    private String name;

    private List<Object> header;

    private List<Row> rows = new ArrayList<Row>();

    public Spreadsheet(final String name) {
	this(name, new ArrayList<Object>());
    }

    public Spreadsheet(final String name, final List<Object> header) {
	setName(name);
	this.header = header;
    }

    protected String getName() {
	return name;
    }

    public void setName(final String name) {
	this.name = name.substring(0, Math.min(31, name.length())).replace('(', '_').replace(')', '_');
    }

    protected List<Object> getHeader() {
	return header;
    }

    public void setHeader(final int columnNumber, final String columnHeader) {
	for (int i = header.size(); i < columnNumber; i++) {
	    header.add("");
	}
	header.add(columnNumber, columnHeader);
    }

    public void setHeader(final String columnHeader) {
	header.add(columnHeader);
    }

    public void setHeaders(final String[] headers) {
	for (int i = 0; i < headers.length; i++) {
	    setHeader(i, headers[i]);
	}
    }

    public Row addRow(final int rowNumber) {
	for (int i = rows.size(); i < rowNumber; i++) {
	    rows.add(new Row());
	}
	return addRow();
    }

    public Row addRow() {
	final Row row = new Row();
	rows.add(row);
	return row;
    }

    public Row getRow(final int rowNumber) {
	return rows.get(rowNumber);
    }
    
    protected List<Row> getRows() {
	return rows;
    }

    public void setRows(List<Row> rows) {
	this.rows = rows;
    }

    public void exportToCSV(final OutputStream outputStream, final String columnSeperator) throws IOException {
	exportToCSV(outputStream, columnSeperator, "\n");
    }

    public void exportToCSV(final OutputStream outputStream, final String columnSeperator, final String lineSepeator)
	    throws IOException {
	exportCSVLine(outputStream, columnSeperator, lineSepeator, header);
	for (final Row row : rows) {
	    exportCSVLine(outputStream, columnSeperator, lineSepeator, row.getCells());
	}
    }

    public void exportToCSV(final File file, final String columnSeperator) throws IOException {
	BufferedOutputStream outputStream = null;
	try {
	    outputStream = new BufferedOutputStream(new FileOutputStream(file));
	    exportToCSV(outputStream, columnSeperator);
	} catch (FileNotFoundException e) {
	    throw new RuntimeException(e);
	} catch (IOException e) {
	    throw new RuntimeException(e);
	} finally {
	    if (outputStream != null) {
		try {
		    outputStream.close();
		} catch (IOException e) {
		    throw new RuntimeException(e);
		}
	    }
	}
    }

    public void exportToCSV(final File file, final String columnSeperator, final String lineSepeator) throws IOException {
	BufferedOutputStream outputStream = null;
	try {
	    outputStream = new BufferedOutputStream(new FileOutputStream(file));
	    exportToCSV(outputStream, columnSeperator, lineSepeator);
	} catch (FileNotFoundException e) {
	    throw new RuntimeException(e);
	} catch (IOException e) {
	    throw new RuntimeException(e);
	} finally {
	    if (outputStream != null) {
		try {
		    outputStream.close();
		} catch (IOException e) {
		    throw new RuntimeException(e);
		}
	    }
	}
    }

    private void exportCSVLine(final OutputStream outputStream, final String columnSeperator, final String lineSepeator,
	    final List<Object> cells) throws IOException {
	final byte[] columnSeperatorAsBytes = columnSeperator.getBytes();

	for (int i = 0; i < cells.size(); i++) {
	    final Object cellValue = cells.get(i);

	    if (i > 0) {
		outputStream.write(columnSeperatorAsBytes);
	    }

	    if (cellValue == null) {
		outputStream.write(StringUtils.EMPTY.getBytes());
	    } else {
		outputStream.write(cellValue.toString().replace(columnSeperator, "").getBytes());
	    }
	}
	outputStream.write(lineSepeator.getBytes());
    }

    public void exportToXLSSheet(final OutputStream outputStream) throws IOException {
	final HSSFWorkbook workbook = new HSSFWorkbook();
	final ExcelStyle excelStyle = new ExcelStyle(workbook);
	exportToXLSSheet(workbook, excelStyle.getHeaderStyle(), excelStyle.getStringStyle());
	workbook.write(outputStream);
    }

    public void exportToXLSSheet(final File file) throws IOException {
	BufferedOutputStream outputStream = null;
	try {
	    outputStream = new BufferedOutputStream(new FileOutputStream(file));
	    exportToXLSSheet(outputStream);
	} catch (FileNotFoundException e) {
	    throw new RuntimeException(e);
	} catch (IOException e) {
	    throw new RuntimeException(e);
	} finally {
	    if (outputStream != null) {
		try {
		    outputStream.close();
		} catch (IOException e) {
		    throw new RuntimeException(e);
		}
	    }
	}
    }

    public void exportToXLSSheet(final HSSFWorkbook workbook, final HSSFCellStyle headerCellStyle, final HSSFCellStyle cellStyle) {
	final HSSFSheet sheet = workbook.createSheet(name);
	sheet.setDefaultColumnWidth((short) 20);

	exportXLSLine(sheet, headerCellStyle, header, 0);

	for (final Row row : rows) {
	    exportXLSLine(sheet, cellStyle, row.getCells());
	}
    }

    protected void exportXLSLine(final HSSFSheet sheet, final HSSFCellStyle cellStyle, final List<Object> cells) {
	exportXLSLine(sheet, cellStyle, cells, 1);
    }

    protected void exportXLSLine(final HSSFSheet sheet, final HSSFCellStyle cellStyle, final List<Object> cells, final int offset) {
	final HSSFRow row = sheet.createRow(sheet.getLastRowNum() + offset);
	for (final Object cellValue : cells) {
	    addColumn(cellStyle, row, cellValue);
	}
    }

    protected static HSSFCell addColumn(final HSSFCellStyle cellStyle, final HSSFRow row, final Object cellValue) {
	final HSSFCell cell = row.createCell((short) (row.getLastCellNum() + 1));
	cell.setCellStyle(cellStyle);
	if (cellValue != null) {
	    cell.setCellValue(cellValue.toString());
	} else {
	    cell.setCellValue("");
	}
	return cell;
    }

}
