package ServidorPersistente.OJB;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.odmg.QueryException;

import Dominio.Equivalence;
import Dominio.IEnrolment;
import Dominio.IEquivalence;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEquivalence;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author dcs-rjao
 *
 * 24/Mar/2003
 */

public class EquivalenceOJB extends ObjectFenixOJB implements IPersistentEquivalence {

	public void deleteAll() throws ExcepcaoPersistencia {
		try {
			String oqlQuery = "select all from " + Equivalence.class.getName();
			super.deleteAll(oqlQuery);
		} catch (ExcepcaoPersistencia ex) {
			throw ex;
		}
	}

	public void lockWrite(IEquivalence equivalenceToWrite) throws ExcepcaoPersistencia, ExistingPersistentException {

		IEquivalence equivalenceFromDB = null;

		// If there is nothing to write, simply return.
		if (equivalenceToWrite == null) {
			return;
		}

		// Read Equivalence from database.
		equivalenceFromDB = this.readEquivalenceByEnrolmentAndEquivalentEnrolment(equivalenceToWrite.getEnrolment(), equivalenceToWrite.getEquivalentEnrolment());

		// If Equivalence is not in database, then write it.
		if (equivalenceFromDB == null) {
			super.lockWrite(equivalenceToWrite);
			// else If the Equivalence is mapped to the database, then write any existing changes.
		} else if ((equivalenceToWrite instanceof Equivalence) && ((Equivalence) equivalenceFromDB).getInternalID().equals(((Equivalence) equivalenceToWrite).getInternalID())) {
			super.lockWrite(equivalenceToWrite);
			// else Throw an already existing exception
		} else
			throw new ExistingPersistentException();
	}

	public void delete(IEquivalence enrolment) throws ExcepcaoPersistencia {
		try {
			super.delete(enrolment);
		} catch (ExcepcaoPersistencia ex) {
			throw ex;
		}
	}

	public IEquivalence readEquivalenceByEnrolmentAndEquivalentEnrolment(IEnrolment enrolment, IEnrolment equivalentEnrolment) throws ExcepcaoPersistencia {

		try {
			IEquivalence equivalence = null;
			String oqlQuery = "select all from " + Equivalence.class.getName();
			oqlQuery += " where enrolment.studentCurricularPlan.student.number = $1";
			oqlQuery += " and enrolment.studentCurricularPlan.student.degreeType = $2";
			oqlQuery += " and enrolment.studentCurricularPlan.currentState = $3";

			oqlQuery += " and enrolment.curricularCourseScope.curricularCourse.name = $4";
			oqlQuery += " and enrolment.curricularCourseScope.curricularCourse.code = $5";
			oqlQuery += " and enrolment.curricularCourseScope.curricularCourse.degreeCurricularPlan.name = $6";
			oqlQuery += " and enrolment.curricularCourseScope.curricularCourse.degreeCurricularPlan.degree.nome = $7";
			oqlQuery += " and enrolment.curricularCourseScope.curricularCourse.degreeCurricularPlan.degree.sigla = $8";
			oqlQuery += " and enrolment.curricularCourseScope.curricularSemester.semester = $9";
			oqlQuery += " and enrolment.curricularCourseScope.curricularSemester.curricularYear.year = $10";
			oqlQuery += " and enrolment.curricularCourseScope.branch.degreeCurricularPlan.name = $11";
			oqlQuery += " and enrolment.curricularCourseScope.branch.degreeCurricularPlan.degree.nome = $12";
			oqlQuery += " and enrolment.curricularCourseScope.branch.degreeCurricularPlan.degree.sigla = $13";
			oqlQuery += " and enrolment.curricularCourseScope.branch.code = $14";

			oqlQuery += " and enrolment.executionPeriod.name = $15";
			oqlQuery += " and enrolment.executionPeriod.executionYear.year = $16";

			oqlQuery += " and equivalentEnrolment.studentCurricularPlan.student.number = $17";
			oqlQuery += " and equivalentEnrolment.studentCurricularPlan.student.degreeType = $18";
			oqlQuery += " and equivalentEnrolment.studentCurricularPlan.currentState = $19";

			oqlQuery += " and equivalentEnrolment.curricularCourseScope.curricularCourse.name = $20";
			oqlQuery += " and equivalentEnrolment.curricularCourseScope.curricularCourse.code = $21";
			oqlQuery += " and equivalentEnrolment.curricularCourseScope.curricularCourse.degreeCurricularPlan.name = $22";
			oqlQuery += " and equivalentEnrolment.curricularCourseScope.curricularCourse.degreeCurricularPlan.degree.nome = $23";
			oqlQuery += " and equivalentEnrolment.curricularCourseScope.curricularCourse.degreeCurricularPlan.degree.sigla = $24";
			oqlQuery += " and equivalentEnrolment.curricularCourseScope.curricularSemester.semester = $25";
			oqlQuery += " and equivalentEnrolment.curricularCourseScope.curricularSemester.curricularYear.year = $26";
			oqlQuery += " and equivalentEnrolment.curricularCourseScope.branch.degreeCurricularPlan.name = $27";
			oqlQuery += " and equivalentEnrolment.curricularCourseScope.branch.degreeCurricularPlan.degree.nome = $28";
			oqlQuery += " and equivalentEnrolment.curricularCourseScope.branch.degreeCurricularPlan.degree.sigla = $29";
			oqlQuery += " and equivalentEnrolment.curricularCourseScope.branch.code = $30";

			oqlQuery += " and equivalentEnrolment.executionPeriod.name = $31";
			oqlQuery += " and equivalentEnrolment.executionPeriod.executionYear.year = $32";

			query.create(oqlQuery);

			query.bind(enrolment.getStudentCurricularPlan().getStudent().getNumber());
			query.bind(enrolment.getStudentCurricularPlan().getStudent().getDegreeType());
			query.bind(enrolment.getStudentCurricularPlan().getCurrentState());

			query.bind(enrolment.getCurricularCourseScope().getCurricularCourse().getName());
			query.bind(enrolment.getCurricularCourseScope().getCurricularCourse().getCode());
			query.bind(enrolment.getCurricularCourseScope().getCurricularCourse().getDegreeCurricularPlan().getName());
			query.bind(enrolment.getCurricularCourseScope().getCurricularCourse().getDegreeCurricularPlan().getDegree().getNome());
			query.bind(enrolment.getCurricularCourseScope().getCurricularCourse().getDegreeCurricularPlan().getDegree().getSigla());
			query.bind(enrolment.getCurricularCourseScope().getCurricularSemester().getSemester());
			query.bind(enrolment.getCurricularCourseScope().getCurricularSemester().getCurricularYear().getYear());
			query.bind(enrolment.getCurricularCourseScope().getBranch().getDegreeCurricularPlan().getName());
			query.bind(enrolment.getCurricularCourseScope().getBranch().getDegreeCurricularPlan().getDegree().getNome());
			query.bind(enrolment.getCurricularCourseScope().getBranch().getDegreeCurricularPlan().getDegree().getSigla());
			query.bind(enrolment.getCurricularCourseScope().getBranch().getCode());

			query.bind(enrolment.getExecutionPeriod().getName());
			query.bind(enrolment.getExecutionPeriod().getExecutionYear().getYear());


			query.bind(equivalentEnrolment.getStudentCurricularPlan().getStudent().getNumber());
			query.bind(equivalentEnrolment.getStudentCurricularPlan().getStudent().getDegreeType());
			query.bind(equivalentEnrolment.getStudentCurricularPlan().getCurrentState());

			query.bind(equivalentEnrolment.getCurricularCourseScope().getCurricularCourse().getName());
			query.bind(equivalentEnrolment.getCurricularCourseScope().getCurricularCourse().getCode());
			query.bind(equivalentEnrolment.getCurricularCourseScope().getCurricularCourse().getDegreeCurricularPlan().getName());
			query.bind(equivalentEnrolment.getCurricularCourseScope().getCurricularCourse().getDegreeCurricularPlan().getDegree().getNome());
			query.bind(equivalentEnrolment.getCurricularCourseScope().getCurricularCourse().getDegreeCurricularPlan().getDegree().getSigla());
			query.bind(equivalentEnrolment.getCurricularCourseScope().getCurricularSemester().getSemester());
			query.bind(equivalentEnrolment.getCurricularCourseScope().getCurricularSemester().getCurricularYear().getYear());
			query.bind(equivalentEnrolment.getCurricularCourseScope().getBranch().getDegreeCurricularPlan().getName());
			query.bind(equivalentEnrolment.getCurricularCourseScope().getBranch().getDegreeCurricularPlan().getDegree().getNome());
			query.bind(equivalentEnrolment.getCurricularCourseScope().getBranch().getDegreeCurricularPlan().getDegree().getSigla());
			query.bind(equivalentEnrolment.getCurricularCourseScope().getBranch().getCode());

			query.bind(equivalentEnrolment.getExecutionPeriod().getName());
			query.bind(equivalentEnrolment.getExecutionPeriod().getExecutionYear().getYear());

			List result = (List) query.execute();
			try {
				lockRead(result);
			} catch (ExcepcaoPersistencia ex) {
				throw ex;
			}

			if ((result != null) && (result.size() != 0)) {
				equivalence = (IEquivalence) result.get(0);
			}
			return equivalence;

		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public ArrayList readAll() throws ExcepcaoPersistencia {

		try {
			ArrayList list = new ArrayList();
			String oqlQuery = "select all from " + Equivalence.class.getName();
			query.create(oqlQuery);
			List result = (List) query.execute();

			try {
				lockRead(result);
			} catch (ExcepcaoPersistencia ex) {
				throw ex;
			}

			if ((result != null) && (result.size() != 0)) {
				ListIterator iterator = result.listIterator();
				while (iterator.hasNext())
					list.add((IEquivalence) iterator.next());
			}
			return list;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

}