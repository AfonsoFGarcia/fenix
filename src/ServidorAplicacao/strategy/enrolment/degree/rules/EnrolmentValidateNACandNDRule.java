package ServidorAplicacao.strategy.enrolment.degree.rules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import Dominio.ICurricularCourseScope;
import ServidorAplicacao.strategy.enrolment.degree.EnrolmentContext;
import ServidorPersistente.ExcepcaoPersistencia;

/**
 * @author dcs-rjao
 *
 * 3/Abr/2003
 */

public class EnrolmentValidateNACandNDRule implements IEnrolmentRule {

	public EnrolmentContext apply(EnrolmentContext enrolmentContext) throws ExcepcaoPersistencia {
		
		List validateMessages = new ArrayList();

		HashMap acumulatedEnrolments = (HashMap) enrolmentContext.getAcumulatedEnrolments();
		int NAC = 0;

		Iterator iterator = enrolmentContext.getActualEnrolment().iterator();
		while (iterator.hasNext()) {
			ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iterator.next();
			if (acumulatedEnrolments.containsKey(curricularCourseScope.getCurricularCourse().getCode() + curricularCourseScope.getCurricularCourse().getName())) {
				if (((Integer) acumulatedEnrolments.get(curricularCourseScope.getCurricularCourse().getCode() + curricularCourseScope.getCurricularCourse().getName())).intValue() > 0) {
					NAC = NAC + 2;
				}
			} else {
				NAC = NAC + 1;
			}
		}

		if (enrolmentContext.getActualEnrolment().size() > 7) {
			validateMessages.add("N�o se pode inscrever a mais de 7 disciplinas");
		}
		if (NAC > 10) {
			validateMessages.add("N�o se pode inscrever a mais de 10 disciplinas acumuladas");
		}
		if (validateMessages.isEmpty()) {
			validateMessages.add("Inscri��o realizada com sucesso");
		}

		enrolmentContext.setValidateMessage(validateMessages);
		return enrolmentContext;
	}
}