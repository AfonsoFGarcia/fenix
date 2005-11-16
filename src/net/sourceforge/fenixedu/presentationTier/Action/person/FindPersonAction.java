/*
 * Created on 22/Dez/2003
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoRole;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
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

    public ActionForward preparePerson(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        DynaActionForm findPersonForm = (DynaActionForm) actionForm;

        String roleType = null;
        String degreeType = null;

        if (request.getParameter("roleType") != null && request.getParameter("roleType").length() > 0) {
            roleType = (String) request.getParameter("roleType");

        } else if (findPersonForm.get("roleType") != null) {
            roleType = (String) findPersonForm.get("roleType");
        }
        if (request.getParameter("degreeType") != null
                && request.getParameter("degreeType").length() > 0) {
            degreeType = (String) request.getParameter("degreeType");

        } else if (findPersonForm.get("degreeType") != null) {
            degreeType = (String) findPersonForm.get("degreeType");
        }


        if (roleType != null && roleType.length() != 0){
            if (roleType.equals(RoleType.EMPLOYEE.getName())
                    || roleType.equals(RoleType.TEACHER.getName())) {
                if (roleType.equals(RoleType.TEACHER.getName())) {
                    List departments = (List) ServiceUtils.executeService(null, "ReadAllDepartments",
                            null);
                    request.setAttribute("departments", departments);
                }
                //request.removeAttribute("degreeType");
            }

            if (roleType.equals(RoleType.STUDENT.getName())) {
            	
                if (degreeType.length() != 0) {
                    Object[] args = { degreeType };
                    List nonMasterDegree = (List) ServiceUtils.executeService(null,
                            "ReadAllDegreesByType", args);
                    
                    request.setAttribute("nonMasterDegree", nonMasterDegree);
                    request.setAttribute("degreeType", true);
                    
                }
                findPersonForm.set("degreeType", degreeType);
                request.setAttribute("degreeType", degreeType);
            }

            findPersonForm.set("roleType", roleType);
            request.setAttribute("roleType", roleType);

        }
        String name = null;
        if (request.getParameter("name") != null && request.getParameter("name").length() > 0) {
            name = request.getParameter("name");
        } else if (findPersonForm.get("name") != null) {
            name = (String) findPersonForm.get("name");
        }
        if (name != null && name.length() > 0){
        	findPersonForm.set("name",name);
        }
       
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
        Integer departmentId = null;
        Integer degreeId = null;
        String degreeType = null;
        Integer indexPage = null;

        if (request.getParameter("roleType") != null && request.getParameter("roleType").length() > 0) {
            roleType = (String) request.getParameter("roleType");

        } else if (findPersonForm.get("roleType") != null) {
            roleType = (String) findPersonForm.get("roleType");
        }

        if (request.getParameter("degreeType") != null
                && request.getParameter("degreeType").length() > 0) {
            degreeType = (String) request.getParameter("degreeType");
            
        } else if (findPersonForm.get("degreeType") != null) {
            degreeType = (String) findPersonForm.get("degreeType");
        }
        if(degreeType.length() == 0 && roleType.length() == 0){
        	degreeType=null;
        }else if (roleType.equals(RoleType.STUDENT.getName())) {
        	if (degreeType.length() != 0) {
                Object[] args1 = { degreeType };
                List nonMasterDegree = (List) ServiceUtils.executeService(null,
                        "ReadAllDegreesByType", args1);
                
                request.setAttribute("nonMasterDegree", nonMasterDegree);
                request.setAttribute("degreeType", degreeType);
                
            }else{
            	request.setAttribute("degreeType",true);
            }
        }
        if (request.getParameter("departmentId") != null
                && request.getParameter("departmentId").length() > 0) {
            departmentId = Integer.valueOf(request.getParameter("departmentId"));

        } else if (findPersonForm.get("departmentId") != null) {
            departmentId = (Integer) findPersonForm.get("departmentId");
        }
          
	    if (roleType.equals(RoleType.TEACHER.getName())) {
            List departments = (List) ServiceUtils.executeService(null, "ReadAllDepartments",
                    null);
            request.setAttribute("departments", departments);
	    }
        


        if (request.getParameter("degreeId") != null && request.getParameter("degreeId").length() > 0) {
            degreeId = Integer.valueOf(request.getParameter("degreeId"));

        } else if (findPersonForm.get("degreeId") != null) {
            degreeId = (Integer) findPersonForm.get("degreeId");
        }
        if (request.getParameter("pagesIndex") != null && request.getParameter("pagesIndex").length() > 0) {
        	indexPage = Integer.valueOf(request.getParameter("pagesIndex"));

        } else if (findPersonForm.get("pagesIndex") != null) {
        	indexPage = (Integer) findPersonForm.get("pagesIndex");
        }

        HashMap parametersSearch = new HashMap();
        parametersSearch.put(new String("name"), name);
        parametersSearch.put(new String("startIndex"), startIndex);
        parametersSearch.put(new String("numberOfElements"), new Integer(
                SessionConstants.LIMIT_FINDED_PERSONS - 1));
        parametersSearch.put(new String("roleType"), roleType);
        if (degreeId != null) {
            parametersSearch.put(new String("degreeId"), new Integer(degreeId));
        }
        if (departmentId != null) {
            parametersSearch.put(new String("departmentId"), new Integer(departmentId));
        }
        parametersSearch.put(new String("degreeType"), degreeType);

        Object[] args = { parametersSearch };

        List personListFinded = null;
        try {
            personListFinded = (List) ServiceManagerServiceFactory.executeService(userView,
                    "SearchPerson", args);
       
        } catch (FenixServiceException e) {           
            errors.add("impossibleFindPerson", new ActionError(e.getMessage()));
            saveErrors(request, errors);      
            return preparePerson(mapping, actionForm, request, response);
        }
               
        if (personListFinded == null || personListFinded.size() < 3) {
            errors.add("impossibleFindPerson", new ActionError("error.manager.implossible.findPerson"));
        }
        
        List infoPerson = (List) personListFinded.get(1);
        if (infoPerson.isEmpty()) {
            errors.add("impossibleFindPerson", new ActionError("error.manager.implossible.findPerson"));
            saveErrors(request, errors);

        }
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return preparePerson(mapping, actionForm, request, response);
        }
        Integer totalPerson = (Integer)personListFinded.get(0);
        List pagesList = getPageList(totalPerson);
        
        request.setAttribute("totalFindedPersons", personListFinded.get(0));
        request.setAttribute("personListFinded", infoPerson);
        request.setAttribute("name", name);
        request.setAttribute("roleType", roleType);
        
        request.setAttribute("degreeId", degreeId);
        request.setAttribute("departmentId", departmentId);
        request.setAttribute("pages" , pagesList);
        request.setAttribute("pagesIndex",indexPage);
        findPersonForm.set("name",name);

        if ((Integer) personListFinded.get(2) - (Integer) personListFinded.get(3) != SessionConstants.LIMIT_FINDED_PERSONS) {
            request.setAttribute("previousStartIndex",
                    ((Integer) personListFinded.get(2) - SessionConstants.LIMIT_FINDED_PERSONS)
                            - ((Integer) personListFinded.get(2) - (Integer) personListFinded.get(3)));
        } else {
            request.setAttribute("previousStartIndex",
                    ((Integer) personListFinded.get(2) - ( 2 * SessionConstants.LIMIT_FINDED_PERSONS)));
        }
       
        request.setAttribute("startIndex", (Integer) personListFinded.get(2));

        if (isEmployeeOrTeacher(userView)) {
            request.setAttribute("show", Boolean.TRUE);
        } else {
            request.setAttribute("show", Boolean.FALSE);
        }

        Boolean viewPhoto = getCheckBoxValue((String) findPersonForm.get("viewPhoto"));
        request.setAttribute("viewPhoto", viewPhoto);
        return mapping.findForward("findPerson");
       
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
    
    private List getPageList (Integer totalPerson){
    	List pagesList = new ArrayList();
        if(totalPerson <= 25){
        	pagesList.add(0);
        }
        if (totalPerson > 25 && totalPerson <= 50){
        	pagesList.add(0);
        	pagesList.add(25); 
        }
        if (totalPerson > 50 && totalPerson <= 75){
        	pagesList.add(0);
        	pagesList.add(25);   
        	pagesList.add(50);
        }
        if (totalPerson > 75 && totalPerson <= 100){
        	pagesList.add(0);
        	pagesList.add(25);
        	pagesList.add(50); 
        	pagesList.add(75);
        }
        if (totalPerson > 100 && totalPerson <= 125){
        	pagesList.add(0);
        	pagesList.add(25);  
        	pagesList.add(50);  
        	pagesList.add(75); 
        	pagesList.add(100);
        }
        if (totalPerson > 125 && totalPerson <= 150){
        	pagesList.add(0);
        	pagesList.add(25);    
        	pagesList.add(50);  
        	pagesList.add(75);  
        	pagesList.add(100); 
        	pagesList.add(125);
        }
        if (totalPerson > 150 && totalPerson <= 175){
        	pagesList.add(0);
        	pagesList.add(25);
        	pagesList.add(50);  
        	pagesList.add(75);  
        	pagesList.add(100);  
        	pagesList.add(125);  
        	pagesList.add(150);
        }
        if (totalPerson > 175 && totalPerson <= 200){
        	pagesList.add(0);
        	pagesList.add(25);
        	pagesList.add(50);  
        	pagesList.add(75);  
        	pagesList.add(100);  
        	pagesList.add(125);  
        	pagesList.add(150);
        	pagesList.add(175);
        }
        return pagesList;
    }
}