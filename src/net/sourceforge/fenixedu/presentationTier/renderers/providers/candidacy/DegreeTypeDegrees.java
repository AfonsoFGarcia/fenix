package net.sourceforge.fenixedu.presentationTier.renderers.providers.candidacy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;

import org.apache.commons.beanutils.BeanComparator;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class DegreeTypeDegrees implements DataProvider {

    @SuppressWarnings("unchecked")
    public Object provide(Object source, Object currentValue) {

	final List<Degree> result = new ArrayList<Degree>();
	for (Degree degree : Degree.readNotEmptyDegrees()) {
	    if (degree.getDegreeType() == DegreeType.BOLONHA_DEGREE) {
		result.add(degree);
	    }
	}
	Collections.sort(result, new BeanComparator("name"));
	return result;
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
