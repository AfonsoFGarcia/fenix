package net.sourceforge.fenixedu.applicationTier.Servico.degreeAdministrativeOffice;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class CreateCurricularCourseEquivalency extends Service {

    public void run(final Integer degreeCurricularPlanID, final Integer curricularCourseID, 
            final Integer oldCurricularCourseID) throws ExcepcaoPersistencia {
        final DegreeCurricularPlan degreeCurricularPlan = rootDomainObject.readDegreeCurricularPlanByOID(degreeCurricularPlanID);
        final CurricularCourse curricularCourse = (CurricularCourse) rootDomainObject.readDegreeModuleByOID(curricularCourseID);
        final CurricularCourse oldCurricularCourse = (CurricularCourse) rootDomainObject.readDegreeModuleByOID(oldCurricularCourseID);

        DomainFactory.makeCurricularCourseEquivalence(degreeCurricularPlan, curricularCourse, oldCurricularCourse);
    }

}
