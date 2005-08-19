package net.sourceforge.fenixedu.presentationTier.Action.coordinator;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeInfo;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.presentationTier.Action.utils.RequestUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author T�nia Pous�o Created on 31/Out/2003
 */
public class DegreeSiteManagementDispatchAction extends FenixDispatchAction {

    public ActionForward subMenu(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        Integer degreeCurricularPlanId = new Integer(request.getParameter("degreeCurricularPlanID"));
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanId);
        return mapping.findForward("degreeSiteMenu");
    }

    public ActionForward viewInformation(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        ActionErrors errors = new ActionErrors();

        IUserView userView = SessionUtils.getUserView(request);

        Integer degreeCurricularPlanID = new Integer(RequestUtils.getAndSetStringToRequest(request,
                "degreeCurricularPlanID"));
        // it's necessary to find which information will be edited
        RequestUtils.getAndSetStringToRequest(request, "info");
        RequestUtils.getAndSetStringToRequest(request, "inEnglish");

        Object[] args = { degreeCurricularPlanID };
        InfoDegreeInfo infoDegreeInfo = null;
        try {
            infoDegreeInfo = (InfoDegreeInfo) ServiceManagerServiceFactory.executeService(userView,
                    "ReadDegreeInfoByDegreeCurricularPlanID", args);
        } catch (NotAuthorizedException e) {
            errors.add("notAuthorized", new ActionError("error.exception.notAuthorized2"));
        } catch (FenixServiceException e) {
            if (e.getMessage().equals("error.invalidExecutionDegree")) {
                errors.add("invalidExecutionDegree", new ActionError("error.invalidExecutionDegree"));
            } else if (e.getMessage().equals("error.impossibleDegreeInfo")) {
                errors.add("impossibleDegreeInfo", new ActionError("error.impossibleDegreeInfo"));
            } else {
                e.printStackTrace();
                throw new FenixActionException(e);
            }
        }

        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }

        DynaActionForm degreeInfoForm = (DynaActionForm) form;
        fillForm(infoDegreeInfo, degreeInfoForm);

        return mapping.findForward("viewInformation");
    }

    public ActionForward editDegreeInformation(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);

        ActionErrors errors = new ActionErrors();

        Integer degreeCurricularPlanID = new Integer(RequestUtils.getAndSetStringToRequest(request,
                "degreeCurricularPlanID"));
        RequestUtils.getAndSetStringToRequest(request, "info");

        DynaActionForm degreeInfoForm = (DynaActionForm) form;
        InfoDegreeInfo infoDegreeInfo = new InfoDegreeInfo();
        fillInfoDegreeInfo(degreeInfoForm, infoDegreeInfo);

        Object[] args = { degreeCurricularPlanID, infoDegreeInfo };

        try {
            ServiceManagerServiceFactory.executeService(userView,
                    "EditDegreeInfoByDegreeCurricularPlanID", args);
        } catch (NotAuthorizedException e) {
            errors.add("notAuthorized", new ActionError("error.exception.notAuthorized2"));
        } catch (FenixServiceException e) {
            if (e.getMessage().equals("error.impossibleEditDegreeInfo")) {
                errors
                        .add("impossibleEditDegreeInfo", new ActionError(
                                "error.impossibleEditDegreeInfo"));
            } else {
                e.printStackTrace();
                throw new FenixActionException(e);
            }
        }
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }

        return mapping.findForward("editOK");
    }

    private void fillForm(InfoDegreeInfo infoDegreeInfo, DynaActionForm degreeInfoForm) {
        degreeInfoForm.set("description", infoDegreeInfo.getDescription());
        degreeInfoForm.set("objectives", infoDegreeInfo.getObjectives());
        degreeInfoForm.set("history", infoDegreeInfo.getHistory());
        degreeInfoForm.set("professionalExits", infoDegreeInfo.getProfessionalExits());
        degreeInfoForm.set("additionalInfo", infoDegreeInfo.getAdditionalInfo());
        degreeInfoForm.set("links", infoDegreeInfo.getLinks());
        degreeInfoForm.set("testIngression", infoDegreeInfo.getTestIngression());
        degreeInfoForm.set("driftsInitial", convertInteger2String(infoDegreeInfo.getDriftsInitial()));
        degreeInfoForm.set("driftsFirst", convertInteger2String(infoDegreeInfo.getDriftsFirst()));
        degreeInfoForm.set("driftsSecond", convertInteger2String(infoDegreeInfo.getDriftsSecond()));
        degreeInfoForm.set("classifications", infoDegreeInfo.getClassifications());
        degreeInfoForm.set("markMin", convertDouble2String(infoDegreeInfo.getMarkMin()));
        degreeInfoForm.set("markMax", convertDouble2String(infoDegreeInfo.getMarkMax()));
        degreeInfoForm.set("markAverage", convertDouble2String(infoDegreeInfo.getMarkAverage()));

        if (infoDegreeInfo.getLastModificationDate() != null) {
            degreeInfoForm.set("lastModificationDate", infoDegreeInfo.getLastModificationDate()
                    .toString());
        }

        // information in english
        degreeInfoForm.set("descriptionEn", infoDegreeInfo.getDescriptionEn());
        degreeInfoForm.set("objectivesEn", infoDegreeInfo.getObjectivesEn());
        degreeInfoForm.set("historyEn", infoDegreeInfo.getHistoryEn());
        degreeInfoForm.set("professionalExitsEn", infoDegreeInfo.getProfessionalExitsEn());
        degreeInfoForm.set("additionalInfoEn", infoDegreeInfo.getAdditionalInfoEn());
        degreeInfoForm.set("linksEn", infoDegreeInfo.getLinksEn());
        degreeInfoForm.set("testIngressionEn", infoDegreeInfo.getTestIngressionEn());
        degreeInfoForm.set("classificationsEn", infoDegreeInfo.getClassificationsEn());
    }

    private void fillInfoDegreeInfo(DynaActionForm degreeInfoForm, InfoDegreeInfo infoDegreeInfo) {
        infoDegreeInfo.setDescription((String) degreeInfoForm.get("description"));
        infoDegreeInfo.setObjectives((String) degreeInfoForm.get("objectives"));
        infoDegreeInfo.setHistory((String) degreeInfoForm.get("history"));
        infoDegreeInfo.setProfessionalExits((String) degreeInfoForm.get("professionalExits"));
        infoDegreeInfo.setAdditionalInfo((String) degreeInfoForm.get("additionalInfo"));
        infoDegreeInfo.setLinks((String) degreeInfoForm.get("links"));
        infoDegreeInfo.setTestIngression((String) degreeInfoForm.get("testIngression"));
        infoDegreeInfo.setDriftsInitial(convertString2Integer((String) degreeInfoForm
                .get("driftsInitial")));
        infoDegreeInfo.setDriftsFirst(convertString2Integer((String) degreeInfoForm.get("driftsFirst")));
        infoDegreeInfo
                .setDriftsSecond(convertString2Integer((String) degreeInfoForm.get("driftsSecond")));
        infoDegreeInfo.setClassifications((String) degreeInfoForm.get("classifications"));
        infoDegreeInfo.setMarkMin(convertString2Double((String) degreeInfoForm.get("markMin")));
        infoDegreeInfo.setMarkMax(convertString2Double((String) degreeInfoForm.get("markMax")));
        infoDegreeInfo.setMarkAverage(convertString2Double((String) degreeInfoForm.get("markAverage")));

        // information in english
        infoDegreeInfo.setDescriptionEn((String) degreeInfoForm.get("descriptionEn"));
        infoDegreeInfo.setObjectivesEn((String) degreeInfoForm.get("objectivesEn"));
        infoDegreeInfo.setHistoryEn((String) degreeInfoForm.get("historyEn"));
        infoDegreeInfo.setProfessionalExitsEn((String) degreeInfoForm.get("professionalExitsEn"));
        infoDegreeInfo.setAdditionalInfoEn((String) degreeInfoForm.get("additionalInfoEn"));
        infoDegreeInfo.setLinksEn((String) degreeInfoForm.get("linksEn"));
        infoDegreeInfo.setTestIngressionEn((String) degreeInfoForm.get("testIngressionEn"));
        infoDegreeInfo.setClassificationsEn((String) degreeInfoForm.get("classificationsEn"));
    }

    public ActionForward viewDescriptionCurricularPlan(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        ActionErrors errors = new ActionErrors();

        IUserView userView = SessionUtils.getUserView(request);

        Integer degreeCurricularPlanID = new Integer(RequestUtils.getAndSetStringToRequest(request,
                "degreeCurricularPlanID"));

        InfoDegreeCurricularPlan infoDegreeCurricularPlan = null;
        Object[] args = { degreeCurricularPlanID };
        try {
            infoDegreeCurricularPlan = (InfoDegreeCurricularPlan) ServiceManagerServiceFactory
                    .executeService(userView, "ReadDegreeCurricularPlan", args);
        } catch (NotAuthorizedException e) {
            errors.add("notAuthorized", new ActionError("error.exception.notAuthorized2"));
        } catch (FenixServiceException e) {
            if (e.getMessage().equals("error.invalidExecutionDegree")) {
                errors.add("noDegreeCurricularPlan", new ActionError("error.coordinator.chosenDegree"));
            } else {
                e.printStackTrace();
                throw new FenixActionException(e);
            }
        }

        if (infoDegreeCurricularPlan == null) {
            errors.add("noDegreeCurricularPlan", new ActionError("error.coordinator.chosenDegree"));
        }
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }

        request.setAttribute("infoDegreeCurricularPlanID", degreeCurricularPlanID);

        DynaActionForm descriptionCurricularPlanForm = (DynaActionForm) form;
        descriptionCurricularPlanForm.set("descriptionDegreeCurricularPlan", infoDegreeCurricularPlan
                .getDescription());
        descriptionCurricularPlanForm.set("descriptionDegreeCurricularPlanEn", infoDegreeCurricularPlan
                .getDescriptionEn());

        return mapping.findForward("viewDescriptionCurricularPlan");
    }

    public ActionForward editDescriptionDegreeCurricularPlan(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        ActionErrors errors = new ActionErrors();

        IUserView userView = SessionUtils.getUserView(request);

        RequestUtils.getAndSetStringToRequest(request, "degreeCurricularPlanID");

        Integer infoExecutionDegreeId = getFromRequest("infoExecutionDegreeID", request);
        request.setAttribute("infoExecutionDegreeID", infoExecutionDegreeId);

        Integer infoDegreeCurricularPlanId = getFromRequest("infoDegreeCurricularPlanID", request);
        request.setAttribute("infoDegreeCurricularPlanID", infoDegreeCurricularPlanId);

        DynaActionForm descriptionCurricularPlanForm = (DynaActionForm) form;

        InfoDegreeCurricularPlan infoDegreeCurricularPlan = new InfoDegreeCurricularPlan();
        infoDegreeCurricularPlan.setIdInternal(infoDegreeCurricularPlanId);

        infoDegreeCurricularPlan.setDescription((String) descriptionCurricularPlanForm
                .get("descriptionDegreeCurricularPlan"));
        infoDegreeCurricularPlan.setDescriptionEn((String) descriptionCurricularPlanForm
                .get("descriptionDegreeCurricularPlanEn"));

        Object[] args = { infoDegreeCurricularPlan };
        try {
            infoDegreeCurricularPlan = (InfoDegreeCurricularPlan) ServiceManagerServiceFactory
                    .executeService(userView, "EditDescriptionDegreeCurricularPlan", args);
        } catch (NotAuthorizedException e) {
            errors.add("notAuthorized", new ActionError("error.exception.notAuthorized2"));
        } catch (NonExistingServiceException e) {
            errors.add("noDegreeCurricularPlan", new ActionError(
                    "message.nonExistingDegreeCurricularPlan"));
        } catch (FenixServiceException e) {
            if (e.getMessage().equals("message.nonExistingDegreeCurricularPlan")) {
                errors.add("nonExistingDegreeCurricularPlan", new ActionError(
                        "message.nonExistingDegreeCurricularPlan"));
            } else {
                e.printStackTrace();
                throw new FenixActionException(e);
            }
        }
        if (infoDegreeCurricularPlan == null) {
            errors.add("error.impossibleEditDCPInfo", new ActionError("error.impossibleEditDCPInfo"));
        }
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }

        // request.setAttribute("infoDegreeCurricularPlan",
        // infoDegreeCurricularPlan);
        request.setAttribute("infoDegreeID", infoDegreeCurricularPlan.getInfoDegree().getIdInternal());
        return mapping.findForward("editOK");
    }

    public ActionForward viewHistoric(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException {

        ActionErrors errors = new ActionErrors();

        IUserView userView = SessionUtils.getUserView(request);

        Integer degreeCurricularPlanID = new Integer(RequestUtils.getAndSetStringToRequest(request,
                "degreeCurricularPlanID"));

        // read execution degree
        Object[] args = { degreeCurricularPlanID };

        InfoExecutionDegree infoExecutionDegree = null;
        Integer degreeId = null;
        try {
            infoExecutionDegree = (InfoExecutionDegree) ServiceManagerServiceFactory.executeService(
                    userView, "ReadActiveExecutionDegreebyDegreeCurricularPlanID", args);
        } catch (FenixServiceException e) {
            errors.add("impossibleHistoricDegreeSite", new ActionError(
                    "error.coordinator.impossibleHistoricDegreeSite"));
        }
        if (infoExecutionDegree == null || infoExecutionDegree.getInfoDegreeCurricularPlan() == null
                || infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree() == null) {
            errors.add("impossibleHistoricDegreeSite", new ActionError(
                    "error.coordinator.impossibleHistoricDegreeSite"));
        }
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }

        // degreeId =
        // infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getIdInternal();
        degreeId = infoExecutionDegree.getInfoDegreeCurricularPlan().getIdInternal();
        // read all execution degres this degree
        Object[] args2 = { degreeId };

        List infoExecutionDegrees = null;
        try {
            infoExecutionDegrees = (List) ServiceManagerServiceFactory.executeService(null,
                    "ReadExecutionDegreesByDegreeCurricularPlanIDForCoordinator", args2);
        } catch (FenixServiceException e) {
            errors.add("impossibleDegreeSite", new ActionError("error.impossibleDegreeSite"));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }

        Collections.sort(infoExecutionDegrees, new BeanComparator("infoExecutionYear.beginDate"));

        request.setAttribute("infoExecutionDegrees", infoExecutionDegrees);

        return mapping.findForward("viewHistoric");
    }

    public Integer convertString2Integer(String string) {
        Integer integer = null;
        if (string != null && string.length() > 0) {
            integer = Integer.valueOf(string);
        }
        return integer;
    }

    public String convertInteger2String(Integer integer) {
        String string = null;

        if (integer != null) {
            string = String.valueOf(integer);
        }

        return string;
    }

    public Double convertString2Double(String string) {
        Double double1 = null;
        if (string != null && string.length() > 0) {
            double1 = Double.valueOf(string);
        }
        return double1;
    }

    public String convertDouble2String(Double double1) {
        String string = null;

        if (double1 != null) {
            string = String.valueOf(double1);
        }

        return string;
    }

    private Integer getFromRequest(String parameter, HttpServletRequest request) {
        Integer parameterCode = null;
        String parameterCodeString = request.getParameter(parameter);
        if (parameterCodeString == null) {
            parameterCodeString = (String) request.getAttribute(parameter);
        }
        if (parameterCodeString != null) {
            try {
                parameterCode = new Integer(parameterCodeString);
            } catch (Exception exception) {
                return null;
            }
        }
        return parameterCode;
    }

}