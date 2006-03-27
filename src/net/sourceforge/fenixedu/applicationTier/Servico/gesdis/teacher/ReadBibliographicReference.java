/*
 * Created on 17/Mar/2003
 *
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.gesdis.teacher;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoBibliographicReference;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.BibliographicReference;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;

public class ReadBibliographicReference extends Service {

    public List run(InfoExecutionCourse infoExecutionCourse, Boolean optional)
            throws FenixServiceException, ExcepcaoPersistencia {

        IPersistentExecutionCourse persistentExecutionCourse = persistentSupport
                .getIPersistentExecutionCourse();

        final ExecutionYear executionYear = ExecutionYear.readExecutionYearByName(infoExecutionCourse
                .getInfoExecutionPeriod().getInfoExecutionYear().getYear());

        final ExecutionPeriod executionPeriod = executionYear
                .readExecutionPeriodByName(infoExecutionCourse.getInfoExecutionPeriod().getName());

        final ExecutionCourse executionCourse = persistentExecutionCourse
                .readByExecutionCourseInitialsAndExecutionPeriodId(infoExecutionCourse.getSigla(),
                        executionPeriod.getIdInternal());

        final List<InfoBibliographicReference> infoBibliographicReferences = new ArrayList<InfoBibliographicReference>();
        for (final BibliographicReference bibliographicReference : executionCourse
                .getAssociatedBibliographicReferences()) {
            if (optional == null || bibliographicReference.getOptional().equals(optional)) {
                infoBibliographicReferences.add(InfoBibliographicReference
                        .newInfoFromDomain(bibliographicReference));
            }
        }
        return infoBibliographicReferences;
    }
}