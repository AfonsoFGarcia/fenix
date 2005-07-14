/*
 * Created on Jul 13, 2005
 *  by jdnf and mrsp
 */
package net.sourceforge.fenixedu.domain;

import java.util.Calendar;
import java.util.Date;

import net.sourceforge.fenixedu.domain.gesdis.CourseReport;
import net.sourceforge.fenixedu.domain.gesdis.ICourseReport;

public class CourseReportTest extends DomainTestBase {

    private ICourseReport courseReport;
    private IExecutionCourse executionCourse;

    protected void setUp() throws Exception {
        super.setUp();

        executionCourse = new ExecutionCourse();
        executionCourse.setIdInternal(1);

        courseReport = new CourseReport();
        courseReport.setIdInternal(1);
        courseReport.setReport("report");
        courseReport.setLastModificationDate(Calendar.getInstance().getTime());
        courseReport.setExecutionCourse(executionCourse);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testEdit() {
        try {
            courseReport.edit(null);
            fail("Expected Null Pointer Exception!");
        } catch (NullPointerException e) {
            assertEquals("Different Report in CourseReport!", "report", courseReport.getReport());
            assertEquals("Different ExecutionCourse in CourseReport!",
                    courseReport.getExecutionCourse(), executionCourse);
        }

        final Date dateBeforeEdition = Calendar.getInstance().getTime();
        sleep(1000);
        courseReport.edit("newReport");
        sleep(1000);
        final Date dateAfterEdition = Calendar.getInstance().getTime();

        assertEquals("Different Report in CourseReport!", "newReport", courseReport.getReport());
        assertEquals("Different ExecutionCourse in CourseReport!", courseReport.getExecutionCourse(),
                executionCourse);
        assertTrue("Expected LastModificationDate After an initial timestamp!", courseReport
                .getLastModificationDate().after(dateBeforeEdition));
        assertTrue("Expected LastModificationDate Before an initial timestamp!", courseReport
                .getLastModificationDate().before(dateAfterEdition));
    }

    public void testDelete() {
        assertEquals("Different ExecutionCourse in CourseReport!", courseReport.getExecutionCourse(),
                executionCourse);
        courseReport.delete();
        assertNull("Expected Null ExecutionCourse in CourseReport!", courseReport.getExecutionCourse());
    }

}
