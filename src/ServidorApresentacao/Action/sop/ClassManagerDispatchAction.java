/**
 * Project Sop 
 * 
 * Package ServidorApresentacao.Action.sop
 * 
 * Created on 4/Dez/2002
 *
 */
package ServidorApresentacao.Action.sop;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.InfoClass;
import DataBeans.InfoCurricularYear;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.sop.base.FenixClassAndExecutionDegreeAndCurricularYearContextDispatchAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author jpvl
 *
 * 
 */
public class ClassManagerDispatchAction
	extends FenixClassAndExecutionDegreeAndCurricularYearContextDispatchAction {
	private static final String CLASS_NAME_PARAM = "className";
	/**
	 * Create class
	 * */
	public ActionForward createClass(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		//ContextUtils.setExecutionPeriodContext(request);
		//ContextUtils.setExecutionDegreeContext(request);
		//ContextUtils.setCurricularYearContext(request);

		//HttpSession session = request.getSession(false);

		request.removeAttribute(SessionConstants.EXECUTION_COURSE_KEY);
		//request.removeAttribute(SessionConstants.CLASS_VIEW);
		request.removeAttribute(SessionConstants.LESSON_LIST_ATT);
		String className = getClassName(form);

		IUserView userView = SessionUtils.getUserView(request);

		if (className != null && !className.equals("")) {

			InfoClass classView = getInfoTurma(userView, className, request);

			if (classView == null) {
				InfoCurricularYear infoCurricularYear =
					(InfoCurricularYear) request.getAttribute(
						SessionConstants.CURRICULAR_YEAR);
				Integer curricularYear = infoCurricularYear.getYear();
				InfoExecutionDegree infoExecutionDegree =
					(InfoExecutionDegree) request.getAttribute(
						SessionConstants.EXECUTION_DEGREE);
				InfoExecutionPeriod infoExecutionPeriod =
					(InfoExecutionPeriod) request.getAttribute(
						SessionConstants.EXECUTION_PERIOD);

				//				CurricularYearAndSemesterAndInfoExecutionDegree context =
				//					SessionUtils.getContext(request);

				InfoClass infoClass = new InfoClass();
				infoClass.setNome(className);
				infoClass.setAnoCurricular(curricularYear);
				infoClass.setInfoExecutionDegree(infoExecutionDegree);
				infoClass.setInfoExecutionPeriod(infoExecutionPeriod);

				Object argsCriarTurma[] = { infoClass };

				try {
					infoClass = (InfoClass) ServiceUtils.executeService(
						userView,
						"CriarTurma",
						argsCriarTurma);
					request.setAttribute(SessionConstants.CLASS_VIEW, infoClass);
				} catch (ExistingServiceException e) {
					throw new ExistingActionException("A Turma", e);
				}
				return viewClass(mapping, form, request, response);
			} 
				ActionErrors actionErrors = new ActionErrors();
				actionErrors.add(
					"existingClass",
					new ActionError("errors.existClass", className));
				saveErrors(request, actionErrors);
				return mapping.getInputForward();
			
		} 
			return mapping.getInputForward();
		

	}

	/**
	 * Change class name
	 * 
	 * */
	public ActionForward editClass(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		String className = getClassName(form);

		//HttpSession session = request.getSession(false);
		IUserView userView = SessionUtils.getUserView(request);
		boolean change = request.getParameter("change") != null;

		if (change) {

			InfoClass oldClassView =
				(InfoClass) request.getAttribute(SessionConstants.CLASS_VIEW);

			if (oldClassView == null) {
				ActionErrors actionErrors = new ActionErrors();
				actionErrors.add(
					"errors.unknownClass",
					new ActionError("errors.unknownClass", className));
				saveErrors(request, actionErrors);
				return mapping.getInputForward();

			}
			InfoClass newClassView =
				(InfoClass) BeanUtils.cloneBean(oldClassView);

			newClassView.setNome(className);

			Object[] argsEditarTurma = { oldClassView, newClassView };
			try {

				ServiceUtils.executeService(
					userView,
					"EditarTurma",
					argsEditarTurma);
			} catch (ExistingServiceException ex) {
				throw new ExistingActionException("A Turma", ex);
			} catch (NotAuthorizedException e) {
				throw e;
			} catch (FenixServiceException e) {
				e.printStackTrace(System.out);
				oldClassView.setNome(newClassView.getNome());
				ActionErrors actionErrors = new ActionErrors();
				actionErrors.add(
					"existingClass",
					new ActionError("errors.existClass", className));
				saveErrors(request, actionErrors);
				return mapping.getInputForward();
			}

			request.setAttribute(SessionConstants.CLASS_VIEW, newClassView);

		} else { /** starting editing */
			//InfoClass classView = getInfoTurma(userView, className, request);
			request.setAttribute(
				SessionConstants.CLASS_VIEW,
				getInfoTurma(
					SessionUtils.getUserView(request),
					className,
					request));
		}

		setLessonListToSession(request, userView, className);

		return mapping.getInputForward();
	}

	/**
	 * View class time table
	 */
	public ActionForward viewClass(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		//String className = getClassName(form);
		IUserView userView = SessionUtils.getUserView(request);
		//InfoClass classView = getInfoTurma(userView, className, request);
		InfoClass infoClass = (InfoClass) request.getAttribute(SessionConstants.CLASS_VIEW);
		setLessonListToSession(request, userView, infoClass.getNome());
		//request.setAttribute(SessionConstants.CLASS_VIEW, classView);

		return mapping.getInputForward();
	}

	/**
	 * Delete class.
	 * */
	public ActionForward deleteClass(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		//DynaValidatorForm classForm = (DynaValidatorForm) form;
		//HttpSession session = request.getSession(false);

		//String className = getClassName(classForm);
		InfoClass infoClass = (InfoClass) request.getAttribute(SessionConstants.CLASS_VIEW);

		IUserView userView = SessionUtils.getUserView(request);

		//InfoClass infoClass = getInfoTurma(userView, className, request);

		//		ClassKey keyClass = new ClassKey(className);
		Object argsApagarTurma[] = { infoClass };
		ServiceUtils.executeService(userView, "ApagarTurma", argsApagarTurma);

		request.removeAttribute(SessionConstants.CLASS_VIEW);

		return mapping.findForward("listClasses");
	}

	/**
	 * 
	 *  Private methods
	 * 
	 * */

	private String getClassName(ActionForm form) {
		DynaValidatorForm classForm = (DynaValidatorForm) form;

		return (String) classForm.get(CLASS_NAME_PARAM);

	}

	private InfoClass getInfoTurma(
		IUserView userView,
		String className,
		HttpServletRequest request)
		throws Exception {
		/* :FIXME: put this 2 variables into parameters */
		InfoExecutionPeriod infoExecutionPeriod =
			(InfoExecutionPeriod) request.getAttribute(
				SessionConstants.EXECUTION_PERIOD);
		InfoExecutionDegree infoExecutionDegree =
			(InfoExecutionDegree) request.getAttribute(
				SessionConstants.EXECUTION_DEGREE);

		Object argsLerTurma[] =
			{ className, infoExecutionDegree, infoExecutionPeriod };
		InfoClass classView =
			(InfoClass) ServiceUtils.executeService(
				userView,
				"LerTurma",
				argsLerTurma);
		return classView;
	}

	private void setLessonListToSession(
		HttpServletRequest request,
		IUserView userView,
		String className)
		throws Exception {

		InfoExecutionPeriod infoExecutionPeriod =
			(InfoExecutionPeriod) request.getAttribute(
				SessionConstants.EXECUTION_PERIOD);
		InfoExecutionDegree infoExecutionDegree =
			(InfoExecutionDegree) request.getAttribute(
				SessionConstants.EXECUTION_DEGREE);

		InfoClass infoClass = new InfoClass();

		infoClass.setInfoExecutionDegree(infoExecutionDegree);
		infoClass.setInfoExecutionPeriod(infoExecutionPeriod);
		infoClass.setNome(className);

		Object argsApagarTurma[] = { infoClass };

		/** InfoLesson ArrayList */
		ArrayList lessonList =
			(ArrayList) ServiceUtils.executeService(
				userView,
				"LerAulasDeTurma",
				argsApagarTurma);

		request.setAttribute(SessionConstants.LESSON_LIST_ATT, lessonList);

	}

}
