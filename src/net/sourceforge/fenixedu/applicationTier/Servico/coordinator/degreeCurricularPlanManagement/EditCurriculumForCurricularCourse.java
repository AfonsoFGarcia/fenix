package net.sourceforge.fenixedu.applicationTier.Servico.coordinator.degreeCurricularPlanManagement;

import java.util.Calendar;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculum;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Curriculum;
import net.sourceforge.fenixedu.domain.DomainFactory;
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
 * @author Fernanda Quit�rio 21/Nov/2003
 * 
 */
public class EditCurriculumForCurricularCourse extends Service {

    public Boolean run(Integer infoExecutionDegreeId, Integer oldCurriculumId,
            Integer curricularCourseCode, InfoCurriculum newInfoCurriculum, String username,
            String language) throws FenixServiceException, ExcepcaoPersistencia {
        Boolean result = new Boolean(false);

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentCurriculum persistentCurriculum = sp.getIPersistentCurriculum();
        IPessoaPersistente persistentPerson = sp.getIPessoaPersistente();
        IPersistentExecutionYear persistentExecutionYear = sp.getIPersistentExecutionYear();
        IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();

        if (oldCurriculumId == null) {
            throw new FenixServiceException("nullCurriculumCode");
        }
        if (curricularCourseCode == null) {
            throw new FenixServiceException("nullCurricularCourseCode");
        }
        if (newInfoCurriculum == null) {
            throw new FenixServiceException("nullCurriculum");
        }
        if (username == null) {
            throw new FenixServiceException("nullUsername");
        }

        CurricularCourse curricularCourse = (CurricularCourse) persistentCurricularCourse.readByOID(
                CurricularCourse.class, curricularCourseCode);
        if (curricularCourse == null) {
            throw new NonExistingServiceException("noCurricularCourse");
        }

        Person person = persistentPerson.lerPessoaPorUsername(username);
        if (person == null) {
            throw new NonExistingServiceException("noPerson");
        }

        Curriculum oldCurriculum = (Curriculum) persistentCurriculum.readByOID(Curriculum.class,
                oldCurriculumId);

        if (oldCurriculum == null) {
            oldCurriculum = DomainFactory.makeCurriculum();

            oldCurriculum.setCurricularCourse(curricularCourse);
            Calendar today = Calendar.getInstance();
            oldCurriculum.setLastModificationDate(today.getTime());
        }

        ExecutionYear currentExecutionYear = persistentExecutionYear.readCurrentExecutionYear();

        if (!oldCurriculum.getLastModificationDate().before(currentExecutionYear.getBeginDate())
                && !oldCurriculum.getLastModificationDate().after(currentExecutionYear.getEndDate())) {

            oldCurriculum.edit(newInfoCurriculum.getGeneralObjectives(), newInfoCurriculum
                    .getOperacionalObjectives(), newInfoCurriculum.getProgram(), newInfoCurriculum
                    .getGeneralObjectivesEn(), newInfoCurriculum.getOperacionalObjectivesEn(),
                    newInfoCurriculum.getProgramEn(), language, person);

        } else {
            Curriculum newCurriculum = DomainFactory.makeCurriculum();
            newCurriculum.setCurricularCourse(curricularCourse);

            newCurriculum.edit(newInfoCurriculum.getGeneralObjectives(), newInfoCurriculum
                    .getOperacionalObjectives(), newInfoCurriculum.getProgram(), newInfoCurriculum
                    .getGeneralObjectivesEn(), newInfoCurriculum.getOperacionalObjectivesEn(),
                    newInfoCurriculum.getProgramEn(), language, person);

        }
        result = Boolean.TRUE;

        return result;
    }
}