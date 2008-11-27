package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager;

import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.ReadDegreesClassesLessons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.ReadExecutionDegreesByExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.comparators.ComparatorByNameForInfoExecutionDegree;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionConstants;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.MessageResources;

/**
 * @author Luis Cruz e Sara Ribeiro
 */
public class ViewAllClassesSchedulesDA extends FenixContextDispatchAction {

    public ActionForward choose(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {

	// GestorServicos gestor = GestorServicos.manager();
	IUserView userView = getUserView(request);

	InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request.getAttribute(SessionConstants.EXECUTION_PERIOD);
	// setExecutionContext(request);
	/* Cria o form bean com as licenciaturas em execucao. */

	List executionDegreeList = (List) ReadExecutionDegreesByExecutionYear.run(infoExecutionPeriod.getInfoExecutionYear());

	Collections.sort(executionDegreeList, new ComparatorByNameForInfoExecutionDegree());
	MessageResources messageResources = this.getResources(request, "ENUMERATION_RESOURCES");
	executionDegreeList = InfoExecutionDegree.buildLabelValueBeansForList(executionDegreeList, messageResources);

	request.setAttribute(SessionConstants.INFO_EXECUTION_DEGREE_LIST, executionDegreeList);

	return mapping.findForward("choose");
    }

    public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {

	IUserView userView = getUserView(request);
	DynaActionForm chooseViewAllClassesSchedulesContextForm = (DynaActionForm) form;

	InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request.getAttribute(SessionConstants.EXECUTION_PERIOD);
	// setExecutionContext(request);

	List infoExecutionDegreeList = (List) ReadExecutionDegreesByExecutionYear.run(infoExecutionPeriod.getInfoExecutionYear());
	Collections.sort(infoExecutionDegreeList, new ComparatorByNameForInfoExecutionDegree());

	Boolean selectAllDegrees = (Boolean) chooseViewAllClassesSchedulesContextForm.get("selectAllDegrees");
	List selectedInfoExecutionDegrees = null;
	if (selectAllDegrees != null && selectAllDegrees.booleanValue()) {
	    selectedInfoExecutionDegrees = infoExecutionDegreeList;
	} else {
	    String[] selectedDegreesIndexes = (String[]) chooseViewAllClassesSchedulesContextForm.get("selectedDegrees");
	    selectedInfoExecutionDegrees = new ArrayList();

	    for (int i = 0; i < selectedDegreesIndexes.length; i++) {
		Integer index = new Integer("" + selectedDegreesIndexes[i]);
		InfoExecutionDegree infoEd = (InfoExecutionDegree) infoExecutionDegreeList.get(index.intValue());

		selectedInfoExecutionDegrees.add(infoEd);
	    }
	}

	List infoViewClassScheduleList = (List) ReadDegreesClassesLessons.run(selectedInfoExecutionDegrees, infoExecutionPeriod);

	if (infoViewClassScheduleList != null && infoViewClassScheduleList.isEmpty()) {
	    request.removeAttribute(SessionConstants.ALL_INFO_VIEW_CLASS_SCHEDULE);
	} else {
	    Collections.sort(infoViewClassScheduleList, new BeanComparator("infoClass.nome"));
	    request.setAttribute(SessionConstants.ALL_INFO_VIEW_CLASS_SCHEDULE, infoViewClassScheduleList);
	}

	return mapping.findForward("list");
    }
}