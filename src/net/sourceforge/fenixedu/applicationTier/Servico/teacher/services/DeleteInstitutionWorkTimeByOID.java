/**
 * Nov 24, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.services;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.teacher.InstitutionWorkTime;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class DeleteInstitutionWorkTimeByOID extends FenixService {

    public void run(Integer institutionWorkTimeID, RoleType roleType) {

	InstitutionWorkTime institutionWorkTime = (InstitutionWorkTime) rootDomainObject
		.readTeacherServiceItemByOID(institutionWorkTimeID);
	if (institutionWorkTime != null) {
	    institutionWorkTime.delete(roleType);
	}
    }

}
