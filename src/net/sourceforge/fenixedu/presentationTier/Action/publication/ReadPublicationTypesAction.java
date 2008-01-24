/*
 * Created on 18/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.publication;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoPublicationType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class ReadPublicationTypesAction extends FenixAction {

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm,
     *      javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        IUserView userView = SessionUtils.getUserView(request);
        DynaActionForm dynaForm = (DynaActionForm) actionForm;

            Object[] args = { userView.getUtilizador() };
            Integer keyTeacher = new Integer(request.getParameter("infoTeacher#idInternal"));
            String typePublication = request.getParameter("typePublication");
            List infoPublicationTypes = (List) ServiceUtils.executeService(userView,
                    "ReadAllPublicationTypes", args);

            //TODO remove when database is updated
            Iterator iterator = infoPublicationTypes.iterator();
            while (iterator.hasNext()) {
                InfoPublicationType infoPublicationType = (InfoPublicationType) iterator.next();
                if (infoPublicationType.getPublicationType().equalsIgnoreCase("Ad-Hoc")) {
                    infoPublicationTypes.remove(infoPublicationType);
                    break;
                }
            }
            request.setAttribute("publicationTypesList", infoPublicationTypes);
            dynaForm.set("teacherId", keyTeacher);
            dynaForm.set("typePublication", typePublication);

        ActionForward actionForward = null;

        actionForward = mapping.findForward("show-publication-types-form");

        return actionForward;
    }
}