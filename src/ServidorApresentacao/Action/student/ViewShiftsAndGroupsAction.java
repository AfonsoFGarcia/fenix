/*
 * Created on 08/Set/2003
 *
 */
package ServidorApresentacao.Action.student;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoSiteShiftsAndGroups;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.exception.FenixFilterException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidSituationServiceException;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorApresentacao.Action.base.FenixContextAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author asnr and scpo
 *  
 */
public class ViewShiftsAndGroupsAction extends FenixContextAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");

        Integer groupPropertiesCode = new Integer(groupPropertiesCodeString);
        
        String username = userView.getUtilizador();

        InfoSiteShiftsAndGroups infoSiteShiftsAndGroups;
        Object[] args = { groupPropertiesCode, username};
        try {
            infoSiteShiftsAndGroups = (InfoSiteShiftsAndGroups) ServiceUtils.executeService(userView,
                    "ReadShiftsAndGroups", args);

        } catch (InvalidSituationServiceException e)
        {
            ActionErrors actionErrors2 = new ActionErrors();
            ActionError error2 = null;
            error2 = new ActionError("error.noProject");
            actionErrors2.add("error.noProject", error2);
            saveErrors(request, actionErrors2);
            return mapping.findForward("viewExecutionCourseProjects");
        }catch (NotAuthorizedException e) {
			ActionErrors actionErrors2 = new ActionErrors();
			ActionError error2 = null;
			error2 = new ActionError("errors.noStudentInAttendsSet");
			actionErrors2.add("errors.noStudentInAttendsSet", error2);
			saveErrors(request, actionErrors2);
			return mapping.findForward("insucess");
		}catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        request.setAttribute("infoSiteShiftsAndGroups", infoSiteShiftsAndGroups);

        return mapping.findForward("sucess");
    }
}