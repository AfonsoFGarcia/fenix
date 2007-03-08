package net.sourceforge.fenixedu.presentationTier.Action.research;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.research.result.ExecutionYearBean;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.research.ResearchInterest;
import net.sourceforge.fenixedu.domain.research.activity.ResearchActivityLocationType;
import net.sourceforge.fenixedu.domain.research.result.publication.ResearchResultPublication;
import net.sourceforge.fenixedu.domain.teacher.Advise;
import net.sourceforge.fenixedu.domain.teacher.AdviseType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ViewCurriculumDispatchAction extends FenixAction {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String personId = request.getParameter("personOID");

		final Person person = ((personId != null && personId.length() > 0) ? (Person) RootDomainObject
				.readDomainObjectByOID(Person.class, Integer.valueOf(personId)) : getLoggedPerson(request));

		request.setAttribute("person", person);

		ExecutionYear executionYear = retrieveExecutionYearFromRequest(request);

		/*
		 * List<Project> projects = new ArrayList<Project>();
		 * for(ProjectParticipation participation :
		 * person.getProjectParticipations()) {
		 * if(!projects.contains(participation.getProject())) {
		 * projects.add(participation.getProject()); } }
		 * request.setAttribute("projects", projects);
		 * 
		 * List<Event> events = new ArrayList<Event>(); for(EventParticipation
		 * participation : person.getEventParticipations()) { if
		 * (!events.contains(participation.getEvent())) {
		 * events.add(participation.getEvent()); } }
		 * request.setAttribute("events", events);
		 */

		if (executionYear != null) {
			putInformationOnRequestForGivenExecutionYear(executionYear, person, request);
		} else {
			putAllInformationOnRequest(person, request);
		}

		final List<ResearchInterest> researchInterests = person.getResearchInterests();
		request.setAttribute("researchInterests", researchInterests);

		return mapping.findForward("Success");
	}

	private ExecutionYear retrieveExecutionYearFromRequest(HttpServletRequest request) {
		IViewState viewState = RenderUtils.getViewState("executionYear");
		ExecutionYear executionYear = (viewState != null) ? (ExecutionYear) viewState.getMetaObject()
				.getObject() : null;
		request.setAttribute("executionYearBean", new ExecutionYearBean(executionYear));
		return executionYear;
	}

	private void putInformationOnRequestForGivenExecutionYear(ExecutionYear executionYear, Person person,
			HttpServletRequest request) {
		if (person.hasTeacher()) {
			Teacher teacher = person.getTeacher();

			List<Advise> final_works = new ArrayList<Advise>(teacher.getAdvisesByAdviseTypeAndExecutionYear(
					AdviseType.FINAL_WORK_DEGREE, executionYear));
			Collections.sort(final_works, new BeanComparator("student.number"));

			request.setAttribute("final_works", final_works);
			request.setAttribute("guidances", teacher
					.getGuidedMasterDegreeThesisByExecutionYear(executionYear));
			request.setAttribute("lectures", teacher
					.getLecturedExecutionCoursesByExecutionYear(executionYear));
		}

		List<PersonFunction> functions = new ArrayList<PersonFunction>(person.getPersonFuntions(executionYear
				.getBeginDateYearMonthDay(), executionYear.getEndDateYearMonthDay()));
		Collections.sort(functions, new ReverseComparator(new BeanComparator("beginDateInDateType")));
		request.setAttribute("functions", functions);
		
		 final List<ResearchResultPublication> resultPublications = person.getResearchResultPublicationsByExecutionYear(executionYear);
         request.setAttribute("resultPublications", resultPublications);
    
        request.setAttribute("books", person.getBooks(executionYear));
        request.setAttribute("local-articles",person.getArticles(ResearchActivityLocationType.LOCAL,executionYear));
	request.setAttribute("national-articles",person.getArticles(ResearchActivityLocationType.NATIONAL,executionYear));
	request.setAttribute("international-articles",person.getArticles(ResearchActivityLocationType.INTERNATIONAL,executionYear));	
        //request.setAttribute("articles", person.getArticles(executionYear));
 		request.setAttribute("inproceedings", person.getInproceedings(executionYear));
 		request.setAttribute("proceedings", person.getProceedings(executionYear));
 		request.setAttribute("theses", person.getTheses(executionYear));
 		request.setAttribute("manuals", person.getManuals(executionYear));
 		request.setAttribute("technicalReports", person.getTechnicalReports(executionYear));
 		request.setAttribute("otherPublications", person.getOtherPublications(executionYear));
 		request.setAttribute("unstructureds", person.getUnstructureds(executionYear));
 		request.setAttribute("inbooks", person.getInbooks(executionYear));
 		
		request.setAttribute("resultPatents", person.getResearchResultPatentsByExecutionYear(executionYear));

	}

	private void putAllInformationOnRequest(Person person, HttpServletRequest request) {
		if (person.hasTeacher()) {
			Teacher teacher = person.getTeacher();

			List<Advise> final_works = new ArrayList<Advise>(teacher
					.getAdvisesByAdviseType(AdviseType.FINAL_WORK_DEGREE));
			Collections.sort(final_works, new ReverseComparator(new BeanComparator("startExecutionPeriod")));

			request.setAttribute("final_works", final_works);
			request.setAttribute("guidances", teacher.getAllGuidedMasterDegreeThesis());
			request.setAttribute("lectures", teacher.getAllLecturedExecutionCourses());
		}

		List<PersonFunction> functions = new ArrayList<PersonFunction>(person.getPersonFunctions());
		Collections.sort(functions, new ReverseComparator(new BeanComparator("beginDateInDateType")));
		request.setAttribute("functions", functions);
		final List<ResearchResultPublication> resultPublications = person.getResearchResultPublications();
        request.setAttribute("resultPublications", resultPublications);
        
		request.setAttribute("books", person.getBooks());
		request.setAttribute("local-articles",person.getArticles(ResearchActivityLocationType.LOCAL));
		request.setAttribute("national-articles",person.getArticles(ResearchActivityLocationType.NATIONAL));
		request.setAttribute("international-articles",person.getArticles(ResearchActivityLocationType.INTERNATIONAL));
		//request.setAttribute("articles", person.getArticles());
		request.setAttribute("inproceedings", person.getInproceedings());
		request.setAttribute("proceedings", person.getProceedings());
		request.setAttribute("theses", person.getTheses());
		request.setAttribute("manuals", person.getManuals());
		request.setAttribute("technicalReports", person.getTechnicalReports());
		request.setAttribute("otherPublications", person.getOtherPublications());
		request.setAttribute("unstructureds", person.getUnstructureds());
		request.setAttribute("inbooks", person.getInbooks());
	
		request.setAttribute("resultPatents", person.getResearchResultPatents());
	}

	
}