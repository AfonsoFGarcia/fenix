package net.sourceforge.fenixedu.dataTransferObject;

import java.io.Serializable;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.teacher.executionCourse.NextPossibleSummaryLessonsAndDatesBean;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.LessonPlanning;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.Summary;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.apache.commons.lang.StringUtils;
import org.joda.time.Partial;
import org.joda.time.YearMonthDay;

public class SummariesManagementBean implements Serializable {

    private DomainReference<ExecutionCourse> executionCourseReference;
    
    private SummaryType summaryType;

    private DomainReference<Lesson> lessonReference;

    private DomainReference<Shift> shiftReference;

    private YearMonthDay summaryDate;

    private Partial summaryTime;

    private DomainReference<Professorship> professorshipReference;

    private DomainReference<Teacher> teacherReference;

    private String teacherName;
    
    private DomainReference<AllocatableSpace> roomReference;

    private Integer studentsNumber;
    
    private MultiLanguageString title;

    private MultiLanguageString summaryText;
    
    private DomainReference<LessonPlanning> lessonPlannigReference;
    
    private DomainReference<Summary> lastSummaryReference;
    
    private DomainReference<Summary> summaryReference;
    
    private DomainReference<Professorship> professorshipLoggedReference;
    
    private List<NextPossibleSummaryLessonsAndDatesBean> nextPossibleSummaryLessonsAndDatesBean;
    

    protected SummariesManagementBean() {
	
    }
    
    public SummariesManagementBean(SummaryType summaryType, ExecutionCourse executionCourse, Professorship professorship, List<NextPossibleSummaryLessonsAndDatesBean> nextPossibleSummaryLessonsAndDatesBean) {        
        setSummaryType(summaryType);
        setExecutionCourse(executionCourse);
        setProfessorship(professorship);                    
        setProfessorshipLogged(professorship);
        setNextPossibleSummaryLessonsAndDatesBean(nextPossibleSummaryLessonsAndDatesBean);
    }
    
    public SummariesManagementBean(MultiLanguageString title, MultiLanguageString summaryText, Integer studentsNumber, SummaryType summaryType,
            Professorship professorship, String teacherName, Teacher teacher, Shift shift,
            Lesson lesson, YearMonthDay summaryDate, AllocatableSpace summaryRoom, Partial summaryTime, Summary summary,
            Professorship professorshipLogged) {
        
        setTitle(title);
        setSummaryText(summaryText);
        setSummaryType(summaryType);
        setShift(shift);
        setLesson(lesson);
        setProfessorship(professorship);
        setTeacher(teacher);
        setTeacherName(teacherName);
        setSummary(summary);
        setSummaryDate(summaryDate);
        setSummaryRoom(summaryRoom);  
        setSummaryTime(summaryTime);
        setStudentsNumber(studentsNumber);
        setExecutionCourse(shift.getDisciplinaExecucao());
        setProfessorshipLogged(professorshipLogged);
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public Integer getStudentsNumber() {
        return studentsNumber;
    }

    public void setStudentsNumber(Integer studentsNumber) {
        this.studentsNumber = studentsNumber;
    }
    
    public Summary getSummary() {
        return (this.summaryReference != null) ? this.summaryReference.getObject() : null;
    }

    public void setSummary(Summary summary) {
        this.summaryReference = (summary != null) ? new DomainReference<Summary>(
                summary) : null;
    }

    public ExecutionCourse getExecutionCourse() {
        return (this.executionCourseReference != null) ? this.executionCourseReference.getObject() : null;
    }

    public void setExecutionCourse(ExecutionCourse executionCourse) {
        this.executionCourseReference = (executionCourse != null) ? new DomainReference<ExecutionCourse>(
                executionCourse) : null;
    }
    
    public Teacher getTeacher() {
        return (this.teacherReference != null) ? this.teacherReference.getObject() : null;
    }

    public void setTeacher(Teacher teacher) {
        this.teacherReference = (teacher != null) ? new DomainReference<Teacher>(
                teacher) : null;
    }
    
    public Professorship getProfessorshipLogged() {
        return (this.professorshipLoggedReference != null) ? this.professorshipLoggedReference.getObject() : null;
    }

    public void setProfessorshipLogged(Professorship professorship) {
        this.professorshipLoggedReference = (professorship != null) ? new DomainReference<Professorship>(
        	professorship) : null;
    }
    
    public Summary getLastSummary() {
        return (this.lastSummaryReference != null) ? this.lastSummaryReference.getObject() : null;
    }

    public void setLastSummary(Summary summary) {
        this.lastSummaryReference = (summary != null) ? new DomainReference<Summary>(
                summary) : null;
    }
    
    public LessonPlanning getLessonPlanning() {
        return (this.lessonPlannigReference != null) ? this.lessonPlannigReference.getObject() : null;
    }

    public void setLessonPlanning(LessonPlanning lessonPlanning) {
        this.lessonPlannigReference = (lessonPlanning != null) ? new DomainReference<LessonPlanning>(
                lessonPlanning) : null;
    }

    public Professorship getProfessorship() {
        return (this.professorshipReference != null) ? this.professorshipReference.getObject() : null;
    }

    public void setProfessorship(Professorship professorship) {
        this.professorshipReference = (professorship != null) ? new DomainReference<Professorship>(
                professorship) : null;
    }

    public Lesson getLesson() {
        return (this.lessonReference != null) ? this.lessonReference.getObject() : null;
    }

    public void setLesson(Lesson lesson) {
        this.lessonReference = (lesson != null) ? new DomainReference<Lesson>(lesson) : null;
    }

    public Shift getShift() {
        return (this.shiftReference != null) ? this.shiftReference.getObject() : null;
    }

    public void setShift(Shift shift) {
        this.shiftReference = (shift != null) ? new DomainReference<Shift>(shift) : null;
    }

    public AllocatableSpace getSummaryRoom() {
        return (this.roomReference != null) ? this.roomReference.getObject() : null;
    }

    public void setSummaryRoom(AllocatableSpace room) {
        this.roomReference = (room != null) ? new DomainReference<AllocatableSpace>(room) : null;
    }
    
    public SummaryType getSummaryType() {
        return summaryType;
    }

    public void setSummaryType(SummaryType summaryType) {
        this.summaryType = summaryType;
    }

    public YearMonthDay getSummaryDate() {
        return summaryDate;
    }

    public void setSummaryDate(YearMonthDay date) {
        this.summaryDate = date;
    }

    public MultiLanguageString getSummaryText() {
        return summaryText;
    }

    public void setSummaryText(MultiLanguageString summary) {
        this.summaryText = summary;
    }

    public MultiLanguageString getTitle() {
        return title;
    }

    public void setTitle(MultiLanguageString title) {
        this.title = title;
    }

    public Partial getSummaryTime() {
        return summaryTime;
    }

    public void setSummaryTime(Partial hour) {
        this.summaryTime = hour;
    }           
           
    public String getTeacherChoose() {
        if (getProfessorship() != null) {
           return getProfessorship().getIdInternal().toString();
        } else if (!StringUtils.isEmpty(getTeacherName())) {
           return "-1";
        } else if (getTeacher() != null) {
           return "0";
        }  
        return "";
    }

    public static enum SummaryType {
        NORMAL_SUMMARY, EXTRA_SUMMARY;
        public String getName() {
            return name();
        }
    }

    public boolean isNewSummary() {
        return getSummary() == null;
    }

    public List<NextPossibleSummaryLessonsAndDatesBean> getNextPossibleSummaryLessonsAndDatesBean() {
        return nextPossibleSummaryLessonsAndDatesBean;
    }

    public void setNextPossibleSummaryLessonsAndDatesBean(
    	List<NextPossibleSummaryLessonsAndDatesBean> nextPossibleSummaryLessonsAndDatesBean) {
        this.nextPossibleSummaryLessonsAndDatesBean = nextPossibleSummaryLessonsAndDatesBean;
    }
}
