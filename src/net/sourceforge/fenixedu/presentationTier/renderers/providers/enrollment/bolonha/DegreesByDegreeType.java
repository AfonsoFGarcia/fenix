package net.sourceforge.fenixedu.presentationTier.renderers.providers.enrollment.bolonha;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.student.enrollment.bolonha.BolonhaStudentOptionalEnrollmentBean;
import net.sourceforge.fenixedu.domain.Degree;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class DegreesByDegreeType implements DataProvider {

	@Override
	public Object provide(Object source, Object currentValue) {

		final BolonhaStudentOptionalEnrollmentBean optionalEnrollmentBean = (BolonhaStudentOptionalEnrollmentBean) source;
		List<Degree> result = null;
		if (optionalEnrollmentBean.hasDegreeType()) {
			result = Degree.readAllByDegreeType(optionalEnrollmentBean.getDegreeType());
			Collections.sort(result, Degree.COMPARATOR_BY_NAME);
		} else {
			result = Collections.EMPTY_LIST;
		}

		final Degree currentSelectedDegree = (Degree) currentValue;
		if (!result.contains(currentSelectedDegree)) {
			optionalEnrollmentBean.setDegree(null);
		}

		return result;
	}

	@Override
	public Converter getConverter() {
		return new DomainObjectKeyConverter();
	}

}
