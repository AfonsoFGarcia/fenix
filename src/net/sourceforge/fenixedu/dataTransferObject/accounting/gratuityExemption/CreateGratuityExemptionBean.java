package net.sourceforge.fenixedu.dataTransferObject.accounting.gratuityExemption;

import java.io.Serializable;
import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityEvent;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityExemptionJustificationType;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.YearMonthDay;

public class CreateGratuityExemptionBean implements Serializable {

	private GratuityEvent gratuityEvent;

	private GratuityExemptionJustificationType exemptionJustificationType;

	private String reason;

	private YearMonthDay dispatchDate;

	private BigDecimal otherPercentage;

	private Money amount;

	private BigDecimal percentage;

	public CreateGratuityExemptionBean(GratuityEvent gratuityEvent) {
		super();
		setGratuityEvent(gratuityEvent);
	}

	public Money getAmount() {
		return amount;
	}

	public void setAmount(Money amount) {
		this.amount = amount;
	}

	public GratuityExemptionJustificationType getExemptionJustificationType() {
		return exemptionJustificationType;
	}

	public void setExemptionJustificationType(GratuityExemptionJustificationType exemptionType) {
		this.exemptionJustificationType = exemptionType;
	}

	public GratuityEvent getGratuityEvent() {
		return (gratuityEvent != null) ? this.gratuityEvent : null;
	}

	public void setGratuityEvent(GratuityEvent gratuityEvent) {
		this.gratuityEvent = gratuityEvent;
	}

	public BigDecimal getPercentage() {
		return percentage;
	}

	public void setPercentage(BigDecimal percentage) {
		this.percentage = percentage;
	}

	public BigDecimal getOtherPercentage() {
		return otherPercentage;
	}

	public void setOtherPercentage(BigDecimal selectedPercentage) {
		this.otherPercentage = selectedPercentage;
	}

	public boolean isPercentageExemption() {
		return getOtherPercentage() != null || getPercentage() != null;
	}

	public BigDecimal getSelectedPercentage() {
		return getOtherPercentage() != null ? getOtherPercentage() : getPercentage();
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public YearMonthDay getDispatchDate() {
		return dispatchDate;
	}

	public void setDispatchDate(YearMonthDay dispatchDate) {
		this.dispatchDate = dispatchDate;
	}

}
