/*
 * Created on 25/Set/2003, 11:52:14 By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package ServidorApresentacao.Action.Seminaries;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.Seminaries.SelectCandidaciesDTO;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt Created at
 *         25/Set/2003, 11:52:14
 */
//TODO: this action IS NOT ready to handle multiple seminaries. It will need a
// select box to select which seminary's candidacies to view
public class SelectCandidacies extends FenixDispatchAction {
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        IUserView userView = SessionUtils.getUserView(request);
        ActionForward destiny = null;
        String seminaryIDString = request.getParameter("seminaryID");
        Integer seminaryID = null;
        Integer wildcard = new Integer(-1);
        try {
            seminaryID = new Integer(seminaryIDString);
        } catch (NumberFormatException ex) {
            seminaryID = wildcard;
        }

        Object[] args = { new Boolean(false), seminaryID };
        try {
            SelectCandidaciesDTO serviceResult = (SelectCandidaciesDTO) ServiceUtils.executeService(
                    userView, "SelectCandidaciesService", args);
            request.setAttribute("seminaries", serviceResult.getSeminaries());
            request.setAttribute("candidacies", serviceResult.getCandidacies());
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        destiny = mapping.findForward("showSelectCandidacies");
        return destiny;
    }

    public List getNewSelectedStudents(Integer[] selectedStudents, Integer[] previousUnselected) {
        List newSelectedStudents = new LinkedList();
        for (int i = 0; i < selectedStudents.length; i++) {
            for (int j = 0; j < previousUnselected.length; j++) {
                if (selectedStudents[i].equals(previousUnselected[j])) {
                    newSelectedStudents.add(selectedStudents[i]);
                    break;
                }
            }
        }
        return newSelectedStudents;
    }

    public List getNewUnselectedStudents(Integer[] selectedStudents, Integer[] previousSelected) {
        List newUnselectedStudents = new LinkedList();
        for (int i = 0; i < previousSelected.length; i++)
            newUnselectedStudents.add(previousSelected[i]);
        //
        //
        for (int i = 0; i < previousSelected.length; i++) {
            for (int j = 0; j < selectedStudents.length; j++) {
                if (previousSelected[i].equals(selectedStudents[j])) {
                    newUnselectedStudents.remove(previousSelected[i]);
                    break;
                }
            }
        }
        return newUnselectedStudents;
    }

    public ActionForward changeSelection(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException {
        HttpSession session = this.getSession(request);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        DynaActionForm selectCases = (DynaActionForm) form;
        Integer[] selectedStudents = null;
        Integer[] previousSelected = null;
        Integer[] previousUnselected = null;
        try {
            selectedStudents = (Integer[]) selectCases.get("selectedStudents");
            previousSelected = (Integer[]) selectCases.get("previousSelected");
            previousUnselected = (Integer[]) selectCases.get("previousUnselected");
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new FenixActionException();
        }
        if (selectedStudents == null || previousSelected == null || previousUnselected == null) {
            throw new FenixActionException();
        }
        try {
            List changedStatusCandidaciesIds = new LinkedList();
            changedStatusCandidaciesIds.addAll(this.getNewSelectedStudents(selectedStudents,
                    previousUnselected));
            changedStatusCandidaciesIds.addAll(this.getNewUnselectedStudents(selectedStudents,
                    previousSelected));
            Object[] argsReadCandidacies = { changedStatusCandidaciesIds };
            ServiceManagerServiceFactory.executeService(userView,
                    "Seminaries.ChangeCandidacyApprovanceStatus", argsReadCandidacies);
        } catch (FenixServiceException ex) {
            throw new FenixActionException(ex);
        }

        // modified by Fernanda Quit�rio
        //destiny = mapping.findForward("prepareForm");
        //return destiny;
        return prepare(mapping, form, request, response);
    }
}