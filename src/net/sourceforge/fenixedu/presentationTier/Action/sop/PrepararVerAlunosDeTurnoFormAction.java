package net.sourceforge.fenixedu.presentationTier.Action.sop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.ShiftKey;
import net.sourceforge.fenixedu.dataTransferObject.comparators.InfoShiftComparatorByLessonType;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.sop.base.FenixShiftAndExecutionCourseAndExecutionDegreeAndCurricularYearContextAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author tfc130
 */
public class PrepararVerAlunosDeTurnoFormAction extends
        FenixShiftAndExecutionCourseAndExecutionDegreeAndCurricularYearContextAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        super.execute(mapping, form, request, response);

        HttpSession sessao = request.getSession(false);
        if (sessao != null) {
            DynaActionForm manipularTurnosForm = (DynaActionForm) request
                    .getAttribute("manipularTurnosForm");
            IUserView userView = (IUserView) sessao.getAttribute("UserView");

            Integer indexTurno = (Integer) manipularTurnosForm.get("indexTurno");
            //List infoTurnos = (ArrayList)
            // request.getAttribute("infoTurnosDeDisciplinaExecucao");
            InfoExecutionCourse iDE = (InfoExecutionCourse) request
                    .getAttribute(SessionConstants.EXECUTION_COURSE);
            Object argsLerTurnosDeDisciplinaExecucao[] = { iDE };
            List infoTurnos = (List) ServiceManagerServiceFactory.executeService(userView,
                    "LerTurnosDeDisciplinaExecucao", argsLerTurnosDeDisciplinaExecucao);
            Collections.sort(infoTurnos, new InfoShiftComparatorByLessonType());

            InfoShift infoTurno = (InfoShift) infoTurnos.get(indexTurno.intValue());

            request.removeAttribute("infoTurno");
            request.setAttribute("infoTurno", infoTurno);
            request.setAttribute(SessionConstants.SHIFT, infoTurno);
            Object argsLerAlunosDeTurno[] = { new ShiftKey(infoTurno.getNome(), infoTurno
                    .getInfoDisciplinaExecucao()) };
            List infoAlunosDeTurno = (ArrayList) ServiceManagerServiceFactory.executeService(userView,
                    "LerAlunosDeTurno", argsLerAlunosDeTurno);
            request.removeAttribute("infoAlunosDeTurno");
            if (!infoAlunosDeTurno.isEmpty())
                request.setAttribute("infoAlunosDeTurno", infoAlunosDeTurno);
            return mapping.findForward("Sucesso");
        }
        throw new Exception();
    }
}