package net.sourceforge.fenixedu.presentationTier.Action.sop.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextAction;
import net.sourceforge.fenixedu.presentationTier.Action.utils.ContextUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public abstract class FenixExecutionDegreeAndCurricularYearContextAction extends FenixContextAction {

    public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        ContextUtils.setExecutionDegreeContext(request);
        ContextUtils.setCurricularYearContext(request);

        ActionForward actionForward = super.execute(mapping, actionForm, request, response);

        return actionForward;
    }

}
