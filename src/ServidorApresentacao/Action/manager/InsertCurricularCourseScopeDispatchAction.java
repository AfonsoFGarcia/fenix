/*
 * Created on 22/Ago/2003
 */
package ServidorApresentacao.Action.manager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.InfoBranch;
import DataBeans.InfoCurricularCourse;
import DataBeans.InfoCurricularCourseScope;
import DataBeans.InfoCurricularSemester;
import DataBeans.InfoExecutionPeriod;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import Util.Data;

/**
 * @author lmac1
 */

public class InsertCurricularCourseScopeDispatchAction extends FenixDispatchAction {

	public ActionForward prepareInsert(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws FenixActionException {

		IUserView userView = SessionUtils.getUserView(request);

		Integer degreeCurricularPlanId = new Integer(request.getParameter("degreeCurricularPlanId"));

		Object[] args = { degreeCurricularPlanId };

		List result = null;
		try {
			result = (List) ServiceUtils.executeService(userView, "ReadBranchesByDegreeCurricularPlan", args);

		} catch (NonExistingServiceException ex) {
			throw new NonExistingActionException("message.nonExistingDegreeCurricularPlan", mapping.findForward("readDegree"));
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		if (result == null)
			throw new NonExistingActionException(
				"message.insert.degreeCurricularCourseScope.error",
				mapping.findForward("readCurricularCourse"));

		//			creation of bean of InfoBranches for use in jsp
		ArrayList branchesList = new ArrayList();

		InfoBranch infoBranch;
		Iterator iter = result.iterator();
		String label, value;
		while (iter.hasNext()) {
			infoBranch = (InfoBranch) iter.next();
			value = infoBranch.getIdInternal().toString();
			label = infoBranch.getCode() + " - " + infoBranch.getName();
			branchesList.add(new LabelValueBean(label, value));
		}

		List infoExecutionPeriods = null;
		try {
			infoExecutionPeriods = (List) ServiceUtils.executeService(userView, "ReadExecutionPeriods", null);

		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		if (infoExecutionPeriods == null)
			throw new NonExistingActionException("message.insert.executionPeriods.error", mapping.findForward("readCurricularCourse"));

		List executionPeriodsLabels = new ArrayList();
		InfoExecutionPeriod infoExecutionPeriod = new InfoExecutionPeriod();
		Iterator iterExecutionPeriods = infoExecutionPeriods.iterator();
		String labelExecutionPeriod, valueExecutionPeriod;
		while (iterExecutionPeriods.hasNext()) {
			infoExecutionPeriod = (InfoExecutionPeriod) iterExecutionPeriods.next();
			valueExecutionPeriod = Data.format2DayMonthYear(infoExecutionPeriod.getBeginDate(), "/");
			labelExecutionPeriod = Data.format2DayMonthYear(infoExecutionPeriod.getBeginDate(), "/");
			executionPeriodsLabels.add(new LabelValueBean(labelExecutionPeriod, valueExecutionPeriod));
		}

		request.setAttribute("executionPeriodsLabels", executionPeriodsLabels);
		request.setAttribute("branchesList", branchesList);

		return mapping.findForward("insertCurricularCourseScope");
	}

	public ActionForward insert(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws FenixActionException {

		IUserView userView = SessionUtils.getUserView(request);

		DynaActionForm dynaForm = (DynaValidatorForm) form;

		InfoCurricularCourseScope infoCurricularCourseScope = new InfoCurricularCourseScope();

		InfoBranch infoBranch = new InfoBranch();
		infoBranch.setIdInternal(new Integer((String) dynaForm.get("branchId")));
		infoCurricularCourseScope.setInfoBranch(infoBranch);

		InfoCurricularCourse infoCurricularCourse = new InfoCurricularCourse();
		infoCurricularCourse.setIdInternal(new Integer(request.getParameter("curricularCourseId")));
		infoCurricularCourseScope.setInfoCurricularCourse(infoCurricularCourse);

		InfoCurricularSemester infoCurricularSemester = new InfoCurricularSemester();
		infoCurricularSemester.setIdInternal(new Integer((String) dynaForm.get("curricularSemesterId")));
		infoCurricularCourseScope.setInfoCurricularSemester(infoCurricularSemester);

		String beginDateString = (String) dynaForm.get("beginDate");
		if (beginDateString.compareTo("") != 0) {
			Calendar beginDateCalendar = Calendar.getInstance();
			beginDateCalendar.setTime(Data.convertStringDate(beginDateString, "/"));
			infoCurricularCourseScope.setBeginDate(beginDateCalendar);
		}

		Object args[] = { infoCurricularCourseScope };

		try {
			ServiceUtils.executeService(userView, "InsertCurricularCourseScopeAtCurricularCourse", args);

		} catch (ExistingServiceException ex) {
			throw new ExistingActionException(ex.getMessage(), ex);
		} catch (NonExistingServiceException exception) {
			throw new NonExistingActionException(exception.getMessage(), mapping.findForward("readDegreeCurricularPlan"));
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		return mapping.findForward("readCurricularCourse");
	}	
}