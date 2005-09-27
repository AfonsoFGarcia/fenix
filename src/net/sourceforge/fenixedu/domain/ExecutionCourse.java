package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.gesdis.CourseReport;
import net.sourceforge.fenixedu.domain.gesdis.ICourseReport;
import net.sourceforge.fenixedu.fileSuport.INode;
import net.sourceforge.fenixedu.util.ProposalState;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

public class ExecutionCourse extends ExecutionCourse_Base {

    public String toString() {
        String result = "[EXECUTION_COURSE";
        result += ", codInt=" + getIdInternal();
        result += ", sigla=" + getSigla();
        result += ", nome=" + getNome();
        result += ", theoreticalHours=" + getTheoreticalHours();
        result += ", praticalHours=" + getPraticalHours();
        result += ", theoPratHours=" + getTheoPratHours();
        result += ", labHours=" + getLabHours();
        result += ", executionPeriod=" + getExecutionPeriod();
        result += "]";
        return result;
    }

    public String getSlideName() {
        String result = getParentNode().getSlideName() + "/EC" + getIdInternal();
        return result;
    }

    public INode getParentNode() {
        IExecutionPeriod executionPeriod = getExecutionPeriod();
        return executionPeriod;
    }

    public List<IGrouping> getGroupings() {
        List<IGrouping> result = new ArrayList();       
        for (final IExportGrouping exportGrouping : this.getExportGroupings()) {
            if (exportGrouping.getProposalState().getState() == ProposalState.ACEITE || 
                    exportGrouping.getProposalState().getState() == ProposalState.CRIADOR) {
                result.add(exportGrouping.getGrouping());
            }
        }
        return result;
    }

    public IGrouping getGroupingByName(String groupingName) {
        for (final IGrouping grouping : this.getGroupings()) {
            if (grouping.getName().equals(groupingName)) {
                return grouping;
            }
        }
        return null;
    }

    public boolean existsGroupingExecutionCourse(
            IExportGrouping groupPropertiesExecutionCourse) {
        return getExportGroupings().contains(groupPropertiesExecutionCourse);
    }

    public boolean existsGroupingExecutionCourse() {
        return getExportGroupings().isEmpty();
    }

    public boolean hasProposals() {
        boolean result = false;
        boolean found = false;
        List groupPropertiesExecutionCourseList = getExportGroupings();
        Iterator iter = groupPropertiesExecutionCourseList.iterator();
        while (iter.hasNext() && !found) {

            IExportGrouping groupPropertiesExecutionCourseAux = (IExportGrouping) iter
                    .next();
            if (groupPropertiesExecutionCourseAux.getProposalState().getState().intValue() == 3) {
                result = true;
                found = true;
            }

        }
        return result;
    }

    public boolean isMasterDegreeOnly() {
        for (final ICurricularCourse curricularCourse : getAssociatedCurricularCourses()) {
            if (curricularCourse.getDegreeCurricularPlan().getDegree().getTipoCurso() != DegreeType.MASTER_DEGREE) {
                return false;
            }
        }
        return true;
    }

    public void edit(String name, String acronym, double theoreticalHours,
            double theoreticalPraticalHours, double praticalHours, double laboratoryHours, String comment) {

        if (name == null || acronym == null || theoreticalHours < 0 || theoreticalPraticalHours < 0
                || praticalHours < 0 || laboratoryHours < 0 || comment == null)
            throw new NullPointerException();

        setNome(name);
        setSigla(acronym);
        setTheoreticalHours(theoreticalHours);
        setTheoPratHours(theoreticalPraticalHours);
        setPraticalHours(praticalHours);
        setLabHours(laboratoryHours);
        setComment(comment);
    }

    public void createSite() {
        final ISite site = new Site();
        site.setExecutionCourse(this);
    }

    public void createEvaluationMethod(final String evaluationElements,
            final String evaluationElementsEng) {
        if (evaluationElements == null || evaluationElementsEng == null)
            throw new NullPointerException();

        final IEvaluationMethod evaluationMethod = new EvaluationMethod();
        evaluationMethod.setEvaluationElements(evaluationElements);
        evaluationMethod.setEvaluationElementsEn(evaluationElementsEng);
        evaluationMethod.setExecutionCourse(this);
    }

    public void createBibliographicReference(final String title, final String authors,
            final String reference, final String year, final Boolean optional) {
        if (title == null || authors == null || reference == null || year == null || optional == null)
            throw new NullPointerException();

        final IBibliographicReference bibliographicReference = new BibliographicReference();
        bibliographicReference.setTitle(title);
        bibliographicReference.setAuthors(authors);
        bibliographicReference.setReference(reference);
        bibliographicReference.setYear(year);
        bibliographicReference.setOptional(optional);
        bibliographicReference.setExecutionCourse(this);
    }

    public void createCourseReport(String report) {
        if (report == null)
            throw new NullPointerException();

        final ICourseReport courseReport = new CourseReport();
        courseReport.setReport(report);
        courseReport.setLastModificationDate(Calendar.getInstance().getTime());
        courseReport.setExecutionCourse(this);
    }

    private ISummary createSummary(String title, String summaryText, Integer studentsNumber,
            Boolean isExtraLesson) {
        
        if (title == null || summaryText == null || isExtraLesson == null)
            throw new NullPointerException();

        final ISummary summary = new Summary();
        summary.setTitle(title);
        summary.setSummaryText(summaryText);
        summary.setStudentsNumber(studentsNumber);
        summary.setIsExtraLesson(isExtraLesson);
        summary.setLastModifiedDate(Calendar.getInstance().getTime());
        summary.setExecutionCourse(this);

        return summary;
    }

    public ISummary createSummary(String title, String summaryText, Integer studentsNumber,
            Boolean isExtraLesson, IProfessorship professorship) {

        if (professorship == null)
            throw new NullPointerException();

        final ISummary summary = createSummary(title, summaryText, studentsNumber, isExtraLesson);
        summary.setProfessorship(professorship);
        summary.setTeacher(null);
        summary.setTeacherName(null);
        
        return summary;
    }

    public ISummary createSummary(String title, String summaryText, Integer studentsNumber,
            Boolean isExtraLesson, ITeacher teacher) {

        if (teacher == null)
            throw new NullPointerException();
        
        final ISummary summary = createSummary(title, summaryText, studentsNumber, isExtraLesson);
        summary.setTeacher(teacher);
        summary.setProfessorship(null);
        summary.setTeacherName(null);
        
        return summary;
    }

    public ISummary createSummary(String title, String summaryText, Integer studentsNumber,
            Boolean isExtraLesson, String teacherName) {

        if (teacherName == null)
            throw new NullPointerException();
        
        final ISummary summary = createSummary(title, summaryText, studentsNumber, isExtraLesson);
        summary.setTeacherName(teacherName);
        summary.setTeacher(null);
        summary.setProfessorship(null);    
        
        return summary;
    }
    
    public List<IProfessorship> responsibleFors() {
        final List<IProfessorship> res = new ArrayList<IProfessorship>();
        for (final IProfessorship professorship : this.getProfessorships()) {
            if (professorship.getResponsibleFor())
                res.add(professorship);
        }
        return res;
    }
	
	public IAttends getAttendsByStudent (final IStudent student) {
		
		return (IAttends)CollectionUtils.find(getAttends(),new Predicate() {

			public boolean evaluate(Object o) {
				IAttends attends = (IAttends) o;
				return attends.getAluno().equals(student);
			}
			
		});
	}
    
    public List<IExam> getAssociatedExams() {
        List<IExam> associatedExams = new ArrayList<IExam>();
        
        for (IEvaluation evaluation : this.getAssociatedEvaluations()) {
            if (evaluation instanceof IExam) {
                associatedExams.add((IExam) evaluation);
            }
        }
        
        return associatedExams;
    }

    public List<IWrittenTest> getAssociatedWrittenTests() {
        List<IWrittenTest> associatedWrittenTests = new ArrayList<IWrittenTest>();
        
        for (IEvaluation evaluation : this.getAssociatedEvaluations()) {
            if (evaluation instanceof IWrittenTest) {
                associatedWrittenTests.add((IWrittenTest) evaluation);
            }
        }
        
        return associatedWrittenTests;
    }
    
}
