/*
 * Created on 19/Ago/2003
 */
package Dominio;

import java.util.Calendar;
import java.util.List;

import Util.tests.CorrectionAvailability;
import Util.tests.TestType;

/**
 * @author Susana Fernandes
 */
public interface IDistributedTest extends IDomainObject {

    public abstract String getTitle();

    public abstract String getTestInformation();

    public abstract Calendar getBeginDate();

    public abstract Calendar getBeginHour();

    public abstract Calendar getEndDate();

    public abstract Calendar getEndHour();

    public abstract CorrectionAvailability getCorrectionAvailability();

    public abstract Boolean getImsFeedback();

    public abstract TestType getTestType();

    public abstract Integer getNumberOfQuestions();

    public abstract ITestScope getTestScope();

    public abstract Integer getKeyTestScope();

    public abstract List getStudentLogs();

    public abstract List getStudentQuestions();

    public abstract void setTitle(String string);

    public abstract void setTestInformation(String string);

    public abstract void setBeginDate(Calendar calendar);

    public abstract void setBeginHour(Calendar calendar);

    public abstract void setEndDate(Calendar calendar);

    public abstract void setEndHour(Calendar calendar);

    public abstract void setCorrectionAvailability(
            CorrectionAvailability availability);

    public abstract void setImsFeedback(Boolean imsFeedback);

    public abstract void setTestType(TestType type);

    public abstract void setNumberOfQuestions(Integer integer);

    public abstract void setTestScope(ITestScope scope);

    public abstract void setKeyTestScope(Integer integer);

    public abstract void setStudentLogs(List studentLogs);

    public abstract void setStudentQuestions(List studentQuestions);
}