package net.sourceforge.fenixedu.presentationTier.Action.webSiteManager;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

public class ScientificCouncilAnnouncementsDA extends UnitSiteAnnouncementManagement {

    @Override
    protected String getContextInformation(ActionMapping mapping, HttpServletRequest request) {
	return "/manageScientificCouncilAnnouncements.do";
    }

}
