package net.sourceforge.fenixedu.domain.messaging;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Comparator;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourseBoardPermittedGroupType;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class ExecutionCourseAnnouncementBoard extends ExecutionCourseAnnouncementBoard_Base {

    public static final Comparator<ExecutionCourseAnnouncementBoard> COMPARE_BY_EXECUTION_PERIOD_AND_NAME = new Comparator<ExecutionCourseAnnouncementBoard>() {
	public int compare(ExecutionCourseAnnouncementBoard o1, ExecutionCourseAnnouncementBoard o2) {
	    int result = -o1.getExecutionCourse().getExecutionPeriod().compareTo(o2.getExecutionCourse().getExecutionPeriod());
	    if (result == 0) {
		result = o1.getExecutionCourse().getNome().compareTo(o2.getExecutionCourse().getNome());
	    }
	    return (result == 0) ? o1.getExecutionCourse().getIdInternal().compareTo(o2.getExecutionCourse().getIdInternal())
		    : result;
	}
    };

    public ExecutionCourseAnnouncementBoard() {
	super();
    }

    public ExecutionCourseAnnouncementBoard(String name, ExecutionCourse executionCourse, Group writers, Group readers,
	    Group managers, ExecutionCourseBoardPermittedGroupType writersGroupType,
	    ExecutionCourseBoardPermittedGroupType readersGroupType, ExecutionCourseBoardPermittedGroupType managersGroupType) {

	this();
	init(name, executionCourse, writers, readers, managers, writersGroupType, readersGroupType, managersGroupType);
    }

    private void init(String name, ExecutionCourse executionCourse, Group writers, Group readers, Group managers,
	    ExecutionCourseBoardPermittedGroupType writersGroupType, ExecutionCourseBoardPermittedGroupType readersGroupType,
	    ExecutionCourseBoardPermittedGroupType managersGroupType) {

	checkParameters(name, executionCourse);

	setName(new MultiLanguageString(name.replace('?', ' ').replace('/', ' ')));
	setExecutionCourse(executionCourse);
	setMandatory(Boolean.FALSE);

	setWriters(writers);
	setReaders(readers);
	setManagers(managers);

	setExecutionCoursePermittedWriteGroupType(writersGroupType);
	setExecutionCoursePermittedReadGroupType(readersGroupType);
	setExecutionCoursePermittedManagementGroupType(managersGroupType);
    }

    private void checkParameters(String name, ExecutionCourse executionCourse) {
	if (name == null) {
	    throw new DomainException("error.messaging.ExecutionCourseAnnouncementBoard.name.cannot.be.null");
	}
	if (executionCourse == null) {
	    throw new DomainException("error.messaging.ExecutionCourseAnnouncementBoard.executionCourse.cannot.be.null");
	}
    }

    @Override
    public String getFullName() {
	final StringBuilder result = new StringBuilder(20);
	result.append(getExecutionCourse().getNome()).append(" ");
	result.append(getExecutionCourse().getExecutionPeriod().getSemester()).append("�Sem. ");
	result.append(getExecutionCourse().getExecutionPeriod().getExecutionYear().getYear());
	return result.toString();
    }

    @Override
    public String getQualifiedName() {
	final StringBuilder result = new StringBuilder(20);
	result.append(getExecutionCourse().getNome()).append(" ");
	result.append(getExecutionCourse().getExecutionPeriod().getSemester()).append("�Sem. ");
	result.append(getExecutionCourse().getExecutionPeriod().getExecutionYear().getYear()).append(" ");
	final Iterator<Degree> degrees = getExecutionCourse().getDegreesSortedByDegreeName().iterator();
	while (degrees.hasNext()) {
	    result.append(degrees.next().getSigla()).append(degrees.hasNext() ? "," : "");
	}
	return result.toString();
    }

    @Override
    protected void disconnect() {
	removeExecutionCourse();
	super.disconnect();
    }

    @Override
    public boolean isCurrentUserApprover() {
	return false;
    }

    @Override
    public boolean hasApprover(Person person) {
	return false;
    }

    @Override
    public boolean isPublicToApprove() {
	return false;
    }

    @Override
    public Boolean getInitialAnnouncementsApprovedState() {
	return true;
    }

    public String getSite() {
	return null;
    }

    public String getSiteParamForAnnouncementBoard(Announcement announcement) {
	String base = super.getSiteParamForAnnouncementBoard(announcement);

	StringBuffer actionPath = new StringBuffer();
	ExecutionCourse executionCourse = this.getExecutionCourse();

	actionPath.append("&executionCourseID=" + executionCourse.getIdInternal());
	actionPath.append("&");
	actionPath.append(ContentInjectionRewriter.CONTEXT_ATTRIBUTE_NAME);
	actionPath.append("=");
	actionPath.append(executionCourse.getSite().getReversePath());
	return base + actionPath.toString();
    }

}