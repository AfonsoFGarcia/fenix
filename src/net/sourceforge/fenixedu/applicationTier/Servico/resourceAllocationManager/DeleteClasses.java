/*
 *
 * Created on 2003/08/15
 */

package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

/**
 * 
 * @author Luis Cruz & Sara Ribeiro
 */
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;

public class DeleteClasses extends Service {

    public Boolean run(final List<Integer> classOIDs) {
	for (final Integer classId : classOIDs) {
	    rootDomainObject.readSchoolClassByOID(classId).delete();
	}
	return Boolean.TRUE;
    }

}
