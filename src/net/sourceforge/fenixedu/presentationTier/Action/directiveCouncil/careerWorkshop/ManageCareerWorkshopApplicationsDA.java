package net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil.careerWorkshop;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.careerWorkshop.CareerWorkshopApplicationEvent;
import net.sourceforge.fenixedu.domain.careerWorkshop.CareerWorkshopSpreadsheet;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

@Mapping(path="/careerWorkshopApplication", module="directiveCouncil")
@Forwards({@Forward(name="manageCareerWorkshops", path="/directiveCouncil/careerWorkshop/manageCareerWorkshops.jsp")})
public class ManageCareerWorkshopApplicationsDA extends FenixDispatchAction {
    
    public ActionForward prepare(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	
	ManageCareerWorkshopApplicationsBean applicationsBean = new ManageCareerWorkshopApplicationsBean();
	request.setAttribute("applicationsBean", applicationsBean);
	
	return actionMapping.findForward("manageCareerWorkshops");
    }
    
    
    public ActionForward addApplicationEvent(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	
	ManageCareerWorkshopApplicationsBean applicationsBean = getRenderedObject("applicationsBean");
	applicationsBean.addNewEvent();
	
	request.setAttribute("applicationsBean", applicationsBean);
	RenderUtils.invalidateViewState("applicationsBean");
	
	return actionMapping.findForward("manageCareerWorkshops");
    }
    
    
    public ActionForward deleteApplicationEvent(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	
	ManageCareerWorkshopApplicationsBean applicationsBean = new ManageCareerWorkshopApplicationsBean();
	final String eventExternalId = request.getParameter("eventId");
	CareerWorkshopApplicationEvent eventToDelete = AbstractDomainObject.fromExternalId(eventExternalId);
	applicationsBean.deleteEvent(eventToDelete);
	
	
	request.setAttribute("applicationsBean", applicationsBean);
	RenderUtils.invalidateViewState("applicationsBean");
	
	return actionMapping.findForward("manageCareerWorkshops");
    }
    
    
    public ActionForward downloadApplications(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	
	ManageCareerWorkshopApplicationsBean applicationsBean = new ManageCareerWorkshopApplicationsBean();
	final String eventExternalId = request.getParameter("eventId");
	CareerWorkshopApplicationEvent eventToDownload = AbstractDomainObject.fromExternalId(eventExternalId);
	CareerWorkshopSpreadsheet spreadsheet = eventToDownload.getApplications();
	if (spreadsheet != null) {
	    final ServletOutputStream writer = response.getOutputStream();
	    try {
		response.setContentLength(spreadsheet.getSize());
		response.setContentType("application/csv");
		response.addHeader("Content-Disposition", "attachment; filename=" + spreadsheet.getFilename());
		writer.write(spreadsheet.getContents());
		writer.flush();
	    } finally {
		writer.close();
	    }
	}
	return null;
	/*
	request.setAttribute("applicationsBean", applicationsBean);
	RenderUtils.invalidateViewState("applicationsBean");
	
	return actionMapping.findForward("manageCareerWorkshops");*/
    }

}
