package net.sourceforge.fenixedu.dataTransferObject;

import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.GenericEvent;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.util.DiaSemana;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import net.sourceforge.fenixedu.util.MultiLanguageString;
import net.sourceforge.fenixedu.util.renderer.GanttDiagramEvent;

import org.joda.time.Interval;

public class InfoGenericEvent extends InfoShowOccupation implements GanttDiagramEvent {
       
    private Calendar beginTime;
    
    private Calendar endTime;
    
    private int diaSemana;
    
    private DomainReference<GenericEvent> genericEventReference;

    public InfoGenericEvent(GenericEvent genericEvent) {
	genericEventReference = new DomainReference<GenericEvent>(genericEvent);
    }
    
    public InfoGenericEvent(GenericEvent genericEvent, int diaSemana_) {
	genericEventReference = new DomainReference<GenericEvent>(genericEvent);
	diaSemana = diaSemana_;
	beginTime = genericEvent.getBeginTimeCalendar();
	endTime = genericEvent.getEndTimeCalendar();
    }
    
    public InfoGenericEvent(GenericEvent genericEvent, int diaSemana_, Calendar beginTime_, Calendar endTime_) {
	genericEventReference = new DomainReference<GenericEvent>(genericEvent);
	diaSemana = diaSemana_;
	beginTime = beginTime_;
	endTime = endTime_;
    }
 
    public GenericEvent getGenericEvent() {
        return genericEventReference == null ? null : genericEventReference.getObject();
    }
    
    public Integer getIdInternal() {
	return getGenericEvent().getIdInternal();
    }
    
    public String getTitle() {
	return getGenericEvent().getTitle().getContent(Language.getLanguage());
    }

    public String getDescription() {
	return getGenericEvent().getDescription().getContent(Language.getLanguage());
    }
    
    @Override
    public DiaSemana getDiaSemana() {
	return new DiaSemana(diaSemana);
    }

    @Override
    public Calendar getFim() {	
	return endTime;
    }

    @Override
    public Calendar getInicio() {
	return beginTime;
    }
       
    @Override
    public InfoRoomOccupation getInfoRoomOccupation() {
	return (getGenericEvent().getGenericEventSpaceOccupations().isEmpty()) ? null : 
	    InfoRoomOccupation.newInfoFromDomain(getGenericEvent().getGenericEventSpaceOccupations().get(0));	
    }

    @Override
    public InfoShift getInfoShift() {
	return null;
    }

    @Override
    public ShiftType getTipo() {
	return null;
    }

    public String getGanttDiagramEventIdentifier() {
	return getGenericEvent().getPunctualRoomsOccupationRequest().getIdInternal().toString();
    }

    public MultiLanguageString getGanttDiagramEventName() {
	return getGenericEvent().getPunctualRoomsOccupationRequest().getFirstComment().getSubject();
    }

    public String getGanttDiagramEventObservations() {
	return getGenericEvent().getGanttDiagramEventObservations();
    }

    public int getGanttDiagramEventOffset() {
	return 0;
    }

    public String getGanttDiagramEventPeriod() {
	return getGenericEvent().getGanttDiagramEventPeriod();
    }

    public List<Interval> getGanttDiagramEventSortedIntervals() {
	return getGenericEvent().getGanttDiagramEventSortedIntervals();
    }   
}
