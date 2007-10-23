package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationSelectExecutionYearBean;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person.PersonBeanFactoryEditor;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.student.registrationStates.ConcludedState.RegistrationConcludedStateCreator;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class StudentDA extends FenixDispatchAction {

    private Student getStudent(final HttpServletRequest request) {
	final String studentID = request.getParameter("studentID");
	final Student student = rootDomainObject.readStudentByOID(Integer.valueOf(studentID));
	request.setAttribute("student", student);
	return student;
    }

    private Registration getRegistration(final HttpServletRequest request) {
	final Integer registrationID = getIntegerFromRequest(request, "registrationID") != null ? getIntegerFromRequest(request, "registrationID") : getIntegerFromRequest(request, "registrationId"); 
	final Registration registration = rootDomainObject.readRegistrationByOID(Integer.valueOf(registrationID));
	request.setAttribute("registration", registration);
	return registration;
    }

    public ActionForward prepareEditPersonalData(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	final Student student = getStudent(request);

	final Employee employee = student.getPerson().getEmployee();
	if (employee != null && employee.getCurrentWorkingContract() != null) {
	    addActionMessage(request, "message.student.personIsEmployee");
	    return mapping.findForward("viewStudentDetails");
	}

	request.setAttribute("personBean", new PersonBeanFactoryEditor(student.getPerson()));
	return mapping.findForward("editPersonalData");
    }

    public ActionForward editPersonalData(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {
	getStudent(request);
	executeFactoryMethod(request);
	RenderUtils.invalidateViewState();
	addActionMessage(request, "message.student.personDataEditedWithSuccess");
	return mapping.findForward("viewStudentDetails");
    }
    
    public ActionForward viewPersonalData(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("personBean", new PersonBeanFactoryEditor(getStudent(request).getPerson()));
	return mapping.findForward("viewPersonalData");
    }

    public ActionForward visualizeRegistration(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	getRegistration(request);
	return mapping.findForward("viewRegistrationDetails");
    }

    public ActionForward visualizeStudent(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	getStudent(request);
	return mapping.findForward("viewStudentDetails");
    }

    public ActionForward viewRegistrationCurriculum(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	RegistrationSelectExecutionYearBean bean = (RegistrationSelectExecutionYearBean) getRenderedObject();
	if (bean == null) {
	    bean = new RegistrationSelectExecutionYearBean(getRegistration(request));
	    final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
	    if (bean.getRegistration().hasAnyEnrolmentsIn(currentExecutionYear)) {
		bean.setExecutionYear(currentExecutionYear);
	    }
	}
	
	final Integer degreeCurricularPlanID = getIntegerFromRequest(request, "degreeCurricularPlanID");
	if (degreeCurricularPlanID != null) {
	    request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);
	}
	
	request.setAttribute("bean", bean);
	return mapping.findForward("view-registration-curriculum");
    }

    public ActionForward prepareRegistrationConclusionProcess(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	RenderUtils.invalidateViewState();
	
	final Registration registration = getRegistration(request);
	request.setAttribute("registrationStateBean", new RegistrationConcludedStateCreator(registration));
	
	return mapping.findForward("registrationConclusion");
    }

    public ActionForward prepareRegistrationConclusionDocument(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	RenderUtils.invalidateViewState();
	
	request.setAttribute("registration", getRegistration(request));
	
	return mapping.findForward("registrationConclusionDocument");
    }

}
