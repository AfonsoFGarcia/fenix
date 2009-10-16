package net.sourceforge.fenixedu.presentationTier.renderers.providers.payments;

import java.util.Arrays;

import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.AcademicServiceRequestJustificationType;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.converters.EnumConverter;

public class AcademicServiceRequestJustificationTypeProvider implements DataProvider {

    @Override
    public Converter getConverter() {
	return new EnumConverter();
    }

    @Override
    public Object provide(Object arg0, Object arg1) {
	return Arrays.asList(AcademicServiceRequestJustificationType.values());
    }

}
