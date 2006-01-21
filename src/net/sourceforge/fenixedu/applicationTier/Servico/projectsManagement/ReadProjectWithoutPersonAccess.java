/*
 * Created on Jan 11, 2005
 */

package net.sourceforge.fenixedu.applicationTier.Servico.projectsManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoProject;
import net.sourceforge.fenixedu.domain.projectsManagement.Project;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTierOracle.IPersistentSuportOracle;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentSuportOracle;

/**
 * @author Susana Fernandes
 */
public class ReadProjectWithoutPersonAccess extends Service {

    public List run(String userName, String costCenter, String newUserName, String userNumber) throws ExcepcaoPersistencia {
        Integer coordinatorId = new Integer(userNumber);
        List projectCodes = persistentSupport.getIPersistentProjectAccess().readProjectCodesByPersonUsernameAndCoordinator(newUserName, coordinatorId, true);
        IPersistentSuportOracle persistentSupportOracle = PersistentSuportOracle.getInstance();
        List<Project> projectList = persistentSupportOracle.getIPersistentProject().readByCoordinatorAndNotProjectsCodes(coordinatorId, projectCodes);
        List<InfoProject> infoProjects = new ArrayList<InfoProject>();
        for (Project project : projectList) {
            infoProjects.add(InfoProject.newInfoFromDomain(project));
        }
        return infoProjects;
    }

}