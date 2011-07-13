package net.sourceforge.fenixedu.presentationTier.Action.coordinator.candidacy.degreeChange;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.coordinator.CoordinatedDegreeInfo;
import net.sourceforge.fenixedu.presentationTier.formbeans.FenixActionForm;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(path = "/caseHandlingDegreeChangeIndividualCandidacyProcess", module = "coordinator", formBeanClass = FenixActionForm.class)
@Forwards( {
	@Forward(name = "intro", path = "/caseHandlingDegreeChangeCandidacyProcess.do?method=listProcessAllowedActivities"),
	@Forward(name = "list-allowed-activities", path = "/coordinator/candidacy/degreeChange/listIndividualCandidacyActivities.jsp") })
public class DegreeChangeIndividualCandidacyProcessDA extends
	net.sourceforge.fenixedu.presentationTier.Action.candidacy.degreeChange.DegreeChangeIndividualCandidacyProcessDA {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	CoordinatedDegreeInfo.setCoordinatorContext(request);
	return super.execute(mapping, actionForm, request, response);
    }

}
