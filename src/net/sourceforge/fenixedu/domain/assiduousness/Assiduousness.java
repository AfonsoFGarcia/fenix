package net.sourceforge.fenixedu.domain.assiduousness;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.assiduousness.Clocking;
import net.sourceforge.fenixedu.domain.assiduousness.ClockingInterval;
import net.sourceforge.fenixedu.domain.assiduousness.util.AttributeType;
import net.sourceforge.fenixedu.domain.assiduousness.util.DomainConstants;
import net.sourceforge.fenixedu.domain.assiduousness.util.TimeInterval;
import net.sourceforge.fenixedu.domain.assiduousness.util.Timeline;
import net.sourceforge.fenixedu.domain.assiduousness.util.DateInterval;

import org.joda.time.Duration;
import org.joda.time.YearMonthDay;
import org.joda.time.Interval;
import org.joda.time.DateTime;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class Assiduousness extends Assiduousness_Base {
    
    public Assiduousness() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }
    
    public void calculateIntervalBalance(DateInterval interval) {
    		List<DailyBalance> dailyBalanceList = new ArrayList();
    		List<Schedule> scheduleList = getSchedulesWithInterval(interval);

    		// para cada dia do intervalo ver q horario esta definido nesse dia
    		for (YearMonthDay date = interval.getStartDate(); date.isBefore(interval.getEndDate()); date.plusDays(1)) {
    			// percorrer os schedules (tipicamente ha' 1 mas pode haver varios...
    			for (Schedule schedule: scheduleList) {
    				
    				WorkSchedule workSchedule = schedule.workScheduleWithDate(date);
    				DailyBalance dailyBalance = new DailyBalance(date);
    				if (workSchedule != null) { // nao ha horario neste dia
    					dailyBalance.setWorkSchedule(workSchedule);
    					List<Clocking> clockingList = clockingsWithDate(date);
    					List<Leave> leaveList = leavesWithDate(date);
    					if (clockingList.size() > 0) {
    						dailyBalance.setClockingList(clockingList);
    					}
    					if (leaveList.size() > 0) {
    						dailyBalance.setLeaveList(leaveList);
    					}
    					dailyBalance = calculateDailyBalance(date, workSchedule, clockingList, leaveList);
    					dailyBalanceList.add(dailyBalance);
    				} else { // nao ha horario neste dia => sabado ou domingo 
    					// neste caso devia ter info de DSC e DS?
    					// TODO ver esta situacao
    				}
    			}
    		}
    }
    
    
    // Returns a list with the schedules valid in a DateInterval
    public List<Schedule> getSchedulesWithInterval(DateInterval interval) {
		List<Schedule> scheduleList = new ArrayList();
		Iterator<Schedule> schedulesIt = getSchedulesIterator();
		while (schedulesIt.hasNext()) {
			Schedule schedule = schedulesIt.next();
			if (schedule.isDefinedInInterval(interval)) {
				scheduleList.add(schedule);
			}
		}
		return scheduleList;
    }
    
    // Returns a list of clockings made on date
    public List<Clocking> clockingsWithDate(YearMonthDay date) {
    		List<Clocking> clockingsList = new ArrayList<Clocking>();
    		Iterator<AssiduousnessRecord> itAssidRecord = getAssiduousnessRecordsIterator();
		while (itAssidRecord.hasNext()) {
			AssiduousnessRecord assidRecord = itAssidRecord.next();
			if (assidRecord instanceof Clocking) {
				Clocking clocking = (Clocking)assidRecord;
				if (clocking.getDate().toYearMonthDay().equals(date)) { // TODO and clocking state valid?!
					clockingsList.add(clocking);
				}
			}
		}
		return clockingsList;
    }
    
    // Returns a list of leaves valid on a date
    public List<Leave> leavesWithDate(YearMonthDay date) {
    		List<Leave> leavesList = new ArrayList<Leave>();
    		Iterator<AssiduousnessRecord> itAssidRecord = getAssiduousnessRecordsIterator();
		while (itAssidRecord.hasNext()) {
			AssiduousnessRecord assidRecord = itAssidRecord.next();
			if (assidRecord instanceof Leave) {
				Leave leave = (Leave)assidRecord;
				if (leave.occuredInDate(date)) {
					leavesList.add(leave);
				}
			}
		}
		return leavesList;
    }
    

    public DailyBalance calculateDailyBalance(YearMonthDay day, WorkSchedule workSchedule, List<Clocking> clockingList, List<Leave> leaveList) {
    		Timeline timeline = new Timeline();
    		workSchedule.getWorkScheduleType().plotInTimeline(timeline);
    		Iterator<AttributeType> attributesIt = DomainConstants.WORKED_ATTRIBUTES.getAttributes().iterator();
    		Clocking.plotListInTimeline(clockingList, attributesIt, timeline);
    		Leave.plotListInTimeline(leaveList,attributesIt,timeline);
    		timeline.print();
    		DailyBalance dailyBalance = workSchedule.calculateWorkingPeriods(day, clockingList, timeline);
    		return dailyBalance;
    }

    
//    // Get all clockings
//    public List<Clocking> allClockings() {
//		List<Clocking> clockingsList = new ArrayList<Clocking>();
//		Iterator<AssiduousnessRecord> itAssidRecord = this.getAssiduousnessRecordsIterator();
//		while (itAssidRecord.hasNext()) {
//			AssiduousnessRecord assidRecord = itAssidRecord.next();
//			if (assidRecord instanceof Clocking) {
//				clockingsList.add((Clocking)assidRecord);
//			}
//		}
//		return clockingsList;
//    }    

}
