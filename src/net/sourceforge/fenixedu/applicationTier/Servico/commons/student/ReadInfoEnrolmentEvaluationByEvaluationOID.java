/**
 * 
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 *  
 */

package net.sourceforge.fenixedu.applicationTier.Servico.commons.student;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadInfoEnrolmentEvaluationByEvaluationOID extends FenixService {

    public InfoEnrolmentEvaluation run(IUserView userView, Integer studentNumber, DegreeType degreeType, Integer enrolmentOID)
	    throws ExcepcaoInexistente, FenixServiceException {
	return (new GetEnrolmentGrade()).run((Enrolment) rootDomainObject.readCurriculumModuleByOID(enrolmentOID));
    }

}