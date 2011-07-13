package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.inquiries.CoordinatorInquiryBean;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.InquiryBlockDTO;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.inquiries.CoordinatorInquiryTemplate;
import net.sourceforge.fenixedu.domain.inquiries.DelegateInquiryTemplate;
import net.sourceforge.fenixedu.domain.inquiries.InquiryBlock;
import net.sourceforge.fenixedu.domain.inquiries.InquiryCoordinatorAnswer;
import net.sourceforge.fenixedu.domain.inquiries.InquiryDelegateAnswer;
import net.sourceforge.fenixedu.domain.inquiries.InquiryRegentAnswer;
import net.sourceforge.fenixedu.domain.inquiries.InquiryTeacherAnswer;
import net.sourceforge.fenixedu.domain.inquiries.RegentInquiryTemplate;
import net.sourceforge.fenixedu.domain.inquiries.TeacherInquiryTemplate;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.pstm.AbstractDomainObject;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(path = "/viewQUCInquiryAnswers", module = "publico")
public class ViewQUCInquiryAnswers extends FenixDispatchAction {

    public ActionForward showCoordinatorInquiry(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	ExecutionDegree executionDegree = AbstractDomainObject.fromExternalId(getFromRequest(request, "executionDegreeOID")
		.toString());
	ExecutionSemester executionSemester = AbstractDomainObject.fromExternalId(getFromRequest(request, "executionPeriodOID")
		.toString());
	CoordinatorInquiryTemplate coordinatorInquiryTemplate = CoordinatorInquiryTemplate
		.getTemplateByExecutionPeriod(executionSemester);
	Coordinator coordinator = AbstractDomainObject.fromExternalId(getFromRequest(request, "coordinatorOID").toString());
	InquiryCoordinatorAnswer inquiryCoordinatorAnswer = coordinator.getInquiryCoordinatorAnswer(executionSemester);

	CoordinatorInquiryBean coordinatorInquiryBean = new CoordinatorInquiryBean(coordinatorInquiryTemplate, coordinator,
		inquiryCoordinatorAnswer, executionSemester);

	Set<InquiryBlockDTO> coordinatorInquiryBlocks = new TreeSet<InquiryBlockDTO>(
		new BeanComparator("inquiryBlock.blockOrder"));
	for (InquiryBlock inquiryBlock : coordinatorInquiryTemplate.getInquiryBlocks()) {
	    coordinatorInquiryBlocks.add(new InquiryBlockDTO(inquiryCoordinatorAnswer, inquiryBlock));
	}

	request.setAttribute("executionPeriod", executionSemester);
	request.setAttribute("executionDegree", executionDegree);
	request.setAttribute("person", coordinator.getPerson());
	request.setAttribute("coordinatorInquiryBlocks", coordinatorInquiryBlocks);

	return new ActionForward(null, "/inquiries/showCoordinatorInquiry.jsp", false, "/coordinator");
    }

    public ActionForward showRegentInquiry(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	Professorship professorship = AbstractDomainObject.fromExternalId(getFromRequest(request, "professorshipOID").toString());

	RegentInquiryTemplate regentInquiryTemplate = RegentInquiryTemplate.getTemplateByExecutionPeriod(professorship
		.getExecutionCourse().getExecutionPeriod());
	InquiryRegentAnswer inquiryRegentAnswer = professorship.getInquiryRegentAnswer();

	Set<InquiryBlockDTO> regentInquiryBlocks = new TreeSet<InquiryBlockDTO>(new BeanComparator("inquiryBlock.blockOrder"));
	for (InquiryBlock inquiryBlock : regentInquiryTemplate.getInquiryBlocks()) {
	    regentInquiryBlocks.add(new InquiryBlockDTO(inquiryRegentAnswer, inquiryBlock));
	}

	request.setAttribute("executionPeriod", professorship.getExecutionCourse().getExecutionPeriod());
	request.setAttribute("executionCourse", professorship.getExecutionCourse());
	request.setAttribute("person", professorship.getPerson());
	request.setAttribute("regentInquiryBlocks", regentInquiryBlocks);

	return new ActionForward(null, "/inquiries/showRegentInquiry.jsp", false, "/teacher");
    }

    public ActionForward showTeacherInquiry(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	Professorship professorship = AbstractDomainObject.fromExternalId(getFromRequest(request, "professorshipOID").toString());

	TeacherInquiryTemplate teacherInquiryTemplate = TeacherInquiryTemplate.getTemplateByExecutionPeriod(professorship
		.getExecutionCourse().getExecutionPeriod());
	InquiryTeacherAnswer inquiryTeacherAnswer = professorship.getInquiryTeacherAnswer();

	Set<InquiryBlockDTO> teacherInquiryBlocks = new TreeSet<InquiryBlockDTO>(new BeanComparator("inquiryBlock.blockOrder"));
	for (InquiryBlock inquiryBlock : teacherInquiryTemplate.getInquiryBlocks()) {
	    teacherInquiryBlocks.add(new InquiryBlockDTO(inquiryTeacherAnswer, inquiryBlock));
	}

	request.setAttribute("executionPeriod", professorship.getExecutionCourse().getExecutionPeriod());
	request.setAttribute("executionCourse", professorship.getExecutionCourse());
	request.setAttribute("person", professorship.getPerson());
	request.setAttribute("teacherInquiryBlocks", teacherInquiryBlocks);

	return new ActionForward(null, "/inquiries/showTeacherInquiry.jsp", false, "/teacher");
    }

    public ActionForward showDelegateInquiry(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	ExecutionCourse executionCourse = AbstractDomainObject.fromExternalId(getFromRequest(request, "executionCourseOID")
		.toString());
	ExecutionDegree executionDegree = AbstractDomainObject.fromExternalId(getFromRequest(request, "executionDegreeOID")
		.toString());

	DelegateInquiryTemplate delegateInquiryTemplate = DelegateInquiryTemplate.getTemplateByExecutionPeriod(executionCourse
		.getExecutionPeriod());
	InquiryDelegateAnswer inquiryDelegateAnswer = null;
	for (InquiryDelegateAnswer delegateAnswer : executionCourse.getInquiryDelegatesAnswers()) {
	    if (delegateAnswer.getExecutionDegree() == executionDegree) {
		inquiryDelegateAnswer = delegateAnswer;
		break;
	    }
	}

	Set<InquiryBlockDTO> delegateInquiryBlocks = new TreeSet<InquiryBlockDTO>(new BeanComparator("inquiryBlock.blockOrder"));
	for (InquiryBlock inquiryBlock : delegateInquiryTemplate.getInquiryBlocks()) {
	    delegateInquiryBlocks.add(new InquiryBlockDTO(inquiryDelegateAnswer, inquiryBlock));
	}

	Integer year = inquiryDelegateAnswer != null ? inquiryDelegateAnswer.getDelegate().getCurricularYear().getYear() : null;
	request.setAttribute("year", year);
	request.setAttribute("executionPeriod", executionCourse.getExecutionPeriod());
	request.setAttribute("executionCourse", executionCourse);
	request.setAttribute("executionDegree", executionDegree);
	request.setAttribute("delegateInquiryBlocks", delegateInquiryBlocks);

	return new ActionForward(null, "/inquiries/showDelegateInquiry.jsp", false, "/delegate");
    }
}
