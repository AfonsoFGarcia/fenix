package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetManagementBaseBean;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

import org.apache.commons.beanutils.BeanComparator;

public class DegreeCurricularPlansForDegree implements DataProvider {

    public Object provide(Object source, Object currentValue) {

	final MarkSheetManagementBaseBean markSheetManagementBean = (MarkSheetManagementBaseBean) source;
	final List<DegreeCurricularPlan> result = new ArrayList<DegreeCurricularPlan>();
	if (markSheetManagementBean.getDegree() != null
		&& markSheetManagementBean.getExecutionPeriod() != null) {
	    Campus employeeCampus = getEmployeeCampus();
	    if (employeeCampus != null) {
		ExecutionPeriod executionPeriod = ExecutionPeriod.readBySemesterAndExecutionYear(1, "2006/2007");
		
		if(markSheetManagementBean.getExecutionPeriod().isBeforeOrEquals(executionPeriod)) {
		    
		    if(!employeeCampus.getName().equalsIgnoreCase("Taguspark")) {
			for (DegreeCurricularPlan degreeCurricularPlan : markSheetManagementBean.getDegree()
				.getDegreeCurricularPlansSet()) {
			    if (degreeCurricularPlan.getExecutionDegreeByYear(markSheetManagementBean
				    .getExecutionPeriod().getExecutionYear()) != null) {
				result.add(degreeCurricularPlan);
			    }
			}
		    }
		    
		} else {
		    for (DegreeCurricularPlan degreeCurricularPlan : markSheetManagementBean.getDegree()
			    .getDegreeCurricularPlansSet()) {
			if (degreeCurricularPlan.getExecutionDegreeByYearAndCampus(markSheetManagementBean
				.getExecutionPeriod().getExecutionYear(), employeeCampus) != null) {
			    result.add(degreeCurricularPlan);
			}
		    }		    
		}		
	    }
	}
	Collections.sort(result, new BeanComparator("name"));
	return result;
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

    public Campus getEmployeeCampus() {
	return AccessControl.getPerson().getEmployee().getCurrentCampus();
    }

}
