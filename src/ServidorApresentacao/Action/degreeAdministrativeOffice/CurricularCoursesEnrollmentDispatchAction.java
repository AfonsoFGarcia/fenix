package ServidorApresentacao.Action.degreeAdministrativeOffice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.InfoEnrolment;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.strategy.enrolment.context.InfoStudentEnrolmentContext;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Fernanda Quit�rio 27/Jan/2004
 *  
 */
public class CurricularCoursesEnrollmentDispatchAction extends DispatchAction
{

	public ActionForward prepareEnrollmentChooseStudent(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		return mapping.findForward("prepareEnrollmentChooseStudent");
	}

	public ActionForward prepareEnrollmentChooseCurricularCourses(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		IUserView userView = SessionUtils.getUserView(request);
		ActionErrors errors = new ActionErrors();
		DynaValidatorForm enrollmentForm = (DynaValidatorForm) form;

		Integer studentNumber = new Integer((String) enrollmentForm.get("studentNumber"));

		InfoStudentEnrolmentContext infoStudentEnrolmentContext = null;
		Object[] args = { studentNumber };
		try
		{
			infoStudentEnrolmentContext =
				(InfoStudentEnrolmentContext) ServiceManagerServiceFactory.executeService(
					userView,
					"ShowAvailableCurricularCourses",
					args);

		}
		catch (ExistingServiceException e)
		{
			errors.add("studentCurricularPlan", new ActionError(e.getMessage()));
			saveErrors(request, errors);
			return mapping.getInputForward();
		}
		catch (FenixServiceException e)
		{
			e.printStackTrace();
			throw new FenixActionException(e.getMessage());
		}

		Integer[] enrolledInArray = buildArrayOfEnrolled(infoStudentEnrolmentContext);
		enrollmentForm.set("enrolledCurricularCoursesBefore", enrolledInArray);
		enrollmentForm.set("enrolledCurricularCoursesAfter", enrolledInArray);

		// por no form a area de especilizacao e a area secundaria do aluno
		// onde estao as listas das areas de especializacao e area secundaria
		enrollmentForm.set(
			"specializationArea",
			infoStudentEnrolmentContext.getInfoStudentCurricularPlan().getInfoBranch().getName());
		enrollmentForm.set(
			"secondaryArea",
			infoStudentEnrolmentContext
				.getInfoStudentCurricularPlan()
				.getInfoSecundaryBranch()
				.getName());
		request.setAttribute("infoStudentEnrolmentContext", infoStudentEnrolmentContext);

		return mapping.findForward("prepareEnrollmentChooseCurricularCourses");
	}

	private Integer[] buildArrayOfEnrolled(InfoStudentEnrolmentContext infoStudentEnrolmentContext)
	{
		List enrolledCurricularCourses = new ArrayList();
		enrolledCurricularCourses =
			(
				List) CollectionUtils
					.collect(
						infoStudentEnrolmentContext.getStudentCurrentSemesterInfoEnrollments(),
						new Transformer()
		{
			public Object transform(Object arg0)
			{
				InfoEnrolment infoEnrolment = (InfoEnrolment) arg0;
				return infoEnrolment.getIdInternal();
			}
		});
		System.out.println(enrolledCurricularCourses.toString());
		Integer[] enrolledInArray = new Integer[enrolledCurricularCourses.size()];
		for (int i = 0; i < enrolledInArray.length; i++)
		{
			enrolledInArray[i] = (Integer) enrolledCurricularCourses.get(i);
		}
		return enrolledInArray;
	}

	public ActionForward prepareEnrollmentChooseAreas(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		IUserView userView = SessionUtils.getUserView(request);
		DynaValidatorForm enrollmentForm = (DynaValidatorForm) form;
		Integer specializationArea = (Integer) enrollmentForm.get("specializationArea");
		Integer secondaryArea = (Integer) enrollmentForm.get("secondaryArea");

		// alterar as areas do aluno
		Object[] args = { specializationArea, secondaryArea };
		try
		{
			ServiceManagerServiceFactory.executeService(userView, "", args);

		}
		catch (FenixServiceException e)
		{

		}

		return prepareEnrollmentChooseCurricularCourses(mapping, form, request, response);
	}

	public ActionForward enrollInCurricularCourse(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{

		List specializationAreas = new ArrayList();
		List secondaryAreas = new ArrayList();

		request.setAttribute("specializationAreas", specializationAreas);
		request.setAttribute("secondaryAreas", secondaryAreas);
		return mapping.findForward("prepareEnrollmentChooseCurricularCourses");
	}

	public ActionForward unenrollFromCurricularCourse(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		IUserView userView = SessionUtils.getUserView(request);
		DynaValidatorForm enrollmentForm = (DynaValidatorForm) form;
		Integer[] enrolledCurricularCoursesBefore =
			(Integer[]) enrollmentForm.get("enrolledCurricularCoursesBefore");
		Integer[] enrolledCurricularCoursesAfter =
			(Integer[]) enrollmentForm.get("enrolledCurricularCoursesAfter");

		List enrollmentsBefore = Arrays.asList(enrolledCurricularCoursesBefore);
		List enrollmentsAfter = Arrays.asList(enrolledCurricularCoursesAfter);
		System.out.println(enrollmentsBefore);
		System.out.println(enrollmentsAfter);
		List toUnenroll = (List) CollectionUtils.subtract(enrollmentsBefore, enrollmentsAfter);
		System.out.println("valor do unEnroll: " + toUnenroll.toString());
		Object[] args = {(Integer) toUnenroll.get(0)};
		try
		{
			ServiceManagerServiceFactory.executeService(userView, "DeleteEnrolment", args);

		}
		catch (FenixServiceException e)
		{

		}

		return prepareEnrollmentChooseCurricularCourses(mapping, form, request, response);
	}
}