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
public interface IDistributedTest extends IDomainObject {
	public abstract Calendar getBeginDate();
	public abstract Calendar getBeginHour();
	public abstract Calendar getEndDate();
	public abstract Calendar getEndHour();
	public abstract CorrectionAvailability getCorrectionAvailability();
	public abstract Integer getKeyTest();
	public abstract Boolean getStudentFeedback();
	public abstract ITest getTest();
	public abstract TestType getTestType();
	public abstract void setBeginDate(Calendar calendar);
	public abstract void setBeginHour(Calendar calendar);
	public abstract void setEndDate(Calendar calendar);
	public abstract void setEndHour(Calendar calendar);
	public abstract void setCorrectionAvailability(CorrectionAvailability availability);
	public abstract void setKeyTest(Integer integer);
	public abstract void setStudentFeedback(Boolean feedback);
	public abstract void setTest(ITest test);
	public abstract void setTestType(TestType type);
}