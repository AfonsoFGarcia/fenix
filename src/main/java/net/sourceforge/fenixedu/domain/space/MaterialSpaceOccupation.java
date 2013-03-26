package net.sourceforge.fenixedu.domain.space;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.material.Material;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.pstm.AbstractDomainObject;

public abstract class MaterialSpaceOccupation extends MaterialSpaceOccupation_Base {

    public final static Comparator<MaterialSpaceOccupation> COMPARATOR_BY_CLASS_NAME = new ComparatorChain();
    static {
        ((ComparatorChain) COMPARATOR_BY_CLASS_NAME).addComparator(new BeanComparator("class.simpleName"));
        ((ComparatorChain) COMPARATOR_BY_CLASS_NAME).addComparator(new BeanComparator("begin"));
        ((ComparatorChain) COMPARATOR_BY_CLASS_NAME).addComparator(AbstractDomainObject.COMPARATOR_BY_ID);
    }

    protected MaterialSpaceOccupation() {
        super();
    }

    @Override
    public void delete() {
        super.setMaterial(null);
        super.delete();
    }

    @Override
    public void setMaterial(Material material) {
        if (material == null) {
            throw new DomainException("error.Material.empty.material");
        }
        super.setMaterial(material);
    }

    public boolean isActive(YearMonthDay currentDate) {
        return (!getBegin().isAfter(currentDate) && (getEnd() == null || !getEnd().isBefore(currentDate)));
    }

    @jvstm.cps.ConsistencyPredicate
    protected boolean checkDateInterval() {
        final YearMonthDay start = getBegin();
        final YearMonthDay end = getEnd();
        return start != null && (end == null || !start.isAfter(end));
    }

    @Override
    public boolean isMaterialSpaceOccupation() {
        return true;
    }
}
