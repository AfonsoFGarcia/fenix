/*
 * Created on 10/Jan/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoGratuitySituation;
import net.sourceforge.fenixedu.dataTransferObject.InfoGratuitySituationWithInfoPersonAndInfoExecutionDegree;
import net.sourceforge.fenixedu.domain.GratuitySituation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGratuitySituation;

/**
 * @author T�nia Pous�o
 * 
 */
public class ReadGratuitySituationByStudentCurricularPlanByGratuityValues extends Service {

	public Object run(Integer studentCurricularPlanID, Integer gratuityValuesID)
			throws FenixServiceException, ExcepcaoPersistencia {
		GratuitySituation gratuitySituation = null;

		IPersistentGratuitySituation persistentGratuitySituation = persistentSupport.getIPersistentGratuitySituation();

		gratuitySituation = persistentGratuitySituation
				.readGratuitySituatuionByStudentCurricularPlanAndGratuityValues(studentCurricularPlanID,
						gratuityValuesID);

		InfoGratuitySituation infoGratuitySituation = null;
		if (gratuitySituation != null) {
			infoGratuitySituation = InfoGratuitySituationWithInfoPersonAndInfoExecutionDegree
					.newInfoFromDomain(gratuitySituation);
		}

		return infoGratuitySituation;
	}
}
