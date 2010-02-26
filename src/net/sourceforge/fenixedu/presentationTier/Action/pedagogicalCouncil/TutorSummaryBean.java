package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Tutorship;
import net.sourceforge.fenixedu.domain.TutorshipSummary;

public class TutorSummaryBean extends TutorSearchBean {

    private static final long serialVersionUID = 1L;

    private ExecutionSemester executionSemester;

    public boolean isAbleToCreateSummary() {
	for (TutorshipSummary ts : getPastSummaries()) {
	    if (ts.getSemester().isCurrent()) {
		return false;
	    }
	}

	return true;
    }

    public ExecutionSemester getActiveSemester() {
	return ExecutionSemester.readActualExecutionSemester();
    }

    public List<CreateSummaryBean> getAvailableSummaries() {

	/* each CreateSummaryBean must have a unique degree */
	List<CreateSummaryBean> result = new ArrayList<CreateSummaryBean>();

	if (getTeacher() != null) {
	    /* add active - already created - summaries */
	    for (TutorshipSummary ts : getTeacher().getTutorshipSummaries()) {
		if (ts.isActive()) {
		    CreateSummaryBean createSummaryBean = new CreateSummaryBean(ts);
		    result.add(createSummaryBean);
		}
	    }

	    /* add - not created - available summaries */
	    if (TutorshipSummary.canBeCreated()) {
		for (Tutorship t : getTeacher().getActiveTutorships()) {
		    boolean addDegree = true;
		    Degree studentDegree = t.getStudent().getDegree();

		    /* check if degree is already added to the result */
		    for (CreateSummaryBean createSummaryBean : result) {
			if (createSummaryBean.getDegree().equals(studentDegree)) {
			    addDegree = false;
			    break;
			}
		    }

		    if (addDegree) {
			CreateSummaryBean createSummaryBean = new CreateSummaryBean(getTeacher(), getActiveSemester(),
				studentDegree);
			result.add(createSummaryBean);
		    }
		}
	    }
	}

	return result;
    }

    public List<TutorshipSummary> getPastSummaries() {

	List<TutorshipSummary> result = new ArrayList<TutorshipSummary>();

	if (getTeacher() != null && getExecutionSemester() != null) {
	    for (TutorshipSummary ts : getTeacher().getTutorshipSummaries()) {
		if ((!ts.isActive()) && ts.getSemester().equals(getExecutionSemester())) {
		    result.add(ts);
		}
	    }
	} else {
	    if (getTeacher() != null) {
		for (TutorshipSummary ts : getTeacher().getTutorshipSummaries()) {
		    if (!ts.isActive()) {
			result.add(ts);
		    }
		}

	    } else {
		if (getExecutionSemester() != null) {
		    for (TutorshipSummary ts : getExecutionSemester().getTutorshipSummaries()) {
			if (!ts.isActive()) {
			    result.add(ts);
			}
		    }
		}
	    }
	}

	return result;
    }

    public ExecutionSemester getExecutionSemester() {
	return executionSemester;
    }

    public void setExecutionSemester(ExecutionSemester executionSemester) {
	this.executionSemester = executionSemester;
    }
}