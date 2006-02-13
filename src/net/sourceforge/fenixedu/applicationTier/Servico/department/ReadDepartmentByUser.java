/*
 * Created on Dec 1, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.department;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDepartment;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;

/**
 * @author jpvl
 */
public class ReadDepartmentByUser extends Service {

    public InfoDepartment run(String username) throws FenixServiceException, ExcepcaoPersistencia {
        InfoDepartment infoDepartment = null;

        IPessoaPersistente personDAO = persistentSupport.getIPessoaPersistente();
        Person person = Person.readPersonByUsername(username);
        List departmentList = person.getManageableDepartmentCredits();
        if (departmentList != null && !departmentList.isEmpty()) {
            infoDepartment = InfoDepartment.newInfoFromDomain((Department) departmentList.get(0));
        }

        return infoDepartment;
    }
}