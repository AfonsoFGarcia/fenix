package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship.ResponsibleForValidator;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship.ResponsibleForValidator.InvalidCategory;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship.ResponsibleForValidator.MaxResponsibleForExceed;
import net.sourceforge.fenixedu.dataTransferObject.credits.InfoCredits;
import net.sourceforge.fenixedu.domain.credits.util.InfoCreditsBuilder;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Group;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupStudent;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.publication.Publication;
import net.sourceforge.fenixedu.domain.publication.PublicationTeacher;
import net.sourceforge.fenixedu.domain.teacher.Advise;
import net.sourceforge.fenixedu.domain.teacher.Category;
import net.sourceforge.fenixedu.domain.teacher.DegreeTeachingService;
import net.sourceforge.fenixedu.domain.teacher.ServiceExemptionType;
import net.sourceforge.fenixedu.domain.teacher.TeacherLegalRegimen;
import net.sourceforge.fenixedu.domain.teacher.TeacherPersonalExpectation;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.domain.teacher.TeacherServiceExemption;
import net.sourceforge.fenixedu.util.CalendarUtil;
import net.sourceforge.fenixedu.util.PublicationArea;
import net.sourceforge.fenixedu.util.State;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

public class Teacher extends Teacher_Base {

	/***************************************************************************
	 * BUSINESS SERVICES *
	 **************************************************************************/

	public void addToTeacherInformationSheet(Publication publication,
			PublicationArea publicationArea) {
		new PublicationTeacher(publication, this, publicationArea);
	}

	public void removeFromTeacherInformationSheet(Publication publication) {
		Iterator<PublicationTeacher> iterator = getTeacherPublications()
				.iterator();

		while (iterator.hasNext()) {
			PublicationTeacher publicationTeacher = iterator.next();
			if (publicationTeacher.getPublication().equals(publication)) {
				iterator.remove();
				publicationTeacher.delete();
				return;
			}
		}
	}

	public Boolean canAddPublicationToTeacherInformationSheet(
			PublicationArea area) {
		// NOTA : a linha seguinte cont�m um n�mero expl�cito quando n�o deve.
		// Isto deve ser mudado! Mas esta mudan�a implica tornar expl�cito o
		// conceito de Ficha de docente.
		return new Boolean(countPublicationsInArea(area) < 5);

	}

	public List responsibleFors() {
		List<Professorship> professorships = this.getProfessorships();
		List<Professorship> res = new ArrayList<Professorship>();

		for (Professorship professorship : professorships) {
			if (professorship.getResponsibleFor())
				res.add(professorship);
		}
		return res;
	}

	public Professorship responsibleFor(Integer executionCourseId) {
		List<Professorship> professorships = this.getProfessorships();

		for (Professorship professorship : professorships) {
			if (professorship.getResponsibleFor()
					&& professorship.getExecutionCourse().getIdInternal()
							.equals(executionCourseId))
				return professorship;
		}
		return null;
	}

	public void updateResponsabilitiesFor(Integer executionYearId,
			List<Integer> executionCourses) throws MaxResponsibleForExceed,
			InvalidCategory {

		if (executionYearId == null || executionCourses == null)
			throw new NullPointerException();

		for (final Professorship professorship : this.getProfessorships()) {
			final ExecutionCourse executionCourse = professorship
					.getExecutionCourse();
			ResponsibleForValidator.getInstance().validateResponsibleForList(
					this, executionCourse, professorship);
			if (executionCourse.getExecutionPeriod().getExecutionYear()
					.getIdInternal().equals(executionYearId)) {
				professorship.setResponsibleFor(executionCourses
						.contains(executionCourse.getIdInternal()));
			}
		}
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

	public Category getCategory() {
		TeacherLegalRegimen regimen = getLastLegalRegimen();
		if (regimen != null) {
			return regimen.getCategory();
		}
		return null;
	}

	public TeacherLegalRegimen getLastLegalRegimen() {
		Date date = null;
		TeacherLegalRegimen regimenToReturn = null;
		for (TeacherLegalRegimen regimen : this.getLegalRegimens()) {
			if (regimen.isActive(Calendar.getInstance().getTime())) {
				return regimen;
			} else if (date == null || date.before(regimen.getEndDate())) {
				date = regimen.getEndDate();
				regimenToReturn = regimen;
			}
		}
		return regimenToReturn;
	}

	public List<TeacherLegalRegimen> getAllLegalRegimensBelongsToPeriod(
			Date beginDate, Date endDate) {
		List<TeacherLegalRegimen> legalRegimens = new ArrayList<TeacherLegalRegimen>();
		for (TeacherLegalRegimen legalRegimen : this.getLegalRegimens()) {
			if (legalRegimen.belongsToPeriod(beginDate, endDate)) {
				legalRegimens.add(legalRegimen);
			}
		}
		return legalRegimens;
	}

	public TeacherPersonalExpectation getTeacherPersonalExpectationByExecutionYear(
			ExecutionYear executionYear) {
		TeacherPersonalExpectation result = null;

		List<TeacherPersonalExpectation> teacherPersonalExpectations = this
				.getTeacherPersonalExpectations();

		for (TeacherPersonalExpectation teacherPersonalExpectation : teacherPersonalExpectations) {
			if (teacherPersonalExpectation.getExecutionYear().equals(
					executionYear)) {
				result = teacherPersonalExpectation;
				break;
			}
		}

		return result;
	}

	public List<Proposal> getFinalDegreeWorksByExecutionYear(
			ExecutionYear executionYear) {
		List<Proposal> proposalList = new ArrayList<Proposal>();
		for (Iterator iter = getAssociatedProposalsByOrientator().iterator(); iter
				.hasNext();) {
			Proposal proposal = (Proposal) iter.next();
			if (proposal.getExecutionDegree().getExecutionYear().equals(
					executionYear)) {
				// if it was attributed by the coordinator the proposal is
				// efective
				if (proposal.getGroupAttributed() != null) {
					proposalList.add(proposal);
				}
				// if not, we have to verify if the teacher has proposed it to
				// any student(s) and if that(those) student(s) has(have)
				// accepted it
				else {
					Group attributedGroupByTeacher = proposal
							.getGroupAttributedByTeacher();
					if (attributedGroupByTeacher != null) {
						boolean toAdd = false;
						for (Iterator iterator = attributedGroupByTeacher
								.getGroupStudents().iterator(); iterator
								.hasNext();) {
							GroupStudent groupStudent = (GroupStudent) iterator
									.next();
							Proposal studentProposal = groupStudent
									.getFinalDegreeWorkProposalConfirmation();
							if (studentProposal != null
									&& studentProposal.equals(proposal)) {
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

	public List<ExecutionCourse> getLecturedExecutionCoursesByExecutionYear(
			ExecutionYear executionYear) {
		List<ExecutionCourse> executionCourses = new ArrayList();
		for (Iterator iter = executionYear.getExecutionPeriods().iterator(); iter
				.hasNext();) {
			ExecutionPeriod executionPeriod = (ExecutionPeriod) iter.next();
			executionCourses
					.addAll(getLecturedExecutionCoursesByExecutionPeriod(executionPeriod));
		}
		return executionCourses;
	}

	public List<ExecutionCourse> getLecturedExecutionCoursesByExecutionPeriod(
			final ExecutionPeriod executionPeriod) {
		List<ExecutionCourse> executionCourses = new ArrayList<ExecutionCourse>();
		for (Iterator iter = getProfessorships().iterator(); iter.hasNext();) {
			Professorship professorship = (Professorship) iter.next();
			ExecutionCourse executionCourse = professorship
					.getExecutionCourse();

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

	public Double getHoursLecturedOnExecutionCourse(
			ExecutionCourse executionCourse) {
		double returnValue = 0;

		Professorship professorship = getProfessorshipByExecutionCourse(executionCourse);
		TeacherService teacherService = getTeacherServiceByExecutionPeriod(executionCourse
				.getExecutionPeriod());
		if (teacherService != null) {
			for (ShiftProfessorship shiftProfessorShiftEntry : professorship
					.getAssociatedShiftProfessorship()) {
				Shift shift = shiftProfessorShiftEntry.getShift();
				DegreeTeachingService degreeTeachingService = teacherService
						.getDegreeTeachingServiceByShiftAndProfessorship(shift,
								professorship);
				if (degreeTeachingService != null) {
					returnValue += ((degreeTeachingService.getPercentage() / 100)
							* shift.hours());
				}
			}
		}
		return returnValue;
	}

	public TeacherService getTeacherServiceByExecutionPeriod(
			final ExecutionPeriod executionPeriod) {
		return (TeacherService) CollectionUtils.find(getTeacherServices(),
				new Predicate() {

					public boolean evaluate(Object arg0) {
						TeacherService teacherService = (TeacherService) arg0;
						return teacherService.getExecutionPeriod() == executionPeriod;
					}
				});
	}

	public Professorship getProfessorshipByExecutionCourse(
			final ExecutionCourse executionCourse) {
		return (Professorship) CollectionUtils.find(getProfessorships(),
				new Predicate() {
					public boolean evaluate(Object arg0) {
						Professorship professorship = (Professorship) arg0;
						return professorship.getExecutionCourse() == executionCourse;
					}
				});
	}

	public List<Professorship> getDegreeProfessorshipsByExecutionPeriod(
			final ExecutionPeriod executionPeriod) {
		return (List<Professorship>) CollectionUtils.select(
				getProfessorships(), new Predicate() {
					public boolean evaluate(Object arg0) {
						Professorship professorship = (Professorship) arg0;
						return professorship.getExecutionCourse()
								.getExecutionPeriod() == executionPeriod
								&& !professorship.getExecutionCourse()
										.isMasterDegreeOnly();
					}
				});
	}

	/***************************************************************************
	 * OTHER METHODS *
	 **************************************************************************/

	public String toString() {
		String result = "[Dominio.Teacher ";
		result += ", teacherNumber=" + getTeacherNumber();
		result += ", person=" + getPerson();
		result += ", category= " + getCategory();
		result += "]";
		return result;
	}

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
						.getMasterDegreeThesis().getStudentCurricularPlan()
						.getDegreeCurricularPlan().getExecutionDegrees();

				for (ExecutionDegree executionDegree : executionDegrees) {
					if (executionDegree.getExecutionYear()
							.equals(executionYear)) {
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
			if (masterDegreeThesisDataVersion.getCurrentState().getState()
					.equals(State.ACTIVE)) {
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
				.getCurrentWorkingDepartment()
				.readTeacherExpectationDefinitionPeriodByExecutionYear(
						executionYear);

		if (teacherExpectationDefinitionPeriod.isPeriodOpen() == false) {
			throw new DomainException(
					"error.exception.personalExpectation.definitionPeriodForExecutionYearAlreadyExpired");
		}

	}

	public List<TeacherServiceExemption> getServiceExemptionSituations(
			Date beginDate, Date endDate) {

		List<TeacherServiceExemption> serviceExemptions = new ArrayList<TeacherServiceExemption>();
		for (TeacherServiceExemption serviceExemption : this
				.getServiceExemptionSituations()) {
			if (!isMedicalSituation(serviceExemption)
					&& serviceExemption.belongsToPeriod(beginDate, endDate)) {
				serviceExemptions.add(serviceExemption);
			}
		}
		return serviceExemptions;
	}

	/**
	 * @param serviceExemption
	 * @return
	 */
	private boolean isMedicalSituation(TeacherServiceExemption serviceExemption) {
		if (serviceExemption.getType().equals(
				ServiceExemptionType.MEDICAL_SITUATION)
				|| serviceExemption
						.getType()
						.equals(
								ServiceExemptionType.MATERNAL_LICENSE_WITH_SALARY_80PERCENT)
				|| serviceExemption.getType().equals(
						ServiceExemptionType.MATERNAL_LICENSE)
				|| serviceExemption.getType().equals(
						ServiceExemptionType.DANGER_MATERNAL_LICENSE)
				|| serviceExemption.getType().equals(
						ServiceExemptionType.CHILDBIRTH_LICENSE)) {
			return true;
		}
		return false;
	}

	public List<PersonFunction> getPersonFuntions(Date beginDate, Date endDate) {
		return getPerson().getPersonFuntions(beginDate, endDate);
	}

	public int getHoursByCategory(Date begin, Date end) {

		List<TeacherLegalRegimen> list = getAllLegalRegimensBelongsToPeriod(
				begin, end);

		if (list.isEmpty()) {
			return 0;
		} else {
			Collections.sort(list, new BeanComparator("beginDate"));
			return (list.get(list.size() - 1).getLessonHours() == null) ? 0
					: list.get(list.size() - 1).getLessonHours();
		}
	}

	public int getHoursByCategory(ExecutionPeriod executionPeriod) {

		OccupationPeriod occupationPeriod = getLessonsPeriod(executionPeriod);
		if (occupationPeriod == null) {
			return 0;
		}
		List<TeacherLegalRegimen> list = getAllLegalRegimensBelongsToPeriod(
				occupationPeriod.getStart(), occupationPeriod.getEnd());

		if (list.isEmpty()) {
			return 0;
		} else {
			Collections.sort(list, new BeanComparator("beginDate"));
			final Integer hours = list.get(list.size() - 1).getLessonHours();
			return (hours == null) ? 0 : hours.intValue();
		}
	}

	public int getServiceExemptionCredits(ExecutionPeriod executionPeriod) {

		OccupationPeriod occupationPeriod = getLessonsPeriod(executionPeriod);
		if (occupationPeriod == null) {
			return 0;
		}
		List<TeacherServiceExemption> list = getServiceExemptionSituations(
				occupationPeriod.getStart(), occupationPeriod.getEnd());

		if (list.isEmpty()) {
			return 0;
		} else {
			return calculateServiceExemptionsCredits(list, executionPeriod);
		}
	}

	public double getManagementFunctionsCredits(ExecutionPeriod executionPeriod) {

		Date begin = executionPeriod.getBeginDate();
		Date end = executionPeriod.getEndDate();

		List<PersonFunction> list = new ArrayList<PersonFunction>();
		for (PersonFunction personFunction : this.getPerson()
				.getPersonFunctions()) {
			if (personFunction.belongsToPeriod(begin, end)) {
				list.add(personFunction);
			}
		}

		double totalCredits = 0.0;
		if (list.size() > 1) {
			for (PersonFunction function : list) {
				totalCredits = (function.getCredits() != null) ? totalCredits
						+ function.getCredits() : totalCredits;
			}
			return totalCredits;
		} else if (list.size() == 1) {
			Double credits = list.iterator().next().getCredits();
			if (credits != null) {
				return credits;
			}
		}
		return 0;
	}

	public Category getCategoryForCreditsByPeriod(
			ExecutionPeriod executionPeriod) {

		OccupationPeriod occupationPeriod = getLessonsPeriod(executionPeriod);
		if (occupationPeriod == null) {
			return null;
		}
		List<TeacherLegalRegimen> list = getAllLegalRegimensBelongsToPeriod(
				occupationPeriod.getStart(), occupationPeriod.getEnd());

		if (list.isEmpty()) {
			return null;
		} else {
			Collections.sort(list, new BeanComparator("beginDate"));
			return list.get(list.size() - 1).getCategory();
		}
	}

	/**
	 * @param executionPeriod
	 * @return
	 */
	private OccupationPeriod getLessonsPeriod(ExecutionPeriod executionPeriod) {
		for (ExecutionDegree executionDegree : executionPeriod
				.getExecutionYear()
				.getExecutionDegreesByType(DegreeType.DEGREE)) {
			if (executionPeriod.getSemester() == 1) {
				return executionDegree.getPeriodLessonsFirstSemester();
			} else {
				return executionDegree.getPeriodLessonsSecondSemester();
			}
		}
		return null;
	}

	private int calculateServiceExemptionsCredits(
			List<TeacherServiceExemption> list, ExecutionPeriod executionPeriod) {

		OccupationPeriod occupationPeriod = getLessonsPeriod(executionPeriod);
		if (occupationPeriod == null) {
			return 0;
		}
		TeacherServiceExemption teacherServiceExemption = getTeacherServiceExemption(
				list, occupationPeriod);

		if (teacherServiceExemption != null
				&& (teacherServiceExemption.getType().equals(
						ServiceExemptionType.SABBATICAL)
						|| teacherServiceExemption
								.getType()
								.equals(
										ServiceExemptionType.GRANT_OWNER_EQUIVALENCE_WITHOUT_SALARY)
						|| teacherServiceExemption
								.getType()
								.equals(
										ServiceExemptionType.GRANT_OWNER_EQUIVALENCE_WITH_SALARY)
						|| teacherServiceExemption
								.getType()
								.equals(
										ServiceExemptionType.GRANT_OWNER_EQUIVALENCE_WITH_SALARY_SABBATICAL) || teacherServiceExemption
						.getType()
						.equals(
								ServiceExemptionType.GRANT_OWNER_EQUIVALENCE_WITH_SALARY_WITH_DEBITS))) {

			Integer daysBetween = CalendarUtil.getNumberOfDaysBetweenDates(
					teacherServiceExemption.getStart(), teacherServiceExemption
							.getEnd());
			if (occupationPeriod
					.containsDay(teacherServiceExemption.getStart())) {
				if (teacherServiceExemption.getType().equals(
						ServiceExemptionType.SABBATICAL)) {
					return 6;
				} else if (daysBetween > 90) { // to be considered long term
					// grant owner
					return getHoursByCategory(executionPeriod);
				}
			} else {
				occupationPeriod = getLessonsPeriod(executionPeriod
						.getPreviousExecutionPeriod());
				if (occupationPeriod != null
						&& occupationPeriod.containsDay(teacherServiceExemption
								.getStart()) && daysBetween > 185 /*
																	 * more than
																	 * 6 months
																	 */) {
					if (teacherServiceExemption.getType().equals(
							ServiceExemptionType.SABBATICAL)) {
						return 6;
					} else {
						return getHoursByCategory(executionPeriod);
					}
				} else if (executionPeriod.containsDay(teacherServiceExemption
						.getStart())) {
					if (teacherServiceExemption.getType().equals(
							ServiceExemptionType.SABBATICAL)) {
						return 6;
					} else {
						return getHoursByCategory(executionPeriod);
					}
				}
			}
		}
		return 0;
	}

	private TeacherServiceExemption getTeacherServiceExemption(
			List<TeacherServiceExemption> list,
			OccupationPeriod occupationPeriod) {
		Integer numberOfDaysInPeriod = null, maxDays = 0;
		TeacherServiceExemption teacherServiceExemption = null;

		Date begin = occupationPeriod.getStart();
		Date end = occupationPeriod.getEnd();

		for (TeacherServiceExemption serviceExemption : list) {

			if (serviceExemption.getStart().before(begin)
					|| serviceExemption.getStart().equals(begin)) {
				Date endDate = (serviceExemption.getEnd() == null) ? end
						: serviceExemption.getEnd();
				numberOfDaysInPeriod = CalendarUtil
						.getNumberOfDaysBetweenDates(begin, endDate);
				if (numberOfDaysInPeriod >= maxDays) {
					teacherServiceExemption = serviceExemption;
				}
			}
			if (serviceExemption.getStart().after(begin)
					&& serviceExemption.getEnd() != null
					&& serviceExemption.getEnd().before(end)) {
				numberOfDaysInPeriod = CalendarUtil
						.getNumberOfDaysBetweenDates(serviceExemption
								.getStart(), serviceExemption.getEnd());
				if (numberOfDaysInPeriod >= maxDays) {
					teacherServiceExemption = serviceExemption;
				}
			} else if (serviceExemption.getStart().after(begin)) {
				numberOfDaysInPeriod = CalendarUtil
						.getNumberOfDaysBetweenDates(serviceExemption
								.getStart(), end);
				if (numberOfDaysInPeriod >= maxDays) {
					teacherServiceExemption = serviceExemption;
				}
			}
		}
		return teacherServiceExemption;
	}

	public List<net.sourceforge.fenixedu.domain.teacher.Advise> getAdvisesByAdviseTypeAndExecutionYear(
			net.sourceforge.fenixedu.domain.teacher.AdviseType adviseType,
			ExecutionYear executionYear) {

		List<Advise> result = new ArrayList<Advise>();
		Date executionYearStartDate = executionYear.getBeginDate();
		Date executionYearEndDate = executionYear.getEndDate();

		for (Advise advise : this.getAdvises()) {
			if ((advise.getAdviseType() == adviseType)) {
				Date adviseStartDate = advise.getStartExecutionPeriod()
						.getBeginDate();
				Date adviseEndDate = advise.getEndExecutionPeriod()
						.getEndDate();

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

	public List<net.sourceforge.fenixedu.domain.teacher.Advise> getAdvisesByAdviseType(
			net.sourceforge.fenixedu.domain.teacher.AdviseType adviseType) {
		List<Advise> result = new ArrayList<Advise>();

		for (Advise advise : this.getAdvises()) {
			if (advise.getAdviseType() == adviseType) {
				result.add(advise);
			}
		}

		return result;
	}

	public double getCreditsBetweenExecutionPeriods(
			ExecutionPeriod startExecutionPeriod,
			ExecutionPeriod endExecutionPeriod) {

		ExecutionPeriod startPeriod = startExecutionPeriod;

		ExecutionPeriod executionPeriodAfterEnd = endExecutionPeriod
				.getNextExecutionPeriod();

		double totalCredits = 0.0;

		while (startPeriod != executionPeriodAfterEnd) {
			TeacherService teacherService = getTeacherServiceByExecutionPeriod(startPeriod);

			if (teacherService != null) {
				totalCredits += getManagementFunctionsCredits(startPeriod);
				totalCredits += getServiceExemptionCredits(startPeriod);
				totalCredits += teacherService.getCredits();
				totalCredits += teacherService.getPastServiceCredits();
				if (teacherService.getPastService() == null) {
					totalCredits -= getMandatoryLessonHours(startPeriod);
				}
			}
			startPeriod = startPeriod.getNextExecutionPeriod();
		}
		return totalCredits;
	}

	public int getMandatoryLessonHours(ExecutionPeriod executionPeriod) {
		OccupationPeriod occupationPeriod = getLessonsPeriod(executionPeriod);
		if (occupationPeriod == null) {
			return 0;
		}
		List<TeacherLegalRegimen> list = getAllLegalRegimensBelongsToPeriod(
				occupationPeriod.getStart(), occupationPeriod.getEnd());

		if (list.isEmpty()) {
			return 0;
		} else {
			List<TeacherServiceExemption> exemptions = getServiceExemptionSituations(
					occupationPeriod.getStart(), occupationPeriod.getEnd());
			TeacherServiceExemption teacherServiceExemption = getTeacherServiceExemption(
					exemptions, occupationPeriod);
			if (isServiceExemptionToCountZero(teacherServiceExemption)) {
				return 0;
			}
			Collections.sort(list, new BeanComparator("beginDate"));
			final Integer hours = list.get(list.size() - 1).getLessonHours();
			return (hours == null) ? 0 : hours.intValue();
		}
	}

	/**
	 * @param teacherServiceExemption
	 * @return
	 */
	private boolean isServiceExemptionToCountZero(
			TeacherServiceExemption teacherServiceExemption) {
		if (teacherServiceExemption == null) {
			return false;
		}
		if (teacherServiceExemption.getType().equals(
				ServiceExemptionType.CONTRACT_SUSPEND)
				|| teacherServiceExemption.getType().equals(
						ServiceExemptionType.GOVERNMENT_MEMBER)
				|| teacherServiceExemption
						.getType()
						.equals(
								ServiceExemptionType.LICENSE_WITHOUT_SALARY_FOR_ACCOMPANIMENT)
				|| teacherServiceExemption
						.getType()
						.equals(
								ServiceExemptionType.LICENSE_WITHOUT_SALARY_FOR_INTERNATIONAL_EXERCISE)
				|| teacherServiceExemption.getType().equals(
						ServiceExemptionType.LICENSE_WITHOUT_SALARY_LONG)
				|| teacherServiceExemption
						.getType()
						.equals(
								ServiceExemptionType.LICENSE_WITHOUT_SALARY_UNTIL_NINETY_DAYS)
				|| teacherServiceExemption.getType().equals(
						ServiceExemptionType.LICENSE_WITHOUT_SALARY_YEAR)
				|| teacherServiceExemption.getType().equals(
						ServiceExemptionType.MILITAR_SITUATION)
				|| teacherServiceExemption.getType().equals(
						ServiceExemptionType.REQUESTED_FOR)
				|| teacherServiceExemption.getType().equals(
						ServiceExemptionType.SERVICE_COMMISSION)
				|| teacherServiceExemption.getType().equals(
						ServiceExemptionType.SERVICE_COMMISSION_IST_OUT)
				|| teacherServiceExemption.getType().equals(
						ServiceExemptionType.SPECIAL_LICENSE)) {
			return true;
		}
		return false;
	}

	public List<PersonFunction> getManagementFunctions(
			ExecutionPeriod executionPeriod) {
		Date begin = executionPeriod.getBeginDate();
		Date end = executionPeriod.getEndDate();

		List<PersonFunction> list = new ArrayList<PersonFunction>();
		for (PersonFunction personFunction : this.getPerson()
				.getPersonFunctions()) {
			if (personFunction.belongsToPeriod(begin, end)) {
				list.add(personFunction);
			}
		}

		return list;
	}

}
