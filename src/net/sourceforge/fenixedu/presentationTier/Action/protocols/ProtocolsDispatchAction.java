package net.sourceforge.fenixedu.presentationTier.Action.protocols;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.protocol.ProtocolSearch;
import net.sourceforge.fenixedu.dataTransferObject.protocol.ProtocolHistoryFactory.ProtocolHistoryEditorFactory;
import net.sourceforge.fenixedu.dataTransferObject.protocol.ProtocolHistoryFactory.ProtocolHistoryRenewerFactory;
import net.sourceforge.fenixedu.domain.protocols.Protocol;
import net.sourceforge.fenixedu.domain.protocols.ProtocolHistory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.LanguageUtils;
import net.sourceforge.fenixedu.util.report.StyledExcelSpreadsheet;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.joda.time.Months;
import org.joda.time.YearMonthDay;

public class ProtocolsDispatchAction extends FenixDispatchAction {

    public ActionForward showProtocols(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	List<Protocol> protocolList = new ArrayList<Protocol>();
	ProtocolSearch protocolSearch = (ProtocolSearch) getRenderedObject();
	if (protocolSearch == null) {
	    protocolSearch = new ProtocolSearch();
	}
	if (protocolSearch.isActives() || protocolSearch.isInactives()) {
	    for (Protocol protocol : rootDomainObject.getProtocols()) {
		if (protocolSearch.satisfiedActivity(protocol)) {
		    protocolList.add(protocol);
		}
	    }
	}
	request.setAttribute("protocolSearch", protocolSearch);
	request.setAttribute("protocols", protocolList);
	return mapping.findForward("show-protocols");
    }

    public ActionForward showProtocolAlerts(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	YearMonthDay now = new YearMonthDay();
	List<ProtocolHistory> protocolHistories = new ArrayList<ProtocolHistory>();
	List<ProtocolHistory> protocolHistoriesNullEndDate = new ArrayList<ProtocolHistory>();
	for (Protocol protocol : rootDomainObject.getProtocols()) {
	    ProtocolHistory protocolHistory = protocol.getLastProtocolHistory();
	    if (protocolHistory.getEndDate() == null) {
		if (protocol.isActive()) {
		    protocolHistoriesNullEndDate.add(protocolHistory);
		}
	    } else if (!protocolHistory.getEndDate().isBefore(now)
		    && Months.monthsBetween(now, protocolHistory.getEndDate()).getMonths() <= 1) {
		protocolHistories.add(protocolHistory);
	    }
	}
	Collections.sort(protocolHistories, new BeanComparator("endDate"));
	request.setAttribute("protocolHistories", protocolHistories);
	request.setAttribute("protocolHistoriesNullEndDate", protocolHistoriesNullEndDate);
	return mapping.findForward("show-protocol-alerts");
    }

    public ActionForward prepareRenewProtocol(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	final Integer protocolId = new Integer(request.getParameter("idInternal"));
	final Protocol protocol = rootDomainObject.readProtocolByOID(protocolId);
	ProtocolHistoryRenewerFactory protocolHistoryFactory = new ProtocolHistoryRenewerFactory(
		protocol);
	request.setAttribute("protocolHistoryFactory", protocolHistoryFactory);
	return mapping.findForward("renew-protocol");
    }

    public ActionForward renewProtocol(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	if (isCancelled(request)) {
	    return showProtocolAlerts(mapping, actionForm, request, response);
	}
	ProtocolHistoryRenewerFactory protocolHistoryFactory = (ProtocolHistoryRenewerFactory) getRenderedObject("protocolHistoryFactory");
	ActionMessage actionMessage = (ActionMessage) executeService(request, "ExecuteFactoryMethod",
		new Object[] { protocolHistoryFactory });
	if (actionMessage != null) {
	    setError(request, "message", actionMessage);
	    request.setAttribute("protocolHistoryFactory", protocolHistoryFactory);
	    return mapping.findForward("renew-protocol");
	}
	request.setAttribute("protocols", rootDomainObject.getProtocols());
	return showProtocolAlerts(mapping, actionForm, request, response);
    }

    public ActionForward prepareEditProtocolHistory(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	final Integer protocolId = new Integer(request.getParameter("idInternal"));
	final Protocol protocol = rootDomainObject.readProtocolByOID(protocolId);
	ProtocolHistoryEditorFactory protocolHistoryFactory = new ProtocolHistoryEditorFactory(protocol);
	request.setAttribute("protocolHistoryFactory", protocolHistoryFactory);
	return mapping.findForward("edit-protocol-history");
    }

    public ActionForward editProtocolHistory(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	if (isCancelled(request)) {
	    return showProtocolAlerts(mapping, actionForm, request, response);
	}
	ProtocolHistoryEditorFactory protocolHistoryFactory = (ProtocolHistoryEditorFactory) getRenderedObject("protocolHistoryFactory");
	ActionMessage actionMessage = (ActionMessage) executeService(request, "ExecuteFactoryMethod",
		new Object[] { protocolHistoryFactory });
	if (actionMessage != null) {
	    setError(request, "message", actionMessage);
	    request.setAttribute("protocolHistoryFactory", protocolHistoryFactory);
	    return mapping.findForward("edit-protocol-history");
	}
	request.setAttribute("protocols", rootDomainObject.getProtocols());
	return showProtocolAlerts(mapping, actionForm, request, response);
    }

    public ActionForward searchProtocols(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	ProtocolSearch protocolSearch = (ProtocolSearch) getRenderedObject("protocolSearch");
	if (protocolSearch == null) {
	    protocolSearch = new ProtocolSearch();
	}
	request.setAttribute("protocolSearch", protocolSearch);
	return mapping.findForward("search-protocols");
    }

    public ActionForward exportProtocols(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	ProtocolSearch protocolSearch = (ProtocolSearch) getRenderedObject("protocolSearch");
	if (protocolSearch == null) {
	    protocolSearch = new ProtocolSearch();
	}
	response.setContentType("text/plain");
	response.setHeader("Content-disposition", "attachment; filename=protocolos.xls");
	final ResourceBundle bundle = ResourceBundle.getBundle("resources.ScientificCouncilResources",
		LanguageUtils.getLocale());
	StyledExcelSpreadsheet spreadsheet = new StyledExcelSpreadsheet(bundle
		.getString("label.protocols.navigation.header"));
	Protocol.getExcelHeader(spreadsheet, bundle);
	List<Protocol> protocolList = protocolSearch.getSearch();
	for (Protocol protocol : protocolList) {
	    protocol.getExcelRow(spreadsheet);
	}
	final ServletOutputStream writer = response.getOutputStream();
	spreadsheet.getWorkbook().write(writer);
	writer.flush();
	response.flushBuffer();
	return null;
    }

    private void setError(HttpServletRequest request, String error, ActionMessage actionMessage) {
	ActionMessages actionMessages = getMessages(request);
	actionMessages.add(error, actionMessage);
	saveMessages(request, actionMessages);
    }
}