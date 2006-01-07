package net.sourceforge.fenixedu.persistenceTier.OJB;

import net.sourceforge.fenixedu.domain.CurricularCourseEquivalence;
import net.sourceforge.fenixedu.domain.CurricularCourseEquivalence;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourseEquivalence;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author David Santos in Jun 29, 2004
 */

public class CurricularCourseEquivalenceOJB extends PersistentObjectOJB implements
        IPersistentCurricularCourseEquivalence {

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentCurricularCourseEquivalence#readByEquivalence(Dominio.CurricularCourse,
     *      Dominio.CurricularCourse)
     */
    public CurricularCourseEquivalence readByEquivalence(Integer oldCurricularCourseId,
            Integer equivalentCurricularCourseId, Integer degreeCurricularPlanId) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("equivalentCurricularCourse.idInternal", equivalentCurricularCourseId);
        crit.addEqualTo("oldCurricularCourse.idInternal", oldCurricularCourseId);
        crit.addEqualTo("degreeCurricularPlan.idInternal", degreeCurricularPlanId);
        return (CurricularCourseEquivalence) queryObject(CurricularCourseEquivalence.class, crit);
    }
}