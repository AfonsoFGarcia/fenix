/**
 * Project sop 
 * 
 * Package ServidorApresentacao.Action.common
 * 
 * Created on 8/Jan/2003
 *
 */
package ServidorApresentacao.Action.commons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

import DataBeans.CurricularYearAndSemesterAndInfoExecutionDegree;
import DataBeans.InfoDegree;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.comparators.ComparatorByNameForInfoExecutionDegree;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixContextDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.RequestUtils;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author jpvl
 */
public class ChooseContextDispatchAction extends FenixContextDispatchAction {

	protected static final String INFO_DEGREE_INITIALS_PARAMETER =
		"degreeInitials";
	protected static final String SEMESTER_PARAMETER = "semester";
	protected static final String CURRICULAR_YEAR_PARAMETER = "curricularYear";
	/* (non-Javadoc)
	 * @see org.apache.struts.actions.DispatchAction#dispatchMethod(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.String)
	 */
	public ActionForward prepare(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		HttpSession session = request.getSession(false);
		if (session != null) {
			String inputPage =
				request.getParameter(SessionConstants.INPUT_PAGE);
			String nextPage = request.getParameter(SessionConstants.NEXT_PAGE);
			if (inputPage != null)
				session.setAttribute(SessionConstants.INPUT_PAGE, inputPage);
			if (nextPage != null)
				session.setAttribute(SessionConstants.NEXT_PAGE, nextPage);

			IUserView userView = SessionUtils.getUserView(request);

			InfoExecutionPeriod infoExecutionPeriod =
				setExecutionContext(request);

			//TODO: this semester and  curricular year list needs to be refactored in order to incorporate masters
			/* Criar o bean de semestres */
			ArrayList semestres = new ArrayList();
			semestres.add(new LabelValueBean("escolher", ""));
			semestres.add(new LabelValueBean("1 �", "1"));
			semestres.add(new LabelValueBean("2 �", "2"));
			session.setAttribute("semestres", semestres);

			/* Criar o bean de anos curricutares */
			ArrayList anosCurriculares = new ArrayList();
			anosCurriculares.add(new LabelValueBean("escolher", ""));
			anosCurriculares.add(new LabelValueBean("1 �", "1"));
			anosCurriculares.add(new LabelValueBean("2 �", "2"));
			anosCurriculares.add(new LabelValueBean("3 �", "3"));
			anosCurriculares.add(new LabelValueBean("4 �", "4"));
			anosCurriculares.add(new LabelValueBean("5 �", "5"));
			session.setAttribute(
				SessionConstants.CURRICULAR_YEAR_LIST_KEY,
				anosCurriculares);

			/* Cria o form bean com as licenciaturas em execucao.*/
			Object argsLerLicenciaturas[] =
				{ infoExecutionPeriod.getInfoExecutionYear()};

			List executionDegreeList =
				(List) ServiceUtils.executeService(
					userView,
					"ReadExecutionDegreesByExecutionYear",
					argsLerLicenciaturas);

			ArrayList licenciaturas = new ArrayList();

			licenciaturas.add(new LabelValueBean("escolher", ""));

			Collections.sort(
				executionDegreeList,
				new ComparatorByNameForInfoExecutionDegree());

			Iterator iterator = executionDegreeList.iterator();

			int index = 0;
			while (iterator.hasNext()) {
				InfoExecutionDegree infoExecutionDegree =
					(InfoExecutionDegree) iterator.next();
				String name =
					infoExecutionDegree
						.getInfoDegreeCurricularPlan()
						.getInfoDegree()
						.getNome();

				name =
					infoExecutionDegree
						.getInfoDegreeCurricularPlan()
						.getInfoDegree()
						.getTipoCurso()
						.toString()
						+ " de "
						+ name;

				name
					+= duplicateInfoDegree(
						executionDegreeList,
						infoExecutionDegree)
					? "-"
						+ infoExecutionDegree
							.getInfoDegreeCurricularPlan()
							.getName()
					: "";

				licenciaturas.add(
					new LabelValueBean(name, String.valueOf(index++)));
			}

			session.setAttribute(
				SessionConstants.INFO_EXECUTION_DEGREE_LIST_KEY,
				executionDegreeList);

			request.setAttribute(SessionConstants.DEGREES, licenciaturas);

			if (inputPage != null)
				return mapping.findForward(inputPage);
			else
				// TODO : throw a proper exception
				throw new Exception("SomeOne is messing around with the links");
		} else
			throw new Exception();
		// nao ocorre... pedido passa pelo filtro Autorizacao

	}

	public ActionForward preparePublic(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		HttpSession session = request.getSession(true);

		String inputPage = request.getParameter(SessionConstants.INPUT_PAGE);
		String nextPage = request.getParameter(SessionConstants.NEXT_PAGE);
		if (inputPage != null)
			session.setAttribute(SessionConstants.INPUT_PAGE, inputPage);
		if (nextPage != null)
			session.setAttribute(SessionConstants.NEXT_PAGE, nextPage);

		InfoExecutionPeriod infoExecutionPeriod =
			(InfoExecutionPeriod) request.getAttribute(
				SessionConstants.EXECUTION_PERIOD);

		//TODO: this semester and  curricular year list needs to be refactored in order to incorporate masters
		/* Criar o bean de semestres */
		ArrayList semestres = new ArrayList();
		semestres.add(new LabelValueBean("escolher", ""));
		semestres.add(new LabelValueBean("1 �", "1"));
		semestres.add(new LabelValueBean("2 �", "2"));
		request.setAttribute("semestres", semestres);

		/* Criar o bean de anos curricutares */
		ArrayList anosCurriculares = new ArrayList();
		anosCurriculares.add(new LabelValueBean("escolher", ""));
		anosCurriculares.add(new LabelValueBean("1 �", "1"));
		anosCurriculares.add(new LabelValueBean("2 �", "2"));
		anosCurriculares.add(new LabelValueBean("3 �", "3"));
		anosCurriculares.add(new LabelValueBean("4 �", "4"));
		anosCurriculares.add(new LabelValueBean("5 �", "5"));
		request.setAttribute("curricularYearList", anosCurriculares);

		/* Cria o form bean com as licenciaturas em execucao.*/
		Object argsLerLicenciaturas[] =
			{ infoExecutionPeriod.getInfoExecutionYear()};

		List executionDegreeList =
			(List) ServiceUtils.executeService(
				null,
				"ReadExecutionDegreesByExecutionYear",
				argsLerLicenciaturas);

		ArrayList licenciaturas = new ArrayList();

		licenciaturas.add(new LabelValueBean("escolher", ""));

		Collections.sort(
			executionDegreeList,
			new ComparatorByNameForInfoExecutionDegree());

		Iterator iterator = executionDegreeList.iterator();

		int index = 0;
		while (iterator.hasNext()) {
			InfoExecutionDegree infoExecutionDegree =
				(InfoExecutionDegree) iterator.next();
			String name =
				infoExecutionDegree
					.getInfoDegreeCurricularPlan()
					.getInfoDegree()
					.getNome();

			name =
				infoExecutionDegree
					.getInfoDegreeCurricularPlan()
					.getInfoDegree()
					.getTipoCurso()
					.toString()
					+ " de "
					+ name;

			name
				+= duplicateInfoDegree(executionDegreeList, infoExecutionDegree)
				? "-"
					+ infoExecutionDegree.getInfoDegreeCurricularPlan().getName()
				: "";

			licenciaturas.add(
				new LabelValueBean(name, String.valueOf(index++)));
		}

		request.setAttribute("degreeList", licenciaturas);

		if (inputPage != null)
			return mapping.findForward(inputPage);
		else
			// TODO : throw a proper exception
			throw new Exception("SomeOne is messing around with the links");

	}

	public ActionForward nextPage(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		HttpSession session = request.getSession(false);
		DynaActionForm escolherContextoForm = (DynaActionForm) form;

		SessionUtils.removeAttributtes(
			session,
			SessionConstants.CONTEXT_PREFIX);

		if (session != null) {
			Integer semestre =
				((InfoExecutionPeriod) session
					.getAttribute(SessionConstants.INFO_EXECUTION_PERIOD_KEY))
					.getSemester();
			Integer anoCurricular =
				(Integer) escolherContextoForm.get("curricularYear");

			int index =
				Integer.parseInt((String) escolherContextoForm.get("index"));

			session.setAttribute("anoCurricular", anoCurricular);
			session.setAttribute("semestre", semestre);

			List infoExecutionDegreeList =
				(List) session.getAttribute(
					SessionConstants.INFO_EXECUTION_DEGREE_LIST_KEY);

			InfoExecutionDegree infoExecutionDegree =
				(InfoExecutionDegree) infoExecutionDegreeList.get(index);

			if (infoExecutionDegree != null) {
				CurricularYearAndSemesterAndInfoExecutionDegree cYSiED =
					new CurricularYearAndSemesterAndInfoExecutionDegree(
						anoCurricular,
						semestre,
						infoExecutionDegree);
				session.setAttribute(SessionConstants.CONTEXT_KEY, cYSiED);

				session.setAttribute(
					SessionConstants.CURRICULAR_YEAR_KEY,
					anoCurricular);
				session.setAttribute(
					SessionConstants.INFO_EXECUTION_DEGREE_KEY,
					infoExecutionDegree);

			} else {
				return mapping.findForward("Licenciatura execucao inexistente");
			}

			String nextPage =
				(String) session.getAttribute(SessionConstants.NEXT_PAGE);
			if (nextPage != null) {
				return mapping.findForward(nextPage);
			} else
				// TODO : throw a proper exception
				throw new Exception("SomeOne is messing around with the links");
		} else
			throw new Exception();
		// nao ocorre... pedido passa pelo filtro Autorizacao

	}

	public ActionForward nextPagePublic(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		HttpSession session = request.getSession(true);
		DynaActionForm escolherContextoForm = (DynaActionForm) form;

		SessionUtils.removeAttributtes(
			session,
			SessionConstants.CONTEXT_PREFIX);

		if (session != null) {
			InfoExecutionPeriod infoExecutionPeriod =
				(InfoExecutionPeriod) request.getAttribute(
					SessionConstants.EXECUTION_PERIOD);

			Integer semestre = infoExecutionPeriod.getSemester();
			Integer anoCurricular =
				(Integer) escolherContextoForm.get("curYear");

			Integer index =
				new Integer((String) escolherContextoForm.get("index"));

			request.setAttribute("curYear", anoCurricular);
			request.setAttribute("semester", semestre);

			Object argsLerLicenciaturas[] =
				{ infoExecutionPeriod.getInfoExecutionYear()};

			List infoExecutionDegreeList;
			try {
				infoExecutionDegreeList =
					(List) ServiceUtils.executeService(
						null,
						"ReadExecutionDegreesByExecutionYear",
						argsLerLicenciaturas);
			} catch (FenixServiceException e) {
				throw new FenixActionException(e);
			}
			Collections.sort(
				infoExecutionDegreeList,
				new ComparatorByNameForInfoExecutionDegree());

			InfoExecutionDegree infoExecutionDegree =
				(InfoExecutionDegree) infoExecutionDegreeList.get(
					index.intValue());

			if (infoExecutionDegree == null) {
				return mapping.findForward("Licenciatura execucao inexistente");
			}
			RequestUtils.setExecutionDegreeToRequest(
				request,
				infoExecutionDegree);
			String nextPage =
				(String) session.getAttribute(SessionConstants.NEXT_PAGE);
			if (nextPage != null)
				return mapping.findForward(nextPage);
			else
				// TODO : throw a proper exception
				throw new FenixActionException("SomeOne is messing around with the links");
		} else
			throw new FenixActionException();
		// nao ocorre... pedido passa pelo filtro Autorizacao

	}

	/**
	 * Method setCurricularYearList.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 */
	private List setCurricularYearList(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response) {

		List curricularYearList =
			(List) request.getSession(false).getAttribute(
				SessionConstants.CURRICULAR_YEAR_LIST_KEY);

		if (curricularYearList == null) {
			curricularYearList = new ArrayList();
			curricularYearList.add(new LabelValueBean("1�", "1"));
			curricularYearList.add(new LabelValueBean("2�", "2"));
			curricularYearList.add(new LabelValueBean("3�", "3"));
			curricularYearList.add(new LabelValueBean("4�", "4"));
			curricularYearList.add(new LabelValueBean("5�", "5"));
			request.getSession(false).setAttribute(
				SessionConstants.CURRICULAR_YEAR_LIST_KEY,
				curricularYearList);
		}
		return curricularYearList;
	}
	/**
	 * Method setSemesterList.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 */
	private List setSemesterList(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response) {

		List semesterList =
			(List) request.getSession(false).getAttribute(
				SessionConstants.SEMESTER_LIST_KEY);

		if (semesterList == null) {

			semesterList = new ArrayList();
			semesterList.add(new LabelValueBean("1�", "1"));
			semesterList.add(new LabelValueBean("2�", "2"));
			request.getSession(false).setAttribute(
				SessionConstants.SEMESTER_LIST_KEY,
				semesterList);
		}
		return semesterList;
	}

	/**
	 * Method existencesOfInfoDegree.
	 * @param executionDegreeList
	 * @param infoExecutionDegree
	 * @return int
	 */
	private boolean duplicateInfoDegree(
		List executionDegreeList,
		InfoExecutionDegree infoExecutionDegree) {
		InfoDegree infoDegree =
			infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree();
		Iterator iterator = executionDegreeList.iterator();

		while (iterator.hasNext()) {
			InfoExecutionDegree infoExecutionDegree2 =
				(InfoExecutionDegree) iterator.next();
			if (infoDegree
				.equals(
					infoExecutionDegree2
						.getInfoDegreeCurricularPlan()
						.getInfoDegree())
				&& !(infoExecutionDegree.equals(infoExecutionDegree2)))
				return true;

		}
		return false;
	}
	/**
	 * Method setExecutionContext.
	 * @param request
	 */
	// TODO When session is removed from SOP, use method with same name from RequestUtils
	private InfoExecutionPeriod setExecutionContext(HttpServletRequest request)
		throws Exception {

		HttpSession session = request.getSession(false);
		InfoExecutionPeriod infoExecutionPeriod =
			(InfoExecutionPeriod) session.getAttribute(
				SessionConstants.INFO_EXECUTION_PERIOD_KEY);
		if (infoExecutionPeriod == null) {
			IUserView userView = SessionUtils.getUserView(request);
			infoExecutionPeriod =
				(InfoExecutionPeriod) ServiceUtils.executeService(
					userView,
					"ReadCurrentExecutionPeriod",
					new Object[0]);

			session.setAttribute(
				SessionConstants.INFO_EXECUTION_PERIOD_KEY,
				infoExecutionPeriod);
		}
		return infoExecutionPeriod;
	}

}
