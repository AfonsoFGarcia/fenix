/*
 * Created on 22/Mai/2003
 *
 * 
 */
package middleware.sigla;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerException;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;

import Dominio.BibliographicReference;
import Dominio.CurricularCourse;
import Dominio.Curriculum;
import Dominio.ExecutionPeriod;
import Dominio.IBibliographicReference;
import Dominio.ICurricularCourse;
import Dominio.ICurriculum;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;
import Dominio.IProfessorship;
import Dominio.IResponsibleFor;
import Dominio.ISite;
import Dominio.ITeacher;
import Dominio.Professorship;
import Dominio.ResponsibleFor;
import Dominio.Site;
import Dominio.Teacher;
import Util.PeriodState;

/**
 * @author Jo�o Mota
 *
 */
public class SiglaDataLoader {

	private List siglaCurricularCoursesWithProblems;
	private List fenixExecutionCoursesWithProblems;
	/**
	 * 
	 */
	public SiglaDataLoader() {
		setSiglaCurricularCoursesWithProblems(new ArrayList());
		setFenixExecutionCoursesWithProblems(new ArrayList());
	}

	/**
	 * @return
	 */
	public List getSiglaCurricularCoursesWithProblems() {
		return siglaCurricularCoursesWithProblems;
	}

	/**
	 * @param siglaCurricularCoursesWithProblems
	 */
	public void setSiglaCurricularCoursesWithProblems(List siglaCurricularCoursesWithProblems) {
		this.siglaCurricularCoursesWithProblems = siglaCurricularCoursesWithProblems;
	}

	public static void main(String[] args) {
		SiglaDataLoader loader = new SiglaDataLoader();
		PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();
		updateFenix(loader, broker);
		loader.printSiglaProblems(loader);
		loader.printFenixProblems(loader);
		broker.close();
	}

	/**
	 * 
	 */
	private void printFenixProblems(SiglaDataLoader loader) {
		System.out.println("######################################################");
		System.out.println("##############Problemas com o Fenix###################");
		System.out.println("######################################################");
		List problems = loader.getFenixExecutionCoursesWithProblems();
		Iterator iter = problems.iterator();
		while(iter.hasNext()){
			IDisciplinaExecucao executionCourse =  (IDisciplinaExecucao) iter.next();
			System.out.println(executionCourse);
		}
		System.out.println("######################################################");
		
	}

	/**
	 * 
	 */
	private void printSiglaProblems(SiglaDataLoader loader) {
		System.out.println("######################################################");
		System.out.println("##############Problemas com o Sigla###################");
		System.out.println("######################################################");
		List problems = loader.getSiglaCurricularCoursesWithProblems();
		Iterator iter = problems.iterator();
		while(iter.hasNext()){
			Plano_curricular_2003final siglaCurricularCourse =  (Plano_curricular_2003final) iter.next();
			System.out.println(siglaCurricularCourse);
		}
		System.out.println("######################################################");
	}

	/**
	 * @param broker
	 */
	private List loadSiglaCurricularCourses(PersistenceBroker broker) {
		Criteria crit = new Criteria();
		Query query = new QueryByCriteria(Plano_curricular_2003final.class, crit);
		List siglaCurricularCourses = (List) broker.getCollectionByQuery(query);
		System.out.println("finished loading sigla Curricular Courses! size->" + siglaCurricularCourses.size());
		return siglaCurricularCourses;
	}

	private Integer sigla2FenixDegreeNumbers(float siglaDegreeNumber) {
		if (siglaDegreeNumber == 24) {
			siglaDegreeNumber = 51;
		}
		Float result = new Float(siglaDegreeNumber);
		return new Integer(result.intValue());
	}

	private static void updateFenix(SiglaDataLoader loader, PersistenceBroker broker) {

		System.out.println("A carregar disciplinas curriculares do sigla");
		List siglaCurricularCourses = loader.loadSiglaCurricularCourses(broker);
		Iterator iterSigla = siglaCurricularCourses.iterator();
		while (iterSigla.hasNext()) {
			Plano_curricular_2003final siglaCurricularCourse = (Plano_curricular_2003final) iterSigla.next();
			List fenixCurricularCourses = getFenixCurricularCourses(loader, broker, siglaCurricularCourse);
			if (fenixCurricularCourses == null || fenixCurricularCourses.size() == 0) {

				if (!loader.getSiglaCurricularCoursesWithProblems().contains(siglaCurricularCourse)) {

					loader.getSiglaCurricularCoursesWithProblems().add(siglaCurricularCourse);
				}
			} else {
				updateFenixCurricularCourses(fenixCurricularCourses, siglaCurricularCourse, broker);
				updateFenixExecutionCourses(fenixCurricularCourses, siglaCurricularCourse, broker, loader);
			}
		}
	}

	private static List getFenixCurricularCourses(
		SiglaDataLoader loader,
		PersistenceBroker broker,
		Plano_curricular_2003final siglaCurricularCourse) {
		Criteria fenixCurricularCourseCriteria = new Criteria();
		fenixCurricularCourseCriteria.addEqualTo("code", siglaCurricularCourse.getCodigo_disc());
		fenixCurricularCourseCriteria.addEqualTo(
			"degreeCurricularPlan.keyDegree",
			loader.sigla2FenixDegreeNumbers(siglaCurricularCourse.getCodigo_lic()));
		fenixCurricularCourseCriteria.addLike("degreeCurricularPlan.name", "%2003/2004%");
		fenixCurricularCourseCriteria.addEqualTo("name", siglaCurricularCourse.getNome_disc());
		Query fenixCurricularCourseQuery =
			new QueryByCriteria(CurricularCourse.class, fenixCurricularCourseCriteria);
		List fenixCurricularCourses = (List) broker.getCollectionByQuery(fenixCurricularCourseQuery);
		return fenixCurricularCourses;
	}

	/**
	 * @param fenixCurricularCourses
	 * @param siglaCurricularCourse
	 */
	private static void updateFenixExecutionCourses(
		List fenixCurricularCourses,
		Plano_curricular_2003final siglaCurricularCourse,
		PersistenceBroker broker,
		SiglaDataLoader loader) {
		Criteria crit = new Criteria();
		crit.addEqualTo("state", PeriodState.CURRENT);
		Query query = new QueryByCriteria(ExecutionPeriod.class, crit);
		IExecutionPeriod currentExecutionPeriod = (IExecutionPeriod) broker.getObjectByQuery(query);
		Iterator iter = fenixCurricularCourses.iterator();
		List executionCourses = new ArrayList();
		while (iter.hasNext()) {
			ICurricularCourse curricularCourse = (ICurricularCourse) iter.next();
			executionCourses =
				(List) CollectionUtils.union(executionCourses, curricularCourse.getAssociatedExecutionCourses());
		}

		iter = executionCourses.iterator();

		while (iter.hasNext()) {
			IDisciplinaExecucao executionCourse = (IDisciplinaExecucao) iter.next();
			//NOTE: only executionCourses of current executionPeriod are updated
			if (executionCourse.getExecutionPeriod().equals(currentExecutionPeriod)) {
				updateBibliographicReferences(executionCourse, broker, loader);
				updateSite(executionCourse, siglaCurricularCourse, broker);
			}
		}

	}

	/**
	 * @param executionCourse
	 * @param siglaCurricularCourse
	 * @param broker
	 */
	private static void updateSite(
		IDisciplinaExecucao executionCourse,
		Plano_curricular_2003final siglaCurricularCourse,
		PersistenceBroker broker) {
		Criteria crit = new Criteria();
		crit.addEqualTo("keyExecutionCourse", executionCourse.getIdInternal());
		Query query = new QueryByCriteria(Site.class, crit);
		ISite site = (ISite) broker.getObjectByQuery(query);
		if ((site.getAlternativeSite() == null || site.getAlternativeSite().equals(""))
			&& (siglaCurricularCourse.getEnder_web() != null && !siglaCurricularCourse.getEnder_web().equals(""))) {
			site.setAlternativeSite(siglaCurricularCourse.getEnder_web());
			broker.store(site);
		}

	}

	/**
	 * @param executionCourse
	 * @param siglaCurricularCourse
	 * @param broker
	 */
	private static void updateBibliographicReferences(
		IDisciplinaExecucao executionCourse,
		PersistenceBroker broker,
		SiglaDataLoader loader) {
		List fenixCurricularCourses = executionCourse.getAssociatedCurricularCourses();
		List siglaCurricularCourses = getSiglaCurricularCourses(fenixCurricularCourses, broker);
		if (siglaCurricularCourses == null || siglaCurricularCourses.isEmpty()) {
			if (!loader.getFenixExecutionCoursesWithProblems().contains(executionCourse)) {
				loader.getFenixExecutionCoursesWithProblems().add(executionCourse);
			}
		} else {
			Plano_curricular_2003final siglaCurricularCourse =
				getBestSiglaCurricularCourse(siglaCurricularCourses);
			if (siglaCurricularCourse != null) {
				insertBibliographicReferences(executionCourse, siglaCurricularCourse, broker);
			}
		}

	}

	/**
	 * @param executionCourse
	 * @param siglaCurricularCourse
	 * @param broker
	 */
	private static void insertBibliographicReferences(
		IDisciplinaExecucao executionCourse,
		Plano_curricular_2003final siglaCurricularCourse,
		PersistenceBroker broker) {
		IBibliographicReference bibliographicReference1 =
			new BibliographicReference(
				executionCourse,
				new String(siglaCurricularCourse.getBib_princ_1()),
				new Boolean(false));
		broker.store(bibliographicReference1);
		IBibliographicReference bibliographicReference2 =
			new BibliographicReference(
				executionCourse,
				new String(siglaCurricularCourse.getBib_princ_2()),
				new Boolean(false));
		broker.store(bibliographicReference2);
		IBibliographicReference bibliographicReference3 =
			new BibliographicReference(
				executionCourse,
				new String(siglaCurricularCourse.getBib_princ_3()),
				new Boolean(false));
		broker.store(bibliographicReference3);
		IBibliographicReference bibliographicReference4 =
			new BibliographicReference(
				executionCourse,
				new String(siglaCurricularCourse.getBib_sec_1()),
				new Boolean(true));
		broker.store(bibliographicReference4);
		IBibliographicReference bibliographicReference5 =
			new BibliographicReference(
				executionCourse,
				new String(siglaCurricularCourse.getBib_sec_2()),
				new Boolean(true));
		broker.store(bibliographicReference5);
		IBibliographicReference bibliographicReference6 =
			new BibliographicReference(
				executionCourse,
				new String(siglaCurricularCourse.getBib_sec_3()),
				new Boolean(true));
		broker.store(bibliographicReference6);

	}

	/**
	 * @param siglaCurricularCourses
	 * @return
	 */
	private static Plano_curricular_2003final getBestSiglaCurricularCourse(List siglaCurricularCourses) {
		Plano_curricular_2003final result = null;
		Iterator iter = siglaCurricularCourses.iterator();
		while (iter.hasNext()) {
			Plano_curricular_2003final aux = (Plano_curricular_2003final) iter.next();
			if (aux.getBib_princ_1() != null
				&& aux.getBib_princ_1().length != 0
				&& (result == null || result.getBib_princ_1().length < aux.getBib_princ_1().length)) {
				result = aux;
			}
		}
		return result;
	}

	/**
	 * @param siglaCurricularCourses
	 * @param broker
	 * @return
	 */
	private static List getSiglaCurricularCourses(List siglaCurricularCourses, PersistenceBroker broker) {
		Iterator iter = siglaCurricularCourses.iterator();
		List result = new ArrayList();
		while (iter.hasNext()) {
			ICurricularCourse curricularCourse = (ICurricularCourse) iter.next();
			result = (List) CollectionUtils.union(result, getSiglaCurricularCourse(curricularCourse, broker));
		}
		return result;
	}

	/**
	 * @param curricularCourse
	 * @param broker
	 * @return
	 */
	private static List getSiglaCurricularCourse(ICurricularCourse curricularCourse, PersistenceBroker broker) {
		Criteria crit = new Criteria();
		crit.addEqualTo("codigo_disc", curricularCourse.getCode());
		crit.addEqualTo("ano_lectivo", "2003");
		crit.addEqualTo("nome_disc", curricularCourse.getName());
		crit.addEqualTo("semestre", "1");
		Query query = new QueryByCriteria(Plano_curricular_2003final.class, crit);
		List result = (List) broker.getCollectionByQuery(query);
		return result;
	}

	/**
	 * @param fenixCurricularCourses
	 * @param siglaCurricularCourse
	 */
	private static void updateFenixCurricularCourses(
		List fenixCurricularCourses,
		Plano_curricular_2003final siglaCurricularCourse,
		PersistenceBroker broker) {
		Iterator iter = fenixCurricularCourses.iterator();
		while (iter.hasNext()) {
			ICurricularCourse curricularCourse = (ICurricularCourse) iter.next();
			//NOTE: if the curricular course in fenix is basic it will continue basic
			if (!curricularCourse.getBasic().booleanValue()) {
				curricularCourse.setBasic(new Boolean(sigla2FenixBasic(siglaCurricularCourse.getCiencia_base())));
				updateCurriculum(curricularCourse, siglaCurricularCourse, broker);
				broker.store(curricularCourse);
			}
		}

	}

	/**
	 * @param curricularCourse
	 * @param siglaCurricularCourse
	 * @param broker
	 */
	private static void updateCurriculum(
		ICurricularCourse curricularCourse,
		Plano_curricular_2003final siglaCurricularCourse,
		PersistenceBroker broker) {
		Criteria crit = new Criteria();
		crit.addEqualTo("keyCurricularCourse", curricularCourse.getIdInternal());
		Query query = new QueryByCriteria(Curriculum.class, crit);
		ICurriculum curriculum = (ICurriculum) broker.getObjectByQuery(query);
		curriculum.setEvaluationElements(new String(siglaCurricularCourse.getCrit_av()));
		curriculum.setEvaluationElementsEn(new String(siglaCurricularCourse.getIngles_crit_av()));
		curriculum.setGeneralObjectives(new String(siglaCurricularCourse.getObjectivos()));
		curriculum.setGeneralObjectivesEn(new String(siglaCurricularCourse.getIngles_objectivos()));
		curriculum.setProgram(new String(siglaCurricularCourse.getProgr_res()));
		curriculum.setProgramEn(new String(siglaCurricularCourse.getIngles_progr_res()));

		broker.store(curriculum);
	}

	/**
	 * @param string
	 */
	private static boolean sigla2FenixBasic(String string) {
		return string.equals("True");
	}

	/**
	 * @param siglaCurricularCourse
	 */
	private static void printSiglaCurricularCourse(Plano_curricular_2003final siglaCurricularCourse) {
		System.out.println(siglaCurricularCourse);
	}

	/**
	 * @param executionCourse
	 * @param siglaCurricularCourses
	 */
	private void updateResponsibleTeachers(
		IDisciplinaExecucao executionCourse,
		List siglaCurricularCourses,
		PersistenceBroker broker) {
		//List teachers = new ArrayList();
		Iterator iter = siglaCurricularCourses.iterator();
		Query query = null;

		List numsMec = new ArrayList();

		while (iter.hasNext()) {

			Curr_licenciatura siglaCurricularCourse = (Curr_licenciatura) iter.next();
			Criteria crit = new Criteria();
			crit.addEqualTo("codigo_Disc", siglaCurricularCourse.getCodigo_disc());
			crit.addEqualTo("ano_Lectivo", siglaCurricularCourse.getAno_lectivo());
			crit.addEqualTo("codigo_Curso", siglaCurricularCourse.getCodigo_lic());
			crit.addEqualTo("ano_Curricular", siglaCurricularCourse.getAno_curricular());
			crit.addEqualTo("semestre", siglaCurricularCourse.getSemestre());
			crit.addEqualTo("codigo_Ramo", siglaCurricularCourse.getCodigo_ramo());
			query = new QueryByCriteria(Responsavel.class, crit);
			Collection siglaTeachers = (Collection) broker.getCollectionByQuery(query);
			Iterator iter1 = siglaTeachers.iterator();
			while (iter1.hasNext()) {
				Responsavel responsavel = (Responsavel) iter1.next();
				if (!numsMec.contains(responsavel.no_Mec)) {
					numsMec.add(responsavel.no_Mec);
				}
			}

		}
		System.out.println("n�mero de docentes respons�veis->" + numsMec.size());
		Iterator iter2 = numsMec.iterator();
		while (iter2.hasNext()) {
			Integer numMec = (Integer) iter2.next();
			Criteria crit = new Criteria();

			crit.addEqualTo("person.username", "D" + numMec);
			query = new QueryByCriteria(Teacher.class, crit);
			ITeacher teacher = (ITeacher) broker.getObjectByQuery(query);
			if (teacher == null) {
				System.out.println("n�o encontrei o docente com n�mero mecanogr�fico:" + numMec);
			} else {
				IProfessorship professorship = new Professorship(teacher, executionCourse);
				IResponsibleFor responsibleFor = new ResponsibleFor(teacher, executionCourse);
				System.out.println(
					"a escrever professorship"
						+ professorship.getExecutionCourse().getNome()
						+ "-"
						+ professorship.getTeacher().getPerson().getUsername());
				try {
					broker.store(professorship);

					broker.store(responsibleFor);
				} catch (PersistenceBrokerException e1) {
					System.out.println(
						"a  professorship j� existe: "
							+ professorship.getExecutionCourse().getNome()
							+ "-"
							+ professorship.getTeacher().getPerson().getUsername());
				}

			}
		}

	}

	/**
	 * @return
	 */
	public List getFenixExecutionCoursesWithProblems() {
		return fenixExecutionCoursesWithProblems;
	}

	/**
	 * @param fenixExecutionCoursesWithProblems
	 */
	public void setFenixExecutionCoursesWithProblems(List fenixExecutionCoursesWithProblems) {
		this.fenixExecutionCoursesWithProblems = fenixExecutionCoursesWithProblems;
	}

}
