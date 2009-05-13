package net.sourceforge.fenixedu.presentationTier.Action.delegate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ReadDelegateDegreeDispatchAction extends FenixAction {

    @Override
    public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) throws FenixActionException {

	final HttpSession session = request.getSession(false);

	if (session != null) {
	    final Person person = getLoggedPerson(request);
	    if (person.hasStudent()) {
		final Registration lastActiveRegistration = person.getStudent().getLastActiveRegistration();
		if (lastActiveRegistration != null) {
		    final Degree degree = lastActiveRegistration.getDegree();
		    final ExecutionDegree executionDegree = degree.getMostRecentDegreeCurricularPlan()
			    .getMostRecentExecutionDegree();
		    final InfoExecutionDegree infoExecutionDegree = InfoExecutionDegree.newInfoFromDomain(executionDegree);
		    session.setAttribute(PresentationConstants.MASTER_DEGREE, infoExecutionDegree);
		}
	    }
	}
	return mapping.findForward("success");
    }
}
