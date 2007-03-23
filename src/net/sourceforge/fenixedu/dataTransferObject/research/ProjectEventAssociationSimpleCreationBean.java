package net.sourceforge.fenixedu.dataTransferObject.research;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.research.activity.EventEdition;
import net.sourceforge.fenixedu.domain.research.project.ProjectEventAssociation.ProjectEventAssociationRole;

public class ProjectEventAssociationSimpleCreationBean implements Serializable {
  
    private DomainReference<EventEdition> event;
    private ProjectEventAssociationRole role;
    private String eventName;

    public ProjectEventAssociationRole getRole() {
        return role;
    }

    public void setRole(ProjectEventAssociationRole associationRole) {
        this.role = associationRole;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String name) {
        this.eventName = name;
    }

    public EventEdition getEvent() {
        return (this.event == null) ? null : this.event.getObject();
    }

    public void setEvent(EventEdition event) {
        this.event = (event != null) ? new DomainReference<EventEdition>(event) : null;
    }

}
