/*
 * Created on 16/Ago/2003
 */
package ServidorApresentacao.Action.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoCurricularCourse;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author lmac1
 */
public class ReadCurricularCourseAction extends FenixAction {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws FenixActionException {

		HttpSession session = request.getSession(false);

		UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
		Integer curricularCourseId = new Integer(request.getParameter("curricularCourseId"));

		Object args[] = { curricularCourseId };

		GestorServicos manager = GestorServicos.manager();
		InfoCurricularCourse infoCurricularCourse = null;

		try {
				infoCurricularCourse = (InfoCurricularCourse) manager.executar(userView, "ReadCurricularCourse", args);
				
		} catch (NonExistingServiceException e) {
			throw new NonExistingActionException("A disciplina curricular", e);
		} catch (FenixServiceException fenixServiceException) {
			throw new FenixActionException(fenixServiceException.getMessage());
		}

		// in case the curricular course really exists
		List executionCourses = null;
		try {
				executionCourses = (List) manager.executar(userView, "ReadExecutionCoursesByCurricularCourse", args);
				
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		if (executionCourses != null)
			Collections.sort(executionCourses, new BeanComparator("_nome"));

		List curricularCourseScopes = new ArrayList();
		try {
				curricularCourseScopes = (List) manager.executar(userView, "ReadCurricularCourseScopes", args);
				
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		if(curricularCourseScopes != null) {
			ComparatorChain comparatorChain = new ComparatorChain();
			comparatorChain.addComparator(new BeanComparator("infoCurricularSemester.infoCurricularYear.year"));
			comparatorChain.addComparator(new BeanComparator("infoCurricularSemester.semester"));
			Collections.sort(curricularCourseScopes, comparatorChain);
		}

		request.setAttribute("executionCoursesList", executionCourses);
		request.setAttribute("infoCurricularCourse", infoCurricularCourse);
		request.setAttribute("curricularCourseScopesList", curricularCourseScopes);
		return mapping.findForward("viewCurricularCourse");
	}
}
