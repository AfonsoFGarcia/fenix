package net.sourceforge.fenixedu.presentationTier.Action.person;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class PersonFileSourceGroupBean implements PersonFileSource {

	/**
	 * Default serial id.
	 */
	private static final long serialVersionUID = 1L;

	private MultiLanguageString name;
	private List<PersonFileSource> children;
	private int count;

	public PersonFileSourceGroupBean(MultiLanguageString name) {
		this.name = name;
		this.children = new ArrayList<PersonFileSource>();
		this.count = -1; // not initialized
	}

	public PersonFileSourceGroupBean(Unit unit) {
		this(unit.getNameI18n());
	}

	@Override
	public MultiLanguageString getName() {
		return name;
	}

	@Override
	public List<PersonFileSource> getChildren() {
		return this.children;
	}

	public PersonFileSourceGroupBean add(PersonFileSourceBean child) {
		this.children.add(child);
		return this;
	}

	@Override
	public int getCount() {
		if (this.count < 0) {
			this.count = 0;

			for (PersonFileSource child : getChildren()) {
				this.count += child.getCount();
			}
		}

		return this.count;
	}

	@Override
	public boolean isAllowedToUpload(Person person) {
		for (PersonFileSource child : getChildren()) {
			if (child.isAllowedToUpload(person)) {
				return true;
			}
		}
		return false;
	}
}
