package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import net.sourceforge.fenixedu.domain.tests.NewAllGroup;
import net.sourceforge.fenixedu.domain.tests.NewAtomicQuestion;
import net.sourceforge.fenixedu.domain.tests.NewQuestion;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.tests.PredicateBean;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class AtomicQuestionsForQuestion implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        List<NewAtomicQuestion> atomicQuestions = new ArrayList<NewAtomicQuestion>();
        Stack<NewAllGroup> allGroups = new Stack<NewAllGroup>();

        PredicateBean predicateBean = (PredicateBean) source;

        allGroups.add(predicateBean.getQuestion().getTopAllGroup());

        while (allGroups.size() > 0) {
            NewAllGroup allGroup = allGroups.pop();

            for (NewQuestion question : allGroup.getChildAtomicQuestions()) {
                if (NewAllGroup.ALL_GROUP_PREDICATE.evaluate(question)) {
                    allGroups.add((NewAllGroup) question);
                } else {
                    atomicQuestions.add((NewAtomicQuestion) question);
                }
            }
        }

        return atomicQuestions;
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
