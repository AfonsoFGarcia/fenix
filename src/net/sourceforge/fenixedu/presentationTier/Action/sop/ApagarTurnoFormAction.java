package net.sourceforge.fenixedu.presentationTier.Action.sop;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.ShiftKey;
import net.sourceforge.fenixedu.dataTransferObject.comparators.InfoShiftComparatorByLessonType;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.presentationTier.Action.sop.base.FenixExecutionCourseAndExecutionDegreeAndCurricularYearContextAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

/**
 * @author tfc130
 */
public class ApagarTurnoFormAction extends
        FenixExecutionCourseAndExecutionDegreeAndCurricularYearContextAction {
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
            manipularTurnosForm.set("indexTurno", null);

            Object argsApagarTurno[] = { new ShiftKey(infoTurno.getNome(), iDE) };
            Boolean result = (Boolean) ServiceManagerServiceFactory.executeService(userView,
                    "ApagarTurno", argsApagarTurno);

            if (result != null && result.booleanValue()) {
                infoTurnos.remove(indexTurno.intValue());
                request.removeAttribute("infoTurnosDeDisciplinaExecucao");
                if (!infoTurnos.isEmpty())
                    request.setAttribute("infoTurnosDeDisciplinaExecucao", infoTurnos);
            }

            return mapping.findForward("Sucesso");
        }
        throw new Exception();
    }
}