package net.sourceforge.fenixedu.presentationTier.Action.residenceManagement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.residenceManagement.ImportResidenceEventBean;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.residence.ResidenceMonth;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.struts.annotations.Forward;
import net.sourceforge.fenixedu.presentationTier.struts.annotations.Forwards;
import net.sourceforge.fenixedu.presentationTier.struts.annotations.Mapping;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

@Mapping(path = "/residenceEventManagement", module = "residenceManagement")
@Forwards( { @Forward(name = "manageResidenceEvents", path = "residenceManagement-events-management") })
public class ResidenceEventManagementDispatchAction extends FenixDispatchAction {

    public ActionForward manageResidenceEvents(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	ImportResidenceEventBean importResidenceEventBean = (ImportResidenceEventBean) getRenderedObject("searchEventMonth");
	if (importResidenceEventBean == null) {
	    importResidenceEventBean = new ImportResidenceEventBean();
	}

	RenderUtils.invalidateViewState();
	request.setAttribute("searchBean", importResidenceEventBean);
	return mapping.findForward("manageResidenceEvents");
    }

    public ActionForward generatePaymentCodes(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	long oid = Long.valueOf(request.getParameter("selectedMonth"));
	ResidenceMonth month = (ResidenceMonth) DomainObject.fromOID(oid);
	executeService("CreateResidencePaymentCodes", new Object[] { month.getEvents() });

	return manageResidenceEvents(mapping, actionForm, request, response);
    }

}
