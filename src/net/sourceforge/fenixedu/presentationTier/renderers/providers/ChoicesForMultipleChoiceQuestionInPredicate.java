package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import net.sourceforge.fenixedu.domain.tests.NewMultipleChoiceQuestion;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.tests.PredicateBean;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ChoicesForMultipleChoiceQuestionInPredicate implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        PredicateBean predicateBean = (PredicateBean) source;
        NewMultipleChoiceQuestion multipleChoiceQuestion = (NewMultipleChoiceQuestion) predicateBean.getQuestion();
        return multipleChoiceQuestion.getOrderedChoices();
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
