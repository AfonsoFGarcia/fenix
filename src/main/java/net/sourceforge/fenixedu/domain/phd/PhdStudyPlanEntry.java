package net.sourceforge.fenixedu.domain.phd;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.joda.time.DateTime;

import dml.runtime.RelationAdapter;

abstract public class PhdStudyPlanEntry extends PhdStudyPlanEntry_Base {

    static {
        PhdStudyPlanPhdStudyPlanEntry.addListener(new RelationAdapter<PhdStudyPlanEntry, PhdStudyPlan>() {
            @Override
            public void beforeAdd(PhdStudyPlanEntry entry, PhdStudyPlan studyPlan) {
                if (entry != null && studyPlan != null) {
                    if (studyPlan.hasSimilarEntry(entry)) {
                        throw new DomainException("error.phd.PhdStudyPlanEntry.found.similar.entry");
                    }
                }
            }
        });
    }

    protected PhdStudyPlanEntry() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setWhenCreated(new DateTime());
        if (AccessControl.getPerson() != null) {
            setCreatedBy(AccessControl.getPerson().getUsername());
        }
    }

    protected PhdStudyPlanEntry(PhdStudyPlanEntryType type, PhdStudyPlan studyPlan) {
        this();

        init(type, studyPlan);

    }

    protected void init(PhdStudyPlanEntryType type, PhdStudyPlan studyPlan) {
        String[] args = {};
        if (type == null) {
            throw new DomainException("error.net.sourceforge.fenixedu.domain.phd.PhdStudyPlanEntry.type.cannot.be.null", args);
        }
        String[] args1 = {};
        if (studyPlan == null) {
            throw new DomainException("error.net.sourceforge.fenixedu.domain.phd.PhdStudyPlanEntry.studyPlan.cannot.be.null",
                    args1);
        }

        super.setType(type);
        super.setStudyPlan(studyPlan);
    }

    public boolean isNormal() {
        return getType() == PhdStudyPlanEntryType.NORMAL;
    }

    public boolean isPropaedeutic() {
        return getType() == PhdStudyPlanEntryType.PROPAEDEUTIC;
    }

    public boolean isExtraCurricular() {
        return getType() == PhdStudyPlanEntryType.EXTRA_CURRICULAR;
    }

    public boolean isInternalEntry() {
        return false;
    }

    public boolean isExternalEntry() {
        return false;
    }

    public void delete() {
        super.setRootDomainObject(null);
        super.setStudyPlan(null);

        super.deleteDomainObject();

    }

    abstract public String getCourseDescription();

    abstract public boolean isSimilar(PhdStudyPlanEntry entry);

}
