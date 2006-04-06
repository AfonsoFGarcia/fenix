package net.sourceforge.fenixedu.applicationTier.Servico.sop.exams;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadExamByOID extends Service {

    public InfoExam run(Integer examID) throws FenixServiceException, ExcepcaoPersistencia {
        final Exam exam = (Exam) rootDomainObject.readEvaluationByOID(examID);
        if (exam == null) {
            throw new FenixServiceException("error.noExam");
        }
        return InfoExam.newInfoFromDomain(exam);
    }
}