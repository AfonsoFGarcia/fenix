/*
 * Created on 19/Ago/2003
 *
 */
package Dominio;

import java.util.Calendar;

import Util.CorrectionAvailability;
import Util.TestType;

/**
 * @author Susana Fernandes
 */
public class DistributedTest extends DomainObject implements IDistributedTest {
	private Calendar beginDate;
	private Calendar endDate;
	private Calendar beginHour;
	private Calendar endHour;
	private TestType testType;
	private CorrectionAvailability correctionAvailability;
	private Boolean studentFeedback;
	private ITest test;
	private Integer keyTest;

	public DistributedTest() {
	}

	public DistributedTest(Integer distributedTestId) {
		setIdInternal(distributedTestId);
	}

	public Calendar getBeginDate() {
		return beginDate;
	}

	public Calendar getBeginHour() {
		return beginHour;
	}

	public CorrectionAvailability getCorrectionAvailability() {
		return correctionAvailability;
	}

	public Calendar getEndDate() {
		return endDate;
	}

	public Calendar getEndHour() {
		return endHour;
	}

	public Integer getKeyTest() {
		return keyTest;
	}

	public Boolean getStudentFeedback() {
		return studentFeedback;
	}

	public ITest getTest() {
		return test;
	}

	public TestType getTestType() {
		return testType;
	}

	public void setBeginDate(Calendar calendar) {
		beginDate = calendar;
	}

	public void setBeginHour(Calendar calendar) {
		beginHour = calendar;
	}

	public void setCorrectionAvailability(CorrectionAvailability availability) {
		correctionAvailability = availability;
	}

	public void setEndDate(Calendar calendar) {
		endDate = calendar;
	}

	public void setEndHour(Calendar calendar) {
		endHour = calendar;
	}

	public void setKeyTest(Integer integer) {
		keyTest = integer;
	}

	public void setStudentFeedback(Boolean studentFeedback) {
		this.studentFeedback = studentFeedback;
	}

	public void setTest(ITest test) {
		this.test = test;
	}

	public void setTestType(TestType type) {
		testType = type;
	}
}
