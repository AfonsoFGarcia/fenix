package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Department;

public class RemoveDegreeFromDepartment extends FenixService {

    public void run(final Department department, final Degree degree) {
	if (department != null && degree != null) {
	    department.removeDegrees(degree);
	}
    }

}
