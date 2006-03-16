/*
 * Created on 27/Mai/2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship.ReadDetailedTeacherProfessorshipsAbstractService;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentProfessorship;

/**
 * @author jpvl
 */
public class ReadProfessorships extends ReadDetailedTeacherProfessorshipsAbstractService {

    public List run(IUserView userView, Integer executionPeriodCode) throws ExcepcaoPersistencia {

        IPersistentProfessorship persistentProfessorship = persistentSupport
                .getIPersistentProfessorship();

        ExecutionPeriod executionPeriod = null;
        if (executionPeriodCode != null) {
            executionPeriod = (ExecutionPeriod) persistentObject.readByOID(
                    ExecutionPeriod.class, executionPeriodCode);
        }

        Teacher teacher = Teacher.readTeacherByUsername(userView.getUtilizador());

        List professorships = persistentProfessorship.readByTeacher(teacher.getIdInternal());
        List professorshipsList = new ArrayList();
        professorshipsList.addAll(professorships);

        if (executionPeriod != null) {
            Iterator iterProfessorships = professorships.iterator();
            while (iterProfessorships.hasNext()) {
                Professorship professorship = (Professorship) iterProfessorships.next();
                if (!professorship.getExecutionCourse().getExecutionPeriod().equals(executionPeriod)) {
                    professorshipsList.remove(professorship);
                }
            }
        }

        final List responsibleFors = teacher.responsibleFors();
        List responsibleForsList = new ArrayList();
        responsibleForsList.addAll(responsibleFors);

        if (executionPeriod != null) {
            Iterator iterResponsibleFors = responsibleFors.iterator();
            while (iterResponsibleFors.hasNext()) {
                Professorship responsibleFor = (Professorship) iterResponsibleFors.next();
                if (!responsibleFor.getExecutionCourse().getExecutionPeriod().equals(executionPeriod)) {
                    responsibleForsList.remove(responsibleFor);
                }
            }
        }

        List detailedProfessorshipList = getDetailedProfessorships(professorshipsList,
                responsibleForsList, persistentSupport);
        return detailedProfessorshipList;
    }
}