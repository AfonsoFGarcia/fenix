package net.sourceforge.fenixedu.applicationTier.Servico;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.support.SupportDoubt;
import net.sourceforge.fenixedu.domain.support.SupportError;
import net.sourceforge.fenixedu.domain.support.SupportException;
import net.sourceforge.fenixedu.domain.support.SupportRequest;
import net.sourceforge.fenixedu.domain.support.SupportRequestPriority;
import net.sourceforge.fenixedu.domain.support.SupportRequestType;
import net.sourceforge.fenixedu.domain.support.SupportSugestion;

public class SupportRequestFactory {

    public static SupportRequest createSupportRequest(SupportRequestType requestType, Content context,
	    SupportRequestPriority priority, Person person, String email, String subject, String message) {

	switch (requestType) {
	case DOUBT:
	    return new SupportDoubt(context, priority, person, email, subject, message);
	case ERROR:
	    return new SupportError(context, priority, person, email, subject, message);
	case EXCEPTION:
	    // FIX ME null: get ErrorLog when merging this with ErrorLog feature
	    return new SupportException(context, priority, person, email, subject, message, null);
	    // FIX ME null: get ErrorLog when merging this with ErrorLog feature
	case SUGESTION:
	    return new SupportSugestion(context, priority, person, email, subject, message);
	default:
	    throw new DomainException("error.domain.support.SupportRequest.invalid.type");
	}
    }

}
