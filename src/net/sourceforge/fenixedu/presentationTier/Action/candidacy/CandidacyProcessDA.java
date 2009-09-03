package net.sourceforge.fenixedu.presentationTier.Action.candidacy;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.PublicCandidacyHashCode;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcessBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyPersonalDetails;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyState;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.casehandling.CaseHandlingDispatchAction;
import net.sourceforge.fenixedu.presentationTier.formbeans.FenixActionForm;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.SpreadsheetXLSExporter;
import pt.utl.ist.fenix.tools.util.i18n.Language;

/**
 * INFO: when extending this class pay attention to the following aspects
 * 
 * <p>
 * Must configure the following forwards: intro (common value:
 * /candidacy/mainCandidacyProcess.jsp), prepare-create-new-process (common
 * value: /candidacy/createCandidacyPeriod.jsp; used schemas:
 * <process_name>Bean.manage), prepare-edit-candidacy-period (common value:
 * /candidacy/editCandidacyPeriod.jsp; used schemas: <process_name>Bean.manage)
 * 
 */

abstract public class CandidacyProcessDA extends CaseHandlingDispatchAction {

    abstract protected Class getChildProcessType();

    abstract protected Class getCandidacyPeriodType();

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	setExecutionInterval(request);
	return super.execute(mapping, actionForm, request, response);
    }

    public ActionForward intro(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	setStartInformation(actionForm, request, response);
	return introForward(mapping);
    }

    protected void setExecutionInterval(final HttpServletRequest request) {
	final Integer executionIntervalId = getIntegerFromRequest(request, "executionIntervalId");
	if (executionIntervalId != null) {
	    request.setAttribute("executionInterval", rootDomainObject.readExecutionIntervalByOID(executionIntervalId));
	}
    }

    protected ExecutionInterval getExecutionInterval(final HttpServletRequest request) {
	return (ExecutionInterval) request.getAttribute("executionInterval");
    }

    protected boolean hasExecutionInterval(final HttpServletRequest request) {
	return getExecutionInterval(request) != null;
    }

    /**
     * Set information used to present main candidacy process page
     */
    abstract protected void setStartInformation(ActionForm actionForm, HttpServletRequest request, HttpServletResponse response);

    abstract protected ActionForward introForward(final ActionMapping mapping);

    @Override
    public ActionForward listProcesses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return intro(mapping, form, request, response);
    }

    @Override
    public ActionForward listProcessAllowedActivities(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	setCandidacyProcessInformation(request, getProcess(request));
	return introForward(mapping);
    }

    @Override
    protected CandidacyProcess getProcess(HttpServletRequest request) {
	return (CandidacyProcess) super.getProcess(request);
    }

    protected void setCandidacyProcessInformation(final HttpServletRequest request, final CandidacyProcess process) {
	if (process != null) {
	    request.setAttribute("process", process);
	    request.setAttribute("processActivities", process.getAllowedActivities(AccessControl.getUserView()));
	    request.setAttribute("childProcesses", getChildProcesses(process, request));
	    request.setAttribute("canCreateChildProcess", canCreateProcess(getChildProcessType().getName()));
	    request.setAttribute("childProcessName", getChildProcessType().getSimpleName());
	    request.setAttribute("executionIntervalId", process.getCandidacyExecutionInterval().getIdInternal());
	    request.setAttribute("individualCandidaciesHashCodesNotBounded", getIndividualCandidacyHashCodesNotBounded());
	    request.setAttribute("hideCancelledCandidacies", getHideCancelledCandidaciesValue(request));
	}
	request.setAttribute("canCreateProcess", canCreateProcess(getProcessType().getName()));
	request.setAttribute("executionIntervals", ExecutionInterval
		.readExecutionIntervalsWithCandidacyPeriod(getCandidacyPeriodType()));
    }

    public static class HideCancelledCandidaciesBean implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Boolean value = Boolean.FALSE;

	public HideCancelledCandidaciesBean(Boolean value) {
	    this.value = value;
	}

	public HideCancelledCandidaciesBean() {
	}

	public Boolean getValue() {
	    return this.value;
	}

	public Boolean isValue() {
	    return this.value;
	}

	public void setValue(Boolean value) {
	    this.value = value;
	}

	public Boolean getOptionValue() {
	    return getValue();
	}

	public void setOptionValue(Boolean value) {
	    setValue(value);
	}
    }

    protected HideCancelledCandidaciesBean getHideCancelledCandidaciesValue(HttpServletRequest request) {
	Object value = this.getObjectFromViewState("hide.cancelled.candidacies");
	if (value == null) {
	    value = getFromRequest(request, "hideCancelledCandidacies") != null ? Boolean.valueOf((String) getFromRequest(
		    request, "hideCancelledCandidacies")) : null;
	}
	return new HideCancelledCandidaciesBean(value != null ? (Boolean) value : Boolean.FALSE);
    }

    protected List<IndividualCandidacyProcess> getChildProcesses(final CandidacyProcess process, HttpServletRequest request) {
	HideCancelledCandidaciesBean hideCancelledCandidacies = getHideCancelledCandidaciesValue(request);
	if (hideCancelledCandidacies.getValue()) {
	    return new ArrayList<IndividualCandidacyProcess>(CollectionUtils.select(process.getChildProcesses(), new Predicate() {

		@Override
		public boolean evaluate(Object arg0) {
		    return !((IndividualCandidacyProcess) arg0).isCandidacyCancelled();
		}

	    }));
	}

	return process.getChildProcesses();
    }

    private List<PublicCandidacyHashCode> getIndividualCandidacyHashCodesNotBounded() {
	List<PublicCandidacyHashCode> publicCandidacyHashCodeList = new ArrayList<PublicCandidacyHashCode>(CollectionUtils
		.select(RootDomainObject.readAllDomainObjects(PublicCandidacyHashCode.class), new Predicate() {

		    @Override
		    public boolean evaluate(Object arg0) {
			final PublicCandidacyHashCode hashCode = (PublicCandidacyHashCode) arg0;
			return hashCode.isFromDegreeOffice() && !hashCode.hasCandidacyProcess();
		    }
		}));

	return publicCandidacyHashCodeList;
    }

    abstract protected CandidacyProcess getCandidacyProcess(HttpServletRequest request, final ExecutionInterval executionInterval);

    static public class CandidacyProcessForm extends FenixActionForm {
	private Integer executionIntervalId;

	public Integer getExecutionIntervalId() {
	    return executionIntervalId;
	}

	public void setExecutionIntervalId(Integer executionIntervalId) {
	    this.executionIntervalId = executionIntervalId;
	}
    }

    /**
     * This bean is used to show summary about created registrations for
     * candidates
     */
    static public class CandidacyDegreeBean implements Serializable, Comparable<CandidacyDegreeBean> {

	private DomainReference<IndividualCandidacyPersonalDetails> details;
	private DomainReference<Degree> degree;
	private IndividualCandidacyState state;
	private boolean isRegistrationCreated;

	public IndividualCandidacyPersonalDetails getPersonalDetails() {
	    return (this.details != null) ? this.details.getObject() : null;
	}

	public void setPersonalDetails(IndividualCandidacyPersonalDetails details) {
	    this.details = (details != null) ? new DomainReference<IndividualCandidacyPersonalDetails>(details) : null;
	}

	public Degree getDegree() {
	    return (this.degree != null) ? this.degree.getObject() : null;
	}

	public void setDegree(Degree degree) {
	    this.degree = (degree != null) ? new DomainReference<Degree>(degree) : null;
	}

	public IndividualCandidacyState getState() {
	    return state;
	}

	public void setState(IndividualCandidacyState state) {
	    this.state = state;
	}

	public boolean isRegistrationCreated() {
	    return isRegistrationCreated;
	}

	public void setRegistrationCreated(boolean isRegistrationCreated) {
	    this.isRegistrationCreated = isRegistrationCreated;
	}

	public int compareTo(CandidacyDegreeBean other) {
	    return IndividualCandidacyPersonalDetails.COMPARATOR_BY_NAME_AND_ID.compare(getPersonalDetails(), other
		    .getPersonalDetails());
	}
    }

    @Override
    public ActionForward prepareCreateNewProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("candidacyProcessBean", new CandidacyProcessBean(ExecutionYear.readCurrentExecutionYear()));
	return mapping.findForward("prepare-create-new-process");
    }

    @Override
    public ActionForward createNewProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	try {
	    return super.createNewProcess(mapping, form, request, response);
	} catch (final DomainException e) {
	    addActionMessage(request, e.getMessage(), e.getArgs());
	    request.setAttribute("candidacyProcessBean", getRenderedObject("candidacyProcessBean"));
	    return mapping.findForward("prepare-create-new-process");
	}
    }

    public ActionForward createNewProcessInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("candidacyProcessBean", getRenderedObject("candidacyProcessBean"));
	return mapping.findForward("prepare-create-new-process");
    }

    public ActionForward prepareExecuteEditCandidacyPeriod(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	final CandidacyProcess process = getProcess(request);
	request.setAttribute("candidacyProcessBean", new CandidacyProcessBean(process));
	return mapping.findForward("prepare-edit-candidacy-period");
    }

    public ActionForward executeEditCandidacyPeriod(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	try {
	    executeActivity(getProcess(request), "EditCandidacyPeriod", getRenderedObject("candidacyProcessBean"));
	} catch (final DomainException e) {
	    addActionMessage(request, e.getMessage());
	    request.setAttribute("candidacyProcessBean", getRenderedObject("candidacyProcessBean"));
	    return mapping.findForward("prepare-edit-candidacy-period");
	}
	return listProcessAllowedActivities(mapping, actionForm, request, response);
    }

    public ActionForward executeEditCandidacyPeriodInvalid(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("candidacyProcessBean", getRenderedObject("candidacyProcessBean"));
	return mapping.findForward("prepare-edit-candidacy-period");
    }

    protected String getReportFilename() {
	final ResourceBundle bundle = ResourceBundle.getBundle("resources/ApplicationResources", Language.getLocale());
	return bundle.getString("label.candidacy." + getProcessType().getSimpleName() + ".report.filename") + "_"
		+ new LocalDate().toString("ddMMyyyy") + ".xls";
    }

    public ActionForward prepareExecuteCreateRegistrations(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("candidacyDegreeBeans", createCandidacyDegreeBeans(request));
	return mapping.findForward("create-registrations");
    }

    /**
     * Create list of CandidacyDegreeBeans with information related to accepted
     * candidacies
     */
    abstract protected List<CandidacyDegreeBean> createCandidacyDegreeBeans(final HttpServletRequest request);

    public ActionForward executeCreateRegistrations(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	try {
	    executeActivity(getProcess(request), "CreateRegistrations");
	} catch (final DomainException e) {
	    addActionMessage(request, e.getMessage(), e.getArgs());
	    request.setAttribute("candidacyDegreeBeans", createCandidacyDegreeBeans(request));
	    return mapping.findForward("create-registrations");
	}
	return listProcessAllowedActivities(mapping, actionForm, request, response);
    }

    public ActionForward prepareExecuteExportCandidacies(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws IOException {

	response.setContentType("application/vnd.ms-excel");
	response.setHeader("Content-disposition", "attachment; filename=" + getReportFilename());

	final ServletOutputStream writer = response.getOutputStream();
	writeCandidaciesReport(request, getProcess(request), writer);
	writer.flush();
	response.flushBuffer();
	return null;
    }

    private void writeCandidaciesReport(HttpServletRequest request, final CandidacyProcess process,
	    final ServletOutputStream writer) throws IOException {
	final ResourceBundle bundle = ResourceBundle.getBundle("resources/CandidateResources", Language.getLocale());
	final Spreadsheet spreadsheet = new Spreadsheet(bundle.getString("title.candidacies"), getCandidacyHeader());
	HideCancelledCandidaciesBean hideCancelledCandidacies = getHideCancelledCandidaciesValue(request);

	for (final IndividualCandidacyProcess individualProcess : process.getChildProcesses()) {
	    if (hideCancelledCandidacies.getValue() && individualProcess.isCandidacyCancelled())
		continue;
	    buildIndividualCandidacyReport(spreadsheet, individualProcess);
	}
	new SpreadsheetXLSExporter().exportToXLSSheet(spreadsheet, writer);
    }

    protected final String MESSAGE_YES = "message.yes";
    protected final String MESSAGE_NO = "message.no";
    protected static final DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyy/MM/dd");

    protected abstract Spreadsheet buildIndividualCandidacyReport(final Spreadsheet spreadsheet,
	    final IndividualCandidacyProcess individualCandidacyProcess);

    private List<Object> getCandidacyHeader() {
	final ResourceBundle bundle = ResourceBundle.getBundle("resources/CandidateResources", Language.getLocale());
	final List<Object> result = new ArrayList<Object>();

	result.add(bundle.getString("label.spreadsheet.processCode"));
	result.add(bundle.getString("label.spreadsheet.name"));
	result.add(bundle.getString("label.spreadsheet.identificationType"));
	result.add(bundle.getString("label.spreadsheet.identificationNumber"));
	result.add(bundle.getString("label.spreadsheet.nationality"));
	result.add(bundle.getString("label.spreadsheet.precedent.institution"));
	result.add(bundle.getString("label.spreadsheet.precedent.degree.designation"));
	result.add(bundle.getString("label.spreadsheet.precedent.degree.conclusion.date"));
	result.add(bundle.getString("label.spreadsheet.precedent.degree.conclusion.grade"));
	result.add(bundle.getString("label.spreadsheet.selected.degree"));
	result.add(bundle.getString("label.spreadsheet.state"));
	result.add(bundle.getString("label.spreadsheet.verified"));

	return result;
    }

    public static class ChooseDegreeBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DomainReference<Degree> degree;

	public ChooseDegreeBean() {

	}

	public Degree getDegree() {
	    return this.degree != null ? this.degree.getObject() : null;
	}

	public void setDegree(Degree degree) {
	    this.degree = degree != null ? new DomainReference<Degree>(degree) : null;
	}
    }

}
