package ServidorApresentacao.Action.degreeAdministrativeOffice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.InfoExecutionYear;
import DataBeans.InfoObject;
import DataBeans.InfoRole;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.BothAreasAreTheSameServiceException;
import ServidorAplicacao.Servico.exceptions.ChosenAreasAreIncompatibleServiceException;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorAplicacao.Servico.exceptions.OutOfCurricularCourseEnrolmentPeriod;
import ServidorAplicacao.strategy.enrolment.context.InfoStudentEnrolmentContext;
import ServidorApresentacao.Action.commons.TransactionalDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import Util.Data;
import Util.RoleType;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Fernanda Quit�rio 27/Jan/2004
 *  
 */
public class CurricularCoursesEnrollmentDispatchAction extends TransactionalDispatchAction
{

	public ActionForward prepareEnrollmentChooseStudent(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		getExecutionDegree(request);

		return mapping.findForward("prepareEnrollmentChooseStudent");
	}

	public ActionForward prepareEnrollmentChooseStudentAndExecutionYear(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{		
		System.out.println("prepareEnrollmentChooseStudentAndExecutionYear");
		ActionErrors errors = new ActionErrors();

		//degree type's code 
		String degreeType = request.getParameter("degreeType");
		request.setAttribute("degreeType", degreeType);
		
		//execution years
		List executionYears = null;
		Object[] args = {
		};
		try
		{
			executionYears =
			(List) ServiceManagerServiceFactory.executeService(
					null,
					"ReadNotClosedExecutionYears",
					args);
		}
		catch (FenixServiceException e)
		{
			errors.add("noExecutionYears", new ActionError("error.impossible.insertExemptionGratuity"));
			saveErrors(request, errors);
			return mapping.getInputForward();
		}
		if (executionYears == null || executionYears.size() <= 0)
		{
			errors.add("noExecutionYears", new ActionError("error.impossible.insertExemptionGratuity"));
			saveErrors(request, errors);
			return mapping.getInputForward();
		}

		ComparatorChain comparator = new ComparatorChain();
		comparator.addComparator(new BeanComparator("year"), true);
		Collections.sort(executionYears, comparator);

		List executionYearLabels = buildLabelValueBeanForJsp(executionYears);
		request.setAttribute("executionYears", executionYearLabels);
				
		return mapping.findForward("prepareEnrollmentChooseStudentWithoutRules");
	}

	private List buildLabelValueBeanForJsp(List infoExecutionYears)
	{
		List executionYearLabels = new ArrayList();
		CollectionUtils.collect(infoExecutionYears, new Transformer()
				{
			public Object transform(Object arg0)
			{
				InfoExecutionYear infoExecutionYear = (InfoExecutionYear) arg0;

				LabelValueBean executionYear =
				new LabelValueBean(infoExecutionYear.getYear(), infoExecutionYear.getYear());
				return executionYear;
			}
		}, executionYearLabels);
		return executionYearLabels;
	}
	
	private Integer getExecutionDegree(HttpServletRequest request)
	{
		Integer executionDegreeId = null;

		String executionDegreeIdString = request.getParameter("executionDegreeId");
		if (executionDegreeIdString == null)
		{
			executionDegreeIdString = (String) request.getAttribute("executionDegreeId");
		}
		if (executionDegreeIdString != null)
		{
			executionDegreeId = Integer.valueOf(executionDegreeIdString);
		}
		request.setAttribute("executionDegreeId", executionDegreeId);

		return executionDegreeId;
	}

	public ActionForward start(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		super.createToken(request);
		return prepareEnrollmentChooseCurricularCourses(mapping, form, request, response);
	}

	private ActionForward prepareEnrollmentChooseCurricularCourses(
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

		Integer executionDegreeId = getExecutionDegree(request);
		InfoStudentEnrolmentContext infoStudentEnrolmentContext = null;
		Object[] args = { executionDegreeId, null, studentNumber };
		try
		{
			if (!(userView.getRoles().contains(new InfoRole(RoleType.DEGREE_ADMINISTRATIVE_OFFICE))
				|| userView.getRoles().contains(
					new InfoRole(RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER))))
			{
				infoStudentEnrolmentContext =
					(InfoStudentEnrolmentContext) ServiceManagerServiceFactory.executeService(
						userView,
						"ShowAvailableCurricularCoursesNew",
						args);
			}
			else
			{
				infoStudentEnrolmentContext =
					(InfoStudentEnrolmentContext) ServiceManagerServiceFactory.executeService(
						userView,
						"ShowAvailableCurricularCoursesWithoutEnrollmentPeriod",
						args);
			}
		}
		catch (NotAuthorizedException e)
		{
			if (e.getMessage() != null)
			{
				addAuthorizationErrors(errors, e);
			}
			else
			{
				errors.add("notauthorized", new ActionError("error.exception.notAuthorized"));
			}
		}
		catch (ExistingServiceException e)
		{
			if (e.getMessage().equals("student"))
			{
				errors.add("student", new ActionError("error.no.student.in.database", studentNumber));
			}
			else if (e.getMessage().equals("studentCurricularPlan"))
			{
				errors.add(
					"studentCurricularPlan",
					new ActionError("error.student.curricularPlan.nonExistent"));
			}
		}
		catch (OutOfCurricularCourseEnrolmentPeriod e)
		{

			errors.add(
				"enrolment",
				new ActionError(
					e.getMessageKey(),
					Data.format2DayMonthYear(e.getStartDate()),
					Data.format2DayMonthYear(e.getEndDate())));
		}
		catch (FenixServiceException e)
		{
			if (e.getMessage().equals("degree"))
			{
				errors.add("degree", new ActionError("error.student.degreeCurricularPlan.LEEC"));
			}
			if (e.getMessage().equals("enrolmentPeriod"))
			{
				errors.add("enrolmentPeriod", new ActionError("error.student.enrolmentPeriod"));
			}

			throw new FenixActionException(e);
		}
		if (!errors.isEmpty())
		{
			saveErrors(request, errors);
			return mapping.getInputForward();
		}

		Collections.sort(
			infoStudentEnrolmentContext.getStudentCurrentSemesterInfoEnrollments(),
			new BeanComparator("infoCurricularCourse.name"));
		Collections.sort(
			infoStudentEnrolmentContext.getFinalInfoCurricularCoursesWhereStudentCanBeEnrolled(),
			new BeanComparator("name"));

		Integer[] enrolledInArray =
			buildArrayForForm(infoStudentEnrolmentContext.getStudentCurrentSemesterInfoEnrollments());
		enrollmentForm.set("enrolledCurricularCoursesBefore", enrolledInArray);
		enrollmentForm.set("enrolledCurricularCoursesAfter", enrolledInArray);

		request.setAttribute("infoStudentEnrolmentContext", infoStudentEnrolmentContext);

		return mapping.findForward("prepareEnrollmentChooseCurricularCourses");
	}

	private void addAuthorizationErrors(ActionErrors errors, NotAuthorizedException e)
	{
		String messageException = e.getCause().getCause().getMessage();
		String message = null;
		String arg1 = null;
		String arg2 = null;
		if (messageException.indexOf("+") != -1)
		{
			message = messageException.substring(0, messageException.indexOf("+"));
			String newMessage = messageException.substring(messageException.indexOf("+") + 1);
			if (newMessage.indexOf("+") != -1)
			{
				arg1 = newMessage.substring(0, newMessage.indexOf("+"));
				arg2 = newMessage.substring(newMessage.indexOf("+") + 1);
			}
			else
			{
				arg1 = newMessage;
			}
		}
		else
		{
			message = messageException;
		}
		errors.add("notauthorized", new ActionError(message, arg1, arg2));
	}

	private Integer[] buildArrayForForm(List listToTransform)
	{
		List newList = new ArrayList();
		newList = (List) CollectionUtils.collect(listToTransform, new Transformer()
		{
			public Object transform(Object arg0)
			{
				InfoObject infoObject = (InfoObject) arg0;
				return infoObject.getIdInternal();
			}
		});
		Integer[] array = new Integer[newList.size()];
		for (int i = 0; i < array.length; i++)
		{
			array[i] = (Integer) newList.get(i);
		}
		return array;
	}

	public ActionForward prepareEnrollmentPrepareChooseAreas(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		super.createToken(request);

		IUserView userView = SessionUtils.getUserView(request);
		ActionErrors errors = new ActionErrors();
		DynaValidatorForm enrollmentForm = (DynaValidatorForm) form;

		Integer studentNumber = Integer.valueOf(request.getParameter("studentNumber"));
		enrollmentForm.set("studentNumber", studentNumber.toString());

		String specialization = request.getParameter("specializationArea");
		String secondary = request.getParameter("secondaryArea");

		maintainEnrollmentState(request, studentNumber);

		Integer executionDegreeId = getExecutionDegree(request);
		List infoBranches = null;
		Object[] args = { executionDegreeId, null, studentNumber };
		try
		{
			infoBranches =
				(List) ServiceManagerServiceFactory.executeService(
					userView,
					"ReadSpecializationAndSecundaryAreasByStudent",
					args);
		}
		catch (NotAuthorizedException e)
		{
			if (e.getMessage() != null)
			{
				addAuthorizationErrors(errors, e);
			}
			else
			{
				errors.add("notauthorized", new ActionError("error.exception.notAuthorized"));
			}
		}
		catch (ExistingServiceException e)
		{
			if (e.getMessage().equals("student"))
			{
				errors.add("student", new ActionError("error.no.student.in.database", studentNumber));
			}
			else if (e.getMessage().equals("studentCurricularPlan"))
			{
				errors.add(
					"studentCurricularPlan",
					new ActionError("error.student.curricularPlan.nonExistent"));
			}
		}
		catch (FenixServiceException e)
		{
			throw new FenixActionException();
		}
		if (!errors.isEmpty())
		{
			saveErrors(request, errors);
			return mapping.findForward("beginTransaction");
		}
		if (specialization != null
			&& specialization.length() > 0
			&& secondary != null
			&& secondary.length() > 0)
		{
			enrollmentForm.set("specializationArea", Integer.valueOf(specialization));
			enrollmentForm.set("secondaryArea", Integer.valueOf(secondary));
		}

		Collections.sort(infoBranches, new BeanComparator("name"));

		request.setAttribute("infoBranches", infoBranches);
		return mapping.findForward("prepareEnrollmentChooseAreas");
	}

	private void maintainEnrollmentState(HttpServletRequest request, Integer studentNumber)
	{
		String executionPeriod = request.getParameter("executionPeriod");
		String executionYear = request.getParameter("executionYear");
		String studentName = request.getParameter("studentName");
		String studentCurricularPlanId = request.getParameter("studentCurricularPlanId");

		request.setAttribute("executionPeriod", executionPeriod);
		request.setAttribute("executionYear", executionYear);
		request.setAttribute("studentName", studentName);
		request.setAttribute("studentNumber", studentNumber.toString());
		request.setAttribute("studentCurricularPlanId", studentCurricularPlanId);
	}

	public ActionForward prepareEnrollmentChooseAreas(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		super.validateToken(request, form, mapping, "error.transaction.enrollment");

		IUserView userView = SessionUtils.getUserView(request);
		ActionErrors errors = new ActionErrors();
		DynaValidatorForm enrollmentForm = (DynaValidatorForm) form;
		Integer specializationArea = (Integer) enrollmentForm.get("specializationArea");
		Integer secondaryArea = (Integer) enrollmentForm.get("secondaryArea");
		Integer studentCurricularPlanId =
			Integer.valueOf(request.getParameter("studentCurricularPlanId"));
		Integer studentNumber = Integer.valueOf((String) enrollmentForm.get("studentNumber"));

		Integer executionDegreeId = getExecutionDegree(request);
		Object[] args =
			{ executionDegreeId, studentCurricularPlanId, specializationArea, secondaryArea };
		try
		{
			ServiceManagerServiceFactory.executeService(userView, "WriteStudentAreas", args);
		}
		catch (NotAuthorizedException e)
		{
			if (e.getMessage() != null)
			{
				addAuthorizationErrors(errors, e);
			}
			else
			{
				errors.add("notauthorized", new ActionError("error.exception.notAuthorized"));
			}
		}
		catch (BothAreasAreTheSameServiceException e)
		{
			errors.add("bothAreas", new ActionError("error.student.enrollment.AreasNotEqual"));
		}
		catch (ChosenAreasAreIncompatibleServiceException e)
		{
			errors.add(
				"incompatibleAreas",
				new ActionError("error.student.enrollment.incompatibleAreas"));
		}
		catch (ExistingServiceException e)
		{
			errors.add(
				"studentCurricularPlan",
				new ActionError("error.student.curricularPlan.nonExistent"));
		}
		catch (InvalidArgumentsServiceException e)
		{
			errors.add("areas", new ActionError("error.areas.choose"));
		}
		catch (FenixServiceException e)
		{
			throw new FenixActionException(e);
		}
		if (!errors.isEmpty())
		{
			maintainEnrollmentState(request, studentNumber);
			saveErrors(request, errors);
			return mapping.findForward("prepareChooseAreas");
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
		super.validateToken(request, form, mapping, "error.transaction.enrollment");

		ActionErrors errors = new ActionErrors();
		IUserView userView = SessionUtils.getUserView(request);
		DynaValidatorForm enrollmentForm = (DynaValidatorForm) form;
		Integer[] curricularCoursesToEnroll =
			(Integer[]) enrollmentForm.get("unenrolledCurricularCourses");
		Integer studentCurricularPlanId =
			Integer.valueOf(request.getParameter("studentCurricularPlanId"));

		List toEnroll = Arrays.asList(curricularCoursesToEnroll);
		if (toEnroll.size() == 1)
		{
			Integer executionDegreeId = getExecutionDegree(request);
			Object[] args = { executionDegreeId, studentCurricularPlanId, toEnroll.get(0), null };
			try
			{
				ServiceManagerServiceFactory.executeService(userView, "WriteEnrolment", args);
			}
			catch (NotAuthorizedException e)
			{
				if (e.getMessage() != null)
				{
					addAuthorizationErrors(errors, e);
				}
				else
				{
					errors.add("notauthorized", new ActionError("error.exception.notAuthorized"));
				}
				saveErrors(request, errors);
				return mapping.findForward("prepareEnrollmentChooseCurricularCourses");
			}
			catch (FenixServiceException e)
			{
				throw new FenixActionException(e);
			}
		}
		return prepareEnrollmentChooseCurricularCourses(mapping, form, request, response);
	}

	public ActionForward unenrollFromCurricularCourse(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		super.validateToken(request, form, mapping, "error.transaction.enrollment");

		ActionErrors errors = new ActionErrors();
		IUserView userView = SessionUtils.getUserView(request);
		DynaValidatorForm enrollmentForm = (DynaValidatorForm) form;
		Integer[] enrolledCurricularCoursesBefore =
			(Integer[]) enrollmentForm.get("enrolledCurricularCoursesBefore");
		Integer[] enrolledCurricularCoursesAfter =
			(Integer[]) enrollmentForm.get("enrolledCurricularCoursesAfter");

		List enrollmentsBefore = Arrays.asList(enrolledCurricularCoursesBefore);
		List enrollmentsAfter = Arrays.asList(enrolledCurricularCoursesAfter);
		List toUnenroll = (List) CollectionUtils.subtract(enrollmentsBefore, enrollmentsAfter);
		if (toUnenroll.size() == 1)
		{
			Integer studentCurricularPlanId =
				Integer.valueOf(request.getParameter("studentCurricularPlanId"));

			Integer executionDegreeId = getExecutionDegree(request);
			Object[] args = { executionDegreeId, studentCurricularPlanId, (Integer) toUnenroll.get(0)};
			try
			{
				ServiceManagerServiceFactory.executeService(userView, "DeleteEnrolment", args);
			}
			catch (NotAuthorizedException e)
			{
				if (e.getMessage() != null)
				{
					addAuthorizationErrors(errors, e);
				}
				else
				{
					errors.add("notauthorized", new ActionError("error.exception.notAuthorized"));
				}
				saveErrors(request, errors);
			}
			catch (FenixServiceException e)
			{
				throw new FenixActionException(e);
			}
		}
		return prepareEnrollmentChooseCurricularCourses(mapping, form, request, response);
	}

	public ActionForward enrollmentConfirmation(
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
		Integer studentCurricularPlanId =
			Integer.valueOf(request.getParameter("studentCurricularPlanId"));

		Integer executionDegreeId = getExecutionDegree(request);
		InfoStudentEnrolmentContext infoStudentEnrolmentContext = null;
		Object[] args = { executionDegreeId, null, studentNumber };
		try
		{
			if (!(userView.getRoles().contains(new InfoRole(RoleType.DEGREE_ADMINISTRATIVE_OFFICE))
				|| userView.getRoles().contains(
					new InfoRole(RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER))))
			{
				infoStudentEnrolmentContext =
					(InfoStudentEnrolmentContext) ServiceManagerServiceFactory.executeService(
						userView,
						"ShowAvailableCurricularCoursesNew",
						args);
			}
			else
			{
				infoStudentEnrolmentContext =
					(InfoStudentEnrolmentContext) ServiceManagerServiceFactory.executeService(
						userView,
						"ShowAvailableCurricularCoursesWithoutEnrollmentPeriod",
						args);
			}
		}
		catch (NotAuthorizedException e)
		{
			if (e.getMessage() != null)
			{
				addAuthorizationErrors(errors, e);
			}
			else
			{
				errors.add("notauthorized", new ActionError("error.exception.notAuthorized"));
			}
		}
		catch (ExistingServiceException e)
		{
			if (e.getMessage().equals("student"))
			{
				errors.add("student", new ActionError("error.no.student.in.database", studentNumber));
			}
			else if (e.getMessage().equals("studentCurricularPlan"))
			{
				errors.add(
					"studentCurricularPlan",
					new ActionError("error.student.curricularPlan.nonExistent"));
			}
		}
		catch (OutOfCurricularCourseEnrolmentPeriod e)
		{
			errors.add(
				"enrolment",
				new ActionError(
					e.getMessageKey(),
					Data.format2DayMonthYear(e.getStartDate()),
					Data.format2DayMonthYear(e.getEndDate())));
		}
		catch (FenixServiceException e)
		{
			if (e.getMessage().equals("degree"))
			{
				errors.add("degree", new ActionError("error.student.degreeCurricularPlan.LEEC"));
			}
			if (e.getMessage().equals("enrolmentPeriod"))
			{
				errors.add("enrolmentPeriod", new ActionError("error.student.enrolmentPeriod"));
			}

			throw new FenixActionException(e);
		}
		if (!errors.isEmpty())
		{
			saveErrors(request, errors);
			return prepareEnrollmentChooseCurricularCourses(mapping, form, request, response);
		}

		List curriculum = null;
		Object args2[] = { executionDegreeId, studentCurricularPlanId };
		try
		{
			curriculum =
				(ArrayList) ServiceManagerServiceFactory.executeService(
					userView,
					"ReadStudentCurriculumForEnrollmentConfirmation",
					args2);
		}
		catch (NotAuthorizedException e)
		{
			if (e.getMessage() != null)
			{
				addAuthorizationErrors(errors, e);
			}
			else
			{
				errors.add("notauthorized", new ActionError("error.exception.notAuthorized"));
			}
			saveErrors(request, errors);
			return prepareEnrollmentChooseCurricularCourses(mapping, form, request, response);
		}
		Collections.sort(
			infoStudentEnrolmentContext.getStudentCurrentSemesterInfoEnrollments(),
			new BeanComparator("infoCurricularCourse.name"));

		sortCurriculum(curriculum);

		request.setAttribute("infoStudentEnrolmentContext", infoStudentEnrolmentContext);
		request.setAttribute("curriculum", curriculum);

		return mapping.findForward("enrollmentConfirmation");
	}

	private void sortCurriculum(List curriculum)
	{
		BeanComparator courseName = new BeanComparator("infoCurricularCourse.name");
		BeanComparator executionYear = new BeanComparator("infoExecutionPeriod.infoExecutionYear.year");
		ComparatorChain chainComparator = new ComparatorChain();
		chainComparator.addComparator(courseName);
		chainComparator.addComparator(executionYear);

		Collections.sort(curriculum, chainComparator);
	}
}