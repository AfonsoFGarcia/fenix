/*
 * Created on 2003/07/16
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionConstants;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Luis Crus & Sara Ribeiro
 */
public class ManageExecutionPeriodsDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {

	try {
	    List infoExecutionPeriods = (List) ServiceUtils.executeService("ReadExecutionPeriods", null);

	    if (infoExecutionPeriods != null && !infoExecutionPeriods.isEmpty()) {

		Collections.sort(infoExecutionPeriods);

		if (infoExecutionPeriods != null && !infoExecutionPeriods.isEmpty()) {
		    request.setAttribute(SessionConstants.LIST_EXECUTION_PERIODS, infoExecutionPeriods);
		}

	    }
	} catch (FenixServiceException ex) {
	    throw new FenixActionException("Problemas de comunica��o com a base de dados.", ex);
	}

	return mapping.findForward("Manage");
    }

    public ActionForward alterExecutionPeriodState(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	final String year = request.getParameter("year");
	final Integer semester = new Integer(request.getParameter("semester"));
	final String periodStateToSet = request.getParameter("periodState");
	final PeriodState periodState = new PeriodState(periodStateToSet);

	final Object[] args = { year, semester, periodState };
	try {
	    ServiceUtils.executeService("AlterExecutionPeriodState", args);
	} catch (InvalidArgumentsServiceException ex) {
	    throw new FenixActionException("errors.nonExisting.executionPeriod", ex);
	}

	return prepare(mapping, form, request, response);
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {

	final String idInternal = request.getParameter("executionPeriodID");
	ExecutionSemester executionSemester = (ExecutionSemester) rootDomainObject.readExecutionIntervalByOID(Integer
		.valueOf(idInternal));
	request.setAttribute("executionPeriod", executionSemester);
	return mapping.findForward("EditExecutionPeriod");
    }

}