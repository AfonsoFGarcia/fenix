/*
 * Created on Dec 9, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DeleteCourseGroup extends Service {

    public void run(final Integer courseGroupID, final Integer contextID) throws ExcepcaoPersistencia, FenixServiceException {
        final CourseGroup courseGroup = (CourseGroup) persistentObject
                .readByOID(CourseGroup.class, courseGroupID);
        if (courseGroup == null) {
            throw new FenixServiceException("error.noCourseGroup");
        }
        final Context context = (Context) persistentObject.readByOID(Context.class, contextID);
        if (context == null) {
            throw new FenixServiceException("error.noCourseGroup");
        }
        courseGroup.deleteContext(context);
    }
}
