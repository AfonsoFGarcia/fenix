
package ServidorApresentacao.Action.masterDegree.administrativeOffice.guide;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

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
import org.apache.struts.util.LabelValueBean;

import DataBeans.InfoContributor;
import DataBeans.InfoGuide;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.InvalidChangeServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorAplicacao.Servico.exceptions.NonValidChangeServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.InvalidChangeActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.exceptions.NonValidChangeActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.Data;
import Util.PaymentType;
import Util.SituationOfGuide;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 * 
 * 
 */
public class EditGuideDispatchAction extends DispatchAction {

	public ActionForward prepareEditSituation(ActionMapping mapping, ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response)
		throws Exception {

		
		HttpSession session = request.getSession(false);

		if (session != null) {
			DynaActionForm editGuideForm = (DynaActionForm) form;
			GestorServicos serviceManager = GestorServicos.manager();
			
			IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

			// Get the Information
			Integer guideNumber = new Integer((String) request.getParameter("number"));
			Integer guideYear = new Integer((String) request.getParameter("year"));
			Integer guideVersion = new Integer((String) request.getParameter("version"));
					
			Object args[] = { guideNumber, guideYear , guideVersion };

			InfoGuide infoGuide = null;
			try {
				infoGuide = (InfoGuide) serviceManager.executar(userView, "ChooseGuide", args);
			} catch (NonExistingServiceException e) {
				throw new NonExistingActionException("A Vers�o da Guia", e);
			}

			editGuideForm.set("paymentDateDay", Data.OPTION_DEFAULT.toString());
			editGuideForm.set("paymentDateMonth", Data.OPTION_DEFAULT.toString());
			editGuideForm.set( "paymentDateYear", Data.OPTION_DEFAULT.toString());
			session.setAttribute(SessionConstants.MONTH_DAYS_KEY, Data.getMonthDays());
			session.setAttribute(SessionConstants.MONTH_LIST_KEY, Data.getMonths());
			session.setAttribute(SessionConstants.YEARS_KEY, Data.getYears());
			session.setAttribute(SessionConstants.PAYMENT_TYPE, PaymentType.toArrayList());
			session.setAttribute(SessionConstants.GUIDE, infoGuide);
			session.setAttribute(SessionConstants.GUIDE_SITUATION_LIST, SituationOfGuide.toArrayList());
				
			return mapping.findForward("EditReady");
		  } else
			throw new Exception();   
	}
		

	public ActionForward editGuideSituation(ActionMapping mapping, ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response)
		throws Exception {

		HttpSession session = request.getSession(false);

		if (session != null) {
			DynaActionForm editGuideForm = (DynaActionForm) form;
			GestorServicos serviceManager = GestorServicos.manager();
			
			IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
			Integer guideYear = new Integer((String) request.getParameter("year"));
			Integer guideNumber = new Integer((String) request.getParameter("number"));
			Integer guideVersion = new Integer((String) request.getParameter("version"));
			
			String remarks = (String) request.getParameter("remarks");
			String situationOfGuide = (String) request.getParameter("guideSituation");
			Integer paymentDateYear = new Integer((String) request.getParameter("paymentDateYear"));
			Integer paymentDateMonth = new Integer((String) request.getParameter("paymentDateMonth"));
			Integer paymentDateDay = new Integer((String) request.getParameter("paymentDateDay"));
			String paymentType = (String) request.getParameter("paymentType");
			
			// Final form Check

			ActionErrors actionErrors = new ActionErrors();
			if ((situationOfGuide.equals(SituationOfGuide.PAYED_STRING)) && (
					(paymentDateYear.equals(new Integer(-1))) ||
					(paymentDateMonth.equals(new Integer(-1))) ||
					(paymentDateDay.equals(new Integer(-1))))) {
				
				ActionError actionError = new ActionError("error.required.paymentDate");
				actionErrors.add("UnNecessary1", actionError);
			}
	
			if ((situationOfGuide.equals(SituationOfGuide.PAYED_STRING)) && (paymentType.equals(PaymentType.DEFAULT_STRING))){
				ActionError actionError = new ActionError("error.required.paymentType");
				actionErrors.add("UnNecessary2", actionError);
			}
			if (actionErrors.size() != 0) {
				saveErrors(request, actionErrors);
				return mapping.getInputForward();
			}
		
			
			Calendar calendar = Calendar.getInstance();
			calendar.set(paymentDateYear.intValue(), paymentDateMonth.intValue(), paymentDateDay.intValue());
			
			Object args[] = {guideNumber, guideYear, guideVersion, calendar.getTime(), remarks, situationOfGuide, paymentType};
			
			try {
				serviceManager.executar(userView, "ChangeGuideSituation", args);
			} catch (NonValidChangeServiceException e) {
				throw new NonValidChangeActionException(e);
			}

			session.removeAttribute(SessionConstants.GUIDE);
			session.removeAttribute(SessionConstants.MONTH_DAYS_KEY);
			session.removeAttribute(SessionConstants.MONTH_LIST_KEY);
			session.removeAttribute(SessionConstants.YEARS_KEY);
			session.removeAttribute(SessionConstants.PAYMENT_TYPE);
			session.removeAttribute(SessionConstants.GUIDE_SITUATION_LIST);
			
			
			return mapping.findForward("SituationChanged");			

		} else
			throw new Exception();   
	}
		

	public ActionForward prepareEditInformation(ActionMapping mapping, ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response)
		throws Exception {

		HttpSession session = request.getSession(false);

		if (session != null) {
			DynaActionForm editGuideForm = (DynaActionForm) form;
			GestorServicos serviceManager = GestorServicos.manager();


System.out.println("Acyion de Prepare");
			
			IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
			Integer guideYear = new Integer((String) request.getParameter("year"));
			Integer guideNumber = new Integer((String) request.getParameter("number"));
			Integer guideVersion = new Integer((String) request.getParameter("version"));
			
			
			Object args[] = { guideNumber, guideYear, guideVersion };
			
			// Read the Guide 
			
			InfoGuide infoGuide = null;
			List contributors = null;			
			try {
				infoGuide = (InfoGuide) serviceManager.executar(userView, "ChooseGuide", args);
				contributors = (List)  serviceManager.executar(userView, "ReadAllContributors", null);
			} catch (FenixServiceException e) {
				throw new FenixActionException(e);
			}
			
			ArrayList contributorList = new ArrayList();
			Iterator iterator = contributors.iterator();
			while (iterator.hasNext()) {
				InfoContributor infoContributor = (InfoContributor) iterator.next();
				contributorList.add(new LabelValueBean(infoContributor.getContributorName(), infoContributor.getContributorNumber().toString()));
			}

			session.setAttribute(SessionConstants.GUIDE, infoGuide);
			session.setAttribute(SessionConstants.CONTRIBUTOR_LIST, contributorList);
			
			return mapping.findForward("PrepareReady");			

		} else
			throw new Exception();   
	}


	public ActionForward editGuideInformation(ActionMapping mapping, ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response)
		throws Exception {

		HttpSession session = request.getSession(false);

		if (session != null) {
			DynaActionForm editGuideForm = (DynaActionForm) form;
			GestorServicos serviceManager = GestorServicos.manager();
			
			IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

			Integer guideYear = new Integer((String) request.getParameter("year"));
			Integer guideNumber = new Integer((String) request.getParameter("number"));
			Integer guideVersion = new Integer((String) request.getParameter("version"));
			
			
			Object args[] = { guideNumber, guideYear, guideVersion };
			
			// Read the Guide 
			
			InfoGuide infoGuide = null;
			List contributors = null;			
			try {
				infoGuide = (InfoGuide) serviceManager.executar(userView, "ChooseGuide", args);
			} catch (FenixServiceException e) {
				throw new FenixActionException(e);
			}

			
			String contributorString = (String) editGuideForm.get("contributor");

			Integer contributorNumber = null;
			if ((contributorString != null) && (contributorString.length() != 0))
				contributorNumber = new Integer(contributorString);
			
			// Fill in the quantity List
			Enumeration arguments = request.getParameterNames();
			
			String[] quantityList = new String[infoGuide.getInfoGuideEntries().size()];
			while(arguments.hasMoreElements()){
				String parameter = (String) arguments.nextElement();
				if (parameter.startsWith("quantityList")){
					int arrayPosition = "quantityList".length();
					String position = parameter.substring(arrayPosition);
					quantityList[new Integer(position).intValue()] = request.getParameter(parameter);
				}
			}
			
			Object args2[] = {infoGuide, quantityList, contributorNumber };
			Integer oldGuideVersion = new Integer(infoGuide.getVersion().intValue());

			InfoGuide result = null;
			try {
				result = (InfoGuide) serviceManager.executar(userView, "EditGuideInformation", args2);
			} catch (InvalidChangeServiceException e) {
				throw new InvalidChangeActionException(e);
			}

			// Add the new Version to the Guide List
			if (!oldGuideVersion.equals(result.getVersion())){
				List guides = (List) session.getAttribute(SessionConstants.GUIDE_LIST);
				guides.add(result);
				session.setAttribute(SessionConstants.GUIDE_LIST, guides);
			}
			
			request.setAttribute(SessionConstants.GUIDE, result);
			return mapping.findForward("EditInformationSuccess");			

		} else
			throw new Exception();   
	}
		  
}
