package net.sourceforge.fenixedu.domain.material;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.MaterialSpaceOccupation;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.joda.time.YearMonthDay;

public abstract class Material extends Material_Base {

    public final static Comparator<Material> COMPARATOR_BY_CLASS_NAME = new ComparatorChain();
    static {
	((ComparatorChain) COMPARATOR_BY_CLASS_NAME).addComparator(new BeanComparator("class.simpleName"));
	((ComparatorChain) COMPARATOR_BY_CLASS_NAME).addComparator(new BeanComparator("acquisition"));
	((ComparatorChain) COMPARATOR_BY_CLASS_NAME).addComparator(DomainObject.COMPARATOR_BY_ID);
    }

    public abstract String getMaterialSpaceOccupationSlotName();

    public abstract Class<? extends MaterialSpaceOccupation> getMaterialSpaceOccupationSubClass();

    public abstract String getPresentationDetails();

    protected Material() {
	super();	
    }
    
    @Override
    public void setAcquisition(YearMonthDay acquisition) {
	if (acquisition == null) {
	    throw new DomainException("error.material.no.acquisitionDate");
	}
	super.setAcquisition(acquisition);
    }

    @Override
    public void setCease(YearMonthDay cease) {
	if (getAcquisition() == null || (cease != null && cease.isBefore(getAcquisition()))) {
	    throw new DomainException("error.material.endDateBeforeBeginDate");
	}
	super.setCease(cease);
    }

    public void setOccupationInterval(YearMonthDay acquisition, YearMonthDay cease) {
	setAcquisition(acquisition);
	setCease(cease);
    }
    
    @Override
    public boolean isMaterial() {
        return true;
    }
}
