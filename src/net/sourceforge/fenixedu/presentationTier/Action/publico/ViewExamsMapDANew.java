/**
 * Project sop 
 * 
 * Package presentationTier.Action.sop
 * 
 * Created on 2/Apr/2003
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoExamsMap;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class ViewExamsMapDANew extends FenixContextDispatchAction {

    public ActionForward view(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws FenixActionException, FenixServiceException, FenixFilterException {
	// inEnglish
	Boolean inEnglish = getFromRequestBoolean("inEnglish", request);
	request.setAttribute("inEnglish", inEnglish);

	// index
	Integer index = getFromRequest("index", request);
	request.setAttribute("index", index);

	// degreeID
	Integer degreeId = getFromRequest("degreeID", request);
	request.setAttribute("degreeID", degreeId);

	// degreeCurricularPlanID
	Integer degreeCurricularPlanId = getFromRequest("degreeCurricularPlanID", request);
	request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanId);

	// curricularYearList
	List<Integer> curricularYears = (List<Integer>) request.getAttribute("curricularYearList");
	if (curricularYears == null) {
	    curricularYears = rootDomainObject.readDegreeByOID(degreeId).buildFullCurricularYearList();
	}
	request.setAttribute("curricularYearList", curricularYears);

	// lista
	List lista = (List) request.getAttribute("lista");
	request.setAttribute("lista", lista);

	// SessionConstants.EXECUTION_DEGREE, infoDegreeCurricularPlan
	InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) request.getAttribute(SessionConstants.EXECUTION_DEGREE);
	final DegreeCurricularPlan degreeCurricularPlan = rootDomainObject.readDegreeCurricularPlanByOID(degreeCurricularPlanId);
	infoExecutionDegree = infoExecutionDegree == null || infoExecutionDegree.getExecutionDegree() == null ? findLatestInfoExecutionDegree(degreeCurricularPlan) : infoExecutionDegree;
	if (infoExecutionDegree == null || infoExecutionDegree.getExecutionDegree() == null) {
	    request.setAttribute("infoDegreeCurricularPlan", "");
	} else {
	    request.setAttribute("infoDegreeCurricularPlan", new InfoDegreeCurricularPlan(degreeCurricularPlan));
	}
	request.setAttribute(SessionConstants.EXECUTION_DEGREE, infoExecutionDegree);

	// indice
	Integer indice = getFromRequest("indice", request);
	request.setAttribute("indice", indice);

	// SessionConstants.EXECUTION_PERIOD,
	// SessionConstants.EXECUTION_PERIOD_OID
	InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request.getAttribute(SessionConstants.EXECUTION_PERIOD);
	request.setAttribute(SessionConstants.EXECUTION_PERIOD, infoExecutionPeriod);
	request.setAttribute(SessionConstants.EXECUTION_PERIOD_OID, infoExecutionPeriod.getIdInternal().toString());

	// executionDegreeID
	Integer executionDegreeId = getFromRequest("executionDegreeID", request);
	request.setAttribute("executionDegreeID", executionDegreeId);

	// SessionConstants.INFO_EXAMS_MAP
	request.removeAttribute(SessionConstants.INFO_EXAMS_MAP);
	try {
	    final IUserView userView = getUserView(request);
	    final Object[] args = { infoExecutionDegree, curricularYears, infoExecutionPeriod };
	    final InfoExamsMap infoExamsMap = (InfoExamsMap) ServiceUtils.executeService("ReadFilteredExamsMap", args);
	    request.setAttribute(SessionConstants.INFO_EXAMS_MAP, infoExamsMap);
	} catch (NonExistingServiceException e) {
	    return mapping.findForward("viewExamsMap");
	}

	return mapping.findForward("viewExamsMap");
    }

    private InfoExecutionDegree findLatestInfoExecutionDegree(DegreeCurricularPlan degreeCurricularPlan) {
	final Set<ExecutionDegree> executionDegrees = degreeCurricularPlan.getExecutionDegreesSet();
	final ExecutionDegree executionDegree = Collections.max(executionDegrees, ExecutionDegree.EXECUTION_DEGREE_COMPARATORY_BY_YEAR);
	return new InfoExecutionDegree(executionDegree);
    }

}
