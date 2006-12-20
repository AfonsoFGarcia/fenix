package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExportGrouping;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.LessonPlanning;
import net.sourceforge.fenixedu.domain.Mark;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.domain.executionCourse.SummariesSearchBean;
import net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext;
import net.sourceforge.fenixedu.domain.messaging.Announcement;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.utils.RequestUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

public class ExecutionCourseDA extends FenixDispatchAction {

    public final static int ANNOUNCEMENTS_TO_SHOW = 5;
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        final String executionCourseIDString = request.getParameter("executionCourseID");
        final Integer executionCourseID = Integer.valueOf(executionCourseIDString);
        final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseID);
        request.setAttribute("executionCourse", executionCourse);
        return super.execute(mapping, actionForm, request, response);
    }

    protected ExecutionCourse getExecutionCourse(final HttpServletRequest request) {
    	return (ExecutionCourse) request.getAttribute("executionCourse");
    }

    public ActionForward firstPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	final ExecutionCourse course = (ExecutionCourse)request.getAttribute("executionCourse");
	
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
	request.setAttribute("lastFiveAnnouncements",lastFiveAnnouncements);
	
        return mapping.findForward("execution-course-first-page");
    }

    public ActionForward summaries(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
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

    public ActionForward objectives(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("execution-course-objectives");
    }

    public ActionForward program(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("execution-course-program");
    }

    public ActionForward evaluationMethod(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("execution-course-evaluation-method");
    }

    public ActionForward bibliographicReference(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("execution-course-bibliographic-reference");
    }
    
    public ActionForward lessonPlannings(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {        
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

    public ActionForward evaluations(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("execution-course-evaluations");
    }

    public ActionForward marks(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        final ExecutionCourse executionCourse = getExecutionCourse(request);
        final Map<Attends, Map<Evaluation, Mark>> attendsMap = new TreeMap<Attends, Map<Evaluation, Mark>>(Attends.COMPARATOR_BY_STUDENT_NUMBER);
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
        return mapping.findForward("execution-course-marks");
    }

    public ActionForward groupings(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("execution-course-groupings");
    }

    public ActionForward grouping(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        final Grouping grouping = getGrouping(request);
        request.setAttribute("grouping", grouping);
        return mapping.findForward("execution-course-grouping");
    }

    public ActionForward studentGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        final StudentGroup studentGroup = getStudentGroup(request);
        request.setAttribute("studentGroup", studentGroup);
        return mapping.findForward("execution-course-student-group");
    }

    public ActionForward studentGroupsByShift(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        final Grouping grouping = getGrouping(request);
        request.setAttribute("grouping", grouping);
        final Shift shift = getShift(request);
        if (shift != null) {
            request.setAttribute("shift", shift);
        }
        final List<StudentGroup> studentGroups = shift == null ?
                grouping.getStudentGroupsWithoutShift() : grouping.readAllStudentGroupsBy(shift);
        Collections.sort(studentGroups, StudentGroup.COMPARATOR_BY_GROUP_NUMBER);
        request.setAttribute("studentGroups", studentGroups);
        return mapping.findForward("execution-course-student-groups-by-shift");
    }

    public ActionForward item(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Item item = selectItem(request);
        
        if (item == null) {
            return mapping.findForward("execution-course-first-page");
        }
        
        IUserView userView = prepareUserView(request);
        FunctionalityContext context = prepareSectionContext(request);
        
        if (item.isAvailable(context)) {
            return mapping.findForward("execution-course-item");
        }
        else {
            if (isAuthenticated(userView)) {
                return mapping.findForward("execution-course-item-deny");
            }
            else {
                return mapping.findForward("execution-course-item-adviseLogin");
            }
        }
    }
    
    public ActionForward section(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Section section = selectSection(request);

        if (section == null) {
            return mapping.findForward("execution-course-first-page");
        }
        
        IUserView userView = prepareUserView(request);
        FunctionalityContext context = prepareSectionContext(request);

        if (section.isAvailable(context)) {
            prepareProtectedItems(request, userView, section.getOrderedItems(), context);
            return mapping.findForward("execution-course-section");
        }
        else {
            if (isAuthenticated(userView)) {
                return mapping.findForward("execution-course-section-deny");
            }
            else {
                return mapping.findForward("execution-course-section-adviseLogin");
            }
        }
    }

    public ActionForward itemWithLogin(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        IUserView userView = getUserView(request);
        
        if (! isAuthenticated(userView)) {
            RequestUtils.sendLoginRedirect(request, response);
            return null;
        }
        else {
            return item(mapping, form, request, response);
        }
    }
    
    public ActionForward sectionWithLogin(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        IUserView userView = getUserView(request);
        
        if (! isAuthenticated(userView)) {
            RequestUtils.sendLoginRedirect(request, response);
            return null;
        }
        else {
            return section(mapping, form, request, response);
        }
    }
    
    private void prepareProtectedItems(HttpServletRequest request, IUserView userView, Collection<Item> items, FunctionalityContext context) {
        List<ProtectedItem> protectedItems = setupItems(request, context, items);
        
        if (!isAuthenticated(userView) && hasRestrictedItems(protectedItems)) {
            request.setAttribute("hasRestrictedItems", true);
        }
    }

    private boolean hasRestrictedItems(List<ProtectedItem> protectedItems) {
        for (ProtectedItem item : protectedItems) {
            if (! item.isAvailable()) {
                return true;
            }
        }
        
        return false;
    }

    private List<ProtectedItem> setupItems(HttpServletRequest request, FunctionalityContext context, Collection<Item> items) {
        List<ProtectedItem> protectedItems = new ArrayList<ProtectedItem>();
        for (Item item : items) {
            if (item.isVisible()) {
                protectedItems.add(new ProtectedItem(context, item));
            }
        }
        
        request.setAttribute("protectedItems", protectedItems);
        return protectedItems;
    }

    private IUserView prepareUserView(HttpServletRequest request) {
        IUserView userView = getUserView(request);
        request.setAttribute("logged", isAuthenticated(userView));
        
        return userView;
    }

    private FunctionalityContext prepareSectionContext(HttpServletRequest request) {
        FunctionalityContext context = new SiteSectionContext(request);
        request.setAttribute("context", context);
        return context;
    }

    private boolean isAuthenticated(IUserView userView) {
        return userView != null && !userView.isPublicRequester();
    }

    private Section selectSection(HttpServletRequest request) {
        return selectSection(request, getSection(request));
    }

    private Section selectSection(HttpServletRequest request, Section section) {
        if (section != null) {
            request.setAttribute("section", section);
            
            final Set<Section> selectedSections = new HashSet<Section>();
            for (Section currentSection = section ; currentSection != null; currentSection = currentSection.getSuperiorSection()) {
                selectedSections.add(currentSection);
            }
            
            request.setAttribute("selectedSections", selectedSections);
        }
        
        return section;
    }
    
    private Item selectItem(HttpServletRequest request) {
       Item item = getItem(request);
       
       if (item != null) {
           selectSection(request, item.getSection());
           request.setAttribute("item", item);
       }
       
       return item;
    }
    
    private Item getItem(HttpServletRequest request) {
        final Integer itemID = Integer.valueOf(request.getParameter("itemID"));
        return rootDomainObject.readItemByOID(itemID);
    }
    
    public ActionForward rss(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("execution-course-rss");
    }

    protected Section getSection(final HttpServletRequest request) {
        final Integer sectionID = Integer.valueOf(request.getParameter("sectionID"));
        return rootDomainObject.readSectionByOID(sectionID);
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

}