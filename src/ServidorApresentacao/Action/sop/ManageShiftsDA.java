package ServidorApresentacao.Action.sop;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoCurricularYear;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoShift;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao
	.Action
	.sop
	.base
	.FenixExecutionDegreeAndCurricularYearContextDispatchAction;
import ServidorApresentacao.Action.sop.utils.RequestUtils;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import ServidorApresentacao.Action.utils.ContextUtils;
import Util.TipoAula;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 */
public class ManageShiftsDA
	extends FenixExecutionDegreeAndCurricularYearContextDispatchAction {

	public ActionForward listShifts(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		IUserView userView = SessionUtils.getUserView(request);

		InfoExecutionPeriod infoExecutionPeriod =
			(InfoExecutionPeriod) request.getAttribute(
				SessionConstants.EXECUTION_PERIOD);

		InfoExecutionDegree infoExecutionDegree =
			(InfoExecutionDegree) request.getAttribute(
				SessionConstants.EXECUTION_DEGREE);

		InfoCurricularYear infoCurricularYear =
			(InfoCurricularYear) request.getAttribute(
				SessionConstants.CURRICULAR_YEAR);

		/* Obtain a list of shifts of specified degree for indicated
		 *  curricular year and execution period */
		Object args[] =
			{ infoExecutionPeriod, infoExecutionDegree, infoCurricularYear };
		List infoShifts =
			(List) ServiceUtils.executeService(
				userView,
				"ReadShiftsByExecutionPeriodAndExecutionDegreeAndCurricularYear",
				args);

		/* Sort the list of shifts */
		ComparatorChain chainComparator = new ComparatorChain();
		chainComparator.addComparator(
			new BeanComparator("infoDisciplinaExecucao.nome"));
		chainComparator.addComparator(new BeanComparator("tipo.tipo"));
		chainComparator.addComparator(new BeanComparator("nome"));
		Collections.sort(infoShifts, chainComparator);

		/* Place list of shifts in request */
		if (infoShifts != null && !infoShifts.isEmpty()) {
			request.setAttribute(SessionConstants.SHIFTS, infoShifts);
		}

		/* Place list of execution courses in request */
		SessionUtils.getExecutionCourses(request);

		/* Place label list of types of shifts/lessons in request */
		RequestUtils.setLessonTypes(request);

		return mapping.findForward("ShowShiftList");
	}

	public ActionForward createShift(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		IUserView userView = SessionUtils.getUserView(request);

		DynaActionForm createShiftForm = (DynaActionForm) form;

		InfoShift infoShift = new InfoShift();
		infoShift.setAvailabilityFinal(new Integer(0));
		InfoExecutionCourse infoExecutionCourse =
			RequestUtils.getExecutionCourseBySigla(
				request,
				(String) createShiftForm.get("courseInitials"));
		infoShift.setInfoDisciplinaExecucao(infoExecutionCourse);
		infoShift.setInfoLessons(null);
		infoShift.setLotacao(new Integer(0));
		infoShift.setNome((String) createShiftForm.get("nome"));
		infoShift.setTipo(
			new TipoAula((Integer) createShiftForm.get("tipoAula")));

		Object argsCriarTurno[] = { infoShift };
		try {
			infoShift =
				(InfoShift) ServiceUtils.executeService(
					userView,
					"CriarTurno",
					argsCriarTurno);
			infoShift.setInfoLessons(null);
			infoShift.setInfoClasses(null);
		} catch (ExistingServiceException ex) {
			throw new ExistingActionException("O Turno", ex);
		}

		request.setAttribute(
			SessionConstants.EXECUTION_COURSE,
			infoExecutionCourse);

		request.setAttribute(SessionConstants.SHIFT, infoShift);

		return mapping.findForward("EditShift");
	}

	public ActionForward deleteShift(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		IUserView userView = SessionUtils.getUserView(request);

		ContextUtils.setShiftContext(request);

		InfoShift infoShiftToDelete =
			(InfoShift) request.getAttribute(SessionConstants.SHIFT);

		Object args[] = { infoShiftToDelete };
		ServiceUtils.executeService(userView, "DeleteShift", args);

		return listShifts(mapping, form, request, response);
	}

}