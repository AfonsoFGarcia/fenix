package net.sourceforge.fenixedu.domain.phd;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.exceptions.PhdDomainOperationException;
import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class ThesisSubject extends ThesisSubject_Base {

    protected ThesisSubject() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    protected ThesisSubject(PhdProgramFocusArea focusArea, MultiLanguageString name, MultiLanguageString description,
            Teacher teacher, String externalAdvisor) {
        this();

        checkParameters(focusArea, name, description, teacher);

        setPhdProgramFocusArea(focusArea);
        setName(name);
        setDescription(description);
        setTeacher(teacher);
        setExternalAdvisorName(externalAdvisor);

        for (PhdIndividualProgramProcess process : focusArea.getIndividualProgramProcesses()) {
            if (isCandidacyPeriodOpen(process)) {
                new ThesisSubjectOrder(this, process, process.getHighestThesisSubjectOrder() + 1);
            }
        }
    }

    private void checkParameters(PhdProgramFocusArea focusArea, MultiLanguageString name, MultiLanguageString description,
            Teacher teacher) {
        String[] args = {};
        if (focusArea == null) {
            throw new DomainException("error.net.sourceforge.fenixedu.domain.phd.ThesisSubject.focusArea.required", args);
        }

        if (name == null) {
            String[] args1 = {};
            if (name == null) {
                throw new DomainException("error.net.sourceforge.fenixedu.domain.phd.ThesisSubject.name.required", args1);
            }
        }

        if (!name.hasContent(Language.en)) {
            throw new PhdDomainOperationException(
                    "error.net.sourceforge.fenixedu.domain.phd.ThesisSubject.name.in.english.required");
        }

        if (teacher == null) {
            throw new PhdDomainOperationException("error.net.sourceforge.fenixedu.domain.phd.ThesisSubject.teacher.required");
        }
    }

    @Service
    public void edit(MultiLanguageString name, MultiLanguageString description, Teacher teacher, String externalAdvisor) {
        checkParameters(getPhdProgramFocusArea(), name, description, teacher);

        setName(name);
        setDescription(description);
        setTeacher(teacher);
        setExternalAdvisorName(externalAdvisor);
    }

    @Service
    public void delete() {
        for (ThesisSubjectOrder order : getThesisSubjectOrders()) {
            if (isCandidacyPeriodOpen(order.getPhdIndividualProgramProcess())) {
                order.delete();
            }
        }
        removePhdProgramFocusArea();

        if (!hasAnyThesisSubjectOrders()) {
            removeTeacher();

            removeRootDomainObject();
            deleteDomainObject();
        }
    }

    @Service
    public static ThesisSubject createThesisSubject(PhdProgramFocusArea focusArea, MultiLanguageString name,
            MultiLanguageString description, Teacher teacher, String externalAdvisor) {
        return new ThesisSubject(focusArea, name, description, teacher, externalAdvisor);
    }

    private boolean isCandidacyPeriodOpen(PhdIndividualProgramProcess process) {
        return process.getCandidacyProcess().getPublicPhdCandidacyPeriod() != null
                && process.getCandidacyProcess().getPublicPhdCandidacyPeriod().isOpen();
    }
}
