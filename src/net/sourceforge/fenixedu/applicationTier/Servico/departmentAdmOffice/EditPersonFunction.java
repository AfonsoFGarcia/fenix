/*
 * Created on Oct 28, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.applicationTier.Servico.departmentAdmOffice;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;

import org.joda.time.YearMonthDay;

public class EditPersonFunction extends FenixService {

	public void run(Integer personFunctionID, Integer functionID, YearMonthDay beginDate, YearMonthDay endDate, Double credits)
			throws FenixServiceException, DomainException {

		PersonFunction person_Function = (PersonFunction) rootDomainObject.readAccountabilityByOID(personFunctionID);

		if (person_Function == null) {
			throw new FenixServiceException("error.no.personFunction");
		}

		Function function = (Function) rootDomainObject.readAccountabilityTypeByOID(functionID);

		if (function == null) {
			throw new FenixServiceException("erro.noFunction");
		}

		person_Function.edit(beginDate, endDate, credits);
	}
}
