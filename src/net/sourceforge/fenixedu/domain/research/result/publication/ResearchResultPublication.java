package net.sourceforge.fenixedu.domain.research.result.publication;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.research.result.ResearchResult;
import net.sourceforge.fenixedu.domain.research.result.ResearchResultDocumentFile;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation;
import net.sourceforge.fenixedu.domain.research.result.ResultUnitAssociation;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation.ResultParticipationRole;
import net.sourceforge.fenixedu.injectionCode.Checked;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;
import net.sourceforge.fenixedu.util.MultiLanguageString;
import bibtex.dom.BibtexEntry;
import bibtex.dom.BibtexFile;
import bibtex.dom.BibtexPerson;
import bibtex.dom.BibtexPersonList;

public abstract class ResearchResultPublication extends ResearchResultPublication_Base {

    static class OrderComparator implements Comparator<ResearchResultPublication> {
	public int compare(ResearchResultPublication rp1, ResearchResultPublication rp2) {
	    Integer pub1 = rp1.getYear();
	    Integer pub2 = rp2.getYear();
	    if (pub1 == null) {
		return 1;
	    } else if (pub2 == null) {
		return -1;
	    }
	    return (-1) * pub1.compareTo(pub2);
	}
    }

    public static <T extends ResearchResultPublication> List<T> sort(Collection<T> resultPublications) {
	List<T> sorted = new ArrayList<T>(resultPublications);
	Collections.sort(sorted, new ResearchResultPublication.OrderComparator());

	return sorted;
    }

    public ResearchResultPublication() {
	super();
    }

    private void removeAssociations() {
	super.setPublisher(null);
	super.setOrganization(null);
    }

    @Override
    @Checked("ResultPredicates.writePredicate")
    public void delete() {
	removeAssociations();
	super.delete();
    }

    public List<Person> getAuthors() {
	ArrayList<Person> authors = new ArrayList<Person>();
	for (ResultParticipation participation : this.getOrderedResultParticipations()) {
	    if (participation.getRole().equals(ResultParticipationRole.Author))
		authors.add(participation.getPerson());
	}
	return authors;
    }

    public List<Person> getEditors() {
	ArrayList<Person> editors = new ArrayList<Person>();
	for (ResultParticipation participation : this.getOrderedResultParticipations()) {
	    if (participation.getRole().equals(ResultParticipationRole.Editor))
		editors.add(participation.getPerson());
	}
	return editors;
    }

    public abstract BibtexEntry exportToBibtexEntry();

    protected String generateBibtexKey() {
	String key = "";
	ResultParticipation participation = getOrderedResultParticipations().get(0);
	key = participation.getPerson().getNickname();
	key = key.replace(" ", "");
	if ((getYear() != null) && (getYear() > 0))
	    key = key + getYear();

	return key;
    }

    protected BibtexPersonList getBibtexAuthorsList(BibtexFile bibtexFile, List<Person> authors) {
	if ((authors != null) && (authors.size() > 0)) {
	    BibtexPersonList authorsList = bibtexFile.makePersonList();
	    BibtexPerson bp;
	    for (Person person : authors) {
		bp = bibtexFile.makePerson(person.getNickname(), null, null, null, false);
		authorsList.add(bp);
	    }
	    return authorsList;
	}
	return null;
    }

    protected BibtexPersonList getBibtexEditorsList(BibtexFile bibtexFile, List<Person> editors) {
	if ((editors != null) && (editors.size() > 0)) {
	    BibtexPersonList editorsList = bibtexFile.makePersonList();
	    BibtexPerson bp;
	    for (Person person : editors) {
		bp = bibtexFile.makePerson(person.getNickname(), null, null, null, false);
		editorsList.add(bp);
	    }
	    return editorsList;
	}
	return null;
    }

    /**
         * this methods are used instead of the provided by the javabib library
         * because in the javabib a BibtexPerson is printed using first lastName
         */
    protected String bibtexPersonToString(BibtexPerson bp) {
	String all = "";
	if (bp.isOthers()) {
	    all = "others";
	} else {
	    if (bp.getFirst() != null)
		all = all + bp.getFirst();
	    if (bp.getPreLast() != null)
		all = all + ' ' + bp.getPreLast();
	    if (bp.getLast() != null)
		all = all + ' ' + bp.getLast();
	    if (bp.getLineage() != null)
		all = all + ' ' + bp.getLineage();
	}
	return all;
    }

    protected String bibtexPersonListToString(BibtexPersonList bpl) {
	String personList = "";

	boolean isFirst = true;
	for (Iterator it = bpl.getList().iterator(); it.hasNext();) {
	    if (isFirst) {
		isFirst = false;
	    } else
		personList = personList + " and ";
	    personList = personList + bibtexPersonToString((BibtexPerson) it.next());
	}
	return personList;
    }

    protected void checkRequiredParameters(MultiLanguageString keywords, MultiLanguageString note) {
	if (note == null)
	    throw new DomainException("error.researcher.Book.description.null");
	if (keywords == null)
	    throw new DomainException("error.researcher.Book.keywords.null");
    }

    public List<String> getKeywordsList() {
	List<String> keywordList = new ArrayList<String>();
	if (keywordHasContent()) {
	    for (String keywordsForLanguage : this.getKeywords().getAllContents()) {
		String[] keywords = keywordsForLanguage.split(",");
		for (int i = 0; i < keywords.length; i++) {
		    keywordList.add(keywords[i].trim());
		}
	    }
	}
	return keywordList;
    }

    private boolean keywordHasContent() {
	if (this.getKeywords() == null)
	    return false;
	for (String content : getKeywords().getAllContents()) {
	    if (content.length() > 0)
		return true;
	}
	return false;
    }

    public String getLocalizedType() {
	return RenderUtils.getResourceString("RESEARCHER_RESOURCES", "researcher.ResultPublication.type."
		+ getClass().getSimpleName());
    }

    public void copyReferecesTo(ResearchResultPublication publication) {
	createNewParticipationsIn(publication);
	createNewUnitAssociationsIn(publication);
	moveFilesTo(publication);
	publication.setUniqueStorageId(getUniqueStorageId());
	publication.setCreator(getCreator());
    }

    private void moveFilesTo(ResearchResult publication) {
	for (ResearchResultDocumentFile file : getAllResultDocumentFiles()) {
	    file.moveFileToNewResearchResultType(publication);
	}
    }

    private void createNewUnitAssociationsIn(ResearchResultPublication publication) {
	for (ResultUnitAssociation association : getResultUnitAssociations()) {
	    publication.addUnitAssociation(association.getUnit(), association.getRole());
	}
    }

    private void createNewParticipationsIn(ResearchResult publication) {
	for (ResultParticipation participation : getOrderedResultParticipations()) {
	    ResultParticipationRole role = participation.getRole();

	    if (!publication.acceptsParticipationRole(role)) {
		role = (publication instanceof Proceedings) ? ResultParticipationRole.Editor
			: ResultParticipationRole.Author;
	    }
	    if (!publication.hasPersonParticipationWithRole(participation.getPerson(), role)) {
		publication.addParticipation(participation.getPerson(), role);
	    }
	}
    }
}
