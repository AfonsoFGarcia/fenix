/*
 * Created on 25/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorApresentacao.Action.teacher;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.gesdis.InfoSite;
import DataBeans.gesdis.InfoTeacher;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorAplicacao.Servico.exceptions.notAuthorizedServiceDeleteException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.InvalidArgumentsActionException;
import ServidorApresentacao.Action.exceptions.notAuthorizedActionDeleteException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author jmota
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class TeacherManagerDispatchAction extends FenixDispatchAction {

	public ActionForward viewTeachersByProfessorship(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		try {
			
			HttpSession session = getSession(request);
			UserView userView =
				(UserView) session.getAttribute(SessionConstants.U_VIEW);
			InfoSite infoSite =
				(InfoSite) session.getAttribute(SessionConstants.INFO_SITE);
			Object args[] = { infoSite.getInfoExecutionCourse()};
			GestorServicos serviceManager = GestorServicos.manager();
			boolean result = false;
			List teachers =
				(List) serviceManager.executar(
					userView,
					"ReadTeachersByExecutionCourseProfessorship",
					args);
			if (teachers != null && !teachers.isEmpty()) {
				session.setAttribute(SessionConstants.TEACHERS_LIST, teachers);
			}

			List responsibleTeachers =
				(List) serviceManager.executar(
					userView,
					"ReadTeachersByExecutionCourseResponsibility",
					args);
					
			Object[] args1={userView.getUtilizador()};
			InfoTeacher teacher = (InfoTeacher) serviceManager.executar(userView,"ReadTeacherByUsername",args1);
			if (responsibleTeachers != null
				&& !responsibleTeachers.isEmpty()
				&& responsibleTeachers.contains(teacher)) {
				result = true;
				}	
				session.setAttribute(
					SessionConstants.IS_RESPONSIBLE,
					new Boolean(result));
			
			return mapping.findForward("viewTeachers");
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

	}

	public ActionForward removeTeacher(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		
		HttpSession session = getSession(request);
		UserView userView =
			(UserView) session.getAttribute(SessionConstants.U_VIEW);
		GestorServicos serviceManager = GestorServicos.manager();
		InfoSite infoSite =
			(InfoSite) session.getAttribute(SessionConstants.INFO_SITE);
		String teacherNumberString =
			(String) request.getParameter("teacherNumber");
		
		Integer teacherNumber= new Integer(teacherNumberString);	
		Object args[] = { infoSite.getInfoExecutionCourse(), teacherNumber };
		try {
			Boolean result =
				(Boolean) serviceManager.executar(
					userView,
					"RemoveTeacher",
					args);
            
		} 
		catch (notAuthorizedServiceDeleteException e) {
			throw new notAuthorizedActionDeleteException("error.invalidTeacherRemoval");
		}
		catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		return viewTeachersByProfessorship(mapping, form, request, response);
	}

	public ActionForward associateTeacher(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws FenixActionException {
		
			HttpSession session = getSession(request);
			UserView userView =
				(UserView) session.getAttribute(SessionConstants.U_VIEW);
			GestorServicos serviceManager = GestorServicos.manager();
			InfoSite infoSite =
				(InfoSite) session.getAttribute(SessionConstants.INFO_SITE);
			DynaActionForm teacherForm = (DynaActionForm) form;	
						
			Integer teacherNumber= (Integer) teacherForm.get("teacherNumber");	
			Object args[] = { infoSite.getInfoExecutionCourse(), teacherNumber };
			try {
				Boolean result =
					(Boolean) serviceManager.executar(
						userView,
						"AssociateTeacher",
						args);
				
			} 	catch (ExistingServiceException e) {
				throw new ExistingActionException("A associa��o do professor n�mero "+teacherNumber ,e);
			}	catch (InvalidArgumentsServiceException e) {
				throw new InvalidArgumentsActionException("Professor n�mero "+teacherNumber,e);
			}	catch (FenixServiceException e) {
				throw new FenixActionException(e);
			}
			return viewTeachersByProfessorship(mapping, form, request, response);
		}

	public ActionForward prepareAssociateTeacher(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws FenixActionException {
		    
			return mapping.findForward("associateTeacher");
		}
}
