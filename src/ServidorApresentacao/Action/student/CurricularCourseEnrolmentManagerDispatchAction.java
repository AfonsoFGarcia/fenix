/*
 * Created on 28/Abr/2003 by jpvl
 *
 */
package ServidorApresentacao.Action.student;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import DataBeans.InfoCurricularCourseScope;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.strategy.enrolment.degree.EnrolmentValidationResult;
import ServidorAplicacao.strategy.enrolment.degree.InfoEnrolmentContext;
import ServidorApresentacao.Action.exceptions.FenixTransactionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author jpvl
 */
public class CurricularCourseEnrolmentManagerDispatchAction
	extends DispatchAction {
	private final String []forwards = {"showAvailableCurricularCourses", "verifyEnrolment", "acceptEnrolment"};
	public ActionForward start(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
		createToken(request);
		
		
		HttpSession session = request.getSession();
		IUserView userView =
			(IUserView) session.getAttribute(SessionConstants.U_VIEW);

		Object args[] = { userView };

		InfoEnrolmentContext infoEnrolmentContext =
			(InfoEnrolmentContext) ServiceUtils.executeService(
				userView,
				"ShowAvailableCurricularCourses",
				args);

		session.setAttribute(
			SessionConstants.INFO_ENROLMENT_CONTEXT_KEY,
			infoEnrolmentContext);
		initializeForm(infoEnrolmentContext, (DynaActionForm) form);
		return mapping.findForward(forwards[0]);
	}


	public ActionForward verifyEnrolment(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		validateToken(request, form, mapping);
		
		DynaActionForm enrolmentForm = (DynaActionForm) form;
		HttpSession session = request.getSession();

		IUserView userView =
			(IUserView) session.getAttribute(SessionConstants.U_VIEW);

		InfoEnrolmentContext infoEnrolmentContext =
			processEnrolment(enrolmentForm, session);


		Object args[] = { infoEnrolmentContext };

		infoEnrolmentContext = (InfoEnrolmentContext) ServiceUtils.executeService(userView, "ValidateActualEnrolment", args);
		ActionForward nextForward = null;
		session.setAttribute(SessionConstants.INFO_ENROLMENT_CONTEXT_KEY, infoEnrolmentContext);
		if (!infoEnrolmentContext.getEnrolmentValidationResult().isSucess()){
			saveErrorsFromInfoEnrolmentContext(request, infoEnrolmentContext);
			nextForward = getBeforeForward(request, mapping);
		}else{
			nextForward = getNextForward(request, mapping);			
		}
		return nextForward;
	}

	/**
	 * @param request
	 * @param infoEnrolmentContext
	 */
	private void saveErrorsFromInfoEnrolmentContext(HttpServletRequest request, InfoEnrolmentContext infoEnrolmentContext) {
		ActionErrors actionErrors = new ActionErrors();
		
		EnrolmentValidationResult enrolmentValidationResult = infoEnrolmentContext.getEnrolmentValidationResult();
		
		Map messages = enrolmentValidationResult.getMessage();
		
		Iterator messagesIterator = messages.keySet().iterator();
		ActionError actionError;
		while (messagesIterator.hasNext()) {
			String message = (String) messagesIterator.next();
			List messageArguments = (List) messages.get(message);
			actionError = new ActionError(message,messageArguments.toArray());
			actionErrors.add(message, actionError);
		}
		saveErrors(request, actionErrors);
	}


	public ActionForward accept(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		if (isCancelled(request)){
			return getBeforeForward(request, mapping);
		}
		validateToken(request, form, mapping);
		
		HttpSession session = request.getSession();

		IUserView userView =
			(IUserView) session.getAttribute(SessionConstants.U_VIEW);

		InfoEnrolmentContext infoEnrolmentContext = (InfoEnrolmentContext) session.getAttribute(SessionConstants.INFO_ENROLMENT_CONTEXT_KEY); 

		Object args[] = { infoEnrolmentContext };

		ServiceUtils.executeService(userView, "ConfirmActualEnrolment", args);
		System.out.println("===========================================CORRI O SERVI�O");
		return getNextForward(request, mapping);
	}

	/**
	 * @param request	
	 */
	private void validateToken(
		HttpServletRequest request,
		ActionForm form,
		ActionMapping mapping)
		throws FenixTransactionException {
	
		if (!isTokenValid(request)) {
			form.reset(mapping, request);
			throw new FenixTransactionException(
				"error.transaction.enrolment");
		} else {
			createToken (request);
		}
	}

	/**
	 * @param request
	 */
	private void createToken(HttpServletRequest request) {
		generateToken(request);
		saveToken(request);
	}

	private void initializeForm(InfoEnrolmentContext infoEnrolmentContext, DynaActionForm enrolmentForm){
		List actualEnrolment = infoEnrolmentContext.getActualEnrolment();
		List infoFinalSpan = infoEnrolmentContext.getInfoFinalCurricularCoursesScopesSpanToBeEnrolled();
		Integer [] curricularCoursesIndexes = new Integer [infoFinalSpan.size()];
		
		for (int i= 0; i < infoFinalSpan.size(); i++) {
			InfoCurricularCourseScope infoCurricularCourseScope = (InfoCurricularCourseScope) infoFinalSpan.get(i);
			if (actualEnrolment.contains(infoCurricularCourseScope)){
				curricularCoursesIndexes[i] = new Integer(i);
			}else{
				curricularCoursesIndexes[i] = null;
			}
		}
		enrolmentForm.set("curricularCourses", curricularCoursesIndexes);		
	}
	
	
	/**
	 * @param enrolmentForm
	 * @param session
	 * @return
	 */
	private InfoEnrolmentContext processEnrolment(
		DynaActionForm enrolmentForm,
		HttpSession session) {

		InfoEnrolmentContext infoEnrolmentContext =
			(InfoEnrolmentContext) session.getAttribute(
				SessionConstants.INFO_ENROLMENT_CONTEXT_KEY);
		Integer[] curricularCourses =
			(Integer[]) enrolmentForm.get("curricularCourses");

		List curricularCourseScopesToBeEnrolled =
			infoEnrolmentContext
				.getInfoFinalCurricularCoursesScopesSpanToBeEnrolled();
		List actualEnrolment = infoEnrolmentContext.getActualEnrolment();
		actualEnrolment.clear();
		for (int i = 0; i < curricularCourses.length; i++) {
			Integer curricularCourseIndex = curricularCourses[i];
			InfoCurricularCourseScope curricularCourseScope =
				(
					InfoCurricularCourseScope) curricularCourseScopesToBeEnrolled
						.get(
					curricularCourseIndex.intValue());
			actualEnrolment.add(curricularCourseScope);
			
		}
		return infoEnrolmentContext;
	}
	
	/**
	 * @param form
	 * @param mapping
	 * @return
	 */
	private ActionForward getBeforeForward(HttpServletRequest request, ActionMapping mapping) throws Exception{
		int step = 0;
		try{
			step = Integer.parseInt(request.getParameter("step"));
		}catch (NumberFormatException e){
		}
		
		if (step < 0 && step >= forwards.length)
			throw new IllegalArgumentException("Illegal step!");
		
		if (step != 0) step -=1; 
		
		return mapping.findForward(forwards[step]);
	}

	/**
	 * @param form
	 * @return
	 */
	private ActionForward getNextForward(HttpServletRequest request, ActionMapping mapping) throws Exception {
		int step = 0;
		try{
			step = Integer.parseInt(request.getParameter("step"));
		}catch (NumberFormatException e){
		}
		
		step = step < 0 ? 0 : step;
		step = step > forwards.length ? forwards.length - 2 : step;
		return mapping.findForward(forwards[step + 1]);
	}

}
