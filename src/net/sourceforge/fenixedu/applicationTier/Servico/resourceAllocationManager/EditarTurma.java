package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.SchoolClass;

public class EditarTurma extends Service {

    public Object run(final Integer idInternal, final String className, final Integer curricularYear, final InfoExecutionDegree infoExecutionDegree,
	    final InfoExecutionPeriod infoExecutionPeriod) throws ExistingServiceException {

	final SchoolClass classToEdit = rootDomainObject.readSchoolClassByOID(idInternal);	
	classToEdit.edit(className);
	return InfoClass.newInfoFromDomain(classToEdit);
    }

}
