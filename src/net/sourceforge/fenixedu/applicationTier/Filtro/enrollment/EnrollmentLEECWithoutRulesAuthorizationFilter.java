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
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author T�nia Pous�o
 * 
 */
public class EnrollmentLEECWithoutRulesAuthorizationFilter extends AuthorizationByManyRolesFilter {
    private static String DEGREE_LEEC_CODE = "LEEC";

    private static DegreeType DEGREE_TYPE = DegreeType.DEGREE;

    @Override
    protected Collection<RoleType> getNeededRoleTypes() {
        List<RoleType> roles = new ArrayList<RoleType>();
        roles.add(RoleType.DEGREE_ADMINISTRATIVE_OFFICE);
        roles.add(RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER);
        return roles;
    }

    protected String hasPrevilege(IUserView id, Object[] arguments) {
        try {
            // verify if the degree type is LICENCIATURA_OBJ
            if (!verifyDegreeTypeIsNonMaster(arguments)) {
                return "error.degree.type";
            }

            // verify if the student to enroll is a non master degree student
            if (!verifyStudentNonMasterDegree(arguments)) {
                return "error.student.degree.nonMaster";
            }

            // verify if the student to enroll is a LEEC degree student
            if (!verifyStudentLEEC(arguments)) {
                return "error.student.degree.nonMaster";
            }

            return null;
        } catch (Exception exception) {
            exception.printStackTrace();
            return "noAuthorization";
        }
    }

    private boolean verifyDegreeTypeIsNonMaster(Object[] arguments) {
        boolean isNonMaster = false;

        if (arguments != null && arguments[1] != null) {
            isNonMaster = DEGREE_TYPE.equals(arguments[1]);
        }

        return isNonMaster;
    }

    private boolean verifyStudentNonMasterDegree(Object[] arguments) throws ExcepcaoPersistencia {
        boolean isNonMaster = false;

        if (arguments != null && arguments[0] != null) {
            Integer studentNumber = ((InfoStudent) arguments[0]).getNumber();
            if (studentNumber != null) {
                Registration registration = Registration.readStudentByNumberAndDegreeType(studentNumber, DEGREE_TYPE);
                if (registration != null) {
                    isNonMaster = true; // non master student
                }
            }
        }

        return isNonMaster;
    }

    // with student number and degree type
    private boolean verifyStudentLEEC(Object[] arguments) throws ExcepcaoPersistencia {
        boolean isLEEC = false;

        if (arguments != null && arguments[0] != null) {
            Integer studentNumber = ((InfoStudent) arguments[0]).getNumber();
            if (studentNumber != null) {
                Registration registration = Registration.readStudentByNumberAndDegreeType(studentNumber, DEGREE_TYPE);
                StudentCurricularPlan studentCurricularPlan = null;
                if(registration != null) {
                	studentCurricularPlan = registration.getActiveStudentCurricularPlan();
                }
                if (studentCurricularPlan != null
                        && studentCurricularPlan.getDegreeCurricularPlan() != null
                        && studentCurricularPlan.getDegreeCurricularPlan().getDegree() != null) {
                    String degreeCode = studentCurricularPlan.getDegreeCurricularPlan().getDegree()
                            .getSigla();
                    isLEEC = degreeCode.startsWith(DEGREE_LEEC_CODE);
                }
            }
        }

        return isLEEC;
    }
}
