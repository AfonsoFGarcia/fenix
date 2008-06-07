/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager.inquiries;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.security.UserView;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class CreateClassificationsForStudentsDispatchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixFilterException, FenixServiceException {

        IUserView userView = UserView.getUser();

        InfoExecutionYear executionYear = (InfoExecutionYear) ServiceUtils.executeService(
                "ReadCurrentExecutionYear", null);

        Object[] argsDCPs = { executionYear.getIdInternal() };
        List degreeCurricularPlans = (List) ServiceUtils.executeService(
                "ReadActiveDegreeCurricularPlansByExecutionYear", argsDCPs);
        final ComparatorChain comparatorChain = new ComparatorChain();
        comparatorChain.addComparator(new BeanComparator("infoDegree.tipoCurso"));
        comparatorChain.addComparator(new BeanComparator("infoDegree.nome"));
        Collections.sort(degreeCurricularPlans, comparatorChain);

        request.setAttribute("degreeCurricularPlans", degreeCurricularPlans);

        DynaActionForm dynaActionForm = (DynaActionForm) form;
        Integer[] defaultLimits = { 0, 10, 35, 65, 90, 100 };
        dynaActionForm.set("entryGradeLimits", defaultLimits);
        dynaActionForm.set("approvationRatioLimits", defaultLimits);
        dynaActionForm.set("arithmeticMeanLimits", defaultLimits);

        return mapping.findForward("chooseDegreeCurricularPlan");

    }

    public ActionForward createClassifications(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
            FenixServiceException, IOException {

        IUserView userView = UserView.getUser();

        DynaActionForm dynaActionForm = (DynaActionForm) form;
        Integer degreeCurricularPlanID = (Integer) dynaActionForm.get("degreeCurricularPlanID");
        Integer[] entryGradeLimits = (Integer[]) dynaActionForm.get("entryGradeLimits");
        Integer[] approvationRatioLimits = (Integer[]) dynaActionForm.get("approvationRatioLimits");
        Integer[] arithmeticMeanLimits = (Integer[]) dynaActionForm.get("arithmeticMeanLimits");

        Object[] args = { entryGradeLimits, approvationRatioLimits, arithmeticMeanLimits,
                degreeCurricularPlanID };
        ByteArrayOutputStream resultStream = (ByteArrayOutputStream) ServiceUtils.executeService(
                "CreateClassificationsForStudents", args);

        String currentDate = new SimpleDateFormat("dd-MMM-yy.HH-mm").format(new Date());
        response.setHeader("Content-disposition", "attachment;filename=" + degreeCurricularPlanID + "_" + currentDate
                + ".zip");
        response.setContentType("application/zip");
        DataOutputStream dos = new DataOutputStream(response.getOutputStream());
        dos.write(resultStream.toByteArray());
        dos.close();

        return null;
    }

}
