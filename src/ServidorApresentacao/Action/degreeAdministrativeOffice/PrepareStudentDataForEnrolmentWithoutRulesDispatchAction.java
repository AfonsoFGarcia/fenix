package ServidorApresentacao.Action.degreeAdministrativeOffice;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoStudent;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.TipoCurso;

/**
 * @author David Santos
 */

public class PrepareStudentDataForEnrolmentWithoutRulesDispatchAction extends PrepareStudentDataAction {
	
	private final String[] forwards = { "startCurricularCourseEnrolmentWithoutRules", "error" };

	public ActionForward getStudentAndDegreeTypeForEnrolmentWithoutRules(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		boolean isSuccess = super.getStudentByNumberAndDegreeType(form, request);

		if(isSuccess) {
			DynaActionForm getStudentByNumberAndDegreeTypeForm = (DynaActionForm) form;
			HttpSession session = request.getSession();

			IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

			List infoExecutionDegreesList = null;
			InfoStudent infoStudent = null;

			Integer degreeTypeInt = new Integer((String) getStudentByNumberAndDegreeTypeForm.get("degreeType"));
			Integer studentNumber = new Integer((String) getStudentByNumberAndDegreeTypeForm.get("studentNumber"));

			try {
				Object args[] = { degreeTypeInt, studentNumber };
				infoStudent = (InfoStudent) ServiceUtils.executeService(userView, "GetStudentByNumberAndDegreeType", args);
			} catch (FenixServiceException e) {
				throw new FenixActionException(e);
			}

			try {
				Integer degreeType = new Integer((String) getStudentByNumberAndDegreeTypeForm.get("degreeType"));
				InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) session.getServletContext().getAttribute(SessionConstants.INFO_EXECUTION_PERIOD_KEY);
				TipoCurso realDegreeType = new TipoCurso(degreeType);
				Object args[] = { infoExecutionPeriod.getInfoExecutionYear(), realDegreeType };
				infoExecutionDegreesList = (List) ServiceUtils.executeService(userView, "ReadExecutionDegreesByExecutionYearAndDegreeType", args);
			} catch (FenixServiceException e) {
				throw new FenixActionException(e);
			}

			session.setAttribute(SessionConstants.ENROLMENT_ACTOR_KEY, infoStudent);
			session.setAttribute(SessionConstants.DEGREE_LIST, this.getExecutionDegreesLableValueBeanList(infoExecutionDegreesList));
			session.setAttribute(SessionConstants.DEGREES, infoExecutionDegreesList);

			session.removeAttribute(SessionConstants.DEGREE_TYPE);

			return mapping.findForward(forwards[0]);
		} else {
			return mapping.getInputForward();
		}
	}

	public ActionForward error(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return mapping.findForward(forwards[1]);
	}

	private List getExecutionDegreesLableValueBeanList(List infoExecutionDegreesList) {
		ArrayList result = null;
		if ( (infoExecutionDegreesList != null) && (!infoExecutionDegreesList.isEmpty()) ) {
			result = new ArrayList();
			result.add(new LabelValueBean("Escolha", ""));
			Iterator iterator = infoExecutionDegreesList.iterator();
			while(iterator.hasNext()) {
				InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) iterator.next();
				Integer index = new Integer(infoExecutionDegreesList.indexOf(infoExecutionDegree));
				result.add(new LabelValueBean(infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getNome(), index.toString()));
			}
		}
		return result;	
	}
}