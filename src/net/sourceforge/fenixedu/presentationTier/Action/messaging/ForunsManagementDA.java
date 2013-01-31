package net.sourceforge.fenixedu.presentationTier.Action.messaging;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.messaging.Forum;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "messaging", path = "/forunsManagement", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "listForuns", path = "forunsManagement.listForuns") })
public class ForunsManagementDA extends FenixDispatchAction {

	public ActionForward list(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) {
		Person person = AccessControl.getPerson();
		Collection<Forum> foruns = person.getForuns(ExecutionSemester.readActualExecutionSemester());
		request.setAttribute("foruns", foruns);
		return mapping.findForward("listForuns");
	}

}
