/*
 * Created on 14/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.gesdis;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.framework.DomainObjectAuthorizationFilter;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class ReadCourseInformationAuthorizationFilter extends DomainObjectAuthorizationFilter {

    protected RoleType getRoleType() {
        return RoleType.TEACHER;
    }

    protected boolean verifyCondition(IUserView id, Integer executionCourseID) {
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();
            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
            Teacher teacher = persistentTeacher.readTeacherByUsername(id.getUtilizador());

            ExecutionCourse executionCourse = (ExecutionCourse) persistentExecutionCourse.readByOID(
                    ExecutionCourse.class, executionCourseID);

            List<Professorship> responsiblesFor = executionCourse.responsibleFors();

            for (Professorship responsibleFor : responsiblesFor) {
                if (responsibleFor.getTeacher().equals(teacher))
                    return true;
            }

            return false;

        } catch (ExcepcaoPersistencia e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}