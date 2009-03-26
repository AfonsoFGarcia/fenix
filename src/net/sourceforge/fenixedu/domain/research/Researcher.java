package net.sourceforge.fenixedu.domain.research;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.contacts.PartyContact;

import org.apache.commons.collections.comparators.ReverseComparator;

public class Researcher extends Researcher_Base {

    public static Comparator<Researcher> PUBLICATION_VOLUME_COMPARATOR = new ReverseComparator(new Comparator<Researcher>() {

	public int compare(Researcher r1, Researcher r2) {
	    Integer resultParticipationsCount = r1.getPerson().getResultParticipationsCount();
	    Integer resultParticipationsCount2 = r2.getPerson().getResultParticipationsCount();
	    return resultParticipationsCount.compareTo(resultParticipationsCount2);
	}

    });

    public Researcher(Person person) {
	super();
	setPerson(person);
	setAllowsToBeSearched(Boolean.FALSE);
	setAllowsContactByStudents(Boolean.FALSE);
	setAllowsContactByMedia(Boolean.FALSE);
	setAllowsContactByStudents(Boolean.FALSE);
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public void delete() {
	removePerson();
	removeRootDomainObject();
	super.deleteDomainObject();
    }

    public boolean hasAtLeastOneKeyword(String... keywords) {
	for (String keyword : keywords) {
	    if (hasKeyword(keyword)) {
		return true;
	    }
	}
	return false;
    }

    public boolean hasKeyword(String keyword) {
	String trimmedKeyword = keyword.trim();
	for (String reseacherKeyword : getKeywordsPt().split(",")) {
	    if (trimmedKeyword.equalsIgnoreCase(reseacherKeyword.trim())) {
		return true;
	    }
	}

	for (String reseacherKeyword : getKeywordsEn().split(",")) {
	    if (trimmedKeyword.equalsIgnoreCase(reseacherKeyword.trim())) {
		return true;
	    }
	}
	
	return false;
    }

    public List<ResearchInterest> getResearchInterests() {
	List<ResearchInterest> orderedInterests = new ArrayList<ResearchInterest>(getPerson().getResearchInterests());
	Collections.sort(orderedInterests, new Comparator<ResearchInterest>() {
	    public int compare(ResearchInterest researchInterest1, ResearchInterest researchInterest2) {
		return researchInterest1.getInterestOrder().compareTo(researchInterest2.getInterestOrder());
	    }
	});

	return orderedInterests;
    }

    public void setAvailableContacts(List<PartyContact> contacts) {
	getAvailableContacts().clear();
	for (PartyContact contact : contacts) {
	    addAvailableContacts(contact);
	}
    }
    
    private String normalizeKeywords(String keywordList) {
	String[] keys = keywordList.split(",");
	
	StringBuilder sb = new StringBuilder();
	for(String key : keys) {
	    String[] dtd = key.split(" ");
	    
	    for(String eee : dtd) {
		if(eee.trim().length() > 0) {
		    sb.append(eee.trim()).append(" ");
		}
	    }
	    sb.deleteCharAt(sb.length() - 1);
	    sb.append(",");
	}
	
	return sb.substring(0, sb.length() - 1);
    }
    
    @Override
    public void setKeywordsEn(String keywordsEn) {
	super.setKeywordsEn(normalizeKeywords(keywordsEn));
    }

    @Override
    public void setKeywordsPt(String keywordsPt) {
	super.setKeywordsPt(normalizeKeywords(keywordsPt));
    }
    
}
