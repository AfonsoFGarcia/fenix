/*
 * Created on Dec 17, 2003
 *  
 */
package ServidorApresentacao.Action.gep;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionYear;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.framework.SearchAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import ServidorApresentacao.mapping.framework.SearchActionMapping;
import Util.TipoCurso;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class SearchTeachersInformationAction extends SearchAction {
    /*
     * (non-Javadoc)
     * 
     * @see ServidorApresentacao.Action.framework.SearchAction#materializeSearchCriteria(ServidorApresentacao.mapping.framework.SearchActionMapping,
     *      javax.servlet.http.HttpServletRequest,
     *      org.apache.struts.action.ActionForm)
     */
    protected void materializeSearchCriteria(SearchActionMapping mapping, HttpServletRequest request,
            ActionForm form) throws Exception {
        IUserView userView = SessionUtils.getUserView(request);

        if (!request.getParameter("executionDegreeId").equals("all")) {
            Integer executionDegreeId = new Integer(request.getParameter("executionDegreeId"));

            Object[] args = { executionDegreeId };
            InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) ServiceUtils.executeService(
                    userView, "ReadExecutionDegreeByOID", args);
            request.setAttribute("infoExecutionDegree", infoExecutionDegree);
        }
        String basic = request.getParameter("basic");
        if (basic != null && basic.length() > 0)
            request.setAttribute("basic", basic);

        request.setAttribute("executionYear", request.getParameter("executionYear"));
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorApresentacao.Action.framework.SearchAction#getSearchServiceArgs(javax.servlet.http.HttpServletRequest,
     *      org.apache.struts.action.ActionForm)
     */
    protected Object[] getSearchServiceArgs(HttpServletRequest request, ActionForm form)
            throws Exception {
        Integer executionDegreeId = null;

        if (!request.getParameter("executionDegreeId").equals("all"))
            executionDegreeId = new Integer(request.getParameter("executionDegreeId"));

        Boolean basic = null;
        if ((request.getParameter("basic") != null) && request.getParameter("basic").equals("true")) {
            basic = Boolean.TRUE;
        }
        if ((request.getParameter("basic") != null) && request.getParameter("basic").equals("false")) {
            basic = Boolean.FALSE;
        }

        String executionYear = request.getParameter("executionYear");

        return new Object[] { executionDegreeId, basic, executionYear };
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorApresentacao.Action.framework.SearchAction#prepareFormConstants(org.apache.struts.action.ActionMapping,
     *      javax.servlet.http.HttpServletRequest,
     *      org.apache.struts.action.ActionForm)
     */
    protected void prepareFormConstants(ActionMapping mapping, HttpServletRequest request,
            ActionForm form) throws Exception {
        IUserView userView = SessionUtils.getUserView(request);

        String executionYear = request.getParameter("executionYear");

        InfoExecutionYear infoExecutionYear = null;
        try {
            if (executionYear != null) {
                Object[] args = { executionYear };

                infoExecutionYear = (InfoExecutionYear) ServiceManagerServiceFactory.executeService(
                        null, "ReadExecutionYear", args);
            } else {
                infoExecutionYear = (InfoExecutionYear) ServiceUtils.executeService(userView,
                        "ReadCurrentExecutionYear", new Object[] {});
            }
        } catch (FenixServiceException e) {
            throw new FenixActionException();
        }

        request.setAttribute("executionYear", infoExecutionYear.getYear());

        Object[] argsExecutionDegrees = { infoExecutionYear, TipoCurso.LICENCIATURA_OBJ };
        List infoExecutionDegrees = (List) ServiceUtils.executeService(userView,
                "ReadExecutionDegreesByExecutionYearAndDegreeType", argsExecutionDegrees);
        Collections.sort(infoExecutionDegrees, new Comparator() {
            public int compare(Object o1, Object o2) {
                InfoExecutionDegree infoExecutionDegree1 = (InfoExecutionDegree) o1;
                InfoExecutionDegree infoExecutionDegree2 = (InfoExecutionDegree) o2;
                return infoExecutionDegree1.getInfoDegreeCurricularPlan().getInfoDegree().getNome()
                        .compareTo(
                                infoExecutionDegree2.getInfoDegreeCurricularPlan().getInfoDegree()
                                        .getNome());
            }
        });

        infoExecutionDegrees = InfoExecutionDegree.buildLabelValueBeansForList(infoExecutionDegrees);

        request.setAttribute("infoExecutionDegrees", infoExecutionDegrees);
        request.setAttribute("showNextSelects", "true");
    }
}