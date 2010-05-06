package net.sourceforge.fenixedu.domain.teacher.evaluation;

import java.text.Collator;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;

public class TeacherEvaluationProcess extends TeacherEvaluationProcess_Base {

    public static Comparator<TeacherEvaluationProcess> COMPARATOR_BY_EVALUEE = new Comparator<TeacherEvaluationProcess>() {
	@Override
	public int compare(TeacherEvaluationProcess p1, TeacherEvaluationProcess p2) {
	    final int i = Collator.getInstance().compare(p1.getEvaluee().getName(), p2.getEvaluee().getName());
	    return i == 0 ? p2.hashCode() - p1.hashCode() : i;
	}
    };

    public static Comparator<TeacherEvaluationProcess> COMPARATOR_BY_INTERVAL = new Comparator<TeacherEvaluationProcess>() {
	@Override
	public int compare(TeacherEvaluationProcess p1, TeacherEvaluationProcess p2) {
	    return FacultyEvaluationProcess.COMPARATOR_BY_INTERVAL.compare(p1.getFacultyEvaluationProcess(), p2
		    .getFacultyEvaluationProcess());
	}
    };

    public TeacherEvaluationProcess() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public TeacherEvaluationProcess(final FacultyEvaluationProcess facultyEvaluationProcess, final Person evaluee,
	    final Person evaluator) {
	setFacultyEvaluationProcess(facultyEvaluationProcess);
	setEvaluee(evaluee);
	setEvaluator(evaluator);
    }

    public TeacherEvaluation getCurrentTeacherEvaluation() {
	TeacherEvaluation last = null;
	for (TeacherEvaluation evaluation : getTeacherEvaluationSet()) {
	    if (last == null || evaluation.getCreatedDate().isAfter(last.getCreatedDate())) {
		last = evaluation;
	    }
	}
	return last;
    }

    public TeacherEvaluationState getState() {
	TeacherEvaluation current = getCurrentTeacherEvaluation();
	return current != null ? current.getState() : getFacultyEvaluationProcess().getState();
    }

    public boolean isAutoEvaluationLocked() {
	TeacherEvaluation current = getCurrentTeacherEvaluation();
	return current != null && current.getAutoEvaluationLock() != null;
    }

    public String getAutoEvaluationMark() {
	TeacherEvaluation current = getCurrentTeacherEvaluation();
	return current != null ? current.getAutoEvaluationMark() : null;
    }

    public TeacherEvaluationType getType() {
	TeacherEvaluation current = getCurrentTeacherEvaluation();
	return current != null ? current.getType() : null;
    }

    public String getEvaluationMark() {
	TeacherEvaluation current = getCurrentTeacherEvaluation();
	return current != null ? current.getEvaluationMark() : null;
    }

    public boolean isEvaluationLocked() {
	return true;// getTeacherEvaluation() != null &&
	// getTeacherEvaluation().getEvaluationLock() != null;
    }

    public boolean isInEvaluationInterval() {
	return getFacultyEvaluationProcess().getEvaluationInterval().containsNow();
    }

    public Set<TeacherEvaluationFileBean> getTeacherEvaluationFileBeanSet() {
	Set<TeacherEvaluationFileBean> teacherEvaluationFileBeans = new HashSet<TeacherEvaluationFileBean>();
	TeacherEvaluation currentTeacherEvaluation = getCurrentTeacherEvaluation();
	if (currentTeacherEvaluation != null) {
	    for (TeacherEvaluationFileType teacherEvaluationFileType : currentTeacherEvaluation.getEvaluationFileSet()) {
		TeacherEvaluationFileBean e = new TeacherEvaluationFileBean(currentTeacherEvaluation, teacherEvaluationFileType);
		teacherEvaluationFileBeans.add(e);
	    }
	}
	return teacherEvaluationFileBeans;
    }

    public Set<TeacherEvaluationFileBean> getTeacherAutoEvaluationFileBeanSet() {
	Set<TeacherEvaluationFileBean> teacherEvaluationFileBeans = new HashSet<TeacherEvaluationFileBean>();
	TeacherEvaluation currentTeacherEvaluation = getCurrentTeacherEvaluation();
	if (currentTeacherEvaluation != null) {
	    for (TeacherEvaluationFileType teacherEvaluationFileType : currentTeacherEvaluation.getAutoEvaluationFileSet()) {
		TeacherEvaluationFileBean e = new TeacherEvaluationFileBean(currentTeacherEvaluation, teacherEvaluationFileType);
		teacherEvaluationFileBeans.add(e);
	    }
	}
	return teacherEvaluationFileBeans;
    }
}
