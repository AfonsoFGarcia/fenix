package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.student.studentCurricularPlan;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentWithInfoPerson;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author David Santos 2/Out/2003
 */

public class ReadStudentsByNameIDnumberIDtypeAndStudentNumber extends Service {

    public List run(String studentName, String idNumber, IDDocumentType idType, Integer studentNumber)
            throws ExcepcaoPersistencia {

        List masterDegreeStudents = Student
                .readMasterDegreeStudentsByNameDocIDNumberIDTypeAndStudentNumber(studentName, idNumber,
                        idType, studentNumber);

        return (List) CollectionUtils.collect(masterDegreeStudents, new Transformer() {

            public Object transform(Object arg0) {
                Student student = (Student) arg0;
                return InfoStudentWithInfoPerson.newInfoFromDomain(student);
            }

        });

    }
}