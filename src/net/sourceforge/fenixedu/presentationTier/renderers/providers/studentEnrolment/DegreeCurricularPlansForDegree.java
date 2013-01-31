package net.sourceforge.fenixedu.presentationTier.renderers.providers.studentEnrolment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.studentEnrolment.StudentOptionalEnrolmentBean;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class DegreeCurricularPlansForDegree implements DataProvider {

	@Override
	public Object provide(Object source, Object currentValue) {

		final StudentOptionalEnrolmentBean optionalEnrolmentBean = (StudentOptionalEnrolmentBean) source;
		final List<DegreeCurricularPlan> result = new ArrayList<DegreeCurricularPlan>();
		if (optionalEnrolmentBean.getDegree() != null && optionalEnrolmentBean.getDegreeType() != null) {
			if (optionalEnrolmentBean.getDegree().getDegreeType().equals(optionalEnrolmentBean.getDegreeType())) {
				result.addAll(optionalEnrolmentBean.getDegree().getDegreeCurricularPlansSet());
				if (optionalEnrolmentBean.getDegreeCurricularPlan() != null
						&& !optionalEnrolmentBean.getDegree().getDegreeCurricularPlansSet()
								.contains(optionalEnrolmentBean.getDegreeCurricularPlan())) {
					optionalEnrolmentBean.setDegreeCurricularPlan(null);
				}
			} else {
				optionalEnrolmentBean.setDegree(null);
				optionalEnrolmentBean.setDegreeCurricularPlan(null);
			}
		}

		Collections.sort(result, DegreeCurricularPlan.COMPARATOR_BY_NAME);
		return result;
	}

	@Override
	public Converter getConverter() {
		return new DomainObjectKeyConverter();
	}

}
