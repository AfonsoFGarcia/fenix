package net.sourceforge.fenixedu.presentationTier.Action.phd;

import net.sourceforge.fenixedu.domain.accessControl.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyArrayConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class PhdProgramsMultipleProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object current) {
        return AcademicAuthorizationGroup.getPhdProgramsForOperation(AccessControl.getPerson(), AcademicOperationType.MANAGE_PHD_PROCESSES);
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyArrayConverter();
    }
}
