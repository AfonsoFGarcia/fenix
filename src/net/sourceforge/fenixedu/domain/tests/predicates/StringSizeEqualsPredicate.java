package net.sourceforge.fenixedu.domain.tests.predicates;

import java.util.HashMap;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.tests.NewQuestion;
import net.sourceforge.fenixedu.domain.tests.NewStringQuestion;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.tests.PredicateBean;

public class StringSizeEqualsPredicate extends AtomicPredicate implements Predicate {
    private final int size;

    public StringSizeEqualsPredicate(int size) {
        super();

        this.size = size;
    }

    public StringSizeEqualsPredicate(PredicateBean predicateBean) {
        this(predicateBean.getSize());
    }

    @Override
    public boolean evaluate(NewQuestion question, Person person) {
        NewStringQuestion stringQuestion = (NewStringQuestion) question;

        if (!stringQuestion.isAnswered(person)) {
            return false;
        }

        return stringQuestion.getStringAnswer(person).length() == size;
    }

    public int getSize() {
        return size;
    }

    @Override
    public boolean uses(Object object) {
        return false;
    }

    @Override
    public Predicate transform(HashMap<Object, Object> transformMap) {
        return new StringSizeEqualsPredicate(getSize());
    }

}
