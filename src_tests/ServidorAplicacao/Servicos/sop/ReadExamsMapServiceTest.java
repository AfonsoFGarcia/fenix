/*
 * ReadExamsMapServiceTest.java
 *
 * Created on 2003/04/02
 */

package ServidorAplicacao.Servicos.sop;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 */
import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoDegree;
import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.InfoExamsMap;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionYear;
import ServidorAplicacao.Servicos.TestCaseReadServices;

public class ReadExamsMapServiceTest
	extends TestCaseReadServices {
		
		InfoDegree infoDegree = null;
		InfoDegreeCurricularPlan infoDegreeCurricularPlan = null;
		InfoExecutionYear infoExecutionYear = null;
		InfoExecutionPeriod infoExecutionPeriod = null;
		InfoExecutionDegree infoExecutionDegree = null;
		List curricularYears = null;
		
	public ReadExamsMapServiceTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite =
			new TestSuite(ReadExamsMapServiceTest.class);

		return suite;
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseReadServices#getNameOfServiceToBeTested()
	 */
	protected String getNameOfServiceToBeTested() {
		return "ReadExamsMap";
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseReadServices#getArgumentsOfServiceToBeTestedUnsuccessfuly()
	 */
	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
		// Service always returns a result.
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseReadServices#getArgumentsOfServiceToBeTestedSuccessfuly()
	 */
	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
		infoDegree = new InfoDegree("LEIC", "Licenciatura de Engenharia Informatica e de Computadores");
		infoDegreeCurricularPlan = new InfoDegreeCurricularPlan("plano1", infoDegree);
		infoExecutionYear = new InfoExecutionYear("2002/2003");
		infoExecutionPeriod = new InfoExecutionPeriod("2� Semestre", infoExecutionYear);
		infoExecutionDegree = new InfoExecutionDegree(infoDegreeCurricularPlan, infoExecutionYear);
		curricularYears = new ArrayList();
		curricularYears.add(new Integer(1));
		curricularYears.add(new Integer(3));
		curricularYears.add(new Integer(5));

		Object[] result = { infoExecutionDegree, curricularYears, infoExecutionPeriod };
		return result;
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseReadServices#getNumberOfItemsToRetrieve()
	 */
	protected int getNumberOfItemsToRetrieve() {
		return 1;
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseReadServices#getObjectToCompare()
	 */
	protected Object getObjectToCompare() {
		InfoExamsMap infoExamsMap = new InfoExamsMap();
		infoExamsMap.setCurricularYears(curricularYears);
		List infoExecutionCourses = new ArrayList();
		infoExecutionCourses.add("1");
		infoExecutionCourses.add("2");
		infoExamsMap.setExecutionCourses(infoExecutionCourses);
		
		return infoExamsMap;
	}

	protected boolean needsAuthorization() {
		return true;
	}

}
