/*
 * Created on Jan 12, 2005
 */

package ServidorAplicacao.Servico.projectsManagement;

import java.util.ArrayList;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.projectsManagement.InfoCoordinatorReport;
import DataBeans.projectsManagement.InfoRubric;
import DataBeans.projectsManagement.InfoSummaryReportLine;
import Dominio.IEmployee;
import Dominio.IPerson;
import Dominio.ITeacher;
import Dominio.projectsManagement.ISummaryReportLine;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistenteOracle.Oracle.PersistentSuportOracle;
import Util.projectsManagement.ReportType;

/**
 * @author Susana Fernandes
 */
public class ReadSummaryReport implements IService {

    public ReadSummaryReport() {
    }

    public List run(UserView userView) throws FenixServiceException, ExcepcaoPersistencia {
        List infoProjectReportList = new ArrayList();
        ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
        Integer userNumber = getUserNumber(persistentSuport, userView);
        if (userNumber != null) {
            PersistentSuportOracle p = PersistentSuportOracle.getInstance();

            Integer thisCoordinator = p.getIPersistentProjectUser().getUserCoordId(userNumber);
            List coordinatorsCodes = persistentSuport.getIPersistentProjectAccess().readCoordinatorsCodesByPersonUsernameAndDates(userView.getUtilizador());
            if (thisCoordinator != null)
                coordinatorsCodes.add(thisCoordinator);
            for (int coord = 0; coord < coordinatorsCodes.size(); coord++) {
                InfoCoordinatorReport infoReport = new InfoCoordinatorReport();
                List infoLines = new ArrayList();
                infoReport.setInfoCoordinator(InfoRubric.newInfoFromDomain(p.getIPersistentProjectUser().readProjectCoordinator(
                        (Integer) coordinatorsCodes.get(coord))));
                List projectCodes = persistentSuport.getIPersistentProjectAccess().readProjectCodesByPersonUsernameAndCoordinator(
                        userView.getUtilizador(), new Integer(infoReport.getInfoCoordinator().getCode()), false);
                List lines = p.getIPersistentSummaryReport().readByCoordinatorAndProjectCodes(ReportType.SUMMARY,
                        new Integer(infoReport.getInfoCoordinator().getCode()), projectCodes);
                for (int line = 0; line < lines.size(); line++)
                    infoLines.add(InfoSummaryReportLine.newInfoFromDomain((ISummaryReportLine) lines.get(line)));
                infoReport.setLines(infoLines);
                infoProjectReportList.add(infoReport);
            }
        }
        return infoProjectReportList;
    }

    private Integer getUserNumber(ISuportePersistente sp, IUserView userView) throws ExcepcaoPersistencia {
        Integer userNumber = null;
        ITeacher teacher = (ITeacher) sp.getIPersistentTeacher().readTeacherByUsername(userView.getUtilizador());
        if (teacher != null)
            userNumber = teacher.getTeacherNumber();
        else {
            IPerson person = sp.getIPessoaPersistente().lerPessoaPorUsername(userView.getUtilizador());
            IEmployee employee = (IEmployee) sp.getIPersistentEmployee().readByPerson(person);
            if (employee != null)
                userNumber = employee.getEmployeeNumber();
        }
        return userNumber;
    }
}