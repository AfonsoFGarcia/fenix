package net.sourceforge.fenixedu.applicationTier.Servico.space;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.spaceManager.MoveSpaceBean;
import net.sourceforge.fenixedu.domain.space.Space;

public class MoveSpace extends FenixService {

    public void run(MoveSpaceBean bean) {
	if (bean != null && bean.getSpace() != null) {
	    Space space = bean.getSpace();
	    space.setNewPossibleParentSpace(bean.getSelectedParentSpace());
	}
    }
}
