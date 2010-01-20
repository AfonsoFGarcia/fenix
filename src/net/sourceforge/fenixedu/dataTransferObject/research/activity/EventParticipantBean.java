package net.sourceforge.fenixedu.dataTransferObject.research.activity;

import java.io.Serializable;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.research.activity.ResearchEvent;
import net.sourceforge.fenixedu.domain.research.activity.Participation.ResearchActivityParticipationRole;

public class EventParticipantBean extends ParticipantBean implements Serializable {

    ResearchEvent event;

    public EventParticipantBean() {
	super();
	this.setEvent(null);
    }

    public ResearchEvent getEvent() {
	return this.event;
    }

    public void setEvent(ResearchEvent event) {
	this.event = event;
    }

    @Override
    public List<ResearchActivityParticipationRole> getAllowedRoles() {
	return ResearchActivityParticipationRole.getAllEventParticipationRoles();
    }

    @Override
    public DomainObject getActivity() {
	return getEvent();
    }
}
