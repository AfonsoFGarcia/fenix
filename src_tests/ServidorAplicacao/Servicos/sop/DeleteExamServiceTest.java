/*
 * ApagarAulaServicosTest.java
 * JUnit based test
 *
 * Created on 26 de Outubro de 2002, 15:07
 */

package ServidorAplicacao.Servicos.sop;

/**
 *
 * @author tfc130
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoExam;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionYear;
import DataBeans.InfoViewExamByDayAndShift;
import ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices;
import Util.Season;

public class DeleteExamServiceTest extends TestCaseDeleteAndEditServices {

	public DeleteExamServiceTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(DeleteExamServiceTest.class);

		return suite;
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}

	protected String getNameOfServiceToBeTested() {
		return "DeleteExam";
	}

	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {

		InfoExecutionCourse infoExecutionCourse = new InfoExecutionCourse(
			"Engenharia da Programa��o",
			"EP",
			"blob",
			new Double(1),
			new Double(0),
			new Double(0),
			new Double(0),
			new InfoExecutionPeriod("2� semestre",new InfoExecutionYear("2002/2003")));

		Calendar beginning = Calendar.getInstance();
		beginning.set(Calendar.YEAR, 2003);
		beginning.set(Calendar.MONTH, Calendar.MARCH);
		beginning.set(Calendar.DAY_OF_MONTH, 19);
		beginning.set(Calendar.HOUR_OF_DAY, 9);
		beginning.set(Calendar.MINUTE, 0);
		beginning.set(Calendar.SECOND, 0);
		Calendar end = Calendar.getInstance();
		end.set(Calendar.YEAR, 2003);
		end.set(Calendar.MONTH, Calendar.MARCH);
		end.set(Calendar.DAY_OF_MONTH, 19);
		end.set(Calendar.HOUR_OF_DAY, 12);
		end.set(Calendar.MINUTE, 0);
		end.set(Calendar.SECOND, 0);
		Season season = new Season(Season.SEASON1);


		InfoExam infoExam = new InfoExam(beginning.getTime(), beginning, null, season);
		List infoExecutionCourses = new ArrayList();
		infoExecutionCourses.add(infoExecutionCourse);

		InfoViewExamByDayAndShift infoViewExam = new InfoViewExamByDayAndShift();
		infoViewExam.setInfoExam(infoExam);
		infoViewExam.setInfoExecutionCourses(infoExecutionCourses);

		Object argsDeleteExam[] = new Object[1];
		argsDeleteExam[0] = infoViewExam;

		return argsDeleteExam;
	}

	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {

		InfoExecutionCourse infoExecutionCourse = new InfoExecutionCourse(
			"Unexisting Course",
			"UC",
			"blob",
			new Double(1),
			new Double(0),
			new Double(0),
			new Double(0),
			new InfoExecutionPeriod("2� semestre",new InfoExecutionYear("2002/2003")));

		Calendar beginning = Calendar.getInstance();
		beginning.set(Calendar.YEAR, 2003);
		beginning.set(Calendar.MONTH, Calendar.MARCH);
		beginning.set(Calendar.DAY_OF_MONTH, 19);
		beginning.set(Calendar.HOUR_OF_DAY, 9);
		beginning.set(Calendar.MINUTE, 0);
		beginning.set(Calendar.SECOND, 0);
		Calendar end = Calendar.getInstance();
		end.set(Calendar.YEAR, 2003);
		end.set(Calendar.MONTH, Calendar.MARCH);
		end.set(Calendar.DAY_OF_MONTH, 19);
		end.set(Calendar.HOUR_OF_DAY, 12);
		end.set(Calendar.MINUTE, 0);
		end.set(Calendar.SECOND, 0);
		Season season = new Season(Season.SEASON1);

		Object argsDeleteExam[] = new Object[1];
		argsDeleteExam[0] = new InfoExam(beginning.getTime(), beginning, null, season);

		return argsDeleteExam;
	}
}