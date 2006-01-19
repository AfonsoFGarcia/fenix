/**
 * Nov 30, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.services;

import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class CreateOtherService extends Service {

    public void run(Integer teacherID, Integer executionPeriodID, Double credits, String reason)
            throws ExcepcaoPersistencia {

        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        Teacher teacher = (Teacher) persistentSupport.getIPersistentTeacher().readByOID(Teacher.class,
                teacherID);
        ExecutionPeriod executionPeriod = (ExecutionPeriod) persistentSupport
                .getIPersistentExecutionPeriod().readByOID(ExecutionPeriod.class, executionPeriodID);
        
        TeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionPeriod);
        if(teacherService == null){
            teacherService = DomainFactory.makeTeacherService(teacher,executionPeriod);
        }
        DomainFactory.makeOtherService(teacherService,credits,reason);
    }
}
