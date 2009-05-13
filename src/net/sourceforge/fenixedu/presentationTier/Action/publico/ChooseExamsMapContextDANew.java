/**
 * Project sop 
 * 
 * Package presentationTier.Action.sop
 * 
 * Created on 2/Apr/2003
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadExecutionPeriodByOID;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.RequestUtils;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class ChooseExamsMapContextDANew extends FenixContextDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {

	Integer degreeId = getFromRequest("degreeID", request);
	request.setAttribute("degreeID", degreeId);
	request.setAttribute("degree", rootDomainObject.readDegreeByOID(degreeId));

	Integer executionDegreeId = getFromRequest("executionDegreeID", request);
	request.setAttribute("executionDegreeID", executionDegreeId);

	Integer degreeCurricularPlanId = getFromRequest("degreeCurricularPlanID", request);
	request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanId);

	List<LabelValueBean> executionPeriodsLabelValueList = buildExecutionPeriodsLabelValueList(degreeCurricularPlanId);
	if (executionPeriodsLabelValueList.size() > 1) {
	    request.setAttribute("lista", executionPeriodsLabelValueList);
	} else {
	    request.removeAttribute("lista");
	}

	Boolean inEnglish = getFromRequestBoolean("inEnglish", request);
	if (inEnglish == null) {
	    inEnglish = getLocale(request).getLanguage().equals(Locale.ENGLISH.getLanguage());
	}
	request.setAttribute("inEnglish", inEnglish);

	request.removeAttribute(PresentationConstants.LABELLIST_EXECUTIONPERIOD);

	return mapping.findForward("prepare");
    }

    public ActionForward choose(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {
	final ActionErrors errors = new ActionErrors();
	final DynaActionForm chooseExamContextoForm = (DynaActionForm) form;

	Boolean inEnglish = getFromRequestBoolean("inEnglish", request);
	if (inEnglish == null) {
	    inEnglish = getLocale(request).getLanguage().equals(Locale.ENGLISH.getLanguage());
	}
	request.setAttribute("inEnglish", inEnglish);

	// index
	Integer indexValue = getFromRequest("index", request);
	request.setAttribute("index", indexValue);

	// degreeID
	Integer degreeId = (Integer) chooseExamContextoForm.get("degreeID");
	request.setAttribute("degreeID", degreeId);
	final Degree degree = rootDomainObject.readDegreeByOID(degreeId);
	request.setAttribute("degree", degree);

	// curricularYearList
	final Boolean selectAllCurricularYears = (Boolean) chooseExamContextoForm.get("selectAllCurricularYears");
	final List<Integer> curricularYears = buildCurricularYearList(selectAllCurricularYears, degree, chooseExamContextoForm);
	request.setAttribute("curricularYearList", curricularYears);

	// degreeCurricularPlanID
	Integer degreeCurricularPlanId = getFromRequest("degreeCurricularPlanID", request);
	final DegreeCurricularPlan degreeCurricularPlan;
	if (degreeCurricularPlanId == null) {
	    degreeCurricularPlan = degree.getMostRecentDegreeCurricularPlan();
	} else {
	    degreeCurricularPlan = rootDomainObject.readDegreeCurricularPlanByOID(degreeCurricularPlanId);
	}

	if (degreeCurricularPlan != null) {
	    request.setAttribute("degreeCurricularPlanID", degreeCurricularPlan.getIdInternal());

	    if (!degreeCurricularPlan.getDegree().getIdInternal().equals(degreeId)) {
		throw new FenixActionException();
	    }

	    // lista
	    List<LabelValueBean> executionPeriodsLabelValueList = buildExecutionPeriodsLabelValueList(degreeCurricularPlan
		    .getIdInternal());
	    if (executionPeriodsLabelValueList.size() > 1) {
		request.setAttribute("lista", executionPeriodsLabelValueList);
	    } else {
		request.removeAttribute("lista");
	    }

	    // infoDegreeCurricularPlan
	    InfoDegreeCurricularPlan infoDegreeCurricularPlan = InfoDegreeCurricularPlan.newInfoFromDomain(degreeCurricularPlan);
	    request.setAttribute("infoDegreeCurricularPlan", infoDegreeCurricularPlan);
	}

	InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request.getAttribute(PresentationConstants.EXECUTION_PERIOD);
	Integer executionPeriodID = (Integer) chooseExamContextoForm.get("indice");
	if (executionPeriodID != null) {
	    infoExecutionPeriod = ReadExecutionPeriodByOID.run(executionPeriodID);
	}
	request.setAttribute("indice", infoExecutionPeriod.getIdInternal());
	chooseExamContextoForm.set("indice", infoExecutionPeriod.getIdInternal());
	RequestUtils.setExecutionPeriodToRequest(request, infoExecutionPeriod);
	request.setAttribute(PresentationConstants.EXECUTION_PERIOD, infoExecutionPeriod);
	request.setAttribute(PresentationConstants.EXECUTION_PERIOD_OID, infoExecutionPeriod.getIdInternal().toString());

	final ExecutionSemester executionSemester = rootDomainObject.readExecutionSemesterByOID(infoExecutionPeriod
		.getIdInternal());
	ExecutionDegree executionDegree = null;

	if (degreeCurricularPlan != null) {
	    executionDegree = degreeCurricularPlan.getExecutionDegreeByYear(executionSemester.getExecutionYear());
	    if (executionDegree == null) {
		executionDegree = degreeCurricularPlan.getMostRecentExecutionDegree();

		if (executionDegree != null) {
		    infoExecutionPeriod = InfoExecutionPeriod.newInfoFromDomain(executionDegree.getExecutionYear()
			    .getExecutionSemesterFor(1));
		    request.setAttribute("indice", infoExecutionPeriod.getIdInternal());
		    chooseExamContextoForm.set("indice", infoExecutionPeriod.getIdInternal());
		    RequestUtils.setExecutionPeriodToRequest(request, infoExecutionPeriod);
		    request.setAttribute(PresentationConstants.EXECUTION_PERIOD, infoExecutionPeriod);
		    request.setAttribute(PresentationConstants.EXECUTION_PERIOD_OID, infoExecutionPeriod.getIdInternal().toString());
		}
	    }
	}

	if (executionDegree != null) {
	    InfoExecutionDegree infoExecutionDegree = InfoExecutionDegree.newInfoFromDomain(executionDegree);
	    request.setAttribute(PresentationConstants.EXECUTION_DEGREE, infoExecutionDegree);
	    request.setAttribute("executionDegreeID", infoExecutionDegree.getIdInternal().toString());
	    RequestUtils.setExecutionDegreeToRequest(request, infoExecutionDegree);
	} else {
	    return mapping.findForward("viewExamsMap");
	}

	return mapping.findForward("showExamsMap");
    }

    private List<Integer> buildCurricularYearList(Boolean allCurricularYears, Degree degree, DynaActionForm chooseExamContextoForm) {
	if (allCurricularYears == null || allCurricularYears) {
	    return degree.buildFullCurricularYearList();
	} else {
	    return buildSelectedList(chooseExamContextoForm);
	}
    }

    private List<Integer> buildSelectedList(DynaActionForm chooseExamContextoForm) {
	String[] selectedCurricularYears = (String[]) chooseExamContextoForm.get("selectedCurricularYears");

	List<Integer> result = new ArrayList<Integer>(selectedCurricularYears.length);
	for (String selectedCurricularYear : selectedCurricularYears) {
	    result.add(Integer.valueOf(selectedCurricularYear));
	}

	return result;
    }

}