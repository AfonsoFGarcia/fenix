/*
 * Created on 2004/03/09
 */
package net.sourceforge.fenixedu.presentationTier.Action.coordinator;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.OutOfPeriodException;
import net.sourceforge.fenixedu.dataTransferObject.InfoBranch;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.FinalDegreeWorkProposalHeader;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoGroup;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoProposal;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoScheduleing;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseType;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupStudent;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Scheduleing;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.presentationTier.Action.utils.CommonServiceRequests;
import net.sourceforge.fenixedu.util.FinalDegreeWorkProposalStatus;
import net.sourceforge.fenixedu.util.report.Spreadsheet;
import net.sourceforge.fenixedu.util.report.Spreadsheet.Row;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.action.DynaActionFormClass;
import org.apache.struts.config.FormBeanConfig;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.util.MessageResources;

/**
 * @author Luis Cruz
 */
public class ManageFinalDegreeWorkDispatchAction extends FenixDispatchAction {

    public ActionForward showChooseExecutionDegreeForm(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException, FenixServiceException {
        final IUserView userView = SessionUtils.getUserView(request);

        final Integer degreeCurricularPlanOID = Integer.valueOf(request
                .getParameter("degreeCurricularPlanID"));
        final Object[] args = { degreeCurricularPlanOID };
        final List<InfoExecutionDegree> infoExecutionDegrees = (List<InfoExecutionDegree>) ServiceUtils
                .executeService(userView, "ReadExecutionDegreesByDegreeCurricularPlan", args);
        request.setAttribute("infoExecutionDegrees", infoExecutionDegrees);
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanOID);

        return mapping.findForward("show-choose-execution-degree-page");
    }

    public ActionForward showChooseExecutionDegreeFormForDepartment(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException, FenixServiceException {
        final IUserView userView = getUserView(request);
        final Department department = userView.getPerson().getEmployee().getCurrentDepartmentWorkingPlace();

        final Set<ExecutionDegree> executionDegrees = new TreeSet<ExecutionDegree>(ExecutionDegree.EXECUTION_DEGREE_COMPARATORY_BY_DEGREE_TYPE_AND_NAME_AND_EXECUTION_YEAR);
        for (final CompetenceCourse competenceCourse : department.getCompetenceCourses()) {
            for (final CurricularCourse curricularCourse : competenceCourse.getAssociatedCurricularCourses()) {
                if (curricularCourse.getType() == CurricularCourseType.TFC_COURSE) {
                    final DegreeCurricularPlan degreeCurricularPlan = curricularCourse.getDegreeCurricularPlan();
                    executionDegrees.addAll(degreeCurricularPlan.getExecutionDegrees());
                }
            }
        }
        request.setAttribute("executionDegrees", executionDegrees);

        return mapping.findForward("show-choose-execution-degree-page");
    }

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException, FenixServiceException {
        IUserView userView = SessionUtils.getUserView(request);

        Integer degreeCurricularPlanID = null;
        if (request.getParameter("degreeCurricularPlanID") != null) {
            degreeCurricularPlanID = new Integer(request.getParameter("degreeCurricularPlanID"));
            request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);
        }

        DynaActionForm dynaActionForm = (DynaActionForm) form;
        Integer executionDegreeOID = Integer.valueOf((String) dynaActionForm.get("executionDegreeOID"));
        request.setAttribute("executionDegreeOID", executionDegreeOID);
        final ExecutionDegree executionDegree = (ExecutionDegree) readDomainObject(request, ExecutionDegree.class, executionDegreeOID);
        request.setAttribute("executionDegree", executionDegree);

        Object args[] = { executionDegreeOID };
        List finalDegreeWorkProposalHeaders = null;
        try {
            finalDegreeWorkProposalHeaders = (List) ServiceUtils.executeService(userView,
                    "ReadFinalDegreeWorkProposalHeadersForDegreeCurricularPlan", args);

            if (finalDegreeWorkProposalHeaders != null && !finalDegreeWorkProposalHeaders.isEmpty()) {
                Collections.sort(finalDegreeWorkProposalHeaders, new BeanComparator("proposalNumber"));

                request.setAttribute("finalDegreeWorkProposalHeaders", finalDegreeWorkProposalHeaders);
            }
        } catch (FenixFilterException e) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("notAuthorized", new ActionError("error.exception.notAuthorized"));
            saveErrors(request, actionErrors);

            degreeCurricularPlanID = new Integer(request.getParameter("degreeCurricularPlanID"));
            request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);

            return mapping.findForward("error");
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        try {
            InfoScheduleing infoScheduleing = (InfoScheduleing) ServiceUtils.executeService(userView,
                    "ReadFinalDegreeWorkProposalSubmisionPeriod", args);

            if (infoScheduleing != null) {
                SimpleDateFormat dateFormatDate = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat dateFormatHour = new SimpleDateFormat("HH:mm");

                DynaActionForm finalDegreeWorkScheduleingForm = (DynaActionForm) form;
                if (infoScheduleing.getStartOfProposalPeriod() != null) {
                    finalDegreeWorkScheduleingForm.set("startOfProposalPeriodDate", dateFormatDate
                            .format(infoScheduleing.getStartOfProposalPeriod()));
                    finalDegreeWorkScheduleingForm.set("startOfProposalPeriodHour", dateFormatHour
                            .format(infoScheduleing.getStartOfProposalPeriod()));
                }
                if (infoScheduleing.getEndOfProposalPeriod() != null) {
                    finalDegreeWorkScheduleingForm.set("endOfProposalPeriodDate", dateFormatDate
                            .format(infoScheduleing.getEndOfProposalPeriod()));
                    finalDegreeWorkScheduleingForm.set("endOfProposalPeriodHour", dateFormatHour
                            .format(infoScheduleing.getEndOfProposalPeriod()));
                }

                List attributedByTeacherList = new ArrayList();
                List attributionsList = new ArrayList();
                if (finalDegreeWorkProposalHeaders != null) {
                    for (int i = 0; i < finalDegreeWorkProposalHeaders.size(); i++) {
                        FinalDegreeWorkProposalHeader finalDegreeWorkProposalHeader = (FinalDegreeWorkProposalHeader) finalDegreeWorkProposalHeaders
                                .get(i);
                        InfoGroup infoGroup = finalDegreeWorkProposalHeader
                                .getGroupAttributedByTeacher();
                        if (infoGroup != null) {
                            attributedByTeacherList.add(finalDegreeWorkProposalHeader.getIdInternal()
                                    .toString()
                                    + infoGroup.getIdInternal().toString());
                        }
                        infoGroup = finalDegreeWorkProposalHeader.getGroupAttributed();
                        if (infoGroup != null) {
                            attributionsList.add(finalDegreeWorkProposalHeader.getIdInternal()
                                    .toString()
                                    + infoGroup.getIdInternal().toString());
                        }
                    }

                    String[] attributedByTeacher = new String[attributedByTeacherList.size()];
                    for (int i = 0; i < attributedByTeacherList.size(); i++) {
                        attributedByTeacher[i] = (String) attributedByTeacherList.get(i);
                    }
                    String[] attributions = new String[attributionsList.size()];
                    for (int i = 0; i < attributionsList.size(); i++) {
                        attributions[i] = (String) attributionsList.get(i);
                    }

                    finalDegreeWorkScheduleingForm.set("attributedByTeacher", attributedByTeacher);
                    finalDegreeWorkScheduleingForm.set("attributions", attributions);
                }

                ModuleConfig moduleConfig = mapping.getModuleConfig();

                FormBeanConfig fbc = moduleConfig.findFormBeanConfig("finalDegreeWorkCandidacyPeriod");
                DynaActionFormClass dafc = DynaActionFormClass.createDynaActionFormClass(fbc);
                DynaActionForm finalDegreeWorkCandidacyPeriodForm;
                try {
                    finalDegreeWorkCandidacyPeriodForm = (DynaActionForm) dafc.newInstance();
                } catch (Exception e1) {
                    throw new FenixActionException(e1);
                }
                if (infoScheduleing.getStartOfCandidacyPeriod() != null) {
                    finalDegreeWorkCandidacyPeriodForm.set("startOfCandidacyPeriodDate", dateFormatDate
                            .format(infoScheduleing.getStartOfCandidacyPeriod()));
                    finalDegreeWorkCandidacyPeriodForm.set("startOfCandidacyPeriodHour", dateFormatHour
                            .format(infoScheduleing.getStartOfCandidacyPeriod()));
                }
                if (infoScheduleing.getEndOfCandidacyPeriod() != null) {
                    finalDegreeWorkCandidacyPeriodForm.set("endOfCandidacyPeriodDate", dateFormatDate
                            .format(infoScheduleing.getEndOfCandidacyPeriod()));
                    finalDegreeWorkCandidacyPeriodForm.set("endOfCandidacyPeriodHour", dateFormatHour
                            .format(infoScheduleing.getEndOfCandidacyPeriod()));
                }
                request.setAttribute("finalDegreeWorkCandidacyPeriod",
                        finalDegreeWorkCandidacyPeriodForm);

                FormBeanConfig fbc2 = moduleConfig
                        .findFormBeanConfig("finalDegreeWorkCandidacyRequirements");
                DynaActionFormClass dafc2 = DynaActionFormClass.createDynaActionFormClass(fbc2);
                DynaActionForm finalDegreeWorkCandidacyRequirementsForm;
                try {
                    finalDegreeWorkCandidacyRequirementsForm = (DynaActionForm) dafc2.newInstance();
                } catch (Exception e1) {
                    throw new FenixActionException(e1);
                }
                if (infoScheduleing.getMinimumNumberOfCompletedCourses() != null) {
                    finalDegreeWorkCandidacyRequirementsForm.set("minimumNumberOfCompletedCourses",
                            infoScheduleing.getMinimumNumberOfCompletedCourses().toString());
                }
                if (infoScheduleing.getMinimumNumberOfStudents() != null) {
                    finalDegreeWorkCandidacyRequirementsForm.set("minimumNumberOfStudents",
                            infoScheduleing.getMinimumNumberOfStudents().toString());
                }
                if (infoScheduleing.getMaximumNumberOfStudents() != null) {
                    finalDegreeWorkCandidacyRequirementsForm.set("maximumNumberOfStudents",
                            infoScheduleing.getMaximumNumberOfStudents().toString());
                }
                if (infoScheduleing.getMaximumNumberOfProposalCandidaciesPerGroup() != null) {
                    finalDegreeWorkCandidacyRequirementsForm.set(
                            "maximumNumberOfProposalCandidaciesPerGroup", infoScheduleing
                                    .getMaximumNumberOfProposalCandidaciesPerGroup().toString());
                }

                request.setAttribute("finalDegreeWorkCandidacyRequirements",
                        finalDegreeWorkCandidacyRequirementsForm);
            }
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        return mapping.findForward("show-final-degree-work-list");
    }

    public ActionForward viewFinalDegreeWorkProposal(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {

        final String finalDegreeWorkProposalOIDString = request
                .getParameter("finalDegreeWorkProposalOID");
        Integer degreeCurricularPlanID = null;
        if (request.getParameter("degreeCurricularPlanID") != null) {
            degreeCurricularPlanID = new Integer(request.getParameter("degreeCurricularPlanID"));
            request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);
        }

        DynaActionForm dynaActionForm = (DynaActionForm) form;
        Integer executionDegreeOID = Integer.valueOf((String) dynaActionForm.get("executionDegreeOID"));
        request.setAttribute("executionDegreeOID", executionDegreeOID);

        if (finalDegreeWorkProposalOIDString != null
                && StringUtils.isNumeric(finalDegreeWorkProposalOIDString)) {
            IUserView userView = SessionUtils.getUserView(request);

            Object args[] = { new Integer(finalDegreeWorkProposalOIDString) };
            try {
                InfoProposal infoProposal = (InfoProposal) ServiceUtils.executeService(userView,
                        "ReadFinalDegreeWorkProposal", args);

                if (infoProposal != null) {
                    DynaActionForm finalWorkForm = (DynaActionForm) form;

                    if (infoProposal.getIdInternal() != null) {
                        finalWorkForm.set("idInternal", infoProposal.getIdInternal().toString());
                    }
                    finalWorkForm.set("title", infoProposal.getTitle());
                    if (infoProposal.getOrientatorsCreditsPercentage() != null) {
                        finalWorkForm.set("responsibleCreditsPercentage", infoProposal
                                .getOrientatorsCreditsPercentage().toString());
                    }
                    if (infoProposal.getCoorientatorsCreditsPercentage() != null) {
                        finalWorkForm.set("coResponsibleCreditsPercentage", infoProposal
                                .getCoorientatorsCreditsPercentage().toString());
                    }
                    finalWorkForm.set("companionName", infoProposal.getCompanionName());
                    finalWorkForm.set("companionMail", infoProposal.getCompanionMail());
                    if (infoProposal.getCompanionPhone() != null) {
                        finalWorkForm.set("companionPhone", infoProposal.getCompanionPhone().toString());
                    }
                    finalWorkForm.set("framing", infoProposal.getFraming());
                    finalWorkForm.set("objectives", infoProposal.getObjectives());
                    finalWorkForm.set("description", infoProposal.getDescription());
                    finalWorkForm.set("requirements", infoProposal.getRequirements());
                    finalWorkForm.set("deliverable", infoProposal.getDeliverable());
                    finalWorkForm.set("url", infoProposal.getUrl());
                    if (infoProposal.getMaximumNumberOfGroupElements() != null) {
                        finalWorkForm.set("maximumNumberOfGroupElements", infoProposal
                                .getMaximumNumberOfGroupElements().toString());
                    }
                    if (infoProposal.getMinimumNumberOfGroupElements() != null) {
                        finalWorkForm.set("minimumNumberOfGroupElements", infoProposal
                                .getMinimumNumberOfGroupElements().toString());
                    }
                    if (infoProposal.getDegreeType() != null) {
                        finalWorkForm.set("degreeType", infoProposal.getDegreeType().toString());
                    }
                    finalWorkForm.set("observations", infoProposal.getObservations());
                    finalWorkForm.set("location", infoProposal.getLocation());

                    finalWorkForm.set("companyAdress", infoProposal.getCompanyAdress());
                    finalWorkForm.set("companyName", infoProposal.getCompanyName());
                    if (infoProposal.getOrientator() != null
                            && infoProposal.getOrientator().getIdInternal() != null) {
                        finalWorkForm.set("orientatorOID", infoProposal.getOrientator().getIdInternal()
                                .toString());
                        finalWorkForm.set("responsableTeacherNumber", infoProposal.getOrientator()
                                .getTeacherNumber().toString());
                        finalWorkForm.set("responsableTeacherName", infoProposal.getOrientator()
                                .getInfoPerson().getNome());
                    }
                    if (infoProposal.getCoorientator() != null
                            && infoProposal.getCoorientator().getIdInternal() != null) {
                        finalWorkForm.set("coorientatorOID", infoProposal.getCoorientator()
                                .getIdInternal().toString());
                        finalWorkForm.set("coResponsableTeacherNumber", infoProposal.getCoorientator()
                                .getTeacherNumber().toString());
                        finalWorkForm.set("coResponsableTeacherName", infoProposal.getCoorientator()
                                .getInfoPerson().getNome());
                    }
                    if (infoProposal.getExecutionDegree() != null
                            && infoProposal.getExecutionDegree().getIdInternal() != null) {
                        finalWorkForm.set("degree", infoProposal.getExecutionDegree().getIdInternal()
                                .toString());
                    }
                    if (infoProposal.getStatus() != null && infoProposal.getStatus().getStatus() != null) {
                        finalWorkForm.set("status", infoProposal.getStatus().getStatus().toString());
                    }

                    if (infoProposal.getBranches() != null && infoProposal.getBranches().size() > 0) {
                        String[] branchList = new String[infoProposal.getBranches().size()];
                        for (int i = 0; i < infoProposal.getBranches().size(); i++) {
                            InfoBranch infoBranch = ((InfoBranch) infoProposal.getBranches().get(i));
                            if (infoBranch != null && infoBranch.getIdInternal() != null) {
                                String brachOIDString = infoBranch.getIdInternal().toString();
                                if (brachOIDString != null && StringUtils.isNumeric(brachOIDString)) {
                                    branchList[i] = brachOIDString;
                                }
                            }
                        }
                        finalWorkForm.set("branchList", branchList);
                    }

                    final ExecutionDegree executionDegree = (ExecutionDegree) readDomainObject(request, ExecutionDegree.class, executionDegreeOID);
                    final Scheduleing scheduleing = executionDegree.getScheduling();
                    final List branches = new ArrayList();
                    for (final ExecutionDegree ed : scheduleing.getExecutionDegrees()) {
                    	final DegreeCurricularPlan degreeCurricularPlan = ed.getDegreeCurricularPlan();
                    	branches.addAll(CommonServiceRequests.getBranchesByDegreeCurricularPlan(userView, degreeCurricularPlan.getIdInternal()));
                    }
//                    InfoExecutionDegree infoExecutionDegree = CommonServiceRequests
//                            .getInfoExecutionDegree(userView, infoProposal.getExecutionDegree()
//                                    .getIdInternal());
//                    List branches = CommonServiceRequests.getBranchesByDegreeCurricularPlan(userView,
//                            infoExecutionDegree.getInfoDegreeCurricularPlan().getIdInternal());
                    request.setAttribute("branches", branches);

                    request.setAttribute("finalDegreeWorkProposalStatusList",
                            FinalDegreeWorkProposalStatus.getLabelValueList());
                }
            } catch (FenixServiceException e) {
                throw new FenixActionException(e);
            }
        }

        return mapping.findForward("show-final-degree-work-proposal");
    }

    public ActionForward createNewFinalDegreeWorkProposal(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException, FenixServiceException {
        IUserView userView = SessionUtils.getUserView(request);

        Integer degreeCurricularPlanID = null;
        if (request.getParameter("degreeCurricularPlanID") != null) {
            degreeCurricularPlanID = new Integer(request.getParameter("degreeCurricularPlanID"));
            request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);
        }

        DynaActionForm dynaActionForm = (DynaActionForm) form;
        Integer executionDegreeOID = Integer.valueOf((String) dynaActionForm.get("executionDegreeOID"));
        request.setAttribute("executionDegreeOID", executionDegreeOID);

        DynaActionForm finalWorkForm = (DynaActionForm) form;
        finalWorkForm.set("degree", executionDegreeOID.toString());

        final ExecutionDegree executionDegree = (ExecutionDegree) readDomainObject(request, ExecutionDegree.class, executionDegreeOID);
        final Scheduleing scheduleing = executionDegree.getScheduling();
        final List branches = new ArrayList();
        for (final ExecutionDegree ed : scheduleing.getExecutionDegrees()) {
            final DegreeCurricularPlan degreeCurricularPlan = ed.getDegreeCurricularPlan();
            branches.addAll(CommonServiceRequests.getBranchesByDegreeCurricularPlan(userView, degreeCurricularPlan.getIdInternal()));
        }
//        List branches = CommonServiceRequests.getBranchesByDegreeCurricularPlan(userView,
//                infoExecutionDegree.getInfoDegreeCurricularPlan().getIdInternal());
        request.setAttribute("branches", branches);

        request.setAttribute("finalDegreeWorkProposalStatusList", FinalDegreeWorkProposalStatus
                .getLabelValueList());

        return mapping.findForward("show-final-degree-work-proposal");
    }

    public ActionForward setFinalDegreeProposalPeriod(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException, FenixServiceException {

        DynaActionForm finalDegreeWorkScheduleingForm = (DynaActionForm) form;

        String executionDegreeOIDString = finalDegreeWorkScheduleingForm.getString("executionDegreeOID");
        Integer executionDegreeOID = Integer.valueOf(executionDegreeOIDString);
        String startOfProposalPeriodDateString = (String) finalDegreeWorkScheduleingForm
                .get("startOfProposalPeriodDate");
        String startOfProposalPeriodHourString = (String) finalDegreeWorkScheduleingForm
                .get("startOfProposalPeriodHour");
        String startOfProposalPeriodString = startOfProposalPeriodDateString + " "
                + startOfProposalPeriodHourString;
        String endOfProposalPeriodDateString = (String) finalDegreeWorkScheduleingForm
                .get("endOfProposalPeriodDate");
        String endOfProposalPeriodHourString = (String) finalDegreeWorkScheduleingForm
                .get("endOfProposalPeriodHour");
        String endOfProposalPeriodString = endOfProposalPeriodDateString + " "
                + endOfProposalPeriodHourString;

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        Date startOfProposalPeriod = null;
        Date endOfProposalPeriod = null;
        try {

            startOfProposalPeriod = dateFormat.parse(startOfProposalPeriodString);
        } catch (ParseException e) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("finalDegreeWorkProposal.setProposalPeriod.validator.start",
                    new ActionError("finalDegreeWorkProposal.setProposalPeriod.validator.start"));
            saveErrors(request, actionErrors);
            return mapping.getInputForward();
        }

        try {
            endOfProposalPeriod = dateFormat.parse(endOfProposalPeriodString);
        } catch (ParseException e) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("finalWorkInformationForm.numberOfGroupElements", new ActionError(
                    "finalWorkInformationForm.numberOfGroupElements"));
            saveErrors(request, actionErrors);
            return mapping.getInputForward();
        }

        Object args[] = { executionDegreeOID, startOfProposalPeriod, endOfProposalPeriod };
        try {
            executeService(request, "DefineFinalDegreeWorkProposalSubmisionPeriod", args);
        } catch (NotAuthorizedFilterException e) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("error", new ActionError("error.exception.notAuthorized2"));
            saveErrors(request, actionErrors);
            return mapping.getInputForward();
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        request.setAttribute("sucessfulSetOfDegreeProposalPeriod", "sucessfulSetOfDegreeProposalPeriod");
        return prepare(mapping, form, request, response);
    }

    public ActionForward setFinalDegreeCandidacyPeriod(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {

        DynaActionForm finalDegreeWorkScheduleingForm = (DynaActionForm) form;

        String executionDegreeOIDString = finalDegreeWorkScheduleingForm.getString("executionDegreeOID");
        Integer executionDegreeOID = Integer.valueOf(executionDegreeOIDString);
        String startOfCandidacyPeriodDateString = (String) finalDegreeWorkScheduleingForm
                .get("startOfCandidacyPeriodDate");
        String startOfCandidacyPeriodHourString = (String) finalDegreeWorkScheduleingForm
                .get("startOfCandidacyPeriodHour");
        String startOfCandidacyPeriodString = startOfCandidacyPeriodDateString + " "
                + startOfCandidacyPeriodHourString;
        String endOfCandidacyPeriodDateString = (String) finalDegreeWorkScheduleingForm
                .get("endOfCandidacyPeriodDate");
        String endOfCandidacyPeriodHourString = (String) finalDegreeWorkScheduleingForm
                .get("endOfCandidacyPeriodHour");
        String endOfCandidacyPeriodString = endOfCandidacyPeriodDateString + " "
                + endOfCandidacyPeriodHourString;

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        Date startOfCandidacyPeriod = null;
        Date endOfCandidacyPeriod = null;
        try {

            startOfCandidacyPeriod = dateFormat.parse(startOfCandidacyPeriodString);
        } catch (ParseException e) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("finalDegreeWorkCandidacy.setCandidacyPeriod.validator.start",
                    new ActionError("finalDegreeWorkCandidacy.setCandidacyPeriod.validator.start"));
            saveErrors(request, actionErrors);
            return mapping.getInputForward();
        }

        try {
            endOfCandidacyPeriod = dateFormat.parse(endOfCandidacyPeriodString);
        } catch (ParseException e) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("finalWorkInformationForm.numberOfGroupElements", new ActionError(
                    "finalWorkInformationForm.numberOfGroupElements"));
            saveErrors(request, actionErrors);
            return mapping.getInputForward();
        }

        Object args[] = { executionDegreeOID, startOfCandidacyPeriod,
                endOfCandidacyPeriod };
        try {
            executeService(request, "DefineFinalDegreeWorkCandidacySubmisionPeriod", args);
        } catch (NotAuthorizedFilterException e) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("error", new ActionError("error.exception.notAuthorized2"));
            saveErrors(request, actionErrors);
            return mapping.getInputForward();
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        request
                .setAttribute("sucessfulSetOfDegreeCandidacyPeriod",
                        "sucessfulSetOfDegreeProposalPeriod");
        return mapping.findForward("Sucess");
    }

    public ActionForward submit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException,
            FenixServiceException {
        DynaActionForm finalWorkForm = (DynaActionForm) form;

        IUserView userView = SessionUtils.getUserView(request);

        Integer degreeCurricularPlanID = (Integer) finalWorkForm.get("degreeCurricularPlanID");
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);
        
        DynaActionForm dynaActionForm = (DynaActionForm) form;
        Integer executionDegreeOID = Integer.valueOf((String) dynaActionForm.get("executionDegreeOID"));
        request.setAttribute("executionDegreeOID", executionDegreeOID);

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
        String status = (String) finalWorkForm.get("status");

        Integer min = new Integer(minimumNumberOfGroupElements);
        Integer max = new Integer(maximumNumberOfGroupElements);
        if ((min.intValue() > max.intValue()) || (min.intValue() <= 0)) {
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
                || (orientatorCreditsPercentage.intValue() + coorientatorCreditsPercentage.intValue() != 100)) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("finalWorkInformationForm.invalidCreditsPercentageDistribuition",
                    new ActionError("finalWorkInformationForm.invalidCreditsPercentageDistribuition"));
            saveErrors(request, actionErrors);
            return mapping.getInputForward();
        }

        InfoProposal infoFinalWorkProposal = new InfoProposal();
        if (idInternal != null && !idInternal.equals("") && StringUtils.isNumeric(idInternal)) {
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
        final DegreeType dt = (degreeType != null && degreeType.length() > 0) ? DegreeType.valueOf(degreeType) : null;
        infoFinalWorkProposal.setDegreeType(dt);

        infoFinalWorkProposal.setOrientator(new InfoTeacher());
        infoFinalWorkProposal.getOrientator().setIdInternal(new Integer(orientatorOID));
        if (coorientatorOID != null && !coorientatorOID.equals("")) {
            infoFinalWorkProposal.setCoorientator(new InfoTeacher());
            infoFinalWorkProposal.getCoorientator().setIdInternal(new Integer(coorientatorOID));
        } else {
            if (coorientatorCreditsPercentage.intValue() != 0) {
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
                    && StringUtils.isNumeric(companionPhone)) {
                infoFinalWorkProposal.setCompanionPhone(new Integer(companionPhone));
            }
            infoFinalWorkProposal.setCompanyAdress(companyAdress);
            infoFinalWorkProposal.setCompanyName(companyName);
        }
        infoFinalWorkProposal.setExecutionDegree(new InfoExecutionDegree());
        infoFinalWorkProposal.getExecutionDegree().setIdInternal(new Integer(degree));

        if (branchList != null && branchList.length > 0) {
            infoFinalWorkProposal.setBranches(new ArrayList());
            for (int i = 0; i < branchList.length; i++) {
                String brachOIDString = branchList[i];
                if (brachOIDString != null && StringUtils.isNumeric(brachOIDString)) {
                    InfoBranch infoBranch = new InfoBranch();
                    infoBranch.setIdInternal(new Integer(brachOIDString));
                    infoFinalWorkProposal.getBranches().add(infoBranch);
                }
            }
        }

        if (status != null && !status.equals("") && StringUtils.isNumeric(status)) {
            infoFinalWorkProposal.setStatus(new FinalDegreeWorkProposalStatus(new Integer(status)));
        }

        try {
            Object argsProposal[] = { infoFinalWorkProposal };
            ServiceUtils.executeService(userView, "SubmitFinalWorkProposal", argsProposal);
        } catch (Exception e) {
            if (e instanceof OutOfPeriodException) {
                ActionErrors actionErrors = new ActionErrors();
                actionErrors.add("finalWorkInformationForm.scheduling.invalidInterval", new ActionError(
                        "finalWorkInformationForm.scheduling.invalidInterval"));
                saveErrors(request, actionErrors);
                return mapping.getInputForward();
            }
            throw new FenixActionException(e);
        }

        return mapping.findForward("show-final-degree-work-list");
    }

    public ActionForward showTeacherName(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException, FenixServiceException {
        IUserView userView = SessionUtils.getUserView(request);

        DynaActionForm finalWorkForm = (DynaActionForm) form;
        String alteredField = (String) finalWorkForm.get("alteredField");
        String number = null;

        if (alteredField.equals("orientator")) {
            number = (String) finalWorkForm.get("responsableTeacherNumber");
        } else if (alteredField.equals("coorientator")) {
            number = (String) finalWorkForm.get("coResponsableTeacherNumber");
        }

        if (number == null || number.equals("")) {
            if (alteredField.equals("orientator")) {
                finalWorkForm.set("orientatorOID", "");
                finalWorkForm.set("responsableTeacherName", "");
            } else if (alteredField.equals("coorientator")) {
                finalWorkForm.set("coorientatorOID", "");
                finalWorkForm.set("coResponsableTeacherName", "");
            }

            return prepareFinalWorkInformation(mapping, form, request, response);
        }

        Object[] args = { new Integer(number) };
        InfoTeacher infoTeacher;
        try {
            infoTeacher = (InfoTeacher) ServiceUtils.executeService(userView, "ReadTeacherByNumber",
                    args);
            if (infoTeacher == null) {
                ActionErrors actionErrors = new ActionErrors();
                actionErrors.add("finalWorkInformationForm.unexistingTeacher", new ActionError(
                        "finalWorkInformationForm.unexistingTeacher"));
                saveErrors(request, actionErrors);
                return mapping.getInputForward();
            }
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        if (alteredField.equals("orientator")) {
            finalWorkForm.set("orientatorOID", infoTeacher.getIdInternal().toString());
            finalWorkForm.set("responsableTeacherName", infoTeacher.getIdInternal().toString());
            request.setAttribute("orientator", infoTeacher);
        } else {
            if (alteredField.equals("coorientator")) {
                finalWorkForm.set("coorientatorOID", infoTeacher.getIdInternal().toString());
                finalWorkForm.set("coResponsableTeacherName", infoTeacher.getIdInternal().toString());
                finalWorkForm.set("companionName", "");
                finalWorkForm.set("companionMail", "");
                finalWorkForm.set("companionPhone", "");
                finalWorkForm.set("companyAdress", "");
                finalWorkForm.set("companyName", "");
                finalWorkForm.set("alteredField", "");
                request.setAttribute("coorientator", infoTeacher);
            }
        }

        return createNewFinalDegreeWorkProposal(mapping, form, request, response);
    }

    public ActionForward prepareFinalWorkInformation(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException, FenixServiceException {
        IUserView userView = SessionUtils.getUserView(request);

        DynaActionForm finalWorkForm = (DynaActionForm) form;
        String degreeId = (String) finalWorkForm.get("degree");
        finalWorkForm.set("degreeType", DegreeType.DEGREE.toString());

        Integer degreeCurricularPlanID = new Integer(Integer.parseInt(request
                .getParameter("degreeCurricularPlanID")));
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);

        InfoExecutionDegree infoExecutionDegree = CommonServiceRequests.getInfoExecutionDegree(userView,
                new Integer(degreeId));

        final ExecutionDegree executionDegree = (ExecutionDegree) readDomainObject(request, ExecutionDegree.class, infoExecutionDegree.getIdInternal());
        final Scheduleing scheduleing = executionDegree.getScheduling();
        final List branches = new ArrayList();
        for (final ExecutionDegree ed : scheduleing.getExecutionDegrees()) {
            final DegreeCurricularPlan degreeCurricularPlan = ed.getDegreeCurricularPlan();
            branches.addAll(CommonServiceRequests.getBranchesByDegreeCurricularPlan(userView, degreeCurricularPlan.getIdInternal()));
        }
//        List branches = CommonServiceRequests.getBranchesByDegreeCurricularPlan(userView,
//                infoExecutionDegree.getInfoDegreeCurricularPlan().getIdInternal());
        request.setAttribute("branches", branches);

        request.setAttribute("finalDegreeWorkProposalStatusList", FinalDegreeWorkProposalStatus
                .getLabelValueList());

        return mapping.findForward("show-final-degree-work-proposal");
    }

    public ActionForward coorientatorVisibility(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException, FenixServiceException {
        DynaActionForm finalWorkForm = (DynaActionForm) form;
        String alteredField = (String) finalWorkForm.get("alteredField");
        String companionName = (String) finalWorkForm.get("companionName");
        String companionMail = (String) finalWorkForm.get("companionMail");
        String companionPhone = (String) finalWorkForm.get("companionPhone");
        String companyAdress = (String) finalWorkForm.get("companyAdress");
        String companyName = (String) finalWorkForm.get("companyName");

        if (alteredField.equals("companion") && companionName.equals("") && companionMail.equals("")
                && companionPhone.equals("") && companyAdress.equals("") && companyName.equals("")) {
            finalWorkForm.set("coorientatorOID", "");
            finalWorkForm.set("coResponsableTeacherName", "");
            finalWorkForm.set("alteredField", "");
        } else {
            if (alteredField.equals("companion") || !companionName.equals("")
                    || !companionMail.equals("") || !companionPhone.equals("")
                    || !companyAdress.equals("") || !companyName.equals("")) {
                finalWorkForm.set("alteredField", "companion");
            }

        }

        return prepareFinalWorkInformation(mapping, form, request, response);
    }

    public ActionForward publishAprovedProposals(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException, FenixServiceException {
        IUserView userView = SessionUtils.getUserView(request);

        DynaActionForm dynaActionForm = (DynaActionForm) form;
        Integer executionDegreeOID = Integer.valueOf((String) dynaActionForm.get("executionDegreeOID"));
        request.setAttribute("executionDegreeOID", executionDegreeOID);

        Object args[] = { executionDegreeOID };
        try {
            ServiceUtils.executeService(userView, "PublishAprovedFinalDegreeWorkProposals", args);
        } catch (NotAuthorizedFilterException e) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("error", new ActionError("error.exception.notAuthorized2"));
            saveErrors(request, actionErrors);
            return mapping.getInputForward();  
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        return prepare(mapping, form, request, response);
    }

    public ActionForward aproveSelectedProposalsStatus(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException, FenixServiceException {
        return changeSelectedProposalsStatus(mapping, form, request, response,
                FinalDegreeWorkProposalStatus.APPROVED_STATUS);
    }

    public ActionForward publishSelectedProposals(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException, FenixServiceException {
        return changeSelectedProposalsStatus(mapping, form, request, response,
                FinalDegreeWorkProposalStatus.PUBLISHED_STATUS);
    }

    public ActionForward changeSelectedProposalsStatus(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response,
            FinalDegreeWorkProposalStatus status) throws FenixActionException, FenixFilterException, FenixServiceException {
        IUserView userView = SessionUtils.getUserView(request);

        DynaActionForm finalWorkForm = (DynaActionForm) form;
        String[] selectedProposals = (String[]) finalWorkForm.get("selectedProposals");
        String executionDegreeOIDString = finalWorkForm.getString("executionDegreeOID");
        Integer executionDegreeOID = Integer.valueOf(executionDegreeOIDString);

        if (selectedProposals != null && selectedProposals.length > 0) {
            List selectedProposalOIDs = new ArrayList();
            for (int i = 0; i < selectedProposals.length; i++) {
                if (selectedProposals[i] != null && StringUtils.isNumeric(selectedProposals[i])) {
                    selectedProposalOIDs.add(new Integer(selectedProposals[i]));
                }
            }

            Object args[] = { executionDegreeOID, selectedProposalOIDs, status };
            try {
                ServiceUtils.executeService(userView, "ChangeStatusOfFinalDegreeWorkProposals", args);
            } catch (FenixServiceException e) {
                throw new FenixActionException(e);
            }
        }

        return prepare(mapping, form, request, response);
    }

    public ActionForward setFinalDegreeCandidacyRequirements(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
            FenixFilterException {

        DynaActionForm finalDegreeWorkScheduleingForm = (DynaActionForm) form;

        String executionDegreeOIDString = finalDegreeWorkScheduleingForm.getString("executionDegreeOID");
        Integer executionDegreeOID = Integer.valueOf(executionDegreeOIDString);

        String minimumNumberOfCompletedCoursesString = (String) finalDegreeWorkScheduleingForm
                .get("minimumNumberOfCompletedCourses");
        String minimumNumberOfStudentsString = (String) finalDegreeWorkScheduleingForm
                .get("minimumNumberOfStudents");
        String maximumNumberOfStudentsString = (String) finalDegreeWorkScheduleingForm
                .get("maximumNumberOfStudents");
        String maximumNumberOfProposalCandidaciesPerGroupString = (String) finalDegreeWorkScheduleingForm
                .get("maximumNumberOfProposalCandidaciesPerGroup");

        Integer minimumNumberOfCompletedCourses = null;
        if (minimumNumberOfCompletedCoursesString != null
                && !minimumNumberOfCompletedCoursesString.equals("")) {
            minimumNumberOfCompletedCourses = new Integer(minimumNumberOfCompletedCoursesString);
        }

        Integer minimumNumberOfStudents = null;
        if (minimumNumberOfStudentsString != null && !minimumNumberOfStudentsString.equals("")) {
            minimumNumberOfStudents = new Integer(minimumNumberOfStudentsString);
        }

        Integer maximumNumberOfStudents = null;
        if (maximumNumberOfStudentsString != null && !maximumNumberOfStudentsString.equals("")) {
            maximumNumberOfStudents = new Integer(maximumNumberOfStudentsString);
        }

        Integer maximumNumberOfProposalCandidaciesPerGroup = null;
        if (maximumNumberOfProposalCandidaciesPerGroupString != null
                && !maximumNumberOfProposalCandidaciesPerGroupString.equals("")) {
            maximumNumberOfProposalCandidaciesPerGroup = new Integer(
                    maximumNumberOfProposalCandidaciesPerGroupString);
        }

        IUserView userView = SessionUtils.getUserView(request);

        try {
            Object args[] = { executionDegreeOID, minimumNumberOfCompletedCourses,
                    minimumNumberOfStudents, maximumNumberOfStudents,
                    maximumNumberOfProposalCandidaciesPerGroup };
            ServiceUtils.executeService(userView, "DefineFinalDegreeWorkCandidacyRequirements", args);
        } catch (NotAuthorizedFilterException e) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("error", new ActionMessage("error.exception.notAuthorized2"));
            saveErrors(request, actionErrors);
            return mapping.getInputForward();
        }

        ActionMessages messages = new ActionMessages();
        ActionMessage msg = new ActionMessage("finalDegreeWorkCandidacy.setRequirements.sucess");
        messages.add("message1", msg);
        saveMessages(request, messages);

        return mapping.findForward("Sucess");
    }

    public ActionForward attributeGroupProposal(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
            FenixFilterException {
        DynaActionForm finalDegreeWorkScheduleingForm = (DynaActionForm) form;

        String selectedGroupProposal = (String) finalDegreeWorkScheduleingForm
                .get("selectedGroupProposal");

        if (selectedGroupProposal != null && !selectedGroupProposal.equals("")
                && StringUtils.isNumeric(selectedGroupProposal)) {
            IUserView userView = SessionUtils.getUserView(request);

            Object args[] = { new Integer(selectedGroupProposal) };
            ServiceUtils.executeService(userView, "AttributeFinalDegreeWork", args);
        }

        return mapping.findForward("prepare-show-final-degree-work-proposal");
    }

    public ActionForward getStudentCP(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        String studentNumber = request.getParameter("studentNumber");

        request.setAttribute("studentNumber", studentNumber);

        return mapping.findForward("show-student-curricular-plan");
    }

    public ActionForward addExecutionDegree(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    		throws FenixActionException, FenixFilterException, FenixServiceException {
    	final DynaActionForm dynaActionForm = (DynaActionForm) form;
    	final String executionDegreeOIDString = dynaActionForm.getString("executionDegreeOID");
    	final ExecutionDegree executionDegree = (ExecutionDegree)
    			readDomainObject(request, ExecutionDegree.class, Integer.valueOf(executionDegreeOIDString));
    	final String otherExecutionDegreeIDString = dynaActionForm.getString("otherExecutionDegreeID");
    	final ExecutionDegree otherExecutionDegree = (ExecutionDegree)
    			readDomainObject(request, ExecutionDegree.class, Integer.valueOf(otherExecutionDegreeIDString));

    	final Object[] args = { executionDegree.getScheduling(), otherExecutionDegree };
    	executeService(request, "AddExecutionDegreeToScheduling", args );

    	dynaActionForm.set("otherExecutionDegreeID", null);

        return prepare(mapping, form, request, response);
    }

    public ActionForward removeExecutionDegree(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws FenixActionException, FenixFilterException, FenixServiceException {
    	final DynaActionForm dynaActionForm = (DynaActionForm) form;
    	final String executionDegreeOIDString = dynaActionForm.getString("executionDegreeOID");
    	final ExecutionDegree executionDegree = (ExecutionDegree)
    			readDomainObject(request, ExecutionDegree.class, Integer.valueOf(executionDegreeOIDString));
    	final String otherExecutionDegreeIDString = dynaActionForm.getString("otherExecutionDegreeID");
    	final ExecutionDegree otherExecutionDegree = (ExecutionDegree)
				readDomainObject(request, ExecutionDegree.class, Integer.valueOf(otherExecutionDegreeIDString));

    	final Object[] args = { executionDegree.getScheduling(), otherExecutionDegree };
    			executeService(request, "RemoveExecutionDegreeToScheduling", args );

    	dynaActionForm.set("otherExecutionDegreeID", null);

    	return prepare(mapping, form, request, response);
    }

    public ActionForward proposalsXLS(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    		throws FenixActionException, FenixFilterException, FenixServiceException {

    	final String executionDegreeOIDString = request.getParameter("executionDegreeOID");
    	final ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(Integer.valueOf(executionDegreeOIDString));
    	final ExecutionYear executionYear = executionDegree.getExecutionYear();
    	final String yearString = executionYear.getNextYearsYearString();

        try {
            response.setContentType("text/plain");
            response.setHeader("Content-disposition", "attachment; filename=proposals_" + yearString + ".xls");

            ServletOutputStream writer = response.getOutputStream();
            final Spreadsheet spreadsheet = new Spreadsheet("Proposals " + yearString);
            setHeaders(spreadsheet);
            fillSpreadSheet(executionDegree, spreadsheet);
            spreadsheet.exportToXLSSheet(writer);

            writer.flush();
            response.flushBuffer();
        } catch (IOException e) {
            throw new FenixServiceException();
        }
        return null;
    }

	private void setHeaders(final Spreadsheet spreadsheet) {
		spreadsheet.setHeader("N�mero");
		spreadsheet.setHeader("Estado da Proposta");
		spreadsheet.setHeader("T�tilo");
		spreadsheet.setHeader("N�mero Orientador");
		spreadsheet.setHeader("Nome Orientador");
		spreadsheet.setHeader("N�mero Coorientador");
		spreadsheet.setHeader("Nome Coorientador");
		spreadsheet.setHeader("Percentagem Cr�ditos Orientador");
		spreadsheet.setHeader("Percentagem Cr�ditos Coorientador");
		spreadsheet.setHeader("Enquadramento");
		spreadsheet.setHeader("Objectivos");
		spreadsheet.setHeader("Descri��o");
		spreadsheet.setHeader("Requisitos");
		spreadsheet.setHeader("Resultado esperado");
		spreadsheet.setHeader("URL");
		spreadsheet.setHeader("�rea de Especializa��o");
		spreadsheet.setHeader("N�mero m�nimo de elementos do grupo");
		spreadsheet.setHeader("N�mero m�ximo de elementos do grupo");
		spreadsheet.setHeader("Adequa��o a Disserta��o");
		spreadsheet.setHeader("Observa��es");
		spreadsheet.setHeader("Localiza��o da realiza��o do TFC");
	}

	private static final MessageResources applicationResources = MessageResources.getMessageResources("resources/ApplicationResources");
	private static final MessageResources enumerationResources = MessageResources.getMessageResources("resources/EnumerationResources");

	private void fillSpreadSheet(final ExecutionDegree executionDegree, final Spreadsheet spreadsheet) {
		int maxNumberStudentsPerGroup = 0;

		final Scheduleing scheduleing = executionDegree.getScheduling();
    	final SortedSet<Proposal> proposals = new TreeSet<Proposal>(new BeanComparator("proposalNumber"));
    	proposals.addAll(scheduleing.getProposalsSet());
		for (final Proposal proposal : proposals) {
			final Row row = spreadsheet.addRow();
			row.setCell(proposal.getProposalNumber().toString());
			if (proposal.getGroupAttributed() != null) {
				row.setCell("Atribuido");
			} else if (proposal.getStatus() != null) {
				row.setCell(proposal.getStatus().getKey());
			} else {
				row.setCell("");
			}
			row.setCell(proposal.getTitle());
			row.setCell(proposal.getOrientator().getTeacherNumber().toString());
			row.setCell(proposal.getOrientator().getPerson().getName());
			if (proposal.getCoorientator() != null) {
				row.setCell(proposal.getCoorientator().getTeacherNumber().toString());
				row.setCell(proposal.getCoorientator().getPerson().getName());
			} else {
				row.setCell("");
				row.setCell("");				
			}
			row.setCell(proposal.getOrientatorsCreditsPercentage().toString());
			row.setCell(proposal.getCoorientatorsCreditsPercentage().toString());
			row.setCell(proposal.getFraming());
			row.setCell(proposal.getObjectives());
			row.setCell(proposal.getDescription());
			row.setCell(proposal.getRequirements());
			row.setCell(proposal.getDeliverable());
			row.setCell(proposal.getUrl());
			final StringBuilder branches = new StringBuilder();
			boolean appendSeperator = false;
			for (final Branch branch : proposal.getBranchesSet()) {
				if (appendSeperator) {
					branches.append(", ");
				} else {
					appendSeperator = true;
				}
				branches.append(branch.getName());
			}
			row.setCell(branches.toString());
			row.setCell(proposal.getMinimumNumberOfGroupElements().toString());
			row.setCell(proposal.getMaximumNumberOfGroupElements().toString());
			if (proposal.getDegreeType() == null) {
				row.setCell(applicationResources.getMessage("label.both"));
			} else {
				row.setCell(enumerationResources.getMessage(proposal.getDegreeType().toString()));
			}
			row.setCell(proposal.getObservations());
			row.setCell(proposal.getLocation());
			if (proposal.getGroupAttributed() != null) {
				int i = 0;
				for (final GroupStudent groupStudent : proposal.getGroupAttributed().getGroupStudentsSet()) {
					final Student student = groupStudent.getStudent();
					row.setCell(student.getNumber().toString());
					row.setCell(student.getPerson().getName());
					maxNumberStudentsPerGroup = Math.max(maxNumberStudentsPerGroup, ++i);
				}
			} else if (proposal.getGroupAttributedByTeacher() != null) {
				int i = 0;
				for (final GroupStudent groupStudent : proposal.getGroupAttributedByTeacher().getGroupStudentsSet()) {
					final Student student = groupStudent.getStudent();
					row.setCell(student.getNumber().toString());
					row.setCell(student.getPerson().getName());
					maxNumberStudentsPerGroup = Math.max(maxNumberStudentsPerGroup, ++i);
				}				
			}
		}

		for (int i = 0; i < maxNumberStudentsPerGroup; i++) {
			spreadsheet.setHeader("N�mero aluno " + (i + 1));
			spreadsheet.setHeader("Nome aluno " + (i + 1));
		}
	}

    public ActionForward detailedProposalList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws FenixActionException, FenixFilterException, FenixServiceException {
    	final String executionDegreeOIDString = request.getParameter("executionDegreeOID");
    	final ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(Integer.valueOf(executionDegreeOIDString));
    	request.setAttribute("executionDegree", executionDegree);
    	request.setAttribute("scheduling", executionDegree.getScheduling());
    	request.setAttribute("degreeCurricularPlanID", executionDegree.getDegreeCurricularPlan().getIdInternal());
    	request.setAttribute("executionDegreeOID", executionDegree.getIdInternal());
    	final SortedSet<Proposal> proposals = new TreeSet<Proposal>(new BeanComparator("proposalNumber"));
    	proposals.addAll(executionDegree.getScheduling().getProposalsSet());
    	request.setAttribute("proposals", proposals);
    	return mapping.findForward("detailed-proposal-list");
    }

}