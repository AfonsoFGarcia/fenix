/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.coordinator.inquiries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.inquiries.CoordinatorInquiryBean;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.CoordinatorResultsBean;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.CurricularCourseResumeResult;
import net.sourceforge.fenixedu.dataTransferObject.oldInquiries.ViewInquiriesResultPageDTO;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.inquiries.CoordinatorInquiryTemplate;
import net.sourceforge.fenixedu.domain.inquiries.InquiryCoordinatorAnswer;
import net.sourceforge.fenixedu.domain.inquiries.InquiryGlobalComment;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResponseState;
import net.sourceforge.fenixedu.domain.inquiries.ResultPersonCategory;
import net.sourceforge.fenixedu.domain.oldInquiries.InquiryResponsePeriod;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.coordinator.CoordinatedDegreeInfo;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */

@Mapping(path = "/viewInquiriesResults", module = "coordinator", formBeanClass = ViewInquiriesResultPageDTO.class)
@Forwards( { @Forward(name = "inquiryResults", path = "/coordinator/inquiries/viewInquiriesResults.jsp"),
	@Forward(name = "curricularUnitSelection", path = "/coordinator/inquiries/curricularUnitSelection.jsp"),
	@Forward(name = "showFilledTeachingInquiry", path = "/inquiries/showFilledTeachingInquiry.jsp", useTile = false),
	@Forward(name = "showFilledTeachingInquiry_v2", path = "/inquiries/showFilledTeachingInquiry_v2.jsp", useTile = false),
	@Forward(name = "showFilledDelegateInquiry", path = "/inquiries/showFilledDelegateInquiry.jsp", useTile = false),
	@Forward(name = "showCourseInquiryResult", path = "/inquiries/showCourseInquiryResult.jsp", useTile = false),
	@Forward(name = "showTeachingInquiryResult", path = "/inquiries/showTeachingInquiryResult.jsp", useTile = false),
	@Forward(name = "coordinatorUCView", path = "/coordinator/inquiries/coordinatorUCView.jsp"),
	@Forward(name = "coordinatorInquiry", path = "/coordinator/inquiries/coordinatorInquiry.jsp") })
public class ViewInquiriesResultsForCoordinatorDA extends ViewInquiriesResultsDA {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	CoordinatedDegreeInfo.setCoordinatorContext(request);
	return super.execute(mapping, actionForm, request, response);
    }

    @Override
    public ActionForward prepare(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	ExecutionSemester executionPeriod = getMostRecentExecutionPeriodWithResults();
	if (executionPeriod != null) {
	    ((ViewInquiriesResultPageDTO) actionForm).setDegreeCurricularPlanID(getIntegerFromRequest(request,
		    "degreeCurricularPlanID"));
	    ((ViewInquiriesResultPageDTO) actionForm).setExecutionSemesterID(executionPeriod.getOid());
	    return selectexecutionSemester(actionMapping, actionForm, request, response);
	}

	return super.prepare(actionMapping, actionForm, request, response);
    }

    private ExecutionSemester getMostRecentExecutionPeriodWithResults() {
	ExecutionSemester oldQucExecutionSemester = ExecutionSemester.readBySemesterAndExecutionYear(2, "2009/2010");
	ExecutionSemester executionPeriod = ExecutionSemester.readActualExecutionSemester();
	while (oldQucExecutionSemester.isBefore(executionPeriod)) {
	    if (executionPeriod.hasAnyInquiryResults()) {
		return executionPeriod;
	    }
	    executionPeriod = executionPeriod.getPreviousExecutionPeriod();
	}
	return null;
    }

    @Override
    public ActionForward selectexecutionSemester(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	ViewInquiriesResultPageDTO resultPageDTO = (ViewInquiriesResultPageDTO) actionForm;
	final ExecutionSemester executionSemester = resultPageDTO.getExecutionSemester();
	if (executionSemester == null) {
	    return super.prepare(actionMapping, actionForm, request, response);
	} else {
	    ExecutionSemester oldQucExecutionSemester = ExecutionSemester.readBySemesterAndExecutionYear(2, "2009/2010");
	    if (!executionSemester.isAfter(oldQucExecutionSemester)) {
		return super.selectexecutionSemester(actionMapping, actionForm, request, response);
	    }
	}

	Map<Integer, List<CurricularCourseResumeResult>> coursesResultResumeMap = new HashMap<Integer, List<CurricularCourseResumeResult>>();
	boolean coursesToAudit = false;
	if (!resultPageDTO.getDegreeCurricularPlan().getDegreeType().isThirdCycle()) {

	    CoordinatorInquiryTemplate coordinatorInquiryTemplate = CoordinatorInquiryTemplate
		    .getTemplateByExecutionPeriod(executionSemester);

	    if (coordinatorInquiryTemplate == null) {
		request.setAttribute("coursesResultResumeMap", coursesResultResumeMap);
		return super.prepare(actionMapping, actionForm, request, response);
	    }

	    ExecutionDegree executionDegree = resultPageDTO.getDegreeCurricularPlan().getExecutionDegreeByAcademicInterval(
		    executionSemester.getAcademicInterval());
	    Coordinator coordinator = executionDegree.getCoordinatorByTeacher(AccessControl.getPerson());
	    if (coordinator == null) {
		request.setAttribute("notCoordinator", "true");
		return actionMapping.findForward("curricularUnitSelection");
	    }

	    InquiryCoordinatorAnswer inquiryCoordinatorAnswer = coordinator.getInquiryCoordinatorAnswer(executionSemester);
	    if (inquiryCoordinatorAnswer == null
		    || inquiryCoordinatorAnswer.hasRequiredQuestionsToAnswer(coordinatorInquiryTemplate)) {
		request.setAttribute("completionState", InquiryResponseState.INCOMPLETE.getLocalizedName());
	    } else {
		request.setAttribute("completionState", InquiryResponseState.COMPLETE.getLocalizedName());
	    }

	    request.setAttribute("executionDegree", executionDegree);
	    request.setAttribute("degreeAcronym", executionDegree.getDegree().getSigla());

	    Set<ExecutionCourse> dcpExecutionCourses = resultPageDTO.getDegreeCurricularPlan()
		    .getExecutionCoursesByExecutionPeriod(executionSemester);
	    for (ExecutionCourse executionCourse : dcpExecutionCourses) {
		CurricularCourseResumeResult courseResumeResult = new CurricularCourseResumeResult(executionCourse,
			executionDegree, "label.inquiry.curricularUnit", executionCourse.getName(), AccessControl.getPerson(),
			ResultPersonCategory.DEGREE_COORDINATOR, false, true);
		if (courseResumeResult.getResultBlocks().size() > 1) {

		    if (executionCourse.getForAudit(executionDegree) != null) {
			coursesToAudit = true;
		    }
		    CurricularCourse curricularCourse = executionCourse.getCurricularCourseFor(resultPageDTO
			    .getDegreeCurricularPlan());

		    for (Context context : curricularCourse.getParentContextsByExecutionSemester(executionSemester)) {
			Integer curricurlarYear = context.getCurricularYear();
			List<CurricularCourseResumeResult> coursesResultResume = coursesResultResumeMap.get(curricurlarYear);
			if (coursesResultResume == null) {
			    coursesResultResume = new ArrayList<CurricularCourseResumeResult>();
			    coursesResultResumeMap.put(curricurlarYear, coursesResultResume);
			}
			coursesResultResume.add(courseResumeResult);
		    }
		}
	    }
	}

	for (Integer curricularYear : coursesResultResumeMap.keySet()) {
	    List<CurricularCourseResumeResult> list = coursesResultResumeMap.get(curricularYear);
	    Collections.sort(list, new BeanComparator("executionCourse.name"));
	}

	request.setAttribute("coursesToAudit", String.valueOf(coursesToAudit));
	request.setAttribute("executionPeriod", executionSemester);
	request.setAttribute("executionPeriods", getExecutionSemesters(request, actionForm));
	request.setAttribute("coursesResultResumeMap", coursesResultResumeMap);
	return actionMapping.findForward("curricularUnitSelection");
    }

    public ActionForward showUCResultsAndComments(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	ExecutionCourse executionCourse = AbstractDomainObject.fromExternalId(getFromRequest(request, "executionCourseOID")
		.toString());
	ExecutionDegree executionDegree = AbstractDomainObject.fromExternalId(getFromRequest(request, "executionDegreeOID")
		.toString());
	InquiryGlobalComment globalComment = executionCourse.getInquiryGlobalComment(executionDegree);

	CoordinatorResultsBean coordinatorInquiryBean = new CoordinatorResultsBean(executionCourse, executionDegree,
		AccessControl.getPerson(), globalComment);

	request.setAttribute("executionPeriod", executionCourse.getExecutionPeriod());
	request.setAttribute("executionCourse", executionCourse);
	request.setAttribute("coordinatorInquiryBean", coordinatorInquiryBean);

	return actionMapping.findForward("coordinatorUCView");
    }

    public ActionForward showCoordinatorInquiry(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	ExecutionDegree executionDegree = AbstractDomainObject.fromExternalId(getFromRequest(request, "executionDegreeOID")
		.toString());
	ExecutionSemester executionSemester = AbstractDomainObject.fromExternalId(getFromRequest(request, "executionPeriodOID")
		.toString());
	CoordinatorInquiryTemplate coordinatorInquiryTemplate = CoordinatorInquiryTemplate
		.getTemplateByExecutionPeriod(executionSemester);
	Coordinator coordinator = executionDegree.getCoordinatorByTeacher(AccessControl.getPerson());
	InquiryCoordinatorAnswer inquiryCoordinatorAnswer = coordinator.getInquiryCoordinatorAnswer(executionSemester);

	CoordinatorInquiryBean coordinatorInquiryBean = new CoordinatorInquiryBean(coordinatorInquiryTemplate, coordinator,
		inquiryCoordinatorAnswer, executionSemester);

	request.setAttribute("degreeAcronym", executionDegree.getDegree().getSigla());
	request.setAttribute("executionPeriod", executionSemester);
	request.setAttribute("coordinatorInquiryBean", coordinatorInquiryBean);

	return actionMapping.findForward("coordinatorInquiry");
    }

    public ActionForward saveComment(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	final CoordinatorResultsBean coordinatorResultsBean = getRenderedObject("coordinatorInquiryBean");
	coordinatorResultsBean.saveComment();

	((ViewInquiriesResultPageDTO) actionForm).setExecutionSemester(coordinatorResultsBean.getExecutionCourse()
		.getExecutionPeriod());
	((ViewInquiriesResultPageDTO) actionForm).setDegreeCurricularPlanID(coordinatorResultsBean.getExecutionDegree()
		.getDegreeCurricularPlan().getIdInternal());
	request.setAttribute("degreeCurricularPlanID", coordinatorResultsBean.getExecutionDegree().getDegreeCurricularPlan()
		.getIdInternal().toString());

	CoordinatedDegreeInfo.setCoordinatorContext(request);
	return selectexecutionSemester(actionMapping, actionForm, request, response);
    }

    public ActionForward saveInquiry(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	final CoordinatorInquiryBean coordinatorInquiryBean = getRenderedObject("coordinatorInquiryBean");

	String validationResult = coordinatorInquiryBean.validateInquiry();
	if (!Boolean.valueOf(validationResult)) {
	    RenderUtils.invalidateViewState();
	    addActionMessage(request, "error.inquiries.fillInQuestion", validationResult);

	    request.setAttribute("coordinatorInquiryBean", coordinatorInquiryBean);
	    request.setAttribute("executionPeriod", coordinatorInquiryBean.getExecutionSemester());
	    request.setAttribute("executionCourse", coordinatorInquiryBean.getCoordinator().getExecutionDegree().getDegree()
		    .getSigla());
	    return actionMapping.findForward("coordinatorInquiry");
	}

	RenderUtils.invalidateViewState("coordinatorInquiry");
	coordinatorInquiryBean.saveInquiry();

	((ViewInquiriesResultPageDTO) actionForm).setExecutionSemester(coordinatorInquiryBean.getExecutionSemester());
	((ViewInquiriesResultPageDTO) actionForm).setDegreeCurricularPlanID(coordinatorInquiryBean.getCoordinator()
		.getExecutionDegree().getDegreeCurricularPlan().getIdInternal());
	request.setAttribute("degreeCurricularPlanID", coordinatorInquiryBean.getCoordinator().getExecutionDegree()
		.getDegreeCurricularPlan().getIdInternal().toString());

	CoordinatedDegreeInfo.setCoordinatorContext(request);
	return selectexecutionSemester(actionMapping, actionForm, request, response);
    }

    protected boolean coordinatorCanComment(final ExecutionDegree executionDegree, final ExecutionSemester executionPeriod) {
	if (executionDegree.getDegreeType().isThirdCycle()) {
	    return false;
	}

	final InquiryResponsePeriod coordinatorReportResponsePeriod = executionPeriod.getCoordinatorReportResponsePeriod();
	final Coordinator coordinator = executionDegree.getCoordinatorByTeacher(AccessControl.getPerson());
	return coordinator != null && coordinator.isResponsible() && coordinatorReportResponsePeriod != null
		&& coordinatorReportResponsePeriod.isOpen();
    }

}
