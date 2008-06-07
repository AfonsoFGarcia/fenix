package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;

import org.apache.commons.beanutils.BeanComparator;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class CountryProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	final Set<Country> countrySet = new TreeSet<Country>(new BeanComparator("name"));
	countrySet.addAll(RootDomainObject.readAllDomainObjects(Country.class));
	return countrySet;
    }

    public Converter getConverter() {

	return new DomainObjectKeyConverter();
    }

}
