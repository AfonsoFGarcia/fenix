package net.sourceforge.fenixedu.presentationTier.Action.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.presentationTier.Action.ExcepcaoSessaoInexistente;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public abstract class FenixAction extends Action {

    protected HttpSession getSession(HttpServletRequest request) throws ExcepcaoSessaoInexistente {
        HttpSession result = request.getSession(false);
        if (result == null) {
            throw new ExcepcaoSessaoInexistente();
        }
        return result;
    }

    protected IUserView getUserView(HttpServletRequest request) {
    	return SessionUtils.getUserView(request);
    }

    protected Person getLoggedPerson(HttpServletRequest request) {
    	return getUserView(request).getPerson();
    }

    public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        return super.execute(mapping, actionForm, request, response);
    }

}
