package ServidorApresentacao.Action.publico;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.CurricularYearAndSemesterAndInfoExecutionDegree;
import DataBeans.InfoExecutionCourse;
import ServidorAplicacao.GestorServicos;
import ServidorApresentacao.Action.FenixAction;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author Jo�o Mota
 */
public class PrepareSelectExecutionCourseAction extends FenixAction {

	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		HttpSession sessao = request.getSession(false);
		if (sessao != null) {
			
			GestorServicos gestor = GestorServicos.manager();

			InfoExecutionCourse executionCourse = new InfoExecutionCourse();
			
			CurricularYearAndSemesterAndInfoExecutionDegree ctx =
				(CurricularYearAndSemesterAndInfoExecutionDegree) request
					.getSession()
					.getAttribute(SessionConstants.CONTEXT_KEY);
			
			executionCourse.setInfoLicenciaturaExecucao(
				ctx.getInfoLicenciaturaExecucao());
			executionCourse.setSemester(ctx.getSemestre());
				
				
			
			Object argsSelectExecutionCourse[] = { executionCourse, ctx.getAnoCurricular()};
			
			List infoExecutionCourses =
				(List) gestor.executar(null, "SelectExecutionCourse", argsSelectExecutionCourse);
		
			request.getSession().setAttribute(
				SessionConstants.EXECUTION_COURSE_LIST_KEY,
				infoExecutionCourses);
			return mapping.findForward("sucess");
		}
		else {
			throw new Exception();
					// nao ocorre... pedido passa pelo filtro Autorizacao
		}
	}

}
