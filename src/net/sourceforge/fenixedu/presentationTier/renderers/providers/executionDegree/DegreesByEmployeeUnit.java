package net.sourceforge.fenixedu.presentationTier.renderers.providers.executionDegree;

import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class DegreesByEmployeeUnit implements DataProvider {

    public Object provide(Object source, Object currentValue) {

	return AccessControl.getPerson().getEmployee().getAdministrativeOffice()
		.getAdministratedDegreesForStudentCreationWithoutCandidacy();
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
