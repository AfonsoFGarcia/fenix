/*
 * Created on 16/Abr/2004
 */
package ServidorApresentacao.Action.teacher;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.ExecutionCourseSiteView;
import DataBeans.InfoLesson;
import DataBeans.InfoProfessorship;
import DataBeans.InfoRoom;
import DataBeans.InfoShift;
import DataBeans.InfoSiteSummaries;
import DataBeans.InfoSiteSummary;
import DataBeans.InfoSummary;
import DataBeans.InfoTeacher;
import DataBeans.SiteView;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import Util.TipoAula;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author T�nia Pous�o
 */
public class SummaryManagerAction extends TeacherAdministrationViewerDispatchAction {
    
    public ActionForward showSummaries(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException {
        
        DynaActionForm actionForm = (DynaActionForm) form;
        
        IUserView userView = SessionUtils.getUserView(request);
        
        Integer executionCourseId = getObjectCode(request);
        request.setAttribute("objectCode", executionCourseId);
        
        Integer lessonType = getLessonType(request);
        
        Integer shiftId = getShiftId(request);
        
        Integer professorShipId = getProfessorShipId(request, actionForm, userView, executionCourseId);
               
        SiteView siteView = getSiteView(userView, executionCourseId, lessonType, shiftId, professorShipId);
        
        processSummaries(siteView);
        
        selectChoices(request,
                ((InfoSiteSummaries) ((ExecutionCourseSiteView) siteView).getComponent()), lessonType);
        
        Collections.sort(((InfoSiteSummaries) ((ExecutionCourseSiteView) siteView).getComponent())
                .getInfoSummaries(), Collections.reverseOrder());
        
        request.setAttribute("siteView", siteView);
        return mapping.findForward("showSummaries");
    }
    
    /**
     * @param siteView
     */
    protected void processSummaries(SiteView siteView) {
        InfoSiteSummaries infoSiteSummaries = (InfoSiteSummaries) siteView.getComponent();
        for (Iterator iter = infoSiteSummaries.getInfoSummaries().iterator(); iter.hasNext();) {
            InfoSummary infoSummary = (InfoSummary) iter.next();
            
            String stuffedSummaryTitle = infoSummary.getTitle().replaceAll("<", "&lt;");
            stuffedSummaryTitle = stuffedSummaryTitle.replaceAll(">", "&gt;");
            String stuffedSummaryBody = infoSummary.getSummaryText().replaceAll("<", "&lt;");
            stuffedSummaryBody = stuffedSummaryBody.replaceAll("<", "&lt;");
            infoSummary.setTitle(stuffedSummaryTitle);
            infoSummary.setSummaryText(stuffedSummaryBody);
        }
    }
    
    /**
     * @param userView
     * @param executionCourseId
     * @param lessonType
     * @param shiftId
     * @param professorShipId
     * @return
     * @throws FenixServiceException
     */
    protected SiteView getSiteView(IUserView userView, Integer executionCourseId, Integer lessonType,
            Integer shiftId, Integer professorShipId) throws FenixServiceException {
        Object[] args = { executionCourseId, lessonType, shiftId, professorShipId };
        SiteView siteView = null;
        siteView = (SiteView) ServiceUtils.executeService(userView, "ReadSummaries", args);
        return siteView;
    }
    
    /**
     * @param request
     * @param actionForm
     * @param userView
     * @param executionCourseId
     * @return
     * @throws FenixServiceException
     */
    protected Integer getProfessorShipId(HttpServletRequest request, DynaActionForm actionForm,
            IUserView userView, Integer executionCourseId) throws FenixServiceException {
        
        Integer professorShipId = null;
        
        if (request.getParameter("byTeacher") != null && request.getParameter("byTeacher").length() > 0) {
            professorShipId = new Integer(request.getParameter("byTeacher"));
        
        } else {
            Object[] args = { userView.getUtilizador(), executionCourseId };
            InfoProfessorship infoProfessorship = (InfoProfessorship) ServiceUtils.executeService(
                    userView, "ReadProfessorshipByTeacherNumberAndExecutionCourseID", args);
            professorShipId = infoProfessorship.getIdInternal();   
            actionForm.set("byTeacher", professorShipId.toString());
        }
        return professorShipId;
    }
    
    /**
     * @param request
     * @return
     */
    protected Integer getShiftId(HttpServletRequest request) {
        Integer shiftId = null;
        if (request.getParameter("byShift") != null && request.getParameter("byShift").length() > 0) {
            shiftId = new Integer(request.getParameter("byShift"));
        }
        return shiftId;
    }
    
    /**
     * @param request
     * @return
     */
    protected Integer getLessonType(HttpServletRequest request) {
        Integer lessonType = null;
        if (request.getParameter("bySummaryType") != null
                && request.getParameter("bySummaryType").length() > 0) {
            lessonType = new Integer(request.getParameter("bySummaryType"));
        }
        return lessonType;
    }
    
    private void selectChoices(HttpServletRequest request, InfoSiteSummaries summaries,
            Integer lessonType) {
        if (lessonType != null && lessonType.intValue() != 0) {
            final TipoAula lessonTypeSelect = new TipoAula(lessonType.intValue());
            List infoShiftsOnlyType = (List) CollectionUtils.select(summaries.getInfoShifts(),
                    new Predicate() {
                
                public boolean evaluate(Object arg0) {
                    return ((InfoShift) arg0).getTipo().equals(lessonTypeSelect);
                }
            });
            
            summaries.setInfoShifts(infoShiftsOnlyType);
        }
    }
    
    public ActionForward prepareInsertSummary(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException {
        HttpSession session = request.getSession(false);
        UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
        
        Integer objectCode = getObjectCode(request);
        request.setAttribute("objectCode", objectCode);
        
        processAnotherDate(request, form);
                
        boolean loggedIsResponsible = false;
        List responsibleTeachers = null;
        Object argsReadResponsibleTeachers[] = { objectCode };
        try {
            responsibleTeachers = (List) ServiceManagerServiceFactory.executeService(userView,
                    "ReadTeachersByExecutionCourseResponsibility", argsReadResponsibleTeachers);
            for (Iterator iter = responsibleTeachers.iterator(); iter.hasNext();) {
                InfoTeacher infoTeacher = (InfoTeacher) iter.next();
                if (infoTeacher.getInfoPerson().getUsername().equals(userView.getUtilizador()))
                    loggedIsResponsible = true;
                break;
            }
            
            request.setAttribute("loggedIsResponsible", new Boolean(loggedIsResponsible));
            
            if (!loggedIsResponsible) {
                InfoTeacher infoTeacher = (InfoTeacher) ServiceManagerServiceFactory.executeService(
                        userView, "ReadTeacherByUsername", new Object[] { userView.getUtilizador() });
                
                InfoProfessorship infoProfessorship = (InfoProfessorship) ServiceManagerServiceFactory
                .executeService(userView, "ReadProfessorshipByTeacherIDandExecutionCourseID",
                        new Object[] { infoTeacher.getIdInternal(), objectCode });
                
                request.setAttribute("loggedTeacherProfessorship", infoProfessorship.getIdInternal());
            }
            
        } catch (Exception e) {
            ActionErrors errors = new ActionErrors();
            errors.add("error", new ActionError("Can't find course's responsible teacher"));
            saveErrors(request, errors);
            return showSummaries(mapping, form, request, response);
        }
        
        Object args[] = { objectCode, userView.getUtilizador() /* userLogged */};
        SiteView siteView = null;
        try {
            siteView = (SiteView) ServiceManagerServiceFactory.executeService(userView,
                    "PrepareInsertSummary", args);
        } catch (Exception e) {
            ActionErrors errors = new ActionErrors();
            errors.add("error", new ActionError("error.summary.impossible.insert"));
            saveErrors(request, errors);
            return showSummaries(mapping, form, request, response);
        }
        if (siteView == null) {
            ActionErrors errors = new ActionErrors();
            errors.add("error", new ActionError("error.summary.impossible.insert"));
            saveErrors(request, errors);
            return showSummaries(mapping, form, request, response);
        }
        request.setAttribute("siteView", siteView);
        
        try {
            choosenShift(request, ((InfoSiteSummaries) siteView.getComponent()).getInfoShifts());
            choosenLesson(request, (InfoSummary) request.getAttribute("summaryToInsert"));
            preSelectTeacherLogged(request, form, (InfoSiteSummaries) siteView.getComponent());
        } catch (Exception e) {
            return showSummaries(mapping, form, request, response);
        }
        
        return mapping.findForward("insertSummary");
    }
    
    /**
     * @param request
     */
    protected void processAnotherDate(HttpServletRequest request, ActionForm form) {
        DynaActionForm actionForm = (DynaActionForm) form;
        String summaryDateInputOption = request.getParameter("summaryDateInputOption");
        
        if(summaryDateInputOption == null || summaryDateInputOption.equals("0")){
            actionForm.set("anotherDateVisible", "true");
        }
        else if(summaryDateInputOption.equals("on")){     //n esta seleccionado
            actionForm.set("anotherDateVisible", "true");
            actionForm.set("summaryDateInput", "");	
        }
        else{
            actionForm.set("anotherDateVisible", "false"); //esta seleccionada
        }
    }


    private void choosenShift(HttpServletRequest request, List infoShifts) {
        if (request.getParameter("shift") != null && request.getParameter("shift").length() > 0) {
            if (infoShifts != null && infoShifts.size() > 0) {
                ListIterator iterator = infoShifts.listIterator();
                while (iterator.hasNext()) {
                    InfoShift infoShift = (InfoShift) iterator.next();
                    if (infoShift.getIdInternal().equals(new Integer(request.getParameter("shift")))) {
                        InfoSummary infoSummaryToInsert = new InfoSummary();
                        infoSummaryToInsert.setInfoShift(infoShift);
                        request.setAttribute("summaryToInsert", infoSummaryToInsert);
                        return;
                    }
                }
            }
        }      
        if (infoShifts != null && infoShifts.size() > 0) {
            InfoSummary infoSummaryToInsert = new InfoSummary();
            infoSummaryToInsert.setInfoShift((InfoShift) infoShifts.get(0));
            request.setAttribute("summaryToInsert", infoSummaryToInsert);
            request.setAttribute("shift", ((InfoShift) infoShifts.get(0)).getIdInternal());
        }
    }
    
    private void choosenLesson(HttpServletRequest request, InfoSummary infoSummary) throws Exception {
        if (request.getParameter("forHidden") != null && request.getParameter("forHidden").length() > 0) {
            request.setAttribute("forHidden", request.getParameter("forHidden"));
        }
        if (request.getAttribute("teste") == null && request.getParameter("lesson") != null && request.getParameter("lesson").length() > 0) {
            if (!request.getParameter("lesson").equals("0")) {
                request.setAttribute("forHidden", "true");
                Integer lessonSelected = new Integer(request.getParameter("lesson"));
                findNextSummaryDate(request, infoSummary, lessonSelected);
                request.setAttribute("datesVisible", "true");
                
            } else {
                request.setAttribute("forHidden", "false");
                request.setAttribute("datesVisible", "false");
            }
        }
    }
    
    /**
     * @param request
     * @param infoSummary
     * @param lessonSelected
     * @throws FenixServiceException
     */
    protected void findNextSummaryDate(HttpServletRequest request, InfoSummary infoSummary, Integer lessonSelected) throws FenixServiceException {
        List lessons = infoSummary.getInfoShift().getInfoLessons();
        for (Iterator iter = lessons.iterator(); iter.hasNext();) {
            InfoLesson element = (InfoLesson) iter.next();
            if (element.getIdInternal().equals(lessonSelected)) {
                GregorianCalendar calendar = new GregorianCalendar();
                InfoSummary summaryBefore;
                IUserView userView = SessionUtils.getUserView(request);
                Object args[] = { getObjectCode(request),
                        infoSummary.getInfoShift().getIdInternal() };
                summaryBefore = (InfoSummary) ServiceManagerServiceFactory.executeService(
                        userView, "ReadLastSummary", args);
                List dates = new ArrayList();
                if (summaryBefore != null) {
                    calendar.setTime(summaryBefore.getSummaryDate().getTime());
                    calendar.set(Calendar.DAY_OF_WEEK, element.getDiaSemana().getDiaSemana()
                            .intValue());
                    calendar.add(Calendar.DATE, 7);
                    dates.add(calendar.getTime());                         
                    request.setAttribute("dates", dates);
                } else
                    request.setAttribute("datesVisible", "false");
                
                break;
            }
        }
    }
    
    private void preSelectTeacherLogged(HttpServletRequest request, ActionForm form,
            InfoSiteSummaries summaries) {
        if (request.getParameter("teacher") == null || request.getParameter("teacher").length() == 0) {
            if (summaries.getTeacherId() != null) {
                DynaActionForm insertSummaryForm = (DynaActionForm) form;
                
                insertSummaryForm.set("teacher", summaries.getTeacherId().toString());
            }
        }
    }
    
    public ActionForward insertSummary(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException {
        try {
            HttpSession session = request.getSession(false);
            IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
            
            Integer executionCourseId = getObjectCode(request);
            request.setAttribute("objectCode", executionCourseId);
            
            InfoSummary infoSummaryToInsert = buildSummaryToInsert(request);
            
            Object[] args = { executionCourseId, infoSummaryToInsert };
            ServiceUtils.executeService(userView, "InsertSummary", args);
            
        } catch (Exception e) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("error.insertSummary", new ActionError((e.getMessage())));
            actionErrors
            .add("error.insertSummary", new ActionError(("error.summary.impossible.insert")));
            saveErrors(request, actionErrors);
            return prepareInsertSummary(mapping, form, request, response);
        }
        
        DynaActionForm actionForm = (DynaActionForm) form;
        Integer saveValue = (Integer) actionForm.get("save");
        
        if(saveValue.equals(new Integer(0)))
            return mapping.findForward("doShowSummariesAction");
        else if(saveValue.equals(new Integer(1))){
            resetActionForm(actionForm, true);
            request.setAttribute("teste", "true");
            return prepareInsertSummary(mapping, actionForm , request, response);
        }
        else{
            resetActionForm(actionForm, false);
            request.setAttribute("teste", "true");
            return prepareInsertSummary(mapping, form , request, response);
        }
    }
    
    /**
     * @param actionForm
     */
    protected void resetActionForm(DynaActionForm actionForm, boolean teste) {        
        if(teste){
            actionForm.set("summaryText", "");
            actionForm.set("title", "");
        }
        actionForm.set("studentsNumber", "");         
        actionForm.set("summaryDateInput", "");
        actionForm.set("summaryDateInputOption", "");
        actionForm.set("summaryHourInput", "");
        actionForm.set("lesson", "");
        actionForm.set("room", "");
    }

    private InfoSummary buildSummaryToInsert(HttpServletRequest request) {
        InfoSummary infoSummary = new InfoSummary();
        
        if (request.getParameter("shift") != null && request.getParameter("shift").length() > 0) {
            InfoShift infoShift = new InfoShift();
            infoShift.setIdInternal(new Integer(request.getParameter("shift")));
            infoSummary.setInfoShift(infoShift);
        }
        
        if (request.getParameter("summaryDateInput") != null
                && request.getParameter("summaryDateInput").length() > 0) {
            String summaryDateString = request.getParameter("summaryDateInput");                
            String[] dateTokens = summaryDateString.split("/");
            Calendar summaryDate = Calendar.getInstance();
            summaryDate.set(Calendar.DAY_OF_MONTH, (new Integer(dateTokens[0])).intValue());
            summaryDate.set(Calendar.MONTH, (new Integer(dateTokens[1])).intValue() - 1);
            summaryDate.set(Calendar.YEAR, (new Integer(dateTokens[2])).intValue());
            infoSummary.setSummaryDate(summaryDate);
        }
        
        //Summary's number of attended student
        if (request.getParameter("studentsNumber") != null
                && request.getParameter("studentsNumber").length() > 0) {
            infoSummary.setStudentsNumber(new Integer(request.getParameter("studentsNumber")));
        }
        
        //lesson extra or not
        if (request.getParameter("lesson") != null && request.getParameter("lesson").length() > 0) {
            Integer lessonId = new Integer(request.getParameter("lesson"));
            //extra lesson
            if (lessonId.equals(new Integer(0))) {
                infoSummary.setIsExtraLesson(Boolean.TRUE);
                
                //Summary's hour
                String summaryHourString = request.getParameter("summaryHourInput");
                String[] hourTokens = summaryHourString.split(":");
                Calendar summaryHour = Calendar.getInstance();
                summaryHour.set(Calendar.HOUR_OF_DAY, (new Integer(hourTokens[0])).intValue());
                summaryHour.set(Calendar.MINUTE, (new Integer(hourTokens[1])).intValue());
                infoSummary.setSummaryHour(summaryHour);
                
                if (request.getParameter("room") != null && request.getParameter("room").length() > 0) {
                    //lesson's room
                    InfoRoom infoRoom = new InfoRoom();
                    infoRoom.setIdInternal(new Integer(request.getParameter("room")));
                    infoSummary.setInfoRoom(infoRoom);
                }
            } else if (lessonId.intValue() >= 0) {
                infoSummary.setIsExtraLesson(Boolean.FALSE);
                
                infoSummary.setLessonIdSelected(lessonId);
            }
        }
        
        if (request.getParameter("teacher") != null && request.getParameter("teacher").length() > 0) {
            Integer teacherId = new Integer(request.getParameter("teacher"));
            if (teacherId.equals(new Integer(0))) //school's teacher
            {
                InfoTeacher infoTeacher = new InfoTeacher();
                infoTeacher.setTeacherNumber(new Integer(request.getParameter("teacherNumber")));
                infoSummary.setInfoTeacher(infoTeacher);
            } else if (teacherId.equals(new Integer(-1))) //external teacher
            {
                infoSummary.setTeacherName(request.getParameter("teacherName"));
            } else { //teacher belong to course
                InfoProfessorship infoProfessorship = new InfoProfessorship();
                infoProfessorship.setIdInternal(teacherId);
                infoSummary.setInfoProfessorship(infoProfessorship);
            }
        }
        
        infoSummary.setTitle(request.getParameter("title"));
        infoSummary.setSummaryText(request.getParameter("summaryText"));
        
        return infoSummary;
    }
    
    public ActionForward prepareEditSummary(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException {
        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        
        String summaryIdString = request.getParameter("summaryCode");
        Integer summaryId = new Integer(summaryIdString);
        
        Integer executionCourseId = getObjectCode(request);
        request.setAttribute("objectCode", executionCourseId);
        
        Object[] args = { executionCourseId, summaryId, userView.getUtilizador() };
        SiteView siteView = null;
        try {
            siteView = (SiteView) ServiceUtils.executeService(userView, "ReadSummary", args);
            
            boolean loggedIsResponsible = false;
            List responsibleTeachers = null;
            Object argsReadResponsibleTeachers[] = { executionCourseId };
            responsibleTeachers = (List) ServiceManagerServiceFactory.executeService(userView,
                    "ReadTeachersByExecutionCourseResponsibility", argsReadResponsibleTeachers);
            for (Iterator iter = responsibleTeachers.iterator(); iter.hasNext();) {
                InfoTeacher infoTeacher = (InfoTeacher) iter.next();
                if (infoTeacher.getInfoPerson().getUsername().equals(userView.getUtilizador()))
                    loggedIsResponsible = true;
                break;
            }
            
            request.setAttribute("loggedIsResponsible", new Boolean(loggedIsResponsible));
            
            if (!loggedIsResponsible) {
                InfoTeacher infoTeacher = (InfoTeacher) ServiceManagerServiceFactory.executeService(
                        userView, "ReadTeacherByUsername", new Object[] { userView.getUtilizador() });
                
                InfoProfessorship infoProfessorship = (InfoProfessorship) ServiceManagerServiceFactory
                .executeService(userView, "ReadProfessorshipByTeacherIDandExecutionCourseID",
                        new Object[] { infoTeacher.getIdInternal(), executionCourseId });
                
                request.setAttribute("loggedTeacherProfessorship", infoProfessorship.getIdInternal());
            } else {
                DynaActionForm summaryForm = (DynaActionForm) form;
                if ((((InfoSiteSummary) siteView.getComponent())).getInfoSummary().getInfoTeacher() != null) {
                    summaryForm.set("teacherNumber", (((InfoSiteSummary) siteView.getComponent()))
                            .getInfoSummary().getInfoTeacher().getTeacherNumber().toString());
                    summaryForm.set("teacher", "0");
                } else if ((((InfoSiteSummary) siteView.getComponent())).getInfoSummary()
                        .getInfoProfessorship() != null) {
                    
                    summaryForm.set("teacher", (((InfoSiteSummary) siteView.getComponent()))
                            .getInfoSummary().getInfoProfessorship().getIdInternal().toString());
                } else {
                    summaryForm.set("teacher", "-1");
                    summaryForm.set("teacherName", (((InfoSiteSummary) siteView.getComponent()))
                            .getInfoSummary().getTeacherName());
                }
            }
            
        } catch (FenixServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("error.editSummary", new ActionError("error.summary.impossible.preedit"));
            actionErrors.add("error.editSummary", new ActionError(e.getMessage()));
            saveErrors(request, actionErrors);
            return showSummaries(mapping, form, request, response);
        }
        if (siteView == null) {
            ActionErrors errors = new ActionErrors();
            errors.add("error", new ActionError("error.summary.impossible.edit"));
            saveErrors(request, errors);
            return showSummaries(mapping, form, request, response);
        }
        
        try {
            shiftChanged(request, siteView);
            choosenLesson(request, (InfoSiteSummary) siteView.getComponent());
        } catch (Exception e) {
            
            return showSummaries(mapping, form, request, response);
        }
        
        request.setAttribute("siteView", siteView);
        
        return mapping.findForward("editSummary");
    }
    
    private void shiftChanged(HttpServletRequest request, SiteView siteView) {
        if (request.getParameter("shift") != null && request.getParameter("shift").length() > 0) {
            List infoShifts = ((InfoSiteSummary) siteView.getComponent()).getInfoShifts();
            ListIterator iterator = infoShifts.listIterator();
            while (iterator.hasNext()) {
                InfoShift infoShift = (InfoShift) iterator.next();
                if (infoShift.getIdInternal().equals(new Integer(request.getParameter("shift")))) {
                    ((InfoSiteSummary) siteView.getComponent()).getInfoSummary().setInfoShift(infoShift);
                }
            }
        }
    }
    
    private void choosenLesson(HttpServletRequest request, InfoSiteSummary siteSummary) {
        if (request.getParameter("lesson") != null && request.getParameter("lesson").length() > 0) {
            if (!request.getParameter("lesson").equals("0")) {
                //normal lesson
                request.setAttribute("forHidden", "true");
                siteSummary.getInfoSummary().setIsExtraLesson(Boolean.FALSE);
                siteSummary.getInfoSummary().setLessonIdSelected(
                        new Integer(request.getParameter("lesson")));
            } else {
                //extra lesson
                request.setAttribute("forHidden", "false");
                siteSummary.getInfoSummary().setIsExtraLesson(Boolean.TRUE);
                siteSummary.getInfoSummary().setLessonIdSelected(new Integer(0));
            }
        } else {
            if (siteSummary.getInfoSummary().getIsExtraLesson().equals(Boolean.TRUE)) {
                request.setAttribute("forHidden", "false");
            } else if (siteSummary.getInfoSummary().getIsExtraLesson().equals(Boolean.FALSE)) {
                request.setAttribute("forHidden", "true");
            }
        }
    }
    
    public ActionForward editSummary(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        try {
            HttpSession session = request.getSession(false);
            IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
            
            String summaryIdString = request.getParameter("summaryCode");
            Integer summaryId = new Integer(summaryIdString);
            
            Integer executionCourseId = getObjectCode(request);
            request.setAttribute("objectCode", executionCourseId);
            
            InfoSummary infoSummaryToEdit = buildSummaryToInsert(request);
            infoSummaryToEdit.setIdInternal(summaryId);
            
            Object[] args = { executionCourseId, infoSummaryToEdit };
            
            ServiceUtils.executeService(userView, "EditSummary", args);
        } catch (Exception e) {
            
            ActionErrors actionErrors = new ActionErrors();
            if (e.getMessage() == null && e instanceof NotAuthorizedException) {
                actionErrors.add("error.editSummary", new ActionError("error.summary.not.authorized"));
            } else {
                actionErrors.add("error.editSummary", new ActionError(e.getMessage()));
            }
            actionErrors.add("error.editSummary", new ActionError("error.summary.impossible.edit"));
            saveErrors(request, actionErrors);
            return prepareEditSummary(mapping, form, request, response);//mudei
        }
        
        return mapping.findForward("doShowSummariesAction");
    }
    
    public ActionForward deleteSummary(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException {
        try {
            HttpSession session = request.getSession(false);
            IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
            
            String summaryIdString = request.getParameter("summaryCode");
            Integer summaryId = new Integer(summaryIdString);
            
            Integer executionCourseId = getObjectCode(request);
            request.setAttribute("objectCode", executionCourseId);
            
            Object[] args = { executionCourseId, summaryId };
            ServiceUtils.executeService(userView, "DeleteSummary", args);
        } catch (Exception e) {
            
            ActionErrors actionErrors = new ActionErrors();
            if (e instanceof NotAuthorizedException) {
                actionErrors.add("error.editSummary", new ActionError("error.summary.not.authorized"));
            }
            actionErrors.add("error.deleteSummary", new ActionError("error.summary.impossible.delete"));
            saveErrors(request, actionErrors);
        }
        return mapping.findForward("doShowSummariesAction");
    }
    
    private Integer getObjectCode(HttpServletRequest request) {
        Integer objectCode = null;
        String objectCodeString = request.getParameter("objectCode");
        if (objectCodeString == null) {
            objectCodeString = (String) request.getAttribute("objectCode");
        }
        if (objectCodeString != null && objectCodeString.length() > 0) {
            objectCode = new Integer(objectCodeString);
        }
        return objectCode;
    }
}