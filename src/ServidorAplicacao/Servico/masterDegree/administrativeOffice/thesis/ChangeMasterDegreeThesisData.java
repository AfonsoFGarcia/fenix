package ServidorAplicacao.Servico.masterDegree.administrativeOffice.thesis;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoExternalPerson;
import DataBeans.InfoStudentCurricularPlan;
import DataBeans.InfoTeacher;
import DataBeans.util.Cloner;
import Dominio.IEmployee;
import Dominio.IMasterDegreeThesisDataVersion;
import Dominio.IPessoa;
import Dominio.IStudentCurricularPlan;
import Dominio.MasterDegreeThesisDataVersion;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.GuiderAlreadyChosenServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.State;

/**
 * 
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 *  
 */
public class ChangeMasterDegreeThesisData implements IService {

    public void run(IUserView userView, InfoStudentCurricularPlan infoStudentCurricularPlan,
            String dissertationTitle, List infoTeacherGuiders, List infoTeacherAssistentGuiders,
            List infoExternalPersonExternalGuiders, List infoExternalPersonExternalAssistentGuiders)
            throws FenixServiceException {

        try {

            //	check duplicate guiders and assistent guiders
            for (Iterator iter = infoTeacherGuiders.iterator(); iter.hasNext();) {
                InfoTeacher guider = (InfoTeacher) iter.next();

                for (Iterator iterator = infoTeacherAssistentGuiders.iterator(); iterator.hasNext();) {
                    InfoTeacher assistentGuider = (InfoTeacher) iterator.next();
                    if (assistentGuider.getIdInternal().equals(guider.getIdInternal())) {
                        throw new GuiderAlreadyChosenServiceException(
                                "error.exception.masterDegree.guiderAlreadyChosen");
                    }
                }
            }

            // check duplicate external guiders and external assistent guiders
            for (Iterator iter = infoExternalPersonExternalGuiders.iterator(); iter.hasNext();) {
                InfoExternalPerson externalGuider = (InfoExternalPerson) iter.next();

                for (Iterator iterator = infoExternalPersonExternalAssistentGuiders.iterator(); iterator
                        .hasNext();) {
                    InfoExternalPerson externalAssistentGuider = (InfoExternalPerson) iterator.next();
                    if (externalAssistentGuider.getIdInternal().equals(externalGuider.getIdInternal())) {
                        throw new GuiderAlreadyChosenServiceException(
                                "error.exception.masterDegree.externalGuiderAlreadyChosen");
                    }
                }
            }

            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IStudentCurricularPlan studentCurricularPlan = Cloner
                    .copyInfoStudentCurricularPlan2IStudentCurricularPlan(infoStudentCurricularPlan);

            IMasterDegreeThesisDataVersion storedMasterDegreeThesisDataVersion = sp
                    .getIPersistentMasterDegreeThesisDataVersion().readActiveByStudentCurricularPlan(
                            studentCurricularPlan);
            if (storedMasterDegreeThesisDataVersion == null)
                throw new NonExistingServiceException(
                        "error.exception.masterDegree.nonExistentMasterDegreeThesis");

            storedMasterDegreeThesisDataVersion.setCurrentState(new State(State.INACTIVE));
            sp.getIPersistentMasterDegreeThesisDataVersion().simpleLockWrite(
                    storedMasterDegreeThesisDataVersion);

            IMasterDegreeThesisDataVersion masterDegreeThesisDataVersionWithChosenDissertationTitle = sp
                    .getIPersistentMasterDegreeThesisDataVersion().readActiveByDissertationTitle(
                            dissertationTitle);
            if (masterDegreeThesisDataVersionWithChosenDissertationTitle != null)
                if (!masterDegreeThesisDataVersionWithChosenDissertationTitle.getMasterDegreeThesis()
                        .getStudentCurricularPlan().equals(studentCurricularPlan))
                    throw new ExistingServiceException(
                            "error.exception.masterDegree.dissertationTitleAlreadyChosen");

            IPessoa person = sp.getIPessoaPersistente().lerPessoaPorUsername(userView.getUtilizador());
            IEmployee employee = sp.getIPersistentEmployee().readByPerson(
                    person.getIdInternal().intValue());

            IMasterDegreeThesisDataVersion masterDegreeThesisDataVersion = new MasterDegreeThesisDataVersion(
                    storedMasterDegreeThesisDataVersion.getMasterDegreeThesis(), employee,
                    dissertationTitle, new Timestamp(new Date().getTime()), new State(State.ACTIVE));
            List guiders = Cloner.copyListInfoTeacher2ListITeacher(infoTeacherGuiders);
            List assistentGuiders = Cloner.copyListInfoTeacher2ListITeacher(infoTeacherAssistentGuiders);
            List externalGuiders = Cloner
                    .copyListInfoExternalPerson2ListIExternalPerson(infoExternalPersonExternalGuiders);
            List externalAssistentGuiders = Cloner
                    .copyListInfoExternalPerson2ListIExternalPerson(infoExternalPersonExternalAssistentGuiders);
            masterDegreeThesisDataVersion.setGuiders(guiders);
            masterDegreeThesisDataVersion.setAssistentGuiders(assistentGuiders);
            masterDegreeThesisDataVersion.setExternalGuiders(externalGuiders);
            masterDegreeThesisDataVersion.setExternalAssistentGuiders(externalAssistentGuiders);
            sp.getIPersistentMasterDegreeThesisDataVersion().simpleLockWrite(
                    masterDegreeThesisDataVersion);

        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

    }
}