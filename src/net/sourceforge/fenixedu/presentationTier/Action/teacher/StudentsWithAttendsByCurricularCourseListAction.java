/*
 * Created on Dec 10, 2004
 */
package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoForReadStudentsWithAttendsByExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoFrequenta;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.TeacherAdministrationSiteView;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.util.AttendacyStateSelectionType;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

/**
 * @author Andre Fernandes / Joao Brito
 */
public class StudentsWithAttendsByCurricularCourseListAction extends
        DispatchAction
{
    public ActionForward readStudents(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException {
        DynaActionForm formBean = (DynaActionForm)form;
        HttpSession session = request.getSession(false);
        Integer executionCourseID = null;
        List coursesIDs = null;
        List enrollmentTypeList = null;
        String[] enrollmentType = null;
        List shiftIDs = null;
        try {
            executionCourseID = new Integer(request.getParameter("objectCode"));
        } catch (NumberFormatException ex) {
            //ok, we don't want to view a shift's student list
        }
        
        Integer degreeCurricularPlanID = (Integer) formBean.get("degreeCurricularPlanID");
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);
        
        Integer checkedCoursesIds[] = (Integer[]) formBean.get("coursesIDs");
        enrollmentType = (String[]) formBean.get("enrollmentType");
        Integer checkedShiftIds[] = (Integer[]) formBean.get("shiftIDs");
        
        enrollmentTypeList = new ArrayList();
        for(int i = 0; i < enrollmentType.length; i++){
            if(enrollmentType[i].equals(AttendacyStateSelectionType.ALL.toString())){
                enrollmentTypeList = null;
                break;
            }
            enrollmentTypeList.add(new AttendacyStateSelectionType(enrollmentType[i]));
        }
        
        coursesIDs = new ArrayList();
        for(int i = 0; i < checkedCoursesIds.length; i++){
            if(checkedCoursesIds[i].equals(new Integer(0))){
                coursesIDs=null;
                break;
            }
            coursesIDs.add(checkedCoursesIds[i]);
        }
        
        shiftIDs = new ArrayList();
        for(int i = 0; i < checkedShiftIds.length; i++){
            if(checkedShiftIds[i].equals(new Integer(0))){
                shiftIDs=null;
                break;
            }
            shiftIDs.add(checkedShiftIds[i]);
        }

       
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        Object args[] = { executionCourseID, coursesIDs, enrollmentTypeList, shiftIDs };
        TeacherAdministrationSiteView siteView = null;
        InfoForReadStudentsWithAttendsByExecutionCourse infoDTO = null;

        try {
            siteView = (TeacherAdministrationSiteView) ServiceManagerServiceFactory.executeService(
                    userView, "ReadStudentsWithAttendsByExecutionCourse", args);
            
            infoDTO = (InfoForReadStudentsWithAttendsByExecutionCourse) siteView.getComponent();
            
            List alreadyInsertedStudents = new ArrayList();
            List attendsWithoutDuplicatedStudents = new ArrayList();
            for (Object obj : infoDTO.getInfoAttends()) {
            	Attends attends = (Attends)obj;
				if (!alreadyInsertedStudents.contains(attends.getAluno()))
				{
					alreadyInsertedStudents.add(attends.getAluno());
					attendsWithoutDuplicatedStudents.add(attends);
				}
			}
            infoDTO.setInfoAttends(attendsWithoutDuplicatedStudents);
            
            Collections.sort(infoDTO.getInfoAttends(), new BeanComparator("infoAttends.aluno.number"));

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute("objectCode", executionCourseID);
        request.setAttribute("siteView", siteView);

        DynaActionForm studentsByCurricularCourseForm = (DynaActionForm) form;
        String value = (String) studentsByCurricularCourseForm.get("viewPhoto");
        if (value != null && (value.equals("true") || value.equals("yes") || value.equals("on"))) {
            request.setAttribute("viewPhoto", Boolean.TRUE);
        } else {
            request.setAttribute("viewPhoto", Boolean.FALSE);
        }
        
        

        
        String cbCoursesString[] = new String[checkedCoursesIds.length];
        for (int i = 0; i < checkedCoursesIds.length; i++){
            cbCoursesString[i] = checkedCoursesIds[i].toString();
        }
        
        String cbShiftsString[] = new String[checkedShiftIds.length];
        for (int i = 0; i < checkedShiftIds.length; i++){
            cbShiftsString[i] = checkedShiftIds[i].toString();
        }

        
        Map selectionForSendMailLink = this.getSelection(infoDTO, cbCoursesString,cbShiftsString,enrollmentType);
        selectionForSendMailLink.put("method","start");
        request.setAttribute("sendMailLinkParameters",selectionForSendMailLink);
        Map selectionForSpreadSheet = this.getSelection(infoDTO, cbCoursesString,cbShiftsString,enrollmentType);
        selectionForSpreadSheet.put("method","prepare");
        request.setAttribute("spreadSheetLinkArgs",selectionForSpreadSheet);
        
        return mapping.findForward("success");
    }
    
    /**
	 * @param enrollmentType 
     * @return
	 */
	private Map getSelection(InfoForReadStudentsWithAttendsByExecutionCourse infoDTO,  String cbCoursesString[],String cbShiftsString[], String[] enrollmentType) {
		Map selectionParameters = new HashMap();
		selectionParameters.put("objectCode",infoDTO.getInfoExecutionCourse().getIdInternal());
		selectionParameters.put("method","prepare");
		selectionParameters.put("coursesIDs",cbCoursesString);
		selectionParameters.put("enrollmentType",enrollmentType);
		selectionParameters.put("shiftIDs",cbShiftsString);

        
        return selectionParameters;
        
	}

	public ActionForward prepare(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException {
        HttpSession session = request.getSession(false);
        Integer executionCourseID = null;
        try {
            executionCourseID = new Integer(request.getParameter("objectCode"));
        
        } catch (NumberFormatException ex) {
            //ok, we don't want to view a shift's student list
        }
        
        DynaActionForm formBean = (DynaActionForm)form;
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        
        Integer degreeCurricularPlanID = null;
        if(request.getParameter("degreeCurricularPlanID") != null){
            degreeCurricularPlanID = new Integer(request.getParameter("degreeCurricularPlanID"));
            request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);
        }
        
        // all the information, no filtering applied
        Object args[] = { executionCourseID, null, null, null };
        TeacherAdministrationSiteView siteView = null;
        InfoForReadStudentsWithAttendsByExecutionCourse infoDTO = null;

        try {
            siteView = (TeacherAdministrationSiteView) ServiceManagerServiceFactory.executeService(
                    userView, "ReadStudentsWithAttendsByExecutionCourse", args);

            infoDTO = (InfoForReadStudentsWithAttendsByExecutionCourse) siteView.getComponent();
            // remove duplicated attends
            final Set<InfoFrequenta> attends = new HashSet<InfoFrequenta>(infoDTO.getInfoAttends());
            infoDTO.setInfoAttends(new ArrayList<InfoFrequenta>(attends));
            
            Collections.sort(infoDTO.getInfoAttends(), new BeanComparator("infoAttends.aluno.number"));

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute("siteView", siteView);
        request.setAttribute("objectCode", executionCourseID);
        request.setAttribute("viewPhoto", Boolean.FALSE);
        
        //filling the courses checkboxes in the form-bean
        List infoDCPs = ((InfoForReadStudentsWithAttendsByExecutionCourse)siteView.getComponent()).getInfoDegreeCurricularPlans();
        Integer cbCourses[] = new Integer[infoDCPs.size() + 1];
        String cbCoursesString[] = new String[infoDCPs.size() + 1];
        cbCourses[0] = new Integer(0);
        cbCoursesString[0] = "0";
        for (int i = 1; i < cbCourses.length; i++){
            cbCourses[i] = ((InfoDegreeCurricularPlan)infoDCPs.get(i-1)).getIdInternal();
            cbCoursesString[i] = cbCourses[i].toString();
        }

        //filling the shifts checkboxes in the form-bean
        List infoShifts = ((InfoForReadStudentsWithAttendsByExecutionCourse)siteView.getComponent()).getInfoShifts();
        Integer cbShifts[] = new Integer[infoShifts.size() + 1];
        String cbShiftsString[] = new String[infoShifts.size() + 1];
        cbShifts[0] = new Integer(0);
        cbShiftsString[0] = "0";
        for (int i = 1; i < cbShifts.length; i++){
            cbShifts[i] = ((InfoShift)infoShifts.get(i-1)).getIdInternal();
            cbShiftsString[i] = cbShifts[i].toString();
        }
        
//      filling the enrollment filters checkboxes in the form-bean
        String cbFilters[] = new String[5]; // 4 selection criteria
        cbFilters[0] = AttendacyStateSelectionType.ALL.toString();
        cbFilters[1] = AttendacyStateSelectionType.ENROLLED.toString();
        cbFilters[2] = AttendacyStateSelectionType.SPECIAL_SEASON.toString();
        cbFilters[3] = AttendacyStateSelectionType.NOT_ENROLLED.toString();
        cbFilters[4] = AttendacyStateSelectionType.IMPROVEMENT.toString();        

        formBean.set("coursesIDs",cbCourses);
        formBean.set("shiftIDs",cbShifts);
        formBean.set("enrollmentType",cbFilters);
        
        
        Map sendMailParameters = new HashMap();
        sendMailParameters.put("objectCode",infoDTO.getInfoExecutionCourse().getIdInternal());
        sendMailParameters.put("method","start");
        request.setAttribute("sendMailLinkParameters",sendMailParameters);
        
        Map spreadSheetParameters = new HashMap();
        spreadSheetParameters.put("objectCode",infoDTO.getInfoExecutionCourse().getIdInternal());
        spreadSheetParameters.put("method","prepare");
        spreadSheetParameters.put("coursesIDs",cbCoursesString);
        spreadSheetParameters.put("enrollmentType",cbFilters);
        spreadSheetParameters.put("shiftIDs",cbShiftsString);
        request.setAttribute("spreadSheetLinkArgs",spreadSheetParameters);

        return mapping.findForward("success");
    }
}
