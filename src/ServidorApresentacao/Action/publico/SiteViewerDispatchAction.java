package ServidorApresentacao.Action.publico;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.ExecutionCourseSiteView;
import DataBeans.ISiteComponent;
import DataBeans.InfoEvaluationMethod;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoSiteAnnouncement;
import DataBeans.InfoSiteAssociatedCurricularCourses;
import DataBeans.InfoSiteBibliography;
import DataBeans.InfoSiteCommon;
import DataBeans.InfoSiteCurricularCourse;
import DataBeans.InfoSiteEvaluation;
import DataBeans.InfoSiteFirstPage;
import DataBeans.InfoSiteObjectives;
import DataBeans.InfoSiteProgram;
import DataBeans.InfoSiteRoomTimeTable;
import DataBeans.InfoSiteSection;
import DataBeans.InfoSiteShifts;
import DataBeans.InfoSiteSummaries;
import DataBeans.InfoSiteTimetable;
import DataBeans.RoomKey;
import DataBeans.SiteView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.base.FenixContextDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

public class SiteViewerDispatchAction extends FenixContextDispatchAction {

	public ActionForward firstPage(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		ISiteComponent firstPageComponent = new InfoSiteFirstPage();
		String objectCodeString = request.getParameter("objectCode");
		if (objectCodeString == null) {
			objectCodeString = (String) request.getAttribute("objectCode");
		}
		Integer infoExecutionCourseCode = new Integer(objectCodeString);

		readSiteView(
			request,
			firstPageComponent,
			infoExecutionCourseCode,
			null,
			null);
		return mapping.findForward("sucess");
	}

	public ActionForward announcements(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		ISiteComponent announcementsComponent = new InfoSiteAnnouncement();
		readSiteView(request, announcementsComponent, null, null, null);

		return mapping.findForward("sucess");
	}

	public ActionForward objectives(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		ISiteComponent objectivesComponent = new InfoSiteObjectives();
		readSiteView(request, objectivesComponent, null, null, null);

		return mapping.findForward("sucess");
	}

	public ActionForward program(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		ISiteComponent programComponent = new InfoSiteProgram();
		readSiteView(request, programComponent, null, null, null);

		return mapping.findForward("sucess");
	}

	public ActionForward evaluationMethod(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		ISiteComponent evaluationComponent = new InfoEvaluationMethod();
		readSiteView(request, evaluationComponent, null, null, null);

		return mapping.findForward("sucess");
	}

	public ActionForward bibliography(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		ISiteComponent bibliographyComponent = new InfoSiteBibliography();
		readSiteView(request, bibliographyComponent, null, null, null);

		return mapping.findForward("sucess");
	}

	public ActionForward curricularCourses(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		ISiteComponent curricularCoursesComponent =
			new InfoSiteAssociatedCurricularCourses();
		readSiteView(request, curricularCoursesComponent, null, null, null);

		return mapping.findForward("sucess");
	}

	public ActionForward timeTable(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		ISiteComponent timeTableComponent = new InfoSiteTimetable();
		readSiteView(request, timeTableComponent, null, null, null);

		return mapping.findForward("sucess");
	}

	public ActionForward shifts(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		ISiteComponent shiftsComponent = new InfoSiteShifts();
		readSiteView(request, shiftsComponent, null, null, null);

		return mapping.findForward("sucess");
	}

	public ActionForward evaluation(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		ISiteComponent evaluationComponent = new InfoSiteEvaluation();
		readSiteView(request, evaluationComponent, null, null, null);

		return mapping.findForward("sucess");
	}

	public ActionForward section(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		String indexString = (String) request.getParameter("index");
		Integer sectionIndex = new Integer(indexString);

		ISiteComponent sectionComponent = new InfoSiteSection();
		readSiteView(request, sectionComponent, null, sectionIndex, null);

		return mapping.findForward("sucess");
	}

	public ActionForward summaries(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		ISiteComponent summariesComponent = new InfoSiteSummaries();
		readSiteView(request, summariesComponent, null, null, null);

		return mapping.findForward("sucess");

	}

	public ActionForward curricularCourse(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		String curricularCourseIdString =
			(String) request.getParameter("ccCode");
		if (curricularCourseIdString == null) {
			curricularCourseIdString = (String) request.getAttribute("ccCode");
		}
		Integer curricularCourseId = new Integer(curricularCourseIdString);
		ISiteComponent curricularCourseComponent =
			new InfoSiteCurricularCourse();
		readSiteView(
			request,
			curricularCourseComponent,
			null,
			null,
			curricularCourseId);

		return mapping.findForward("sucess");

	}

	private void readSiteView(
		HttpServletRequest request,
		ISiteComponent firstPageComponent,
		Integer infoExecutionCourseCode,
		Integer sectionIndex,
		Integer curricularCourseId)
		throws FenixActionException {

		Integer objectCode = null;
		if (infoExecutionCourseCode == null) {
			String objectCodeString = request.getParameter("objectCode");
			if (objectCodeString == null) {
				objectCodeString = (String) request.getAttribute("objectCode");

			}
			objectCode = new Integer(objectCodeString);

		}

		ISiteComponent commonComponent = new InfoSiteCommon();

		Object[] args =
			{
				commonComponent,
				firstPageComponent,
				objectCode,
				infoExecutionCourseCode,
				sectionIndex,
				curricularCourseId };

		try {
			ExecutionCourseSiteView siteView =
				(ExecutionCourseSiteView) ServiceUtils.executeService(
					null,
					"ExecutionCourseSiteComponentService",
					args);

			if (infoExecutionCourseCode != null) {
				request.setAttribute(
					"objectCode",
					((InfoSiteFirstPage) siteView.getComponent())
						.getSiteIdInternal());
			} else {
				request.setAttribute("objectCode", objectCode);
			}
			request.setAttribute("siteView", siteView);
			request.setAttribute(
				"executionCourseCode",
				((InfoSiteCommon) siteView.getCommonComponent())
					.getExecutionCourse()
					.getIdInternal());
			request.setAttribute(
				"executionPeriodCode",
				((InfoSiteCommon) siteView.getCommonComponent())
					.getExecutionCourse()
					.getInfoExecutionPeriod()
					.getIdInternal());
			if (siteView.getComponent() instanceof InfoSiteSection) {
				request.setAttribute(
					"infoSection",
					((InfoSiteSection) siteView.getComponent()).getSection());
			}

		} catch (NonExistingServiceException e) {
			throw new NonExistingActionException("A disciplina", e);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
	}

	public ActionForward roomViewer(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		String roomName = (String) request.getParameter("roomName");
		if (roomName == null) {
			roomName = (String) request.getAttribute("roomName");
		}

		RoomKey roomKey = null;

		if (roomName != null) {

			roomKey = new RoomKey(roomName);

			InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request.getAttribute(SessionConstants.EXECUTION_PERIOD);
			Integer infoExecutionPeriodOID = infoExecutionPeriod.getIdInternal();


			ISiteComponent bodyComponent = new InfoSiteRoomTimeTable();

			Object[] args = { bodyComponent, roomKey, infoExecutionPeriodOID/*objectCode*/ };

			try {
				SiteView siteView =
					(SiteView) ServiceUtils.executeService(
						null,
						"RoomSiteComponentService",
						args);

				request.setAttribute("siteView", siteView);

		} catch (NonExistingServiceException e) {
				throw new NonExistingActionException(e);
			} catch (FenixServiceException e) {
				throw new FenixActionException(e);
			}
			return mapping.findForward("roomViewer");
		} else {
			throw new FenixActionException();
		}
	}
}