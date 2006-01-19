package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentWithInfoPerson;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.IService;

/**
 * 
 * @author Fernanda Quit�rio 1/Mar/2004
 * 
 */
public class ReadStudentByNumberAndAllDegreeTypes implements IService {

    public Object run(Integer number) throws ExcepcaoPersistencia {
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        InfoStudent infoStudent = null;

        Student student = sp.getIPersistentStudent().readStudentByNumberAndDegreeType(number,
                DegreeType.DEGREE);

        if (student == null) {
            student = sp.getIPersistentStudent().readStudentByNumberAndDegreeType(number,
                    DegreeType.MASTER_DEGREE);
        }
        if (student != null) {
            infoStudent = InfoStudentWithInfoPerson.newInfoFromDomain(student);
        }

        return infoStudent;
    }

}