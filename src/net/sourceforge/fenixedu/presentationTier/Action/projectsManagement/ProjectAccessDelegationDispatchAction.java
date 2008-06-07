/*
 * Created on Jan 11, 2005
 */

package net.sourceforge.fenixedu.presentationTier.Action.projectsManagement;

import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.comparators.CalendarDateComparator;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoProjectAccess;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.security.UserView;

/**
 * @author Susana Fernandes
 */
public class ProjectAccessDelegationDispatchAction extends FenixDispatchAction {

    public ActionForward showProjectsAccesses(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixServiceException, FenixFilterException {
        final IUserView userView = UserView.getUser();
        final String costCenter = request.getParameter("costCenter");
        getCostCenterName(request, costCenter);
        try {
            final List projectsAccessesList = (List) ServiceManagerServiceFactory.executeService( "ReadProjectAccesses", new Object[] {
                    userView.getUtilizador(), costCenter });
            request.setAttribute("projectsAccessesList", projectsAccessesList);
        } catch (InvalidArgumentsServiceException e) {
        }
        return mapping.findForward("showProjectsAccesses");
    }

    public ActionForward choosePerson(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixServiceException, FenixFilterException {
        final IUserView userView = UserView.getUser();
        final String costCenter = request.getParameter("costCenter");
        getCostCenterName(request, costCenter);
        final List projectList = (List) ServiceUtils.executeService("ReadUserProjects", new Object[] { userView.getUtilizador(),
                costCenter, new Boolean(false) });
        if (projectList == null || projectList.size() == 0)
            request.setAttribute("noUserProjects", new Boolean(true));
        return mapping.findForward("choosePerson");
    }

    public ActionForward showPersonAccesses(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixServiceException, FenixFilterException {
        final IUserView userView = UserView.getUser();
        final String username = (String) ((DynaActionForm) form).get("username");
        request.setAttribute("username", username);
        final String costCenter = request.getParameter("costCenter");
        getCostCenterName(request, costCenter);
        if (username != null && !username.equals("")) {
            try {
                if (!username.equalsIgnoreCase(userView.getUtilizador())) {
                    final List personAccessesList = (List) ServiceManagerServiceFactory.executeService( "ReadProjectAccesses", new Object[] {
                            userView.getUtilizador(), costCenter, username });
                    request.setAttribute("personAccessesList", personAccessesList);
                    if (personAccessesList == null || personAccessesList.size() == 0) {
                        final InfoPerson infoPerson = (InfoPerson) ServiceManagerServiceFactory.executeService(
                                "ReadPersonToDelegateAccess", new Object[] { userView.getUtilizador(), costCenter, username });
                        request.setAttribute("infoPerson", infoPerson);
                    }

                    final List projectList = (List) ServiceUtils.executeService("ReadProjectWithoutPersonAccess", new Object[] {
                            userView.getUtilizador(), costCenter, personAccessesList });
                    request.setAttribute("projectList", projectList);
                    return mapping.findForward("showPersonAccesses");
                }
            } catch (ExcepcaoInexistente e) {
                request.setAttribute("noPerson", new Boolean(true));
            } catch (InvalidArgumentsServiceException e) {
                request.setAttribute("noValidPerson", new Boolean(true));
            }
        }
        return mapping.findForward("choosePerson");
    }

    public ActionForward delegateAccess(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixServiceException, FenixFilterException {
        final IUserView userView = UserView.getUser();
        final Integer projectCode = (Integer) ((DynaActionForm) form).get("projectCode");
        final String costCenter = request.getParameter("costCenter");
        getCostCenterName(request, costCenter);
        final String username = (String) ((DynaActionForm) form).get("username");

        final String beginDay = (String) ((DynaActionForm) form).get("beginDay");
        final String beginMonth = (String) ((DynaActionForm) form).get("beginMonth");
        final String beginYear = (String) ((DynaActionForm) form).get("beginYear");
        final String endDay = (String) ((DynaActionForm) form).get("endDay");
        final String endMonth = (String) ((DynaActionForm) form).get("endMonth");
        final String endYear = (String) ((DynaActionForm) form).get("endYear");
        final Calendar beginDate = string2Date(beginDay, beginMonth, beginYear);
        final Calendar endDate = string2Date(endDay, endMonth, endYear);
        CalendarDateComparator dateComparator = new CalendarDateComparator();
        if (dateComparator.compare(beginDate, endDate) >= 0) {
            request.setAttribute("invalidTime", new Boolean(true));
        } else if (dateComparator.compare(Calendar.getInstance(), endDate) > 0) {
            request.setAttribute("invalidEndTime", new Boolean(true));
        } else {
            if (projectCode != null && projectCode.equals(new Integer(-1))) {
                ServiceManagerServiceFactory.executeService( "InsertNewProjectAccess", new Object[] { userView.getUtilizador(), costCenter,
                        username, beginDate, endDate });
            } else {
                final String[] projectCodes = request.getParameterValues("projectCodes");
                if (projectCodes != null && projectCodes.length != 0) {
                    ServiceManagerServiceFactory.executeService( "InsertNewProjectAccess", new Object[] { userView.getUtilizador(),
                            costCenter, username, projectCodes, beginDate, endDate });
                } else {
                    request.setAttribute("noProjectsSelected", new Boolean(true));
                }
            }
        }

        return showPersonAccesses(mapping, form, request, response);
    }

    public ActionForward removeProjectAccess(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixServiceException, FenixFilterException {
        final IUserView userView = UserView.getUser();
        final Integer projectCode = getCodeFromRequest(request, "projectCode");
        final String personUsername = request.getParameter("username");
        final String costCenter = request.getParameter("costCenter");
        ServiceManagerServiceFactory.executeService( "RemoveProjectAccess", new Object[] { userView.getUtilizador(), costCenter,
                personUsername, projectCode });
        return showProjectsAccesses(mapping, form, request, response);
    }

    public ActionForward removePersonAccess(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixServiceException, FenixFilterException {
        final IUserView userView = UserView.getUser();
        final Integer projectCode = getCodeFromRequest(request, "projectCode");
        final String personUsername = request.getParameter("username");
        final String costCenter = request.getParameter("costCenter");
        ServiceManagerServiceFactory.executeService( "RemoveProjectAccess", new Object[] { userView.getUtilizador(), costCenter,
                personUsername, projectCode });
        return showPersonAccesses(mapping, form, request, response);
    }

    public ActionForward prepareEditProjectAccess(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixServiceException, FenixFilterException {
        final IUserView userView = UserView.getUser();
        final Integer projectCode = getCodeFromRequest(request, "projectCode");
        final Integer personCode = getCodeFromRequest(request, "personCode");
        final String costCenter = request.getParameter("costCenter");
        getCostCenterName(request, costCenter);
        final InfoProjectAccess infoProjectAccess = (InfoProjectAccess) ServiceManagerServiceFactory.executeService( "ReadProjectAccesses",
                new Object[] { userView.getUtilizador(), costCenter, personCode, projectCode });
        ((DynaActionForm) form).set("beginDay", new Integer(infoProjectAccess.getBeginDate().get(Calendar.DAY_OF_MONTH)).toString());
        ((DynaActionForm) form).set("beginMonth", new Integer(infoProjectAccess.getBeginDate().get(Calendar.MONTH) + 1).toString());
        ((DynaActionForm) form).set("beginYear", new Integer(infoProjectAccess.getBeginDate().get(Calendar.YEAR)).toString());
        ((DynaActionForm) form).set("endDay", new Integer(infoProjectAccess.getEndDate().get(Calendar.DAY_OF_MONTH)).toString());
        ((DynaActionForm) form).set("endMonth", new Integer(infoProjectAccess.getEndDate().get(Calendar.MONTH) + 1).toString());
        ((DynaActionForm) form).set("endYear", new Integer(infoProjectAccess.getEndDate().get(Calendar.YEAR)).toString());
        request.setAttribute("infoProjectAccess", infoProjectAccess);
        return mapping.findForward("editProjectAccess");
    }

    public ActionForward editProjectAccess(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixServiceException, FenixFilterException {
        final IUserView userView = UserView.getUser();
        final Integer projectCode = (Integer) ((DynaActionForm) form).get("projectCode");
        final String costCenter = request.getParameter("costCenter");
        final Integer personCode = (Integer) ((DynaActionForm) form).get("personCode");
        final String beginDay = (String) ((DynaActionForm) form).get("beginDay");
        final String beginMonth = (String) ((DynaActionForm) form).get("beginMonth");
        final String beginYear = (String) ((DynaActionForm) form).get("beginYear");
        final String endDay = (String) ((DynaActionForm) form).get("endDay");
        final String endMonth = (String) ((DynaActionForm) form).get("endMonth");
        final String endYear = (String) ((DynaActionForm) form).get("endYear");
        final Calendar beginDate = string2Date(beginDay, beginMonth, beginYear);
        final Calendar endDate = string2Date(endDay, endMonth, endYear);
        CalendarDateComparator dateComparator = new CalendarDateComparator();
        if (dateComparator.compare(beginDate, endDate) >= 0) {
            request.setAttribute("invalidTime", new Boolean(true));
            return prepareEditProjectAccess(mapping, form, request, response);
        } else if (dateComparator.compare(Calendar.getInstance(), endDate) > 0) {
            request.setAttribute("invalidEndTime", new Boolean(true));
            return prepareEditProjectAccess(mapping, form, request, response);
        } else
            ServiceManagerServiceFactory.executeService( "EditProjectAccess", new Object[] { userView.getUtilizador(), costCenter,
                    personCode, projectCode, beginDate, endDate });
        return showPersonAccesses(mapping, form, request, response);
    }

    private Integer getCodeFromRequest(HttpServletRequest request, String codeString) {
        Integer code = null;
        try {
            Object objectCode = request.getAttribute(codeString);
            if (objectCode != null) {
                if (objectCode instanceof String)
                    code = new Integer((String) objectCode);
                else if (objectCode instanceof Integer)
                    code = (Integer) objectCode;
            } else {
                String thisCodeString = request.getParameter(codeString);
                if (thisCodeString != null)
                    code = new Integer(thisCodeString);
            }
        } catch (NumberFormatException e) {
            return null;
        }
        return code;
    }

    private Calendar string2Date(String day, String month, String year) {
        Calendar result = Calendar.getInstance();
        result.set(Calendar.DAY_OF_MONTH, (new Integer(day)).intValue());
        result.set(Calendar.MONTH, (new Integer(month)).intValue() - 1);
        result.set(Calendar.YEAR, (new Integer(year)).intValue());
        return result;
    }

    private void getCostCenterName(HttpServletRequest request, String costCenter) throws FenixServiceException, FenixFilterException {
        if (costCenter != null && !costCenter.equals("")) {
            final IUserView userView = UserView.getUser();
            request.setAttribute("infoCostCenter", ServiceUtils.executeService("ReadCostCenter", new Object[] { userView.getUtilizador(),
                    costCenter }));
        }
    }
}