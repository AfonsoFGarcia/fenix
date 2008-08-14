package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.organizationalStructure.ScientificAreaUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.vigilancy.VigilancyCourseGroupBean;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;

import org.apache.commons.beanutils.BeanComparator;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class CompetenceCourseGroupsForScientificArea implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	VigilancyCourseGroupBean bean = (VigilancyCourseGroupBean) source;
	Unit unit = bean.getSelectedUnit();
	List<Unit> competenceCourseGroups;
	if (unit == null) {
	    competenceCourseGroups = new ArrayList<Unit>();
	} else {
	    competenceCourseGroups = new ArrayList<Unit>(((ScientificAreaUnit) unit).getCompetenceCourseGroupUnits());
	}
	Collections.sort(competenceCourseGroups, new BeanComparator("name"));
	return competenceCourseGroups;
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
