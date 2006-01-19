/*
 * Created on 23/Jul/2003
 *
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Curriculum;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurriculum;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.IService;

/**
 * @author Jo�o Mota
 * 
 * 23/Jul/2003 fenix-head ServidorAplicacao.Servico.scientificCouncil
 * 
 */
public class InsertCurriculum implements IService {

    public Boolean run(Integer curricularCourseId, String program, String programEn,
            String operacionalObjectives, String operacionalObjectivesEn, String generalObjectives,
            String generalObjectivesEn, Boolean basic) throws FenixServiceException,
            ExcepcaoPersistencia {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentCurriculum persistentCurriculum = sp.getIPersistentCurriculum();
        IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();
        CurricularCourse curricularCourse = (CurricularCourse) persistentCurricularCourse.readByOID(
                CurricularCourse.class, curricularCourseId);

        if (curricularCourse == null) {
            throw new InvalidArgumentsServiceException();
        }

        Curriculum curriculumFromDB = persistentCurriculum
                .readCurriculumByCurricularCourse(curricularCourse.getIdInternal());

        if (curriculumFromDB != null) {
            throw new InvalidArgumentsServiceException();
        }

        if (curricularCourse.getBasic().equals(basic)) {
            
            curricularCourse.insertCurriculum(program, programEn, operacionalObjectives,
                    operacionalObjectivesEn, generalObjectives, generalObjectivesEn);
           
            return new Boolean(true);
        }

        return new Boolean(false);
    }
}