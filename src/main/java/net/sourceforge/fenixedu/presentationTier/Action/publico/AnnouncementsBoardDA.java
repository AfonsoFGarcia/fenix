package net.sourceforge.fenixedu.presentationTier.Action.publico;

import javax.servlet.http.HttpServletRequest;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class AnnouncementsBoardDA extends UnitSiteBoardsDA {

    @Override
    protected MultiLanguageString getBoardName(HttpServletRequest request) {
        return UnitSiteBoardsDA.ANNOUNCEMENTS;
    }

}
