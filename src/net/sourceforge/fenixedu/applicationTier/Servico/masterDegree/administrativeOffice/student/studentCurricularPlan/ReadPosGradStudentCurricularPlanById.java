package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.student.studentCurricularPlan;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author T�nia Pous�o 6/Out/2003
 */

public class ReadPosGradStudentCurricularPlanById extends Service {

    public Object run(Integer studentCurricularPlanId) {
        StudentCurricularPlan studentCurricularPlan = rootDomainObject.readStudentCurricularPlanByOID(studentCurricularPlanId);
        return studentCurricularPlan == null ? null : InfoStudentCurricularPlan.newInfoFromDomain(studentCurricularPlan);
    }

}