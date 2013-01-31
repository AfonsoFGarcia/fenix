package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.onlineTests.Test;

public class ReadTest extends FenixService {

	public Test run(Integer executionCourseId, Integer testId) throws FenixServiceException {
		final Test test = rootDomainObject.readTestByOID(testId);
		if (test == null) {
			throw new FenixServiceException();
		}
		return test;
	}

}
