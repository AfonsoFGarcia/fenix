package middleware.almeida.dcsrjao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import Dominio.Enrolment;
import Dominio.EnrolmentEquivalence;
import Dominio.EnrolmentEquivalenceRestriction;
import Dominio.EnrolmentEvaluation;
import Dominio.ExecutionPeriod;
import Dominio.ExecutionYear;
import Dominio.IBranch;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseEquivalence;
import Dominio.ICurricularCourseEquivalenceRestricition;
import Dominio.ICurricularCourseScope;
import Dominio.IDegreeCurricularPlan;
import Dominio.IEnrolment;
import Dominio.IEnrolmentEquivalence;
import Dominio.IEnrolmentEquivalenceRestriction;
import Dominio.IEnrolmentEvaluation;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Util.EnrolmentEvaluationState;
import Util.EnrolmentEvaluationType;
import Util.EnrolmentState;
import Util.PeriodState;

/**
 * @author dcs-rjao
 *
 * 21/Mai/2003
 */

public class CreateEnrolmentEquivalences extends LoadDataToFenix {

	private static CreateEnrolmentEquivalences loader = null;
	private static String logString = "";
	private static HashMap error = new HashMap();
	private static String errorMessage = "";
	private static String errorDBID = "";
	private IDegreeCurricularPlan newDegreeCurricularPlan = null;
	private boolean before1977 = true;
	private String outputFilename = "";

	public CreateEnrolmentEquivalences(int time) {
	}

	public static void main(boolean flag) {
		if (loader == null) {
			loader = new CreateEnrolmentEquivalences(1);
		}
		
		logString = "";
		error = new HashMap();;
		errorMessage = "";
		errorDBID = "";
		
		loader.before1977 = flag;
		if (loader.before1977 == true) {
			loader.outputFilename = "Before1997.txt";
		} else {
			loader.outputFilename = "After1997.txt";
		}

		loader.migrationStart(loader.getClassName());

		loader.setupDAO();

		loader.newDegreeCurricularPlan = loader.processNewDegreeCurricularPlan();
		if (loader.newDegreeCurricularPlan == null) {
			logString += error.toString();
			loader.migrationEnd(loader.getClassName(), logString);
			return;
		}

		List curricularCourseEquivalencesList = null;

		curricularCourseEquivalencesList = loader.persistentObjectOJB.readAllCurricularCourseEquivalences();
		loader.shutdownDAO();

		Iterator iterator = curricularCourseEquivalencesList.iterator();
		ICurricularCourseEquivalence curricularCourseEquivalence = null;
		while (iterator.hasNext()) {
			curricularCourseEquivalence = (ICurricularCourseEquivalence) iterator.next();
			loader.printIteration(loader.getClassName(), curricularCourseEquivalence.getIdInternal().longValue());
			loader.setupDAO();
			loader.processCourseEquivalence(curricularCourseEquivalence);
			loader.shutdownDAO();
		}

		logString += error.toString();
		loader.migrationEnd(loader.getClassName(), logString);
	}

	private void processCourseEquivalence(ICurricularCourseEquivalence curricularCourseEquivalence) {
		if (curricularCourseEquivalence.getCurricularCourse().getDegreeCurricularPlan().equals(this.newDegreeCurricularPlan)) {
			List courseEquivalenceRestrictions = curricularCourseEquivalence.getEquivalenceRestrictions();
			int numRestrictionsNecessaryToEquivalence = courseEquivalenceRestrictions.size();
			List enrolmentsInRestrictions = new ArrayList();

			Iterator iterator = courseEquivalenceRestrictions.iterator();
			while (iterator.hasNext()) {
				ICurricularCourseEquivalenceRestricition curricularCourseEquivalenceRestriction = (ICurricularCourseEquivalenceRestricition) iterator.next();
				ICurricularCourse equivalentCurricularCourse = curricularCourseEquivalenceRestriction.getEquivalentCurricularCourse();
				List enrolmentsInEquivalentCourse = persistentObjectOJB.readEnrolmentsByCurricularCourse(equivalentCurricularCourse);
				if (enrolmentsInEquivalentCourse == null) {
					errorMessage =
						"\n O curricularCourse "
							+ curricularCourseEquivalence.getCurricularCourse().getName()
							+ " tem equivalencias mas n�o tem restrictions! Registos: ";
					errorDBID = curricularCourseEquivalence.getIdInternal() + ",";
					error = loader.setErrorMessage(errorMessage, errorDBID, error);
					numberUntreatableElements++;
				} else {
					enrolmentsInRestrictions.addAll(enrolmentsInEquivalentCourse);
				}
			}

			final List aprovedEnrolmentsInRestrictions = (List) CollectionUtils.select(enrolmentsInRestrictions, new Predicate() {
				public boolean evaluate(Object obj) {
					IEnrolment enrolment = (IEnrolment) obj;
					return enrolment.getEnrolmentState().equals(EnrolmentState.APROVED);
				}
			});

			List enrolmentsFromSameStudentCurricularPlanAsFirstEnrolment = new ArrayList();
			while (!aprovedEnrolmentsInRestrictions.isEmpty()) {
				iterator = aprovedEnrolmentsInRestrictions.iterator();
				IEnrolment firstEnrolment = null;
				if (iterator.hasNext()) {
					firstEnrolment = (IEnrolment) iterator.next();
					enrolmentsFromSameStudentCurricularPlanAsFirstEnrolment.add(firstEnrolment);
				}

				while (iterator.hasNext()) {
					IEnrolment nextEnrolment = (IEnrolment) iterator.next();
					if (nextEnrolment.getStudentCurricularPlan().equals(firstEnrolment.getStudentCurricularPlan())) {
						enrolmentsFromSameStudentCurricularPlanAsFirstEnrolment.add(nextEnrolment);
					}
				}

				aprovedEnrolmentsInRestrictions.removeAll(enrolmentsFromSameStudentCurricularPlanAsFirstEnrolment);

				enrolmentsFromSameStudentCurricularPlanAsFirstEnrolment =
					processEnrolmentEquivalencesByYear(enrolmentsFromSameStudentCurricularPlanAsFirstEnrolment);

				if (enrolmentsFromSameStudentCurricularPlanAsFirstEnrolment == null) {
					return;
				}

				if (enrolmentsFromSameStudentCurricularPlanAsFirstEnrolment.size() == numRestrictionsNecessaryToEquivalence) {
					processEquivalentEnrolment(firstEnrolment, curricularCourseEquivalence, enrolmentsFromSameStudentCurricularPlanAsFirstEnrolment);
				}

				enrolmentsFromSameStudentCurricularPlanAsFirstEnrolment.clear();
			}
		}
	}

	private List processEnrolmentEquivalencesByYear(List enrolmentsFromSameStudentCurricularPlanAsFirstEnrolment) {
		List result = new ArrayList();
		Iterator iterator = enrolmentsFromSameStudentCurricularPlanAsFirstEnrolment.iterator();
		while (iterator.hasNext()) {
			IEnrolment enrolment = (IEnrolment) iterator.next();
			String enrolmentYearStr = enrolment.getExecutionPeriod().getExecutionYear().getYear().substring(0, 3);
			Integer enrolmentYear = null;
			try {
				enrolmentYear = new Integer(enrolmentYearStr);
			} catch (NumberFormatException e) {
				errorMessage = "\n O ano " + enrolmentYearStr + " inv�lido para a cria��o de Intergers!";
				errorDBID = "";
				error = loader.setErrorMessage(errorMessage, errorDBID, error);
				numberUntreatableElements++;
				return null;
			}
			int enrolmentSemester = enrolment.getExecutionPeriod().getSemester().intValue();

			if ((this.before1977 == true) && (enrolmentSemester == 2) && (enrolmentYear.intValue() < 1997)) {
				result.add(enrolment);
			}

			if ((this.before1977 == false) && (enrolmentSemester == 2) && (enrolmentYear.intValue() >= 1997)) {
				result.add(enrolment);
			}
		}
		return result;
	}

	private void processEquivalentEnrolment(
		IEnrolment firstEnrolment,
		ICurricularCourseEquivalence curricularCourseEquivalence,
		List enrolmentsFromSameStudentCurricularPlanAsFirstEnrolment) {
		IStudent student = firstEnrolment.getStudentCurricularPlan().getStudent();
		IStudentCurricularPlan activeStudentCurricularPlan =
			persistentObjectOJB.readActiveStudentCurricularPlanByStudentAndDegreeCurricularPlan(student, this.newDegreeCurricularPlan);
		if (student == null) {
			errorMessage =
				"\n O StudentCurricularPlan do estudante "
					+ student.getNumber()
					+ " no plano curricular "
					+ this.newDegreeCurricularPlan.getName()
					+ "n�o existe! Registos: ";
			errorDBID = curricularCourseEquivalence.getIdInternal() + ",";
			error = loader.setErrorMessage(errorMessage, errorDBID, error);
			numberUntreatableElements++;
			return;
		}

		int year = 2003;
		// o semestre vai a 1 porque n�o interessa qual o semestre curricular do novo enrolment (por equivalencia)
		IExecutionPeriod executionPeriod = processExecutionPeriod(year, 1);
		ICurricularCourse curricularCourseToEnrol = curricularCourseEquivalence.getCurricularCourse();
		IBranch branch = activeStudentCurricularPlan.getBranch();
		ICurricularCourseScope curricularCourseScope =
			persistentObjectOJB.readCurricularCourseScopeByCurricularCourseAndBranch(curricularCourseToEnrol, branch);
		if (curricularCourseScope == null) {
			errorMessage =
				"\n N�o existe o curricularCourseScope no course = "
					+ curricularCourseToEnrol.getCode()
					+ "["
					+ curricularCourseToEnrol.getIdInternal()
					+ "]"
					+ " Ano = 2003 Ramo = "
					+ branch.getCode()
					+ "["
					+ branch.getInternalID()
					+ "]"
					+ ". Registos: ";
			errorDBID = curricularCourseEquivalence.getIdInternal() + ",";
			error = loader.setErrorMessage(errorMessage, errorDBID, error);
			numberUntreatableElements++;
			return;
		}

		IEnrolment newEnrolment = persistentObjectOJB.readEnrolmentByUnique(activeStudentCurricularPlan, curricularCourseScope, executionPeriod);
		if (newEnrolment == null) {
			newEnrolment = new Enrolment();
			newEnrolment.setCurricularCourseScope(curricularCourseScope);
			newEnrolment.setExecutionPeriod(executionPeriod);
			newEnrolment.setStudentCurricularPlan(activeStudentCurricularPlan);
			newEnrolment.setEnrolmentEvaluationType(EnrolmentEvaluationType.CLOSED_OBJ);
			newEnrolment.setEnrolmentState(EnrolmentState.APROVED);
			writeElement(newEnrolment);

			IEnrolmentEquivalence enrolmentEquivalence =
				processEnrolmentEquivalence(curricularCourseEquivalence, newEnrolment, enrolmentsFromSameStudentCurricularPlanAsFirstEnrolment);
			if (enrolmentEquivalence == null) {
				return;
			}

			IEnrolmentEvaluation enrolmentEvaluation =
				processEnrolmentEvaluation(curricularCourseEquivalence, newEnrolment, enrolmentsFromSameStudentCurricularPlanAsFirstEnrolment);
			if (enrolmentEvaluation == null) {
				return;
			}
		}
		// O else n�o � erro porque existem v�rias equivalencias para a mesma disciplina, por isso, v�o se o aluno tiver feito
		// uma equivalencia � criado um enrolment, na iteracao seguintem, j� existe um enrolment para outra equivalencia
		//		 else {
		//			errorMessage = "\n j� existe um enrolment para o aluno " + student.getNumber() + " na cadeira " + curricularCourseToEnrol.getName() + " no ano " + executionPeriod.getName() + " Registos: ";
		//			errorDBID = curricularCourseEquivalence.getIdInternal() + ",";
		//			error = loader.setErrorMessage(errorMessage, errorDBID, error);
		//			numberUntreatableElements++;
		//			return;
		//		}

	}

	private IEnrolmentEvaluation processEnrolmentEvaluation(
		ICurricularCourseEquivalence curricularCourseEquivalence,
		IEnrolment newEnrolment,
		List enrolmentsFromSameStudentCurricularPlanAsFirstEnrolment) {
		int actualGrade = 0;
		int maxGrade = 0;
		List grades = new ArrayList();

		Iterator enrolmentsIterator = enrolmentsFromSameStudentCurricularPlanAsFirstEnrolment.iterator();
		while (enrolmentsIterator.hasNext()) {
			IEnrolment oldEnrolment = (IEnrolment) enrolmentsIterator.next();
			List oldEvaluations = oldEnrolment.getEvaluations();

			Iterator evaluationsIterator = oldEvaluations.iterator();
			while (evaluationsIterator.hasNext()) {
				IEnrolmentEvaluation oldEnrolmentEvaluation = (IEnrolmentEvaluation) evaluationsIterator.next();
				try {
					if (!oldEnrolmentEvaluation.getGrade().equals("RE")) {
						actualGrade = new Integer(oldEnrolmentEvaluation.getGrade()).intValue();
					}

				} catch (NumberFormatException e) {
					errorMessage = "\n Nota " + oldEnrolmentEvaluation.getGrade() + " inv�lida! Registos: ";
					errorDBID = curricularCourseEquivalence.getIdInternal() + ",";
					error = loader.setErrorMessage(errorMessage, errorDBID, error);
					numberUntreatableElements++;
					return null;
				}

				if (actualGrade > maxGrade) {
					maxGrade = actualGrade;
				}
			}
			grades.add(new Integer(maxGrade));
		}

		IEnrolmentEvaluation equivalentEnrolmentEvaluation = new EnrolmentEvaluation();

		Iterator gradeIterator = grades.iterator();
		int somaNotas = 0;
		int numeroNotas = 0;
		int notaFinal = 0;
		while (gradeIterator.hasNext()) {
			somaNotas = somaNotas + ((Integer) gradeIterator.next()).intValue();
			numeroNotas++;
		}

		notaFinal = somaNotas / numeroNotas;
		// NOTE DAVID-RICARDO: Qual a nota quando � mais que um enrolment para equivalencia?
		equivalentEnrolmentEvaluation.setGrade("" + notaFinal);
		// NOTE DAVID-RICARDO: Quais s�o os restantes campos quando � mais que um enrolment para equivalencia?
		equivalentEnrolmentEvaluation.setEnrolmentEvaluationType(EnrolmentEvaluationType.EQUIVALENCE_OBJ);
		equivalentEnrolmentEvaluation.setEnrolmentEvaluationState(EnrolmentEvaluationState.FINAL_OBJ);
		equivalentEnrolmentEvaluation.setEnrolment(newEnrolment);
		// TODO DAVID-RICARDO: Quando o algoritmo do checksum estiver feito tem de ser actualizar este campo
		equivalentEnrolmentEvaluation.setCheckSum(null);

		return equivalentEnrolmentEvaluation;
	}

	private IEnrolmentEquivalence processEnrolmentEquivalence(
		ICurricularCourseEquivalence curricularCourseEquivalence,
		IEnrolment newEnrolment,
		List enrolmentsFromSameStudentCurricularPlanAsFirstEnrolment) {
		//		IEnrolmentEquivalence enrolmentEquivalence = persistentObjectOJB.readEnrolmentEquivalenceByUnique(newEnrolment);
		//		if (enrolmentEquivalence == null) {

		IEnrolmentEquivalence enrolmentEquivalence = new EnrolmentEquivalence();
		enrolmentEquivalence.setEnrolment(newEnrolment);
		writeElement(enrolmentEquivalence);

		Iterator iterator = enrolmentsFromSameStudentCurricularPlanAsFirstEnrolment.iterator();
		while (iterator.hasNext()) {
			IEnrolment oldEnrolment = (IEnrolment) iterator.next();
			IEnrolmentEquivalenceRestriction enrolmentEquivalenceRestriction =
				persistentObjectOJB.readEnrolmentEquivalenceRestrictionByUnique(oldEnrolment, enrolmentEquivalence);
			if (enrolmentEquivalenceRestriction == null) {
				enrolmentEquivalenceRestriction = new EnrolmentEquivalenceRestriction();
				enrolmentEquivalenceRestriction.setEnrolmentEquivalence(enrolmentEquivalence);
				enrolmentEquivalenceRestriction.setEquivalentEnrolment(oldEnrolment);
				writeElement(enrolmentEquivalenceRestriction);
				numberElementsWritten--;
			} else {
				errorMessage =
					"\n j� existe uma restric�o entre o enrolment novo = "
						+ newEnrolment.getIdInternal()
						+ " e o enrolment antigo = "
						+ oldEnrolment.getIdInternal()
						+ "! Registos: ";
				errorDBID = curricularCourseEquivalence.getIdInternal() + ",";
				error = loader.setErrorMessage(errorMessage, errorDBID, error);
			}
		}
		//		} else {
		//			errorMessage = "\n j� existe uma equivalencia entre para o enrolment novo = " + newEnrolment.getIdInternal() + "! Registos: ";
		//			errorDBID = curricularCourseEquivalence.getIdInternal() + ",";
		//			error = loader.setErrorMessage(errorMessage, errorDBID, error);
		//			return null;
		//		}
		return enrolmentEquivalence;
	}

	private IDegreeCurricularPlan processNewDegreeCurricularPlan() {
		IDegreeCurricularPlan degreeCurricularPlan = persistentObjectOJB.readDegreeCurricularPlanByName(InfoForMigration.NAME_OF_NEW_DEGREE_CURRICULAR_PLAN);
		if (degreeCurricularPlan == null) {
			errorMessage = "\n O plano curricular" + InfoForMigration.NAME_OF_NEW_DEGREE_CURRICULAR_PLAN + " n�o existe!";
			errorDBID = "";
			error = loader.setErrorMessage(errorMessage, errorDBID, error);
			return null;
		}

		return degreeCurricularPlan;
	}

	private IExecutionPeriod processExecutionPeriod(long yearLong, long semesterLong) {

		Integer semester = new Integer("" + semesterLong);
		Integer year = new Integer("" + yearLong);
		String yearStr = new String((year.intValue() - 1) + "/" + year.intValue());

		IExecutionYear executionYear = persistentObjectOJB.readExecutionYearByYear(yearStr);
		if (executionYear == null) {
			executionYear = new ExecutionYear();
			executionYear.setState(PeriodState.CLOSED);
			executionYear.setYear(yearStr);
			writeElement(executionYear);
			loader.numberElementsWritten--;
		}

		IExecutionPeriod executionPeriod = persistentObjectOJB.readExecutionPeriodByYearAndSemester(executionYear, semester);
		if (executionPeriod == null) {
			executionPeriod = new ExecutionPeriod();
			executionPeriod.setExecutionYear(executionYear);
			executionPeriod.setSemester(semester);
			executionPeriod.setState(PeriodState.CLOSED);
			String name = "";
			if (semester.intValue() == 1) {
				name = "1� Semestre";
			} else if (semester.intValue() == 2) {
				name = "2� Semestre";
			}
			executionPeriod.setName(name);
			writeElement(executionPeriod);
			loader.numberElementsWritten--;
		}

		return executionPeriod;
	}

	protected String getFilenameOutput() {
		return "etc/migration/dcs-rjao/logs/CreateEnrolmentEquivalences" + this.outputFilename;
	}

	protected String getClassName() {
		return "CreateEnrolmentEquivalences";
	}
}