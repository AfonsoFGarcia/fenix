package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.io.DataOutputStream;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.organizationalStructure.Contract;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.FileEntry;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.homepage.Homepage;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import pt.utl.ist.fenix.tools.image.TextPngCreator;

public class ViewHomepageDA extends FenixDispatchAction {

    public ActionForward show(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
    	final String homepageIDString = request.getParameter("homepageID");
    	final Integer homepageID = Integer.valueOf(homepageIDString);
    	final Homepage homepage = (Homepage) readDomainObject(request, Homepage.class, homepageID);
    	if (homepage == null || !homepage.getActivated().booleanValue()) {
    		final ActionMessages actionMessages = new ActionMessages();
    		actionMessages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("homepage.not.found"));
    		saveMessages(request, actionMessages);
    		return list(mapping, actionForm, request, response);
    	} else {
            SortedSet<Attends> personAttendsSortedByExecutionCourseName = new TreeSet<Attends>(
                    Attends.ATTENDS_COMPARATOR_BY_EXECUTION_COURSE_NAME);
            personAttendsSortedByExecutionCourseName.addAll(homepage.getPerson().getCurrentAttends());
            
            request.setAttribute("personAttends", personAttendsSortedByExecutionCourseName);
    		request.setAttribute("homepage", homepage);
            
    		return mapping.findForward("view-homepage");
    	}
    }

    public ActionForward notFound(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward("not-found-homepage");
    }

    public ActionForward list(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
    	final SortedMap<String, SortedSet<Homepage>> homepages = new TreeMap<String, SortedSet<Homepage>>();
    	for (int i = (int) 'A'; i <= (int) 'Z'; i++) {
    		homepages.put("" + ((char) i), new TreeSet<Homepage>(Homepage.HOMEPAGE_COMPARATOR_BY_NAME));
    	}
    	for (final Homepage homepage : rootDomainObject.getHomepagesSet()) {
    		if (homepage.getActivated().booleanValue()) {
    			final String key = homepage.getName().substring(0, 1);
    			final SortedSet<Homepage> sortedSet;
    			if (homepages.containsKey(key)) {
    				sortedSet = homepages.get(key);
    				sortedSet.add(homepage);
    			}
    		}
    	}
    	request.setAttribute("homepages", homepages);

    	final String selectedPage = request.getParameter("selectedPage");
    	if (selectedPage != null) {
    		request.setAttribute("selectedPage", selectedPage);
    	} else {
    		request.setAttribute("selectedPage", "");
    	}

        return mapping.findForward("list-homepages");
    }

    public ActionForward listTeachers(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
    	final SortedMap<Unit, SortedSet<Homepage>> homepages = new TreeMap<Unit, SortedSet<Homepage>>(Unit.UNIT_COMPARATOR_BY_NAME);
    	for (final Teacher teacher : rootDomainObject.getTeachersSet()) {
    		final Person person = teacher.getPerson();
    		final Employee employee = person.getEmployee();
    		if (employee != null) {
    			final Contract contract = employee.getCurrentWorkingContract();
    			if (contract != null) {
    				final Unit unit = contract.getWorkingUnit();
    				final SortedSet<Homepage> unitHomepages;
    				if (homepages.containsKey(unit)) {
    					unitHomepages = homepages.get(unit);
    				} else {
    					unitHomepages = new TreeSet<Homepage>(Homepage.HOMEPAGE_COMPARATOR_BY_NAME);
    					homepages.put(unit, unitHomepages);
    				}
    				final Homepage homepage = person.getHomepage();
    				if (homepage != null && homepage.getActivated().booleanValue()) {
    					unitHomepages.add(homepage);
    				}
    			}
    		}
    	}
    	request.setAttribute("homepages", homepages);

    	final String selectedPage = request.getParameter("selectedPage");
    	if (selectedPage != null) {
    		request.setAttribute("selectedPage", selectedPage);
    	}

        return mapping.findForward("list-homepages-teachers");
    }

    public ActionForward listEmployees(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
    	final SortedMap<Unit, SortedSet<Homepage>> homepages = new TreeMap<Unit, SortedSet<Homepage>>(Unit.UNIT_COMPARATOR_BY_NAME);
    	for (final Employee employee : rootDomainObject.getEmployeesSet()) {
    		final Person person = employee.getPerson();
    		if (person != null) {
    			final Teacher teacher = person.getTeacher();
    			if (teacher == null) {
    				final Contract contract = employee.getCurrentWorkingContract();
    				if (contract != null) {
    					final Unit unit = contract.getWorkingUnit();
    					final SortedSet<Homepage> unitHomepages;
        				if (homepages.containsKey(unit)) {
        					unitHomepages = homepages.get(unit);
        				} else {
        					unitHomepages = new TreeSet<Homepage>(Homepage.HOMEPAGE_COMPARATOR_BY_NAME);
        					homepages.put(unit, unitHomepages);
        				}
        				final Homepage homepage = person.getHomepage();
        				if (homepage != null && homepage.getActivated().booleanValue()) {
        					unitHomepages.add(homepage);
        				}
    				}
    			}
    		}
    	}
    	request.setAttribute("homepages", homepages);

    	final String selectedPage = request.getParameter("selectedPage");
    	if (selectedPage != null) {
    		request.setAttribute("selectedPage", selectedPage);
    	}

        return mapping.findForward("list-homepages-employees");
    }

    public ActionForward listStudents(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
    	final SortedMap<Degree, SortedSet<Homepage>> homepages = new TreeMap<Degree, SortedSet<Homepage>>(Degree.DEGREE_COMPARATOR_BY_NAME_AND_DEGREE_TYPE);
    	for (final Registration registration : rootDomainObject.getRegistrationsSet()) {
    		final StudentCurricularPlan studentCurricularPlan = registration.getActiveStudentCurricularPlan();
    		if (studentCurricularPlan != null) {
    			final DegreeCurricularPlan degreeCurricularPlan = studentCurricularPlan.getDegreeCurricularPlan();
    			final Degree degree = degreeCurricularPlan.getDegree();
    			final Person person = registration.getPerson();
    			final SortedSet<Homepage> degreeHomepages;
				if (homepages.containsKey(degree)) {
					degreeHomepages = homepages.get(degree);
				} else {
					degreeHomepages = new TreeSet<Homepage>(Homepage.HOMEPAGE_COMPARATOR_BY_NAME);
					homepages.put(degree, degreeHomepages);
				}
				final Homepage homepage = person.getHomepage();
				if (homepage != null && homepage.getActivated().booleanValue()) {
					degreeHomepages.add(homepage);
				}
    		}
    	}
    	request.setAttribute("homepages", homepages);

    	final String selectedPage = request.getParameter("selectedPage");
    	if (selectedPage != null) {
    		request.setAttribute("selectedPage", selectedPage);
    	}

        return mapping.findForward("list-homepages-students");
    }

    public ActionForward listAlumni(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
    	final SortedMap<Degree, SortedSet<Homepage>> homepages = new TreeMap<Degree, SortedSet<Homepage>>(Degree.DEGREE_COMPARATOR_BY_NAME_AND_DEGREE_TYPE);
    	for (final Registration registration : rootDomainObject.getRegistrationsSet()) {
    		for (final StudentCurricularPlan studentCurricularPlan : registration.getStudentCurricularPlansSet()) {
   				final DegreeCurricularPlan degreeCurricularPlan = studentCurricularPlan.getDegreeCurricularPlan();
   				final Degree degree = degreeCurricularPlan.getDegree();
   				final Person person = registration.getPerson();
   				if (person != null) {
   					final SortedSet<Homepage> degreeHomepages;
   					if (homepages.containsKey(degree)) {
   						degreeHomepages = homepages.get(degree);
   					} else {
   						degreeHomepages = new TreeSet<Homepage>(Homepage.HOMEPAGE_COMPARATOR_BY_NAME);
   						homepages.put(degree, degreeHomepages);
   					}
   					final Homepage homepage = person.getHomepage();
   					if (studentCurricularPlan.getCurrentState() == StudentCurricularPlanState.CONCLUDED
   							&& homepage != null && homepage.getActivated().booleanValue()) {
   						degreeHomepages.add(homepage);
   					}
   				}
    		}
    	}
    	request.setAttribute("homepages", homepages);

    	final String selectedPage = request.getParameter("selectedPage");
    	if (selectedPage != null) {
    		request.setAttribute("selectedPage", selectedPage);
    	}

        return mapping.findForward("list-homepages-alumni");
    }

    public ActionForward emailPng(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        final String email = getEmailString(request);
        final byte[] pngFile = TextPngCreator.createPng("arial", 12, "000000", email);
        response.setContentType("image/png");
        response.getOutputStream().write(pngFile);
        response.getOutputStream().close();
        return null;
    }

    public ActionForward stats(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
    	request.setAttribute("homepages", rootDomainObject.getHomepages());
    	return mapping.findForward("homepage-stats");
    }

    private String getEmailString(final HttpServletRequest request) {
        final String personIDString = request.getParameter("personID");
        if (personIDString != null && personIDString.length() > 0) {
            final Person person = (Person) rootDomainObject.readPartyByOID(Integer.valueOf(personIDString));
            if (person != null && person.getHomepage() != null && person.getHomepage().getActivated().booleanValue() && person.getHomepage().getShowEmail().booleanValue()) {
                return person.getEmail();
            }
        }
        return "";
    }

    public ActionForward retrievePhoto(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        final String homepageIDString = request.getParameter("homepageID");
        final Integer homepageID = Integer.valueOf(homepageIDString);
        final Homepage homepage = (Homepage) readDomainObject(request, Homepage.class, homepageID);
        if (homepage != null && homepage.getShowPhoto() != null && homepage.getShowPhoto().booleanValue()) {
            final Person person = homepage.getPerson();
            final FileEntry personalPhoto = person.getPersonalPhoto();

            if (personalPhoto != null) {
                response.setContentType(personalPhoto.getContentType().getMimeType());
                DataOutputStream dos = new DataOutputStream(response.getOutputStream());
                dos.write(personalPhoto.getContents());
                dos.close();
            }
        }

        return null;
    }

}