/*
 * Created on 21/Mar/2003
 *
 */
package ServidorApresentacao.Action.masterDegree.administrativeOffice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import DataBeans.InfoContributor;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 * 
 * This is the Action to create a Contributor
 * 
 */
public class CreateContributorDispatchAction extends DispatchAction {

	public ActionForward prepare(ActionMapping mapping, ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response)
		throws Exception {

		SessionUtils.validSessionVerification(request, mapping);
		HttpSession session = request.getSession(false);

		if (session != null) {
			DynaActionForm createContributorForm = (DynaActionForm) form;
			GestorServicos serviceManager = GestorServicos.manager();
			
			IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
			
			return mapping.findForward("PrepareReady");
		  } else
			throw new Exception();   

	}
		

	public ActionForward create(ActionMapping mapping, ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response)
		throws Exception {

		SessionUtils.validSessionVerification(request, mapping);
		HttpSession session = request.getSession(false);

		if (session != null) {
			
			DynaActionForm createContributorForm = (DynaActionForm) form;

			GestorServicos serviceManager = GestorServicos.manager();
			
			IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
			
			// Get the Information
			Integer contributorNumber = new Integer((String) createContributorForm.get("contributorNumber"));
			String contributorName = (String) createContributorForm.get("contributorName");
			String contributorAddress = (String) createContributorForm.get("contributorAddress");
			
			if (contributorNumber.equals(new Integer(0))) {
				ActionErrors errors = new ActionErrors();
				errors.add("error.invalid.contributorNumber", new ActionError("error.invalid.contributorNumber"));
				saveErrors(request, errors);
				return mapping.getInputForward();
			}
				
				
			
			// Create the new Contributor
			InfoContributor infoContributor = new InfoContributor();
			infoContributor.setContributorNumber(contributorNumber);
			infoContributor.setContributorName(contributorName);
			infoContributor.setContributorAddress(contributorAddress);

			Object args[] = {infoContributor};
	  
	  		try {
				serviceManager.executar(userView, "CreateContributor", args);
			} catch (ExistingServiceException e) {
				throw new ExistingActionException("O Contribuinte", e);
			}

		  return mapping.findForward("CreateSuccess");
		} else
		  throw new Exception();   
	  }
	  
}
