/*
 * Created on Jul 27, 2004
 *
 */
package ServidorApresentacao.Action.manager.curricularCourseGroupManagement;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoCurricularCourse;
import DataBeans.InfoCurricularCourseGroupWithCoursesToAdd;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author Jo�o Mota
 *  
 */
public class CurricularCoursesGroupManagementDispatchAction extends FenixDispatchAction {

    public ActionForward manageCourses(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException {

        IUserView userView = SessionUtils.getUserView(request);

        Integer groupId = new Integer(request.getParameter("groupId"));
        Object[] args1 = { groupId };
        InfoCurricularCourseGroupWithCoursesToAdd composite;
        try {
            composite = (InfoCurricularCourseGroupWithCoursesToAdd) ServiceUtils.executeService(
                    userView, "ReadCoursesByCurricularCourseGroup", args1);

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute("groupWithAll", composite);
        DynaActionForm actionForm = (DynaActionForm) form;

        actionForm.set("courseIds", getListOfIds(composite.getInfoCurricularCourses()).toArray(
                new Integer[0]));

        return mapping.findForward("viewCurricularCourses");
    }

    public ActionForward removeCourses(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException {

        IUserView userView = SessionUtils.getUserView(request);
        DynaActionForm actionForm = (DynaActionForm) form;
        Integer[] coursesIdsToRemove = (Integer[]) actionForm.get("courseIds");
        Integer groupId = new Integer(request.getParameter("groupId"));
        Object[] args1 = { groupId, coursesIdsToRemove };

        try {
            ServiceUtils.executeService(userView, "RemoveCurricularCoursesFromGroup", args1);

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        request.setAttribute("groupId", groupId);

        return manageCourses(mapping, form, request, response);
    }

    public ActionForward addCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        IUserView userView = SessionUtils.getUserView(request);
        DynaActionForm actionForm = (DynaActionForm) form;
        Integer[] coursesIdsToAdd = (Integer[]) actionForm.get("courseIdsToAdd");
        Integer groupId = new Integer(request.getParameter("groupId"));
        Object[] args1 = { groupId, coursesIdsToAdd };

        try {
            ServiceUtils.executeService(userView, "AddCurricularCoursesToGroup", args1);

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        request.setAttribute("groupId", groupId);

        return manageCourses(mapping, form, request, response);
    }

    /**
     * @param composite
     * @return
     */
    private Collection getListOfIds(List courses) {
        return CollectionUtils.collect(courses, new Transformer() {

            public Object transform(Object arg0) {

                return ((InfoCurricularCourse) arg0).getIdInternal();
            }
        });
    }
}