package net.sourceforge.fenixedu.presentationTier.Action.sop;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.base.FenixExecutionDegreeAndCurricularYearContextAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.RequestUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.util.TipoAula;

/**
 * @author tfc130
 */
public class CriarTurnoFormAction extends FenixExecutionDegreeAndCurricularYearContextAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        super.execute(mapping, form, request, response);

        DynaActionForm criarTurnoForm = (DynaActionForm) form;
        HttpSession sessao = request.getSession(false);
        if (sessao != null) {
            IUserView userView = (IUserView) sessao.getAttribute("UserView");
            InfoExecutionCourse courseView = RequestUtils.getExecutionCourseBySigla(request,
                    (String) criarTurnoForm.get("courseInitials"));

            Object argsCriarTurno[] = { new InfoShift((String) criarTurnoForm.get("nome"), new TipoAula(
                    (Integer) criarTurnoForm.get("tipoAula")), (Integer) criarTurnoForm.get("lotacao"),
                    courseView) };
            try {
                ServiceUtils.executeService(userView, "CriarTurno", argsCriarTurno);
            } catch (ExistingServiceException ex) {
                throw new ExistingActionException("O Shift", ex);
            }

            return mapping.findForward("Sucesso");
        }
        throw new Exception();

    }
}