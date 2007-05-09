package net.sourceforge.fenixedu.domain.organizationalStructure;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.YearMonthDay;

public class EmployeeContract extends EmployeeContract_Base {
    
    public EmployeeContract(Person person, YearMonthDay beginDate, YearMonthDay endDate, Unit unit, AccountabilityTypeEnum contractType) {
        super();
        super.init(person, beginDate, endDate, unit);
	AccountabilityType accountabilityType = AccountabilityType.readAccountabilityTypeByType(contractType);
	setAccountabilityType(accountabilityType);
    }
    
    @Override
    public Unit getWorkingUnit() {
	if(getAccountabilityType().getType().equals(AccountabilityTypeEnum.WORKING_CONTRACT)) {
	    return getUnit();    
	}
	return null;
    } 
    
    @Override
    public Unit getMailingUnit() {
	if(getAccountabilityType().getType().equals(AccountabilityTypeEnum.MAILING_CONTRACT)) {
	    return getUnit();    
	}
	return null;
    } 
    
    @Override
    public void setAccountabilityType(AccountabilityType accountabilityType) {
        super.setAccountabilityType(accountabilityType);
        if(!accountabilityType.getType().equals(AccountabilityTypeEnum.WORKING_CONTRACT) && 
        	!accountabilityType.getType().equals(AccountabilityTypeEnum.MAILING_CONTRACT)) {
            throw new DomainException("error.EmployeeContract.invalid.accountabilityType");
        }
    }
    
    @Override
    public void setChildParty(Party childParty) {        
	super.setChildParty(childParty);
	if(!((Person)childParty).hasEmployee()) {
	    throw new DomainException("error.EmployeeContract.person.not.has.employee");
	}
    }
    
    @Override
    public Employee getEmployee() {
	return getPerson().getEmployee();
    }
}
