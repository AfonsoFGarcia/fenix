package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.LocalDate;

import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class DegreeOfficialPublication extends DegreeOfficialPublication_Base {
    public DegreeOfficialPublication(Degree degree, LocalDate date) {
        if (degree == null) {
            throw new DomainException("error.degree.officialpublication.unlinked");
        }
        if (date == null) {
            throw new DomainException("error.degree.officialpublication.undated");
        }
        setDegree(degree);
        setPublication(date);
    }

    protected RootDomainObject getRootDomainObject() {
        return getDegree().getRootDomainObject();
    }

    @Atomic
    public DegreeSpecializationArea createSpecializationArea(String nameEn, String namePt) {

        MultiLanguageString area = new MultiLanguageString(Language.en, nameEn).with(Language.pt, namePt);

        return new DegreeSpecializationArea(this, area);
    }

    @Atomic
    public void changeOfficialreference(String officialReference, final LocalDate publication) {
        this.setOfficialReference(officialReference);
        this.setPublication(publication);
    }

    @Atomic
    public void delete() {

        setDegree(null);

        super.deleteDomainObject();
    }
}
