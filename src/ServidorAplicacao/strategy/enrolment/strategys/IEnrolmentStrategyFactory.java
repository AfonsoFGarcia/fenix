package ServidorAplicacao.strategy.enrolment.strategys;

import ServidorAplicacao.strategy.enrolment.context.*;

/**
 * @author dcs-rjao
 *
 * 15/Abr/2003
 */
public interface IEnrolmentStrategyFactory {
	public IEnrolmentStrategy getEnrolmentStrategyInstance(EnrolmentContext enrolmentContext);
}