/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.renderers.providers.student;

import java.util.Collections;

import net.sourceforge.fenixedu.dataTransferObject.student.ChooseStudentCurricularPlanBean;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class StudentCurricularPlansProvider implements DataProvider {

	@Override
	public Object provide(Object source, Object currentValue) {
		ChooseStudentCurricularPlanBean bean = (ChooseStudentCurricularPlanBean) source;
		return bean.getRegistration() != null ? bean.getRegistration().getStudentCurricularPlans() : Collections.EMPTY_LIST;
	}

	@Override
	public Converter getConverter() {
		return new DomainObjectKeyConverter();
	}

}
