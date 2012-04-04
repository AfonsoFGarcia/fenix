package net.sourceforge.fenixedu.domain.homepage;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.injectionCode.IGroup;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class Homepage extends Homepage_Base {

    public static final long MB = 1024 * 1024;

    public static final long REGULAR_QUOTA = 10 * MB;
    public static final long TEACHER_QUOTA = 200 * MB;

    public static final Comparator<Homepage> HOMEPAGE_COMPARATOR_BY_NAME = new Comparator<Homepage>() {

	@Override
	public int compare(Homepage o1, Homepage o2) {
	    return Collator.getInstance().compare(o1.getName(), o2.getName());
	}

    };

    public Homepage() {
	super();

	setRootDomainObject(RootDomainObject.getInstance());

	setActivated(false);
	setShowUnit(false);
	setShowCategory(false);
	setShowPhoto(false);
	setShowEmail(false);
	setShowTelephone(false);
	setShowWorkTelephone(false);
	setShowMobileTelephone(false);
	setShowAlternativeHomepage(false);
	setShowResearchUnitHomepage(false);
	setShowCurrentExecutionCourses(false);
	setShowActiveStudentCurricularPlans(false);
	setShowAlumniDegrees(false);
	setShowPublications(false);
	setShowPatents(false);
	setShowInterests(false);
	setShowCurrentAttendingExecutionCourses(false);
    }

    public Homepage(Person person) {
	this();

	setPerson(person);
    }

    public String getOwnersName() {
	return getPerson().getNickname();
    }

    public void setOwnersName(String name) {
	getPerson().setNickname(name);
    }

    @Override
    public IGroup getOwner() {
	return getPerson().getPersonGroup();
    }

    public static List<Homepage> getAllHomepages() {
	List<Homepage> result = new ArrayList<Homepage>();

	for (Content content : RootDomainObject.getInstance().getContents()) {
	    if (content instanceof Homepage) {
		result.add((Homepage) content);
	    }
	}
	return result;
    }

    @Override
    public List<IGroup> getContextualPermissionGroups() {
	List<IGroup> groups = super.getContextualPermissionGroups();
	groups.add(getPerson().getPersonGroup());

	return groups;
    }

    @Override
    public boolean hasQuota() {
	return true;
    }

    @Override
    public long getQuota() {
	final Person person = getPerson();
	return person.hasTeacher() ? TEACHER_QUOTA : REGULAR_QUOTA;
    }

    @Override
    protected void disconnect() {
	removePerson();
	super.disconnect();
    }

    public boolean isHomepageActivated() {
	return getActivated() != null && getActivated().booleanValue();
    }

    @Override
    public MultiLanguageString getName() {
	return new MultiLanguageString().with(Language.pt, String.valueOf(getPerson().getIstUsername()));
    }

    @Override
    public void setNormalizedName(final MultiLanguageString normalizedName) {
	// unable to optimize because we cannot track changes to name correctly.
	// don't call super.setNormalizedName() !
    }

}
