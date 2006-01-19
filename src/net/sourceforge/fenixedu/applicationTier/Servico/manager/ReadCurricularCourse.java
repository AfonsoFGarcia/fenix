/*
 * Created on 16/Ago/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseWithInfoDegreeAndScopes;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author lmac1
 */
public class ReadCurricularCourse extends Service {

	/**
	 * Executes the service. Returns the current InfoCurricularCourse.
	 * @throws ExcepcaoPersistencia 
	 */
	public InfoCurricularCourse run(Integer idInternal) throws FenixServiceException, ExcepcaoPersistencia {
		CurricularCourse curricularCourse;
		ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		curricularCourse = (CurricularCourse) sp.getIPersistentCurricularCourse().readByOID(
				CurricularCourse.class, idInternal);

		if (curricularCourse == null) {
			throw new NonExistingServiceException();
		}

		return InfoCurricularCourseWithInfoDegreeAndScopes.newInfoFromDomain(curricularCourse);
	}
}