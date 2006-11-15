package net.sourceforge.fenixedu.domain.accounting.events.gratuity;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.AccountType;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.PostingRule;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.util.resources.LabelFormatter;

import org.joda.time.DateTime;

import dml.runtime.RelationAdapter;

public class GratuityEvent extends GratuityEvent_Base {

    static {

	PersonAccountingEvent.addListener(new RelationAdapter<Event, Person>() {
	    @Override
	    public void beforeAdd(Event event, Person person) {
		if (event instanceof GratuityEvent) {
		    final GratuityEvent gratuityEvent = (GratuityEvent) event;
		    if (person.hasGratuityEventFor(gratuityEvent.getStudentCurricularPlan(),
			    gratuityEvent.getExecutionYear())) {
			throw new DomainException(
				"error.accounting.events.gratuity..GratuityEvent.person.already.has.gratuity.event.for.student.curricular.plan.and.year");
		    }
		}
	    }
	});

    }

    protected GratuityEvent() {
	super();
    }

    public GratuityEvent(AdministrativeOffice administrativeOffice, Person person,
	    StudentCurricularPlan studentCurricularPlan, ExecutionYear executionYear) {
	this();
	init(administrativeOffice, person, studentCurricularPlan, executionYear);
    }

    protected void init(AdministrativeOffice administrativeOffice, Person person,
	    StudentCurricularPlan studentCurricularPlan, ExecutionYear executionYear) {
	super.init(administrativeOffice, EventType.GRATUITY, person);
	checkParameters(studentCurricularPlan, executionYear);
	super.setStudentCurricularPlan(studentCurricularPlan);
	super.setExecutionYear(executionYear);
    }

    private void checkParameters(StudentCurricularPlan studentCurricularPlan, ExecutionYear executionYear) {
	if (studentCurricularPlan == null) {
	    throw new DomainException(
		    "error.accounting.events.gratuity.GratuityEvent.studentCurricularPlan.cannot.be.null");
	}

	if (executionYear == null) {
	    throw new DomainException(
		    "error.accounting.events.gratuity.GratuityEvent.executionYear.cannot.be.null");
	}

    }

    @Override
    public Account getToAccount() {
	return getUnit().getAccountBy(AccountType.INTERNAL);
    }

    @Override
    protected Account getFromAccount() {
	return getPerson().getAccountBy(AccountType.EXTERNAL);
    }

    private Unit getUnit() {
	return getExecutionDegree().getDegreeCurricularPlan().getDegree().getUnit();
    }

    private ExecutionDegree getExecutionDegree() {
	return getStudentCurricularPlan().getDegreeCurricularPlan().getExecutionDegreeByYear(
		getExecutionYear());
    }

    private Degree getDegree() {
	return getExecutionDegree().getDegree();
    }

    @Override
    public LabelFormatter getDescriptionForEntryType(EntryType entryType) {
	final LabelFormatter labelFormatter = new LabelFormatter();
	labelFormatter.appendLabel(entryType.name(), "enum").appendLabel(" (").appendLabel(
		getDegree().getDegreeType().name(), "enum").appendLabel(" - ").appendLabel(
		getDegree().getName()).appendLabel(" - ").appendLabel(getExecutionYear().getYear())
		.appendLabel(")");

	return labelFormatter;
    }

    @Override
    protected PostingRule getPostingRule(DateTime whenRegistered) {
	return getExecutionDegree().getDegreeCurricularPlan().getServiceAgreementTemplate()
		.findPostingRuleByEventTypeAndDate(getEventType(), whenRegistered);
    }

    public Registration getRegistration() {
	return getStudentCurricularPlan().getRegistration();
    }

    @Override
    public void setExecutionYear(ExecutionYear executionYear) {
	throw new DomainException(
		"error.accounting.events.gratuity.GratuityEvent.cannot.modify.executionYear");
    }

    @Override
    public void setStudentCurricularPlan(StudentCurricularPlan studentCurricularPlan) {
	throw new DomainException(
		"error.accounting.events.gratuity.GratuityEvent.cannot.modify.studentCurricularPlan");
    }

    public boolean isCompleteEnrolmentModel() {
	return getRegistration().isCompleteEnrolmentModel(getExecutionYear());
    }

    public boolean isCustomEnrolmentModel() {
	return getRegistration().isCustomEnrolmentModel(getExecutionYear());
    }

    public BigDecimal getTotalEctsCreditsForRegistration() {
	return getRegistration().getTotalEctsCredits(getExecutionYear());
    }

}
