/*
 * Created on 10/Abr/2003 by jpvl
 *
 */
package ServidorAplicacao.strategy.enrolment.degree;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import DataBeans.InfoCurricularCourse;
import DataBeans.InfoCurricularCourseScope;
import DataBeans.InfoDegree;
import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.InfoEnrolment;
import DataBeans.InfoEnrolmentInOptionalCurricularCourse;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoStudent;
import DataBeans.InfoStudentCurricularPlan;
import DataBeans.util.Cloner;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.ICurso;
import Dominio.IDegreeCurricularPlan;
import Dominio.IEnrolment;
import Dominio.IEnrolmentInOptionalCurricularCourse;
import Dominio.IEnrolmentPeriod;
import Dominio.IExecutionPeriod;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.Servico.exceptions.OutOfCurricularCourseEnrolmentPeriod;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IPersistentEnrolmentPeriod;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.EnrolmentState;

/**
 * @author jpvl
 */
public abstract class EnrolmentContextManager {

	public static EnrolmentContext initialEnrolmentContext(IStudent student) throws ExcepcaoPersistencia, OutOfCurricularCourseEnrolmentPeriod {
		
		EnrolmentContext enrolmentContext = new EnrolmentContext();

		ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();

		IStudentCurricularPlanPersistente persistentStudentCurricularPlan = persistentSupport.getIStudentCurricularPlanPersistente();
		IPersistentEnrolmentPeriod enrolmentPeriodDAO = persistentSupport.getIPersistentEnrolmentPeriod();
		IPersistentEnrolment persistentEnrolment = persistentSupport.getIPersistentEnrolment();

		final IStudentCurricularPlan studentActiveCurricularPlan =
			persistentStudentCurricularPlan.readActiveStudentCurricularPlan(student.getNumber(), student.getDegreeType());

		IEnrolmentPeriod enrolmentPeriod =
			getEnrolmentPeriod(enrolmentPeriodDAO, studentActiveCurricularPlan);
		List degreeCurricularPlanCurricularCourses = studentActiveCurricularPlan.getDegreeCurricularPlan().getCurricularCourses();

		final List studentEnrolments = persistentEnrolment.readAllByStudentCurricularPlan(studentActiveCurricularPlan);

		final List studentEnrolmentsWithStateApproved = (List) CollectionUtils.select(studentEnrolments, new Predicate() {
			public boolean evaluate(Object obj) {
				IEnrolment enrolment = (IEnrolment) obj;
				return enrolment.getEnrolmentState().equals(EnrolmentState.APROVED_OBJ);
			}
		});

		final List studentDoneCurricularCourses = (List) CollectionUtils.collect(studentEnrolmentsWithStateApproved, new Transformer() {
			public Object transform(Object obj) {
				IEnrolment enrolment = (IEnrolment) obj;
				return enrolment.getCurricularCourseScope().getCurricularCourse();
			}
		});

		final List studentEnrolmentsWithStateEnroled = (List) CollectionUtils.select(studentEnrolments, new Predicate() {
			public boolean evaluate(Object obj) {
				IEnrolment enrolment = (IEnrolment) obj;
				return enrolment.getEnrolmentState().equals(EnrolmentState.ENROLED_OBJ);
			}
		});

		final List studentEnroledCurricularCourses = (List) CollectionUtils.collect(studentEnrolmentsWithStateEnroled, new Transformer() {
			public Object transform(Object obj) {
				IEnrolment enrolment = (IEnrolment) obj;
				return enrolment.getCurricularCourseScope().getCurricularCourse();
			}
		});

		List studentEnroledAndDoneCurricularCourses = new ArrayList();
		studentEnroledAndDoneCurricularCourses.addAll(studentDoneCurricularCourses);
		studentEnroledAndDoneCurricularCourses.addAll(studentEnroledCurricularCourses);

		List studentEnroledCurricularCourseScopes = computeScopesOfCurricularCourses(studentEnroledCurricularCourses);

		List enrolmentsWithStateNotApproved = (List) CollectionUtils.select(studentEnrolments, new Predicate() {
			public boolean evaluate(Object obj) {
				IEnrolment enrolment = (IEnrolment) obj;
				ICurricularCourse curricularCourse = enrolment.getCurricularCourseScope().getCurricularCourse();
				return !studentDoneCurricularCourses.contains(curricularCourse)
					&& enrolment.getEnrolmentState().equals(EnrolmentState.NOT_APROVED_OBJ);
			}
		});

		List curricularCoursesEnrolled = (List) CollectionUtils.collect(enrolmentsWithStateNotApproved, new Transformer() {
			public Object transform(Object obj) {
				IEnrolment enrolment = (IEnrolment) obj;
				return (enrolment.getCurricularCourseScope().getCurricularCourse().getCode() + enrolment.getCurricularCourseScope().getCurricularCourse().getName());
			}
		});

		List studentCurricularPlanCurricularCourses = computeStudentCurricularPlanCurricularCourses(degreeCurricularPlanCurricularCourses, studentActiveCurricularPlan);
				
		IExecutionPeriod enrolmentExecutionPeriod = enrolmentPeriod.getExecutionPeriod(); 

		enrolmentContext.setCurricularCoursesFromStudentCurricularPlan(studentCurricularPlanCurricularCourses);
		enrolmentContext.setStudent(student);
		enrolmentContext.setFinalCurricularCoursesScopesSpanToBeEnrolled(computeCurricularCoursesScopesNotYetDoneByStudent(degreeCurricularPlanCurricularCourses, studentEnroledAndDoneCurricularCourses));
		enrolmentContext.setEnrolmentsAprovedByStudent(studentEnrolmentsWithStateApproved);
		enrolmentContext.setAcumulatedEnrolments(CollectionUtils.getCardinalityMap(curricularCoursesEnrolled));
		enrolmentContext.setStudentActiveCurricularPlan(studentActiveCurricularPlan);
		enrolmentContext.setEnrolmentValidationResult(new EnrolmentValidationResult());
		enrolmentContext.setCurricularCoursesScopesAutomaticalyEnroled(studentEnroledCurricularCourseScopes);
		enrolmentContext.setExecutionPeriod(enrolmentExecutionPeriod);
		
//		enrolmentContext.setChosenOptionalDegree(new Curso());
//		enrolmentContext.setChosenOptionalCurricularCourseScope(new CurricularCourseScope());
		enrolmentContext.setActualEnrolments(new ArrayList());
		enrolmentContext.setDegreesForOptionalCurricularCourses(new ArrayList());
		enrolmentContext.setOptionalCurricularCoursesEnrolments(new ArrayList());
		enrolmentContext.setOptionalCurricularCoursesToChooseFromDegree(new ArrayList());

		return enrolmentContext;
	}

	private static IEnrolmentPeriod getEnrolmentPeriod(
		IPersistentEnrolmentPeriod enrolmentPeriodDAO,
		final IStudentCurricularPlan studentActiveCurricularPlan)
		throws ExcepcaoPersistencia, OutOfCurricularCourseEnrolmentPeriod {
		IEnrolmentPeriod enrolmentPeriod = enrolmentPeriodDAO.readActualEnrolmentPeriodForDegreeCurricularPlan(studentActiveCurricularPlan.getDegreeCurricularPlan());
		if (enrolmentPeriod == null){
			IEnrolmentPeriod nextEnrolmentPeriod = enrolmentPeriodDAO.readNextEnrolmentPeriodForDegreeCurricularPlan(studentActiveCurricularPlan.getDegreeCurricularPlan());
			Date startDate = null;
			Date endDate = null;
			if (nextEnrolmentPeriod != null){
				startDate = nextEnrolmentPeriod.getStartDate();
				endDate = nextEnrolmentPeriod.getEndDate();
			}
			throw new OutOfCurricularCourseEnrolmentPeriod(startDate, endDate);
		}
		return enrolmentPeriod;
	}

	public static List computeStudentCurricularPlanCurricularCourses(
		List degreeCurricularPlanCurricularCourses,
		IStudentCurricularPlan studentActiveCurricularPlan) {

		List scopesOfCurricularCourses = computeScopesOfCurricularCourses(degreeCurricularPlanCurricularCourses);
		Iterator iteratorScopesOfCurricularCourses = scopesOfCurricularCourses.iterator();
		List aux = new ArrayList();
		while (iteratorScopesOfCurricularCourses.hasNext()) {
			ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iteratorScopesOfCurricularCourses.next();
			if ((curricularCourseScope.getBranch().equals(studentActiveCurricularPlan.getBranch()))
				|| (curricularCourseScope.getBranch().getName().equals(""))) {
				if (!aux.contains(curricularCourseScope.getCurricularCourse())) {
					aux.add(curricularCourseScope.getCurricularCourse());
				}
			}
		}
		return aux;
	}

	private static List computeCurricularCoursesScopesNotYetDoneByStudent(
		List curricularCoursesFromStudentDegreeCurricularPlan,
		List aprovedCurricularCoursesFromStudent)
		throws ExcepcaoPersistencia {

		List coursesNotDone = (List) CollectionUtils.subtract(curricularCoursesFromStudentDegreeCurricularPlan, aprovedCurricularCoursesFromStudent);

		return computeScopesOfCurricularCourses(coursesNotDone);
	}

	public static List computeScopesOfCurricularCourses(List curricularCourses) {

		List scopes = new ArrayList();

		Iterator iteratorCourses = curricularCourses.iterator();
		while (iteratorCourses.hasNext()) {
			ICurricularCourse curricularCourse = (ICurricularCourse) iteratorCourses.next();
			List scopesTemp = curricularCourse.getScopes();
			Iterator iteratorScopes = scopesTemp.iterator();
			while (iteratorScopes.hasNext()) {
				ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iteratorScopes.next();
				scopes.add(curricularCourseScope);
			}
		}

		return scopes;
	}

	public static EnrolmentContext getEnrolmentContext(InfoEnrolmentContext infoEnrolmentContext) {

		EnrolmentContext enrolmentContext = new EnrolmentContext();

		IStudent student = Cloner.copyInfoStudent2IStudent(infoEnrolmentContext.getInfoStudent());

		IStudentCurricularPlan studentActiveCurricularPlan = Cloner.copyInfoStudentCurricularPlan2IStudentCurricularPlan(infoEnrolmentContext.getInfoStudentActiveCurricularPlan());

		// Transform final span of info curricular course scopes:
		List curricularCourseScopeList = new ArrayList();
		List infoCurricularCourseScopeList = null;
		infoCurricularCourseScopeList = infoEnrolmentContext.getInfoFinalCurricularCoursesScopesSpanToBeEnrolled();
		if (infoCurricularCourseScopeList != null && !infoCurricularCourseScopeList.isEmpty()) {
			Iterator iterator = infoCurricularCourseScopeList.iterator();
			while (iterator.hasNext()) {
				InfoCurricularCourseScope infoCurricularCourseScope = (InfoCurricularCourseScope) iterator.next();
				ICurricularCourseScope curricularCourseScope = Cloner.copyInfoCurricularCourseScope2ICurricularCourseScope(infoCurricularCourseScope);
				curricularCourseScopeList.add(curricularCourseScope);
			}
		}

		// Transform list of info curricular course scopes being enroled:
		List curricularCourseScopeList2 = new ArrayList();
		List infoCurricularCourseScopeList2 = infoEnrolmentContext.getActualEnrolment();
		if (infoCurricularCourseScopeList2 != null && !infoCurricularCourseScopeList2.isEmpty()) {
			Iterator iterator = infoCurricularCourseScopeList2.iterator();
			while (iterator.hasNext()) {
				InfoCurricularCourseScope infoCurricularCourseScope = (InfoCurricularCourseScope) iterator.next();
				ICurricularCourseScope curricularCourseScope = Cloner.copyInfoCurricularCourseScope2ICurricularCourseScope(infoCurricularCourseScope);
				curricularCourseScopeList2.add(curricularCourseScope);
			}
		}

		// Transform list of info curricular course scopes actually enroled:
		List infoEnroledCurricularCourseScopeList = infoEnrolmentContext.getInfoCurricularCoursesScopesAutomaticalyEnroled();
		List enroledCurricularCourseScopeList = new ArrayList();
		if (infoEnroledCurricularCourseScopeList != null && !infoEnroledCurricularCourseScopeList.isEmpty()) {
			Iterator iterator = infoEnroledCurricularCourseScopeList.iterator();
			while (iterator.hasNext()) {
				InfoCurricularCourseScope infoCurricularCourseScope = (InfoCurricularCourseScope) iterator.next();
				ICurricularCourseScope curricularCourseScope = Cloner.copyInfoCurricularCourseScope2ICurricularCourseScope(infoCurricularCourseScope);
				enroledCurricularCourseScopeList.add(curricularCourseScope);
			}
		}

		// Transform list of info degrees to choose from, for optional courses selection:
		List infoDegreeList = infoEnrolmentContext.getInfoDegreesForOptionalCurricularCourses();
		List optionalDegreeList = new ArrayList();
		if (infoDegreeList != null && !infoDegreeList.isEmpty()) {
			Iterator iterator = infoDegreeList.iterator();
			while (iterator.hasNext()) {
				InfoDegree infoDegree = (InfoDegree) iterator.next();
				ICurso degree = getDegree(infoDegree);
				optionalDegreeList.add(degree);
			}
		}

		// Transform list of optional info curricular courses to choose from:
		List infoOptionalCurricularCourseList = infoEnrolmentContext.getOptionalInfoCurricularCoursesToChooseFromDegree();
		List optionalCurricularCourseList = new ArrayList();
		if (infoOptionalCurricularCourseList != null && !infoOptionalCurricularCourseList.isEmpty()) {
			Iterator iterator = infoOptionalCurricularCourseList.iterator();
			while (iterator.hasNext()) {
				InfoCurricularCourse infoCurricularCourse = (InfoCurricularCourse) iterator.next();
				ICurricularCourse curricularCourse = Cloner.copyInfoCurricularCourse2CurricularCourse(infoCurricularCourse);
				optionalCurricularCourseList.add(curricularCourse);
			}
		}

		// Transform list of optional info curricular courses enrolments:
		List infoOptionalCurricularCoursesEnrolmentsList = infoEnrolmentContext.getInfoOptionalCurricularCoursesEnrolments();
		List optionalCurricularCoursesEnrolmentsList = new ArrayList();
		if (infoOptionalCurricularCoursesEnrolmentsList != null && !infoOptionalCurricularCoursesEnrolmentsList.isEmpty()) {
			Iterator iterator = infoOptionalCurricularCoursesEnrolmentsList.iterator();
			while (iterator.hasNext()) {
				InfoEnrolmentInOptionalCurricularCourse infoEnrolmentInOptionalCurricularCourse = (InfoEnrolmentInOptionalCurricularCourse) iterator.next();
				IEnrolmentInOptionalCurricularCourse enrolmentInOptionalCurricularCourse = (IEnrolmentInOptionalCurricularCourse) Cloner.copyInfoEnrolment2IEnrolment(infoEnrolmentInOptionalCurricularCourse);
				optionalCurricularCoursesEnrolmentsList.add(enrolmentInOptionalCurricularCourse);
			}
		}

		ICurso degree = getDegree(infoEnrolmentContext.getChosenOptionalInfoDegree());
		
		IExecutionPeriod executionPeriod = Cloner.copyInfoExecutionPeriod2IExecutionPeriod(infoEnrolmentContext.getInfoExecutionPeriod());
		
		ICurricularCourseScope chosenCurricularCourseScope = null;
		if(infoEnrolmentContext.getInfoChosenOptionalCurricularCourseScope() != null){
			chosenCurricularCourseScope = Cloner.copyInfoCurricularCourseScope2ICurricularCourseScope(infoEnrolmentContext.getInfoChosenOptionalCurricularCourseScope());
		}

		// Transform list of info curricular courses done by the student:
		List infoEnrolmentsAprovedByStudentList = infoEnrolmentContext.getInfoEnrolmentsAprovedByStudent();
		List enrolmentsAprovedByStudentList = new ArrayList();
		if (infoEnrolmentsAprovedByStudentList != null && !infoEnrolmentsAprovedByStudentList.isEmpty()) {
			Iterator iterator = infoEnrolmentsAprovedByStudentList.iterator();
			while (iterator.hasNext()) {
				InfoEnrolment infoEnrolment = (InfoEnrolment) iterator.next();
				IEnrolment enrolment = Cloner.copyInfoEnrolment2IEnrolment(infoEnrolment);
				enrolmentsAprovedByStudentList.add(enrolment);
			}
		}

		try {
			BeanUtils.copyProperties(enrolmentContext, infoEnrolmentContext);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		enrolmentContext.setStudentActiveCurricularPlan(studentActiveCurricularPlan);
		enrolmentContext.setFinalCurricularCoursesScopesSpanToBeEnrolled(curricularCourseScopeList);
		enrolmentContext.setStudent(student);
		enrolmentContext.setActualEnrolments(curricularCourseScopeList2);
		enrolmentContext.setCurricularCoursesScopesAutomaticalyEnroled(enroledCurricularCourseScopeList);
		enrolmentContext.setChosenOptionalDegree(degree);
		enrolmentContext.setDegreesForOptionalCurricularCourses(optionalDegreeList);
		enrolmentContext.setOptionalCurricularCoursesToChooseFromDegree(optionalCurricularCourseList);
		enrolmentContext.setEnrolmentsAprovedByStudent(enrolmentsAprovedByStudentList);
		enrolmentContext.setOptionalCurricularCoursesEnrolments(optionalCurricularCoursesEnrolmentsList);
		enrolmentContext.setChosenOptionalCurricularCourseScope(chosenCurricularCourseScope);
		enrolmentContext.setExecutionPeriod(executionPeriod);
		
		return enrolmentContext;
	}

	public static InfoEnrolmentContext getInfoEnrolmentContext(EnrolmentContext enrolmentContext) {

		InfoEnrolmentContext infoEnrolmentContext = new InfoEnrolmentContext();

		InfoStudent infoStudent = Cloner.copyIStudent2InfoStudent(enrolmentContext.getStudent());

		InfoStudentCurricularPlan infoStudentActiveCurricularPlan = Cloner.copyIStudentCurricularPlan2InfoStudentCurricularPlan(enrolmentContext.getStudentActiveCurricularPlan());

		// Transform final span of curricular course scopes to respective info:
		List infoCurricularCourseScopeList = new ArrayList();
		List curricularCourseScopeList = null;
		curricularCourseScopeList = enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled();
		if (curricularCourseScopeList != null && !curricularCourseScopeList.isEmpty()) {
			Iterator iterator = curricularCourseScopeList.iterator();
			while (iterator.hasNext()) {
				ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iterator.next();
				InfoCurricularCourseScope infoCurricularCourseScope =
					Cloner.copyICurricularCourseScope2InfoCurricularCourseScope(curricularCourseScope);
				infoCurricularCourseScopeList.add(infoCurricularCourseScope);
			}
		}

		// Transform list of curricular course scopes being enroled to respective info:
		List infoCurricularCourseScopeList2 = new ArrayList();
		List curricularCourseScopeList2 = enrolmentContext.getActualEnrolments();
		if (curricularCourseScopeList2 != null && !curricularCourseScopeList2.isEmpty()) {
			Iterator iterator = curricularCourseScopeList2.iterator();
			while (iterator.hasNext()) {
				ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iterator.next();
				InfoCurricularCourseScope infoCurricularCourseScope =
					Cloner.copyICurricularCourseScope2InfoCurricularCourseScope(curricularCourseScope);
				infoCurricularCourseScopeList2.add(infoCurricularCourseScope);
			}
		}

		// Transform list of curricular course scopes actually enroled to respective info:
		List enroledCurricularCourseScopeList = enrolmentContext.getCurricularCoursesScopesAutomaticalyEnroled();
		List infoEnroledCurricularCourseScopeList = new ArrayList();
		if (enroledCurricularCourseScopeList != null && !enroledCurricularCourseScopeList.isEmpty()) {
			Iterator iterator = enroledCurricularCourseScopeList.iterator();
			while (iterator.hasNext()) {
				ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iterator.next();
				InfoCurricularCourseScope infoCurricularCourseScope =
					Cloner.copyICurricularCourseScope2InfoCurricularCourseScope(curricularCourseScope);
				infoEnroledCurricularCourseScopeList.add(infoCurricularCourseScope);
			}
		}

		// Transform list of degrees to choose from, for optional courses selection to respective info:
		List optionalDegreeList = enrolmentContext.getDegreesForOptionalCurricularCourses();
		List infoDegreeList = new ArrayList();
		if (optionalDegreeList != null && !optionalDegreeList.isEmpty()) {
			Iterator iterator = optionalDegreeList.iterator();
			while (iterator.hasNext()) {
				ICurso degree = (ICurso) iterator.next();
				InfoDegree infoDegree = getInfoDegree(degree);
				infoDegreeList.add(infoDegree);
			}
		}

		// Transform list of optional curricular courses to choose from to respective info:
		List optionalCurricularCourseList = enrolmentContext.getOptionalCurricularCoursesToChooseFromDegree();
		List infoOptionalCurricularCourseList = new ArrayList();
		if (optionalCurricularCourseList != null && !optionalCurricularCourseList.isEmpty()) {
			Iterator iterator = optionalCurricularCourseList.iterator();
			while (iterator.hasNext()) {
				ICurricularCourse curricularCourse = (ICurricularCourse) iterator.next();
				InfoCurricularCourse infoCurricularCourse =	Cloner.copyCurricularCourse2InfoCurricularCourse(curricularCourse);
				infoOptionalCurricularCourseList.add(infoCurricularCourse);
			}
		}

		// Transform list of optional curricular courses enrolments to respective info:
		List optionalCurricularCourseEnrolmentsList = enrolmentContext.getOptionalCurricularCoursesEnrolments();
		List infoOptionalCurricularCoursesEnrolmentsList = new ArrayList();
		if (optionalCurricularCourseEnrolmentsList != null && !optionalCurricularCourseEnrolmentsList.isEmpty()) {
			Iterator iterator = optionalCurricularCourseEnrolmentsList.iterator();
			while (iterator.hasNext()) {
				IEnrolmentInOptionalCurricularCourse enrolmentInOptionalCurricularCourse = (IEnrolmentInOptionalCurricularCourse) iterator.next();
				InfoEnrolmentInOptionalCurricularCourse infoEnrolmentInOptionalCurricularCourse =	(InfoEnrolmentInOptionalCurricularCourse) Cloner.copyIEnrolment2InfoEnrolment(enrolmentInOptionalCurricularCourse);
				infoOptionalCurricularCoursesEnrolmentsList.add(infoEnrolmentInOptionalCurricularCourse);
			}
		}

		InfoDegree infoDegree = getInfoDegree(enrolmentContext.getChosenOptionalDegree());
		
		InfoExecutionPeriod infoExecutionPeriod = Cloner.copyIExecutionPeriod2InfoExecutionPeriod(enrolmentContext.getExecutionPeriod());
		
		InfoCurricularCourseScope infoChosenCurricularCourseScope = null;
		if(enrolmentContext.getChosenOptionalCurricularCourseScope() != null) {
			infoChosenCurricularCourseScope = Cloner.copyICurricularCourseScope2InfoCurricularCourseScope(enrolmentContext.getChosenOptionalCurricularCourseScope());
		}

		// Transform list of info curricular courses done by the student:
		List enrolmentsAprovedByStudentList = enrolmentContext.getEnrolmentsAprovedByStudent();
		List infoEnrolmentsAprovedByStudentList = new ArrayList();
		if (enrolmentsAprovedByStudentList != null && !enrolmentsAprovedByStudentList.isEmpty()) {
			Iterator iterator = enrolmentsAprovedByStudentList.iterator();
			while (iterator.hasNext()) {
				IEnrolment enrolment = (IEnrolment) iterator.next();
				InfoEnrolment infoEnrolment = Cloner.copyIEnrolment2InfoEnrolment(enrolment);
				infoEnrolmentsAprovedByStudentList.add(infoEnrolment);
			}
		}

		try {
			BeanUtils.copyProperties(infoEnrolmentContext, enrolmentContext);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		infoEnrolmentContext.setInfoStudentActiveCurricularPlan(infoStudentActiveCurricularPlan);
		infoEnrolmentContext.setInfoFinalCurricularCoursesScopesSpanToBeEnrolled(infoCurricularCourseScopeList);
		infoEnrolmentContext.setInfoStudent(infoStudent);
		infoEnrolmentContext.setActualEnrolment(infoCurricularCourseScopeList2);
		infoEnrolmentContext.setInfoCurricularCoursesScopesAutomaticalyEnroled(infoEnroledCurricularCourseScopeList);
		infoEnrolmentContext.setChosenOptionalInfoDegree(infoDegree);
		infoEnrolmentContext.setInfoDegreesForOptionalCurricularCourses(infoDegreeList);
		infoEnrolmentContext.setOptionalInfoCurricularCoursesToChooseFromDegree(infoOptionalCurricularCourseList);
		infoEnrolmentContext.setInfoEnrolmentsAprovedByStudent(infoEnrolmentsAprovedByStudentList);
		infoEnrolmentContext.setInfoOptionalCurricularCoursesEnrolments(infoOptionalCurricularCoursesEnrolmentsList);
		infoEnrolmentContext.setInfoChosenOptionalCurricularCourseScope(infoChosenCurricularCourseScope);
		infoEnrolmentContext.setInfoExecutionPeriod(infoExecutionPeriod);
		
		return infoEnrolmentContext;
	}

	private static InfoDegree getInfoDegree(ICurso degree) {
		InfoDegree infoDegree = null;
		if (degree != null) {
			infoDegree = Cloner.copyIDegree2InfoDegree(degree);
			List degreeCurricularPlanList = degree.getDegreeCurricularPlans();
			List infoDegreeCurricularPlanList = new ArrayList();
			if (degreeCurricularPlanList != null && !degreeCurricularPlanList.isEmpty()) {
				Iterator iterator = degreeCurricularPlanList.iterator();
				while (iterator.hasNext()) {
					IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) iterator.next();
					InfoDegreeCurricularPlan infoDegreeCurricularPlan =	Cloner.copyIDegreeCurricularPlan2InfoDegreeCurricularPlan(degreeCurricularPlan);
					infoDegreeCurricularPlanList.add(infoDegreeCurricularPlan);
				}
			}
			infoDegree.setInfoDegreeCurricularPlans(infoDegreeCurricularPlanList);
		}
		return infoDegree;
	}

	private static ICurso getDegree(InfoDegree infoDegree) {

		ICurso degree = null;
		if (infoDegree != null) {
			degree = Cloner.copyInfoDegree2IDegree(infoDegree);
			List infoDegreeCurricularPlanList = infoDegree.getInfoDegreeCurricularPlans();
			List degreeCurricularPlanList = new ArrayList();
			if (infoDegreeCurricularPlanList != null && !infoDegreeCurricularPlanList.isEmpty()) {
				Iterator iterator = infoDegreeCurricularPlanList.iterator();
				while (iterator.hasNext()) {
					InfoDegreeCurricularPlan infoDegreeCurricularPlan = (InfoDegreeCurricularPlan) iterator.next();
					IDegreeCurricularPlan degreeCurricularPlan = Cloner.copyInfoDegreeCurricularPlan2IDegreeCurricularPlan(infoDegreeCurricularPlan);
					degreeCurricularPlanList.add(degreeCurricularPlan);
				}
			}
			degree.setDegreeCurricularPlans(degreeCurricularPlanList);
		}
		return degree;
	}
}
