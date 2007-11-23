package net.sourceforge.fenixedu.presentationTier.Action.parkingManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.research.result.OpenFileBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.parking.ParkingGroup;
import net.sourceforge.fenixedu.domain.parking.ParkingParty;
import net.sourceforge.fenixedu.domain.parking.Vehicle;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.DateFormatUtil;
import net.sourceforge.fenixedu.util.projectsManagement.ExcelStyle;
import net.sourceforge.fenixedu.util.report.Spreadsheet;
import net.sourceforge.fenixedu.util.report.Spreadsheet.Row;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.util.FileUtils;

import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.Table;

public class ExportParkingDataToAccessDatabaseDA extends FenixDispatchAction {

    public ActionForward prepareExportFile(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("openFileBean", new OpenFileBean());

	return mapping.findForward("exportFile");
    }

    public ActionForward exportFile(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	try {
	    response.setContentType("text/plain");
	    response.setHeader("Content-disposition", "attachment; filename=parkingDB.xls");
	    //response.setContentType("application/vnd.ms-excel");

	    final HSSFWorkbook workbook = new HSSFWorkbook();
	    final ExcelStyle excelStyle = new ExcelStyle(workbook);
	    final ServletOutputStream writer = response.getOutputStream();

	    final Spreadsheet parkingBDSpreadsheet = new Spreadsheet("BD F�nix Parque");
	    setHeaders(parkingBDSpreadsheet);
	    for (ParkingParty parkingParty : ParkingParty.getAll()) {
		if (parkingParty.getCardNumber() != null) {
		    fillInRow(parkingBDSpreadsheet, parkingParty);
		}
	    }
	    parkingBDSpreadsheet.exportToXLSSheet(workbook, excelStyle.getHeaderStyle(), excelStyle
		    .getStringStyle());
	    workbook.write(writer);
	    writer.flush();
	    response.flushBuffer();

	} catch (Exception e) {
	    throw new FenixServiceException();
	}
	return null;
    }

    private void fillInRow(final Spreadsheet parkingBDSpreadsheet, ParkingParty parkingParty)
	    throws FenixServiceException {
	DateTime dateTime = new DateTime();
	final Row row = parkingBDSpreadsheet.addRow();
	Person person = null;
	if (parkingParty.getParty().isPerson()) {
	    person = (Person) parkingParty.getParty();
	}
	row.setCell("0"); // garage
	row.setCell(parkingParty.getCardNumber().toString()); // cardNumber
	row.setCell("3"); // type Mirafe
	row.setCell("TRUE"); // access (if the card is active or not)
	row.setCell(convertParkingGroupToAccessDB(parkingParty.getParkingGroup()));
	row.setCell("1"); // fee
	row.setCell("0"); // SAC
	row.setCell("1/1/2000"); // alterDate
	row.setCell(dateTime.toString("dd/MM/yyyy HH:mm:ss")); // createdDate
	row.setCell(dateTime.toString("dd/MM/yyyy HH:mm:ss")); // editedDate
	row.setCell(person != null ? getName(person.getNickname()) : getName(parkingParty.getParty()
		.getName())); // name
	row.setCell(""); // address         
	String vehicle1PlateNumber = "";
	String vehicle2PlateNumber = "";
	int counter = 1;
	for (Vehicle vehicle : parkingParty.getVehicles()) {
	    if (counter == 1) {
		vehicle1PlateNumber = vehicle.getPlateNumber();
	    } else if (counter == 2) {
		vehicle2PlateNumber = vehicle.getPlateNumber();
	    } else {
		break;
	    }
	    counter++;
	}
	row.setCell(vehicle1PlateNumber); // license
	row.setCell(vehicle2PlateNumber); // licenseAlt
	row.setCell(person != null && person.getWorkPhone() != null ? person.getWorkPhone() : ""); // registration
	row.setCell(person != null && person.getMobile() != null ? person.getMobile() : ""); // registrationAlt
	row.setCell(""); // clientRef
	row.setCell(""); // comment
	row.setCell("0"); // price
	String endValidityDate = parkingParty.getCardEndDate() == null ? "" : parkingParty
		.getCardEndDate().toString("dd/MM/yyyy HH:mm:ss");
	row.setCell(endValidityDate);
	row.setCell("1/1/2000"); // lastUsedDate
	row.setCell("FALSE"); // invoice
	row.setCell(parkingParty.getCardStartDate() != null ? "FALSE" : "TRUE"); // if true, start and end validity dates are ignored
	row.setCell("FALSE"); // present
	row.setCell("FALSE"); // payDirect
	row.setCell("FALSE"); // apbCorrect (if it's already in the park)
	row.setCell("TRUE"); // noFee
	String startValidityDate = parkingParty.getCardStartDate() == null ? "" : parkingParty
		.getCardStartDate().toString("dd/MM/yyyy HH:mm:ss");
	row.setCell(startValidityDate);
    }

    private Element generateParkingCardElement(ParkingParty parkingParty) throws FenixServiceException {
	long thisInstant = Calendar.getInstance().getTimeInMillis();
	DateTime dateTime = new DateTime(thisInstant);
	Person person = null;
	if (parkingParty.getParty().isPerson()) {
	    person = (Person) parkingParty.getParty();
	}
	String vehicle1PlateNumber = ""; // license
	String vehicle2PlateNumber = ""; // licenseAlt
	int counter = 1;
	for (Vehicle vehicle : parkingParty.getVehicles()) {
	    if (counter == 1) {
		vehicle1PlateNumber = vehicle.getPlateNumber();
	    } else if (counter == 2) {
		vehicle2PlateNumber = vehicle.getPlateNumber();
	    } else {
		break;
	    }
	    counter++;
	}

	String endValidityDate = parkingParty.getCardEndDate() == null ? "" : parkingParty
		.getCardEndDate().toString("dd/MM/yyyy HH:mm:ss");
	String startValidityDate = parkingParty.getCardStartDate() == null ? "" : parkingParty
		.getCardStartDate().toString("dd/MM/yyyy HH:mm:ss");

	Element element = new Element("XML");

	element.addContent(new Element("Garage").setText("0")).addContent(
		new Element("Card").setText(parkingParty.getCardNumber().toString())).addContent(
		new Element("Type").setText("3")).addContent(new Element("Access").setText("TRUE"))
		.addContent(
			new Element("AccessGroup").setText(convertParkingGroupToAccessDB(parkingParty
				.getParkingGroup()))).addContent(new Element("Fee").setText("1"))
		.addContent(new Element("SAC").setText("0")).addContent(
			new Element("AlterDate").setText("1/1/2000")).addContent(
			new Element("CreatedDate").setText(dateTime.toString("dd/MM/yyyy HH:mm:ss")))
		.addContent(new Element("EditedDate").setText(dateTime.toString("dd/MM/yyyy HH:mm:ss")))
		.addContent(
			new Element("Name").setText(person != null ? getName(person.getNickname())
				: getName(parkingParty.getParty().getName()))).addContent(
			new Element("Address").setText("")).addContent(
			new Element("License").setText(vehicle1PlateNumber)).addContent(
			new Element("LicenseAlt").setText(vehicle2PlateNumber)).addContent(
			new Element("Registration").setText(person != null
				&& person.getWorkPhone() != null ? person.getWorkPhone() : ""))
		.addContent(
			new Element("RegistrationAlt").setText(person != null
				&& person.getMobile() != null ? person.getMobile() : "")).addContent(
			new Element("ClientRef").setText("")).addContent(
			new Element("Comment").setText(""))
		.addContent(new Element("Price").setText("0")).addContent(
			new Element("EndValidityDate").setText(endValidityDate)).addContent(
			new Element("LastUsedDate").setText("1/1/2000")).addContent(
			new Element("Invoice").setText("FALSE")).addContent(
			new Element("Unlimited")
				.setText(parkingParty.getCardStartDate() != null ? "FALSE" : "TRUE"))
		.addContent(new Element("Present").setText("FALSE")).addContent(
			new Element("PayDirect").setText("FALSE")).addContent(
			new Element("APBCorrect").setText("FALSE")).addContent(
			new Element("NoFee").setText("TRUE")).addContent(
			new Element("StartValidityDate").setText(startValidityDate));
	return element;
    }

    private String getName(String name) {
	if (name.length() > 59) { //max size of the other parking application DB
	    StringBuilder resultName = new StringBuilder();
	    resultName = new StringBuilder();
	    String[] names = name.split("\\p{Space}+");
	    for (int iter = 1; iter < names.length - 1; iter++) {
		if (names[iter].length() > 5) {
		    names[iter] = names[iter].substring(0, 5) + ".";
		}
	    }
	    for (int iter = 0; iter < names.length; iter++) {
		resultName.append(names[iter]).append(" ");
	    }
	    if (resultName.length() > 59) {
		resultName = new StringBuilder(names[0]).append(" ").append(names[names.length - 1]);
	    }
	    return resultName.toString().trim();
	} else {
	    return name;
	}

    }

    private void setHeaders(final Spreadsheet spreadsheet) {
	spreadsheet.setHeader("Garage");
	spreadsheet.setHeader("Card");
	spreadsheet.setHeader("Type");
	spreadsheet.setHeader("Access");
	spreadsheet.setHeader("AccessGroup");
	spreadsheet.setHeader("Fee");
	spreadsheet.setHeader("SAC");
	spreadsheet.setHeader("AlterDate");
	spreadsheet.setHeader("CreatedDate");
	spreadsheet.setHeader("EditedDate");
	spreadsheet.setHeader("Name");
	spreadsheet.setHeader("Address");
	spreadsheet.setHeader("License");
	spreadsheet.setHeader("LicenseAlt");
	spreadsheet.setHeader("Registration");
	spreadsheet.setHeader("RegistrationAlt");
	spreadsheet.setHeader("ClientRef");
	spreadsheet.setHeader("Comment");
	spreadsheet.setHeader("Price");
	spreadsheet.setHeader("EndValidityDate");
	spreadsheet.setHeader("LastUsedDate");
	spreadsheet.setHeader("Invoice");
	spreadsheet.setHeader("Unlimited");
	spreadsheet.setHeader("Present");
	spreadsheet.setHeader("PayDirect");
	spreadsheet.setHeader("APBCorrect");
	spreadsheet.setHeader("NoFee");
	spreadsheet.setHeader("StartValidityDate");
    }

    private String convertParkingGroupToAccessDB(ParkingGroup parkingGroup) throws FenixServiceException {
	if (parkingGroup.getGroupName().equalsIgnoreCase("Docentes")) {
	    return "1";
	} else if (parkingGroup.getGroupName().equalsIgnoreCase("N�o Docentes")) {
	    return "2";
	} else if (parkingGroup.getGroupName().equalsIgnoreCase("Especiais")) {
	    return "3";
	} else if (parkingGroup.getGroupName().equalsIgnoreCase("Bolseiros")) {
	    return "4";
	} else if (parkingGroup.getGroupName().equalsIgnoreCase("Investigadores")) {
	    return "5";
	} else if (parkingGroup.getGroupName().equalsIgnoreCase("3� ciclo")) {
	    return "6";
	} else if (parkingGroup.getGroupName().equalsIgnoreCase("2� ciclo")) {
	    return "7";
	} else if (parkingGroup.getGroupName().equalsIgnoreCase("IPSFL")) {
	    return "8";
	} else if (parkingGroup.getGroupName().equalsIgnoreCase("Jubilados")) {
	    return "9";
	} else if (parkingGroup.getGroupName().equalsIgnoreCase("Limitados")) {
	    return "10";
	} else if (parkingGroup.getGroupName().equalsIgnoreCase("Limitado1")) {
	    return "11";
	} else if (parkingGroup.getGroupName().equalsIgnoreCase("Limitado2")) {
	    return "12";
	} else if (parkingGroup.getGroupName().equalsIgnoreCase("Limitado3")) {
	    return "13";
	} else if (parkingGroup.getGroupName().equalsIgnoreCase("Limitado4")) {
	    return "14";
	} else if (parkingGroup.getGroupName().equalsIgnoreCase("Limitado5")) {
	    return "15";
	}

	throw new FenixServiceException();
    }

    public ActionForward mergeFilesAndExportXML(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	OpenFileBean openFileBean = (OpenFileBean) getRenderedObject();
	if (openFileBean != null) {

	    Database database = Database.open(FileUtils.copyToTemporaryFile(openFileBean
		    .getInputStream()));
	    Table table = database.getTable("XML");
	    List<ParkingParty> parkingParties = getValidParkingParties();
	    Element root = new Element("parking");
	    for (Map<String, Object> row = table.getNextRow(); row != null; row = table.getNextRow()) {
		Long cardNumber = new Long((String) row.get("Card"));
		ParkingParty parkingParty = getParkingPartyCardNumber(cardNumber, parkingParties);
		if (parkingParty != null) {
		    root.addContent(mergeAndGenerateParkingCardElement(parkingParty, row));
		}
	    }

	    for (ParkingParty parkingParty : parkingParties) {
		root.addContent(generateParkingCardElement(parkingParty));
	    }

	    database.close();
	    Document document = new Document();
	    document.setRootElement(root);

	    response.setContentType("text/xml");
	    response.setHeader("Content-Disposition", "attachment; filename=parque.xml");

	    XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat().setEncoding("ISO-8859-1"));
	    outputter.output(document, response.getWriter());
	}
	return null;
    }

    public ActionForward mergeFilesAndExport(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	OpenFileBean openFileBean = (OpenFileBean) getRenderedObject();
	if (openFileBean != null) {
	    response.setContentType("text/plain");
	    response.setHeader("Content-disposition", "attachment; filename=parkingDB_merge.xls");
	    Database database = Database.open(FileUtils.copyToTemporaryFile(openFileBean
		    .getInputStream()));
	    Table table = database.getTable("XML");

	    List<ParkingParty> parkingParties = getValidParkingParties();
	    final HSSFWorkbook workbook = new HSSFWorkbook();
	    final ExcelStyle excelStyle = new ExcelStyle(workbook);
	    final ServletOutputStream writer = response.getOutputStream();
	    final Spreadsheet parkingBDSpreadsheet = new Spreadsheet("BD F�nix Parque");
	    setHeaders(parkingBDSpreadsheet);

	    for (Map<String, Object> row = table.getNextRow(); row != null; row = table.getNextRow()) {
		Long cardNumber = new Long((String) row.get("Card"));
		ParkingParty parkingParty = getParkingPartyCardNumber(cardNumber, parkingParties);
		if (parkingParty != null) {
		    addRow(parkingBDSpreadsheet, parkingParty, row);
		}
	    }

	    for (ParkingParty parkingParty : parkingParties) {
		fillInRow(parkingBDSpreadsheet, parkingParty);
	    }

	    database.close();
	    parkingBDSpreadsheet.exportToXLSSheet(workbook, excelStyle.getHeaderStyle(), excelStyle
		    .getStringStyle());
	    workbook.write(writer);
	    writer.flush();
	    response.flushBuffer();

	}
	return null;
    }

    private List<ParkingParty> getValidParkingParties() {
	List<ParkingParty> parkingParties = new ArrayList<ParkingParty>();
	for (ParkingParty parkingParty : ParkingParty.getAll()) {
	    if (parkingParty.getCardNumber() != null) {
		parkingParties.add(parkingParty);
	    }
	}
	return parkingParties;
    }

    private ParkingParty getParkingPartyCardNumber(Long cardNumber, List<ParkingParty> parkingParties) {
	for (ParkingParty parkingParty : parkingParties) {
	    if (parkingParty.getCardNumber().equals(cardNumber)) {
		parkingParties.remove(parkingParty);
		return parkingParty;
	    }
	}
	return null;
    }

    private Element mergeAndGenerateParkingCardElement(ParkingParty parkingParty,
	    Map<String, Object> accessTableRow) throws FenixServiceException {
	long thisInstant = Calendar.getInstance().getTimeInMillis();
	DateTime dateTime = new DateTime(thisInstant);
	Person person = null;
	if (parkingParty.getParty().isPerson()) {
	    person = (Person) parkingParty.getParty();
	}

	String garage = ((Short) accessTableRow.get("Garage")).toString();
	String cardNumber = parkingParty.getCardNumber().toString(); // cardNumber
	String type = ((Short) accessTableRow.get("Type")).toString();
	String access = getBooleanString(accessTableRow.get("Access")); // if the card is active or not
	String accessGroup = convertParkingGroupToAccessDB(parkingParty.getParkingGroup()); // accessGroup
	String fee = ((Short) accessTableRow.get("Fee")).toString();
	String sac = ((Short) accessTableRow.get("SAC")).toString();
	String alterDate = DateFormatUtil.format("dd/MM/yyyy HH:mm:ss", (Date) accessTableRow
		.get("AlterDate"));
	String createdDate = DateFormatUtil.format("dd/MM/yyyy HH:mm:ss", (Date) accessTableRow
		.get("CreatedDate"));
	String editedDate = dateTime.toString("dd/MM/yyyy HH:mm:ss"); // editedDate
	String name = person != null ? getName(person.getNickname()) : getName(parkingParty.getParty()
		.getName()); // name
	String address = (String) accessTableRow.get("Address");
	String vehicle1PlateNumber = ""; // license
	String vehicle2PlateNumber = ""; // licenseAlt
	int counter = 1;
	for (Vehicle vehicle : parkingParty.getVehicles()) {
	    if (counter == 1) {
		vehicle1PlateNumber = vehicle.getPlateNumber();
	    } else if (counter == 2) {
		vehicle2PlateNumber = vehicle.getPlateNumber();
	    } else {
		break;
	    }
	    counter++;
	}
	String workPhone = person != null && person.getWorkPhone() != null ? person.getWorkPhone() : ""; // registration
	String mobilePhone = person != null && person.getMobile() != null ? person.getMobile() : ""; // registrationAlt
	String clientRef = (String) accessTableRow.get("ClientRef");
	String comment = (String) accessTableRow.get("Comment");
	String price = ((Integer) accessTableRow.get("Price")).toString();
	String endValidityDate = parkingParty.getCardEndDate() == null ? "" : parkingParty
		.getCardEndDate().toString("dd/MM/yyyy HH:mm:ss");
	String lastUsedDate = DateFormatUtil.format("dd/MM/yyyy HH:mm:ss", (Date) accessTableRow
		.get("LastUsedDate"));
	String invoice = getBooleanString(accessTableRow.get("Invoice"));
	String unlimited = parkingParty.getCardEndDate() != null ? "FALSE" : "TRUE"; // if true, start and end validity dates are ignored
	String present = getBooleanString(accessTableRow.get("Present"));
	String payDirect = getBooleanString(accessTableRow.get("PayDirect"));
	String apbCorrect = getBooleanString(accessTableRow.get("APBCorrect")); // if it's already in the park
	String noFee = getBooleanString(accessTableRow.get("NoFee"));
	String startValidityDate = parkingParty.getCardStartDate() == null ? "" : parkingParty
		.getCardStartDate().toString("dd/MM/yyyy HH:mm:ss");

	Element element = new Element("XML");

	element.addContent(new Element("Garage").setText(garage)).addContent(
		new Element("Card").setText(cardNumber)).addContent(new Element("Type").setText(type))
		.addContent(new Element("Access").setText(access)).addContent(
			new Element("AccessGroup").setText(accessGroup)).addContent(
			new Element("Fee").setText(fee)).addContent(new Element("SAC").setText(sac))
		.addContent(new Element("AlterDate").setText(alterDate)).addContent(
			new Element("CreatedDate").setText(createdDate)).addContent(
			new Element("EditedDate").setText(editedDate)).addContent(
			new Element("Name").setText(name)).addContent(
			new Element("Address").setText(address)).addContent(
			new Element("License").setText(vehicle1PlateNumber)).addContent(
			new Element("LicenseAlt").setText(vehicle2PlateNumber)).addContent(
			new Element("Registration").setText(workPhone)).addContent(
			new Element("RegistrationAlt").setText(mobilePhone)).addContent(
			new Element("ClientRef").setText(clientRef)).addContent(
			new Element("Comment").setText(comment)).addContent(
			new Element("Price").setText(price)).addContent(
			new Element("EndValidityDate").setText(endValidityDate)).addContent(
			new Element("LastUsedDate").setText(lastUsedDate)).addContent(
			new Element("Invoice").setText(invoice)).addContent(
			new Element("Unlimited").setText(unlimited)).addContent(
			new Element("Present").setText(present)).addContent(
			new Element("PayDirect").setText(payDirect)).addContent(
			new Element("APBCorrect").setText(apbCorrect)).addContent(
			new Element("NoFee").setText(noFee)).addContent(
			new Element("StartValidityDate").setText(startValidityDate));
	return element;
    }

    private void addRow(final Spreadsheet parkingBDSpreadsheet, ParkingParty parkingParty,
	    Map<String, Object> accessTableRow) throws FenixServiceException {
	long thisInstant = Calendar.getInstance().getTimeInMillis();
	DateTime dateTime = new DateTime(thisInstant);
	final Row row = parkingBDSpreadsheet.addRow();
	Person person = null;
	if (parkingParty.getParty().isPerson()) {
	    person = (Person) parkingParty.getParty();
	}
	row.setCell(((Short) accessTableRow.get("Garage")).toString());
	row.setCell(parkingParty.getCardNumber().toString()); // cardNumber
	row.setCell(((Short) accessTableRow.get("Type")).toString());
	row.setCell(getBooleanString(accessTableRow.get("Access"))); // if the card is active or not
	row.setCell(convertParkingGroupToAccessDB(parkingParty.getParkingGroup())); // accessGroup
	row.setCell(((Short) accessTableRow.get("Fee")).toString());
	row.setCell(((Short) accessTableRow.get("SAC")).toString());
	row
		.setCell(DateFormatUtil.format("dd/MM/yyyy HH:mm:ss", (Date) accessTableRow
			.get("AlterDate")));
	row.setCell(DateFormatUtil.format("dd/MM/yyyy HH:mm:ss", (Date) accessTableRow
		.get("CreatedDate")));
	row.setCell(dateTime.toString("dd/MM/yyyy HH:mm:ss")); // editedDate
	row.setCell(person != null ? getName(person.getNickname()) : getName(parkingParty.getParty()
		.getName())); // name
	row.setCell((String) accessTableRow.get("Address"));
	String vehicle1PlateNumber = "";
	String vehicle2PlateNumber = "";
	int counter = 1;
	for (Vehicle vehicle : parkingParty.getVehicles()) {
	    if (counter == 1) {
		vehicle1PlateNumber = vehicle.getPlateNumber();
	    } else if (counter == 2) {
		vehicle2PlateNumber = vehicle.getPlateNumber();
	    } else {
		break;
	    }
	    counter++;
	}
	row.setCell(vehicle1PlateNumber); // license
	row.setCell(vehicle2PlateNumber); // licenseAlt

	row.setCell(person != null && person.getWorkPhone() != null ? person.getWorkPhone() : ""); // registration
	row.setCell(person != null && person.getMobile() != null ? person.getMobile() : ""); // registrationAlt
	row.setCell((String) accessTableRow.get("ClientRef"));
	row.setCell((String) accessTableRow.get("Comment"));
	row.setCell(((Integer) accessTableRow.get("Price")).toString());

	String endValidityDate = parkingParty.getCardEndDate() == null ? "" : parkingParty
		.getCardEndDate().toString("dd/MM/yyyy HH:mm:ss");
	row.setCell(endValidityDate);
	row.setCell(DateFormatUtil.format("dd/MM/yyyy HH:mm:ss", (Date) accessTableRow
		.get("LastUsedDate")));
	row.setCell(getBooleanString(accessTableRow.get("Invoice")));
	row.setCell(parkingParty.getCardEndDate() != null ? "FALSE" : "TRUE"); // if true, start and end validity dates are ignored
	row.setCell(getBooleanString(accessTableRow.get("Present")));
	row.setCell(getBooleanString(accessTableRow.get("PayDirect")));
	row.setCell(getBooleanString(accessTableRow.get("APBCorrect"))); // if it's already in the park
	row.setCell(getBooleanString(accessTableRow.get("NoFee")));

	String startValidityDate = parkingParty.getCardStartDate() == null ? "" : parkingParty
		.getCardStartDate().toString("dd/MM/yyyy HH:mm:ss");
	row.setCell(startValidityDate);
    }

    private String getBooleanString(Object object) {
	Boolean booleanNumber = (Boolean) object;
	if (booleanNumber) {
	    return "TRUE";
	} else {
	    return "FALSE";
	}
    }
}