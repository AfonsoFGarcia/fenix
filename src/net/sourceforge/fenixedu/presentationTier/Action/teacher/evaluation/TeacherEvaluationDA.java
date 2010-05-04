package net.sourceforge.fenixedu.presentationTier.Action.teacher.evaluation;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.teacher.evaluation.FacultyEvaluationProcess;
import net.sourceforge.fenixedu.domain.teacher.evaluation.FacultyEvaluationProcessBean;
import net.sourceforge.fenixedu.domain.teacher.evaluation.TeacherEvaluationProcess;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "researcher", path = "/teacherEvaluation")
@Forwards( { @Forward(name = "viewAutoEvaluation", path = "/teacher/evaluation/viewAutoEvaluation.jsp"),
	@Forward(name = "viewEvaluation", path = "/teacher/evaluation/viewEvaluation.jsp"),
	@Forward(name = "viewManagementInterface", path = "/teacher/evaluation/viewManagementInterface.jsp") })
public class TeacherEvaluationDA extends FenixDispatchAction {

    public ActionForward viewAutoEvaluation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	SortedSet<TeacherEvaluationProcess> openProcesses = new TreeSet<TeacherEvaluationProcess>(
		TeacherEvaluationProcess.COMPARATOR_BY_INTERVAL);
	final Set<FacultyEvaluationProcess> facultyEvaluationProcessSet = rootDomainObject.getFacultyEvaluationProcessSet();
	for (FacultyEvaluationProcess process : facultyEvaluationProcessSet) {
	    if (process.getAutoEvaluationInterval().getStart().isBeforeNow()) {
		for (TeacherEvaluationProcess teacherProcess : process.getTeacherEvaluationProcessSet()) {
		    openProcesses.add(teacherProcess);
		}
	    }
	}
	request.setAttribute("openProcesses", openProcesses);
	return mapping.findForward("viewAutoEvaluation");
    }

    public ActionForward viewEvaluation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	return mapping.findForward("viewEvaluation");
    }

    public ActionForward viewManagementInterface(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final Set<FacultyEvaluationProcess> facultyEvaluationProcessSet = rootDomainObject.getFacultyEvaluationProcessSet();
	request.setAttribute("facultyEvaluationProcessSet", facultyEvaluationProcessSet);
	return mapping.findForward("viewManagementInterface");
    }

    public ActionForward prepareCreateFacultyEvaluationProcess(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	final FacultyEvaluationProcessBean facultyEvaluationProcessBean = new FacultyEvaluationProcessBean();
	request.setAttribute("facultyEvaluationProcessCreationBean", facultyEvaluationProcessBean);
	return mapping.findForward("viewManagementInterface");
    }

    public ActionForward createFacultyEvaluationProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final FacultyEvaluationProcessBean facultyEvaluationProcessBean = (FacultyEvaluationProcessBean) getRenderedObject();
	final FacultyEvaluationProcess facultyEvaluationProcess = facultyEvaluationProcessBean.create();
	request.setAttribute("facultyEvaluationProcess", facultyEvaluationProcess);
	return mapping.findForward("viewManagementInterface");
    }

    public ActionForward prepareEditFacultyEvaluationProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final FacultyEvaluationProcess facultyEvaluationProcess = getDomainObject(request, "facultyEvaluationProcessOID");
	final FacultyEvaluationProcessBean facultyEvaluationProcessBean = new FacultyEvaluationProcessBean(
		facultyEvaluationProcess);
	request.setAttribute("facultyEvaluationProcessEditnBean", facultyEvaluationProcessBean);
	return mapping.findForward("viewManagementInterface");
    }

    public ActionForward editFacultyEvaluationProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final FacultyEvaluationProcessBean facultyEvaluationProcessBean = (FacultyEvaluationProcessBean) getRenderedObject();
	facultyEvaluationProcessBean.edit();
	final FacultyEvaluationProcess facultyEvaluationProcess = facultyEvaluationProcessBean.getFacultyEvaluationProcess();
	request.setAttribute("facultyEvaluationProcess", facultyEvaluationProcess);
	return mapping.findForward("viewManagementInterface");
    }

    public ActionForward viewFacultyEvaluationProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final FacultyEvaluationProcess facultyEvaluationProcess = getDomainObject(request, "facultyEvaluationProcessOID");
	request.setAttribute("facultyEvaluationProcess", facultyEvaluationProcess);
	return mapping.findForward("viewManagementInterface");
    }

}
