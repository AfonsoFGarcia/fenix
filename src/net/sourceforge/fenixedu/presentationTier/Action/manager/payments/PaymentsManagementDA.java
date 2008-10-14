package net.sourceforge.fenixedu.presentationTier.Action.manager.payments;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson.SearchParameters;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson.SearchPersonPredicate;
import net.sourceforge.fenixedu.dataTransferObject.accounting.AnnulAccountingTransactionBean;
import net.sourceforge.fenixedu.dataTransferObject.accounting.CancelEventBean;
import net.sourceforge.fenixedu.dataTransferObject.accounting.DepositAmountBean;
import net.sourceforge.fenixedu.dataTransferObject.accounting.TransferPaymentsToOtherEventAndCancelBean;
import net.sourceforge.fenixedu.dataTransferObject.person.SimpleSearchPersonWithStudentBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.AccountingTransaction;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.Receipt;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.DomainExceptionWithLabelFormatter;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.CollectionPager;

@Mapping(path = "/payments", module = "manager")
@Forwards( {
	@Forward(name = "searchPersons", path = "/manager/payments/events/searchPersons.jsp"),
	@Forward(name = "showEvents", path = "/manager/payments/events/showEvents.jsp"),
	@Forward(name = "editCancelEventJustification", path = "/manager/payments/events/editCancelEventJustification.jsp"),
	@Forward(name = "showPaymentsForEvent", path = "/manager/payments/events/showPaymentsForEvent.jsp"),
	@Forward(name = "chooseTargetEventForPaymentsTransfer", path = "/manager/payments/events/chooseTargetEventForPaymentsTransfer.jsp"),
	@Forward(name = "annulTransaction", path = "/manager/payments/events/annulTransaction.jsp"),
	@Forward(name = "showOperations", path = "/manager/payments/showOperations.jsp"),
	@Forward(name = "showReceipts", path = "/manager/payments/receipts/showReceipts.jsp"),
	@Forward(name = "showReceipt", path = "/manager/payments/receipts/showReceipt.jsp"),
	@Forward(name = "depositAmount", path = "/manager/payments/events/depositAmount.jsp")

})
public class PaymentsManagementDA extends FenixDispatchAction {

    public ActionForward prepareSearchPerson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("searchPersonBean", new SimpleSearchPersonWithStudentBean());

	return mapping.findForward("searchPersons");
    }

    public ActionForward prepareSearchPersonInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("searchPersonBean", getObjectFromViewState("searchPersonBean"));

	return mapping.findForward("searchPersons");
    }

    public ActionForward searchPerson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	final SimpleSearchPersonWithStudentBean searchPersonBean = (SimpleSearchPersonWithStudentBean) getObjectFromViewState("searchPersonBean");
	request.setAttribute("searchPersonBean", searchPersonBean);

	final Collection<Person> persons = searchPerson(request, searchPersonBean);
	if (persons.size() == 1) {
	    request.setAttribute("personId", persons.iterator().next().getIdInternal());

	    return showOperations(mapping, form, request, response);

	}

	request.setAttribute("persons", persons);
	return mapping.findForward("searchPersons");
    }

    public ActionForward showEvents(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	request.setAttribute("person", getPerson(request));

	return mapping.findForward("showEvents");

    }

    public ActionForward showPaymentsForEvent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("event", getEvent(request));

	return mapping.findForward("showPaymentsForEvent");
    }

    @SuppressWarnings( { "unused", "unchecked" })
    private Collection<Person> searchPerson(HttpServletRequest request, final SimpleSearchPersonWithStudentBean searchPersonBean)
	    throws FenixFilterException, FenixServiceException {
	final SearchParameters searchParameters = new SearchPerson.SearchParameters(searchPersonBean.getName(), null,
		searchPersonBean.getUsername(), searchPersonBean.getDocumentIdNumber(),
		searchPersonBean.getIdDocumentType() != null ? searchPersonBean.getIdDocumentType().toString() : null, null,
		null, null, null, null, searchPersonBean.getStudentNumber(), Boolean.FALSE);

	final SearchPersonPredicate predicate = new SearchPerson.SearchPersonPredicate(searchParameters);

	final CollectionPager<Person> result = (CollectionPager<Person>) executeService("SearchPerson", new Object[] {
		searchParameters, predicate });

	return result.getCollection();

    }

    public ActionForward prepareCancelEvent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("cancelEventBean", new CancelEventBean(getEvent(request), getLoggedPerson(request).getEmployee()));

	return mapping.findForward("editCancelEventJustification");
    }

    public ActionForward cancelEvent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final CancelEventBean cancelEventBean = getCancelEventBean();

	try {
	    executeService("CancelEvent", new Object[] { cancelEventBean.getEvent(), cancelEventBean.getEmployee(),
		    cancelEventBean.getJustification() });
	} catch (DomainExceptionWithLabelFormatter ex) {

	    addActionMessage(request, ex.getKey(), solveLabelFormatterArgs(request, ex.getLabelFormatterArgs()));
	    request.setAttribute("cancelEventBean", cancelEventBean);

	    return mapping.findForward("editCancelEventJustification");
	} catch (DomainException ex) {

	    addActionMessage(request, ex.getKey(), ex.getArgs());
	    request.setAttribute("cancelEventBean", cancelEventBean);

	    return mapping.findForward("editCancelEventJustification");
	}

	request.setAttribute("person", cancelEventBean.getEvent().getPerson());

	return mapping.findForward("showEvents");

    }

    public ActionForward openEvent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	try {
	    executeService("OpenEvent", new Object[] { getEvent(request) });
	} catch (DomainExceptionWithLabelFormatter ex) {
	    addActionMessage(request, ex.getKey(), solveLabelFormatterArgs(request, ex.getLabelFormatterArgs()));
	} catch (DomainException ex) {
	    addActionMessage(request, ex.getKey(), ex.getArgs());
	}

	request.setAttribute("personId", getEvent(request).getPerson().getIdInternal());

	return showEvents(mapping, form, request, response);

    }

    private CancelEventBean getCancelEventBean() {
	final CancelEventBean cancelEventBean = (CancelEventBean) getObjectFromViewState("cancelEventBean");
	return cancelEventBean;
    }

    public ActionForward backToShowEvents(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("person", getPerson(request));

	return mapping.findForward("showEvents");

    }

    protected Person getPerson(HttpServletRequest request) {
	return (Person) rootDomainObject.readPartyByOID(getIntegerFromRequest(request, "personId"));
    }

    private Event getEvent(HttpServletRequest request) {
	return rootDomainObject.readEventByOID(getRequestParameterAsInteger(request, "eventId"));
    }

    public ActionForward prepareTransferPaymentsToOtherEventAndCancel(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	final Event event = getEvent(request);

	final TransferPaymentsToOtherEventAndCancelBean transferPaymentsBean = new TransferPaymentsToOtherEventAndCancelBean(
		event, getLoggedPerson(request).getEmployee());

	request.setAttribute("transferPaymentsBean", transferPaymentsBean);

	return mapping.findForward("chooseTargetEventForPaymentsTransfer");

    }

    public ActionForward transferPaymentsToOtherEventAndCancel(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final TransferPaymentsToOtherEventAndCancelBean transferPaymentsBean = (TransferPaymentsToOtherEventAndCancelBean) getObjectFromViewState("transferPaymentsBean");

	try {
	    executeService("TransferPaymentsToOtherEventAndCancel", new Object[] { transferPaymentsBean.getEmployee(),
		    transferPaymentsBean.getSourceEvent(), transferPaymentsBean.getTargetEvent(),
		    transferPaymentsBean.getCancelJustification() });
	} catch (DomainExceptionWithLabelFormatter ex) {

	    addActionMessage(request, ex.getKey(), solveLabelFormatterArgs(request, ex.getLabelFormatterArgs()));
	    request.setAttribute("transferPaymentsBean", transferPaymentsBean);

	    return mapping.findForward("chooseTargetEventForPaymentsTransfer");
	} catch (DomainException ex) {

	    addActionMessage(request, ex.getKey(), ex.getArgs());
	    request.setAttribute("transferPaymentsBean", transferPaymentsBean);

	    return mapping.findForward("chooseTargetEventForPaymentsTransfer");
	}

	request.setAttribute("event", transferPaymentsBean.getSourceEvent());

	return mapping.findForward("showPaymentsForEvent");

    }

    public ActionForward prepareAnnulTransaction(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("annulAccountingTransactionBean", new AnnulAccountingTransactionBean(getTransaction(request)));

	return mapping.findForward("annulTransaction");

    }

    public ActionForward prepareAnnulTransactionInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("annulAccountingTransactionBean", getObjectFromViewState("annulAccountingTransactionBean"));

	return mapping.findForward("annulTransaction");
    }

    public ActionForward annulTransaction(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	final AnnulAccountingTransactionBean annulAccountingTransactionBean = (AnnulAccountingTransactionBean) getObjectFromViewState("annulAccountingTransactionBean");
	try {

	    executeService("AnnulAccountingTransaction", new Object[] { getLoggedPerson(request).getEmployee(),
		    annulAccountingTransactionBean });
	} catch (DomainExceptionWithLabelFormatter ex) {

	    addActionMessage(request, ex.getKey(), solveLabelFormatterArgs(request, ex.getLabelFormatterArgs()));
	    request.setAttribute("annulAccountingTransactionBean", annulAccountingTransactionBean);

	    return mapping.findForward("annulTransaction");
	} catch (DomainException ex) {

	    addActionMessage(request, ex.getKey(), ex.getArgs());
	    request.setAttribute("annulAccountingTransactionBean", annulAccountingTransactionBean);

	    return mapping.findForward("annulTransaction");
	}

	return showEvents(mapping, form, request, response);
    }

    private AccountingTransaction getTransaction(HttpServletRequest request) {
	return rootDomainObject.readAccountingTransactionByOID(getIntegerFromRequest(request, "transactionId"));
    }

    public ActionForward showOperations(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("person", getPerson(request));

	return mapping.findForward("showOperations");
    }

    public ActionForward showReceipts(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("person", getPerson(request));

	return mapping.findForward("showReceipts");
    }

    public ActionForward showReceipt(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("receipt", getReceipt(request));

	return mapping.findForward("showReceipt");
    }

    public ActionForward annulReceipt(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	try {
	    executeService("AnnulReceipt", new Object[] { getLoggedPerson(request).getEmployee(), getReceipt(request) });
	} catch (DomainExceptionWithLabelFormatter ex) {
	    addActionMessage(request, ex.getKey(), solveLabelFormatterArgs(request, ex.getLabelFormatterArgs()));
	} catch (DomainException ex) {
	    addActionMessage(request, ex.getKey(), ex.getArgs());
	}

	return showReceipts(mapping, form, request, response);
    }

    private Receipt getReceipt(HttpServletRequest request) {
	return rootDomainObject.readReceiptByOID(getIntegerFromRequest(request, "receiptId"));
    }

    @Override
    protected Map<String, String> getMessageResourceProviderBundleMappings() {
	final Map<String, String> bundleMappings = new HashMap<String, String>();
	bundleMappings.put("enum", "ENUMERATION_RESOURCES");
	bundleMappings.put("application", "DEFAULT");
	return bundleMappings;
    }

    public ActionForward prepareDepositAmount(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("depositAmountBean", new DepositAmountBean(getEvent(request)));

	return mapping.findForward("depositAmount");
    }

    public ActionForward prepareDepositAmountInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("depositAmountBean", getRenderedObject("depositAmountBean"));

	return mapping.findForward("depositAmount");
    }

    public ActionForward depositAmount(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final DepositAmountBean renderedObject = (DepositAmountBean) getRenderedObject("depositAmountBean");
	try {
	    executeService("DepositAmountOnEvent", new Object[] { renderedObject });
	} catch (DomainException e) {
	    addActionMessage(request, e.getKey(), e.getArgs());

	    request.setAttribute("depositAmountBean", renderedObject);

	    return mapping.findForward("depositAmount");
	}

	return showEvents(mapping, form, request, response);
    }
}
