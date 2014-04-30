package net.sourceforge.fenixedu.domain.phd.individualProcess.activities;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Qualification;
import net.sourceforge.fenixedu.domain.accessControl.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;

import org.fenixedu.bennu.core.domain.User;

public class DeleteQualification extends PhdIndividualProgramProcessActivity {

    @Override
    protected void activityPreConditions(PhdIndividualProgramProcess arg0, User userView) {
    }

    @Override
    protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, User userView, Object object) {
        final Qualification qualification = (Qualification) object;
        if (process.getPerson().getAssociatedQualificationsSet().contains(qualification)) {
            if (!canDelete(qualification, process, userView != null ? userView.getPerson() : null)) {
                throw new DomainException("error.PhdIndividualProgramProcess.DeleteQualification.not.authorized");
            }
            qualification.delete();
        }
        return process;
    }

    private boolean canDelete(final Qualification qualification, final PhdIndividualProgramProcess process, final Person person) {
        if (!qualification.hasCreator()) {
            return process.getCandidacyProcess().isPublicCandidacy();
        }
        final Person creator = qualification.getCreator();
        return creator == person
                || AcademicAuthorizationGroup.get(AcademicOperationType.MANAGE_PHD_PROCESS_STATE)
                        .isMember(creator.getUser());
    }
}