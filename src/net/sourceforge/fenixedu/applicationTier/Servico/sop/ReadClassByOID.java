/*
 * Created on 2003/08/01
 *
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 * 
 */
public class ReadClassByOID extends Service {

	public InfoClass run(Integer oid) throws FenixServiceException, ExcepcaoPersistencia {
		InfoClass result = null;
		SchoolClass turma = (SchoolClass) persistentObject.readByOID(SchoolClass.class, oid);
		if (turma != null) {
			result = InfoClass.newInfoFromDomain(turma);
		}

		return result;
	}
}