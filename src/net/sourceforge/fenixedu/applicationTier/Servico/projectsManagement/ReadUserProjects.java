/*
 * Created on Jan 11, 2005
 */

package net.sourceforge.fenixedu.applicationTier.Servico.projectsManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoProject;
import net.sourceforge.fenixedu.domain.projectsManagement.Project;
import net.sourceforge.fenixedu.domain.projectsManagement.ProjectAccess;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentSuportOracle;

import org.apache.commons.lang.StringUtils;

/**
 * @author Susana Fernandes
 */
public class ReadUserProjects extends Service {

    public List<InfoProject> run(String username, String costCenter, Boolean all, String userNumber)
	    throws ExcepcaoPersistencia {
	List<InfoProject> infoProjectList = new ArrayList<InfoProject>();

	List<Integer> projectCodes = new ArrayList<Integer>();
	List<Integer> costCenterCodes = new ArrayList<Integer>();
	List<ProjectAccess> accesses = ProjectAccess.getAllByPersonUsernameAndDatesAndCostCenter(
		username, costCenter);
	for (ProjectAccess access : accesses) {
	    Integer keyProject = access.getKeyProject();
	    if (keyProject == null && access.getCostCenter()
		    && !costCenterCodes.contains(access.getKeyProjectCoordinator())) {
		costCenterCodes.add(access.getKeyProjectCoordinator());
	    } else if (!projectCodes.contains(keyProject)) {
		projectCodes.add(keyProject);
	    }
	}

	PersistentSuportOracle p = PersistentSuportOracle.getProjectDBInstance();
	List<Project> projectList = new ArrayList<Project>();
	if (StringUtils.isEmpty(costCenter) || costCenter.equals(userNumber)) {
	    projectList = p.getIPersistentProject().readByUserLogin(userNumber);
	}

	if (all) {
	    projectList.addAll(p.getIPersistentProject().readByProjectsCodes(projectCodes));
	    for (Integer ccCode : costCenterCodes) {
		projectList.addAll(p.getIPersistentProject().readByCoordinatorAndNotProjectsCodes(
			ccCode, null));
	    }
	}
	for (Project project : projectList) {
	    infoProjectList.add(InfoProject.newInfoFromDomain(project));
	}
	return infoProjectList;
    }
}