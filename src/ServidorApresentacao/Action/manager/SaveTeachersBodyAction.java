/*
 * Created on 23/Set/2003
 */
package ServidorApresentacao.Action.manager;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.InvalidArgumentsActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author lmac1
 */

public class SaveTeachersBodyAction extends FenixAction {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws FenixActionException {

		IUserView userView = SessionUtils.getUserView(request);
		Integer executionCourseId = new Integer(request.getParameter("executionCourseId"));
		DynaActionForm actionForm = (DynaActionForm) form;
		
		Integer[] responsibleTeachersIds = (Integer[]) actionForm.get("responsibleTeachersIds");
		Integer[] professorShipTeachersIds = (Integer[]) actionForm.get("professorShipTeachersIds");
		List respTeachersIds = Arrays.asList(responsibleTeachersIds);
		List profTeachersIds = Arrays.asList(professorShipTeachersIds);
		Collections.sort(profTeachersIds, new BeanComparator("name"));
		Object args[] = { respTeachersIds, profTeachersIds, executionCourseId };
		Boolean result;
		
		try {
				result = (Boolean) ServiceUtils.executeService(userView, "SaveTeachersBody", args);
				
		} catch (FenixServiceException fenixServiceException) {
			throw new FenixActionException(fenixServiceException.getMessage());
		}
		
		if(!result.booleanValue())
			throw new InvalidArgumentsActionException("message.non.existing.teachers");
		
		return mapping.findForward("readCurricularCourse");
	}
}