package net.sourceforge.fenixedu.dataTransferObject.assiduousness;

import java.io.Serializable;
import java.util.List;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessStatusHistory;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

import org.joda.time.Duration;
import org.joda.time.MutablePeriod;
import org.joda.time.PeriodType;
import org.joda.time.YearMonthDay;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

public class EmployeeWorkSheet implements Serializable {

    Employee employee;

    String unitCode;

    Unit unit;

    List<WorkDaySheet> workDaySheetList;

    Duration totalBalance;

    Duration unjustifiedBalance;

    Duration weeklyRest;

    Duration complementaryWeeklyRest;

    Duration holidayRest;

    Duration nightWork;

    Duration firstHourExtraWork;

    Duration nextHoursExtraWork;

    Duration balanceToCompensate;

    AssiduousnessStatusHistory lastAssiduousnessStatusHistory;

    public EmployeeWorkSheet() {
    }

    public EmployeeWorkSheet(Employee employee, List<WorkDaySheet> workDaySheetList,
	    Duration totalBalance, Duration totalComplementaryWeeklyRestBalance,
	    Duration totalWeeklyRestBalance, Duration holidayRest, Duration nightWork,
	    Duration firstHourExtraWork, Duration nextHoursExtraWork, Duration unjustified,
	    Duration balanceToCompensate) {
	setEmployee(employee);
	setWorkDaySheetList(workDaySheetList);
	setTotalBalance(totalBalance);
	setUnjustifiedBalance(unjustified);
	setWeeklyRest(totalWeeklyRestBalance);
	setComplementaryWeeklyRest(totalComplementaryWeeklyRestBalance);
	setHolidayRest(holidayRest);
	setNightWork(nightWork);
	setFirstHourExtraWork(firstHourExtraWork);
	setNextHoursExtraWork(nextHoursExtraWork);
	setBalanceToCompensate(balanceToCompensate);
    }

    public String getTotalBalanceString() {
	PeriodFormatter fmt = new PeriodFormatterBuilder().printZeroAlways().appendHours()
		.appendSeparator(":").minimumPrintedDigits(2).appendMinutes().toFormatter();
	MutablePeriod finalTotalBalance = new MutablePeriod(getTotalBalance().getMillis(), PeriodType
		.time());
	if (getTotalBalance().toPeriod().getMinutes() < 0) {
	    finalTotalBalance.setMinutes(-getTotalBalance().toPeriod().getMinutes());
	    if (getTotalBalance().toPeriod().getHours() == 0) {
		fmt = new PeriodFormatterBuilder().printZeroAlways().appendLiteral("-").appendHours()
			.appendSeparator(":").minimumPrintedDigits(2).appendMinutes().toFormatter();
	    }
	}
	return fmt.print(finalTotalBalance);
    }

    public String getUnjustifiedBalanceString() {
	PeriodFormatter fmt = new PeriodFormatterBuilder().printZeroAlways().appendHours()
		.appendSeparator(":").minimumPrintedDigits(2).appendMinutes().toFormatter();
	MutablePeriod finalUnjustifiedBalance = new MutablePeriod(getUnjustifiedBalance().getMillis(),
		PeriodType.time());
	return fmt.print(finalUnjustifiedBalance);
    }

    public Employee getEmployee() {
	return employee;
    }

    public void setEmployee(Employee employee) {
	this.employee = employee;
    }

    public String getUnitCode() {
	return unitCode;
    }

    public void setUnitCode(String unitCode) {
	this.unitCode = unitCode;
    }

    public Unit getUnit() {
	return unit;
    }

    public void setUnit(Unit unit) {
	this.unit = unit;
    }

    public Duration getTotalBalance() {

	return totalBalance;
    }

    public void setTotalBalance(Duration totalBalance) {
	this.totalBalance = totalBalance;
    }

    public Duration getUnjustifiedBalance() {
	return unjustifiedBalance;
    }

    public void setUnjustifiedBalance(Duration unjustifiedBalance) {
	this.unjustifiedBalance = unjustifiedBalance;
    }

    public List<WorkDaySheet> getWorkDaySheetList() {
	return workDaySheetList;
    }

    public void setWorkDaySheetList(List<WorkDaySheet> workDaySheetList) {
	this.workDaySheetList = workDaySheetList;
    }

    public Duration getComplementaryWeeklyRest() {
	return complementaryWeeklyRest;
    }

    public void setComplementaryWeeklyRest(Duration complementaryWeeklyRest) {
	this.complementaryWeeklyRest = complementaryWeeklyRest;
    }

    public Duration getWeeklyRest() {
	return weeklyRest;
    }

    public void setWeeklyRest(Duration weeklyRest) {
	this.weeklyRest = weeklyRest;
    }

    public Duration getNightWork() {
	return nightWork;
    }

    public void setNightWork(Duration nightWork) {
	this.nightWork = nightWork;
    }

    public String getComplementaryWeeklyRestString() {
	PeriodFormatter fmt = new PeriodFormatterBuilder().printZeroAlways().appendHours()
		.appendSeparator(":").minimumPrintedDigits(2).appendMinutes().toFormatter();
	MutablePeriod finalComplementaryWeeklyRestWork = new MutablePeriod(getComplementaryWeeklyRest()
		.getMillis(), PeriodType.time());
	return fmt.print(finalComplementaryWeeklyRestWork);
    }

    public String getWeeklyRestString() {
	PeriodFormatter fmt = new PeriodFormatterBuilder().printZeroAlways().appendHours()
		.appendSeparator(":").minimumPrintedDigits(2).appendMinutes().toFormatter();
	MutablePeriod finalWeeklyRestExtraWork = new MutablePeriod(getWeeklyRest().getMillis(),
		PeriodType.time());
	return fmt.print(finalWeeklyRestExtraWork);
    }

    public String getHolidayRestString() {
	PeriodFormatter fmt = new PeriodFormatterBuilder().printZeroAlways().appendHours()
		.appendSeparator(":").minimumPrintedDigits(2).appendMinutes().toFormatter();
	MutablePeriod finalHolidayRestExtraWork = new MutablePeriod(getHolidayRest().getMillis(),
		PeriodType.time());
	return fmt.print(finalHolidayRestExtraWork);
    }

    public Duration getFirstHourExtraWork() {
	return firstHourExtraWork;
    }

    public void setFirstHourExtraWork(Duration firstHourExtraWork) {
	this.firstHourExtraWork = firstHourExtraWork;
    }

    public Duration getHolidayRest() {
	return holidayRest;
    }

    public void setHolidayRest(Duration holidayWork) {
	this.holidayRest = holidayWork;
    }

    public Duration getNextHoursExtraWork() {
	return nextHoursExtraWork;
    }

    public void setNextHoursExtraWork(Duration nextHoursExtraWork) {
	this.nextHoursExtraWork = nextHoursExtraWork;
    }

    public Duration getBalanceToCompensate() {
	return balanceToCompensate;
    }

    public void setBalanceToCompensate(Duration balanceToCompensate) {
	this.balanceToCompensate = balanceToCompensate;
    }

    public AssiduousnessStatusHistory getLastAssiduousnessStatusHistory() {
	return lastAssiduousnessStatusHistory;
    }

    public void setLastAssiduousnessStatusHistory(YearMonthDay beginDate, YearMonthDay endDate) {
	for (AssiduousnessStatusHistory assiduousnessStatusHistory : getEmployee().getAssiduousness()
		.getStatusBetween(beginDate, endDate)) {
	    if (assiduousnessStatusHistory.getEndDate() != null) {
		if (lastAssiduousnessStatusHistory == null
			|| assiduousnessStatusHistory.getEndDate().isAfter(
				lastAssiduousnessStatusHistory.getEndDate())) {
		    this.lastAssiduousnessStatusHistory = assiduousnessStatusHistory;
		}
	    } else {
		this.lastAssiduousnessStatusHistory = assiduousnessStatusHistory;
		break;
	    }

	}

    }
}
