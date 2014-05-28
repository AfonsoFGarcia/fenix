package net.sourceforge.fenixedu.presentationTier.backBeans.sop.evaluation;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeModuleScope;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.WrittenTest;
import net.sourceforge.fenixedu.domain.space.SpaceUtils;
import net.sourceforge.fenixedu.domain.space.WrittenEvaluationSpaceOccupation;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicPeriod;
import net.sourceforge.fenixedu.presentationTier.backBeans.teacher.evaluation.EvaluationManagementBackingBean;
import net.sourceforge.fenixedu.presentationTier.jsf.components.util.CalendarLink;

import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.util.MessageResources;
import org.fenixedu.commons.i18n.I18N;
import org.fenixedu.spaces.domain.Space;
import org.fenixedu.spaces.domain.SpaceClassification;
import org.fenixedu.spaces.domain.occupation.Occupation;

import pt.utl.ist.fenix.tools.util.DateFormatUtil;

import com.google.common.base.Objects;
import com.google.common.base.Strings;

public class WrittenEvaluationsByRoomBackingBean extends EvaluationManagementBackingBean {

    private static final MessageResources messages = MessageResources
            .getMessageResources("resources/ResourceAllocationManagerResources");

    private String name;

    private String building;

    private String floor;

    private String type;

    private String normalCapacity;

    private String examCapacity;

    private String academicInterval = AcademicInterval.readDefaultAcademicInterval(AcademicPeriod.SEMESTER)
            .getResumedRepresentationInStringFormat();

    private String startDate;

    private String endDate;

    private Boolean includeEntireYear;

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getExamCapacity() {
        return examCapacity;
    }

    public void setExamCapacity(String examCapacity) {
        this.examCapacity = examCapacity;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNormalCapacity() {
        return normalCapacity;
    }

    public void setNormalCapacity(String normalCapacity) {
        this.normalCapacity = normalCapacity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAcademicInterval() {
        return academicInterval == null ? academicInterval = getAndHoldStringParameter("academicInterval") : academicInterval;
    }

    public void setAcademicInterval(String academicInterval) {
        this.academicInterval = academicInterval;
    }

    protected AcademicInterval getAcademicIntervalObject() {
        return (getAcademicInterval() == null) ? null : AcademicInterval
                .getAcademicIntervalFromResumedString(getAcademicInterval());
    }

    private boolean submittedForm = false;

    public boolean getSubmittedForm() {
        return submittedForm;
    }

    public WrittenEvaluationsByRoomBackingBean() {
        if (getRequestParameter("submittedForm") != null) {
            this.submittedForm = true;
        }
        getExecutionCourseID();
    }

    private Collection<Space> allRooms = null;

    private Collection<Space> getAllRooms() throws FenixServiceException {
        if (allRooms == null) {
            allRooms = SpaceUtils.allocatableSpacesForEducation().collect(Collectors.toList());
        }
        return allRooms;
    }

    private Set<String> selectedRoomIDs = null;

    public Set<String> getSelectedRoomIDs() {
        if (selectedRoomIDs == null) {
            final String[] selectedRoomIDStrings = getRequest().getParameterValues("selectedRoomIDs");
            if (selectedRoomIDStrings != null) {
                selectedRoomIDs = new HashSet<String>(selectedRoomIDStrings.length);
                for (final String roomIDString : selectedRoomIDStrings) {
                    selectedRoomIDs.add(roomIDString);
                }

            } else if (getRequest().getParameter("selectedRoomID") != null) {
                String roomID = getRequest().getParameter("selectedRoomID");
                selectedRoomIDs = new HashSet<String>(1);
                selectedRoomIDs.add(roomID);
            }
        }
        return selectedRoomIDs;
    }

    private Collection<Space> searchRooms() throws FenixServiceException {

        final String name = getName();
        final String building = (getBuilding() != null && getBuilding().length() > 0) ? getBuilding() : null;
        final Integer floor = (getFloor() != null && getFloor().length() > 0) ? Integer.valueOf(getFloor()) : null;
        final String type = getType();
        final Integer normalCapacity =
                (getNormalCapacity() != null && getNormalCapacity().length() > 0) ? Integer.valueOf(getNormalCapacity()) : null;
                final Integer examCapacity =
                        (getExamCapacity() != null && getExamCapacity().length() > 0) ? Integer.valueOf(getExamCapacity()) : null;

                        final Collection<Space> rooms = getAllRooms();
                        final Collection<Space> selectedRooms = new ArrayList<Space>();

                        for (final Space room : rooms) {
                            boolean matchesCriteria = true;

                            if (!Strings.isNullOrEmpty(name) && !room.getName().equalsIgnoreCase(name)) {
                                matchesCriteria = false;
                            } else if (building != null && !SpaceUtils.getSpaceBuilding(room).getExternalId().equals(building)) {
                                matchesCriteria = false;
                            } else if (floor != null
                                    && !Objects.equal(floor,
                                            SpaceUtils.getSpaceFloor(room).map(f -> f.<Integer> getMetadata("level").orElse(null)))) {
                                matchesCriteria = false;
                            } else if (type != null && type.length() > 0
                                    && !room.getClassification().get().getExternalId().toString().equals(type)) {
                                matchesCriteria = false;
                            } else if (normalCapacity != null && room.getAllocatableCapacity().intValue() < normalCapacity.intValue()) {
                                matchesCriteria = false;
                            } else if (examCapacity != null && room.<Integer> getMetadata("examCapacity").orElse(0) < examCapacity.intValue()) {
                                matchesCriteria = false;
                            }

                            if (matchesCriteria && !StringUtils.isEmpty(room.getName())) {
                                selectedRooms.add(room);
                            }
                        }
                        return selectedRooms;
    }

    public Collection<Space> getRooms() throws FenixServiceException {
        return getSubmittedForm() ? searchRooms() : null;
    }

    public Collection<Space> getBuildings() throws FenixServiceException {
        return SpaceUtils.buildings();
    }

    public Collection<Space> getRoomsToDisplayMap() throws FenixServiceException {
        final Set<String> selectedRoomIDs = getSelectedRoomIDs();
        if (selectedRoomIDs != null) {
            return filterRooms(getAllRooms(), selectedRoomIDs);
        } else {
            final Collection<Space> rooms = getRooms();
            return (rooms != null && rooms.size() == 1) ? getRooms() : null;
        }
    }

    private Collection<Space> filterRooms(final Collection<Space> allRooms, final Set<String> selectedRoomIDs) {
        final Collection<Space> rooms = new ArrayList<Space>(selectedRoomIDs.size());
        for (final Space room : allRooms) {
            if (selectedRoomIDs.contains(room.getExternalId())) {
                rooms.add(room);
            }
        }
        return rooms;
    }

    private static final Comparator<SelectItem> SELECT_ITEM_LABEL_COMPARATOR = new Comparator<SelectItem>() {

        @Override
        public int compare(SelectItem o1, SelectItem o2) {
            return o1.getLabel().compareTo(o2.getLabel());
        }

    };

    public Collection<SelectItem> getBuildingSelectItems() throws FenixServiceException {
        final List<Space> buildings = (List<Space>) getBuildings();
        final List<SelectItem> buildingSelectItems = new ArrayList<SelectItem>();
        for (final Space building : buildings) {
            buildingSelectItems.add(new SelectItem(building.getExternalId().toString(), building.getName()));
        }
        Collections.sort(buildingSelectItems, SELECT_ITEM_LABEL_COMPARATOR);
        return buildingSelectItems;
    }

    public Collection<SelectItem> getAcademicIntervals() throws FenixServiceException {
        List<AcademicInterval> intervals = AcademicInterval.readAcademicIntervals(AcademicPeriod.SEMESTER);
        Collections.sort(intervals, new ReverseComparator(AcademicInterval.COMPARATOR_BY_BEGIN_DATE));
        List<SelectItem> items = new ArrayList<>();
        for (AcademicInterval interval : intervals) {
            items.add(new SelectItem(interval.getResumedRepresentationInStringFormat(), interval.getPathName()));
        }
        return items;
    }

    public Collection<SelectItem> getRoomTypeSelectItems() throws FenixServiceException {
        Collection<SpaceClassification> roomClassificationsForEducation = rootDomainObject.getRootClassificationSet();
        final List<SelectItem> roomTypeSelectItems = new ArrayList<SelectItem>();
        for (SpaceClassification classification : SpaceUtils.sortByRoomClassificationAndCode(roomClassificationsForEducation)) {
            if (classification.getParent() != null) {
                roomTypeSelectItems.add(new SelectItem(String.valueOf(classification.getExternalId()), classification
                        .getAbsoluteCode() + " - " + classification.getName().getContent(I18N.getLocale())));
            }
        }
        return roomTypeSelectItems;
    }

    @Deprecated
    public ExecutionSemester getExecutionPeriod() throws FenixServiceException {
        return (ExecutionSemester) (getAcademicIntervalObject() != null ? ExecutionSemester
                .getExecutionInterval(getAcademicIntervalObject()) : null);
    }

    public Date getCalendarBegin() throws FenixServiceException, ParseException {
        if (getStartDate() != null && getStartDate().length() > 0) {
            return DateFormatUtil.parse("dd/MM/yyyy", getStartDate());
        }
        return getAcademicIntervalObject().getStart().toDate();
    }

    public Date getCalendarEnd() throws FenixServiceException, ParseException {
        if (getEndDate() != null && getEndDate().length() > 0) {
            return DateFormatUtil.parse("dd/MM/yyyy", getEndDate());
        }
        return getAcademicIntervalObject().getEnd().toDate();
    }

    public Map<Space, List<CalendarLink>> getWrittenEvaluationCalendarLinks() throws FenixServiceException {
        final Collection<Space> rooms = getRoomsToDisplayMap();
        if (rooms != null) {
            AcademicInterval interval = getAcademicIntervalObject();
            final AcademicInterval otherAcademicInterval;
            final Boolean includeEntireYear = getIncludeEntireYear();
            if (includeEntireYear != null && includeEntireYear.booleanValue()) {
                otherAcademicInterval = interval.getPreviousAcademicInterval();
            } else {
                otherAcademicInterval = null;
            }

            final Map<Space, List<CalendarLink>> calendarLinksMap = new HashMap<Space, List<CalendarLink>>();
            for (final Space room : rooms) {
                final List<CalendarLink> calendarLinks = new ArrayList<CalendarLink>();
                for (final Occupation roomOccupation : room.getOccupationSet()) {
                    if (roomOccupation instanceof WrittenEvaluationSpaceOccupation) {
                        Collection<WrittenEvaluation> writtenEvaluations =
                                ((WrittenEvaluationSpaceOccupation) roomOccupation).getWrittenEvaluationsSet();
                        for (WrittenEvaluation writtenEvaluation : writtenEvaluations) {
                            if (verifyWrittenEvaluationExecutionPeriod(writtenEvaluation, interval, otherAcademicInterval)) {
                                final ExecutionCourse executionCourse =
                                        writtenEvaluation.getAssociatedExecutionCoursesSet().iterator().next();
                                final CalendarLink calendarLink =
                                        new CalendarLink(executionCourse, writtenEvaluation, I18N.getLocale());
                                calendarLink.setLinkParameters(constructLinkParameters(executionCourse, writtenEvaluation));
                                calendarLinks.add(calendarLink);
                            }
                        }
                    }
                }
                calendarLinksMap.put(room, calendarLinks);
            }
            return calendarLinksMap;
        } else {
            return null;
        }
    }

    protected boolean verifyWrittenEvaluationExecutionPeriod(WrittenEvaluation writtenEvaluation, AcademicInterval interval,
            AcademicInterval otherAcademicInterval) {
        for (ExecutionCourse executionCourse : writtenEvaluation.getAssociatedExecutionCoursesSet()) {
            if (executionCourse.getAcademicInterval().equals(interval)
                    || (otherAcademicInterval != null && executionCourse.getAcademicInterval().equals(otherAcademicInterval))) {
                return true;
            }
        }
        return false;
    }

    public List<Entry<Space, List<CalendarLink>>> getWrittenEvaluationCalendarLinksEntryList() throws FenixServiceException {
        final Map<Space, List<CalendarLink>> calendarLinks = getWrittenEvaluationCalendarLinks();
        return (calendarLinks != null) ? new ArrayList<Entry<Space, List<CalendarLink>>>(calendarLinks.entrySet()) : null;
    }

    private Map<String, String> constructLinkParameters(final ExecutionCourse executionCourse,
            final WrittenEvaluation writtenEvaluation) {
        final ExecutionSemester executionSemester = executionCourse.getExecutionPeriod();
        final ExecutionDegree executionDegree = findExecutionDegree(executionCourse);
        final Integer year = findCurricularYear(executionCourse);
        CurricularYear curricularYear = CurricularYear.readByYear(year);

        final Map<String, String> linkParameters = new HashMap<String, String>();
        linkParameters.put("executionCourseID", executionCourse.getExternalId().toString());
        linkParameters.put("evaluationID", writtenEvaluation.getExternalId().toString());
        linkParameters.put("executionPeriodID", executionSemester.getExternalId().toString());
        linkParameters.put("executionDegreeID", executionDegree.getExternalId().toString());
        if (curricularYear != null) {
            linkParameters.put("curricularYearID", curricularYear.getExternalId().toString());
        }
        linkParameters.put("evaluationTypeClassname", writtenEvaluation.getClass().getName());
        linkParameters.put("academicInterval", getAcademicInterval());
        return linkParameters;
    }

    private ExecutionDegree findExecutionDegree(final ExecutionCourse executionCourse) {
        final ExecutionSemester executionSemester = executionCourse.getExecutionPeriod();
        final ExecutionYear executionYear = executionSemester.getExecutionYear();

        for (final CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCoursesSet()) {
            final DegreeCurricularPlan degreeCurricularPlan = curricularCourse.getDegreeCurricularPlan();
            for (final ExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegreesSet()) {
                if (executionDegree.getExecutionYear() == executionYear) {
                    return executionDegree;
                }
            }
        }
        return null;
    }

    private Integer findCurricularYear(final ExecutionCourse executionCourse) {
        final ExecutionSemester executionSemester = executionCourse.getExecutionPeriod();

        for (final CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCoursesSet()) {
            for (DegreeModuleScope degreeModuleScope : curricularCourse.getDegreeModuleScopes()) {
                if (degreeModuleScope.isActiveForExecutionPeriod(executionSemester)) {
                    return degreeModuleScope.getCurricularYear();
                }
            }
        }
        return null;
    }

    protected String constructEvaluationCalendarPresentarionString(final WrittenEvaluation writtenEvaluation,
            final ExecutionCourse executionCourse) {
        final StringBuilder stringBuilder = new StringBuilder();
        if (writtenEvaluation instanceof WrittenTest) {
            stringBuilder.append(messages.getMessage("label.evaluation.shortname.test"));
        } else if (writtenEvaluation instanceof Exam) {
            stringBuilder.append(messages.getMessage("label.evaluation.shortname.exam"));
        }
        stringBuilder.append(" ");
        stringBuilder.append(executionCourse.getSigla());
        stringBuilder.append(" (");
        stringBuilder.append(DateFormatUtil.format("HH:mm", writtenEvaluation.getBeginning().getTime()));
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    public String getEndDate() {
        return (endDate == null) ? endDate = getAndHoldStringParameter("endDate") : endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStartDate() {
        return (startDate == null) ? startDate = getAndHoldStringParameter("startDate") : startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public Boolean getIncludeEntireYear() {
        return (includeEntireYear == null) ? includeEntireYear = getAndHoldBooleanParameter("includeEntireYear") : includeEntireYear;
    }

    public void setIncludeEntireYear(Boolean includeEntireYear) {
        this.includeEntireYear = includeEntireYear;
    }

}