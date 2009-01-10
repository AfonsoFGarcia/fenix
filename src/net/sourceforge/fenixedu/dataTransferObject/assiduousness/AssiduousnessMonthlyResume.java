package net.sourceforge.fenixedu.dataTransferObject.assiduousness;

import java.io.Serializable;
import java.util.Comparator;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessClosedMonth;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessExtraWork;
import net.sourceforge.fenixedu.domain.assiduousness.ClosedMonth;
import net.sourceforge.fenixedu.domain.assiduousness.ExtraWorkRequest;
import net.sourceforge.fenixedu.util.report.StyledExcelSpreadsheet;

import org.joda.time.Duration;
import org.joda.time.Hours;
import org.joda.time.MutablePeriod;
import org.joda.time.PeriodType;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

public class AssiduousnessMonthlyResume implements Serializable {

    // Collections.sort(assiduousnessMonthlyResumeList, new
    // BeanComparator("employee.employeeNumber"));
    public static final Comparator<AssiduousnessMonthlyResume> COMPARATOR_BY_EMPLOYEE_NUMBER = new Comparator<AssiduousnessMonthlyResume>() {

	@Override
	public int compare(AssiduousnessMonthlyResume o1, AssiduousnessMonthlyResume o2) {
	    return o1.getEmployee().getEmployeeNumber().compareTo(o2.getEmployee().getEmployeeNumber());
	}

    };

    Employee employee;

    Duration totalBalance;

    Duration unjustifiedBalance;

    Duration saturdaysBalance;

    Duration sundaysBalance;

    Duration holidayBalance;

    Duration nightlyBalance;

    Duration firstLevelBalance;

    Duration secondLevelBalance;

    Duration secondLevelBalanceWithoutLimits;

    Integer payedSaturdaysBalance;

    Integer payedSundaysBalance;

    Integer payedHolidayBalance;

    Integer payedNightlyBalance;

    Integer payedWorkWeekBalance;

    Integer payedNightWorkWeekBalance;

    Duration firstLevelNightBalance;

    Duration secondLevelNightBalance;

    Duration secondLevelNightBalanceWithoutLimits;

    Integer extraNightDays;

    protected final Duration midHourDuration = new Duration(1800000);

    public AssiduousnessMonthlyResume(Employee employee, Duration totalBalance, Duration totalComplementaryWeeklyRestBalance,
	    Duration totalWeeklyRestBalance, Duration holidayRest, Duration nightWork, Duration unjustified) {
	setEmployee(employee);
	setTotalBalance(totalBalance);
	setUnjustifiedBalance(unjustified);
	setSaturdaysBalance(totalWeeklyRestBalance);
	setSundaysBalance(totalComplementaryWeeklyRestBalance);
	setHolidayBalance(holidayRest);
	setNightlyBalance(nightWork);
    }

    public AssiduousnessMonthlyResume(AssiduousnessClosedMonth assiduousnessClosedMonth) {
	setEmployee(assiduousnessClosedMonth.getAssiduousnessStatusHistory().getAssiduousness().getEmployee());
	setTotalBalance(assiduousnessClosedMonth.getBalance());
	setUnjustifiedBalance(assiduousnessClosedMonth.getTotalUnjustifiedBalance());
	setSaturdaysBalance(assiduousnessClosedMonth.getSaturdayBalance());
	setSundaysBalance(assiduousnessClosedMonth.getSundayBalance());
	setHolidayBalance(assiduousnessClosedMonth.getHolidayBalance());
	setNightlyBalance(assiduousnessClosedMonth.getTotalNightBalance());
    }

    public AssiduousnessMonthlyResume(Employee employee, ClosedMonth closedMonth, ExtraWorkRequest thisExtraWorkRequest) {
	setEmployee(employee);
	for (AssiduousnessClosedMonth assiduousnessClosedMonth : closedMonth.getAssiduousnessClosedMonths(employee
		.getAssiduousness())) {
	    setTotalBalance(getTotalBalance().plus(assiduousnessClosedMonth.getBalance()));
	    setUnjustifiedBalance(getUnjustifiedBalance().plus(assiduousnessClosedMonth.getTotalUnjustifiedBalance()));
	    setSaturdaysBalance(getSaturdaysBalance().plus(assiduousnessClosedMonth.getSaturdayBalance()));
	    setSundaysBalance(getSundaysBalance().plus(assiduousnessClosedMonth.getSundayBalance()));
	    setHolidayBalance(getHolidayBalance().plus(assiduousnessClosedMonth.getHolidayBalance()));
	    setNightlyBalance(getNightlyBalance().plus(assiduousnessClosedMonth.getTotalNightBalance()));

	    for (AssiduousnessExtraWork assiduousnessExtraWork : assiduousnessClosedMonth.getAssiduousnessExtraWorks()) {
		Duration firstLevelBalance = assiduousnessExtraWork.getFirstLevelBalance() == null ? Duration.ZERO
			: assiduousnessExtraWork.getFirstLevelBalance();
		Duration secondLevelBalance = assiduousnessExtraWork.getSecondLevelBalanceWithLimit() == null ? Duration.ZERO
			: assiduousnessExtraWork.getSecondLevelBalanceWithLimit();
		Duration secondLevelBalanceWithoutLimits = assiduousnessExtraWork.getSecondLevelBalance() == null ? Duration.ZERO
			: assiduousnessExtraWork.getSecondLevelBalance();

		Duration firstLevelNightBalance = assiduousnessExtraWork.getFirstLevelNightBalance() == null ? Duration.ZERO
			: assiduousnessExtraWork.getFirstLevelNightBalance();
		Duration secondLevelNightBalance = assiduousnessExtraWork.getSecondLevelNightBalanceWithLimit() == null ? Duration.ZERO
			: assiduousnessExtraWork.getSecondLevelNightBalanceWithLimit();
		Duration secondLevelNightBalanceWithoutLimits = assiduousnessExtraWork.getSecondLevelNightBalance() == null ? Duration.ZERO
			: assiduousnessExtraWork.getSecondLevelNightBalance();

		if (assiduousnessClosedMonth.getBalance().isLongerThan(Duration.ZERO)) {
		    Duration total = firstLevelBalance.plus(secondLevelBalance);
		    Duration totalWithoutLimits = firstLevelBalance.plus(secondLevelBalanceWithoutLimits);

		    Duration totalNight = firstLevelNightBalance.plus(secondLevelNightBalance);
		    Duration totalNightWithoutLimits = firstLevelNightBalance.plus(secondLevelNightBalanceWithoutLimits);

		    if (assiduousnessClosedMonth.getBalance().isShorterThan(total)) {
			Duration diference = total.minus(assiduousnessClosedMonth.getBalance().getMillis());
			if (diference.isLongerThan(Duration.ZERO)) {
			    if (diference.isShorterThan(secondLevelBalance)) {
				secondLevelBalance = secondLevelBalance.minus(diference);
				secondLevelBalanceWithoutLimits = secondLevelBalance;
			    } else {
				firstLevelBalance = firstLevelBalance.minus(diference.minus(secondLevelBalance));
			    }
			}
		    } else if (assiduousnessClosedMonth.getBalance().isShorterThan(totalWithoutLimits)) {
			Duration diference = totalWithoutLimits.minus(assiduousnessClosedMonth.getBalance().getMillis());
			secondLevelBalanceWithoutLimits = secondLevelBalanceWithoutLimits.minus(diference);
		    }

		    if (assiduousnessClosedMonth.getBalance().isShorterThan(totalNight)) {
			Duration diferenceNight = totalNight.minus(assiduousnessClosedMonth.getBalance().getMillis());
			if (diferenceNight.isLongerThan(Duration.ZERO)) {
			    if (diferenceNight.isShorterThan(secondLevelNightBalance)) {
				secondLevelNightBalance = secondLevelNightBalance.minus(diferenceNight);
				secondLevelNightBalanceWithoutLimits = secondLevelNightBalance;
			    } else {
				firstLevelNightBalance = firstLevelNightBalance.minus(diferenceNight
					.minus(secondLevelNightBalance));
			    }
			}
		    } else if (assiduousnessClosedMonth.getBalance().isShorterThan(totalNightWithoutLimits)) {
			Duration diferenceNight = totalNightWithoutLimits
				.minus(assiduousnessClosedMonth.getBalance().getMillis());
			secondLevelNightBalanceWithoutLimits = secondLevelNightBalanceWithoutLimits.minus(diferenceNight);
		    }

		    addFirstLevelBalance(roundToHalfHour(firstLevelBalance));
		    addSecondLevelBalance(roundToHalfHour(secondLevelBalance));
		    addSecondLevelBalanceWithoutLimits(roundToHalfHour(secondLevelBalanceWithoutLimits));

		    addFirstLevelNightBalance(roundToHalfHour(firstLevelNightBalance));
		    addSecondLevelNightBalance(roundToHalfHour(secondLevelNightBalance));
		    addSecondLevelNightBalanceWithoutLimits(roundToHalfHour(secondLevelNightBalanceWithoutLimits));
		    addExtraNightDays(assiduousnessExtraWork.getNightExtraWorkDays() == null ? 0 : assiduousnessExtraWork
			    .getNightExtraWorkDays());
		}
	    }
	}
	for (ExtraWorkRequest extraWorkRequest : employee.getAssiduousness().getExtraWorkRequests()) {
	    if ((thisExtraWorkRequest == null || (!thisExtraWorkRequest.equals(extraWorkRequest)))
		    && extraWorkRequest.getHoursDoneInPartialDate().equals(closedMonth.getClosedYearMonth())) {
		addPayedSaturdaysBalance(extraWorkRequest.getSaturdayHours() == null ? 0 : extraWorkRequest.getSaturdayHours());
		addPayedSundaysBalance(extraWorkRequest.getSundayHours() == null ? 0 : extraWorkRequest.getSundayHours());
		addPayedHolidayBalance(extraWorkRequest.getHolidayHours() == null ? 0 : extraWorkRequest.getHolidayHours());
		addPayedNightlyBalance(extraWorkRequest.getNightHours() == null ? 0 : extraWorkRequest.getNightHours());
		addPayedWorkWeekBalance(extraWorkRequest.getWorkdayHours() == null ? 0 : extraWorkRequest.getWorkdayHours());
		addPayedNightWorkWeekBalance(extraWorkRequest.getExtraNightHours() == null ? 0 : extraWorkRequest
			.getExtraNightHours());
	    }
	}
    }

    private Duration roundToHalfHour(Duration duration) {
	if (duration.toPeriod(PeriodType.dayTime()).getMinutes() >= 30) {
	    return new Duration(Hours.hours(duration.toPeriod(PeriodType.dayTime()).getHours() + 1).toStandardDuration());
	}
	Duration result = Hours.hours(duration.toPeriod(PeriodType.dayTime()).getHours()).toStandardDuration();
	if (duration.toPeriod(PeriodType.dayTime()).getMinutes() != 0) {
	    result = result.plus(midHourDuration);
	}
	return result;
    }

    public Employee getEmployee() {
	return employee;
    }

    public void setEmployee(Employee employee) {
	this.employee = employee;
    }

    public Duration getTotalBalance() {
	return totalBalance == null ? Duration.ZERO : totalBalance;
    }

    public void setTotalBalance(Duration totalBalance) {
	this.totalBalance = totalBalance;
    }

    public Duration getUnjustifiedBalance() {
	return unjustifiedBalance == null ? Duration.ZERO : unjustifiedBalance;
    }

    public void setUnjustifiedBalance(Duration unjustifiedBalance) {
	this.unjustifiedBalance = unjustifiedBalance;
    }

    public Duration getSaturdaysBalance() {
	return saturdaysBalance == null ? Duration.ZERO : saturdaysBalance;
    }

    public void setSaturdaysBalance(Duration saturdaysBalance) {
	this.saturdaysBalance = saturdaysBalance;
    }

    public Duration getSundaysBalance() {
	return sundaysBalance == null ? Duration.ZERO : sundaysBalance;
    }

    public void setSundaysBalance(Duration sundaysRest) {
	this.sundaysBalance = sundaysRest;
    }

    public Duration getNightlyBalance() {
	return nightlyBalance == null ? Duration.ZERO : nightlyBalance;
    }

    public void setNightlyBalance(Duration nightlyBalance) {
	this.nightlyBalance = nightlyBalance;
    }

    public Duration getHolidayBalance() {
	return holidayBalance == null ? Duration.ZERO : holidayBalance;
    }

    public void setHolidayBalance(Duration holidayWork) {
	this.holidayBalance = holidayWork;
    }

    public static void getExcelHeader(StyledExcelSpreadsheet spreadsheet, ResourceBundle bundle) {
	spreadsheet.newHeaderRow();
	spreadsheet.addHeader(bundle.getString("label.employeeNumber"));
	spreadsheet.addHeader(bundle.getString("label.totalBalance"));
	spreadsheet.addHeader(bundle.getString("label.totalUnjustified"));
	spreadsheet.addHeader(bundle.getString("label.totalSaturday"));
	spreadsheet.addHeader(bundle.getString("label.totalSunday"));
	spreadsheet.addHeader(bundle.getString("label.totalHoliday"));
	spreadsheet.addHeader(bundle.getString("label.totalNight"));
    }

    public void getExcelRow(StyledExcelSpreadsheet spreadsheet) {
	spreadsheet.newRow();
	spreadsheet.addCell(getEmployee().getEmployeeNumber().toString());
	spreadsheet.addCell(getTotalBalance().equals(Duration.ZERO) ? "" : getDurationString(getTotalBalance()));
	spreadsheet.addCell(getUnjustifiedBalance().equals(Duration.ZERO) ? "" : getDurationString(getUnjustifiedBalance()));
	spreadsheet.addCell(getSaturdaysBalance().equals(Duration.ZERO) ? "" : getDurationString(getSaturdaysBalance()));
	spreadsheet.addCell(getSundaysBalance().equals(Duration.ZERO) ? "" : getDurationString(getSundaysBalance()));
	spreadsheet.addCell(getHolidayBalance().equals(Duration.ZERO) ? "" : getDurationString(getHolidayBalance()));
	spreadsheet.addCell(getNightlyBalance().equals(Duration.ZERO) ? "" : getDurationString(getNightlyBalance()));
    }

    public String getDurationString(Duration duration) {
	PeriodFormatter fmt = new PeriodFormatterBuilder().printZeroAlways().appendHours().appendSeparator(":")
		.minimumPrintedDigits(2).appendMinutes().toFormatter();
	MutablePeriod finalDuration = new MutablePeriod(duration.getMillis(), PeriodType.time());
	if (duration.toPeriod().getMinutes() < 0) {
	    finalDuration.setMinutes(-duration.toPeriod().getMinutes());
	    if (duration.toPeriod().getHours() == 0) {
		fmt = new PeriodFormatterBuilder().printZeroAlways().appendLiteral("-").appendHours().appendSeparator(":")
			.minimumPrintedDigits(2).appendMinutes().toFormatter();
	    }
	}
	return fmt.print(finalDuration);
    }

    public Duration getFirstLevelBalance() {
	return firstLevelBalance == null ? Duration.ZERO : firstLevelBalance;
    }

    public void setFirstLevelBalance(Duration firstLevelBalance) {
	this.firstLevelBalance = firstLevelBalance;
    }

    public void addFirstLevelBalance(Duration firstLevelBalance) {
	setFirstLevelBalance(getFirstLevelBalance().plus(firstLevelBalance));
    }

    public Duration getSecondLevelBalance() {
	return secondLevelBalance == null ? Duration.ZERO : secondLevelBalance;
    }

    public void setSecondLevelBalance(Duration secondLevelBalance) {
	this.secondLevelBalance = secondLevelBalance;
    }

    public void addSecondLevelBalance(Duration secondLevelBalance) {
	setSecondLevelBalance(getSecondLevelBalance().plus(secondLevelBalance));
    }

    public Duration getSecondLevelBalanceWithoutLimits() {
	return secondLevelBalanceWithoutLimits == null ? Duration.ZERO : secondLevelBalanceWithoutLimits;
    }

    public void setSecondLevelBalanceWithoutLimits(Duration secondLevelBalanceWithoutLimits) {
	this.secondLevelBalanceWithoutLimits = secondLevelBalanceWithoutLimits;
    }

    public void addSecondLevelBalanceWithoutLimits(Duration secondLevelBalanceWithoutLimits) {
	setSecondLevelBalanceWithoutLimits(getSecondLevelBalanceWithoutLimits().plus(secondLevelBalanceWithoutLimits));
    }

    public Integer getPayedSaturdaysBalance() {
	return payedSaturdaysBalance == null ? 0 : payedSaturdaysBalance;
    }

    public void setPayedSaturdaysBalance(Integer payedSaturdaysBalance) {
	this.payedSaturdaysBalance = payedSaturdaysBalance;
    }

    public void addPayedSaturdaysBalance(Integer payedSaturdaysBalance) {
	setPayedSaturdaysBalance(getPayedSaturdaysBalance() + payedSaturdaysBalance);
    }

    public Integer getPayedSundaysBalance() {
	return payedSundaysBalance == null ? 0 : payedSundaysBalance;
    }

    public void setPayedSundaysBalance(Integer payedSundaysBalance) {
	this.payedSundaysBalance = payedSundaysBalance;
    }

    public void addPayedSundaysBalance(Integer payedSundaysBalance) {
	setPayedSundaysBalance(getPayedSundaysBalance() + payedSundaysBalance);
    }

    public Integer getPayedHolidayBalance() {
	return payedHolidayBalance == null ? 0 : payedHolidayBalance;
    }

    public void setPayedHolidayBalance(Integer payedHolidayBalance) {
	this.payedHolidayBalance = payedHolidayBalance;
    }

    public void addPayedHolidayBalance(Integer payedHolidayBalance) {
	setPayedHolidayBalance(getPayedHolidayBalance() + payedHolidayBalance);
    }

    public Integer getPayedNightlyBalance() {
	return payedNightlyBalance == null ? 0 : payedNightlyBalance;
    }

    public void setPayedNightlyBalance(Integer payedNightlyBalance) {
	this.payedNightlyBalance = payedNightlyBalance;
    }

    public void addPayedNightlyBalance(Integer payedNightlyBalance) {
	setPayedNightlyBalance(getPayedNightlyBalance() + payedNightlyBalance);
    }

    public Integer getPayedWorkWeekBalance() {
	return payedWorkWeekBalance == null ? 0 : payedWorkWeekBalance;
    }

    public void setPayedWorkWeekBalance(Integer payedWorkWeekBalance) {
	this.payedWorkWeekBalance = payedWorkWeekBalance;
    }

    public void addPayedWorkWeekBalance(Integer payedWorkWeekBalance) {
	setPayedWorkWeekBalance(getPayedWorkWeekBalance() + payedWorkWeekBalance);
    }

    public Duration getFirstLevelNightBalance() {
	return firstLevelNightBalance == null ? Duration.ZERO : firstLevelNightBalance;
    }

    public void setFirstLevelNightBalance(Duration firstLevelNightBalance) {
	this.firstLevelNightBalance = firstLevelNightBalance;
    }

    public void addFirstLevelNightBalance(Duration firstLevelNightBalance) {
	setFirstLevelNightBalance(getFirstLevelNightBalance().plus(firstLevelNightBalance));
    }

    public Duration getSecondLevelNightBalance() {
	return secondLevelNightBalance == null ? Duration.ZERO : secondLevelNightBalance;
    }

    public void setSecondLevelNightBalance(Duration secondLevelNightBalance) {
	this.secondLevelNightBalance = secondLevelNightBalance;
    }

    public void addSecondLevelNightBalance(Duration secondLevelNightBalance) {
	setSecondLevelNightBalance(getSecondLevelNightBalance().plus(secondLevelNightBalance));
    }

    public Duration getSecondLevelNightBalanceWithoutLimits() {
	return secondLevelNightBalanceWithoutLimits == null ? Duration.ZERO : secondLevelNightBalanceWithoutLimits;
    }

    public void setSecondLevelNightBalanceWithoutLimits(Duration secondLevelNightBalanceWithoutLimits) {
	this.secondLevelNightBalanceWithoutLimits = secondLevelNightBalanceWithoutLimits;
    }

    public void addSecondLevelNightBalanceWithoutLimits(Duration secondLevelNightBalanceWithoutLimits) {
	setSecondLevelNightBalanceWithoutLimits(getSecondLevelNightBalanceWithoutLimits().plus(
		secondLevelNightBalanceWithoutLimits));
    }

    public Integer getPayedNightWorkWeekBalance() {
	return payedNightWorkWeekBalance == null ? 0 : payedNightWorkWeekBalance;
    }

    public void setPayedNightWorkWeekBalance(Integer payedNightWorkWeekBalance) {
	this.payedNightWorkWeekBalance = payedNightWorkWeekBalance;
    }

    public void addPayedNightWorkWeekBalance(Integer payedNightWorkWeekBalance) {
	setPayedNightWorkWeekBalance(getPayedNightWorkWeekBalance() + payedNightWorkWeekBalance);
    }

    public Integer getExtraNightDays() {
	return extraNightDays == null ? 0 : extraNightDays;
    }

    public void setExtraNightDays(Integer extraNightDays) {
	this.extraNightDays = extraNightDays;
    }

    public void addExtraNightDays(Integer extraNightDays) {
	this.extraNightDays = getExtraNightDays() + extraNightDays;
    }

}
