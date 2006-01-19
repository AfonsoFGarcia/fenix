package net.sourceforge.fenixedu.applicationTier.Servico.gep.inquiries;

import java.util.Date;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.IService;

public class DefineInquiryResponsePeriod implements IService {

    public void run(final Integer executionPeriodID, final Date inquiryResponseBegin, final Date inquiryResponseEnd)
            throws ExcepcaoPersistencia {
        final ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentObject persistentObject = persistentSupport.getIPersistentObject();

        final ExecutionPeriod executionPeriod = (ExecutionPeriod) persistentObject.readByOID(
                ExecutionPeriod.class, executionPeriodID);

        executionPeriod.setInquiryResponseBegin(inquiryResponseBegin);
        executionPeriod.setInquiryResponseEnd(inquiryResponseEnd);
    }

}
