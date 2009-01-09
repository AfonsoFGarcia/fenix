package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.ReadPavillionsRoomsLessons;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.Util;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

/**
 * @author Luis Cruz e Sara Ribeiro
 */
public class ViewAllRoomsSchedulesDA extends FenixContextDispatchAction {

    public ActionForward choose(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {

	/* Criar o bean de pavilhoes */
	List<String> pavillionsNamesList = new ArrayList<String>();
	List<LabelValueBean> readExistingBuldings = Util.readExistingBuldings(null, null);
	for (LabelValueBean building : readExistingBuldings) {
	    pavillionsNamesList.add(building.getLabel());
	}

	request.setAttribute(SessionConstants.PAVILLIONS_NAMES_LIST, pavillionsNamesList);

	return mapping.findForward("choose");
    }

    public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {
	DynaActionForm chooseViewAllRoomsSchedulesContextForm = (DynaActionForm) form;

	AcademicInterval academicInterval = AcademicInterval.getAcademicIntervalFromResumedString((String) request
		.getAttribute(SessionConstants.ACADEMIC_INTERVAL));

	List<String> pavillions = new ArrayList<String>();
	List<LabelValueBean> readExistingBuldings = Util.readExistingBuldings(null, null);
	for (LabelValueBean building : readExistingBuldings) {
	    pavillions.add(building.getLabel());
	}

	Boolean selectAllPavillions = (Boolean) chooseViewAllRoomsSchedulesContextForm.get("selectAllPavillions");
	List<String> selectedPavillions = null;
	if (selectAllPavillions != null && selectAllPavillions.booleanValue()) {
	    selectedPavillions = pavillions;
	} else {
	    String[] selectedPavillionsNames = (String[]) chooseViewAllRoomsSchedulesContextForm.get("selectedPavillions");
	    selectedPavillions = new ArrayList();
	    for (int i = 0; i < selectedPavillionsNames.length; i++) {
		selectedPavillions.add(selectedPavillionsNames[i]);
	    }
	}

	List infoViewClassScheduleList = ReadPavillionsRoomsLessons.run(selectedPavillions, academicInterval);

	if (infoViewClassScheduleList != null && infoViewClassScheduleList.isEmpty()) {
	    request.removeAttribute(SessionConstants.ALL_INFO_VIEW_ROOM_SCHEDULE);
	} else {
	    request.setAttribute(SessionConstants.ALL_INFO_VIEW_ROOM_SCHEDULE, infoViewClassScheduleList);
	    request.setAttribute(SessionConstants.ACADEMIC_INTERVAL, academicInterval);
	}

	return mapping.findForward("list");
    }

}