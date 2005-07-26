package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.thesis;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.GuiderAlreadyChosenServiceException;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.IEmployee;
import net.sourceforge.fenixedu.domain.IExternalPerson;
import net.sourceforge.fenixedu.domain.IMasterDegreeThesis;
import net.sourceforge.fenixedu.domain.IMasterDegreeThesisDataVersion;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.State;

import org.apache.commons.collections.CollectionUtils;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 * 
 */
public class CreateMasterDegreeThesis implements IService {

    public void run(IUserView userView, Integer studentCurricularPlanID, String dissertationTitle,
            List<Integer> guidersNumbers, List<Integer> assistentGuidersNumbers,
            List<Integer> externalGuidersIDs, List<Integer> externalAssistentGuidersIDs)
            throws FenixServiceException, ExcepcaoPersistencia {

        // check duplicate guiders and assistent guiders
        if (CollectionUtils.intersection(guidersNumbers, assistentGuidersNumbers).size() > 0) {
            throw new GuiderAlreadyChosenServiceException(
                    "error.exception.masterDegree.guiderAlreadyChosen");
        }

        // check duplicate external guiders and external assistent guiders
        if (CollectionUtils.intersection(externalGuidersIDs, externalAssistentGuidersIDs).size() > 0) {
            throw new GuiderAlreadyChosenServiceException(
                    "error.exception.masterDegree.externalGuiderAlreadyChosen");
        }

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IMasterDegreeThesis storedMasterDegreeThesis = sp.getIPersistentMasterDegreeThesis()
                .readByStudentCurricularPlan(studentCurricularPlanID);
        if (storedMasterDegreeThesis != null) {
            throw new ExistingServiceException("error.exception.masterDegree.existingMasterDegreeThesis");
        }

        IMasterDegreeThesisDataVersion storedMasterDegreeThesisDataVersion = sp
                .getIPersistentMasterDegreeThesisDataVersion().readActiveByDissertationTitle(
                        dissertationTitle);
        if ((storedMasterDegreeThesisDataVersion != null)
                && (!storedMasterDegreeThesisDataVersion.getMasterDegreeThesis()
                        .getStudentCurricularPlan().getIdInternal().equals(studentCurricularPlanID))) {
            throw new ExistingServiceException(
                    "error.exception.masterDegree.dissertationTitleAlreadyChosen");
        }

        IPerson person = sp.getIPessoaPersistente().lerPessoaPorUsername(userView.getUtilizador());
        IEmployee employee = sp.getIPersistentEmployee().readByPerson(person.getIdInternal().intValue());
        IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) sp
                .getIStudentCurricularPlanPersistente().readByOID(StudentCurricularPlan.class,
                        studentCurricularPlanID);

        IMasterDegreeThesis masterDegreeThesis = DomainFactory.makeMasterDegreeThesis();
        masterDegreeThesis.setStudentCurricularPlan(studentCurricularPlan);

        // write data version
        IMasterDegreeThesisDataVersion masterDegreeThesisDataVersion = DomainFactory
                .makeMasterDegreeThesisDataVersion(masterDegreeThesis, employee, dissertationTitle,
                        new Date(), new State(State.ACTIVE));

        Collection<ITeacher> guiders = sp.getIPersistentTeacher().readByNumbers(guidersNumbers);
        Collection<ITeacher> assistentGuiders = sp.getIPersistentTeacher().readByNumbers(
                assistentGuidersNumbers);
        Collection<IExternalPerson> externalGuiders = sp.getIPersistentExternalPerson().readByIDs(
                externalGuidersIDs);
        Collection<IExternalPerson> externalAssistentGuiders = sp.getIPersistentExternalPerson()
                .readByIDs(externalAssistentGuidersIDs);

        masterDegreeThesisDataVersion.getGuiders().addAll(guiders);
        masterDegreeThesisDataVersion.getAssistentGuiders().addAll(assistentGuiders);
        masterDegreeThesisDataVersion.getExternalGuiders().addAll(externalGuiders);
        masterDegreeThesisDataVersion.getExternalAssistentGuiders().addAll(externalAssistentGuiders);

        masterDegreeThesis.getMasterDegreeThesisDataVersions().add(masterDegreeThesisDataVersion);

    }

}
