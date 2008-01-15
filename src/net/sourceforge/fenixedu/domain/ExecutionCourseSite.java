package net.sourceforge.fenixedu.domain;

import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.accessControl.ExecutionCourseTeachersAndStudentsGroup;
import net.sourceforge.fenixedu.domain.accessControl.ExecutionCourseTeachersGroup;
import net.sourceforge.fenixedu.domain.contents.Node;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.messaging.ExecutionCourseForum;
import net.sourceforge.fenixedu.injectionCode.IGroup;
import net.sourceforge.fenixedu.util.MultiLanguageString;

public class ExecutionCourseSite extends ExecutionCourseSite_Base {
    
    protected ExecutionCourseSite() {
        super(); 
        
        setDynamicMailDistribution(false);
        setLessonPlanningAvailable(false);
    }

    public ExecutionCourseSite(ExecutionCourse course) {
        this();
        
        setSiteExecutionCourse(course);
        MultiLanguageString forumName = new MultiLanguageString(course.getNome());
        createForum(forumName, forumName);
    }

    public void edit(final String initialStatement, final String introduction, final String mail,
            final String alternativeSite) {

        setInitialStatement(initialStatement);
        setIntroduction(introduction);
        setMail(mail);
        setAlternativeSite(alternativeSite);
    }
    
    public void copyCustomizationOptionsFrom(ExecutionCourseSite siteFrom) {
        setMail(siteFrom.getMail());
        setAlternativeSite(siteFrom.getAlternativeSite());
        setInitialStatement(siteFrom.getInitialStatement());
        setIntroduction(siteFrom.getIntroduction());
    }

    @Override
    protected void deleteRelations() {
        super.deleteRelations();
        
        removeSiteExecutionCourse();
    }

    @Override
    public List<IGroup> getContextualPermissionGroups() {
        List<IGroup> groups = super.getContextualPermissionGroups();
        
        ExecutionCourse executionCourse = getSiteExecutionCourse();
        
        groups.add(new ExecutionCourseTeachersGroup(executionCourse));
        groups.add(new ExecutionCourseTeachersAndStudentsGroup(executionCourse));
        
        return groups;
    }

    @Override
    public IGroup getOwner() {
        return new ExecutionCourseTeachersGroup(getSiteExecutionCourse());
    
    }

    @Override
    public String getAuthorName() {
        return getSiteExecutionCourse().getNome();
    }
 
    @Override
    public ExecutionPeriod getExecutionPeriod() {
        return getSiteExecutionCourse().getExecutionPeriod();
    }

    public static ExecutionCourseSite readExecutionCourseSiteByOID(Integer oid) {
        Site site = (Site) RootDomainObject.readDomainObjectByOID(ExecutionCourseSite.class, oid);
        if (site == null) {
            return null;
        }
        
        if (site instanceof ExecutionCourseSite) {
            return (ExecutionCourseSite) site;
        }
        else {
            return null;
        }
    }

    @Override
    public boolean isFileClassificationSupported() {
        return true;
    }
    
    @Override
    public boolean isScormContentAccepted() {
        return true;
    }

    @Deprecated
    public ExecutionCourse getExecutionCourse() {
	return super.getSiteExecutionCourse();
    }
    
    public Collection<ExecutionCourseForum> getForuns() {
	return getChildren(ExecutionCourseForum.class);
    }
    
    public void addForum(ExecutionCourseForum executionCourseForum) {
	checkIfCanAddForum(executionCourseForum.getNormalizedName());
	addChild(executionCourseForum);	
    }
    
    public void removeForum(ExecutionCourseForum executionCourseForum) {
	removeChild(executionCourseForum);
    }
    
    public void checkIfCanAddForum(MultiLanguageString name) {
	if (hasForumWithName(name)) {
	    throw new DomainException("executionCourse.already.existing.forum");
	}
    }

    public boolean hasForumWithName(MultiLanguageString name) {
	return getForumByName(name) != null;
    }

    public ExecutionCourseForum getForumByName(MultiLanguageString name) {
	for (final ExecutionCourseForum executionCourseForum : getForuns()) {
	    if (executionCourseForum.getNormalizedName().equalInAnyLanguage(name)) {
		return executionCourseForum;
	    }
	}

	return null;
    }

    public void createForum(MultiLanguageString name, MultiLanguageString description) {

	if (hasForumWithName(name)) {
	    throw new DomainException("executionCourse.already.existing.forum");
	}
	addForum(new ExecutionCourseForum(name, description));
    }

    
}
