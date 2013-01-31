package net.sourceforge.fenixedu.presentationTier.Action.research.researchUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ResultPublicationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ResultPublicationBean.ResultPublicationType;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResearchUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.result.publication.ResearchResultPublication;
import net.sourceforge.fenixedu.domain.research.result.publication.ScopeType;
import net.sourceforge.fenixedu.presentationTier.Action.commons.UnitFunctionalities;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "researcher", path = "/researchUnitFunctionalities", scope = "session", parameter = "method")
@Forwards(value = { @Forward(name = "uploadFile", path = "research-unit-upload-file"),
		@Forward(name = "manageFiles", path = "research-unit-manage-files"),
		@Forward(name = "editUploaders", path = "research-unit-edit-uploaders"),
		@Forward(name = "publications", path = "research-unit-list-publications"),
		@Forward(name = "managePersistedGroups", path = "manage-persisted-groups"),
		@Forward(name = "ShowUnitFunctionalities", path = "show-unit-functionalities"),
		@Forward(name = "editPublicationCollaborators", path = "research-unit-edit-publication-collaborators"),
		@Forward(name = "editFile", path = "research-unit-edit-file"),
		@Forward(name = "editPersistedGroup", path = "edit-persisted-group"),
		@Forward(name = "createPersistedGroup", path = "create-persisted-group") })
public class ResearchUnitFunctionalities extends UnitFunctionalities {

	public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		return mapping.findForward("ShowUnitFunctionalities");
	}

	public ActionForward configurePublicationCollaborators(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		return mapping.findForward("editPublicationCollaborators");
	}

	public ActionForward preparePublications(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ResultPublicationBean publicationBean = ResultPublicationBean.getBeanToCreate(ResultPublicationType.getDefaultType());
		Unit unit = getUnit(request);
		publicationBean.setUnit(unit);
		request.setAttribute("bean", publicationBean);
		putPublicationsInRequest(request, (ResearchUnit) unit);
		request.setAttribute("publications", unit.getResearchResultPublications());
		String publicationType = request.getParameter("publicationType");
		if (publicationType != null) {
			request.setAttribute("publicationType", publicationType);
		}

		return mapping.findForward("publications");
	}

	private void putPublicationsInRequest(HttpServletRequest request, ResearchUnit unit) {
		request.setAttribute("books", ResearchResultPublication.sort(unit.getBooks()));
		request.setAttribute("national-articles", ResearchResultPublication.sort(unit.getArticles(ScopeType.NATIONAL)));
		request.setAttribute("international-articles", ResearchResultPublication.sort(unit.getArticles(ScopeType.INTERNATIONAL)));
		request.setAttribute("national-inproceedings", ResearchResultPublication.sort(unit.getInproceedings(ScopeType.NATIONAL)));
		request.setAttribute("international-inproceedings",
				ResearchResultPublication.sort(unit.getInproceedings(ScopeType.INTERNATIONAL)));
		request.setAttribute("proceedings", ResearchResultPublication.sort(unit.getProceedings()));
		request.setAttribute("theses", ResearchResultPublication.sort(unit.getTheses()));
		request.setAttribute("manuals", ResearchResultPublication.sort(unit.getManuals()));
		request.setAttribute("technicalReports", ResearchResultPublication.sort(unit.getTechnicalReports()));
		request.setAttribute("otherPublications", ResearchResultPublication.sort(unit.getOtherPublications()));
		request.setAttribute("unstructureds", ResearchResultPublication.sort(unit.getUnstructureds()));
		request.setAttribute("inbooks", ResearchResultPublication.sort(unit.getInbooks()));

	}
}
