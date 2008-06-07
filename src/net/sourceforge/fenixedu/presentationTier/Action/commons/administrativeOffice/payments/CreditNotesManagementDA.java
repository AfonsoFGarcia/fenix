package net.sourceforge.fenixedu.presentationTier.Action.commons.administrativeOffice.payments;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.accounting.CreateCreditNoteBean;
import net.sourceforge.fenixedu.domain.accounting.CreditNote;
import net.sourceforge.fenixedu.domain.accounting.CreditNoteState;
import net.sourceforge.fenixedu.domain.accounting.Receipt;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.DomainExceptionWithLabelFormatter;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public abstract class CreditNotesManagementDA extends PaymentsManagementDispatchAction {

    public ActionForward showCreditNotes(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("receipt", getReceiptFromViewState("receipt"));

	return mapping.findForward("list");
    }

    private Receipt getReceiptFromViewState(String viewStateId) {
	return (Receipt) getObjectFromViewState(viewStateId);
    }

    public ActionForward showCreditNote(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("creditNote", getCreditNote(request));
	((DynaActionForm) form).set("creditNoteState", getCreditNote(request).getState().name());

	return mapping.findForward("show");
    }

    private CreditNote getCreditNote(HttpServletRequest request) {
	return rootDomainObject.readCreditNoteByOID(getIntegerFromRequest(request, "creditNoteId"));
    }

    public ActionForward prepareCreateCreditNote(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	request.setAttribute("createCreditNoteBean", new CreateCreditNoteBean(
		getReceiptFromViewState("receipt")));

	return mapping.findForward("create");

    }

    public ActionForward createCreditNote(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	final CreateCreditNoteBean createCreditNoteBean = (CreateCreditNoteBean) RenderUtils
		.getViewState("create-credit-note").getMetaObject().getObject();

	try {
	    executeService("CreateCreditNote", new Object[] {
		    getUserView(request).getPerson().getEmployee(), createCreditNoteBean });

	} catch (DomainExceptionWithLabelFormatter ex) {
	    addActionMessage(request, ex.getKey(), solveLabelFormatterArgs(request, ex
		    .getLabelFormatterArgs()));
	    request.setAttribute("createCreditNoteBean", createCreditNoteBean);
	    return mapping.findForward("create");

	} catch (DomainException ex) {
	    addActionMessage(request, ex.getKey());
	    request.setAttribute("createCreditNoteBean", createCreditNoteBean);
	    return mapping.findForward("create");

	}

	request.setAttribute("receipt", createCreditNoteBean.getReceipt());

	return mapping.findForward("list");

    }

    public ActionForward changeCreditNoteState(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {
	final CreditNote creditNote = getCreditNoteFromViewState();
	final CreditNoteState creditNoteState = CreditNoteState.valueOf(((DynaActionForm) form)
		.getString("creditNoteState"));

	try {
	    executeService("ChangeCreditNoteState", new Object[] {
		    getUserView(request).getPerson().getEmployee(), creditNote, creditNoteState });

	} catch (DomainExceptionWithLabelFormatter ex) {
	    addActionMessage(request, ex.getKey(), solveLabelFormatterArgs(request, ex
		    .getLabelFormatterArgs()));
	} catch (DomainException ex) {
	    addActionMessage(request, ex.getKey());
	}

	request.setAttribute("creditNote", creditNote);

	return mapping.findForward("show");

    }

    private CreditNote getCreditNoteFromViewState() {
	final CreditNote creditNote = (CreditNote) RenderUtils.getViewState("creditNote")
		.getMetaObject().getObject();
	return creditNote;
    }

    public ActionForward printCreditNote(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("creditNote", getCreditNoteFromViewState());
	request.setAttribute("currentUnit", getCurrentUnit(request));

	return mapping.findForward("print");
    }

    public ActionForward prepareShowReceipt(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	return mapping.findForward("prepareShowReceipt");

    }

}
