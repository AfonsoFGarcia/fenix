/*
 * Created on 3/Fev/2004
 *  
 */
package ServidorApresentacao.Action.coordinator;

import java.util.Arrays;
import java.util.HashMap;
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
import org.apache.struts.actions.LookupDispatchAction;

import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author T�nia Pous�o
 *  
 */
public class TutorManagementLookupDispatchAction extends LookupDispatchAction
{

	public ActionForward insertTutorShipWithOneStudent(
		ActionMapping mapping,
		ActionForm actionForm,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		ActionErrors errors = new ActionErrors();

		HttpSession session = request.getSession();
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		DynaActionForm tutorForm = (DynaActionForm) actionForm;
		Integer tutorNumber = Integer.valueOf((String) tutorForm.get("tutorNumber"));
		request.setAttribute("tutorNumber", tutorNumber);

		Integer executionDegreeId = Integer.valueOf((String) tutorForm.get("executionDegreeId"));
		request.setAttribute("executionDegreeId", executionDegreeId);

		Integer studentNumber = null;
		try
		{
			studentNumber = Integer.valueOf((String) tutorForm.get("studentNumber"));
		}
		catch (NumberFormatException e)
		{
			errors.add("errors", new ActionError("error.tutor.numberAndRequired"));
			saveErrors(request, errors);
			return mapping.getInputForward();
		}

		Object[] args = { executionDegreeId, tutorNumber, studentNumber };

		try
		{
			ServiceManagerServiceFactory.executeService(userView, "InsertTutorShipWithOneStudent", args);
		}
		catch (NonExistingServiceException e1)
		{
			e1.printStackTrace();
			if (e1.getMessage().endsWith("Teacher"))
			{
				errors.add("errors", new ActionError(e1.getMessage(), tutorNumber));
			}
			if (e1.getMessage().endsWith("Student"))
			{
				errors.add("errors", new ActionError(e1.getMessage(), studentNumber));
			}
		}
		catch (FenixServiceException e2)
		{
			e2.printStackTrace();
			if (e2.getMessage().endsWith("NoDegree"))
			{
				errors.add("errors", new ActionError(e2.getMessage(), studentNumber));
			}
			else if (e2.getMessage().endsWith("Tutor"))
			{
				errors.add("errors", new ActionError(e2.getMessage(), studentNumber));
			}
			else
			{
				errors.add("errors", new ActionError(e2.getMessage()));
			}
		}
		if (!errors.isEmpty())
		{
			saveErrors(request, errors);
			return mapping.getInputForward();
		}

		return mapping.findForward("confirmation");
	}

	public ActionForward insertTutorShipWithManyStudent(
		ActionMapping mapping,
		ActionForm actionForm,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		ActionErrors errors = new ActionErrors();

		HttpSession session = request.getSession();
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		DynaActionForm tutorForm = (DynaActionForm) actionForm;
		Integer tutorNumber = Integer.valueOf((String) tutorForm.get("tutorNumber"));
		request.setAttribute("tutorNumber", tutorNumber);

		Integer executionDegreeId = Integer.valueOf((String) tutorForm.get("executionDegreeId"));
		request.setAttribute("executionDegreeId", executionDegreeId);

		Integer studentNumberFirst = null;
		Integer studentNumberSecond = null;
		try
		{
			studentNumberFirst = Integer.valueOf((String) tutorForm.get("studentNumberFirst"));
			studentNumberSecond = Integer.valueOf((String) tutorForm.get("studentNumberSecond"));
		}
		catch (NumberFormatException e)
		{
			errors.add("errors", new ActionError("error.tutor.numberAndRequired"));
			saveErrors(request, errors);
			return mapping.getInputForward();
		}
		if (studentNumberFirst.intValue() > studentNumberSecond.intValue())
		{
			errors.add("errors", new ActionError("error.tutor.numbersRange"));
			saveErrors(request, errors);
			return mapping.getInputForward();
		}

		Object[] args = { executionDegreeId, tutorNumber, studentNumberFirst, studentNumberSecond };
		List studentsWithErros = null;
		try
		{
			studentsWithErros =
				(List) ServiceManagerServiceFactory.executeService(
					userView,
					"InsertTutorShipWithManyStudent",
					args);
		}
		catch (NonExistingServiceException e1)
		{
			e1.printStackTrace();
			if (e1.getMessage().endsWith("Teacher"))
			{
				errors.add("errors", new ActionError(e1.getMessage(), tutorNumber));
			}
		}
		catch (FenixServiceException e)
		{
			e.printStackTrace();
			errors.add("errors", new ActionError(e.getMessage()));
		}
		if (!errors.isEmpty())
		{
			saveErrors(request, errors);
			return mapping.getInputForward();
		}
		if (studentsWithErros != null && studentsWithErros.size() > 0)
		{
			request.setAttribute("studentsWithErros", studentsWithErros);
		}

		return mapping.findForward("confirmation");
	}

	public ActionForward deleteTutorShip(
		ActionMapping mapping,
		ActionForm actionForm,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		ActionErrors errors = new ActionErrors();

		HttpSession session = request.getSession();
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		DynaActionForm tutorForm = (DynaActionForm) actionForm;
		Integer tutorNumber = Integer.valueOf((String) tutorForm.get("tutorNumber"));
		request.setAttribute("tutorNumber", tutorNumber);

		Integer executionDegreeId = Integer.valueOf((String) tutorForm.get("executionDegreeId"));
		request.setAttribute("executionDegreeId", executionDegreeId);

		Integer[] deletedTutors = (Integer[]) tutorForm.get("deletedTutorsIds");
		List deletedTutorsList = Arrays.asList(deletedTutors);
		Object[] args = { executionDegreeId, tutorNumber, deletedTutorsList };

		try
		{
			ServiceManagerServiceFactory.executeService(userView, "DeleteTutorShip", args);
		}
		catch (FenixServiceException e)
		{
			e.printStackTrace();
			errors.add("errors", new ActionError(e.getMessage()));
		}
		if (!errors.isEmpty())
		{
			saveErrors(request, errors);
			return mapping.getInputForward();
		}

		return mapping.findForward("confirmation");
	}

	public ActionForward cancel(
		ActionMapping mapping,
		ActionForm actionForm,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		return mapping.findForward("cancel");
	}

	protected Map getKeyMethodMap()
	{
		Map map = new HashMap();
		map.put("button.coordinator.tutor.associateOneStudent", "insertTutorShipWithOneStudent");
		map.put("button.coordinator.tutor.associateManyStudent", "insertTutorShipWithManyStudent");
		map.put("button.coordinator.tutor.remove", "deleteTutorShip");
		map.put("button.cancel", "cancel");
		return map;
	}

}
