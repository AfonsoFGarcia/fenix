/*
 * Created on May 15, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package middleware.almeida;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;

import Dominio.Branch;
import Dominio.CurricularCourse;
import Dominio.CurricularCourseEquivalence;
import Dominio.CurricularCourseEquivalenceRestriction;
import Dominio.CurricularCourseScope;
import Dominio.CurricularSemester;
import Dominio.CurricularYear;
import Dominio.Curso;
import Dominio.DegreeCurricularPlan;
import Dominio.DisciplinaExecucao;
import Dominio.Enrolment;
import Dominio.EnrolmentEquivalence;
import Dominio.EnrolmentEquivalenceRestriction;
import Dominio.EnrolmentEvaluation;
import Dominio.ExecutionPeriod;
import Dominio.ExecutionYear;
import Dominio.Frequenta;
import Dominio.Funcionario;
import Dominio.IBranch;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseEquivalence;
import Dominio.ICurricularCourseEquivalenceRestricition;
import Dominio.ICurricularCourseScope;
import Dominio.ICurricularSemester;
import Dominio.ICurricularYear;
import Dominio.ICurso;
import Dominio.IDegreeCurricularPlan;
import Dominio.IDisciplinaExecucao;
import Dominio.IEnrolment;
import Dominio.IEnrolmentEquivalence;
import Dominio.IEnrolmentEquivalenceRestriction;
import Dominio.IEnrolmentEvaluation;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.IFrequenta;
import Dominio.IPessoa;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Dominio.ITeacher;
import Dominio.IUniversityCode;
import Dominio.Pessoa;
import Dominio.Student;
import Dominio.StudentCurricularPlan;
import Dominio.Teacher;
import Dominio.UniversityCode;
import Util.DegreeCurricularPlanState;
import Util.EnrolmentEvaluationType;
import Util.PeriodState;
import Util.StudentCurricularPlanState;
import Util.TipoCurso;

/**
 *
 * @author  Luis Cruz & Sara Ribeiro
 */
public class PersistentObjectOJBReader extends PersistentObjectOJB {

	public IStudentCurricularPlan readStudentCurricularPlan(Integer studentNumber) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("student.number", studentNumber);
		List result = query(StudentCurricularPlan.class, criteria);
		if (result.size() == 1) {
			return (IStudentCurricularPlan) result.get(0);
		} else {
			return null;
		}
	}

	public IStudent readStudent(Integer studentNumber, TipoCurso degreeType) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("number", studentNumber);
		criteria.addEqualTo("degreeType", degreeType);
		List result = query(Student.class, criteria);
		if (result.size() == 1) {
			return (IStudent) result.get(0);
		} else {
			return null;
		}
	}

	public IDegreeCurricularPlan readDegreeCurricularPlanByDegreeID(Integer degreeID) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("degree.idInternal", degreeID);
		List result = query(DegreeCurricularPlan.class, criteria);
		if (result.size() == 1) {
			return (IDegreeCurricularPlan) result.get(0);
		} else {
			System.out.println("readDegreeCurricularPlan: degree =  " + degreeID + " result.size = " + result.size());
			return null;
		}
	}

	private ICurricularCourse readCurricularCourse(String code, Integer degreeID) {
		// Delete blank space in the beggining of code1
		if (code.charAt(0) == ' ') {
			code = code.substring(1);
		} else if (code.equals("ALG") && degreeID.intValue() == 5) {
			code = "AP9";
		} else if (code.equals("AWX") && degreeID.intValue() == 21) {
			code = "AXK";
		}

		Criteria criteria = new Criteria();
		criteria.addEqualTo("code", code);
		criteria.addEqualTo("degreeCurricularPlan", degreeID);
		List result = query(CurricularCourse.class, criteria);
		if (result.size() == 1) {
			return (ICurricularCourse) result.get(0);
		} else if (result.size() > 1) {
			System.out.println("readCurricularCourse: code = " + code + "  degreeID = " + degreeID + " result.size =" + result.size());
		} else /* if (result.size() == 0) */ {
			System.out.println("readCurricularCourse: code = " + code + "  degreeID = " + degreeID + " result.size =" + result.size());
		}
		return null;
	}

	public ICurricularCourse readCurricularCourse(String name, Integer degreeID, String code) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("name", name);
		criteria.addEqualTo("degreeCurricularPlan", degreeID);
		criteria.addEqualTo("code", code);
		List result = query(CurricularCourse.class, criteria);
		if (result.size() == 1) {
			return (ICurricularCourse) result.get(0);
		} else {
			//			return readCurricularCourse(code, degreeID);
			return null;
		}
	}

	public Almeida_disc readAlmeidaCurricularCourse(String code, long curso) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("coddis", code);
		criteria.addEqualTo("codcur", (new Integer("" + curso)));
		List result = query(Almeida_disc.class, criteria);
		//System.out.println("result.size" + result.size());
		if (result.size() > 0) {
			return (Almeida_disc) result.get(0);
		} else {
			return null;
		}
	}

	/**
	 * @param curricularCourse
	 * @param executionPeriod
	 * @return
	 */
	public IDisciplinaExecucao readExecutionCourse(ICurricularCourse curricularCourse, IExecutionPeriod executionPeriod) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("associatedCurricularCourses.idInternal", ((CurricularCourse) curricularCourse).getIdInternal());
		criteria.addEqualTo("executionPeriod.name", executionPeriod.getName());
		criteria.addEqualTo("executionPeriod.name.executionYear.year", executionPeriod.getExecutionYear().getYear());
		List result = query(DisciplinaExecucao.class, criteria);
		//System.out.println("result.size" + result.size());
		if (result.size() == 1) {
			return (IDisciplinaExecucao) result.get(0);
		} else {
			return null;
		}
	}

	/** 
	 * @return IExecutionPeriod
	 */
	public IExecutionPeriod readActiveExecutionPeriod() {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("state", PeriodState.CURRENT_CODE);
		List result = query(ExecutionPeriod.class, criteria);
		if (result.size() == 1) {
			return (IExecutionPeriod) result.get(0);
		} else {
			return null;
		}
	}

	/** 
		 * @param IExecutionYear
	 * @param Integer
	 * @return IExecutionPeriod
	 */
	public IExecutionPeriod readExecutionPeriodByYearAndSemester(IExecutionYear executionYear, Integer semester) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("executionYear.year", executionYear.getYear());
		criteria.addEqualTo("semester", semester);
		List result = query(ExecutionPeriod.class, criteria);
		if (result.size() == 1) {
			return (IExecutionPeriod) result.get(0);
		} else {
			return null;
		}
	}

	/** 
	 * @param Integer
	 * @return IExecutionYear
	 */
	public IExecutionYear readExecutionYearByYear(String year) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("year", year);
		List result = query(ExecutionYear.class, criteria);
		if (result.size() == 1) {
			return (IExecutionYear) result.get(0);
		} else {
			return null;
		}
	}
	/**
	 * @param curricularCourse
	 * @param executionPeriod
	 * @return
	 */
	public IEnrolment readEnrolment(
		IStudentCurricularPlan studentCurricularPlan,
		ICurricularCourse curricularCourse,
		IExecutionPeriod executionPeriod) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("studentCurricularPlan.internalCode", ((StudentCurricularPlan) studentCurricularPlan).getInternalCode());
		criteria.addEqualTo("curricularCourse.idInternal", ((CurricularCourse) curricularCourse).getIdInternal());
		criteria.addEqualTo("executionPeriod.name", executionPeriod.getName());
		criteria.addEqualTo("executionPeriod.name.executionYear.year", executionPeriod.getExecutionYear().getYear());
		List result = query(Enrolment.class, criteria);
		if (result.size() == 1) {
			return (IEnrolment) result.get(0);
		} else {
			return null;
		}
	}

	/**
	 * @param student
	 * @param disciplinaExecucao
	 * @return
	 */
	public IFrequenta readFrequenta(IStudent student, IDisciplinaExecucao disciplinaExecucao) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("aluno.internalCode", ((Student) student).getIdInternal());
		criteria.addEqualTo("disciplinaExecucao.idInternal", ((DisciplinaExecucao) disciplinaExecucao).getIdInternal());
		criteria.addEqualTo("aluno.internalCode", ((Student) student).getIdInternal());
		criteria.addEqualTo("disciplinaExecucao.idInternal", ((DisciplinaExecucao) disciplinaExecucao).getIdInternal());
		List result = query(Frequenta.class, criteria);
		if (result.size() == 1) {
			return (IFrequenta) result.get(0);
		} else {
			return null;
		}
	}

	public List readAllAlmeidaCurricularCourses() {
		List result = query(Almeida_curricular_course.class, null);
		return result;
	}

	public List readAllAlmeidaDisc() {
		List result = query(Almeida_disc.class, null);
		return result;
	}

	public ICurricularCourse readCurricularCourseByCodeAndNameAndDegreeCurricularPlan(
		String code,
		String name,
		IDegreeCurricularPlan degreeCurricularPlan) {

		Criteria criteria = new Criteria();
		criteria.addEqualTo("code", code);
		criteria.addEqualTo("name", name);
		criteria.addEqualTo("degreeCurricularPlan.idInternal", ((DegreeCurricularPlan) degreeCurricularPlan).getIdInternal());

		List result = query(CurricularCourse.class, criteria);
		if (result.size() == 1) {
			return (ICurricularCourse) result.get(0);
		} else if (result.size() > 1) {
			System.out.println(
				"readCurricularCourseByCodeAndNameAndDegreeCurricularPlan: name = " + name + " result.size = " + result.size());
		}
		return null;
	}

	public IDegreeCurricularPlan readDegreeCurricularPlanByName(String name) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("name", name);
		List result = query(DegreeCurricularPlan.class, criteria);
		if (result.size() == 1) {
			return (IDegreeCurricularPlan) result.get(0);
		} else if (result.size() > 1) {
			System.out.println("readDegreeCurricularPlanByName: name = " + name + " result.size = " + result.size());
		}
		return null;
	}

	public ICurso readDegreeByCode(String code) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("sigla", code);
		List result = query(Curso.class, criteria);
		if (result.size() == 1) {
			return (ICurso) result.get(0);
		} else {
			return null;
		}
	}

	//	public List readAllLEQNewCC() {
	//		List result = query(Leq_new_curricular_course.class, null);
	//		return result;
	//	}

	public ICurricularCourse readCurricularCourseByNameAndDegreeCurricularPlan(
		String name,
		IDegreeCurricularPlan degreeCurricularPlan) {

		Criteria criteria = new Criteria();
		criteria.addEqualTo("name", name);
		criteria.addEqualTo("degreeCurricularPlan.idInternal", ((DegreeCurricularPlan) degreeCurricularPlan).getIdInternal());

		List result = query(CurricularCourse.class, criteria);
		if (result.size() == 1) {
			return (ICurricularCourse) result.get(0);
		} else if (result.size() > 1) {
			System.out.println("readCurricularCourseByNameAndDegreeCurricularPlan: name = " + name + " result.size = " + result.size());
		}
		return null;
	}

	//	public List readAllLEQScopes() {
	//		List result = query(Leq_cc_scope.class, null);
	//		return result;
	//	}

	public ICurricularYear readCurricularYear(Integer key) {

		Criteria criteria = new Criteria();
		criteria.addEqualTo("internalID", key);

		List result = query(CurricularYear.class, criteria);
		if (result.size() == 1) {
			return (ICurricularYear) result.get(0);
		} else if (result.size() > 1) {
			System.out.println("readCurricularYear: key = " + key + " result.size = " + result.size());
		}
		return null;
	}

	public ICurricularSemester readCurricularSemester(Integer semester, ICurricularYear curricularYear) {

		Criteria criteria = new Criteria();
		criteria.addEqualTo("semester", semester);
		criteria.addEqualTo("curricularYear.internalID", curricularYear.getInternalID());

		List result = query(CurricularSemester.class, criteria);
		if (result.size() == 1) {
			return (ICurricularSemester) result.get(0);
		} else if (result.size() > 1) {
			System.out.println("readCurricularSemester: semester = " + semester + " result.size = " + result.size());
		}
		return null;
	}

	public IBranch readBranch(Integer key) {

		Criteria criteria = new Criteria();
		criteria.addEqualTo("internalID", key);

		List result = query(Branch.class, criteria);
		if (result.size() == 1) {
			return (IBranch) result.get(0);
		} else if (result.size() > 1) {
			System.out.println("readBranch: key = " + key + " result.size = " + result.size());
		}
		return null;
	}

	public IStudentCurricularPlan readStudentCurricularPlanByUnique(
		IStudent student,
		IDegreeCurricularPlan degreeCurricularPlan,
		IBranch branch,
		StudentCurricularPlanState studentCurricularPlanState) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("student.idInternal", student.getIdInternal());
		criteria.addEqualTo("degreeCurricularPlan.idInternal", degreeCurricularPlan.getIdInternal());
		criteria.addEqualTo("branch.internalID", branch.getInternalID());
		criteria.addEqualTo("currentState", studentCurricularPlanState);
		List result = query(StudentCurricularPlan.class, criteria);
		if (result.size() == 1) {
			return (IStudentCurricularPlan) result.get(0);
			//		} else {
			//			System.out.println("StudentCurricularPlan: " + result.size());
		}
		return null;
	}

	public IStudentCurricularPlan readActiveStudentCurricularPlan(IStudent student, IDegreeCurricularPlan degreeCurricularPlan) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("student.idInternal", student.getIdInternal());
		criteria.addEqualTo("degreeCurricularPlan.idInternal", degreeCurricularPlan.getIdInternal());
		criteria.addEqualTo("currentState", StudentCurricularPlanState.ACTIVE_OBJ);
		List result = query(StudentCurricularPlan.class, criteria);
		if (result.size() == 1) {
			return (IStudentCurricularPlan) result.get(0);
		} else if (result.size() > 1) {
			System.out.println("readActiveStudentCurricularPlan mais que um");
		}
		return null;

	}

	public ICurricularCourse readCurricularCourseByCodeAndDegreeCurricularPlan(
		String code,
		IDegreeCurricularPlan degreeCurricularPlan) {

		Criteria criteria = new Criteria();
		criteria.addEqualTo("code", code);
		criteria.addEqualTo("degreeCurricularPlan.idInternal", ((DegreeCurricularPlan) degreeCurricularPlan).getIdInternal());

		List result = query(CurricularCourse.class, criteria);
		if (result.size() == 1) {
			return (ICurricularCourse) result.get(0);
		} else if (result.size() > 1) {
			System.out.println("readCurricularCourseByCodeAndDegreeCurricularPlan: code = " + code + " result.size = " + result.size());
		}
		return null;
	}

	public ICurricularCourseScope readCurricularCourseScopeByUnique(
		ICurricularCourse curricularCourse,
		IBranch branch,
		ICurricularSemester curricularSemester,
		Integer executionYear) {

		Criteria criteria = new Criteria();
		criteria.addEqualTo("curricularCourse.idInternal", curricularCourse.getIdInternal());
		criteria.addEqualTo("branch.internalID", branch.getInternalID());
		criteria.addEqualTo("curricularSemester.internalID", curricularSemester.getInternalID());
		criteria.addEqualTo("executionYear", executionYear);

		List result = query(CurricularCourseScope.class, criteria);
		if (result.size() == 1) {
			return (ICurricularCourseScope) result.get(0);
		} else if (result.size() > 1) {
			System.out.println("CurricularCourseScope: " + result.size());
		}
		return null;
	}

	public ICurricularCourseScope readCurricularCourseScopeWithoutSemester(
		ICurricularCourse curricularCourse,
		IBranch branch,
		Integer executionYear) {

		Criteria criteria = new Criteria();
		criteria.addEqualTo("curricularCourse.idInternal", curricularCourse.getIdInternal());
		criteria.addEqualTo("branch.internalID", branch.getInternalID());
		criteria.addEqualTo("executionYear", executionYear);

		List result = query(CurricularCourseScope.class, criteria);
		if (result.size() >= 1) {
			return (ICurricularCourseScope) result.get(0);
		} else if (result.size() < 1) {
			System.out.println("CurricularCourseScope: " + result.size());
		}
		return null;
	}

	public IPessoa readPersonByEmployeeNumber(Integer number) {

		Criteria criteria = new Criteria();
		criteria.addEqualTo("numeroMecanografico", number);

		List result = query(Funcionario.class, criteria);
		if (result.size() == 1) {
			int personKey = ((Funcionario) result.get(0)).getChavePessoa();
			Criteria criteria2 = new Criteria();
			criteria2.addEqualTo("idInternal", new Integer(personKey));
			List result2 = query(Pessoa.class, criteria2);
			if (result2.size() == 1) {
				return (IPessoa) result2.get(0);
			} else if (result2.size() > 1) {
				System.out.println("Pessoa: " + result2.size());
			}
		} else if (result.size() > 1) {
			System.out.println("Funcionario: " + result.size());
		}
		return null;
	}

	public List readAllStudentCurricularPlansFromDegreeCurricularPlan(IDegreeCurricularPlan degreeCurricularPlan) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("degreeCurricularPlan.idInternal", degreeCurricularPlan.getIdInternal());
		return query(StudentCurricularPlan.class, criteria);
	}

	public List readAllEnrolmentsFromStudentCurricularPlan(IStudentCurricularPlan studentCurricularPlan) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("studentCurricularPlan.internalCode", ((StudentCurricularPlan) studentCurricularPlan).getInternalCode());
		return query(Enrolment.class, criteria);
	}

	public List readAllAlmeidaCurrams() {
		return query(Almeida_curram.class, null);
	}

	public IDegreeCurricularPlan readDegreeCurricularPlanByDegreeKeyAndState(
		Integer degreeKey,
		DegreeCurricularPlanState degreeCurricularPlanState) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("degree.idInternal", degreeKey);
		criteria.addEqualTo("state", degreeCurricularPlanState);
		List result = query(DegreeCurricularPlan.class, criteria);
		if (result.size() == 1) {
			return (IDegreeCurricularPlan) result.get(0);
		} else {
			System.out.println("readDegreeCurricularPlanByDegreeKeyAndState: Degree = " + degreeKey + " result.size = " + result.size());
			return null;
		}
	}

	public List readAllAlmeidaEnrolments() {
		return query(Almeida_enrolment.class, null);
	}

	public Almeida_curricular_course readAlmeidaCurricularCourse(String code) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("code", code);
		List result = query(Almeida_curricular_course.class, criteria);
		if (result.size() == 1) {
			return (Almeida_curricular_course) result.get(0);
		} else if (result.size() > 1) {
			System.out.println("readAlmeidaCurricularCourse: code = " + code + " result.size = " + result.size());
		}
		return null;
	}

	public Almeida_disc readAlmeidaDiscByCodeAndName(String code, long curso) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("coddis", code);
		criteria.addEqualTo("codcur", (new Integer("" + curso)));
		List result = query(Almeida_disc.class, criteria);
		if (result.size() == 1) {
			return (Almeida_disc) result.get(0);
		} else {
			System.out.println("readAlmeidaDiscByCodeAndName: coddis = " + code + " codcur =  " + curso + " result.size = " + result.size());
			return null;
		}
	}

	public IBranch readBranchByUnique(String code, IDegreeCurricularPlan degreeCurricularPlan) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("code", code);
		criteria.addEqualTo("degreeCurricularPlan.idInternal", degreeCurricularPlan.getIdInternal());
		List result = query(Branch.class, criteria);
		if (result.size() == 1) {
			return (IBranch) result.get(0);
		} else if (result.size() > 1) {
			System.out.println(
				"readBranchByUnique: code ["
					+ code
					+ "] e plano curricular ["
					+ degreeCurricularPlan.getIdInternal()
					+ "] result.size = "
					+ result.size());
		}
		return null;
	}

	//	public Almeida_disc readAlmeidaDiscByUnique(String codigoDisciplina, long longCodigoCurso, long longCodigoRamo, long longSemestre, long executionYear, long longAnoCurricular, long longOrientacao, double doubleCreditos, long tipo, double doubleTeorica, double doubleTeoricopratica, double doublePratica, double doubleLaboratorio) {
	//		Criteria criteria = new Criteria();
	//		criteria.addEqualTo("coddis", codigoDisciplina);
	//		criteria.addEqualTo("codcur", (new Integer("" + longCodigoCurso)));
	//		criteria.addEqualTo("codRam", (new Integer("" + longCodigoRamo)));
	//		criteria.addEqualTo("semDis", (new Integer("" + longSemestre)));
	//		criteria.addEqualTo("anoLectivo", (new Integer("" + executionYear)));
	//		criteria.addEqualTo("anodis", (new Integer("" + longAnoCurricular)));
	//		criteria.addEqualTo("orientation", (new Integer("" + longOrientacao)));
	//		criteria.addEqualTo("credits", (new Double(doubleCreditos)));
	//		criteria.addEqualTo("tipo", (new Integer("" + tipo)));
	//		criteria.addEqualTo("teo", (new Double(doubleTeorica)));
	//		criteria.addEqualTo("teopra", (new Double(doubleTeoricopratica)));
	//		criteria.addEqualTo("pra", (new Double(doublePratica)));
	//		criteria.addEqualTo("lab", (new Double(doubleLaboratorio)));
	//
	//		List result = query(Almeida_disc.class, criteria);
	//		if (result.size() == 1) {
	//			return (Almeida_disc) result.get(0);
	//		} else if (result.size() > 1) {
	//			System.out.println("readAlmeidaDiscByUnique: coddis = " + codigoDisciplina + "result.size = " + result.size());
	//		}
	//		return null;
	//	}

	public Almeida_curram readAlmeidaCurramByUnique(long curso, long ramo, long orientacao) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("codcur", (new Integer("" + curso)));
		criteria.addEqualTo("codram", (new Integer("" + ramo)));
		criteria.addEqualTo("codorien", (new Integer("" + orientacao)));
		List result = query(Almeida_curram.class, criteria);
		if (result.size() == 1) {
			return (Almeida_curram) result.get(0);
		} else if (result.size() > 1) {
			System.out.println(
				"readAlmeidaCurramByUnique:"
					+ " codcur = "
					+ curso
					+ " codRam =  "
					+ ramo
					+ " codorien =  "
					+ orientacao
					+ " result.size = "
					+ result.size());
		}
		return null;
	}

	public Almeida_disc readAlmeidaDiscByUnique(
		String codigoDisciplina,
		long longCodigoCurso,
		long longCodigoRamo,
		long longSemestre,
		long executionYear,
		long longAnoCurricular,
		long longOrientacao) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("coddis", codigoDisciplina);
		criteria.addEqualTo("codcur", (new Integer("" + longCodigoCurso)));
		criteria.addEqualTo("codRam", (new Integer("" + longCodigoRamo)));
		criteria.addEqualTo("semDis", (new Integer("" + longSemestre)));
		criteria.addEqualTo("anoLectivo", (new Integer("" + executionYear)));
		criteria.addEqualTo("anodis", (new Integer("" + longAnoCurricular)));
		criteria.addEqualTo("orientation", (new Integer("" + longOrientacao)));

		List result = query(Almeida_disc.class, criteria);
		if (result.size() == 1) {
			return (Almeida_disc) result.get(0);
		} else if (result.size() > 1) {
			System.out.println("readAlmeidaDiscByUnique: coddis = " + codigoDisciplina + "result.size = " + result.size());
		}
		return null;
	}

	public IEnrolment readEnrolmentByUnique(IStudentCurricularPlan plan, ICurricularCourseScope scope, IExecutionPeriod period) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("studentCurricularPlan.internalCode", ((StudentCurricularPlan) plan).getInternalCode());
		criteria.addEqualTo("curricularCourseScope.idInternal", scope.getIdInternal());
		criteria.addEqualTo("executionPeriod.idInternal", period.getIdInternal());
		List result = query(Enrolment.class, criteria);
		if (result.size() == 1) {
			return (IEnrolment) result.get(0);
		} else if (result.size() > 1) {
			System.out.println("readEnrolmentByUnique mais que um\n");
		}
		return null;
	}

	public IEnrolment readEnrolmentByStudentCurricularPlanAndScope(IStudentCurricularPlan plan, ICurricularCourseScope scope) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("studentCurricularPlan.internalCode", ((StudentCurricularPlan) plan).getInternalCode());
		criteria.addEqualTo("curricularCourseScope.idInternal", scope.getIdInternal());
		List result = query(Enrolment.class, criteria);
		if (result.size() == 1) {
			return (IEnrolment) result.get(0);
		} else if (result.size() > 1) {
			System.out.println("readEnrolmentByStudentCurricularPlanAndScope mais que um\n");
		}
		return null;
	}

	public IEnrolmentEvaluation readEnrolmentEvaluationByUnique(IEnrolment enrolment, EnrolmentEvaluationType type, String grade) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("enrolment.idInternal", enrolment.getIdInternal());
		criteria.addEqualTo("enrolmentEvaluationType", type);
		criteria.addEqualTo("grade", grade);
		List result = query(EnrolmentEvaluation.class, criteria);
		if (result.size() == 1) {
			return (IEnrolmentEvaluation) result.get(0);
		} else if (result.size() > 1) {
			System.out.println("readEnrolmentByUnique mais que um\n");
		}
		return null;
	}

	public List readAllCurricularCourses() {
		return query(CurricularCourse.class, null);
	}

	public List readAllEnrolments() {
		return query(Enrolment.class, null);
	}

	public ICurricularCourseEquivalence readCurricularCourseEquivalence(ICurricularCourse curricularCourse) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("curricularCourse.idInternal", curricularCourse.getIdInternal());
		List result = query(CurricularCourseEquivalence.class, criteria);
		if (result.size() == 1) {
			return (ICurricularCourseEquivalence) result.get(0);
		} else if (result.size() > 1) {
			System.out.println("readCurricularCourseEquivalenceByUnique mais que um\n");
		}
		return null;
	}

	public ICurricularCourseEquivalence readCurricularCourseEquivalenceByCurricularCourse(ICurricularCourse curricularCourse) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("curricularCourse.idInternal", curricularCourse.getIdInternal());
		List result = query(CurricularCourseEquivalence.class, criteria);
		if (result.size() == 1) {
			return (ICurricularCourseEquivalence) result.get(0);
		} else if (result.size() > 1) {
			System.out.println("readCurricularCourseEquivalenceByCurricularCourse mais que um\n");
		}
		return null;
	}

	public IEnrolmentEquivalence readEnrolmentEquivalenceByUnique(IEnrolment enrolment) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("enrolment.idInternal", enrolment.getIdInternal());
		List result = query(EnrolmentEquivalence.class, criteria);
		if (result.size() == 1) {
			return (IEnrolmentEquivalence) result.get(0);
		} else if (result.size() > 1) {
			System.out.println("readEnrolmentEquivalenceByUnique mais que um\n");
		}
		return null;
	}

	public IEnrolment readEnrolmentByID(int id) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("idInternal", new Integer(id));
		List result = query(Enrolment.class, criteria);
		if (result.size() == 1) {
			return (IEnrolment) result.get(0);
		} else if (result.size() > 1) {
			System.out.println("readEnrolmentByID mais que um\n");
		}
		return null;
	}

	public IUniversityCode readUniversityCodeByCode(String universityCodeRead) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("universityCode", universityCodeRead);
		List result = query(UniversityCode.class, criteria);
		if (result.size() == 1) {
			return (IUniversityCode) result.get(0);
		} else if (result.size() > 1) {
			System.out.println("readUniversityCodeByCode mais que um\n");
		}
		return null;
	}

	public ICurricularCourseEquivalenceRestricition readCurricularCourseEquivalenceRestrictionByUnique(
		ICurricularCourse equivalentCurricularCourse,
		ICurricularCourseEquivalence curricularCourseEquivalence) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("equivalentCurricularCourse.idInternal", equivalentCurricularCourse.getIdInternal());
		criteria.addEqualTo("curricularCourseEquivalence.idInternal", curricularCourseEquivalence.getIdInternal());
		List result = query(CurricularCourseEquivalenceRestriction.class, criteria);
		if (result.size() == 1) {
			return (ICurricularCourseEquivalenceRestricition) result.get(0);
		} else if (result.size() > 1) {
			System.out.println("readCurricularCourseEquivalenceRestrictionByUnique mais que um\n");
		}
		return null;
	}

	public List readAllCurricularCourseEquivalences() {
		Query query = new QueryByCriteria(CurricularCourseEquivalence.class, null);
		return (List) broker.getCollectionByQuery(query);
	}

	public List readEnrolmentsByCurricularCourse(ICurricularCourse curricularCourseEnroled) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("curricularCourseScope.curricularCourse.idInternal", curricularCourseEnroled.getIdInternal());
		List result = query(Enrolment.class, criteria);
		if (result.size() < 0) {
			return null;
		}
		return result;
	}

	public IEnrolmentEquivalenceRestriction readEnrolmentEquivalenceRestrictionByUnique(
		IEnrolment enrolment,
		IEnrolmentEquivalence enrolmentEquivalence) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("enrolmentEquivalence.idInternal", enrolmentEquivalence.getIdInternal());
		criteria.addEqualTo("equivalentEnrolment.idInternal", enrolment.getIdInternal());
		List result = query(EnrolmentEquivalenceRestriction.class, criteria);
		if (result.size() == 1) {
			return (IEnrolmentEquivalenceRestriction) result.get(0);
		} else if (result.size() > 1) {
			System.out.println("IEnrolmentEquivalenceRestriction mais que um\n");
		}
		return null;
	}

	public ITeacher readTeacher(Integer teacherNumber) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("teacherNumber", teacherNumber);

		List result = query(Teacher.class, criteria);
		if (result.size() == 1) {
			return (ITeacher) result.get(0);
		} else {
			return null;
		}
	}
}