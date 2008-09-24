package net.sourceforge.fenixedu.presentationTier.renderers.providers.executionDegree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class PreBolonhaMasterDegrees implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	final List<Degree> result = new ArrayList<Degree>();
	for (Degree degree : Degree.readNotEmptyDegrees()) {
	    if (degree.getTipoCurso().equals(DegreeType.MASTER_DEGREE)) {
		result.add(degree);
	    }
	}
	Collections.sort(result, Degree.COMPARATOR_BY_NAME);
	return result;
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
