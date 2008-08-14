/*
 * Created on 17/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.framework.CRUDActionByOID;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.security.UserView;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class CareerAction extends CRUDActionByOID {
    protected void prepareFormConstants(ActionMapping mapping, ActionForm form, HttpServletRequest request)
	    throws FenixServiceException, FenixFilterException {
	IUserView userView = UserView.getUser();
	List categories = (List) ServiceUtils.executeService("ReadCategories", new Object[] {});

	request.setAttribute("categories", categories);
    }
}