/*
 * Created on 19/Fev/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.enrollment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.AuthorizationByManyRolesFilter;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;

/**
 * @author David Santos in Mar 1, 2004
 */

public class MasterDegreeEnrollmentWithoutRulesAuthorizationFilter extends AuthorizationByManyRolesFilter {

    public static final MasterDegreeEnrollmentWithoutRulesAuthorizationFilter instance = new MasterDegreeEnrollmentWithoutRulesAuthorizationFilter();
    private static DegreeType DEGREE_TYPE = DegreeType.MASTER_DEGREE;

    @Override
    protected Collection<RoleType> getNeededRoleTypes() {
        List<RoleType> roles = new ArrayList<RoleType>();
        roles.add(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE);
        return roles;
    }

    @Override
    protected String hasPrevilege(IUserView id, Object[] arguments) {
        try {
            if (!verifyDegreeTypeIsMasterDegree(arguments)) {
                return "error.degree.type";
            }

            if (!verifyStudentIsFromMasterDegree(arguments)) {
                return "error.student.degree.nonMaster";
            }

            return null;
        } catch (Exception exception) {
            exception.printStackTrace();
            return "noAuthorization";
        }
    }

    private boolean verifyDegreeTypeIsMasterDegree(Object[] arguments) {
        boolean isNonMaster = false;

        if (arguments != null && arguments[1] != null) {
            isNonMaster = DEGREE_TYPE.equals(arguments[1]);
        }

        return isNonMaster;
    }

    private boolean verifyStudentIsFromMasterDegree(Object[] arguments) {
        Object object = arguments[0];
        DegreeType degreeType = null;
        if (object instanceof Registration) {
            Registration registration = (Registration) object;
            degreeType = registration.getDegreeType();
        }
        if (object instanceof StudentCurricularPlan) {
            StudentCurricularPlan studentCurricularPlan = (StudentCurricularPlan) object;
            degreeType = studentCurricularPlan.getDegreeType();
        }

        return (degreeType != null && (degreeType.equals(DegreeType.MASTER_DEGREE) || degreeType
                .equals(DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA)));
    }
}