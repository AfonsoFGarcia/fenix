/*
 * Created on 2003/07/30
 *
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ILesson;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IAulaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 *  
 */
public class ReadLessonByOID implements IService {

    public InfoLesson run(Integer oid) throws FenixServiceException {

        InfoLesson result = null;
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IAulaPersistente lessonDAO = sp.getIAulaPersistente();
            ILesson lesson = (ILesson) lessonDAO.readByOID(Lesson.class, oid);
            if (lesson != null) {
                InfoLesson infoLesson = Cloner.copyILesson2InfoLesson(lesson);
                //				IShift shift = lesson.getShift();
                //				InfoShift infoShift = Cloner.copyShift2InfoShift(shift);
                //				infoLesson.setInfoShift(infoShift);

                result = infoLesson;
            }
        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException(ex);
        }

        return result;
    }
}