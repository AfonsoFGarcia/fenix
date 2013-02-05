/*
 * Created on Dec 16, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.teacher.professorship;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship.UpdateProfessorshipsHours;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.security.UserView;

/**
 * @author jpvl
 */
public class UpdateProfessorshipHoursAction extends Action {

    /*
     * (non-Javadoc)
     * 
     * @seeorg.apache.struts.action.Action#execute(org.apache.struts.action.
     * ActionMapping, org.apache.struts.action.ActionForm,
     * javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        DynaActionForm professorshipsHours = (DynaActionForm) form;

        HashMap hours = (HashMap) professorshipsHours.get("hours");
        Integer teacherId = (Integer) professorshipsHours.get("teacherId");
        Integer executionYearId = (Integer) professorshipsHours.get("executionYearId");

        IUserView userView = UserView.getUser();
        UpdateProfessorshipsHours.run(teacherId, executionYearId, hours);

        return mapping.findForward("successfull-update");
    }
}