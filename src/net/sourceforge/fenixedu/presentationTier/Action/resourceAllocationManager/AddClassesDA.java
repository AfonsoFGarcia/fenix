package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.AddSchoolClassesToShift;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.ReadAvailableClassesForShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.base.FenixShiftAndExecutionCourseAndExecutionDegreeAndCurricularYearContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionConstants;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 */
public class AddClassesDA extends FenixShiftAndExecutionCourseAndExecutionDegreeAndCurricularYearContextDispatchAction {

    public ActionForward listClasses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	InfoShift infoShift = (InfoShift) request.getAttribute(SessionConstants.SHIFT);

	List classes = ReadAvailableClassesForShift.run(infoShift.getIdInternal());

	if (classes != null && !classes.isEmpty()) {
	    Collections.sort(classes, new BeanComparator("nome"));
	    request.setAttribute(SessionConstants.CLASSES, classes);
	}

	return mapping.findForward("ListClasses");
    }

    public ActionForward add(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {

	InfoShift infoShift = (InfoShift) request.getAttribute(SessionConstants.SHIFT);

	DynaActionForm addClassesForm = (DynaActionForm) form;
	String[] selectedClasses = (String[]) addClassesForm.get("selectedItems");

	List classOIDs = new ArrayList();
	for (int i = 0; i < selectedClasses.length; i++) {
	    classOIDs.add(new Integer(selectedClasses[i]));
	}

	try {
	    AddSchoolClassesToShift.run(infoShift, classOIDs);
	} catch (FenixServiceException ex) {
	    // No probem, the user refreshed the page after adding classes
	    request.setAttribute("selectMultipleItemsForm", null);
	    return mapping.getInputForward();
	}

	request.setAttribute("selectMultipleItemsForm", null);

	return mapping.findForward("EditShift");
    }

}