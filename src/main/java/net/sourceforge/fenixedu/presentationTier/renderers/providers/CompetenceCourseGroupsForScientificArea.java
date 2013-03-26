package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.ScientificAreaUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.vigilancy.VigilancyCourseGroupBean;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class CompetenceCourseGroupsForScientificArea implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        VigilancyCourseGroupBean bean = (VigilancyCourseGroupBean) source;
        Unit unit = bean.getSelectedUnit();
        List<Unit> competenceCourseGroups;
        if (unit == null) {
            competenceCourseGroups = new ArrayList<Unit>();
        } else {
            competenceCourseGroups = new ArrayList<Unit>(((ScientificAreaUnit) unit).getCompetenceCourseGroupUnits());
        }
        Collections.sort(competenceCourseGroups, Party.COMPARATOR_BY_NAME);
        return competenceCourseGroups;
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
