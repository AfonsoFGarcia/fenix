/*
 * Created on 2004/02/17
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.Calendar;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IAulaPersistente;

/**
 * @author Luis Cruz
 *  
 */
public class CalculateFirstDayOfLesson extends Service {

	public Calendar run(Integer lessonId) throws ExcepcaoPersistencia {
	    IAulaPersistente persistentLesson = persistentSupport.getIAulaPersistente();

	    Lesson lesson = (Lesson) persistentLesson.readByOID(Lesson.class, lessonId);
	    Calendar startDate = lesson.getRoomOccupation().getPeriod().getStartDate();
	    return startDate;
    }

}