/*
 * Created on Nov 23, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoProfessorship;
import net.sourceforge.fenixedu.dataTransferObject.InfoProfessorshipWithAll;
import net.sourceforge.fenixedu.dataTransferObject.teacher.professorship.InfoSupportLesson;
import net.sourceforge.fenixedu.dataTransferObject.teacher.professorship.ProfessorshipSupportLessonsDTO;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.SupportLesson;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author jpvl
 */
public class ReadProfessorshipSupportLessons extends Service {

    public ProfessorshipSupportLessonsDTO run(Integer teacherId, Integer executionCourseId)
            throws FenixServiceException, ExcepcaoPersistencia {

        ProfessorshipSupportLessonsDTO professorshipSupportLessonsDTO = new ProfessorshipSupportLessonsDTO();

        Teacher teacher = rootDomainObject.readTeacherByOID(teacherId);
        ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseId);

        Professorship professorship = null;
        if (teacher != null) {
            teacher.getProfessorshipByExecutionCourse(executionCourse);
        }

        InfoProfessorship infoProfessorship = InfoProfessorshipWithAll.newInfoFromDomain(professorship);
        professorshipSupportLessonsDTO.setInfoProfessorship(infoProfessorship);

        List supportLessons = professorship.getSupportLessons();
        List infoSupportLessons = (List) CollectionUtils.collect(supportLessons, new Transformer() {

            public Object transform(Object input) {
                SupportLesson supportLesson = (SupportLesson) input;
                InfoSupportLesson infoSupportLesson = InfoSupportLesson.newInfoFromDomain(supportLesson);
                return infoSupportLesson;
            }
        });

        professorshipSupportLessonsDTO.setInfoSupportLessonList(infoSupportLessons);
        return professorshipSupportLessonsDTO;

    }

}