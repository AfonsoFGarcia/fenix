/*
 * EditarAulaServicosTest.java
 * JUnit based test
 *
 * Created on 27 de Outubro de 2002, 19:17
 */

package ServidorAplicacao.Servicos.sop;

/**
 *
 * @author tfc130
 */
import java.util.Calendar;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoDegree;
import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionYear;
import DataBeans.InfoLesson;
import DataBeans.InfoLessonServiceResult;
import DataBeans.InfoRoom;
import DataBeans.KeyLesson;
import DataBeans.RoomKey;
import ServidorAplicacao.Servicos.TestCaseServicosWithAuthorization;
import Util.DiaSemana;
import Util.TipoAula;
import Util.TipoSala;

public class EditarAulaServicosTest extends TestCaseServicosWithAuthorization {
	public EditarAulaServicosTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(EditarAulaServicosTest.class);

		return suite;
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}

	// edit new existing aula
	public void testEditExistingAula() {
		InfoRoom infoSala =
			new InfoRoom(
				"Ga1",
				"Pavilhao Central",
				new Integer(0),
				new TipoSala(1),
				new Integer(100),
				new Integer(50));
		InfoDegree infoLicenciatura =
			new InfoDegree(
				"LEIC",
				"Licenciatura de Engenharia Informatica e de Computadores");
		InfoExecutionYear infoExecutionYear =
			new InfoExecutionYear("2002/2003");
		InfoExecutionPeriod infoExecutionPeriod =
			new InfoExecutionPeriod("2� Semestre", infoExecutionYear);
		InfoDegreeCurricularPlan curricularPlan =
			new InfoDegreeCurricularPlan("plano1", infoLicenciatura);

		InfoExecutionDegree infoLicenciaturaExecucao =
			new InfoExecutionDegree(curricularPlan, infoExecutionYear);
		InfoExecutionCourse iDE =
			new InfoExecutionCourse(
				"Trabalho Final de Curso I",
				"TFCI",
				"programa1",
				new Double(0),
				new Double(0),
				new Double(0),
				new Double(0),
				infoExecutionPeriod);

		RoomKey keySala = new RoomKey("GA1");
		Calendar inicio = Calendar.getInstance();
		Calendar fim = Calendar.getInstance();
		inicio.set(Calendar.HOUR_OF_DAY, 8);
		inicio.set(Calendar.MINUTE, 0);
		inicio.set(Calendar.SECOND, 0);
		fim.set(Calendar.HOUR_OF_DAY, 9);
		fim.set(Calendar.MINUTE, 30);
		fim.set(Calendar.SECOND, 0);
		
		Calendar inicio1 = Calendar.getInstance();
		Calendar fim1 = Calendar.getInstance();
		inicio1.set(Calendar.HOUR_OF_DAY, 8);
		inicio1.set(Calendar.MINUTE, 0);
		inicio1.set(Calendar.SECOND, 0);
		fim1.set(Calendar.HOUR_OF_DAY, 9);
		fim1.set(Calendar.MINUTE, 30);
		fim1.set(Calendar.SECOND, 0);
		Object argsEditarAula[] = new Object[2];
		argsEditarAula[0] =
			new KeyLesson(new DiaSemana(2), inicio, fim, keySala);
		argsEditarAula[1] =
			new InfoLesson(
				new DiaSemana(2),
				inicio,
				fim,
				new TipoAula(TipoAula.TEORICA),
				infoSala,
				iDE);

		Object result = null;

		try {
			result = _gestor.executar(_userView, "EditarAula", argsEditarAula);

			assertTrue(
				"testEditExistingAula",
				((InfoLessonServiceResult) result).isSUCESS());

		} catch (Exception e) {
			e.printStackTrace();
			fail("testEditExistingAula");
		}

	}

	// edit new non-existing aula
		public void testEditarNonExistingAula() {
			InfoRoom infoSala =
				new InfoRoom(
					"Ga1",
					"Pavilhao Central",
					new Integer(0),
					new TipoSala(1),
					new Integer(100),
					new Integer(50));
			InfoDegree infoLicenciatura =
				new InfoDegree(
					"LEIC",
					"Licenciatura de Engenharia Informatica e de Computadores");
			InfoExecutionYear infoExecutionYear =
				new InfoExecutionYear("2002/2003");
			InfoExecutionPeriod infoExecutionPeriod =
				new InfoExecutionPeriod("2� Semestre", infoExecutionYear);
			InfoDegreeCurricularPlan curricularPlan =
				new InfoDegreeCurricularPlan("plano1", infoLicenciatura);
	
			InfoExecutionDegree infoLicenciaturaExecucao =
				new InfoExecutionDegree(curricularPlan, infoExecutionYear);
			InfoExecutionCourse iDE =
				new InfoExecutionCourse(
					"Trabalho Final de Curso I",
					"TFCI",
					"programa1",
					new Double(0),
					new Double(0),
					new Double(0),
					new Double(0),
					infoExecutionPeriod);
			RoomKey keySala = new RoomKey("GA1");
			Calendar inicio = Calendar.getInstance();
			Calendar fim = Calendar.getInstance();
			inicio.set(Calendar.HOUR_OF_DAY, 18);
			inicio.set(Calendar.MINUTE, 0);
			inicio.set(Calendar.SECOND, 0);
			fim.set(Calendar.HOUR_OF_DAY, 19);
			fim.set(Calendar.MINUTE, 30);
			fim.set(Calendar.SECOND, 0);
			Object argsEditarAula[] = new Object[2];
			argsEditarAula[0] =
				new KeyLesson(new DiaSemana(2), inicio, fim, keySala);
			argsEditarAula[1] =
				new InfoLesson(
					new DiaSemana(2),
					inicio,
					fim,
					new TipoAula(TipoAula.PRATICA),
					infoSala,
					iDE);
	
			Object result = null;
			
			try {
				result = _gestor.executar(_userView, "EditarAula", argsEditarAula);
				assertNull("testEditNonExistingAula", result);
			} catch (Exception ex) {
				fail("testEditNonExistingAula");
			}
		}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseServicosWithAuthorization#getNameOfServiceToBeTested()
	 */
	protected String getNameOfServiceToBeTested() {
		return "EditarAula";
	}

}
