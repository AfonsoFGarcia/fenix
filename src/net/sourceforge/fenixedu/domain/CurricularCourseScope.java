package net.sourceforge.fenixedu.domain;

import java.util.Calendar;
import java.util.Date;

import net.sourceforge.fenixedu.dataTransferObject.comparators.CalendarDateComparator;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.DateFormatUtil;



public class CurricularCourseScope extends CurricularCourseScope_Base {

	public CurricularCourseScope() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}
	
	public CurricularCourseScope(Branch branch, CurricularCourse curricularCourse, CurricularSemester curricularSemester,
								 Calendar beginDate, Calendar endDate, String Annotation){
		this();
        // check that there isn't another scope active with the same curricular course, branch and semester
		
        if (curricularCourse.hasActiveScopeInGivenSemesterForGivenBranch(curricularSemester, branch)) {
            throw new DomainException("error.curricular.course.scope.conflict.creation");
        }
		
		setBranch(branch);
		setCurricularCourse(curricularCourse);
		setCurricularSemester(curricularSemester);
		
		setBeginDate(beginDate);
		setEndDate(endDate);
		setAnotation(Annotation);
	}
	
	
    /**
     * @return Returns the beginDate.
     */
    public Calendar getBeginDate() {
        if (this.getBegin() != null) {
            Calendar result = Calendar.getInstance();
            result.setTime(this.getBegin());
            return result;
        }
        return null;
    }

    /**
     * @param beginDate
     *            The beginDate to set.
     */
    public void setBeginDate(Calendar beginDate) {
        if (beginDate != null) {
            this.setBegin(beginDate.getTime());
        } else {
            this.setBegin(null);
        }
    }

    /**
     * @return Returns the endDate.
     */
    public Calendar getEndDate() {
        if (this.getEnd() != null) {
            Calendar result = Calendar.getInstance();
            result.setTime(this.getEnd());
            return result;
        }
        return null;
    }

    /**
     * @param endDate
     *            The endDate to set.
     */
    public void setEndDate(Calendar endDate) {
        if (endDate != null) {
            this.setEnd(endDate.getTime());
        } else {
            this.setEnd(null);
        }
    }

    public Boolean isActive() {
        return isActive(new Date());
    }
    
    public boolean getActive (){
    	return this.isActive();
    }
	
	
	public Boolean canBeDeleted() {
		return !hasAnyAssociatedWrittenEvaluations();
	}
	
	
	public void edit(Branch branch, CurricularSemester curricularSemester,
			 Calendar beginDate, Calendar endDate, String Annotation) {
   	
		setBranch(branch);
		setCurricularSemester(curricularSemester);
		setBeginDate(beginDate);
		setEndDate(endDate);
		setAnotation(Annotation);
	}
	
	public void end(Calendar endDate) {
		setEndDate(endDate);
	}	
	
	public void delete() throws DomainException {

	    if (canBeDeleted()) {
			removeCurricularSemester();
			removeCurricularCourse();
			removeBranch();
			
			removeRootDomainObject();
        super.deleteDomainObject();
        } else {
            throw new DomainException("error.curricular.course.scope.has.written.evaluations");
        }
	}

    public Boolean isActive(Date date) {
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(date);
    	CalendarDateComparator calendarDateComparator = new CalendarDateComparator();
    	Boolean result = Boolean.FALSE;
    	if(calendarDateComparator.compare(getBeginDate(), calendar) <= 0) {
    		if(getEnd() == null || calendarDateComparator.compare(getEndDate(), calendar) >= 0) {
    			result = Boolean.TRUE;
			}
    	}
    	return result;
    }

    public boolean intersects(final Date begin, final Date end) {
        return DateFormatUtil.compareDates("yyyyMMdd", getBegin(), end) < 0 &&
                (getEnd() == null || DateFormatUtil.compareDates("yyyyMMdd", getEnd(), begin) > 0);
    }

}
