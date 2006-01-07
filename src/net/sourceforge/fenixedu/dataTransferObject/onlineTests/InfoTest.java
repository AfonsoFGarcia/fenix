/*
 * Created on 28/Jul/2003
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.onlineTests;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.domain.onlineTests.Test;

/**
 * @author Susana Fernandes
 */
public class InfoTest extends InfoObject {
    private String title;

    private String information;

    private Integer numberOfQuestions;

    private Date creationDate;

    private Date lastModifiedDate;

    private InfoTestScope infoTestScope;

    private List<InfoTestQuestion> infoTestQuestions;

    public InfoTestScope getInfoTestScope() {
        return infoTestScope;
    }

    public Integer getNumberOfQuestions() {
        return numberOfQuestions;
    }

    public String getTitle() {
        return title;
    }

    public void setInfoTestScope(InfoTestScope infoTestScope) {
        this.infoTestScope = infoTestScope;
    }

    public void setNumberOfQuestions(Integer integer) {
        numberOfQuestions = integer;
    }

    public void setTitle(String string) {
        title = string;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setCreationDate(Date date) {
        creationDate = date;
    }

    public void setLastModifiedDate(Date date) {
        lastModifiedDate = date;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String string) {
        information = string;
    }

    public List<InfoTestQuestion> getInfoTestQuestions() {
        return infoTestQuestions;
    }

    public void setInfoTestQuestions(List<InfoTestQuestion> infoTestQuestions) {
        this.infoTestQuestions = infoTestQuestions;
    }

    public String getLastModifiedDateFormatted() {
        String result = "";
        Date date = getLastModifiedDate();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date.getTime());
        result += calendar.get(Calendar.DAY_OF_MONTH);
        result += "/";
        result += calendar.get(Calendar.MONTH) + 1;
        result += "/";
        result += calendar.get(Calendar.YEAR);
        result += " ";
        result += calendar.get(Calendar.HOUR_OF_DAY);
        result += ":";
        if (calendar.get(Calendar.MINUTE) < 10)
            result += "0";
        result += calendar.get(Calendar.MINUTE);

        return result;
    }

    public String getCreationDateFormatted() {
        String result = "";
        Date date = getCreationDate();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date.getTime());
        result += calendar.get(Calendar.DAY_OF_MONTH);
        result += "/";
        result += calendar.get(Calendar.MONTH) + 1;
        result += "/";
        result += calendar.get(Calendar.YEAR);
        result += " ";
        result += calendar.get(Calendar.HOUR_OF_DAY);
        result += ":";
        if (calendar.get(Calendar.MINUTE) < 10) {
            result += "0";
        }
        result += calendar.get(Calendar.MINUTE);

        return result;
    }

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof InfoTest) {
            InfoTest infoTest = (InfoTest) obj;
            result = getIdInternal().equals(infoTest.getIdInternal());
            result = result || (getInfoTestScope().equals(infoTest.getInfoTestScope())) && (getTitle().equals(infoTest.getTitle()))
                    && (getInformation().equals(infoTest.getInformation())) && (getNumberOfQuestions().equals(infoTest.getNumberOfQuestions()))
                    && (getCreationDate().equals(infoTest.getCreationDate())) && (getLastModifiedDate().equals(infoTest.getLastModifiedDate()));
        }
        return result;
    }

    public void copyFromDomain(Test test) {
        super.copyFromDomain(test);
        if (test != null) {
            setTitle(test.getTitle());
            setInformation(test.getInformation());
            setNumberOfQuestions(test.getTestQuestionsCount());
            setCreationDate(test.getCreationDate());
            setLastModifiedDate(test.getLastModifiedDate());
        }
    }

    public static InfoTest newInfoFromDomain(Test test) {
        InfoTest infoTest = null;
        if (test != null) {
            infoTest = new InfoTest();
            infoTest.copyFromDomain(test);
        }
        return infoTest;
    }
}