/*
 * Created on 31/Jul/2003, 19:12:41
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Servico.Seminaries;

import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoSeminaryWithEquivalencies;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoSeminaryWithEquivalenciesWithAll;
import net.sourceforge.fenixedu.domain.Seminaries.Seminary;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.Seminaries.IPersistentSeminary;
import net.sourceforge.fenixedu.presentationTier.Action.Seminaries.Exceptions.BDException;
import net.sourceforge.fenixedu.applicationTier.IService;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 31/Jul/2003, 19:12:41
 * 
 */
public class GetSeminary implements IService {

	public InfoSeminaryWithEquivalencies run(Integer seminaryID) throws BDException, ExcepcaoPersistencia {
		InfoSeminaryWithEquivalencies infoSeminary = null;

		ISuportePersistente persistenceSupport = PersistenceSupportFactory
				.getDefaultPersistenceSupport();
		IPersistentSeminary persistentSeminary = persistenceSupport.getIPersistentSeminary();
		Seminary seminary = (Seminary) persistentSeminary.readByOID(Seminary.class, seminaryID);
		if (seminary != null) {

			infoSeminary = InfoSeminaryWithEquivalenciesWithAll.newInfoFromDomain(seminary);
		}

		return infoSeminary;
	}
}