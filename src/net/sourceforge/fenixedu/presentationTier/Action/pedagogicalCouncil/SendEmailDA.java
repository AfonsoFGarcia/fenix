package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.presentationTier.Action.cms.messaging.mailSender.UnitMailSenderAction;

public class SendEmailDA extends UnitMailSenderAction {

    private final static String fromName = "Conselho Pedag�gico";

    @Override
    protected String getFromName(HttpServletRequest request) {
	return fromName;
    }

}
