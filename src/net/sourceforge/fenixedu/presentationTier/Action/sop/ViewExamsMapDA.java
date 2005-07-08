/**
 * Project sop 
 * 
 * Package ServidorApresentacao.Action.sop
 * 
 * Created on 2/Apr/2003
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.sop;

import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExamsMap;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoViewExamByDayAndShift;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.base.FenixExecutionDegreeAndCurricularYearsContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.Util;
import net.sourceforge.fenixedu.presentationTier.Action.utils.ContextUtils;
import net.sourceforge.fenixedu.util.Season;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class ViewExamsMapDA extends FenixExecutionDegreeAndCurricularYearsContextDispatchAction {

    public ActionForward view(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {
        HttpSession session = request.getSession(false);

        if (session != null) {
            InfoExamsMap infoExamsMap = getExamsMap(request);
            request.setAttribute(SessionConstants.INFO_EXAMS_MAP, infoExamsMap);
        } else {
            throw new FenixActionException();
        }

        return mapping.findForward("viewExamsMap");
    }

    private InfoExamsMap getExamsMap(HttpServletRequest request) throws FenixActionException, FenixFilterException {
        IUserView userView = (IUserView) request.getSession().getAttribute(SessionConstants.U_VIEW);

        InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) request
                .getAttribute(SessionConstants.EXECUTION_DEGREE);

        List curricularYears = (List) request.getAttribute(SessionConstants.CURRICULAR_YEARS_LIST);

        InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request
                .getAttribute(SessionConstants.EXECUTION_PERIOD);

        Object[] args = { infoExecutionDegree, curricularYears, infoExecutionPeriod };
        InfoExamsMap infoExamsMap;
        try {
            infoExamsMap = (InfoExamsMap) ServiceManagerServiceFactory.executeService(userView,
                    "ReadExamsMap", args);
        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException(e);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        return infoExamsMap;
    }

    public ActionForward create(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        InfoExamsMap infoExamsMap = getExamsMap(request);

        Integer indexExecutionCourse = new Integer(request.getParameter("indexExecutionCourse"));

        InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) infoExamsMap
                .getExecutionCourses().get(indexExecutionCourse.intValue());

        Integer curricularYear = infoExecutionCourse.getCurricularYear();

        request.setAttribute(SessionConstants.CURRICULAR_YEAR_OID, curricularYear.toString());
        ContextUtils.setCurricularYearContext(request);

        request.setAttribute(SessionConstants.EXECUTION_COURSE_KEY, infoExecutionCourse);
        request.setAttribute(SessionConstants.EXECUTION_COURSE_OID, infoExecutionCourse.getIdInternal()
                .toString());

        InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) request
                .getAttribute(SessionConstants.EXECUTION_DEGREE);
        request.setAttribute(SessionConstants.EXECUTION_DEGREE_OID, infoExecutionDegree.getIdInternal()
                .toString());

        InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request
                .getAttribute(SessionConstants.EXECUTION_PERIOD);
        request.setAttribute(SessionConstants.EXECUTION_PERIOD_OID, infoExecutionPeriod.getIdInternal()
                .toString());

        return mapping.findForward("createExam");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);
        DynaValidatorForm editExamForm = (DynaValidatorForm) form;

        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request
                .getAttribute(SessionConstants.EXECUTION_PERIOD);

        String executionCourseInitials = request.getParameter("executionCourseInitials");
        Season season = new Season(new Integer(request.getParameter("season")));

        Object args[] = { executionCourseInitials, season, infoExecutionPeriod };
        InfoViewExamByDayAndShift infoViewExamByDayAndShift = (InfoViewExamByDayAndShift) ServiceUtils
                .executeService(userView,
                        "ReadExamsByExecutionCourseInitialsAndSeasonAndExecutionPeriod", args);

        List horas = Util.getExamShifts();
        request.setAttribute(SessionConstants.LABLELIST_HOURS, horas);

        List daysOfMonth = Util.getDaysOfMonth();
        request.setAttribute(SessionConstants.LABLELIST_DAYSOFMONTH, daysOfMonth);

        List monthsOfYear = Util.getMonthsOfYear();
        request.setAttribute(SessionConstants.LABLELIST_MONTHSOFYEAR, monthsOfYear);

        List examSeasons = Util.getExamSeasons();
        request.setAttribute(SessionConstants.LABLELIST_SEASONS, examSeasons);

        Calendar date = Calendar.getInstance();
        date = infoViewExamByDayAndShift.getInfoExam().getDay();

        editExamForm.set("day", new Integer(date.get(Calendar.DAY_OF_MONTH)).toString());
        editExamForm.set("month", new Integer(date.get(Calendar.MONTH)).toString());
        editExamForm.set("year", new Integer(date.get(Calendar.YEAR)).toString());
        if (infoViewExamByDayAndShift.getInfoExam().getBeginning() != null) {
            editExamForm.set("beginning", new Integer(infoViewExamByDayAndShift.getInfoExam()
                    .getBeginning().get(Calendar.HOUR_OF_DAY)).toString());
        }
        editExamForm.set("season", infoViewExamByDayAndShift.getInfoExam().getSeason().getseason()
                .toString());

        request.setAttribute(SessionConstants.INFO_EXAMS_KEY, infoViewExamByDayAndShift);

        InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) request
                .getAttribute(SessionConstants.EXECUTION_DEGREE);
        request.setAttribute(SessionConstants.EXECUTION_DEGREE_OID, infoExecutionDegree.getIdInternal()
                .toString());

        request.setAttribute(SessionConstants.EXECUTION_PERIOD_OID, infoExecutionPeriod.getIdInternal()
                .toString());

        request.setAttribute(SessionConstants.EXECUTION_COURSE, infoViewExamByDayAndShift
                .getInfoExecutionCourses().get(0));
        request.setAttribute(SessionConstants.EXECUTION_COURSE_OID,
                ((InfoExecutionCourse) infoViewExamByDayAndShift.getInfoExecutionCourses().get(0))
                        .getIdInternal().toString());

        request.setAttribute(SessionConstants.NEXT_PAGE, "viewExamsMap");

        request.setAttribute("input", "viewExamsMap");

        return mapping.findForward("editExam");
    }

    public ActionForward comment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        InfoExamsMap infoExamsMap = (InfoExamsMap) request.getAttribute(SessionConstants.INFO_EXAMS_MAP);

        Integer indexExecutionCourse = new Integer(request.getParameter("indexExecutionCourse"));

        InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) infoExamsMap
                .getExecutionCourses().get(indexExecutionCourse.intValue());

        Integer curricularYear = infoExecutionCourse.getCurricularYear();

        request.setAttribute(SessionConstants.CURRICULAR_YEAR_KEY, curricularYear);

        request.setAttribute(SessionConstants.EXECUTION_COURSE_KEY, infoExecutionCourse);

        return mapping.findForward("comment");
    }

}