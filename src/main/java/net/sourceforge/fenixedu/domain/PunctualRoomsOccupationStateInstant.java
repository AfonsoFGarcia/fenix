package net.sourceforge.fenixedu.domain;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.FenixFramework;

public class PunctualRoomsOccupationStateInstant extends PunctualRoomsOccupationStateInstant_Base {

    public static final Comparator<PunctualRoomsOccupationStateInstant> COMPARATOR_BY_INSTANT = new ComparatorChain();
    static {
        ((ComparatorChain) COMPARATOR_BY_INSTANT).addComparator(new BeanComparator("instant"));
        ((ComparatorChain) COMPARATOR_BY_INSTANT).addComparator(AbstractDomainObject.COMPARATOR_BY_ID);
    }

    @Checked("ResourceAllocationRolePredicates.checkPermissionsToManagePunctualRoomsOccupationStateInstants")
    public PunctualRoomsOccupationStateInstant(PunctualRoomsOccupationRequest request, RequestState state, DateTime instant) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setRequest(request);
        setRequestState(state);
        setInstant(instant);
    }

    @jvstm.cps.ConsistencyPredicate
    protected boolean checkRequiredParameters() {
        return getRequestState() != null && getInstant() != null;
    }

    @Override
    public void setRequest(PunctualRoomsOccupationRequest request) {
        if (request == null) {
            throw new DomainException("error.PunctualRoomsOccupationStateInstant.empty.request");
        }
        super.setRequest(request);
    }

    @Override
    public void setRequestState(RequestState state) {
        if (state == null) {
            throw new DomainException("error.PunctualRoomsOccupationStateInstant.empty.state");
        }
        super.setRequestState(state);
    }

    @Override
    public void setInstant(DateTime instant) {
        if (instant == null) {
            throw new DomainException("error.PunctualRoomsOccupationStateInstant.empty.instant");
        }
        super.setInstant(instant);
    }
}
