/*
 * Created on 16/Mai/2003
 * 
 *  
 */
package ServidorApresentacao.Action.student.enrollment;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoStudent;
import DataBeans.comparators.ComparatorByNameForInfoExecutionDegree;
import DataBeans.enrollment.shift.InfoShiftEnrollment;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.commons.TransactionalDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import Util.ExecutionDegreesFormat;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author tdi-dev (bruno) Modified by T�nia Pous�o Modified by Fernanda Quit�rio
 *  
 */
public class ShiftStudentEnrolmentManagerDispatchAction extends TransactionalDispatchAction
{
	public ActionForward prepareStartViewWarning(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		return mapping.findForward("prepareEnrollmentViewWarning");
	}

	public ActionForward start(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		super.createToken(request);

		return chooseCourses(mapping, form, request, response);
	}

	public ActionForward chooseCourses(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		ActionErrors errors = new ActionErrors();
		IUserView userView = SessionUtils.getUserView(request);

		//TODO:FIXME:THIS IS JUST A TEMPORARY BYPASS TO PREVENT 1ST YEAR STUDENTS FROM ENROLLING IN
		// SHIFTS
		if ((new Integer(userView.getUtilizador().substring(1))).intValue() > 53227)
		{
			errors.add(
				"notAuthorizedShiftEnrollment",
				new ActionError("error.notAuthorized.ShiftEnrollment"));
			saveErrors(request, errors);
			return mapping.findForward("studentFirstPage");
		}

		DynaActionForm enrolmentForm = (DynaActionForm) form;
		Integer executionDegreeIdChosen = (Integer) enrolmentForm.get("degree");

		String studentNumber = obtainStudentNumber(request, userView);

		InfoShiftEnrollment infoShiftEnrollment = null;
		Object[] args = { Integer.valueOf(studentNumber), executionDegreeIdChosen };
		try
		{
			infoShiftEnrollment =
				(InfoShiftEnrollment) ServiceManagerServiceFactory.executeService(
					userView,
					"PrepareInfoShiftEnrollmentByStudentNumber",
					args);
		}
		catch (FenixServiceException serviceException)
		{
			errors.add("error", new ActionError(serviceException.getMessage()));
			saveErrors(request, errors);
			return mapping.findForward("studentFirstPage");
		}

		//inicialize the form with the degree chosen and student number
		enrolmentForm.set("degree", infoShiftEnrollment.getInfoExecutionDegree().getIdInternal());
		enrolmentForm.set("studentId", infoShiftEnrollment.getInfoStudent().getIdInternal());

		request.setAttribute("infoShiftEnrollment", infoShiftEnrollment);

		String selectCourses = checkParameter(request);
		if (infoShiftEnrollment.getInfoShiftEnrollment() != null
			&& infoShiftEnrollment.getInfoShiftEnrollment().size() > 0
			&&  selectCourses == null)
		{
			order(infoShiftEnrollment);
			return mapping.findForward("showShiftsEnrollment");
		}
		else
		{
			//order degree's list and format them names
			if (infoShiftEnrollment.getInfoExecutionDegreesList() != null
				&& infoShiftEnrollment.getInfoExecutionDegreesList().size() > 0)
			{
				Collections.sort(
					infoShiftEnrollment.getInfoExecutionDegreesList(),
					new ComparatorByNameForInfoExecutionDegree());
				infoShiftEnrollment.setInfoExecutionDegreesLabelsList(
					ExecutionDegreesFormat.buildExecutionDegreeLabelValueBean(
						infoShiftEnrollment.getInfoExecutionDegreesList()));
			}

			return mapping.findForward("selectCourses");
		}
	}

	private void order(InfoShiftEnrollment infoShiftEnrollment)
	{
		ComparatorChain comparator = new ComparatorChain();
		comparator.addComparator(new BeanComparator("infoDisciplinaExecucao.nome"));
		comparator.addComparator(new BeanComparator("tipo"));
		Collections.sort(infoShiftEnrollment.getInfoShiftEnrollment(), comparator);
	}
	
	private String checkParameter(HttpServletRequest request)
	{
		String selectCourses = request.getParameter("selectCourses");
		if(selectCourses != null) {
		request.setAttribute("selectCourses", selectCourses);
		}
		return selectCourses;
	}

	private String obtainStudentNumber(HttpServletRequest request, IUserView userView)
		throws FenixActionException
	{
		String studentNumber = getStudent(request);
		if (studentNumber == null)
		{
			InfoStudent infoStudent = obtainStudent(request, userView);
			studentNumber = infoStudent.getNumber().toString();
		}
		return studentNumber;
	}

	private String getStudent(HttpServletRequest request)
	{
		String studentNumber = request.getParameter("studentNumber");
		if (studentNumber == null)
		{
			studentNumber = (String) request.getAttribute("studentNumber");
		}
		return studentNumber;
	}

	public ActionForward unEnroleStudentFromShift(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		IUserView userView = SessionUtils.getUserView(request);

		String studentIDString = request.getParameter("studentId");
		String shidtIDString = request.getParameter("shiftId");

		Integer studentId = new Integer(studentIDString);
		Integer shiftId = new Integer(shidtIDString);

		try
		{
			Object args[] = { studentId, shiftId };
			ServiceManagerServiceFactory.executeService(userView, "UnEnrollStudentFromShift", args);
		}
		catch (FenixServiceException e)
		{
			throw new FenixActionException(e);
		}

		return start(mapping, form, request, response);
	}

	private InfoStudent obtainStudent(HttpServletRequest request, IUserView userView)
		throws FenixActionException
	{
		InfoStudent infoStudent = null;
		try
		{
			Object args[] = { userView.getUtilizador()};
			infoStudent =
				(InfoStudent) ServiceManagerServiceFactory.executeService(
					userView,
					"ReadStudentByUsername",
					args);
		}
		catch (FenixServiceException e)
		{
			throw new FenixActionException(e);
		}
		return infoStudent;
	}

}
