package ServidorApresentacao.Action.sop;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoLesson;
import DataBeans.InfoShift;
import DataBeans.ShiftKey;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.sop.base.FenixShiftAndExecutionCourseAndExecutionDegreeAndCurricularYearContextAction;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Luis Cruz & SaraRibeiro
 *  
 */
public class RemoverAulaDeTurnoFormAction extends
        FenixShiftAndExecutionCourseAndExecutionDegreeAndCurricularYearContextAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        super.execute(mapping, form, request, response);

        HttpSession sessao = request.getSession(false);
        if (sessao != null) {

            DynaActionForm editarAulasDeTurnoForm = (DynaActionForm) request
                    .getAttribute("editarAulasDeTurnoForm");

            IUserView userView = (IUserView) sessao.getAttribute(SessionConstants.U_VIEW);

            Integer shiftOID = new Integer(request.getParameter(SessionConstants.SHIFT_OID));
            Object args[] = { shiftOID };
            InfoShift infoTurno = (InfoShift) ServiceManagerServiceFactory.executeService(userView,
                    "ReadShiftByOID", args);

            InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) request
                    .getAttribute(SessionConstants.EXECUTION_COURSE);

            Integer indexAula = (Integer) editarAulasDeTurnoForm.get("indexAula");

            Object argsLerAulasDeTurno[] = { new ShiftKey(infoTurno.getNome(), infoExecutionCourse) };
            List infoAulas = (ArrayList) ServiceManagerServiceFactory.executeService(userView,
                    "LerAulasDeTurno", argsLerAulasDeTurno);

            InfoLesson infoLesson = (InfoLesson) infoAulas.get(indexAula.intValue());

            Object argsRemoverAula[] = { infoLesson, infoTurno };

            ServiceManagerServiceFactory.executeService(userView, "RemoverAula", argsRemoverAula);

            return mapping.findForward("Sucesso");
        }
        throw new Exception();

    }
}