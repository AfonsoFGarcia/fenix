/*
 * Created on Dec 17, 2003
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.coordinator;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadExecutionYearsByDegreeCurricularPlanID;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.coordinator.CoordinatedDegreeInfo;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class ReadTeachersInformationAction extends FenixAction {

    /*
     * (non-Javadoc)
     * 
     * @seeorg.apache.struts.action.Action#execute(org.apache.struts.action.
     * ActionMapping, org.apache.struts.action.ActionForm,
     * javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse)
     */
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	CoordinatedDegreeInfo.setCoordinatorContext(request);
	Integer degreeCurricularPlanID = (Integer) request.getAttribute("degreeCurricularPlanID");

	Integer executionDegreeID = new Integer(request.getParameter("executionDegreeId"));
	request.setAttribute("executionDegreeId", executionDegreeID);

	// Lists all years attatched to the degree curricular plan
	List executionYearList = (List) ReadExecutionYearsByDegreeCurricularPlanID.run(degreeCurricularPlanID);

	List executionYearsLabelValueList = new ArrayList();
	for (int i = 0; i < executionYearList.size(); i++) {
	    InfoExecutionYear infoExecutionYear = (InfoExecutionYear) executionYearList.get(i);
	    executionYearsLabelValueList.add(new LabelValueBean(infoExecutionYear.getYear(), infoExecutionYear.getYear()));
	}

	request.setAttribute("executionYearList", executionYearsLabelValueList);

	DynaActionForm teacherInformationForm = (DynaActionForm) actionForm;
	String yearString = (String) teacherInformationForm.get("yearString");
	List infoSiteTeachersInformation = null;

	if (yearString.equalsIgnoreCase("") || yearString == null) {
	    // show user option
	    teacherInformationForm.set("yearString", ((InfoExecutionYear) executionYearList.get(executionYearList.size() - 1))
		    .getYear());
	}

	Object[] args = { executionDegreeID, Boolean.FALSE, yearString };
	infoSiteTeachersInformation = (List) ServiceUtils.executeService("ReadTeachersInformation", args);

	request.setAttribute("infoSiteTeachersInformation", infoSiteTeachersInformation);

	return mapping.findForward("show");
    }

}