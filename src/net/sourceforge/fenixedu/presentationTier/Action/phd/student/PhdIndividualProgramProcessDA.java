package net.sourceforge.fenixedu.presentationTier.Action.phd.student;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.alert.PhdAlertMessage;
import net.sourceforge.fenixedu.presentationTier.Action.phd.PhdProcessDA;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/phdIndividualProgramProcess", module = "student")
@Forwards( {

@Forward(name = "viewProcess", path = "/phd/student/viewProcess.jsp"),

@Forward(name = "viewAlertMessages", path = "/phd/student/viewAlertMessages.jsp"),

@Forward(name = "viewProcessAlertMessages", path = "/phd/student/viewProcessAlertMessages.jsp")

})
public class PhdIndividualProgramProcessDA extends PhdProcessDA {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	final PhdIndividualProgramProcess process = getProcess(request);

	if (process != null) {
	    request.setAttribute("processAlertMessagesToNotify", process.getUnreadedAlertMessagesFor(getLoggedPerson(request)));
	}

	return super.execute(mapping, actionForm, request, response);
    }

    @Override
    protected PhdIndividualProgramProcess getProcess(HttpServletRequest request) {
	return (PhdIndividualProgramProcess) super.getProcess(request);
    }

    // Alerts Management
    public ActionForward viewAlertMessages(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("alertMessages", getLoggedPerson(request).getPhdAlertMessages());
	return mapping.findForward("viewAlertMessages");
    }

    public ActionForward markAlertMessageAsReaded(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	getAlertMessage(request).markAsReaded(getLoggedPerson(request));
	return globalMessagesView(request) ? viewAlertMessages(mapping, form, request, response) : viewProcessAlertMessages(
		mapping, form, request, response);
    }

    private boolean globalMessagesView(HttpServletRequest request) {
	return StringUtils.isEmpty(request.getParameter("global")) || request.getParameter("global").equals("true");
    }

    private PhdAlertMessage getAlertMessage(HttpServletRequest request) {
	return getDomainObject(request, "alertMessageId");
    }

    public ActionForward viewProcessAlertMessages(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("alertMessages", getProcess(request).getAlertMessagesFor(getLoggedPerson(request)));
	return mapping.findForward("viewProcessAlertMessages");
    }

    // End of Alerts Management

    public ActionForward viewProcess(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final Person person = getLoggedPerson(request);

	final PhdIndividualProgramProcess process = getProcess(request);
	if (process != null) {
	    return mapping.findForward("viewProcess");
	}

	if (!person.hasAnyPhdIndividualProgramProcesses()) {
	    return mapping.findForward("viewProcess");
	}

	if (person.getPhdIndividualProgramProcessesCount() > 1) {
	    return mapping.findForward("choosePhdProcess");
	}

	request.setAttribute("process", person.getPhdIndividualProgramProcesses().get(0));
	return mapping.findForward("viewProcess");
    }

}
