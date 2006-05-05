/*
 * Created on May 5, 2006
 */
package net.sourceforge.fenixedu.presentationTier.Action.degreeAdministrativeOffice.gradeSubmission;

import java.text.ParseException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetManagementSearchBean;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetSearchResultBean;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.MarkSheetState;
import net.sourceforge.fenixedu.domain.MarkSheetType;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;
import net.sourceforge.fenixedu.util.DateFormatUtil;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

public class MarkSheetSearchDispatchAction extends MarkSheetDispatchAction {
    
    public ActionForward prepareSearchMarkSheet(ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        
        MarkSheetManagementSearchBean markSheetManagementSearchBean = new MarkSheetManagementSearchBean();
        markSheetManagementSearchBean.setExecutionPeriod(ExecutionPeriod.readActualExecutionPeriod());
        request.setAttribute("edit", markSheetManagementSearchBean);
        
        return mapping.findForward("searchMarkSheet");
    }
    
    public ActionForward prepareSearchMarkSheetFilled(ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        
        MarkSheetManagementSearchBean markSheetBean = new MarkSheetManagementSearchBean();
        fillMarkSheetSearchBean(actionForm, request, markSheetBean);
        
        return searchMarkSheets(mapping, actionForm, request, response, markSheetBean);
    }
    
    public ActionForward searchMarkSheets(ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {

        MarkSheetManagementSearchBean searchBean = (MarkSheetManagementSearchBean) RenderUtils.getViewState().getMetaObject().getObject();
        return searchMarkSheets(mapping, actionForm, request, response, searchBean);
    }

    private ActionForward searchMarkSheets(ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request, HttpServletResponse response, MarkSheetManagementSearchBean searchBean) {
      
        ActionMessages actionMessages = createActionMessages();        
        try {
            Map<MarkSheetType, MarkSheetSearchResultBean> result = (Map<MarkSheetType, MarkSheetSearchResultBean>) ServiceUtils
                    .executeService(getUserView(request), "SearchMarkSheets",
                            new Object[] { searchBean });

            request.setAttribute("edit", searchBean);
            request.setAttribute("searchResult", result);
            request.setAttribute("url", buildUrl(searchBean));

        } catch (FenixFilterException e) {
            addMessage(request, actionMessages, "error.notAuthorized");
        } catch (FenixServiceException e) {
            addMessage(request, actionMessages, e.getMessage());
        }
        return mapping.getInputForward();

    }
    
    protected void fillMarkSheetSearchBean(ActionForm actionForm, HttpServletRequest request, MarkSheetManagementSearchBean markSheetBean) {
        DynaActionForm form = (DynaActionForm) actionForm;
        fillMarkSheetBean(actionForm, request, markSheetBean);
        
        if(form.getString("tn") != null && form.getString("tn").length() != 0) {            
            markSheetBean.setTeacherNumber(Integer.valueOf(form.getString("tn")));
        }
        try {
            markSheetBean.setEvaluationDate(DateFormatUtil.parse("dd/MM/yyyy", form.getString("ed")));
        } catch (ParseException e) {
            markSheetBean.setEvaluationDate(null);
        }
        if(form.getString("mss") != null && form.getString("mss").length() != 0) {
            markSheetBean.setMarkSheetState(MarkSheetState.valueOf(form.getString("mss")));
        }
        if(form.getString("mst") != null && form.getString("mst").length() != 0) {
            markSheetBean.setMarkSheetType(MarkSheetType.valueOf(form.getString("mst")));
        }
    }

    private String buildUrl(MarkSheetManagementSearchBean searchBean) {

        StringBuilder stringBuilder = new StringBuilder();
        
        stringBuilder.append("&epID=").append(searchBean.getExecutionPeriod().getIdInternal());
        stringBuilder.append("&dID=").append(searchBean.getDegree().getIdInternal());
        stringBuilder.append("&dcpID=").append(searchBean.getDegreeCurricularPlan().getIdInternal());
        stringBuilder.append("&ccID=").append(searchBean.getCurricularCourse().getIdInternal());
        
        if(searchBean.getTeacherNumber() != null) {
            stringBuilder.append("&tn=").append(searchBean.getTeacherNumber());
        }
        if(searchBean.getEvaluationDate() != null) {
            stringBuilder.append("&ed=").append(DateFormatUtil.format("dd/MM/yyyy", searchBean.getEvaluationDate()));
        }
        if (searchBean.getMarkSheetState() != null) {
            stringBuilder.append("&mss=").append(searchBean.getMarkSheetState().getName());
        }
        if (searchBean.getMarkSheetType() != null) {
            stringBuilder.append("&mst=").append(searchBean.getMarkSheetType().getName());
        }
        return stringBuilder.toString();
    }

}
