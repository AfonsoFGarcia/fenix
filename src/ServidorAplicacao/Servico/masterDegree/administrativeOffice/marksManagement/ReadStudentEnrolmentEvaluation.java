package ServidorAplicacao.Servico.masterDegree.administrativeOffice.marksManagement;

import java.util.ArrayList;
import java.util.List;

import DataBeans.InfoCurricularCourseScope;
import DataBeans.InfoEnrolment;
import DataBeans.InfoEnrolmentEvaluation;
import DataBeans.InfoPerson;
import DataBeans.InfoSiteEnrolmentEvaluation;
import DataBeans.InfoTeacher;
import DataBeans.util.Cloner;
import Dominio.CurricularCourseScope;
import Dominio.EnrolmentEvaluation;
import Dominio.ICurricularCourseScope;
import Dominio.IEnrolmentEvaluation;
import Dominio.IPessoa;
import Dominio.ITeacher;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentCurricularCourseScope;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IPersistentEnrolmentEvaluation;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Angela
 * 04/07/2003
 * 
 */
public class ReadStudentEnrolmentEvaluation implements IServico {

	private static ReadStudentEnrolmentEvaluation servico = new ReadStudentEnrolmentEvaluation();

	/**
	 * The singleton access method of this class.
	 **/
	public static ReadStudentEnrolmentEvaluation getService() {
		return servico;
	}

	/**
	 * The actor of this class.
	 **/
	private ReadStudentEnrolmentEvaluation() {
	}

	/**
	 * Returns The Service Name */

	public final String getNome() {
		return "ReadStudentEnrolmentEvaluation";
	}

	public InfoSiteEnrolmentEvaluation run(Integer studentEvaluationCode) throws FenixServiceException {

		IEnrolmentEvaluation enrolmentEvaluation = new EnrolmentEvaluation();
		InfoEnrolmentEvaluation infoEnrolmentEvaluation = new InfoEnrolmentEvaluation();
		InfoEnrolment infoEnrolment = new InfoEnrolment();
		InfoTeacher infoTeacher = new InfoTeacher();
		List infoEnrolmentEvaluations = new ArrayList();
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();
			IPersistentEnrolmentEvaluation persistentEnrolmentEvaluation = sp.getIPersistentEnrolmentEvaluation();
			IPersistentEnrolment persistentEnrolment = sp.getIPersistentEnrolment();
			IPersistentCurricularCourseScope persistentCurricularCourseScope = sp.getIPersistentCurricularCourseScope();
			IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();

			
			
			enrolmentEvaluation.setIdInternal(studentEvaluationCode);
			enrolmentEvaluation = (IEnrolmentEvaluation) persistentEnrolmentEvaluation.readByOId(enrolmentEvaluation,false);
//			get curricularCourseScope for enrolmentEvaluation
			ICurricularCourseScope curricularCourseScope = new CurricularCourseScope();
			curricularCourseScope.setIdInternal(enrolmentEvaluation.getEnrolment().getCurricularCourseScope().getIdInternal());
			curricularCourseScope =	(ICurricularCourseScope) persistentCurricularCourseScope.readByOId(curricularCourseScope, false);

			//			this becomes necessary to use criteria
			InfoCurricularCourseScope infoCurricularCourseScope =
				Cloner.copyICurricularCourseScope2InfoCurricularCourseScope(curricularCourseScope);
//			ICurricularCourseScope curricularCourseScopeForCriteria =
//				Cloner.copyInfoCurricularCourseScope2ICurricularCourseScope(infoCurricularCourseScope);
			infoEnrolment = Cloner.copyIEnrolment2InfoEnrolment(enrolmentEvaluation.getEnrolment());
	
			IPessoa person = (IPessoa) enrolmentEvaluation.getPersonResponsibleForGrade();
			InfoPerson person2 = Cloner.copyIPerson2InfoPerson(person);
			ITeacher teacher = persistentTeacher.readTeacherByUsername(person.getUsername());
			infoTeacher = Cloner.copyITeacher2InfoTeacher(teacher);
			
			infoEnrolmentEvaluation = Cloner.copyIEnrolmentEvaluation2InfoEnrolmentEvaluation(enrolmentEvaluation);
			infoEnrolmentEvaluation.setInfoPersonResponsibleForGrade(infoTeacher.getInfoPerson());
			infoEnrolmentEvaluation.setInfoEnrolment(infoEnrolment);
			infoEnrolmentEvaluations.add(infoEnrolmentEvaluation);

//			enrolmenEvaluation.setEnrolment
		}catch (ExcepcaoPersistencia ex) {
			FenixServiceException newEx = new FenixServiceException("Persistence layer error");
			newEx.fillInStackTrace();
			throw newEx;
		}
			InfoSiteEnrolmentEvaluation infoSiteEnrolmentEvaluation = new InfoSiteEnrolmentEvaluation();
			infoSiteEnrolmentEvaluation.setEnrolmentEvaluations(infoEnrolmentEvaluations);
			infoSiteEnrolmentEvaluation.setInfoTeacher(infoTeacher);
		
		return infoSiteEnrolmentEvaluation;
		
	}

}
