/*
 * Created on Nov 2, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.presentationTier.backBeans.manager.advisoriesManagement;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.component.html.HtmlInputHidden;
import javax.faces.event.ActionEvent;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Advisory;
import net.sourceforge.fenixedu.domain.IAdvisory;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IRole;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;

import org.apache.commons.collections.CollectionUtils;

public class AdvisoriesManagementBackingBean extends FenixBackingBean {

    List<IAdvisory> allAdvisories;

    IAdvisory advisory;

    Integer advisoryID;

    String message;

    String subject;

    String sender;

    String expires;

    HtmlInputHidden advisoryIDHidden;

    public AdvisoriesManagementBackingBean() {
        if (getRequestParameter("advisoryID") != null) {
            this.advisoryID = Integer.valueOf(getRequestParameter("advisoryID"));
        }
    }

    public List<IAdvisory> getAllAdvisories() throws FenixFilterException, FenixServiceException {

        if (allAdvisories == null) {

            allAdvisories = new ArrayList<IAdvisory>();
            final Object[] argsToRead = { Advisory.class };
            List<IAdvisory> allAdvisories_ = (List<IAdvisory>) ServiceUtils.executeService(null,
                    "ReadAllDomainObject", argsToRead);

            Date currentDate = Calendar.getInstance().getTime();
            for (IAdvisory advisory : allAdvisories_) {
                if (advisory.getExpires() != null && advisory.getExpires().after(currentDate)) {
                    allAdvisories.add(advisory);
                }
            }
        }
        return allAdvisories;
    }

    public void closeAdvisory(ActionEvent actionEvent) throws FenixFilterException,
            FenixServiceException {

        try {
            final Object[] argsToRead = { this.advisoryID, null, null, null,
                    Calendar.getInstance().getTime() };
            ServiceUtils.executeService(getUserView(), "EditAdvisory", argsToRead);
            allAdvisories = null;

        } catch (FenixServiceException e) {
            setErrorMessage(e.getMessage());
        }
    }

    public String editAdvisory() throws FenixFilterException {

        try {
            Date expires_ = null;
            DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            expires_ = format.parse(this.getExpires());

            final Object[] argsToRead = { this.advisoryID, this.getSender(), this.getSubject(),
                    this.getMessage(), expires_ };
            ServiceUtils.executeService(getUserView(), "EditAdvisory", argsToRead);
            allAdvisories = null;
            return "editAdvisory";

        } catch (FenixServiceException e) {
            setErrorMessage(e.getMessage());
        } catch (ParseException e) {
            setErrorMessage("message.manager.advisory.expirationDate");
        }

        return "";
    }

    public void setAllAdvisories(List<IAdvisory> allAdvisories) {
        this.allAdvisories = allAdvisories;
    }

    public Integer getAdvisoryID() {
        if (this.advisoryID == null && this.advisoryIDHidden != null
                && this.getAdvisoryIDHidden().getValue() != null
                && !this.getAdvisoryIDHidden().getValue().equals("")) {
            this.advisoryID = Integer.valueOf(this.getAdvisoryIDHidden().getValue().toString());
        }
        return advisoryID;
    }

    public void setAdvisoryID(Integer advisoryID) {
        this.advisoryID = advisoryID;
    }

    public IAdvisory getAdvisory() throws FenixFilterException, FenixServiceException {
        if (advisory == null) {
            final Object[] argsToRead = { Advisory.class, this.getAdvisoryID() };
            this.advisory = (IAdvisory) ServiceUtils
                    .executeService(null, "ReadDomainObject", argsToRead);
        }
        return advisory;
    }

    public RoleType getPeopleOfAdvisory() throws FenixFilterException, FenixServiceException {

        List<RoleType> list = new ArrayList<RoleType>();
        List<RoleType> resultList = new ArrayList<RoleType>();

        IAdvisory advisory = this.getAdvisory();
        for (IPerson person : advisory.getPeople()) {

            IRole rolePerson = person.getPersonRole(RoleType.TEACHER);
            IRole roleEmployee = person.getPersonRole(RoleType.EMPLOYEE);
            IRole roleStudent = person.getPersonRole(RoleType.STUDENT);

            if (rolePerson != null) {
                list.add(RoleType.TEACHER);
            }

            if (roleEmployee != null) {
                list.add(RoleType.EMPLOYEE);
            }

            if (roleStudent != null) {
                list.add(RoleType.STUDENT);
            }

            if (!resultList.isEmpty()) {
                resultList = (List<RoleType>) CollectionUtils.intersection(resultList, list);
            }
            else{
                resultList.addAll(list);
            }

            if (resultList.size() == 1) {
                return list.get(0);
            }
            list.clear();
        }
        return null;
    }

    public void setAdvisory(IAdvisory advisory) {
        this.advisory = advisory;
    }

    public String getExpires() throws FenixFilterException, FenixServiceException {
        if (this.expires == null && this.getAdvisory() != null) {
            this.expires = processDate(this.getAdvisory().getExpires());
        }
        return expires;
    }

    public void setExpires(String expires) {
        this.expires = expires;
    }

    public String getMessage() throws FenixFilterException, FenixServiceException {
        if (this.message == null && this.getAdvisory() != null) {
            this.message = this.getAdvisory().getMessage();
        }
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() throws FenixFilterException, FenixServiceException {
        if (this.sender == null && this.getAdvisory() != null) {
            this.sender = this.getAdvisory().getSender();
        }
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSubject() throws FenixFilterException, FenixServiceException {
        if (this.subject == null && this.getAdvisory() != null) {
            this.subject = this.getAdvisory().getSubject();
        }
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    private String processDate(Date date) throws FenixFilterException, FenixServiceException {
                
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        
        return (day + "/" + month + "/" + year + " " + hours + ":" + minutes);
    }

    public HtmlInputHidden getAdvisoryIDHidden() {
        if (this.advisoryIDHidden == null) {
            this.advisoryIDHidden = new HtmlInputHidden();
            this.advisoryIDHidden.setValue(this.getAdvisoryID());
        }
        return advisoryIDHidden;
    }

    public void setAdvisoryIDHidden(HtmlInputHidden advisoryIDHidden) {
        this.advisoryIDHidden = advisoryIDHidden;
    }
}
