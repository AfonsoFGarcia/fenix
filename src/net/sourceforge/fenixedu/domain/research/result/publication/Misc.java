package net.sourceforge.fenixedu.domain.research.result.publication;

import bibtex.dom.BibtexEntry;
import bibtex.dom.BibtexFile;
import bibtex.dom.BibtexPersonList;
import bibtex.dom.BibtexString;
import net.sourceforge.fenixedu.accessControl.Checked;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation.ResultParticipationRole;
import net.sourceforge.fenixedu.util.Month;

/**
 * Use this type when nothing else fits. Required fields: none. Optional fields:
 * author, title, howpublished, month, year, note.
 * 
 * Extra from previous publications: otherPublicationType, numberPages,
 * language, publisher, address
 */
public class Misc extends Misc_Base {

    public Misc() {
        super();
    }

    public Misc(Person participator, String title, Unit publisher, Integer year, String howPublished,
            String note, String address, String otherPublicationType, Integer numberPages,
            String language, Country country, Month month, String url) {
        this();
        checkRequiredParameters(title);
        super.setCreatorParticipation(participator, ResultParticipationRole.Author);
        fillAllAttributes(title, publisher, year, howPublished, note, address, otherPublicationType,
                numberPages, language, country, month, url);
    }

    @Checked("ResultPredicates.writePredicate")
    public void setEditAll(String title, Unit publisher, Integer year, String howPublished, String note,
            String address, String otherPublicationType, Integer numberPages, String language,
            Country country, Month month, String url) {
        checkRequiredParameters(title);
        fillAllAttributes(title, publisher, year, howPublished, note, address, otherPublicationType,
                numberPages, language, country, month, url);
        super.setModifyedByAndDate();
    }

    private void fillAllAttributes(String title, Unit publisher, Integer year, String howPublished,
            String note, String address, String otherPublicationType, Integer numberPages,
            String language, Country country, Month month, String url) {
        super.setTitle(title);
        super.setPublisher(publisher);
        super.setYear(year);
        super.setHowPublished(howPublished);
        super.setNote(note);
        super.setAddress(address);
        super.setOtherPublicationType(otherPublicationType);
        super.setNumberPages(numberPages);
        super.setLanguage(language);
        super.setCountry(country);
        super.setMonth(month);
        super.setUrl(url);
    }

    @Override
    public String getResume() {
        String resume = getParticipationsAndTitleString();
        if ((getOtherPublicationType() != null) && (getOtherPublicationType().length() > 0))
            resume = resume + getOtherPublicationType() + ", ";
        if ((getYear() != null) && (getYear() > 0))
            resume = resume + getYear() + ", ";
        if (getPublisher() != null)
            resume = resume + getPublisher().getName() + ", ";

        resume = finishResume(resume);
        return resume;
    }

    @Override
    public BibtexEntry exportToBibtexEntry() {
        BibtexFile bibtexFile = new BibtexFile();
        BibtexEntry bibEntry = bibtexFile.makeEntry("misc", null);
        if ((getTitle() != null) && (getTitle().length() > 0))
            bibEntry.setField("title", bibtexFile.makeString(getTitle()));
        if ((getYear() != null) && (getYear() > 0))
            bibEntry.setField("year", bibtexFile.makeString(getYear().toString()));
        if ((getHowPublished() != null) && (getHowPublished().length() > 0))
            bibEntry.setField("howpublished", bibtexFile.makeString(getHowPublished()));
        if (getMonth() != null)
            bibEntry.setField("month", bibtexFile.makeString(getMonth().toString().toLowerCase()));
        if ((getNote() != null) && (getNote().length() > 0))
            bibEntry.setField("note", bibtexFile.makeString(getNote()));

        BibtexPersonList authorsList = getBibtexAuthorsList(bibtexFile, getAuthors());
        if (authorsList != null) {
            BibtexString bplString = bibtexFile.makeString(bibtexPersonListToString(authorsList));
            bibEntry.setField("author", bplString);
        }

        return bibEntry;
    }

    private void checkRequiredParameters(String title) {
        if ((title == null) || (title.length() == 0))
            throw new DomainException("error.researcher.Misc.title.null");
    }

    @Override
    public void setTitle(String title) {
        throw new DomainException("error.researcher.Misc.call", "setTitle");
    }

    @Override
    public void setYear(Integer year) {
        throw new DomainException("error.researcher.Misc.call", "setYear");
    }

    @Override
    public void setHowPublished(String howPublished) {
        throw new DomainException("error.researcher.Misc.call", "setHowPublished");
    }

    @Override
    public void setAddress(String address) {
        throw new DomainException("error.researcher.Misc.call", "setAddress");
    }

    @Override
    public void setOtherPublicationType(String otherPublicationType) {
        throw new DomainException("error.researcher.Misc.call", "setOtherPublicationType");
    }

    @Override
    public void setNote(String note) {
        throw new DomainException("error.researcher.Misc.call", "setNote");
    }

    @Override
    public void setLanguage(String language) {
        throw new DomainException("error.researcher.Misc.call", "setLanguage");
    }

    @Override
    public void setMonth(Month month) {
        throw new DomainException("error.researcher.Misc.call", "setMonth");
    }

    @Override
    public void setUrl(String url) {
        throw new DomainException("error.researcher.Misc.call", "setUrl");
    }

    @Override
    public void setNumberPages(Integer numberPages) {
        throw new DomainException("error.researcher.Misc.call", "setNumberPages");
    }

    @Override
    public void setOrganization(Unit organization) {
        throw new DomainException("error.researcher.Misc.call", "setOrganization");
    }

    @Override
    public void setPublisher(Unit publisher) {
        throw new DomainException("error.researcher.Misc.call", "setPublisher");
    }
}
