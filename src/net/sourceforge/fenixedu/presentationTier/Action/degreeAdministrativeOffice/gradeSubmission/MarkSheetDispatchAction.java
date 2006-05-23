package net.sourceforge.fenixedu.presentationTier.Action.degreeAdministrativeOffice.gradeSubmission;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetManagementBaseBean;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.MarkSheet;
import net.sourceforge.fenixedu.domain.MarkSheetType;
import net.sourceforge.fenixedu.domain.OccupationPeriod;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;
import net.sourceforge.fenixedu.util.DateFormatUtil;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

public class MarkSheetDispatchAction extends FenixDispatchAction {
    
    protected ActionMessages createActionMessages() {
        return new ActionMessages();
    }
    
    protected void addMessage(HttpServletRequest request, ActionMessages actionMessages, String keyMessage, String ... args) {
        actionMessages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(keyMessage, args));
        saveMessages(request, actionMessages);
    }

    protected void fillMarkSheetBean(ActionForm actionForm, HttpServletRequest request, MarkSheetManagementBaseBean markSheetBean) {
        DynaActionForm form = (DynaActionForm) actionForm;
        
        Integer executionPeriodID = (Integer) form.get("epID");
        Integer degreeID = (Integer) form.get("dID");
        Integer degreeCurricularPlanID = (Integer) form.get("dcpID");
        Integer curricularCourseID = (Integer) form.get("ccID");
        
        markSheetBean.setExecutionPeriod(rootDomainObject.readExecutionPeriodByOID(executionPeriodID));
        markSheetBean.setDegree(rootDomainObject.readDegreeByOID(degreeID));
        markSheetBean.setDegreeCurricularPlan(rootDomainObject.readDegreeCurricularPlanByOID(degreeCurricularPlanID));
        markSheetBean.setCurricularCourse((CurricularCourse) rootDomainObject.readDegreeModuleByOID(curricularCourseID));
        
        request.setAttribute("edit", markSheetBean);
    }

    public ActionForward prepareSearchMarkSheetPostBack(ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        
        Object object = RenderUtils.getViewState().getMetaObject().getObject();
        RenderUtils.invalidateViewState();
        request.setAttribute("edit", object);
        
        return mapping.getInputForward();
    }
    
    public ActionForward prepareSearchMarkSheetInvalid(ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        
        request.setAttribute("edit", RenderUtils.getViewState().getMetaObject().getObject());        
        return mapping.getInputForward();
    }
    
    public ActionForward viewMarkSheet(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm form = (DynaActionForm) actionForm;
        Integer markSheetID = (Integer) form.get("msID");
        MarkSheet markSheet = rootDomainObject.readMarkSheetByOID(markSheetID);

        request.setAttribute("markSheet", markSheet);
        return mapping.findForward("viewMarkSheet");
    }
    
    public ActionForward prepareDeleteMarkSheet(ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        
        DynaActionForm form = (DynaActionForm) actionForm;
        Integer markSheetID = (Integer) form.get("msID");
        request.setAttribute("markSheet", rootDomainObject.readMarkSheetByOID(markSheetID));
        
        return mapping.findForward("removeMarkSheet");
    }
    
    public ActionForward deleteMarkSheet(ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        
        ActionMessages actionMessages = createActionMessages();
        DynaActionForm form = (DynaActionForm) actionForm;
        Integer markSheetID = (Integer) form.get("msID");
        
        try {
            ServiceUtils.executeService(getUserView(request), "DeleteMarkSheet", new Object[] {markSheetID});
        } catch (FenixFilterException e) {
            addMessage(request, actionMessages, "error.notAuthorized");
        } catch (FenixServiceException e) {
            addMessage(request, actionMessages, e.getMessage());
        } catch (DomainException e) {
            addMessage(request, actionMessages, e.getMessage(), e.getArgs());
        }
        
        return mapping.findForward("searchMarkSheetFilled");
    }
    
    public ActionForward prepareConfirmMarkSheet(ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm form = (DynaActionForm) actionForm;
        Integer markSheetID = (Integer) form.get("msID");
        request.setAttribute("markSheet", rootDomainObject.readMarkSheetByOID(markSheetID));
        
        return mapping.findForward("confirmMarkSheet");
    }
    
    public ActionForward confirmMarkSheet(ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
        DynaActionForm form = (DynaActionForm) actionForm;
        Integer markSheetID = (Integer) form.get("msID");
        MarkSheet markSheet = rootDomainObject.readMarkSheetByOID(markSheetID);
        IUserView userView = getUserView(request);
        ActionMessages actionMessages = new ActionMessages();
        try {
            ServiceUtils.executeService(userView, "ConfirmMarkSheet", new Object[] {markSheet, userView.getPerson().getEmployee()});
        } catch (NotAuthorizedFilterException e) {
            addMessage(request, actionMessages, "error.notAuthorized");
        } catch (DomainException e) {
            addMessage(request, actionMessages, e.getMessage(), e.getArgs());
        }

        
        return mapping.findForward("searchMarkSheetFilled");
    }    

    
    protected void checkIfEvaluationDateIsInExamsPeriod(DegreeCurricularPlan degreeCurricularPlan,
            ExecutionPeriod executionPeriod, Date evaluationDate, MarkSheetType markSheetType,
            HttpServletRequest request, ActionMessages actionMessages) {
        
        ExecutionDegree executionDegree = degreeCurricularPlan.getExecutionDegreeByYear(executionPeriod.getExecutionYear());
        
        if (executionDegree == null) {
            addMessage(request, actionMessages, "error.evaluationDateNotInExamsPeriod");
            
        } else if (! executionDegree.isEvaluationDateInExamPeriod(evaluationDate, executionPeriod, markSheetType)) {
            
            OccupationPeriod occupationPeriod = executionDegree.getOccupationPeriodFor(executionPeriod, markSheetType);
            if (occupationPeriod == null) {
                addMessage(request, actionMessages, "error.evaluationDateNotInExamsPeriod");
            } else {
                addMessage(request, actionMessages, "error.evaluationDateNotInExamsPeriodWithDates",
                        DateFormatUtil.format("dd/MM/yyyy", occupationPeriod.getStart()),
                        DateFormatUtil.format("dd/MM/yyyy", occupationPeriod.getEnd()));
            }
        }
    }

    protected void checkIfTeacherIsResponsibleOrCoordinator(DegreeCurricularPlan degreeCurricularPlan, CurricularCourse curricularCourse, ExecutionPeriod executionPeriod, Integer teacherNumber, Teacher teacher, HttpServletRequest request, ActionMessages actionMessages) {
        if (teacher == null) {
            addMessage(request, actionMessages, "error.noTeacher", String.valueOf(teacherNumber));
        } else if (!teacher.isResponsibleOrCoordinatorFor(degreeCurricularPlan, curricularCourse, executionPeriod)) {
            addMessage(request, actionMessages, "error.teacherNotResponsibleOrNotCoordinator");
        }
    }

    public ActionForward backSearchMarkSheet(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
        return mapping.findForward("searchMarkSheetFilled");
    }
}
