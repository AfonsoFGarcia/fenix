package net.sourceforge.fenixedu.presentationTier.Action.research.activity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.research.activity.CreateJournalIssue;
import net.sourceforge.fenixedu.applicationTier.Servico.research.activity.CreateResearchActivityParticipation;
import net.sourceforge.fenixedu.dataTransferObject.research.activity.ResearchJournalIssueCreationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.CreateIssueBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.research.activity.JournalIssue;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "researcher", path = "/activities/createJournalIssue", scope = "request", parameter = "method")
@Forwards(value = {
        @Forward(name = "CreateIssue", path = "/researcher/activities/createJournalIssueParticipation.jsp",
                tileProperties = @Tile(
                        title = "private.operator.personnelmanagement.managementfaculty.teacherevaluation.activities")),
        @Forward(name = "Success", path = "/activities/activitiesManagement.do?method=listActivities", tileProperties = @Tile(
                title = "private.operator.personnelmanagement.managementfaculty.teacherevaluation.activities")) })
public class CreateJournalIssueDispatchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        request.setAttribute("party", getLoggedPerson(request));
        return mapping.findForward("CreateIssue");
    }

    public ActionForward prepareJournalIssueSearch(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ResearchJournalIssueCreationBean bean = new ResearchJournalIssueCreationBean();
        request.setAttribute("bean", bean);

        return prepare(mapping, form, request, response);
    }

    public ActionForward createPostback(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ResearchJournalIssueCreationBean bean =
                (ResearchJournalIssueCreationBean) RenderUtils.getViewState("name").getMetaObject().getObject();
        CreateIssueBean issueBean = (CreateIssueBean) RenderUtils.getViewState("issueBean").getMetaObject().getObject();

        request.setAttribute(((issueBean.getJournal() != null) ? "newIssue" : "newJournal"), "true");
        request.setAttribute("issueBean", issueBean);
        request.setAttribute("bean", bean);
        RenderUtils.invalidateViewState();
        return prepare(mapping, form, request, response);
    }

    public ActionForward createScientificJournal(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ResearchJournalIssueCreationBean bean =
                (ResearchJournalIssueCreationBean) RenderUtils.getViewState("name").getMetaObject().getObject();
        CreateIssueBean issueBean = (CreateIssueBean) RenderUtils.getViewState("issueBean").getMetaObject().getObject();
        if (!issueBean.isJournalFormValid()) {
            request.setAttribute("newJournal", "true");
            request.setAttribute("issueBean", issueBean);

        } else {
            JournalIssue issue = CreateJournalIssue.run(issueBean);
            bean.setJournalIssue(issue);
            bean.setScientificJournal(issue.getScientificJournal());
        }
        request.setAttribute("skipNewIssueCreation", "true");
        request.setAttribute("bean", bean);
        return prepare(mapping, form, request, response);
    }

    public ActionForward createIssue(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ResearchJournalIssueCreationBean bean =
                (ResearchJournalIssueCreationBean) RenderUtils.getViewState("name").getMetaObject().getObject();
        CreateIssueBean issueBean = (CreateIssueBean) RenderUtils.getViewState("issueBean").getMetaObject().getObject();
        JournalIssue issue = CreateJournalIssue.run(issueBean);
        bean.setJournalIssue(issue);
        bean.setScientificJournal(issue.getScientificJournal());
        request.setAttribute("bean", bean);
        request.setAttribute("skipNewIssueCreation", "true");
        return prepare(mapping, form, request, response);
    }

    public ActionForward addNewLanguage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ResearchJournalIssueCreationBean bean = getJournalIssueBean(request);
        request.setAttribute("bean", bean);
        return prepare(mapping, form, request, response);
    }

    public ActionForward prepareJournalIssueParticipation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ResearchJournalIssueCreationBean bean = getJournalIssueBean(request);
        if (bean == null) {
            return prepareJournalIssueSearch(mapping, form, request, response);
        }
        if (bean.getJournalIssue() != null) {
            return createJournalIssueParticipation(mapping, form, request, response);
        }
        if (request.getParameter("prepareCreateNewJournal") != null) {
            request.setAttribute("newJournal", "true");
            CreateIssueBean issueBean = new CreateIssueBean();
            issueBean.setScientificJournalName(bean.getScientificJournalName());
            request.setAttribute("issueBean", issueBean);
        }
        if (request.getParameter("createNewJournal") != null) {
            return createScientificJournal(mapping, form, request, response);
        }
        if (request.getParameter("newIssue") != null) {
            request.setAttribute("newIssue", true);
            CreateIssueBean issueBean = new CreateIssueBean();
            issueBean.setJournal(bean.getScientificJournal());
            request.setAttribute("issueBean", issueBean);
        }
        if (request.getParameter("createNewIssue") != null) {
            return createIssue(mapping, form, request, response);
        }
        request.setAttribute("bean", bean);
        RenderUtils.invalidateViewState();
        return prepare(mapping, form, request, response);

    }

    public ActionForward createJournalIssueParticipation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        Person person = getLoggedPerson(request);
        ResearchJournalIssueCreationBean bean = getJournalIssueBean(request);
        if (bean == null) {
            return prepareJournalIssueSearch(mapping, form, request, response);
        }

        if (bean.getRole() != null) {
            try {
                CreateResearchActivityParticipation.run(bean.getJournalIssue(), bean.getRole(), person, bean.getRoleMessage());

            } catch (DomainException e) {
                addActionMessage(request, e.getMessage());
                request.setAttribute("existentJournalBean", bean);
                return prepare(mapping, form, request, response);
            }
        }

        return mapping.findForward("Success");
    }

    public ActionForward createInexistentJournalIssueParticipation(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        Person person = getLoggedPerson(request);

        ResearchJournalIssueCreationBean bean = getJournalIssueBean(request);
        if (bean == null) {
            return prepareJournalIssueSearch(mapping, form, request, response);
        }

        try {
            CreateResearchActivityParticipation.run(bean.getJournalIssue(), bean.getRole(), person, bean.getRoleMessage());
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage());
            request.setAttribute("inexistentJournalBean", bean);
            return prepare(mapping, form, request, response);
        }

        return mapping.findForward("Success");
    }

    private ResearchJournalIssueCreationBean getJournalIssueBean(HttpServletRequest request) {
        ResearchJournalIssueCreationBean bean = null;
        if (RenderUtils.getViewState() != null) {
            bean = (ResearchJournalIssueCreationBean) RenderUtils.getViewState().getMetaObject().getObject();
            return bean;
        }
        return bean;
    }

}