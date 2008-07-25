package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourseSite;
import net.sourceforge.fenixedu.domain.ExportGrouping;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.LessonPlanning;
import net.sourceforge.fenixedu.domain.Mark;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.domain.executionCourse.SummariesSearchBean;
import net.sourceforge.fenixedu.domain.functionalities.AbstractFunctionalityContext;
import net.sourceforge.fenixedu.domain.messaging.Announcement;
import net.sourceforge.fenixedu.presentationTier.Action.manager.SiteVisualizationDA;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors.ExecutionCourseProcessor;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.RequestUtils;

public class ExecutionCourseDA extends SiteVisualizationDA {

    public final static int ANNOUNCEMENTS_TO_SHOW = 5;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	final String executionCourseIDString = request.getParameter("executionCourseID");

	ExecutionCourse executionCourse = null;
	if (executionCourseIDString != null) {
	    executionCourse = rootDomainObject.readExecutionCourseByOID(Integer.valueOf(executionCourseIDString));
	} else {
	    ExecutionCourseSite site = (ExecutionCourseSite) AbstractFunctionalityContext.getCurrentContext(request)
		    .getSelectedContainer();
	    executionCourse = site.getSiteExecutionCourse();
	}

	request.setAttribute("executionCourse", executionCourse);
	request.setAttribute("executionCourseID", executionCourse.getIdInternal());

	return super.execute(mapping, actionForm, request, response);
    }

    @Override
    protected String getDirectLinkContext(HttpServletRequest request) {
	ExecutionCourse executionCourse = getExecutionCourse(request);
	try {
	    String path = executionCourse.getSite().getReversePath(); 

	    if (path == null) {
		return null;
	    }

	    return RequestUtils.absoluteURL(request, path).toString();
	} catch (MalformedURLException e) {
	    return null;
	}
    }

    protected ExecutionCourse getExecutionCourse(final HttpServletRequest request) {
	return (ExecutionCourse) request.getAttribute("executionCourse");
    }

    public ActionForward firstPage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	final ExecutionCourse course = (ExecutionCourse) request.getAttribute("executionCourse");

	final Iterator<Announcement> announcementsIterator = course.getBoard().getActiveAnnouncements().iterator();
	if (announcementsIterator.hasNext()) {
	    request.setAttribute("lastAnnouncement", announcementsIterator.next());
	}

	int i = 0;
	final Collection<Announcement> lastFiveAnnouncements = new ArrayList<Announcement>(ANNOUNCEMENTS_TO_SHOW);
	while (announcementsIterator.hasNext() && i < ANNOUNCEMENTS_TO_SHOW) {
	    lastFiveAnnouncements.add(announcementsIterator.next());
	    i++;
	}
	request.setAttribute("lastFiveAnnouncements", lastFiveAnnouncements);

	return mapping.findForward("execution-course-first-page");
    }

    public ActionForward summaries(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	final ExecutionCourse executionCourse = getExecutionCourse(request);
	final DynaActionForm dynaActionForm = (DynaActionForm) form;
	final SummariesSearchBean summariesSearchBean = executionCourse.getSummariesSearchBean();
	request.setAttribute("summariesSearchBean", summariesSearchBean);
	if (dynaActionForm != null) {
	    final String shiftType = (String) dynaActionForm.get("shiftType");
	    if (shiftType != null && shiftType.length() > 0) {
		summariesSearchBean.setShiftType(ShiftType.valueOf(shiftType));
	    }
	    final String shiftID = (String) dynaActionForm.get("shiftID");
	    if (shiftID != null && shiftID.length() > 0) {
		summariesSearchBean.setShift(rootDomainObject.readShiftByOID(Integer.valueOf(shiftID)));
	    }
	    final String professorshipID = (String) dynaActionForm.get("professorshipID");
	    if (professorshipID != null && professorshipID.equals("-1")) {
		summariesSearchBean.setShowOtherProfessors(Boolean.TRUE);
	    } else if (professorshipID != null && !professorshipID.equals("0")) {
		summariesSearchBean.setProfessorship(rootDomainObject.readProfessorshipByOID(Integer.valueOf(professorshipID)));
	    }
	}
	return mapping.findForward("execution-course-summaries");
    }

    public ActionForward objectives(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return mapping.findForward("execution-course-objectives");
    }

    public ActionForward program(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	return mapping.findForward("execution-course-program");
    }

    public ActionForward evaluationMethod(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return mapping.findForward("execution-course-evaluation-method");
    }

    public ActionForward bibliographicReference(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return mapping.findForward("execution-course-bibliographic-reference");
    }

    public ActionForward lessonPlannings(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	final ExecutionCourse executionCourse = getExecutionCourse(request);
	Map<ShiftType, List<LessonPlanning>> lessonPlanningsMap = new TreeMap<ShiftType, List<LessonPlanning>>();
	if (executionCourse.getSite().getLessonPlanningAvailable()) {
	    for (ShiftType shiftType : executionCourse.getShiftTypes()) {
		List<LessonPlanning> lessonPlanningsOrderedByOrder = executionCourse.getLessonPlanningsOrderedByOrder(shiftType);
		if (!lessonPlanningsOrderedByOrder.isEmpty()) {
		    lessonPlanningsMap.put(shiftType, lessonPlanningsOrderedByOrder);
		}
	    }
	}
	request.setAttribute("lessonPlanningsMap", lessonPlanningsMap);
	return mapping.findForward("execution-course-lesson-plannings");
    }

    public ActionForward schedule(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	final ExecutionCourse executionCourse = getExecutionCourse(request);
	final List<InfoLesson> infoLessons = new ArrayList<InfoLesson>();
	for (final Lesson lesson : executionCourse.getLessons()) {
	    infoLessons.add(InfoLesson.newInfoFromDomain(lesson));
	}
	request.setAttribute("infoLessons", infoLessons);
	return mapping.findForward("execution-course-schedule");
    }

    public ActionForward shifts(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	return mapping.findForward("execution-course-shifts");
    }

    public ActionForward evaluations(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return mapping.findForward("execution-course-evaluations");
    }

    public ActionForward marks(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	final ExecutionCourse executionCourse = getExecutionCourse(request);
	final Map<Attends, Map<Evaluation, Mark>> attendsMap = new TreeMap<Attends, Map<Evaluation, Mark>>(
		Attends.COMPARATOR_BY_STUDENT_NUMBER);
	for (final Attends attends : executionCourse.getAttendsSet()) {
	    final Map<Evaluation, Mark> evaluationsMap = new TreeMap<Evaluation, Mark>(ExecutionCourse.EVALUATION_COMPARATOR);
	    attendsMap.put(attends, evaluationsMap);
	    for (final Evaluation evaluation : executionCourse.getAssociatedEvaluationsSet()) {
		if (evaluation.getPublishmentMessage() != null) {
		    evaluationsMap.put(evaluation, null);
		}
	    }
	    for (final Mark mark : attends.getAssociatedMarksSet()) {
		if (mark.getEvaluation().getPublishmentMessage() != null) {
		    evaluationsMap.put(mark.getEvaluation(), mark);
		}
	    }
	}
	request.setAttribute("attendsMap", attendsMap);
	request.setAttribute("dont-cache-pages-in-search-engines", Boolean.TRUE);
	return mapping.findForward("execution-course-marks");
    }

    public ActionForward groupings(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return mapping.findForward("execution-course-groupings");
    }

    public ActionForward grouping(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	final Grouping grouping = getGrouping(request);
	request.setAttribute("grouping", grouping);
	return mapping.findForward("execution-course-grouping");
    }

    public ActionForward studentGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	final StudentGroup studentGroup = getStudentGroup(request);
	request.setAttribute("studentGroup", studentGroup);
	return mapping.findForward("execution-course-student-group");
    }

    public ActionForward studentGroupsByShift(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	final Grouping grouping = getGrouping(request);
	request.setAttribute("grouping", grouping);
	final Shift shift = getShift(request);
	if (shift != null) {
	    request.setAttribute("shift", shift);
	}
	final List<StudentGroup> studentGroups = shift == null ? grouping.getStudentGroupsWithoutShift() : grouping
		.readAllStudentGroupsBy(shift);
	Collections.sort(studentGroups, StudentGroup.COMPARATOR_BY_GROUP_NUMBER);
	request.setAttribute("studentGroups", studentGroups);
	return mapping.findForward("execution-course-student-groups-by-shift");
    }

    public ActionForward rss(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	return mapping.findForward("execution-course-rss");
    }

    protected StudentGroup getStudentGroup(final HttpServletRequest request) {
	final Integer studentGroupID = Integer.valueOf(request.getParameter("studentGroupID"));
	return rootDomainObject.readStudentGroupByOID(studentGroupID);
    }

    protected Shift getShift(final HttpServletRequest request) {
	if (request.getParameter("shiftID") != null) {
	    final Integer shiftID = Integer.valueOf(request.getParameter("shiftID"));
	    return rootDomainObject.readShiftByOID(shiftID);
	} else {
	    return null;
	}
    }

    protected Grouping getGrouping(final HttpServletRequest request) {
	final Integer groupingID = Integer.valueOf(request.getParameter("groupingID"));
	final ExecutionCourse executionCourse = getExecutionCourse(request);
	for (final ExportGrouping exportGrouping : executionCourse.getExportGroupingsSet()) {
	    final Grouping grouping = exportGrouping.getGrouping();
	    if (grouping.getIdInternal().equals(groupingID)) {
		return grouping;
	    }
	}
	return null;
    }

    public ActionForward notFound(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	return mapping.findForward("execution-course-not-found");
    }

    protected ActionForward getSiteDefaultView(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return mapping.findForward("execution-course-first-page");
    }

}