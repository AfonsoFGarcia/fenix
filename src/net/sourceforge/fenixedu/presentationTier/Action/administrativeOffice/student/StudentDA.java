package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person.PersonBeanFactoryEditor;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class StudentDA extends FenixDispatchAction {

    private Student getStudent(final HttpServletRequest request) {
        final String studentID = request.getParameter("studentID");
        final Student student = rootDomainObject.readStudentByOID(Integer.valueOf(studentID));
        request.setAttribute("student", student);
        return student;
    }

    public ActionForward prepareEditPersonalData(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        final Student student = getStudent(request);        
        request.setAttribute("personBean", new PersonBeanFactoryEditor(student.getPerson()));
	return mapping.findForward("editPersonalData");
    }

    public ActionForward editPersonalData(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
        getStudent(request);
        executeFactoryMethod(request);
        RenderUtils.invalidateViewState();
        return mapping.findForward("viewStudentDetails");
    }

}
