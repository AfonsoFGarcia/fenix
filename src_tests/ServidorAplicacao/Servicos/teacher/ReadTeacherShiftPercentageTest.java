/*
 * Created on 15/Mai/2003 by jpvl
 *
 */
package ServidorAplicacao.Servicos.teacher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoRole;
import DataBeans.InfoTeacher;
import DataBeans.teacher.credits.InfoShiftPercentage;
import DataBeans.util.Cloner;
import Dominio.DisciplinaExecucao;
import Dominio.IDisciplinaExecucao;
import Dominio.ITeacher;
import Dominio.Teacher;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servicos.TestCaseServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.RoleType;

/**
 * @author jpvl
 */
public class ReadTeacherShiftPercentageTest extends TestCaseServices {

	/**
	 * @param testName
	 */
	public ReadTeacherShiftPercentageTest(String testName) {
		super(testName);
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseServices#getNameOfServiceToBeTested()
	 */
	protected String getNameOfServiceToBeTested() {
		return "ReadTeacherExecutionCourseShiftsPercentage";
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseServices#getDataSetFilePath()
	 */
	protected String getDataSetFilePath() {
		return "etc/testDataSetForTeacherCredits.xml";
	}

	public void testSucessfullExecution() {
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			//Execution Course
			IDisciplinaExecucao executionCourse = new DisciplinaExecucao();
			executionCourse.setIdInternal(new Integer(1));

			IDisciplinaExecucaoPersistente executionCourseDAO =
				sp.getIDisciplinaExecucaoPersistente();
			executionCourse =
				(IDisciplinaExecucao) executionCourseDAO.readByOId(
					executionCourse);

			InfoExecutionCourse infoExecutionCourse =
				Cloner.copyIExecutionCourse2InfoExecutionCourse(
					executionCourse);

			//Teacher		
			ITeacher teacher = new Teacher();
			teacher.setIdInternal(new Integer(1));

			IPersistentTeacher teacherDAO = sp.getIPersistentTeacher();
			teacher = (ITeacher) teacherDAO.readByOId(teacher);

			InfoTeacher infoTeacher = Cloner.copyITeacher2InfoTeacher(teacher);

			GestorServicos serviceManager = GestorServicos.manager();

			Object[] args = { infoTeacher, infoExecutionCourse };

			List infoShiftPercentageList =
				(List) serviceManager.executar(
					authorizedUserView(),
					getNameOfServiceToBeTested(),
					args);

			assertNotNull("is null!", infoShiftPercentageList);

			assertEquals("size not 10!", 10, infoShiftPercentageList.size());

			Iterator iterator = infoShiftPercentageList.iterator();
			while (iterator.hasNext()) {
				InfoShiftPercentage infoShiftPercentage =
					(InfoShiftPercentage) iterator.next();
				int internalCode =
					infoShiftPercentage.getShift().getIdInternal().intValue();
				switch (internalCode) {
					case 1 :
						assertEquals(
							"shift 1 - 1",
							new Double(0),
							infoShiftPercentage.getAvailablePercentage());
						assertEquals(
							"shift 1 - 2",
							2,
							infoShiftPercentage
								.getTeacherShiftPercentageList()
								.size());
						break;
					case 2 :
						assertEquals(
							"shift 2 - 1",
							new Double(75),
							infoShiftPercentage.getAvailablePercentage());
						assertEquals(
							"shift 2 - 2",
							1,
							infoShiftPercentage
								.getTeacherShiftPercentageList()
								.size());
						break;
					default :
						assertEquals(
							"shift " + internalCode + " - 1",
							new Double(100),
							infoShiftPercentage.getAvailablePercentage());
						assertEquals(
							"shift " + internalCode + " - 2",
							0,
							infoShiftPercentage
								.getTeacherShiftPercentageList()
								.size());
						break;
				}
			}

		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			fail("Reading database!");
		} catch (FenixServiceException e) {
			e.printStackTrace();
			fail("Executing  Service!");
		}
	}

	public IUserView authorizedUserView() {
		InfoRole infoRole = new InfoRole();
		infoRole.setRoleType(RoleType.TEACHER);

		Collection roles = new ArrayList();
		roles.add(infoRole);

		UserView userView = new UserView("user", roles);

		return userView;
	}
}
