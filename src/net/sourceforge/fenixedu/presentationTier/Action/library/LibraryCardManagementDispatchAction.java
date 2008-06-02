package net.sourceforge.fenixedu.presentationTier.Action.library;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.library.LibraryCardDTO;
import net.sourceforge.fenixedu.dataTransferObject.library.LibraryCardSearch;
import net.sourceforge.fenixedu.dataTransferObject.person.ExternalPersonBean;
import net.sourceforge.fenixedu.domain.PartyClassification;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.library.LibraryCard;
import net.sourceforge.fenixedu.domain.organizationalStructure.ExternalContract;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionUtils;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import net.sourceforge.fenixedu.util.ReportsUtils;
import net.sourceforge.fenixedu.util.report.Spreadsheet;
import net.sourceforge.fenixedu.util.report.Spreadsheet.Row;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import pt.utl.ist.fenix.tools.util.CollectionPager;
import pt.utl.ist.fenix.tools.util.StringNormalizer;

public class LibraryCardManagementDispatchAction extends FenixDispatchAction {

    private final static int maxUserNameLength = 45;

    private final static int maxUnitNameLength = 41;

    private final static int minimumPinNumber = 6910;

    private final static int maximumPinNumber = 100000;

    private ActionForward showUsersBase(ActionMapping mapping, HttpServletRequest request, LibraryCardSearch libraryCardSearch) {
	request.setAttribute("dontSearch", request.getParameter("dontSearch"));
	request.setAttribute("libraryCardSearch", libraryCardSearch);
	final List<LibraryCardDTO> result = libraryCardSearch.getSearchResult();

	CollectionPager<LibraryCardDTO> collectionPager = new CollectionPager<LibraryCardDTO>(result != null ? result : new ArrayList<LibraryCardDTO>(), 50);
	request.setAttribute("collectionPager", collectionPager);
	final String pageNumberString = request.getParameter("pageNumber");
	final Integer pageNumber = !StringUtils.isEmpty(pageNumberString) ? Integer.valueOf(pageNumberString) : Integer.valueOf(1);
	request.setAttribute("pageNumber", pageNumber);
	request.setAttribute("numberOfPages", Integer.valueOf(collectionPager.getNumberOfPages()));
	request.setAttribute("resultPage", collectionPager.getPage(pageNumber));

	return mapping.findForward("show-users");
    }

    public ActionForward showUsers(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	LibraryCardSearch libraryCardSearch = (LibraryCardSearch) getRenderedObject("libraryCardSearch");

	if (libraryCardSearch == null) {
	    PartyClassification partyClassification = null;
	    if (request.getAttribute("newUser") == null) {
		libraryCardSearch = (LibraryCardSearch) getRenderedObject();
		partyClassification = PartyClassification.TEACHER;
	    } else {
		partyClassification = PartyClassification.PERSON;
	    }
	    if (libraryCardSearch == null) {
		libraryCardSearch = new LibraryCardSearch();
		libraryCardSearch.setPartyClassification(partyClassification);
	    }
	    setSearchCriteria(request, libraryCardSearch);
	}

	if (request.getParameter("dontSearch") == null) {
	    libraryCardSearch.doSearch();
	}
	RenderUtils.invalidateViewState();
	return showUsersBase(mapping, request, libraryCardSearch);
    }

    public ActionForward showUsersFromPageSelect(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final String numberString = request.getParameter("number");

	final PartyClassification partyClassification = PartyClassification.valueOf(request.getParameter("partyClassification"));
	final String userName = request.getParameter("userName");
	final Integer number = numberString == null || numberString.length() == 0 ? null : Integer.valueOf(numberString);

	final LibraryCardSearch libraryCardSearch = new LibraryCardSearch();
	libraryCardSearch.setPartyClassification(partyClassification);
	libraryCardSearch.setUserName(userName);
	libraryCardSearch.setNumber(number);

	libraryCardSearch.doSearch();

	return showUsersBase(mapping, request, libraryCardSearch);
    }

    

    private void setSearchCriteria(HttpServletRequest request, LibraryCardSearch libraryCardSearch) {
	String partyClassificationString = request.getParameter("partyClassification");
	if (!StringUtils.isEmpty(partyClassificationString)) {
	    libraryCardSearch.setPartyClassification(PartyClassification.valueOf(partyClassificationString));
	}
	String userName = request.getParameter("userName");
	if (!StringUtils.isEmpty(userName)) {
	    libraryCardSearch.setUserName(userName);
	}
	String number = request.getParameter("number");
	if (!StringUtils.isEmpty(number)) {
	    libraryCardSearch.setNumber(Integer.valueOf(number));
	}
    }

    public ActionForward prepareGenerateCard(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	Integer personID = getIntegerFromRequest(request, "personID");
	Person person = (Person) rootDomainObject.readPartyByOID(personID);
	PartyClassification partyClassification = person.getPartyClassification();
	LibraryCardDTO libraryCardDTO = new LibraryCardDTO(person, partyClassification);

	libraryCardDTO.setUnlimitedCard(Boolean.TRUE);
	if (!partyClassification.equals(PartyClassification.EMPLOYEE) && !partyClassification.equals(PartyClassification.TEACHER)) {
	    libraryCardDTO.setUnlimitedCard(Boolean.FALSE);
	    request.setAttribute("presentDate", "presentDate");
	}

	if (person.getName().length() > maxUserNameLength) {
	    addMessage(request, "message.card.userName.tooLong", person.getName().length(), maxUserNameLength);
	}
	if (partyClassification.equals(PartyClassification.EMPLOYEE) || partyClassification.equals(PartyClassification.PERSON)
		|| partyClassification.equals(PartyClassification.GRANT_OWNER)) {
	    if (libraryCardDTO.getUnitName().length() > maxUnitNameLength) {
		addMessage(request, "message.card.unitName.tooLong", libraryCardDTO.getUnitName().length(), maxUnitNameLength);
	    }
	    libraryCardDTO.setChosenUnitName(libraryCardDTO.getUnitName());
	    request.setAttribute("employee", "employee");
	}
	List<Integer> pinList = getExistingPins();

	Random random = new Random();
	while (true) {
	    Integer pin = random.nextInt(maximumPinNumber);
	    if (pin <= minimumPinNumber) {
		continue;
	    }
	    if (!pinList.contains(pin)) {
		libraryCardDTO.setPin(pin);
		break;
	    }
	}

	Integer number = !StringUtils.isEmpty(request.getParameter("number")) ? Integer.valueOf(request.getParameter("number"))
		: null;
	String name = request.getParameter("name");
	request.setAttribute("libraryCardSearch", new LibraryCardSearch(partyClassification, name, number));
	request.setAttribute("libraryCardDTO", libraryCardDTO);
	return mapping.findForward("create-card");
    }

    public ActionForward createCard(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	if (RenderUtils.getViewState().isCanceled()) {
	    return showUsers(mapping, actionForm, request, response);
	}

	LibraryCardDTO libraryCardDTO = (LibraryCardDTO) getRenderedObject("libraryCardToCreate");

	if (!libraryCardDTO.getUnlimitedCard() && libraryCardDTO.getValidUntil() == null) {
	    setError(request, "error.card.date.canNotBeNull");
	    request.setAttribute("presentDate", "presentDate");
	    request.setAttribute("libraryCardDTO", libraryCardDTO);
	    return mapping.findForward("create-card");
	}

	boolean validationError = validateNamesMaxLength(request, libraryCardDTO);
	if (validationError) {
	    request.setAttribute("libraryCardDTO", libraryCardDTO);
	    return mapping.findForward("create-card");
	}

	LibraryCard libraryCard = (LibraryCard) ServiceUtils.executeService(SessionUtils.getUserView(request),
		"CreateLibraryCard", new Object[] { libraryCardDTO });
	request.setAttribute("libraryCardDTO", new LibraryCardDTO(libraryCard));
	request.setAttribute("libraryCardSearch", getRenderedObject("libraryCardSearch"));
	return mapping.findForward("show-details");
    }

    public ActionForward generatePdfCard(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws JRException, IOException, FenixFilterException, FenixServiceException {

	if (request.getParameter("back") != null) {
	    return showUsers(mapping, actionForm, request, response);
	}

	LibraryCardDTO libraryCardDTO = (LibraryCardDTO) getRenderedObject("libraryCardDTO");

	if (request.getParameter("modify") != null) {
	    libraryCardDTO = getRenderedObject("libraryCardEdit") != null ? (LibraryCardDTO) getRenderedObject("libraryCardEdit")
		    : libraryCardDTO;
	    //TODO remove this condition, when user names that already exist are no longer bigger than the max length
	    if (libraryCardDTO.getPerson().getName().length() > maxUserNameLength) {
		addMessage(request, "message.card.userName.tooLong", libraryCardDTO.getPerson().getName().length(),
			maxUserNameLength);
	    }
	    if (libraryCardDTO.getPartyClassification().equals(PartyClassification.TEACHER)) {
		libraryCardDTO.setChosenUnitNameToEdit();
	    }
	    request.setAttribute("libraryCardDTO", libraryCardDTO);
	    request.setAttribute("libraryCardSearch", getRenderedObject("libraryCardSearch"));
	    return mapping.findForward("edit-card");
	}

	List<LibraryCardDTO> cardList = new ArrayList<LibraryCardDTO>();
	cardList.add(libraryCardDTO);
	final ResourceBundle bundle = ResourceBundle.getBundle("resources.LibraryResources", Language.getLocale());
	byte[] data = ReportsUtils.exportToPdf("net.sourceforge.fenixedu.domain.library.LibrabryCard", null, bundle, cardList);

	ServiceUtils.executeService(SessionUtils.getUserView(request), "MarkLibraryCardAsEmited", new Object[] { libraryCardDTO
		.getLibraryCard() });

	response.setContentType("application/pdf");
	response.addHeader("Content-Disposition", "attachment; filename=cartao.pdf");
	response.setContentLength(data.length);
	ServletOutputStream writer = response.getOutputStream();
	writer.write(data);
	writer.flush();
	writer.close();
	response.flushBuffer();

	return null;
    }

    public ActionForward editCard(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws JRException, IOException, FenixFilterException, FenixServiceException {

	if (request.getParameter("cancel") != null) {
	    return showUsers(mapping, actionForm, request, response);
	}

	LibraryCardDTO libraryCardDTO = (LibraryCardDTO) getRenderedObject("libraryCardEdit");

	boolean validationError = validateNamesMaxLength(request, libraryCardDTO);
	if (validationError) {
	    request.setAttribute("libraryCardDTO", libraryCardDTO);
	    request.setAttribute("libraryCardSearch", new LibraryCardSearch(libraryCardDTO.getPerson().getPartyClassification()));
	    RenderUtils.invalidateViewState();
	    return mapping.findForward("edit-card");
	}

	LibraryCard libraryCard = (LibraryCard) ServiceUtils.executeService(SessionUtils.getUserView(request), "EditLibraryCard",
		new Object[] { libraryCardDTO });

	request.setAttribute("libraryCardDTO", new LibraryCardDTO(libraryCard));
	request.setAttribute("libraryCardSearch", new LibraryCardSearch(libraryCardDTO.getPerson().getPartyClassification()));

	RenderUtils.invalidateViewState();
	return mapping.findForward("show-details");
    }

    public ActionForward prepareGenerateMissingCards(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws JRException, IOException, FenixFilterException, FenixServiceException {

	return mapping.findForward("generate-missing-cards");
    }

    public ActionForward generateMissingCards(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws JRException, IOException, FenixFilterException, FenixServiceException {

	List<LibraryCardDTO> cardList = new ArrayList<LibraryCardDTO>();
	for (LibraryCard libraryCard : rootDomainObject.getLibraryCards()) {
	    if (libraryCard.getCardEmitionDate() == null) {
		cardList.add(new LibraryCardDTO(libraryCard));
	    }
	}

	if (!cardList.isEmpty()) {
	    final ResourceBundle bundle = ResourceBundle.getBundle("resources.LibraryResources", Language.getLocale());
	    byte[] data = ReportsUtils
		    .exportToPdf("net.sourceforge.fenixedu.domain.library.LibrabryCard", null, bundle, cardList);

	    ServiceUtils.executeService(SessionUtils.getUserView(request), "MarkLibraryCardListAsEmited",
		    new Object[] { cardList });

	    response.setContentType("application/pdf");
	    response.addHeader("Content-Disposition", "attachment; filename=cartoes.pdf");
	    response.setContentLength(data.length);
	    ServletOutputStream writer = response.getOutputStream();
	    writer.write(data);
	    writer.flush();
	    writer.close();
	    response.flushBuffer();

	    return null;
	} else {
	    request.setAttribute("nothingMissing", "nothingMissing");
	    return mapping.findForward("generate-missing-cards");
	}
    }

    public ActionForward generatePdfLetter(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws JRException, IOException, FenixFilterException, FenixServiceException {

	Integer libraryCardID = new Integer(request.getParameter("libraryCardID"));

	LibraryCard libraryCard = rootDomainObject.readLibraryCardByOID(libraryCardID);

	List<LibraryCardDTO> cardList = new ArrayList<LibraryCardDTO>();
	cardList.add(new LibraryCardDTO(libraryCard));
	final ResourceBundle bundle = ResourceBundle.getBundle("resources.LibraryResources", Language.getLocale());
	String reportID = "net.sourceforge.fenixedu.domain.library.LibrabryCard.letter";
	if (!libraryCard.getPartyClassification().equals(PartyClassification.EMPLOYEE)
		&& !libraryCard.getPartyClassification().equals(PartyClassification.TEACHER)) {
	    reportID += "ForStudents";
	}
	byte[] data = ReportsUtils.exportToPdf(reportID, null, bundle, cardList);

	ServiceUtils.executeService(SessionUtils.getUserView(request), "MarkLibraryCardLetterAsEmited",
		new Object[] { libraryCard });

	response.setContentType("application/pdf");
	response.addHeader("Content-Disposition", "attachment; filename=carta.pdf");
	response.setContentLength(data.length);
	ServletOutputStream writer = response.getOutputStream();
	writer.write(data);
	writer.flush();
	writer.close();
	response.flushBuffer();

	return null;
    }

    public ActionForward prepareGenerateMissingLetters(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws JRException, IOException, FenixFilterException, FenixServiceException {

	return mapping.findForward("generate-missing-letters");
    }

    public ActionForward prepareGenerateMissingLettersForStudents(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws JRException, IOException, FenixFilterException,
	    FenixServiceException {

	return mapping.findForward("generate-missing-letters-for-students");
    }

    public ActionForward generateMissingLetters(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws JRException, IOException, FenixFilterException, FenixServiceException {

	String result = request.getParameter("students");
	List<LibraryCardDTO> cardList = new ArrayList<LibraryCardDTO>();
	for (LibraryCard libraryCard : rootDomainObject.getLibraryCards()) {
	    if (libraryCard.getLetterGenerationDate() == null) {
		if (result.equalsIgnoreCase("no")
			&& (libraryCard.getPartyClassification().equals(PartyClassification.EMPLOYEE) || libraryCard
				.getPartyClassification().equals(PartyClassification.TEACHER))) {
		    cardList.add(new LibraryCardDTO(libraryCard));
		} else if (result.equalsIgnoreCase("yes")
			&& !libraryCard.getPartyClassification().equals(PartyClassification.EMPLOYEE)
			&& !libraryCard.getPartyClassification().equals(PartyClassification.TEACHER)) {
		    cardList.add(new LibraryCardDTO(libraryCard));
		}
	    }
	}

	if (!cardList.isEmpty()) {
	    final ResourceBundle bundle = ResourceBundle.getBundle("resources.LibraryResources", Language.getLocale());
	    String reportID = "net.sourceforge.fenixedu.domain.library.LibrabryCard.letter";
	    if (result.equalsIgnoreCase("yes")) {
		reportID += "ForStudents";
	    }
	    byte[] data = ReportsUtils.exportToPdf(reportID, null, bundle, cardList);

	    ServiceUtils.executeService(SessionUtils.getUserView(request), "MarkLibraryCardListLettersAsEmited",
		    new Object[] { cardList });

	    response.setContentType("application/pdf");
	    response.addHeader("Content-Disposition", "attachment; filename=cartas.pdf");
	    response.setContentLength(data.length);
	    ServletOutputStream writer = response.getOutputStream();
	    writer.write(data);
	    writer.flush();
	    writer.close();
	    response.flushBuffer();

	    return null;
	} else {
	    request.setAttribute("nothingMissing", "nothingMissing");
	    if (result.equalsIgnoreCase("yes")) {
		return mapping.findForward("generate-missing-letters-for-students");
	    }
	    return mapping.findForward("generate-missing-letters");
	}
    }

    public ActionForward prepareCreatePerson(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("externalPersonBean", new ExternalPersonBean());
	return mapping.findForward("create-person");
    }

    public ActionForward createPerson(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	if (RenderUtils.getViewState().isCanceled()) {
	    request.setAttribute("newUser", "newUser");
	    return showUsers(mapping, actionForm, request, response);
	}

	ExternalPersonBean externalPersonBean = (ExternalPersonBean) getRenderedObject("createPerson");
	if (externalPersonBean == null) {
	    externalPersonBean = (ExternalPersonBean) request.getAttribute("externalPersonBean");
	}
	RenderUtils.invalidateViewState();
	if (externalPersonBean.getPerson() != null
		&& !externalPersonBean.getName().equalsIgnoreCase(
			StringNormalizer.normalize(externalPersonBean.getPerson().getName()))) {
	    externalPersonBean.setPerson(null);

	}
	if (externalPersonBean.getPerson() == null && request.getParameter("createPerson") == null) {
	    request.setAttribute("externalPersonBean", externalPersonBean);
	    request.setAttribute("needToCreatePerson", "needToCreatePerson");
	    return mapping.findForward("create-person");
	} else if (externalPersonBean.getPerson() == null && request.getParameter("createPerson") != null) {
	    request.setAttribute("externalPersonBean", externalPersonBean);
	    return mapping.findForward("create-unit-person");
	}

	request.setAttribute("personID", externalPersonBean.getPerson().getIdInternal());
	return prepareGenerateCard(mapping, actionForm, request, response);
    }

    public ActionForward createUnitPerson(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	ExternalPersonBean externalPersonBean = (ExternalPersonBean) getRenderedObject("createUnitPerson");
	RenderUtils.invalidateViewState();
	request.setAttribute("externalPersonBean", externalPersonBean);

	if (request.getParameter("cancel") != null) {
	    if (externalPersonBean.getPerson() == null) {
		request.setAttribute("needToCreatePerson", "needToCreatePerson");
	    }
	    return mapping.findForward("create-person");
	}

	if (externalPersonBean.getDocumentIdNumber() != null && externalPersonBean.getDocumentIdNumber().length() != 0
		&& externalPersonBean.getIdDocumentType() != null) {
	    Person person = Person.readByDocumentIdNumberAndIdDocumentType(externalPersonBean.getDocumentIdNumber(),
		    externalPersonBean.getIdDocumentType());
	    if (person != null) {
		ActionMessages actionMessages = getMessages(request);
		actionMessages.add("error", new ActionMessage("error.createExternalePerson.existingId", person.getName()));
		saveMessages(request, actionMessages);
		return mapping.findForward("create-unit-person");
	    }
	}

	if (externalPersonBean.getUnit() != null
		&& !externalPersonBean.getUnit().getName().equalsIgnoreCase(externalPersonBean.getUnitName())) {
	    externalPersonBean.setUnit(null);
	}
	if (externalPersonBean.getUnit() == null && request.getParameter("createUnit") == null) {
	    request.setAttribute("needToCreateUnit", "needToCreateUnit");
	    return mapping.findForward("create-unit-person");
	}
	ExternalContract externalContract = null;
	if (externalPersonBean.getUnit() == null) {
	    Object[] args = { externalPersonBean.getName(), externalPersonBean.getUnitName(), externalPersonBean.getPhone(),
		    externalPersonBean.getMobile(), externalPersonBean.getEmail() };
	    externalContract = (ExternalContract) executeService("InsertExternalPerson", args);
	}
	if (externalPersonBean.getUnit() != null) {
	    Object[] args = { externalPersonBean.getName(), externalPersonBean.getUnit().getIdInternal(),
		    externalPersonBean.getPhone(), externalPersonBean.getMobile(), externalPersonBean.getEmail() };
	    externalContract = (ExternalContract) executeService("InsertExternalPerson", args);
	}

	request.setAttribute("personID", externalContract.getPerson().getIdInternal());

	return prepareGenerateCard(mapping, actionForm, request, response);
    }

    public ActionForward showDetails(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws JRException, IOException {

	Integer libraryCardID = new Integer(request.getParameter("libraryCardID"));
	LibraryCard libraryCard = rootDomainObject.readLibraryCardByOID(libraryCardID);
	//TODO remove this condition, when user names that already exist are no longer bigger than the max length
	if (libraryCard.getUserName().length() > maxUserNameLength) {
	    addMessage(request, "message.card.userName.tooLong", libraryCard.getUserName().length(), maxUserNameLength);
	}

	Integer number = !StringUtils.isEmpty(request.getParameter("number")) ? Integer.valueOf(request.getParameter("number"))
		: null;
	String name = request.getParameter("name");
	request.setAttribute("libraryCardSearch", new LibraryCardSearch(libraryCard.getPartyClassification(), name, number));
	request.setAttribute("libraryCardDTO", new LibraryCardDTO(libraryCard));
	return mapping.findForward("show-details");
    }

    public ActionForward changeDateVisibility(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws JRException, IOException {

	LibraryCardDTO libraryCardDTO = (LibraryCardDTO) getRenderedObject("validUntil");
	if (libraryCardDTO.getPerson().getName().length() > maxUserNameLength) {
	    addMessage(request, "message.card.userName.tooLong", libraryCardDTO.getPerson().getName().length(), maxUserNameLength);
	}

	if (!libraryCardDTO.getUnlimitedCard()) {
	    request.setAttribute("presentDate", "presentDate");
	} else {
	    libraryCardDTO.setValidUntil(null);
	}
	request.setAttribute("libraryCardSearch", getRenderedObject("libraryCardSearch"));
	request.setAttribute("libraryCardDTO", libraryCardDTO);
	RenderUtils.invalidateViewState();
	return mapping.findForward("create-card");
    }

    public ActionForward invalidDate(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws JRException, IOException {

	if (request.getParameter("cancel") != null) {
	    return showUsers(mapping, actionForm, request, response);
	}

	LibraryCardDTO libraryCardDTO = (LibraryCardDTO) getRenderedObject("validUntil");
	request.setAttribute("libraryCardDTO", libraryCardDTO);
	request.setAttribute("libraryCardSearch", getRenderedObject("libraryCardSearch"));
	request.setAttribute("presentDate", "presentDate");
	return mapping.findForward("create-card");
    }

    public ActionForward exportToExcel(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	LibraryCardSearch libraryCardSearch = new LibraryCardSearch();
	setSearchCriteria(request, libraryCardSearch);
	libraryCardSearch.doSearch();
	Spreadsheet spreadsheet = new Spreadsheet("Utilizadores_Biblioteca");
	spreadsheet.setHeader("Categoria");
	spreadsheet.setHeader("N�mero");
	spreadsheet.setHeader("Nome");
	spreadsheet.setHeader("Unidade");
	spreadsheet.setHeader("Telefone");
	spreadsheet.setHeader("Telem�vel");
	spreadsheet.setHeader("Email");
	spreadsheet.setHeader("Pin");
	spreadsheet.setHeader("Morada");
	spreadsheet.setHeader("Localidade");
	spreadsheet.setHeader("C�digo Postal");

	for (LibraryCardDTO libraryCardDTO : libraryCardSearch.getSearchResult()) {
	    if (libraryCardDTO.getPin() != null) {
		Person person = libraryCardDTO.getPerson();
		final Row row = spreadsheet.addRow();
		row.setCell(libraryCardDTO.getCategory());
		row.setCell(libraryCardDTO.getNumberToPDF());
		row.setCell(libraryCardDTO.getUserName());
		row.setCell(libraryCardDTO.getUnitName());
		row.setCell(libraryCardDTO.getPhone());
		row.setCell(libraryCardDTO.getMobile());
		row.setCell(person.getEmail());
		row.setCell(libraryCardDTO.getPinToShow());
		row.setCell(person.getAddress());
		row.setCell(person.getParishOfResidence());
		row.setCell(person.getPostalCode());
	    }
	}
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("Content-Disposition", "attachment; filename=utentes_biblioteca.xls");
	final ServletOutputStream writer = response.getOutputStream();
	spreadsheet.exportToXLSSheet(writer);
	writer.flush();
	response.flushBuffer();
	return null;
    }

    private boolean validateNamesMaxLength(HttpServletRequest request, LibraryCardDTO libraryCardDTO) {
	boolean validationError = Boolean.FALSE;
	if (libraryCardDTO.getUserName().length() > maxUserNameLength) {
	    setError(request, "message.card.userName.tooLong", libraryCardDTO.getUserName().length(), maxUserNameLength);
	    validationError = Boolean.TRUE;
	}
	if (libraryCardDTO.getPerson().getPartyClassification().equals(PartyClassification.EMPLOYEE)) {
	    if (libraryCardDTO.getChosenUnitName().length() > maxUnitNameLength) {
		setError(request, "message.card.unitName.tooLong", libraryCardDTO.getUnitName().length(), maxUnitNameLength);
		validationError = Boolean.TRUE;
	    }
	}
	return validationError;
    }

    private List<Integer> getExistingPins() {
	List<Integer> pins = new ArrayList<Integer>();
	for (LibraryCard libraryCard : rootDomainObject.getLibraryCards()) {
	    if (libraryCard.getPin() != null) {
		pins.add(libraryCard.getPin());
	    }
	}
	return pins;
    }

    private void addMessage(HttpServletRequest request, String msg, int parameter1, int parameter2) {
	ActionMessages actionMessages = getMessages(request);
	actionMessages.add("message", new ActionMessage(msg, parameter1, parameter2));
	saveMessages(request, actionMessages);
    }

    private void setError(HttpServletRequest request, String errorMsg) {
	ActionMessages actionMessages = getMessages(request);
	actionMessages.add("error", new ActionMessage(errorMsg));
	saveMessages(request, actionMessages);
    }

    private void setError(HttpServletRequest request, String errorMsg, int parameter1, int parameter2) {
	ActionMessages actionMessages = getMessages(request);
	actionMessages.add("error", new ActionMessage(errorMsg, parameter1, parameter2));
	saveMessages(request, actionMessages);
    }
}
