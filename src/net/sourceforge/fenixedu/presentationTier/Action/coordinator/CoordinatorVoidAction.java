/*
 * Created on 7/Fev/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package net.sourceforge.fenixedu.presentationTier.Action.coordinator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

/**
 * 
 * @author <a href="mailto:amam@mega.ist.utl.pt">Amin Amirali</a>
 * @author <a href="mailto:frnp@mega.ist.utl.pt">Francisco Paulo</a>
 * 
 */
@Mapping(module = "coordinator", path = "/studentEnrollementSection", scope = "session", parameter = "method")
@Forwards(value = { @Forward(name = "Success", path = "firstPageStudentEnrollementSection") })
public class CoordinatorVoidAction extends FenixAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {
	Integer degreeCurricularPlanID = null;
	if (request.getParameter("degreeCurricularPlanID") != null) {
	    degreeCurricularPlanID = new Integer(request.getParameter("degreeCurricularPlanID"));
	    request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);
	}
	return mapping.findForward("Success");
    }
}