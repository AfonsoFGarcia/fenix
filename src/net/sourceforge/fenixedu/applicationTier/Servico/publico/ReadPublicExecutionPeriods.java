/*
 * Created on 18/07/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class ReadPublicExecutionPeriods extends FenixService {

    @Service
    public static List<InfoExecutionPeriod> run() throws FenixServiceException {
	final List<InfoExecutionPeriod> result = new ArrayList<InfoExecutionPeriod>();
	for (final ExecutionSemester executionSemester : ExecutionSemester.readPublicExecutionPeriods()) {
	    result.add(InfoExecutionPeriod.newInfoFromDomain(executionSemester));
	}
	return result;
    }
}