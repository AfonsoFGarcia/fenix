/*
 * Created on 30/Mai/2003
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.util.TipoCurso;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * @author lmac1
 */
public class InsertDegreeDispatchAction extends FenixDispatchAction {

    public ActionForward prepareInsert(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        return mapping.findForward("insertDegree");
    }

    public ActionForward insert(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);

        DynaActionForm dynaForm = (DynaValidatorForm) form;
        String code = (String) dynaForm.get("code");
        String name = (String) dynaForm.get("name");
        Integer degreeTypeInt = (Integer) dynaForm.get("degreeType");

        TipoCurso degreeType = new TipoCurso(degreeTypeInt);
        InfoDegree infoDegree = new InfoDegree(code, name, degreeType);

        Object args[] = { infoDegree };

        try {
            ServiceUtils.executeService(userView, "InsertDegree", args);

        } catch (ExistingServiceException ex) {
            throw new ExistingActionException("message.already.existing.degree", mapping
                    .findForward("readDegrees"));
        } catch (FenixServiceException e) {
            throw new FenixActionException(e.getMessage());
        }

        return mapping.findForward("readDegrees");
    }
}