package net.sourceforge.fenixedu.domain.phd;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class PhdProgram extends PhdProgram_Base {

    static public Comparator<PhdProgram> COMPARATOR_BY_NAME = new Comparator<PhdProgram>() {
	@Override
	public int compare(final PhdProgram p1, final PhdProgram p2) {
	    int res = p1.getName().compareTo(p2.getName());
	    return res != 0 ? res : DomainObject.COMPARATOR_BY_ID.compare(p1, p2);
	}
    };

    private PhdProgram() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setWhenCreated(new DateTime());
	setCreator(getCurrentUser());
	new PhdProgramServiceAgreementTemplate(this);
    }

    private PhdProgram(final Degree degree, final MultiLanguageString name, final String acronym) {
	this();

	checkDegree(degree);
	checkAcronym(acronym);

	setDegree(degree);
	setName(name);
	setAcronym(acronym);
    }

    private PhdProgram(final Degree degree, final MultiLanguageString name, final String acronym, final Unit parentProgramUnit) {
	this(degree, name, acronym);
	PhdProgramUnit.create(this, getName(), getWhenCreated().toYearMonthDay(), null, parentProgramUnit);
    }

    private void checkDegree(final Degree degree) {
	check(degree, "error.PhdProgram.invalid.degree");
	if (degree.getDegreeType() != DegreeType.BOLONHA_ADVANCED_SPECIALIZATION_DIPLOMA) {
	    throw new DomainException("error.PhdProgram.invalid.degree");
	}
    }

    private void checkAcronym(final String acronym) {
	check(acronym, "error.PhdProgram.invalid.acronym");
	final PhdProgram program = readByAcronym(acronym);
	if (program != null && program != this) {
	    throw new DomainException("error.PhdProgram.acronym.already.exists", acronym);
	}
    }

    private boolean hasAcronym(final String acronym) {
	return getAcronym() != null && getAcronym().equalsIgnoreCase(acronym);
    }

    public String getPresentationName() {
	return getPresentationName(Language.getLocale());
    }

    private String getPresentationName(final Locale locale) {
	return getPrefix(locale) + getNameFor(locale);
    }

    private String getNameFor(final Locale locale) {
	final Language language = Language.valueOf(locale.getLanguage());
	return getName().hasContent(language) ? getName().getContent(language) : getName().getPreferedContent();
    }

    private String getPrefix(final Locale locale) {
	final ResourceBundle bundle = ResourceBundle.getBundle("resources.PhdResources", locale);
	return bundle.getString("label.php.program") + " " + bundle.getString("label.in") + " ";
    }

    @Service
    public void delete() {
	if (hasAnyIndividualProgramProcesses()) {
	    throw new DomainException("error.PhdProgram.cannot.delete.has.individual.php.program.processes");
	}

	getPhdProgramUnit().delete();
	removeDegree();
	removeServiceAgreementTemplate();
	removeRootDomainObject();
	super.deleteDomainObject();
    }

    @Service
    static public PhdProgram create(final Degree degree, final MultiLanguageString name, final String acronym) {
	return new PhdProgram(degree, name, acronym);
    }

    @Service
    static public PhdProgram create(final Degree degree, final MultiLanguageString name, final String acronym, final Unit parent) {
	return new PhdProgram(degree, name, acronym, parent);
    }

    static public PhdProgram readByAcronym(final String acronym) {
	for (final PhdProgram program : RootDomainObject.getInstance().getPhdProgramsSet()) {
	    if (program.hasAcronym(acronym)) {
		return program;
	    }
	}
	return null;
    }

    public Set<Person> getCoordinatorsFor(ExecutionYear executionYear) {
	final ExecutionDegree executionDegree = getDegree().getLastActiveDegreeCurricularPlan().getExecutionDegreeByYear(
		executionYear);

	final Set<Person> result = new HashSet<Person>();
	if (executionDegree != null) {
	    for (final Coordinator coordinator : executionDegree.getCoordinatorsList()) {
		result.add(coordinator.getPerson());
	    }
	}

	return result;
    }

}
