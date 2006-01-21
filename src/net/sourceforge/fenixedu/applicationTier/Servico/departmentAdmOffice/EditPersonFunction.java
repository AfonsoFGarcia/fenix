/*
 * Created on Oct 28, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.applicationTier.Servico.departmentAdmOffice;

import java.util.Date;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class EditPersonFunction extends Service {

    public void run(Integer personFunctionID, Integer functionID, Date beginDate, Date endDate,
            Double credits) throws ExcepcaoPersistencia, FenixServiceException, DomainException {
        PersonFunction person_Function = (PersonFunction) persistentObject
                .readByOID(PersonFunction.class, personFunctionID);

        if (person_Function == null) {
            throw new FenixServiceException("error.no.personFunction");
        }

        Function function = (Function) persistentObject.readByOID(
                Function.class, functionID);

        if (function == null) {
            throw new FenixServiceException("erro.noFunction");
        }

        person_Function.edit(function, beginDate, endDate, credits);
    }
}
