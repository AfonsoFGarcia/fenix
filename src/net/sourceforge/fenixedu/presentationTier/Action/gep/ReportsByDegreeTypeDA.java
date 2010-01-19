package net.sourceforge.fenixedu.presentationTier.Action.gep;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.QueueJob;
import net.sourceforge.fenixedu.domain.ReportFileFactory;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.reports.CourseLoadAndResponsiblesReportFile;
import net.sourceforge.fenixedu.domain.reports.CourseLoadReportFile;
import net.sourceforge.fenixedu.domain.reports.DissertationsProposalsReportFile;
import net.sourceforge.fenixedu.domain.reports.DissertationsWithExternalAffiliationsReportFile;
import net.sourceforge.fenixedu.domain.reports.EctsLabelCurricularCourseReportFile;
import net.sourceforge.fenixedu.domain.reports.EctsLabelDegreeReportFile;
import net.sourceforge.fenixedu.domain.reports.EtiReportFile;
import net.sourceforge.fenixedu.domain.reports.EurAceReportFile;
import net.sourceforge.fenixedu.domain.reports.FlunkedReportFile;
import net.sourceforge.fenixedu.domain.reports.GepReportFile;
import net.sourceforge.fenixedu.domain.reports.GraduationReportFile;
import net.sourceforge.fenixedu.domain.reports.RaidesDfaReportFile;
import net.sourceforge.fenixedu.domain.reports.RaidesGraduationReportFile;
import net.sourceforge.fenixedu.domain.reports.RaidesPhdReportFile;
import net.sourceforge.fenixedu.domain.reports.RegistrationReportFile;
import net.sourceforge.fenixedu.domain.reports.StatusAndApprovalReportFile;
import net.sourceforge.fenixedu.domain.reports.TeachersByShiftReportFile;
import net.sourceforge.fenixedu.domain.reports.TeachersListFromGiafReportFile;
import net.sourceforge.fenixedu.domain.reports.TeachersListReportFile;
import net.sourceforge.fenixedu.domain.reports.TimetablesReportFile;
import net.sourceforge.fenixedu.domain.reports.TutorshipProgramReportFile;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.StringUtils;

import org.apache.commons.collections.Predicate;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class ReportsByDegreeTypeDA extends FenixDispatchAction {

    public static class ReportBean implements Serializable {
	private DegreeType degreeType;

	private DomainReference<ExecutionYear> executionYearReference;

	public DegreeType getDegreeType() {
	    return degreeType;
	}

	public void setDegreeType(DegreeType degreeType) {
	    this.degreeType = degreeType;
	}

	public ExecutionYear getExecutionYear() {
	    return executionYearReference == null ? null : executionYearReference.getObject();
	}

	public Integer getExecutionYearOID() {
	    return getExecutionYear() == null ? null : getExecutionYear().getIdInternal();
	}

	public void setExecutionYear(final ExecutionYear executionYear) {
	    executionYearReference = executionYear == null ? null : new DomainReference<ExecutionYear>(executionYear);
	}

	public ExecutionYear getExecutionYearFourYearsBack() {
	    final ExecutionYear executionYear = getExecutionYear();
	    return executionYear == null ? null : ReportsByDegreeTypeDA.getExecutionYearFourYearsBack(executionYear);
	}
    }

    public static ExecutionYear getExecutionYearFourYearsBack(final ExecutionYear executionYear) {
	ExecutionYear executionYearFourYearsBack = executionYear;
	if (executionYear != null) {
	    for (int i = 5; i > 1; i--) {
		final ExecutionYear previousExecutionYear = executionYearFourYearsBack.getPreviousExecutionYear();
		if (previousExecutionYear != null) {
		    executionYearFourYearsBack = previousExecutionYear;
		}
	    }
	}
	return executionYearFourYearsBack;
    }

    public List<QueueJob> getLatestJobs() {
	return QueueJob.getAllJobsForClassOrSubClass(GepReportFile.class, 5);
    }

    @SuppressWarnings("unused")
    public ActionForward selectDegreeType(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	ReportBean reportBean = (ReportBean) getRenderedObject();

	if (reportBean == null) {
	    final DegreeType degreeType = getDegreeType(request);
	    final ExecutionYear executionYear = getExecutionYear(request);
	    reportBean = new ReportBean();
	    reportBean.setDegreeType(degreeType);
	    reportBean.setExecutionYear(executionYear);
	}
	RenderUtils.invalidateViewState();

	request.setAttribute("reportBean", reportBean);
	request.setAttribute("queueJobList", getLatestJobs());
	return mapping.findForward("selectDegreeType");
    }

    private DegreeType getDegreeType(final HttpServletRequest httpServletRequest) {
	final String degreeTypeString = httpServletRequest.getParameter("degreeType");
	return StringUtils.isEmpty(degreeTypeString) ? null : DegreeType.valueOf(degreeTypeString);
    }

    private ExecutionYear getExecutionYear(final HttpServletRequest httpServletRequest) {
	final String OIDString = httpServletRequest.getParameter("executionYearID");
	return StringUtils.isEmpty(OIDString) ? null : rootDomainObject.readExecutionYearByOID(Integer.valueOf(OIDString));
    }

    private String getFormat(final HttpServletRequest httpServletRequest) {
	return httpServletRequest.getParameter("format");
    }

    @SuppressWarnings("unused")
    public ActionForward downloadEurAce(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	final DegreeType degreeType = getDegreeType(request);
	final ExecutionYear executionYear = getExecutionYear(request);
	final String format = getFormat(request);

	prepareNewJobResponse(request, ReportFileFactory.createEurAceReportFile(format, degreeType, executionYear));

	return mapping.findForward("selectDegreeType");
    }

    @SuppressWarnings("unused")
    public ActionForward downloadEctsLabelForCurricularCourses(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws IOException {

	final DegreeType degreeType = getDegreeType(request);
	final ExecutionYear executionYear = getExecutionYear(request);
	final String format = getFormat(request);

	prepareNewJobResponse(request, ReportFileFactory.createEctsLabelCurricularCourseReportFile(format, degreeType,
		executionYear));

	return mapping.findForward("selectDegreeType");
    }

    @SuppressWarnings("unused")
    public ActionForward downloadEctsLabelForDegrees(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	final DegreeType degreeType = getDegreeType(request);
	final ExecutionYear executionYear = getExecutionYear(request);
	final String format = getFormat(request);

	prepareNewJobResponse(request, ReportFileFactory.createEctsLabelDegreeReportFile(format, degreeType, executionYear));

	return mapping.findForward("selectDegreeType");
    }

    @SuppressWarnings("unused")
    public ActionForward downloadStatusAndAproval(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	final DegreeType degreeType = getDegreeType(request);
	final ExecutionYear executionYear = getExecutionYear(request);
	final String format = getFormat(request);

	prepareNewJobResponse(request, ReportFileFactory.createStatusAndApprovalReportFile(format, degreeType, executionYear));

	return mapping.findForward("selectDegreeType");
    }

    @SuppressWarnings("unused")
    public ActionForward downloadEti(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	final DegreeType degreeType = getDegreeType(request);
	final ExecutionYear executionYear = getExecutionYear(request);
	final String format = getFormat(request);

	prepareNewJobResponse(request, ReportFileFactory.createEtiReportFile(format, degreeType, executionYear));

	return mapping.findForward("selectDegreeType");
    }

    @SuppressWarnings("unused")
    public ActionForward downloadCourseLoadAndResponsibles(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws IOException {
	final DegreeType degreeType = getDegreeType(request);
	final ExecutionYear executionYear = getExecutionYear(request);
	final String format = getFormat(request);

	prepareNewJobResponse(request, ReportFileFactory.createCourseLoadAndResponsiblesReportFile(format, degreeType,
		executionYear));

	return mapping.findForward("selectDegreeType");
    }

    @SuppressWarnings("unused")
    public ActionForward downloadRegistrations(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	final DegreeType degreeType = getDegreeType(request);
	final ExecutionYear executionYear = getExecutionYear(request);
	final String format = getFormat(request);

	prepareNewJobResponse(request, ReportFileFactory.createRegistrationReportFile(format, degreeType, executionYear));

	return mapping.findForward("selectDegreeType");
    }

    @SuppressWarnings("unused")
    public ActionForward downloadFlunked(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	final DegreeType degreeType = getDegreeType(request);
	final ExecutionYear executionYear = getExecutionYear(request);
	final String format = getFormat(request);

	prepareNewJobResponse(request, ReportFileFactory.createFlunkedReportFile(format, degreeType, executionYear));

	return mapping.findForward("selectDegreeType");
    }

    @SuppressWarnings("unused")
    public ActionForward downloadTeachersListFromAplica(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	final DegreeType degreeType = getDegreeType(request);
	final ExecutionYear executionYear = getExecutionYear(request);
	final String format = getFormat(request);

	prepareNewJobResponse(request, ReportFileFactory.createTeachersListReportFile(format, degreeType, executionYear));

	return mapping.findForward("selectDegreeType");
    }

    @SuppressWarnings("unused")
    public ActionForward downloadTeachersListFromGiaf(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	final DegreeType degreeType = getDegreeType(request);
	final ExecutionYear executionYear = getExecutionYear(request);
	final String format = getFormat(request);

	prepareNewJobResponse(request, ReportFileFactory.createTeachersListFromGiafReportFile(format, degreeType, executionYear));

	return mapping.findForward("selectDegreeType");
    }

    @SuppressWarnings("unused")
    public ActionForward downloadTimetables(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	final DegreeType degreeType = getDegreeType(request);
	final ExecutionYear executionYear = getExecutionYear(request);
	final String format = getFormat(request);

	prepareNewJobResponse(request, ReportFileFactory.createTimetablesReportFile(format, degreeType, executionYear));

	return mapping.findForward("selectDegreeType");
    }

    @SuppressWarnings("unused")
    public ActionForward downloadDissertationsProposals(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	final DegreeType degreeType = getDegreeType(request);
	final ExecutionYear executionYear = getExecutionYear(request);
	final String format = getFormat(request);

	prepareNewJobResponse(request, ReportFileFactory
		.createDissertationsProposalsReportFile(format, degreeType, executionYear));

	return mapping.findForward("selectDegreeType");
    }

    @SuppressWarnings("unused")
    public ActionForward downloadDissertationsWithExternalAffiliations(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws IOException {
	final DegreeType degreeType = getDegreeType(request);
	final ExecutionYear executionYear = getExecutionYear(request);
	final String format = getFormat(request);

	prepareNewJobResponse(request, ReportFileFactory.createDissertationsWithExternalAffiliationsReportFile(format,
		degreeType, executionYear));

	return mapping.findForward("selectDegreeType");
    }

    @SuppressWarnings("unused")
    public ActionForward downloadGraduations(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	final DegreeType degreeType = getDegreeType(request);
	final ExecutionYear executionYear = getExecutionYear(request);
	final String format = getFormat(request);

	prepareNewJobResponse(request, ReportFileFactory.createGraduationReportFile(format, degreeType, executionYear));

	return mapping.findForward("selectDegreeType");
    }

    @SuppressWarnings("unused")
    public ActionForward downloadTeachersByShift(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	final DegreeType degreeType = getDegreeType(request);
	final ExecutionYear executionYear = getExecutionYear(request);
	final String format = getFormat(request);

	prepareNewJobResponse(request, ReportFileFactory.createTeachersByShiftReportFile(format, degreeType, executionYear));

	return mapping.findForward("selectDegreeType");
    }

    @SuppressWarnings("unused")
    public ActionForward downloadCourseLoads(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	final DegreeType degreeType = getDegreeType(request);
	final ExecutionYear executionYear = getExecutionYear(request);
	final String format = getFormat(request);

	prepareNewJobResponse(request, ReportFileFactory.createCourseLoadReportFile(format, degreeType, executionYear));

	return mapping.findForward("selectDegreeType");
    }

    @SuppressWarnings("unused")
    public ActionForward downloadTutorshipProgram(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	final DegreeType degreeType = getDegreeType(request);
	final ExecutionYear executionYear = getExecutionYear(request);
	final String format = getFormat(request);

	prepareNewJobResponse(request, ReportFileFactory.createTutorshipProgramReportFile(format, degreeType, executionYear));

	return mapping.findForward("selectDegreeType");
    }

    public ActionForward downloadRaidesGraduation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	prepareNewJobResponse(request, ReportFileFactory.createRaidesGraduationReportFile(getFormat(request),
		getDegreeType(request), getExecutionYear(request)));
	return mapping.findForward("selectDegreeType");
    }

    public ActionForward downloadRaidesDfa(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	prepareNewJobResponse(request, ReportFileFactory.createRaidesDfaReportFile(getFormat(request), getDegreeType(request),
		getExecutionYear(request)));
	return mapping.findForward("selectDegreeType");
    }

    public ActionForward downloadRaidesPhd(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	prepareNewJobResponse(request, ReportFileFactory.createRaidesPhdReportFile(getFormat(request), getDegreeType(request),
		getExecutionYear(request)));
	return mapping.findForward("selectDegreeType");
    }

    private void prepareNewJobResponse(HttpServletRequest request, GepReportFile job) {

	ReportBean reportBean = (ReportBean) getRenderedObject();
	if (reportBean == null) {
	    reportBean = new ReportBean();
	    reportBean.setDegreeType(job.getDegreeType());
	    reportBean.setExecutionYear(job.getExecutionYear());
	}
	RenderUtils.invalidateViewState();

	request.setAttribute("job", job);
	request.setAttribute("reportBean", reportBean);
	request.setAttribute("queueJobList", getLatestJobs());
    }

    public Class getClassForParameter(String type) {
	int i = Integer.valueOf(type);
	switch (i) {
	case 1:
	    return EurAceReportFile.class;
	case 2:
	    return EctsLabelDegreeReportFile.class;
	case 3:
	    return EctsLabelCurricularCourseReportFile.class;
	case 4:
	    return StatusAndApprovalReportFile.class;
	case 5:
	    return EtiReportFile.class;
	case 6:
	    return RegistrationReportFile.class;
	case 7:
	    return FlunkedReportFile.class;
	case 8:
	    return TeachersByShiftReportFile.class;
	case 9:
	    return CourseLoadReportFile.class;
	case 10:
	    return GraduationReportFile.class;
	case 11:
	    return DissertationsWithExternalAffiliationsReportFile.class;
	case 12:
	    return DissertationsProposalsReportFile.class;
	case 13:
	    return RaidesGraduationReportFile.class;
	case 14:
	    return RaidesDfaReportFile.class;
	case 15:
	    return RaidesPhdReportFile.class;
	case 16:
	    return TutorshipProgramReportFile.class;
	case 17:
	    return TeachersListReportFile.class;
	case 18:
	    return TeachersListFromGiafReportFile.class;
	case 19:
	    return CourseLoadAndResponsiblesReportFile.class;
	case 20:
	    return TimetablesReportFile.class;
	default:
	    return null;
	}
    }

    public static class FindSelectedGepReports implements Predicate {

	ExecutionYear executionYear;

	DegreeType degreeType;

	Class reportClass;

	int elements = 0;

	public FindSelectedGepReports(ExecutionYear executionYear, DegreeType degreeType, Class reportClass) {
	    this.executionYear = executionYear;
	    this.degreeType = degreeType;
	    this.reportClass = reportClass;
	}

	public boolean evaluate(Object object) {
	    QueueJob queueJob = (QueueJob) object;
	    try {
		GepReportFile gepReportFile = (GepReportFile) queueJob;
		if (gepReportFile.getClass() == this.reportClass) {
		    if (this.executionYear == gepReportFile.getExecutionYear()
			    && this.degreeType == gepReportFile.getDegreeType() && elements < 5) {
			elements++;
			return true;
		    } else {
			return false;
		    }
		} else {
		    return false;
		}
	    } catch (ClassCastException E) {
		return false;
	    }

	}
    }

    public ActionForward viewReports(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	final String type = request.getParameter("type");

	Class reportClass = getClassForParameter(type);
	final DegreeType degreeType = getDegreeType(request);
	final ExecutionYear executionYear = getExecutionYear(request);
	Predicate predicate = new FindSelectedGepReports(executionYear, degreeType, reportClass);

	List<GepReportFile> selectedJobs = (List<GepReportFile>) org.apache.commons.collections.CollectionUtils.select(
		rootDomainObject.getQueueJob(), predicate);
	String reportName = "";
	if (selectedJobs.size() > 0) {
	    reportName = selectedJobs.get(0).getJobName();
	}

	request.setAttribute("degreeType", degreeType);
	request.setAttribute("executionYear", executionYear);
	request.setAttribute("list", reportName);
	request.setAttribute("queueJobList", selectedJobs);

	return mapping.findForward("viewReports");
    }

}
