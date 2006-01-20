package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculum;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Curriculum;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurriculum;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author Fernanda Quit�rio
 * 
 * Modified by T�nia Pous�o at 2/Dez/2003
 */

public class EditObjectives extends Service {

    public boolean run(Integer infoExecutionCourseCode, Integer infoCurricularCourseCode,
            InfoCurriculum infoCurriculumNew, String username) throws FenixServiceException,
            ExcepcaoPersistencia {

        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentCurriculum persistentCurriculum = persistentSupport.getIPersistentCurriculum();
        IPessoaPersistente persistentPerson = persistentSupport.getIPessoaPersistente();
        IPersistentCurricularCourse persistentCurricularCourse = persistentSupport.getIPersistentCurricularCourse();
        
        // Person who change all information        
        Person person = persistentPerson.lerPessoaPorUsername(username);
        if (person == null) {
            throw new NonExistingServiceException("noPerson");
        }

        // inexistent new information
        if (infoCurriculumNew == null) {
            throw new FenixServiceException("nullCurriculum");
        }

        // Curricular Course
        if (infoCurricularCourseCode == null) {
            throw new FenixServiceException("nullCurricularCourseCode");
        }

        CurricularCourse curricularCourse = (CurricularCourse) persistentCurricularCourse.readByOID(
                CurricularCourse.class, infoCurricularCourseCode);
        if (curricularCourse == null) {
            throw new NonExistingServiceException("noCurricularCourse");
        }

        // Curriculum       
        Curriculum curriculum = persistentCurriculum.readCurriculumByCurricularCourse(curricularCourse
                .getIdInternal());

        if (curriculum == null) {                    
            curriculum = curricularCourse.insertCurriculum("", "", "", "", "", "");
        }

        IPersistentExecutionYear persistentExecutionYear = persistentSupport.getIPersistentExecutionYear();
        ExecutionYear currentExecutionYear = persistentExecutionYear.readCurrentExecutionYear();

        if (!curriculum.getLastModificationDate().before(currentExecutionYear.getBeginDate())
                && !curriculum.getLastModificationDate().after(currentExecutionYear.getEndDate())) {
                        
            curriculum.edit(infoCurriculumNew.getGeneralObjectives(), infoCurriculumNew
                    .getOperacionalObjectives(), curriculum.getProgram(), infoCurriculumNew
                    .getGeneralObjectivesEn(), infoCurriculumNew.getOperacionalObjectivesEn(),
                    curriculum.getProgramEn(), null, person);

            curriculum.edit(infoCurriculumNew.getGeneralObjectives(), infoCurriculumNew
                    .getOperacionalObjectives(), curriculum.getProgram(), infoCurriculumNew
                    .getGeneralObjectivesEn(), infoCurriculumNew.getOperacionalObjectivesEn(),
                    curriculum.getProgramEn(), "Eng", person);
           
        } else {
          
            Curriculum newCurriculum;

            newCurriculum = curricularCourse.insertCurriculum(curriculum.getProgram(), curriculum
                    .getProgramEn(), infoCurriculumNew.getOperacionalObjectives(), infoCurriculumNew
                    .getOperacionalObjectivesEn(), infoCurriculumNew.getGeneralObjectives(),
                    infoCurriculumNew.getGeneralObjectivesEn());

            newCurriculum.setPersonWhoAltered(person);
        }
        
        return true;
    }
}