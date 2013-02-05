package net.sourceforge.fenixedu.presentationTier.Action.research.activity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.research.activity.CreateResearchActivityParticipation;
import net.sourceforge.fenixedu.applicationTier.Servico.research.activity.CreateResearchEvent;
import net.sourceforge.fenixedu.dataTransferObject.research.activity.ResearchEventCreationBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.research.activity.ResearchEvent;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "researcher", path = "/activities/createEvent", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "CreateEvent", path = "/researcher/activities/createEventParticipation.jsp"),
        @Forward(name = "Success", path = "/activities/activitiesManagement.do?method=listActivities") })
public class CreateEventDispatchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        request.setAttribute("party", getLoggedPerson(request));
        return mapping.findForward("CreateEvent");
    }

    public ActionForward prepareEventSearch(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ResearchEventCreationBean bean = getEventBean(request);
        if (bean == null) {
            bean = new ResearchEventCreationBean();
        }

        request.setAttribute("eventBean", bean);
        request.setAttribute("eventCreationSchema", "eventCreation.eventName");

        return prepare(mapping, form, request, response);
    }

    public ActionForward prepareCreateEventParticipation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ResearchEventCreationBean bean = getEventBean(request);
        if (bean == null) {
            return prepareEventSearch(mapping, form, request, response);
        }

        if (bean.getEvent() != null) {
            request.setAttribute("existentEventBean", bean);
            request.setAttribute("eventCreationSchema", "eventCreation.existentEvent");
            return prepare(mapping, form, request, response);
        } else {
            request.setAttribute("inexistentEventBean", bean);
            request.setAttribute("eventCreationSchema", "eventCreation.inexistentEvent");
            return prepare(mapping, form, request, response);
        }
    }

    public ActionForward createExistentEventParticipation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        Person person = getLoggedPerson(request);
        ResearchEventCreationBean bean = getEventBean(request);
        if (bean == null) {
            return prepareEventSearch(mapping, form, request, response);
        }

        if (bean.getRole() != null) {
            try {
                CreateResearchActivityParticipation.run(bean.getEvent(), bean.getRole(), person, bean.getRoleMessage());
            } catch (DomainException e) {
                addActionMessage(request, e.getMessage());
                request.setAttribute("existentEventBean", bean);
                request.setAttribute("eventCreationSchema", "eventCreation.existentEvent");
                return prepare(mapping, form, request, response);
            }
        }

        return mapping.findForward("Success");
    }

    public ActionForward createInexistentEventParticipation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Person person = getLoggedPerson(request);

        ResearchEventCreationBean bean = getEventBean(request);
        if (bean == null) {
            return prepareEventSearch(mapping, form, request, response);
        }

        ResearchEvent event = null;
        try {
            event = CreateResearchEvent.run(bean.getEventName(), bean.getEventType(), bean.getLocationType(), bean.getUrl());
            CreateResearchActivityParticipation.run(event, bean.getRole(), person, bean.getRoleMessage());
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage());
            request.setAttribute("inexistentEventBean", bean);
            request.setAttribute("eventCreationSchema", "eventCreation.inexistentEvent");
            return prepare(mapping, form, request, response);
        }

        return mapping.findForward("Success");
    }

    public ResearchEventCreationBean getEventBean(HttpServletRequest request) {
        ResearchEventCreationBean bean = null;
        if (RenderUtils.getViewState() != null) {
            bean = (ResearchEventCreationBean) RenderUtils.getViewState().getMetaObject().getObject();
            return bean;
        }
        return bean;
    }
}