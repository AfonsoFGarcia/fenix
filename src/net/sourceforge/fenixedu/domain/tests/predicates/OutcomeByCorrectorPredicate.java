package net.sourceforge.fenixedu.domain.tests.predicates;

import java.util.HashMap;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.tests.NewCorrector;
import net.sourceforge.fenixedu.domain.tests.NewQuestion;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.tests.PredicateBean;

public class OutcomeByCorrectorPredicate extends AtomicPredicate implements Predicate {
    private final DomainReference<NewCorrector> corrector;

    public OutcomeByCorrectorPredicate(NewCorrector corrector) {
	super();

	this.corrector = new DomainReference<NewCorrector>(corrector);
    }

    public OutcomeByCorrectorPredicate(PredicateBean predicateBean) {
	this(predicateBean.getCorrector());
    }

    public boolean evaluate(NewQuestion question, Person person) {
	NewCorrector corrector = this.getCorrector();

	if (!corrector.getAtomicQuestion().isAnswered(person)) {
	    return false;
	}

	return corrector.getPredicate().evaluate(corrector.getAtomicQuestion(), person);
    }

    public NewCorrector getCorrector() {
	return corrector.getObject();
    }

    public boolean uses(Object object) {
	NewCorrector corrector = (NewCorrector) object;

	return corrector.equals(this.getCorrector());
    }

    public Predicate transform(HashMap<Object, Object> transformMap) {
	NewCorrector transformation = (NewCorrector) transformMap.get(getCorrector());
	return new OutcomeByCorrectorPredicate(transformation != null ? transformation : getCorrector());
    }

}
