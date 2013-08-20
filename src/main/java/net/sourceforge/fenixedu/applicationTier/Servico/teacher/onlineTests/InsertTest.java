package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;


import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.onlineTests.Test;
import net.sourceforge.fenixedu.domain.onlineTests.TestScope;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class InsertTest {

    protected Integer run(Integer executionCourseId, String title, String information) throws InvalidArgumentsServiceException {
        ExecutionCourse executionCourse = AbstractDomainObject.fromExternalId(executionCourseId);
        if (executionCourse == null) {
            throw new InvalidArgumentsServiceException();
        }
        TestScope testScope = TestScope.readByDomainObject(ExecutionCourse.class, executionCourseId);
        if (testScope == null) {
            testScope = new TestScope(ExecutionCourse.class.getName(), executionCourseId);
        }
        Test test = new Test(title, information, testScope);
        return test.getExternalId();
    }

    // Service Invokers migrated from Berserk

    private static final InsertTest serviceInstance = new InsertTest();

    @Service
    public static Integer runInsertTest(Integer executionCourseId, String title, String information)
            throws InvalidArgumentsServiceException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseId);
        return serviceInstance.run(executionCourseId, title, information);
    }

}