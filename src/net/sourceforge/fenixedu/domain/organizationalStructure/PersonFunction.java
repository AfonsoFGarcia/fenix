package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.util.Date;

public class PersonFunction extends PersonFunction_Base {
    
    public void edit(IFunction function, Date beginDate, Date endDate, Integer credits){        
        this.setFunction(function);
        this.setCredits(credits);
        this.setEndDate(endDate);
        this.setBeginDate(beginDate);
    }
    
}
