/*
 * Created on 14/Ago/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author lmac1
 */

public class ReadAllExecutionYears implements IService {

	public List run() throws FenixServiceException, ExcepcaoPersistencia {
		ISuportePersistente sp;
		List allExecutionYears = null;

		sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		allExecutionYears = (List) sp.getIPersistentExecutionYear().readAll(ExecutionYear.class);

		if (allExecutionYears == null || allExecutionYears.isEmpty())
			return allExecutionYears;

		// build the result of this service
		Iterator iterator = allExecutionYears.iterator();
		List result = new ArrayList(allExecutionYears.size());

		while (iterator.hasNext())
			result.add(InfoExecutionYear.newInfoFromDomain((ExecutionYear) iterator.next()));

		return result;
	}
}