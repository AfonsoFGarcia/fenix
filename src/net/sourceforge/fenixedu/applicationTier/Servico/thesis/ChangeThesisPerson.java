package net.sourceforge.fenixedu.applicationTier.Servico.thesis;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.externalPerson.InsertExternalPerson;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.domain.thesis.ThesisEvaluationParticipant;
import pt.ist.fenixWebFramework.services.Service;

public class ChangeThesisPerson extends FenixService {

    public static enum PersonTarget {
	orientator, coorientator, president, vowel
    };

    public static class PersonChange {
	PersonTarget type;
	Person person;
	String personName;
	Unit unit;
	String unitName;
	ThesisEvaluationParticipant target;

	public PersonChange(PersonTarget type, Person person, ThesisEvaluationParticipant target) {
	    super();

	    this.type = type;
	    this.person = person;
	    this.target = target;
	}

	public PersonChange(PersonTarget type, String personName, Unit unit, ThesisEvaluationParticipant target) {
	    super();

	    this.type = type;
	    this.personName = personName;
	    this.unit = unit;
	    this.target = target;
	}

	public PersonChange(PersonTarget type, String personName, String unitName, ThesisEvaluationParticipant target) {
	    super();

	    this.type = type;
	    this.personName = personName;
	    this.unitName = unitName;
	    this.target = target;
	}
    }

    @Service
    public static void run(DegreeCurricularPlan degreeCurricularPlan, Thesis thesis, PersonChange change)
	    throws FenixServiceException {
	Person person = getPerson(change);

	thesis.checkIsScientificCommission();

	switch (change.type) {
	case orientator:
	    thesis.setOrientator(person);
	    break;
	case coorientator:
	    thesis.setCoorientator(person);
	    break;
	case president:
	    thesis.setPresident(person);
	    break;
	case vowel:
	    if (change.target != null) {
		change.target.delete();
		if (person != null) {
		    thesis.addVowel(person);
		}
	    } else {
		if (person != null) {
		    thesis.addVowel(person);
		}
	    }

	    break;
	}
    }

    private static Person getPerson(PersonChange change) throws FenixServiceException {
	if (change.person != null) {
	    return change.person;
	} else {
	    if (change.personName == null) {
		return null;
	    } else {
		if (change.unit != null) {
		    return new InsertExternalPerson().run(
			    new InsertExternalPerson.ServiceArguments(change.personName, change.unit)).getPerson();
		} else {
		    return new InsertExternalPerson().run(change.personName, change.unitName).getPerson();
		}
	    }
	}
    }
}