/**
 * Project sop 
 * 
 * Package ServidorApresentacao.Action.utils
 * 
 * Created on 6/Jan/2003
 *
 */
package ServidorApresentacao.utils;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import junit.framework.TestCase;

import org.apache.commons.beanutils.BeanUtils;

import servletunit.HttpServletRequestSimulator;
import servletunit.HttpSessionSimulator;
import servletunit.ServletContextSimulator;
import DataBeans.CurricularYearAndSemesterAndInfoExecutionDegree;
import DataBeans.InfoDegree;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionDegree;
import Dominio.CurricularCourse;
import Dominio.Curso;
import Dominio.CursoExecucao;
import Dominio.Departamento;
import Dominio.DisciplinaDepartamento;
import Dominio.DisciplinaExecucao;
import Dominio.ICurricularCourse;
import Dominio.ICurso;
import Dominio.ICursoExecucao;
import Dominio.IDepartamento;
import Dominio.IDisciplinaDepartamento;
import Dominio.IDisciplinaExecucao;
import Dominio.IPessoa;
import Dominio.IPlanoCurricularCurso;
import Dominio.Pessoa;
import Dominio.PlanoCurricularCurso;
import Dominio.Privilegio;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.IPlanoCurricularCursoPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoCurso;
import Util.TipoDocumentoIdentificacao;

/**
 * @author jpvl
 *
 * 
 */
public class SessionUtilsTest extends TestCase {
	private ServletContextSimulator _ctx;

	private String _schoolYear = new String("2002/2003");

	private int _semester = 1;
	private int _curricularYear = 2;

	private String _degreeName = "Licenciatura";
	private String _degreeInitials = "LEIC";

	protected ISuportePersistente _sp;

	protected ICursoExecucaoPersistente _executionDegreeDAO;
	protected ICursoPersistente _degreeDAO;
	protected IDisciplinaExecucaoPersistente _executionCourseDAO;
	protected IPersistentCurricularCourse _curricularCourseDAO;
	protected IPlanoCurricularCursoPersistente _degreeCurriculumDAO;

	protected ICursoExecucao _executionDegree;
	protected ICurso _degree;

	protected IDisciplinaExecucao _executionCourse;
	protected ICurricularCourse _curricularCourse;
	protected IPessoaPersistente _personDAO = null;
	private List _executionCourseList;

	/**
	 * Constructor for SessionUtilsTest.
	 * @param arg0
	 */
	public SessionUtilsTest(String name) {
		super(name);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(SessionUtilsTest.class);
	}

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		_ctx = new ServletContextSimulator();

		startPersistentLayer();

		IPessoa person = new Pessoa();

		Set privileges = new HashSet();
		person.setNome("Marvin");
		person.setUsername("45498");
		person.setNumeroDocumentoIdentificacao("010101010101");
		person.setCodigoFiscal("010101010101");
		person.setTipoDocumentoIdentificacao(
			new TipoDocumentoIdentificacao(
				TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE));
		privileges.add(
			new Privilegio(
				person,
				new String("LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular")));

		person.setPrivilegios(privileges);

		_degree =
			new Curso(
				_degreeInitials,
				_degreeName,
				new TipoCurso(TipoCurso.LICENCIATURA));
		_executionDegree = new CursoExecucao(_schoolYear, _degree);

		IPlanoCurricularCurso degreeCurriculum =
			new PlanoCurricularCurso("Plano 1", "P1", _degree);

		IDisciplinaExecucao _executionCourse = null;
		ICurricularCourse _curricularCourse = null;

		_executionCourseList = new ArrayList();

		_sp.iniciarTransaccao();
		for (int i = 1; i < 10; i++) {
			IDepartamento d = new Departamento("nome"+i, "sigla"+i);
			IDisciplinaDepartamento dd = new DisciplinaDepartamento(
				"Disciplina " + i,
				"D" + i,
				d);			
			_curricularCourse =
				new CurricularCourse(
					new Double(1.0),
					new Double(2.0),
					new Double(2.0),
					new Double(0),
					new Double(0),
					new Integer(_curricularYear),
					new Integer(_semester),
					"Disciplina " + i,
					"D" + i,dd,
					degreeCurriculum);
			_executionCourse =
				new DisciplinaExecucao(
					"Disciplina " + i,
					"D" + i,
					"Programa",
					_executionDegree,
					new Double(2.0),
					new Double(2.0),
					new Double(0),
					new Double(0));

			List list = new ArrayList();

			list.add(_curricularCourse);
			_executionCourse.setAssociatedCurricularCourses(list);

			
			_sp.getIDepartamentoPersistente().escreverDepartamento(d);
			_sp.getIDisciplinaDepartamentoPersistente().escreverDisciplinaDepartamento(dd);
			
			_curricularCourseDAO.writeCurricularCourse(_curricularCourse);
			_executionCourseDAO.escreverDisciplinaExecucao(_executionCourse);

			InfoExecutionCourse infoExecutionCourse = new InfoExecutionCourse();

			copyExecutionCourseToInfoExecutionCourse(
				infoExecutionCourse,
				_executionCourse);
			_executionCourseList.add(infoExecutionCourse);
		}

		_executionDegreeDAO.lockWrite(_executionDegree);
		_personDAO.escreverPessoa(person);

		_sp.confirmarTransaccao();

	}
	/**
	 * Method copyExecutionCourseToInfoExecutionCourse.
	 * @param infoExecutionCourse
	 * @param _executionCourse
	 */
	private void copyExecutionCourseToInfoExecutionCourse(
		InfoExecutionCourse infoExecutionCourse,
		IDisciplinaExecucao executionCourse) {
		InfoDegree infoDegree = new InfoDegree();
		InfoExecutionDegree infoExecutionDegree = new InfoExecutionDegree();

		try {
			BeanUtils.copyProperties(infoExecutionCourse, executionCourse);

			BeanUtils.copyProperties(
				infoExecutionDegree,
				executionCourse.getLicenciaturaExecucao());

			BeanUtils.copyProperties(
				infoDegree,
				executionCourse.getLicenciaturaExecucao().getCurso());
		} catch (Exception e) {
			fail("Executing copyExecutionCourseToInfoExecutionCourse");
		}

		infoExecutionDegree.setInfoLicenciatura(infoDegree);
		infoExecutionCourse.setInfoLicenciaturaExecucao(infoExecutionDegree);
	}

	protected void startPersistentLayer() {
		try {
			_sp = SuportePersistenteOJB.getInstance();
		} catch (ExcepcaoPersistencia e) {
			fail("Starting persistent layer!");
		}

		_executionDegreeDAO = _sp.getICursoExecucaoPersistente();
		_degreeDAO = _sp.getICursoPersistente();
		_executionCourseDAO = _sp.getIDisciplinaExecucaoPersistente();
		_curricularCourseDAO = _sp.getIPersistentCurricularCourse();
		_degreeCurriculumDAO = _sp.getIPlanoCurricularCursoPersistente();
		_personDAO = _sp.getIPessoaPersistente();
		cleanData();
	}

	protected void cleanData() {
		try {
			_sp.iniciarTransaccao();

			_executionDegreeDAO.deleteAll();
			_degreeDAO.deleteAll();
			_executionCourseDAO.apagarTodasAsDisciplinasExecucao();
			_curricularCourseDAO.deleteAllCurricularCourse();
			_degreeCurriculumDAO.apagarTodosOsPlanosCurriculares();
			_personDAO.apagarTodasAsPessoas();
			_sp.getIDepartamentoPersistente().apagarTodosOsDepartamentos();
			_sp.getIDisciplinaDepartamentoPersistente().apagarTodasAsDisciplinasDepartamento();
		} catch (ExcepcaoPersistencia e) {
			fail("Cleaning data!");
		} finally {
			try {
				_sp.confirmarTransaccao();
			} catch (ExcepcaoPersistencia ignored) {
			}
		}

	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testRemoveAttributtes() {
		String prefix = "prefix.";
		HttpSessionSimulator sessionSimulator = new HttpSessionSimulator(_ctx);
		for (int i = 0; i < 10; i++) {
			sessionSimulator.setAttribute("dummy" + i, "dummy");
			sessionSimulator.setAttribute(prefix + i, "value");
		}
		sessionSimulator.setAttribute("dummy", "dummy");
		SessionUtils.removeAttributtes(sessionSimulator, prefix);

		Enumeration attNames = sessionSimulator.getAttributeNames();

		while (attNames.hasMoreElements()) {
			String attName = (String) attNames.nextElement();
			if (attName.startsWith(prefix)) {
				fail(
					"Contains one attribute starting with "
						+ prefix
						+ ": att =("
						+ attName
						+ ")");
			}
		}
	}

	public void testGetContext() {
		HttpServletRequestSimulator request =
			new HttpServletRequestSimulator(_ctx);

		CurricularYearAndSemesterAndInfoExecutionDegree c =
			setToSessionCurricularYearAndSemesterAndInfoExecutionDegree(request);
		CurricularYearAndSemesterAndInfoExecutionDegree c2 =
			SessionUtils.getContext(request);

		assertNotNull("Obtnained object from session must be not null!", c2);
		assertEquals(c, c2);
	}

	public void testGetUserView() {
		HttpServletRequestSimulator request =
			new HttpServletRequestSimulator(_ctx);
		IUserView userView = setToSessionUserView(request);

		IUserView userView2 = SessionUtils.getUserView(request);

		assertNotNull(
			"Obtnained object from session must be not null!",
			userView2);
		assertEquals(userView, userView2);

	}

	public void testGetExecutionCoursesAlreadyInSession() {
		HttpServletRequestSimulator request =
			new HttpServletRequestSimulator(_ctx);

		CurricularYearAndSemesterAndInfoExecutionDegree c =
			createCurricularYearAndSemesterAndInfoExecutionDegree();

		IUserView userView = setToSessionUserView(request);

		List executionCourseList = new ArrayList();

		request.getSession().setAttribute(
			SessionConstants.EXECUTION_COURSE_LIST_KEY,
			executionCourseList);

		List executionCourseList2 = null;
		try {
			executionCourseList2 = SessionUtils.getExecutionCourses(request, c);
		} catch (Exception e) {
			fail("Exception running getExecutionCourses");
		}

		assertNotNull(
			"Execution course list must be not null!",
			executionCourseList2);
		assertEquals(true, executionCourseList2.isEmpty());
	}

	public void testGetExecutionCoursesNotYetInSession() {
		HttpServletRequestSimulator request =
			new HttpServletRequestSimulator(_ctx);

		CurricularYearAndSemesterAndInfoExecutionDegree c =
			createCurricularYearAndSemesterAndInfoExecutionDegree();

		setToSessionUserView(request);
		List infoExecutionCourseList = null;
		try {
			infoExecutionCourseList =
				SessionUtils.getExecutionCourses(request, c);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			fail("testGetExecutionCoursesNotYetInSession: Executing getExecutionCourses");
		}

		assertNotNull(
			"infoExecutionCourseList is null.",
			infoExecutionCourseList);
		assertEquals(
			"List is not of the same size",
			infoExecutionCourseList.size(),
			_executionCourseList.size());
		assertTrue(
			"List not contains all elements!",
			infoExecutionCourseList.containsAll(_executionCourseList));
		assertNotNull(
			"Session must contain attribute in SessionConstants.EXECUTION_COURSE_LIST_KEY",
			request.getSession().getAttribute(
				SessionConstants.EXECUTION_COURSE_LIST_KEY));
	}

	private IUserView createUserView() {
		HashSet privileges = new HashSet();
		privileges.add(
			new String("LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular"));

		IUserView userView = new UserView("45498", privileges);
		return userView;
	}

	private IUserView setToSessionUserView(HttpServletRequest request) {
		IUserView userView = createUserView();
		HttpSession session = request.getSession(true);

		session.setAttribute(SessionConstants.U_VIEW, userView);
		return userView;
	}

	private CurricularYearAndSemesterAndInfoExecutionDegree setToSessionCurricularYearAndSemesterAndInfoExecutionDegree(HttpServletRequest request) {
		CurricularYearAndSemesterAndInfoExecutionDegree c =
			createCurricularYearAndSemesterAndInfoExecutionDegree();

		HttpSession session = request.getSession(true);

		session.setAttribute(SessionConstants.CONTEXT_KEY, c);
		return c;

	}

	private CurricularYearAndSemesterAndInfoExecutionDegree createCurricularYearAndSemesterAndInfoExecutionDegree() {
		CurricularYearAndSemesterAndInfoExecutionDegree c =
			new CurricularYearAndSemesterAndInfoExecutionDegree();
		c.setSemestre(new Integer(_semester));
		c.setAnoCurricular(new Integer(_curricularYear));

		InfoDegree infoDegree = new InfoDegree(_degreeInitials, _degreeName);
		InfoExecutionDegree infoExecutionDegree =
			new InfoExecutionDegree(_schoolYear, infoDegree);
		c.setInfoLicenciaturaExecucao(infoExecutionDegree);

		return c;
	}

}
