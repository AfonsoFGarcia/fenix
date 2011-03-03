/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.renderers.inquiries;

import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.inquiries.CurricularCourseResumeResult;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResult;
import net.sourceforge.fenixedu.domain.inquiries.ResultClassification;
import pt.ist.fenixWebFramework.renderers.OutputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlBlockContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlInlineContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlLink;
import pt.ist.fenixWebFramework.renderers.components.HtmlTable;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableCell;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableRow;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableCell.CellType;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;

/**
 * @author - Ricardo Rodrigues (ricardo.rodrigues@ist.utl.pt)
 * 
 */
public class InquiryDelegateCoursesResumeRenderer extends OutputRenderer {

    private String columnClasses;

    public String getColumnClasses() {
	return columnClasses;
    }

    public void setColumnClasses(String columnClasses) {
	this.columnClasses = columnClasses;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
	return new InquiryQuestionLayout(object);
    }

    private class InquiryQuestionLayout extends Layout {

	public InquiryQuestionLayout(Object object) {
	}

	@Override
	public HtmlComponent createComponent(Object object, Class type) {
	    HtmlBlockContainer blockContainer = new HtmlBlockContainer();
	    final HtmlTable mainTable = new HtmlTable();
	    blockContainer.addChild(mainTable);
	    mainTable.setClasses("tstyle1 thlight tdcenter");
	    mainTable.setStyle("width: 100%; margin-bottom: 0;");
	    List<CurricularCourseResumeResult> coursesResume = (List<CurricularCourseResumeResult>) object;

	    if (!coursesResume.isEmpty()) {
		createHeader(coursesResume.get(0).getCurricularBlocks(), mainTable);
	    }
	    for (CurricularCourseResumeResult courseResumeResult : coursesResume) {
		Set<InquiryResult> courseBlocksResults = courseResumeResult.getCurricularBlocks();

		HtmlTableRow tableRow = mainTable.createRow();
		HtmlTableCell firstCell = tableRow.createCell();
		firstCell.setBody(new HtmlText(courseResumeResult.getExecutionCourse().getNameI18N().toString()));
		firstCell.setClasses("col-course");

		int iter = 0;
		List<Integer> uncommentedMandatoryIssues = courseResumeResult.getUncommentedMandatoryIssues();
		for (InquiryResult inquiryResult : courseBlocksResults) {
		    HtmlTableCell curricularCell = tableRow.createCell();
		    String numberOfIssues = uncommentedMandatoryIssues.get(iter) != 0 ? String.valueOf(uncommentedMandatoryIssues
			    .get(iter)) : "-";
		    String presentNumberOfIssues = " (" + numberOfIssues + ")</div>";
		    HtmlText bodyText = new HtmlText(getColoredBar(inquiryResult) + presentNumberOfIssues);
		    bodyText.setEscaped(false);
		    curricularCell.setBody(bodyText);
		    curricularCell.setClasses("col-bar");
		    iter++;
		}

		HtmlTableCell fillingStatus = tableRow.createCell();
		fillingStatus.setBody(new HtmlText("Completo"));
		fillingStatus.setClasses("col-fill");

		HtmlInlineContainer container = new HtmlInlineContainer();
		HtmlTableCell linksCell = tableRow.createCell();
		HtmlLink link = new HtmlLink();
		link.setModuleRelative(false);
		link.setContextRelative(true);

		String fillInParameters = buildFillInParameters(courseResumeResult);
		link.setUrl("/viewQUCResults/delegateInquiry.do?" + fillInParameters + "&method=showFillInquiryPage");
		link.setText("Resultados UC");
		container.addChild(link);

		container.addChild(new HtmlText("&nbsp;|&nbsp;", false));

		HtmlLink link1 = new HtmlLink();
		link1.setUrl("/delegateInquiry.do?" + fillInParameters + "&method=showFillInquiryPage");
		link1.setText("Preencher");
		container.addChild(link1);

		linksCell.setBody(container);
		linksCell.setClasses("col-actions");
	    }

	    return blockContainer;
	}

	private String buildFillInParameters(CurricularCourseResumeResult courseResumeResult) {
	    StringBuilder builder = new StringBuilder();
	    builder.append("delegateOID=").append(courseResumeResult.getYearDelegate().getExternalId());
	    builder.append("&executionDegreeOID=").append(courseResumeResult.getExecutionDegree().getExternalId());
	    builder.append("&executionCourseOID=").append(courseResumeResult.getExecutionCourse().getExternalId());
	    return builder.toString();
	}

	private String getColoredBar(InquiryResult inquiryResult) {
	    StringBuilder sb = new StringBuilder("<div class='");
	    if (inquiryResult.getResultClassification().equals(ResultClassification.GREEN)) {
		sb.append("bar-green");
	    } else if (inquiryResult.getResultClassification().equals(ResultClassification.BLUE)) {
		sb.append("bar-blue");
	    } else if (inquiryResult.getResultClassification().equals(ResultClassification.YELLOW)) {
		sb.append("bar-yellow");
	    } else if (inquiryResult.getResultClassification().equals(ResultClassification.RED)) {
		sb.append("bar-red");
	    } else if (inquiryResult.getResultClassification().equals(ResultClassification.GREY)) {
		sb.append("bar-grey");
	    }
	    sb.append("'><div>&nbsp;</div>");
	    return sb.toString();
	}

	private void createHeader(final Set<InquiryResult> courseBlocksResults, final HtmlTable mainTable) {
	    final HtmlTableRow headerRow = mainTable.createRow();

	    final HtmlTableCell firstHeaderCell = headerRow.createCell(CellType.HEADER);
	    firstHeaderCell.setBody(new HtmlText("Unidade Curricular"));
	    firstHeaderCell.setClasses("col-course");

	    for (InquiryResult inquiryResult : courseBlocksResults) {
		final HtmlTableCell firstGrouptInnerCell = headerRow.createCell(CellType.HEADER);
		firstGrouptInnerCell.setBody(new HtmlText(inquiryResult.getInquiryQuestion().getLabel().toString()));
		firstGrouptInnerCell.setClasses("col-bar");
	    }

	    final HtmlTableCell fillingStatus = headerRow.createCell(CellType.HEADER);
	    fillingStatus.setBody(new HtmlText("Estado do preenchimento"));
	    fillingStatus.setClasses("col-fill");

	    final HtmlTableCell finalCell = headerRow.createCell(CellType.HEADER);
	    finalCell.setClasses("col-actions");
	}

	@Override
	public String getClasses() {
	    return "delegate-resume";
	}

	@Override
	public String getStyle() {
	    return "max-width: 1000px;";
	}
    }
}
