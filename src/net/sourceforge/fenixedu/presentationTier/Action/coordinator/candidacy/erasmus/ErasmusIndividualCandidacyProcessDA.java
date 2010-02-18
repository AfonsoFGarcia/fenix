package net.sourceforge.fenixedu.presentationTier.Action.coordinator.candidacy.erasmus;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.person.ChoosePersonBean;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusIndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusIndividualCandidacyProcessBean;
import net.sourceforge.fenixedu.domain.caseHandling.Activity;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.candidacy.IndividualCandidacyProcessDA;
import net.sourceforge.fenixedu.presentationTier.formbeans.FenixActionForm;
import net.sourceforge.fenixedu.util.StringUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(path = "/caseHandlingErasmusIndividualCandidacyProcess", module = "coordinator", formBeanClass = FenixActionForm.class)
@Forwards(tileProperties = @Tile(navLocal = "/coordinator/localNavigationBar.jsp"), value = {
	@Forward(name = "intro", path = "/caseHandlingErasmusCandidacyProcess.do?method=listProcessAllowedActivities"),
	@Forward(name = "list-allowed-activities", path = "/candidacy/erasmus/listIndividualCandidacyActivities.jsp"),
	@Forward(name = "set-coordinator-validation", path = "/coordinator/candidacy/erasmus/setCoordinatorValidation.jsp"),
	@Forward(name = "visualize-alerts", path = "/candidacy/erasmus/visualizeAlerts.jsp") })
public class ErasmusIndividualCandidacyProcessDA extends IndividualCandidacyProcessDA {

    @Override
    protected Class getParentProcessType() {
	return ErasmusCandidacyProcess.class;
    }

    @Override
    protected void prepareInformationForBindPersonToCandidacyOperation(HttpServletRequest request,
	    IndividualCandidacyProcess process) {
	// TODO Auto-generated method stub

    }

    @Override
    protected void setStartInformation(ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	final ErasmusIndividualCandidacyProcessBean bean = new ErasmusIndividualCandidacyProcessBean();
	bean.setCandidacyProcess(getParentProcess(request));

	/*
	 * 06/05/2009 - Due to Public Candidacies, a candidacy created in admin
	 * office is external So we dont require ChoosePersonBean because a
	 * Person will not be associated or created at individual candidacy
	 * creation stage. Instead we bind with an empty PersonBean.
	 * 
	 * bean.setChoosePersonBean(new ChoosePersonBean());
	 */
	/*
	 * 21/07/2009 - Now we create a person to process the payments
	 * imediately
	 */
	bean.setChoosePersonBean(new ChoosePersonBean());
	bean.setPersonBean(new PersonBean());

	bean.getChoosePersonBean().setName("Oh really fun");
	bean.getChoosePersonBean().setDocumentType(IDDocumentType.FOREIGNER_IDENTITY_CARD);

	/*
	 * 06/05/2009 - Also we mark the bean as an external candidacy.
	 */
	bean.setInternalPersonCandidacy(Boolean.TRUE);
	request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);

    }

    @Override
    protected Class getProcessType() {
	return ErasmusIndividualCandidacyProcess.class;
    }

    @Override
    protected ErasmusCandidacyProcess getParentProcess(HttpServletRequest request) {
	return (ErasmusCandidacyProcess) super.getParentProcess(request);
    }

    @Override
    protected ErasmusIndividualCandidacyProcess getProcess(HttpServletRequest request) {
	return (ErasmusIndividualCandidacyProcess) super.getProcess(request);
    }

    @Override
    public ErasmusIndividualCandidacyProcessBean getIndividualCandidacyProcessBean() {
	return (ErasmusIndividualCandidacyProcessBean) super.getIndividualCandidacyProcessBean();
    }

    @Override
    protected List<Activity> getAllowedActivities(final IndividualCandidacyProcess process) {
	List<Activity> activities = process.getAllowedActivities(AccessControl.getUserView());
	ArrayList<Activity> resultActivities = new ArrayList<Activity>();

	for (Activity activity : activities) {
	    if (activity.isVisibleForCoordinator()) {
		resultActivities.add(activity);
	    }
	}

	return resultActivities;
    }

    public ActionForward prepareExecuteSetCoordinatorValidation(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	final ErasmusIndividualCandidacyProcessBean bean = new ErasmusIndividualCandidacyProcessBean(getProcess(request));

	bean.setCreateAlert(true);
	bean.setSendEmail(true);

	request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
	return mapping.findForward("set-coordinator-validation");

    }

    public ActionForward executeSetCoordinatorValidation(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	try {
	    ErasmusIndividualCandidacyProcessBean bean = (ErasmusIndividualCandidacyProcessBean) getIndividualCandidacyProcessBean();

	    if (bean.getCreateAlert()
		    && (StringUtils.isEmpty(bean.getAlertSubject()) || StringUtils.isEmpty(bean.getAlertBody()))) {
		addActionMessage(request, "error.erasmus.alert.subject.and.body.must.not.be.empty");
	    } else {
		executeActivity(getProcess(request), "SetCoordinatorValidation", getIndividualCandidacyProcessBean());
		return listProcessAllowedActivities(mapping, actionForm, request, response);
	    }
	} catch (final DomainException e) {
	    addActionMessage(request, e.getMessage(), e.getArgs());
	}

	request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
	return mapping.findForward("set-coordinator-validation");
    }

    public ActionForward executeSetCoordinatorValidationInvalid(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
	return mapping.findForward("set-coordinator-validation");
    }

    public ActionForward prepareExecuteVisualizeAlerts(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	final ErasmusIndividualCandidacyProcessBean bean = new ErasmusIndividualCandidacyProcessBean(getProcess(request));

	request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
	return mapping.findForward("visualize-alerts");
    }

}
