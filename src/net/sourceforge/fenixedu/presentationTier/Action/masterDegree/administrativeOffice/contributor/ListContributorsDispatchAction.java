/*
 * Created on 14/Mar/2003
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.contributor;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoContributor;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 * 
 * 
 */
public class ListContributorsDispatchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {

	HttpSession session = request.getSession(false);

	DynaActionForm createContributorForm = (DynaActionForm) form;

	// Clean the form
	createContributorForm.set("contributorNumber", null);

	String action = request.getParameter("action");

	if (action.equals("visualize")) {
	    session.removeAttribute(PresentationConstants.CONTRIBUTOR_ACTION);
	    session.setAttribute(PresentationConstants.CONTRIBUTOR_ACTION, "label.action.contributor.visualize");
	} else if (action.equals("edit")) {
	    session.removeAttribute(PresentationConstants.CONTRIBUTOR_ACTION);
	    session.setAttribute(PresentationConstants.CONTRIBUTOR_ACTION, "label.action.contributor.edit");
	}

	return mapping.findForward("PrepareReady");

    }

    public ActionForward getContributors(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) throws Exception {

	final HttpSession session = request.getSession(false);
	final Integer contributorNumber = getIntegerFromRequestOrForm(request, (DynaActionForm) form, "contributorNumber");

	List<InfoContributor> contributors = Collections.singletonList(InfoContributor.newInfoFromDomain(Party
		.readByContributorNumber(contributorNumber.toString())));

	if (contributors.size() == 1) {
	    InfoContributor infoContributor = (InfoContributor) contributors.get(0);
	    session.removeAttribute(PresentationConstants.CONTRIBUTOR);
	    session.setAttribute(PresentationConstants.CONTRIBUTOR, infoContributor);
	    return mapping.findForward("ActionReady");
	}

	session.removeAttribute(PresentationConstants.CONTRIBUTOR_LIST);
	session.setAttribute(PresentationConstants.CONTRIBUTOR_LIST, contributors);
	return mapping.findForward("ChooseContributor");
    }

    public ActionForward chooseContributor(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	HttpSession session = request.getSession(false);

	List contributorList = (List) session.getAttribute(PresentationConstants.CONTRIBUTOR_LIST);

	Integer choosenContributorPosition = Integer.valueOf(request.getParameter("contributorPosition"));

	// Put the selected Contributor in Session
	InfoContributor infoContributor = (InfoContributor) contributorList.get(choosenContributorPosition.intValue());

	session.setAttribute(PresentationConstants.CONTRIBUTOR, infoContributor);
	return mapping.findForward("ActionReady");

    }

    public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	HttpSession session = request.getSession(false);

	DynaActionForm editContributorForm = (DynaActionForm) form;

	InfoContributor infoContributor = (InfoContributor) session.getAttribute(PresentationConstants.CONTRIBUTOR);

	editContributorForm.set("contributorNumber", String.valueOf(infoContributor.getContributorNumber()));
	editContributorForm.set("contributorName", infoContributor.getContributorName());
	editContributorForm.set("contributorAddress", infoContributor.getContributorAddress());
	editContributorForm.set("areaCode", infoContributor.getAreaCode());
	editContributorForm.set("areaOfAreaCode", infoContributor.getAreaOfAreaCode());
	editContributorForm.set("area", infoContributor.getArea());
	editContributorForm.set("parishOfResidence", infoContributor.getParishOfResidence());
	editContributorForm.set("districtSubdivisionOfResidence", infoContributor.getDistrictSubdivisionOfResidence());
	editContributorForm.set("districtOfResidence", infoContributor.getDistrictOfResidence());

	return mapping.findForward("EditReady");

    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {

	HttpSession session = request.getSession(false);

	DynaActionForm editContributorForm = (DynaActionForm) form;

	IUserView userView = getUserView(request);
	InfoContributor infoContributor = (InfoContributor) session.getAttribute(PresentationConstants.CONTRIBUTOR);

	// Get the Information
	String contributorNumberString = (String) editContributorForm.get("contributorNumber");
	Integer contributorNumber = Integer.valueOf(contributorNumberString);
	String contributorName = (String) editContributorForm.get("contributorName");
	String contributorAddress = (String) editContributorForm.get("contributorAddress");
	String areaCode = (String) editContributorForm.get("areaCode");
	String areaOfAreaCode = (String) editContributorForm.get("areaOfAreaCode");
	String area = (String) editContributorForm.get("area");
	String parishOfResidence = (String) editContributorForm.get("parishOfResidence");
	String districtSubdivisionOfResidence = (String) editContributorForm.get("districtSubdivisionOfResidence");
	String districtOfResidence = (String) editContributorForm.get("districtOfResidence");

	Object args[] = { infoContributor, contributorNumber, contributorName, contributorAddress, areaCode, areaOfAreaCode,
		area, parishOfResidence, districtSubdivisionOfResidence, districtOfResidence };
	InfoContributor newInfoContributor = null;
	try {
	    newInfoContributor = (InfoContributor) ServiceManagerServiceFactory.executeService("EditContributor", args);
	} catch (ExistingServiceException e) {
	    throw new ExistingActionException("O Contribuinte", e);
	}

	session.setAttribute(PresentationConstants.CONTRIBUTOR, newInfoContributor);
	return mapping.findForward("EditSuccess");

    }

}