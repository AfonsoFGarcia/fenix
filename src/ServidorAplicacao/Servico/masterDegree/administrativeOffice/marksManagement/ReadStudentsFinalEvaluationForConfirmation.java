package ServidorAplicacao.Servico.masterDegree.administrativeOffice.marksManagement;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import DataBeans.InfoEnrolmentEvaluation;
import DataBeans.InfoSiteEnrolmentEvaluation;
import DataBeans.InfoTeacher;
import DataBeans.util.Cloner;
import Dominio.CurricularCourse;
import Dominio.ICurricularCourse;
import Dominio.IEnrolment;
import Dominio.IEnrolmentEvaluation;
import Dominio.IPessoa;
import Dominio.ITeacher;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidSituationServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IPersistentEnrolmentEvaluation;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.EnrolmentEvaluationState;

/**
 * @author Fernanda Quit�rio
 * 10/07/2003
 * 
 */
public class ReadStudentsFinalEvaluationForConfirmation implements IServico {

	private static ReadStudentsFinalEvaluationForConfirmation servico = new ReadStudentsFinalEvaluationForConfirmation();

	/**
	 * The singleton access method of this class.
	 **/
	public static ReadStudentsFinalEvaluationForConfirmation getService() {
		return servico;
	}

	/**
	 * The actor of this class.
	 **/
	private ReadStudentsFinalEvaluationForConfirmation() {
	}

	/**
	 * Returns The Service Name */

	public final String getNome() {
		return "ReadStudentsFinalEvaluationForConfirmation";
	}

	public InfoSiteEnrolmentEvaluation run(Integer curricularCourseCode, String yearString) throws FenixServiceException {

		List infoEnrolmentEvaluations = new ArrayList();
		InfoTeacher infoTeacher = new InfoTeacher();
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentEnrolmentEvaluation persistentEnrolmentEvaluation = sp.getIPersistentEnrolmentEvaluation();
			IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();
			IPersistentEnrolment persistentEnrolment = sp.getIPersistentEnrolment();
			IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();

			ICurricularCourse curricularCourse = new CurricularCourse();
			curricularCourse.setIdInternal(curricularCourseCode);
			curricularCourse = (ICurricularCourse) persistentCurricularCourse.readByOId(curricularCourse, false);

			List enrolments = persistentEnrolment.readByCurricularCourse(curricularCourse, yearString);
			List enrolmentEvaluations = new ArrayList();
			Iterator iterEnrolment = enrolments.listIterator();
			while (iterEnrolment.hasNext()) {
				IEnrolment enrolment = (IEnrolment) iterEnrolment.next();
				List allEnrolmentEvaluations = (List) persistentEnrolmentEvaluation.readEnrolmentEvaluationByEnrolment(enrolment);
				IEnrolmentEvaluation enrolmentEvaluation =
					(IEnrolmentEvaluation) allEnrolmentEvaluations.get(allEnrolmentEvaluations.size() - 1);
				enrolmentEvaluations.add(enrolmentEvaluation);
			}

			if (enrolmentEvaluations != null && enrolmentEvaluations.size() > 0) {
				
				try {
					
					checkForInvalidSituations(enrolmentEvaluations);
					
				} catch (ExistingServiceException e) {
					throw new ExistingServiceException();
				} catch (InvalidSituationServiceException e) {
					throw new InvalidSituationServiceException();
				}

				//				get teacher responsible for final evaluation - he is responsible for all evaluations for this
				//				curricularCourseScope
				IPessoa person = ((IEnrolmentEvaluation) enrolmentEvaluations.get(0)).getPersonResponsibleForGrade();
				ITeacher teacher = persistentTeacher.readTeacherByUsername(person.getUsername());
				infoTeacher = Cloner.copyITeacher2InfoTeacher(teacher);

				//				transform evaluations in databeans
				ListIterator iter = enrolmentEvaluations.listIterator();
				while (iter.hasNext()) {
					IEnrolmentEvaluation elem = (IEnrolmentEvaluation) iter.next();
					InfoEnrolmentEvaluation infoEnrolmentEvaluation = Cloner.copyIEnrolmentEvaluation2InfoEnrolmentEvaluation(elem);

					infoEnrolmentEvaluation.setInfoEnrolment(Cloner.copyIEnrolment2InfoEnrolment(elem.getEnrolment()));
					infoEnrolmentEvaluations.add(infoEnrolmentEvaluation);
				}
			}
			if (infoEnrolmentEvaluations.size() == 0) {
				throw new NonExistingServiceException();
			}

		} catch (ExcepcaoPersistencia ex) {
			FenixServiceException newEx = new FenixServiceException("Persistence layer error");
			newEx.fillInStackTrace();
			throw newEx;
		}

		InfoSiteEnrolmentEvaluation infoSiteEnrolmentEvaluation = new InfoSiteEnrolmentEvaluation();
		infoSiteEnrolmentEvaluation.setEnrolmentEvaluations(infoEnrolmentEvaluations);
		infoSiteEnrolmentEvaluation.setInfoTeacher(infoTeacher);
		Date evaluationDate = ((InfoEnrolmentEvaluation) infoEnrolmentEvaluations.get(0)).getGradeAvailableDate();
		infoSiteEnrolmentEvaluation.setLastEvaluationDate(evaluationDate);

		return infoSiteEnrolmentEvaluation;
	}

	private void checkForInvalidSituations(List enrolmentEvaluations)
		throws ExistingServiceException, InvalidSituationServiceException {
		//			evaluations can only be confirmated if they are not already confirmated
		if (((IEnrolmentEvaluation) enrolmentEvaluations.get(0))
			.getEnrolmentEvaluationState()
			.equals(EnrolmentEvaluationState.FINAL_OBJ)) {
			throw new ExistingServiceException();
		}

		//				and if all students have final evaluation
		List enrolmentEvaluationsWithoutGrade = (List) CollectionUtils.select(enrolmentEvaluations, new Predicate() {
			public boolean evaluate(Object input) {
					//						see if there are evaluations without grade
	IEnrolmentEvaluation enrolmentEvaluationInput = (IEnrolmentEvaluation) input;
				if (enrolmentEvaluationInput.getGrade() == null || enrolmentEvaluationInput.getGrade().length() == 0)
					return true;
				return false;
			}
		});
		if (enrolmentEvaluationsWithoutGrade.size() > 0) {
			throw new InvalidSituationServiceException();
		}
	}
}