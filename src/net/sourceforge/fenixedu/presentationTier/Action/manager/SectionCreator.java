package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.accessControl.EveryoneGroup;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class SectionCreator implements Serializable {

    private static final long serialVersionUID = 1L;

    private MultiLanguageString name;

    private boolean visible;

    private DomainReference<Section> nextSection;

    private DomainReference<Site> site;

    private DomainReference<Section> superiorSection;

    private Group permittedGroup;

    public SectionCreator(Site site) {
	super();

	this.site = new DomainReference<Site>(site);
	this.superiorSection = new DomainReference<Section>(null);
	this.nextSection = new DomainReference<Section>(null);
	this.visible = true;
	this.permittedGroup = new EveryoneGroup();
    }

    public SectionCreator(Section section) {
	this(section.getSite());

	setSuperiorSection(section);
    }

    public MultiLanguageString getName() {
	return this.name;
    }

    public void setName(MultiLanguageString name) {
	this.name = name;
    }

    public boolean getVisible() {
	return this.visible;
    }

    public void setVisible(boolean visible) {
	this.visible = visible;
    }

    public Section getNextSection() {
	return this.nextSection.getObject();
    }

    public void setNextSection(Section nextSection) {
	this.nextSection = new DomainReference<Section>(nextSection);
    }

    public Site getSite() {
	return this.site.getObject();
    }

    public Section getSuperiorSection() {
	return this.superiorSection.getObject();
    }

    public void setSuperiorSection(Section superiorSection) {
	this.superiorSection = new DomainReference<Section>(superiorSection);
    }

    public Group getPermittedGroup() {
	return this.permittedGroup;
    }

    public void setPermittedGroup(Group permittedGroup) {
	this.permittedGroup = permittedGroup;
    }

    public void createSection() {
	Section section = new Section((getSuperiorSection() == null) ? getSite() : getSuperiorSection(), getName());
	section.setNextSection(getNextSection());
	section.setPermittedGroup(getPermittedGroup());
	section.setVisible(getVisible());
    }
}
