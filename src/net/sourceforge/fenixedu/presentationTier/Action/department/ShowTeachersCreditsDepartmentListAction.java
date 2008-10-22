/**
 * Dec 20, 2005
 */
package net.sourceforge.fenixedu.presentationTier.Action.department;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadNotClosedExecutionPeriods;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.commons.OrderedIterator;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.credits.CreditLineDTO;
import net.sourceforge.fenixedu.dataTransferObject.credits.TeacherWithCreditsDTO;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacher.Category;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

import pt.ist.fenixWebFramework.security.UserView;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ShowTeachersCreditsDepartmentListAction extends FenixAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws NumberFormatException, FenixFilterException, FenixServiceException, ParseException {

	DynaActionForm dynaActionForm = (DynaActionForm) form;
	IUserView userView = UserView.getUser();

	Integer executionPeriodID = (Integer) dynaActionForm.get("executionPeriodId");

	ExecutionSemester executionSemester = null;
	if (executionPeriodID == null) {
	    executionSemester = ExecutionSemester.readActualExecutionSemester();
	} else {
	    executionSemester = rootDomainObject.readExecutionSemesterByOID(executionPeriodID);
	}

	dynaActionForm.set("executionPeriodId", executionSemester.getIdInternal());

	List<TeacherWithCreditsDTO> teachersCredits = new ArrayList<TeacherWithCreditsDTO>();
	for (Department department : userView.getPerson().getManageableDepartmentCredits()) {

	    List<Teacher> teachers = department.getAllTeachers(executionSemester.getBeginDateYearMonthDay(), executionSemester
		    .getEndDateYearMonthDay());

	    for (Teacher teacher : teachers) {
		double managementCredits = teacher.getManagementFunctionsCredits(executionSemester);
		double serviceExemptionsCredits = teacher.getServiceExemptionCredits(executionSemester);
		double thesesCredits = teacher.getThesesCredits(executionSemester);
		Category category = teacher.getCategoryForCreditsByPeriod(executionSemester);
		int mandatoryLessonHours = teacher.getMandatoryLessonHours(executionSemester);

		TeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionSemester);
		CreditLineDTO creditLineDTO = new CreditLineDTO(executionSemester, teacherService, managementCredits,
			serviceExemptionsCredits, mandatoryLessonHours, teacher, thesesCredits);
		TeacherWithCreditsDTO teacherWithCreditsDTO = new TeacherWithCreditsDTO(teacher, category, creditLineDTO);
		teachersCredits.add(teacherWithCreditsDTO);
	    }
	}
	String sortBy = request.getParameter("sortBy");
	request.setAttribute("teachersCreditsListSize", teachersCredits.size());
	Iterator orderedTeacherCredits = orderList(sortBy, teachersCredits.iterator());
	request.setAttribute("departmentsList", userView.getPerson().getManageableDepartmentCredits());
	request.setAttribute("teachersCreditsList", orderedTeacherCredits);

	readAndSaveAllExecutionPeriods(request);
	return mapping.findForward("show-teachers-credits-list");
    }

    private Iterator orderList(String sortBy, Iterator<TeacherWithCreditsDTO> iterator) {
	Iterator orderedIterator = null;
	if (sortBy == null || sortBy.length() == 0 || sortBy.equals("name")) {
	    orderedIterator = new OrderedIterator(iterator, new BeanComparator("teacher.person.name"));
	} else {
	    orderedIterator = new OrderedIterator(iterator, new BeanComparator("teacher.teacherNumber"));
	}
	return orderedIterator;
    }

    private void readAndSaveAllExecutionPeriods(HttpServletRequest request) throws FenixFilterException, FenixServiceException {
	List<InfoExecutionPeriod> notClosedExecutionPeriods = new ArrayList<InfoExecutionPeriod>();

	notClosedExecutionPeriods = (List<InfoExecutionPeriod>) ReadNotClosedExecutionPeriods.run();

	List<LabelValueBean> executionPeriods = getNotClosedExecutionPeriods(notClosedExecutionPeriods);
	request.setAttribute("executionPeriods", executionPeriods);
    }

    private List<LabelValueBean> getNotClosedExecutionPeriods(List<InfoExecutionPeriod> allExecutionPeriods) {
	List<LabelValueBean> executionPeriods = new ArrayList<LabelValueBean>();
	for (InfoExecutionPeriod infoExecutionPeriod : allExecutionPeriods) {
	    LabelValueBean labelValueBean = new LabelValueBean();
	    labelValueBean.setLabel(infoExecutionPeriod.getInfoExecutionYear().getYear() + " - "
		    + infoExecutionPeriod.getSemester() + "� Semestre");
	    labelValueBean.setValue(infoExecutionPeriod.getIdInternal().toString());
	    executionPeriods.add(labelValueBean);
	}
	Collections.sort(executionPeriods, new BeanComparator("label"));
	return executionPeriods;
    }
}