package ServidorAplicacao.strategy.enrolment.degree.rules;

import junit.framework.Test;
import junit.framework.TestSuite;
import Dominio.ICurricularCourseScope;
import ServidorAplicacao.strategy.enrolment.degree.EnrolmentContext;
import Tools.dbaccess;

/**
 * @author dcs-rjao
 *
 * 15/Abr/2003
 */
public class EnrolmentValidateCurricularYearPrecedenceRuleTest extends BaseEnrolmentRuleTestCase {

	private dbaccess dbAcessPoint;

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(EnrolmentValidateCurricularYearPrecedenceRuleTest.class);
		return suite;
	}

	protected String getDataSetFilePath() {
		return "etc/testEnrolmentDataSet.xml";
	}

	public void testApplyEnrolmentValidateCurricularYearPrecedenceRule() {

		autentication();
		Object serviceArgs[] = { userView, new Integer(1)};
		EnrolmentContext enrolmentContext = executeService("ShowAvailableCurricularCourses", serviceArgs);

		// tudo ok
		enrolmentContext.getActualEnrolment().clear();
		for (int i = 0; i < 6; i++) {
			ICurricularCourseScope curricularCourseScope =
				(ICurricularCourseScope) enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled().get(i);
			enrolmentContext.getActualEnrolment().add(curricularCourseScope);
		}

		doApplyRule(new EnrolmentValidateCurricularYearPrecedenceRule(), enrolmentContext);
		assertEquals(enrolmentContext.getEnrolmentValidationResult().isSucess(), true);
		enrolmentContext.getEnrolmentValidationResult().setSucess(true);

		// falha
		enrolmentContext.getActualEnrolment().clear();
		ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled().get(5);
		enrolmentContext.getActualEnrolment().add(curricularCourseScope);

		doApplyRule(new EnrolmentValidateCurricularYearPrecedenceRule(), enrolmentContext);
		assertEquals(enrolmentContext.getEnrolmentValidationResult().isSucess(), false);
		enrolmentContext.getEnrolmentValidationResult().setSucess(true);

	}

	public void doApplyRule(IEnrolmentRule enrolmentRule, EnrolmentContext enrolmentContext) {
		enrolmentRule.apply(enrolmentContext);
	}
}