package net.sourceforge.fenixedu.domain.candidacyProcess;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

import org.joda.time.LocalDate;

abstract public class CandidacyPrecedentDegreeInformation extends CandidacyPrecedentDegreeInformation_Base {

    protected CandidacyPrecedentDegreeInformation() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public boolean hasInstitution() {
	return getInstitution() != null;
    }

    public boolean isExternal() {
	return false;
    }

    abstract public String getDegreeDesignation();

    abstract public LocalDate getConclusionDate();

    abstract public Unit getInstitution();

    abstract public String getConclusionGrade();

    abstract public void edit(final CandidacyPrecedentDegreeInformationBean precedentDegreeInformation);

    public String getDegreeAndInstitutionName() {
	return getDegreeDesignation() + " / " + getInstitution().getName();
    }
}
