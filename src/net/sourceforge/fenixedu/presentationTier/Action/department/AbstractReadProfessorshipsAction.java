/*
 * Created on Dec 17, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.department;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author jpvl
 */
public abstract class AbstractReadProfessorshipsAction extends Action {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        IUserView userView = SessionUtils.getUserView(request);
        DynaActionForm dynaForm = (DynaActionForm) form;

        InfoTeacher infoTeacher = getInfoTeacher(request, dynaForm);

        List detailedInfoProfessorshipList = getDetailedProfessorships(userView, infoTeacher
                .getIdInternal(), dynaForm, request);

        ComparatorChain chain = new ComparatorChain();

        Comparator executionPeriodComparator = new BeanComparator(
                "infoProfessorship.infoExecutionCourse.infoExecutionPeriod.semester");
        Comparator nameComparator = new BeanComparator("infoProfessorship.infoExecutionCourse.nome");

        chain.addComparator(executionPeriodComparator);
        chain.addComparator(nameComparator);
        Collections.sort(detailedInfoProfessorshipList, chain);

        request.setAttribute("detailedProfessorshipList", detailedInfoProfessorshipList);

        extraPreparation(userView, infoTeacher, request, dynaForm);
        return mapping.findForward("list-professorships");
    }

    protected void extraPreparation(IUserView userView, InfoTeacher infoTeacher,
            HttpServletRequest request, DynaActionForm dynaForm) throws Exception {
    }

    protected InfoTeacher getInfoTeacher(HttpServletRequest request, DynaActionForm dynaForm)
            throws Exception {
        InfoTeacher infoTeacher = (InfoTeacher) request.getAttribute("infoTeacher");
        if (infoTeacher == null) {
            infoTeacher = (InfoTeacher) ServiceUtils.executeService(SessionUtils.getUserView(request),
                    "ReadTeacherByOID", new Object[] { Integer.valueOf(dynaForm.get("idInternal")
                            .toString()) });
            request.setAttribute("infoTeacher", infoTeacher);

        }
        return infoTeacher;
    }

    abstract List getDetailedProfessorships(IUserView userView, Integer teacherId,
            DynaActionForm actionForm, HttpServletRequest request) throws FenixServiceException, FenixFilterException;
}