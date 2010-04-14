package net.sourceforge.fenixedu.presentationTier.backBeans.departmentMember;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.ServletOutputStream;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadCurrentExecutionPeriod;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadNotClosedExecutionYears;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.department.CompetenceCourseStatisticsDTO;
import net.sourceforge.fenixedu.dataTransferObject.department.DegreeCourseStatisticsDTO;
import net.sourceforge.fenixedu.dataTransferObject.department.ExecutionCourseStatisticsDTO;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.studentCurriculum.BranchCurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;

import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

/**
 * 
 * @author pcma
 * 
 */
public class CourseStatistics extends FenixBackingBean {
    private List<CompetenceCourseStatisticsDTO> competenceCourses;

    private List<DegreeCourseStatisticsDTO> degreeCourses;

    private List<ExecutionCourseStatisticsDTO> executionCourses;

    private List<SelectItem> executionPeriods;

    private CompetenceCourse competenceCourse;

    public Department getDepartment() {
	return getUserView().getPerson().getTeacher().getLastWorkingDepartment();
    }

    public Integer getCompetenceCourseId() {
	return (Integer) this.getViewState().getAttribute("competenceCourseId");
    }

    public void setCompetenceCourseId(Integer competenceCourseId) {
	this.getViewState().setAttribute("competenceCourseId", competenceCourseId);
    }

    public Integer getDegreeId() {
	return (Integer) this.getViewState().getAttribute("degreeId");
    }

    public void setDegreeId(Integer degreeId) {
	this.getViewState().setAttribute("degreeId", degreeId);
    }

    public Integer getExecutionPeriodId() {
	Integer executionPeriodId = (Integer) this.getViewState().getAttribute("executionYearPeriod");

	if (executionPeriodId == null) {
	    executionPeriodId = (Integer) getRequestAttribute("executionPeriodId");

	    if (executionPeriodId == null) {
		InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) ReadCurrentExecutionPeriod.run();

		if (infoExecutionPeriod == null) {
		    executionPeriodId = (Integer) this.getExecutionPeriods().get(this.executionPeriods.size() - 1).getValue();
		} else {
		    executionPeriodId = infoExecutionPeriod.getIdInternal();
		}
	    }

	    this.getViewState().setAttribute("executionPeriodId", executionPeriodId);
	}

	return executionPeriodId;
    }

    public void setExecutionPeriodId(Integer executionPeriodId) {
	this.getViewState().setAttribute("executionPeriodId", executionPeriodId);
	setRequestAttribute("executionPeriodId", executionPeriodId);
    }

    public void onExecutionPeriodChangeForCompetenceCourses(ValueChangeEvent valueChangeEvent) throws FenixFilterException,
	    FenixServiceException {
	setExecutionPeriodId((Integer) valueChangeEvent.getNewValue());
	loadCompetenceCourses();
    }

    public void onExecutionPeriodChangeForDegreeCourses(ValueChangeEvent valueChangeEvent) throws FenixFilterException,
	    FenixServiceException {
	setExecutionPeriodId((Integer) valueChangeEvent.getNewValue());
	loadDegreeCourses();
    }

    public void onExecutionPeriodChangeForExecutionCourses(ValueChangeEvent valueChangeEvent) throws FenixFilterException,
	    FenixServiceException {
	setExecutionPeriodId((Integer) valueChangeEvent.getNewValue());
	loadExecutionCourses();
    }

    public List<SelectItem> getExecutionPeriods() {
	if (this.executionPeriods == null) {

	    List<InfoExecutionYear> executionYearsList = (List<InfoExecutionYear>) ReadNotClosedExecutionYears.run();
	    List<SelectItem> result = new ArrayList<SelectItem>();
	    for (InfoExecutionYear executionYear : executionYearsList) {
		List<ExecutionSemester> executionSemesters = rootDomainObject.readExecutionYearByOID(
			executionYear.getIdInternal()).getExecutionPeriods();
		for (ExecutionSemester executionSemester : executionSemesters) {
		    result.add(new SelectItem(executionSemester.getIdInternal(), executionSemester.getExecutionYear().getYear()
			    + " - " + executionSemester.getName()));
		}
	    }
	    this.executionPeriods = result;
	}
	return this.executionPeriods;
    }

    private void loadCompetenceCourses() throws FenixFilterException, FenixServiceException {
	Integer departmentID = getUserView().getPerson().getTeacher().getLastWorkingDepartment().getIdInternal();
	Object args[] = { departmentID, this.getExecutionPeriodId() };
	competenceCourses = (List<CompetenceCourseStatisticsDTO>) ServiceUtils.executeService(
		"ComputeCompetenceCourseStatistics", args);
    }

    public List<CompetenceCourseStatisticsDTO> getCompetenceCourses() throws FenixFilterException, FenixServiceException {
	if (competenceCourses == null) {
	    loadCompetenceCourses();
	}

	return competenceCourses;
    }

    private void loadDegreeCourses() throws FenixFilterException, FenixServiceException {
	degreeCourses = (List<DegreeCourseStatisticsDTO>) ServiceUtils.executeService("ComputeDegreeCourseStatistics",
		new Object[] { getCompetenceCourseId(), getExecutionPeriodId() });
    }

    public List<DegreeCourseStatisticsDTO> getDegreeCourses() throws FenixFilterException, FenixServiceException {
	if (degreeCourses == null) {
	    loadDegreeCourses();
	}

	return degreeCourses;
    }

    private void loadExecutionCourses() throws FenixFilterException, FenixServiceException {
	executionCourses = (List<ExecutionCourseStatisticsDTO>) ServiceUtils.executeService("ComputeExecutionCourseStatistics",
		new Object[] { this.getCompetenceCourseId(), this.getDegreeId(), getExecutionPeriodId() });
    }

    public List<ExecutionCourseStatisticsDTO> getExecutionCourses() throws FenixFilterException, FenixServiceException {
	if (executionCourses == null) {
	    loadExecutionCourses();
	}

	return executionCourses;
    }

    public void onCompetenceCourseSelect(ActionEvent event) throws FenixFilterException, FenixServiceException {

	int competenceCourseId = Integer.parseInt(getRequestParameter("competenceCourseId"));
	setCompetenceCourseId(competenceCourseId);
    }

    public void onDegreeCourseSelect(ActionEvent event) throws FenixFilterException, FenixServiceException {
	int degreeId = Integer.parseInt(getRequestParameter("degreeId"));
	setDegreeId(degreeId);
    }

    public CompetenceCourse getCompetenceCourse() {
	return competenceCourse == null ? rootDomainObject.readCompetenceCourseByOID(getCompetenceCourseId()) : competenceCourse;
    }

    private ResourceBundle getApplicationResources() {
	return getResourceBundle("resources/ApplicationResources");
    }

    /*
     * Export curricular course students
     */
    private CurricularCourse getCurricularCourseToExport() {

	final CompetenceCourse cc = getCompetenceCourse();
	final Degree degree = rootDomainObject.readDegreeByOID(getDegreeId());

	for (final CurricularCourse curricularCourse : cc.getAssociatedCurricularCourses()) {
	    if (curricularCourse.getDegree().equals(degree)) {
		return curricularCourse;
	    }
	}

	return null;
    }

    public void exportStudentsToExcel() throws FenixServiceException {
	try {
	    exportToXls(getFilename());
	} catch (IOException e) {
	    throw new FenixServiceException();
	}
    }

    private String getFilename() {
	final ResourceBundle bundle = getApplicationResources();
	return String.format("%s_%s_%s.xls", new DateTime().toString("ddMMyyyyHHmm"), bundle.getString("label.students"),
		getCurricularCourseToExport().getName().replaceAll(" ", "_"));
    }

    private void exportToXls(String filename) throws IOException {
	this.getResponse().setContentType("application/vnd.ms-excel");
	this.getResponse().setHeader("Content-disposition", "attachment; filename=" + filename + ".xls");
	ServletOutputStream outputStream = this.getResponse().getOutputStream();

	final Spreadsheet spreadsheet = createSpreadsheet();
	reportInfo(spreadsheet);

	spreadsheet.exportToXLSSheet(outputStream);
	outputStream.flush();

	this.getResponse().flushBuffer();
	FacesContext.getCurrentInstance().responseComplete();
    }

    private Spreadsheet createSpreadsheet() {
	return new Spreadsheet("-", getStudentsEnroledListHeaders());
    }

    private List<Object> getStudentsEnroledListHeaders() {
	final ResourceBundle bundle = getApplicationResources();

	final List<Object> headers = new ArrayList<Object>(4);
	// headers.add(bundle.getString("label.number"));
	// headers.add(bundle.getString("label.name"));
	// headers.add(bundle.getString("label.room"));
	headers.add("N�mero");
	headers.add("Curso");
	headers.add("Disciplina");
	headers.add("Ano Lectivo");
	headers.add("Ramo/Perfil principal");
	headers.add("Ramo/Perfil secund�rio");
	return headers;
    }

    private void reportInfo(Spreadsheet spreadsheet) {
	final ExecutionYear executionYear = getExecutionYear();
	final CurricularCourse curricularCourse = getCurricularCourseToExport();

	for (final Enrolment enrolment : curricularCourse.getEnrolments()) {
	    
	    if (!enrolment.isValid(executionYear)) {
		continue;
	    }
	    
	    final Row row = spreadsheet.addRow();

	    row.setCell(enrolment.getStudent().getNumber());
	    row.setCell(enrolment.getDegreeCurricularPlanOfStudent().getDegree().getPresentationName());
	    row.setCell(enrolment.getName().getContent());
	    row.setCell(enrolment.getExecutionYear().getName());
	    
	    final CycleCurriculumGroup cycle = enrolment.getParentCycleCurriculumGroup();
	    
	    final BranchCurriculumGroup major = cycle.getMajorBranchCurriculumGroup();
	    row.setCell(major != null ? major.getName().getContent() : "");
	    
	    final BranchCurriculumGroup minor = cycle.getMinorBranchCurriculumGroup();
	    row.setCell(minor != null ? minor.getName().getContent() : "");
	}
    }

    private ExecutionYear getExecutionYear() {
	return rootDomainObject.readExecutionSemesterByOID(getExecutionPeriodId()).getExecutionYear();
    }

    /*
     * End of export curricular course students
     */

}