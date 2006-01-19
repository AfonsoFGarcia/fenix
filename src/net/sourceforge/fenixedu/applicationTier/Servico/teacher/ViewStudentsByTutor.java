/*
 * Created on 21/Jan/2005
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoTutorWithInfoStudent;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.Tutor;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTutor;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.IService;

/**
 * @author joaosa and rmalo
 *  
 */
public class ViewStudentsByTutor implements IService {

    /*
     * This service returns a list of students that tutor tutorizes
     */
    public List run(String userName) throws FenixServiceException, ExcepcaoPersistencia {
        if (userName == null) {
            throw new FenixServiceException("error.tutor.impossibleOperation");
        }

        List infoTutorStudents = new ArrayList();
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
        Teacher teacher = persistentTeacher.readTeacherByUsername(userName);

        IPersistentTutor persistentTutor = sp.getIPersistentTutor();
        List<Tutor> tutorStudents = persistentTutor.readStudentsByTeacher(teacher.getIdInternal(),
                teacher.getTeacherNumber());

        if (tutorStudents == null) {
            return infoTutorStudents;
        }

        for (Tutor tutor : tutorStudents) {
            infoTutorStudents.add(InfoTutorWithInfoStudent.newInfoFromDomain(tutor));
        }

        return infoTutorStudents;
    }

}
