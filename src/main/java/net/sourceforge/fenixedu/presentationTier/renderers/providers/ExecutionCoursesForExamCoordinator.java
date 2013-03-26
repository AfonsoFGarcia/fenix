package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.vigilancy.ExamCoordinator;
import net.sourceforge.fenixedu.presentationTier.Action.vigilancy.ConvokeBean;

import org.apache.commons.beanutils.BeanComparator;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ExecutionCoursesForExamCoordinator implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        ConvokeBean bean = (ConvokeBean) source;
        ExamCoordinator coordinator = bean.getExamCoordinator();
        List<ExecutionCourse> courses = coordinator.getAssociatedExecutionCourses();

        Collections.sort(courses, new BeanComparator("nome"));
        return courses;
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}