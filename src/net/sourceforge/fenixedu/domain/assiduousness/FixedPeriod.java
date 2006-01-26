/*
 * Created on Mar 24, 2005
 */
package net.sourceforge.fenixedu.domain.assiduousness;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.assiduousness.util.AttributeType;
import net.sourceforge.fenixedu.domain.assiduousness.util.TimeInterval;
import net.sourceforge.fenixedu.domain.assiduousness.util.TimePoint;

import org.joda.time.Duration;
import org.joda.time.Interval;

/**
 * @author velouria
 *
 */

// TODO test all classes
public class FixedPeriod extends FixedPeriod_Base {


    public FixedPeriod(TimeInterval fixedPeriod1, TimeInterval fixedPeriod2) {
        setFixedPeriod1(fixedPeriod1);
        setFixedPeriod2(fixedPeriod2);
    }

    public FixedPeriod(TimeInterval fixedPeriod1) {
        setFixedPeriod1(fixedPeriod1);
        setFixedPeriod2(null);
    }
    
    public boolean definedFixedPeriod2() {
        return (getFixedPeriod2() != null);
    }
    
    public boolean definedFixedPeriod1() {
        return (getFixedPeriod1() != null);
    }
  
    // TODO verificar ISTO!
    public Duration getFixedPeriod1Duration() {
        if (this.definedFixedPeriod1()) {
            return new Duration(getFixedPeriod1().getDuration());
        } else {
            return Duration.ZERO;
        }
    }
    
    public Duration getFixedPeriod2Duration() {
        if (this.definedFixedPeriod2()) {
            return new Duration(getFixedPeriod2().getDuration());
        } else {
            return Duration.ZERO;
        }
    }
    
    public Duration getTotalFixedPeriodDuration() {
        if (getFixedPeriod2() != null) {
            return (this.getFixedPeriod1Duration()).plus(this.getFixedPeriod2Duration());
        } else {
            return getFixedPeriod1Duration();
        }
    }

    // Returns a list with the start and end points of both Fixed Periods if defined
    public List<TimePoint> toTimePoints() {
        List<TimePoint> pointList = new ArrayList();
        if (this.definedFixedPeriod1()) {
            pointList.add(this.getFixedPeriod1().startPointToTimePoint(AttributeType.FP1));
            pointList.add(this.getFixedPeriod1().endPointToTimePoint(AttributeType.FP1));
        }
        if (this.definedFixedPeriod2()) {
            pointList.add(this.getFixedPeriod2().startPointToTimePoint(AttributeType.FP2));
            pointList.add(this.getFixedPeriod2().endPointToTimePoint(AttributeType.FP2));
        }
        return pointList;
    }
    
    
    
//    // Sums the work done during the whole Fixed Platform (the employee's work schedule may have 2 fixed periods)
//    public Duration countFixedPeriodTotalWorkTime(Clocking clockingIn, Clocking clockingOut) {
//        Duration worked = new Duration(0);
//        if (this.definedFixedPeriod1()) {
//            worked = TimeInterval.countDurationFromClockings(clockingIn, clockingOut, this.getFixedPeriod1());
//        }
//        if (this.definedFixedPeriod2()) {
//            worked = worked.plus(TimeInterval.countDurationFromClockings(clockingIn, clockingOut, this.getFixedPeriod2()));
//        }
//        return worked;
//    }    
        
}
