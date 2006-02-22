package net.sourceforge.fenixedu.presentationTier.Action.degreeAdministrativeOffice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.BothAreasAreTheSameServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ChosenAreasAreIncompatibleServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.EnrolmentRuleServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedBranchChangeException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.OutOfCurricularCourseEnrolmentPeriod;
import net.sourceforge.fenixedu.applicationTier.strategy.enrolment.context.InfoStudentEnrollmentContext;
import net.sourceforge.fenixedu.dataTransferObject.InfoRole;
import net.sourceforge.fenixedu.dataTransferObject.enrollment.InfoAreas2Choose;
import net.sourceforge.fenixedu.dataTransferObject.enrollment.InfoCurricularCourse2EnrollWithInfoCurricularCourse;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseEnrollmentType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.commons.TransactionalDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.util.Data;
import net.sourceforge.fenixedu.util.SecretaryEnrolmentStudentReason;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * @author Fernanda Quit�rio 27/Jan/2004
 * 
 */
public class CurricularCoursesEnrollmentDispatchAction extends TransactionalDispatchAction {

    public ActionForward prepareEnrollmentChooseStudent(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        getExecutionDegree(request);
        Integer degreeCurricularPlanID = null;
        DynaValidatorForm enrollmentForm = (DynaValidatorForm) form;
        if (request.getParameter("degreeCurricularPlanID") != null
                && !request.getParameter("degreeCurricularPlanID").equals("")) {
            degreeCurricularPlanID = new Integer(request.getParameter("degreeCurricularPlanID"));
            request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);
            enrollmentForm.set("degreeCurricularPlanID", degreeCurricularPlanID);
        }
        return mapping.findForward("prepareEnrollmentChooseStudent");
    }

    private Integer getExecutionDegree(HttpServletRequest request) {
        Integer executionDegreeId = null;

        String executionDegreeIdString = request.getParameter("executionDegreeId");
        if (executionDegreeIdString == null) {
            executionDegreeIdString = (String) request.getAttribute("executionDegreeId");
        }
        if (executionDegreeIdString != null) {
            executionDegreeId = Integer.valueOf(executionDegreeIdString);
        }
        request.setAttribute("executionDegreeId", executionDegreeId);

        return executionDegreeId;
    }

    public ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        super.createToken(request);
        return prepareEnrollmentChooseCurricularCourses(mapping, form, request, response);
    }

    private ActionForward prepareEnrollmentChooseCurricularCourses(ActionMapping mapping,
            ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        IUserView userView = SessionUtils.getUserView(request);
        ActionErrors errors = new ActionErrors();
        DynaValidatorForm enrollmentForm = (DynaValidatorForm) form;

        if (enrollmentForm.get("studentNumber") == null
                || ((String) enrollmentForm.get("studentNumber")).equals("")) {
            errors.add("error", new ActionError("error.no.student.in.database", ""));
            saveErrors(request, errors);
            return mapping.getInputForward();
        }

        Integer studentNumber = new Integer((String) enrollmentForm.get("studentNumber"));

        Integer degreeCurricularPlanID = (Integer) enrollmentForm.get("degreeCurricularPlanID");
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);

        Integer executionDegreeId = getExecutionDegree(request);
        InfoStudentEnrollmentContext infoStudentEnrolmentContext = null;
        Object[] args = { executionDegreeId, null, studentNumber };
        try {
            if (!(userView.getRoles().contains(new InfoRole(RoleType.DEGREE_ADMINISTRATIVE_OFFICE)) || userView
                    .getRoles().contains(new InfoRole(RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER)) || userView
                    .getRoles().contains(new InfoRole(RoleType.COORDINATOR)) || userView
                    .getRoles().contains(new InfoRole(RoleType.TEACHER)))) {
                infoStudentEnrolmentContext = (InfoStudentEnrollmentContext) ServiceManagerServiceFactory
                        .executeService(userView, "ShowAvailableCurricularCoursesNew", args);
            } else {
                infoStudentEnrolmentContext = (InfoStudentEnrollmentContext) ServiceManagerServiceFactory
                        .executeService(userView,
                                "ShowAvailableCurricularCoursesWithoutEnrollmentPeriod", args);
            }
        } catch (NotAuthorizedFilterException e) {
            if (e.getMessage() != null) {
                addAuthorizationErrors(errors, e);
            } else {
                errors.add("notauthorized", new ActionError(
                        "error.exception.notAuthorized.student.warningTuition"));
            }
        } catch (ExistingServiceException e) {
            if (e.getMessage().equals("student")) {
                errors.add("student", new ActionError("error.no.student.in.database", studentNumber));
            } else if (e.getMessage().equals("studentCurricularPlan")) {
                errors.add("studentCurricularPlan", new ActionError(
                        "error.student.curricularPlan.nonExistent"));
            }
        } catch (OutOfCurricularCourseEnrolmentPeriod e) {
            String startDate = "", endDate = "";
            if (e.getStartDate() != null) {
                startDate = Data.format2DayMonthYear(e.getStartDate());
            }
            if (e.getEndDate() != null) {
                endDate = Data.format2DayMonthYear(e.getEndDate()); 
            }
            errors.add("enrolment", new ActionError(e.getMessageKey(), startDate, endDate));
        } catch (EnrolmentRuleServiceException e) {

            SecretaryEnrolmentStudentReason reason = SecretaryEnrolmentStudentReason.getEnum(e
                    .getErrorType());
            errors.add("enrolmentRule", new ActionError(reason.getName()));
        } catch (FenixServiceException e) {
            if (e.getMessage().equals("degree")) {
                errors.add("degree", new ActionError("error.student.degreeCurricularPlan.LEEC"));
            } else if (e.getMessage().equals("enrolmentPeriod")) {
                errors.add("enrolmentPeriod", new ActionError("error.student.enrolmentPeriod"));
            } else if (errors.isEmpty()) {
                throw new FenixActionException(e);
            }
        }
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.getInputForward();
        }

        Collections.sort(infoStudentEnrolmentContext.getStudentCurrentSemesterInfoEnrollments(),
                new BeanComparator("infoCurricularCourse.name"));

        List warnings = new ArrayList();
        // check if AMII is available to enroll
        boolean amIItoEnroll = CollectionUtils.exists(infoStudentEnrolmentContext
                .getCurricularCourses2Enroll(), new Predicate() {

            public boolean evaluate(Object arg0) {
                InfoCurricularCourse2EnrollWithInfoCurricularCourse curricularCourse2Enroll = (InfoCurricularCourse2EnrollWithInfoCurricularCourse) arg0;
                if (curricularCourse2Enroll.getInfoCurricularCourse().getName().equals(
                        "An�lise Matem�tica II"))
                    return true;
                return false;
            }
        });

        /*if (amIItoEnroll)
            warnings.add("warning.amIItoEnroll");*/

        // check if students belongs to LERCI2003/2004
        if (infoStudentEnrolmentContext.getInfoStudentCurricularPlan().getInfoDegreeCurricularPlan()
                .getIdInternal().equals(90))
            warnings.add("warning.lerci");

        if (!warnings.isEmpty())
            request.setAttribute("warnings", warnings);

        request.setAttribute("infoStudentEnrolmentContext", infoStudentEnrolmentContext);

        return mapping.findForward("prepareEnrollmentChooseCurricularCourses");
    }

    private void addAuthorizationErrors(ActionErrors errors, NotAuthorizedFilterException e) {
        String messageException = e.getMessage();
        String message = null;
        String arg1 = null;
        String arg2 = null;
        if (messageException.indexOf("+") != -1) {
            message = messageException.substring(0, messageException.indexOf("+"));
            String newMessage = messageException.substring(messageException.indexOf("+") + 1);
            if (newMessage.indexOf("+") != -1) {
                arg1 = newMessage.substring(0, newMessage.indexOf("+"));
                arg2 = newMessage.substring(newMessage.indexOf("+") + 1);
            } else {
                arg1 = newMessage;
            }
        } else {
            message = messageException;
        }
        errors.add("notauthorized", new ActionError(message, arg1, arg2));
    }

    public ActionForward prepareEnrollmentPrepareChooseAreas(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        super.createToken(request);

        IUserView userView = SessionUtils.getUserView(request);
        ActionErrors errors = new ActionErrors();
        DynaValidatorForm enrollmentForm = (DynaValidatorForm) form;

        Integer degreeCurricularPlanID = (Integer) enrollmentForm.get("degreeCurricularPlanID");
        if (degreeCurricularPlanID == null) {
            degreeCurricularPlanID = new Integer(request.getParameter("degreeCurricularPlanID"));
        }
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);

        Integer studentNumber = Integer.valueOf(request.getParameter("studentNumber"));
        enrollmentForm.set("studentNumber", studentNumber.toString());

        String specialization = request.getParameter("specializationArea");
        String secondary = request.getParameter("secondaryArea");

        maintainEnrollmentState(request, studentNumber);

        Integer executionDegreeId = getExecutionDegree(request);
        InfoAreas2Choose infoAreas2Choose = null;
        Object[] args = { executionDegreeId, null, studentNumber };
        try {
            infoAreas2Choose = (InfoAreas2Choose) ServiceManagerServiceFactory.executeService(userView,
                    "ReadSpecializationAndSecundaryAreasByStudent", args);
        } catch (NotAuthorizedFilterException e) {
            if (e.getMessage() != null) {
                addAuthorizationErrors(errors, e);
            } else {
                errors.add("notauthorized", new ActionError("error.exception.notAuthorized"));
            }
        } catch (ExistingServiceException e) {
            if (e.getMessage().equals("student")) {
                errors.add("student", new ActionError("error.no.student.in.database", studentNumber));
            } else if (e.getMessage().equals("studentCurricularPlan")) {
                errors.add("studentCurricularPlan", new ActionError(
                        "error.student.curricularPlan.nonExistent"));
            }
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.findForward("beginTransaction");
        }
        if (specialization != null && specialization.length() > 0 && secondary != null
                && secondary.length() > 0) {
            enrollmentForm.set("specializationArea", Integer.valueOf(specialization));
            enrollmentForm.set("secondaryArea", Integer.valueOf(secondary));
        }

        Collections.sort(infoAreas2Choose.getFinalSpecializationAreas(), new BeanComparator("name"));
        Collections.sort(infoAreas2Choose.getFinalSecundaryAreas(), new BeanComparator("name"));
        request.setAttribute("branches", infoAreas2Choose);
        return mapping.findForward("prepareEnrollmentChooseAreas");
    }

    private void maintainEnrollmentState(HttpServletRequest request, Integer studentNumber) {
        String executionPeriod = request.getParameter("executionPeriod");
        String executionYear = request.getParameter("executionYear");
        String studentName = request.getParameter("studentName");
        String studentCurricularPlanId = request.getParameter("studentCurricularPlanId");

        request.setAttribute("executionPeriod", executionPeriod);
        request.setAttribute("executionYear", executionYear);
        request.setAttribute("studentName", studentName);
        request.setAttribute("studentNumber", studentNumber.toString());
        request.setAttribute("studentCurricularPlanId", studentCurricularPlanId);
    }

    public ActionForward prepareEnrollmentChooseAreas(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        super.validateToken(request, form, mapping, "error.transaction.enrollment");

        IUserView userView = SessionUtils.getUserView(request);
        ActionErrors errors = new ActionErrors();
        DynaValidatorForm enrollmentForm = (DynaValidatorForm) form;
        Integer specializationArea = (Integer) enrollmentForm.get("specializationArea");
        Integer secondaryArea = (Integer) enrollmentForm.get("secondaryArea");
        Integer studentCurricularPlanId = Integer.valueOf(request
                .getParameter("studentCurricularPlanId"));
        Integer studentNumber = Integer.valueOf((String) enrollmentForm.get("studentNumber"));

        Integer degreeCurricularPlanID = (Integer) enrollmentForm.get("degreeCurricularPlanID");
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);

        Integer executionDegreeId = getExecutionDegree(request);
        Object[] args = { executionDegreeId, studentCurricularPlanId, specializationArea, secondaryArea };
        try {
            ServiceManagerServiceFactory.executeService(userView, "WriteStudentAreas", args);
        } catch (NotAuthorizedFilterException e) {
            if (e.getMessage() != null) {
                addAuthorizationErrors(errors, e);
            } else {
                errors.add("notauthorized", new ActionError("error.exception.notAuthorized"));
            }
        } catch (BothAreasAreTheSameServiceException e) {
            errors.add("bothAreas", new ActionError("error.student.enrollment.AreasNotEqual"));
        } catch (ChosenAreasAreIncompatibleServiceException e) {
            errors.add("incompatibleAreas",
                    new ActionError("error.student.enrollment.incompatibleAreas"));
        } catch (ExistingServiceException e) {
            errors.add("studentCurricularPlan", new ActionError(
                    "error.student.curricularPlan.nonExistent"));
        } catch (InvalidArgumentsServiceException e) {
            errors.add("areas", new ActionError("error.areas.choose"));
        } catch (NotAuthorizedBranchChangeException e) {
            errors.add("areas", new ActionError("error.areas.notAuthorized"));
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        if (!errors.isEmpty()) {
            maintainEnrollmentState(request, studentNumber);
            saveErrors(request, errors);
            return mapping.findForward("prepareChooseAreas");
        }

        return prepareEnrollmentChooseCurricularCourses(mapping, form, request, response);
    }

    public ActionForward enrollInCurricularCourse(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        super.validateToken(request, form, mapping, "error.transaction.enrollment");

        ActionErrors errors = new ActionErrors();
        IUserView userView = SessionUtils.getUserView(request);
        DynaValidatorForm enrollmentForm = (DynaValidatorForm) form;
        String curricularCourseToEnroll = (String) enrollmentForm.get("curricularCourse");

        Integer studentCurricularPlanId = Integer.valueOf(request
                .getParameter("studentCurricularPlanId"));

        Integer degreeCurricularPlanID = (Integer) enrollmentForm.get("degreeCurricularPlanID");
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);

        Integer toEnroll = Integer.valueOf(curricularCourseToEnroll.split("-")[0]);

        CurricularCourseEnrollmentType enrollmentType = CurricularCourseEnrollmentType
                .valueOf(curricularCourseToEnroll.split("-")[1]);

        String courseType = (String) enrollmentForm.get("courseType");
        Integer executionDegreeId = getExecutionDegree(request);
        Object[] args = { executionDegreeId, studentCurricularPlanId, toEnroll, null, enrollmentType,
                new Integer(courseType), userView };
        try {
            ServiceManagerServiceFactory.executeService(userView, "WriteEnrollment", args);
        } catch (NotAuthorizedFilterException e) {
            if (e.getMessage() != null) {
                addAuthorizationErrors(errors, e);
            } else {
                errors.add("notauthorized", new ActionError("error.exception.notAuthorized"));
            }
            saveErrors(request, errors);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        return prepareEnrollmentChooseCurricularCourses(mapping, form, request, response);
    }

    public ActionForward unenrollFromCurricularCourse(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        super.validateToken(request, form, mapping, "error.transaction.enrollment");

        ActionErrors errors = new ActionErrors();
        IUserView userView = SessionUtils.getUserView(request);
        DynaValidatorForm enrollmentForm = (DynaValidatorForm) form;

        Integer unenroll = Integer.valueOf((String) enrollmentForm.get("curricularCourse"));
        Integer degreeCurricularPlanID = (Integer) enrollmentForm.get("degreeCurricularPlanID");
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);

        Integer studentCurricularPlanId = Integer.valueOf(request
                .getParameter("studentCurricularPlanId"));

        Integer executionDegreeId = getExecutionDegree(request);
        Object[] args = { executionDegreeId, studentCurricularPlanId, unenroll };
        try {
            ServiceManagerServiceFactory.executeService(userView, "DeleteEnrolment", args);
        } catch (NotAuthorizedFilterException e) {
            if (e.getMessage() != null) {
                addAuthorizationErrors(errors, e);
            } else {
                errors.add("notauthorized", new ActionError("error.exception.notAuthorized"));
            }
            saveErrors(request, errors);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        return prepareEnrollmentChooseCurricularCourses(mapping, form, request, response);
    }

    public ActionForward enrollmentConfirmation(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        IUserView userView = SessionUtils.getUserView(request);
        ActionErrors errors = new ActionErrors();
        DynaValidatorForm enrollmentForm = (DynaValidatorForm) form;

        Integer studentNumber = new Integer((String) enrollmentForm.get("studentNumber"));
        Integer studentCurricularPlanId = Integer.valueOf(request
                .getParameter("studentCurricularPlanId"));

        Integer degreeCurricularPlanID = (Integer) enrollmentForm.get("degreeCurricularPlanID");
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);

        Integer executionDegreeId = getExecutionDegree(request);
        InfoStudentEnrollmentContext infoStudentEnrolmentContext = null;
        Object[] args = { executionDegreeId, null, studentNumber };
        try {
            if (!(userView.getRoles().contains(new InfoRole(RoleType.DEGREE_ADMINISTRATIVE_OFFICE))
                    || userView.getRoles().contains(
                            new InfoRole(RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER)) || userView
                    .getRoles().contains(new InfoRole(RoleType.TEACHER)))) {
                infoStudentEnrolmentContext = (InfoStudentEnrollmentContext) ServiceManagerServiceFactory
                        .executeService(userView, "ShowAvailableCurricularCoursesNew", args);
            } else {
                infoStudentEnrolmentContext = (InfoStudentEnrollmentContext) ServiceManagerServiceFactory
                        .executeService(userView,
                                "ShowAvailableCurricularCoursesWithoutEnrollmentPeriod", args);
            }
        } catch (NotAuthorizedFilterException e) {
            if (e.getMessage() != null) {
                addAuthorizationErrors(errors, e);
            } else {
                errors.add("notauthorized", new ActionError("error.exception.notAuthorized"));
            }
        } catch (ExistingServiceException e) {
            if (e.getMessage().equals("student")) {
                errors.add("student", new ActionError("error.no.student.in.database", studentNumber));
            } else if (e.getMessage().equals("studentCurricularPlan")) {
                errors.add("studentCurricularPlan", new ActionError(
                        "error.student.curricularPlan.nonExistent"));
            }
        } catch (OutOfCurricularCourseEnrolmentPeriod e) {
            errors.add("enrolment", new ActionError(e.getMessageKey(), Data.format2DayMonthYear(e
                    .getStartDate()), Data.format2DayMonthYear(e.getEndDate())));
        } catch (FenixServiceException e) {
            if (e.getMessage().equals("degree")) {
                errors.add("degree", new ActionError("error.student.degreeCurricularPlan.LEEC"));
            }
            if (e.getMessage().equals("enrolmentPeriod")) {
                errors.add("enrolmentPeriod", new ActionError("error.student.enrolmentPeriod"));
            }

            throw new FenixActionException(e);
        }
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return prepareEnrollmentChooseCurricularCourses(mapping, form, request, response);
        }

        List curriculum = null;
        Object args2[] = { executionDegreeId, studentCurricularPlanId };
        try {
            curriculum = (ArrayList) ServiceManagerServiceFactory.executeService(userView,
                    "ReadStudentCurriculumForEnrollmentConfirmation", args2);
        } catch (NotAuthorizedFilterException e) {
            if (e.getMessage() != null) {
                addAuthorizationErrors(errors, e);
            } else {
                errors.add("notauthorized", new ActionError("error.exception.notAuthorized"));
            }
            saveErrors(request, errors);
            return prepareEnrollmentChooseCurricularCourses(mapping, form, request, response);
        }
        Collections.sort(infoStudentEnrolmentContext.getStudentCurrentSemesterInfoEnrollments(),
                new BeanComparator("infoCurricularCourse.name"));

        sortCurriculum(curriculum);

        request.setAttribute("infoStudentEnrolmentContext", infoStudentEnrolmentContext);
        request.setAttribute("curriculum", curriculum);

        return mapping.findForward("enrollmentConfirmation");
    }

    private void sortCurriculum(List curriculum) {
        BeanComparator courseName = new BeanComparator("infoCurricularCourse.name");
        BeanComparator executionYear = new BeanComparator("infoExecutionPeriod.infoExecutionYear.year");
        ComparatorChain chainComparator = new ComparatorChain();
        chainComparator.addComparator(courseName);
        chainComparator.addComparator(executionYear);

        Collections.sort(curriculum, chainComparator);
    }
}