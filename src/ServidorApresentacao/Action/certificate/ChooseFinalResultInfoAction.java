package ServidorApresentacao.Action.certificate;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import DataBeans.InfoCurricularCourse;
import DataBeans.InfoEnrolment;
import DataBeans.InfoEnrolmentEvaluation;
import DataBeans.InfoFinalResult;
import DataBeans.InfoStudent;
import DataBeans.InfoStudentCurricularPlan;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.FinalResulUnreachedActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.Data;
import Util.EnrollmentState;
import Util.Specialization;
import Util.StudentCurricularPlanState;
import Util.TipoCurso;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 *  
 */
public class ChooseFinalResultInfoAction extends DispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);
        if (session != null) {

            session.removeAttribute(SessionConstants.SPECIALIZATIONS);

            List specializations = Specialization.toArrayList();
            session.setAttribute(SessionConstants.SPECIALIZATIONS, specializations);

            return mapping.findForward("PrepareReady");
        }

        throw new Exception();

    }

    public ActionForward choose(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);

        if (session != null) {

            DynaActionForm chooseDeclaration = (DynaActionForm) form;

            IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

            //remove sessions variables
            session.removeAttribute(SessionConstants.DEGREE_TYPE);

            // Get request Information
            Integer requesterNumber = new Integer((String) chooseDeclaration.get("requesterNumber"));
            String graduationType = (String) chooseDeclaration.get("graduationType");

            // inputs
            InfoStudent infoStudent = new InfoStudent();
            infoStudent.setNumber(requesterNumber);
            infoStudent.setDegreeType(new TipoCurso(TipoCurso.MESTRADO));
            session.setAttribute(SessionConstants.DEGREE_TYPE, infoStudent.getDegreeType());

            // output
            List infoStudentCurricularPlanList = null;

            //get informations
            try {
                ArrayList states = new ArrayList();
                states.add(StudentCurricularPlanState.ACTIVE_OBJ);
                states.add(StudentCurricularPlanState.SCHOOLPARTCONCLUDED_OBJ);
                Object args[] = { infoStudent, new Specialization(graduationType), states };
                infoStudentCurricularPlanList = (List) ServiceManagerServiceFactory.executeService(
                        userView, "CreateDeclaration", args);

            } catch (NonExistingServiceException e) {
                throw new NonExistingActionException("A Declara��o", e);
            }

            if (infoStudentCurricularPlanList == null) {
                throw new NonExistingActionException("O aluno");
            }

            if (infoStudentCurricularPlanList.size() == 1) {
                InfoStudentCurricularPlan infoStudentCurricularPlan = (InfoStudentCurricularPlan) infoStudentCurricularPlanList
                        .get(0);
                request.setAttribute("studentCurricularPlanID", infoStudentCurricularPlan
                        .getIdInternal());

                return chooseFinal(mapping, form, request, response);

            } else {
                request.setAttribute("studentCurricularPlans", infoStudentCurricularPlanList);
                request.setAttribute("path", "FinalResult");

                return mapping.findForward("ChooseStudentCurricularPlan");
            }

        }

        throw new Exception();
    }

    /**
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward chooseFinal(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);

        if (session != null) {
            DynaActionForm chooseDeclaration = (DynaActionForm) form;

            IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

            session.removeAttribute(SessionConstants.INFO_STUDENT_CURRICULAR_PLAN);
            session.removeAttribute(SessionConstants.DATE);
            session.removeAttribute(SessionConstants.INFO_FINAL_RESULT);
            session.removeAttribute(SessionConstants.ENROLMENT_LIST);
            session.removeAttribute(SessionConstants.INFO_EXECUTION_YEAR);
            session.removeAttribute(SessionConstants.CONCLUSION_DATE);
            session.removeAttribute(SessionConstants.INFO_BRANCH);
            session.removeAttribute("total");
            session.removeAttribute("givenCredits");

            Integer studentCurricularPlanID = (Integer) request.getAttribute("studentCurricularPlanID");
            if (studentCurricularPlanID == null) {
                studentCurricularPlanID = Integer.valueOf(request
                        .getParameter("studentCurricularPlanID"));
            }

            InfoStudentCurricularPlan infoStudentCurricularPlan = null;

            Object args[] = { studentCurricularPlanID };
            try {
                infoStudentCurricularPlan = (InfoStudentCurricularPlan) ServiceManagerServiceFactory
                        .executeService(userView, "ReadStudentCurricularPlan", args);
            } catch (NonExistingServiceException e) {
                throw new NonExistingActionException("O aluno");
            }

            List enrolmentList = null;
            InfoFinalResult infoFinalResult = null;
            try {
                Object argsFinalResult[] = { infoStudentCurricularPlan };
                infoFinalResult = (InfoFinalResult) ServiceManagerServiceFactory.executeService(
                        userView, "FinalResult", argsFinalResult);
            } catch (FenixServiceException e) {
                throw new FenixServiceException("");
            }

            if (infoFinalResult == null) {
                throw new FinalResulUnreachedActionException("");
            }

            try {
                Object argsEnrolmentList[] = { infoStudentCurricularPlan.getIdInternal(),
                        EnrollmentState.APROVED };
                enrolmentList = (List) ServiceManagerServiceFactory.executeService(userView,
                        "GetEnrolmentList", argsEnrolmentList);

            } catch (NonExistingServiceException e) {
                throw new NonExistingActionException("Inscri��o", e);
            }

            if (enrolmentList.size() == 0) {
                throw new NonExistingActionException("Inscri��o em Disciplinas");
            }

            //check the last exam date
            //                        InfoEnrolmentEvaluation infoEnrolmentEvaluation = new
            // InfoEnrolmentEvaluation();

            String conclusionDate = null;

            Date endOfScholarshipDate = null;
            try {
                Object argsTemp[] = { infoStudentCurricularPlan };
                endOfScholarshipDate = (Date) ServiceManagerServiceFactory.executeService(userView,
                        "GetEndOfScholarshipDate", argsTemp);

            } catch (FenixServiceException e) {
                throw new FenixActionException(e);
            }

            conclusionDate = Data.format2DayMonthYear(endOfScholarshipDate, "/");
            Date dateConclusion = Data.convertStringDate(conclusionDate, "/");
            conclusionDate = DateFormat.getDateInstance().format(dateConclusion);
            //String dataAux = null;
            Object result = null;
            //                        Iterator iterator = enrolmentList.iterator();
            //                        int i = 0;
            //                        while (iterator.hasNext())
            //                        {
            //                            result = iterator.next();
            //                            infoEnrolmentEvaluation = (InfoEnrolmentEvaluation)
            // (((InfoEnrolment) result)
            //                                            .getInfoEvaluations().get(i));
            //                            // dataAux =
            //                            // DateFormat.getDateInstance().format(
            //                            // infoEnrolmentEvaluation.getExamDate());
            //                            /*
            //                             * if (conclusionDate.compareTo(dataAux) == -1) {
            //                             * conclusionDate = dataAux;
            //                             */
            //                        }
            List newEnrolmentList = new ArrayList();
            //get the last enrolmentEvaluation
            Iterator iterator1 = enrolmentList.iterator();
            double sum = 0;
            while (iterator1.hasNext()) {
                result = iterator1.next();
                InfoEnrolment infoEnrolment2 = (InfoEnrolment) result;
                //                            InfoCurricularCourseScope infoCurricularCourseScope =
                // infoEnrolment2
                //                                            .getInfoCurricularCourseScope();
                //                            sum = sum
                //                                            + Double.parseDouble(String
                //                                                            .valueOf(infoCurricularCourseScope
                //                                                                            .getInfoCurricularCourse()
                //                                                                            .getCredits()));

                InfoCurricularCourse infoCurricularCourse = infoEnrolment2.getInfoCurricularCourse();
                sum = sum + Double.parseDouble(String.valueOf(infoCurricularCourse.getCredits()));

                List aux = infoEnrolment2.getInfoEvaluations();

                if (aux != null && aux.size() > 0) {
                    if (aux.size() > 1) {
                        BeanComparator dateComparator = new BeanComparator("when");
                        Collections.sort(aux, dateComparator);
                        Collections.reverse(aux);
                    }
                    InfoEnrolmentEvaluation latestEvaluation = (InfoEnrolmentEvaluation) aux.get(0);
                    infoEnrolment2.setInfoEnrolmentEvaluation(latestEvaluation);
                    newEnrolmentList.add(infoEnrolment2);
                }
            }
            if (!infoStudentCurricularPlan.getGivenCredits().equals(new Double(0))) {
                sum = sum
                        + Double
                                .parseDouble(String.valueOf(infoStudentCurricularPlan.getGivenCredits()));
                session.setAttribute("givenCredits", "POR ATRIBUI��O DE CR�DITOS");
            }

            BigDecimal roundedSum = new BigDecimal(sum);
            session.setAttribute("total", roundedSum.setScale(1, BigDecimal.ROUND_HALF_UP));//.toBigInteger());

            session.setAttribute(SessionConstants.CONCLUSION_DATE, conclusionDate);
            try {
                ServiceManagerServiceFactory.executeService(userView, "ReadCurrentExecutionYear", null);

            } catch (RuntimeException e) {
                throw new RuntimeException("Error", e);
            }
            Locale locale = new Locale("pt", "PT");
            Date date = new Date();
            String anoLectivo = "";
            if (newEnrolmentList != null && newEnrolmentList.size() > 1) {
                anoLectivo = ((InfoEnrolment) newEnrolmentList.get(0)).getInfoExecutionPeriod()
                        .getInfoExecutionYear().getYear();
            }
            String formatedDate = "Lisboa, "
                    + DateFormat.getDateInstance(DateFormat.LONG, locale).format(date);

            session.setAttribute(SessionConstants.INFO_STUDENT_CURRICULAR_PLAN,
                    infoStudentCurricularPlan);

            session.setAttribute(SessionConstants.DATE, formatedDate);
            if (infoStudentCurricularPlan.getInfoBranch() != null
                    && infoStudentCurricularPlan.getInfoBranch().getName().length() != 0)
                session.setAttribute(SessionConstants.INFO_BRANCH, infoStudentCurricularPlan
                        .getInfoBranch().getName());
            session.setAttribute(SessionConstants.INFO_EXECUTION_YEAR, anoLectivo);
            session.setAttribute(SessionConstants.ENROLMENT_LIST, newEnrolmentList);

            session.setAttribute(SessionConstants.INFO_FINAL_RESULT, infoFinalResult);

            return mapping.findForward("ChooseSuccess");

        }

        throw new Exception();
    }
}