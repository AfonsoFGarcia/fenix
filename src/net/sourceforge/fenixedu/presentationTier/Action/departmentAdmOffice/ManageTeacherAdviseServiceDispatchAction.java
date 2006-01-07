/**
 * Nov 24, 2005
 */
package net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.commons.OrderedIterator;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacher.AdviseType;
import net.sourceforge.fenixedu.domain.teacher.Advise;
import net.sourceforge.fenixedu.domain.teacher.TeacherAdviseService;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.domain.teacher.Advise.AdvisePercentageException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ManageTeacherAdviseServiceDispatchAction extends FenixDispatchAction {

    public ActionForward prepareTeacherSearch(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws NumberFormatException,
            FenixFilterException, FenixServiceException {

        DynaActionForm dynaForm = (DynaActionForm) form;
        dynaForm.set("method", "showTeacherAdvises");
        return mapping.findForward("search-teacher-form");
    }

    public ActionForward showTeacherAdvises(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws NumberFormatException,
            FenixFilterException, FenixServiceException {

        DynaActionForm dynaForm = (DynaActionForm) form;
        IUserView userView = SessionUtils.getUserView(request);

        final Integer executionPeriodID = (Integer) dynaForm.get("executionPeriodId");
        final ExecutionPeriod executionPeriod = (ExecutionPeriod) ServiceUtils.executeService(
                userView, "ReadDomainExecutionPeriodByOID", new Object[] { executionPeriodID });        

        Integer teacherNumber = Integer.valueOf(dynaForm.getString("teacherNumber"));
        List<Department> manageableDepartments = userView.getPerson().getManageableDepartmentCredits();
        Teacher teacher = null;
        for (Department department : manageableDepartments) {
            teacher = department.getTeacherByPeriod(teacherNumber, executionPeriod.getBeginDate(),
                    executionPeriod.getEndDate());
            if (teacher != null) {
                break;
            }
        }
        if (teacher == null) {
            request.setAttribute("teacherNotFound", "teacherNotFound");
            return mapping.findForward("teacher-not-found");
        }
        dynaForm.set("executionPeriodId", executionPeriod.getIdInternal());
        dynaForm.set("teacherId", teacher.getIdInternal());

        TeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionPeriod);
        if (teacherService != null && !teacherService.getTeacherAdviseServices().isEmpty()) {
            BeanComparator comparator = new BeanComparator("advise.student.number");
            Iterator orderedAdviseServicesIter = new OrderedIterator(teacherService
                    .getTeacherAdviseServices().iterator(), comparator);
            request.setAttribute("adviseServices", orderedAdviseServicesIter);
        }

        request.setAttribute("executionPeriod", executionPeriod);
        request.setAttribute("teacher", teacher);
        return mapping.findForward("list-teacher-advise-services");
    }

    public ActionForward editAdviseService(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws NumberFormatException,
            FenixFilterException, FenixServiceException {

        DynaActionForm adviseServiceForm = (DynaActionForm) form;

        Integer studentNumber = Integer.valueOf(adviseServiceForm.getString("studentNumber"));
        Double percentage = Double.valueOf(adviseServiceForm.getString("percentage"));
        Integer teacherID = (Integer) adviseServiceForm.get("teacherId");
        Integer executionPeriodID = (Integer) adviseServiceForm.get("executionPeriodId");
        Object[] args = { teacherID, executionPeriodID, studentNumber, percentage,
                AdviseType.FINAL_WORK_DEGREE };
        try {
            ServiceUtils.executeService(SessionUtils.getUserView(request), "EditTeacherAdviseService",
                    args);
        } catch (AdvisePercentageException ape) {
            ActionMessages actionMessages = new ActionMessages();
            addMessages(ape, actionMessages, AdviseType.FINAL_WORK_DEGREE);
            saveMessages(request, actionMessages);
            return mapping.getInputForward();
        }
        return showTeacherAdvises(mapping, form, request, response);
    }

    public ActionForward deleteAdviseService(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws NumberFormatException,
            FenixFilterException, FenixServiceException {

        Integer adviseServiceID = Integer.valueOf(request.getParameter("teacherAdviseServiceID"));
        ServiceUtils.executeService(SessionUtils.getUserView(request),
                "DeleteTeacherAdviseServiceByOID", new Object[] { adviseServiceID });
        return showTeacherAdvises(mapping, form, request, response);
    }

    private void addMessages(AdvisePercentageException ape, ActionMessages actionMessages,
            AdviseType adviseType) {
        ExecutionPeriod executionPeriod = ape.getExecutionPeriod();
        for (Advise advise : ape.getAdvises()) {
            TeacherAdviseService teacherAdviseService = advise
                    .getTeacherAdviseServiceByExecutionPeriod(executionPeriod);
            if (adviseType.equals(ape.getAdviseType()) && teacherAdviseService != null) {
                Integer teacherNumber = advise.getTeacher().getTeacherNumber();
                String teacherName = advise.getTeacher().getPerson().getNome();
                Double percentage = teacherAdviseService.getPercentage();
                ActionMessage actionMessage = new ActionMessage(
                        "message.teacherAdvise.teacher.percentageExceed", teacherNumber.toString(),
                        teacherName, percentage.toString(), "%");
                actionMessages.add("message.teacherAdvise.teacher.percentageExceed", actionMessage);
            }
        }
    }
}
