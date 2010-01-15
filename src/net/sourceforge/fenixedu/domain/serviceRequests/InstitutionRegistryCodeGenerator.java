package net.sourceforge.fenixedu.domain.serviceRequests;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DiplomaRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.RegistryDiplomaRequest;

public class InstitutionRegistryCodeGenerator extends InstitutionRegistryCodeGenerator_Base {
    public InstitutionRegistryCodeGenerator() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    protected Integer getNextNumber(CycleType cycle) {
	switch (cycle) {
	case FIRST_CYCLE:
	    super.setCurrentFirstCycle((super.getCurrentFirstCycle() != null ? super.getCurrentFirstCycle() : 0) + 1);
	    return super.getCurrentFirstCycle();
	case SECOND_CYCLE:
	    super.setCurrentSecondCycle((super.getCurrentSecondCycle() != null ? super.getCurrentSecondCycle() : 0) + 1);
	    return super.getCurrentSecondCycle();
	case THIRD_CYCLE:
	    super.setCurrentThirdCycle((super.getCurrentThirdCycle() != null ? super.getCurrentThirdCycle() : 0) + 1);
	    return super.getCurrentThirdCycle();
	default:
	    throw new DomainException("error.InstitutionRegistryCodeGenerator.unsupportedCycle");
	}
    }

    @Override
    public Integer getCurrentFirstCycle() {
	throw new UnsupportedOperationException();
    }

    @Override
    public Integer getCurrentSecondCycle() {
	throw new UnsupportedOperationException();
    }

    @Override
    public Integer getCurrentThirdCycle() {
	throw new UnsupportedOperationException();
    }

    @Override
    public void setCurrentFirstCycle(Integer currentFirstCycle) {
	throw new UnsupportedOperationException();
    }

    @Override
    public void setCurrentSecondCycle(Integer currentSecondCycle) {
	throw new UnsupportedOperationException();
    }

    @Override
    public void setCurrentThirdCycle(Integer currentThirdCycle) {
	throw new UnsupportedOperationException();
    }

    public RegistryCode createRegistryFor(RegistryDiplomaRequest request) {
	return new RegistryCode(this, request);
    }

    public RegistryCode createRegistryFor(DiplomaRequest request) {
	return new RegistryCode(this, request);
    }
}
