package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceMultipleException;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoShiftEditor;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.ShiftKey;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.base.FenixShiftAndExecutionCourseAndExecutionDegreeAndCurricularYearContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.RequestUtils;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionUtils;
import net.sourceforge.fenixedu.presentationTier.Action.utils.ContextUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

import pt.ist.fenixWebFramework.security.UserView;

/**
 * @author Luis Cruz & Sara Ribeiro
 *  
 */
public class ManageShiftDA extends FenixShiftAndExecutionCourseAndExecutionDegreeAndCurricularYearContextDispatchAction {

    public ActionForward prepareEditShift(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	InfoShift infoShiftToEdit = (InfoShift) request.getAttribute(SessionConstants.SHIFT);

	DynaActionForm editShiftForm = (DynaActionForm) form;
	editShiftForm.set("courseInitials", infoShiftToEdit.getInfoDisciplinaExecucao().getSigla());
	editShiftForm.set("nome", infoShiftToEdit.getNome());        
	editShiftForm.set("lotacao", infoShiftToEdit.getLotacao());

	List<ShiftType> shiftTypes = infoShiftToEdit.getShift().getTypes();
	String[] selectedshiftTypesArray = new String[shiftTypes.size()];        

	for (int i = 0; i < shiftTypes.size(); i++) {
	    ShiftType shiftType = shiftTypes.get(i);
	    selectedshiftTypesArray[i] = shiftType.getName();
	}

	editShiftForm.set("shiftTiposAula", selectedshiftTypesArray);

	SessionUtils.getExecutionCourses(request);

	readAndSetShiftTypes(request, infoShiftToEdit.getInfoDisciplinaExecucao());    

	return mapping.findForward("EditShift");
    }

    public ActionForward listExecutionCourseCourseLoads(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {    

	DynaActionForm editShiftForm = (DynaActionForm) form;
	InfoShift infoShiftToEdit = (InfoShift) request.getAttribute(SessionConstants.SHIFT);
	InfoExecutionCourse infoExecutionCourse = RequestUtils.getExecutionCourseBySigla(request, (String) editShiftForm.get("courseInitials"));

	if(infoShiftToEdit.getInfoDisciplinaExecucao().getExecutionCourse().equals(infoExecutionCourse.getExecutionCourse())) {
	    editShiftForm.set("courseInitials", infoShiftToEdit.getInfoDisciplinaExecucao().getSigla());
	    editShiftForm.set("nome", infoShiftToEdit.getNome());                       

	    List<ShiftType> shiftTypes = infoShiftToEdit.getShift().getTypes();
	    String[] selectedshiftTypesArray = new String[shiftTypes.size()];        
	    for (int i = 0; i < shiftTypes.size(); i++) {
		ShiftType shiftType = shiftTypes.get(i);
		selectedshiftTypesArray[i] = shiftType.getName();
	    }        
	    editShiftForm.set("shiftTiposAula", selectedshiftTypesArray);                        
	    editShiftForm.set("lotacao", infoShiftToEdit.getLotacao());

	} else {
	    editShiftForm.set("shiftTiposAula", new String[] {});
	    editShiftForm.set("lotacao", Integer.valueOf(0));            
	    editShiftForm.set("nome", ""); 
	}

	SessionUtils.getExecutionCourses(request);

	readAndSetShiftTypes(request, infoExecutionCourse);

	return mapping.findForward("EditShift");
    }

    public ActionForward editShift(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	IUserView userView = UserView.getUser();

	DynaActionForm editShiftForm = (DynaActionForm) form;

	InfoShift infoShiftOld = (InfoShift) request.getAttribute(SessionConstants.SHIFT);

	InfoExecutionCourse infoExecutionCourseNew = RequestUtils.getExecutionCourseBySigla(request, (String) editShiftForm.get("courseInitials"));
	InfoShiftEditor infoShiftNew = new InfoShiftEditor();                   
	infoShiftNew.setIdInternal(infoShiftOld.getIdInternal());	    
	infoShiftNew.setInfoDisciplinaExecucao(infoExecutionCourseNew);
	infoShiftNew.setInfoLessons(infoShiftOld.getInfoLessons());
	infoShiftNew.setLotacao((Integer) editShiftForm.get("lotacao"));
	infoShiftNew.setNome((String) editShiftForm.get("nome"));

	String[] selectedShiftTypes = (String[]) editShiftForm.get("shiftTiposAula");
	if (selectedShiftTypes.length == 0) {
	    ActionErrors actionErrors = new ActionErrors();
	    actionErrors.add("errors.shift.types.notSelected", new ActionError("errors.shift.types.notSelected"));
	    saveErrors(request, actionErrors);
	    return mapping.getInputForward();
	}

	final List<ShiftType> shiftTypes = new ArrayList<ShiftType>();
	for (int i = 0; i < selectedShiftTypes.length; i++) {
	    shiftTypes.add(ShiftType.valueOf(selectedShiftTypes[i].toString()));
	}

	infoShiftNew.setTipos(shiftTypes);

	Object argsCriarTurno[] = { infoShiftOld, infoShiftNew };
	try {
	    ServiceUtils.executeService("EditarTurno", argsCriarTurno);

	} catch (DomainException ex) {
	    ActionErrors actionErrors = new ActionErrors();
	    actionErrors.add("error", new ActionError(ex.getMessage()));
	    saveErrors(request, actionErrors);
	    return mapping.getInputForward();

	}

	request.setAttribute(SessionConstants.EXECUTION_COURSE, infoExecutionCourseNew);        
	ContextUtils.setShiftContext(request);

	return prepareEditShift(mapping, form, request, response);
    }

    public ActionForward removeClass(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	ContextUtils.setClassContext(request);

	IUserView userView = UserView.getUser();
	InfoClass infoClass = (InfoClass) request.getAttribute(SessionConstants.CLASS_VIEW);
	InfoShift infoShift = (InfoShift) request.getAttribute(SessionConstants.SHIFT);

	Object argsRemove[] = { infoShift, infoClass };
	ServiceUtils.executeService("RemoverTurno", argsRemove);

	ContextUtils.setShiftContext(request);
	request.removeAttribute(SessionConstants.CLASS_VIEW);

	return prepareEditShift(mapping, form, request, response);
    }

    public ActionForward removeClasses(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	DynaActionForm removeClassesForm = (DynaActionForm) form;
	String[] selectedClasses = (String[]) removeClassesForm.get("selectedItems");

	if (selectedClasses.length == 0) {
	    ActionErrors actionErrors = new ActionErrors();
	    actionErrors.add("errors.classes.notSelected", new ActionError("errors.classes.notSelected"));
	    saveErrors(request, actionErrors);
	    return mapping.getInputForward();
	}

	List<Integer> classOIDs = new ArrayList<Integer>();
	for (int i = 0; i < selectedClasses.length; i++) {
	    classOIDs.add(Integer.valueOf(selectedClasses[i]));
	}

	InfoShift infoShift = (InfoShift) request.getAttribute(SessionConstants.SHIFT);

	Object args[] = { infoShift, classOIDs };
	ServiceUtils.executeService("RemoveClasses", args);

	return mapping.findForward("EditShift");

    }

    public ActionForward deleteLessons(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	DynaActionForm deleteLessonsForm = (DynaActionForm) form;
	String[] selectedLessons = (String[]) deleteLessonsForm.get("selectedItems");

	if (selectedLessons.length == 0) {
	    ActionErrors actionErrors = new ActionErrors();
	    actionErrors.add("errors.lessons.notSelected", new ActionError("errors.lessons.notSelected"));
	    saveErrors(request, actionErrors);
	    return mapping.getInputForward();

	}

	final List<Integer> lessonOIDs = new ArrayList<Integer>();
	for (int i = 0; i < selectedLessons.length; i++) {
	    lessonOIDs.add(Integer.valueOf(selectedLessons[i]));
	}

	final Object args[] = { lessonOIDs };

	try {
	    ServiceUtils.executeService("DeleteLessons", args);

	} catch (FenixServiceMultipleException e) {
	    final ActionErrors actionErrors = new ActionErrors();            
	    for (final DomainException domainException: e.getExceptionList()) {
		actionErrors.add(domainException.getMessage(), new ActionError(domainException.getMessage(), domainException.getArgs()));
	    }
	    saveErrors(request, actionErrors);
	}

	return mapping.findForward("EditShift");
    }

    public ActionForward viewStudentsEnroled(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	InfoShift infoShift = (InfoShift) request.getAttribute(SessionConstants.SHIFT);

	ShiftKey shiftKey = new ShiftKey(infoShift.getNome(), infoShift.getInfoDisciplinaExecucao());

	Object args[] = { shiftKey };
	List<InfoStudent> students = (List<InfoStudent>) ServiceUtils.executeService(
		"LerAlunosDeTurno", args);

	Collections.sort(students, new BeanComparator("number"));

	if (students != null && !students.isEmpty()) {
	    request.setAttribute(SessionConstants.STUDENT_LIST, students);
	}

	InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) request
	.getAttribute(SessionConstants.EXECUTION_COURSE);

	Object args2[] = { infoExecutionCourse };
	List<InfoShift> shifts = (List<InfoShift>) ServiceUtils.executeService(
		"LerTurnosDeDisciplinaExecucao", args2);

	if (shifts != null && !shifts.isEmpty()) {
	    request.setAttribute(SessionConstants.SHIFTS, shifts);
	}

	return mapping.findForward("ViewStudentsEnroled");
    }

    public ActionForward changeStudentsShift(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	IUserView userView = UserView.getUser();

	DynaActionForm dynaActionForm = (DynaActionForm) form;

	Integer oldShiftId = new Integer((String) dynaActionForm.get("oldShiftId"));
	final String newShiftIdString = (String) dynaActionForm.get("newShiftId");
	Integer newShiftId = newShiftIdString == null || newShiftIdString.length() == 0 ? null : Integer.valueOf(newShiftIdString);

	final String[] studentIDs = (String[]) dynaActionForm.get("studentIDs");
	final Set<Registration> registrations = new HashSet<Registration>();
	if (studentIDs != null) {
	    for (final String studentID : studentIDs) {
		final Integer id = Integer.valueOf(studentID);
		final Registration registration = rootDomainObject.readRegistrationByOID(id);
		registrations.add(registration);
	    }
	}

	Object args[] = { userView, oldShiftId, newShiftId, registrations };
	ServiceUtils.executeService("ChangeStudentsShift", args);

	return mapping.findForward("Continue");
    }

    private void readAndSetShiftTypes(HttpServletRequest request, InfoExecutionCourse infoExecutionCourse) {	           
	final List<LabelValueBean> tiposAula = new ArrayList<LabelValueBean>();
	final ResourceBundle bundle = ResourceBundle.getBundle("resources.EnumerationResources", request.getLocale());
	for (final ShiftType shiftType : infoExecutionCourse.getExecutionCourse().getShiftTypes()) {
	    tiposAula.add(new LabelValueBean(bundle.getString(shiftType.getName()), shiftType.name()));
	}
	request.setAttribute("tiposAula", tiposAula);
    }
}