package ServidorApresentacao.Action.masterDegree.administrativeOffice.gratuity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoContributor;
import DataBeans.InfoGratuitySituation;
import DataBeans.InfoGuide;
import DataBeans.InfoStudent;
import DataBeans.transactions.InfoPaymentTransaction;
import DataBeans.transactions.InfoTransaction;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.masterDegree.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import Util.GuideRequester;
import Util.SituationOfGuide;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public class CreateGuideFromTransactionsDispatchAction extends FenixDispatchAction {

    public ActionForward chooseContributor(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        return mapping.findForward("chooseContributor");

    }

    public ActionForward confirmCreate(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        DynaActionForm createGuideFromTransactionsForm = (DynaActionForm) form;
        IUserView userView = SessionUtils.getUserView(request);

        Integer contributorNumber = (Integer) createGuideFromTransactionsForm.get("contributorNumber");
        Integer gratuitySituationId = (Integer) createGuideFromTransactionsForm
                .get("gratuitySituationId");
        Integer studentId = (Integer) createGuideFromTransactionsForm.get("studentId");
        //Read Contributor
        InfoContributor infoContributor = readContributor(mapping, userView, contributorNumber);
        request.setAttribute(SessionConstants.CONTRIBUTOR, infoContributor);

        // Read Student
        InfoStudent infoStudent = readStudent(mapping, userView, studentId);
        request.setAttribute(SessionConstants.STUDENT, infoStudent);

        //Read Transactions
        List infoTransactions = null;
        Object argsTransactions[] = { gratuitySituationId };
        try {
            infoTransactions = (List) ServiceUtils.executeService(userView,
                    "ReadAllTransactionsByGratuitySituationID", argsTransactions);

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        //Remove Transactions with Guide
        Iterator it = infoTransactions.iterator();
        InfoTransaction infoTransaction = null;
        List infoTransactionsWithoutGuides = new ArrayList();
        while (it.hasNext()) {
            infoTransaction = (InfoTransaction) it.next();
            if (infoTransaction instanceof InfoPaymentTransaction) {
                InfoPaymentTransaction infoPaymentTransaction = (InfoPaymentTransaction) infoTransaction;
                if (infoPaymentTransaction.getInfoGuideEntry() == null) {
                    infoTransactionsWithoutGuides.add(infoTransaction);
                }
            }
        }

        request.setAttribute(SessionConstants.TRANSACTION_LIST, infoTransactionsWithoutGuides);

        return mapping.findForward("confirmCreate");

    }

    public ActionForward create(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        DynaActionForm createGuideFromTransactionsForm = (DynaActionForm) form;
        IUserView userView = SessionUtils.getUserView(request);

        Integer contributorNumber = (Integer) createGuideFromTransactionsForm.get("contributorNumber");
        Integer gratuitySituationId = (Integer) createGuideFromTransactionsForm
                .get("gratuitySituationId");
        Integer studentId = (Integer) createGuideFromTransactionsForm.get("studentId");
        Integer[] transactionsWithoutGuide = (Integer[]) createGuideFromTransactionsForm
                .get("transactionsWithoutGuide");

        //Read Contributor
        InfoContributor infoContributor = readContributor(mapping, userView, contributorNumber);

        // Read Student
        InfoStudent infoStudent = readStudent(mapping, userView, studentId);

        // Read Gratuity Situation
        InfoGratuitySituation infoGratuitySituation = readGratuitySituation(userView,
                gratuitySituationId);

        InfoGuide infoGuide = new InfoGuide();
        infoGuide.setCreationDate(Calendar.getInstance().getTime());
        infoGuide.setGuideRequester(new GuideRequester(GuideRequester.STUDENT));
        infoGuide.setInfoContributor(infoContributor);
        infoGuide.setInfoExecutionDegree(infoGratuitySituation.getInfoGratuityValues()
                .getInfoExecutionDegree());
        infoGuide.setInfoPerson(infoStudent.getInfoPerson());
        infoGuide.setVersion(new Integer(1));
        infoGuide.setYear(new Integer(Calendar.getInstance().get(Calendar.YEAR)));

        Object argsGuide[] = { infoGuide, "", new SituationOfGuide(SituationOfGuide.PAYED),
                Arrays.asList(transactionsWithoutGuide) };
        try {
            infoGuide = (InfoGuide) ServiceUtils.executeService(userView, "CreateGuideFromTransactions",
                    argsGuide);

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        request.setAttribute(SessionConstants.GUIDE, infoGuide);

        return mapping.findForward("createSuccess");

    }

    /**
     * @param errorMapping
     * @param userView
     * @param contributorNumber
     * @return @throws
     *         NonExistingActionException
     * @throws FenixActionException
     */
    private InfoContributor readContributor(ActionMapping errorMapping, IUserView userView,
            Integer contributorNumber) throws NonExistingActionException, FenixActionException {

        InfoContributor infoContributor = null;
        Object argsContributor[] = { contributorNumber };
        try {
            infoContributor = (InfoContributor) ServiceUtils.executeService(userView, "ReadContributor",
                    argsContributor);

        } catch (ExcepcaoInexistente e) {
            throw new NonExistingActionException(
                    "error.masterDegree.administrativeOffice.nonExistingContributorSimple", errorMapping
                            .findForward("chooseContributor"));
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        return infoContributor;
    }

    /**
     * @param userView
     * @param gratuitySituationId
     * @return @throws
     *         FenixActionException
     */
    private InfoGratuitySituation readGratuitySituation(IUserView userView, Integer gratuitySituationId)
            throws FenixActionException {
        InfoGratuitySituation infoGratuitySituation = null;
        Object argsGratuitySituation[] = { gratuitySituationId };
        try {
            infoGratuitySituation = (InfoGratuitySituation) ServiceUtils.executeService(userView,
                    "ReadGratuitySituationById", argsGratuitySituation);

        } catch (ExcepcaoInexistente e) {
            throw new FenixActionException(e);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        return infoGratuitySituation;
    }

    /**
     * @param mapping
     * @param userView
     * @param studentId
     * @return @throws
     *         FenixActionException
     * @throws NonExistingActionException
     */
    private InfoStudent readStudent(ActionMapping mapping, IUserView userView, Integer studentId)
            throws FenixActionException, NonExistingActionException {
        InfoStudent infoStudent = null;
        Object argsStudent[] = { studentId };
        try {
            infoStudent = (InfoStudent) ServiceUtils.executeService(userView, "student.ReadStudentById",
                    argsStudent);

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        if (infoStudent == null) {
            throw new NonExistingActionException("error.exception.masterDegree.nonExistentStudent",
                    mapping.findForward("chooseContributor"));
        }
        return infoStudent;
    }

}