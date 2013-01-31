package net.sourceforge.fenixedu.domain.accounting.events.gratuity;

import java.math.BigDecimal;
import java.math.RoundingMode;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.EventState;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

public class ValueGratuityExemption extends ValueGratuityExemption_Base {

	public ValueGratuityExemption(final Person responsible, final GratuityEvent gratuityEvent,
			final GratuityExemptionJustificationType gratuityExemptionType, final String reason, final YearMonthDay dispatchDate,
			final Money value) {
		super();
		init(responsible, gratuityEvent, gratuityExemptionType, reason, dispatchDate, value);
	}

	public ValueGratuityExemption(final GratuityEvent gratuityEvent,
			final GratuityExemptionJustificationType gratuityExemptionType, final String reason, final YearMonthDay dispatchDate,
			final Money value) {
		this(null, gratuityEvent, gratuityExemptionType, reason, dispatchDate, value);
	}

	protected void init(Person responsible, GratuityEvent gratuityEvent, GratuityExemptionJustificationType exemptionType,
			String reason, YearMonthDay dispatchDate, Money value) {

		checkParameters(value);
		super.setValue(value);

		super.init(responsible, gratuityEvent, exemptionType, reason, dispatchDate);
	}

	private void checkParameters(Money value) {
		if (value == null) {
			throw new DomainException("error.accounting.events.gratuity.ValueGratuityExemption.value.cannot.be.null");
		}
	}

	@Checked("RolePredicates.MANAGER_PREDICATE")
	@Override
	public void setValue(Money value) {
		super.setValue(value);
		final DateTime now = new DateTime();
		getGratuityEvent().forceChangeState(EventState.OPEN, now);
		getGratuityEvent().recalculateState(now);
	}

	@Override
	public BigDecimal calculateDiscountPercentage(Money amount) {
		final BigDecimal amountToDiscount = new BigDecimal(getValue().toString());
		return amountToDiscount.divide(amount.getAmount(), 10, RoundingMode.HALF_EVEN);
	}

	@Override
	public boolean isValueExemption() {
		return true;
	}

}
