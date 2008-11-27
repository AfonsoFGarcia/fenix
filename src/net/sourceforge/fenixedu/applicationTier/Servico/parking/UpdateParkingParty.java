package net.sourceforge.fenixedu.applicationTier.Servico.parking;

import pt.ist.fenixWebFramework.services.Service;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.parking.ParkingGroup;
import net.sourceforge.fenixedu.domain.parking.ParkingParty;
import net.sourceforge.fenixedu.domain.parking.ParkingPartyHistory;
import net.sourceforge.fenixedu.domain.parking.ParkingRequest;
import net.sourceforge.fenixedu.domain.parking.ParkingRequestState;
import net.sourceforge.fenixedu.domain.util.Email;

import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public class UpdateParkingParty extends FenixService {

    @Checked("RolePredicates.PARKING_MANAGER_PREDICATE")
    @Service
    public static void run(ParkingRequest parkingRequest, final ParkingRequestState parkingRequestState, final Long cardCode,
	    final ParkingGroup parkingGroup, final String note, final DateTime cardStartDate, final DateTime cardEndDate,
	    final Integer number) {

	ParkingParty parkingParty = parkingRequest.getParkingParty();

	if (parkingRequestState == ParkingRequestState.ACCEPTED) {
	    if (parkingParty.getCardNumber() != null
		    && (changedObject(parkingParty.getCardStartDate(), cardStartDate)
			    || changedObject(parkingParty.getCardEndDate(), cardEndDate)
			    || changedObject(parkingParty.getCardNumber(), cardCode) || changedObject(parkingParty
			    .getParkingGroup(), parkingGroup))) {
		new ParkingPartyHistory(parkingParty, true);
	    }
	    parkingParty.setCardStartDate(cardStartDate);
	    parkingParty.setCardEndDate(cardEndDate);
	    parkingParty.setAuthorized(true);
	    parkingParty.setCardNumber(cardCode);
	    parkingParty.setUsedNumber(number);
	    parkingParty.edit(parkingRequest);
	    parkingParty.setParkingGroup(parkingGroup);
	}
	parkingParty.setRequestedAs(parkingRequest.getRequestedAs());
	parkingRequest.setParkingRequestState(parkingRequestState);
	parkingRequest.setNote(note);

	String email = parkingParty.getParty().getDefaultEmailAddress().getValue();

	if (note != null && note.trim().length() != 0 && email != null) {
	    ResourceBundle bundle = ResourceBundle.getBundle("resources.ParkingResources", Language.getLocale());
	    List<String> to = new ArrayList<String>();
	    to.add(email);
	    new Email(bundle.getString("label.fromName"), bundle.getString("label.fromAddress"), null, to, null, null, bundle
		    .getString("label.subject"), note);
	}
    }

    private static boolean changedObject(Object oldObject, Object newObject) {
	return oldObject == null && newObject == null ? false : (oldObject != null && newObject != null ? (!oldObject
		.equals(newObject)) : true);
    }
}