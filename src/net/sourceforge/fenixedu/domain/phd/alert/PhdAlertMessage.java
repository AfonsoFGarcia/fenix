package net.sourceforge.fenixedu.domain.phd.alert;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class PhdAlertMessage extends PhdAlertMessage_Base {

    protected PhdAlertMessage() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setWhenCreated(new DateTime());
    }

    public PhdAlertMessage(PhdIndividualProgramProcess process, Person person, MultiLanguageString subject,
	    MultiLanguageString body) {
	this();
	init(process, Collections.singletonList(person), subject, body);
    }

    public PhdAlertMessage(PhdIndividualProgramProcess process, Collection<Person> persons, MultiLanguageString subject,
	    MultiLanguageString body) {
	this();
	init(process, persons, subject, body);
    }

    protected void init(PhdIndividualProgramProcess process, Collection<Person> persons, MultiLanguageString subject,
	    MultiLanguageString body) {
	checkParameters(process, persons, subject, body);
	super.setProcess(process);
	super.getPersons().addAll(persons);
	super.setSubject(subject);
	super.setBody(body);
	super.setReaded(Boolean.FALSE);
    }

    private void checkParameters(PhdIndividualProgramProcess process, Collection<Person> persons, MultiLanguageString subject,
	    MultiLanguageString body) {
	check(process, "error.net.sourceforge.fenixedu.domain.phd.alert.PhdAlertMessage.process.cannot.be.null");
	check(persons, "error.phd.alert.PhdAlertMessage.persons.cannot.be.empty");
	check(subject, "error.phd.alert.PhdAlertMessage.subject.cannot.be.null");
	check(body, "error.phd.alert.PhdAlertMessage.body.cannot.be.null");
    }

    @Override
    public void setProcess(PhdIndividualProgramProcess process) {
	throw new DomainException("error.net.sourceforge.fenixedu.domain.phd.alert.PhdAlertMessage.cannot.modify.process");
    }

    @Override
    public void addPersons(Person person) {
	throw new DomainException("error.net.sourceforge.fenixedu.domain.phd.alert.PhdAlertMessage.cannot.add.person");
    }

    @Override
    public List<Person> getPersons() {
	return Collections.unmodifiableList(super.getPersons());
    }

    @Override
    public Set<Person> getPersonsSet() {
	return Collections.unmodifiableSet(super.getPersonsSet());
    }

    @Override
    public Iterator<Person> getPersonsIterator() {
	return getPersonsSet().iterator();
    }

    @Override
    public void removePersons(Person person) {
	throw new DomainException("error.net.sourceforge.fenixedu.domain.phd.alert.PhdAlertMessage.cannot.remove.person");
    }

    @Override
    public void setSubject(MultiLanguageString subject) {
	throw new DomainException("error.net.sourceforge.fenixedu.domain.phd.alert.PhdAlertMessage.cannot.modify.subject");
    }

    @Override
    public void setBody(MultiLanguageString body) {
	throw new DomainException("error.net.sourceforge.fenixedu.domain.phd.alert.PhdAlertMessage.cannot.modify.body");
    }

    @Override
    public void setReaded(Boolean readed) {
	throw new DomainException("error.net.sourceforge.fenixedu.domain.phd.alert.PhdAlertMessage.cannot.modify.readed");
    }

    @Service
    public void markAsReaded(Person person) {
	check(person, "error.net.sourceforge.fenixedu.domain.phd.alert.PhdAlertMessage.personWhoMarkAsReaded.cannot.be.null");

	super.setReaded(true);
	super.setPersonWhoMarkedAsReaded(person);
    }

    public boolean isReaded() {
	return getReaded().booleanValue();
    }

    public boolean isFor(Person person) {
	return getPersons().contains(person);
    }

}
