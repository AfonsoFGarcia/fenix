/*
 * Created on 18/Fev/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.enrollment;

import java.util.List;
import java.util.ListIterator;

import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.IEnrollment;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.utils.enrolment.DeleteEnrolmentUtils;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrollment;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrolmentEvaluation;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.util.TipoCurso;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author T�nia Pous�o
 *  
 */
public class DeleteEnrollmentsList implements IService {
    public DeleteEnrollmentsList() {
    }

    // some of these arguments may be null. they are only needed for filter
    public void run(InfoStudent infoStudent, TipoCurso degreeType, List enrolmentIDList)
            throws FenixServiceException {
        try {
            
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
            IPersistentEnrollment enrolmentDAO = persistentSuport.getIPersistentEnrolment();
            IPersistentEnrolmentEvaluation enrolmentEvaluationDAO = persistentSuport
                    .getIPersistentEnrolmentEvaluation();
            
            if (enrolmentIDList != null && enrolmentIDList.size() > 0) {
                ListIterator iterator = enrolmentIDList.listIterator();
                while (iterator.hasNext()) {
                    Integer enrolmentID = (Integer) iterator.next();
                    
                    final IEnrollment enrollment = (IEnrollment) enrolmentDAO.readByOID(Enrolment.class,
                            enrolmentID);
                    
                    DeleteEnrolmentUtils.deleteEnrollment(enrolmentDAO, enrolmentEvaluationDAO, enrollment);

                    /*DeleteEnrolment deleteEnrolmentService = new DeleteEnrolment();
                    deleteEnrolmentService.run(null, null, enrolmentID);*/                   
                    
                }
            }
        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            throw new FenixServiceException(e);
        }
    }
}