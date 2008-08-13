package net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.tests.NewQuestion;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DeleteQuestion extends Service {
	public void run(NewQuestion question) throws FenixServiceException{
		question.delete();
	}
}
