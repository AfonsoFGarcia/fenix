package ServidorApresentacao.Action.masterDegree.administrativeOffice.marksManagement;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import DataBeans.InfoSiteEnrolmentEvaluation;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidSituationServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import framework.factory.ServiceManagerServiceFactory;

/**
 * 
 * @author Fernanda Quit�rio 10/07/2003
 *  
 */
public class ConfirmMarksAction extends DispatchAction {

    public ActionForward prepareMarksConfirmation(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        Integer curricularCourseCode = new Integer(MarksManagementDispatchAction.getFromRequest(
                "courseId", request));
        MarksManagementDispatchAction.getFromRequest("objectCode", request);
        MarksManagementDispatchAction.getFromRequest("degreeId", request);

        // Get students final evaluation
        Object args[] = { curricularCourseCode, null };
        IUserView userView = SessionUtils.getUserView(request);
        InfoSiteEnrolmentEvaluation infoSiteEnrolmentEvaluation = null;
        try {
            infoSiteEnrolmentEvaluation = (InfoSiteEnrolmentEvaluation) ServiceManagerServiceFactory
                    .executeService(userView, "ReadStudentsFinalEvaluationForConfirmation", args);
        } catch (NonExistingServiceException e) {
            sendErrors(request, "nonExisting", "message.masterDegree.notfound.students");
            return mapping.findForward("ShowMarksManagementMenu");
        } catch (ExistingServiceException e) {
            sendErrors(request, "existing", "message.masterDegree.evaluation.alreadyConfirmed");
            return mapping.findForward("ShowMarksManagementMenu");
        } catch (InvalidSituationServiceException e) {
            sendErrors(request, "invalidSituation", "error.masterDegree.studentsWithoutGrade");
            return mapping.findForward("ShowMarksManagementMenu");
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        Collections.sort(infoSiteEnrolmentEvaluation.getEnrolmentEvaluations(), new BeanComparator(
                "infoEnrolment.infoStudentCurricularPlan.infoStudent.number"));

        request.setAttribute("infoSiteEnrolmentEvaluation", infoSiteEnrolmentEvaluation);

        String forward = findForward(request, infoSiteEnrolmentEvaluation);
        return mapping.findForward(forward);

    }

    private String findForward(HttpServletRequest request,
            InfoSiteEnrolmentEvaluation infoSiteEnrolmentEvaluation) {
        String useCase = (String) getFromRequest("useCase", request);
        String forward = new String("MarksConfirmationMenu");
        if (useCase != null && useCase.equals("confirm")) {
            forward = "MarksConfirmation";
        } else if (useCase != null && useCase.equals("print")) {
            forward = "MarksPrint";
        }
        return forward;
    }

    private void sendErrors(HttpServletRequest request, String arg0, String arg1) {
        ActionErrors errors = new ActionErrors();
        errors.add(arg0, new ActionError(arg1));
        saveErrors(request, errors);
    }

    public ActionForward confirm(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Integer curricularCourseCode = new Integer(MarksManagementDispatchAction.getFromRequest(
                "courseId", request));
        MarksManagementDispatchAction.getFromRequest("objectCode", request);
        MarksManagementDispatchAction.getFromRequest("degreeId", request);

        //		set final evaluation to final state
        IUserView userView = SessionUtils.getUserView(request);
        Object args[] = { curricularCourseCode, null, userView };
        try {
            ServiceManagerServiceFactory
                    .executeService(userView, "ConfirmStudentsFinalEvaluation", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        return mapping.findForward("ShowMarksManagementMenu");
    }

    private Object getFromRequest(String parameter, HttpServletRequest request) {
        Object parameterString = request.getParameter(parameter);
        if (parameterString == null) {
            parameterString = request.getAttribute(parameter);
        }
        return parameterString;
    }
}