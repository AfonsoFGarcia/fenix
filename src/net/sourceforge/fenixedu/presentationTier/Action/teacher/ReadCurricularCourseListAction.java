package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.TeacherAdministrationSiteView;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

/**
 * @author T�nia Pous�o
 * @author �ngela
 *  
 */
public class ReadCurricularCourseListAction extends DispatchAction {

    public ActionForward read(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {
        HttpSession session = request.getSession(false);

        Integer objectCode = null;
        String objectCodeString = request.getParameter("objectCode");
        if (objectCodeString == null) {
            objectCodeString = (String) request.getAttribute("objectCode");
        }
        objectCode = new Integer(objectCodeString);

        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        Object args[] = { objectCode };

        TeacherAdministrationSiteView siteView = null;
        try {
            siteView = (TeacherAdministrationSiteView) ServiceManagerServiceFactory.executeService(
                    userView, "ReadCurricularCourseListByExecutionCourseCode", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        request.setAttribute("siteView", siteView);
        request.setAttribute("objectCode", objectCode);

        return mapping.findForward("success");
    }
}