package net.sourceforge.fenixedu.dataTransferObject.research.result;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.research.event.Event;
import net.sourceforge.fenixedu.domain.research.event.EventType;
import net.sourceforge.fenixedu.domain.research.result.Result;
import net.sourceforge.fenixedu.domain.research.result.ResultEventAssociation.ResultEventAssociationRole;
import net.sourceforge.fenixedu.util.MultiLanguageString;

public class ResultEventAssociationCreationBean implements Serializable {
    private DomainReference<Result> result;
    private DomainReference<Event> event;
    private ResultEventAssociationRole role;
    private MultiLanguageString eventNameMLS;
    private String eventNameStr;
    private EventType eventType;

    public ResultEventAssociationCreationBean(Result resultReference) {
        this.result = new DomainReference<Result>(resultReference);
        this.role = ResultEventAssociationRole.getDefaultEventRoleType();
        this.event = null;
        this.eventType = EventType.getDefaultType();
        this.eventNameMLS = null;
        this.eventNameStr = null;
    }
    
    public ResultEventAssociationRole getRole() {
        return this.role;
    }

    public void setRole(ResultEventAssociationRole associationRole) {
        this.role = associationRole;
    }
    
    public MultiLanguageString getEventNameMLS() {
        return eventNameMLS;
    }

    public void setEventNameMLS(MultiLanguageString name) {
        this.eventNameMLS = name;
    }

    public Event getEvent() {
        return (this.event == null) ? null : this.event.getObject();
    }

    public void setEvent(Event event) {
        this.event = (event != null) ? new DomainReference<Event>(event) : null;
    }
    
    public Result getResult() {
        return (this.result == null) ? null : this.result.getObject();
    }

    public void setResult(Result result) {
        this.result = (result != null) ? new DomainReference<Result>(result) : null;
    }
    
    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public String getEventNameStr() {
        return eventNameStr;
    }

    public void setEventNameStr(String eventNameStr) {
        this.eventNameStr = eventNameStr;
    }
}
