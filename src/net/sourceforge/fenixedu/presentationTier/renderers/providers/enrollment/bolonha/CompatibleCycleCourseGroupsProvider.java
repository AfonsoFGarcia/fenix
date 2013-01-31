package net.sourceforge.fenixedu.presentationTier.renderers.providers.enrollment.bolonha;

import net.sourceforge.fenixedu.dataTransferObject.student.enrollment.bolonha.CycleEnrolmentBean;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class CompatibleCycleCourseGroupsProvider implements DataProvider {

	@Override
	public Object provide(Object source, Object currentValue) {
		final CycleEnrolmentBean cycleEnrolmentBean = (CycleEnrolmentBean) source;
		return cycleEnrolmentBean.getCycleDestinationAffinities();
	}

	@Override
	public Converter getConverter() {
		return new DomainObjectKeyConverter();
	}

}
