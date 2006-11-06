package net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletException;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;

public class SemesterProcessor extends PathProcessor {

    public SemesterProcessor(String forwardURI) {
        super(forwardURI);
    }

    public SemesterProcessor add(SectionProcessor processor) {
        addChild(processor);
        return this;
    }
    
    @Override
    public ProcessingContext getProcessingContext(ProcessingContext parentContext) {
        return new SemesterContext(parentContext);
    }

    @Override
    protected boolean accepts(ProcessingContext context, PathElementsProvider provider) {
        String current = provider.current();
        
        SemesterContext ownContext = (SemesterContext) context;
        ownContext.setSemester(current);
        
        return ownContext.getSemesterNumber() != null;
    }

    @Override
    protected boolean forward(ProcessingContext context, PathElementsProvider provider)
            throws IOException, ServletException {
        if (provider.hasNext()) {
            return false;
        }
        else {
            SemesterContext ownContext = (SemesterContext) context;
            ExecutionCourse executionCourse = ownContext.getExecutionCourse();
            
            if (executionCourse == null) {
                return false;
            }
            else {
                return doForward(context, executionCourse.getIdInternal());
            }
        }
    }
    
    public static class SemesterContext extends AbstractExecutionCourseContext {

        private String semester;
        
        public SemesterContext(ProcessingContext parent) {
            super(parent);
        }

        public String getSemester() {
            return this.semester;
        }

        public void setSemester(String semester) {
            this.semester = semester;
        }
        
        public CurricularCourse getCurricularCourse() {
            return ((ExecutionCourseContext) getParent()).getCurricularCourse();
        }

        public List<ExecutionCourse> getExecutionCourses() {
            SortedSet<ExecutionCourse> result = new TreeSet<ExecutionCourse>(ExecutionCourse.EXECUTION_COURSE_COMPARATOR_BY_EXECUTION_PERIOD_AND_NAME);
            
            Integer semester = getSemesterNumber();
            for (ExecutionCourse executionCourse : getParentExecutionCourses()) {
                if (executionCourse.getExecutionPeriod().getSemester().equals(semester)) {
                    result.add(executionCourse);
                }
            }
            
            return new ArrayList<ExecutionCourse>(result);
        }

        private List<ExecutionCourse> getParentExecutionCourses() {
            return ((ExecutionCourseContext) getParent()).getExecutionCourses();
        }

        public Integer getSemesterNumber() {
            if (getSemester() == null) {
                return null;
            }
            
            String semester = getSemester();
            if (! semester.matches("\\p{Digit}-semestre")) {
                return null;
            }
            
            return new Integer(semester.substring(0, 1));
        }

    }
    
}
