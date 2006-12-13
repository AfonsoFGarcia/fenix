package net.sourceforge.fenixedu.presentationTier.Action.student.administrativeOfficeServices;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.serviceRequest.documentRequest.DocumentRequestCreateBean;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.DomainExceptionWithLabelFormatter;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentPurposeType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequestType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

public class DocumentRequestDispatchAction extends FenixDispatchAction {

    public ActionForward chooseRegistration(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("registrations", getLoggedPerson(request).getStudent().getRegistrations());

	return mapping.findForward("chooseRegistration");
    }

    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	final Registration registration = getRegistration(request, actionForm);

	if (!registration.getPayedTuition()) {
	    addActionMessage(request, "error.message.tuitionNotPayed");
	} else {
	    if (!registration.hasAnyStudentCurricularPlans()) {
		addActionMessage(request, "error.student.curricularPlan.nonExistent");
	    } else {
		request.setAttribute("registration", registration);

		if (registration.getStudentCurricularPlansCount() > 1) {
		    request.setAttribute("studentCurricularPlans", registration
			    .getStudentCurricularPlans());
		}

		request.setAttribute("executionYears", registration.getEnrolmentsExecutionYears());
	    }
	}
       
        return mapping.findForward("createDocumentRequests");
    }

    private Registration getRegistration(final HttpServletRequest request, final ActionForm actionForm) {
	return rootDomainObject.readRegistrationByOID(getIntegerFromRequestOrForm(request,
		(DynaActionForm) actionForm, "registrationId"));
    }

    public ActionForward onSCPChange(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("registration", getRegistration(request, actionForm));
	getAndSetExecutionYears(request, getAndSetStudentCurricularPlans(request,
		(DynaActionForm) actionForm));

	return mapping.findForward("createDocumentRequests");
    }

    private void getAndSetExecutionYears(HttpServletRequest request,
	    StudentCurricularPlan studentCurricularPlan) {
	if (studentCurricularPlan.getEnrolmentsExecutionYears().isEmpty()) {
	    addActionMessage(request, "message.no.enrolments");
	} else {
	    request.setAttribute("executionYears", studentCurricularPlan.getEnrolmentsExecutionYears());
	}
    }
    
    private StudentCurricularPlan getAndSetStudentCurricularPlans(HttpServletRequest request,
	    final DynaActionForm actionForm) {
	final Registration registration = getRegistration(request, actionForm);
	if (registration.getStudentCurricularPlansCount() > 1) {
	    request.setAttribute("studentCurricularPlans", registration.getStudentCurricularPlans());

	    return rootDomainObject.readStudentCurricularPlanByOID((Integer) actionForm.get("scpId"));
	}
	return registration.getActiveStudentCurricularPlan();

    }


    public ActionForward viewDocumentRequestsToCreate(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("student", getRegistration(request, actionForm));
	final DynaActionForm dynaActionForm = (DynaActionForm) actionForm;

	getAndSetExecutionYears(request, getAndSetStudentCurricularPlans(request, dynaActionForm));

	final List<String> chosenDocumentRequestTypes = getAndSetChosenDocumentRequestTypes(request,
		dynaActionForm);
	final DocumentPurposeType chosenDocumentPurposeType = getAndSetChoseDocumentPurposeType(request,
		dynaActionForm);
	final String otherPurpose = getAndSetOtherPurpose(request, dynaActionForm,
		chosenDocumentPurposeType);
	if (hasActionMessage(request)) {
	    return mapping.findForward("createDocumentRequests");
	}

	getAndSetDocumentRequestCreateBeans(request, dynaActionForm, getAndSetStudentCurricularPlans(
		request, dynaActionForm), chosenDocumentRequestTypes, chosenDocumentPurposeType,
		otherPurpose, dynaActionForm.getString("notes"), Boolean.valueOf(dynaActionForm
			.getString("urgentRequest")));

	return mapping.findForward("viewDocumentRequestsToCreate");
    }

    private List<String> getAndSetChosenDocumentRequestTypes(HttpServletRequest request,
	    final DynaActionForm dynaActionForm) {
	final List<String> chosenDocumentRequestTypes = Arrays.asList((String[]) dynaActionForm
		.get("chosenDocumentRequestTypes"));
	if (chosenDocumentRequestTypes.isEmpty()) {
	    addActionMessage(request, "error.choose.one.type.of.document");
	}
	request.setAttribute("chosenDocumentRequestTypes", chosenDocumentRequestTypes);
	return chosenDocumentRequestTypes;
    }

    private DocumentPurposeType getAndSetChoseDocumentPurposeType(HttpServletRequest request,
	    final DynaActionForm dynaActionForm) {
	final DocumentPurposeType chosenDocumentPurposeType = (!StringUtils.isEmpty(dynaActionForm
		.getString("chosenDocumentPurposeType"))) ? DocumentPurposeType.valueOf(dynaActionForm
		.getString("chosenDocumentPurposeType")) : null;
	request.setAttribute("chosenDocumentPurposeType", chosenDocumentPurposeType);
	return chosenDocumentPurposeType;
    }

    private String getAndSetOtherPurpose(HttpServletRequest request,
	    final DynaActionForm dynaActionForm, final DocumentPurposeType chosenDocumentPurposeType) {
	final String otherPurpose = dynaActionForm.getString("otherPurpose");
	if (chosenDocumentPurposeType == DocumentPurposeType.OTHER) {
	    if (otherPurpose == null) {
		addActionMessage(request, "error.fill.notes");
	    }
	} else if (chosenDocumentPurposeType != null) {
	    if (!StringUtils.isEmpty(otherPurpose)) {
		addActionMessage(request, "error.only.one.purpose");
	    }
	}
	request.setAttribute("otherPurpose", otherPurpose);
	return otherPurpose;
    }

    private void getAndSetDocumentRequestCreateBeans(HttpServletRequest request,
	    final DynaActionForm dynaActionForm, StudentCurricularPlan studentCurricularPlan,
	    final List<String> chosenDocumentRequestTypes,
	    final DocumentPurposeType chosenDocumentPurposeType, final String otherPurpose,
	    final String notes, final Boolean urgentRequest) {
	final List<DocumentRequestCreateBean> documentRequestCreateBeans = new ArrayList<DocumentRequestCreateBean>();

	for (final String chosenDocumentRequestType : chosenDocumentRequestTypes) {
	    final DocumentRequestType documentRequestType = DocumentRequestType
		    .valueOf(chosenDocumentRequestType);

	    final DocumentRequestCreateBean documentRequestCreateBean = buildDocumentRequestCreateBean(
		    dynaActionForm, studentCurricularPlan, chosenDocumentPurposeType, otherPurpose,
		    notes, urgentRequest, documentRequestType, request);

	    documentRequestCreateBeans.add(documentRequestCreateBean);
	}

	request.setAttribute("documentRequestCreateBeans", documentRequestCreateBeans);
    }

    private DocumentRequestCreateBean buildDocumentRequestCreateBean(
	    final DynaActionForm dynaActionForm, final StudentCurricularPlan studentCurricularPlan,
	    final DocumentPurposeType chosenDocumentPurposeType, final String otherPurpose,
	    final String notes, final Boolean urgentRequest,
	    final DocumentRequestType documentRequestType, final HttpServletRequest request) {

       final DocumentRequestCreateBean documentRequestCreateBean = new DocumentRequestCreateBean(
		getRegistration(request, dynaActionForm));
		
	documentRequestCreateBean.setToBeCreated(Boolean.TRUE);
	documentRequestCreateBean.getRegistration();
	documentRequestCreateBean.setChosenDocumentRequestType(documentRequestType);
	documentRequestCreateBean.setChosenDocumentPurposeType(chosenDocumentPurposeType);
	documentRequestCreateBean.setOtherPurpose(otherPurpose);
	documentRequestCreateBean.setNotes(notes);
	documentRequestCreateBean.setUrgentRequest(urgentRequest);

	final Boolean average;
	final Boolean detailed;
	final ExecutionYear executionYear;
	if (documentRequestType == DocumentRequestType.SCHOOL_REGISTRATION_CERTIFICATE) {
	    average = null;
	    detailed = null;

	    Integer executionYearId = (Integer) dynaActionForm.get("schoolRegistrationExecutionYearId");
	    executionYear = rootDomainObject.readExecutionYearByOID(executionYearId);
	} else if (documentRequestType == DocumentRequestType.ENROLMENT_CERTIFICATE) {
	    average = null;
	    detailed = Boolean.valueOf(dynaActionForm.getString("enrolmentDetailed"));

	    Integer executionYearId = (Integer) dynaActionForm.get("enrolmentExecutionYearId");
	    executionYear = rootDomainObject.readExecutionYearByOID(executionYearId);
	} else if (documentRequestType == DocumentRequestType.DEGREE_FINALIZATION_CERTIFICATE) {
	    average = Boolean.valueOf(dynaActionForm.getString("degreeFinalizationAverage"));
	    detailed = Boolean.valueOf(dynaActionForm.getString("degreeFinalizationDetailed"));
	    executionYear = null;
	} else {
	    average = null;
	    executionYear = null;
	    detailed = null;
	}
	documentRequestCreateBean.setAverage(average);
	documentRequestCreateBean.setDetailed(detailed);
	documentRequestCreateBean.setExecutionYear(executionYear);

	if (documentRequestCreateBean.hasWarningsToReport()) {
	    request.setAttribute("warningsToReport", documentRequestCreateBean.getWarningsToReport());
	}

	return documentRequestCreateBean;
    }

    public ActionForward create(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {
	final Object[] args = { getConfirmedDocumentRequestCreateBeans() };
	final List<String> messages = (List<String>) executeService(request, "CreateDocumentRequests",
		args);
	for (final String message : messages) {
	    final ActionMessages actionMessages = new ActionMessages();
	    actionMessages.add(message, new ActionMessage(message));
	    saveMessages(request, actionMessages);
	}

	return mapping.findForward("createSuccess");
    }

    private List<DocumentRequestCreateBean> getConfirmedDocumentRequestCreateBeans() {
        final List<DocumentRequestCreateBean> result = new ArrayList<DocumentRequestCreateBean>();

        for (final DocumentRequestCreateBean documentRequestCreateBean : (List<DocumentRequestCreateBean>) RenderUtils
                .getViewState().getMetaObject().getObject()) {

            if (documentRequestCreateBean.getToBeCreated()) {

                result.add(documentRequestCreateBean);
            }
        }
        return result;
    }

    public ActionForward viewDocumentRequests(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("student", getLoggedPerson(request).getStudent());
	request.setAttribute("documentRequests", getDocumentRequest(request));
	return mapping.findForward("viewDocumentRequests");
    }

    private List<DocumentRequest> getDocumentRequest(final HttpServletRequest request) {

	final List<DocumentRequest> result = new ArrayList<DocumentRequest>();
	for (final Registration registration : getLoggedPerson(request).getStudent()
		.getRegistrationsSet()) {
	    result.addAll(registration.getDocumentRequests());
	}
	return result;
    }

    public ActionForward viewDocumentRequest(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("documentRequest", rootDomainObject
		.readAcademicServiceRequestByOID(getRequestParameterAsInteger(request,
			"documentRequestId")));

	return mapping.findForward("viewDocumentRequest");
    }

    public ActionForward prepareCancelAcademicServiceRequest(ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)
            throws FenixFilterException, FenixServiceException {

        getAndSetAcademicServiceRequest(request);
        return mapping.findForward("prepareCancelAcademicServiceRequest");
    }

    public ActionForward cancelAcademicServiceRequest(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
            FenixServiceException {

        final AcademicServiceRequest academicServiceRequest = getAndSetAcademicServiceRequest(request);
        final String justification = ((DynaActionForm) actionForm).getString("justification");
        final String message = null;

        try {
            executeService("CancelAcademicServiceRequest", academicServiceRequest, justification);
        } catch (DomainExceptionWithLabelFormatter ex) {
            addActionMessage(request, ex.getKey(), solveLabelFormatterArgs(request, ex
                    .getLabelFormatterArgs()));
            return mapping.findForward("prepareCancelAcademicServiceRequest");
        } catch (DomainException ex) {
            addActionMessage(request, ex.getKey());
            return mapping.findForward("prepareCancelAcademicServiceRequest");
        }
       
        
       // return viewDocumentRequests(mapping, actionForm, request, response);
        return mapping.findForward("cancelSuccess");
    }

    private AcademicServiceRequest getAndSetAcademicServiceRequest(final HttpServletRequest request) {
        final AcademicServiceRequest academicServiceRequest = rootDomainObject
                .readAcademicServiceRequestByOID(getRequestParameterAsInteger(request,
                        "academicServiceRequestId"));
        request.setAttribute("academicServiceRequest", academicServiceRequest);
        return academicServiceRequest;
    }

}
