/*
 * Created on Jan 18, 2005
 *
 */
package ServidorApresentacao.Action.manager.guideManagement;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoGuide;
import DataBeans.InfoGuideEntry;
import DataBeans.transactions.InfoPaymentTransaction;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import Util.Data;
import Util.DocumentType;
import Util.GraduationType;
import Util.SituationOfGuide;

/**
 * @author <a href="mailto:shezad@ist.utl.pt">Shezad Anavarali </a>
 * 
 */
public class GuideManagementDispatchAction extends FenixDispatchAction {

    public ActionForward firstPage(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward("firstPage");
    }

    public ActionForward prepareChooseGuide(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        return mapping.findForward("chooseGuide");
    }

    public ActionForward chooseGuide(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        IUserView userView = SessionUtils.getUserView(request);

        DynaActionForm guideForm = (DynaActionForm) actionForm;
        Integer number = (Integer) guideForm.get("number");
        Integer year = (Integer) guideForm.get("year");
        Integer version = (Integer) guideForm.get("version");

        // read guide
        InfoGuide guide = null;
        try {

            if (version.intValue() == 0) {
                Object[] args = { number, year };
                List guidesList = (List) ServiceUtils.executeService(userView, "ChooseGuide", args);
                guide = (InfoGuide) guidesList.get(0);
            } else {
                Object[] args = { number, year, version };
                guide = (InfoGuide) ServiceUtils.executeService(userView, "ChooseGuide", args);
            }

        } catch (NonExistingServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FenixServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // read transactions
        List paymentTransactions = new ArrayList();
        for (Iterator iter = guide.getInfoGuideEntries().iterator(); iter.hasNext();) {
            InfoGuideEntry guideEntry = (InfoGuideEntry) iter.next();
            InfoPaymentTransaction paymentTransaction = null;

            Object[] argsPaymentTransactions = { guideEntry.getIdInternal() };
            try {
                paymentTransaction = (InfoPaymentTransaction) ServiceUtils.executeService(userView,
                        "ReadPaymentTransactionByGuideEntryID", argsPaymentTransactions);
            } catch (FenixServiceException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            paymentTransactions.add(paymentTransaction);
        }

        List executionYears = null;
        Object[] argsEmpty = {};
        try {
            executionYears = (List) ServiceUtils.executeService(userView, "ReadExecutionYears",
                    argsEmpty);
        } catch (FenixServiceException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        List degreeCurricularPlans = null;
        try {
            degreeCurricularPlans = (List) ServiceUtils.executeService(userView,
                    "ReadDegreeCurricularPlansLabelValueBeanList", argsEmpty);
        } catch (FenixServiceException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        guideForm.set("guideID", guide.getIdInternal());
        guideForm.set("newExecutionYear", guide.getInfoExecutionDegree().getInfoExecutionYear()
                .getYear());
        guideForm.set("newDegreeCurricularPlanID", guide.getInfoExecutionDegree()
                .getInfoDegreeCurricularPlan().getIdInternal());
        request.setAttribute("paymentTransactions", paymentTransactions);
        request.setAttribute("degreeCurricularPlans", degreeCurricularPlans);
        request.setAttribute("executionYears", executionYears);
        request.setAttribute("guide", guide);
        request.setAttribute("documentTypes", DocumentType.toArrayListWithIntValues());
        request.setAttribute("situationTypes", SituationOfGuide.toArrayList());
        request.setAttribute("days", Data.getMonthDays());
        request.setAttribute("months", Data.getMonths());
        request.setAttribute("years", Data.getYears());

        return mapping.findForward("editGuide");
    }

    public ActionForward addGuideEntry(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        IUserView userView = SessionUtils.getUserView(request);

        DynaActionForm guideForm = (DynaActionForm) actionForm;
        Integer guideID = (Integer) guideForm.get("guideID");
        String newEntryDescription = (String) guideForm.get("newEntryDescription");
        Integer newEntryQuantity = (Integer) guideForm.get("newEntryQuantity");
        Double newEntryPrice = (Double) guideForm.get("newEntryPrice");
        Integer newEntryDocumentType = (Integer) guideForm.get("newEntryDocumentType");

        Object[] args = { guideID, GraduationType.MASTER_DEGREE_TYPE,
                new DocumentType(newEntryDocumentType), newEntryDescription, newEntryPrice,
                newEntryQuantity };
        try {
            ServiceUtils.executeService(userView, "CreateGuideEntry", args);
        } catch (FenixServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        guideForm.set("newEntryDescription", null);
        guideForm.set("newEntryQuantity", null);
        guideForm.set("newEntryPrice", null);
        guideForm.set("newEntryDocumentType", null);

        return chooseGuide(mapping, actionForm, request, response);
    }

    public ActionForward addGuideSituation(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        IUserView userView = SessionUtils.getUserView(request);

        DynaActionForm guideForm = (DynaActionForm) actionForm;

        /** ***************** */

        Integer guideID = (Integer) guideForm.get("guideID");
        String newSituationRemarks = (String) guideForm.get("newSituationRemarks");
        Integer newSituationDay = (Integer) guideForm.get("newSituationDay");
        Integer newSituationMonth = (Integer) guideForm.get("newSituationMonth");
        Integer newSituationYear = (Integer) guideForm.get("newSituationYear");
        String newSituationType = (String) guideForm.get("newSituationType");

        Date date = (new GregorianCalendar(newSituationYear.intValue(), newSituationMonth.intValue(),
                newSituationDay.intValue())).getTime();

        Object[] args = { guideID, newSituationRemarks, new SituationOfGuide(newSituationType), date };

        try {
            ServiceUtils.executeService(userView, "CreateGuideSituation", args);
        } catch (FenixServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        guideForm.set("newSituationRemarks", null);
        guideForm.set("newSituationDay", null);
        guideForm.set("newSituationMonth", null);
        guideForm.set("newSituationYear", null);
        guideForm.set("newSituationType", null);

        return chooseGuide(mapping, actionForm, request, response);
    }

    public ActionForward createPaymentTransaction(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        IUserView userView = SessionUtils.getUserView(request);

        DynaActionForm guideForm = (DynaActionForm) actionForm;

        Integer selectedGuideEntryDocumentType = (Integer) guideForm
                .get("selectedGuideEntryDocumentType");
        Integer selectedGuideEntryID = (Integer) guideForm.get("selectedGuideEntryID");

        Object[] args = { selectedGuideEntryID, userView };

        try {

            if (selectedGuideEntryDocumentType.intValue() == DocumentType.GRATUITY) {
                ServiceUtils.executeService(userView, "CreateGratuityTransaction", args);
            } else if (selectedGuideEntryDocumentType.intValue() == DocumentType.INSURANCE) {
                ServiceUtils.executeService(userView, "CreateInsuranceTransaction", args);
            }

        } catch (FenixServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return chooseGuide(mapping, actionForm, request, response);

    }

    public ActionForward editExecutionDegree(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        IUserView userView = SessionUtils.getUserView(request);

        DynaActionForm guideForm = (DynaActionForm) actionForm;

        Integer newDegreeCurricularPlanID = (Integer) guideForm.get("newDegreeCurricularPlanID");
        String newExecutionYear = (String) guideForm.get("newExecutionYear");
        Integer guideID = (Integer) guideForm.get("guideID");

        Object[] args = { guideID, newDegreeCurricularPlanID, newExecutionYear };
        try {
            ServiceUtils.executeService(userView, "EditGuideInformationInManager", args);
        } catch (FenixServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return chooseGuide(mapping, actionForm, request, response);

    }

    public ActionForward deleteGuideSituation(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        IUserView userView = SessionUtils.getUserView(request);

        DynaActionForm guideForm = (DynaActionForm) actionForm;
        Integer guideSituationID = (Integer) guideForm.get("guideSituationID");

        Object[] args = { guideSituationID };
        try {
            ServiceUtils.executeService(userView, "DeleteGuideSituationInManager", args);
        } catch (FenixServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return chooseGuide(mapping, actionForm, request, response);

    }

    public ActionForward deleteGuideEntry(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        IUserView userView = SessionUtils.getUserView(request);

        DynaActionForm guideForm = (DynaActionForm) actionForm;
        Integer selectedGuideEntryID = (Integer) guideForm.get("selectedGuideEntryID");

        Object[] args = { selectedGuideEntryID };
        try {
            ServiceUtils
                    .executeService(userView, "DeleteGuideEntryAndPaymentTransactionInManager", args);
        } catch (FenixServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return chooseGuide(mapping, actionForm, request, response);

    }

    public ActionForward deleteGuide(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        IUserView userView = SessionUtils.getUserView(request);

        DynaActionForm guideForm = (DynaActionForm) actionForm;
        Integer guideID = (Integer) guideForm.get("guideID");

        Object[] args = { guideID };
        try {
            ServiceUtils.executeService(userView, "DeleteGuideVersionInManager", args);
        } catch (FenixServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return chooseGuide(mapping, actionForm, request, response);
        }

        return mapping.findForward("firstPage");

    }

}
