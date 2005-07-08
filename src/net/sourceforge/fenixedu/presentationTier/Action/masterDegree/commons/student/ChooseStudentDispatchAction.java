package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.commons.student;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class ChooseStudentDispatchAction extends DispatchAction {

    public ActionForward choose(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession();

        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        DynaActionForm studentForm = (DynaActionForm) form;

        Integer studentNumber = (Integer) studentForm.get("number");

        List result = null;

        try {
            Object args[] = { studentNumber, DegreeType.MASTER_DEGREE};
            result = (List) ServiceManagerServiceFactory.executeService(userView,
                    "ReadStudentCurricularPlansByNumberAndDegreeType", args);
        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException("O Aluno");
        }

        if (result.size() == 1) {
            request.setAttribute("studentCPID", ((InfoStudentCurricularPlan) result.get(0))
                    .getIdInternal());
            return mapping.findForward("StudentCurricularPlanChosen");
        }

        request.setAttribute("studentCurricularPlans", result);
        return mapping.findForward("ShowCurricularPlans");
    }

}