package ServidorApresentacao.Action.commons.password;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import DataBeans.InfoPerson;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.RandomStringGenerator;
import framework.factory.ServiceManagerServiceFactory;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *  
 */

public class GenerateNewPasswordDispatchAction extends DispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        return mapping.findForward("PrepareSuccess");
    }

    public ActionForward findPerson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession();

        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        DynaActionForm newPasswordForm = (DynaActionForm) form;

        String username = (String) newPasswordForm.get("username");

        InfoPerson infoPerson = null;
        try {
            Object args[] = { username };
            infoPerson = (InfoPerson) ServiceManagerServiceFactory.executeService(userView,
                    "ReadPersonByUsername", args);
        } catch (ExcepcaoInexistente e) {
            throw new NonExistingActionException("A Person", e);
        }

        request.setAttribute("infoPerson", infoPerson);

        return mapping.findForward("Confirm");
    }

    public ActionForward generatePassword(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession();

        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        Integer personID = new Integer(request.getParameter("personID"));

        String password = RandomStringGenerator.getRandomStringGenerator(8);

        // Change the Password
        try {
            Object args[] = { personID, password };
            ServiceManagerServiceFactory.executeService(userView, "ChangePersonPassword", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException();
        }

        request.setAttribute("password", password);

        InfoPerson infoPerson = null;
        try {
            Object args[] = { request.getParameter("username") };
            infoPerson = (InfoPerson) ServiceManagerServiceFactory.executeService(userView,
                    "ReadPersonByUsername", args);
        } catch (ExcepcaoInexistente e) {
            throw new NonExistingActionException("A Person", e);
        }

        request.setAttribute("infoPerson", infoPerson);

        return mapping.findForward("Success");
    }
}