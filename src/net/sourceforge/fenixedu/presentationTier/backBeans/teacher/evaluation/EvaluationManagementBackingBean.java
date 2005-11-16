package net.sourceforge.fenixedu.presentationTier.backBeans.teacher.evaluation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.faces.component.html.HtmlInputHidden;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoMark;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteMarks;
import net.sourceforge.fenixedu.dataTransferObject.TeacherAdministrationSiteView;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourseScope;
import net.sourceforge.fenixedu.domain.IEvaluation;
import net.sourceforge.fenixedu.domain.IExam;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IFinalEvaluation;
import net.sourceforge.fenixedu.domain.IMark;
import net.sourceforge.fenixedu.domain.IRoom;
import net.sourceforge.fenixedu.domain.IRoomOccupation;
import net.sourceforge.fenixedu.domain.IWrittenEvaluation;
import net.sourceforge.fenixedu.domain.IWrittenEvaluationEnrolment;
import net.sourceforge.fenixedu.domain.IWrittenTest;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.WrittenTest;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.onlineTests.IOnlineTest;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;
import net.sourceforge.fenixedu.util.DateFormatUtil;
import net.sourceforge.fenixedu.util.Season;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.myfaces.component.html.util.MultipartRequestWrapper;
import org.apache.struts.util.MessageResources;

public class EvaluationManagementBackingBean extends FenixBackingBean {

    protected Integer executionCourseID;

    private HtmlInputHidden executionCourseIdHidden;

    protected Integer evaluationID;

    private HtmlInputHidden evaluationIdHidden;

    protected String evaluationTypeClassname;

    protected Integer day;

    protected Integer month;

    protected Integer year;

    protected Integer beginHour;

    protected Integer beginMinute;

    protected Integer endHour;

    protected Integer endMinute;

    protected Integer enrolmentBeginDay;

    protected Integer enrolmentBeginMonth;

    protected Integer enrolmentBeginYear;

    protected Integer enrolmentBeginHour;

    protected Integer enrolmentBeginMinute;

    protected Integer enrolmentEndDay;

    protected Integer enrolmentEndMonth;

    protected Integer enrolmentEndYear;

    protected Integer enrolmentEndHour;

    protected Integer enrolmentEndMinute;

    protected String description;

    private String season;

    protected List<IWrittenEvaluationEnrolment> writtenEvaluationEnrolments;

    protected IEvaluation evaluation;

    protected String distributeEnroledStudentsOption;
    
    private boolean resetPosition = false;

    private String publishMarksMessage;

    private Boolean sendSMS;

    private String originPage;

    protected Map<Integer, String> marks = new HashMap<Integer, String>();

    public Integer getExecutionCourseID() {
        if (this.executionCourseID == null) {
            if (this.executionCourseIdHidden != null && this.executionCourseIdHidden.getValue() != null) {
                this.executionCourseID = Integer.valueOf(this.executionCourseIdHidden.getValue()
                        .toString());
            } else {
                String executionCourseIDString = this.getRequestParameter("executionCourseID");
                if (executionCourseIDString != null) {
                    this.executionCourseID = Integer.valueOf(executionCourseIDString);
                }
            }
        }
        return this.executionCourseID;
    }

    public void setExecutionCourseID(Integer executionCourseId) {
        this.executionCourseID = executionCourseId;
    }

    public HtmlInputHidden getExecutionCourseIdHidden() {
        if (this.executionCourseIdHidden == null) {
            Integer executionCourseId = this.getExecutionCourseID();

            this.executionCourseIdHidden = new HtmlInputHidden();
            this.executionCourseIdHidden.setValue(executionCourseId);
            this.executionCourseID = executionCourseId;
        }
        return this.executionCourseIdHidden;
    }

    public void setExecutionCourseIdHidden(HtmlInputHidden executionCourseIdHidden) {
        if (executionCourseIdHidden != null) {
            this.executionCourseID = Integer.valueOf(executionCourseIdHidden.getValue().toString());
        }
        this.executionCourseIdHidden = executionCourseIdHidden;
    }

    public Integer getEvaluationID() {
        if (this.evaluationID == null) {
            if (this.evaluationIdHidden != null && this.evaluationIdHidden.getValue() != null && !this.evaluationIdHidden.getValue().equals("")) {
                this.evaluationID = Integer.valueOf(this.evaluationIdHidden.getValue().toString());
            } else if (this.getRequestParameter("evaluationID") != null && !this.getRequestParameter("evaluationID").equals("")) {
                this.evaluationID = Integer.valueOf(this.getRequestParameter("evaluationID"));
            }
        }
        return this.evaluationID;
    }

    public void setEvaluationID(Integer evaluationId) {
        this.evaluationID = evaluationId;
    }

    public HtmlInputHidden getEvaluationIdHidden() {
        if (this.evaluationIdHidden == null) {
            Integer evaluationId = this.getEvaluationID();

            this.evaluationIdHidden = new HtmlInputHidden();
            this.evaluationIdHidden.setValue(evaluationId);
        }

        return this.evaluationIdHidden;
    }

    public void setEvaluationIdHidden(HtmlInputHidden evaluationIdHidden) {
        if (evaluationIdHidden != null && evaluationIdHidden.getValue() != null && !evaluationIdHidden.getValue().toString().equals("")) {
            this.evaluationID = Integer.valueOf(evaluationIdHidden.getValue().toString());
        }

        this.evaluationIdHidden = evaluationIdHidden;
    }

    public Integer getDay() throws FenixFilterException, FenixServiceException {
        if (this.day == null && this.getEvaluation() != null) {
            this.day = ((IWrittenEvaluation) getEvaluation()).getDay().get(Calendar.DAY_OF_MONTH);
        } else if (this.getRequestParameter("day") != null && !this.getRequestParameter("day").equals("")) {
            this.day = Integer.valueOf(this.getRequestParameter("day"));
        }
        return this.day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getMonth() throws FenixFilterException, FenixServiceException {
        if (this.month == null && this.getEvaluation() != null) {
            this.month = ((IWrittenEvaluation) getEvaluation()).getDay().get(Calendar.MONTH) + 1;
        } else if (this.getRequestParameter("month") != null && !this.getRequestParameter("month").equals("")) {
            this.month = Integer.valueOf(this.getRequestParameter("month"));
        }
        return this.month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() throws FenixFilterException, FenixServiceException {
        if (this.year == null && this.getEvaluation() != null) {
            this.year = ((IWrittenEvaluation) getEvaluation()).getDay().get(Calendar.YEAR);
        } else if (this.getRequestParameter("year") != null && !this.getRequestParameter("year").equals("")) {
            this.year = Integer.valueOf(this.getRequestParameter("year"));
        }
        return this.year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getBeginHour() throws FenixFilterException, FenixServiceException {
        if (this.beginHour == null && this.getEvaluation() != null) {
            this.beginHour = ((IWrittenEvaluation) getEvaluation()).getBeginning().get(Calendar.HOUR_OF_DAY);
        }
        return this.beginHour;
    }

    public void setBeginHour(Integer beginHour) {
        this.beginHour = beginHour;
    }
    
    public Integer getBeginMinute() throws FenixFilterException, FenixServiceException {
        if (this.beginMinute == null && this.getEvaluation() != null) {
            this.beginMinute = ((IWrittenEvaluation) getEvaluation()).getBeginning().get(Calendar.MINUTE);
        }
        return this.beginMinute;
    }

    public void setBeginMinute(Integer beginMinute) {
        this.beginMinute = beginMinute;
    }

    public Integer getEndHour() throws FenixFilterException, FenixServiceException {
        if (this.endHour == null && this.getEvaluation() != null && ((IWrittenEvaluation) getEvaluation()).getEnd() != null) {
            this.endHour = ((IWrittenEvaluation) getEvaluation()).getEnd().get(Calendar.HOUR_OF_DAY);
        }
        return this.endHour;
    }

    public void setEndHour(Integer endHour) {
        this.endHour = endHour;
    }

    public Integer getEndMinute() throws FenixFilterException, FenixServiceException {
        if (this.endMinute == null && this.getEvaluation() != null && ((IWrittenEvaluation) getEvaluation()).getEnd() != null) {
            this.endMinute = ((IWrittenEvaluation) getEvaluation()).getEnd().get(Calendar.MINUTE);
        }
        return this.endMinute;
    }

    public void setEndMinute(Integer endMinute) {
        this.endMinute = endMinute;
    }

    public String getDescription() throws FenixFilterException, FenixServiceException {
        if (this.description == null && this.getEvaluation() != null) {
            final IEvaluation writtenEvaluation = getEvaluation();
            if (writtenEvaluation instanceof IWrittenTest) {
                this.description = ((IWrittenTest) writtenEvaluation).getDescription();
            }
        } else if (this.getViewState().getAttribute("description") != null && !this.getViewState().getAttribute("description").equals("")) {
            this.description = (String) this.getViewState().getAttribute("description");
        }
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
        this.getViewState().setAttribute("description", description);
    }

    public Integer getEnrolmentBeginDay() throws FenixFilterException, FenixServiceException {
        if (this.enrolmentBeginDay == null && ((IWrittenEvaluation) this.getEvaluation()).getEnrollmentBeginDay() != null) {
            this.enrolmentBeginDay = ((IWrittenEvaluation) this.getEvaluation()).getEnrollmentBeginDay().get(
                    Calendar.DAY_OF_MONTH);
        }
        return this.enrolmentBeginDay;
    }

    public void setEnrolmentBeginDay(Integer enrolmentBeginDay) {
        this.enrolmentBeginDay = enrolmentBeginDay;
    }

    public Integer getEnrolmentBeginHour() throws FenixFilterException, FenixServiceException {
        if (this.enrolmentBeginHour == null && ((IWrittenEvaluation) this.getEvaluation()).getEnrollmentBeginTime() != null) {
            this.enrolmentBeginHour = ((IWrittenEvaluation) this.getEvaluation()).getEnrollmentBeginTime().get(
                    Calendar.HOUR_OF_DAY);
        }
        return this.enrolmentBeginHour;
    }

    public void setEnrolmentBeginHour(Integer enrolmentBeginHour) {
        this.enrolmentBeginHour = enrolmentBeginHour;
    }

    public Integer getEnrolmentBeginMinute() throws FenixFilterException, FenixServiceException {
        if (this.enrolmentBeginMinute == null && ((IWrittenEvaluation) this.getEvaluation()).getEnrollmentBeginTime() != null) {
            this.enrolmentBeginMinute = ((IWrittenEvaluation) this.getEvaluation()).getEnrollmentBeginTime().get(
                    Calendar.MINUTE);
        }
        return this.enrolmentBeginMinute;
    }

    public void setEnrolmentBeginMinute(Integer enrolmentBeginMinute) {
        this.enrolmentBeginMinute = enrolmentBeginMinute;
    }

    public Integer getEnrolmentBeginMonth() throws FenixFilterException, FenixServiceException {
        if (this.enrolmentBeginMonth == null && ((IWrittenEvaluation) this.getEvaluation()).getEnrollmentBeginDay() != null) {
            this.enrolmentBeginMonth = ((IWrittenEvaluation) this.getEvaluation()).getEnrollmentBeginDay().get(Calendar.MONTH) + 1;
        }
        return enrolmentBeginMonth;
    }

    public void setEnrolmentBeginMonth(Integer enrolmentBeginMonth) {
        this.enrolmentBeginMonth = enrolmentBeginMonth;
    }

    public Integer getEnrolmentBeginYear() throws FenixFilterException, FenixServiceException {
        if (this.enrolmentBeginYear == null && ((IWrittenEvaluation) this.getEvaluation()).getEnrollmentBeginDay() != null) {
            this.enrolmentBeginYear = ((IWrittenEvaluation) this.getEvaluation()).getEnrollmentBeginDay().get(Calendar.YEAR);
        }
        return enrolmentBeginYear;
    }

    public void setEnrolmentBeginYear(Integer enrolmentBeginYear) {
        this.enrolmentBeginYear = enrolmentBeginYear;
    }

    public Integer getEnrolmentEndDay() throws FenixFilterException, FenixServiceException {
        if (this.enrolmentEndDay == null && ((IWrittenEvaluation) this.getEvaluation()).getEnrollmentEndDay() != null) {
            this.enrolmentEndDay = ((IWrittenEvaluation) this.getEvaluation()).getEnrollmentEndDay().get(Calendar.DAY_OF_MONTH);
        }
        return this.enrolmentEndDay;
    }

    public void setEnrolmentEndDay(Integer enrolmentEndDay) {
        this.enrolmentEndDay = enrolmentEndDay;
    }

    public Integer getEnrolmentEndHour() throws FenixFilterException, FenixServiceException {
        if (this.enrolmentEndHour == null && ((IWrittenEvaluation) this.getEvaluation()).getEnrollmentEndTime() != null) {
            this.enrolmentEndHour = ((IWrittenEvaluation) this.getEvaluation()).getEnrollmentEndTime()
                    .get(Calendar.HOUR_OF_DAY);
        }
        return this.enrolmentEndHour;
    }

    public void setEnrolmentEndHour(Integer enrolmentEndHour) {
        this.enrolmentEndHour = enrolmentEndHour;
    }

    public Integer getEnrolmentEndMinute() throws FenixFilterException, FenixServiceException {
        if (this.enrolmentEndMinute == null && ((IWrittenEvaluation) this.getEvaluation()).getEnrollmentEndTime() != null) {
            this.enrolmentEndMinute = ((IWrittenEvaluation) this.getEvaluation()).getEnrollmentEndTime().get(Calendar.MINUTE);
        }
        return this.enrolmentEndMinute;
    }

    public void setEnrolmentEndMinute(Integer enrolmentEndMinute) {
        this.enrolmentEndMinute = enrolmentEndMinute;
    }

    public Integer getEnrolmentEndMonth() throws FenixFilterException, FenixServiceException {
        if (this.enrolmentEndMonth == null && ((IWrittenEvaluation) this.getEvaluation()).getEnrollmentEndDay() != null) {
            this.enrolmentEndMonth = ((IWrittenEvaluation) this.getEvaluation()).getEnrollmentEndDay().get(Calendar.MONTH) + 1;
        }
        return enrolmentEndMonth;
    }

    public void setEnrolmentEndMonth(Integer enrolmentEndMonth) {
        this.enrolmentEndMonth = enrolmentEndMonth;
    }

    public Integer getEnrolmentEndYear() throws FenixFilterException, FenixServiceException {
        if (this.enrolmentEndYear == null && ((IWrittenEvaluation) this.getEvaluation()).getEnrollmentEndDay() != null) {
            this.enrolmentEndYear = ((IWrittenEvaluation) this.getEvaluation()).getEnrollmentEndDay().get(Calendar.YEAR);
        }
        return enrolmentEndYear;
    }

    public void setEnrolmentEndYear(Integer enrolmentEndYear) {
        this.enrolmentEndYear = enrolmentEndYear;
    }

    public String getDistributeEnroledStudentsOption() {
        return (distributeEnroledStudentsOption == null) ? "true" : this.distributeEnroledStudentsOption;
    }

    public void setDistributeEnroledStudentsOption(String distributeEnroledStudentsOption) {
        this.distributeEnroledStudentsOption = distributeEnroledStudentsOption;
    }

    public Integer getRoomToChangeID() throws FenixFilterException, FenixServiceException {
        if (getViewState().getAttribute("roomToChangeID") == null) {
            getViewState().setAttribute("roomToChangeID",
                    getElementKeyFor(getEvaluationRoomsPositions(), 1));
        }
        return (Integer) getViewState().getAttribute("roomToChangeID");
    }

    public void setRoomToChangeID(Integer roomToChangeID) {
        getViewState().setAttribute("roomToChangeID", roomToChangeID);
    }

    public Integer getNewRoomPosition() throws FenixFilterException, FenixServiceException {
        if (getViewState().getAttribute("newRoomPosition") == null || this.resetPosition) {
            getViewState().setAttribute("newRoomPosition", 0);
        }
        return (Integer) getViewState().getAttribute("newRoomPosition");
    }

    public void setNewRoomPosition(Integer newRoomPosition) {
        getViewState().setAttribute("newRoomPosition", newRoomPosition);
    }

    public List<IExam> getExamList() throws FenixFilterException, FenixServiceException {
        final Object[] args = { ExecutionCourse.class, this.getExecutionCourseID() };
        IExecutionCourse executionCourse = (IExecutionCourse) ServiceUtils.executeService(null,
                "ReadDomainObject", args);

        List<IExam> examsList = new ArrayList(executionCourse.getAssociatedExams());
        Collections.sort(examsList, new BeanComparator("dayDate"));
        return examsList;
    }

    public List<IOnlineTest> getOnlineTestList() throws FenixFilterException, FenixServiceException {
        final Object[] args = { ExecutionCourse.class, this.getExecutionCourseID() };
        IExecutionCourse executionCourse = (IExecutionCourse) ServiceUtils.executeService(null,
                "ReadDomainObject", args);

        List<IOnlineTest> onlineTestList = new ArrayList(executionCourse.getAssociatedOnlineTests());
        Collections.sort(onlineTestList, new BeanComparator("distributedTest.beginDateDate"));
        return onlineTestList;
    }
    
    public List<IWrittenTest> getWrittenTestList() throws FenixFilterException, FenixServiceException {
        final Object[] args = { ExecutionCourse.class, this.getExecutionCourseID() };
        IExecutionCourse executionCourse = (IExecutionCourse) ServiceUtils.executeService(null,
                "ReadDomainObject", args);

        List<IWrittenTest> writtenTestList = new ArrayList(executionCourse.getAssociatedWrittenTests());
        Collections.sort(writtenTestList, new BeanComparator("dayDate"));
        return writtenTestList;
    }

    public IEvaluation getEvaluation() throws FenixFilterException, FenixServiceException {
        if (this.evaluation == null) {
            if (this.getEvaluationID() != null) {
                final Object[] args = { WrittenEvaluation.class, this.getEvaluationID() };
                evaluation = (IEvaluation) ServiceUtils.executeService(null, "ReadDomainObject", args);
            } else { // Should not happen
                return null;
            }
        }
        return this.evaluation;
    }

    private Calendar getEnrolmentBegin() throws FenixFilterException, FenixServiceException {
        Calendar result = Calendar.getInstance();

        result.set(getEnrolmentBeginYear(), getEnrolmentBeginMonth() - 1, getEnrolmentBeginDay(),
                getEnrolmentBeginHour(), getEnrolmentBeginMinute());

        return result;
    }

    private Calendar getEnrolmentEnd() throws FenixFilterException, FenixServiceException {
        Calendar result = Calendar.getInstance();

        result.set(getEnrolmentEndYear(), getEnrolmentEndMonth() - 1, getEnrolmentEndDay(),
                getEnrolmentEndHour(), getEnrolmentEndMinute());

        return result;
    }

    public String editEvaluationEnrolmentPeriod() throws FenixFilterException, FenixServiceException {
        Calendar enrolmentBeginDay = getEnrolmentBegin();
        Calendar enrolmentBeginTime = getEnrolmentBegin();
        Calendar enrolmentEndDay = getEnrolmentEnd();
        Calendar enrolmentEndTime = getEnrolmentEnd();

        final Object[] args = { getExecutionCourseID(), getEvaluationID(), enrolmentBeginDay,
                enrolmentEndDay, enrolmentBeginTime, enrolmentEndTime };

        try {
            ServiceUtils.executeService(getUserView(), "EditWrittenEvaluationEnrolmentPeriod", args);
        } catch (Exception e) {
            addErrorMessage(e.getMessage());
            String errorMessage = e.getMessage();
            if (e instanceof NotAuthorizedFilterException) {
                errorMessage = "message.error.notAuthorized";
            }
            this.setErrorMessage(errorMessage);
            return "";
        }

        return this.getEvaluation().getClass().getSimpleName();
    }

    public String editMarks() throws FenixFilterException, FenixServiceException {
        final Object[] args = { getEvaluationID(), this.marks };
        ServiceUtils.executeService(getUserView(), "WriteMarks", args);
        return getEvaluation().getClass().getSimpleName();
    }

    public List<IWrittenEvaluationEnrolment> getWrittenEvaluationEnrolments()
            throws FenixFilterException, FenixServiceException {

        if (this.writtenEvaluationEnrolments == null) {
            this.writtenEvaluationEnrolments = new ArrayList(((IWrittenEvaluation) getEvaluation())
                    .getWrittenEvaluationEnrolments());
            Collections.sort(this.writtenEvaluationEnrolments, new BeanComparator("student.number"));
        }
        return this.writtenEvaluationEnrolments;
    }
    
    public Calendar getBegin() throws FenixFilterException, FenixServiceException {
        Calendar result = Calendar.getInstance();

        result.set(this.getYear(), this.getMonth() - 1, this.getDay(), this.getBeginHour(), this
                .getBeginMinute());

        return result;
    }

    public Calendar getEnd() throws FenixFilterException, FenixServiceException {
        Calendar result = Calendar.getInstance();

        result.set(this.getYear(), this.getMonth() - 1, this.getDay(), this.getEndHour(), this
                .getEndMinute());

        return result;
    }
    
    public String createWrittenTest() throws FenixFilterException, FenixServiceException {
        List<String> executionCourseIDs = new ArrayList<String>();
        executionCourseIDs.add(this.getExecutionCourseID().toString());

        List<String> curricularCourseScopeIDs = new ArrayList<String>();
        final Object[] argsToReadExecutionCourse = { ExecutionCourse.class, this.getExecutionCourseID() };
        IExecutionCourse executionCourse = (IExecutionCourse) ServiceUtils.executeService(null,
                "ReadDomainObject", argsToReadExecutionCourse);
        for (ICurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCourses()) {
            for (ICurricularCourseScope curricularCourseScope : curricularCourse.getScopes()) {
                if (curricularCourseScope.getCurricularSemester().getSemester().equals(
                        executionCourse.getExecutionPeriod().getSemester())) {
                    curricularCourseScopeIDs.add(curricularCourseScope.getIdInternal().toString());
                }
            }
        }

        final Season season = (getSeason() != null) ? new Season(getSeason()) : null;
        final Object[] args = { this.getExecutionCourseID(), this.getBegin(), this.getBegin(), this.getEnd(), executionCourseIDs,
                curricularCourseScopeIDs, null, season, this.getDescription() };
        try {
            ServiceUtils.executeService(getUserView(), "CreateWrittenEvaluation", args);
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            if (e instanceof NotAuthorizedFilterException) {
                errorMessage = "message.error.notAuthorized";
            }
            this.setErrorMessage(errorMessage);
            return "";
        }

        return WrittenTest.class.getSimpleName();
    }

    public IExecutionCourse getExecutionCourse() throws FenixFilterException, FenixServiceException {
        final Object[] args = { ExecutionCourse.class, getExecutionCourseID() };
        return (IExecutionCourse) ServiceUtils.executeService(null, "ReadDomainObject", args);
    }

    public Map<Integer, String> getMarks() throws FenixFilterException, FenixServiceException {
        final IEvaluation evaluation = getEvaluation();
        final IExecutionCourse executionCourse = getExecutionCourse();
        if (executionCourse != null) {
            for (final IAttends attends : executionCourse.getAttends()) {
                for (final IMark mark : attends.getAssociatedMarks()) {
                    if (mark.getEvaluation() == evaluation
                            && !marks.containsKey(attends.getIdInternal())) {
                        marks.put(attends.getIdInternal(), mark.getMark());
                    }
                }
            }
        }
        return marks;
    }

    public void setMarks(Map<Integer, String> marks) {
        this.marks = marks;
    }

    public String loadMarks() throws FenixFilterException, FenixServiceException, FileUploadException {
        System.out.println("In load service");

        final HttpServletRequest httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        final MultipartRequestWrapper multipartRequestWrapper = (MultipartRequestWrapper) httpServletRequest.getAttribute("multipartRequestWrapper");

        final FileItem fileItem = multipartRequestWrapper.getFileItem("theFile");
        InputStream inputStream = null;
        try {
            inputStream = fileItem.getInputStream();
            final HashMap marks = loadMarks(inputStream);

            final Object[] args = { getExecutionCourseID(), getEvaluationID(), marks };
            TeacherAdministrationSiteView siteView = (TeacherAdministrationSiteView) ServiceUtils.executeService(getUserView(), "InsertEvaluationMarks", args);
            processServiceErrors(siteView);
        } catch (Exception e) {
            this.setErrorMessage(e.getMessage());
            return "";
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    // Nothing to do ...
                }
            }
        }

        return "success";
    }

    private HashMap loadMarks(final InputStream inputStream) throws IOException {
        final HashMap marks = new HashMap();

        final InputStreamReader input = new InputStreamReader(inputStream);
        final BufferedReader reader = new BufferedReader(input);

        // parsing uploaded file
        int n = 0;

        for (String lineReader = reader.readLine(); lineReader != null; lineReader = reader.readLine(), n++) {
            if ((lineReader != null) && (lineReader.length() != 0)) {
                try {
                    final StringTokenizer stringTokenizer = new StringTokenizer(lineReader);
                    final String studentNumber = stringTokenizer.nextToken().trim();
                    final String mark = stringTokenizer.nextToken().trim();
                    marks.put(studentNumber, mark);
                } catch (NoSuchElementException e) {
                    throw new IOException("error.file.badFormat");
                }
            }
        }

        if (n == 0) {
            throw new IOException("error.file.badFormat");        
        }

        return marks;
    }

    private void processServiceErrors(final TeacherAdministrationSiteView siteView) {
        final InfoSiteMarks infoSiteMarks = (InfoSiteMarks) siteView.getComponent();
        final List<String> studentsListErrors = infoSiteMarks.getStudentsListErrors();
        if (studentsListErrors != null && studentsListErrors.size() > 0) {
            for (final String studentNumber : studentsListErrors) {
                addErrorMessage("errors.student.nonExisting " + studentNumber);
            }
        }

        final List<InfoMark> marksListErrors = infoSiteMarks.getMarksListErrors();
        if (marksListErrors != null && marksListErrors.size() > 0) {
            for (final InfoMark infoMark : marksListErrors) {
                addErrorMessage("errors.invalidMark " + infoMark.getMark() + infoMark.getInfoFrequenta().getAluno().getNumber().toString());
            }
        }
    }

    public String editWrittenTest() throws Exception {
        final List<String> executionCourseIDs = new ArrayList<String>();
        executionCourseIDs.add(this.getExecutionCourseID().toString());

        final List<String> curricularCourseScopeIDs = new ArrayList<String>();
        final Object[] argsToReadExecutionCourse = { ExecutionCourse.class, this.getExecutionCourseID() };
        IExecutionCourse executionCourse = null;
        try {
            executionCourse = (IExecutionCourse) ServiceUtils.executeService(null, "ReadDomainObject", argsToReadExecutionCourse);
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            this.setErrorMessage(errorMessage);
            return "";
        }

        for (ICurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCourses()) {
            for (ICurricularCourseScope curricularCourseScope : curricularCourse.getScopes()) {
                if (curricularCourseScope.getCurricularSemester().getSemester().equals(
                        executionCourse.getExecutionPeriod().getSemester())) {
                    curricularCourseScopeIDs.add(curricularCourseScope.getIdInternal().toString());
                }
            }
        }

        final Season season = (getSeason() != null) ? new Season(getSeason()) : null;
        final Object[] args = { this.getExecutionCourseID(), this.getBegin(), this.getBegin(), this.getEnd(), executionCourseIDs,
                curricularCourseScopeIDs, null, this.evaluationID, season, this.getDescription() };
        try {
            ServiceUtils.executeService(getUserView(), "EditWrittenEvaluation", args);
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            if (e instanceof NotAuthorizedFilterException) {
                errorMessage = "message.error.notAuthorized";
            }
            this.setErrorMessage(errorMessage);
            return "";
        }

        final String originPage = getOriginPage();
        if (originPage != null) {
            
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect(getApplicationContext()
                            + "/sop/searchWrittenEvaluationsByDate.do?method=returnToSearchPage&amp;page=0&date="
                            + DateFormatUtil.format("yyyy/MM/dd", this.getBegin().getTime())
//                    + "&begin="
//                    + timeFormat.format(this.getBegin().getTime())
//                    + "&end="
//                    + timeFormat.format(this.getEnd().getTime())
                            + "&" + SessionConstants.EXECUTION_PERIOD_OID + "="
                            + executionCourse.getExecutionPeriod().getIdInternal()
                    );
            return originPage;
        } else {
            return this.getEvaluation().getClass().getSimpleName();
        }
    }

    private String getApplicationContext() {
        final String appContext = PropertiesManager.getProperty("app.context");
        return (appContext != null && appContext.length() > 0) ? "/" + appContext : "";
    }

    public String deleteWrittenTest() throws FenixFilterException, FenixServiceException {
        final Object args[] = { this.getExecutionCourseID(), this.getEvaluationID() };
        try {
            ServiceUtils.executeService(getUserView(), "DeleteWrittenEvaluation", args);
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            if (e instanceof NotAuthorizedFilterException) {
                errorMessage = "message.error.notAuthorized";
            }
            this.setErrorMessage(errorMessage);
            return "";
        }
        return WrittenTest.class.getSimpleName();
    }    

    public List<IRoom> getEvaluationRooms() throws FenixFilterException, FenixServiceException {
        final IRoom[] result = new IRoom[getEvaluationRoomsPositions().size()];
        for (final Entry<Integer, Integer> entry : getEvaluationRoomsPositions().entrySet()) {
            final IRoom room = getRoom(entry.getKey());
            result[entry.getValue() - 1] = room;
        }
        return Arrays.asList(result);
    }

    private IRoom getRoom(final Integer roomID) throws FenixFilterException, FenixServiceException {
        for (final IRoomOccupation roomOccupation : ((IWrittenEvaluation) getEvaluation()).getAssociatedRoomOccupation()) {
            if (roomOccupation.getRoom().getIdInternal().equals(roomID)) {
                return roomOccupation.getRoom();
            }
        }
        return null;
    }

    public Map<Integer, Integer> getEvaluationRoomsPositions() throws FenixFilterException,
            FenixServiceException {
        if (getViewState().getAttribute("evaluationRooms") == null) {
            final Map<Integer, Integer> evaluationRooms = initializeEvaluationRoomsPositions();
            getViewState().setAttribute("evaluationRooms", evaluationRooms);
        }
        return (Map<Integer, Integer>) getViewState().getAttribute("evaluationRooms");
    }

    private Map<Integer, Integer> initializeEvaluationRoomsPositions() throws FenixFilterException, FenixServiceException {
        final Map<Integer, Integer> evaluationRooms = new TreeMap();
        final List<IRoomOccupation> roomOccupations = new ArrayList(((IWrittenEvaluation) getEvaluation())
                .getAssociatedRoomOccupation());
        Collections.sort(roomOccupations, new ReverseComparator(new BeanComparator("room.capacidadeExame")));
        int count = 0;
        for (final IRoomOccupation roomOccupation : roomOccupations) {
            evaluationRooms.put(roomOccupation.getRoom().getIdInternal(), Integer.valueOf(++count));
        }
        return evaluationRooms;
    }
   
    public void changeRoom(ValueChangeEvent valueChangeEvent) {
        this.resetPosition = true;
    }

    public void changePosition(ValueChangeEvent valueChangeEvent) throws FenixFilterException,
            FenixServiceException {
        final Integer roomToChangeNewPosition = (Integer) valueChangeEvent.getNewValue();
        if (roomToChangeNewPosition != 0) {
            final Integer roomToChangeOldPosition = getEvaluationRoomsPositions().get(
                    getRoomToChangeID());
            final Integer elementKey = getElementKeyFor(getEvaluationRoomsPositions(),
                    roomToChangeNewPosition);
            getEvaluationRoomsPositions().put(elementKey, roomToChangeOldPosition);
            getEvaluationRoomsPositions().put(getRoomToChangeID(), roomToChangeNewPosition);
        }
    }

    private Integer getElementKeyFor(Map<Integer, Integer> evaluationRooms, Integer roomPosition) {
        for (final Entry<Integer, Integer> entry : evaluationRooms.entrySet()) {
            if (entry.getValue().equals(roomPosition)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public List<SelectItem> getNames() throws FenixFilterException, FenixServiceException {
        final List<SelectItem> result = new ArrayList(((IWrittenEvaluation) getEvaluation()).getAssociatedRoomOccupationCount());
        for (final IRoomOccupation roomOccupation : ((IWrittenEvaluation) getEvaluation()).getAssociatedRoomOccupation()) {
            result.add(new SelectItem(roomOccupation.getRoom().getIdInternal(), roomOccupation.getRoom()
                    .getNome()));
        }
        return result;
    }

    public List<SelectItem> getPositions() throws FenixFilterException, FenixServiceException {
        final List<SelectItem> result = new ArrayList(getEvaluationRoomsPositions().size());
        result.add(new SelectItem(0, ""));
        for (final Integer value : getEvaluationRoomsPositions().values()) {
            result.add(new SelectItem(value, value.toString()));
        }
        Collections.sort(result, new BeanComparator("label"));
        this.resetPosition = true;
        return result;
    }

    public String distributeStudentsByRooms() throws FenixFilterException, FenixServiceException {
        try {
            final Boolean distributeOnlyEnroledStudents = Boolean.valueOf(this
                    .getDistributeEnroledStudentsOption());
            final Object[] args = { getExecutionCourseID(), getEvaluationID(),
                    getRoomIDs(), Boolean.FALSE, distributeOnlyEnroledStudents };
            ServiceUtils.executeService(getUserView(), "WrittenEvaluationRoomDistribution", args);
            return "enterShowStudentsEnroled";
        } catch (Exception e) {
            setErrorMessage(e.getMessage());
            return "";
        }
    }
    
    private List<Integer> getRoomIDs() throws FenixFilterException, FenixServiceException {
        final List<IRoom> rooms = getEvaluationRooms(); 
        final List<Integer> result = new ArrayList(rooms.size());
        for (final IRoom room : rooms) {
            result.add(room.getIdInternal());
        }
        return result;
    }
    
    public String checkIfCanDistributeStudentsByRooms() throws FenixFilterException, FenixServiceException {
        try {
            final IEvaluation writtenEvaluation = getEvaluation();
            ((IWrittenEvaluation) writtenEvaluation).checkIfCanDistributeStudentsByRooms();
            return "enterDistributeStudentsByRooms";
        } catch (DomainException e) {
            setErrorMessage(e.getMessage());
            return "";            
        }
    }
    
    public int getNumberOfAttendingStudents() throws FenixFilterException, FenixServiceException {
        int numberOfAttendingStudents = 0;
        for (final IExecutionCourse executionCourse : getEvaluation().getAssociatedExecutionCourses()) {
            numberOfAttendingStudents += executionCourse.getAttendsCount();
        }
        return numberOfAttendingStudents;
    }

    public IFinalEvaluation getFinalEvaluation() throws FenixFilterException, FenixServiceException {
    	for (final IEvaluation evaluation : getExecutionCourse().getAssociatedEvaluations()) {
    		if (evaluation instanceof IFinalEvaluation) {
    			return (IFinalEvaluation) evaluation;
    		}
    	}
    	return null;
    }

    public List<IAttends> getExecutionCourseAttends() throws FenixFilterException, FenixServiceException {
        final Object[] args = { ExecutionCourse.class, this.getExecutionCourseID() };
        IExecutionCourse executionCourse = (IExecutionCourse) ServiceUtils.executeService(null,
                "ReadDomainObject", args);
        
        List<IAttends> executionCourseAttends = new ArrayList(executionCourse.getAttends());
        Collections.sort(executionCourseAttends, new BeanComparator("aluno.number"));
        return executionCourseAttends;
    }

	public String getPublishMarksMessage() {
		return publishMarksMessage;
	}

	public void setPublishMarksMessage(String publishMarksMessage) {
		this.publishMarksMessage = publishMarksMessage;
	}

	public Boolean getSendSMS() {
		return sendSMS;
	}

	public void setSendSMS(Boolean sendSMS) {
		this.sendSMS = sendSMS;
	}

    public String publishMarks() throws FenixFilterException, FenixServiceException {
    	final MessageResources messages = MessageResources.getMessageResources("ServidorApresentacao/ApplicationResources");
    	final String announcementTitle = (getPublishMarksMessage() != null && getPublishMarksMessage().length() > 0) ? 
        		messages.getMessage("message.publishment") : null;

        try {
            final Object[] args = { this.getExecutionCourseID(), this.getEvaluationID(), this.getPublishMarksMessage(), this.getSendSMS(), announcementTitle };
            ServiceUtils.executeService(getUserView(), "PublishMarks", args);
        } catch (Exception e) {
            this.setErrorMessage(e.getMessage());
            return "";
        }
        
        return this.getEvaluation().getClass().getSimpleName();
    }

    public String getEvaluationTypeClassname() throws FenixFilterException, FenixServiceException {
        String evaluationTypeClassname = (String) this.getViewState().getAttribute("evaluationTypeClassname");
        if (evaluationTypeClassname == null) {
            evaluationTypeClassname = this.getRequestParameter("evaluationTypeClassname");
            if (evaluationTypeClassname != null) {
                setEvaluationTypeClassname(evaluationTypeClassname);
            }
        }
        if (evaluationTypeClassname == null) {
            final IEvaluation evaluation = getEvaluation();
            if (evaluation != null) {
                evaluationTypeClassname = evaluation.getClass().getName();
                setEvaluationTypeClassname(evaluationTypeClassname);
            }
        }
        return evaluationTypeClassname;
    }

    public void setEvaluationTypeClassname(String evaluationTypeClassname) {
        this.getViewState().setAttribute("evaluationTypeClassname", evaluationTypeClassname);
    }

    public String getSeason() throws FenixFilterException, FenixServiceException {
        if (season == null) {
            final IEvaluation evaluation = getEvaluation();
            if (evaluation != null && evaluation instanceof IExam) {
                final IExam exam = (IExam) evaluation;
                season = exam.getSeason().toString();
            }
        } 
        if(this.getViewState().getAttribute("season") != null && !this.getViewState().getAttribute("season").equals("")) {
            this.season = (String) this.getViewState().getAttribute("season");
        }
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
        this.getViewState().setAttribute("season", season);
    }

    public String getOriginPage() {
        if (originPage == null) {
            originPage = getRequestParameter("originPage");
        }
        return originPage;
    }

    public void setOriginPage(String originPage) {
        this.originPage = originPage;
    }

}
