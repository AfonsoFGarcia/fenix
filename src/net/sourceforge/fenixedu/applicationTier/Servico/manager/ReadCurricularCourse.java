/*
 * Created on 16/Ago/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author lmac1
 */
public class ReadCurricularCourse extends FenixService {

    /**
     * Executes the service. Returns the current InfoCurricularCourse.
     * 
     * @throws ExcepcaoPersistencia
     */
    public InfoCurricularCourse run(Integer idInternal) throws FenixServiceException {
	CurricularCourse curricularCourse;
	curricularCourse = (CurricularCourse) rootDomainObject.readDegreeModuleByOID(idInternal);

	if (curricularCourse == null) {
	    throw new NonExistingServiceException();
	}

	return InfoCurricularCourse.newInfoFromDomain(curricularCourse);
    }
}