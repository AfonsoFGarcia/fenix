package ServidorApresentacao.Action.sop;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.ExecutionCourseSiteView;
import DataBeans.ISiteComponent;
import DataBeans.InfoSite;
import DataBeans.InfoSiteCommon;
import DataBeans.InfoSiteCurricularCoursesAndAssociatedShiftsAndClasses;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;

public class SiteViewerDispatchAction extends FenixDispatchAction {

	public ActionForward firstPage(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
        
		HttpSession session = request.getSession(false);
		ISiteComponent firstPageComponent =  new InfoSiteCurricularCoursesAndAssociatedShiftsAndClasses();

		String objectCodeString = request.getParameter("objectCode");
		if (objectCodeString == null) {
			objectCodeString = (String) request.getAttribute("objectCode");
		}
		Integer infoExecutionCourseCode = new Integer(objectCodeString);
		
		if(readSiteView(request,firstPageComponent,infoExecutionCourseCode,null) == true)
				return mapping.findForward("sucess");
	    return mapping.findForward("erro");
	}

			

	private boolean readSiteView(
		HttpServletRequest request,
		ISiteComponent firstPageComponent,
		Integer infoExecutionCourseCode,
		Integer sectionIndex)
		throws FenixActionException {
			
		InfoSite infoSite = null;
		Integer objectCode = null;
		if (infoExecutionCourseCode == null) {
			String objectCodeString = request.getParameter("objectCode");
			if (objectCodeString == null) {
				objectCodeString = (String) request.getAttribute("objectCode");

			}
			objectCode = new Integer(objectCodeString);

		}

		ISiteComponent commonComponent = new InfoSiteCommon();

		Object[] args =
			{
				commonComponent,
				firstPageComponent,
				objectCode,
				infoExecutionCourseCode,
				sectionIndex };
		boolean result = true;
		try {
			ExecutionCourseSiteView siteView  =
				(ExecutionCourseSiteView) ServiceUtils.executeService(
					null,
					"ExecutionCourseSiteComponentService",
					args);
			request.setAttribute("objectCode", objectCode);
			if (siteView == null){
System.out.println(" ----------------------siteView is null" + siteView == null);
				result=false;
				
				ActionErrors actionErrors = new ActionErrors();
				actionErrors.add(
					"StudentNotEnroled",
					new ActionError(
						"error.nonExisting.AssociatedCurricularCourses"));
			    saveErrors(request, actionErrors);
							
			}else
				{			
					request.setAttribute("siteView", siteView);
					request.setAttribute(
						"executionCourseCode",
						((InfoSiteCommon) siteView.getCommonComponent())
							.getExecutionCourse()
							.getIdInternal());
					request.setAttribute(
						"executionPeriodCode",
						((InfoSiteCommon) siteView.getCommonComponent())
							.getExecutionCourse()
							.getInfoExecutionPeriod()
							.getIdInternal());
					
			}
		} catch (NonExistingServiceException e) {
			throw new NonExistingActionException("A disciplina",e);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		return result;
	}
	

}