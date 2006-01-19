package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide;

import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Guide;
import net.sourceforge.fenixedu.domain.transactions.PaymentType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * 
 * @author <a href="mailto:shezad@ist.utl.pt">Shezad Anavarali </a>
 * 
 */
public class EditGuideInformationInManager extends Service {

    public void run(Integer guideID, Integer degreeCurricularPlanID, String executionYear, String newPaymentType)
            throws ExcepcaoPersistencia {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        Guide guide = (Guide) sp.getIPersistentGuide().readByOID(Guide.class, guideID);
        ExecutionDegree cursoExecucao = sp.getIPersistentExecutionDegree()
                .readByDegreeCurricularPlanIDAndExecutionYear(degreeCurricularPlanID, executionYear);

        guide.setPaymentType(PaymentType.valueOf(newPaymentType));
        guide.setExecutionDegree(cursoExecucao);

    }

}
