/*
 * Created on 27/Ago/2003
 */

package ServidorApresentacao.Action.student;

import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.util.Base64;

import DataBeans.InfoSiteStudentDistributedTests;
import DataBeans.InfoSiteStudentTestFeedback;
import DataBeans.InfoStudentTestQuestion;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.formbeans.tests.TestForm;
import Util.tests.CorrectionAvailability;
import Util.tests.Response;

/**
 * @author Susana Fernandes
 */
public class StudentTestsAction extends FenixDispatchAction
{

    public ActionForward viewStudentExecutionCoursesWithTests(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        HttpSession session = request.getSession();
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        List studentExecutionCoursesList = null;
        try
        {
            studentExecutionCoursesList = (List) ServiceUtils.executeService(userView,
                    "ReadExecutionCoursesByStudentTests", new Object[]{userView.getUtilizador()});
        }
        catch (FenixServiceException e)
        {
            throw new FenixActionException(e);
        }
        request.setAttribute("studentExecutionCoursesList", studentExecutionCoursesList);
        return mapping.findForward("viewStudentExecutionCoursesWithTests");
    }

    public ActionForward testsFirstPage(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException
    {

        HttpSession session = request.getSession();
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        InfoSiteStudentDistributedTests infoSiteStudentDistributedTests = null;
        String objectCode = request.getParameter("objectCode");
        try
        {
            infoSiteStudentDistributedTests = (InfoSiteStudentDistributedTests) ServiceUtils
                    .executeService(userView, "ReadStudentTests", new Object[]{userView.getUtilizador(),
                            new Integer(objectCode)});
        }
        catch (FenixServiceException e)
        {
            throw new FenixActionException(e);
        }
        request.setAttribute("infoSiteStudentDistributedTests", infoSiteStudentDistributedTests);
        request.setAttribute("objectCode", objectCode);
        return mapping.findForward("testsFirstPage");
    }

    public ActionForward prepareToDoTest(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException
    {

        HttpSession session = request.getSession();
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        String testCode = request.getParameter("testCode");
        List infoStudentTestQuestionList = null;
        String path = getServlet().getServletContext().getRealPath("/");
        try
        {
            infoStudentTestQuestionList = (List) ServiceUtils.executeService(userView,
                    "ReadStudentTestToDo", new Object[]{userView.getUtilizador(), new Integer(testCode),
                            new Boolean(true), path});
        }
        catch (NotAuthorizedException e)
        {
            request.setAttribute("cantDoTest", new Boolean(true));
            return mapping.findForward("testError");
        }
        catch (InvalidArgumentsServiceException e)
        {
            request.setAttribute("invalidTest", new Boolean(true));
            return mapping.findForward("testError");
        }
        catch (FenixServiceException e)
        {
            throw new FenixActionException(e);
        }

        Collections.sort(infoStudentTestQuestionList);
        request.setAttribute("infoStudentTestQuestionList", infoStudentTestQuestionList);
        int numQuestions = ((InfoStudentTestQuestion) infoStudentTestQuestionList.get(0))
                .getDistributedTest().getNumberOfQuestions().intValue();

        Iterator it = infoStudentTestQuestionList.iterator();
        Response[] userResponse = new Response[numQuestions];

        while (it.hasNext())
        {
            InfoStudentTestQuestion infoStudentTestQuestion = (InfoStudentTestQuestion) it.next();
            userResponse[infoStudentTestQuestion.getTestQuestionOrder().intValue() - 1] = infoStudentTestQuestion
                    .getResponse();
        }

        ((TestForm) form).setQuestion(userResponse);
        request.setAttribute("date", getDate());
        return mapping.findForward("doTest");
    }

    public ActionForward showImage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException
    {

        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        String testCode = request.getParameter("testCode");
        String exerciseIdString = request.getParameter("exerciseCode");
        String imgCodeString = request.getParameter("imgCode");
        String imgTypeString = request.getParameter("imgType");
        String feedbackId = request.getParameter("feedbackCode");
        String path = getServlet().getServletContext().getRealPath("/");
        String img = null;
        try
        {
            Object[] args = {userView.getUtilizador(), new Integer(testCode),
                    new Integer(exerciseIdString), new Integer(imgCodeString), feedbackId, path};
            img = (String) ServiceUtils.executeService(userView, "ReadStudentTestQuestionImage", args);
        }
        catch (FenixServiceException e)
        {
            throw new FenixActionException(e);
        }
        byte[] imageData = Base64.decode(img.getBytes());
        try
        {
            response.reset();
            response.setContentType(imgTypeString);
            response.setContentLength(imageData.length);
            response.setBufferSize(imageData.length);
            String imageName = new String("image"
                    + exerciseIdString
                    + imgCodeString
                    + "."
                    + imgTypeString
                            .substring(imgTypeString.lastIndexOf("/") + 1, imgTypeString.length()));
            response.setHeader("Content-disposition", "attachment; filename=" + imageName);
            OutputStream os = response.getOutputStream();
            os.write(imageData, 0, imageData.length);
            response.flushBuffer();
        }
        catch (java.io.IOException e)
        {
            throw new FenixActionException(e);
        }
        return null;
    }

    public ActionForward doTest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException
    {

        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        Response[] userResponse = (Response[]) ((TestForm) form).getQuestion();
        String testCode = request.getParameter("testCode");
        String objectCode = request.getParameter("objectCode");
        String path = getServlet().getServletContext().getRealPath("/");
        List infoStudentTestQuestionList;
        //Boolean sent = new Boolean(false);
        InfoSiteStudentTestFeedback infoSiteStudentTestFeedback;
        try
        {
            infoSiteStudentTestFeedback = (InfoSiteStudentTestFeedback) ServiceUtils.executeService(
                    userView, "InsertStudentTestResponses", new Object[]{userView.getUtilizador(),
                            new Integer(testCode), userResponse, path});
            infoStudentTestQuestionList = (List) ServiceUtils.executeService(userView,
                    "ReadStudentTestToDo", new Object[]{userView.getUtilizador(), new Integer(testCode),
                            new Boolean(false), path});
        }
        catch (NotAuthorizedException e)
        {
            request.setAttribute("cantDoTest", new Boolean(true));
            return mapping.findForward("testError");
        }
        catch (FenixServiceException e)
        {
            throw new FenixActionException(e);
        }
        Collections.sort(infoStudentTestQuestionList);
        request.setAttribute("objectCode", objectCode);
        request.setAttribute("testCode", testCode);

        if (infoSiteStudentTestFeedback != null)
        {
            Iterator it = infoStudentTestQuestionList.iterator();
            int numQuestions = ((InfoStudentTestQuestion) infoStudentTestQuestionList.get(0))
                    .getDistributedTest().getNumberOfQuestions().intValue();
            userResponse = new Response[numQuestions];
            while (it.hasNext())
            {
                InfoStudentTestQuestion infoStudentTestQuestion = (InfoStudentTestQuestion) it.next();
                userResponse[infoStudentTestQuestion.getTestQuestionOrder().intValue() - 1] = infoStudentTestQuestion
                        .getResponse();
            }
            ((TestForm) form).setQuestion(userResponse);
            InfoStudentTestQuestion infoStudentTestQuestion = (InfoStudentTestQuestion) infoStudentTestQuestionList
                    .get(0);
            if (infoStudentTestQuestion.getDistributedTest().getCorrectionAvailability()
                    .getAvailability().equals(new Integer(CorrectionAvailability.ALWAYS)))
                request.setAttribute("infoStudentTestQuestionList", infoStudentTestQuestionList);
            else
                request.setAttribute("infoStudentTestQuestionList", new ArrayList());
            request.setAttribute("infoSiteStudentTestFeedback", infoSiteStudentTestFeedback);
        }

        return mapping.findForward("studentFeedback");
    }

    public ActionForward showTestCorrection(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException
    {

        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        String testCode = request.getParameter("testCode");
        String path = getServlet().getServletContext().getRealPath("/");
        List infoStudentTestQuestionList = null;
        try
        {
            infoStudentTestQuestionList = (List) ServiceUtils.executeService(userView,
                    "ReadStudentTestForCorrection", new Object[]{userView.getUtilizador(),
                            new Integer(testCode), new Boolean(false), path});
        }
        catch (InvalidArgumentsServiceException e)
        {
            request.setAttribute("invalidTest", new Boolean(true));
            return mapping.findForward("testError");
        }
        catch (NotAuthorizedException e)
        {
            request.setAttribute("cantShowTestCorrection", new Boolean(true));
            return mapping.findForward("testError");
        }
        catch (FenixServiceException e)
        {
            throw new FenixActionException(e);
        }

        Collections.sort(infoStudentTestQuestionList);
        request.setAttribute("infoStudentTestQuestionList", infoStudentTestQuestionList);

        int numQuestions = ((InfoStudentTestQuestion) infoStudentTestQuestionList.get(0))
                .getDistributedTest().getNumberOfQuestions().intValue();

        Iterator it = infoStudentTestQuestionList.iterator();
        Response[] userResponse = new Response[numQuestions];
        Double classification = new Double(0);
        while (it.hasNext())
        {
            InfoStudentTestQuestion infoStudentTestQuestion = (InfoStudentTestQuestion) it.next();
            userResponse[infoStudentTestQuestion.getTestQuestionOrder().intValue() - 1] = infoStudentTestQuestion
                    .getResponse();
            if (infoStudentTestQuestion.getTestQuestionMark() != null)
                classification = new Double(classification.doubleValue()
                        + infoStudentTestQuestion.getTestQuestionMark().doubleValue());
        }
        DecimalFormat df = new DecimalFormat("#0.##");
        if (classification.doubleValue() < 0)
            classification = new Double(0);
        request.setAttribute("classification", df.format(classification));
        ((TestForm) form).setQuestion(userResponse);
        return mapping.findForward("showTestCorrection");
    }

    private String getDate()
    {
        String result = new String();
        Calendar calendar = Calendar.getInstance();
        result += calendar.get(Calendar.DAY_OF_MONTH);
        result += "/";
        result += calendar.get(Calendar.MONTH) + 1;
        result += "/";
        result += calendar.get(Calendar.YEAR);
        result += " ";
        result += calendar.get(Calendar.HOUR_OF_DAY);
        result += ":";
        if (calendar.get(Calendar.MINUTE) < 10)
            result += "0";
        result += calendar.get(Calendar.MINUTE);
        result += ":";
        if (calendar.get(Calendar.SECOND) < 10)
            result += "0";
        result += calendar.get(Calendar.SECOND);
        return result;
    }
}