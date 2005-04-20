/*
 * Created on Jan 11, 2005
 */

package net.sourceforge.fenixedu.applicationTier.Servico.projectsManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoProject;
import net.sourceforge.fenixedu.domain.IEmployee;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.projectsManagement.IProject;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentSuportOracle;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class ReadUserProjects implements IService {

    public ReadUserProjects() {
    }

    public List run(String userView, Boolean all) throws ExcepcaoPersistencia {
        List infoProjectList = new ArrayList();

        ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();

        Integer userNumber = null;

        ITeacher teacher = persistentSuport.getIPersistentTeacher().readTeacherByUsername(userView);
        if (teacher != null)
            userNumber = teacher.getTeacherNumber();
        else {
            IPerson person = persistentSuport.getIPessoaPersistente().lerPessoaPorUsername(userView);
            IEmployee employee = persistentSuport.getIPersistentEmployee().readByPerson(person);
            if (employee != null)
                userNumber = employee.getEmployeeNumber();
        }
        if (userNumber != null) {
            PersistentSuportOracle p = PersistentSuportOracle.getInstance();
            List projectList = p.getIPersistentProject().readByUserLogin(userNumber.toString());
            if (all.booleanValue())
                projectList.addAll(p.getIPersistentProject().readByProjectsCodes(
                        persistentSuport.getIPersistentProjectAccess().readProjectCodesByPersonUsernameAndDate(userView)));
            for (int i = 0; i < projectList.size(); i++)
                infoProjectList.add(InfoProject.newInfoFromDomain((IProject) projectList.get(i)));
        }
        return infoProjectList;
    }    
}