/*
 * Created on Feb 18, 2004
 */
package ServidorApresentacao.Action.teacher;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.action.DynaActionFormClass;
import org.apache.struts.config.FormBeanConfig;

import DataBeans.InfoBranch;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoPerson;
import DataBeans.InfoStudent;
import DataBeans.InfoStudentCurricularPlan;
import DataBeans.InfoTeacher;
import DataBeans.finalDegreeWork.FinalDegreeWorkProposalHeader;
import DataBeans.finalDegreeWork.InfoGroupProposal;
import DataBeans.finalDegreeWork.InfoProposal;
import DataBeans.finalDegreeWork.InfoScheduleing;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import ServidorApresentacao.Action.utils.CommonServiceRequests;
import Util.TipoCurso;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class FinalWorkManagementAction extends FenixDispatchAction
{

    public ActionForward submit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException
    {
        DynaActionForm finalWorkForm = (DynaActionForm) form;

        String idInternal = (String) finalWorkForm.get("idInternal");
        String title = (String) finalWorkForm.get("title");
        String responsibleCreditsPercentage = (String) finalWorkForm.get("responsibleCreditsPercentage");
        String coResponsibleCreditsPercentage = (String) finalWorkForm
                .get("coResponsibleCreditsPercentage");
        String companionName = (String) finalWorkForm.get("companionName");
        String companionMail = (String) finalWorkForm.get("companionMail");
        String companionPhone = (String) finalWorkForm.get("companionPhone");
        String framing = (String) finalWorkForm.get("framing");
        String objectives = (String) finalWorkForm.get("objectives");
        String description = (String) finalWorkForm.get("description");
        String requirements = (String) finalWorkForm.get("requirements");
        String deliverable = (String) finalWorkForm.get("deliverable");
        String url = (String) finalWorkForm.get("url");
        String minimumNumberOfGroupElements = (String) finalWorkForm.get("minimumNumberOfGroupElements");
        String maximumNumberOfGroupElements = (String) finalWorkForm.get("maximumNumberOfGroupElements");
        String degreeType = (String) finalWorkForm.get("degreeType");
        String observations = (String) finalWorkForm.get("observations");
        String location = (String) finalWorkForm.get("location");
        String companyAdress = (String) finalWorkForm.get("companyAdress");
        String companyName = (String) finalWorkForm.get("companyName");
        String orientatorOID = (String) finalWorkForm.get("orientatorOID");
        String coorientatorOID = (String) finalWorkForm.get("coorientatorOID");
        String degree = (String) finalWorkForm.get("degree");
        String[] branchList = (String[]) finalWorkForm.get("branchList");

        Integer min = new Integer(minimumNumberOfGroupElements);
        Integer max = new Integer(maximumNumberOfGroupElements);
        if ((min.intValue() > max.intValue()) || (min.intValue() <= 0))
        {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("finalWorkInformationForm.numberGroupElements.invalidInterval",
                    new ActionError("finalWorkInformationForm.numberGroupElements.invalidInterval"));
            saveErrors(request, actionErrors);
            return mapping.getInputForward();
        }

        Integer orientatorCreditsPercentage = new Integer(responsibleCreditsPercentage);
        Integer coorientatorCreditsPercentage = new Integer(coResponsibleCreditsPercentage);
        if ((orientatorCreditsPercentage.intValue() < 0)
                || (coorientatorCreditsPercentage.intValue() < 0)
                || (orientatorCreditsPercentage.intValue() + coorientatorCreditsPercentage.intValue() != 100))
        {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("finalWorkInformationForm.invalidCreditsPercentageDistribuition",
                    new ActionError("finalWorkInformationForm.invalidCreditsPercentageDistribuition"));
            saveErrors(request, actionErrors);
            return mapping.getInputForward();
        }

        InfoProposal infoFinalWorkProposal = new InfoProposal();
        if (idInternal != null && !idInternal.equals("") && StringUtils.isNumeric(idInternal))
        {
            infoFinalWorkProposal.setIdInternal(new Integer(idInternal));
        }
        infoFinalWorkProposal.setTitle(title);
        infoFinalWorkProposal.setOrientatorsCreditsPercentage(new Integer(responsibleCreditsPercentage));
        infoFinalWorkProposal.setCoorientatorsCreditsPercentage(new Integer(
                coResponsibleCreditsPercentage));
        infoFinalWorkProposal.setFraming(framing);
        infoFinalWorkProposal.setObjectives(objectives);
        infoFinalWorkProposal.setDescription(description);
        infoFinalWorkProposal.setRequirements(requirements);
        infoFinalWorkProposal.setDeliverable(deliverable);
        infoFinalWorkProposal.setUrl(url);
        infoFinalWorkProposal.setMinimumNumberOfGroupElements(new Integer(minimumNumberOfGroupElements));
        infoFinalWorkProposal.setMaximumNumberOfGroupElements(new Integer(maximumNumberOfGroupElements));
        infoFinalWorkProposal.setObservations(observations);
        infoFinalWorkProposal.setLocation(location);
        TipoCurso tipoCurso = new TipoCurso(new Integer(degreeType));
        infoFinalWorkProposal.setDegreeType(tipoCurso);

        infoFinalWorkProposal.setOrientator(new InfoTeacher());
        infoFinalWorkProposal.getOrientator().setIdInternal(new Integer(orientatorOID));
        if (coorientatorOID != null && !coorientatorOID.equals(""))
        {
            infoFinalWorkProposal.setCoorientator(new InfoTeacher());
            infoFinalWorkProposal.getCoorientator().setIdInternal(new Integer(coorientatorOID));
        }
        else
        {
            if (coorientatorCreditsPercentage.intValue() != 0)
            {
                ActionErrors actionErrors = new ActionErrors();
                actionErrors
                        .add(
                                "finalWorkInformationForm.invalidCreditsPercentageDistribuition",
                                new ActionError(
                                        "finalWorkInformationForm.invalidCreditsPercentageDistribuition"));
                saveErrors(request, actionErrors);
                return mapping.getInputForward();
            }

            infoFinalWorkProposal.setCompanionName(companionName);
            infoFinalWorkProposal.setCompanionMail(companionMail);
            if (companionPhone != null && !companionPhone.equals("")
                    && StringUtils.isNumeric(companionPhone))
            {
                infoFinalWorkProposal.setCompanionPhone(new Integer(companionPhone));
            }
            infoFinalWorkProposal.setCompanyAdress(companyAdress);
            infoFinalWorkProposal.setCompanyName(companyName);
        }
        infoFinalWorkProposal.setExecutionDegree(new InfoExecutionDegree());
        infoFinalWorkProposal.getExecutionDegree().setIdInternal(new Integer(degree));

        if (branchList != null && branchList.length > 0)
        {
            infoFinalWorkProposal.setBranches(new ArrayList());
            for (int i = 0; i < branchList.length; i++)
            {
                String brachOIDString = branchList[i];
                if (brachOIDString != null && StringUtils.isNumeric(brachOIDString))
                {
                    InfoBranch infoBranch = new InfoBranch();
                    infoBranch.setIdInternal(new Integer(brachOIDString));
                    infoFinalWorkProposal.getBranches().add(infoBranch);
                }
            }
        }

        try
        {
            IUserView userView = SessionUtils.getUserView(request);
            Object argsProposal[] = {infoFinalWorkProposal};
            ServiceUtils.executeService(userView, "SubmitFinalWorkProposal", argsProposal);
        }
        catch (FenixServiceException e)
        {
            throw new FenixActionException(e);
        }

        return mapping.findForward("SubmitionOfFinalDegreeWorkProposalSucessful");
    }

    public ActionForward chooseDegree(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException
    {
        IUserView userView = SessionUtils.getUserView(request);

        DynaActionForm finalWorkForm = (DynaActionForm) form;
        finalWorkForm.set("role", "responsable");
        finalWorkForm.set("responsibleCreditsPercentage", "100");
        finalWorkForm.set("coResponsibleCreditsPercentage", "0");

        List executionDegreeList;
        try
        {
            executionDegreeList = (List) ServiceUtils.executeService(userView,
                    "ReadExecutionDegreesOfTypeDegree", new Object[0]);
        }
        catch (FenixServiceException e)
        {
            throw new FenixActionException(e);
        }
        BeanComparator name = new BeanComparator("infoDegreeCurricularPlan.infoDegree.nome");
        Collections.sort(executionDegreeList, name);

        request.setAttribute("executionDegreeList", executionDegreeList);

        InfoTeacher infoTeacher = getTeacher(userView);
        Object args[] = {infoTeacher.getIdInternal()};
        try
        {
            List finalDegreeWorkProposalHeaders = (List) ServiceUtils.executeService(userView,
                    "ReadFinalDegreeWorkProposalHeadersByTeacher", args);

            if ((finalDegreeWorkProposalHeaders != null) && !(finalDegreeWorkProposalHeaders.isEmpty()))
            {
                BeanComparator title = new BeanComparator("title");
                Collections.sort(finalDegreeWorkProposalHeaders, title);

                String[] selectedProposals = new String[finalDegreeWorkProposalHeaders.size()];
                for (int i = 0; i < finalDegreeWorkProposalHeaders.size(); i++)
                {
                    FinalDegreeWorkProposalHeader header = (FinalDegreeWorkProposalHeader) finalDegreeWorkProposalHeaders
                            .get(i);

                    System.out.println("header.getGroupProposals()= " + header.getGroupProposals());
                    if (header.getGroupProposals() != null)
                    {
                        System.out.println("header.getGroupProposals().size= "
                                + header.getGroupProposals().size());
                    }

                    if (header.getGroupAttributedByTeacher() != null)
                    {
                        InfoGroupProposal infoGroupProposal = (InfoGroupProposal) CollectionUtils.find(
                                header.getGroupProposals(), new PREDICATE_FIND_GROUP_PROPOSAL_BY_GROUP(
                                        header.getGroupAttributedByTeacher().getIdInternal()));
                        selectedProposals[i] = infoGroupProposal.getIdInternal().toString();
                        System.out.println("selectedProposals[" + i + "]= " + selectedProposals[i]);
                    }
                }
                FormBeanConfig fbc2 = new FormBeanConfig();
                fbc2.setModuleConfig(mapping.getModuleConfig());
                fbc2.setName("finalWorkAttributionForm");
                DynaActionFormClass dafc2 = DynaActionFormClass.createDynaActionFormClass(fbc2);
                DynaActionForm finalWorkAttributionForm;
                try
                {
                    finalWorkAttributionForm = (DynaActionForm) dafc2.newInstance();
                }
                catch (Exception e1)
                {
                    throw new FenixActionException(e1);
                }
                finalWorkAttributionForm.set("selectedGroupProposals", selectedProposals);
                request.setAttribute("finalWorkAttributionForm", finalWorkAttributionForm);
                System.out.println("selectedProposals= " + selectedProposals);

                request.setAttribute("finalDegreeWorkProposalHeaders", finalDegreeWorkProposalHeaders);
            }
        }
        catch (FenixServiceException e)
        {
            throw new FenixActionException(e);
        }

        return mapping.findForward("chooseDegreeForFinalWorkProposal");
    }

    public ActionForward prepareFinalWorkInformation(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException
    {
        IUserView userView = SessionUtils.getUserView(request);

        DynaActionForm finalWorkForm = (DynaActionForm) form;
        String role = (String) finalWorkForm.get("role");
        String degreeId = (String) finalWorkForm.get("degree");
        finalWorkForm.set("degreeType", "" + TipoCurso.LICENCIATURA);

        InfoExecutionDegree infoExecutionDegree = CommonServiceRequests.getInfoExecutionDegree(userView,
                new Integer(degreeId));

        InfoScheduleing infoScheduleing = null;
        try
        {
            Object[] args = {new Integer(degreeId)};
            infoScheduleing = (InfoScheduleing) ServiceUtils.executeService(userView,
                    "ReadFinalDegreeWorkProposalSubmisionPeriod", args);
            if (infoScheduleing == null
                    || infoScheduleing.getStartOfProposalPeriod().getTime() > Calendar.getInstance()
                            .getTimeInMillis()
                    || infoScheduleing.getEndOfProposalPeriod().getTime() < Calendar.getInstance()
                            .getTimeInMillis())
            {
                ActionErrors actionErrors = new ActionErrors();

                if (infoScheduleing != null)
                {
                    actionErrors.add("finalDegreeWorkProposal.ProposalPeriod.validator.OutOfPeriod",
                            new ActionError(
                                    "finalDegreeWorkProposal.ProposalPeriod.validator.OutOfPeriod"));
                    request.setAttribute("infoScheduleing", infoScheduleing);
                }
                else
                {
                    actionErrors
                            .add("finalDegreeWorkProposal.ProposalPeriod.interval.undefined",
                                    new ActionError(
                                            "finalDegreeWorkProposal.ProposalPeriod.interval.undefined"));
                }
                saveErrors(request, actionErrors);

                return mapping.findForward("OutOfSubmisionPeriod");
            }
        }
        catch (FenixServiceException fse)
        {
            throw new FenixActionException(fse);
        }

        InfoTeacher infoTeacher = getTeacher(userView);

        if (role != null && role.equals("responsable"))
        {
            finalWorkForm.set("orientatorOID", infoTeacher.getIdInternal().toString());
            finalWorkForm.set("responsableTeacherName", infoTeacher.getInfoPerson().getNome());
            request.setAttribute("orientator", infoTeacher);
        }
        else if (role != null && role.equals("coResponsable"))
        {
            finalWorkForm.set("coorientatorOID", infoTeacher.getIdInternal().toString());
            finalWorkForm.set("coResponsableTeacherName", infoTeacher.getInfoPerson().getNome());
            request.setAttribute("coorientator", infoTeacher);
        }

        List branches = CommonServiceRequests.getBranchesByDegreeCurricularPlan(userView,
                infoExecutionDegree.getInfoDegreeCurricularPlan().getIdInternal());
        request.setAttribute("branches", branches);

        return mapping.findForward("submitFinalWorkProposal");
    }

    public ActionForward showTeacherName(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException
    {
        IUserView userView = SessionUtils.getUserView(request);

        DynaActionForm finalWorkForm = (DynaActionForm) form;
        String alteredField = (String) finalWorkForm.get("alteredField");
        String number = null;

        if (alteredField.equals("orientator"))
        {
            number = (String) finalWorkForm.get("responsableTeacherNumber");
        }
        else if (alteredField.equals("coorientator"))
        {
            number = (String) finalWorkForm.get("coResponsableTeacherNumber");
        }

        if (number == null || number.equals(""))
        {
            if (alteredField.equals("orientator"))
            {
                finalWorkForm.set("orientatorOID", "");
                finalWorkForm.set("responsableTeacherName", "");
            }
            else if (alteredField.equals("coorientator"))
            {
                finalWorkForm.set("coorientatorOID", "");
                finalWorkForm.set("coResponsableTeacherName", "");
            }

            return prepareFinalWorkInformation(mapping, form, request, response);
        }

        Object[] args = {new Integer(number)};
        InfoTeacher infoTeacher;
        try
        {
            infoTeacher = (InfoTeacher) ServiceUtils.executeService(userView, "ReadTeacherByNumber",
                    args);
            if (infoTeacher == null)
            {
                ActionErrors actionErrors = new ActionErrors();
                actionErrors.add("finalWorkInformationForm.unexistingTeacher", new ActionError(
                        "finalWorkInformationForm.unexistingTeacher"));
                saveErrors(request, actionErrors);
                return mapping.getInputForward();
            }
        }
        catch (FenixServiceException e)
        {
            throw new FenixActionException(e);
        }

        InfoTeacher teacherUser = getTeacher(userView);

        if (alteredField.equals("orientator"))
        {
            finalWorkForm.set("orientatorOID", infoTeacher.getIdInternal().toString());
            finalWorkForm.set("responsableTeacherName", infoTeacher.getIdInternal().toString());
            request.setAttribute("orientator", infoTeacher);
            if (infoTeacher.getIdInternal().equals(teacherUser.getIdInternal()))
            {
                finalWorkForm.set("role", "responsable");
            }
        }
        else
        {
            if (alteredField.equals("coorientator"))
            {
                finalWorkForm.set("coorientatorOID", infoTeacher.getIdInternal().toString());
                finalWorkForm.set("coResponsableTeacherName", infoTeacher.getIdInternal().toString());
                finalWorkForm.set("companionName", "");
                finalWorkForm.set("companionMail", "");
                finalWorkForm.set("companionPhone", "");
                finalWorkForm.set("companyAdress", "");
                finalWorkForm.set("companyName", "");
                finalWorkForm.set("alteredField", "");
                request.setAttribute("coorientator", infoTeacher);
                if (infoTeacher.getIdInternal().equals(teacherUser.getIdInternal()))
                {
                    finalWorkForm.set("role", "coResponsable");
                }
            }
        }

        return prepareFinalWorkInformation(mapping, form, request, response);
    }

    public ActionForward coorientatorVisibility(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException
    {
        DynaActionForm finalWorkForm = (DynaActionForm) form;
        String alteredField = (String) finalWorkForm.get("alteredField");
        String companionName = (String) finalWorkForm.get("companionName");
        String companionMail = (String) finalWorkForm.get("companionMail");
        String companionPhone = (String) finalWorkForm.get("companionPhone");
        String companyAdress = (String) finalWorkForm.get("companyAdress");
        String companyName = (String) finalWorkForm.get("companyName");

        if (alteredField.equals("companion") && companionName.equals("") && companionMail.equals("")
                && companionPhone.equals("") && companyAdress.equals("") && companyName.equals(""))
        {
            finalWorkForm.set("coorientatorOID", "");
            finalWorkForm.set("coResponsableTeacherName", "");
            finalWorkForm.set("alteredField", "");
        }
        else
        {
            if (alteredField.equals("companion") || !companionName.equals("")
                    || !companionMail.equals("") || !companionPhone.equals("")
                    || !companyAdress.equals("") || !companyName.equals(""))
            {
                finalWorkForm.set("alteredField", "companion");
            }

        }

        return prepareFinalWorkInformation(mapping, form, request, response);
    }

    public ActionForward deleteFinalDegreeWorkProposal(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException
    {
        String finalDegreeWorkProposalOIDString = request.getParameter("finalDegreeWorkProposalOID");

        if (finalDegreeWorkProposalOIDString != null
                && StringUtils.isNumeric(finalDegreeWorkProposalOIDString))
        {
            IUserView userView = SessionUtils.getUserView(request);

            Object[] args = {new Integer(finalDegreeWorkProposalOIDString)};
            try
            {
                ServiceUtils.executeService(userView, "DeleteFinalDegreeWorkProposal", args);
            }
            catch (FenixServiceException fse)
            {
                throw new FenixActionException(fse);
            }
        }

        return chooseDegree(mapping, form, request, response);
    }

    public ActionForward viewFinalDegreeWorkProposal(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException
    {
        String finalDegreeWorkProposalOIDString = request.getParameter("finalDegreeWorkProposalOID");

        if (finalDegreeWorkProposalOIDString != null
                && StringUtils.isNumeric(finalDegreeWorkProposalOIDString))
        {
            IUserView userView = SessionUtils.getUserView(request);

            Object args[] = {new Integer(finalDegreeWorkProposalOIDString)};
            try
            {
                InfoProposal infoProposal = (InfoProposal) ServiceUtils.executeService(userView,
                        "ReadFinalDegreeWorkProposal", args);

                if (infoProposal != null)
                {
                    request.setAttribute("finalDegreeWorkProposal", infoProposal);
                }
            }
            catch (FenixServiceException e)
            {
                throw new FenixActionException();
            }
        }

        return mapping.findForward("viewFinalDegreeWorkProposal");
    }

    public ActionForward editFinalDegreeWorkProposal(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException
    {
        String finalDegreeWorkProposalOIDString = request.getParameter("finalDegreeWorkProposalOID");

        if (finalDegreeWorkProposalOIDString != null
                && StringUtils.isNumeric(finalDegreeWorkProposalOIDString))
        {
            IUserView userView = SessionUtils.getUserView(request);

            Object args[] = {new Integer(finalDegreeWorkProposalOIDString)};
            try
            {
                InfoProposal infoProposal = (InfoProposal) ServiceUtils.executeService(userView,
                        "ReadFinalDegreeWorkProposal", args);

                if (infoProposal != null)
                {
                    DynaActionForm finalWorkForm = (DynaActionForm) form;

                    if (infoProposal.getIdInternal() != null)
                    {
                        finalWorkForm.set("idInternal", infoProposal.getIdInternal().toString());
                    }
                    finalWorkForm.set("title", infoProposal.getTitle());
                    if (infoProposal.getOrientatorsCreditsPercentage() != null)
                    {
                        finalWorkForm.set("responsibleCreditsPercentage", infoProposal
                                .getOrientatorsCreditsPercentage().toString());
                    }
                    if (infoProposal.getCoorientatorsCreditsPercentage() != null)
                    {
                        finalWorkForm.set("coResponsibleCreditsPercentage", infoProposal
                                .getCoorientatorsCreditsPercentage().toString());
                    }
                    finalWorkForm.set("companionName", infoProposal.getCompanionName());
                    finalWorkForm.set("companionMail", infoProposal.getCompanionMail());
                    if (infoProposal.getCompanionPhone() != null)
                    {
                        finalWorkForm.set("companionPhone", infoProposal.getCompanionPhone().toString());
                    }
                    finalWorkForm.set("framing", infoProposal.getFraming());
                    finalWorkForm.set("objectives", infoProposal.getObjectives());
                    finalWorkForm.set("description", infoProposal.getDescription());
                    finalWorkForm.set("requirements", infoProposal.getRequirements());
                    finalWorkForm.set("deliverable", infoProposal.getDeliverable());
                    finalWorkForm.set("url", infoProposal.getUrl());
                    if (infoProposal.getMaximumNumberOfGroupElements() != null)
                    {
                        finalWorkForm.set("maximumNumberOfGroupElements", infoProposal
                                .getMaximumNumberOfGroupElements().toString());
                    }
                    if (infoProposal.getMinimumNumberOfGroupElements() != null)
                    {
                        finalWorkForm.set("minimumNumberOfGroupElements", infoProposal
                                .getMinimumNumberOfGroupElements().toString());
                    }
                    if (infoProposal.getDegreeType() != null
                            && infoProposal.getDegreeType().getTipoCurso() != null)
                    {
                        finalWorkForm.set("degreeType", infoProposal.getDegreeType().getTipoCurso()
                                .toString());
                    }
                    finalWorkForm.set("observations", infoProposal.getObservations());
                    finalWorkForm.set("location", infoProposal.getLocation());
           
					finalWorkForm.set(
						"companyAdress",
						infoProposal.getCompanyAdress());
					finalWorkForm.set(
						"companyName",
						infoProposal.getCompanyName());
                    if (infoProposal.getOrientator() != null
                            && infoProposal.getOrientator().getIdInternal() != null)
                    {
                        finalWorkForm.set("orientatorOID", infoProposal.getOrientator().getIdInternal()
                                .toString());
                        finalWorkForm.set("responsableTeacherNumber", infoProposal.getOrientator()
                                .getTeacherNumber().toString());
                        finalWorkForm.set("responsableTeacherName", infoProposal.getOrientator()
                                .getInfoPerson().getNome());

                        InfoTeacher infoTeacher = getTeacher(userView);
                        if (infoTeacher.getTeacherNumber().equals(
                                infoProposal.getOrientator().getTeacherNumber()))
                        {
                            finalWorkForm.set("role", "responsable");
                        }
                        else
                        {
                            finalWorkForm.set("role", "coResponsable");
                        }
                    }
                    if (infoProposal.getCoorientator() != null
                            && infoProposal.getCoorientator().getIdInternal() != null)
                    {
                        finalWorkForm.set("coorientatorOID", infoProposal.getCoorientator()
                                .getIdInternal().toString());
                        finalWorkForm.set("coResponsableTeacherNumber", infoProposal.getCoorientator()
                                .getTeacherNumber().toString());
                        finalWorkForm.set("coResponsableTeacherName", infoProposal.getCoorientator()
                                .getInfoPerson().getNome());
                    }
                    if (infoProposal.getExecutionDegree() != null
                            && infoProposal.getExecutionDegree().getIdInternal() != null)
                    {
                        finalWorkForm.set("degree", infoProposal.getExecutionDegree().getIdInternal()
                                .toString());
                    }

                    if (infoProposal.getBranches() != null && infoProposal.getBranches().size() > 0)
                    {
                        String[] branchList = new String[infoProposal.getBranches().size()];
                        for (int i = 0; i < infoProposal.getBranches().size(); i++)
                        {
                            InfoBranch infoBranch = ((InfoBranch) infoProposal.getBranches().get(i));
                            if (infoBranch != null && infoBranch.getIdInternal() != null)
                            {
                                String brachOIDString = infoBranch.getIdInternal().toString();
                                if (brachOIDString != null && StringUtils.isNumeric(brachOIDString))
                                {
                                    branchList[i] = brachOIDString;
                                }
                            }
                        }
                        finalWorkForm.set("branchList", branchList);
                    }

                    InfoExecutionDegree infoExecutionDegree = CommonServiceRequests
                            .getInfoExecutionDegree(userView, infoProposal.getExecutionDegree()
                                    .getIdInternal());
                    List branches = CommonServiceRequests.getBranchesByDegreeCurricularPlan(userView,
                            infoExecutionDegree.getInfoDegreeCurricularPlan().getIdInternal());

                    request.setAttribute("branches", branches);
                }
            }
            catch (FenixServiceException e)
            {
                throw new FenixActionException(e);
            }
        }

        return mapping.findForward("submitFinalWorkProposal");
    }

    InfoTeacher getTeacher(IUserView userView) throws FenixActionException
    {
        Object argsTeacher[] = {userView.getUtilizador()};

        InfoTeacher infoTeacher;
        try
        {
            infoTeacher = (InfoTeacher) ServiceUtils.executeService(userView, "ReadTeacherByUsername",
                    argsTeacher);
            if (infoTeacher == null) { throw new FenixActionException(
                    "Unable to identify user as a teacher."); }
        }
        catch (FenixServiceException e)
        {
            throw new FenixActionException(e);
        }

        return infoTeacher;
    }

    public ActionForward attributeFinalDegreeWork(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException
    {
        DynaActionForm finalWorkAttributionForm = (DynaActionForm) form;

        String selectedGroupProposalOID = (String) finalWorkAttributionForm.get("selectedGroupProposal");

        IUserView userView = SessionUtils.getUserView(request);

        if (selectedGroupProposalOID != null && !selectedGroupProposalOID.equals(""))
        {
            Object args[] = {new Integer(selectedGroupProposalOID)};
            ServiceUtils.executeService(userView, "TeacherAttributeFinalDegreeWork", args);
        }

        return mapping.findForward("sucess");
    }

    private class PREDICATE_FIND_GROUP_PROPOSAL_BY_GROUP implements Predicate
    {

        Integer groupID = null;

        public boolean evaluate(Object arg0)
        {
            InfoGroupProposal infoGroupProposal = (InfoGroupProposal) arg0;
            return groupID.equals(infoGroupProposal.getInfoGroup().getIdInternal());
        }

        public PREDICATE_FIND_GROUP_PROPOSAL_BY_GROUP(Integer groupID)
        {
            super();
            this.groupID = groupID;
        }
    }

    public ActionForward getCurriculum(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception
    {

        HttpSession session = request.getSession();

        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        String studentCurricularPlanID = request.getParameter("studentCPID");
        if (studentCurricularPlanID == null)
        {
            studentCurricularPlanID = (String) request.getAttribute("studentCPID");
        }
        
        Integer executionDegreeId = getExecutionDegree(request);
        List result = null;

        System.out.println("studentCurricularPlanID= " + studentCurricularPlanID);
        System.out.println("executionDegreeId= " + executionDegreeId);
        Object args1[] = {executionDegreeId, Integer.valueOf(studentCurricularPlanID)};
        result = (ArrayList) ServiceManagerServiceFactory.executeService(userView,
                "ReadStudentCurriculum", args1);

        BeanComparator courseName = new BeanComparator("infoCurricularCourse.name");
        BeanComparator executionYear = new BeanComparator("infoExecutionPeriod.infoExecutionYear.year");
        ComparatorChain chainComparator = new ComparatorChain();
        chainComparator.addComparator(courseName);
        chainComparator.addComparator(executionYear);

        Collections.sort(result, chainComparator);

        InfoStudentCurricularPlan infoStudentCurricularPlan = null;
        try
        {
            Object args[] = {Integer.valueOf(studentCurricularPlanID)};
            infoStudentCurricularPlan = (InfoStudentCurricularPlan) ServiceManagerServiceFactory
                    .executeService(userView, "ReadStudentCurricularPlan", args);
        }
        catch (ExistingServiceException e)
        {
            throw new ExistingActionException(e);
        }

        request.setAttribute(SessionConstants.CURRICULUM, result);
        request.setAttribute(SessionConstants.STUDENT_CURRICULAR_PLAN, infoStudentCurricularPlan);

        return mapping.findForward("ShowStudentCurriculum");
    }

    public ActionForward getStudentCP(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception
    {

        HttpSession session = request.getSession();
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        String studentNumber = getStudent(request);
        List infoStudents = null;

        if (studentNumber == null)
        {
            try
            {
                Object args1[] = {userView.getUtilizador()};
                InfoPerson infoPerson = (InfoPerson) ServiceManagerServiceFactory.executeService(
                        userView, "ReadPersonByUsername", args1);

                Object args2[] = {infoPerson};
                infoStudents = (List) ServiceManagerServiceFactory.executeService(userView,
                        "ReadStudentsByPerson", args2);
            }
            catch (FenixServiceException e)
            {
                throw new FenixActionException(e);
            }
        }
        else
        {
            try
            {
                Object args[] = {Integer.valueOf(studentNumber)};
                InfoStudent infoStudent = (InfoStudent) ServiceManagerServiceFactory.executeService(
                        userView, "ReadStudentByNumberAndAllDegreeTypes", args);
                infoStudents = new ArrayList();
                infoStudents.add(infoStudent);
            }
            catch (FenixServiceException e)
            {
                throw new FenixActionException(e);
            }

        }

        List result = new ArrayList();
        if (infoStudents != null)
        {
            Iterator iterator = infoStudents.iterator();
            while (iterator.hasNext())
            {
                InfoStudent infoStudent = (InfoStudent) iterator.next();
                try
                {
                    Object args[] = {infoStudent.getNumber(), infoStudent.getDegreeType()};
                    List resultTemp = (ArrayList) ServiceManagerServiceFactory.executeService(userView,
                            "ReadStudentCurricularPlans", args);
                    result.addAll(resultTemp);
                }
                catch (NonExistingServiceException e)
                {
                    //
                }
            }
        }

        getExecutionDegree(request);

        request.setAttribute("studentCPs", result);

        return mapping.findForward("ShowStudentCurricularPlans");
    }

    private String getStudent(HttpServletRequest request)
    {
        String studentNumber = request.getParameter("studentNumber");
        if (studentNumber == null)
        {
            studentNumber = (String) request.getAttribute("studentNumber");
        }
        return studentNumber;
    }

    private Integer getExecutionDegree(HttpServletRequest request)
    {
        Integer executionDegreeId = null;

        String executionDegreeIdString = request.getParameter("executionDegreeId");
        if (executionDegreeIdString == null)
        {
            executionDegreeIdString = (String) request.getAttribute("executionDegreeId");
        }
        if (executionDegreeIdString != null)
        {
            executionDegreeId = Integer.valueOf(executionDegreeIdString);
        }
        request.setAttribute("executionDegreeId", executionDegreeId);

        return executionDegreeId;
    }

}