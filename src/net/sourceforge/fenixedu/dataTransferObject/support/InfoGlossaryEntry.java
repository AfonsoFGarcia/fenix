/*
 * Created on 2004/08/30
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.support;

import java.io.Serializable;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.domain.support.GlossaryEntry;

/**
 * @author Luis Cruz
 * 
 */
public class InfoGlossaryEntry extends InfoObject implements Serializable {

	private String term = null;

	private String definition = null;

	public InfoGlossaryEntry() {
		super();
	}

	public String getDefinition() {
		return definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public void copyFromDomain(GlossaryEntry glossaryEntry) {
		super.copyFromDomain(glossaryEntry);
		if (glossaryEntry != null) {
			setTerm(glossaryEntry.getTerm());
			setDefinition(glossaryEntry.getDefinition());
		}
	}

	public static InfoGlossaryEntry newInfoFromDomain(GlossaryEntry glossaryEntry) {
		InfoGlossaryEntry infoGlossaryEntry = null;
		if (glossaryEntry != null) {
			infoGlossaryEntry = new InfoGlossaryEntry();
			infoGlossaryEntry.copyFromDomain(glossaryEntry);
		}
		return infoGlossaryEntry;
	}
}
