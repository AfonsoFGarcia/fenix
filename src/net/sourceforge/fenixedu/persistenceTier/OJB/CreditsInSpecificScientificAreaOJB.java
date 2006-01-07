package net.sourceforge.fenixedu.persistenceTier.OJB;

import net.sourceforge.fenixedu.domain.CreditsInScientificArea;
import net.sourceforge.fenixedu.domain.CreditsInScientificArea;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCreditsInSpecificScientificArea;

import org.apache.ojb.broker.query.Criteria;

public class CreditsInSpecificScientificAreaOJB extends PersistentObjectOJB implements
        IPersistentCreditsInSpecificScientificArea {
    public CreditsInSpecificScientificAreaOJB() {
    }

    public CreditsInScientificArea readByStudentCurricularPlanAndEnrollmentAndScientificArea(
            Integer studentCurricularPlanKey, Integer enrolmentKey,
            Integer scientificAreaKey) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("studentCurricularPlan.idInternal", studentCurricularPlanKey);
        criteria.addEqualTo("enrolment.idInternal", enrolmentKey);
        criteria.addEqualTo("scientificArea.idInternal", scientificAreaKey);
        return (CreditsInScientificArea) queryObject(CreditsInScientificArea.class, criteria);
    }
}