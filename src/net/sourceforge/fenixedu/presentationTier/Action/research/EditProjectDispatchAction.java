package net.sourceforge.fenixedu.presentationTier.Action.research;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.research.ProjectEventAssociationFullCreationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.ProjectEventAssociationSimpleCreationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.ProjectParticipantFullCreationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.ProjectParticipantSimpleCreationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.ProjectParticipantUnitCreationBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.project.Project;
import net.sourceforge.fenixedu.domain.research.project.ProjectEventAssociation;
import net.sourceforge.fenixedu.domain.research.project.ProjectParticipation;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class EditProjectDispatchAction extends FenixDispatchAction {

    //***************************************
    //                   DATA
    //***************************************
    
    public ActionForward prepareEditData(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        setAttributeSelectedProject(request);
        
        request.setAttribute("party", getUserView(request).getPerson());
        return mapping.findForward("EditProjectData");  
    }
    
    //***************************************
    //               PARTICIPANTS
    //***************************************
    
    public ActionForward prepareEditParticipantsSimple(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final Integer oid = Integer.parseInt(request.getParameter("projectId"));

        for( Project project : rootDomainObject.getProjects()) {
            if (project.getIdInternal().equals(oid)) {
                request.setAttribute("selectedProject", project);
                List<ProjectParticipation> participations = new ArrayList<ProjectParticipation>();
                for(ProjectParticipation participation : project.getProjectParticipations()) {
                    if( participation.getParty() instanceof Person) {
                        participations.add(participation);
                    }
                }
                request.setAttribute("participations", participations);
            }
        }  
        return mapping.findForward("EditProjectParticipants");  
    }
    
    public ActionForward prepareEditParticipants(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ActionForward forward = prepareEditParticipantsSimple(mapping, form, request, response);
        
        ProjectParticipantSimpleCreationBean simpleBean = new ProjectParticipantSimpleCreationBean();
        request.setAttribute("simpleBean", simpleBean);
        
        return forward;  
    }
  
    public ActionForward createParticipantSimple(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final IUserView userView = getUserView(request);
        
        if(RenderUtils.getViewState().getMetaObject().getObject() instanceof ProjectParticipantSimpleCreationBean){        
            ProjectParticipantSimpleCreationBean simpleBean = (ProjectParticipantSimpleCreationBean) RenderUtils.getViewState().getMetaObject().getObject();
            
            if(simpleBean.getPerson() != null) {
                // Criar a participa��o efectivamente quando j� existe a pessoa escolhida
                Integer oid = Integer.parseInt(request.getParameter("projectId"));
                ServiceUtils.executeService(userView, "CreateProjectParticipant", new Object[] {simpleBean, oid });
                return prepareEditParticipants(mapping, form, request, response);
            }
            else {
                //Permitir a cria��o de uma pessoa externa
                ProjectParticipantFullCreationBean fullBean = new ProjectParticipantFullCreationBean();
                fullBean.setPersonName(simpleBean.getPersonName());
                fullBean.setRole(simpleBean.getRole());
                request.setAttribute("fullBean", fullBean);
                return prepareEditParticipantsSimple(mapping, form, request, response);
            }
        }
        else {
            request.setAttribute("fullBean", (ProjectParticipantFullCreationBean) RenderUtils.getViewState().getMetaObject().getObject());
            return prepareEditParticipantsSimple(mapping, form, request, response);
        }
    }
    
    public ActionForward createParticipantFull(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProjectParticipantFullCreationBean fullBean = (ProjectParticipantFullCreationBean) RenderUtils.getViewState().getMetaObject().getObject();
        final IUserView userView = getUserView(request);
        Integer oid = Integer.parseInt(request.getParameter("projectId"));
        
        ServiceUtils.executeService(userView, "CreateProjectParticipant", new Object[] { fullBean, oid });
        
        return prepareEditParticipants(mapping, form, request, response);
    }    
    
    public ActionForward removeParticipant(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        final IUserView userView = getUserView(request);
        Integer participantId = Integer.parseInt(request.getParameter("participantId"));
        
        ServiceUtils.executeService(userView, "DeleteProjectParticipant", new Object[] { participantId });
        
        return prepareEditParticipants(mapping, form, request, response);
    }    
    
    //***************************************
    //                   EVENTS
    //***************************************

    public ActionForward prepareEditEventAssociationsSimple(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final Integer oid = Integer.parseInt(request.getParameter("projectId"));

        for( Project project : rootDomainObject.getProjects()) {
            if (project.getIdInternal().equals(oid)) {
                request.setAttribute("selectedProject", project);
                List<ProjectEventAssociation> associations = project.getAssociatedEvents();
                request.setAttribute("eventAssociations", associations);
            }
        }  
        
        return mapping.findForward("EditProjectEventAssociations");  
    }
    
    public ActionForward prepareEditEventAssociations(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ActionForward forward = prepareEditEventAssociationsSimple(mapping, form, request, response);
        
        ProjectEventAssociationSimpleCreationBean simpleBean = new ProjectEventAssociationSimpleCreationBean();
        request.setAttribute("simpleBean", simpleBean);
        
        return forward;  
    }    
    
    public ActionForward createEventAssociationSimple(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final IUserView userView = getUserView(request);
        
        if(RenderUtils.getViewState().getMetaObject().getObject() instanceof ProjectEventAssociationSimpleCreationBean){
            ProjectEventAssociationSimpleCreationBean simpleBean = (ProjectEventAssociationSimpleCreationBean) RenderUtils.getViewState().getMetaObject().getObject();
            if(simpleBean.getEvent() != null) {
                // Criar a associa��o efectivamente quando j� existe o evento escolhido
                Integer oid = Integer.parseInt(request.getParameter("projectId"));
                ServiceUtils.executeService(userView, "CreateProjectEventAssociation", new Object[] {simpleBean, oid });
                return prepareEditEventAssociations(mapping, form, request, response);
            }
            else {
                //Permitir a cria��o de um novo evento on-the-fly
                ProjectEventAssociationFullCreationBean fullBean = new ProjectEventAssociationFullCreationBean();
                fullBean.setEventName(new MultiLanguageString(simpleBean.getEventName()));
                fullBean.setRole(simpleBean.getRole());
                request.setAttribute("fullBean", fullBean);
                return prepareEditEventAssociationsSimple(mapping, form, request, response);
            }
        }
        else {
            request.setAttribute("fullBean", (ProjectEventAssociationFullCreationBean) RenderUtils.getViewState().getMetaObject().getObject());
            return prepareEditEventAssociationsSimple(mapping, form, request, response);
        }        
    }
    
    public ActionForward createEventAssociationFull(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProjectEventAssociationFullCreationBean fullBean = (ProjectEventAssociationFullCreationBean) RenderUtils.getViewState().getMetaObject().getObject();
        final IUserView userView = getUserView(request);
        Integer oid = Integer.parseInt(request.getParameter("projectId"));
        
        ServiceUtils.executeService(userView, "CreateProjectEventAssociation", new Object[] { fullBean, oid });
        
        return prepareEditEventAssociations(mapping, form, request, response);
    }       
    
    public ActionForward removeEventAssociation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        final IUserView userView = getUserView(request);
        Integer associationId = Integer.parseInt(request.getParameter("associationId"));
        
        ServiceUtils.executeService(userView, "DeleteProjectEventAssociation", new Object[] { associationId });
        
        return prepareEditEventAssociations(mapping, form, request, response);
    }       


	// ***************************************
	// UNITS
	// ***************************************
	public ActionForward prepareEditParticipantUnits(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		final Integer oid = Integer.parseInt(request.getParameter("projectId"));

		for (Project project : rootDomainObject.getProjects()) {
			if (project.getIdInternal().equals(oid)) {
				request.setAttribute("selectedProject", project);
				List<ProjectParticipation> unitParticipations = new ArrayList<ProjectParticipation>();
				for (ProjectParticipation unitParticipation : project
						.getProjectParticipations()) {
					if (unitParticipation.getParty() instanceof Unit) {
						unitParticipations.add(unitParticipation);
					}
				}
				request.setAttribute("unitParticipations", unitParticipations);
			}
		}

		ProjectParticipantUnitCreationBean bean = new ProjectParticipantUnitCreationBean();
		request.setAttribute("bean", bean);

		return mapping.findForward("EditProjectParticipantUnits");
	}



	public ActionForward createParticipantUnit(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		final IUserView userView = getUserView(request);

		ProjectParticipantUnitCreationBean bean = (ProjectParticipantUnitCreationBean) RenderUtils
				.getViewState().getMetaObject().getObject();
		
		Integer oid = Integer.parseInt(request
				.getParameter("projectId"));
		ServiceUtils.executeService(userView,
				"CreateProjectParticipant", new Object[] { bean,
						oid });
		return prepareEditParticipantUnits(mapping, form, request, response);
	}


	public ActionForward removeParticipantUnit(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		final IUserView userView = getUserView(request);
		Integer participantId = Integer.parseInt(request
				.getParameter("participantUnitId"));

		ServiceUtils.executeService(userView, "DeleteProjectParticipant",
				new Object[] { participantId });

		return prepareEditParticipants(mapping, form, request, response);
	}

	// ***************************************
	// PRIVATE
	// ***************************************

	private void setAttributeSelectedProject(HttpServletRequest request) {
		final Integer oid = Integer.parseInt(request.getParameter("projectId"));

		for (Project project : rootDomainObject.getProjects()) {
			if (project.getIdInternal().equals(oid)) {
				request.setAttribute("selectedProject", project);
			}
		}
	}
}