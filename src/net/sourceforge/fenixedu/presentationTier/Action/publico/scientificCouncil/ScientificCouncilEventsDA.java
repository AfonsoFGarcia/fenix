package net.sourceforge.fenixedu.presentationTier.Action.publico.scientificCouncil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.publico.UnitSiteBoardsDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ScientificCouncilEventsDA extends UnitSiteBoardsDA {

    @Override
    protected String getBoardName(HttpServletRequest request) {
        return UnitSiteBoardsDA.EVENTS;
    }

    public ActionForward viewEvents(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	return viewAnnouncements(mapping, form, request, response);
    }

	@Override
	protected String getActionPath(HttpServletRequest request) {
		return "/scientificCouncil/events.do";
	}

}
