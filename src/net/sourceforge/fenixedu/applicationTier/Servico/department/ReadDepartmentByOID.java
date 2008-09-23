/*
 * 
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.department;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDepartment;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Jo�o Fialho & Rita Ferreira
 */
public class ReadDepartmentByOID extends FenixService {

    public InfoDepartment run(Integer oid) throws FenixServiceException {
	Department department = rootDomainObject.readDepartmentByOID(oid);
	return InfoDepartment.newInfoFromDomain(department);
    }
}