package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import java.util.Collections;
import java.util.Iterator;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.GenericEvent;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.PunctualRoomsOccupationComment;
import net.sourceforge.fenixedu.domain.PunctualRoomsOccupationRequest;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.SystemSender;

import org.apache.struts.util.MessageResources;
import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class ClosePunctualRoomsOccupationRequest extends FenixService {

    @Checked("RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE")
    @Service
    public static void run(PunctualRoomsOccupationRequest request, Person person) {
	if (request != null) {
	    request.closeRequestAndAssociateOwnerOnlyForEmployees(new DateTime(), person);
	    sendCloseRequestMessage(request);
	}
    }

    private static void sendCloseRequestMessage(PunctualRoomsOccupationRequest roomsReserveRequest) {
	MessageResources messages = MessageResources.getMessageResources("resources/ResourceAllocationManagerResources");
	String body = messages.getMessage("message.room.reservation.solved") + "\n\n"
		+ messages.getMessage("message.room.reservation.request.number") + "\n" + roomsReserveRequest.getIdentification()
		+ "\n\n";
	body += messages.getMessage("message.room.reservation.request") + "\n";
	if (roomsReserveRequest.getSubject() != null) {
	    body += roomsReserveRequest.getSubject();
	} else {
	    body += "-";
	}
	body += "\n\n" + messages.getMessage("label.rooms.reserve.periods") + ":";
	for (GenericEvent genericEvent : roomsReserveRequest.getGenericEvents()) {
	    body += "\n\t" + genericEvent.getGanttDiagramEventPeriod() + genericEvent.getGanttDiagramEventObservations();
	}
	if (roomsReserveRequest.getGenericEvents().isEmpty()) {
	    body += "\n" + messages.getMessage("label.rooms.reserve.periods.none");
	}
	body += "\n\n" + messages.getMessage("message.room.reservation.description") + "\n";
	if (roomsReserveRequest.getDescription() != null) {
	    body += roomsReserveRequest.getDescription();
	} else {
	    body += "-";
	}
	PunctualRoomsOccupationComment punctualRoomsOccupationComment = getLastComment(roomsReserveRequest);
	
	body += "\n\n" + messages.getMessage("message.room.reservation.last.comment") + "\n";

	if (punctualRoomsOccupationComment != null){
	    body += punctualRoomsOccupationComment.getDescription();
	}else{
	    body += "-";
	}
	
	sendMessage(roomsReserveRequest.getRequestor().getDefaultEmailAddressValue(),
		messages.getMessage("message.room.reservation"), body);
    }

    private static PunctualRoomsOccupationComment getLastComment(PunctualRoomsOccupationRequest roomsReserveRequest) {
	PunctualRoomsOccupationComment last = null;
	for (Iterator<PunctualRoomsOccupationComment> iterator = roomsReserveRequest.getCommentsWithoutFirstCommentOrderByDate()
		.iterator(); iterator.hasNext();) {
	    last = iterator.next();
	}
	return last;
    }

    @Service
    private static void sendMessage(String email, String subject, String body) {
	SystemSender systemSender = RootDomainObject.getInstance().getSystemSender();
	if (email != null) {
	    new Message(systemSender, systemSender.getConcreteReplyTos(), Collections.EMPTY_LIST, subject, body, email);
	}
    }
}