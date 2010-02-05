package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.thesis;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.thesis.MakeThesisDocumentsAvailable;
import net.sourceforge.fenixedu.applicationTier.Servico.thesis.MakeThesisDocumentsUnavailable;
import net.sourceforge.fenixedu.dataTransferObject.VariantBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.ScientificCommission;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.ExecutionDegree.ThesisCreationPeriodFactoryExecutor;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.GroupUnion;
import net.sourceforge.fenixedu.domain.accessControl.ThesisFileReadersGroup;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.domain.thesis.ThesisEvaluationParticipant;
import net.sourceforge.fenixedu.domain.thesis.ThesisFile;
import net.sourceforge.fenixedu.domain.thesis.ThesisParticipationType;
import net.sourceforge.fenixedu.injectionCode.IGroup;
import net.sourceforge.fenixedu.presentationTier.Action.commons.AbstractManageThesisDA;
import net.sourceforge.fenixedu.presentationTier.Action.coordinator.thesis.ThesisPresentationState;
import net.sourceforge.fenixedu.presentationTier.Action.student.thesis.ThesisFileBean;
import net.sourceforge.fenixedu.presentationTier.renderers.providers.ExecutionDegreesWithDissertationByExecutionYearProvider;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.FileUtils;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

@Mapping(path = "/scientificCouncilManageThesis", module = "scientificCouncil")
@Forwards( { @Forward(name = "list-thesis", path = "/scientificCouncil/thesis/listThesis.jsp"),
	@Forward(name = "review-proposal", path = "/scientificCouncil/thesis/reviewProposal.jsp"),
	@Forward(name = "review-thesis", path = "/scientificCouncil/thesis/reviewThesis.jsp"),
	@Forward(name = "view-thesis", path = "/scientificCouncil/thesis/viewThesis.jsp"),
	@Forward(name = "list-scientific-comission", path = "/scientificCouncil/thesis/listScientificComission.jsp"),
	@Forward(name = "list-thesis-creation-periods", path = "/scientificCouncil/thesis/listThesisCreationPeriods.jsp"),
	@Forward(name = "viewOperationsThesis", path = "/student/thesis/viewOperationsThesis.jsp") })
public class ScientificCouncilManageThesisDA extends AbstractManageThesisDA {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final Thesis thesis = getThesis(request);
	request.setAttribute("thesis", thesis);

	if (thesis != null) {
	    final ThesisPresentationState thesisPresentationState = ThesisPresentationState.getThesisPresentationState(thesis);
	    request.setAttribute("thesisPresentationState", thesisPresentationState);
	    request.setAttribute("degreeID", thesis.getDegree().getIdInternal());
	    request.setAttribute("executionYearID", thesis.getExecutionYear().getIdInternal());
	}

	Degree degree = getDegree(request);
	ExecutionYear executionYear = getExecutionYear(request);

	setFilterContext(request, degree, executionYear);

	return super.execute(mapping, actionForm, request, response);
    }

    private void setFilterContext(HttpServletRequest request, Degree degree, ExecutionYear executionYear) {
	request.setAttribute("degree", degree);
	request.setAttribute("degreeId", degree == null ? "" : degree.getIdInternal());
	request.setAttribute("executionYear", executionYear);
	request.setAttribute("executionYearId", executionYear == null ? "" : executionYear.getIdInternal());
    }

    private Degree getDegree(HttpServletRequest request) {
	final Integer id = getIntegerFromRequest(request, "degreeID");
	if (id == null) {
	    return null;
	} else {
	    return RootDomainObject.getInstance().readDegreeByOID(id);
	}
    }

    private ExecutionYear getExecutionYear(HttpServletRequest request) {
	final Integer id = getIntegerFromRequest(request, "executionYearID");
	if (id == null) {
	    return null;
	} else {
	    return RootDomainObject.getInstance().readExecutionYearByOID(id);
	}
    }

    public ActionForward listThesis(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	ThesisContextBean bean = getContextBean(request);

	Degree degree = bean.getDegree();
	ExecutionYear executionYear = bean.getExecutionYear();

	setFilterContext(request, degree, executionYear);

	List<Thesis> theses = new ArrayList<Thesis>();

	theses.addAll(Thesis.getSubmittedThesis(degree, executionYear));
	theses.addAll(Thesis.getApprovedThesis(degree, executionYear));
	theses.addAll(Thesis.getConfirmedThesis(degree, executionYear));
	theses.addAll(Thesis.getEvaluatedThesis(degree, executionYear));

	request.setAttribute("contextBean", bean);
	request.setAttribute("theses", theses);

	return mapping.findForward("list-thesis");
    }

    public Integer getIntegerParameter(final HttpServletRequest request, final String paramName) {
	final String string = request.getParameter(paramName);
	return (string == null || string.isEmpty()) ? null : Integer.valueOf(string);
    }

    public ActionForward listScientificComission(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final Integer degreeId = getIntegerParameter(request, "degreeId");
	final Degree degree = degreeId == null ? null : rootDomainObject.readDegreeByOID(degreeId);
	request.setAttribute("degree", degree);

	final Integer executionYearId = getIntegerParameter(request, "executionYearId");
	final ExecutionYear executionYear = (ExecutionYear) (executionYearId == null ? null : rootDomainObject
		.readExecutionIntervalByOID(executionYearId));
	request.setAttribute("executionYear", executionYear);

	if (degree == null || executionYear == null) {
	    return listThesis(mapping, actionForm, request, response);
	}
	final Set<ExecutionDegree> executionDegrees = new HashSet<ExecutionDegree>();
	for (final DegreeCurricularPlan degreeCurricularPlan : degree.getDegreeCurricularPlansSet()) {
	    for (final ExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegreesSet()) {
		if (executionDegree.getExecutionYear() == executionYear) {
		    executionDegrees.add(executionDegree);
		}
	    }
	}
	request.setAttribute("executionDegrees", executionDegrees);
	request.setAttribute("usernameBean", new VariantBean());
	return mapping.findForward("list-scientific-comission");
    }

    public ActionForward removeScientificCommission(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	ScientificCommission scientificCommission = rootDomainObject.readScientificCommissionByOID(Integer.valueOf(request
		.getParameter("commissionID")));
	removeScientificCommissionFromExecutionDegree(scientificCommission, scientificCommission.getExecutionDegree());
	return listScientificComission(mapping, actionForm, request, response);
    }

    @Service
    public void removeScientificCommissionFromExecutionDegree(ScientificCommission scientificCommission,
	    ExecutionDegree executionDegree) {
	scientificCommission.delete();
    }

    public ActionForward addScientificCommission(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	VariantBean bean = (VariantBean) getRenderedObject("usernameChoice");
	if (bean != null) {
	    ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(Integer.valueOf(request
		    .getParameter("executionDegreeID")));
	    Employee employee = Employee.readByNumber(bean.getInteger());
	    if (employee == null || executionDegree.isPersonInScientificCommission(employee.getPerson())) {
		addActionMessage("addError", request, "error.scientificComission.employee");
	    } else {
		addScientificCommissionFromExecutionDegree(executionDegree, employee.getPerson());
		RenderUtils.invalidateViewState("usernameChoice");
	    }
	}
	return listScientificComission(mapping, actionForm, request, response);
    }

    @Service
    public void addScientificCommissionFromExecutionDegree(ExecutionDegree executionDegree, Person person) {
	new ScientificCommission(executionDegree, person);
    }

    private ThesisContextBean getContextBean(HttpServletRequest request) {
	ThesisContextBean bean = (ThesisContextBean) getRenderedObject("contextBean");
	RenderUtils.invalidateViewState("contextBean");

	if (bean != null) {
	    return bean;
	} else {
	    Degree degree = getDegree(request);
	    ExecutionYear executionYear = getExecutionYear(request);

	    if (executionYear == null) {
		executionYear = ExecutionYear.readCurrentExecutionYear();
	    }

	    return new ThesisContextBean(degree, executionYear);
	}
    }

    public ActionForward reviewProposal(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	return mapping.findForward("review-proposal");
    }

    public ActionForward approveProposal(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Thesis thesis = getThesis(request);

	if (thesis != null) {
	    executeService("ApproveThesisProposal", new Object[] { thesis });
	    addActionMessage("mail", request, "thesis.approved.mail.sent");
	}

	return listThesis(mapping, actionForm, request, response);
    }

    public ActionForward confirmRejectProposal(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	request.setAttribute("confirmReject", true);
	return reviewProposal(mapping, actionForm, request, response);
    }

    public ActionForward reviewThesis(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	return mapping.findForward("review-thesis");
    }

    public ActionForward confirmApprove(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	request.setAttribute("confirmApprove", true);
	return reviewThesis(mapping, actionForm, request, response);
    }

    public ActionForward confirmDisapprove(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	request.setAttribute("confirmDisapprove", true);
	return reviewThesis(mapping, actionForm, request, response);
    }

    public ActionForward approveThesis(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Thesis thesis = getThesis(request);

	if (thesis != null) {
	    try {
		executeService("ApproveThesisDiscussion", new Object[] { thesis });
		addActionMessage("mail", request, "thesis.evaluated.mail.sent");
	    } catch (DomainException e) {
		addActionMessage("error", request, e.getKey(), e.getArgs());
		return reviewThesis(mapping, actionForm, request, response);
	    }
	}

	return listThesis(mapping, actionForm, request, response);
    }

    public ActionForward viewThesis(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	setDocumentAvailability(request);
	return mapping.findForward("view-thesis");
    }

    private void setDocumentAvailability(final HttpServletRequest request) {
	final Thesis thesis = getThesis(request);
	final ThesisFile thesisFile = thesis.getDissertation();
	if (thesisFile != null) {
	    final Group group = thesisFile.getPermittedGroup();
	    if (containsThesisFileReadersGroup(group)) {
		request.setAttribute("containsThesisFileReadersGroup", Boolean.TRUE);
	    }
	}
    }

    private boolean containsThesisFileReadersGroup(final IGroup group) {
	if (group instanceof GroupUnion) {
	    final GroupUnion groupUnion = (GroupUnion) group;
	    for (IGroup child : groupUnion.getChildren()) {
		if (containsThesisFileReadersGroup(child)) {
		    return true;
		}
	    }
	} else if (group instanceof ThesisFileReadersGroup) {
	    return true;
	}
	return false;
    }

    public ActionForward showMakeDocumentUnavailablePage(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	request.setAttribute("showMakeDocumentUnavailablePage", Boolean.TRUE);
	return viewThesis(mapping, actionForm, request, response);
    }

    public ActionForward showMakeDocumentsAvailablePage(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	request.setAttribute("showMakeDocumentsAvailablePage", Boolean.TRUE);
	return viewThesis(mapping, actionForm, request, response);
    }

    public ActionForward showSubstituteDocumentsPage(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final Thesis thesis = getThesis(request);
	final ThesisFile thesisFile = thesis.getDissertation();
	final ThesisFileBean thesisFileBean = new ThesisFileBean();
	thesisFileBean.setTitle(thesisFile.getTitle());
	thesisFileBean.setSubTitle(thesisFile.getSubTitle());
	thesisFileBean.setLanguage(thesisFile.getLanguage());
	request.setAttribute("fileBean", thesisFileBean);
	request.setAttribute("showSubstituteDocumentsPage", Boolean.TRUE);
	return viewThesis(mapping, actionForm, request, response);
    }

    public ActionForward showSubstituteExtendedAbstractPage(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	final ThesisFileBean thesisFileBean = new ThesisFileBean();
	request.setAttribute("fileBean", thesisFileBean);
	request.setAttribute("showSubstituteExtendedAbstractPage", Boolean.TRUE);
	return viewThesis(mapping, actionForm, request, response);
    }

    public ActionForward makeDocumentUnavailablePage(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final Thesis thesis = getThesis(request);
	MakeThesisDocumentsUnavailable.run(thesis);
	return viewThesis(mapping, actionForm, request, response);
    }

    public ActionForward makeDocumentAvailablePage(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final Thesis thesis = getThesis(request);
	MakeThesisDocumentsAvailable.run(thesis);
	return viewThesis(mapping, actionForm, request, response);
    }

    public ActionForward substituteDocuments(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	ThesisFileBean bean = (ThesisFileBean) getRenderedObject();
	RenderUtils.invalidateViewState();

	if (bean != null && bean.getFile() != null) {
	    File temporaryFile = null;

	    try {
		temporaryFile = FileUtils.copyToTemporaryFile(bean.getFile());
		executeService("CreateThesisDissertationFile", new Object[] { getThesis(request), temporaryFile,
			bean.getSimpleFileName(), bean.getTitle(), bean.getSubTitle(), bean.getLanguage() });
	    } finally {
		if (temporaryFile != null) {
		    temporaryFile.delete();
		}
	    }
	}

	return viewThesis(mapping, actionForm, request, response);
    }

    public ActionForward substituteExtendedAbstract(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	ThesisFileBean bean = (ThesisFileBean) getRenderedObject();
	RenderUtils.invalidateViewState();

	if (bean != null && bean.getFile() != null) {
	    File temporaryFile = null;

	    try {
		temporaryFile = FileUtils.copyToTemporaryFile(bean.getFile());
		executeService("CreateThesisAbstractFile", new Object[] { getThesis(request), temporaryFile,
			bean.getSimpleFileName(), bean.getTitle(), bean.getSubTitle(), bean.getLanguage() });
	    } finally {
		if (temporaryFile != null) {
		    temporaryFile.delete();
		}
	    }
	}

	return viewThesis(mapping, actionForm, request, response);
    }

    public ActionForward listThesisCreationPeriods(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final ThesisCreationPeriodFactoryExecutor thesisCreationPeriodFactoryExecutor = getThesisCreationPeriodFactoryExecutor(request);

	return forwardToListThesisCreationPeriodsPage(mapping, request, thesisCreationPeriodFactoryExecutor);
    }

    private ActionForward forwardToListThesisCreationPeriodsPage(ActionMapping mapping, HttpServletRequest request,
	    final ThesisCreationPeriodFactoryExecutor thesisCreationPeriodFactoryExecutor) {
	if (thesisCreationPeriodFactoryExecutor.getExecutionDegree() == null) {
	    final ExecutionDegreesWithDissertationByExecutionYearProvider provider = new ExecutionDegreesWithDissertationByExecutionYearProvider();
	    request.setAttribute("executionDegrees", provider.provide(thesisCreationPeriodFactoryExecutor, null));
	}

	return mapping.findForward("list-thesis-creation-periods");
    }

    private ThesisCreationPeriodFactoryExecutor getThesisCreationPeriodFactoryExecutor(final HttpServletRequest request) {
	return getThesisCreationPeriodFactoryExecutor(request, true);
    }

    private ThesisCreationPeriodFactoryExecutor getThesisCreationPeriodFactoryExecutor(final HttpServletRequest request,
	    boolean invalidateViewState) {
	ThesisCreationPeriodFactoryExecutor thesisCreationPeriodFactoryExecutor = (ThesisCreationPeriodFactoryExecutor) getRenderedObject("thesisCreationPeriodFactoryExecutor");
	if (thesisCreationPeriodFactoryExecutor == null) {
	    thesisCreationPeriodFactoryExecutor = new ThesisCreationPeriodFactoryExecutor();

	    final String executionYearIdString = request.getParameter("executionYearId");
	    final ExecutionYear executionYear;
	    if (executionYearIdString == null) {
		if (thesisCreationPeriodFactoryExecutor.hasExecutionYear()) {
		    executionYear = thesisCreationPeriodFactoryExecutor.getExecutionYear();
		} else {
		    executionYear = ExecutionYear.readCurrentExecutionYear();
		}
	    } else {
		final Integer executionYearId = Integer.valueOf(executionYearIdString);
		executionYear = rootDomainObject.readExecutionYearByOID(executionYearId);
	    }
	    thesisCreationPeriodFactoryExecutor.setExecutionYear(executionYear);

	    final String executionDegreeIdString = request.getParameter("executionDegreeId");
	    if (executionDegreeIdString != null) {
		final Integer executionDegreeId = Integer.valueOf(executionDegreeIdString);
		final ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(executionDegreeId);
		thesisCreationPeriodFactoryExecutor.setExecutionDegree(executionDegree);
	    }
	}
	if (invalidateViewState) {
	    RenderUtils.invalidateViewState();
	}
	request.setAttribute("thesisCreationPeriodFactoryExecutor", thesisCreationPeriodFactoryExecutor);
	return thesisCreationPeriodFactoryExecutor;
    }

    public ActionForward prepareDefineCreationPeriods(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final ThesisCreationPeriodFactoryExecutor thesisCreationPeriodFactoryExecutor = getThesisCreationPeriodFactoryExecutor(request);

	final ExecutionDegree executionDegree = thesisCreationPeriodFactoryExecutor.getExecutionDegree();
	if (executionDegree == null) {
	    final ExecutionYear executionYear = thesisCreationPeriodFactoryExecutor.getExecutionYear();
	    if (executionYear != null) {
		for (final ExecutionDegree otherExecutionDegree : executionYear.getExecutionDegreesSet()) {
		    final YearMonthDay beginThesisCreationPeriod = otherExecutionDegree.getBeginThesisCreationPeriod();
		    final YearMonthDay endThesisCreationPeriod = otherExecutionDegree.getEndThesisCreationPeriod();
		    if (beginThesisCreationPeriod != null) {
			thesisCreationPeriodFactoryExecutor.setBeginThesisCreationPeriod(beginThesisCreationPeriod);
		    }
		    if (endThesisCreationPeriod != null) {
			thesisCreationPeriodFactoryExecutor.setEndThesisCreationPeriod(endThesisCreationPeriod);
		    }
		    if (thesisCreationPeriodFactoryExecutor.getBeginThesisCreationPeriod() != null
			    && thesisCreationPeriodFactoryExecutor.getEndThesisCreationPeriod() != null) {
			break;
		    }
		}
	    }
	} else {
	    thesisCreationPeriodFactoryExecutor.setBeginThesisCreationPeriod(executionDegree.getBeginThesisCreationPeriod());
	    thesisCreationPeriodFactoryExecutor.setEndThesisCreationPeriod(executionDegree.getEndThesisCreationPeriod());
	}

	request.setAttribute("prepareDefineCreationPeriods", Boolean.TRUE);

	return mapping.findForward("list-thesis-creation-periods");
    }

    public ActionForward defineCreationPeriods(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final ThesisCreationPeriodFactoryExecutor thesisCreationPeriodFactoryExecutor = getThesisCreationPeriodFactoryExecutor(request);
	executeFactoryMethod(thesisCreationPeriodFactoryExecutor);
	thesisCreationPeriodFactoryExecutor.setExecutionDegree(null);
	return forwardToListThesisCreationPeriodsPage(mapping, request, thesisCreationPeriodFactoryExecutor);
    }

    public ActionForward defineCreationPeriodsInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final ThesisCreationPeriodFactoryExecutor thesisCreationPeriodFactoryExecutor = getThesisCreationPeriodFactoryExecutor(
		request, false);
	request.setAttribute("prepareDefineCreationPeriods", Boolean.TRUE);
	return forwardToListThesisCreationPeriodsPage(mapping, request, thesisCreationPeriodFactoryExecutor);
    }

    public ActionForward downloadDissertationsList(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final String executionYearIdString = request.getParameter("executionYearId");
	final Integer executionYearId = executionYearIdString == null ? null : Integer.valueOf(executionYearIdString);
	final ExecutionYear executionYear = rootDomainObject.readExecutionYearByOID(executionYearId);

	response.setContentType("application/vnd.ms-excel");
	response.setHeader("Content-disposition", "attachment; filename=dissertacoes" + executionYear.getYear().replace("/", "")
		+ ".xls");
	ServletOutputStream writer = response.getOutputStream();

	exportDissertations(writer, executionYear);

	writer.flush();
	response.flushBuffer();
	return null;
    }

    private void exportDissertations(final ServletOutputStream writer, final ExecutionYear executionYear) throws IOException {
	final Spreadsheet spreadsheet = new Spreadsheet("Dissertacoes " + executionYear.getYear().replace("/", ""));
	spreadsheet.setHeader("Numero aluno");
	spreadsheet.setHeader("Nome aluno");
	spreadsheet.setHeader("Tipo Curso");
	spreadsheet.setHeader("Curso");
	spreadsheet.setHeader("Sigla Curso");
	spreadsheet.setHeader("Tese");
	spreadsheet.setHeader("Estado da tese");
	spreadsheet.setHeader("Numero Orientador");
	spreadsheet.setHeader("Nome Orientador");
	spreadsheet.setHeader("Affiliacao Orientador");
	spreadsheet.setHeader("Distribuicao Creditos Orientador");
	spreadsheet.setHeader("Numero Corientador");
	spreadsheet.setHeader("Nome Corientador");
	spreadsheet.setHeader("Affiliacao Corientador");
	spreadsheet.setHeader("Distribuicao Creditos Corientador");
	spreadsheet.setHeader("Numero Presidente");
	spreadsheet.setHeader("Nome Presidente");
	spreadsheet.setHeader("Affiliacao Presidente");
	spreadsheet.setHeader("Nota Disserta��o");

	for (final Thesis thesis : rootDomainObject.getThesesSet()) {
	    final Enrolment enrolment = thesis.getEnrolment();
	    final ExecutionSemester executionSemester = enrolment.getExecutionPeriod();
	    if (executionSemester.getExecutionYear() == executionYear) {
		final ThesisPresentationState thesisPresentationState = ThesisPresentationState
			.getThesisPresentationState(thesis);

		final Degree degree = enrolment.getStudentCurricularPlan().getDegree();
		final DegreeType degreeType = degree.getDegreeType();

		final Row row = spreadsheet.addRow();
		row.setCell(thesis.getStudent().getNumber().toString());
		row.setCell(thesis.getStudent().getPerson().getName());
		row.setCell(degreeType.getLocalizedName());
		row.setCell(degree.getPresentationName(executionYear));
		row.setCell(degree.getSigla());
		row.setCell(thesis.getTitle().getContent());
		row.setCell(thesisPresentationState.getName());

		addTeacherRows(thesis, row, ThesisParticipationType.ORIENTATOR);
		addTeacherRows(thesis, row, ThesisParticipationType.COORIENTATOR);
		addTeacherRows(thesis, row, ThesisParticipationType.PRESIDENT);

		row.setCell(thesis.getMark());
	    }
	}
	spreadsheet.exportToXLSSheet(writer);
    }

    protected void addTeacherRows(final Thesis thesis, final Row row, final ThesisParticipationType thesisParticipationType) {
	final StringBuilder numbers = new StringBuilder();
	final StringBuilder names = new StringBuilder();
	final StringBuilder oasb = new StringBuilder();
	final StringBuilder odsb = new StringBuilder();
	for (final ThesisEvaluationParticipant thesisEvaluationParticipant : thesis.getAllParticipants(thesisParticipationType)) {
	    if (numbers.length() > 0) {
		numbers.append(" ");
	    }
	    if (thesisEvaluationParticipant.hasPerson()) {
		final Person person = thesisEvaluationParticipant.getPerson();
		if (person.hasTeacher()) {
		    final Teacher teacher = person.getTeacher();
		    numbers.append(teacher.getTeacherNumber().toString());
		}
	    }

	    if (names.length() > 0) {
		names.append(" ");
	    }
	    names.append(thesisEvaluationParticipant.getPersonName());

	    if (oasb.length() > 0) {
		oasb.append(" ");
	    }
	    final String affiliation = thesisEvaluationParticipant.getAffiliation();
	    if (affiliation != null) {
		oasb.append(affiliation);
	    } else {
		oasb.append("--");
	    }

	    if (thesisParticipationType == ThesisParticipationType.ORIENTATOR
		    || thesisParticipationType == ThesisParticipationType.COORIENTATOR) {
		if (odsb.length() > 0) {
		    odsb.append(" ");
		}
		final double credistDistribution = thesisEvaluationParticipant.getCreditsDistribution();
		odsb.append(Double.toString(credistDistribution));
	    }
	}
	row.setCell(numbers.toString());
	row.setCell(names.toString());
	row.setCell(oasb.toString());
	if (thesisParticipationType == ThesisParticipationType.ORIENTATOR
		|| thesisParticipationType == ThesisParticipationType.COORIENTATOR) {
	    row.setCell(odsb.toString());
	}
    }
}