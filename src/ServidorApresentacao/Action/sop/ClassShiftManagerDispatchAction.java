/**
 * Project Sop 
 * 
 * Package ServidorApresentacao.Action.sop
 * 
 * Created on 12/Dez/2002
 *
 */
package ServidorApresentacao.Action.sop;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import DataBeans.InfoClass;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoShift;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author jpvl
 */
public class ClassShiftManagerDispatchAction extends DispatchAction {

	public static final String SHIFT_LIST_ATT = "shiftList";

	public static final String AVAILABLE_LIST = "showAvailableList";

	public ActionForward addClassShift(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		try {
			HttpSession session = request.getSession();
			IUserView userView = SessionUtils.getUserView(request);

			InfoShift infoShift = getInfoShift (request, SessionConstants.AVAILABLE_INFO_SHIFT_LIST_KEY);
			InfoClass classView = getInfoTurma(request);

			Object[] argsAdicionarTurno = { classView, infoShift };

			ServiceUtils.executeService(
				userView,
				"AdicionarTurno",
				argsAdicionarTurno);

			setClassShiftListToRequest(request, userView, classView.getNome());

			session.setAttribute(SessionConstants.CLASS_INFO_SHIFT_LIST_KEY, request.getAttribute(SHIFT_LIST_ATT));
			session.removeAttribute(SessionConstants.AVAILABLE_INFO_SHIFT_LIST_KEY);		
			

			//			/* TO REMOVE */
			//			session.setAttribute(SessionConstants.CLASS_VIEW, classView);

		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;

		}
		return mapping.findForward("viewClassShiftList");

	}

	public ActionForward removeClassShift(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
			HttpSession session = request.getSession();
		IUserView userView = SessionUtils.getUserView(request);

		InfoShift infoShift = getInfoShift(request, SessionConstants.CLASS_INFO_SHIFT_LIST_KEY);

		InfoClass classView = getInfoTurma(request);
		
		Object[] argsRemoverTurno = { infoShift, classView };
		ServiceUtils.executeService(userView, "RemoverTurno", argsRemoverTurno);

		setClassShiftListToRequest(request, userView, classView.getNome());

		session.setAttribute(SessionConstants.CLASS_INFO_SHIFT_LIST_KEY, request.getAttribute(SHIFT_LIST_ATT));
		session.removeAttribute(SessionConstants.AVAILABLE_INFO_SHIFT_LIST_KEY);		
		
		session.setAttribute(SessionConstants.CLASS_VIEW, classView);

		return mapping.findForward("viewClassShiftList");

	}

	/**
	 * @param request
	 * @return InfoShift
	 */
	private InfoShift getInfoShift(HttpServletRequest request, String listAttributeKey) {
		try {
			Integer infoShiftIndex = new Integer(request.getParameter("shiftIndex"));
			HttpSession session = request.getSession();
			
			List infoShiftList = (List) session.getAttribute(listAttributeKey);
			
			return (InfoShift) infoShiftList.get(infoShiftIndex.intValue());
		} catch (RuntimeException e) {
			e.printStackTrace(System.out);
			
		}
		return null;
		
		
	}

	public ActionForward viewClassShiftList(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		IUserView userView = SessionUtils.getUserView(request);

		InfoClass classView = getInfoTurma(request);

		HttpSession session = request.getSession();
		
		setClassShiftListToRequest(request, userView, classView.getNome());

		
		session.removeAttribute(SessionConstants.AVAILABLE_INFO_SHIFT_LIST_KEY);
		session.setAttribute(SessionConstants.CLASS_INFO_SHIFT_LIST_KEY, request.getAttribute(SHIFT_LIST_ATT));		
	
		session.setAttribute(SessionConstants.CLASS_VIEW, classView);

		return mapping.findForward("viewClassShiftList");
	}

	public ActionForward listAvailableShifts(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		HttpSession session = request.getSession();
		
		IUserView userView = SessionUtils.getUserView(request);

		setShiftAvailableListToRequest(request, userView);

		request.setAttribute(AVAILABLE_LIST, " ");

		session.setAttribute(SessionConstants.AVAILABLE_INFO_SHIFT_LIST_KEY, request.getAttribute(SHIFT_LIST_ATT));
		session.removeAttribute(SessionConstants.CLASS_INFO_SHIFT_LIST_KEY);
		

		return mapping.findForward("viewAvailableShiftList");

	}

	private ArrayList returnShiftList(
		HttpServletRequest request,
		IUserView userView,
		String serviceName,
		Object[] args)
		throws Exception {

		/** InfoShift ArrayList */
		ArrayList shiftList =
			(ArrayList) ServiceUtils.executeService(
				userView,
				serviceName,
				args);

		return shiftList;
	}

	private void setShiftListToRequest(
		HttpServletRequest request,
		IUserView userView,
		String serviceName,
		Object[] args)
		throws Exception {

		/** InfoShift ArrayList */
		ArrayList shiftList =
			returnShiftList(request, userView, serviceName, args);
		//if (shiftList != null && !shiftList.isEmpty())
			request.setAttribute(SHIFT_LIST_ATT, shiftList);
	}

	private void setClassShiftListToRequest(
		HttpServletRequest request,
		IUserView userView,
		String className)
		throws Exception {

		HttpSession session = request.getSession();

		InfoExecutionPeriod infoExecutionPeriod =
			(InfoExecutionPeriod) session.getAttribute(
				SessionConstants.INFO_EXECUTION_PERIOD_KEY);
		InfoExecutionDegree infoExecutionDegree =
			(InfoExecutionDegree) session.getAttribute(
				SessionConstants.INFO_EXECUTION_DEGREE_KEY);

		Object argsLerTurnosTurma[] =
			{ className, infoExecutionDegree, infoExecutionPeriod };

		setShiftListToRequest(
			request,
			userView,
			"LerTurnosDeTurma",
			argsLerTurnosTurma);

	}

	private ArrayList returnClassShiftList(
		HttpServletRequest request,
		IUserView userView,
		String className)
		throws Exception {

		HttpSession session = request.getSession();

		InfoExecutionPeriod infoExecutionPeriod =
			(InfoExecutionPeriod) session.getAttribute(
				SessionConstants.INFO_EXECUTION_PERIOD_KEY);
		InfoExecutionDegree infoExecutionDegree =
			(InfoExecutionDegree) session.getAttribute(
				SessionConstants.INFO_EXECUTION_DEGREE_KEY);

		Object argsLerTurnosTurma[] =
			{ className, infoExecutionDegree, infoExecutionPeriod };

		ArrayList list =
			returnShiftList(
				request,
				userView,
				"LerTurnosDeTurma",
				argsLerTurnosTurma);

		return list;

	}

	private void setShiftAvailableListToRequest(
		HttpServletRequest request,
		IUserView userView)
		throws Exception {
		HttpSession session = request.getSession();

		InfoClass classView = getInfoTurma(request);

		InfoExecutionCourse courseView =
			(InfoExecutionCourse) session.getAttribute(
				SessionConstants.EXECUTION_COURSE_KEY);

		Object[] argsLerTurnosDeDisciplinaExecucao = { courseView };

		ArrayList listClassShift =
			returnClassShiftList(request, userView, classView.getNome());

		ArrayList listAvailable =
			returnShiftList(
				request,
				userView,
				"LerTurnosDeDisciplinaExecucao",
				argsLerTurnosDeDisciplinaExecucao);
				if (listAvailable != null && listClassShift != null) {
			listAvailable.removeAll(listClassShift);
		}

		if (listAvailable != null && !listAvailable.isEmpty()) {
			request.setAttribute(SHIFT_LIST_ATT, listAvailable);
		}
	}

	/**
	 * Method getInfoTurma.
	 * @param request
	 * @return InfoClass
	 */
	private InfoClass getInfoTurma(HttpServletRequest request)
		throws Exception {
		HttpSession session = request.getSession();

		InfoClass classView =
			(InfoClass) session.getAttribute(SessionConstants.CLASS_VIEW);
		if (classView == null)
			throw new Exception("Class is not in session!");
		return classView;
	}

}
