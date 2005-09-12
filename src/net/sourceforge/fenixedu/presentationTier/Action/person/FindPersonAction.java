/*
 * Created on 22/Dez/2003
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.person;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoRole;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author T�nia Pous�o
 *  
 */
public class FindPersonAction extends FenixDispatchAction {
    public ActionForward prepareFindPerson(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward("findPerson");
    }

    public ActionForward findPerson(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionErrors errors = new ActionErrors();

        IUserView userView = SessionUtils.getUserView(request);

        DynaActionForm findPersonForm = (DynaActionForm) actionForm;
        String name = null;
        if (request.getParameter("name") != null && request.getParameter("name").length() > 0) {
            name = request.getParameter("name");
        } else if (findPersonForm.get("name") != null) {
            name = (String) findPersonForm.get("name");
        }

        Integer startIndex = null;
        if (request.getParameter("startIndex") != null
                && request.getParameter("startIndex").length() > 0) {
            startIndex = new Integer(request.getParameter("startIndex"));
        } else if (findPersonForm.get("startIndex") != null) {
            startIndex = new Integer((String) findPersonForm.get("startIndex"));
        }
        
        String roleType = null;
        
        if (request.getParameter("roleType") != null
                && request.getParameter("roleType").length() > 0) {
        	roleType =  (String) request.getParameter("roleType");
        	
        } else if (findPersonForm.get("roleType") != null) {
        	roleType =(String) findPersonForm.get("roleType");
        }
        
        HashMap parametersSearch = new HashMap();
        parametersSearch.put(new String("name"), putSearchChar(name));
        parametersSearch.put(new String("email"), putSearchChar(null));
        parametersSearch.put(new String("username"), putSearchChar(null));
        parametersSearch.put(new String("documentIdNumber"), putSearchChar(null));
        parametersSearch.put(new String("startIndex"), startIndex);
        parametersSearch.put(new String("numberOfElements"), new Integer(
                SessionConstants.LIMIT_FINDED_PERSONS - 1));
        parametersSearch.put(new String("roleType"),roleType);
        

        Object[] args = { parametersSearch };

        List personListFinded = null;
        try {
            personListFinded = (List) ServiceManagerServiceFactory.executeService(userView,
                    "SearchPerson", args);
        } catch (FenixServiceException e) {
            e.printStackTrace();
            errors.add("impossibleFindPerson", new ActionError("error.manager.implossible.findPerson"));
        }
        if (personListFinded == null || personListFinded.size() < 2) {
            errors.add("impossibleFindPerson", new ActionError("error.manager.implossible.findPerson"));
        }
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.getInputForward();
        }
        
        List infoPerson = (List) personListFinded.get(1);
        if (infoPerson.size() == 0){
       	 errors.add("PersonNotExist", new ActionError("error.manager.implossible.findPerson"));
       	 saveErrors(request, errors);
         return mapping.getInputForward();
       }
        InfoPerson iinfoPerson = (InfoPerson)infoPerson.get(0);
        request.setAttribute("totalFindedPersons", personListFinded.get(0));
        //Collections.sort((List) personListFinded.get(1), new
        // BeanComparator("nome"));
        request.setAttribute("personListFinded", personListFinded.get(1));
        request.setAttribute("name", name);
        request.setAttribute("roleType", roleType);
        request.setAttribute("previousStartIndex", new Integer(startIndex.intValue()
                - SessionConstants.LIMIT_FINDED_PERSONS));
	    request.setAttribute("startIndex", new Integer(startIndex.intValue()
	                + SessionConstants.LIMIT_FINDED_PERSONS));
       

        if (isEmployeeOrTeacher(userView)) {
            request.setAttribute("show", Boolean.TRUE);
        } else {
            request.setAttribute("show", Boolean.FALSE);
        }

        Boolean viewPhoto = getCheckBoxValue((String) findPersonForm.get("viewPhoto"));
        request.setAttribute("viewPhoto", viewPhoto);

        return mapping.findForward("displayPerson");
    }

    private String putSearchChar(String searchElem) {
        String newSearchElem = null;
        if (searchElem != null) {
            newSearchElem = "%".concat(searchElem.replace(' ', '%')).concat("%");
        }
        return newSearchElem;
    }

    private boolean isEmployeeOrTeacher(IUserView userView) {
        List employeeAndTeacherRoles = (List) CollectionUtils.select(userView.getRoles(),
                new Predicate() {

                    public boolean evaluate(Object arg0) {
                        InfoRole role = (InfoRole) arg0;
                        return role.getRoleType().equals(RoleType.EMPLOYEE)
                                || role.getRoleType().equals(RoleType.TEACHER);
                    }

                });

        return employeeAndTeacherRoles != null && employeeAndTeacherRoles.size() > 0;
    }

    private Boolean getCheckBoxValue(String value) {

        if (value != null && (value.equals("true") || value.equals("yes") || value.equals("on"))) {
            return new Boolean(true);
        }
        return new Boolean(false);

    }
}