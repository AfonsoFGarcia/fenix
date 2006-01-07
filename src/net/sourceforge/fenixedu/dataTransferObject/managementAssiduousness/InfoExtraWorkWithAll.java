/*
 * Created on 5/Fev/2005
 */
package net.sourceforge.fenixedu.dataTransferObject.managementAssiduousness;

import net.sourceforge.fenixedu.dataTransferObject.InfoEmployeeWithPerson;
import net.sourceforge.fenixedu.domain.managementAssiduousness.ExtraWork;

/**
 * @author T�nia Pous�o
 *
 */
public class InfoExtraWorkWithAll extends InfoExtraWork {

    public void copyFromDomain(ExtraWork extraWork) {
        super.copyFromDomain(extraWork);
        if(extraWork != null) {
            setInfoEmployee(InfoEmployeeWithPerson.newInfoFromDomain(extraWork.getEmployee()));          
        }
    }
    
    public static InfoExtraWork newInfoFromDomain(ExtraWork extraWork) {
        InfoExtraWorkWithAll infoExtraWorkWithAll = null;
        if (extraWork != null) {
            infoExtraWorkWithAll = new InfoExtraWorkWithAll();
            infoExtraWorkWithAll.copyFromDomain(extraWork);
        }
        return infoExtraWorkWithAll;
    }
}
