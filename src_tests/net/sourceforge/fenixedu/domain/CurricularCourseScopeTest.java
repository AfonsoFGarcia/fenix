package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;



public class CurricularCourseScopeTest  extends DomainTestBase {

	private ICurricularCourseScope curricularCourseScopeToDelete;
	private ICurricularCourseScope curricularCourseScopeNotToDelete;
	
	protected void setUp() throws Exception {
        super.setUp();
		
		curricularCourseScopeToDelete = new CurricularCourseScope();
		curricularCourseScopeToDelete.setIdInternal(1);
		curricularCourseScopeNotToDelete = new CurricularCourseScope();
		curricularCourseScopeNotToDelete.setIdInternal(2);
		
		ICurricularCourse cc1 = new CurricularCourse();
		cc1.setIdInternal(1);
		
		ICurricularSemester cs1 = new CurricularSemester();
		cs1.setIdInternal(1);
		
		IWrittenEvaluation we1 = new WrittenEvaluation();
		we1.setIdInternal(1);
		IWrittenEvaluation we2 = new WrittenEvaluation();
		we2.setIdInternal(2);
		
		curricularCourseScopeToDelete.setCurricularCourse(cc1);
		curricularCourseScopeToDelete.setCurricularSemester(cs1);
		
		curricularCourseScopeNotToDelete.setCurricularCourse(cc1);
		curricularCourseScopeNotToDelete.setCurricularSemester(cs1);
		curricularCourseScopeNotToDelete.addAssociatedWrittenEvaluations(we1);
		curricularCourseScopeNotToDelete.addAssociatedWrittenEvaluations(we2);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }
	
	public void testDeleteCurricularCourseGroup() {
		try {
			curricularCourseScopeNotToDelete.deleteCurricularCourseScope();
			fail("Should not have been deleted.");
		} catch (DomainException e) {

		}
		
		try {
			curricularCourseScopeToDelete.deleteCurricularCourseScope();
		} catch (DomainException e) {
			fail("Should have been deleted.");
		}
		
		
		assertNull(curricularCourseScopeToDelete.getCurricularCourse());
		assertNull(curricularCourseScopeToDelete.getCurricularSemester());
		assertTrue(curricularCourseScopeToDelete.getAssociatedWrittenEvaluations().isEmpty());
		
		assertNotNull(curricularCourseScopeNotToDelete.getCurricularCourse());
		assertNotNull(curricularCourseScopeNotToDelete.getCurricularSemester());
		assertFalse(curricularCourseScopeNotToDelete.getAssociatedWrittenEvaluations().isEmpty());
	}

}
