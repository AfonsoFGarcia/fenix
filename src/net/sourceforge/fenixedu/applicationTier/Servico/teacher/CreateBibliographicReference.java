package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;

/**
 * @author Fernanda Quit�rio
 */
public class CreateBibliographicReference extends FenixService {

    public Boolean run(Integer infoExecutionCourseID, String newBibliographyTitle, String newBibliographyAuthors,
	    String newBibliographyReference, String newBibliographyYear, Boolean newBibliographyOptional)
	    throws FenixServiceException {

	final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(infoExecutionCourseID);
	if (executionCourse == null)
	    throw new InvalidArgumentsServiceException();

	executionCourse.createBibliographicReference(newBibliographyTitle, newBibliographyAuthors, newBibliographyReference,
		newBibliographyYear, newBibliographyOptional);
	return true;
    }
}