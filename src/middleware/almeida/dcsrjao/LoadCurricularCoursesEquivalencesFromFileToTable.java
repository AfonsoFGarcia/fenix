package middleware.almeida.dcsrjao;

import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import middleware.almeida.PersistentObjectOJBReader;
import Dominio.CurricularCourseEquivalence;
import Dominio.CurricularCourseEquivalenceRestriction;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseEquivalence;
import Dominio.ICurricularCourseEquivalenceRestricition;
import Dominio.IDegreeCurricularPlan;

/**
 *
 * @author  Luis Cruz & Sara Ribeiro
 */

public class LoadCurricularCoursesEquivalencesFromFileToTable extends LoadAlmeidaDataToTable {

	private static LoadCurricularCoursesEquivalencesFromFileToTable loader = null;
	private static String logString = "";
	private static final String ONE_SPACE = " ";
	private IDegreeCurricularPlan oldDegreeCurricularPlan = null;
	private IDegreeCurricularPlan newDegreeCurricularPlan = null;

	public LoadCurricularCoursesEquivalencesFromFileToTable() {
	}

	public static void main(String[] args) {
		if (loader == null) {
			loader = new LoadCurricularCoursesEquivalencesFromFileToTable();
		}

		loader.persistentObjectOJB = new PersistentObjectOJBReader();
		loader.setupDAO();
		loader.oldDegreeCurricularPlan = loader.persistentObjectOJB.readDegreeCurricularPlanByName(InfoForMigration.NAME_OF_OLD_DEGREE_CURRICULAR_PLAN);
		if (loader.oldDegreeCurricularPlan == null) {
			logString = "o plano curricular " + InfoForMigration.NAME_OF_OLD_DEGREE_CURRICULAR_PLAN + " n�o existe!";
			logString = loader.report(logString);
			loader.writeToFile(logString);
			loader.shutdownDAO();
			return;
		}

		loader.newDegreeCurricularPlan = loader.persistentObjectOJB.readDegreeCurricularPlanByName(InfoForMigration.NAME_OF_NEW_DEGREE_CURRICULAR_PLAN);
		if (loader.newDegreeCurricularPlan == null) {
			logString = "o plano curricular " + InfoForMigration.NAME_OF_NEW_DEGREE_CURRICULAR_PLAN + " n�o existe!";
			logString = loader.report(logString);
			loader.writeToFile(logString);
			loader.shutdownDAO();
			return;
		}
		loader.shutdownDAO();

		System.out.println("\nMigrating " + loader.getClassName());
		loader.load();
		logString = loader.report(logString);
		loader.writeToFile(logString);
	}

	protected void processLine(String line) {
		loader.printLine(getClassName());
		StringTokenizer stringTokenizer = new StringTokenizer(line, getFieldSeparator());

		String oldCourse1 = stringTokenizer.nextToken();
		String oldCourse2 = "";
		if (oldCourse1.startsWith("+")) {
			oldCourse2 = stringTokenizer.nextToken();
			oldCourse1 = oldCourse1.substring(1);
		}
		String newCourse = stringTokenizer.nextToken();

		List oldCurricularCourse1List = persistentObjectOJB.readListOfCurricularCoursesByNameAndDegreCurricularPlan(oldCourse1, this.oldDegreeCurricularPlan);
		if (oldCurricularCourse1List == null) {
			loader.numberUntreatableElements++;
			logString += "\nERRO: Na linha ["
				+ (numberLinesProcessed + 1)
				+ "] o curricular course "
				+ oldCourse1
				+ " do plano "
				+ this.oldDegreeCurricularPlan.getName()
				+ " n�o existe!";
			return;
		}

		List oldCurricularCourse2List = null;
		if (!oldCourse2.equals("")) {
			oldCurricularCourse2List = persistentObjectOJB.readListOfCurricularCoursesByNameAndDegreCurricularPlan(oldCourse2, this.oldDegreeCurricularPlan);
			if (oldCurricularCourse2List == null) {
				loader.numberUntreatableElements++;
				logString += "\nERRO: Na linha ["
					+ (numberLinesProcessed + 1)
					+ "] o curricular course "
					+ oldCourse2
					+ " do plano "
					+ this.oldDegreeCurricularPlan.getName()
					+ " n�o existe!";
				return;
			}
		}

		ICurricularCourse newCurricularCourse = persistentObjectOJB.readCurricularCourseByNameAndDegreeCurricularPlan(newCourse, this.newDegreeCurricularPlan);
		if (newCurricularCourse == null) {
			loader.numberUntreatableElements++;
			logString += "\nERRO: Na linha ["
				+ (numberLinesProcessed + 1)
				+ "] o curricular course "
				+ newCourse
				+ " do plano "
				+ this.newDegreeCurricularPlan.getName()
				+ " n�o existe!";
			return;
		}

		Iterator iterator1 = oldCurricularCourse1List.iterator();
		ICurricularCourse oldCurricularCourse1 = null;
		ICurricularCourseEquivalence curricularCourseEquivalence = null;
		while (iterator1.hasNext()) {
			curricularCourseEquivalence = new CurricularCourseEquivalence();
			curricularCourseEquivalence.setCurricularCourse(newCurricularCourse);
			writeElement(curricularCourseEquivalence);

			oldCurricularCourse1 = (ICurricularCourse) iterator1.next();
			ICurricularCourseEquivalenceRestricition curricularCourseEquivalenceRestricition1 =
				persistentObjectOJB.readCurricularCourseEquivalenceRestrictionByUnique(oldCurricularCourse1, curricularCourseEquivalence);
			if (curricularCourseEquivalenceRestricition1 == null) {
				curricularCourseEquivalenceRestricition1 = new CurricularCourseEquivalenceRestriction();
				curricularCourseEquivalenceRestricition1.setCurricularCourseEquivalence(curricularCourseEquivalence);
				curricularCourseEquivalenceRestricition1.setEquivalentCurricularCourse(oldCurricularCourse1);
				writeElement(curricularCourseEquivalenceRestricition1);
				numberElementsWritten--;
			} else {
				logString += "\nERRO: Na linha ["
					+ (numberLinesProcessed + 1)
					+ "] j� existe uma equivalencia entre "
					+ oldCurricularCourse1.getName()
					+ " e "
					+ curricularCourseEquivalence.getCurricularCourse().getName();
			}

			if (oldCurricularCourse2List != null) {
				Iterator iterator2 = oldCurricularCourse2List.iterator();
				ICurricularCourse oldCurricularCourse2 = null;
				while (iterator2.hasNext()) {
					oldCurricularCourse2 = (ICurricularCourse) iterator2.next();
					ICurricularCourseEquivalenceRestricition curricularCourseEquivalenceRestricition2 =
						persistentObjectOJB.readCurricularCourseEquivalenceRestrictionByUnique(oldCurricularCourse2, curricularCourseEquivalence);
					if (curricularCourseEquivalenceRestricition2 == null) {
						curricularCourseEquivalenceRestricition2 = new CurricularCourseEquivalenceRestriction();
						curricularCourseEquivalenceRestricition2.setCurricularCourseEquivalence(curricularCourseEquivalence);
						curricularCourseEquivalenceRestricition2.setEquivalentCurricularCourse(oldCurricularCourse2);
						writeElement(curricularCourseEquivalenceRestricition2);
						numberElementsWritten--;
					} else {
						logString += "\nERRO: Na linha ["
							+ (numberLinesProcessed + 1)
							+ "] j� existe uma equivalencia entre "
							+ oldCurricularCourse2.getName()
							+ " e "
							+ curricularCourseEquivalence.getCurricularCourse().getName();
					}

					if (iterator2.hasNext()) {
						curricularCourseEquivalence = new CurricularCourseEquivalence();
						curricularCourseEquivalence.setCurricularCourse(newCurricularCourse);
						writeElement(curricularCourseEquivalence);
						
						curricularCourseEquivalenceRestricition1 = new CurricularCourseEquivalenceRestriction();
						curricularCourseEquivalenceRestricition1.setCurricularCourseEquivalence(curricularCourseEquivalence);
						curricularCourseEquivalenceRestricition1.setEquivalentCurricularCourse(oldCurricularCourse1);
						writeElement(curricularCourseEquivalenceRestricition1);
						numberElementsWritten--;
					}
				}
			}
		}
	}

	protected String getFilename() {
		return "etc/migration/dcs-rjao/almeidaLEQData/equivalenciasLEQ.txt";
	}

	protected String getFieldSeparator() {
		return "\t";
	}

	protected String getFilenameOutput() {
		return "etc/migration/dcs-rjao/logs/LoadCurricularCoursesEquivalencesFromFileToTable.txt";
	}

	protected String getClassName() {
		return "LoadCurricularCoursesEquivalencesFromFileToTable";
	}
}