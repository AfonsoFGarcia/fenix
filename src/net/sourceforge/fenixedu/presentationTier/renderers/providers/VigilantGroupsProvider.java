package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import net.sourceforge.fenixedu.presentationTier.Action.vigilancy.VigilantBean;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;

import org.apache.commons.beanutils.BeanComparator;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class VigilantGroupsProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {

	VigilantBean bean = (VigilantBean) source;
	List<VigilantGroup> groups = bean.getVigilantGroups();

	Collections.sort(groups, new BeanComparator("name"));
	return groups;
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}