/*
 * Created on 22/Dez/2003
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.person;

import java.net.MalformedURLException;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.homepage.Homepage;
import net.sourceforge.fenixedu.presentationTier.Action.manager.SiteManagementDA;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.RequestUtils;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class ManageHomepageDA extends SiteManagementDA {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Homepage homepage = getSite(request);
	if (homepage != null) {
	    request.setAttribute("homepage", homepage);
	}

	return super.execute(mapping, actionForm, request, response);
    }

    public ActionForward options(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final Person person = getUserView(request).getPerson();
	final Homepage homepage = getSite(request);

	final DynaActionForm dynaActionForm = (DynaActionForm) actionForm;
	if (homepage != null) {
	    dynaActionForm.set("activated", booleanString(homepage.getActivated()));
	    dynaActionForm.set("showUnit", booleanString(homepage.getShowUnit()));
	    dynaActionForm.set("showCategory", booleanString(homepage.getShowCategory()));
	    dynaActionForm.set("showPhoto", booleanString(homepage.getShowPhoto()));
	    dynaActionForm.set("showResearchUnitHomepage", booleanString(homepage.getShowResearchUnitHomepage()));
	    dynaActionForm.set("showCurrentExecutionCourses", booleanString(homepage.getShowCurrentExecutionCourses()));
	    dynaActionForm.set("showActiveStudentCurricularPlans", booleanString(homepage.getShowActiveStudentCurricularPlans()));
	    dynaActionForm.set("showAlumniDegrees", booleanString(homepage.getShowAlumniDegrees()));
	    dynaActionForm.set("researchUnitHomepage", homepage.getResearchUnitHomepage());
	    dynaActionForm.set("researchUnit", homepage.getResearchUnit() != null ? homepage.getResearchUnit().getContent()
		    : null);
	    dynaActionForm.set("showCurrentAttendingExecutionCourses", booleanString(homepage
		    .getShowCurrentAttendingExecutionCourses()));
	    dynaActionForm.set("showPublications", booleanString(homepage.getShowPublications()));
	    dynaActionForm.set("showPatents", booleanString(homepage.getShowPatents()));
	    dynaActionForm.set("showInterests", booleanString(homepage.getShowInterests()));
	    dynaActionForm.set("showParticipations", booleanString(homepage.getShowParticipations()));
	    dynaActionForm.set("showPrizes", booleanString(homepage.getShowPrizes()));
	}

	SortedSet<Attends> personAttendsSortedByExecutionCourseName = new TreeSet<Attends>(
		Attends.ATTENDS_COMPARATOR_BY_EXECUTION_COURSE_NAME);
	personAttendsSortedByExecutionCourseName.addAll(person.getCurrentAttends());

	request.setAttribute("personAttends", personAttendsSortedByExecutionCourseName);
	request.setAttribute("hasPhoto", person.getPersonalPhoto() != null);

	return mapping.findForward("show-homepage-options");
    }

    private Object booleanString(Boolean values) {
	if (values == null) {
	    return Boolean.FALSE.toString();
	} else {
	    return values.toString();
	}
    }

    public ActionForward changeHomepageOptions(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	final DynaActionForm dynaActionForm = (DynaActionForm) actionForm;

	final String activated = (String) dynaActionForm.get("activated");
	final String showUnit = (String) dynaActionForm.get("showUnit");
	final String showCategory = (String) dynaActionForm.get("showCategory");
	final String showPhoto = (String) dynaActionForm.get("showPhoto");
	final String showResearchUnitHomepage = (String) dynaActionForm.get("showResearchUnitHomepage");
	final String showCurrentExecutionCourses = (String) dynaActionForm.get("showCurrentExecutionCourses");
	final String showActiveStudentCurricularPlans = (String) dynaActionForm.get("showActiveStudentCurricularPlans");
	final String showAlumniDegrees = (String) dynaActionForm.get("showAlumniDegrees");
	final String researchUnitHomepage = (String) dynaActionForm.get("researchUnitHomepage");
	final String researchUnit = (String) dynaActionForm.get("researchUnit");
	final String showPublications = (String) dynaActionForm.get("showPublications");
	final String showPatents = (String) dynaActionForm.get("showPatents");
	final String showInterests = (String) dynaActionForm.get("showInterests");
	final String showParticipations = (String) dynaActionForm.get("showParticipations");
	final String showPrizes = (String) dynaActionForm.get("showPrizes");

	final MultiLanguageString researchUnitMultiLanguageString;
	if (researchUnit != null && researchUnit.length() > 0) {
	    researchUnitMultiLanguageString = new MultiLanguageString();
	    researchUnitMultiLanguageString.setContent(researchUnit);
	} else {
	    researchUnitMultiLanguageString = null;
	}
	final String showCurrentAttendingExecutionCourses = (String) dynaActionForm.get("showCurrentAttendingExecutionCourses");

	final Object[] args = { getUserView(request).getPerson(), Boolean.valueOf(activated), Boolean.valueOf(showUnit),
		Boolean.valueOf(showCategory), Boolean.valueOf(showPhoto), Boolean.valueOf(showResearchUnitHomepage),
		Boolean.valueOf(showCurrentExecutionCourses), Boolean.valueOf(showActiveStudentCurricularPlans),
		Boolean.valueOf(showAlumniDegrees), researchUnitHomepage, researchUnitMultiLanguageString,
		Boolean.valueOf(showCurrentAttendingExecutionCourses), Boolean.valueOf(showPublications),
		Boolean.valueOf(showPatents), Boolean.valueOf(showInterests), Boolean.valueOf(showParticipations),
		Boolean.valueOf(showPrizes) };
	executeService(request, "SubmitHomepage", args);

	return options(mapping, actionForm, request, response);
    }

    @Override
    protected Homepage getSite(HttpServletRequest request) {
	try {
	    return (Homepage) ServiceUtils.executeService("GetHomepage", new Object[] { getUserView(request).getPerson(), true });
	} catch (Exception e) {
	    throw new RuntimeException(e);
	}
    }

    @Override
    protected String getAuthorNameForFile(HttpServletRequest request) {
	return getUserView(request).getPerson().getName();
    }

    @Override
    protected String getItemLocationForFile(HttpServletRequest request, Item item, Section section) {
	String userUId = getUserView(request).getPerson().getUser().getUserUId();
	try {
	    return RequestUtils.absoluteURL(request, "/homepage/" + userUId).toString();
	} catch (MalformedURLException e) {
	    throw new RuntimeException(e);
	}
    }

}