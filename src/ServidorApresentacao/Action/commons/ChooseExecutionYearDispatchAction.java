package ServidorApresentacao.Action.commons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.util.MessageResources;

import DataBeans.InfoExecutionDegree;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.TipoCurso;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class ChooseExecutionYearDispatchAction extends DispatchAction
{

	public ActionForward chooseDegreeFromList(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{

		HttpSession session = request.getSession(false);

		if (session != null)
		{

			session.removeAttribute(SessionConstants.MASTER_DEGREE_LIST);

			IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

			TipoCurso degreeType = TipoCurso.MESTRADO_OBJ;

			Object args[] = { degreeType };

			List result = null;
			try
			{
				result = (List) ServiceManagerServiceFactory.executeService(userView, "ReadAllMasterDegrees", args);
			} catch (NonExistingServiceException e)
			{
				throw new NonExistingActionException("O Curso de Mestrado", e);
			}

			request.setAttribute(SessionConstants.MASTER_DEGREE_LIST, result);

			return mapping.findForward("DisplayMasterDegreeList");
		} else
			throw new Exception();
	}
	
	public ActionForward chooseMasterDegree(
		  ActionMapping mapping,
		  ActionForm form,
		  HttpServletRequest request,
		  HttpServletResponse response)
		  throws Exception
	  {
	
		  HttpSession session = request.getSession(false);
	
		  if (session != null)
		  {
	
			  IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
	
			  //Get the Chosen Master Degree
			  Integer masterDegreeID = new Integer(request.getParameter("degreeID"));
			  if (masterDegreeID == null)
			  {
				  masterDegreeID = (Integer) request.getAttribute("degreeID");
			  }
	
			  Object args[] = { masterDegreeID };
			  List result = null;
	
			  try
			  {
	
				  result =
					  (List) ServiceManagerServiceFactory.executeService(userView, "ReadCPlanFromChosenMasterDegree", args);
	
			  } catch (NonExistingServiceException e)
			  {
				  throw new NonExistingActionException("O plano curricular ", e);
			  }
	
			  request.setAttribute(SessionConstants.MASTER_DEGREE_CURRICULAR_PLAN_LIST, result);
	
			  return mapping.findForward("MasterDegreeReady");
		  } else
			  throw new Exception();
	  }

	public ActionForward prepareChooseExecutionYear(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{

	//	HttpSession session = request.getSession(false);
		MessageResources messages = getResources(request);

		// Get Execution Year List
	/*	IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
		ArrayList executionYearList = null;
		try
		{
			executionYearList =
				(ArrayList) ServiceManagerServiceFactory.executeService(
					userView,
					"ReadNotClosedExecutionYears",
					null);
		}
		catch (ExistingServiceException e)
		{
			throw new ExistingActionException(e);
		}

		if (request.getParameter("jspTitle") != null)
		{
			request.setAttribute("jspTitle", messages.getMessage(request.getParameter("jspTitle")));
		}

		List executionYearsLabels = transformIntoLabels(executionYearList);
		
		request.setAttribute(SessionConstants.EXECUTION_YEAR_LIST, executionYearsLabels);*/
		HttpSession session = request.getSession(false);

		if (session != null) {
			IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

			//Get the Chosen Master Degree
			 Integer curricularPlanID = new Integer(request.getParameter("curricularPlanID"));
			 if (curricularPlanID == null)
			 {
				curricularPlanID = (Integer) request.getAttribute("curricularPlanID");
			 }
			ArrayList executionYearList = null;
			Object args[] = { curricularPlanID };			
			try {
				executionYearList = (ArrayList) ServiceManagerServiceFactory.executeService(userView, "ReadExecutionDegreesByDegreeCurricularPlanID", args);
			} catch (ExistingServiceException e) {
				throw new ExistingActionException(e);
			}
		   List executionYearsLabels = transformIntoLabels(executionYearList);
		   request.setAttribute(SessionConstants.EXECUTION_YEAR_LIST, executionYearsLabels);
		   request.setAttribute(SessionConstants.EXECUTION_DEGREE, curricularPlanID);

			return mapping.findForward("PrepareSuccess");
		} else
			throw new Exception();

	}

	private List transformIntoLabels(ArrayList executionYearList)
	{
		List executionYearsLabels = new ArrayList();
		CollectionUtils.collect(executionYearList, new Transformer()
		{
			public Object transform(Object input)
			{
				InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) input;
				LabelValueBean labelValueBean =
					new LabelValueBean(infoExecutionDegree.getInfoExecutionYear().getYear(), infoExecutionDegree.getIdInternal().toString());
				return labelValueBean;
			}
		}, executionYearsLabels);
		Collections.sort(executionYearsLabels, new BeanComparator("label"));
		Collections.reverse(executionYearsLabels);
		
		return executionYearsLabels;
	}

	public ActionForward chooseExecutionYear(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{

/*		HttpSession session = request.getSession(false);

		if (session != null)
		{
			session.setAttribute(SessionConstants.EXECUTION_YEAR, request.getParameter("executionYear"));
			request.setAttribute("executionYear", request.getParameter("executionYear"));

			request.setAttribute("jspTitle", request.getParameter("jspTitle"));



			return mapping.findForward("ChooseSuccess");
		}
		else
			throw new Exception();*/
			
		HttpSession session = request.getSession(false);

				if (session != null)
				{
					session.setAttribute(SessionConstants.EXECUTION_YEAR, request.getParameter("executionYear"));
					Integer curricularPlanID = new Integer(request.getParameter("curricularPlanID"));
		
					if (curricularPlanID == null)
					{
					   curricularPlanID = (Integer) request.getAttribute("curricularPlanID");
			   
					}
			
					request.setAttribute(SessionConstants.EXECUTION_DEGREE, request.getParameter("executionDegreeID"));
					return mapping.findForward("ChooseSuccess");
				} else
					throw new Exception();
		
			
	}

}