package net.sourceforge.fenixedu.presentationTier.Action.publico.department;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.presentationTier.Action.publico.UnitSiteBoardsDA;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class DepartmentAnnouncementsDA extends DepartmentBoardsDA {

    @Override
    protected MultiLanguageString getBoardName(HttpServletRequest request) {
        return UnitSiteBoardsDA.ANNOUNCEMENTS;
    }

}
