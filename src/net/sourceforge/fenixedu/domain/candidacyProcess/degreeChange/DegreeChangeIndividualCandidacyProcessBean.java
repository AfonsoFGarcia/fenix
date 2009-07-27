package net.sourceforge.fenixedu.domain.candidacyProcess.degreeChange;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyPrecedentDegreeInformationBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcessDocumentUploadBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyDocumentFileType;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcessWithPrecedentDegreeInformationBean;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;

import org.joda.time.LocalDate;

public class DegreeChangeIndividualCandidacyProcessBean extends IndividualCandidacyProcessWithPrecedentDegreeInformationBean {

    private DomainReference<Degree> selectDegree;

    public DegreeChangeIndividualCandidacyProcessBean() {
	setCandidacyDate(new LocalDate());
	initializeDocumentUploadBeans();
	setObservations("");
    }

    public DegreeChangeIndividualCandidacyProcessBean(final DegreeChangeIndividualCandidacyProcess process) {
	setIndividualCandidacyProcess(process);
	setCandidacyProcess(process.getCandidacyProcess());
	setCandidacyDate(process.getCandidacyDate());
	setSelectedDegree(process.getCandidacySelectedDegree());
	setPrecedentDegreeType(PrecedentDegreeType.valueOf(process.getCandidacyPrecedentDegreeInformation()));
	final CandidacyPrecedentDegreeInformationBean precedentDegreeInformation = new CandidacyPrecedentDegreeInformationBean(
		process.getCandidacyPrecedentDegreeInformation());
	precedentDegreeInformation.initCurricularCoursesInformation(process.getCandidacyPrecedentDegreeInformation());
	setPrecedentDegreeInformation(precedentDegreeInformation);
	initializeFormation(process.getCandidacy().getFormations());
	setObservations(process.getCandidacy().getObservations());
    }

    @Override
    protected List<CycleType> getValidPrecedentCycleTypes() {
	return Collections.singletonList(CycleType.FIRST_CYCLE);
    }

    @Override
    protected boolean isPreBolonhaPrecedentDegreeAllowed() {
	return true;
    }

    @Override
    public ExecutionYear getCandidacyExecutionInterval() {
	return (ExecutionYear) super.getCandidacyExecutionInterval();
    }

    @Override
    public List<StudentCurricularPlan> getPrecedentStudentCurricularPlans() {
	final Student student = getStudent();
	if (student == null) {
	    return Collections.emptyList();
	}

	final List<StudentCurricularPlan> studentCurricularPlans = new ArrayList<StudentCurricularPlan>();
	for (final Registration registration : student.getRegistrations()) {

	    if (registration.isBolonha()) {
		final StudentCurricularPlan studentCurricularPlan = registration.getLastStudentCurricularPlan();

		for (final CycleType cycleType : getValidPrecedentCycleTypes()) {
		    if (studentCurricularPlan.hasCycleCurriculumGroup(cycleType)) {
			final CycleCurriculumGroup cycle = studentCurricularPlan.getCycle(cycleType);

			if (!cycle.isConclusionProcessed() && !cycle.isConcluded()) {
			    studentCurricularPlans.add(registration.getLastStudentCurricularPlan());
			    break;
			}
		    }
		}

	    } else if (isPreBolonhaPrecedentDegreeAllowed()) {
		if (!registration.isConcluded() && !registration.isRegistrationConclusionProcessed()) {
		    studentCurricularPlans.add(registration.getLastStudentCurricularPlan());
		}
	    }
	}

	return studentCurricularPlans;
    }

    @Override
    public DegreeChangeCandidacyProcess getCandidacyProcess() {
	return (DegreeChangeCandidacyProcess) super.getCandidacyProcess();
    }

    public Degree getSelectedDegree() {
	return (this.selectDegree != null) ? this.selectDegree.getObject() : null;
    }

    public void setSelectedDegree(final Degree selectDegree) {
	this.selectDegree = (selectDegree != null) ? new DomainReference<Degree>(selectDegree) : null;
    }

    @Override
    protected void initializeDocumentUploadBeans() {
	setPhotoDocument(new CandidacyProcessDocumentUploadBean(IndividualCandidacyDocumentFileType.PHOTO));
    }

}
