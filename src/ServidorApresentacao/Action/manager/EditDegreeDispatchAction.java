/*
 * Created on 28/Jul/2003
 */
package ServidorApresentacao.Action.manager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoDegree;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.TipoCurso;

/**
 * @author lmac1
 */

public class EditDegreeDispatchAction extends FenixDispatchAction {

	public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws FenixActionException {

		HttpSession session = request.getSession(false);
		DynaActionForm readDegreeForm = (DynaActionForm) form;

		UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);

		Integer degreeId = new Integer(request.getParameter("degreeId"));

		InfoDegree oldInfoDegree = null;

		Object args[] = { degreeId };
		GestorServicos manager = GestorServicos.manager();

		try {
			oldInfoDegree = (InfoDegree) manager.executar(userView, "ReadDegree", args);
		} catch (FenixServiceException fenixServiceException) {
			throw new FenixActionException(fenixServiceException.getMessage());
		}

		TipoCurso degreeType = (TipoCurso) oldInfoDegree.getTipoCurso();

		readDegreeForm.set("name", (String) oldInfoDegree.getNome());
		readDegreeForm.set("code", (String) oldInfoDegree.getSigla());
		readDegreeForm.set("degreeType", degreeType.getTipoCurso());
		return mapping.findForward("editDegree");
	}

	public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws FenixActionException {

		HttpSession session = request.getSession(false);

		UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);

		DynaActionForm editDegreeForm = (DynaActionForm) form;
		Integer oldDegreeId = new Integer(request.getParameter("degreeId"));
		String code = (String) editDegreeForm.get("code");
		String name = (String) editDegreeForm.get("name");
		Integer degreeTypeInt = (Integer) editDegreeForm.get("degreeType");

		TipoCurso degreeType = new TipoCurso(degreeTypeInt);
		InfoDegree newInfoDegree = new InfoDegree(code, name, degreeType);

		Object args[] = { oldDegreeId, newInfoDegree };
		GestorServicos manager = GestorServicos.manager();

		try {
			manager.executar(userView, "EditDegree", args);
		} catch (ExistingServiceException e) {
			throw new ExistingActionException(e.getMessage(), e);
		} catch (FenixServiceException fenixServiceException) {
			throw new FenixActionException(fenixServiceException.getMessage());
		}
		return mapping.findForward("readDegrees");
	}
}
