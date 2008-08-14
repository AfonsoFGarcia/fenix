/*
 * Created on May 18, 2004
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.grant.contract;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.grant.contract.GrantCostCenter;
import net.sourceforge.fenixedu.domain.grant.contract.GrantProject;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.security.UserView;

/**
 * @author Pica
 * @author Barbosa
 */
public class ShowAllGrantPaymentEntitiesAction extends FenixDispatchAction {

    public ActionForward showForm(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {

	IUserView userView = UserView.getUser();
	String grantPaymentEntity = null;

	if (verifyParameterInRequest(request, "project")) {
	    grantPaymentEntity = GrantProject.class.getName();
	    request.setAttribute("project", "yes");
	} else if (verifyParameterInRequest(request, "costcenter")) {
	    grantPaymentEntity = GrantCostCenter.class.getName();
	    request.setAttribute("costcenter", "yes");
	} else {
	    throw new Exception();
	}

	Object[] args = { grantPaymentEntity };
	List grantPaymentList = (List) ServiceUtils.executeService("ReadAllGrantPaymentEntitiesByClassName", args);
	request.setAttribute("grantPaymentList", grantPaymentList);

	return mapping.findForward("show-payment-entities");
    }
}