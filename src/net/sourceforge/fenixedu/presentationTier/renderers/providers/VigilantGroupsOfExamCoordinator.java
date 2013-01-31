package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.List;

import net.sourceforge.fenixedu.domain.vigilancy.ExamCoordinator;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import net.sourceforge.fenixedu.presentationTier.Action.vigilancy.VigilantGroupBean;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class VigilantGroupsOfExamCoordinator implements DataProvider {

	@Override
	public Object provide(Object source, Object currentValue) {

		VigilantGroupBean bean = (VigilantGroupBean) source;

		ExamCoordinator coordinator = bean.getExamCoordinator();
		List<VigilantGroup> vigilantGroups = (coordinator == null) ? bean.getVigilantGroups() : coordinator.getVigilantGroups();

		return vigilantGroups;
	}

	@Override
	public Converter getConverter() {
		return new DomainObjectKeyConverter();
	}

}