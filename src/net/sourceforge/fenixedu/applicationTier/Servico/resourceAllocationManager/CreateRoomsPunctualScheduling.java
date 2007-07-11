package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager.RoomsPunctualSchedulingBean;
import net.sourceforge.fenixedu.domain.GenericEvent;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.util.HourMinuteSecond;

import org.joda.time.DateTimeFieldType;

public class CreateRoomsPunctualScheduling extends Service {

    public void run(RoomsPunctualSchedulingBean bean) throws FenixServiceException {
	
	List<AllocatableSpace> selectedRooms = bean.getRooms();	
	if (!selectedRooms.isEmpty()) {
	    
	    HourMinuteSecond beginTime = new HourMinuteSecond(bean.getBeginTime().get(DateTimeFieldType.hourOfDay()), bean.getBeginTime().get(DateTimeFieldType.minuteOfHour()), 0);	    
	    HourMinuteSecond endTime = new HourMinuteSecond(bean.getEndTime().get(DateTimeFieldType.hourOfDay()), bean.getEndTime().get(DateTimeFieldType.minuteOfHour()), 0);	    
	    
	    new GenericEvent(bean.getSmallDescription(), bean.getCompleteDescription(), selectedRooms, bean.getBegin(),
		    bean.getEnd(), beginTime, endTime, bean.getFrequency(), bean.getRoomsReserveRequest(), bean.getMarkSaturday(),
		    bean.getMarkSunday());	    	             	  			    	  
	}
    }    
}
