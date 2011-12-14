package net.sourceforge.fenixedu.presentationTier.renderers.providers.choiceType.replacement.single;

import java.util.ArrayList;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ExecutionSemesterProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
	return new ArrayList(ExecutionSemester.readNotClosedExecutionPeriods());
    }

    @Override
    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
