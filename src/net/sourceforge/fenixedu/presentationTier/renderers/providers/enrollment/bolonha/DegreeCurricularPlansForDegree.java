package net.sourceforge.fenixedu.presentationTier.renderers.providers.enrollment.bolonha;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.student.enrollment.bolonha.BolonhaStudentOptionalEnrollmentBean;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class DegreeCurricularPlansForDegree implements DataProvider {

	@Override
	public Object provide(Object source, Object currentValue) {
		final BolonhaStudentOptionalEnrollmentBean optionalEnrollmentBean = (BolonhaStudentOptionalEnrollmentBean) source;

		final List<DegreeCurricularPlan> result = new ArrayList<DegreeCurricularPlan>();
		if (optionalEnrollmentBean.hasDegree() && optionalEnrollmentBean.hasDegreeType()) {
			if (optionalEnrollmentBean.getDegree().getDegreeType() == optionalEnrollmentBean.getDegreeType()) {
				result.addAll(optionalEnrollmentBean.getDegree().getDegreeCurricularPlansForYear(
						optionalEnrollmentBean.getExecutionYear()));
			} else {
				optionalEnrollmentBean.setDegree(null);
				optionalEnrollmentBean.setDegreeType(null);
			}
		}

		Collections.sort(result, DegreeCurricularPlan.COMPARATOR_BY_NAME);

		final DegreeCurricularPlan currentSelectedDegreeCurricularPlan = (DegreeCurricularPlan) currentValue;
		if (!result.contains(currentSelectedDegreeCurricularPlan)) {
			optionalEnrollmentBean.setDegreeCurricularPlan(null);
		}

		return result;
	}

	@Override
	public Converter getConverter() {
		return new DomainObjectKeyConverter();
	}

}
