package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import DataBeans.ISiteComponent;
import DataBeans.InfoExam;
import DataBeans.InfoFrequenta;
import DataBeans.InfoMark;
import DataBeans.InfoSiteCommon;
import DataBeans.InfoSiteMarks;
import DataBeans.TeacherAdministrationSiteView;
import DataBeans.util.Cloner;
import Dominio.DisciplinaExecucao;
import Dominio.Exam;
import Dominio.IDegreeCurricularPlan;
import Dominio.IDisciplinaExecucao;
import Dominio.IExam;
import Dominio.IFrequenta;
import Dominio.IMark;
import Dominio.ISite;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Dominio.Mark;
import Dominio.Student;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Factory.TeacherAdministrationSiteComponentBuilder;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.strategy.degreeCurricularPlan.DegreeCurricularPlanStrategyFactory;
import ServidorAplicacao.strategy.degreeCurricularPlan.IDegreeCurricularPlanStrategyFactory;
import ServidorAplicacao.strategy.degreeCurricularPlan.strategys.IDegreeCurricularPlanStrategy;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentExam;
import ServidorPersistente.IPersistentMark;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Fernanda Quit�rio
 *
 */
public class InsertExamMarks implements IServico {
	private static InsertExamMarks _servico = new InsertExamMarks();

	/**
		* The actor of this class.
		**/
	private InsertExamMarks() {

	}

	/**
	 * Returns Service Name
	 */
	public String getNome() {
		return "InsertExamMarks";
	}

	/**
	 * Returns the _servico.
	 * @return ReadExecutionCourse
	 */
	public static InsertExamMarks getService() {
		return _servico;
	}

	public Object run(Integer executionCourseCode, Integer examCode, List examMarks) throws ExcepcaoInexistente, FenixServiceException {

		ISite site = null;
		IDisciplinaExecucao executionCourse = null;
		IExam exam = null;
		List infoMarksList = null;
		List marksErrorsInvalidMark = null;
		List marksErrorsStudentExistence = null;
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IDisciplinaExecucaoPersistente executionCourseDAO = sp.getIDisciplinaExecucaoPersistente();
			IPersistentSite persistentSite = sp.getIPersistentSite();
			IPersistentExam persistentExam = sp.getIPersistentExam();
			IFrequentaPersistente persistentAttend = sp.getIFrequentaPersistente();
			IPersistentMark persistentMark = sp.getIPersistentMark();

			//Execution Course
			executionCourse = new DisciplinaExecucao(executionCourseCode);
			executionCourse = (IDisciplinaExecucao) executionCourseDAO.readByOId(executionCourse, false);

			//Site
			site = persistentSite.readByExecutionCourse(executionCourse);

			//Exam
			exam = new Exam(examCode);
			exam = (IExam) persistentExam.readByOId(exam, false);

			//Attend List
			List attendList = persistentAttend.readByExecutionCourse(executionCourse);

			infoMarksList = new ArrayList();
			marksErrorsInvalidMark = new ArrayList();
			marksErrorsStudentExistence = new ArrayList();
			boolean foundStudent = false;

			ListIterator iterMarks = examMarks.listIterator();
			while (iterMarks.hasNext()) {
				InfoMark infoMark = (InfoMark) iterMarks.next();

				completeMark(infoMark, exam, executionCourse);
				if (infoMark.getMark().length() > 0) {

					if (!isValidMark(infoMark)) {
						marksErrorsInvalidMark.add(infoMark);
					} else {
						IMark mark = null;
						ListIterator iterAttend = attendList.listIterator();
						while (iterAttend.hasNext()) {
							IFrequenta attend = (IFrequenta) iterAttend.next();
							if (attend.getAluno().getNumber().equals(infoMark.getInfoFrequenta().getAluno().getNumber())) {
								foundStudent = true;

								mark = persistentMark.readBy(exam, attend);
								if (mark == null) {
									mark = new Mark();

									persistentMark.simpleLockWrite(mark);

									mark.setAttend(attend);
									mark.setExam(exam);
									mark.setMark(infoMark.getMark());
									mark.setPublishedMark(infoMark.getPublishedMark());
								} else {
									persistentMark.simpleLockWrite(mark);

									mark.setMark(infoMark.getMark());
								}

								infoMark = Cloner.copyIMark2InfoMark(mark);								
							}
						}

						if (!foundStudent) {
							marksErrorsStudentExistence.add(infoMark);
						} else {
							foundStudent = false;
						}
					}
				}
				
				infoMarksList.add(infoMark);
			}
		} catch (ExcepcaoPersistencia ex) {
			ex.printStackTrace();
			FenixServiceException newEx = new FenixServiceException("");
			newEx.fillInStackTrace();
			throw newEx;
		}

		return createSiteView(site, exam, infoMarksList, marksErrorsInvalidMark, marksErrorsStudentExistence);
	}

	private void completeMark(InfoMark infoMark, IExam exam, IDisciplinaExecucao disciplinaExecucao) {
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			//Exam
			InfoExam infoExam = Cloner.copyIExam2InfoExam(exam);

			//Student
			IPersistentStudent persistentStudent = sp.getIPersistentStudent();
			IStudent student = new Student();
			student.setIdInternal(infoMark.getInfoFrequenta().getAluno().getIdInternal());
			student = (IStudent) persistentStudent.readByOId(student, false);

			//Attend			
			IFrequentaPersistente frequentaPersistente = sp.getIFrequentaPersistente();
			IFrequenta frequenta = frequentaPersistente.readByAlunoAndDisciplinaExecucao(student, disciplinaExecucao);
			InfoFrequenta infoFrequenta = Cloner.copyIFrequenta2InfoFrequenta(frequenta);

			infoMark.setInfoFrequenta(infoFrequenta);
			infoMark.setInfoExam(infoExam);

		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
		}
	}

	private Object createSiteView(
		ISite site,
		IExam exam,
		List infoMarksList,
		List marksErrorsInvalidMark,
		List marksErrorsStudentExistence)
		throws FenixServiceException {
		InfoSiteMarks infoSiteMarks = new InfoSiteMarks();
		infoSiteMarks.setInfoExam(Cloner.copyIExam2InfoExam(exam));
		infoSiteMarks.setMarksList(infoMarksList);
		infoSiteMarks.setMarksListErrors(marksErrorsInvalidMark);
		infoSiteMarks.setMarksListErrors2(marksErrorsStudentExistence);

		TeacherAdministrationSiteComponentBuilder componentBuilder = new TeacherAdministrationSiteComponentBuilder();
		ISiteComponent commonComponent = componentBuilder.getComponent(new InfoSiteCommon(), site, null, null, null);

		TeacherAdministrationSiteView siteView = new TeacherAdministrationSiteView(commonComponent, infoSiteMarks);
		return siteView;
	}

	private boolean isValidMark(InfoMark infoMark) {
		IStudentCurricularPlan studentCurricularPlan = null;

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IStudentCurricularPlanPersistente curricularPlanPersistente = sp.getIStudentCurricularPlanPersistente();

			studentCurricularPlan =
				curricularPlanPersistente.readActiveStudentCurricularPlan(
					infoMark.getInfoFrequenta().getAluno().getNumber(),
					infoMark.getInfoFrequenta().getAluno().getDegreeType());
		} catch (Exception e) {
			e.printStackTrace();
		}

		IDegreeCurricularPlan degreeCurricularPlan = studentCurricularPlan.getDegreeCurricularPlan();

		// test marks by execution course: strategy 
		IDegreeCurricularPlanStrategyFactory degreeCurricularPlanStrategyFactory = DegreeCurricularPlanStrategyFactory.getInstance();
		IDegreeCurricularPlanStrategy degreeCurricularPlanStrategy =
			degreeCurricularPlanStrategyFactory.getDegreeCurricularPlanStrategy(degreeCurricularPlan);

		return degreeCurricularPlanStrategy.checkMark(infoMark.getMark());
	}
}
