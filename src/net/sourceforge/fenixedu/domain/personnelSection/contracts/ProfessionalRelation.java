package net.sourceforge.fenixedu.domain.personnelSection.contracts;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class ProfessionalRelation extends ProfessionalRelation_Base {

    public ProfessionalRelation(final String giafId, final MultiLanguageString name, final Boolean fullTimeEquivalent) {
	super();
	check(giafId, "");
	check(name, "");
	setRootDomainObject(RootDomainObject.getInstance());
	setGiafId(giafId);
	setName(name);
	setFullTimeEquivalent(fullTimeEquivalent);
    }

    @Service
    public void edit(final MultiLanguageString name, final Boolean fullTimeEquivalent) {
	check(name, "");
	setName(name);
	setFullTimeEquivalent(fullTimeEquivalent);
    }
}
