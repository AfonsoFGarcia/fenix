package net.sourceforge.fenixedu.presentationTier.Action.coordinator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegreeWithInfoExecutionYearAndDegreeCurricularPlan;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

/**
 * 
 * @author Jo�o Mota
 *  
 */
public class CoordinationTeamDispatchAction extends FenixDispatchAction {

    public ActionForward viewTeam(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixServiceException, FenixFilterException {

        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        
        Integer degreeCurricularPlanID = null;
        if(request.getParameter("degreeCurricularPlanID") != null){
            degreeCurricularPlanID = new Integer(request.getParameter("degreeCurricularPlanID"));
            request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);
        }
        
        Integer infoExecutionDegreeID = null;
        Object[] infoArgs = { degreeCurricularPlanID, new Integer(2) };
 
	    InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegreeWithInfoExecutionYearAndDegreeCurricularPlan) ServiceUtils
	    .executeService(userView, "ReadExecutionDegreeByDegreeCurricularPlanID", infoArgs);

        infoExecutionDegreeID = infoExecutionDegree.getIdInternal();

        request.setAttribute("infoExecutionDegreeId", infoExecutionDegreeID);
        Object[] args = { infoExecutionDegreeID };
        List coordinators = new ArrayList();
        try {
            coordinators = (List) ServiceUtils.executeService(userView, "ReadCoordinationTeam", args);

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        Boolean result = new Boolean(false);
        Object[] args1 = { infoExecutionDegreeID, userView };
        try {
            result = (Boolean) ServiceUtils.executeService(userView, "ReadCoordinationResponsibility",
                    args1);

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        request.setAttribute("isResponsible", result);
        request.setAttribute("coordinators", coordinators);
        return mapping.findForward("coordinationTeam");
    }

    public ActionForward prepareAddCoordinator(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException {
        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        
        Integer degreeCurricularPlanID = new Integer(Integer.parseInt(request.getParameter("degreeCurricularPlanID")));
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);
        
        String infoExecutionDegreeIdString = request.getParameter("infoExecutionDegreeId");
        Integer infoExecutionDegreeId = new Integer(infoExecutionDegreeIdString);
        request.setAttribute("infoExecutionDegreeId", infoExecutionDegreeId);
        Boolean result = new Boolean(false);
        Object[] args1 = { infoExecutionDegreeId, userView };
        try {
            result = (Boolean) ServiceUtils.executeService(userView, "ReadCoordinationResponsibility",
                    args1);

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        request.setAttribute("isResponsible", result);

        return mapping.findForward("addCoordinator");

    }

    public ActionForward AddCoordinator(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException {
        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        DynaActionForm teacherForm = (DynaActionForm) form;
        Integer teacherNumber = new Integer((String) teacherForm.get("teacherNumber"));
        String infoExecutionDegreeIdString = request.getParameter("infoExecutionDegreeId");
        Integer infoExecutionDegreeId = new Integer(infoExecutionDegreeIdString);
        request.setAttribute("infoExecutionDegreeId", infoExecutionDegreeId);
        Object[] args = { infoExecutionDegreeId, teacherNumber };
        try {
            ServiceUtils.executeService(userView, "AddCoordinator", args);
        } catch (NonExistingServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("unknownTeacher", new ActionError("error.nonExistingTeacher"));
            saveErrors(request, actionErrors);
            return prepareAddCoordinator(mapping, form, request, response);
        } catch (InvalidArgumentsServiceException e) {
            throw new FenixActionException(e);
        } catch (ExistingServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("existingCoordinator", new ActionError("error.existingTeacher"));
            saveErrors(request, actionErrors);
            return prepareAddCoordinator(mapping, form, request, response);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        return mapping.findForward("sucess");
    }

    public ActionForward removeCoordinators(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixServiceException, FenixFilterException {
        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);        
        DynaActionForm removeCoordinatorsForm = (DynaActionForm) form;
        Integer[] coordinatorsIds = (Integer[]) removeCoordinatorsForm.get("coordinatorsIds");
        List coordinators = Arrays.asList(coordinatorsIds);
        
        String infoExecutionDegreeIdString = request.getParameter("infoExecutionDegreeId");
        Integer infoExecutionDegreeId = new Integer(infoExecutionDegreeIdString);
        request.setAttribute("infoExecutionDegreeId", infoExecutionDegreeId);
        Object[] args = { infoExecutionDegreeId, coordinators };
        try {
            ServiceUtils.executeService(userView, "RemoveCoordinators", args);

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        return viewTeam(mapping, form, request, response);
    }
}