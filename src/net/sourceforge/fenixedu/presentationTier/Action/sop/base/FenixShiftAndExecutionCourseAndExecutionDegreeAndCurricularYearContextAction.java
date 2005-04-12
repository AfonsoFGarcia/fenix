/*
 * Created on 2003/07/31
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.sop.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.utils.ContextUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public abstract class FenixShiftAndExecutionCourseAndExecutionDegreeAndCurricularYearContextAction
        extends FenixExecutionCourseAndExecutionDegreeAndCurricularYearContextAction {

    /**
     * Tests if the session is valid
     * 
     * @see SessionUtils#validSessionVerification(HttpServletRequest,
     *      ActionMapping)
     * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm,
     *      HttpServletRequest, HttpServletResponse)
     */
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        ActionForward actionForward = super.execute(mapping, actionForm, request, response);

        ContextUtils.setShiftContext(request);

        return actionForward;
    }

}