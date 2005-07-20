/*
 * Created on Feb 18, 2005
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Luis Cruz
 * 
 */
public class DeleteStudentCurricularPlan implements IService {

    public void run(final Integer studentCurricularPlanId) throws DomainException, ExcepcaoPersistencia, NonExistingServiceException {

        final ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentStudentCurricularPlan persistentStudentCurricularPlan = persistentSupport
                .getIStudentCurricularPlanPersistente();
        final IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) persistentStudentCurricularPlan
                .readByOID(StudentCurricularPlan.class, studentCurricularPlanId);

		if (studentCurricularPlan != null)
			studentCurricularPlan.delete();
		else
			throw new NonExistingServiceException();
    }
}