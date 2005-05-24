/*
 * Created on 16/Dez/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.sop.exams;

/**
 * @author Ana e Ricardo
 * 
 */
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.notAuthorizedServiceDeleteException;
import net.sourceforge.fenixedu.applicationTier.Servico.sop.DeleteExam;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.IExam;
import net.sourceforge.fenixedu.domain.IPeriod;
import net.sourceforge.fenixedu.domain.IRoomOccupation;
import net.sourceforge.fenixedu.domain.Period;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExamStudentRoom;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;

import pt.utl.ist.berserk.logic.serviceManager.IService;

public class DeleteExamNew implements IService {

    public Object run(Integer examOID) throws FenixServiceException {

        boolean result = false;

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            IExam examToDelete = (IExam) sp.getIPersistentExam().readByOID(Exam.class, examOID);
            if (examToDelete == null) {
                throw new FenixServiceException("The exam does not exist");
            }

            IPersistentExamStudentRoom persistentExamStudentRoom = sp.getIPersistentExamStudentRoom();
            List examStudentRoomList = persistentExamStudentRoom.readByExamOID(examToDelete
                    .getIdInternal());
            if (examStudentRoomList != null && examStudentRoomList.size() > 0) {
                throw new notAuthorizedServiceDeleteException(
                        "error.notAuthorizedExamDelete.withStudent");
            }

            List roomOccupations = examToDelete.getAssociatedRoomOccupation();
            if (roomOccupations != null && !roomOccupations.isEmpty()) {
                Iterator iter = roomOccupations.iterator();
                IPeriod period = null;
                if (!examToDelete.getAssociatedRoomOccupation().isEmpty()) {
                    period = ((IRoomOccupation) roomOccupations.get(0)).getPeriod();
                }

                boolean isEmpty = false;
                List periodRoomOccupations = period.getRoomOccupations();
                Collection otherRoomOccupations = CollectionUtils.disjunction(roomOccupations,
                        periodRoomOccupations);
                isEmpty = otherRoomOccupations.isEmpty();
                while (iter.hasNext()) {
                    IRoomOccupation roomOccupation = (IRoomOccupation) iter.next();
                    sp.getIPersistentRoomOccupation().delete(roomOccupation);
                }
                if (period != null) {
                    if (isEmpty && period.getExecutionDegreesForExamsFirstSemester().isEmpty()
                            && period.getExecutionDegreesForExamsSecondSemester().isEmpty()
                            && period.getExecutionDegreesForLessonsFirstSemester().isEmpty()
                            && period.getExecutionDegreesForLessonsSecondSemester().isEmpty()) {
                        //sp.getIPersistentPeriod().delete(period);
                        sp.getIPersistentPeriod().deleteByOID(Period.class, period.getIdInternal());

                    }
                }
            }

            new DeleteExam().run(examOID);

            result = true;
        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException("Error deleting exam");
        }

        return new Boolean(result);

    }

}