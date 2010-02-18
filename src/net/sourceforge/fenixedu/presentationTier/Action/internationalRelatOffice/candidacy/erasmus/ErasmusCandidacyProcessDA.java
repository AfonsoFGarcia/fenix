package net.sourceforge.fenixedu.presentationTier.Action.internationalRelatOffice.candidacy.erasmus;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusIndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.period.ErasmusCandidacyPeriod;
import net.sourceforge.fenixedu.presentationTier.Action.candidacy.CandidacyProcessDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.servlets.filters.I18NFilter;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;

@Mapping(path = "/caseHandlingErasmusCandidacyProcess", module = "internationalRelatOffice", formBeanClass = ErasmusCandidacyProcessDA.ErasmusCandidacyProcessForm.class)
@Forwards( { @Forward(name = "intro", path = "/candidacy/erasmus/mainCandidacyProcess.jsp"),
	@Forward(name = "prepare-create-new-process", path = "/candidacy/createCandidacyPeriod.jsp"),
	@Forward(name = "prepare-edit-candidacy-period", path = "/candidacy/editCandidacyPeriod.jsp") })
public class ErasmusCandidacyProcessDA extends CandidacyProcessDA {

    static public class ErasmusCandidacyProcessForm extends CandidacyProcessForm {
	private Integer selectedProcessId;

	public Integer getSelectedProcessId() {
	    return selectedProcessId;
	}

	public void setSelectedProcessId(Integer selectedProcessId) {
	    this.selectedProcessId = selectedProcessId;
	}
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	setChooseDegreeBean(request);
	I18NFilter.setLocale(request, request.getSession(true), Locale.ENGLISH);
	return super.execute(mapping, actionForm, request, response);
    }

    private void setChooseDegreeBean(HttpServletRequest request) {
	ChooseDegreeBean chooseDegreeBean = (ChooseDegreeBean) getObjectFromViewState("choose.degree.bean");

	if (chooseDegreeBean == null) {
	    chooseDegreeBean = new ChooseDegreeBean();
	}

	request.setAttribute("chooseDegreeBean", chooseDegreeBean);
    }

    private ChooseDegreeBean getChooseDegreeBean(HttpServletRequest request) {
	return (ChooseDegreeBean) request.getAttribute("chooseDegreeBean");
    }

    @Override
    protected Spreadsheet buildIndividualCandidacyReport(Spreadsheet spreadsheet,
	    IndividualCandidacyProcess individualCandidacyProcess) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    protected List<CandidacyDegreeBean> createCandidacyDegreeBeans(HttpServletRequest request) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    protected Class getCandidacyPeriodType() {
	return ErasmusCandidacyPeriod.class;
    }

    @Override
    protected Class getChildProcessType() {
	return ErasmusIndividualCandidacyProcess.class;
    }

    private List<ErasmusCandidacyProcess> getCandidacyProcesses(final ExecutionInterval executionInterval) {
	final List<ErasmusCandidacyProcess> result = new ArrayList<ErasmusCandidacyProcess>();
	for (final ErasmusCandidacyPeriod period : executionInterval.getErasmusCandidacyPeriods()) {
	    result.add(period.getErasmusCandidacyProcess());
	}
	return result;
    }

    private List<ExecutionInterval> getExecutionIntervalsWithCandidacyPeriod() {
	return ExecutionInterval.readExecutionIntervalsWithCandidacyPeriod(getCandidacyPeriodType());
    }

    @Override
    protected ErasmusCandidacyProcess getCandidacyProcess(final HttpServletRequest request,
	    final ExecutionInterval executionInterval) {

	final Integer selectedProcessId = getIntegerFromRequest(request, "selectedProcessId");
	if (selectedProcessId != null) {
	    for (final ErasmusCandidacyPeriod candidacyPeriod : executionInterval.getErasmusCandidacyPeriods()) {
		if (candidacyPeriod.getErasmusCandidacyProcess().getIdInternal().equals(selectedProcessId)) {
		    return candidacyPeriod.getErasmusCandidacyProcess();
		}
	    }
	}
	return null;
    }

    @Override
    protected Class getProcessType() {
	return ErasmusCandidacyProcess.class;
    }

    private void setCandidacyProcessInformation(final ActionForm actionForm, final ErasmusCandidacyProcess process) {
	final ErasmusCandidacyProcessForm form = (ErasmusCandidacyProcessForm) actionForm;
	form.setSelectedProcessId(process.getIdInternal());
	form.setExecutionIntervalId(process.getCandidacyExecutionInterval().getIdInternal());
    }

    @Override
    protected ErasmusCandidacyProcess getProcess(HttpServletRequest request) {
	return (ErasmusCandidacyProcess) super.getProcess(request);
    }

    @Override
    protected List<IndividualCandidacyProcess> getChildProcesses(final CandidacyProcess process, HttpServletRequest request) {
	List<IndividualCandidacyProcess> processes = process.getChildProcesses();
	List<IndividualCandidacyProcess> selectedDegreesIndividualCandidacyProcesses = new ArrayList<IndividualCandidacyProcess>();
	Degree selectedDegree = getChooseDegreeBean(request).getDegree();

	for (IndividualCandidacyProcess child : processes) {
	    if ((selectedDegree == null)
		    || ((ErasmusIndividualCandidacyProcess) child).getCandidacy().getSelectedDegree() == selectedDegree) {
		selectedDegreesIndividualCandidacyProcesses.add(child);
	    }
	}

	return selectedDegreesIndividualCandidacyProcesses;
    }

    @Override
    protected ActionForward introForward(ActionMapping mapping) {
	return mapping.findForward("intro");
    }

    @Override
    public ActionForward listProcessAllowedActivities(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	setCandidacyProcessInformation(request, getProcess(request));
	setCandidacyProcessInformation(form, getProcess(request));
	request.setAttribute("candidacyProcesses", getCandidacyProcesses(getProcess(request).getCandidacyExecutionInterval()));
	return introForward(mapping);
    }

    @Override
    protected void setStartInformation(ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
	if (!hasExecutionInterval(request)) {
	    final List<ExecutionInterval> executionIntervals = getExecutionIntervalsWithCandidacyPeriod();

	    if (executionIntervals.size() == 1) {
		final ExecutionInterval executionInterval = executionIntervals.get(0);
		final List<ErasmusCandidacyProcess> candidacyProcesses = getCandidacyProcesses(executionInterval);

		if (candidacyProcesses.size() == 1) {
		    setCandidacyProcessInformation(request, candidacyProcesses.get(0));
		    setCandidacyProcessInformation(actionForm, getProcess(request));
		    request.setAttribute("candidacyProcesses", candidacyProcesses);
		    return;
		}
	    }

	    request.setAttribute("canCreateProcess", canCreateProcess(getProcessType().getName()));
	    request.setAttribute("executionIntervals", executionIntervals);

	} else {
	    final ExecutionInterval executionInterval = getExecutionInterval(request);
	    final ErasmusCandidacyProcess candidacyProcess = getCandidacyProcess(request, executionInterval);

	    if (candidacyProcess != null) {
		setCandidacyProcessInformation(request, candidacyProcess);
		setCandidacyProcessInformation(actionForm, getProcess(request));
	    } else {
		final List<ErasmusCandidacyProcess> candidacyProcesses = getCandidacyProcesses(executionInterval);

		if (candidacyProcesses.size() == 1) {
		    setCandidacyProcessInformation(request, candidacyProcesses.get(0));
		    setCandidacyProcessInformation(actionForm, getProcess(request));
		    request.setAttribute("candidacyProcesses", candidacyProcesses);
		    return;
		}

		request.setAttribute("canCreateProcess", canCreateProcess(getProcessType().getName()));
		request.setAttribute("executionIntervals", getExecutionIntervalsWithCandidacyPeriod());
	    }
	    request.setAttribute("candidacyProcesses", getCandidacyProcesses(executionInterval));
	}
    }

}
