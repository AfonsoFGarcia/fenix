package ServidorApresentacao.Action.student;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.InfoCurricularCourse;
import DataBeans.InfoCurricularCourseScope;
import DataBeans.InfoDegree;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.OutOfCurricularCourseEnrolmentPeriod;
import ServidorAplicacao.strategy.enrolment.context.EnrolmentValidationResult;
import ServidorAplicacao.strategy.enrolment.context.InfoEnrolmentContext;
import ServidorApresentacao.Action.commons.GeneralCurricularCourseEnrolmentManagerDispatchAction;
import ServidorApresentacao.Action.exceptions.OutOfCurricularEnrolmentPeriodActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author David Santos
 *
 */

public class CurricularCourseEnrolmentManagerDispatchAction extends GeneralCurricularCourseEnrolmentManagerDispatchAction {

	private final String[] forwards = { "showAvailableCurricularCourses", "verifyEnrolment", "acceptEnrolment", "cancel" };

	public ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		super.createToken(request);

		HttpSession session = request.getSession();

		session.removeAttribute(SessionConstants.INFO_ENROLMENT_CONTEXT_KEY);
		session.removeAttribute(SessionConstants.ENROLMENT_TO_REMOVE_LIST_KEY);
		session.removeAttribute(SessionConstants.ACTUAL_ENROLMENT_KEY);

		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		Object args[] = { userView };

		InfoEnrolmentContext infoEnrolmentContext = null;
		try {
			infoEnrolmentContext = (InfoEnrolmentContext) ServiceUtils.executeService(userView, "ShowAvailableCurricularCourses", args);
		} catch (OutOfCurricularCourseEnrolmentPeriod e) {
			throw new OutOfCurricularEnrolmentPeriodActionException(e.getMessageKey(), e.getStartDate(), e.getEndDate(), mapping.findForward("globalOutOfPeriod"));
		}

		session.setAttribute(SessionConstants.INFO_ENROLMENT_CONTEXT_KEY, infoEnrolmentContext);
		this.initializeForm(infoEnrolmentContext, (DynaActionForm) form);
		super.initializeRemovedCurricularCourseScopesList(request, infoEnrolmentContext);

		if(infoEnrolmentContext.getInfoFinalCurricularCoursesScopesSpanToBeEnrolled().isEmpty()) {
			return mapping.findForward("cantEnroll");
		} else {
			return mapping.findForward(forwards[0]);
		}
	}

	public ActionForward verifyEnrolment(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		super.validateToken(request, form, mapping, "error.transaction.enrolment");

		HttpSession session = request.getSession();

		if (isCancelled(request)) {
			session.removeAttribute(SessionConstants.INFO_ENROLMENT_CONTEXT_KEY);
			session.removeAttribute(SessionConstants.ENROLMENT_TO_REMOVE_LIST_KEY);
			session.removeAttribute(SessionConstants.ACTUAL_ENROLMENT_KEY);
			return mapping.findForward(forwards[3]);
		}

		DynaActionForm enrolmentForm = (DynaActionForm) form;

		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		InfoEnrolmentContext infoEnrolmentContext = processEnrolment(request, enrolmentForm, session);

		Object args[] = { infoEnrolmentContext };

		infoEnrolmentContext = (InfoEnrolmentContext) ServiceUtils.executeService(userView, "ValidateActualEnrolment", args);

		session.setAttribute(SessionConstants.INFO_ENROLMENT_CONTEXT_KEY, infoEnrolmentContext);

		ActionForward nextForward = null;
		if(!infoEnrolmentContext.getEnrolmentValidationResult().isSucess()) {
			super.saveErrorsFromInfoEnrolmentContext(request, infoEnrolmentContext);
			nextForward = getBeforeForward(request, mapping);
		} else {
			nextForward = getNextForward(request, mapping);
		}
		return nextForward;
	}

	public ActionForward accept(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		super.validateToken(request, form, mapping, "error.transaction.enrolment");

		if (isCancelled(request)) {
			return getBeforeForward(request, mapping);
		}

		HttpSession session = request.getSession();

		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		InfoEnrolmentContext infoEnrolmentContext = (InfoEnrolmentContext) session.getAttribute(SessionConstants.INFO_ENROLMENT_CONTEXT_KEY);

		Object args[] = { infoEnrolmentContext };

		infoEnrolmentContext = (InfoEnrolmentContext) ServiceUtils.executeService(userView, "ConfirmActualEnrolment", args);

		if (infoEnrolmentContext.getEnrolmentValidationResult().isSucess()) {
			session.removeAttribute(SessionConstants.INFO_ENROLMENT_CONTEXT_KEY);
			session.removeAttribute(SessionConstants.ENROLMENT_TO_REMOVE_LIST_KEY);
			session.removeAttribute(SessionConstants.ACTUAL_ENROLMENT_KEY);
			return getNextForward(request, mapping);
		} else {
			session.setAttribute(SessionConstants.INFO_ENROLMENT_CONTEXT_KEY, infoEnrolmentContext);
			super.saveErrorsFromInfoEnrolmentContext(request, infoEnrolmentContext);
			return mapping.findForward(forwards[0]);
		}

	}

	public ActionForward startEnrolmentInOptional(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		super.validateToken(request, form, mapping, "error.transaction.enrolment");

		HttpSession session = request.getSession();

		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
		InfoEnrolmentContext infoEnrolmentContext = (InfoEnrolmentContext) session.getAttribute(SessionConstants.INFO_ENROLMENT_CONTEXT_KEY);

		DynaValidatorForm enrolmentForm = (DynaValidatorForm) form;
		Integer optionalCurricularCourseIndex = (Integer) enrolmentForm.get("optionalCourseIndex");
		InfoCurricularCourseScope infoCurricularCourseScope = (InfoCurricularCourseScope) infoEnrolmentContext.getInfoFinalCurricularCoursesScopesSpanToBeEnrolled().get(optionalCurricularCourseIndex.intValue());
		infoEnrolmentContext.setInfoChosenOptionalCurricularCourseScope(infoCurricularCourseScope);

		Object args[] = { infoEnrolmentContext };

		infoEnrolmentContext = (InfoEnrolmentContext) ServiceUtils.executeService(userView, "ShowAvailableDegreesForOption", args);

		List infoDegreeList = infoEnrolmentContext.getInfoDegreesForOptionalCurricularCourses();
		ActionForward forward = null;
		if(infoDegreeList.size() == 1) {
			infoEnrolmentContext.setChosenOptionalInfoDegree((InfoDegree) infoDegreeList.get(0));

			args[0] = infoEnrolmentContext;

			infoEnrolmentContext = (InfoEnrolmentContext) ServiceUtils.executeService(userView, "ShowAvailableCurricularCoursesForOption", args);
			session.setAttribute(SessionConstants.INFO_ENROLMENT_CONTEXT_KEY, infoEnrolmentContext);
			enrolmentForm.set("optionalCurricularCourse", null);
			forward = mapping.findForward("concreteOptionalList");
		} else {
			session.setAttribute(SessionConstants.INFO_ENROLMENT_CONTEXT_KEY, infoEnrolmentContext);
			forward = mapping.findForward("searchOptionalCurricularCourses");
		}

		return forward;
	}

	public ActionForward showOptionalCurricularCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		super.validateToken(request, form, mapping, "error.transaction.enrolment");

		DynaValidatorForm enrolmentForm = (DynaValidatorForm) form;
		HttpSession session = request.getSession();

		InfoEnrolmentContext infoEnrolmentContext = (InfoEnrolmentContext) session.getAttribute(SessionConstants.INFO_ENROLMENT_CONTEXT_KEY);

		if(isCancelled(request)) {
			super.uncheckCurricularCourse(enrolmentForm, infoEnrolmentContext);
			return mapping.findForward(forwards[0]);
		}

		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		enrolmentForm.set("optionalCurricularCourse", null);
		Integer infoDegreeInternalId = (Integer) enrolmentForm.get("infoDegree");
		List infoDegreeList = infoEnrolmentContext.getInfoDegreesForOptionalCurricularCourses();
		
		Iterator iterator = infoDegreeList.iterator();
		InfoDegree infoDegree = null;
		while(iterator.hasNext()) {
			InfoDegree infoDegree2 = (InfoDegree) iterator.next();
			if(infoDegree2.getIdInternal().equals(infoDegreeInternalId)) {
				infoDegree = infoDegree2;
				break;
			}
		}
		infoEnrolmentContext.setChosenOptionalInfoDegree(infoDegree);

		Object args[] = { infoEnrolmentContext };

		infoEnrolmentContext = (InfoEnrolmentContext) ServiceUtils.executeService(userView, "ShowAvailableCurricularCoursesForOption", args);
		session.setAttribute(SessionConstants.INFO_ENROLMENT_CONTEXT_KEY, infoEnrolmentContext);
		return mapping.findForward("concreteOptionalList");
	}

	public ActionForward chooseOptionalCourse(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		super.validateToken(request, form, mapping, "error.transaction.enrolment");

		DynaValidatorForm enrolmentForm = (DynaValidatorForm) form;
		HttpSession session = request.getSession();

		InfoEnrolmentContext infoEnrolmentContext = (InfoEnrolmentContext) session.getAttribute(SessionConstants.INFO_ENROLMENT_CONTEXT_KEY);

		if(isCancelled(request)) {
			List infoDegreeList = infoEnrolmentContext.getInfoDegreesForOptionalCurricularCourses();
			if(infoDegreeList.size() == 1) {
				super.uncheckCurricularCourse(enrolmentForm, infoEnrolmentContext);
				return mapping.findForward(forwards[0]);
			} else {
				return mapping.findForward("searchOptionalCurricularCourses");
			}
		}

		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		Integer optionalCurricularCourseIndex = (Integer) enrolmentForm.get("optionalCourseIndex");
		
		InfoCurricularCourseScope infoCurricularCourseScope = (InfoCurricularCourseScope) infoEnrolmentContext.getInfoFinalCurricularCoursesScopesSpanToBeEnrolled().get(optionalCurricularCourseIndex.intValue());

		Integer optionalCourseChoosenIndex = (Integer) enrolmentForm.get("optionalCurricularCourse");

		if(optionalCourseChoosenIndex == null) {
			infoEnrolmentContext.getEnrolmentValidationResult().reset();
			infoEnrolmentContext.getEnrolmentValidationResult().setErrorMessage(EnrolmentValidationResult.NO_OPTIONAL_CURRICULAR_COURSES_TO_ENROLL);
			super.saveErrorsFromInfoEnrolmentContext(request, infoEnrolmentContext);
			return mapping.findForward("concreteOptionalList");
		} else {
			InfoCurricularCourse infoCurricularCourseOptional = (InfoCurricularCourse) infoEnrolmentContext.getOptionalInfoCurricularCoursesToChooseFromDegree().get(optionalCourseChoosenIndex.intValue());

			infoEnrolmentContext.setInfoChosenOptionalCurricularCourseScope(infoCurricularCourseScope);

			Object args[] = { infoEnrolmentContext, infoCurricularCourseOptional };

			infoEnrolmentContext = (InfoEnrolmentContext) ServiceUtils.executeService(userView, "SelectOptionalCurricularCourse", args);

			session.setAttribute(SessionConstants.INFO_ENROLMENT_CONTEXT_KEY, infoEnrolmentContext);

			return mapping.findForward(forwards[0]);
		}
	}

	public ActionForward cancel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.validateToken(request, form, mapping, "error.transaction.enrolment");
		return mapping.findForward(forwards[3]);
	}

	/**
	 * @param form
	 * @param mapping
	 * @return
	 */
	private ActionForward getBeforeForward(HttpServletRequest request, ActionMapping mapping) throws Exception {
		int step = 0;
		try {
			step = Integer.parseInt(request.getParameter("step"));
		} catch (NumberFormatException e) {
		}

		if (step < 0 && step >= forwards.length)
			throw new IllegalArgumentException("Illegal step!");

		if (step != 0)
			step -= 1;

		return mapping.findForward(forwards[step]);
	}

	/**
	 * @param form
	 * @return
	 */
	private ActionForward getNextForward(HttpServletRequest request, ActionMapping mapping) throws Exception {
		int step = 0;
		try {
			step = Integer.parseInt(request.getParameter("step"));
		} catch (NumberFormatException e) {
		}

		step = step < 0 ? 0 : step;
		step = step > forwards.length ? forwards.length - 2 : step;
		return mapping.findForward(forwards[step + 1]);
	}
}