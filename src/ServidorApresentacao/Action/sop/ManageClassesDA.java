package ServidorApresentacao.Action.sop;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanComparator;
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
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao
	.Action
	.sop
	.base
	.FenixExecutionDegreeAndCurricularYearContextDispatchAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import ServidorApresentacao.Action.utils.ContextUtils;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 */
public class ManageClassesDA
	extends FenixExecutionDegreeAndCurricularYearContextDispatchAction {

	public ActionForward listClasses(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		InfoExecutionPeriod infoExecutionPeriod =
			(InfoExecutionPeriod) request.getAttribute(
				SessionConstants.EXECUTION_PERIOD);

		InfoCurricularYear infoCurricularYear =
			(InfoCurricularYear) request.getAttribute(
				SessionConstants.CURRICULAR_YEAR);

		InfoExecutionDegree infoExecutionDegree =
			(InfoExecutionDegree) request.getAttribute(
				SessionConstants.EXECUTION_DEGREE);

		Object argsLerTurmas[] =
			{
				infoExecutionDegree,
				infoExecutionPeriod,
				infoCurricularYear.getYear()};

		List classesList =
			(List) ServiceUtils.executeService(
				SessionUtils.getUserView(request),
				"LerTurmas",
				argsLerTurmas);

		if (classesList != null && !classesList.isEmpty()) {
			BeanComparator nameComparator = new BeanComparator("nome");
			Collections.sort(classesList, nameComparator);
			
			request.setAttribute(SessionConstants.CLASSES, classesList);
		}

		return mapping.findForward("ShowClassList");
	}

	public ActionForward create(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		DynaValidatorForm classForm = (DynaValidatorForm) form;

		String className = (String) classForm.get("className");

		IUserView userView = SessionUtils.getUserView(request);

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

		InfoClass infoClass = new InfoClass();
		infoClass.setNome(className);
		infoClass.setAnoCurricular(curricularYear);
		infoClass.setInfoExecutionDegree(infoExecutionDegree);
		infoClass.setInfoExecutionPeriod(infoExecutionPeriod);

		Object argsCriarTurma[] = { infoClass };

		try {
			infoClass =
				(InfoClass) ServiceUtils.executeService(
					userView,
					"CriarTurma",
					argsCriarTurma);
		} catch (ExistingServiceException e) {
			throw new ExistingActionException("A Turma", e);
		}

		return listClasses(mapping, form, request, response);
	}

	/**
	 * Delete class.
	 * */
	public ActionForward delete(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		ContextUtils.setClassContext(request);

		InfoClass infoClass =
			(InfoClass) request.getAttribute(SessionConstants.CLASS_VIEW);

		IUserView userView = SessionUtils.getUserView(request);

		Object argsApagarTurma[] = { infoClass };
		ServiceUtils.executeService(userView, "ApagarTurma", argsApagarTurma);

		request.removeAttribute(SessionConstants.CLASS_VIEW);

		return listClasses(mapping, form, request, response);
	}

}