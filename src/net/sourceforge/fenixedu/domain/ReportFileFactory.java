package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.reports.CourseLoadAndResponsiblesReportFile;
import net.sourceforge.fenixedu.domain.reports.CourseLoadReportFile;
import net.sourceforge.fenixedu.domain.reports.DissertationsProposalsReportFile;
import net.sourceforge.fenixedu.domain.reports.DissertationsWithExternalAffiliationsReportFile;
import net.sourceforge.fenixedu.domain.reports.EctsLabelCurricularCourseReportFile;
import net.sourceforge.fenixedu.domain.reports.EctsLabelDegreeReportFile;
import net.sourceforge.fenixedu.domain.reports.EtiReportFile;
import net.sourceforge.fenixedu.domain.reports.EurAceReportFile;
import net.sourceforge.fenixedu.domain.reports.FlunkedReportFile;
import net.sourceforge.fenixedu.domain.reports.GepReportFile;
import net.sourceforge.fenixedu.domain.reports.GraduationReportFile;
import net.sourceforge.fenixedu.domain.reports.RaidesDfaReportFile;
import net.sourceforge.fenixedu.domain.reports.RaidesGraduationReportFile;
import net.sourceforge.fenixedu.domain.reports.RaidesPhdReportFile;
import net.sourceforge.fenixedu.domain.reports.RegistrationReportFile;
import net.sourceforge.fenixedu.domain.reports.StatusAndApprovalReportFile;
import net.sourceforge.fenixedu.domain.reports.TeachersByShiftReportFile;
import net.sourceforge.fenixedu.domain.reports.TeachersListFromGiafReportFile;
import net.sourceforge.fenixedu.domain.reports.TeachersListReportFile;
import net.sourceforge.fenixedu.domain.reports.TutorshipProgramReportFile;
import pt.ist.fenixWebFramework.services.Service;

public class ReportFileFactory {

    @Service
    public static PublicRelationsStudentListQueueJob createPublicRelationsStudentListQueueJob(ExecutionYear executionYear,
	    DegreeType degreeType, Boolean concluded, Boolean active) {
	return new PublicRelationsStudentListQueueJob(executionYear, degreeType, concluded, active);
    }

    @Service
    static public GepReportFile createStatusAndApprovalReportFile(String type, DegreeType degreeType, ExecutionYear executionYear) {
	final StatusAndApprovalReportFile statusAndApprovalReportFile = new StatusAndApprovalReportFile();
	statusAndApprovalReportFile.setType(type);
	statusAndApprovalReportFile.setDegreeType(degreeType);
	statusAndApprovalReportFile.setExecutionYear(executionYear);
	return statusAndApprovalReportFile;
    }

    @Service
    public static GepReportFile createDissertationsProposalsReportFile(String type, DegreeType degreeType,
	    ExecutionYear executionYear) {
	final DissertationsProposalsReportFile dissertationsProposalsReportFile = new DissertationsProposalsReportFile();
	dissertationsProposalsReportFile.setType(type);
	dissertationsProposalsReportFile.setDegreeType(degreeType);
	dissertationsProposalsReportFile.setExecutionYear(executionYear);
	return dissertationsProposalsReportFile;
    }

    @Service
    public static GepReportFile createDissertationsWithExternalAffiliationsReportFile(String type, DegreeType degreeType,
	    ExecutionYear executionYear) {
	final DissertationsWithExternalAffiliationsReportFile dissertationsWithExternalAffiliationsReportFile = new DissertationsWithExternalAffiliationsReportFile();
	dissertationsWithExternalAffiliationsReportFile.setType(type);
	dissertationsWithExternalAffiliationsReportFile.setDegreeType(degreeType);
	dissertationsWithExternalAffiliationsReportFile.setExecutionYear(executionYear);
	return dissertationsWithExternalAffiliationsReportFile;
    }

    @Service
    public static GepReportFile createTeachersListReportFile(String type, DegreeType degreeType, ExecutionYear executionYear) {
	final TeachersListReportFile teachersListReportFile = new TeachersListReportFile();
	teachersListReportFile.setType(type);
	teachersListReportFile.setDegreeType(degreeType);
	teachersListReportFile.setExecutionYear(executionYear);
	return teachersListReportFile;
    }

    @Service
    public static GepReportFile createTeachersListFromGiafReportFile(String type, DegreeType degreeType,
	    ExecutionYear executionYear) {
	final TeachersListFromGiafReportFile teachersListFromGiafReportFile = new TeachersListFromGiafReportFile();
	teachersListFromGiafReportFile.setType(type);
	teachersListFromGiafReportFile.setDegreeType(degreeType);
	teachersListFromGiafReportFile.setExecutionYear(executionYear);
	return teachersListFromGiafReportFile;
    }

    @Service
    public static GepReportFile createGraduationReportFile(String type, DegreeType degreeType, ExecutionYear executionYear) {
	final GraduationReportFile graduationReportFile = new GraduationReportFile();
	graduationReportFile.setType(type);
	graduationReportFile.setDegreeType(degreeType);
	graduationReportFile.setExecutionYear(executionYear);
	return graduationReportFile;
    }

    @Service
    public static GepReportFile createEctsLabelCurricularCourseReportFile(String type, DegreeType degreeType,
	    ExecutionYear executionYear) {
	final EctsLabelCurricularCourseReportFile ectsLabelCurricularCourseReportFile = new EctsLabelCurricularCourseReportFile();
	ectsLabelCurricularCourseReportFile.setType(type);
	ectsLabelCurricularCourseReportFile.setDegreeType(degreeType);
	ectsLabelCurricularCourseReportFile.setExecutionYear(executionYear);
	return ectsLabelCurricularCourseReportFile;
    }

    @Service
    public static GepReportFile createCourseLoadReportFile(String type, DegreeType degreeType, ExecutionYear executionYear) {
	final CourseLoadReportFile courseLoadReportFile = new CourseLoadReportFile();
	courseLoadReportFile.setType(type);
	courseLoadReportFile.setDegreeType(degreeType);
	courseLoadReportFile.setExecutionYear(executionYear);
	return courseLoadReportFile;
    }

    @Service
    public static GepReportFile createEctsLabelDegreeReportFile(String type, DegreeType degreeType, ExecutionYear executionYear) {
	final EctsLabelDegreeReportFile ectsLabelDegreeReportFile = new EctsLabelDegreeReportFile();
	ectsLabelDegreeReportFile.setType(type);
	ectsLabelDegreeReportFile.setDegreeType(degreeType);
	ectsLabelDegreeReportFile.setExecutionYear(executionYear);
	return ectsLabelDegreeReportFile;
    }

    @Service
    public static GepReportFile createEtiReportFile(String type, DegreeType degreeType, ExecutionYear executionYear) {
	final EtiReportFile etiReportFile = new EtiReportFile();
	etiReportFile.setType(type);
	etiReportFile.setDegreeType(degreeType);
	etiReportFile.setExecutionYear(executionYear);
	return etiReportFile;
    }

    @Service
    public static GepReportFile createCourseLoadAndResponsiblesReportFile(String type, DegreeType degreeType,
	    ExecutionYear executionYear) {
	final CourseLoadAndResponsiblesReportFile courseLoadAndResponsiblesReportFile = new CourseLoadAndResponsiblesReportFile();
	courseLoadAndResponsiblesReportFile.setType(type);
	courseLoadAndResponsiblesReportFile.setDegreeType(degreeType);
	courseLoadAndResponsiblesReportFile.setExecutionYear(executionYear);
	return courseLoadAndResponsiblesReportFile;
    }

    @Service
    public static GepReportFile createEurAceReportFile(String type, DegreeType degreeType, ExecutionYear executionYear) {
	final EurAceReportFile eurAceReportFile = new EurAceReportFile();
	eurAceReportFile.setType(type);
	eurAceReportFile.setDegreeType(degreeType);
	eurAceReportFile.setExecutionYear(executionYear);
	return eurAceReportFile;
    }

    @Service
    public static GepReportFile createFlunkedReportFile(String type, DegreeType degreeType, ExecutionYear executionYear) {
	final FlunkedReportFile flunkedReportFile = new FlunkedReportFile();
	flunkedReportFile.setType(type);
	flunkedReportFile.setDegreeType(degreeType);
	flunkedReportFile.setExecutionYear(executionYear);
	return flunkedReportFile;
    }

    @Service
    public static GepReportFile createRegistrationReportFile(String type, DegreeType degreeType, ExecutionYear executionYear) {
	final RegistrationReportFile registrationReportFile = new RegistrationReportFile();
	registrationReportFile.setType(type);
	registrationReportFile.setDegreeType(degreeType);
	registrationReportFile.setExecutionYear(executionYear);
	return registrationReportFile;
    }

    @Service
    public static GepReportFile createTeachersByShiftReportFile(String type, DegreeType degreeType, ExecutionYear executionYear) {
	final TeachersByShiftReportFile teachersByShiftReportFile = new TeachersByShiftReportFile();
	teachersByShiftReportFile.setType(type);
	teachersByShiftReportFile.setDegreeType(degreeType);
	teachersByShiftReportFile.setExecutionYear(executionYear);
	return teachersByShiftReportFile;
    }

    @Service
    public static GepReportFile createTutorshipProgramReportFile(String type, DegreeType degreeType, ExecutionYear executionYear) {
	final TutorshipProgramReportFile tutorshipProgramReportFile = new TutorshipProgramReportFile();
	tutorshipProgramReportFile.setType(type);
	tutorshipProgramReportFile.setDegreeType(degreeType);
	tutorshipProgramReportFile.setExecutionYear(executionYear);
	return tutorshipProgramReportFile;
    }

    @Service
    public static GepReportFile createRaidesGraduationReportFile(String type, DegreeType degreeType, ExecutionYear executionYear) {
	final RaidesGraduationReportFile reportFile = new RaidesGraduationReportFile();
	reportFile.setType(type);
	reportFile.setDegreeType(degreeType);
	reportFile.setExecutionYear(executionYear);
	return reportFile;
    }

    @Service
    public static GepReportFile createRaidesDfaReportFile(String type, DegreeType degreeType, ExecutionYear executionYear) {
	final RaidesDfaReportFile reportFile = new RaidesDfaReportFile();
	reportFile.setType(type);
	reportFile.setDegreeType(degreeType);
	reportFile.setExecutionYear(executionYear);
	return reportFile;
    }

    @Service
    public static GepReportFile createRaidesPhdReportFile(String type, DegreeType degreeType, ExecutionYear executionYear) {
	final RaidesPhdReportFile reportFile = new RaidesPhdReportFile();
	reportFile.setType(type);
	reportFile.setDegreeType(degreeType);
	reportFile.setExecutionYear(executionYear);
	return reportFile;
    }
}
