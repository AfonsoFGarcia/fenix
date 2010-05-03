/*
 * Created on Jan 11, 2005
 */

package net.sourceforge.fenixedu.applicationTier.Servico.projectsManagement;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.projectsManagement.Project;
import net.sourceforge.fenixedu.domain.projectsManagement.ProjectAccess;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentProject;

/**
 * @author Susana Fernandes
 */
public class InsertNewProjectAccess extends FenixService {

    public void run(String userView, String costCenter, String username, GregorianCalendar beginDate, GregorianCalendar endDate,
	    Boolean it, String userNumber) throws ExcepcaoPersistencia {
	Person person = Person.readPersonByUsername(username);
	if (person == null)
	    throw new IllegalArgumentException();

	// deletePastProjectAccesses(person);

	Integer coordinatorCode = new Integer(userNumber);
	Boolean isCostCenter = setProjectsRoles(person, costCenter, it);

	List<Integer> projectCodes = new ArrayList<Integer>();

	for (ProjectAccess projectAccess : person.readProjectAccessesByCoordinator(coordinatorCode, it)) {
	    projectCodes.add(projectAccess.getKeyProject());
	}

	PersistentProject persistentProject = new PersistentProject();
	List<Project> projectList = persistentProject.readByCoordinatorAndNotProjectsCodes(coordinatorCode, projectCodes, it);

	for (Project project : projectList) {
	    if (ProjectAccess.getByPersonAndProject(person, new Integer(project.getProjectCode()), it) != null) {
		throw new IllegalArgumentException();
	    }
	    ProjectAccess projectAccess = new ProjectAccess();
	    projectAccess.setPerson(person);
	    projectAccess.setKeyProjectCoordinator(coordinatorCode);
	    projectAccess.setKeyProject(new Integer(project.getProjectCode()));
	    projectAccess.setBeginDate(beginDate);
	    projectAccess.setEndDate(endDate);
	    projectAccess.setCostCenter(isCostCenter);
	    projectAccess.setItProject(it);
	}

    }

    public void run(String userView, String costCenter, String username, String[] projectCodes, GregorianCalendar beginDate,
	    GregorianCalendar endDate, Boolean it, String userNumber) {
	Person person = Person.readPersonByUsername(username);
	if (person == null)
	    throw new IllegalArgumentException();

	Boolean isCostCenter = setProjectsRoles(person, costCenter, it);

	for (int i = 0; i < projectCodes.length; i++) {
	    Integer projectCode = new Integer(projectCodes[i]);
	    ProjectAccess projectAccess = getPersonOldProjectAccess(person, projectCode, it);
	    if (projectAccess == null) {
		projectAccess = new ProjectAccess();
		projectAccess.setPerson(person);
		projectAccess.setKeyProjectCoordinator(new Integer(userNumber));
		projectAccess.setKeyProject(projectCode);
		projectAccess.setCostCenter(isCostCenter);
		projectAccess.setItProject(it);
	    }
	    projectAccess.setBeginDate(beginDate);
	    projectAccess.setEndDate(endDate);
	}

	// deletePastProjectAccesses(person);
    }

    private ProjectAccess getPersonOldProjectAccess(Person person, Integer projectCode, Boolean it) {
	for (ProjectAccess projectAccess : person.getProjectAccesses()) {
	    if (projectAccess.getKeyProject().equals(projectCode) && projectAccess.getItProject().equals(it)) {
		return projectAccess;
	    }
	}
	return null;
    }

    private Boolean setProjectsRoles(Person person, String costCenter, Boolean it) {
	Boolean isCostCenter = Boolean.FALSE;
	RoleType roleType = RoleType.PROJECTS_MANAGER;
	if (it) {
	    roleType = RoleType.IT_PROJECTS_MANAGER;
	}
	if (costCenter != null && !costCenter.equals("")) {
	    roleType = RoleType.INSTITUCIONAL_PROJECTS_MANAGER;
	    isCostCenter = Boolean.TRUE;
	}

	if (!hasProjectsManagerRole(person, roleType)) {
	    person.getPersonRoles().add(Role.getRoleByRoleType(roleType));
	}
	return isCostCenter;
    }

    // private void deletePastProjectAccesses(Person person) {
    // List<ProjectAccess> projectAccessesToRemove = new
    // ArrayList<ProjectAccess>();
    // Date currentDate = Calendar.getInstance().getTime();
    //
    // for (ProjectAccess projectAccess : person.getProjectAccesses()) {
    // if (projectAccess.getEnd().before(currentDate)) {
    // projectAccessesToRemove.add(projectAccess);
    // }
    // }
    //
    // for (ProjectAccess projectAccess : projectAccessesToRemove) {
    // projectAccess.delete();
    // }
    // }

    private boolean hasProjectsManagerRole(Person person, RoleType roleType) {
	Iterator iterator = person.getPersonRoles().iterator();
	while (iterator.hasNext())
	    if (((Role) iterator.next()).getRoleType().equals(roleType))
		return true;
	return false;
    }

}