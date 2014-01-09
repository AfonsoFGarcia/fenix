package net.sourceforge.fenixedu.presentationTier.Action.research.result.patents;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.research.result.patent.DeleteResultPatent;
import net.sourceforge.fenixedu.applicationTier.Servico.research.result.patent.UpdateMetaInformation;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.research.result.patent.ResearchResultPatent;
import net.sourceforge.fenixedu.presentationTier.Action.research.result.ResultsManagementAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "researcher", path = "/patents/management", scope = "session", parameter = "method")
@Forwards(value = {
        @Forward(name = "editPatentData", path = "/researcher/result/patents/editPatentData.jsp", tileProperties = @Tile(
                title = "private.operator.personnelmanagement.managementfaculty.teacherevaluation.patents")),
        @Forward(name = "deletePatent", path = "/researcher/result/patents/deletePatent.jsp", tileProperties = @Tile(
                title = "private.operator.personnelmanagement.managementfaculty.teacherevaluation.patents")),
        @Forward(name = "editPatent", path = "/researcher/result/patents/editPatent.jsp", tileProperties = @Tile(
                title = "private.operator.personnelmanagement.managementfaculty.teacherevaluation.patents")),
        @Forward(name = "createPatent", path = "/researcher/result/patents/createPatent.jsp", tileProperties = @Tile(
                title = "private.operator.personnelmanagement.managementfaculty.teacherevaluation.patents")),
        @Forward(name = "patentDetails", path = "/researcher/result/patents/patentDetails.jsp", tileProperties = @Tile(
                title = "private.operator.personnelmanagement.managementfaculty.teacherevaluation.patents")),
        @Forward(name = "listPatents", path = "/researcher/result/patents/managePatents.jsp", tileProperties = @Tile(
                title = "private.operator.personnelmanagement.managementfaculty.teacherevaluation.patents")) })
public class ResultPatentsManagementAction extends ResultsManagementAction {

    private static final Logger logger = LoggerFactory.getLogger(ResultPatentsManagementAction.class);

    public ActionForward management(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        /*
         * for(Message message : RenderUtils.getViewState().getMessages()) {
         * addActionMessage(request, message.getMessage()); }
         */

        request.setAttribute("resultPatents", getLoggedPerson(request).getResearchResultPatents());

        return mapping.findForward("listPatents");
    }

    public ActionForward prepareDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        final ResearchResultPatent patent = (ResearchResultPatent) getResultFromRequest(request);
        if (patent == null) {
            return management(mapping, form, request, response);
        }

        return mapping.findForward("patentDetails");
    }

    public ActionForward createPatent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        final ResearchResultPatent patent = (ResearchResultPatent) getResultFromRequest(request);
        return showPatent(mapping, form, request, response);
    }

    public ActionForward showPatent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        final ResearchResultPatent patent = (ResearchResultPatent) getResultFromRequest(request);
        if (patent == null) {
            return management(mapping, form, request, response);
        }

        if (!patent.hasPersonParticipation(getLoggedPerson(request))) {
            addActionMessage(request, "researcher.ResultParticipation.last.participation.warning");
        }

        return mapping.findForward("editPatent");
    }

    public ActionForward prepareCreate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        return mapping.findForward("createPatent");
    }

    public ActionForward prepareEditData(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final ResearchResultPatent patent = (ResearchResultPatent) getResultFromRequest(request);
        if (patent == null) {
            return management(mapping, form, request, response);
        }

        return mapping.findForward("editPatentData");
    }

    public ActionForward updateMetaInformation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final ResearchResultPatent patent = (ResearchResultPatent) getResultFromRequest(request);
        if (patent == null) {
            return management(mapping, form, request, response);
        }

        try {
            UpdateMetaInformation.run(patent);
        } catch (DomainException e) {
            logger.error(e.getMessage(), e);
            addActionMessage(request, "label.communicationError");
        }

        return showPatent(mapping, form, request, response);
    }

    public ActionForward prepareDelete(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final ResearchResultPatent patent = (ResearchResultPatent) getResultFromRequest(request);
        if (patent == null) {
            return management(mapping, form, request, response);
        }
        request.setAttribute("confirm", "yes");
        return mapping.findForward("editPatent");
    }

    public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixServiceException {
        final String resultId = request.getParameter("resultId");

        if (getFromRequest(request, "cancel") != null) {
            ResearchResultPatent patent = (ResearchResultPatent) getResultByIdFromRequest(request);
            request.setAttribute("result", patent);
            request.setAttribute("resultId", resultId);
            return mapping.findForward("editPatent");
        }

        if (getFromRequest(request, "confirm") != null) {
            try {

                DeleteResultPatent.run(resultId);
            } catch (Exception e) {
                final ActionForward defaultForward = management(mapping, form, request, response);
                return processException(request, mapping, defaultForward, e);
            }
        }

        return management(mapping, form, request, response);
    }

    @Override
    public ResearchResultPatent getRenderedObject(String id) {
        return (ResearchResultPatent) super.getRenderedObject(id);
    }
}