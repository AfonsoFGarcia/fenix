package net.sourceforge.fenixedu.presentationTier.Action.phd.academicAdminOffice.serviceRequests;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.exceptions.PhdDomainOperationException;
import net.sourceforge.fenixedu.domain.phd.serviceRequests.PhdAcademicServiceRequest;
import net.sourceforge.fenixedu.domain.phd.serviceRequests.PhdAcademicServiceRequestBean;
import net.sourceforge.fenixedu.domain.phd.serviceRequests.PhdAcademicServiceRequestCreateBean;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;
import net.sourceforge.fenixedu.presentationTier.Action.phd.PhdDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/phdAcademicServiceRequestManagement", module = "academicAdminOffice")
@Forwards( {
	@Forward(name = "prepareCreateNewRequest", path = "/phd/academicAdminOffice/serviceRequests/prepareCreateNewRequest.jsp"),
	@Forward(name = "listAcademicServiceRequests", path = "/phd/academicAdminOffice/serviceRequests/listAcademicServiceRequests.jsp"),
	@Forward(name = "viewRequest", path = "/phd/academicAdminOffice/serviceRequests/viewRequest.jsp"),
	@Forward(name = "prepareProcessNewState", path = "/phd/academicAdminOffice/serviceRequests/prepareProcessNewState.jsp") })
public class PhdAcademicServiceRequestsManagementDA extends PhdDA {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	request.setAttribute("phdAcademicServiceRequest", getPhdAcademicServiceRequest(request));
	request.setAttribute("phdIndividualProgramProcess", getPhdIndividualProgramProcess(request));

	if (getPhdAcademicServiceRequest(request) != null) {
	    request.setAttribute("phdIndividualProgramProcess", getPhdAcademicServiceRequest(request)
		    .getPhdIndividualProgramProcess());
	}

	return super.execute(mapping, actionForm, request, response);
    }

    public ActionForward listAcademicServiceRequests(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	PhdIndividualProgramProcess process = getPhdIndividualProgramProcess(request);
	request.setAttribute("phdIndividualProgramProcess", process);

	return mapping.findForward("listAcademicServiceRequests");
    }

    public ActionForward viewAcademicServiceRequest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return mapping.findForward("viewRequest");
    }

    public ActionForward prepareCreateNewRequest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	PhdIndividualProgramProcess process = getPhdIndividualProgramProcess(request);
	PhdAcademicServiceRequestCreateBean academicServiceRequestCreateBean = new PhdAcademicServiceRequestCreateBean(process);

	request.setAttribute("phdAcademicServiceRequestCreateBean", academicServiceRequestCreateBean);

	return mapping.findForward("prepareCreateNewRequest");
    }

    public ActionForward createNewRequest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	try {
	    PhdAcademicServiceRequestCreateBean academicServiceRequestCreateBean = getPhdAcademicServiceRequestCreateBean();
	    academicServiceRequestCreateBean.createNewRequest();
	} catch (PhdDomainOperationException exception) {
	    addErrorMessage(request, exception.getMessage(), new String[0]);
	    return prepareCreateNewRequest(mapping, form, request, response);
	}

	return listAcademicServiceRequests(mapping, form, request, response);
    }

    public ActionForward createNewRequestInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	PhdAcademicServiceRequestCreateBean academicServiceRequestCreateBean = getPhdAcademicServiceRequestCreateBean();
	request.setAttribute("phdAcademicServiceRequestCreateBean", academicServiceRequestCreateBean);

	return mapping.findForward("prepareCreateNewRequest");
    }

    public ActionForward prepareProcessServiceRequest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return prepareProcessNewState(mapping, form, request, response, AcademicServiceRequestSituationType.PROCESSING);
    }

    public ActionForward processServiceRequest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	PhdAcademicServiceRequestBean academicServiceRequestBean = getPhdAcademicServiceRequestBean();
	academicServiceRequestBean.handleNewSituation();

	return viewAcademicServiceRequest(mapping, form, request, response);
    }

    public ActionForward prepareCancelServiceRequest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return prepareProcessNewState(mapping, form, request, response, AcademicServiceRequestSituationType.CANCELLED);
    }

    public ActionForward cancelServiceRequest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	PhdAcademicServiceRequestBean academicServiceRequestBean = getPhdAcademicServiceRequestBean();
	academicServiceRequestBean.handleNewSituation();

	return viewAcademicServiceRequest(mapping, form, request, response);
    }

    public ActionForward prepareRejectServiceRequest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return prepareProcessNewState(mapping, form, request, response, AcademicServiceRequestSituationType.REJECTED);
    }

    public ActionForward rejectServiceRequest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	PhdAcademicServiceRequestBean academicServiceRequestBean = getPhdAcademicServiceRequestBean();
	academicServiceRequestBean.handleNewSituation();

	return viewAcademicServiceRequest(mapping, form, request, response);
    }

    public ActionForward prepareConcludeServiceRequest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return prepareProcessNewState(mapping, form, request, response, AcademicServiceRequestSituationType.CONCLUDED);
    }

    public ActionForward concludeServiceRequest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	PhdAcademicServiceRequestBean academicServiceRequestBean = getPhdAcademicServiceRequestBean();
	academicServiceRequestBean.handleNewSituation();

	return viewAcademicServiceRequest(mapping, form, request, response);
    }

    private ActionForward prepareProcessNewState(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response, AcademicServiceRequestSituationType situationType) {
	PhdAcademicServiceRequest academicServiceRequest = getPhdAcademicServiceRequest(request);
	PhdAcademicServiceRequestBean academicServiceRequestBean = new PhdAcademicServiceRequestBean(academicServiceRequest);

	academicServiceRequestBean.setSituationType(situationType);
	request.setAttribute("phdAcademicServiceRequestBean", academicServiceRequestBean);

	return mapping.findForward("prepareProcessNewState");
    }

    public ActionForward processNewState(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("phdAcademicServiceRequestBean", getPhdAcademicServiceRequestBean());
	return mapping.findForward("prepareProcessNewState");
    }

    private PhdAcademicServiceRequestBean getPhdAcademicServiceRequestBean() {
	return (PhdAcademicServiceRequestBean) getObjectFromViewState("phd-academic-service-request-bean");
    }

    private PhdIndividualProgramProcess getPhdIndividualProgramProcess(final HttpServletRequest request) {
	return (PhdIndividualProgramProcess) getDomainObject(request, "phdIndividualProgramProcessId");
    }

    private PhdAcademicServiceRequestCreateBean getPhdAcademicServiceRequestCreateBean() {
	return (PhdAcademicServiceRequestCreateBean) getObjectFromViewState("phd-academic-service-request-create-bean");
    }

    private PhdAcademicServiceRequest getPhdAcademicServiceRequest(final HttpServletRequest request) {
	return (PhdAcademicServiceRequest) getDomainObject(request, "phdAcademicServiceRequestId");
    }
}
