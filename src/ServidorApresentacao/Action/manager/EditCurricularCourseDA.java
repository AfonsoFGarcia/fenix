/*
 * Created on 18/Ago/2003
 */
package ServidorApresentacao.Action.manager;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.InfoCurricularCourse;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.CurricularCourseType;

/**
 * @author lmac1
 */
public class EditCurricularCourseDA extends FenixDispatchAction {


	public ActionForward prepareEdit(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws FenixActionException {
				
			HttpSession session = request.getSession(false);
			DynaActionForm dynaForm = (DynaActionForm) form;
			
			UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
			Integer degreeCurricularPlanId = new Integer(request.getParameter("degreeCurricularPlanId"));
			Integer curricularCourseId = new Integer(request.getParameter("curricularCourseId"));
			//pois o degreeId tava a vir a null/tb nao sei s preciso dele
//			Integer degreeId = new Integer(request.getParameter("degreeId"));

			InfoCurricularCourse oldInfoCurricularCourse = null;

			Object args[] = { degreeCurricularPlanId };
			GestorServicos manager = GestorServicos.manager();
			
			try {
				oldInfoCurricularCourse = (InfoCurricularCourse) manager.executar(userView, "ReadCurricularCourse", args);
			} catch (FenixServiceException fenixServiceException) {
				throw new FenixActionException(fenixServiceException.getMessage());
			}

			dynaForm.set("name", oldInfoCurricularCourse.getName());
			dynaForm.set("code", oldInfoCurricularCourse.getCode());
			
			if(oldInfoCurricularCourse.getCredits() != null)
				dynaForm.set("credits", oldInfoCurricularCourse.getCredits().toString());
			
			if(oldInfoCurricularCourse.getTheoreticalHours() != null)
				dynaForm.set("theoreticalHours", oldInfoCurricularCourse.getTheoreticalHours().toString());
			
			if(oldInfoCurricularCourse.getPraticalHours() != null)
				dynaForm.set("praticalHours", oldInfoCurricularCourse.getPraticalHours().toString());
			
			if(oldInfoCurricularCourse.getTheoPratHours() != null)
				dynaForm.set("theoPratHours", oldInfoCurricularCourse.getTheoPratHours().toString());
			
			if(oldInfoCurricularCourse.getLabHours() != null)
				dynaForm.set("labHours", oldInfoCurricularCourse.getLabHours().toString());
			
			if(oldInfoCurricularCourse.getType() != null)
				dynaForm.set("type", oldInfoCurricularCourse.getType().toString());
			
			if(oldInfoCurricularCourse.getMandatory() != null)
				dynaForm.set("mandatory", oldInfoCurricularCourse.getMandatory().toString());
			
//			if(oldInfoCurricularCourse.getUniversity() != null)
//				dynaForm.set("university", oldInfoCurricularCourse.getUniversity().toString());
			
			if(oldInfoCurricularCourse.getBasic() != null)
				dynaForm.set("basic", oldInfoCurricularCourse.getBasic().toString());
			
			
			request.setAttribute("degreeCurricularPlanId", degreeCurricularPlanId);
//			request.setAttribute("degreeId", degreeId);
			request.setAttribute("curricularCourseId", curricularCourseId);
			return mapping.findForward("editCurricularCourse");
		}
				

	public ActionForward edit(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		HttpSession session = request.getSession(false);
		UserView userView =	(UserView) session.getAttribute(SessionConstants.U_VIEW);
    	
		DynaActionForm dynaForm = (DynaValidatorForm) form;
		
		Integer degreeCPId = (Integer) dynaForm.get("degreeCurricularPlanId");
		Integer oldCurricularCourseId = new Integer(request.getParameter("curricularCourseId"));
//		Integer degreeId = (Integer) dynaForm.get("degreeId");
		
		InfoCurricularCourse newInfoCurricularCourse = new InfoCurricularCourse();	
		

		String name = (String) dynaForm.get("name");
		String code = (String) dynaForm.get("code");
				
		String creditsString = (String) dynaForm.get("credits");
		String theoreticalHoursString = (String) dynaForm.get("theoreticalHours");
		String praticalHoursString = (String) dynaForm.get("praticalHours");
		String theoPratHoursString = (String) dynaForm.get("theoPratHours");
		String labHoursString = (String) dynaForm.get("labHours");
		String typeString = (String) dynaForm.get("type");
		String mandatoryString = (String) dynaForm.get("mandatory");
//		String universityString = (String) dynaForm.get("university");
		String basicString = (String) dynaForm.get("basic");
		
		newInfoCurricularCourse.setName(name);
		newInfoCurricularCourse.setCode(code);
					
		if(creditsString.compareTo("") != 0) {
			Double credits = new Double(creditsString); 
			newInfoCurricularCourse.setCredits(credits);
		}
		
		if(theoreticalHoursString.compareTo("") != 0) {
			Double theoreticalHours = new Double(theoreticalHoursString); 
			newInfoCurricularCourse.setTheoreticalHours(theoreticalHours);
		}
				
		if(praticalHoursString.compareTo("") != 0) {
			Double praticalHours = new Double(praticalHoursString); 
			newInfoCurricularCourse.setPraticalHours(praticalHours);
		}
		
		if(theoPratHoursString.compareTo("") != 0) {
			Double theoPratHours = new Double(theoPratHoursString); 
			newInfoCurricularCourse.setTheoPratHours(theoPratHours);
		}
		
		if(labHoursString.compareTo("") != 0) {
			Double labHours = new Double(labHoursString); 
			newInfoCurricularCourse.setLabHours(labHours);
		}
		
		if(typeString.compareTo("") != 0) {
			CurricularCourseType type = new CurricularCourseType(new Integer(typeString)); 
			newInfoCurricularCourse.setType(type);
		}
		
		if(mandatoryString.compareTo("") != 0) {
			Boolean mandatory = new Boolean(mandatoryString); 
			newInfoCurricularCourse.setMandatory(mandatory);
		}
		
//		if(universityString.compareTo("") != 0) {
//			university
//			
//			University university = new University(); 
//			
//			newInfoCurricularCourse.setUniversity(university);
//		}
//		
		if(basicString.compareTo("") != 0) {
			Boolean basic = new Boolean(basicString); 
			newInfoCurricularCourse.setBasic(basic);
		}
		
		
			
		Object args[] = { oldCurricularCourseId,  newInfoCurricularCourse, degreeCPId };
		GestorServicos manager = GestorServicos.manager();
		List serviceResult = null;
		try {
				serviceResult = (List) manager.executar(userView, "EditCurricularCourse", args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		
		if(serviceResult != null) {
			ActionErrors actionErrors = new ActionErrors();
			ActionError error = null;
			if(serviceResult.get(0) != null) {
				error = new ActionError("message.existingCurricularCourse", serviceResult.get(0),  serviceResult.get(1));
				actionErrors.add("message.existingCurricularCourse", error);
			}			
			saveErrors(request, actionErrors);
		}
		
		return mapping.findForward("readDegreeCP");
	}			
}