package net.sourceforge.fenixedu.presentationTier.renderers.providers.executionDegree;

import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.ExecutionDegreeBean;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;

import org.apache.commons.collections.comparators.ReverseComparator;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ExecutionDegreesForDegreeCurricularPlan implements DataProvider {

	@Override
	public Object provide(Object source, Object currentValue) {
		Set<ExecutionDegree> result =
				new TreeSet<ExecutionDegree>(new ReverseComparator(ExecutionDegree.EXECUTION_DEGREE_COMPARATORY_BY_YEAR));

		ExecutionDegreeBean executionDegreeBean = (ExecutionDegreeBean) source;
		DegreeCurricularPlan degreeCurricularPlan = executionDegreeBean.getDegreeCurricularPlan();

		if (degreeCurricularPlan != null) {
			result.addAll(degreeCurricularPlan.getExecutionDegrees());
		}

		return result;
	}

	@Override
	public Converter getConverter() {
		return new DomainObjectKeyConverter();
	}

}
