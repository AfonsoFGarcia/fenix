package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourseSite;

public class EditCustomizationOptions extends FenixService {

    public boolean run(Integer infoExecutionCourseCode, final String alternativeSite, final String mail,
	    final Boolean dynamicMailDistribution, final String initialStatement, final String introduction)
	    throws FenixServiceException {
	final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(infoExecutionCourseCode);
	final ExecutionCourseSite site = executionCourse.getSite();

	site.setAlternativeSite(alternativeSite);
	site.setMail(mail);
	site.setInitialStatement(initialStatement);
	site.setIntroduction(introduction);
	site.setDynamicMailDistribution(dynamicMailDistribution);

	return true;
    }

}
