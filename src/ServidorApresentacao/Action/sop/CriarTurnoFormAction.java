package ServidorApresentacao.Action.sop;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoShift;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.sop.exceptions.ExistingServiceException;
import ServidorApresentacao.Action.FenixAction;
import ServidorApresentacao.Action.sop.exceptions.ExistingActionException;
import ServidorApresentacao.Action.sop.utils.RequestUtils;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import Util.TipoAula;
/**
 * @author tfc130
 */
public class CriarTurnoFormAction extends FenixAction {
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		DynaActionForm criarTurnoForm = (DynaActionForm) form;
		HttpSession sessao = request.getSession(false);
		if (sessao != null) {
			IUserView userView = (IUserView) sessao.getAttribute("UserView");
			InfoExecutionCourse courseView =
				RequestUtils.getExecutionCourseBySigla(
					request,
					(String) criarTurnoForm.get("courseInitials"));

			Object argsCriarTurno[] =
				{
					 new InfoShift(
						(String) criarTurnoForm.get("nome"),
						new TipoAula((Integer) criarTurnoForm.get("tipoAula")),
						(Integer) criarTurnoForm.get("lotacao"),
						courseView)};
			try {
				ServiceUtils.executeService(
					userView,
					"CriarTurno",
					argsCriarTurno);
			} catch (ExistingServiceException ex) {
				throw new ExistingActionException("O Turno", ex);
			}

			return mapping.findForward("Sucesso");
		} else
			throw new Exception();
		// nao ocorre... pedido passa pelo filtro Autorizacao
	}
}
