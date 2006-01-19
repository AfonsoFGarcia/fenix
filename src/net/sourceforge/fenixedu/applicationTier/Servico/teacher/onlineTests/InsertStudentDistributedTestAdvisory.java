/*
 * Created on 19/Ago/2003
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import net.sourceforge.fenixedu.domain.Advisory;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author Susana Fernandes
 */
public class InsertStudentDistributedTestAdvisory extends Service {

    public void run(final Integer executionCourseId, final Integer advisoryId, final Integer studentId) throws ExcepcaoPersistencia {
        final ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        Advisory advisory = (Advisory) persistentSuport.getIPersistentObject().readByOID(Advisory.class, advisoryId);
        Student student = (Student) persistentSuport.getIPersistentStudent().readByOID(Student.class, studentId);
        advisory.addPeople(student.getPerson());
    }

}