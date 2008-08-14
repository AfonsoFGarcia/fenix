package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import net.sourceforge.fenixedu.domain.tests.NewTestModel;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.tests.TestModelBean;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ExecutionCoursesForTestModel implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	TestModelBean testModelBean = (TestModelBean) source;
	NewTestModel testModel = testModelBean.getTestModel();
	return testModel.getCreator().getCurrentExecutionCourses();
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
