package ServidorApresentacao.Action.sop;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoExecutionCourse;
import DataBeans.comparators.InfoShiftComparatorByLessonType;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.sop.base.FenixExecutionCourseAndExecutionDegreeAndCurricularYearContextAction;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
*/

public class PrepararManipularTurnosFormAction extends FenixExecutionCourseAndExecutionDegreeAndCurricularYearContextAction {

  public ActionForward execute(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response)
      throws Exception {
	super.execute(mapping, form, request, response);
		
    HttpSession sessao = request.getSession(false);
    if (sessao != null) {
        IUserView userView = (IUserView) sessao.getAttribute(SessionConstants.U_VIEW);
        GestorServicos gestor = GestorServicos.manager();
        
		// Ler Turnos de Disciplinas em Execucao
        InfoExecutionCourse iDE = (InfoExecutionCourse) request.getAttribute(SessionConstants.EXECUTION_COURSE);
        Object argsLerTurnosDeDisciplinaExecucao[] = { iDE };
		
		List infoTurnosDeDisciplinaExecucao = (List) gestor.executar(userView, "LerTurnosDeDisciplinaExecucao", argsLerTurnosDeDisciplinaExecucao);
        
		Collections.sort(infoTurnosDeDisciplinaExecucao, new InfoShiftComparatorByLessonType());
		
		request.removeAttribute(SessionConstants.INFO_SHIFTS_EXECUTION_COURSE_KEY);
		if (infoTurnosDeDisciplinaExecucao.size() > 0)
			request.setAttribute(SessionConstants.INFO_SHIFTS_EXECUTION_COURSE_KEY, infoTurnosDeDisciplinaExecucao);
			
		request.removeAttribute(SessionConstants.CLASS_VIEW);
      return mapping.findForward("Sucesso");
    } else
      throw new Exception();  // nao ocorre... pedido passa pelo filtro Autorizacao 
  }
}