/*
 * Created on 8/Ago/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDegreeCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.exceptions.ExistingPersistentException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author lmac1
 */

public class InsertCurricularCourseAtDegreeCurricularPlan implements IService {

    public void run(InfoCurricularCourse infoCurricularCourse) throws FenixServiceException, ExcepcaoPersistencia {

        try {
            ISuportePersistente persistentSuport = PersistenceSupportFactory
                    .getDefaultPersistenceSupport();

            Integer degreeCurricularPlanId = infoCurricularCourse.getInfoDegreeCurricularPlan()
                    .getIdInternal();
            IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = persistentSuport
                    .getIPersistentDegreeCurricularPlan();

            IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) persistentDegreeCurricularPlan
                    .readByOID(DegreeCurricularPlan.class, degreeCurricularPlanId);

            if (degreeCurricularPlan == null)
                throw new NonExistingServiceException();

            String name = infoCurricularCourse.getName();
            String nameEn = infoCurricularCourse.getNameEn();
            String code = infoCurricularCourse.getCode();
            final String acronym = infoCurricularCourse.getAcronym();

            List curricularCourses = null;
            curricularCourses = degreeCurricularPlan.getCurricularCourses();

            ICurricularCourse cCourse = (ICurricularCourse) CollectionUtils.find(curricularCourses,
                    new Predicate() {

                        public boolean evaluate(Object arg0) {
                            ICurricularCourse curricularCourse = (ICurricularCourse) arg0;
                            if (acronym.equalsIgnoreCase(curricularCourse.getAcronym()))
                                return true;
                            return false;
                        }
                    });

            if (cCourse == null) {
                ICurricularCourse curricularCourse = degreeCurricularPlan.createCurricularCourse(name,
                        code, infoCurricularCourse.getAcronym(), infoCurricularCourse
                                .getEnrollmentAllowed(), CurricularStage.OLD); 
                curricularCourse.setBasic(infoCurricularCourse.getBasic());
                curricularCourse.setMandatory(infoCurricularCourse.getMandatory());
                curricularCourse.setNameEn(nameEn);
                curricularCourse.setType(infoCurricularCourse.getType());
                curricularCourse.setTheoreticalHours(infoCurricularCourse.getTheoreticalHours());
                curricularCourse.setTheoPratHours(infoCurricularCourse.getTheoPratHours());
                curricularCourse.setPraticalHours(infoCurricularCourse.getPraticalHours());
                curricularCourse.setLabHours(infoCurricularCourse.getLabHours());
                curricularCourse.setMaximumValueForAcumulatedEnrollments(infoCurricularCourse
                        .getMaximumValueForAcumulatedEnrollments());
                curricularCourse.setMinimumValueForAcumulatedEnrollments(infoCurricularCourse
                        .getMinimumValueForAcumulatedEnrollments());
                curricularCourse.setCredits(infoCurricularCourse.getCredits());
                curricularCourse.setEctsCredits(infoCurricularCourse.getEctsCredits());
                curricularCourse.setEnrollmentWeigth(infoCurricularCourse.getEnrollmentWeigth());
                curricularCourse.setWeigth(infoCurricularCourse.getWeigth());
                curricularCourse.setMandatoryEnrollment(infoCurricularCourse.getMandatoryEnrollment());
            } else {
                throw new ExistingAcronymException();
            }

        } catch (ExistingPersistentException existingException) {
            throw new ExistingServiceException("A disciplina curricular "
                    + infoCurricularCourse.getName() + ", com c�digo " + infoCurricularCourse.getCode(),
                    existingException);
        }
    }

    public class ExistingAcronymException extends FenixServiceException {

        public ExistingAcronymException() {
        }

        public ExistingAcronymException(String message) {
            super(message);
        }

        public ExistingAcronymException(Throwable cause) {
            super(cause);
        }

        public ExistingAcronymException(String message, Throwable cause) {
            super(message, cause);
        }

        @Override
        public String toString() {
            String result = "[ExistingAcronymException\n";
            result += "message" + this.getMessage() + "\n";
            result += "cause" + this.getCause() + "\n";
            result += "]";
            return result;
        }

    }
}
