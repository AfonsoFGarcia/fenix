package net.sourceforge.fenixedu.domain.space;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.FrequencyType;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.Checked;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.HourMinuteSecond;

import org.joda.time.Interval;
import org.joda.time.YearMonthDay;

public class WrittenEvaluationSpaceOccupation extends WrittenEvaluationSpaceOccupation_Base {
      
    @Checked("SpacePredicates.checkPermissionsToManageSpaceOccupationsWithoutCheckSpaceManagerRole")
    public WrittenEvaluationSpaceOccupation(AllocatableSpace allocatableSpace) {	
        
	super();		
	
	if(allocatableSpace != null && !allocatableSpace.isFree(this)) {		
	    throw new DomainException("error.roomOccupied", getRoom().getName());
	}

        setResource(allocatableSpace);                		       		  
    }    
    
    @Checked("SpacePredicates.checkPermissionsToManageSpaceOccupationsWithoutCheckSpaceManagerRole")
    public void edit() {	
	if(!getRoom().isFree(this)) {		
	    throw new DomainException("error.roomOccupied", getRoom().getName());
	}	
    }
          
    @Checked("SpacePredicates.checkPermissionsToManageSpaceOccupationsWithoutCheckSpaceManagerRole")
    public void delete() {
	if(canBeDeleted()) {	    	    
	    super.delete();
	}
    }
    
    public boolean canBeDeleted() {
	return !hasAnyWrittenEvaluations();
    }
    
    @Override
    public List<Interval> getEventSpaceOccupationIntervals() {
	List<Interval> result = new ArrayList<Interval>();
	List<WrittenEvaluation> writtenEvaluations = getWrittenEvaluations();
	for (WrittenEvaluation writtenEvaluation : writtenEvaluations) {	    
	    result.add(createNewInterval(writtenEvaluation.getDayDateYearMonthDay(), writtenEvaluation.getDayDateYearMonthDay(),
		    writtenEvaluation.getBeginningDateHourMinuteSecond(), writtenEvaluation.getEndDateHourMinuteSecond()));
	}
	return result;
    }
        
    @Override
    public boolean isWrittenEvaluationSpaceOccupation() {
	return true;
    }
    
    @Override
    protected boolean intersects(YearMonthDay startDate, YearMonthDay endDate) {
	return true;
    }
             
    @Override
    public Group getAccessGroup() {
	return getSpace().getWrittenEvaluationOccupationsAccessGroupWithChainOfResponsibility();
    }
        
    @Override
    public YearMonthDay getBeginDate() {
	return null;
    }

    @Override
    public YearMonthDay getEndDate() {
	return null;
    }
    
    @Override
    public HourMinuteSecond getStartTimeDateHourMinuteSecond() {
	return null;
    }
       
    @Override
    public HourMinuteSecond getEndTimeDateHourMinuteSecond() {
	return null;
    }

    @Override
    public Boolean getDailyFrequencyMarkSaturday() {
	return null;
    }

    @Override
    public Boolean getDailyFrequencyMarkSunday() {
	return null;
    }

    @Override
    public DiaSemana getDayOfWeek() {
	return null;
    }    
    
    @Override
    public FrequencyType getFrequency() {
	return null;
    }
}
