package net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.tests.NewQuestion;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DeletePreCondition extends FenixService {
    public void run(NewQuestion question) throws FenixServiceException {
	question.setPreCondition(null);
    }
}
