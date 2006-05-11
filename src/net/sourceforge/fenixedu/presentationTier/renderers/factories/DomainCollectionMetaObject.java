package net.sourceforge.fenixedu.presentationTier.renderers.factories;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.renderers.model.SimpleMetaObjectCollection;

public class DomainCollectionMetaObject extends SimpleMetaObjectCollection {

    @Override
    public void commit() {
	try {
	    ServiceUtils.executeService(getUserView(), "CommitMetaObjects",
		    new Object[] { getAllMetaObjects() });
	} catch (Exception e) {
	    if (e instanceof DomainException) {
		throw (DomainException) e;
	    } else {
		throw new DomainException("domain.metaobject.service.failed", e);
	    }
	}
    }

    protected IUserView getUserView() {
	return ((FenixUserIdentity) getUser()).getUserView();
    }
}
