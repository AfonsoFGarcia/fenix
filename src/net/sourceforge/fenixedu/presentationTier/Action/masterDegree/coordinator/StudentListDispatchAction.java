package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.coordinator;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.util.TipoCurso;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class StudentListDispatchAction extends DispatchAction {

    public ActionForward getStudentsFromDCP(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);

        if (session != null) {

            IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

            Integer degreeCurricularPlanID = null;
            if(request.getParameter("degreeCurricularPlanID") != null){
                degreeCurricularPlanID = new Integer(request.getParameter("degreeCurricularPlanID"));
                request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);
            }
            
            List result = null;

            try {
                Object args[] = { degreeCurricularPlanID, TipoCurso.MESTRADO_OBJ };
                result = (List) ServiceManagerServiceFactory.executeService(userView,
                        "ReadStudentsFromDegreeCurricularPlan", args);

            } catch (NotAuthorizedException e) {
                return mapping.findForward("NotAuthorized");
            } catch (NonExistingServiceException e) {
                throw new NonExistingActionException("error.exception.noStudents", "");
            }

            BeanComparator numberComparator = new BeanComparator("infoStudent.number");
            Collections.sort(result, numberComparator);

            request.setAttribute(SessionConstants.STUDENT_LIST, result);

            InfoExecutionDegree infoExecutionDegreeForRequest = null;
            try {
                Object args[] = { degreeCurricularPlanID };
                infoExecutionDegreeForRequest = (InfoExecutionDegree) ServiceManagerServiceFactory
                        .executeService(userView, "ReadExecutionDegreeByDCPID", args);
            } catch (NonExistingServiceException e) {

            } catch (FenixServiceException e) {
                throw new FenixActionException(e);
            }

            if (infoExecutionDegreeForRequest != null) {
                request.setAttribute("infoExecutionDegree", infoExecutionDegreeForRequest);
            }

            String value = request.getParameter("viewPhoto");
            if (value != null && value.equals("true")) {
                request.setAttribute("viewPhoto", Boolean.TRUE);
            } else {
                request.setAttribute("viewPhoto", Boolean.FALSE);
            }

            return mapping.findForward("PrepareSuccess");
        }
        throw new Exception();
    }

    public ActionForward getCurricularCourses(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        Integer degreeCurricularPlanID = null;
        if(request.getParameter("degreeCurricularPlanID") != null){
            degreeCurricularPlanID = new Integer(request.getParameter("degreeCurricularPlanID"));
            request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);
        }
        Object args[] = { degreeCurricularPlanID };
        List result = null;

        try {

            result = (List) ServiceManagerServiceFactory.executeService(userView,
                    "ReadCurricularCoursesByDegree", args);

        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException("error.exception.noStudents", "");
        } catch (FenixServiceException e) {
            throw new FenixActionException();
        }

        BeanComparator nameComparator = new BeanComparator("name");
        Collections.sort(result, nameComparator);

        InfoExecutionDegree infoExecutionDegree = null;
        try {
            infoExecutionDegree = (InfoExecutionDegree) ServiceManagerServiceFactory
                    .executeService(userView, "ReadExecutionDegreeByDCPID", args);
        } catch (NonExistingServiceException e) {

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

         request.setAttribute("executionYear", infoExecutionDegree.getInfoExecutionYear().getYear());
         request.setAttribute("degree", infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree()
         .getNome());
        request.setAttribute("curricularCourses", result);

        return mapping.findForward("ShowCourseList");
    }
}