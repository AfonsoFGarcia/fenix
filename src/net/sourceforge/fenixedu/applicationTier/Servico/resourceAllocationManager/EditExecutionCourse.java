package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager.CourseLoadBean;
import net.sourceforge.fenixedu.domain.ExecutionCourse;

public class EditExecutionCourse extends FenixService {

    public void run(CourseLoadBean bean) throws FenixServiceException {
	if (bean != null) {
	    ExecutionCourse executionCourse = bean.getExecutionCourse();
	    executionCourse.editCourseLoad(bean.getType(), bean.getUnitQuantity(), bean.getTotalQuantity());
	}
    }
}
