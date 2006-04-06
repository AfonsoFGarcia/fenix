/*
 * Created on 21/Jul/2003
 *
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.Summary;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Jo�o Mota
 * @author Susana Fernandes
 * 
 * 21/Jul/2003 fenix-head ServidorAplicacao.Servico.teacher
 * 
 */
public class DeleteSummary extends Service {

    public boolean run(Integer executionCourseId, Integer summaryID) throws FenixServiceException,
            ExcepcaoPersistencia {
        final Summary summary = rootDomainObject.readSummaryByOID(summaryID);
        if (summary == null)
            throw new InvalidArgumentsServiceException();

        summary.delete();
                
        return true;
    }

}