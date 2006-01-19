package net.sourceforge.fenixedu.applicationTier.Servico.gesdis.teacher;

import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

public class ReadTeacherByUsername extends Service {

    public InfoTeacher run(String username) throws ExcepcaoPersistencia {
        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
        final Teacher teacher = persistentTeacher.readTeacherByUsername(username);

        if (teacher != null) {
            return InfoTeacher.newInfoFromDomain(teacher);
        }
        return null;
    }
}