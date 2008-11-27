package net.sourceforge.fenixedu.presentationTier.Action.manager.precedences;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.precedences.MergePrecedencesForDegreeCurricularPlan;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.precedences.ReadPrecedencesFromDegreeCurricularPlan;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.security.UserView;

/**
 * @author David Santos in Jul 28, 2004
 */

public class MakePrecedenceConjunctionForDegreeCurricularPlanDA extends FenixDispatchAction {

    public ActionForward showFirstPage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixActionException, FenixFilterException {

	IUserView userView = UserView.getUser();

	Integer degreeID = new Integer(request.getParameter("degreeId"));
	Integer degreeCurricularPlanID = new Integer(request.getParameter("degreeCurricularPlanId"));

	if (degreeID == null && degreeCurricularPlanID == null) {
	    degreeID = (Integer) request.getAttribute("degreeId");
	    degreeCurricularPlanID = (Integer) request.getAttribute("degreeCurricularPlanId");
	}

	try {
	    Map result = ReadPrecedencesFromDegreeCurricularPlan.run(degreeCurricularPlanID);
	    request.setAttribute("precedences", result);
	} catch (FenixServiceException e) {
	    throw new FenixActionException(e);
	}

	request.setAttribute("degreeId", degreeID);
	request.setAttribute("degreeCurricularPlanId", degreeCurricularPlanID);

	return mapping.findForward("showFirstPage");
    }

    public ActionForward showSecondPage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixActionException, FenixFilterException {

	DynaActionForm mergePrecedencesForm = (DynaActionForm) form;
	IUserView userView = UserView.getUser();

	Integer degreeID = (Integer) mergePrecedencesForm.get("degreeId");
	Integer degreeCurricularPlanID = (Integer) mergePrecedencesForm.get("degreeCurricularPlanId");
	Integer firstPrecedenceID = (Integer) mergePrecedencesForm.get("firstPrecedence");

	try {
	    Map result = ReadPrecedencesFromDegreeCurricularPlan.run(degreeCurricularPlanID);
	    request.setAttribute("precedences", result);
	} catch (FenixServiceException e) {
	    throw new FenixActionException(e);
	}

	mergePrecedencesForm.set("firstPrecedence", firstPrecedenceID);
	request.setAttribute("degreeId", degreeID);
	request.setAttribute("degreeCurricularPlanId", degreeCurricularPlanID);

	return mapping.findForward("showSecondPage");
    }

    public ActionForward merge(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws FenixActionException, FenixFilterException {

	ActionErrors errors = new ActionErrors();
	DynaActionForm mergePrecedencesForm = (DynaActionForm) form;
	IUserView userView = UserView.getUser();

	Integer degreeID = (Integer) mergePrecedencesForm.get("degreeId");
	Integer degreeCurricularPlanID = (Integer) mergePrecedencesForm.get("degreeCurricularPlanId");
	Integer firstPrecedenceID = (Integer) mergePrecedencesForm.get("firstPrecedence");
	Integer secondPrecedenceID = (Integer) mergePrecedencesForm.get("secondPrecedence");

	try {
	    MergePrecedencesForDegreeCurricularPlan.run(firstPrecedenceID, secondPrecedenceID);
	    Map result = ReadPrecedencesFromDegreeCurricularPlan.run(degreeCurricularPlanID);
	    request.setAttribute("precedences", result);
	} catch (InvalidArgumentsServiceException e) {
	    errors.add(e.getMessage(), new ActionError(e.getMessage()));
	    saveErrors(request, errors);
	    return mapping.getInputForward();
	} catch (FenixServiceException e) {
	    throw new FenixActionException(e);
	}

	request.setAttribute("degreeId", degreeID);
	request.setAttribute("degreeCurricularPlanId", degreeCurricularPlanID);

	return mapping.findForward("backToBeginig");
    }
}