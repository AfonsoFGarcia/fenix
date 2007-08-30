package net.sourceforge.fenixedu.presentationTier.Action.publication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoAuthor;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoSiteAttributes;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author Carlos Pereira
 * @author Francisco Passos
 *  
 */
public class PrepareInsertPublicationAction extends FenixDispatchAction {

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm,
     *      javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    public ActionForward prepareInsert(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        IUserView userView = SessionUtils.getUserView(request);
        DynaActionForm dynaForm = (DynaActionForm) actionForm;

            Integer keyTeacher = new Integer(request.getParameter("infoTeacher#idInternal"));
            dynaForm.set("authorIdInternal",keyTeacher);
            
            Object[] args = { new Integer(1) }; //journal
            InfoSiteAttributes siteAttributes = (InfoSiteAttributes)
                ServiceUtils.executeService(userView, "ReadAllPublicationAttributes", args);
    		request.setAttribute("siteAttributes",siteAttributes);

            request.setAttribute("publicationManagementForm",dynaForm);

        return mapping.findForward("insert-publication");

    }

    public ActionForward moveAuthorDown(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        DynaActionForm dynaForm = (DynaActionForm) actionForm;

        ArrayList authors = (ArrayList) dynaForm.get("authors");
        Integer selectedId = Integer.valueOf(request.getParameter("idInternal"));

        Iterator iterator = authors.iterator();
        for (int iter = 0; iterator.hasNext(); iter++) {
            InfoAuthor infoAuthor = (InfoAuthor) iterator.next();
            if (infoAuthor.getIdInternal().equals(selectedId)) {
                try {
                	Collections.swap(authors, iter, --iter);
	            } catch (IndexOutOfBoundsException ioobe) {
	                //this empty catch is on purpose :)
	            }
                break;
            }
        }

        //dynaForm.set("authors", authors);

        //request.setAttribute("infoAuthors", authors);
        request.setAttribute("publicationManagementForm",dynaForm);

        return mapping.findForward("insert-publication");

    }

    public ActionForward moveAuthorUp(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        DynaActionForm dynaForm = (DynaActionForm) actionForm;

        ArrayList authors = (ArrayList) dynaForm.get("authors");
        Integer selectedId = (Integer) request.getAttribute("idInternal");

        Iterator iterator = authors.iterator();
        for (int iter = 0; iterator.hasNext(); iter++) {
            InfoAuthor infoAuthor = (InfoAuthor) iterator.next();
            if (infoAuthor.getIdInternal().equals(selectedId)) {
                try {
                    Collections.swap(authors, iter, ++iter);
                } catch (IndexOutOfBoundsException ioobe) {
                }
                break;
            }
        }

        dynaForm.set("authors", authors);

        request.setAttribute("infoAuthors", authors);

        return mapping.findForward("insert-publication");

    }
}