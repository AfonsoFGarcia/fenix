package net.sourceforge.fenixedu.domain;

/**
 * 
 * @author Fernanda Quit�rio created on 16/06/2004
 */
public interface IWrittenEvaluationCurricularCourseScope extends IDomainObject {

    public void setWrittenEvaluation(IWrittenEvaluation writtenEvaluation);

    public void setCurricularCourseScope(ICurricularCourseScope curricularCourseScope);

    public IWrittenEvaluation getWrittenEvaluation();

    public ICurricularCourseScope getCurricularCourseScope();

}