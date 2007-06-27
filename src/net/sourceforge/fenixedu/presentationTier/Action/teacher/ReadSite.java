/*
 * Created on 21/Mar/2003
 *
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoSite;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Jo�o Mota
 * 
 * 
 */
public class ReadSite extends FenixAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

        HttpSession session = request.getSession(false);
        session.removeAttribute(SessionConstants.INFO_SECTION);
        IUserView userView = getUserView(request);

        InfoSite site = null;

        try {

            Object[] args = { Integer.valueOf(request.getParameter("objectCode")) };
            try {
                site = (InfoSite) ServiceUtils.executeService(userView, "ReadExecutionCourseSite", args);
            } catch (FenixServiceException e) {
                throw new FenixActionException();
            }
        } catch (NumberFormatException e) {
            site = (InfoSite) session.getAttribute(SessionConstants.INFO_SITE);

        }

        session.setAttribute(SessionConstants.INFO_SITE, site);
        session.setAttribute(SessionConstants.ALTERNATIVE_SITE, site.getAlternativeSite());
        session.setAttribute(SessionConstants.MAIL, site.getMail());

        // read sections
        Object[] args = new Object[1];
        args[0] = site;

        List sections = null;
        try {
            sections = (List) ServiceManagerServiceFactory
                    .executeService(userView, "ReadSections", args);
            Collections.sort(sections);
            session.setAttribute(SessionConstants.SECTIONS, sections);
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }
        return mapping.findForward("viewSite");
    }

}