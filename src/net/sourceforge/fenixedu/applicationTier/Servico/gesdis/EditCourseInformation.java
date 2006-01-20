/*
 * Created on 12/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.gesdis;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.gesdis.InfoCourseReport;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.gesdis.CourseReport;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author jdnf and mrsp
 */
public class EditCourseInformation extends Service {

    public void run(Integer courseReportID, InfoCourseReport infoCourseReport, String newReport) throws ExcepcaoPersistencia,
            FenixServiceException {
        final CourseReport courseReport;
        if (courseReportID != 0) {
            courseReport = (CourseReport) persistentObject.readByOID(CourseReport.class, courseReportID);
        } else {
            final ExecutionCourse executionCourse = (ExecutionCourse) persistentObject.readByOID(ExecutionCourse.class, infoCourseReport.getInfoExecutionCourse().getIdInternal());

            courseReport = executionCourse.createCourseReport(newReport);
        }

        if (courseReport == null)
            throw new FenixServiceException();

        courseReport.edit(newReport);
    }

}
