package net.sourceforge.fenixedu.presentationTier.renderers.providers.candidacy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.candidacy.DFACandidacyBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.degree.DegreeType;

import org.apache.commons.beanutils.BeanComparator;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class DFATypeDegrees implements DataProvider {

	@Override
	public Object provide(Object source, Object currentValue) {

		DFACandidacyBean bean = (DFACandidacyBean) source;

		final List<Degree> result = new ArrayList<Degree>();
		DegreeType degreeType = bean.getDegreeType();
		if (degreeType != null) {
			for (Degree degree : Degree.readNotEmptyDegrees()) {
				if (degree.getDegreeType() == degreeType) {
					result.add(degree);
				}
			}
		}
		Collections.sort(result, new BeanComparator("name"));
		return result;
	}

	@Override
	public Converter getConverter() {
		return new DomainObjectKeyConverter();
	}

}
