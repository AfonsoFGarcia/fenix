package net.sourceforge.fenixedu.presentationTier.Action.delegate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.VariantBean;
import net.sourceforge.fenixedu.dataTransferObject.delegate.DelegateCurricularCourseBean;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeModuleScope;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.DelegateCurricularCourseStudentsGroup;
import net.sourceforge.fenixedu.domain.accessControl.DelegateStudentsGroup;
import net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.injectionCode.IGroup;
import net.sourceforge.fenixedu.presentationTier.Action.cms.messaging.mailSender.MailBean;
import net.sourceforge.fenixedu.presentationTier.Action.cms.messaging.mailSender.SimpleMailSenderAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class SendEmailToDelegateStudents extends SimpleMailSenderAction {

    @Override
    protected List<IGroup> getPossibleReceivers(HttpServletRequest request) {
	return getPossibleReceivers(request, null);
    }

    protected List<IGroup> getPossibleReceivers(HttpServletRequest request, ExecutionYear executionYear) {
	List<IGroup> groups = super.getPossibleReceivers(request);

	final Person person = getLoggedPerson(request);

	PersonFunction delegateFunction = null;
	if (person.hasStudent()) {
	    final Student student = person.getStudent();
	    final Degree degree = student.getLastActiveRegistration().getDegree();
	    delegateFunction = degree.getMostSignificantDelegateFunctionForStudent(student, executionYear);
	} else {
	    delegateFunction = person.getActiveGGAEDelegatePersonFunction();
	}

	if (delegateFunction != null) {
	    if (request.getAttribute("curricularCoursesList") != null) {
		executionYear = executionYear == null ? ExecutionYear.getExecutionYearByDate(delegateFunction.getBeginDate())
			: executionYear;

		List<CurricularCourse> curricularCourses = (List<CurricularCourse>) request.getAttribute("curricularCoursesList");
		for (CurricularCourse curricularCourse : curricularCourses) {
		    groups.add(new DelegateCurricularCourseStudentsGroup(curricularCourse, executionYear));
		}
	    } else {
		if (delegateFunction != null
			&& delegateFunction.getFunction().getFunctionType().equals(FunctionType.DELEGATE_OF_GGAE)) {
		    groups.add(new DelegateStudentsGroup(delegateFunction));
		} else if (delegateFunction != null
			&& delegateFunction.getFunction().getFunctionType().equals(FunctionType.DELEGATE_OF_YEAR)) {
		    groups.add(new DelegateStudentsGroup(delegateFunction));
		} else {
		    groups.add(new DelegateStudentsGroup(delegateFunction, FunctionType.DELEGATE_OF_YEAR));
		    groups.add(new DelegateStudentsGroup(delegateFunction));
		}
	    }
	}

	return groups;
    }

    @Override
    protected MailBean createMailBean(HttpServletRequest request) {
	return createMailBean(request, null);
    }

    protected MailBean createMailBean(HttpServletRequest request, ExecutionYear executionYear) {
	MailBean bean = new MailBean();

	Person person = getLoggedPerson(request);
	bean.setFromName(person.getName());
	bean.setFromAddress(person.getEmail());

	if (request.getAttribute("curricularCoursesList") != null) {
	    bean.setReceiversOptions(getPossibleReceivers(request, executionYear));
	    bean.setReceiversGroupList(bean.getReceiversOptions());
	} else {
	    bean.setReceiversOptions(getPossibleReceivers(request, executionYear));
	}

	return bean;
    }

    @Override
    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	RenderUtils.invalidateViewState();
	String year = request.getParameter("year");
	ExecutionYear executionYear = year == null ? ExecutionYear.readCurrentExecutionYear() : ExecutionYear
		.readExecutionYearByName(year);
	return prepare(mapping, actionForm, request, response, executionYear);
    }

    private ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response, ExecutionYear executionYear) throws Exception {
	MailBean bean = createMailBean(request, executionYear);
	request.setAttribute("mailBean", bean);
	VariantBean variantBean = new VariantBean();
	variantBean.setDomainObject(executionYear);
	request.setAttribute("currentExecutionYear", variantBean);
	return mapping.findForward("compose-mail");
    }

    private ActionForward prepareSendToStudentsFromSelectedCurricularCourses(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response, ExecutionYear executionYear) throws Exception {
	final Person person = getLoggedPerson(request);
	PersonFunction delegateFunction = null;
	if (person.hasStudent()) {
	    final Student student = person.getStudent();
	    final Degree degree = student.getLastActiveRegistration().getDegree();
	    delegateFunction = degree.getMostSignificantDelegateFunctionForStudent(student, executionYear);
	} else {
	    delegateFunction = person.getActiveGGAEDelegatePersonFunction();
	}

	if (delegateFunction != null) {
	    request.setAttribute("curricularCoursesList", getCurricularCourses(person, executionYear));
	} else {
	    addActionMessage(request, "error.delegates.sendMail.notExistentDelegateFunction");
	}

	VariantBean variantBean = new VariantBean();
	variantBean.setDomainObject(executionYear);
	request.setAttribute("currentExecutionYear", variantBean);

	return mapping.findForward("choose-receivers");
    }

    public ActionForward prepareSendToStudentsFromSelectedCurricularCourses(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	String year = request.getParameter("year");
	ExecutionYear executionYear = year != null ? ExecutionYear.readExecutionYearByName(year) : ExecutionYear
		.readCurrentExecutionYear();
	return prepareSendToStudentsFromSelectedCurricularCourses(mapping, actionForm, request, response, executionYear);
    }

    public ActionForward sendToStudentsFromSelectedCurricularCourses(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	String year = request.getParameter("year");
	ExecutionYear executionYear = year != null ? ExecutionYear.readExecutionYearByName(year) : ExecutionYear
		.readCurrentExecutionYear();

	final List<String> selectedCurricularCourses = Arrays.asList(request.getParameterValues("selectedCurricularCourses"));
	List<CurricularCourse> curricularCourses = new ArrayList<CurricularCourse>();
	for (String curricularCourseId : selectedCurricularCourses) {
	    Integer curricularId = Integer.parseInt(curricularCourseId);
	    CurricularCourse curricularCourse = (CurricularCourse) rootDomainObject.readDegreeModuleByOID(curricularId);
	    curricularCourses.add(curricularCourse);
	}

	if (!curricularCourses.isEmpty()) {
	    request.setAttribute("curricularCoursesList", curricularCourses);
	    return prepare(mapping, actionForm, request, response, executionYear);

	} else {
	    addActionMessage(request, "error.delegates.sendMail.curricularCoursesNotSelected");
	    RenderUtils.invalidateViewState("selectedCurricularCourses");
	    return prepareSendToStudentsFromSelectedCurricularCourses(mapping, actionForm, request, response);
	}
    }

    @Override
    public ActionForward send(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	request.setAttribute("currentExecutionYear", ExecutionYear.readCurrentExecutionYear());

	return super.send(mapping, actionForm, request, response);
    }

    @Override
    public ActionForward sendInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	MailBean bean = createMailBean(request);
	request.setAttribute("mailBean", bean);
	request.setAttribute("currentExecutionYear", ExecutionYear.readCurrentExecutionYear());
	return prepare(mapping, actionForm, request, response);
    }

    /*
     * AUXILIARY METHODS
     */

    private PersonFunction getPersonFunction(Person person, ExecutionYear executionYear) {
	if (person.hasStudent()) {
	    final Student student = person.getStudent();
	    final Degree degree = student.getLastActiveRegistration().getDegree();
	    return degree.getMostSignificantDelegateFunctionForStudent(student, executionYear);
	} else {
	    return person.getActiveGGAEDelegatePersonFunction();
	}
    }

    private Set<CurricularCourse> getDegreesCurricularCoursesFromCoordinatorRoles(List<Coordinator> coordinators,
	    ExecutionYear executionYear) {
	Set<CurricularCourse> curricularCourses = new HashSet<CurricularCourse>();
	for (Coordinator coordinator : coordinators) {
	    final Degree degree = coordinator.getExecutionDegree().getDegree();
	    curricularCourses.addAll(degree.getAllCurricularCourses(executionYear));
	}
	return curricularCourses;
    }

    private List<DelegateCurricularCourseBean> getCurricularCourses(final Person person, ExecutionYear executionYear) {
	List<DelegateCurricularCourseBean> result = new ArrayList<DelegateCurricularCourseBean>();
	executionYear = executionYear == null ? ExecutionYear.readCurrentExecutionYear() : executionYear;
	final PersonFunction delegateFunction = getPersonFunction(person, executionYear);
	if (delegateFunction != null) {
	    if (person.hasStudent()) {
		Set<CurricularCourse> curricularCourses = person.getStudent().getCurricularCoursesResponsibleForByFunctionType(
			delegateFunction.getFunction().getFunctionType(), executionYear);
		return getCurricularCoursesBeans(delegateFunction, curricularCourses, executionYear);
	    } else if (person.hasAnyCoordinators()) {
		Set<CurricularCourse> curricularCourses = getDegreesCurricularCoursesFromCoordinatorRoles(person
			.getCoordinators(), executionYear);
		return getCurricularCoursesBeans(delegateFunction, curricularCourses, executionYear);
	    }
	}
	return result;
    }

    private List<DelegateCurricularCourseBean> getCurricularCoursesBeans(PersonFunction delegateFunction,
	    Set<CurricularCourse> curricularCourses, ExecutionYear executionYear) {
	final FunctionType delegateFunctionType = delegateFunction.getFunction().getFunctionType();

	List<DelegateCurricularCourseBean> result = new ArrayList<DelegateCurricularCourseBean>();

	for (CurricularCourse curricularCourse : curricularCourses) {
	    for (ExecutionSemester executionSemester : executionYear.getExecutionPeriods()) {
		if (curricularCourse.hasAnyExecutionCourseIn(executionSemester)) {
		    for (DegreeModuleScope scope : curricularCourse.getDegreeModuleScopes()) {
			if (!scope.getCurricularSemester().equals(executionSemester.getSemester())) {
			    continue;
			}

			if (delegateFunctionType.equals(FunctionType.DELEGATE_OF_YEAR)
				&& !scopeBelongsToDelegateCurricularYear(scope, delegateFunction.getCurricularYear().getYear())) {
			    continue;
			}
			DelegateCurricularCourseBean bean = new DelegateCurricularCourseBean(curricularCourse, executionYear,
				scope.getCurricularYear(), executionSemester);
			bean.calculateEnrolledStudents();
			result.add(bean);
		    }
		}

	    }
	}
	Collections.sort(result,
		DelegateCurricularCourseBean.CURRICULAR_COURSE_COMPARATOR_BY_CURRICULAR_YEAR_AND_CURRICULAR_SEMESTER);

	return result;
    }

    private boolean scopeBelongsToDelegateCurricularYear(DegreeModuleScope scope, Integer curricularYear) {
	if (scope.getCurricularYear().equals(curricularYear)) {
	    return true;
	}
	return false;
    }

    private List<Student> getStudentsEnrolledIn(CurricularCourse curricularCourse, ExecutionYear executionYear) {
	List<Student> result = new ArrayList<Student>();
	for (Enrolment enrolment : curricularCourse.getEnrolmentsByExecutionYear(executionYear)) {
	    Registration registration = enrolment.getRegistration();
	    result.add(registration.getStudent());
	}
	return result;
    }

    public ActionForward chooseExecutionYear(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	VariantBean variantBean = (VariantBean) getRenderedObject("chooseExecutionYear");
	RenderUtils.invalidateViewState();
	return prepare(mapping, actionForm, request, response, (ExecutionYear) variantBean.getDomainObject());
    }

    public ActionForward chooseExecutionYearCurricularCourseList(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	VariantBean variantBean = (VariantBean) getRenderedObject("chooseExecutionYear");
	RenderUtils.invalidateViewState();
	return prepareSendToStudentsFromSelectedCurricularCourses(mapping, actionForm, request, response,
		(ExecutionYear) variantBean.getDomainObject());
    }

}
