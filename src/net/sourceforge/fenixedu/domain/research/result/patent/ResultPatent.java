package net.sourceforge.fenixedu.domain.research.result.patent;

import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.Partial;

public class ResultPatent extends ResultPatent_Base {
    
    public  ResultPatent() {
        super();
    }
    
    public ResultPatent(String title, Partial registrationDate, Partial approvalDate, List<Person> participations, String modifyedBy) {
        this();
        checkParameters(title, registrationDate, approvalDate, participations, modifyedBy);
        setTitle(title);
        setRegistrationDate(registrationDate);
        setApprovalDate(approvalDate);
        setParticipations(participations);
        setModificationDateAndAuthor(modifyedBy);
    }
    
    private void checkParameters(String title, Partial registrationDate, Partial approvalDate, List<Person> participations, String modifyedBy) {
        if (title == null || title.equals("")) {
            throw new DomainException("error.ResultPatent.title.cannot.be.null");
        }
        if (registrationDate == null) {
            throw new DomainException("error.ResultPatent.registrationDate.cannot.be.null");
        }
        if (approvalDate == null) {
            throw new DomainException("error.ResultPatent.approvalDate.cannot.be.null");
        }
        if(!isApprovalDateAfterRegistrationDate(approvalDate, registrationDate)){
            throw new DomainException("error.ResultPatent.approvalDate.before.registrationDate");
        }
        if (participations == null || participations.size()==0) {
            throw new DomainException("error.ResultPatent.participations.cannot.be.null");
        }
        if (modifyedBy == null || modifyedBy.equals("")) {
            throw new DomainException("error.ResultAssociations.changedBy.null");
        }
    }
    
    public void setPatentEdit(String title, Partial registrationDate, Partial approvalDate, List<Person> participations, String modifyedBy){
        checkParameters(title,registrationDate,approvalDate,participations,modifyedBy);
        this.setTitle(title);
        this.setRegistrationDate(registrationDate);
        this.setApprovalDate(approvalDate);
        this.setParticipations(participations);
        this.setModificationDateAndAuthor(modifyedBy);
    }
    
    private boolean isApprovalDateAfterRegistrationDate(Partial approvalDate, Partial registrationDate) {
        if(approvalDate.isEqual(registrationDate)) {
            return false;
        }
        if(approvalDate.isAfter(registrationDate)) {
            return true;
        }
        return false;
    }
    
    public enum ResultPatentType {
        
        LOCAL,
        NATIONAL,
        INTERNATIONAL;
              
        public String getName() {
            return name();
        }  
    }
    
    public enum ResultPatentStatus {
        
        /*type defined in CERIF*/
        APPLIED,
        PUBLISHED,
        GRANTED,
        MAINTAINED;
              
        public String getName() {
            return name();
        }  
    }
}
