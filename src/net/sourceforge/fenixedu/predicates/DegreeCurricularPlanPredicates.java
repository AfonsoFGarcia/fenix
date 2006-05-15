/**
 * 
 */
package net.sourceforge.fenixedu.predicates;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.accessControl.AccessControlPredicate;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.domain.person.RoleType;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class DegreeCurricularPlanPredicates {

    public static final AccessControlPredicate<DegreeCurricularPlan> readPredicate = new AccessControlPredicate<DegreeCurricularPlan>() {

        public boolean evaluate(DegreeCurricularPlan dcp) {
            
            if (!dcp.isBolonha()) {
                return true;
            }
            
            Person person = AccessControl.getUserView().getPerson();

            if (person.hasRole(RoleType.SCIENTIFIC_COUNCIL)) {
                return true;
            }

            boolean isCurricularPlanMember = dcp.getCurricularPlanMembersGroup().isMember(person);

            switch (dcp.getCurricularStage()) {
            case DRAFT:
                return isCurricularPlanMember;
            case PUBLISHED:
                return isCurricularPlanMember || person.hasRole(RoleType.BOLONHA_MANAGER);
            case APPROVED:
                return true;
            default:
                return false;
            }

        }

    };

    public static final AccessControlPredicate<DegreeCurricularPlan> scientificCouncilWritePredicate = new AccessControlPredicate<DegreeCurricularPlan>() {

        public boolean evaluate(DegreeCurricularPlan dcp) {

            Person person = AccessControl.getUserView().getPerson();
            if (person.hasRole(RoleType.SCIENTIFIC_COUNCIL)) {
                return true;
            }
            
            if (!dcp.isBolonha()) {
                return true;
            }
            


            return false;
        }

    };

    public static final AccessControlPredicate<DegreeCurricularPlan> curricularPlanMemberWritePredicate = new AccessControlPredicate<DegreeCurricularPlan>() {

        public boolean evaluate(DegreeCurricularPlan dcp) {
            
            if (!dcp.isBolonha()) {
                return true;
            }
            
            Person person = AccessControl.getUserView().getPerson();
            return dcp.getCurricularPlanMembersGroup().isMember(person);
        }

    };

}
