package net.sourceforge.fenixedu.domain;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship.ResponsibleForValidator;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship.ResponsibleForValidator.InvalidCategory;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship.ResponsibleForValidator.MaxResponsibleForExceed;
import net.sourceforge.fenixedu.dataTransferObject.credits.InfoCredits;
import net.sourceforge.fenixedu.domain.credits.ManagementPositionCreditLine;
import net.sourceforge.fenixedu.domain.credits.util.InfoCreditsBuilder;
import net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
import net.sourceforge.fenixedu.domain.degree.finalProject.TeacherDegreeFinalProjectStudent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Group;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupStudent;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.publication.Publication;
import net.sourceforge.fenixedu.domain.publication.PublicationTeacher;
import net.sourceforge.fenixedu.domain.teacher.Advise;
import net.sourceforge.fenixedu.domain.teacher.AdviseType;
import net.sourceforge.fenixedu.domain.teacher.Category;
import net.sourceforge.fenixedu.domain.teacher.DegreeTeachingService;
import net.sourceforge.fenixedu.domain.teacher.OldPublication;
import net.sourceforge.fenixedu.domain.teacher.Orientation;
import net.sourceforge.fenixedu.domain.teacher.PublicationsNumber;
import net.sourceforge.fenixedu.domain.teacher.ServiceExemptionType;
import net.sourceforge.fenixedu.domain.teacher.TeacherLegalRegimen;
import net.sourceforge.fenixedu.domain.teacher.TeacherPersonalExpectation;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.domain.teacher.TeacherServiceExemption;
import net.sourceforge.fenixedu.util.LegalRegimenType;
import net.sourceforge.fenixedu.util.OldPublicationType;
import net.sourceforge.fenixedu.util.OrientationType;
import net.sourceforge.fenixedu.util.PeriodState;
import net.sourceforge.fenixedu.util.PublicationArea;
import net.sourceforge.fenixedu.util.PublicationType;
import net.sourceforge.fenixedu.util.State;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.joda.time.Interval;
import org.joda.time.PeriodType;
import org.joda.time.YearMonthDay;

public class Teacher extends Teacher_Base {

    public static final Comparator TEACHER_COMPARATOR_BY_CATEGORY_AND_NUMBER = new Comparator<Teacher>() {

        public int compare(Teacher teacher1, Teacher teacher2) {
            final int teacherNumberCompare = teacher1.getTeacherNumber().compareTo(
                    teacher2.getTeacherNumber());

            if (teacher1.getCategory() == null && teacher2.getCategory() == null) {
                return teacherNumberCompare;
            } else if (teacher1.getCategory() == null) {
                return 1;
            } else if (teacher2.getCategory() == null) {
                return -1;
            } else {
                final int categoryCompare = teacher1.getCategory().compareTo(teacher2.getCategory());
                return categoryCompare == 0 ? teacherNumberCompare : categoryCompare;
            }
        }

    };

    public Teacher() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    /***************************************************************************
     * BUSINESS SERVICES *
     **************************************************************************/

    public void addToTeacherInformationSheet(Publication publication, PublicationArea publicationArea) {
        new PublicationTeacher(publication, this, publicationArea);
    }

    public void removeFromTeacherInformationSheet(Publication publication) {
        Iterator<PublicationTeacher> iterator = getTeacherPublications().iterator();

        while (iterator.hasNext()) {
            PublicationTeacher publicationTeacher = iterator.next();
            if (publicationTeacher.getPublication().equals(publication)) {
                iterator.remove();
                publicationTeacher.delete();
                return;
            }
        }
    }

    public Boolean canAddPublicationToTeacherInformationSheet(PublicationArea area) {
        // NOTA : a linha seguinte cont???m um n???mero expl???cito quando n???o
        // deve.
        // Isto deve ser mudado! Mas esta mudan???a implica tornar expl???cito o
        // conceito de Ficha de docente.
        return new Boolean(countPublicationsInArea(area) < 5);

    }

    public List<Professorship> responsibleFors() {
        final List<Professorship> result = new ArrayList<Professorship>();
        for (final Professorship professorship : this.getProfessorships()) {
            if (professorship.isResponsibleFor())
                result.add(professorship);
        }
        return result;
    }

    public Professorship responsibleFor(ExecutionCourse executionCourse) {
        for (final Professorship professorship : this.getProfessorships()) {
            if (professorship.getResponsibleFor()
                    && professorship.getExecutionCourse() == executionCourse) {
                return professorship;
            }
        }
        return null;
    }

    public void updateResponsabilitiesFor(Integer executionYearId, List<Integer> executionCourses)
            throws MaxResponsibleForExceed, InvalidCategory {

        if (executionYearId == null || executionCourses == null)
            throw new NullPointerException();
        
        boolean responsible;
        for (final Professorship professorship : this.getProfessorships()) {
            final ExecutionCourse executionCourse = professorship.getExecutionCourse();            
            if (executionCourse.getExecutionPeriod().getExecutionYear().getIdInternal().equals(executionYearId)) {
                responsible = executionCourses.contains(executionCourse.getIdInternal());
                if(!professorship.getResponsibleFor().equals(Boolean.valueOf(responsible))) {
                    ResponsibleForValidator.getInstance().validateResponsibleForList(this, executionCourse, professorship);
                    professorship.setResponsibleFor(responsible);
                }
            }
        }
    }

    public Unit getCurrentWorkingUnit() {
        Employee employee = this.getPerson().getEmployee();
        if (employee != null) {
            return employee.getCurrentWorkingPlace();
        }
        return null;
    }

    public Unit getLastWorkingUnit() {
        Employee employee = this.getPerson().getEmployee();
        if (employee != null) {
            return employee.getLastWorkingPlace();
        }
        return null;
    }

    public Unit getLastWorkingUnit(YearMonthDay begin, YearMonthDay end) {
        Employee employee = this.getPerson().getEmployee();
        if (employee != null) {
            return employee.getLastWorkingPlaceByPeriod(begin, end);
        }
        return null;
    }
    
    public Category getLastCategory(YearMonthDay begin, YearMonthDay end) {
        TeacherLegalRegimen lastLegalRegimen = getLastLegalRegimenWithoutEndSituations(begin, end);
        return (lastLegalRegimen != null) ? lastLegalRegimen.getCategory() : null;
    }
    
    public TeacherLegalRegimen getLastLegalRegimenWithoutEndSituations(YearMonthDay begin, YearMonthDay end) {
        SortedSet<TeacherLegalRegimen> legalRegimens = new TreeSet<TeacherLegalRegimen>(
                TeacherLegalRegimen.TEACHER_LEGAL_REGIMEN_COMPARATOR_BY_BEGIN_DATE);
        
        legalRegimens.addAll(getAllLegalRegimensWithoutEndSituations(begin, end));
        return (!legalRegimens.isEmpty()) ? legalRegimens.last() : null;   
    }
    
    public Department getCurrentWorkingDepartment() {
        Employee employee = this.getPerson().getEmployee();
        if (employee != null) {
            return employee.getCurrentDepartmentWorkingPlace();
        }
        return null;
    }

    public Department getLastWorkingDepartment() {
        Employee employee = this.getPerson().getEmployee();
        if (employee != null) {
            return employee.getLastDepartmentWorkingPlace();
        }
        return null;
    }   
    
    public List<Unit> getWorkingPlacesByPeriod(YearMonthDay beginDate, YearMonthDay endDate) {
        Employee employee = this.getPerson().getEmployee();
        if (employee != null) {
            return employee.getWorkingPlacesByPeriod(beginDate, endDate);
        }
        return new ArrayList<Unit>();
    }

    public Category getCategory() {
        TeacherLegalRegimen regimen = getLastLegalRegimenWithoutEndSituations();
        if (regimen != null) {
            return regimen.getCategory();
        }
        return null;
    }
       
    public TeacherLegalRegimen getLastLegalRegimenWithoutEndSituations() {
        YearMonthDay date = null;
        TeacherLegalRegimen regimenToReturn = null;
        for (TeacherLegalRegimen regimen : this.getLegalRegimens()) {
            if (!regimen.isEndSituation()) {
                if (regimen.isActive(new YearMonthDay())) {
                    return regimen;
                } else if (date == null || date.isBefore(regimen.getEndDateYearMonthDay())) { 
                    date = regimen.getEndDateYearMonthDay(); 
                    regimenToReturn = regimen;
                }
            }
        }
        return regimenToReturn;
    }

    public List<TeacherLegalRegimen> getAllLegalRegimensWithoutEndSituations(YearMonthDay beginDate, YearMonthDay endDate) {
        Set<TeacherLegalRegimen> legalRegimens = new HashSet<TeacherLegalRegimen>();
        for (TeacherLegalRegimen legalRegimen : this.getLegalRegimens()) {
            if (!legalRegimen.isEndSituation() && legalRegimen.belongsToPeriod(beginDate, endDate)) {
                legalRegimens.add(legalRegimen);
            }
        }
        return new ArrayList<TeacherLegalRegimen>(legalRegimens);
    }
    
    public List<TeacherLegalRegimen> getAllLegalRegimensWithoutEndSituations() {
        Set<TeacherLegalRegimen> legalRegimens = new HashSet<TeacherLegalRegimen>();
        for (TeacherLegalRegimen legalRegimen : getLegalRegimens()) {
            if (!legalRegimen.isEndSituation()) {
                legalRegimens.add(legalRegimen);
            }
        }
        return new ArrayList<TeacherLegalRegimen>(legalRegimens);
    }

    public TeacherPersonalExpectation getTeacherPersonalExpectationByExecutionYear(
            ExecutionYear executionYear) {
        TeacherPersonalExpectation result = null;

        List<TeacherPersonalExpectation> teacherPersonalExpectations = this
                .getTeacherPersonalExpectations();

        for (TeacherPersonalExpectation teacherPersonalExpectation : teacherPersonalExpectations) {
            if (teacherPersonalExpectation.getExecutionYear().equals(executionYear)) {
                result = teacherPersonalExpectation;
                break;
            }
        }

        return result;
    }

    public List<Proposal> getFinalDegreeWorksByExecutionYear(ExecutionYear executionYear) {
        List<Proposal> proposalList = new ArrayList<Proposal>();
        for (Iterator iter = getAssociatedProposalsByOrientator().iterator(); iter.hasNext();) {
            Proposal proposal = (Proposal) iter.next();
            if (proposal.getScheduleing().getExecutionDegreesSet().iterator().next().getExecutionYear()
                    .equals(executionYear)) {
                // if it was attributed by the coordinator the proposal is
                // efective
                if (proposal.getGroupAttributed() != null) {
                    proposalList.add(proposal);
                }
                // if not, we have to verify if the teacher has proposed it to
                // any student(s) and if that(those) student(s) has(have)
                // accepted it
                else {
                    Group attributedGroupByTeacher = proposal.getGroupAttributedByTeacher();
                    if (attributedGroupByTeacher != null) {
                        boolean toAdd = false;
                        for (Iterator iterator = attributedGroupByTeacher.getGroupStudents().iterator(); iterator
                                .hasNext();) {
                            GroupStudent groupStudent = (GroupStudent) iterator.next();
                            Proposal studentProposal = groupStudent
                                    .getFinalDegreeWorkProposalConfirmation();
                            if (studentProposal != null && studentProposal.equals(proposal)) {
                                toAdd = true;
                            } else {
                                toAdd = false;
                            }
                        }
                        if (toAdd) {
                            proposalList.add(proposal);
                        }
                    }
                }
            }
        }
        return proposalList;
    }

    public List<ExecutionCourse> getLecturedExecutionCoursesByExecutionYear(ExecutionYear executionYear) {
        List<ExecutionCourse> executionCourses = new ArrayList();
        for (Iterator iter = executionYear.getExecutionPeriods().iterator(); iter.hasNext();) {
            ExecutionPeriod executionPeriod = (ExecutionPeriod) iter.next();
            executionCourses.addAll(getLecturedExecutionCoursesByExecutionPeriod(executionPeriod));
        }
        return executionCourses;
    }

    public List<ExecutionCourse> getLecturedExecutionCoursesByExecutionPeriod(
            final ExecutionPeriod executionPeriod) {
        List<ExecutionCourse> executionCourses = new ArrayList<ExecutionCourse>();
        for (Iterator iter = getProfessorships().iterator(); iter.hasNext();) {
            Professorship professorship = (Professorship) iter.next();
            ExecutionCourse executionCourse = professorship.getExecutionCourse();

            if (executionCourse.getExecutionPeriod().equals(executionPeriod)) {
                executionCourses.add(executionCourse);
            }
        }
        return executionCourses;
    }

    public List<ExecutionCourse> getAllLecturedExecutionCourses() {
        List<ExecutionCourse> executionCourses = new ArrayList<ExecutionCourse>();

        for (Professorship professorship : this.getProfessorships()) {
            executionCourses.add(professorship.getExecutionCourse());
        }

        return executionCourses;
    }

    public Double getHoursLecturedOnExecutionCourse(ExecutionCourse executionCourse) {
        double returnValue = 0;

        Professorship professorship = getProfessorshipByExecutionCourse(executionCourse);
        TeacherService teacherService = getTeacherServiceByExecutionPeriod(executionCourse
                .getExecutionPeriod());
        if (teacherService != null) {
            List<DegreeTeachingService> teachingServices = teacherService
                    .getDegreeTeachingServiceByProfessorship(professorship);
            for (DegreeTeachingService teachingService : teachingServices) {
                returnValue += ((teachingService.getPercentage() / 100) * teachingService.getShift()
                        .hours());
            }
        }
        return returnValue;
    }

    public TeacherService getTeacherServiceByExecutionPeriod(final ExecutionPeriod executionPeriod) {
        return (TeacherService) CollectionUtils.find(getTeacherServices(), new Predicate() {
            public boolean evaluate(Object arg0) {
                TeacherService teacherService = (TeacherService) arg0;
                return teacherService.getExecutionPeriod() == executionPeriod;
            }
        });
    }

    public Professorship getProfessorshipByExecutionCourse(final ExecutionCourse executionCourse) {
        return (Professorship) CollectionUtils.find(getProfessorships(), new Predicate() {
            public boolean evaluate(Object arg0) {
                Professorship professorship = (Professorship) arg0;
                return professorship.getExecutionCourse() == executionCourse;
            }
        });
    }

    public boolean hasProfessorshipForExecutionCourse(final ExecutionCourse executionCourse) {
        return (getProfessorshipByExecutionCourse(executionCourse) != null);
    }

    public List<Professorship> getDegreeProfessorshipsByExecutionPeriod(
            final ExecutionPeriod executionPeriod) {

        return (List<Professorship>) CollectionUtils.select(getProfessorships(), new Predicate() {
            public boolean evaluate(Object arg0) {
                Professorship professorship = (Professorship) arg0;
                return professorship.getExecutionCourse().getExecutionPeriod() == executionPeriod
                        && !professorship.getExecutionCourse().isMasterDegreeOnly();
            }
        });
    }

    public List<DegreeCurricularPlan> getCoordinatedDegreeCurricularPlans() {
        Set<DegreeCurricularPlan> result = new HashSet<DegreeCurricularPlan>();
        for (Coordinator coordinator : getCoordinators()) {
            result.add(coordinator.getExecutionDegree().getDegreeCurricularPlan());
        }
        return new ArrayList<DegreeCurricularPlan>(result);
    }

    public List<DegreeCurricularPlan> getCoordinatedActiveDegreeCurricularPlans() {
        final Set<DegreeCurricularPlan> temp = new HashSet<DegreeCurricularPlan>();
        for (Coordinator coordinator : getCoordinators()) {
            DegreeCurricularPlan degreeCurricularPlan = coordinator.getExecutionDegree()
                    .getDegreeCurricularPlan();
            if (degreeCurricularPlan.getState().equals(DegreeCurricularPlanState.ACTIVE)) {
                temp.add(degreeCurricularPlan);
            }
        }

        List<DegreeCurricularPlan> result = new ArrayList<DegreeCurricularPlan>(temp);
        Collections.sort(result, DegreeCurricularPlan.DEGREE_CURRICULAR_PLAN_COMPARATOR_BY_DEGREE_TYPE_AND_EXECUTION_DEGREE_AND_DEGREE_CODE);
        return result;
    }

    public List<ExecutionDegree> getCoordinatedExecutionDegrees() {
        List<ExecutionDegree> result = new ArrayList<ExecutionDegree>();
        for (Coordinator coordinator : getCoordinators()) {
            result.add(coordinator.getExecutionDegree());
        }
        return result;
    }

    public Collection<ExecutionDegree> getCoordinatedExecutionDegrees(DegreeCurricularPlan degreeCurricularPlan) {
        Set<ExecutionDegree> result = new TreeSet<ExecutionDegree>(new BeanComparator("executionYear"));
        for (Coordinator coordinator : getCoordinators()) {
            if (coordinator.getExecutionDegree().getDegreeCurricularPlan().equals(degreeCurricularPlan)) {
                result.add(coordinator.getExecutionDegree());    
            }
        }
        return result;
    }

    /***************************************************************************
     * OTHER METHODS *
     **************************************************************************/

    public InfoCredits getExecutionPeriodCredits(ExecutionPeriod executionPeriod) {
        return InfoCreditsBuilder.build(this, executionPeriod);
    }

    /***************************************************************************
     * PRIVATE METHODS *
     **************************************************************************/

    private int countPublicationsInArea(PublicationArea area) {
        int count = 0;
        for (PublicationTeacher publicationTeacher : getTeacherPublications()) {
            if (publicationTeacher.getPublicationArea().equals(area)) {
                count++;
            }
        }
        return count;
    }

    public List<MasterDegreeThesisDataVersion> getGuidedMasterDegreeThesisByExecutionYear(
            ExecutionYear executionYear) {
        List<MasterDegreeThesisDataVersion> guidedThesis = new ArrayList<MasterDegreeThesisDataVersion>();

        for (MasterDegreeThesisDataVersion masterDegreeThesisDataVersion : this
                .getMasterDegreeThesisGuider()) {

            if (masterDegreeThesisDataVersion.getCurrentState().getState() == State.ACTIVE) {

                List<ExecutionDegree> executionDegrees = masterDegreeThesisDataVersion
                        .getMasterDegreeThesis().getStudentCurricularPlan().getDegreeCurricularPlan()
                        .getExecutionDegrees();

                for (ExecutionDegree executionDegree : executionDegrees) {
                    if (executionDegree.getExecutionYear().equals(executionYear)) {
                        guidedThesis.add(masterDegreeThesisDataVersion);
                    }
                }

            }
        }

        return guidedThesis;
    }

    public List<MasterDegreeThesisDataVersion> getAllGuidedMasterDegreeThesis() {
        List<MasterDegreeThesisDataVersion> guidedThesis = new ArrayList<MasterDegreeThesisDataVersion>();

        for (MasterDegreeThesisDataVersion masterDegreeThesisDataVersion : this
                .getMasterDegreeThesisGuider()) {
            if (masterDegreeThesisDataVersion.getCurrentState().getState().equals(State.ACTIVE)) {
                guidedThesis.add(masterDegreeThesisDataVersion);
            }
        }

        return guidedThesis;
    }

    public void createTeacherPersonalExpectation(
            net.sourceforge.fenixedu.dataTransferObject.InfoTeacherPersonalExpectation infoTeacherPersonalExpectation,
            ExecutionYear executionYear) {

        checkIfCanCreatePersonalExpectation(executionYear);

        TeacherPersonalExpectation teacherPersonalExpectation = new TeacherPersonalExpectation(
                infoTeacherPersonalExpectation, executionYear);

        addTeacherPersonalExpectations(teacherPersonalExpectation);

    }

    private void checkIfCanCreatePersonalExpectation(ExecutionYear executionYear) {
        TeacherPersonalExpectation storedTeacherPersonalExpectation = getTeacherPersonalExpectationByExecutionYear(executionYear);

        if (storedTeacherPersonalExpectation != null) {
            throw new DomainException(
                    "error.exception.personalExpectation.expectationAlreadyExistsForExecutionYear");
        }

        TeacherExpectationDefinitionPeriod teacherExpectationDefinitionPeriod = this
                .getCurrentWorkingDepartment().readTeacherExpectationDefinitionPeriodByExecutionYear(
                        executionYear);

        if (teacherExpectationDefinitionPeriod.isPeriodOpen() == false) {
            throw new DomainException(
                    "error.exception.personalExpectation.definitionPeriodForExecutionYearAlreadyExpired");
        }

    }

    public List<TeacherServiceExemption> getServiceExemptionsWithoutMedicalSituations(YearMonthDay beginDate,
            YearMonthDay endDate) {

        List<TeacherServiceExemption> serviceExemptions = new ArrayList<TeacherServiceExemption>();
        for (TeacherServiceExemption serviceExemption : this.getServiceExemptionSituations()) {
            if (!serviceExemption.isMedicalSituation()
                    && serviceExemption.belongsToPeriod(beginDate, endDate)) {
                serviceExemptions.add(serviceExemption);
            }
        }
        return serviceExemptions;
    }

    public List<PersonFunction> getPersonFuntions(YearMonthDay beginDate, YearMonthDay endDate) {
        return getPerson().getPersonFuntions(beginDate, endDate);
    }

    public int getHoursByCategory(ExecutionPeriod executionPeriod) {
        OccupationPeriod occupationPeriod = executionPeriod.getLessonsPeriod();                        
        return getHoursByCategory(occupationPeriod);
    }

    private int getHoursByCategory(OccupationPeriod lessonsPeriod) {        
        if (lessonsPeriod != null) {                  
            TeacherLegalRegimen teacherLegalRegimen = getLastLegalRegimenWithoutEndSituations(lessonsPeriod.getStartYearMonthDay(), lessonsPeriod.getEndYearMonthDay());        
            if(teacherLegalRegimen != null && teacherLegalRegimen.getLessonHours() != null) {
                return teacherLegalRegimen.getLessonHours();
            }   
        }
        return 0;
    }

    public double getManagementFunctionsCredits(ExecutionPeriod executionPeriod) {
        double totalCredits = 0.0;        
        for (PersonFunction personFunction : this.getPerson().getPersonFunctions()) {
            if (personFunction.belongsToPeriod(executionPeriod.getBeginDateYearMonthDay(), executionPeriod.getEndDateYearMonthDay())) {
                totalCredits = (personFunction.getCredits() != null) ? totalCredits + personFunction.getCredits() : totalCredits;
            }
        }
        return round(totalCredits);
    }

    public Category getCategoryForCreditsByPeriod(ExecutionPeriod executionPeriod) {

        OccupationPeriod occupationPeriod = executionPeriod.getLessonsPeriod();
        if (occupationPeriod == null) {
            return null;
        }        
        return getLastCategory(occupationPeriod.getStartYearMonthDay(), occupationPeriod.getEndYearMonthDay());
    }
    
    public double getServiceExemptionCredits(ExecutionPeriod executionPeriod) {
        
        OccupationPeriod occupationPeriod = executionPeriod.getLessonsPeriod();
        if (occupationPeriod == null) {
            return 0;
        }

        List<TeacherServiceExemption> serviceExemptions = getServiceExemptionsWithoutMedicalSituations(occupationPeriod
                .getStartYearMonthDay(), occupationPeriod.getEndYearMonthDay());

        if (!serviceExemptions.isEmpty()) {
            TeacherServiceExemption teacherServiceExemption = chooseOneServiceExemption(serviceExemptions, occupationPeriod);
            return getCreditsForServiceExemption(executionPeriod, teacherServiceExemption, occupationPeriod);
        }
        return 0.0;
    }    

    private double getCreditsForServiceExemption(ExecutionPeriod executionPeriod,
            TeacherServiceExemption teacherServiceExemption, OccupationPeriod lessonsPeriod) {
            
        if (teacherServiceExemption != null && teacherServiceExemption.isServiceExemptionToCountInCredits()) {
                               
            Integer daysBetween = null;
            if(teacherServiceExemption.getEndYearMonthDay() != null) {
                daysBetween = new Interval(teacherServiceExemption.getStartYearMonthDay().toDateMidnight(),
                        teacherServiceExemption.getEndYearMonthDay().toDateMidnight()).toPeriod(PeriodType.days()).getDays();
            }
            
            if (teacherServiceExemption.getType().equals(ServiceExemptionType.SABBATICAL)
                    || teacherServiceExemption.getType().equals(ServiceExemptionType.TEACHER_SERVICE_EXEMPTION_E_C_D_U)
                    || teacherServiceExemption.getType().equals(ServiceExemptionType.GRANT_OWNER_EQUIVALENCE_WITH_SALARY_SABBATICAL)) {
                
                int sabbaticalMonths = getSabbaticalMonthDuration(teacherServiceExemption);
                return getSabbaticalCredits(sabbaticalMonths, lessonsPeriod, teacherServiceExemption,
                        executionPeriod);
            }                                                            
            else if (daysBetween == null || daysBetween > 90) {                                
                double percentage = getOverlapPercentage(lessonsPeriod, teacherServiceExemption); 
                int categoryHours = getHoursByCategory(lessonsPeriod);
                return round(percentage * categoryHours);        
            }
        }
        return 0.0;
    }
    
    private int getSabbaticalCredits(int sabbaticalMonths, OccupationPeriod lessonsPeriod, TeacherServiceExemption teacherServiceExemption,
            ExecutionPeriod executionPeriod) {
        
        double overlapPercentage1 = getOverlapPercentage(lessonsPeriod, teacherServiceExemption), overlapPercentage2 = 0.0;
        
        if(overlapPercentage1 == 1.0) {
            return getCreditsByServiceExemptionType(teacherServiceExemption.getType(), lessonsPeriod);
        } else if(executionPeriod.containsDay(teacherServiceExemption.getStart())) {                                                                 
            ExecutionPeriod nextExecutionPeriod = executionPeriod.getNextExecutionPeriod();
            if(sabbaticalMonths >= 11) {                
                nextExecutionPeriod = (nextExecutionPeriod != null) ? nextExecutionPeriod.getNextExecutionPeriod() : null;
            }                
            if(nextExecutionPeriod != null) {                            
                OccupationPeriod nextLessonsPeriod = nextExecutionPeriod.getLessonsPeriod();                        
                overlapPercentage2 = getOverlapPercentage(nextLessonsPeriod, teacherServiceExemption);                          
            }
            if(overlapPercentage1 > overlapPercentage2) {
               return getCreditsByServiceExemptionType(teacherServiceExemption.getType(), lessonsPeriod); 
            }            
        } else {
            ExecutionPeriod previousExecutionPeriod = executionPeriod.getPreviousExecutionPeriod();
            if(sabbaticalMonths >= 11) {
                previousExecutionPeriod = previousExecutionPeriod.getPreviousExecutionPeriod();
            }
            OccupationPeriod previousLessonsPeriod = previousExecutionPeriod.getLessonsPeriod();
            overlapPercentage2 = getOverlapPercentage(previousLessonsPeriod, teacherServiceExemption);
            if(overlapPercentage1 > overlapPercentage2) {
                return getCreditsByServiceExemptionType(teacherServiceExemption.getType(), lessonsPeriod); 
            }                        
        }        
        return 0;
    }
    
    private int getCreditsByServiceExemptionType(ServiceExemptionType serviceExemptionType, OccupationPeriod occupationPeriod) {
        if (serviceExemptionType.equals(ServiceExemptionType.SABBATICAL)) {
            return 6;
        }
        return getHoursByCategory(occupationPeriod);        
    }

    private int getSabbaticalMonthDuration(TeacherServiceExemption teacherServiceExemption) {        
        Interval serviceExemptionsInterval = new Interval(teacherServiceExemption.getStartYearMonthDay().toDateMidnight(), 
                teacherServiceExemption.getEndYearMonthDay().toDateMidnight());        
        return serviceExemptionsInterval.toPeriod(PeriodType.months()).getMonths();               
    }

    private double getOverlapPercentage(OccupationPeriod lessonsPeriod, TeacherServiceExemption teacherServiceExemption) {   
        if(lessonsPeriod == null) {
            return 0.0;
        }
        
        Interval lessonsInterval = new Interval(lessonsPeriod.getStartYearMonthDay().toDateMidnight(), 
                lessonsPeriod.getEndYearMonthDay().toDateMidnight());                       
        Interval serviceExemptionsInterval = new Interval(teacherServiceExemption.getStartYearMonthDay().toDateMidnight(), 
                (teacherServiceExemption.getEndYearMonthDay() != null) ? 
                        teacherServiceExemption.getEndYearMonthDay().toDateMidnight() : lessonsPeriod.getEndYearMonthDay().toDateMidnight());
        
        Interval overlapInterval = lessonsInterval.overlap(serviceExemptionsInterval);              
        if(overlapInterval != null) {
            int intersectedDays = overlapInterval.toPeriod(PeriodType.days()).getDays();
            return round(Double.valueOf(intersectedDays) / Double.valueOf(lessonsInterval.toPeriod(PeriodType.days()).getDays()));
        } 
        
        return 0.0;
    }
        
    private Double round(double n) {
        return Math.round((n * 100.0)) / 100.0;
    }
       
    public boolean isDeceased() {
        for (TeacherLegalRegimen legalRegimen : getLegalRegimens()) {
            if (legalRegimen.getLegalRegimenType().equals(LegalRegimenType.DEATH)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isInactive(ExecutionPeriod executionPeriod) {
        if (executionPeriod != null) {            
            OccupationPeriod occupationPeriod = executionPeriod.getLessonsPeriod();  
            if(occupationPeriod != null) {
                List<TeacherLegalRegimen> allLegalRegimens = getAllLegalRegimensWithoutEndSituations(occupationPeriod.getStartYearMonthDay(),
                                occupationPeriod.getEndYearMonthDay());
                if (allLegalRegimens.isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean isMonitor(ExecutionPeriod executionPeriod) {
        if (executionPeriod != null) {
            Category category = getCategoryForCreditsByPeriod(executionPeriod);
            if (category != null
                    && (category.getCode().equalsIgnoreCase("MNT") || category.getCode()
                            .equalsIgnoreCase("MNL"))) {
                return true;
            }
        }
        return false;
    }
    
    public TeacherServiceExemption getDominantServiceExemption(ExecutionPeriod executionPeriod) {        
        OccupationPeriod occupationPeriod = executionPeriod.getLessonsPeriod();
        List<TeacherServiceExemption> serviceExemptions = getServiceExemptionsWithoutMedicalSituations(
                occupationPeriod.getStartYearMonthDay(), occupationPeriod.getEndYearMonthDay());       
        return chooseOneServiceExemption(serviceExemptions, occupationPeriod);
    }

    private TeacherServiceExemption chooseOneServiceExemption(List<TeacherServiceExemption> serviceExemptions,
            OccupationPeriod lessonsPeriod) {
       
        Integer numberOfDaysInPeriod = null, maxDays = 0;
        TeacherServiceExemption teacherServiceExemption = null;
        
        if(lessonsPeriod == null){
            return null;
        }

        Interval lessonsInterval = new Interval(lessonsPeriod.getStartYearMonthDay().toDateMidnight(), 
                lessonsPeriod.getEndYearMonthDay().toDateMidnight());                       
                
        for (TeacherServiceExemption serviceExemption : serviceExemptions) {
            Interval serviceExemptionsInterval = new Interval(serviceExemption.getStartYearMonthDay().toDateMidnight(), 
                    (serviceExemption.getEndYearMonthDay() != null) ? 
                            serviceExemption.getEndYearMonthDay().toDateMidnight() :                        
                                lessonsPeriod.getEndYearMonthDay().toDateMidnight());            
            Interval overlapInterval = lessonsInterval.overlap(serviceExemptionsInterval);
            if(overlapInterval != null) {
                numberOfDaysInPeriod = overlapInterval.toPeriod(PeriodType.days()).getDays();
                if (numberOfDaysInPeriod >= maxDays) {
                    maxDays = numberOfDaysInPeriod;
                    teacherServiceExemption = serviceExemption;
                }
            }
        }
        return teacherServiceExemption;
    }
    
    public List<Advise> getAdvisesByAdviseTypeAndExecutionYear(AdviseType adviseType,
            ExecutionYear executionYear) {

        List<Advise> result = new ArrayList<Advise>();
        Date executionYearStartDate = executionYear.getBeginDate();
        Date executionYearEndDate = executionYear.getEndDate();

        for (Advise advise : this.getAdvises()) {
            if ((advise.getAdviseType() == adviseType)) {
                Date adviseStartDate = advise.getStartExecutionPeriod().getBeginDate();
                Date adviseEndDate = advise.getEndExecutionPeriod().getEndDate();

                if (((executionYearStartDate.compareTo(adviseStartDate) < 0) && (executionYearEndDate
                        .compareTo(adviseStartDate) < 0))
                        || ((executionYearStartDate.compareTo(adviseEndDate) > 0) && (executionYearEndDate
                                .compareTo(adviseEndDate) > 0))) {
                    continue;
                }
                result.add(advise);
            }
        }

        return result;
    }

    public List<Advise> getAdvisesByAdviseType(AdviseType adviseType) {

        List<Advise> result = new ArrayList<Advise>();
        for (Advise advise : this.getAdvises()) {
            if (advise.getAdviseType() == adviseType) {
                result.add(advise);
            }
        }

        return result;
    }    

    public double getBalanceOfCreditsUntil(ExecutionPeriod executionPeriod) throws ParseException {
        
        double balanceCredits = 0.0;
        ExecutionPeriod firstExecutionPeriod = TeacherService.getStartExecutionPeriodForCredits();
                
        TeacherService firstTeacherService = getTeacherServiceByExecutionPeriod(firstExecutionPeriod);
        if (firstTeacherService != null) {
            balanceCredits = firstTeacherService.getPastServiceCredits();
        }

        if(executionPeriod.isAfter(firstExecutionPeriod)) {
            balanceCredits = sumCreditsBetweenPeriods(firstExecutionPeriod.getNextExecutionPeriod(),
                    executionPeriod, balanceCredits);
        }
        return balanceCredits;
    }

    private double sumCreditsBetweenPeriods(ExecutionPeriod startPeriod, ExecutionPeriod endExecutionPeriod, double totalCredits) throws ParseException {

        ExecutionPeriod executionPeriodAfterEnd = endExecutionPeriod.getNextExecutionPeriod();
        while (startPeriod != executionPeriodAfterEnd) {
            TeacherService teacherService = getTeacherServiceByExecutionPeriod(startPeriod);
            if (teacherService != null) {                
                totalCredits += teacherService.getCredits();                                
            }
            totalCredits += getManagementFunctionsCredits(startPeriod);
            totalCredits += getServiceExemptionCredits(startPeriod);
            totalCredits -= getMandatoryLessonHours(startPeriod);
            startPeriod = startPeriod.getNextExecutionPeriod();
        }
        return totalCredits;
    }
    
    public int getMandatoryLessonHours(ExecutionPeriod executionPeriod) {
        OccupationPeriod occupationPeriod = executionPeriod.getLessonsPeriod();
        if (occupationPeriod == null) {
            return 0;
        }
        List<TeacherLegalRegimen> legalRegimens = getAllLegalRegimensWithoutEndSituations(occupationPeriod
                .getStartYearMonthDay(), occupationPeriod.getEndYearMonthDay());

        if (!legalRegimens.isEmpty()) {
            List<TeacherServiceExemption> exemptions = getServiceExemptionsWithoutMedicalSituations(
                    occupationPeriod.getStartYearMonthDay(), occupationPeriod.getEndYearMonthDay());
            TeacherServiceExemption teacherServiceExemption = chooseOneServiceExemption(exemptions,
                    occupationPeriod);
            if (teacherServiceExemption != null
                    && teacherServiceExemption.isServiceExemptionToCountZeroInCredits()) {
                return 0;
            }
            Collections.sort(legalRegimens, new BeanComparator("beginDate"));
            final Integer hours = legalRegimens.get(legalRegimens.size() - 1).getLessonHours();
            return (hours == null) ? 0 : hours.intValue();
        }
        return 0;
    }

    public List<PersonFunction> getManagementFunctions(ExecutionPeriod executionPeriod) {
        List<PersonFunction> personFunctions = new ArrayList<PersonFunction>();
        for (PersonFunction personFunction : this.getPerson().getPersonFunctions()) {
            if (personFunction.belongsToPeriod(executionPeriod.getBeginDateYearMonthDay(), 
                    executionPeriod.getEndDateYearMonthDay())) {
                personFunctions.add(personFunction);
            }
        }
        return personFunctions;
    }      

    public static Teacher readTeacherByUsername(final String userName) {
        final Person person = Person.readPersonByUsername(userName);
        return (person.getTeacher() != null) ? person.getTeacher() : null;
    }

    public static Teacher readByNumber(final Integer teacherNumber) {
        for (final Teacher teacher : RootDomainObject.getInstance().getTeachers()) {
            if (teacher.getTeacherNumber().equals(teacherNumber)) {
                return teacher;
            }
        }
        return null;
    }

    public static List<Teacher> readByNumbers(Collection<Integer> teacherNumbers) {
        List<Teacher> selectedTeachers = new ArrayList<Teacher>();
        for (final Teacher teacher : RootDomainObject.getInstance().getTeachers()) {
            if (teacherNumbers.contains(teacher.getTeacherNumber())) {
                selectedTeachers.add(teacher);
            }
            // This isn't necessary, its just a fast optimization.
            if (teacherNumbers.size() == selectedTeachers.size()) {
                break;
            }
        }
        return selectedTeachers;
    }

    public List<Professorship> getProfessorships(ExecutionPeriod executionPeriod) {
        List<Professorship> professorships = new ArrayList<Professorship>();
        for (Professorship professorship : this.getProfessorships()) {
            if (professorship.getExecutionCourse().getExecutionPeriod().equals(executionPeriod)) {
                professorships.add(professorship);
            }
        }
        return professorships;
    }

    public List<Professorship> getProfessorships(ExecutionYear executionYear) {
        List<Professorship> professorships = new ArrayList<Professorship>();
        for (Professorship professorship : this.getProfessorships()) {
            if (professorship.getExecutionCourse().getExecutionPeriod().getExecutionYear().equals(
                    executionYear)) {
                professorships.add(professorship);
            }
        }
        return professorships;
    }

    public Set<TeacherDegreeFinalProjectStudent> findTeacherDegreeFinalProjectStudentsByExecutionPeriod(
            final ExecutionPeriod executionPeriod) {
        final Set<TeacherDegreeFinalProjectStudent> teacherDegreeFinalProjectStudents = new HashSet<TeacherDegreeFinalProjectStudent>();
        for (final TeacherDegreeFinalProjectStudent teacherDegreeFinalProjectStudent : getDegreeFinalProjectStudents()) {
            if (executionPeriod == teacherDegreeFinalProjectStudent.getExecutionPeriod()) {
                teacherDegreeFinalProjectStudents.add(teacherDegreeFinalProjectStudent);
            }
        }
        return teacherDegreeFinalProjectStudents;
    }

    public List<ManagementPositionCreditLine> getManagementPositionsFor(ExecutionPeriod executionPeriod) {
        final List<ManagementPositionCreditLine> result = new ArrayList<ManagementPositionCreditLine>();
        for (final ManagementPositionCreditLine managementPositionCreditLine : this
                .getManagementPositions()) {
            if (managementPositionCreditLine.getStart().before(executionPeriod.getEndDate())
                    && managementPositionCreditLine.getEnd().after(executionPeriod.getBeginDate())) {
                result.add(managementPositionCreditLine);
            }
        }
        return result;
    }

    public List<PublicationTeacher> readPublicationsByPublicationArea(PublicationArea publicationArea) {
        final List<PublicationTeacher> result = new ArrayList<PublicationTeacher>();
        for (final PublicationTeacher publicationTeacher : this.getTeacherPublicationsSet()) {
            if (publicationTeacher.getPublicationArea().equals(publicationArea)) {
                result.add(publicationTeacher);
            }
        }
        return result;
    }

    public List<OldPublication> readOldPublicationsByType(OldPublicationType oldPublicationType) {
        final List<OldPublication> result = new ArrayList<OldPublication>();
        for (final OldPublication oldPublication : this.getAssociatedOldPublicationsSet()) {
            if (oldPublication.getOldPublicationType().equals(oldPublicationType)) {
                result.add(oldPublication);
            }
        }
        return result;
    }

    public Orientation readOrientationByType(OrientationType orientationType) {
        for (final Orientation orientation : this.getAssociatedOrientationsSet()) {
            if (orientation.getOrientationType().equals(orientationType)) {
                return orientation;
            }
        }
        return null;
    }

    public PublicationsNumber readPublicationsNumberByType(PublicationType publicationType) {
        for (final PublicationsNumber publicationsNumber : this.getAssociatedPublicationsNumbersSet()) {
            if (publicationsNumber.getPublicationType().equals(publicationType)) {
                return publicationsNumber;
            }
        }
        return null;
    }

    public SortedSet<ExecutionCourse> getCurrentExecutionCourses() {
        final SortedSet<ExecutionCourse> executionCourses = new TreeSet<ExecutionCourse>(
                ExecutionCourse.EXECUTION_COURSE_COMPARATOR_BY_EXECUTION_PERIOD_AND_NAME);
        for (final Professorship professorship : getProfessorshipsSet()) {
            final ExecutionCourse executionCourse = professorship.getExecutionCourse();
            final ExecutionPeriod executionPeriod = executionCourse.getExecutionPeriod();
            final ExecutionPeriod nextExecutionPeriod = executionPeriod.getNextExecutionPeriod();
            if (executionPeriod.getState().equals(PeriodState.CURRENT)
                    || (nextExecutionPeriod != null && nextExecutionPeriod.getState().equals(
                            PeriodState.CURRENT))) {
                executionCourses.add(executionCourse);
            }
        }
        return executionCourses;
    }

    public Set<Proposal> findFinalDegreeWorkProposals() {
        final Set<Proposal> proposals = new HashSet<Proposal>();
        proposals.addAll(getAssociatedProposalsByCoorientatorSet());
        proposals.addAll(getAssociatedProposalsByOrientatorSet());
        return proposals;
    }

    public boolean isResponsibleFor(CurricularCourse curricularCourse, ExecutionPeriod executionPeriod) {
        for (final ExecutionCourse executionCourse : curricularCourse.getAssociatedExecutionCoursesSet()) {
            if (executionCourse.getExecutionPeriod() == executionPeriod) {
                return responsibleFor(executionCourse) != null;
            }
        }
        return false;
    }

    public boolean isCoordinatorFor(DegreeCurricularPlan degreeCurricularPlan,
            ExecutionYear executionYear) {
        for (final ExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegreesSet()) {
            if (executionDegree.getExecutionYear() == executionYear) {
                return executionDegree.getCoordinatorByTeacher(this) != null;
            }
        }
        return false;
    }

    public boolean isResponsibleOrCoordinatorFor(CurricularCourse curricularCourse,
            ExecutionPeriod executionPeriod) {
        return isResponsibleFor(curricularCourse, executionPeriod)
                || isCoordinatorFor(curricularCourse.getDegreeCurricularPlan(), executionPeriod
                        .getExecutionYear());
    }
}