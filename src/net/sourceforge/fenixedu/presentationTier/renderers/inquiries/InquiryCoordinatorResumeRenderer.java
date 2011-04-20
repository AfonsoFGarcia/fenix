/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.renderers.inquiries;

import net.sourceforge.fenixedu.dataTransferObject.inquiries.BlockResumeResult;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.CurricularCourseResumeResult;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.TeacherShiftTypeResultsBean;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.inquiries.InquiryDelegateAnswer;
import net.sourceforge.fenixedu.domain.inquiries.ResultClassification;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.ChecksumRewriter;
import pt.ist.fenixWebFramework.renderers.components.HtmlInlineContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlLink;
import pt.ist.fenixWebFramework.renderers.components.HtmlMenu;
import pt.ist.fenixWebFramework.renderers.components.HtmlMenuGroup;
import pt.ist.fenixWebFramework.renderers.components.HtmlMenuOption;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableCell;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableRow;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;

/**
 * @author - Ricardo Rodrigues (ricardo.rodrigues@ist.utl.pt)
 * 
 */
public class InquiryCoordinatorResumeRenderer extends InquiryBlocksResumeRenderer {

    @Override
    protected void createFinalCells(HtmlTableRow tableRow, BlockResumeResult blockResumeResult) {
	CurricularCourseResumeResult courseResumeResult = (CurricularCourseResumeResult) blockResumeResult;

	HtmlInlineContainer container = new HtmlInlineContainer();
	HtmlTableCell linksCell = tableRow.createCell();
	String fillInParameters = buildFillInParameters(courseResumeResult);
	String resultsParameters = buildParametersForResults(courseResumeResult);

	HtmlLink link = new HtmlLink();
	link.setModule("/publico");
	link.setUrl("/viewCourseResults.do?" + resultsParameters + "&contentContextPath_PATH=/coordenador/coordenador");
	link.setEscapeAmpersand(false);

	HtmlMenu menu = new HtmlMenu();
	menu
		.setOnChange("var value=this.options[this.selectedIndex].value; this.selectedIndex=0; if(value!= ''){ window.open(value,'_blank'); }");
	menu.setStyle("width: 150px");
	HtmlMenuOption optionEmpty = menu.createOption("-- Consultar --");
	HtmlMenuGroup resultsGroup = menu.createGroup("Resultados");
	HtmlMenuOption optionUC = resultsGroup.createOption();
	optionUC.setText("Resultados UC");
	String calculatedUrl = link.calculateUrl();
	optionUC.setValue(calculatedUrl + "&_request_checksum_=" + ChecksumRewriter.calculateChecksum(calculatedUrl));

	for (TeacherShiftTypeResultsBean teacherShiftTypeResultsBean : courseResumeResult.getTeachersResults()) {
	    String teacherResultsParameters = buildParametersForTeacherResults(teacherShiftTypeResultsBean);
	    HtmlLink teacherLink = new HtmlLink();
	    teacherLink.setEscapeAmpersand(false);
	    teacherLink.setModule("/publico");
	    teacherLink.setUrl("/viewTeacherResults.do?" + teacherResultsParameters
		    + "&contentContextPath_PATH=/coordenador/coordenador");
	    calculatedUrl = teacherLink.calculateUrl();

	    HtmlMenuOption optionTeacher = resultsGroup.createOption();
	    optionTeacher.setText(teacherShiftTypeResultsBean.getShiftType().getFullNameTipoAula() + " - "
		    + teacherShiftTypeResultsBean.getProfessorship().getPerson().getName());
	    optionTeacher.setValue(calculatedUrl + "&_request_checksum_=" + ChecksumRewriter.calculateChecksum(calculatedUrl));
	}

	HtmlMenuGroup reportsGroup = menu.createGroup("Relat�rios");
	for (InquiryDelegateAnswer inquiryDelegateAnswer : courseResumeResult.getExecutionCourse()
		.getInquiryDelegatesAnswersSet()) {
	    if (inquiryDelegateAnswer.getExecutionDegree() == courseResumeResult.getExecutionDegree()) {
		String delegateInquiryParameters = buildParametersForDelegateInquiry(inquiryDelegateAnswer);
		HtmlLink delegateLink = new HtmlLink();
		delegateLink.setEscapeAmpersand(false);
		delegateLink.setModule("/publico");
		delegateLink.setUrl("/viewQUCInquiryAnswers.do?" + delegateInquiryParameters
			+ "&contentContextPath_PATH=/coordenador/coordenador");
		calculatedUrl = delegateLink.calculateUrl();

		HtmlMenuOption optionDelegate = reportsGroup.createOption();
		optionDelegate.setText("Delegado");
		optionDelegate.setValue(calculatedUrl + "&_request_checksum_="
			+ ChecksumRewriter.calculateChecksum(calculatedUrl));
	    }
	}

	for (Professorship professorship : courseResumeResult.getExecutionCourse().getProfessorships()) {
	    if (professorship.getInquiryTeacherAnswer() != null) {
		HtmlLink teacherLink = new HtmlLink();
		teacherLink.setEscapeAmpersand(false);
		teacherLink.setModule("/publico");
		teacherLink.setUrl("/viewQUCInquiryAnswers.do?method=showTeacherInquiry&professorshipOID="
			+ professorship.getExternalId() + "&contentContextPath_PATH=/coordenador/coordenador");
		calculatedUrl = teacherLink.calculateUrl();

		HtmlMenuOption optionTeacher = reportsGroup.createOption();
		optionTeacher.setText("Docente (" + professorship.getPerson().getName() + ")");
		optionTeacher
			.setValue(calculatedUrl + "&_request_checksum_=" + ChecksumRewriter.calculateChecksum(calculatedUrl));
	    }
	}

	for (Professorship professorship : courseResumeResult.getExecutionCourse().getProfessorships()) {
	    if (professorship.getInquiryRegentAnswer() != null) {
		HtmlLink regentLink = new HtmlLink();
		regentLink.setEscapeAmpersand(false);
		regentLink.setModule("/publico");
		regentLink.setUrl("/viewQUCInquiryAnswers.do?method=showRegentInquiry&professorshipOID="
			+ professorship.getExternalId() + "&contentContextPath_PATH=/coordenador/coordenador");
		calculatedUrl = regentLink.calculateUrl();

		HtmlMenuOption optionRegent = reportsGroup.createOption();
		optionRegent.setText("Regente (" + professorship.getPerson().getName() + ")");
		optionRegent.setValue(calculatedUrl + "&_request_checksum_=" + ChecksumRewriter.calculateChecksum(calculatedUrl));
	    }
	}

	container.addChild(menu);

	container.addChild(new HtmlText("&nbsp;|&nbsp;", false));

	HtmlLink commentLink = new HtmlLink();
	commentLink.setUrl("/viewInquiriesResults.do?method=showUCResultsAndComments" + fillInParameters);
	commentLink.setText("Comentar");
	container.addChild(commentLink);

	ResultClassification forAudit = courseResumeResult.getExecutionCourse().getForAudit();
	if (forAudit != null) {
	    if (forAudit.equals(ResultClassification.RED)) {
		container.addChild(new HtmlText(" (Auditoria)"));
	    } else if (forAudit.equals(ResultClassification.YELLOW)) {
		container.addChild(new HtmlText(" (Em observa��o)"));
	    }
	}

	linksCell.setBody(container);
	linksCell.setClasses("col-actions");
    }

    private String buildParametersForDelegateInquiry(InquiryDelegateAnswer inquiryDelegateAnswer) {
	StringBuilder builder = new StringBuilder("method=showDelegateInquiry");
	builder.append("&executionCourseOID=").append(inquiryDelegateAnswer.getExecutionCourse().getExternalId());
	builder.append("&executionDegreeOID=").append(inquiryDelegateAnswer.getExecutionDegree().getExternalId());
	return builder.toString();
    }

    private String buildParametersForTeacherResults(TeacherShiftTypeResultsBean teacherShiftTypeResultsBean) {
	StringBuilder builder = new StringBuilder();
	builder.append("shiftType=").append(teacherShiftTypeResultsBean.getShiftType().name());
	builder.append("&professorshipOID=").append(teacherShiftTypeResultsBean.getProfessorship().getExternalId());
	return builder.toString();
    }

    private String buildFillInParameters(CurricularCourseResumeResult courseResumeResult) {
	StringBuilder builder = new StringBuilder();
	builder.append("&executionDegreeOID=").append(courseResumeResult.getExecutionDegree().getExternalId());
	builder.append("&executionCourseOID=").append(courseResumeResult.getExecutionCourse().getExternalId());
	builder.append("&degreeCurricularPlanID=").append(
		courseResumeResult.getExecutionDegree().getDegreeCurricularPlan().getIdInternal());
	return builder.toString();
    }

    private String buildParametersForResults(CurricularCourseResumeResult courseResumeResult) {
	StringBuilder builder = new StringBuilder();
	builder.append("degreeCurricularPlanOID=").append(
		courseResumeResult.getExecutionDegree().getDegreeCurricularPlan().getExternalId());
	builder.append("&executionCourseOID=").append(courseResumeResult.getExecutionCourse().getExternalId());
	return builder.toString();
    }
}
