/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.lists;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.lists.SearchStudents;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.lists.SearchStudentsByDegreeParametersBean;
import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationConclusionBean;
import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationWithStateForExecutionYearBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.StringUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.excel.StyledExcelSpreadsheet;
import pt.utl.ist.fenix.tools.util.i18n.Language;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * @author - �ngela Almeida (argelina@ist.utl.pt)
 * 
 */

@Mapping(path = "/studentsListByDegree", module = "academicAdminOffice")
@Forwards( { @Forward(name = "searchRegistrations", path = "/academicAdminOffice/lists/searchRegistrationsByDegree.jsp") })
public class StudentsListByDegreeDA extends FenixDispatchAction {

    private static final String YMD_FORMAT = "yyyy-MM-dd";

    public ActionForward prepareByDegree(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("searchParametersBean", getOrCreateSearchParametersBean());
	return mapping.findForward("searchRegistrations");
    }

    public ActionForward postBack(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final SearchStudentsByDegreeParametersBean searchParametersBean = getOrCreateSearchParametersBean();
	RenderUtils.invalidateViewState();
	request.setAttribute("searchParametersBean", searchParametersBean);

	return mapping.findForward("searchRegistrations");
    }

    private SearchStudentsByDegreeParametersBean getOrCreateSearchParametersBean() {
	SearchStudentsByDegreeParametersBean bean = (SearchStudentsByDegreeParametersBean) getRenderedObject("searchParametersBean");
	return (bean != null) ? bean : new SearchStudentsByDegreeParametersBean();
    }

    public ActionForward searchByDegree(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final SearchStudentsByDegreeParametersBean searchBean = (SearchStudentsByDegreeParametersBean) getRenderedObject();

	final List<RegistrationWithStateForExecutionYearBean> registrations = SearchStudents.run(searchBean);

	request.setAttribute("searchParametersBean", searchBean);
	request.setAttribute("studentCurricularPlanList", registrations);

	return mapping.findForward("searchRegistrations");
    }

    public ActionForward exportInfoToExcel(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {

	final SearchStudentsByDegreeParametersBean searchBean = (SearchStudentsByDegreeParametersBean) getRenderedObject();
	if (searchBean != null) {
	    final List<RegistrationWithStateForExecutionYearBean> registrations = SearchStudents.run(searchBean);

	    try {
		String filename;

		ExecutionYear executionYear = searchBean.getExecutionYear();
		if (searchBean.getDegree() == null) {
		    filename = executionYear.getYear();
		} else {
		    filename = searchBean.getDegree().getNameFor(executionYear).getContent(Language.getLanguage()).replace(' ',
			    '_')
			    + "_" + executionYear.getYear();
		}

		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-disposition", "attachment; filename=" + filename + ".xls");
		ServletOutputStream writer = response.getOutputStream();

		final String param = request.getParameter("extendedInfo");
		boolean extendedInfo = param != null && param.length() > 0 && Boolean.valueOf(param).booleanValue();

		exportToXls(registrations, writer, searchBean.getExecutionYear(), searchBean.getDegree(), extendedInfo);
		writer.flush();
		response.flushBuffer();

	    } catch (IOException e) {
		throw new FenixServiceException();
	    }
	}
	return null;
    }

    private void exportToXls(List<RegistrationWithStateForExecutionYearBean> registrationWithStateForExecutionYearBean,
	    OutputStream outputStream, ExecutionYear executionYear, Degree degree, boolean extendedInfo) throws IOException {

	final StyledExcelSpreadsheet spreadsheet = new StyledExcelSpreadsheet("AlunosPorCurso");
	spreadsheet.newHeaderRow();
	if (degree == null) {
	    spreadsheet.addHeader(executionYear.getYear());
	} else {
	    spreadsheet.addHeader(degree.getNameFor(executionYear) + " - " + executionYear.getYear());
	}
	spreadsheet.newRow();
	spreadsheet.newRow();
	spreadsheet.addCell(registrationWithStateForExecutionYearBean.size() + " Alunos");
	fillSpreadSheet(registrationWithStateForExecutionYearBean, spreadsheet, executionYear, extendedInfo);
	spreadsheet.getWorkbook().write(outputStream);
    }

    private void fillSpreadSheet(List<RegistrationWithStateForExecutionYearBean> registrations,
	    final StyledExcelSpreadsheet spreadsheet, ExecutionYear executionYear, boolean extendedInfo) {
	setHeaders(spreadsheet, extendedInfo);
	for (RegistrationWithStateForExecutionYearBean registrationWithStateForExecutionYearBean : registrations) {
	    Registration registration = registrationWithStateForExecutionYearBean.getRegistration();
	    spreadsheet.newRow();
	    Degree degree = registration.getDegree();
	    spreadsheet.addCell(!(StringUtils.isEmpty(degree.getSigla())) ? degree.getSigla() : degree.getNameFor(executionYear)
		    .getContent());
	    spreadsheet.addCell(registration.getNumber().toString());
	    final Person person = registration.getPerson();
	    spreadsheet.addCell(person.getName());
	    final RegistrationState lastRegistrationState = registration.getLastRegistrationState(executionYear);

	    spreadsheet.addCell(lastRegistrationState.getStateType().getDescription());
	    spreadsheet.addCell(registration.getRegistrationAgreement().getName());

	    if (extendedInfo) {
		spreadsheet.addCell(registration.getPerson().getCountry().getName());
		spreadsheet.addCell(person.getDefaultEmailAddress() == null ? StringUtils.EMPTY : person.getDefaultEmailAddress()
			.getValue());
		spreadsheet.addCell(person.getGender().toLocalizedString());
		spreadsheet.addCell(person.getDateOfBirthYearMonthDay() == null ? StringUtils.EMPTY : person
			.getDateOfBirthYearMonthDay().toString(YMD_FORMAT));
		spreadsheet.addCell(registration.getEnrolmentsExecutionYears().size());
		spreadsheet.addCell(registration.getCurricularYear(executionYear));
		spreadsheet.addCell(registration.getEnrolments(executionYear).size());
		spreadsheet.addCell(registration.isPartialRegime(executionYear) ? "Tempo Parcial" : "Tempo Integral");

		fillSpreadSheetPreBolonhaInfo(spreadsheet, registration);
		fillSpreadSheetBolonhaInfo(spreadsheet, registration, registration.getLastStudentCurricularPlan().getFirstCycle());
		fillSpreadSheetBolonhaInfo(spreadsheet, registration, registration.getLastStudentCurricularPlan()
			.getSecondCycle());
		fillSpreadSheetBolonhaInfo(spreadsheet, registration, registration.getLastStudentCurricularPlan().getThirdCycle());
	    }
	}
    }

    private void fillSpreadSheetPreBolonhaInfo(StyledExcelSpreadsheet spreadsheet, Registration registration) {
	if (!registration.isBolonha()) {
	    RegistrationConclusionBean registrationConclusionBean = new RegistrationConclusionBean(registration);
	    fillSpreadSheetRegistrationInfo(spreadsheet, registrationConclusionBean, registration.hasConcluded());
	} else {
	    fillSpreadSheetEmptyCells(spreadsheet);
	}
    }

    private void fillSpreadSheetBolonhaInfo(StyledExcelSpreadsheet spreadsheet, Registration registration,
	    CycleCurriculumGroup cycle) {
	if ((cycle != null) && (!cycle.isExternal())) {
	    RegistrationConclusionBean registrationConclusionBean = new RegistrationConclusionBean(registration, cycle);
	    fillSpreadSheetRegistrationInfo(spreadsheet, registrationConclusionBean, registrationConclusionBean.isConcluded());
	} else {
	    fillSpreadSheetEmptyCells(spreadsheet);
	}
    }

    private void fillSpreadSheetRegistrationInfo(StyledExcelSpreadsheet spreadsheet,
	    RegistrationConclusionBean registrationConclusionBean, boolean isConcluded) {
	spreadsheet.addCell(isConcluded ? "Sim" : "N�o");
	spreadsheet.addCell(isConcluded ? registrationConclusionBean.getConclusionDate().toString(YMD_FORMAT) : "");
	spreadsheet.addCell(registrationConclusionBean.getAverage().toString());
    }

    private void fillSpreadSheetEmptyCells(StyledExcelSpreadsheet spreadsheet) {
	spreadsheet.addCell("");
	spreadsheet.addCell("");
	spreadsheet.addCell("");
    }

    private void setHeaders(final StyledExcelSpreadsheet spreadsheet, final boolean extendedInfo) {
	spreadsheet.newHeaderRow();
	spreadsheet.addHeader("Curso");
	spreadsheet.addHeader("N�mero");
	spreadsheet.addHeader("Nome");
	spreadsheet.addHeader("Estado da Matr�cula");
	spreadsheet.addHeader("Acordo");
	if (extendedInfo) {
	    spreadsheet.addHeader("Nacionalidade");
	    spreadsheet.addHeader("Email");
	    spreadsheet.addHeader("G�nero");
	    spreadsheet.addHeader("Data Nascimento");
	    spreadsheet.addHeader("N� inscri��es");
	    spreadsheet.addHeader("Ano acad�mico");
	    spreadsheet.addHeader("N� disciplinas inscritas");
	    spreadsheet.addHeader("Regime");
	    spreadsheet.addHeader("Curso Conclu�do");
	    spreadsheet.addHeader("Data de Conclus�o");
	    spreadsheet.addHeader("M�dia de Curso");
	    spreadsheet.addHeader("1� Ciclo Conclu�do");
	    spreadsheet.addHeader("Data de Conclus�o (1� Ciclo)");
	    spreadsheet.addHeader("M�dia de Curso (1� Ciclo)");
	    spreadsheet.addHeader("2� Ciclo Conclu�do");
	    spreadsheet.addHeader("Data de Conclus�o (2� Ciclo)");
	    spreadsheet.addHeader("M�dia de Curso (2� Ciclo)");
	    spreadsheet.addHeader("3� Ciclo Conclu�do");
	    spreadsheet.addHeader("Data de Conclus�o (3� Ciclo)");
	    spreadsheet.addHeader("M�dia de Curso (3� Ciclo)");
	}
    }

}