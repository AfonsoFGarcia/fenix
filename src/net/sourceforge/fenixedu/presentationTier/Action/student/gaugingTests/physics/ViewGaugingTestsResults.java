/*
 * Created on 26/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.student.gaugingTests.physics;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.gaugingTests.physics.InfoGaugingTestResult;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt">Jo�o Mota </a> 26/Nov/2003
 * 
 */
public class ViewGaugingTestsResults extends FenixAction {

    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixActionException, FenixFilterException {

	IUserView userView = getUserView(request);

	Object[] args = { userView };
	try {
	    InfoGaugingTestResult result = (InfoGaugingTestResult) ServiceUtils.executeService("readGaugingTestsResults", args);
	    request.setAttribute("gaugingTestResult", result);
	} catch (FenixServiceException e) {
	    throw new FenixActionException(e);
	}

	return mapping.findForward("viewGaugingTestsResults");
    }

}