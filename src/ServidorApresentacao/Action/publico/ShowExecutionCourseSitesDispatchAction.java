/*
 * Created 2004/11/5
 * 
 */
package ServidorApresentacao.Action.publico;

import java.text.Collator;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.ExecutionCourseView;
import DataBeans.InfoDegree;
import DataBeans.InfoExecutionPeriod;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixContextDispatchAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

import commons.collections.Table;

import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Luis Cruz
 */
public class ShowExecutionCourseSitesDispatchAction extends FenixContextDispatchAction {

    public ActionForward listSites(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        Integer degreeOID = new Integer(request.getParameter("degreeOID"));
        request.setAttribute("degreeID", degreeOID);

        List executionCourseViews = getExecutionCourseViews(request, degreeOID);
        
        getInfoDegreeCurricularPlan(request, degreeOID);

        InfoExecutionPeriod infoExecutionPeriod = getPreviouseExecutionPeriod(request);
        organizeExecutionCourseViews(request, executionCourseViews, infoExecutionPeriod);

        return mapping.findForward("show-exeution-course-site-list");
    }

    /**
     * @param request
     * @param executionCourseViews
     * @param previousExecutionPeriod
     */
    private void organizeExecutionCourseViews(HttpServletRequest request, List executionCourseViews,
            InfoExecutionPeriod previousExecutionPeriod) {

        Table executionCourseViewsTableCurrent1_2 	= new Table(2);      
        Table executionCourseViewsTableCurrent3_4 	= new Table(2); 
        Table executionCourseViewsTableCurrent5   	= new Table(1); 
        Table executionCourseViewsTablePrevious1_2 = new Table(2);
        Table executionCourseViewsTablePrevious3_4 = new Table(2);
        Table executionCourseViewsTablePrevious5 	= new Table(1);

        for (Iterator iterator=executionCourseViews.iterator(); iterator.hasNext(); ) {
        		ExecutionCourseView executionCourseView = (ExecutionCourseView) iterator.next();    

        		int curricularYear = executionCourseView.getCurricularYear().intValue();
        		boolean previousExecPeriod = executionCourseView.getExecutionPeriodOID().equals(previousExecutionPeriod.getIdInternal());
        		switch (curricularYear) {
        			case 1:
        			case 2:
        				if (previousExecPeriod) {  
            				executionCourseViewsTablePrevious1_2.appendToColumn(curricularYear-1,executionCourseView);
            			} else {
            				executionCourseViewsTableCurrent1_2.appendToColumn(curricularYear-1,executionCourseView);
            			}
        				break;
        			case 3:
        			case 4:

        				if (previousExecPeriod) {
        					executionCourseViewsTablePrevious3_4.appendToColumn(curricularYear-3,executionCourseView);
        				} else {
        					executionCourseViewsTableCurrent3_4.appendToColumn(curricularYear-3,executionCourseView);
        				}
        				break;
        			case 5:
        				if (previousExecPeriod) {
        					executionCourseViewsTablePrevious5.appendToColumn(curricularYear-5,executionCourseView);
        				} else {
        					executionCourseViewsTableCurrent5.appendToColumn(curricularYear-5,executionCourseView);
        				}
        				break;
        		}
        	}
        				
        request.setAttribute("executionCourseViewsTableCurrent1_2", executionCourseViewsTableCurrent1_2);
        request.setAttribute("executionCourseViewsTableCurrent3_4", executionCourseViewsTableCurrent3_4);
        request.setAttribute("executionCourseViewsTableCurrent5", executionCourseViewsTableCurrent5);
        
        request.setAttribute("executionCourseViewsTablePrevious1_2", executionCourseViewsTablePrevious1_2);
        request.setAttribute("executionCourseViewsTablePrevious3_4", executionCourseViewsTablePrevious3_4);
        request.setAttribute("executionCourseViewsTablePrevious5", executionCourseViewsTablePrevious5);
    }
    
    private InfoExecutionPeriod getPreviouseExecutionPeriod(HttpServletRequest request) throws FenixServiceException {
        InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request.getAttribute(SessionConstants.EXECUTION_PERIOD);

        Object[] args = { infoExecutionPeriod.getIdInternal() };

        InfoExecutionPeriod previousInfoExecutionPeriod = (InfoExecutionPeriod) ServiceUtils.executeService(null,
                "ReadPreviousExecutionPeriod", args);
        
        request.setAttribute("previousInfoExecutionPeriod", previousInfoExecutionPeriod);
        return previousInfoExecutionPeriod;
    }

    private List getExecutionCourseViews(HttpServletRequest request, Integer degreeOID) throws FenixServiceException {
        Object[] args = { degreeOID };

        List executionCourseViews = (List) ServiceManagerServiceFactory.executeService(
                null, "ReadExecutionCoursesForCurrentAndPreviousPeriodByDegree", args);
        BeanComparator beanComparator = new BeanComparator("executionCourseName", Collator.getInstance());
        Collections.sort(executionCourseViews, beanComparator);

        //request.setAttribute("executionCourseViews", executionCourseViews);
        return executionCourseViews;        
    }

    private void getInfoDegreeCurricularPlan(HttpServletRequest request, Integer degreeOID) throws FenixServiceException {
        Object[] args = { degreeOID };

        InfoDegree infoDegree = (InfoDegree)
        		ServiceManagerServiceFactory.executeService(null, "ReadDegreeByOID", args);

        request.setAttribute("infoDegree", infoDegree);
    }

}