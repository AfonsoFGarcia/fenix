package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegreeEditor;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.space.Campus;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class EditExecutionDegree extends FenixService {

    @Checked("RolePredicates.MANAGER_OR_OPERATOR_PREDICATE")
    @Service
    public static void run(InfoExecutionDegreeEditor infoExecutionDegree) throws FenixServiceException {

        final ExecutionDegree oldExecutionDegree = rootDomainObject.readExecutionDegreeByOID(infoExecutionDegree.getIdInternal());
        if (oldExecutionDegree == null) {
            throw new NonExistingServiceException("message.nonExistingExecutionDegree", null);
        }

        final Campus campus = (Campus) rootDomainObject.readResourceByOID(infoExecutionDegree.getInfoCampus().getIdInternal());
        if (campus == null) {
            throw new NonExistingServiceException("message.nonExistingCampus", null);
        }

        final ExecutionYear executionYear =
                rootDomainObject.readExecutionYearByOID(infoExecutionDegree.getInfoExecutionYear().getIdInternal());
        if (executionYear == null) {
            throw new NonExistingServiceException("message.non.existing.execution.year", null);
        }

        oldExecutionDegree.setCampus(campus);
        oldExecutionDegree.setExecutionYear(executionYear);
        oldExecutionDegree.setTemporaryExamMap(infoExecutionDegree.getTemporaryExamMap());
    }

}