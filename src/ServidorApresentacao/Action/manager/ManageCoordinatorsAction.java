/*
 * Created on 11/Dez/2003
 */
package ServidorApresentacao.Action.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoCoordinator;
import DataBeans.InfoExecutionDegree;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author T�nia Pous�o
 *  
 */
public class ManageCoordinatorsAction extends FenixDispatchAction {

	public ActionForward view(
		ActionMapping mapping,
		ActionForm actionForm,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		ActionErrors errors = new ActionErrors();

		Integer executionDegreeId =
			getFromRequest("executionDegreeId", request);
		request.setAttribute("executionDegreeId", executionDegreeId);

		IUserView userView = SessionUtils.getUserView(request);

		Object[] args = { executionDegreeId };
		InfoExecutionDegree infoExecutionDegree = null;
		try {
			infoExecutionDegree =
				(
					InfoExecutionDegree) ServiceManagerServiceFactory
						.executeService(
					userView,
					"ReadExecutionDegree",
					args);
		} catch (FenixServiceException e) {
			e.printStackTrace();
			errors.add(
				"impossibleExecutionDegree",
				new ActionError("error.invalidExecutionDegree"));
		}
		if (infoExecutionDegree == null
			|| infoExecutionDegree.getInfoDegreeCurricularPlan() == null
			|| infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree()
				== null) {
			errors.add(
				"impossibleExecutionDegree",
				new ActionError("error.invalidExecutionDegree"));
		}
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		
		Integer[] responsibleCoordinatorsIds = findResponsibleCoodinators(
		infoExecutionDegree.getCoordinatorsList());
		DynaActionForm coordinatorsForm = (DynaActionForm) actionForm;
		coordinatorsForm.set(
			"responsibleCoordinatorsIds",
			responsibleCoordinatorsIds);
		request.setAttribute("infoExecutionDegree", infoExecutionDegree);	
		request.setAttribute(
			"degreeId",
			infoExecutionDegree
				.getInfoDegreeCurricularPlan()
				.getInfoDegree()
				.getIdInternal());
		request.setAttribute(
			"degreeCurricularPlanId",
			infoExecutionDegree.getInfoDegreeCurricularPlan().getIdInternal());
		
		return mapping.findForward("manageCoordinators");
	}
	
	/**
	 * Get all the responsible professors from the list of coordinators.
	 * 
	 * @param list
	 * @return Integer[]
	 */
	private Integer[] findResponsibleCoodinators(List coordinatorsList) {
		List responsibleCoordinatorsList =
		(List) CollectionUtils.select(coordinatorsList, new Predicate() {
			public boolean evaluate(Object obj) {
				InfoCoordinator infoCoordinator = (InfoCoordinator) obj;
				return infoCoordinator.getResponsible().booleanValue();
			}
		});
		
		ListIterator listIterator = responsibleCoordinatorsList.listIterator();
		List responsibleCoordinatorsIdsList = new ArrayList();
		
		while (listIterator.hasNext()) {
			InfoCoordinator infoCoordinator = (InfoCoordinator) listIterator.next();
			
			responsibleCoordinatorsIdsList.add(infoCoordinator.getIdInternal());
		}

		return (Integer[]) responsibleCoordinatorsIdsList.toArray(new Integer[] {});
	}

	public ActionForward prepareInsert(
			ActionMapping mapping,
			ActionForm actionForm,
			HttpServletRequest request,
			HttpServletResponse response)
	throws Exception {
		ActionErrors errors = new ActionErrors();

		Integer executionDegreeId =
		getFromRequest("executionDegreeId", request);
		request.setAttribute("executionDegreeId", executionDegreeId);


		IUserView userView = SessionUtils.getUserView(request);

		Object[] args = { executionDegreeId };
		InfoExecutionDegree infoExecutionDegree = null;
		try {
			infoExecutionDegree =
			(
					InfoExecutionDegree) ServiceManagerServiceFactory
			.executeService(
					userView,
					"ReadExecutionDegree",
					args);
		} catch (FenixServiceException e) {
			e.printStackTrace();
			errors.add(
					"impossibleExecutionDegree",
					new ActionError("error.invalidExecutionDegree"));
		}
		if (infoExecutionDegree == null
				|| infoExecutionDegree.getInfoDegreeCurricularPlan() == null
				|| infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree()
				== null) {
			errors.add(
					"impossibleExecutionDegree",
					new ActionError("error.invalidExecutionDegree"));
		}
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		
		request.setAttribute("infoExecutionDegree", infoExecutionDegree);	
		request.setAttribute(
				"degreeId",
				infoExecutionDegree
				.getInfoDegreeCurricularPlan()
				.getInfoDegree()
				.getIdInternal());
		request.setAttribute(
				"degreeCurricularPlanId",
				infoExecutionDegree.getInfoDegreeCurricularPlan().getIdInternal());

		return mapping.findForward("insertCoordinator");
	}
	
	public ActionForward insert(
		ActionMapping mapping,
		ActionForm actionForm,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		ActionErrors errors = new ActionErrors();
		IUserView userView = SessionUtils.getUserView(request);

		Integer executionDegreeId =
			getFromRequest("executionDegreeId", request);
		request.setAttribute("executionDegreeId", executionDegreeId);

		Integer degreeId = getFromRequest("degreeId", request);
		request.setAttribute("degreeId", degreeId);

		getFromRequest("degreeCurricularPlanId", request);

		request.setAttribute("degreeCurricularPlanId", executionDegreeId);

		DynaActionForm coordinatorForm = (DynaActionForm) actionForm;
		Integer coordinatorNumber =
			new Integer((String) coordinatorForm.get("number"));

		Object[] args = { executionDegreeId, coordinatorNumber };
		try {
			ServiceManagerServiceFactory.executeService(
				userView,
				"AddCoordinatorByManager",
				args);
		} catch (FenixServiceException e) {
			e.printStackTrace();
			errors.add(
				"impossibleInsertCoordinator",
				new ActionError("error.impossibleInsertCoordinator"));
		}
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}

		return mapping.findForward("viewCoordinators");
	}

	public ActionForward edit(
		ActionMapping mapping,
		ActionForm actionForm,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		ActionErrors errors = new ActionErrors();

		IUserView userView = SessionUtils.getUserView(request);

		Integer executionDegreeId =
			getFromRequest("executionDegreeId", request);
		request.setAttribute("executionDegreeId", executionDegreeId);

		Integer degreeId = getFromRequest("degreeId", request);
		request.setAttribute("degreeId", degreeId);

		getFromRequest("degreeCurricularPlanId", request);
		request.setAttribute("degreeCurricularPlanId", executionDegreeId);

		DynaActionForm coordinatorsForm = (DynaActionForm) actionForm;
		Integer[] responsibleCoordinatorsIds =
			(Integer[]) coordinatorsForm.get("responsibleCoordinatorsIds");
		Integer[] deletedCoordinatorsIds =
		(Integer[]) coordinatorsForm.get("deletedCoordinatorsIds");
				
		if (responsibleCoordinatorsIds != null) {
			List responsibleCoordinatorsIdsList =
				Arrays.asList(responsibleCoordinatorsIds);
			Object[] args =
				{ executionDegreeId, responsibleCoordinatorsIdsList };
			try {
				ServiceManagerServiceFactory.executeService(
					userView,
					"ResponsibleCoordinatorsByManager",
					args);
			} catch (FenixServiceException e) {
				e.printStackTrace();
				errors.add(
					"impossibleInsertCoordinator",
					new ActionError("error.impossibleInsertCoordinator"));
			}
			if (!errors.isEmpty()) {
				saveErrors(request, errors);
			}
		}

		if (deletedCoordinatorsIds != null) {
			List deletedCoordinatorsIdsList =
				Arrays.asList(deletedCoordinatorsIds);
			Object[] args = { executionDegreeId, deletedCoordinatorsIdsList };
			try {
				ServiceManagerServiceFactory.executeService(
					userView,
					"RemoveCoordinatorsByManager",
					args);
			} catch (FenixServiceException e) {
				e.printStackTrace();
				errors.add(
					"impossibleInsertCoordinator",
					new ActionError("error.impossibleInsertCoordinator"));
			}
			if (!errors.isEmpty()) {
				saveErrors(request, errors);
			}
		}

		return mapping.findForward("viewCoordinators");
	}

	private Integer getFromRequest(
		String parameter,
		HttpServletRequest request) {
		Integer parameterCode = null;
		String parameterCodeString = request.getParameter(parameter);
		if (parameterCodeString == null) {
			parameterCodeString = (String) request.getAttribute(parameter);
		}
		if (parameterCodeString != null) {
			parameterCode = new Integer(parameterCodeString);
		}
		return parameterCode;
	}
}
