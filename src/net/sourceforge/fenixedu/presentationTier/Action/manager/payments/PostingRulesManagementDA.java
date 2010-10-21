package net.sourceforge.fenixedu.presentationTier.Action.manager.payments;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.accounting.PostingRulesManager;
import net.sourceforge.fenixedu.applicationTier.Servico.accounting.gratuity.paymentPlan.GratuityPaymentPlanManager;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.accounting.paymentPlan.InstallmentBean;
import net.sourceforge.fenixedu.dataTransferObject.accounting.paymentPlan.PaymentPlanBean;
import net.sourceforge.fenixedu.dataTransferObject.accounting.postingRule.CreateDFAGratuityPostingRuleBean;
import net.sourceforge.fenixedu.dataTransferObject.accounting.postingRule.CreateGratuityPostingRuleBean;
import net.sourceforge.fenixedu.dataTransferObject.accounting.postingRule.CreateStandaloneEnrolmentGratuityPRBean;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.PaymentPlan;
import net.sourceforge.fenixedu.domain.accounting.PostingRule;
import net.sourceforge.fenixedu.domain.accounting.postingRules.gratuity.DFAGratuityByAmountPerEctsPR;
import net.sourceforge.fenixedu.domain.accounting.postingRules.gratuity.DFAGratuityByNumberOfEnrolmentsPR;
import net.sourceforge.fenixedu.domain.accounting.postingRules.gratuity.DFAGratuityPR;
import net.sourceforge.fenixedu.domain.accounting.postingRules.gratuity.StandaloneEnrolmentGratuityPR;
import net.sourceforge.fenixedu.domain.accounting.postingRules.gratuity.DFAGratuityByAmountPerEctsPR.DFAGratuityByAmountPerEctsPREditor;
import net.sourceforge.fenixedu.domain.accounting.postingRules.gratuity.DFAGratuityByNumberOfEnrolmentsPR.DFAGratuityByNumberOfEnrolmentsPREditor;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/postingRules", module = "manager", formBeanClass = PostingRulesManagementDA.PostingRulesManagementForm.class)
@Forwards( {

	@Forward(name = "chooseCategory", path = "/manager/payments/postingRules/management/chooseCategory.jsp"),
	@Forward(name = "choosePostGraduationDegreeCurricularPlans", path = "/manager/payments/postingRules/management/choosePostGraduationDegreeCurricularPlans.jsp"),
	@Forward(name = "showPostGraduationDegreeCurricularPlanPostingRules", path = "/manager/payments/postingRules/management/showPostGraduationDegreeCurricularPlanPostingRules.jsp"),
	@Forward(name = "viewPostingRuleDetails", path = "/manager/payments/postingRules/management/viewPostingRuleDetails.jsp"),
	@Forward(name = "createDFAGratuityPR", path = "/manager/payments/postingRules/management/createDFAGratuityPR.jsp"),
	@Forward(name = "editDFAGratuityPR", path = "/manager/payments/postingRules/management/editDFAGratuityPR.jsp"),
	@Forward(name = "editDegreeCurricularPlanPostingRule", path = "/manager/payments/postingRules/management/editDegreeCurricularPlanPostingRule.jsp"),
	@Forward(name = "showInsurancePostingRules", path = "/manager/payments/postingRules/management/showInsurancePostingRules.jsp"),
	@Forward(name = "editInsurancePR", path = "/manager/payments/postingRules/management/editInsurancePR.jsp"),
	@Forward(name = "showGraduationDegreeCurricularPlans", path = "/manager/payments/postingRules/management/graduation/showGraduationDegreeCurricularPlans.jsp"),
	@Forward(name = "showPaymentPlans", path = "/manager/payments/postingRules/management/graduation/showPaymentPlans.jsp"),
	@Forward(name = "createPaymentPlan", path = "/manager/payments/postingRules/management/graduation/createPaymentPlan.jsp"),
	@Forward(name = "createGraduationGratuityPR", path = "/manager/payments/postingRules/management/graduation/createGraduationGratuityPR.jsp"),
	@Forward(name = "showGraduationDegreeCurricularPlanPostingRules", path = "/manager/payments/postingRules/management/graduation/showGraduationDegreeCurricularPlanPostingRules.jsp"),
	@Forward(name = "createGraduationStandaloneEnrolmentGratuityPR", path = "/manager/payments/postingRules/management/graduation/createGraduationStandaloneEnrolmentGratuityPR.jsp")

})
public class PostingRulesManagementDA extends FenixDispatchAction {

    public static class PostingRulesManagementForm extends ActionForm {

	static private final long serialVersionUID = 1L;

	private Integer executionYearId;

	public Integer getExecutionYearId() {
	    return executionYearId;
	}

	public void setExecutionYearId(Integer executionYearId) {
	    this.executionYearId = executionYearId;
	}

    }

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	return mapping.findForward("chooseCategory");
    }

    public ActionForward managePostGraduationRules(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	final Set<DegreeType> degreeTypes = new HashSet<DegreeType>(2);
	degreeTypes.add(DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA);
	degreeTypes.add(DegreeType.BOLONHA_ADVANCED_SPECIALIZATION_DIPLOMA);

	request.setAttribute("degreeCurricularPlans", DegreeCurricularPlan.readByDegreeTypesAndState(degreeTypes,
		DegreeCurricularPlanState.ACTIVE));

	return mapping.findForward("choosePostGraduationDegreeCurricularPlans");
    }

    public ActionForward viewPostingRuleDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("degreeCurricularPlan", getDegreeCurricularPlan(request));
	request.setAttribute("postingRule", getPostingRule(request));

	return mapping.findForward("viewPostingRuleDetails");
    }

    private PostingRule getPostingRule(HttpServletRequest request) {
	return rootDomainObject.readPostingRuleByOID(getIntegerFromRequest(request, "postingRuleId"));
    }

    private DegreeCurricularPlan getDegreeCurricularPlan(final HttpServletRequest request) {
	return rootDomainObject.readDegreeCurricularPlanByOID(getIntegerFromRequest(request, "degreeCurricularPlanId"));
    }

    public ActionForward prepareEditDegreeCurricularPlanPostingRule(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	final PostingRule postingRule = getPostingRule(request);

	if (postingRule instanceof DFAGratuityPR) {
	    return prepareEditDFAGratuityPR(mapping, form, request, response);
	}

	request.setAttribute("degreeCurricularPlan", getDegreeCurricularPlan(request));
	request.setAttribute("postingRule", postingRule);

	return mapping.findForward("editDegreeCurricularPlanPostingRule");
    }

    public ActionForward prepareEditDegreeCurricularPlanPostingRuleInvalid(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("degreeCurricularPlan", getDegreeCurricularPlan(request));
	request.setAttribute("postingRule", getRenderedObject("postingRule"));

	return mapping.findForward("editDegreeCurricularPlanPostingRule");

    }

    public ActionForward prepareEditDFAGratuityPR(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("degreeCurricularPlan", getDegreeCurricularPlan(request));

	final PostingRule rule = getPostingRule(request);
	if (rule instanceof DFAGratuityByAmountPerEctsPR) {
	    request.setAttribute("postingRuleEditor", DFAGratuityByAmountPerEctsPREditor
		    .buildFrom((DFAGratuityByAmountPerEctsPR) rule));
	} else {
	    request.setAttribute("postingRuleEditor", DFAGratuityByNumberOfEnrolmentsPREditor
		    .buildFrom((DFAGratuityByNumberOfEnrolmentsPR) rule));
	}

	return mapping.findForward("editDFAGratuityPR");
    }

    public ActionForward prepareEditDFAGratuityPRInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("degreeCurricularPlan", getDegreeCurricularPlan(request));
	request.setAttribute("postingRuleEditor", getRenderedObject("postingRuleEditor"));

	return mapping.findForward("editDFAGratuityPR");
    }

    public ActionForward editDFAGratuityPR(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	try {

	    executeFactoryMethod((FactoryExecutor) getRenderedObject("postingRuleEditor"));
	    request.setAttribute("degreeCurricularPlanId", getDegreeCurricularPlan(request).getIdInternal());

	    return showPostGraduationDegreeCurricularPlanPostingRules(mapping, form, request, response);

	} catch (DomainException e) {
	    addActionMessage(request, e.getKey(), e.getArgs());
	    request.setAttribute("postingRuleEditor", getRenderedObject());
	    return mapping.findForward("editDegreeCurricularPlanPostingRule");
	}
    }

    public ActionForward deleteDegreeCurricularPlanPostingRule(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	try {
	    PostingRulesManager.deletePostingRule(getPostingRule(request));
	} catch (DomainException e) {
	    addActionMessage(request, e.getKey(), e.getArgs());
	}

	return showPostGraduationDegreeCurricularPlanPostingRules(mapping, form, request, response);

    }

    public ActionForward showInsurancePostingRules(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("postingRules", getInsurancePostingRules());

	return mapping.findForward("showInsurancePostingRules");
    }

    private Set<PostingRule> getInsurancePostingRules() {
	return RootDomainObject.getInstance().getInstitutionUnit().getUnitServiceAgreementTemplate().getAllPostingRulesFor(
		EventType.INSURANCE);
    }

    public ActionForward prepareEditInsurancePR(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("postingRule", getPostingRule(request));

	return mapping.findForward("editInsurancePR");
    }

    public ActionForward prepareEditInsurancePRInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("postingRule", getRenderedObject("postingRuleEditor"));

	return mapping.findForward("editInsurancePR");
    }

    public ActionForward manageGraduationRules(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	final List<DegreeCurricularPlan> degreeCurricularPlans = DegreeCurricularPlan.readByDegreeTypesAndState(DegreeType
		.getDegreeTypesFor(AdministrativeOfficeType.DEGREE), null);
	degreeCurricularPlans.add(DegreeCurricularPlan.readEmptyDegreeCurricularPlan());

	request.setAttribute("degreeCurricularPlans", degreeCurricularPlans);

	return mapping.findForward("showGraduationDegreeCurricularPlans");
    }

    public ActionForward showPaymentPlans(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	final PostingRulesManagementForm postingRulesManagementForm = (PostingRulesManagementForm) form;

	if (postingRulesManagementForm.getExecutionYearId() == null) {
	    postingRulesManagementForm.setExecutionYearId(ExecutionYear.readCurrentExecutionYear().getIdInternal());
	}

	setRequestAttributesToShowPaymentPlans(request, postingRulesManagementForm);

	return mapping.findForward("showPaymentPlans");
    }

    public ActionForward changeExecutionYearForPaymentPlans(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	setRequestAttributesToShowPaymentPlans(request, (PostingRulesManagementForm) form);

	return mapping.findForward("showPaymentPlans");
    }

    private void setRequestAttributesToShowPaymentPlans(HttpServletRequest request, final PostingRulesManagementForm form) {

	final ExecutionYear executionYear = rootDomainObject.readExecutionYearByOID(form.getExecutionYearId());

	request.setAttribute("executionYears", new ArrayList<ExecutionYear>(rootDomainObject.getExecutionYears()));
	request.setAttribute("paymentPlans", getDegreeCurricularPlan(request).getServiceAgreementTemplate()
		.getGratuityPaymentPlansFor(executionYear));
	request.setAttribute("degreeCurricularPlan", getDegreeCurricularPlan(request));
    }

    public ActionForward prepareCreatePaymentPlan(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	final PaymentPlanBean paymentPlanBean = new PaymentPlanBean(ExecutionYear.readCurrentExecutionYear());
	request.setAttribute("paymentPlanEditor", paymentPlanBean);
	request.setAttribute("installmentEditor", new InstallmentBean(paymentPlanBean));

	return mapping.findForward("createPaymentPlan");
    }

    public ActionForward prepareCreatePaymentPlanInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("paymentPlanEditor", getPaymentPlanBean());
	request.setAttribute("installmentEditor", getInstallment());

	return mapping.findForward("createPaymentPlan");
    }

    public ActionForward changeExecutionYearForPaymentPlanCreate(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("paymentPlanEditor", getPaymentPlanBean());
	request.setAttribute("installmentEditor", getInstallment());

	invalidatePaymentPlanViewStates();

	return mapping.findForward("createPaymentPlan");
    }

    public ActionForward addInstallmentInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("paymentPlanEditor", getPaymentPlanBean());
	request.setAttribute("installmentEditor", getInstallment());

	return mapping.findForward("createPaymentPlan");
    }

    public ActionForward addInstallment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	if (!getInstallment().hasRequiredInformation()) {
	    addActionMessage("installment", request,
		    "label.payments.postingRules.paymentPlan.information.to.create.installment.is.all.required");

	    return addInstallmentInvalid(mapping, form, request, response);

	}

	getPaymentPlanBean().addInstallment(getInstallment());
	request.setAttribute("paymentPlanEditor", getPaymentPlanBean());
	request.setAttribute("installmentEditor", new InstallmentBean(getPaymentPlanBean()));
	invalidatePaymentPlanViewStates();

	return mapping.findForward("createPaymentPlan");
    }

    private void invalidatePaymentPlanViewStates() {
	RenderUtils.invalidateViewState("paymentPlanEditor");
	RenderUtils.invalidateViewState("installmentEditor");
	RenderUtils.invalidateViewState("installmentsEditor");
    }

    private InstallmentBean getInstallment() {
	return getRenderedObject("installmentEditor");
    }

    public ActionForward removeInstallments(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	getPaymentPlanBean().removeSelectedInstallments();

	request.setAttribute("paymentPlanEditor", getPaymentPlanBean());
	request.setAttribute("installmentEditor", new InstallmentBean(getPaymentPlanBean()));

	invalidatePaymentPlanViewStates();

	return mapping.findForward("createPaymentPlan");
    }

    public ActionForward createPaymentPlan(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	try {
	    GratuityPaymentPlanManager.create(getPaymentPlanBean());
	} catch (DomainException e) {
	    addActionMessage(request, e.getKey(), e.getArgs());

	    request.setAttribute("paymentPlanEditor", getPaymentPlanBean());
	    request.setAttribute("installmentEditor", getInstallment());

	    return mapping.findForward("createPaymentPlan");
	}

	return manageGraduationRules(mapping, form, request, response);
    }

    private PaymentPlanBean getPaymentPlanBean() {
	return getRenderedObject("paymentPlanEditor");
    }

    private PaymentPlan getPaymentPlan(final HttpServletRequest request) {
	return rootDomainObject.readPaymentPlanByOID(getIntegerFromRequest(request, "paymentPlanId"));
    }

    public ActionForward deletePaymentPlan(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	try {
	    GratuityPaymentPlanManager.delete(getPaymentPlan(request));
	} catch (DomainException e) {
	    addActionMessage(request, e.getKey(), e.getArgs());
	}

	setRequestAttributesToShowPaymentPlans(request, (PostingRulesManagementForm) form);

	return mapping.findForward("showPaymentPlans");

    }

    public ActionForward prepareCreateGraduationGratuityPR(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("createPostingRuleBean", new CreateGratuityPostingRuleBean());

	return mapping.findForward("createGraduationGratuityPR");
    }

    public ActionForward prepareCreateGraduationGratuityPRInvalid(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("createPostingRuleBean", getRenderedObject("createPostingRuleBean"));

	return mapping.findForward("createGraduationGratuityPR");
    }

    public ActionForward prepareCreateGraduationGratuityPRPostback(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	final Object object = getRenderedObject("createPostingRuleBean");
	RenderUtils.invalidateViewState();

	request.setAttribute("createPostingRuleBean", object);
	return mapping.findForward("createGraduationGratuityPR");
    }

    public ActionForward createGraduationGratuityPR(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	final CreateGratuityPostingRuleBean bean = getRenderedObject("createPostingRuleBean");

	try {
	    PostingRulesManager.createGraduationGratuityPostingRule(bean);
	} catch (DomainException e) {
	    addActionMessage(request, e.getKey(), e.getArgs());

	    request.setAttribute("createPostingRuleBean", bean);

	    return mapping.findForward("createGraduationGratuityPR");
	}

	return manageGraduationRules(mapping, form, request, response);

    }

    public ActionForward showPostGraduationDegreeCurricularPlanPostingRules(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	final DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);

	request.setAttribute("allowCreateGratuityPR", allowCreateGratuityPR(degreeCurricularPlan));
	request.setAttribute("degreeCurricularPlan", degreeCurricularPlan);

	return mapping.findForward("showPostGraduationDegreeCurricularPlanPostingRules");
    }

    private boolean allowCreateGratuityPR(final DegreeCurricularPlan degreeCurricularPlan) {
	// TODO: temporay until DEAs support gratuity types
	return degreeCurricularPlan.getDegreeType() == DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA
		&& !degreeCurricularPlan.getServiceAgreementTemplate().hasActivePostingRuleFor(EventType.GRATUITY);
    }

    public ActionForward showGraduationDegreeCurricularPlanPostingRules(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("degreeCurricularPlan", getDegreeCurricularPlan(request));

	return mapping.findForward("showGraduationDegreeCurricularPlanPostingRules");
    }

    public ActionForward prepareCreateGraduationStandaloneEnrolmentGratuityPR(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	final CreateGratuityPostingRuleBean bean = new CreateStandaloneEnrolmentGratuityPRBean();
	bean.setRule(StandaloneEnrolmentGratuityPR.class);

	request.setAttribute("createPostingRuleBean", bean);

	return mapping.findForward("createGraduationStandaloneEnrolmentGratuityPR");
    }

    public ActionForward prepareCreateGraduationStandaloneEnrolmentGratuityPRInvalid(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("createPostingRuleBean", getRenderedObject("createPostingRuleBean"));
	return mapping.findForward("createGraduationStandaloneEnrolmentGratuityPR");
    }

    public ActionForward prepareCreateGraduationStandaloneEnrolmentGratuityPRPostback(ActionMapping mapping,
	    ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {

	final Object object = getRenderedObject("createPostingRuleBean");
	RenderUtils.invalidateViewState();
	request.setAttribute("createPostingRuleBean", object);

	return mapping.findForward("createGraduationStandaloneEnrolmentGratuityPR");
    }

    public ActionForward createGraduationStandaloneEnrolmentGratuityPR(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	final CreateStandaloneEnrolmentGratuityPRBean bean = getRenderedObject("createPostingRuleBean");

	try {
	    PostingRulesManager.createStandaloneGraduationGratuityPostingRule(bean);
	} catch (DomainException e) {
	    addActionMessage(request, e.getKey(), e.getArgs());

	    request.setAttribute("createPostingRuleBean", bean);

	    return mapping.findForward("createGraduationStandaloneEnrolmentGratuityPR");

	}

	return manageGraduationRules(mapping, form, request, response);

    }

    public ActionForward deleteGraduationDegreeCurricularPlanPostingRule(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	try {
	    PostingRulesManager.deletePostingRule(getPostingRule(request));
	} catch (DomainException e) {
	    addActionMessage(request, e.getKey(), e.getArgs());

	}

	request.setAttribute("degreeCurricularPlanId", getDegreeCurricularPlan(request).getIdInternal());

	return showGraduationDegreeCurricularPlanPostingRules(mapping, form, request, response);
    }

    public ActionForward prepareCreateDFAGratuityPR(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	final DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);

	request.setAttribute("createDFAGratuityPostingRuleBean", new CreateDFAGratuityPostingRuleBean(degreeCurricularPlan
		.getServiceAgreementTemplate()));

	return mapping.findForward("createDFAGratuityPR");
    }

    public ActionForward prepareCreateDFAGratuityPRTypeChosen(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("createDFAGratuityPostingRuleBean",
		(CreateDFAGratuityPostingRuleBean) getObjectFromViewState("createDFAGratuityPostingRuleBean.chooseType"));

	RenderUtils.invalidateViewState();

	return mapping.findForward("createDFAGratuityPR");
    }

    public ActionForward prepareCreateDFAGratuityPRInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("createDFAGratuityPostingRuleBean", getCreateDFAGratuityPostingRuleBeanFromRequest());

	return mapping.findForward("createDFAGratuityPR");
    }

    public ActionForward createDFAGratuityPR(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	try {
	    PostingRulesManager.createDFAGratuityPostingRule(getCreateDFAGratuityPostingRuleBeanFromRequest());
	} catch (DomainException ex) {
	    addActionMessage(request, ex.getKey(), ex.getArgs());

	    request.setAttribute("createDFAGratuityPostingRuleBean", getCreateDFAGratuityPostingRuleBeanFromRequest());

	    return mapping.findForward("createDFAGratuityPR");
	}

	request.setAttribute("degreeCurricularPlanId", getCreateDFAGratuityPostingRuleBeanFromRequest().getDegreeCurricularPlan()
		.getIdInternal());

	return showPostGraduationDegreeCurricularPlanPostingRules(mapping, form, request, response);
    }

    private CreateDFAGratuityPostingRuleBean getCreateDFAGratuityPostingRuleBeanFromRequest() {
	return (CreateDFAGratuityPostingRuleBean) getObjectFromViewState("createDFAGratuityPostingRuleBean");
    }

}
