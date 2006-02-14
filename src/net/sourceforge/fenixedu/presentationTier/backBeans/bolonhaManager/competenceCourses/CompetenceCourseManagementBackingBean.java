/*
 * Created on Dec 8, 2005
 */
package net.sourceforge.fenixedu.presentationTier.backBeans.bolonhaManager.competenceCourses;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingCompetenceCourseInformationException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.bolonhaManager.CourseLoad;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseLoad;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.domain.degreeStructure.RegimeType;
import net.sourceforge.fenixedu.domain.degreeStructure.BibliographicReferences.BibliographicReference;
import net.sourceforge.fenixedu.domain.degreeStructure.BibliographicReferences.BibliographicReferenceType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;

import org.apache.commons.beanutils.BeanComparator;

public class CompetenceCourseManagementBackingBean extends FenixBackingBean {
    private final ResourceBundle bolonhaResources = getResourceBundle("resources/BolonhaManagerResources");
    private final ResourceBundle domainResources = getResourceBundle("resources/DomainExceptionResources");
    
    private Integer selectedDepartmentUnitID = null;
    private Integer competenceCourseID = null;
    private Unit competenceCourseGroupUnit = null;
    private CompetenceCourse competenceCourse = null;        
    // Competence-Course-Information
    private String name;
    private String nameEn;
    private String acronym;
    private Boolean basic;
    private boolean setNumberOfPeriods = true;    
    // Competence-Course-Additional-Data
    private String objectives;
    private String program;
    private String evaluationMethod;
    private String objectivesEn;
    private String programEn;
    private String evaluationMethodEn;
    private String stage;
    // BibliographicReferences
    private Integer bibliographicReferenceID;
    private String year;
    private String title;
    private String author;
    private String reference;
    private String type;
    private String url;
    
    public String getAction() {
        return getAndHoldStringParameter("action");
    }
    
    public Boolean getCanView() throws FenixFilterException, FenixServiceException {
        return (this.getPersonDepartment() != null) ? this.getPersonDepartment().getCompetenceCourseMembersGroup().isMember(this.getUserView().getPerson()) : false;
    }
    
    public Department getPersonDepartment() throws FenixFilterException, FenixServiceException {
        final Employee employee = getUserView().getPerson().getEmployee();
        return (employee != null) ? employee.getCurrentDepartmentWorkingPlace() : null;
    }
    
    public Unit getSelectedDepartmentUnit() throws FenixFilterException, FenixServiceException {
        if (this.getSelectedDepartmentUnitID() != null) {
            return (Unit) this.readDomainObject(Unit.class, this.getSelectedDepartmentUnitID());
        } else {
            return null;
        }
    }
    
    public List<Unit> getScientificAreaUnits() throws FenixFilterException, FenixServiceException {
        Unit departmentUnit = null;
        if (getSelectedDepartmentUnit() != null) {
            departmentUnit = getSelectedDepartmentUnit();
        } else if (getPersonDepartment() != null) {
            departmentUnit = getPersonDepartment().getUnit();
        } 
        return (departmentUnit != null) ? departmentUnit.getScientificAreaUnits() : null;
    }

    public List<CompetenceCourse> getDepartmentApprovedCompetenceCourses() throws FenixFilterException, FenixServiceException {
        List<CompetenceCourse> result = new ArrayList<CompetenceCourse>();
        if (getSelectedDepartmentUnit() != null) {
            for (Unit scientificAreaUnit : getSelectedDepartmentUnit().getScientificAreaUnits()) {
                for (Unit competenceCourseGroupUnit : scientificAreaUnit.getCompetenceCourseGroupUnits()) {
                    for (CompetenceCourse competenceCourse : competenceCourseGroupUnit.getCompetenceCourses()) {
                        if (competenceCourse.getCurricularStage().equals(CurricularStage.APPROVED)) {
                            result.add(competenceCourse);                        
                        }
                    }
                }
            }
        }
        return result;
    }
    
    public List<String> getGroupMembersLabels() throws FenixFilterException, FenixServiceException {
        List<String> result = null;

        if(getSelectedDepartmentUnit() == null){
            return result;
        }
        
        Group competenceCoursesManagementGroup = getSelectedDepartmentUnit().getDepartment().getCompetenceCourseMembersGroup();
        if (competenceCoursesManagementGroup != null) {
            result = new ArrayList<String>();
            Iterator<Person> personIterator = competenceCoursesManagementGroup.getElementsIterator();

            while (personIterator.hasNext()) {
                Person person = personIterator.next();
                result.add(person.getNome() + " (" + person.getUsername() + ")");
            }
        }

        return result;
    }
    
    public Integer getCompetenceCourseGroupUnitID() {
        return getAndHoldIntegerParameter("competenceCourseGroupUnitID");        
    }
    
    public Unit getCompetenceCourseGroupUnit() throws FenixFilterException, FenixServiceException {
        if (competenceCourseGroupUnit == null && getCompetenceCourseGroupUnitID() != null) {
            competenceCourseGroupUnit = (Unit) readDomainObject(Unit.class, getCompetenceCourseGroupUnitID());
        }
        return competenceCourseGroupUnit;
    }

    public String getName() throws FenixFilterException, FenixServiceException {      
        if (name == null && getCompetenceCourse() != null) {
            name = getCompetenceCourse().getName();        
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameEn() throws FenixFilterException, FenixServiceException {        
        if (nameEn == null && getCompetenceCourse() != null) {
            nameEn = getCompetenceCourse().getNameEn();
        }
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }
    
    public String getAcronym() throws FenixFilterException, FenixServiceException {
        if (acronym == null && getCompetenceCourse() != null) {
            acronym = getCompetenceCourse().getAcronym();
        }
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public Boolean getBasic() throws FenixFilterException, FenixServiceException {
        if (basic == null && getCompetenceCourse() != null) {
            basic = Boolean.valueOf(getCompetenceCourse().isBasic());
        }
        return basic;
    }

    public void setBasic(Boolean basic) {
        this.basic = basic;
    }

    public String getRegime() throws FenixFilterException, FenixServiceException {
        if (getViewState().getAttribute("regime") == null) {
            if (getCompetenceCourse() != null) {
                setRegime(getCompetenceCourse().getRegime().name());
            } else {
                setRegime("SEMESTRIAL");
            }            
        }
        return (String) getViewState().getAttribute("regime");
    }

    public void setRegime(String regime) {
        getViewState().setAttribute("regime", regime);
    }   
    
    public Integer getNumberOfPeriods() throws FenixFilterException, FenixServiceException {
        if (getViewState().getAttribute("numberOfPeriods") == null) {
            if (getCompetenceCourse() != null && getCompetenceCourse().getCompetenceCourseLoads().size() > 0) {
                setNumberOfPeriods(getCompetenceCourse().getCompetenceCourseLoads().size());
            } else {
                setNumberOfPeriods(Integer.valueOf(1));
            }                    
        }
        return (Integer) getViewState().getAttribute("numberOfPeriods");
    }

    public void setNumberOfPeriods(Integer numberOfPeriods) {
        if (setNumberOfPeriods) {
            getViewState().setAttribute("numberOfPeriods", numberOfPeriods);
        }        
    }    
    
    public List<SelectItem> getPeriods() throws FenixFilterException, FenixServiceException {
        final List<SelectItem> result = new ArrayList<SelectItem>(2);
        result.add(new SelectItem(Integer.valueOf(2), bolonhaResources.getString("yes")));
        result.add(new SelectItem(Integer.valueOf(1), bolonhaResources.getString("no")));
        return result;
    }
    
    public List<CourseLoad> getCourseLoads() throws FenixFilterException, FenixServiceException {
        if (getViewState().getAttribute("courseLoads") == null) {
            if (getAction().equals("create")) {
                getViewState().setAttribute("courseLoads", createNewCourseLoads());
            } else if (getAction().equals("edit") && getCompetenceCourse() != null) {
                getViewState().setAttribute("courseLoads", getExistingCourseLoads());
            }
        }
        return (List<CourseLoad>) getViewState().getAttribute("courseLoads");
    }
    
    private List<CourseLoad> createNewCourseLoads() throws FenixFilterException, FenixServiceException {
        int numberOfPeriods = getNumberOfPeriods().intValue();
        final List<CourseLoad> courseLoads = new ArrayList<CourseLoad>(numberOfPeriods);        
        for (int i = 0; i < numberOfPeriods; i++) {
            courseLoads.add(new CourseLoad(i + 1));
        }
        return courseLoads;
    }
    
    private List<CourseLoad> getExistingCourseLoads() throws FenixFilterException, FenixServiceException {
        final List<CourseLoad> courseLoads = new ArrayList<CourseLoad>(getCompetenceCourse().getCompetenceCourseLoads().size());        
        for (final CompetenceCourseLoad competenceCourseLoad : getCompetenceCourse().getCompetenceCourseLoads()) {
            courseLoads.add(new CourseLoad("edit", competenceCourseLoad));
        }
        if (courseLoads.isEmpty()) {
            courseLoads.add(new CourseLoad(1));
        }
        Collections.sort(courseLoads, new BeanComparator("order"));
        return courseLoads;
    }
    
    public void setCourseLoads(List<CourseLoad> courseLoads) {
        getViewState().setAttribute("courseLoads", courseLoads);
    }
    
    public void resetCourseLoad(ValueChangeEvent event) throws FenixFilterException, FenixServiceException {
        calculateCourseLoad((String) event.getNewValue(), 1);
    }
    
    public void resetCorrespondentCourseLoad(ValueChangeEvent event) throws FenixFilterException, FenixServiceException {
        calculateCourseLoad(getRegime(), ((Integer) event.getNewValue()).intValue());
    }
    
    private void calculateCourseLoad(String regime, int newNumberOfPeriods) throws FenixFilterException, FenixServiceException {
        final List<CourseLoad> courseLoads = getCourseLoads();
        if (regime.equals("ANUAL")) {
            if (newNumberOfPeriods > getNumberOfPeriods().intValue()) {
                addCourseLoad(courseLoads);                                
            } else {
                removeCourseLoad(courseLoads);
            }            
        } else if (regime.equals("SEMESTRIAL")) {
            removeCourseLoad(courseLoads);
            setNumberOfPeriods(Integer.valueOf(1));
            // prevent application to reset the value
            setNumberOfPeriods = false; 
        }
        setCourseLoads(courseLoads);
    }

    private void addCourseLoad(final List<CourseLoad> courseLoads) {
        if (getAction().equals("create")) {
            courseLoads.add(new CourseLoad(courseLoads.size() + 1));
        } else if (getAction().equals("edit")) {
            final CourseLoad courseLoad = searchDeleteCourseLoad(courseLoads);
            if (courseLoad != null) {
                courseLoad.setAction("edit");
            } else {
                courseLoads.add(new CourseLoad(courseLoads.size() + 1));
            }
        }        
    }
    
    private CourseLoad searchDeleteCourseLoad(final List<CourseLoad> courseLoads) {       
        for (final CourseLoad courseLoad : courseLoads) {
            if (courseLoad.getAction().equals("delete")) {
                return courseLoad;                
            }
        }
        return null;
    }
    
    private void removeCourseLoad(final List<CourseLoad> courseLoads) {
        if (getAction().equals("create") && courseLoads.size() > 1) {
            courseLoads.remove(courseLoads.size() - 1);
        } else if (getAction().equals("edit") && courseLoads.size() > 1) {
            courseLoads.get(courseLoads.size() - 1).setAction("delete");
        }
    }

    public Integer getSelectedDepartmentUnitID() throws FenixFilterException, FenixServiceException {
        if (selectedDepartmentUnitID == null) {
            if (getAndHoldIntegerParameter("selectedDepartmentUnitID") != null) {
                selectedDepartmentUnitID = getAndHoldIntegerParameter("selectedDepartmentUnitID");
            } else if (getPersonDepartment() != null) {
                selectedDepartmentUnitID = getPersonDepartment().getUnit().getIdInternal();
            }
        }
        return selectedDepartmentUnitID;
    }
    
    public void setSelectedDepartmentUnitID(Integer selectedDepartmentUnitID) {
        this.selectedDepartmentUnitID = selectedDepartmentUnitID;
    }
    
    public Integer getCompetenceCourseID() {
        return (competenceCourseID == null) ? (competenceCourseID = getAndHoldIntegerParameter("competenceCourseID")) : competenceCourseID;
    }
    
    public void setCompetenceCourseID(Integer competenceCourseID) {
        this.competenceCourseID = competenceCourseID;
    }
    
    public CompetenceCourse getCompetenceCourse() throws FenixFilterException, FenixServiceException {
        if (competenceCourse == null && getCompetenceCourseID() != null) {
            competenceCourse = (CompetenceCourse) readDomainObject(CompetenceCourse.class, getCompetenceCourseID()); 
        }
        return competenceCourse;
    }
    
    public void setCompetenceCourse(CompetenceCourse competenceCourse) {
        this.competenceCourse = competenceCourse;
    }
    
    public String getObjectives() throws FenixFilterException, FenixServiceException {
        if (objectives == null && getCompetenceCourse() != null) {
            objectives = getCompetenceCourse().getObjectives();
        }
        return objectives;
    }

    public void setObjectives(String objectives) {
        this.objectives = objectives;
    }

    public String getProgram() throws FenixFilterException, FenixServiceException {
        if (program == null && getCompetenceCourse() != null) {
            program = getCompetenceCourse().getProgram();
        }
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }
    
    public String getEvaluationMethod() throws FenixFilterException, FenixServiceException {
        if (evaluationMethod == null && getCompetenceCourse() != null) {
            evaluationMethod = getCompetenceCourse().getEvaluationMethod();
        }
        return evaluationMethod;
    }

    public void setEvaluationMethod(String evaluationMethod) {
        this.evaluationMethod = evaluationMethod;
    }

    public String getObjectivesEn() throws FenixFilterException, FenixServiceException {
        if (objectivesEn == null && getCompetenceCourse() != null) {
            objectivesEn = getCompetenceCourse().getObjectivesEn();
        }
        return objectivesEn;
    }

    public void setObjectivesEn(String objectivesEn) {
        this.objectivesEn = objectivesEn;
    }

    public String getProgramEn() throws FenixFilterException, FenixServiceException {
        if (programEn == null && getCompetenceCourse() != null) {
            programEn = getCompetenceCourse().getProgramEn();
        }
        return programEn;
    }

    public void setProgramEn(String programEn) {
        this.programEn = programEn;
    }
    
    public String getEvaluationMethodEn() throws FenixFilterException, FenixServiceException {
        if (evaluationMethodEn == null && getCompetenceCourse() != null) {
            evaluationMethodEn = getCompetenceCourse().getEvaluationMethodEn();
        }
        return evaluationMethodEn;
    }

    public void setEvaluationMethodEn(String evaluationMethodEn) {
        this.evaluationMethodEn = evaluationMethodEn;
    } 
    
    public String getStage() throws FenixFilterException, FenixServiceException {
        if (stage == null && getCompetenceCourse() != null) {
            stage = getCompetenceCourse().getCurricularStage().name();
        }
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }
    
    public Integer getBibliographicReferenceID() {
        return (bibliographicReferenceID == null) ? (bibliographicReferenceID = getAndHoldIntegerParameter("bibliographicReferenceID")) : bibliographicReferenceID ;
    }

    public void setBibliographicReferenceID(Integer bibliographicReferenceID) {
        this.bibliographicReferenceID = bibliographicReferenceID;
    }

    public String getYear() throws FenixFilterException, FenixServiceException {
        if (this.year == null && getCompetenceCourse() != null && getBibliographicReferenceID() != null) {
            this.year = getCompetenceCourse().getBibliographicReference(getBibliographicReferenceID()).getYear();
        }
        return this.year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getTitle() throws FenixFilterException, FenixServiceException {
        if (this.title == null && getCompetenceCourse() != null && getBibliographicReferenceID() != null) {
            this.title = getCompetenceCourse().getBibliographicReference(getBibliographicReferenceID()).getTitle();
        }
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() throws FenixFilterException, FenixServiceException {
        if (this.author == null && getCompetenceCourse() != null && getBibliographicReferenceID() != null) {
            this.author = getCompetenceCourse().getBibliographicReference(getBibliographicReferenceID()).getAuthors();
        }
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getReference() throws FenixFilterException, FenixServiceException {
        if (this.reference == null && getCompetenceCourse() != null && getBibliographicReferenceID() != null) {
            this.reference = getCompetenceCourse().getBibliographicReference(getBibliographicReferenceID()).getReference();
        }
        return this.reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getType() throws FenixFilterException, FenixServiceException {
        if (this.type == null && getCompetenceCourse() != null && getBibliographicReferenceID() != null) {
            this.type = getCompetenceCourse().getBibliographicReference(getBibliographicReferenceID()).getType().getName();
        }
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() throws FenixFilterException, FenixServiceException {
        if (this.url == null && getCompetenceCourse() != null && getBibliographicReferenceID() != null) {
            this.url = getCompetenceCourse().getBibliographicReference(getBibliographicReferenceID()).getUrl();
        }
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
    public List<CompetenceCourseLoad> getSortedCompetenceCourseLoads() throws FenixFilterException, FenixServiceException {
        final List<CompetenceCourseLoad> result = new ArrayList<CompetenceCourseLoad>();
        if (getCompetenceCourse() != null) {
            result.addAll(getCompetenceCourse().getCompetenceCourseLoads());
            Collections.sort(result, new BeanComparator("order"));
        }
        return result;
    }
    
    public List<BibliographicReference> getMainBibliographicReferences() throws FenixFilterException, FenixServiceException {
        final List<BibliographicReference> result = new ArrayList<BibliographicReference>();
        if (this.getBibliographicReferences() == null) {
            return result;
        }
        for (final BibliographicReference bibliographicReference : getBibliographicReferences()) {
            if (bibliographicReference.getType().equals(BibliographicReferenceType.MAIN)) {
                result.add(bibliographicReference);
            }
        }
        return result;
    }
    
    public List<BibliographicReference> getSecondaryBibliographicReferences() throws FenixFilterException, FenixServiceException {
        final List<BibliographicReference> result = new ArrayList<BibliographicReference>();
        if (this.getBibliographicReferences() == null) {
            return result;
        }
        for (final BibliographicReference bibliographicReference : getBibliographicReferences()) {
            if (bibliographicReference.getType().equals(BibliographicReferenceType.SECONDARY)) {
                result.add(bibliographicReference);
            }
        }
        return result;
    }
    
    private List<BibliographicReference> getBibliographicReferences() throws FenixFilterException, FenixServiceException {
        return (getCompetenceCourse().getBibliographicReferences() == null) ? null :
            getCompetenceCourse().getBibliographicReferences().getBibliographicReferencesList();
    }
    
    public int getBibliographicReferencesCount() throws FenixFilterException, FenixServiceException {
        return (getBibliographicReferences() != null) ? getBibliographicReferences().size() : 0;
    }
    
    public String createCompetenceCourse() {
        try {
            final Object args[] = { getName(), getNameEn(), getAcronym(), getBasic(),
                    RegimeType.SEMESTRIAL, getCompetenceCourseGroupUnitID() };
            final CompetenceCourse competenceCourse = (CompetenceCourse) ServiceUtils.executeService(
                    getUserView(), "CreateCompetenceCourse", args);
            setCompetenceCourse(competenceCourse);
            return "setCompetenceCourseLoad";
        } catch (FenixFilterException e) {
            addErrorMessage(bolonhaResources.getString("error.creatingCompetenceCourse"));
        } catch (ExistingCompetenceCourseInformationException e) {
            addErrorMessage(getFormatedMessage(bolonhaResources, e.getKey(), e.getArgs()));
        } catch (FenixServiceException e) {
            addErrorMessage(bolonhaResources.getString(e.getMessage()));
        } 
        return "";
    }
    
    public String createCompetenceCourseLoad() {
        try {
            setCompetenceCourseLoad();
            return "setCompetenceCourseAdditionalInformation";
        } catch (FenixFilterException e) {
            addErrorMessage(bolonhaResources.getString("error.editingCompetenceCourse"));
        } catch (FenixServiceException e) {
            addErrorMessage(bolonhaResources.getString(e.getMessage()));
        }
        return "";
    }
    
    public String createCompetenceCourseAdditionalInformation() {
        try {
            setCompetenceCourseAdditionalInformation();
            return "competenceCoursesManagement";
        } catch (FenixFilterException e) {
            addErrorMessage(bolonhaResources.getString("error.editingCompetenceCourse"));
        } catch (FenixServiceException e) {
            addErrorMessage(bolonhaResources.getString(e.getMessage()));
        }
        return "";        
    }
    
    public String editCompetenceCourse() {
        try {
            final Object args[] = {getCompetenceCourseID(), getName(), getNameEn(), getAcronym(), getBasic(),
                    CurricularStage.valueOf(getStage()), false };
            ServiceUtils.executeService(getUserView(), "EditCompetenceCourse", args);
            return "editCompetenceCourseMainPage";
        } catch (FenixFilterException e) {
            addErrorMessage(bolonhaResources.getString("error.editingCompetenceCourse"));
        } catch (ExistingCompetenceCourseInformationException e) {
            addErrorMessage(getFormatedMessage(bolonhaResources, e.getKey(), e.getArgs()));
        } catch (FenixServiceException e) {
            addErrorMessage(bolonhaResources.getString(e.getMessage()));
        } catch (DomainException e) {
            addErrorMessage(domainResources.getString(e.getMessage()));
        }
        return "";
    }   
    
    public String editCompetenceCourseLoad() {
        try {
            setCompetenceCourseLoad();
            return "editCompetenceCourseMainPage";
        } catch (FenixFilterException e) {
            addErrorMessage(bolonhaResources.getString("error.editingCompetenceCourse"));
        } catch (FenixServiceException e) {
            addErrorMessage(bolonhaResources.getString(e.getMessage()));
        }
        return "";
    }
    
    public String editCompetenceCourseAdditionalInformation() {
        try {
            setCompetenceCourseAdditionalInformation();
            return "editCompetenceCourseMainPage";
        } catch (FenixFilterException e) {
            addErrorMessage(bolonhaResources.getString("error.editingCompetenceCourse"));
        } catch (FenixServiceException e) {
            addErrorMessage(bolonhaResources.getString(e.getMessage()));
        }
        return "";
    }
    
    private void setCompetenceCourseLoad() throws FenixFilterException, FenixServiceException {
        Object args[] = { getCompetenceCourseID(), RegimeType.valueOf(getRegime()), getNumberOfPeriods(), getCourseLoads() };
        ServiceUtils.executeService(getUserView(), "EditCompetenceCourseLoad", args);
    }

    private void setCompetenceCourseAdditionalInformation() throws FenixFilterException, FenixServiceException {
        final Object args[] = { getCompetenceCourseID(), getObjectives(), getProgram(),
                getEvaluationMethod(), getObjectivesEn(), getProgramEn(), getEvaluationMethodEn() };
        ServiceUtils.executeService(getUserView(), "EditCompetenceCourse", args);
    }
    
    public String deleteCompetenceCourse() {
        try {
            Object[] args = { getCompetenceCourseID() };
            ServiceUtils.executeService(getUserView(), "DeleteCompetenceCourse", args);
            addInfoMessage(bolonhaResources.getString("competenceCourseDeleted"));
            return "competenceCoursesManagement";
        } catch (FenixFilterException e) {
            addErrorMessage(bolonhaResources.getString("error.deletingCompetenceCourse"));
        } catch (FenixServiceException e) {
            addErrorMessage(bolonhaResources.getString("error.deletingCompetenceCourse"));
        } catch (DomainException e) {
            addErrorMessage(domainResources.getString(e.getMessage()));
        }
        return "";
    }
    
    public String createBibliographicReference() {        
        try {
            final Object[] args = {getCompetenceCourseID(), getYear(), getTitle(), getAuthor(),
                    getReference(), BibliographicReferenceType.valueOf(getType()), getUrl()};
            ServiceUtils.executeService(getUserView(), "EditCompetenceCourse", args);
        } catch (FenixFilterException e) {
            addErrorMessage(bolonhaResources.getString("error.creatingBibliographicReference"));
        } catch (FenixServiceException e) {
            addErrorMessage(e.getMessage());
        } catch (DomainException e) {
            addErrorMessage(domainResources.getString(e.getMessage()));
        }
        setBibliographicReferenceID(-1);
        return "";
    }
    
    public String editBibliographicReference() {        
        try {
            final Object[] args = {getCompetenceCourseID(), getBibliographicReferenceID(), getYear(), getTitle(),
                    getAuthor(), getReference(), BibliographicReferenceType.valueOf(getType()), getUrl()};
            ServiceUtils.executeService(getUserView(), "EditCompetenceCourse", args);
        } catch (FenixFilterException e) {
            addErrorMessage(bolonhaResources.getString("error.editingBibliographicReference"));
        } catch (FenixServiceException e) {
            addErrorMessage(e.getMessage());
        } catch (DomainException e) {
            addErrorMessage(domainResources.getString(e.getMessage()));
        }
        setBibliographicReferenceID(-1);
        return "";
    }
    
    public Integer getBibliographicReferenceIDToDelete() {
        return getAndHoldIntegerParameter("bibliographicReferenceIDToDelete");
    }
    
    public String deleteBibliographicReference() {        
        try {
            final Object[] args = {getCompetenceCourseID(), getBibliographicReferenceIDToDelete() };
            ServiceUtils.executeService(getUserView(), "EditCompetenceCourse", args);            
        } catch (FenixFilterException e) {
            addErrorMessage(bolonhaResources.getString("error.deletingBibliographicReference"));
        } catch (FenixServiceException e) {
            addErrorMessage(e.getMessage());
        } catch (DomainException e) {
            addErrorMessage(domainResources.getString(e.getMessage()));
        }   
        setBibliographicReferenceID(-1);
        return "";
    }
    
    public Integer getOldPosition() {
        return getAndHoldIntegerParameter("oldPosition");
    }
    
    public Integer getNewPosition() {
        return getAndHoldIntegerParameter("newPosition");
    }
    
    public String switchBibliographicReferencePosition() {        
        try {
            final Object[] args = {getCompetenceCourseID(), getOldPosition(), getNewPosition() };
            ServiceUtils.executeService(getUserView(), "EditCompetenceCourse", args);
        } catch (FenixFilterException e) {
            addErrorMessage(bolonhaResources.getString("error.switchBibliographicReferencePositions"));
        } catch (FenixServiceException e) {
            addErrorMessage(bolonhaResources.getString(e.getMessage()));
        } catch (DomainException e) {
            addErrorMessage(domainResources.getString(e.getMessage()));
        }
        setBibliographicReferenceID(-1);
        return "";
    }
    
    public String cancelBibliographicReference() {        
        setBibliographicReferenceID(-1);
        return "";
    }

    
    public String changeCompetenceCourseState() {
        try {
            CurricularStage changed = (getCompetenceCourse().getCurricularStage().equals(CurricularStage.PUBLISHED) ? CurricularStage.APPROVED : CurricularStage.PUBLISHED);
            
            final Object args[] = { getCompetenceCourseID(), getName(), getNameEn(), getAcronym(), getBasic(), changed, true };
            ServiceUtils.executeService(getUserView(), "EditCompetenceCourse", args);
            return "";
        } catch (FenixFilterException e) {
            addErrorMessage(bolonhaResources.getString("error.editingCompetenceCourse"));
        } catch (FenixServiceException e) {
            addErrorMessage(bolonhaResources.getString(e.getMessage()));
        } catch (DomainException e) {
            addErrorMessage(domainResources.getString(e.getMessage()));
        }
        return "";
    }    
}
