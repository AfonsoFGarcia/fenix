/*
 * Created on 24/Ago/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExportGrouping;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGrouping;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.ProposalState;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author joaosa & rmalo
 */

public class PrepareEditGroupingMembers implements IService {

    public List run(Integer executionCourseCode, Integer groupingID)
            throws FenixServiceException, ExcepcaoPersistencia {

        final ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();        
        final IPersistentGrouping persistentGrouping = persistentSupport.getIPersistentGrouping();
        final Grouping grouping = (Grouping) persistentGrouping.readByOID(Grouping.class, groupingID);
        if (grouping == null) {
            throw new InvalidArgumentsServiceException();
        }
        
        final List<Attends> attends = new ArrayList<Attends>();
        final List<InfoStudent> infoStudents = new ArrayList<InfoStudent>();
        
        for (final ExportGrouping exportGrouping : grouping.getExportGroupings()) {
            if (exportGrouping.getProposalState().getState() == ProposalState.ACEITE
                    || exportGrouping.getProposalState().getState() == ProposalState.CRIADOR) {
                for (final Attends attend : exportGrouping.getExecutionCourse().getAttends()) {
                    if (!attends.contains(attend) && !grouping.getAttends().contains(attend)) {
                        attends.add(attend);
                        infoStudents.add(InfoStudent.newInfoFromDomain(attend.getAluno()));
                    }
                }
            }
        }
        return infoStudents;       
    }
}
