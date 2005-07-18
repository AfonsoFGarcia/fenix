/*
 * Created on Jun 29, 2005
 *  by jdnf and mrsp
 */
package net.sourceforge.fenixedu.domain;

import java.util.Calendar;
import java.util.Date;

public class ExecutionCourseTest extends DomainTestBase {

    private IExecutionCourse executionCourse;

    protected void setUp() throws Exception {
        super.setUp();

        executionCourse = new ExecutionCourse();
        executionCourse.setIdInternal(0);
        
        executionCourse.setNome("name");
        executionCourse.setSigla("acronym");
        executionCourse.setTheoreticalHours(4.0);
        executionCourse.setTheoPratHours(0.0);
        executionCourse.setPraticalHours(2.0);
        executionCourse.setLabHours(0.0);
        executionCourse.setComment("comment");
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    public void testEdit() {
        try {
            executionCourse.edit(null, null, 0.0, 0.0, 0.0, 0.0, null);
            fail ("Expected NullPointerException!");
        } catch (NullPointerException e) {
            assertEquals("Different ExecutionCourse Name!", "name", executionCourse.getNome());
            assertEquals("Different ExecutionCourse Acronym!", "acronym", executionCourse.getSigla());
            assertEquals("Different ExecutionCourse Theoretical Hours!", 4.0, executionCourse.getTheoreticalHours());
            assertEquals("Different ExecutionCourse TheoreticalPratical Hours!", 0.0, executionCourse.getTheoPratHours());
            assertEquals("Different ExecutionCourse Pratical Hours!", 2.0, executionCourse.getPraticalHours());
            assertEquals("Different ExecutionCourse Laboratory Hours!", 0.0, executionCourse.getLabHours());
            assertEquals("Different ExecutionCourse Comment!", "comment", executionCourse.getComment());
        }
        
        try {
            executionCourse.edit(null, null, 0.0, 2.0, 0.0, 0.0, "newComment");
            fail ("Expected NullPointerException!");
        } catch (NullPointerException e) {
            assertEquals("Different ExecutionCourse Name!", "name", executionCourse.getNome());
            assertEquals("Different ExecutionCourse Acronym!", "acronym", executionCourse.getSigla());
            assertEquals("Different ExecutionCourse Theoretical Hours!", 4.0, executionCourse.getTheoreticalHours());
            assertEquals("Different ExecutionCourse TheoreticalPratical Hours!", 0.0, executionCourse.getTheoPratHours());
            assertEquals("Different ExecutionCourse Pratical Hours!", 2.0, executionCourse.getPraticalHours());
            assertEquals("Different ExecutionCourse Laboratory Hours!", 0.0, executionCourse.getLabHours());
            assertEquals("Different ExecutionCourse Comment!", "comment", executionCourse.getComment());
        }
        
        try {
            executionCourse.edit("newName", null, 0.0, 0.0, 4.0, 0.0, null);
            fail ("Expected NullPointerException!");
        } catch (NullPointerException e) {
            assertEquals("Different ExecutionCourse Name!", "name", executionCourse.getNome());
            assertEquals("Different ExecutionCourse Acronym!", "acronym", executionCourse.getSigla());
            assertEquals("Different ExecutionCourse Theoretical Hours!", 4.0, executionCourse.getTheoreticalHours());
            assertEquals("Different ExecutionCourse TheoreticalPratical Hours!", 0.0, executionCourse.getTheoPratHours());
            assertEquals("Different ExecutionCourse Pratical Hours!", 2.0, executionCourse.getPraticalHours());
            assertEquals("Different ExecutionCourse Laboratory Hours!", 0.0, executionCourse.getLabHours());
            assertEquals("Different ExecutionCourse Comment!", "comment", executionCourse.getComment());
        }
        
        executionCourse.edit("newName", "newAcronym", 2.0, 1.0, 2.0, 1.0, "newComment");
        assertEquals("Different ExecutionCourse Name!", "newName", executionCourse.getNome());
        assertEquals("Different ExecutionCourse Acronym!", "newAcronym", executionCourse.getSigla());
        assertEquals("Different ExecutionCourse Theoretical Hours!", 2.0, executionCourse.getTheoreticalHours());
        assertEquals("Different ExecutionCourse TheoreticalPratical Hours!", 1.0, executionCourse.getTheoPratHours());
        assertEquals("Different ExecutionCourse Pratical Hours!", 2.0, executionCourse.getPraticalHours());
        assertEquals("Different ExecutionCourse Laboratory Hours!", 1.0, executionCourse.getLabHours());
        assertEquals("Different ExecutionCourse Comment!", "newComment", executionCourse.getComment());
        
    }

    public void testCreateSite() {
        executionCourse.setSite(null);

        assertNull("Expected Null Site in ExecutionCourse!", executionCourse.getSite());
        executionCourse.createSite();
        assertNotNull("Expected Not Null Site in ExecutionCourse!", executionCourse.getSite());
        assertTrue("Expected the Same Execution Course in created Site!", executionCourse.getSite()
                .getExecutionCourse().equals(executionCourse));
        assertEquals("Size unexpected in Site Announcements!", 0, executionCourse.getSite()
                .getAssociatedAnnouncementsCount());
        assertEquals("Size unexpected in Site Sections!", 0, executionCourse.getSite()
                .getAssociatedSectionsCount());
    }

    public void testCreateEvaluationMethod() {
        executionCourse.setEvaluationMethod(null);

        assertNull("Expected Null Evaluation Method in ExecutionCourse!", executionCourse
                .getEvaluationMethod());

        try {
            executionCourse.createEvaluationMethod(null, null);
            fail("Expected NullPointerException!");
        } catch (NullPointerException e) {
            assertNull("Expected Null Evaluation Method in ExecutionCourse!", executionCourse
                    .getEvaluationMethod());
        }

        try {
            executionCourse.createEvaluationMethod(null, "evaluationElementsEng");
            fail("Expected NullPointerException!");
        } catch (NullPointerException e) {
            assertNull("Expected Null Evaluation Method in ExecutionCourse!", executionCourse
                    .getEvaluationMethod());
        }

        try {
            executionCourse.createEvaluationMethod("evaluationElements", null);
            fail("Expected NullPointerException!");
        } catch (NullPointerException e) {
            assertNull("Expected Null Evaluation Method in ExecutionCourse!", executionCourse
                    .getEvaluationMethod());
        }

        executionCourse.createEvaluationMethod("evaluationElements", "evaluationElementsEng");
        assertNotNull("Expected Not Null EvaluationMethod in ExecutionCourse!", executionCourse
                .getEvaluationMethod());
        assertEquals("Different EvaluationElements in EvaluationMethod!", "evaluationElements",
                executionCourse.getEvaluationMethod().getEvaluationElements());
        assertEquals("Different EvaluationElementsEng in EvaluationMethod!", "evaluationElementsEng",
                executionCourse.getEvaluationMethod().getEvaluationElementsEn());
        assertTrue("Different ExecutionCourse in EvaluationMethod!", executionCourse
                .getEvaluationMethod().getExecutionCourse().equals(executionCourse));

    }

    public void testCreateBibliographicReference() {
        assertEquals("Size unexpected for AssociatedBibliographicReference!", 0, executionCourse
                .getAssociatedBibliographicReferencesCount());
        try {
            executionCourse.createBibliographicReference(null, null, null, null, null);
            fail("Expected NullPointerException!");
        } catch (NullPointerException e) {
            assertEquals("Size unexpected for AssociatedBibliographicReference!", 0, executionCourse
                    .getAssociatedBibliographicReferencesCount());
        }

        try {
            executionCourse.createBibliographicReference("title", null, "reference", null, true);
            fail("Expected NullPointerException!");
        } catch (NullPointerException e) {
            assertEquals("Size unexpected for AssociatedBibliographicReference!", 0, executionCourse
                    .getAssociatedBibliographicReferencesCount());
        }

        try {
            executionCourse.createBibliographicReference(null, "authors", null, "year", true);
            fail("Expected NullPointerException!");
        } catch (NullPointerException e) {
            assertEquals("Size unexpected for AssociatedBibliographicReference!", 0, executionCourse
                    .getAssociatedBibliographicReferencesCount());
        }

        executionCourse.createBibliographicReference("title", "authors", "reference", "year", true);
        assertEquals("Size unexpected for AssociatedBibliographicReference!", 1, executionCourse
                .getAssociatedBibliographicReferencesCount());
        assertEquals("Different Title in BibliographicReference!", "title", executionCourse
                .getAssociatedBibliographicReferences(0).getTitle());
        assertEquals("Different Authors in BibliographicReference!", "authors", executionCourse
                .getAssociatedBibliographicReferences(0).getAuthors());
        assertEquals("Different Reference in BibliographicReference!", "reference", executionCourse
                .getAssociatedBibliographicReferences(0).getReference());
        assertEquals("Different Year in BibliographicReference!", "year", executionCourse
                .getAssociatedBibliographicReferences(0).getYear());
        assertTrue("Different Optional in BibliographicReference!", executionCourse
                .getAssociatedBibliographicReferences(0).getOptional());
        assertEquals("Different ExecutionCourse in BibliographicReference!", executionCourse,
                executionCourse.getAssociatedBibliographicReferences(0).getExecutionCourse());
    }

    public void testCreateCourseReport() {
        executionCourse.setCourseReport(null);
        assertNull("Expected Null CourseReport in ExecutionCourse!", executionCourse.getCourseReport());

        try {
            executionCourse.createCourseReport(null);
            fail("Expected Null Pointer Exception");
        } catch (NullPointerException e) {
            assertNull("Expected Null CourseReport in ExecutionCourse!", executionCourse
                    .getCourseReport());
        }

        final Date dateBeforeCreation = Calendar.getInstance().getTime();
        sleep(1000);
        executionCourse.createCourseReport("report");
        sleep(1000);
        final Date dateAfterCreation = Calendar.getInstance().getTime();

        assertEquals("Different Report in CourseReport!", "report", executionCourse.getCourseReport()
                .getReport());
        assertTrue("Expected LastModificationDate After an initial timestamp!", executionCourse
                .getCourseReport().getLastModificationDate().after(dateBeforeCreation));
        assertTrue("Expected LastModificationDate Before an initial timestamp!", executionCourse
                .getCourseReport().getLastModificationDate().before(dateAfterCreation));
        assertEquals("Different ExecutionCourse in CourseReport!", executionCourse.getCourseReport()
                .getExecutionCourse(), executionCourse);
    }

}
