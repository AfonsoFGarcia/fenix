package ServidorApresentacao.Action.teacher;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.upload.FormFile;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.validator.DynaValidatorForm;

import fileSuport.FileSuportObject;

import DataBeans.ExecutionCourseSiteView;
import DataBeans.ISiteComponent;
import DataBeans.InfoAnnouncement;
import DataBeans.InfoBibliographicReference;
import DataBeans.InfoCurriculum;
import DataBeans.InfoGroupProperties;
import DataBeans.InfoItem;
import DataBeans.InfoShift;
import DataBeans.InfoSite;
import DataBeans.InfoSiteAllGroups;
import DataBeans.InfoSiteAnnouncement;
import DataBeans.InfoSiteBibliography;
import DataBeans.InfoSiteCommon;
import DataBeans.InfoSiteEvaluation;
import DataBeans.InfoSiteEvaluationMethods;
import DataBeans.InfoSiteGroupProperties;
import DataBeans.InfoSiteInstructions;
import DataBeans.InfoSiteItems;
import DataBeans.InfoSiteObjectives;
import DataBeans.InfoSitePrograms;
import DataBeans.InfoSiteProjects;
import DataBeans.InfoSiteRegularSections;
import DataBeans.InfoSiteRootSections;
import DataBeans.InfoSiteSection;
import DataBeans.InfoSiteSections;
import DataBeans.InfoSiteShifts;
import DataBeans.InfoSiteStudentGroup;
import DataBeans.InfoSiteSummaries;
import DataBeans.InfoSiteSummary;
import DataBeans.InfoSiteTeachers;
import DataBeans.SiteView;
import DataBeans.TeacherAdministrationSiteView;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorAplicacao.Servico.exceptions.NonValidChangeServiceException;
import ServidorAplicacao.Servico.exceptions.notAuthorizedServiceDeleteException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.InvalidArgumentsActionException;
import ServidorApresentacao.Action.exceptions.InvalidSessionActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao
	.Action
	.exceptions
	.notAuthorizedActionDeleteException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.mapping.SiteManagementActionMapping;
import Util.EnrolmentGroupPolicyType;
import Util.TipoAula;
/**
 * @author Fernanda Quit�rio
 *
 */
public class TeacherAdministrationViewerDispatchAction
	extends FenixDispatchAction {
	public ActionForward instructions(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		ISiteComponent instructionsComponent = new InfoSiteInstructions();
		readSiteView(request, instructionsComponent, null, null, null);
		return mapping.findForward("viewSite");
	}
	// ========================  Customization Options Management  ========================
	public ActionForward prepareCustomizationOptions(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		ISiteComponent customizationOptionsComponent = new InfoSite();
		readSiteView(request, customizationOptionsComponent, null, null, null);
		TeacherAdministrationSiteView siteView =
			(TeacherAdministrationSiteView) request.getAttribute("siteView");
		DynaValidatorForm alternativeSiteForm = (DynaValidatorForm) form;
		alternativeSiteForm.set(
			"siteAddress",
			((InfoSite) siteView.getComponent()).getAlternativeSite());
		alternativeSiteForm.set(
			"mail",
			((InfoSite) siteView.getComponent()).getMail());
		alternativeSiteForm.set(
			"initialStatement",
			((InfoSite) siteView.getComponent()).getInitialStatement());
		alternativeSiteForm.set(
			"introduction",
			((InfoSite) siteView.getComponent()).getIntroduction());
		return mapping.findForward("editAlternativeSite");
	}
	public ActionForward editCustomizationOptions(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		DynaValidatorForm alternativeSiteForm = (DynaValidatorForm) form;
		HttpSession session = request.getSession();
		session.removeAttribute(SessionConstants.INFO_SECTION);
		Integer objectCode = getObjectCode(request);
		String alternativeSite =
			(String) alternativeSiteForm.get("siteAddress");
		String mail = (String) alternativeSiteForm.get("mail");
		String initialStatement =
			(String) alternativeSiteForm.get("initialStatement");
		String introduction = (String) alternativeSiteForm.get("introduction");
		InfoSite infoSiteNew = new InfoSite();
		infoSiteNew.setAlternativeSite(alternativeSite);
		infoSiteNew.setMail(mail);
		infoSiteNew.setInitialStatement(initialStatement);
		infoSiteNew.setIntroduction(introduction);
		UserView userView = (UserView) session.getAttribute("UserView");
		Object args[] = { objectCode, infoSiteNew };
		GestorServicos gestor = GestorServicos.manager();
		try {
			gestor.executar(userView, "EditCustomizationOptions", args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		ISiteComponent instructionsComponent = new InfoSiteInstructions();
		readSiteView(request, instructionsComponent, null, null, null);
		session.setAttribute("alternativeSiteForm", alternativeSiteForm);
		return mapping.findForward("viewSite");
	}
	//	========================  Announcements Management  ========================
	public ActionForward showAnnouncements(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		ISiteComponent announcementsComponent = new InfoSiteAnnouncement();
		readSiteView(request, announcementsComponent, null, null, null);
		TeacherAdministrationSiteView siteView =
			(TeacherAdministrationSiteView) request.getAttribute("siteView");
		if (!((InfoSiteAnnouncement) siteView.getComponent())
			.getAnnouncements()
			.isEmpty()) {
			return mapping.findForward("showAnnouncements");
		} else {
			HttpSession session = request.getSession(false);
			session.removeAttribute("insertAnnouncementForm");
			return mapping.findForward("insertAnnouncement");
		}
	}
	public ActionForward prepareCreateAnnouncement(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		HttpSession session = request.getSession(false);
		session.removeAttribute("insertAnnouncementForm");
		readSiteView(request, null, null, null, null);
		return mapping.findForward("insertAnnouncement");
	}
	public ActionForward createAnnouncement(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		HttpSession session = request.getSession(false);
		Integer objectCode = getObjectCode(request);
		DynaActionForm insertAnnouncementForm = (DynaActionForm) form;
		String title = (String) insertAnnouncementForm.get("title");
		String information = (String) insertAnnouncementForm.get("information");
		UserView userView = (UserView) session.getAttribute("UserView");
		Object args[] = { objectCode, title, information };
		GestorServicos gestor = GestorServicos.manager();
		try {
			gestor.executar(userView, "CreateAnnouncement", args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		ISiteComponent announcementsComponent = new InfoSiteAnnouncement();
		readSiteView(request, announcementsComponent, null, null, null);
		return mapping.findForward("showAnnouncements");
	}
	public ActionForward prepareEditAnnouncement(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		//retrieve announcement
		Integer announcementCode = getAnnouncementCode(request);
		ISiteComponent announcementComponent = new InfoAnnouncement();
		readSiteView(
			request,
			announcementComponent,
			null,
			announcementCode,
			null);
		return mapping.findForward("editAnnouncement");
	}
	private Integer getAnnouncementCode(HttpServletRequest request) {
		String announcementCodeString =
			request.getParameter("announcementCode");
		Integer announcementCode = null;
		if (announcementCodeString == null) {
			announcementCodeString =
				(String) request.getAttribute("announcementCode");
		}
		if (announcementCodeString != null)
			announcementCode = new Integer(announcementCodeString);
		return announcementCode;
	}
	public ActionForward editAnnouncement(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		HttpSession session = request.getSession(false);
		String announcementCodeString =
			request.getParameter("announcementCode");
		if (announcementCodeString == null) {
			announcementCodeString =
				(String) request.getAttribute("announcementCode");
		}
		Integer announcementCode = new Integer(announcementCodeString);
		Integer objectCode = getObjectCode(request);
		DynaActionForm insertAnnouncementForm = (DynaActionForm) form;
		String newTitle = (String) insertAnnouncementForm.get("title");
		String newInformation =
			(String) insertAnnouncementForm.get("information");
		UserView userView = (UserView) session.getAttribute("UserView");
		Object args[] =
			{ objectCode, announcementCode, newTitle, newInformation };
		GestorServicos gestor = GestorServicos.manager();
		try {
			gestor.executar(userView, "EditAnnouncementService", args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		ISiteComponent announecementsComponent = new InfoSiteAnnouncement();
		readSiteView(request, announecementsComponent, null, null, null);
		return mapping.findForward("showAnnouncements");
	}
	public ActionForward deleteAnnouncement(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		HttpSession session = request.getSession(false);
		String announcementCodeString =
			request.getParameter("announcementCode");
		if (announcementCodeString == null) {
			announcementCodeString =
				(String) request.getAttribute("announcementCode");
		}
		Integer announcementCode = new Integer(announcementCodeString);
		Integer objectCode = getObjectCode(request);
		UserView userView = (UserView) session.getAttribute("UserView");
		Object args[] = { objectCode, announcementCode };
		GestorServicos gestor = GestorServicos.manager();
		try {
			gestor.executar(userView, "DeleteAnnouncementService", args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		ISiteComponent announecementsComponent = new InfoSiteAnnouncement();
		readSiteView(request, announecementsComponent, null, null, null);
		TeacherAdministrationSiteView siteView =
			(TeacherAdministrationSiteView) request.getAttribute("siteView");
		if (!((InfoSiteAnnouncement) siteView.getComponent())
			.getAnnouncements()
			.isEmpty()) {
			return mapping.findForward("showAnnouncements");
		} else {
			session.removeAttribute("insertAnnouncementForm");
			return mapping.findForward("insertAnnouncement");
		}
	}
	//	========================  Objectives Management  ========================
	public ActionForward viewObjectives(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		ISiteComponent objectivesComponent = new InfoSiteObjectives();
		readSiteView(request, objectivesComponent, null, null, null);
		return mapping.findForward("viewObjectives");
	}
	public ActionForward prepareEditObjectives(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		ISiteComponent objectivesComponent = new InfoCurriculum();

		String curricularCourseCodeString =
			request.getParameter("curricularCourseCode");
		Integer curricularCourseCode = new Integer(curricularCourseCodeString);

		try {
			readSiteView(
				request,
				objectivesComponent,
				null,
				curricularCourseCode,
				null);
		} catch (FenixActionException e1) {
			throw e1;
		}

		TeacherAdministrationSiteView siteView =
			(TeacherAdministrationSiteView) request.getAttribute("siteView");

		if (siteView.getComponent() != null) {
			DynaActionForm objectivesForm = (DynaActionForm) form;

			objectivesForm.set(
				"generalObjectives",
				((InfoCurriculum) siteView.getComponent())
					.getGeneralObjectives());
			objectivesForm.set(
				"generalObjectivesEn",
				((InfoCurriculum) siteView.getComponent())
					.getGeneralObjectivesEn());
			objectivesForm.set(
				"operacionalObjectives",
				((InfoCurriculum) siteView.getComponent())
					.getOperacionalObjectives());
			objectivesForm.set(
				"operacionalObjectivesEn",
				((InfoCurriculum) siteView.getComponent())
					.getOperacionalObjectivesEn());
		}
		request.setAttribute("curricularCourseCode", curricularCourseCode);
		return mapping.findForward("editObjectives");
	}

	public ActionForward editObjectives(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		HttpSession session = request.getSession(false);
		Integer objectCode = getObjectCode(request);
		String curricularCourseCodeString =
			request.getParameter("curricularCourseCode");
		Integer curricularCourseCode = new Integer(curricularCourseCodeString);
		DynaActionForm objectivesForm = (DynaActionForm) form;

		InfoCurriculum infoCurriculumNew = new InfoCurriculum();

		infoCurriculumNew.setIdInternal(curricularCourseCode);
		infoCurriculumNew.setGeneralObjectives(
			(String) objectivesForm.get("generalObjectives"));
		infoCurriculumNew.setGeneralObjectivesEn(
			(String) objectivesForm.get("generalObjectivesEn"));
		infoCurriculumNew.setOperacionalObjectives(
			(String) objectivesForm.get("operacionalObjectives"));
		infoCurriculumNew.setOperacionalObjectivesEn(
			(String) objectivesForm.get("operacionalObjectivesEn"));

		Object args[] = { objectCode, curricularCourseCode, infoCurriculumNew };

		UserView userView =
			(UserView) session.getAttribute(SessionConstants.U_VIEW);
		GestorServicos serviceManager = GestorServicos.manager();
		try {
			serviceManager.executar(userView, "EditObjectives", args);

		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		return viewObjectives(mapping, form, request, response);
	}

	//	========================  Program Management  ========================
	public ActionForward viewProgram(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		ISiteComponent programComponent = new InfoSitePrograms();
		readSiteView(request, programComponent, null, null, null);
		return mapping.findForward("viewProgram");
	}

	public ActionForward prepareEditProgram(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		String curricularCourseCodeString =
			request.getParameter("curricularCourseCode");
		Integer curricularCourseCode = new Integer(curricularCourseCodeString);
		ISiteComponent programComponent = new InfoCurriculum();

		try {
			readSiteView(
				request,
				programComponent,
				null,
				curricularCourseCode,
				null);

		} catch (FenixActionException e1) {
			throw e1;
		}
		TeacherAdministrationSiteView siteView =
			(TeacherAdministrationSiteView) request.getAttribute("siteView");

		if (siteView.getComponent() != null) {
			DynaActionForm programForm = (DynaActionForm) form;
			programForm.set(
				"program",
				((InfoCurriculum) siteView.getComponent()).getProgram());
			programForm.set(
				"programEn",
				((InfoCurriculum) siteView.getComponent()).getProgramEn());
		}

		request.setAttribute("curricularCourseCode", curricularCourseCode);
		return mapping.findForward("editProgram");
	}

	public ActionForward editProgram(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		HttpSession session = request.getSession(false);
		Integer objectCode = getObjectCode(request);
		String curricularCourseCodeString =
			request.getParameter("curricularCourseCode");
		Integer curricularCourseCode = new Integer(curricularCourseCodeString);

		DynaActionForm programForm = (DynaActionForm) form;

		InfoCurriculum infoCurriculumNew = new InfoCurriculum();
		infoCurriculumNew.setIdInternal(curricularCourseCode);
		infoCurriculumNew.setProgram((String) programForm.get("program"));
		infoCurriculumNew.setProgramEn((String) programForm.get("programEn"));

		Object args[] = { objectCode, curricularCourseCode, infoCurriculumNew };
		UserView userView =
			(UserView) session.getAttribute(SessionConstants.U_VIEW);
		GestorServicos serviceManager = GestorServicos.manager();

		try {
			serviceManager.executar(userView, "EditProgram", args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		return viewProgram(mapping, form, request, response);
	}

	//	========================  EvaluationMethod Management  ========================
	public ActionForward viewEvaluationMethod(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		ISiteComponent evaluationComponent = new InfoSiteEvaluationMethods();
		readSiteView(request, evaluationComponent, null, null, null);
		return mapping.findForward("viewEvaluationMethod");
	}

	public ActionForward prepareEditEvaluationMethod(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		ISiteComponent evaluationComponent = new InfoCurriculum();

		String curricularCourseCodeString =
			request.getParameter("curricularCourseCode");
		Integer curricularCourseCode = new Integer(curricularCourseCodeString);

		try {
			readSiteView(
				request,
				evaluationComponent,
				null,
				curricularCourseCode,
				null);

		} catch (FenixActionException e1) {
			throw e1;
		}
		TeacherAdministrationSiteView siteView =
			(TeacherAdministrationSiteView) request.getAttribute("siteView");

		if (siteView.getComponent() != null) {
			DynaActionForm evaluationForm = (DynaActionForm) form;
			evaluationForm.set(
				"evaluationElements",
				((InfoCurriculum) siteView.getComponent())
					.getEvaluationElements());
			evaluationForm.set(
				"evaluationElementsEn",
				((InfoCurriculum) siteView.getComponent())
					.getEvaluationElementsEn());
		}
		request.setAttribute("curricularCourseCode", curricularCourseCode);
		return mapping.findForward("editEvaluationMethod");
	}

	public ActionForward editEvaluationMethod(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		HttpSession session = request.getSession(false);
		Integer objectCode = getObjectCode(request);
		String curricularCourseCodeString =
			request.getParameter("curricularCourseCode");
		Integer curricularCourseCode = new Integer(curricularCourseCodeString);

		DynaActionForm evaluationForm = (DynaActionForm) form;

		InfoCurriculum infoCurriculumNew = new InfoCurriculum();
		infoCurriculumNew.setIdInternal(curricularCourseCode);
		infoCurriculumNew.setEvaluationElements(
			(String) evaluationForm.get("evaluationElements"));
		infoCurriculumNew.setEvaluationElementsEn(
			(String) evaluationForm.get("evaluationElementsEn"));

		Object args[] = { objectCode, curricularCourseCode, infoCurriculumNew };

		UserView userView =
			(UserView) session.getAttribute(SessionConstants.U_VIEW);
		GestorServicos serviceManager = GestorServicos.manager();
		try {
			serviceManager.executar(userView, "EditEvaluation", args);

		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		return viewEvaluationMethod(mapping, form, request, response);
	}
	//	========================  Bibliographic References Management  ========================
	public ActionForward viewBibliographicReference(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		ISiteComponent bibliographyComponent = new InfoSiteBibliography();
		readSiteView(request, bibliographyComponent, null, null, null);
		TeacherAdministrationSiteView siteView =
			(TeacherAdministrationSiteView) request.getAttribute("siteView");
		if (((InfoSiteBibliography) siteView.getComponent())
			.getBibliographicReferences()
			!= null
			&& (!((InfoSiteBibliography) siteView.getComponent())
				.getBibliographicReferences()
				.isEmpty())) {
			return mapping.findForward("bibliographyManagement");
		} else {
			HttpSession session = request.getSession(false);
			session.removeAttribute("bibliographicReferenceForm");
			return mapping.findForward("insertBibliographicReference");
		}
	}
	public ActionForward prepareCreateBibliographicReference(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		HttpSession session = request.getSession(false);
		session.removeAttribute("bibliographicReferenceForm");
		readSiteView(request, null, null, null, null);
		return mapping.findForward("insertBibliographicReference");
	}
	public ActionForward createBibliographicReference(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		HttpSession session = request.getSession(false);
		Integer objectCode = getObjectCode(request);
		DynaActionForm insertBibliographicReferenceForm = (DynaActionForm) form;
		String title = (String) insertBibliographicReferenceForm.get("title");
		String authors =
			(String) insertBibliographicReferenceForm.get("authors");
		String reference =
			(String) insertBibliographicReferenceForm.get("reference");
		String year = (String) insertBibliographicReferenceForm.get("year");
		Boolean optional =
			new Boolean(
				(String) insertBibliographicReferenceForm.get("optional"));
		UserView userView = (UserView) session.getAttribute("UserView");
		Object args[] =
			{ objectCode, title, authors, reference, year, optional };
		GestorServicos gestor = GestorServicos.manager();
		try {
			gestor.executar(userView, "CreateBibliographicReference", args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		ISiteComponent bibliographyComponent = new InfoSiteBibliography();
		readSiteView(request, bibliographyComponent, null, null, null);
		return mapping.findForward("bibliographyManagement");
	}
	public ActionForward prepareEditBibliographicReference(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		//retrieve bibliographic reference
		String bibliographicReferenceCodeString =
			request.getParameter("bibliographicReferenceCode");
		if (bibliographicReferenceCodeString == null) {
			bibliographicReferenceCodeString =
				(String) request.getAttribute("bibliographicReferenceCode");
		}
		Integer bibliographicReferenceCode =
			new Integer(bibliographicReferenceCodeString);
		ISiteComponent bibliographyComponent = new InfoBibliographicReference();
		readSiteView(
			request,
			bibliographyComponent,
			null,
			bibliographicReferenceCode,
			null);
		return mapping.findForward("editBibliographicReference");
	}
	public ActionForward editBibliographicReference(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		HttpSession session = request.getSession(false);
		String bibliographicReferenceCodeString =
			request.getParameter("bibliographicReferenceCode");
		if (bibliographicReferenceCodeString == null) {
			bibliographicReferenceCodeString =
				(String) request.getAttribute("bibliographicReferenceCode");
		}
		Integer bibliographicReferenceCode =
			new Integer(bibliographicReferenceCodeString);
		Integer objectCode = getObjectCode(request);
		DynaActionForm editBibliographicReferenceForm = (DynaActionForm) form;
		String title = (String) editBibliographicReferenceForm.get("title");
		String authors = (String) editBibliographicReferenceForm.get("authors");
		String reference =
			(String) editBibliographicReferenceForm.get("reference");
		String year = (String) editBibliographicReferenceForm.get("year");
		//String optionalStr = (String) editBibliographicReferenceForm.get("optional");
		Boolean optional =
			new Boolean(
				(String) editBibliographicReferenceForm.get("optional"));
		//		if (optionalStr.equals(this.getResources(request).getMessage("message.optional"))) {
		//			optional = new Boolean(true);
		//		} else {
		//			optional = new Boolean(false);
		//		}
		Object args[] =
			{
				objectCode,
				bibliographicReferenceCode,
				title,
				authors,
				reference,
				year,
				optional };
		UserView userView = (UserView) session.getAttribute("UserView");
		GestorServicos gestor = GestorServicos.manager();
		try {
			gestor.executar(userView, "EditBibliographicReference", args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		ISiteComponent bibliographyComponent = new InfoSiteBibliography();
		readSiteView(request, bibliographyComponent, null, null, null);
		return mapping.findForward("bibliographyManagement");
	}
	public ActionForward deleteBibliographicReference(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		HttpSession session = request.getSession(false);
		String bibliographicReferenceCodeString =
			request.getParameter("bibliographicReferenceCode");
		if (bibliographicReferenceCodeString == null) {
			bibliographicReferenceCodeString =
				(String) request.getAttribute("bibliographicReferenceCode");
		}
		Integer bibliographicReferenceCode =
			new Integer(bibliographicReferenceCodeString);
		Integer objectCode = getObjectCode(request);
		UserView userView = (UserView) session.getAttribute("UserView");
		Object args[] = { objectCode, bibliographicReferenceCode };
		GestorServicos gestor = GestorServicos.manager();
		try {
			gestor.executar(userView, "DeleteBibliographicReference", args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		return viewBibliographicReference(mapping, form, request, response);
	}
	//	========================  Teachers Management  ========================
	public ActionForward viewTeachersByProfessorship(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		String username = getUsername(request);
		ISiteComponent teachersComponent = new InfoSiteTeachers();
		readSiteView(request, teachersComponent, null, null, username);
		return mapping.findForward("viewTeachers");
	}
	private String getUsername(HttpServletRequest request)
		throws InvalidSessionActionException {
		HttpSession session = getSession(request);
		UserView userView =
			(UserView) session.getAttribute(SessionConstants.U_VIEW);
		String username = userView.getUtilizador();
		return username;
	}
	public ActionForward prepareAssociateTeacher(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		HttpSession session = request.getSession(false);
		session.removeAttribute("teacherForm");
		readSiteView(request, null, null, null, null);
		return mapping.findForward("associateTeacher");
	}
	public ActionForward associateTeacher(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		HttpSession session = getSession(request);
		Integer objectCode = getObjectCode(request);
		DynaActionForm teacherForm = (DynaActionForm) form;
		Integer teacherNumber =
			new Integer((String) teacherForm.get("teacherNumber"));
		UserView userView = (UserView) session.getAttribute("UserView");
		Object args[] = { objectCode, teacherNumber };
		GestorServicos gestor = GestorServicos.manager();
		try {
			gestor.executar(userView, "AssociateTeacher", args);
		} catch (InvalidArgumentsServiceException e) {
			throw new InvalidArgumentsActionException(
				teacherNumber.toString(),
				e);
		} catch (ExistingServiceException e) {
			throw new ExistingActionException(teacherNumber.toString(), e);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		return viewTeachersByProfessorship(mapping, form, request, response);
	}
	public ActionForward removeTeacher(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		HttpSession session = getSession(request);
		String teacherCodeString = request.getParameter("teacherCode");
		if (teacherCodeString == null) {
			teacherCodeString = (String) request.getAttribute("teacherCode");
		}
		Integer teacherCode = new Integer(teacherCodeString);
		Integer objectCode = getObjectCode(request);
		UserView userView = (UserView) session.getAttribute("UserView");
		Object args[] = { objectCode, teacherCode };
		GestorServicos gestor = GestorServicos.manager();
		try {
			gestor.executar(userView, "DeleteTeacher", args);
		} catch (notAuthorizedServiceDeleteException ee) {
			throw new notAuthorizedActionDeleteException("error.invalidTeacherRemoval");
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		return viewTeachersByProfessorship(mapping, form, request, response);
	}
	//	========================  Evaluation Management  ========================
	public ActionForward viewEvaluation(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		ISiteComponent evaluationComponent = new InfoSiteEvaluation();
		readSiteView(request, evaluationComponent, null, null, null);
		return mapping.findForward("viewEvaluation");
	}
	//	========================  Sections Management  ========================
	public ActionForward sectionsFirstPage(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		readSiteView(request, null, null, null, null);
		return mapping.findForward("sectionsFirstPage");
	}
	public ActionForward viewSection(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		Integer sectionCode = getSectionCode(request);
		return viewSection(mapping, form, request, response, sectionCode);
	}
	public ActionForward viewSection(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response,
		Integer sectionCode)
		throws FenixActionException {
		ISiteComponent sectionComponent = new InfoSiteSection();
		readSiteView(request, sectionComponent, null, sectionCode, null);
		return mapping.findForward("viewSection");
	}
	public ActionForward prepareCreateRegularSection(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		Integer sectionCode = getSectionCode(request);
		ISiteComponent regularSectionsComponent = new InfoSiteRegularSections();
		readSiteView(
			request,
			regularSectionsComponent,
			null,
			sectionCode,
			null);
		request.setAttribute("currentSectionCode", sectionCode);
		return mapping.findForward("createSection");
	}
	private Integer getSectionCode(HttpServletRequest request) {
		Integer sectionCode = null;
		String sectionCodeString = request.getParameter("currentSectionCode");
		if (sectionCodeString == null) {
			sectionCodeString =
				(String) request.getAttribute("currentSectionCode");
		}
		if (sectionCodeString != null) {
			sectionCode = new Integer(sectionCodeString);
		}
		return sectionCode;
	}
	public ActionForward prepareCreateRootSection(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		ISiteComponent rootSectionsComponent = new InfoSiteRootSections();
		readSiteView(request, rootSectionsComponent, null, null, null);
		return mapping.findForward("createSection");
	}
	public ActionForward createSection(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		HttpSession session = request.getSession();
		Integer sectionCode = getSectionCode(request);
		Integer objectCode = getObjectCode(request);
		DynaActionForm dynaForm = (DynaValidatorForm) form;
		String sectionName = (String) dynaForm.get("name");
		Integer order = Integer.valueOf((String) dynaForm.get("sectionOrder"));
		UserView userView =
			(UserView) session.getAttribute(SessionConstants.U_VIEW);
		Object args[] = { objectCode, sectionCode, sectionName, order };
		GestorServicos manager = GestorServicos.manager();
		try {
			manager.executar(userView, "InsertSection", args);
		} catch (ExistingServiceException e) {
			throw new ExistingActionException("Uma sec��o com esse nome", e);
		} catch (FenixServiceException fenixServiceException) {
			throw new FenixActionException(fenixServiceException.getMessage());
		}
		return sectionsFirstPage(mapping, form, request, response);
	}
	public ActionForward prepareEditSection(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		Integer sectionCode = getSectionCode(request);
		ISiteComponent sectionsComponent = new InfoSiteSections();
		readSiteView(request, sectionsComponent, null, sectionCode, null);
		return mapping.findForward("editSection");
	}
	public ActionForward editSection(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		HttpSession session = request.getSession();
		Integer sectionCode = getSectionCode(request);
		Integer objectCode = getObjectCode(request);
		DynaActionForm sectionForm = (DynaValidatorForm) form;
		String sectionName = (String) sectionForm.get("name");
		Integer order = (Integer) sectionForm.get("sectionOrder");
		order = new Integer(order.intValue() - 1);
		UserView userView =
			(UserView) session.getAttribute(SessionConstants.U_VIEW);
		GestorServicos manager = GestorServicos.manager();
		Object editionArgs[] = { objectCode, sectionCode, sectionName, order };
		try {
			manager.executar(userView, "EditSection", editionArgs);
		} catch (ExistingServiceException ex) {
			throw new ExistingActionException(sectionName, ex);
		} catch (NonExistingServiceException ex) {
			throw new NonExistingActionException(sectionName, ex);
		} catch (FenixServiceException fenixServiceException) {
			throw new FenixActionException(fenixServiceException);
		}
		return sectionsFirstPage(mapping, form, request, response);
	}
	public ActionForward deleteSection(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		HttpSession session = request.getSession(false);
		Integer superiorSectionCode = getSuperiorSectionCode(request);
		Integer sectionCode = getSectionCode(request);
		Integer objectCode = getObjectCode(request);
		UserView userView =
			(UserView) session.getAttribute(SessionConstants.U_VIEW);
		Object deleteSectionArguments[] = { objectCode, sectionCode };
		GestorServicos manager = GestorServicos.manager();
		try {
			manager.executar(userView, "DeleteSection", deleteSectionArguments);
			//			if this section has a parent section
			if (superiorSectionCode != null) {
				return viewSection(
					mapping,
					form,
					request,
					response,
					superiorSectionCode);
			} else {
				return sectionsFirstPage(mapping, form, request, response);
			}
		} catch (FenixServiceException fenixServiceException) {
			throw new FenixActionException(fenixServiceException.getMessage());
		} catch (Exception e) {
			throw new FenixActionException(e.getMessage());
		}
	}
	private Integer getSuperiorSectionCode(HttpServletRequest request) {
		Integer sectionCode = null;
		String sectionCodeString = request.getParameter("superiorSectionCode");
		if (sectionCodeString == null) {
			sectionCodeString =
				(String) request.getAttribute("superiorSectionCode");
		}
		if (sectionCodeString != null) {
			sectionCode = new Integer(sectionCodeString);
		}
		return sectionCode;
	}

	public ActionForward prepareFileUpload(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		Integer itemCode = getItemCode(request);
		ISiteComponent itemsComponent = new InfoSiteItems();
		readSiteView(request, itemsComponent, null, itemCode, null);
		return mapping.findForward("uploadItemFile");
	}

	public ActionForward fileUpload(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		Integer itemCode = getItemCode(request);
		DynaActionForm xptoForm = (DynaActionForm) form;
		FormFile formFile = (FormFile) xptoForm.get("theFile");
		Integer objectCode = getObjectCode(request);
		FileSuportObject file = null;
		try {
			file = new FileSuportObject();
			file.setContent(formFile.getFileData());
			file.setFileName(formFile.getFileName());
			file.setContentType(formFile.getContentType());
			file.setLinkName((String) xptoForm.get("linkName"));

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}

		HttpSession session = request.getSession(false);
		IUserView userView =
			(IUserView) session.getAttribute(SessionConstants.U_VIEW);
		Object[] args = { file, itemCode };
		Boolean serviceResult = null;
		ActionErrors actionErrors = new ActionErrors();
		try {
			serviceResult =
				(Boolean) ServiceUtils.executeService(
					userView,
					"StoreItemFile",
					args);
		} catch (FenixServiceException e1) {
			actionErrors.add(
				"unableToStoreFile",
				new ActionError(
					"errors.unableToStoreFile",
					file.getFileName()));
			saveErrors(request, actionErrors);
			return prepareFileUpload(mapping, form,request, response);
			
		}
		if (!serviceResult.booleanValue()) {
			actionErrors.add(
				"fileTooBig",
				new ActionError("errors.fileTooBig", file.getFileName()));
			saveErrors(request, actionErrors);
			return prepareFileUpload(mapping, form,request, response);
		}
		return viewSection(mapping, form, request, response);
	}

	public ActionForward deleteFile(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		Integer itemCode = getItemCode(request);
		String fileName = request.getParameter("fileName");
		HttpSession session = request.getSession(false);
		IUserView userView =
			(IUserView) session.getAttribute(SessionConstants.U_VIEW);
		Object[] args = { itemCode, fileName };
		try {
			ServiceUtils.executeService(userView, "DeleteItemFile", args);
		} catch (FenixServiceException e1) {
			// TODO Auto-generated catch block
			//	e1.printStackTrace();
		}

		return viewSection(mapping, form, request, response);
	}

	public ActionForward prepareInsertItem(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		Integer sectionCode = getSectionCode(request);
		ISiteComponent sectionComponent = new InfoSiteSection();
		readSiteView(request, sectionComponent, null, sectionCode, null);
		return mapping.findForward("insertItem");
	}

	public ActionForward insertItem(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		HttpSession session = request.getSession(false);
		Integer sectionCode = getSectionCode(request);
		Integer objectCode = getObjectCode(request);
		DynaActionForm dynaForm = (DynaValidatorForm) form;
		Integer order = new Integer((String) dynaForm.get("itemOrder"));
		String itemName = (String) dynaForm.get("name");
		String information = (String) dynaForm.get("information");
		String urgentString = (String) dynaForm.get("urgent");
		InfoItem newInfoItem = new InfoItem();
		newInfoItem.setItemOrder(order);
		newInfoItem.setName(itemName);
		newInfoItem.setInformation(information);
		newInfoItem.setUrgent(new Boolean(urgentString));
		UserView userView =
			(UserView) session.getAttribute(SessionConstants.U_VIEW);
		Object args[] = { objectCode, sectionCode, newInfoItem };
		GestorServicos manager = GestorServicos.manager();
		try {
			manager.executar(userView, "InsertItem", args);
		} catch (ExistingServiceException e) {
			throw new ExistingActionException("Um item com esse nome", e);
		} catch (FenixServiceException fenixServiceException) {
			throw new FenixActionException(fenixServiceException.getMessage());
		}
		return viewSection(mapping, form, request, response);
	}
	public ActionForward prepareEditItem(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		Integer itemCode = getItemCode(request);
		ISiteComponent itemsComponent = new InfoSiteItems();
		readSiteView(request, itemsComponent, null, itemCode, null);
		return mapping.findForward("editItem");
	}
	private Integer getItemCode(HttpServletRequest request) {
		Integer itemCode = null;
		String itemCodeString = request.getParameter("itemCode");
		if (itemCodeString == null) {
			itemCodeString = (String) request.getAttribute("itemCode");
		}
		if (itemCodeString != null) {
			itemCode = new Integer(itemCodeString);
		}
		return itemCode;
	}
	public ActionForward editItem(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		HttpSession session = request.getSession(false);
		Integer itemCode = getItemCode(request);
		Integer objectCode = getObjectCode(request);
		DynaActionForm itemForm = (DynaActionForm) form;
		String information = (String) itemForm.get("information");
		String name = (String) itemForm.get("name");
		Boolean urgent = new Boolean((String) itemForm.get("urgent"));
		Integer itemOrder = new Integer((String) itemForm.get("itemOrder"));
		itemOrder = new Integer(itemOrder.intValue() - 1);
		InfoItem newInfoItem = new InfoItem();
		newInfoItem.setInformation(information);
		newInfoItem.setName(name);
		newInfoItem.setItemOrder(itemOrder);
		newInfoItem.setUrgent(urgent);
		UserView userView =
			(UserView) session.getAttribute(SessionConstants.U_VIEW);
		Object editItemArgs[] = { objectCode, itemCode, newInfoItem };
		GestorServicos manager = GestorServicos.manager();
		try {
			manager.executar(userView, "EditItem", editItemArgs);
		} catch (ExistingServiceException e) {
			throw new ExistingActionException("Um item com esse nome", e);
		} catch (FenixServiceException fenixServiceException) {
			throw new FenixActionException(fenixServiceException.getMessage());
		}
		return viewSection(mapping, form, request, response);
	}
	public ActionForward deleteItem(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		HttpSession session = request.getSession(false);
		Integer itemCode = getItemCode(request);
		Integer objectCode = getObjectCode(request);
		UserView userView =
			(UserView) session.getAttribute(SessionConstants.U_VIEW);
		Object deleteItemArguments[] = { objectCode, itemCode };
		GestorServicos manager = GestorServicos.manager();
		try {
			manager.executar(userView, "DeleteItem", deleteItemArguments);
		} catch (FenixServiceException fenixServiceException) {
			throw new FenixActionException(fenixServiceException.getMessage());
		}
		return viewSection(mapping, form, request, response);
	}
	public ActionForward validationError(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		SiteManagementActionMapping siteManagementActionMapping =
			(SiteManagementActionMapping) mapping;
		ISiteComponent siteComponent =
			getSiteComponentForValidationError(siteManagementActionMapping);
		Integer infoExecutionCourseCode = null;
		Object obj1 = null;
		Object obj2 = null;
		if (siteComponent instanceof InfoSiteItems) {
			obj1 = getItemCode(request);
		} else if (siteComponent instanceof InfoSiteTeachers) {
			obj2 = getUsername(request);
		} else if (siteComponent instanceof InfoSiteRegularSections) {
			obj1 = getSectionCode(request);
		} else if (siteComponent instanceof InfoAnnouncement) {
			obj1 = getAnnouncementCode(request);
		} else if (siteComponent instanceof InfoSiteSections) {
			obj1 = getSectionCode(request);
		} else if (siteComponent instanceof InfoSiteSection) {
			obj1 = getSectionCode(request);
		}
		readSiteView(
			request,
			siteComponent,
			infoExecutionCourseCode,
			obj1,
			obj2);
		return mapping.findForward(
			siteManagementActionMapping.getInputForwardName());
	}
	private ISiteComponent getSiteComponentForValidationError(SiteManagementActionMapping mapping) {
		ISiteComponent siteComponent = null;
		String className = mapping.getComponentClassName();
		try {
			Class componentClass =
				this.getClass().getClassLoader().loadClass(className);
			Constructor c = componentClass.getConstructor(new Class[] {
			});
			c.setAccessible(true);
			siteComponent = (ISiteComponent) c.newInstance(new Object[] {
			});
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return siteComponent;
	}
	private Integer getObjectCode(HttpServletRequest request) {
		Integer objectCode = null;
		String objectCodeString = request.getParameter("objectCode");
		if (objectCodeString == null) {
			objectCodeString = (String) request.getAttribute("objectCode");
		}
		if (objectCodeString != null) {
			objectCode = new Integer(objectCodeString);
		}
		return objectCode;
	}
	private SiteView readSiteView(
		HttpServletRequest request,
		ISiteComponent firstPageComponent,
		Integer infoExecutionCourseCode,
		Object obj1,
		Object obj2)
		throws FenixActionException {
		HttpSession session = getSession(request);
		UserView userView =
			(UserView) session.getAttribute(SessionConstants.U_VIEW);
		Integer objectCode = null;
		if (infoExecutionCourseCode == null) {
			objectCode = getObjectCode(request);
			infoExecutionCourseCode = objectCode;
		}
		ISiteComponent commonComponent = new InfoSiteCommon();
		Object[] args =
			{
				infoExecutionCourseCode,
				commonComponent,
				firstPageComponent,
				objectCode,
				obj1,
				obj2 };

		try {
			TeacherAdministrationSiteView siteView =
				(TeacherAdministrationSiteView) ServiceUtils.executeService(
					userView,
					"TeacherAdministrationSiteComponentService",
					args);
			request.setAttribute("siteView", siteView);
			request.setAttribute(
				"objectCode",
				((InfoSiteCommon) siteView.getCommonComponent())
					.getExecutionCourse()
					.getIdInternal());
			if (siteView.getComponent() instanceof InfoSiteSection) {
				request.setAttribute(
					"infoSection",
					((InfoSiteSection) siteView.getComponent()).getSection());
			}

			return siteView;

		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

	}

	//	========================  Tests Management  ========================
	public ActionForward testsFirstPage(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		readSiteView(request, null, null, null, null);
		return mapping.findForward("testsFirstPage");
	}
	public ActionForward createTest(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		readSiteView(request, null, null, null, null);
		return mapping.findForward("createTest");
	}
	public ActionForward showSummaries(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		String executionCourseIdString = request.getParameter("objectCode");
		Integer executionCourseId = new Integer(executionCourseIdString);
		HttpSession session = request.getSession(false);
		IUserView userView =
			(IUserView) session.getAttribute(SessionConstants.U_VIEW);
		Object[] args = { executionCourseId };
		SiteView siteView = null;
		try {
			siteView =
				(SiteView) ServiceUtils.executeService(
					userView,
					"ReadSummaries",
					args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		Collections.sort(
			((InfoSiteSummaries) ((ExecutionCourseSiteView) siteView)
				.getComponent())
				.getInfoSummaries());
		request.setAttribute("siteView", siteView);
		return mapping.findForward("showSummaries");
	}
	public ActionForward prepareInsertSummary(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		readSiteView(request, null, null, null, null);
		List lessonTypeValues = new ArrayList();
		lessonTypeValues.add(new Integer(1));
		lessonTypeValues.add(new Integer(2));
		lessonTypeValues.add(new Integer(3));
		lessonTypeValues.add(new Integer(4));
		List lessonTypeNames = new ArrayList();
		lessonTypeNames.add("Te�rica");
		lessonTypeNames.add("Pr�tica");
		lessonTypeNames.add("Te�rico-Pr�tica");
		lessonTypeNames.add("Laboratorial");
		request.setAttribute("lessonTypeValues", lessonTypeValues);
		request.setAttribute("lessonTypeNames", lessonTypeNames);
		return mapping.findForward("insertSummary");
	}
	public ActionForward insertSummary(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		String executionCourseIdString = request.getParameter("objectCode");
		Integer executionCourseId = new Integer(executionCourseIdString);
		HttpSession session = request.getSession(false);
		IUserView userView =
			(IUserView) session.getAttribute(SessionConstants.U_VIEW);
		String summaryDateString = request.getParameter("summaryDate");
		String summaryHourString = request.getParameter("summaryHour");
		String summaryType = request.getParameter("summaryType");
		String title = request.getParameter("title");
		String summaryText = request.getParameter("summaryText");
		String[] dateTokens = summaryDateString.split("/");
		Calendar summaryDate = Calendar.getInstance();
		summaryDate.set(
			Calendar.DAY_OF_MONTH,
			(new Integer(dateTokens[0])).intValue());
		summaryDate.set(
			Calendar.MONTH,
			(new Integer(dateTokens[1])).intValue() - 1);
		summaryDate.set(Calendar.YEAR, (new Integer(dateTokens[2])).intValue());
		String[] hourTokens = summaryHourString.split(":");
		Calendar summaryHour = Calendar.getInstance();
		summaryHour.set(
			Calendar.HOUR_OF_DAY,
			(new Integer(hourTokens[0])).intValue());
		summaryHour.set(
			Calendar.MINUTE,
			(new Integer(hourTokens[1])).intValue());
		Object[] args =
			{
				executionCourseId,
				summaryDate,
				summaryHour,
				new Integer(summaryType),
				title,
				summaryText };
		try {
			ServiceUtils.executeService(userView, "InsertSummary", args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		return showSummaries(mapping, form, request, response);
	}
	public ActionForward deleteSummary(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		String summaryIdString = request.getParameter("summaryCode");
		Integer summaryId = new Integer(summaryIdString);
		HttpSession session = request.getSession(false);
		IUserView userView =
			(IUserView) session.getAttribute(SessionConstants.U_VIEW);
		String executionCourseIdString = request.getParameter("objectCode");
		Integer executionCourseId = new Integer(executionCourseIdString);
		Object[] args = { executionCourseId, summaryId };
		try {
			ServiceUtils.executeService(userView, "DeleteSummary", args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		return showSummaries(mapping, form, request, response);
	}
	public ActionForward prepareEditSummary(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		String summaryIdString = request.getParameter("summaryCode");
		Integer summaryId = new Integer(summaryIdString);
		HttpSession session = request.getSession(false);
		IUserView userView =
			(IUserView) session.getAttribute(SessionConstants.U_VIEW);
		String executionCourseIdString = request.getParameter("objectCode");
		Integer executionCourseId = new Integer(executionCourseIdString);
		Object[] args = { executionCourseId, summaryId };
		SiteView siteView = null;
		try {
			siteView =
				(SiteView) ServiceUtils.executeService(
					userView,
					"ReadSummary",
					args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		List lessonTypeValues = new ArrayList();
		lessonTypeValues.add(new Integer(1));
		lessonTypeValues.add(new Integer(2));
		lessonTypeValues.add(new Integer(3));
		lessonTypeValues.add(new Integer(4));
		List lessonTypeNames = new ArrayList();
		lessonTypeNames.add("Te�rica");
		lessonTypeNames.add("Pr�tica");
		lessonTypeNames.add("Te�rico-Pr�tica");
		lessonTypeNames.add("Laboratorial");
		Integer summaryType =
			((InfoSiteSummary) siteView.getComponent())
				.getInfoSummary()
				.getSummaryType()
				.getTipo();
		lessonTypeValues.remove(summaryType.intValue() - 1);
		String summaryTypeName =
			lessonTypeNames.remove(summaryType.intValue() - 1).toString();
		request.setAttribute("summaryTypeName", summaryTypeName);
		request.setAttribute("summaryTypeValue", summaryType);
		request.setAttribute("lessonTypeValues", lessonTypeValues);
		request.setAttribute("lessonTypeNames", lessonTypeNames);
		request.setAttribute("siteView", siteView);
		return mapping.findForward("editSummary");
	}
	public ActionForward editSummary(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		String summaryIdString = request.getParameter("summaryCode");
		String executionCourseIdString = request.getParameter("objectCode");
		Integer executionCourseId = new Integer(executionCourseIdString);
		Integer summaryId = new Integer(summaryIdString);
		HttpSession session = request.getSession(false);
		IUserView userView =
			(IUserView) session.getAttribute(SessionConstants.U_VIEW);
		String summaryDateString = request.getParameter("summaryDateFormatted");
		String summaryHourString = request.getParameter("summaryHourFormatted");
		String summaryType = request.getParameter("summaryType");
		String title = request.getParameter("title");
		String summaryText = request.getParameter("summaryText");
		String[] dateTokens = summaryDateString.split("/");
		Calendar summaryDate = Calendar.getInstance();
		summaryDate.set(
			Calendar.DAY_OF_MONTH,
			(new Integer(dateTokens[0])).intValue());
		summaryDate.set(
			Calendar.MONTH,
			(new Integer(dateTokens[1])).intValue() - 1);
		summaryDate.set(Calendar.YEAR, (new Integer(dateTokens[2])).intValue());
		String[] hourTokens = summaryHourString.split(":");
		Calendar summaryHour = Calendar.getInstance();
		summaryHour.set(
			Calendar.HOUR_OF_DAY,
			(new Integer(hourTokens[0])).intValue());
		summaryHour.set(
			Calendar.MINUTE,
			(new Integer(hourTokens[1])).intValue());
		Object[] args =
			{
				executionCourseId,
				summaryId,
				summaryDate,
				summaryHour,
				new Integer(summaryType),
				title,
				summaryText };
		try {
			ServiceUtils.executeService(userView, "EditSummary", args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		return showSummaries(mapping, form, request, response);
	}

	//	========================  GROUPS MANAGEMENT  ========================

	public ActionForward viewExecutionCourseProjects(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		ISiteComponent viewProjectsComponent = new InfoSiteProjects();
		readSiteView(request, viewProjectsComponent, null, null, null);
		return mapping.findForward("viewProjectsAndLink");
	}
	public ActionForward viewProjectStudentGroups(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		String groupPropertiesCodeString =
			(String) request.getParameter("groupPropertiesCode");
		Integer groupPropertiesCode = new Integer(groupPropertiesCodeString);
		Integer objectCode = getObjectCode(request);

		ISiteComponent shiftsView = new InfoSiteShifts();
		TeacherAdministrationSiteView siteView =
			(TeacherAdministrationSiteView) readSiteView(request,
				shiftsView,
				null,
				objectCode,
				null);

		List shifts =
			(List) ((InfoSiteShifts) siteView.getComponent()).getShifts();
		if (shifts.size() == 0) {

			request.setAttribute("noShifts", new Boolean(true));
		}

		ISiteComponent viewAllGroups = new InfoSiteAllGroups();
		readSiteView(request, viewAllGroups, null, groupPropertiesCode, null);

		return mapping.findForward("groupsManagement");

	}
	public ActionForward viewStudentGroupInformation(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		String studentGroupCodeString =
			(String) request.getParameter("studentGroupCode");
		Integer studentGroupCode = new Integer(studentGroupCodeString);

		ISiteComponent viewStudentGroup = new InfoSiteStudentGroup();
		TeacherAdministrationSiteView result =
			(TeacherAdministrationSiteView) readSiteView(request,
				viewStudentGroup,
				null,
				studentGroupCode,
				null);

		InfoSiteStudentGroup infoSiteStudentGroup =
			(InfoSiteStudentGroup) result.getComponent();
		if (infoSiteStudentGroup.getInfoSiteStudentInformationList() == null) {
			ActionErrors actionErrors = new ActionErrors();
			ActionError error = null;
			error = new ActionError("error.noGroup");
			actionErrors.add("error.noGroup", error);
			saveErrors(request, actionErrors);
			return viewProjectStudentGroups(mapping, form, request, response);
		}
		return mapping.findForward("viewStudentGroupInformation");
	}
	public ActionForward prepareCreateGroupProperties(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		readSiteView(request, null, null, null, null);
		List shiftTypeValues = new ArrayList();
		shiftTypeValues.add(new Integer(1));
		shiftTypeValues.add(new Integer(2));
		shiftTypeValues.add(new Integer(3));
		shiftTypeValues.add(new Integer(4));
		List shiftTypeNames = new ArrayList();
		shiftTypeNames.add("Te�rico");
		shiftTypeNames.add("Pr�tico");
		shiftTypeNames.add("Te�rico-Pr�tico");
		shiftTypeNames.add("Laboratorial");
		request.setAttribute("shiftTypeValues", shiftTypeValues);
		request.setAttribute("shiftTypeNames", shiftTypeNames);
		return mapping.findForward("insertGroupProperties");
	}
	public ActionForward createGroupProperties(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		HttpSession session = request.getSession(false);
		DynaActionForm insertGroupPropertiesForm = (DynaActionForm) form;
		UserView userView = (UserView) session.getAttribute("UserView");
		String name = (String) insertGroupPropertiesForm.get("name");
		String projectDescription =
			(String) insertGroupPropertiesForm.get("projectDescription");
		String maximumCapacity =
			(String) insertGroupPropertiesForm.get("maximumCapacity");
		String minimumCapacity =
			(String) insertGroupPropertiesForm.get("minimumCapacity");
		String idealCapacity =
			(String) insertGroupPropertiesForm.get("idealCapacity");
		String groupMaximumNumber =
			(String) insertGroupPropertiesForm.get("groupMaximumNumber");
		String enrolmentBeginDayString =
			(String) insertGroupPropertiesForm.get("enrolmentBeginDay");
		String enrolmentEndDayString =
			(String) insertGroupPropertiesForm.get("enrolmentEndDay");
		String shiftType = (String) insertGroupPropertiesForm.get("shiftType");
		Boolean optional =
			new Boolean(
				(String) insertGroupPropertiesForm.get("enrolmentPolicy"));
		InfoGroupProperties infoGroupProperties = new InfoGroupProperties();
		infoGroupProperties.setName(name);
		infoGroupProperties.setProjectDescription(projectDescription);
		infoGroupProperties.setShiftType(new TipoAula(new Integer(shiftType)));
		if (!maximumCapacity.equals(""))
			infoGroupProperties.setMaximumCapacity(
				new Integer(maximumCapacity));
		if (!groupMaximumNumber.equals(""))
			infoGroupProperties.setGroupMaximumNumber(
				new Integer(groupMaximumNumber));
		if (!idealCapacity.equals(""))
			infoGroupProperties.setIdealCapacity(new Integer(idealCapacity));
		if (!minimumCapacity.equals(""))
			infoGroupProperties.setMinimumCapacity(
				new Integer(minimumCapacity));
		EnrolmentGroupPolicyType enrolmentPolicy;
		if (optional.booleanValue())
			enrolmentPolicy = new EnrolmentGroupPolicyType(1);
		else
			enrolmentPolicy = new EnrolmentGroupPolicyType(2);
		infoGroupProperties.setEnrolmentPolicy(enrolmentPolicy);
		Calendar enrolmentBeginDay = null;
		if (!enrolmentBeginDayString.equals("")) {
			String[] beginDate = enrolmentBeginDayString.split("/");
			enrolmentBeginDay = Calendar.getInstance();
			enrolmentBeginDay.set(
				Calendar.DAY_OF_MONTH,
				(new Integer(beginDate[0])).intValue());
			enrolmentBeginDay.set(
				Calendar.MONTH,
				(new Integer(beginDate[1])).intValue() - 1);
			enrolmentBeginDay.set(
				Calendar.YEAR,
				(new Integer(beginDate[2])).intValue());
		}
		infoGroupProperties.setEnrolmentBeginDay(enrolmentBeginDay);
		Calendar enrolmentEndDay = null;
		if (!enrolmentEndDayString.equals("")) {
			String[] endDate = enrolmentEndDayString.split("/");
			enrolmentEndDay = Calendar.getInstance();
			enrolmentEndDay.set(
				Calendar.DAY_OF_MONTH,
				(new Integer(endDate[0])).intValue());
			enrolmentEndDay.set(
				Calendar.MONTH,
				(new Integer(endDate[1])).intValue() - 1);
			enrolmentEndDay.set(
				Calendar.YEAR,
				(new Integer(endDate[2])).intValue());
		}
		infoGroupProperties.setEnrolmentEndDay(enrolmentEndDay);
		Integer objectCode = getObjectCode(request);
		Object args[] = { objectCode, infoGroupProperties };
		GestorServicos gestor = GestorServicos.manager();
		try {
			gestor.executar(userView, "CreateGroupProperties", args);
		} catch (ExistingServiceException e) {
			ActionErrors actionErrors = new ActionErrors();
			ActionError error = null;
			error = new ActionError("error.exception.existing.groupProperties");
			actionErrors.add("error.exception.existing.groupProperties", error);
			saveErrors(request, actionErrors);
			return prepareCreateGroupProperties(
				mapping,
				form,
				request,
				response);
		} catch (FenixServiceException fenixServiceException) {
			throw new FenixActionException(fenixServiceException.getMessage());
		}
		return viewExecutionCourseProjects(mapping, form, request, response);
	}
	public ActionForward prepareEditGroupProperties(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		String groupPropertiesCodeString =
			(String) request.getParameter("groupPropertiesCode");
		Integer groupPropertiesCode = new Integer(groupPropertiesCodeString);
		ISiteComponent viewGroupProperties = new InfoSiteGroupProperties();
		SiteView siteView =
			readSiteView(
				request,
				viewGroupProperties,
				null,
				groupPropertiesCode,
				null);

		List shiftTypeValues = new ArrayList();
		shiftTypeValues.add(new Integer(1));
		shiftTypeValues.add(new Integer(2));
		shiftTypeValues.add(new Integer(3));
		shiftTypeValues.add(new Integer(4));

		List shiftTypeNames = new ArrayList();
		shiftTypeNames.add("Te�rica");
		shiftTypeNames.add("Pr�tica");
		shiftTypeNames.add("Te�rico-Pr�tica");
		shiftTypeNames.add("Laboratorial");

		List enrolmentPolicyValues = new ArrayList();
		enrolmentPolicyValues.add(new Integer(1));
		enrolmentPolicyValues.add(new Integer(2));

		List enrolmentPolicyNames = new ArrayList();
		enrolmentPolicyNames.add("At�mica");
		enrolmentPolicyNames.add("Individual");

		InfoGroupProperties infoGroupProperties =
			((InfoSiteGroupProperties) siteView.getComponent())
				.getInfoGroupProperties();

		Integer enrolmentPolicy =
			infoGroupProperties.getEnrolmentPolicy().getType();

		enrolmentPolicyValues.remove(enrolmentPolicy.intValue() - 1);
		String enrolmentPolicyName =
			enrolmentPolicyNames
				.remove(enrolmentPolicy.intValue() - 1)
				.toString();

		Integer shiftType = infoGroupProperties.getShiftType().getTipo();
		shiftTypeValues.remove(shiftType.intValue() - 1);
		String shiftTypeName =
			shiftTypeNames.remove(shiftType.intValue() - 1).toString();

		request.setAttribute("shiftTypeName", shiftTypeName);
		request.setAttribute("shiftTypeValue", shiftType);
		request.setAttribute("shiftTypeValues", shiftTypeValues);
		request.setAttribute("shiftTypeNames", shiftTypeNames);
		request.setAttribute("enrolmentPolicyName", enrolmentPolicyName);
		request.setAttribute("enrolmentPolicyValue", enrolmentPolicy);
		request.setAttribute("enrolmentPolicyValues", enrolmentPolicyValues);
		request.setAttribute("enrolmentPolicyNames", enrolmentPolicyNames);

		return mapping.findForward("editGroupProperties");
	}
	public ActionForward editGroupProperties(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		HttpSession session = request.getSession(false);
		DynaActionForm editGroupPropertiesForm = (DynaActionForm) form;
		String groupPropertiesString =
			request.getParameter("groupPropertiesCode");
		Integer groupPropertiesCode = new Integer(groupPropertiesString);
		String name = (String) editGroupPropertiesForm.get("name");
		String projectDescription =
			(String) editGroupPropertiesForm.get("projectDescription");
		String maximumCapacity =
			(String) editGroupPropertiesForm.get("maximumCapacity");
		String minimumCapacity =
			(String) editGroupPropertiesForm.get("minimumCapacity");
		String idealCapacity =
			(String) editGroupPropertiesForm.get("idealCapacity");
		String groupMaximumNumber =
			(String) editGroupPropertiesForm.get("groupMaximumNumber");
		String enrolmentBeginDayString =
			(String) editGroupPropertiesForm.get("enrolmentBeginDayFormatted");
		String enrolmentEndDayString =
			(String) editGroupPropertiesForm.get("enrolmentEndDayFormatted");
		String shiftType = (String) editGroupPropertiesForm.get("shiftType");
		String enrolmentPolicy =
			(String) editGroupPropertiesForm.get("enrolmentPolicy");
		Calendar enrolmentBeginDay = null;
		if (!enrolmentBeginDayString.equals("")) {
			String[] beginDate = enrolmentBeginDayString.split("/");
			enrolmentBeginDay = Calendar.getInstance();
			enrolmentBeginDay.set(
				Calendar.DAY_OF_MONTH,
				(new Integer(beginDate[0])).intValue());
			enrolmentBeginDay.set(
				Calendar.MONTH,
				(new Integer(beginDate[1])).intValue() - 1);
			enrolmentBeginDay.set(
				Calendar.YEAR,
				(new Integer(beginDate[2])).intValue());
		}
		Calendar enrolmentEndDay = null;
		if (!enrolmentEndDayString.equals("")) {
			String[] endDate = enrolmentEndDayString.split("/");
			enrolmentEndDay = Calendar.getInstance();
			enrolmentEndDay.set(
				Calendar.DAY_OF_MONTH,
				(new Integer(endDate[0])).intValue());
			enrolmentEndDay.set(
				Calendar.MONTH,
				(new Integer(endDate[1])).intValue() - 1);
			enrolmentEndDay.set(
				Calendar.YEAR,
				(new Integer(endDate[2])).intValue());
		}
		UserView userView = (UserView) session.getAttribute("UserView");
		InfoGroupProperties infoGroupProperties = new InfoGroupProperties();
		infoGroupProperties.setIdInternal(groupPropertiesCode);
		infoGroupProperties.setEnrolmentBeginDay(enrolmentBeginDay);
		infoGroupProperties.setEnrolmentEndDay(enrolmentEndDay);
		infoGroupProperties.setEnrolmentPolicy(
			new EnrolmentGroupPolicyType(new Integer(enrolmentPolicy)));
		if (!groupMaximumNumber.equals(""))
			infoGroupProperties.setGroupMaximumNumber(
				new Integer(groupMaximumNumber));
		if (!idealCapacity.equals(""))
			infoGroupProperties.setIdealCapacity(new Integer(idealCapacity));
		if (!maximumCapacity.equals(""))
			infoGroupProperties.setMaximumCapacity(
				new Integer(maximumCapacity));
		if (!minimumCapacity.equals(""))
			infoGroupProperties.setMinimumCapacity(
				new Integer(minimumCapacity));
		infoGroupProperties.setName(name);
		infoGroupProperties.setProjectDescription(projectDescription);
		infoGroupProperties.setShiftType(new TipoAula(new Integer(shiftType)));
		Integer objectCode = getObjectCode(request);
		Object args[] = { objectCode, infoGroupProperties };
		GestorServicos gestor = GestorServicos.manager();
		try {
			gestor.executar(userView, "EditGroupProperties", args);
		} catch (NonValidChangeServiceException e) {
			ActionErrors actionErrors = new ActionErrors();
			ActionError error = null;
			error =
				new ActionError("error.exception.nonValidChange.editGroupProperties");
			actionErrors.add(
				"error.exception.nonValidChange.editGroupProperties",
				error);
			saveErrors(request, actionErrors);
			return prepareEditGroupProperties(mapping, form, request, response);

		} catch (ExistingServiceException e) {
			ActionErrors actionErrors = new ActionErrors();
			ActionError error = null;
			error = new ActionError("error.exception.existing.groupProperties");
			actionErrors.add("error.exception.existing.groupProperties", error);
			saveErrors(request, actionErrors);
			return prepareEditGroupProperties(mapping, form, request, response);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		return viewProjectStudentGroups(mapping, form, request, response);
	}
	public ActionForward deleteStudentGroup(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		HttpSession session = request.getSession(false);

		UserView userView =
			(UserView) session.getAttribute(SessionConstants.U_VIEW);
		Integer objectCode = getObjectCode(request);
		String studentGroupCodeString =
			(String) request.getParameter("studentGroupCode");
		Integer studentGroupCode = new Integer(studentGroupCodeString);
		Object[] args = { objectCode, studentGroupCode };
		GestorServicos gestorServicos = GestorServicos.manager();
		Boolean result = new Boolean(false);
		try {
			result =
				(Boolean) gestorServicos.executar(
					userView,
					"DeleteStudentGroup",
					args);
		} catch (ExistingServiceException e) {
			result = new Boolean(true);
		} catch (FenixServiceException e) {
			e.printStackTrace();
			throw new FenixActionException(e.getMessage());
		}
		if (!result.booleanValue()) {
			ActionErrors actionErrors = new ActionErrors();
			ActionError error = null;
			error =
				new ActionError("errors.invalid.delete.not.empty.studentGroup");
			actionErrors.add(
				"errors.invalid.delete.not.empty.studentGroup",
				error);
			saveErrors(request, actionErrors);
		}

		return viewProjectStudentGroups(mapping, form, request, response);
	}
	public ActionForward prepareCreateStudentGroup(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		HttpSession session = request.getSession(false);
		session.removeAttribute("insertStudentGroupForm");
		UserView userView = (UserView) session.getAttribute("UserView");
		Integer objectCode = getObjectCode(request);
		String groupPropertiesString =
			(String) request.getParameter("groupPropertiesCode");
		Integer groupPropertiesCode = new Integer(groupPropertiesString);
		ISiteComponent viewShifts = new InfoSiteShifts();
		TeacherAdministrationSiteView shiftsView =
			(TeacherAdministrationSiteView) readSiteView(request,
				viewShifts,
				null,
				objectCode,
				null);
		List shifts =
			(List) ((InfoSiteShifts) shiftsView.getComponent()).getShifts();
		ArrayList shiftsList = new ArrayList();
		if (shifts.size() != 0) {
			shiftsList.add(new LabelValueBean("(escolher)", ""));
			InfoShift infoShift;
			Iterator iter = shifts.iterator();
			String label, value;
			while (iter.hasNext()) {
				infoShift = (InfoShift) iter.next();
				value = infoShift.getIdInternal().toString();
				label = infoShift.getNome();
				shiftsList.add(new LabelValueBean(label, value));
			}
			request.setAttribute("shiftsList", shiftsList);
		}
		List infoStudentList;
		Object args[] = { objectCode, groupPropertiesCode };
		GestorServicos gestor = GestorServicos.manager();
		try {
			infoStudentList =
				(List) gestor.executar(
					userView,
					"PrepareCreateStudentGroup",
					args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		request.setAttribute("infoStudentList", infoStudentList);
		readSiteView(request, null, null, null, null);
		return mapping.findForward("insertStudentGroup");
	}
	public ActionForward createStudentGroup(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		HttpSession session = request.getSession(false);
		UserView userView = (UserView) session.getAttribute("UserView");
		Integer objectCode = getObjectCode(request);

		String groupPropertiesCodeString =
			(String) request.getParameter("groupPropertiesCode");
		Integer groupPropertiesCode = new Integer(groupPropertiesCodeString);

		DynaActionForm insertStudentGroupForm = (DynaActionForm) form;

		List studentCodes =
			Arrays.asList(
				(Integer[]) insertStudentGroupForm.get("studentCodes"));

		String groupNumberString =
			(String) insertStudentGroupForm.get("groupNumber");
		Integer groupNumber = new Integer(groupNumberString);

		String newShiftString = (String) insertStudentGroupForm.get("shift");

		if (newShiftString.equals("")) {
			ActionErrors actionErrors = new ActionErrors();
			ActionError error = null;
			// Create an ACTION_ERROR 
			error = new ActionError("errors.invalid.insert.studentGroupShift");
			actionErrors.add("errors.invalid.insert.studentGroupShift", error);
			saveErrors(request, actionErrors);
			return prepareCreateStudentGroup(mapping, form, request, response);
		} else {
			Integer shiftCode = new Integer(newShiftString);
			Object args[] =
				{
					objectCode,
					groupNumber,
					groupPropertiesCode,
					shiftCode,
					studentCodes };
			GestorServicos gestor = GestorServicos.manager();
			try {
				gestor.executar(userView, "CreateStudentGroup", args);

			} catch (ExistingServiceException e) {
				ActionErrors actionErrors = new ActionErrors();
				ActionError error = null;
				// Create an ACTION_ERROR 
				error = new ActionError("errors.invalid.insert.studentGroup");
				actionErrors.add("errors.invalid.insert.studentGroup", error);
				saveErrors(request, actionErrors);
				return prepareCreateStudentGroup(
					mapping,
					form,
					request,
					response);
			} catch (FenixServiceException e) {
				throw new FenixActionException(e);
			}
			return viewProjectStudentGroups(mapping, form, request, response);
		}
	}
	public ActionForward prepareEditStudentGroupShift(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		String studentGroupCodeString =
			(String) request.getParameter("studentGroupCode");
		String shiftCodeString = (String) request.getParameter("shiftCode");
		Integer studentGroupCode = new Integer(studentGroupCodeString);
		Integer shiftCode = new Integer(shiftCodeString);
		System.out.println("SHIFT CODE = " + shiftCode);
		Integer objectCode = getObjectCode(request);
		ISiteComponent viewShifts = new InfoSiteShifts();
		TeacherAdministrationSiteView shiftsView =
			(TeacherAdministrationSiteView) readSiteView(request,
				viewShifts,
				null,
				objectCode,
				studentGroupCode);
		List shifts =
			(List) ((InfoSiteShifts) shiftsView.getComponent()).getShifts();

		if (shifts == null) {
			ActionErrors actionErrors = new ActionErrors();
			ActionError error = null;
			error = new ActionError("error.noGroup");
			actionErrors.add("error.noGroup", error);
			saveErrors(request, actionErrors);
			return viewProjectStudentGroups(mapping, form, request, response);
		}
		//			creation of bean of InfoSiteShifts for use in jsp
		ArrayList shiftsList = new ArrayList();
		InfoShift oldInfoShift = new InfoShift();
		if (shifts.size() != 0) {
			shiftsList.add(new LabelValueBean("(escolher)", ""));
			InfoShift infoShift;
			Iterator iter = shifts.iterator();
			String label, value;
			while (iter.hasNext()) {
				infoShift = (InfoShift) iter.next();
				if (infoShift.getIdInternal().equals(shiftCode)) {
					oldInfoShift = infoShift;
				}
				value = infoShift.getIdInternal().toString();
				label = infoShift.getNome();
				shiftsList.add(new LabelValueBean(label, value));
			}
			request.setAttribute("shiftsList", shiftsList);
		}
		request.setAttribute("shift", oldInfoShift);
		return mapping.findForward("editStudentGroupShift");
	}
	public ActionForward editStudentGroupShift(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		HttpSession session = request.getSession(false);
		Integer objectCode = getObjectCode(request);
		DynaActionForm editStudentGroupForm = (DynaActionForm) form;
		UserView userView = (UserView) session.getAttribute("UserView");
		String oldShiftString = (String) request.getParameter("shiftCode");
		String studentGroupCodeString =
			(String) request.getParameter("studentGroupCode");
		Integer studentGroupCode = new Integer(studentGroupCodeString);
		String newShiftString = (String) editStudentGroupForm.get("shift");
		if (newShiftString.equals(oldShiftString)
			|| newShiftString.equals("")) {
			return viewProjectStudentGroups(mapping, form, request, response);
		} else {
			Integer newShiftCode = new Integer(newShiftString);
			Object args[] = { objectCode, studentGroupCode, newShiftCode };
			GestorServicos gestor = GestorServicos.manager();
			Boolean result;
			try {
				result =
					(Boolean) gestor.executar(
						userView,
						"EditStudentGroupShift",
						args);
			} catch (FenixServiceException e) {
				throw new FenixActionException(e);
			}
			if (result.booleanValue() == false) {
				ActionErrors actionErrors = new ActionErrors();
				ActionError error = null;
				// Create an ACTION_ERROR for each DEGREE_CURRICULAR_PLAN
				error =
					new ActionError("errors.invalid.edit.studentGroupShift");
				actionErrors.add(
					"errors.invalid.edit.studentGroupShift",
					error);
				saveErrors(request, actionErrors);
				return prepareEditStudentGroupShift(
					mapping,
					form,
					request,
					response);
			}
			return viewProjectStudentGroups(mapping, form, request, response);
		}
	}
	public ActionForward prepareEditStudentGroupMembers(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		HttpSession session = request.getSession(false);

		UserView userView = (UserView) session.getAttribute("UserView");

		String studentGroupCodeString =
			(String) request.getParameter("studentGroupCode");
		Integer studentGroupCode = new Integer(studentGroupCodeString);

		Integer objectCode = getObjectCode(request);

		ISiteComponent viewStudentGroup = new InfoSiteStudentGroup();
		TeacherAdministrationSiteView siteView =
			(TeacherAdministrationSiteView) readSiteView(request,
				viewStudentGroup,
				null,
				studentGroupCode,
				null);
		InfoSiteStudentGroup component =
			(InfoSiteStudentGroup) siteView.getComponent();

		if (component.getInfoSiteStudentInformationList() == null) {
			ActionErrors actionErrors = new ActionErrors();
			ActionError error = null;
			error = new ActionError("error.noGroup");
			actionErrors.add("error.noGroup", error);
			saveErrors(request, actionErrors);
			return viewProjectStudentGroups(mapping, form, request, response);
		}

		Object args[] = { objectCode, studentGroupCode };
		GestorServicos gestor = GestorServicos.manager();
		List infoStudentList = null;
		try {
			infoStudentList =
				(List) gestor.executar(
					userView,
					"PrepareEditStudentGroupMembers",
					args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		request.setAttribute("infoStudentList", infoStudentList);
		return mapping.findForward("editStudentGroupMembers");
	}

	public ActionForward insertStudentGroupMembers(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		HttpSession session = request.getSession(false);
		UserView userView = (UserView) session.getAttribute("UserView");
		Integer objectCode = getObjectCode(request);
		String studentGroupCodeString =
			(String) request.getParameter("studentGroupCode");
		Integer studentGroupCode = new Integer(studentGroupCodeString);
		DynaActionForm insertStudentGroupForm = (DynaActionForm) form;
		List studentCodes =
			Arrays.asList(
				(Integer[]) insertStudentGroupForm.get("studentCodes"));

		Object args[] = { objectCode, studentGroupCode, studentCodes };
		GestorServicos gestor = GestorServicos.manager();
		//Boolean result;
		try {
			gestor.executar(userView, "InsertStudentGroupMembers", args);
		} catch (ExistingServiceException e) {
			ActionErrors actionErrors = new ActionErrors();
			ActionError error = null;
			error = new ActionError("error.noGroup");
			actionErrors.add("error.noGroup", error);
			saveErrors(request, actionErrors);
			return viewProjectStudentGroups(mapping, form, request, response);

		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		return prepareEditStudentGroupMembers(mapping, form, request, response);
	}

	public ActionForward deleteStudentGroupMembers(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		HttpSession session = request.getSession(false);
		UserView userView = (UserView) session.getAttribute("UserView");
		Integer objectCode = getObjectCode(request);
		String studentGroupCodeString =
			(String) request.getParameter("studentGroupCode");
		Integer studentGroupCode = new Integer(studentGroupCodeString);
		DynaActionForm deleteStudentGroupForm = (DynaActionForm) form;
		List studentUsernames =
			Arrays.asList(
				(String[]) deleteStudentGroupForm.get("studentsToRemove"));

		Object args[] = { objectCode, studentGroupCode, studentUsernames };
		GestorServicos gestor = GestorServicos.manager();
		try {
			gestor.executar(userView, "DeleteStudentGroupMembers", args);
		} catch (ExistingServiceException e) {
			ActionErrors actionErrors = new ActionErrors();
			ActionError error = null;
			error = new ActionError("error.noGroup");
			actionErrors.add("error.noGroup", error);
			saveErrors(request, actionErrors);
			return viewProjectStudentGroups(mapping, form, request, response);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		return prepareEditStudentGroupMembers(mapping, form, request, response);
	}
}
