package ServidorApresentacao.Action.degreeAdministrativeOffice;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.TipoCurso;

/**
 * @author David Santos
 */

public class PrepareEnrolmentContextDispatchAction extends DispatchAction {
	
	private static final int MAX_CURRICULAR_YEARS = 5;
	private static final int MAX_CURRICULAR_SEMESTERS = 2;

	private final String[] forwards = { "showAvailableCurricularCourses", "home", "error" };

	public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		List infoExecutionDegreesList = this.getInfoExecutionDegreesList(request);

		Integer infoExecutionDegreeIndex = this.getChosenInfoExecutionDegreeIndex(form);
		InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) infoExecutionDegreesList.get(infoExecutionDegreeIndex.intValue());

		List listOfChosenCurricularYears = this.getListOfChosenCurricularYears(form);
		List listOfChosenCurricularSemesters = this.getListOfChosenCurricularSemesters(form);

		request.setAttribute(SessionConstants.ENROLMENT_YEAR_LIST_KEY, listOfChosenCurricularYears);
		request.setAttribute(SessionConstants.ENROLMENT_SEMESTER_LIST_KEY, listOfChosenCurricularSemesters);
		request.setAttribute(SessionConstants.DEGREE, infoExecutionDegree);
		
		request.setAttribute("studentOID", this.getStudentOID(form));

		return mapping.findForward(forwards[0]);
	}

	public ActionForward error(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return mapping.findForward(forwards[2]);
	}

	private List getInfoExecutionDegreesList(HttpServletRequest request) throws FenixActionException {
		HttpSession session = request.getSession();
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
		List infoExecutionDegreesList = null;
		try {
			Integer degreeType = new Integer((String) request.getParameter("degreeType"));
			InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) session.getServletContext().getAttribute(SessionConstants.INFO_EXECUTION_PERIOD_KEY);
			TipoCurso realDegreeType = new TipoCurso(degreeType);
			Object args[] = { infoExecutionPeriod.getInfoExecutionYear(), realDegreeType };
			infoExecutionDegreesList = (List) ServiceUtils.executeService(userView, "ReadExecutionDegreesByExecutionYearAndDegreeType", args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		return infoExecutionDegreesList;
	}

	private Integer getChosenInfoExecutionDegreeIndex(ActionForm form) throws FenixActionException {
		DynaActionForm getDegreeAndCurricularSemesterAndCurricularYearForm = (DynaActionForm) form;
		Integer infoChosenExecutionDegreeIndex = new Integer((String) getDegreeAndCurricularSemesterAndCurricularYearForm.get("infoExecutionDegree"));
		return infoChosenExecutionDegreeIndex;
	}

	private List getListOfChosenCurricularYears(ActionForm form) {

		DynaActionForm getDegreeAndCurricularSemesterAndCurricularYearForm = (DynaActionForm) form;
		List result = new ArrayList();

		if(getDegreeAndCurricularSemesterAndCurricularYearForm.get("curricularYears") == null) {
			getDegreeAndCurricularSemesterAndCurricularYearForm.set("curricularYears", new Integer[PrepareEnrolmentContextDispatchAction.MAX_CURRICULAR_YEARS]);
		}

		Integer[] curricularYears = (Integer[]) getDegreeAndCurricularSemesterAndCurricularYearForm.get("curricularYears");

		if(curricularYears != null) {
			for(int i = 0; i < curricularYears.length; i++) {
				Integer curricularYear = curricularYears[i];
				if(curricularYear != null) {
					Integer realYear = new Integer(curricularYear.intValue() + 1);
					result.add(realYear);
				}
			}
		}
		return result;
	}

	private List getListOfChosenCurricularSemesters(ActionForm form) {

		DynaActionForm getDegreeAndCurricularSemesterAndCurricularYearForm = (DynaActionForm) form;
		List result = new ArrayList();

		if(getDegreeAndCurricularSemesterAndCurricularYearForm.get("curricularSemesters") == null) {
			getDegreeAndCurricularSemesterAndCurricularYearForm.set("curricularSemesters", new Integer[PrepareEnrolmentContextDispatchAction.MAX_CURRICULAR_SEMESTERS]);
		}

		Integer[] curricularSemesters = (Integer[]) getDegreeAndCurricularSemesterAndCurricularYearForm.get("curricularSemesters");

		if(curricularSemesters != null) {
			for(int i = 0; i < curricularSemesters.length; i++) {
				Integer curricularSemester = curricularSemesters[i];
				if(curricularSemester != null) {
					Integer realSemester = new Integer(curricularSemester.intValue() + 1);
					result.add(realSemester);
				}
			}
		}
		return result;
	}

	private Integer getStudentOID(ActionForm form) throws FenixActionException {
		DynaActionForm getDegreeAndCurricularSemesterAndCurricularYearForm = (DynaActionForm) form;
		Integer studentOID = (Integer) getDegreeAndCurricularSemesterAndCurricularYearForm.get("studentOID");
		return studentOID;
	}
}