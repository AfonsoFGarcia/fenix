package ServidorAplicacao.Servico.degreeAdministrativeOffice.enrolment.withoutRules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoCurricularCourse;
import DataBeans.InfoExecutionPeriod;
import DataBeans.util.Cloner;
import Dominio.DisciplinaExecucao;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.strategy.enrolment.context.EnrolmentContext;
import ServidorAplicacao.strategy.enrolment.context.EnrolmentContextManager;
import ServidorAplicacao.strategy.enrolment.context.InfoEnrolmentContext;
import ServidorAplicacao.strategy.enrolment.rules.EnrolmentFilterAllOptionalCoursesRule;
import ServidorAplicacao.strategy.enrolment.rules.IEnrolmentRule;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author dcs-rjao
 *
 * 9/Abr/2003
 */

public class ShowAvailableCurricularCoursesForOptionWithoutRules implements IServico {

	private static ShowAvailableCurricularCoursesForOptionWithoutRules _servico = new ShowAvailableCurricularCoursesForOptionWithoutRules();

	public static ShowAvailableCurricularCoursesForOptionWithoutRules getService() {
		return _servico;
	}

	private ShowAvailableCurricularCoursesForOptionWithoutRules() {
	}

	public final String getNome() {
		return "ShowAvailableCurricularCoursesForOptionWithoutRules";
	}

	public InfoEnrolmentContext run(InfoEnrolmentContext infoEnrolmentContext) throws FenixServiceException {

		// Small trick to be able to use code from class 'EnrolmentFilterAllOptionalCoursesRule' but with no restrivtion in terms of
		// minimal curricular year for curricular courses that can be chosen for optional courses:
		int min_year_of_optional_courses = infoEnrolmentContext.getInfoStudentActiveCurricularPlan().getInfoDegreeCurricularPlan().getMinimalYearForOptionalCourses().intValue();
		infoEnrolmentContext.getInfoStudentActiveCurricularPlan().getInfoDegreeCurricularPlan().setMinimalYearForOptionalCourses(new Integer(1));
		
		IEnrolmentRule enrolmentRule = new EnrolmentFilterAllOptionalCoursesRule();
		EnrolmentContext enrolmentContext = enrolmentRule.apply(EnrolmentContextManager.getEnrolmentContext(infoEnrolmentContext));
		InfoEnrolmentContext infoEnrolmentContext2 = EnrolmentContextManager.getInfoEnrolmentContext(enrolmentContext);
		infoEnrolmentContext2.setOptionalInfoCurricularCoursesToChooseFromDegree(this.filterByExecutionCourses(infoEnrolmentContext2.getOptionalInfoCurricularCoursesToChooseFromDegree(), infoEnrolmentContext2.getInfoExecutionPeriod()));

		infoEnrolmentContext.getInfoStudentActiveCurricularPlan().getInfoDegreeCurricularPlan().setMinimalYearForOptionalCourses(new Integer(min_year_of_optional_courses));

		return infoEnrolmentContext2;
	}

	// FIXME DAVID-RICARDO: Eventualmente esta filtragem dever� ser feita dentro da pr�pria regra.
	private List filterByExecutionCourses(List infoCurricularCoursesList, InfoExecutionPeriod infoExecutionPeriod) throws FenixServiceException {

		try {
			ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
			IDisciplinaExecucaoPersistente persistentExecutionCourse = persistentSupport.getIDisciplinaExecucaoPersistente();
			IExecutionPeriod executionPeriod = Cloner.copyInfoExecutionPeriod2IExecutionPeriod(infoExecutionPeriod);
			
			List infoCurricularCoursesToRemove = new ArrayList();
			
			Iterator iterator = infoCurricularCoursesList.iterator();
			while (iterator.hasNext()) {
				InfoCurricularCourse infoCurricularCourse = (InfoCurricularCourse) iterator.next();
			
				IDisciplinaExecucao executionCourseCriteria = new DisciplinaExecucao();
				executionCourseCriteria.setExecutionPeriod(executionPeriod);
				executionCourseCriteria.setNome(infoCurricularCourse.getName());
				executionCourseCriteria.setSigla(infoCurricularCourse.getCode());
				List associatedExecutionCourses = persistentExecutionCourse.readByCriteria(executionCourseCriteria);
					
				if(associatedExecutionCourses.isEmpty()){
					infoCurricularCoursesToRemove.add(infoCurricularCourse);
				} else {
					associatedExecutionCourses.clear();
				}
			}
			
			infoCurricularCoursesList.removeAll(infoCurricularCoursesToRemove);
			
			return infoCurricularCoursesList;
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
	}
}