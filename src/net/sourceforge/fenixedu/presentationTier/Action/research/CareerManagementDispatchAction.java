package net.sourceforge.fenixedu.presentationTier.Action.research;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.CareerType;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.teacher.Career;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/career/careerManagement", module = "researcher")
@Forwards(@Forward(name = "showCareer", path = "/researcher/career/showCareer.jsp"))
public class CareerManagementDispatchAction extends FenixDispatchAction {
    public ActionForward showCareer(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	Person person = getLoggedPerson(request);
	SortedSet<Career> sortedCareer = new TreeSet<Career>(new Comparator<Career>() {
	    @Override
	    public int compare(Career o1, Career o2) {
		if (o2.getEndYear() != null) {
		    return -(o1.getBeginYear().compareTo(o2.getEndYear()));
		} else {
		    return 1;
		}
	    }
	});
	sortedCareer.addAll(person.getCareersByType(CareerType.PROFESSIONAL));
	request.setAttribute("career", sortedCareer);
	return mapping.findForward("showCareer");
    }

    public ActionForward prepareCreateCareer(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("creating", true);
	return showCareer(mapping, actionForm, request, response);
    }

    public ActionForward prepareEditCareer(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	Career career = getDomainObject(request, "id");
	request.setAttribute("editCareer", career);
	return showCareer(mapping, actionForm, request, response);
    }

    public ActionForward prepareDeleteCareer(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	Career career = getDomainObject(request, "id");
	request.setAttribute("deleteCareer", career);
	return showCareer(mapping, actionForm, request, response);
    }

    public ActionForward deleteCareer(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	Career career = (Career) getRenderedObject("deleteCareer");
	career.delete();
	return showCareer(mapping, actionForm, request, response);
    }
}
