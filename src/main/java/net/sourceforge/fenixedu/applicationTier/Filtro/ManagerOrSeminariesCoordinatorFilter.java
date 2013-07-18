package net.sourceforge.fenixedu.applicationTier.Filtro;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Seminaries.SeminaryCandidacy;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

public class ManagerOrSeminariesCoordinatorFilter extends Filtro {

    public void execute(Object[] parameters) throws Exception {
        IUserView id = AccessControl.getUserView();
        Integer SCPIDINternal = (Integer) parameters[1];

        boolean seminaryCandidate = false;
        if (SCPIDINternal != null) {
            seminaryCandidate = this.doesThisSCPBelongToASeminaryCandidate(SCPIDINternal);
        }

        if (((id != null && id.getRoleTypes() != null && !id.hasRoleType(getRoleType1()) && !(id.hasRoleType(getRoleType2()) && seminaryCandidate)))
                || (id == null) || (id.getRoleTypes() == null)) {
            throw new NotAuthorizedFilterException();
        }
    }

    public boolean doesThisSCPBelongToASeminaryCandidate(Integer SCPIDInternal) {
        StudentCurricularPlan scp = RootDomainObject.getInstance().readStudentCurricularPlanByOID(SCPIDInternal);
        if (scp != null) {
            List<SeminaryCandidacy> candidacies = scp.getRegistration().getAssociatedCandidancies();
            return !candidacies.isEmpty();
        }

        return false;
    }

    protected RoleType getRoleType1() {
        return RoleType.MANAGER;
    }

    protected RoleType getRoleType2() {
        return RoleType.SEMINARIES_COORDINATOR;
    }

}
