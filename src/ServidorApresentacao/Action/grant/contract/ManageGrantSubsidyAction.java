/*
 * Created on 04 Mar 2004
 */

package ServidorApresentacao.Action.grant.contract;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.grant.contract.InfoGrantContract;
import DataBeans.grant.contract.InfoGrantSubsidy;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author Barbosa
 * @author Pica
 */

public class ManageGrantSubsidyAction extends FenixDispatchAction {

    public ActionForward prepareManageGrantSubsidyForm(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            Integer idContract = null;
            try {
                if (request.getAttribute("idContract") != null) {
                    idContract = (Integer) request.getAttribute("idContract");
                } else {
                    idContract = new Integer(request.getParameter("idContract"));
                }
            } catch (Exception e) {
                request.setAttribute("idContract", new Integer(request.getParameter("idContract")));
                request.setAttribute("idGrantOwner", new Integer(request.getParameter("idGrantOwner")));
                return setError(request, mapping, "errors.grant.unrecoverable", "manage-grant-subsidy",
                        null);
            }

            //Read Contract
            Object[] args = { idContract };
            IUserView userView = SessionUtils.getUserView(request);
            InfoGrantContract infoGrantContract = (InfoGrantContract) ServiceUtils.executeService(
                    userView, "ReadGrantContract", args);

            request.setAttribute("idContract", idContract);
            request.setAttribute("idGrantOwner", infoGrantContract.getGrantOwnerInfo().getIdInternal());

            //Read Subsidies
            Object[] argsActiveSubsidy = { idContract, InfoGrantSubsidy.getActiveStateValue() };
            Object[] argsNotActiveSubsidy = { idContract, InfoGrantSubsidy.getInactiveStateValue() };
            List infoGrantActiveSubsidyList = (List) ServiceUtils.executeService(userView,
                    "ReadAllGrantSubsidiesByGrantContractAndState", argsActiveSubsidy);
            List infoGrantNotActiveSubsidyList = (List) ServiceUtils.executeService(userView,
                    "ReadAllGrantSubsidiesByGrantContractAndState", argsNotActiveSubsidy);

            //If they exist put them on request
            if (infoGrantActiveSubsidyList != null && !infoGrantActiveSubsidyList.isEmpty()) {
                request.setAttribute("infoGrantActiveSubsidyList", infoGrantActiveSubsidyList);
            }
            if (infoGrantNotActiveSubsidyList != null && !infoGrantNotActiveSubsidyList.isEmpty()) {
                request.setAttribute("infoGrantNotActiveSubsidyList", infoGrantNotActiveSubsidyList);
            }

            //Presenting adittional information
            request.setAttribute("grantOwnerNumber", infoGrantContract.getGrantOwnerInfo()
                    .getGrantOwnerNumber());
            request.setAttribute("grantContractNumber", infoGrantContract.getContractNumber());

            return mapping.findForward("manage-grant-subsidy");
        } catch (FenixServiceException e) {
            return setError(request, mapping, "errors.grant.unrecoverable", "manage-grant-subsidy", null);
        } catch (Exception e) {
            return setError(request, mapping, "errors.grant.unrecoverable", "manage-grant-subsidy", null);
        }
    }
}